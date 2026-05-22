package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.cobalt.network.NetworkHandler;
import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.AirplaneEntity;
import immersive_aircraft.entity.misc.Trail;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.velleagle.civilian_aviation.client.CivilianAviationKeyBindings;
import net.velleagle.civilian_aviation.item.AircraftVariant;
import net.velleagle.civilian_aviation.item.PaintSprayItem;
import net.velleagle.civilian_aviation.network.c2s.DoorMessage;
import net.velleagle.civilian_aviation.network.c2s.LandingGearMessage;
import org.joml.Matrix4f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Civilian Aviation の全機体共通の基底クラス。
 *
 * 提供する機能:
 *   - 左右ドアの開閉（SynchedEntityData による同期、アニメーション補間）
 *   - スティックアニメーション（variable_stick: 左右入力の補間値）
 *   - ペイントスプレーによる色バリアント変更
 */
public abstract class CivilianAircraftEntity extends AirplaneEntity {

    // ---- ドアAABBのデフォルト値（サブクラスでオーバーライド）----
    //
    // 「クローズAABB」: ドアが閉じているときに表示されるインタラクション領域。
    //   ここをクリックするとドアが開く。
    //   ドアが開いている間は非表示（インタラクション無効）になる。
    //
    // 「オープンAABB」: ドアが開いているときに表示されるインタラクション領域。
    //   ここをクリックするとドアが閉まる。
    //   ドアが閉じている間は非表示（インタラクション無効）になる。
    //   デフォルト値はすべて 0f（= オーバーライドしなければオープン時クリック不可）。

    // ── 左ドア クローズAABB ──
    protected float getDoorLCX() { return 0f; }
    protected float getDoorLCY() { return 0f; }
    protected float getDoorLCZ() { return 0f; }
    protected float getDoorLHW() { return 0f; }
    protected float getDoorLHH() { return 0f; }
    protected float getDoorLHD() { return 0f; }

    // ── 左ドア オープンAABB ──
    protected float getDoorLOpenCX() { return 0f; }
    protected float getDoorLOpenCY() { return 0f; }
    protected float getDoorLOpenCZ() { return 0f; }
    protected float getDoorLOpenHW() { return 0f; }
    protected float getDoorLOpenHH() { return 0f; }
    protected float getDoorLOpenHD() { return 0f; }

    // ── 右ドア クローズAABB ──
    protected float getDoorRCX() { return 0f; }
    protected float getDoorRCY() { return 0f; }
    protected float getDoorRCZ() { return 0f; }
    protected float getDoorRHW() { return 0f; }
    protected float getDoorRHH() { return 0f; }
    protected float getDoorRHD() { return 0f; }

    // ── 右ドア オープンAABB ──
    protected float getDoorROpenCX() { return 0f; }
    protected float getDoorROpenCY() { return 0f; }
    protected float getDoorROpenCZ() { return 0f; }
    protected float getDoorROpenHW() { return 0f; }
    protected float getDoorROpenHH() { return 0f; }
    protected float getDoorROpenHD() { return 0f; }

    public boolean hasDoors() { return false; }
    protected double getDoorReach() { return 3.0; }

    /** ランディングギアを持つ機体かどうか（ComancheRed/Pzl37Los でオーバーライド） */
    public boolean hasLandingGear() { return false; }

    /** ランディングギアの展開/格納をトグル（hasLandingGear()=true の機体でオーバーライド） */
    public void toggleLandingGear() { }

    // ---- 航跡（コントレイル）------------------------------
    //
    // IAの AircraftEntity はトレイル位置を JSON の TrailDescriptor から読むが、
    // CA には data JSON がないため、Java で直接管理する。
    //
    // 実装方法:
    //   1. createTrails()     … Trail インスタンスのリストを返す（長さ・輝度の定義）
    //   2. getTrailPositions() … 翼端などの位置を {x, y, z, halfWidth} の配列で返す
    //                            各要素が createTrails() の Trail と 1:1 で対応
    //   3. tick() 内の recordCivilianTrails() でまとめて位置を記録
    //
    // Trail(length, gray):
    //   length … 残像の長さ（ticks）
    //   gray   … 白(1.0f)〜黒(0.0f)の輝度
    //
    // getTrailPositions() は float[][] を返す。
    // 各行: { x, y, z, halfWidth }
    //   x, y, z      … 機体ローカル座標での翼端位置
    //   halfWidth     … トレイルの半幅（m）
    // -------------------------------------------------------

    /** このエンティティが使用する Trail インスタンスのリストを返す。 */
    private List<Trail> trailCache = null;

    @Override
    public List<Trail> getTrails() {
        if (trailCache == null) {
            trailCache = createTrails();
        }
        return trailCache;
    }

    /**
     * 機体固有の Trail リストを生成して返す。
     * サブクラスでオーバーライドして各機体の翼端本数に合わせること。
     * デフォルトは空リスト（航跡なし）。
     */
    protected List<Trail> createTrails() {
        return List.of();
    }

    /**
     * 各 Trail の記録位置を返す。
     * 戻り値の配列行数は createTrails() の要素数と一致させること。
     * 各行: { x, y, z, halfWidth }
     *   x, y, z  … 機体ローカル座標（右翼端は x>0、左翼端は x<0）
     *   halfWidth … トレイルの半幅（m）
     * デフォルト実装は空配列（recordCivilianTrails が何もしない）。
     */
    protected float[][] getTrailPositions() {
        return new float[0][];
    }

    /**
     * tick() から呼ばれ、getTrailPositions() に基づいて各 Trail に現在位置を記録する。
     * ヘリコプター（Bell47g / Bell206Blackstripe）はこのメソッドを呼ばない。
     */
    /**
     * 航跡の濃さ係数。値を大きくするほど濃く・早く出る。
     *   0.5f … 標準（高速飛行時にうっすら出る）
     *   1.0f … 濃いめ
     *   2.0f … かなり濃い（低速でも出る）
     */
    private static final float TRAIL_STRENGTH = 0.8f;

    private void recordCivilianTrails() {
        List<Trail> trails = getTrails();
        if (trails.isEmpty()) return;

        Matrix4f transform = getVehicleTransform();
        float[][] positions = getTrailPositions();
        float speed = (float) Math.sqrt(getDeltaMovement().length());
        // バンク角（左右旋回）による翼端の強度変化
        float bankFactor = pressingInterpolatedX.getSmooth();

        for (int i = 0; i < Math.min(trails.size(), positions.length); i++) {
            float[] pos = positions[i];
            float x = pos[0];
            float y = pos[1];
            float z = pos[2];
            float hw = pos[3];

            Vector4f p0 = transformPosition(transform, x, y - hw, z);
            Vector4f p1 = transformPosition(transform, x, y + hw, z);

            // 翼端のトレイル強度: 速度が上がるほど濃く、銀行角で左右非対称に
            float strength = Math.max(0.0f, Math.min(1.0f,
                    speed * (TRAIL_STRENGTH - bankFactor * x * 0.025f) - 0.25f));
            trails.get(i).add(p0, p1, strength);
        }
    }

    // ---- IAデフォルトエンジン音を完全無効化 ----------------
    // EngineVehicle.tick() の音量計算: Math.min(1.0f, getEngineVolume() + engineSpinUpStrength)
    // -1.0f だと engineSpinUpStrength 次第で正になってしまうため
    // NEGATIVE_INFINITY で完全に封じる。
    // getEngineStartSound() は EngineVehicleMixin でスキップする。
// ✅ 代わりにこれを追加（IA標準ループ音を無効化）
    @Override
    protected SoundEvent getEngineSound() {
        return SoundEvents.EMPTY;
    }

    // ✅ 起動音もEMPTYにして完全消音（カスタム起動音は各サブクラスで上書き）
    @Override
    protected SoundEvent getEngineStartSound() {
        return SoundEvents.EMPTY;
    }
    /**
     * 乗車した瞬間に閉めるドアのサイドを返す。
     * null を返すと乗車時の自動クローズなし（エンジン発進時にすべて閉まる）。
     *
     * Skyhawk / SkyhawkPr: 右ドア（機体から見て右 = RIGHTサイド）
     * Comanche           : 右ドア（ドアが1枚でRIGHTに設定）
     * その他              : null（デフォルト）
     */
    public DoorMessage.Side getOnMountCloseSide() { return null; }

    // ---- 同期データ ----------------------------------------
    private static final EntityDataAccessor<Boolean> DOOR_L_OPEN =
            SynchedEntityData.defineId(CivilianAircraftEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DOOR_R_OPEN =
            SynchedEntityData.defineId(CivilianAircraftEntity.class, EntityDataSerializers.BOOLEAN);

    // ---- アニメーション補間値（クライアント専用）----------
    private float doorLProgress     = 0.0f;
    private float prevDoorLProgress = 0.0f;
    private float doorRProgress     = 0.0f;
    private float prevDoorRProgress = 0.0f;

    /** ドアアニメーション進捗値（0.0=全閉, 1.0=全開）を返す（クライアント専用） */
    public float getDoorLProgress() { return doorLProgress; }
    public float getDoorRProgress() { return doorRProgress; }
    public float getPrevDoorLProgress() { return prevDoorLProgress; }
    public float getPrevDoorRProgress() { return prevDoorRProgress; }

    private static final float DOOR_SPEED = 1.0f / 10.0f;

    // -------------------------------------------------------

    public CivilianAircraftEntity(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world, true);
    }

    // ---- SynchedEntityData --------------------------------

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DOOR_L_OPEN, false);
        entityData.define(DOOR_R_OPEN, false);
    }

    // ---- バリアントID ------------------------------------

    /**
     * このエンティティが属するバリアントの entityId を返す。
     * BuiltInRegistries からエンティティタイプのキー(path部分)を取得する。
     * 例: "skyhawk_pr_red"
     */
    public String getVariantEntityId() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(getType()).getPath();
    }

    // ---- ドア状態アクセサ --------------------------------

    public boolean isDoorOpen(DoorMessage.Side side) {
        return side == DoorMessage.Side.LEFT
                ? entityData.get(DOOR_L_OPEN)
                : entityData.get(DOOR_R_OPEN);
    }

    public void toggleDoor(DoorMessage.Side side) {
        if (side == DoorMessage.Side.LEFT) {
            entityData.set(DOOR_L_OPEN, !entityData.get(DOOR_L_OPEN));
        } else {
            entityData.set(DOOR_R_OPEN, !entityData.get(DOOR_R_OPEN));
        }
    }

    /** 指定サイドのドアを閉める（既に閉まっていれば何もしない） */
    public void closeDoor(DoorMessage.Side side) {
        if (side == DoorMessage.Side.LEFT) {
            entityData.set(DOOR_L_OPEN, false);
        } else {
            entityData.set(DOOR_R_OPEN, false);
        }
    }

    /** 全ドアを閉める */
    public void closeAllDoors() {
        entityData.set(DOOR_L_OPEN, false);
        entityData.set(DOOR_R_OPEN, false);
    }

    // ---- 乗車フック: 乗車直後に指定ドアを閉める --------

    @Override
    protected void addPassenger(net.minecraft.world.entity.Entity passenger) {
        super.addPassenger(passenger);
        if (!level().isClientSide && hasDoors()) {
            DoorMessage.Side side = getOnMountCloseSide();
            if (side != null) {
                closeDoor(side);
            }
        }
    }

    // ---- ドアAABB（ワールド座標）--------------------------

    private AABB buildDoorWorldAABB(float cx, float cy, float cz,
                                    float hw, float hh, float hd) {
        // IAの getOffsetBoundingBox と同じ方式:
        // 機体の正規化回転行列でローカル中心座標を回転させた後、
        // 機体のワールド座標を加算してAABBの中心を決める。
        // これにより機体の向きに追従しつつAABBサイズは常に一定に保たれる。
        org.joml.Vector3f center = transformVectorQuantized(cx, cy, cz);
        return new AABB(
                center.x() - hw + getX(),
                center.y() - hh + getY(),
                center.z() - hd + getZ(),
                center.x() + hw + getX(),
                center.y() + hh + getY(),
                center.z() + hd + getZ());
    }

    /** 左ドアのクローズAABB（ドアが閉じているときのインタラクション領域） */
    private AABB getDoorLCloseWorldAABB() {
        return buildDoorWorldAABB(getDoorLCX(), getDoorLCY(), getDoorLCZ(),
                getDoorLHW(), getDoorLHH(), getDoorLHD());
    }

    /** 左ドアのオープンAABB（ドアが開いているときのインタラクション領域） */
    private AABB getDoorLOpenWorldAABB() {
        return buildDoorWorldAABB(getDoorLOpenCX(), getDoorLOpenCY(), getDoorLOpenCZ(),
                getDoorLOpenHW(), getDoorLOpenHH(), getDoorLOpenHD());
    }

    /** 右ドアのクローズAABB */
    private AABB getDoorRCloseWorldAABB() {
        return buildDoorWorldAABB(getDoorRCX(), getDoorRCY(), getDoorRCZ(),
                getDoorRHW(), getDoorRHH(), getDoorRHD());
    }

    /** 右ドアのオープンAABB */
    private AABB getDoorROpenWorldAABB() {
        return buildDoorWorldAABB(getDoorROpenCX(), getDoorROpenCY(), getDoorROpenCZ(),
                getDoorROpenHW(), getDoorROpenHH(), getDoorROpenHD());
    }

    /**
     * 現在のドア状態に応じてアクティブなAABBを返す。
     *   閉じているとき → クローズAABB
     *   開いているとき → オープンAABB
     */
    private AABB getActiveDoorLWorldAABB() {
        return isDoorOpen(DoorMessage.Side.LEFT)
                ? getDoorLOpenWorldAABB()
                : getDoorLCloseWorldAABB();
    }

    private AABB getActiveDoorRWorldAABB() {
        return isDoorOpen(DoorMessage.Side.RIGHT)
                ? getDoorROpenWorldAABB()
                : getDoorRCloseWorldAABB();
    }

    @Override
    public List<AABB> getAdditionalShapes() {
        List<AABB> shapes = new ArrayList<>(super.getAdditionalShapes());
        if (hasDoors()) {
            shapes.add(getActiveDoorLWorldAABB());
            shapes.add(getActiveDoorRWorldAABB());
        }
        return shapes;
    }

    // ---- インタラクション --------------------------------

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {

        // ── ペイントスプレー判定 ──────────────────────────────
        // クライアント・サーバー両側でアイテムチェックを行い、
        // ヒットしたら両側とも CONSUME を返して搭乗をブロックする。
        // GUI はクライアント側でのみ開く。
        if (player.isShiftKeyDown() && player.getItemInHand(hand).getItem() instanceof PaintSprayItem) {
            String entityId = getVariantEntityId();
            List<AircraftVariant> variants = AircraftVariant.findGroup(entityId);
            if (!variants.isEmpty()) {
                if (level().isClientSide) {
                    net.velleagle.civilian_aviation.client.gui.PaintSprayScreen
                            .open(this, variants);
                }
                return InteractionResult.CONSUME;
            }
        }

        // ── ドア判定 ─────────────────────────────────────────
        if (hasDoors()) {
            Vec3 eyePos = player.getEyePosition();
            Vec3 endPos = eyePos.add(player.getLookAngle().scale(getDoorReach()));

            // 状態に応じたアクティブAABBでレイキャスト:
            //   閉じているとき → クローズAABBにヒットで「開く」
            //   開いているとき → オープンAABBにヒットで「閉める」
            Optional<Vec3> hitL = getActiveDoorLWorldAABB().clip(eyePos, endPos);
            Optional<Vec3> hitR = getActiveDoorRWorldAABB().clip(eyePos, endPos);

            DoorMessage.Side hitSide = null;
            if (hitL.isPresent() && hitR.isPresent()) {
                double distL = hitL.get().distanceToSqr(eyePos);
                double distR = hitR.get().distanceToSqr(eyePos);
                hitSide = (distL <= distR) ? DoorMessage.Side.LEFT : DoorMessage.Side.RIGHT;
            } else if (hitL.isPresent()) {
                hitSide = DoorMessage.Side.LEFT;
            } else if (hitR.isPresent()) {
                hitSide = DoorMessage.Side.RIGHT;
            }

            if (hitSide != null) {
                if (level().isClientSide) {
                    NetworkHandler.sendToServer(new DoorMessage(getId(), hitSide));
                }
                return InteractionResult.CONSUME;
            }
        }

        return super.interact(player, hand);
    }

    // ---- tick -----------------------------------------------

    /** エンジン発進検知用: 前tickのエンジンパワー */
    private float prevEnginePower = 0f;

    @Override
    public void tick() {
        super.tick();

        // ── 航跡の記録（両サイド・毎tick）──────────────────────────────
        recordCivilianTrails();

        if (hasDoors() && level().isClientSide) {
            prevDoorLProgress = doorLProgress;
            prevDoorRProgress = doorRProgress;
            float targetL = isDoorOpen(DoorMessage.Side.LEFT)  ? 1.0f : 0.0f;
            float targetR = isDoorOpen(DoorMessage.Side.RIGHT) ? 1.0f : 0.0f;
            doorLProgress = approach(doorLProgress, targetL);
            doorRProgress = approach(doorRProgress, targetR);
        }

        // ── エンジン発進検知: 発進したら開いているドアをすべて閉める ──
        // サーバー側のみで処理（ドア状態はサーバーが管理）
        if (!level().isClientSide && hasDoors()) {
            float power = enginePower.getSmooth();
            // 前tickが閾値未満 → 今tickが閾値以上 = 発進したとみなす
            if (prevEnginePower < 0.05f && power >= 0.05f) {
                closeAllDoors();
            }
            prevEnginePower = power;
        }

        // ── ランディングギア キー入力チェック（クライアント側・自分が搭乗中のみ）──
        if (level().isClientSide && hasLandingGear()) {
            net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
            if (mc.player != null && mc.player.equals(getFirstPassenger())) {
                if (CivilianAviationKeyBindings.landingGear.consumeClick()) {
                    NetworkHandler.sendToServer(new LandingGearMessage(getId()));
                }
            }
        }
    }

    private static float approach(float value, float target) {
        if (value < target) return Math.min(value + DOOR_SPEED, target);
        if (value > target) return Math.max(value - DOOR_SPEED, target);
        return value;
    }

    // ---- アニメーション変数 ---------------------------------

    @Override
    public void setAnimationVariables(float tickDelta) {
        super.setAnimationVariables(tickDelta);

        if (hasDoors()) {
            float interpL = prevDoorLProgress + (doorLProgress - prevDoorLProgress) * tickDelta;
            float interpR = prevDoorRProgress + (doorRProgress - prevDoorRProgress) * tickDelta;
            BBAnimationVariables.set("door_l", interpL);
            BBAnimationVariables.set("door_r", interpR);
        }

        BBAnimationVariables.set("stick", pressingInterpolatedX.getSmooth(tickDelta));
    }

    // ---- NBT 保存 / 読み込み --------------------------------

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (hasDoors()) {
            tag.putBoolean("DoorLOpen", entityData.get(DOOR_L_OPEN));
            tag.putBoolean("DoorROpen", entityData.get(DOOR_R_OPEN));
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (hasDoors()) {
            if (tag.contains("DoorLOpen")) {
                entityData.set(DOOR_L_OPEN, tag.getBoolean("DoorLOpen"));
                doorLProgress = prevDoorLProgress = isDoorOpen(DoorMessage.Side.LEFT) ? 1.0f : 0.0f;
            }
            if (tag.contains("DoorROpen")) {
                entityData.set(DOOR_R_OPEN, tag.getBoolean("DoorROpen"));
                doorRProgress = prevDoorRProgress = isDoorOpen(DoorMessage.Side.RIGHT) ? 1.0f : 0.0f;
            }
        }
    }
}