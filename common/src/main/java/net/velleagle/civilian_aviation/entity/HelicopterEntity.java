package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.cobalt.network.NetworkHandler;
import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.Rotorcraft;
import immersive_aircraft.entity.misc.TrailDescriptor;
import immersive_aircraft.item.upgrade.VehicleStat;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.velleagle.civilian_aviation.item.AircraftVariant;
import net.velleagle.civilian_aviation.item.PaintSprayItem;
import net.velleagle.civilian_aviation.network.c2s.DoorMessage;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * HelicopterEntity — ヘリコプター挙動の基底クラス。
 *
 * ドア4本（door_l / door_r / sidedoor_l / sidedoor_r）の
 * 状態管理・AABB当たり判定・BBアニメーション変数・NBT保存をすべて担う。
 * サブクラスで hasDoors() を true にし、各AABB値をオーバーライドして使う。
 */
public abstract class HelicopterEntity extends Rotorcraft {

    // -------------------------------------------------------
    // ドア設定（サブクラスでオーバーライド）
    // -------------------------------------------------------
    public boolean hasDoors() { return false; }
    protected double getDoorReach() { return 3.0; }

    /** 乗車した瞬間に閉めるドアサイド（null = 乗車時は閉めない） */
    public DoorMessage.Side getOnMountCloseSide() { return null; }

    // ---- IAデフォルトエンジン音を完全無効化 ----------------
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

    // -------------------------------------------------------
    // ドアAABB定義（サブクラスでオーバーライド）
    //
    // 「クローズAABB」: ドアが閉じているときのインタラクション領域（クリックで開く）
    // 「オープンAABB」: ドアが開いているときのインタラクション領域（クリックで閉める）
    //   オープンAABBのデフォルト値はすべて 0f（オーバーライドしなければ開放時クリック不可）
    // -------------------------------------------------------

    // door_l クローズAABB
    protected float getDoorLCX() { return 0f; }
    protected float getDoorLCY() { return 0f; }
    protected float getDoorLCZ() { return 0f; }
    protected float getDoorLHW() { return 0f; }
    protected float getDoorLHH() { return 0f; }
    protected float getDoorLHD() { return 0f; }

    // door_l オープンAABB
    protected float getDoorLOpenCX() { return 0f; }
    protected float getDoorLOpenCY() { return 0f; }
    protected float getDoorLOpenCZ() { return 0f; }
    protected float getDoorLOpenHW() { return 0f; }
    protected float getDoorLOpenHH() { return 0f; }
    protected float getDoorLOpenHD() { return 0f; }

    // door_r クローズAABB
    protected float getDoorRCX() { return 0f; }
    protected float getDoorRCY() { return 0f; }
    protected float getDoorRCZ() { return 0f; }
    protected float getDoorRHW() { return 0f; }
    protected float getDoorRHH() { return 0f; }
    protected float getDoorRHD() { return 0f; }

    // door_r オープンAABB
    protected float getDoorROpenCX() { return 0f; }
    protected float getDoorROpenCY() { return 0f; }
    protected float getDoorROpenCZ() { return 0f; }
    protected float getDoorROpenHW() { return 0f; }
    protected float getDoorROpenHH() { return 0f; }
    protected float getDoorROpenHD() { return 0f; }

    // sidedoor_l クローズAABB
    protected float getSideDoorLCX() { return 0f; }
    protected float getSideDoorLCY() { return 0f; }
    protected float getSideDoorLCZ() { return 0f; }
    protected float getSideDoorLHW() { return 0f; }
    protected float getSideDoorLHH() { return 0f; }
    protected float getSideDoorLHD() { return 0f; }

    // sidedoor_l オープンAABB
    protected float getSideDoorLOpenCX() { return 0f; }
    protected float getSideDoorLOpenCY() { return 0f; }
    protected float getSideDoorLOpenCZ() { return 0f; }
    protected float getSideDoorLOpenHW() { return 0f; }
    protected float getSideDoorLOpenHH() { return 0f; }
    protected float getSideDoorLOpenHD() { return 0f; }

    // sidedoor_r クローズAABB
    protected float getSideDoorRCX() { return 0f; }
    protected float getSideDoorRCY() { return 0f; }
    protected float getSideDoorRCZ() { return 0f; }
    protected float getSideDoorRHW() { return 0f; }
    protected float getSideDoorRHH() { return 0f; }
    protected float getSideDoorRHD() { return 0f; }

    // sidedoor_r オープンAABB
    protected float getSideDoorROpenCX() { return 0f; }
    protected float getSideDoorROpenCY() { return 0f; }
    protected float getSideDoorROpenCZ() { return 0f; }
    protected float getSideDoorROpenHW() { return 0f; }
    protected float getSideDoorROpenHH() { return 0f; }
    protected float getSideDoorROpenHD() { return 0f; }

    // -------------------------------------------------------
    // 同期データ
    // -------------------------------------------------------
    private static final EntityDataAccessor<Boolean> DOOR_L_OPEN =
            SynchedEntityData.defineId(HelicopterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DOOR_R_OPEN =
            SynchedEntityData.defineId(HelicopterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SIDEDOOR_L_OPEN =
            SynchedEntityData.defineId(HelicopterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SIDEDOOR_R_OPEN =
            SynchedEntityData.defineId(HelicopterEntity.class, EntityDataSerializers.BOOLEAN);

    // ---- アニメーション補間値（クライアント専用）----------
    private float doorLProgress      = 0f, prevDoorLProgress      = 0f;
    private float doorRProgress      = 0f, prevDoorRProgress      = 0f;
    private float sidedoorLProgress  = 0f, prevSidedoorLProgress  = 0f;
    private float sidedoorRProgress  = 0f, prevSidedoorRProgress  = 0f;

    /** ドアアニメーション進捗値（0.0=全閉, 1.0=全開）を返す（クライアント専用） */
    public float getDoorLProgress()      { return doorLProgress; }
    public float getDoorRProgress()      { return doorRProgress; }
    public float getSidedoorLProgress()  { return sidedoorLProgress; }
    public float getSidedoorRProgress()  { return sidedoorRProgress; }
    public float getPrevDoorLProgress()      { return prevDoorLProgress; }
    public float getPrevDoorRProgress()      { return prevDoorRProgress; }
    public float getPrevSidedoorLProgress()  { return prevSidedoorLProgress; }
    public float getPrevSidedoorRProgress()  { return prevSidedoorRProgress; }

    private static final float DOOR_SPEED = 1.0f / 10.0f;

    // -------------------------------------------------------
    // 物理定数
    // -------------------------------------------------------
    private static final float HOVER_THRESHOLD      = 0.8f;
    private static final float PITCH_VELOCITY_SCALE = 15.0f;

    // -------------------------------------------------------

    public HelicopterEntity(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world, true);
    }

    // -------------------------------------------------------
    // SynchedEntityData
    // -------------------------------------------------------
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DOOR_L_OPEN,     false);
        entityData.define(DOOR_R_OPEN,     false);
        entityData.define(SIDEDOOR_L_OPEN, false);
        entityData.define(SIDEDOOR_R_OPEN, false);
    }

    // -------------------------------------------------------
    // ドア状態アクセサ
    // -------------------------------------------------------
    public boolean isDoorOpen(DoorMessage.Side side) {
        return side == DoorMessage.Side.LEFT
                ? entityData.get(DOOR_L_OPEN)
                : entityData.get(DOOR_R_OPEN);
    }

    public boolean isSideDoorOpen(DoorMessage.Side side) {
        return side == DoorMessage.Side.LEFT
                ? entityData.get(SIDEDOOR_L_OPEN)
                : entityData.get(SIDEDOOR_R_OPEN);
    }

    public void toggleDoor(DoorMessage.Side side) {
        if (side == DoorMessage.Side.LEFT) {
            entityData.set(DOOR_L_OPEN, !entityData.get(DOOR_L_OPEN));
        } else {
            entityData.set(DOOR_R_OPEN, !entityData.get(DOOR_R_OPEN));
        }
    }

    public void toggleSideDoor(DoorMessage.Side side) {
        if (side == DoorMessage.Side.LEFT) {
            entityData.set(SIDEDOOR_L_OPEN, !entityData.get(SIDEDOOR_L_OPEN));
        } else {
            entityData.set(SIDEDOOR_R_OPEN, !entityData.get(SIDEDOOR_R_OPEN));
        }
    }

    public void closeDoor(DoorMessage.Side side) {
        if (side == DoorMessage.Side.LEFT) {
            entityData.set(DOOR_L_OPEN, false);
        } else {
            entityData.set(DOOR_R_OPEN, false);
        }
    }

    public void closeAllDoors() {
        entityData.set(DOOR_L_OPEN,     false);
        entityData.set(DOOR_R_OPEN,     false);
        entityData.set(SIDEDOOR_L_OPEN, false);
        entityData.set(SIDEDOOR_R_OPEN, false);
    }

    // -------------------------------------------------------
    // 乗車フック
    // -------------------------------------------------------
    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (!level().isClientSide && hasDoors()) {
            closeAllDoors();
        }
    }

    // -------------------------------------------------------
    // ドアAABB（ワールド座標）
    // -------------------------------------------------------
    private AABB buildDoorWorldAABB(float cx, float cy, float cz,
                                    float hw, float hh, float hd) {
        org.joml.Vector3f center = transformVectorQuantized(cx, cy, cz);
        return new AABB(
                center.x() - hw + getX(), center.y() - hh + getY(), center.z() - hd + getZ(),
                center.x() + hw + getX(), center.y() + hh + getY(), center.z() + hd + getZ());
    }

    // ── クローズAABB ──
    private AABB getDoorLCloseWorldAABB() {
        return buildDoorWorldAABB(getDoorLCX(), getDoorLCY(), getDoorLCZ(),
                getDoorLHW(), getDoorLHH(), getDoorLHD());
    }
    private AABB getDoorRCloseWorldAABB() {
        return buildDoorWorldAABB(getDoorRCX(), getDoorRCY(), getDoorRCZ(),
                getDoorRHW(), getDoorRHH(), getDoorRHD());
    }
    private AABB getSideDoorLCloseWorldAABB() {
        return buildDoorWorldAABB(getSideDoorLCX(), getSideDoorLCY(), getSideDoorLCZ(),
                getSideDoorLHW(), getSideDoorLHH(), getSideDoorLHD());
    }
    private AABB getSideDoorRCloseWorldAABB() {
        return buildDoorWorldAABB(getSideDoorRCX(), getSideDoorRCY(), getSideDoorRCZ(),
                getSideDoorRHW(), getSideDoorRHH(), getSideDoorRHD());
    }

    // ── オープンAABB ──
    private AABB getDoorLOpenWorldAABB() {
        return buildDoorWorldAABB(getDoorLOpenCX(), getDoorLOpenCY(), getDoorLOpenCZ(),
                getDoorLOpenHW(), getDoorLOpenHH(), getDoorLOpenHD());
    }
    private AABB getDoorROpenWorldAABB() {
        return buildDoorWorldAABB(getDoorROpenCX(), getDoorROpenCY(), getDoorROpenCZ(),
                getDoorROpenHW(), getDoorROpenHH(), getDoorROpenHD());
    }
    private AABB getSideDoorLOpenWorldAABB() {
        return buildDoorWorldAABB(getSideDoorLOpenCX(), getSideDoorLOpenCY(), getSideDoorLOpenCZ(),
                getSideDoorLOpenHW(), getSideDoorLOpenHH(), getSideDoorLOpenHD());
    }
    private AABB getSideDoorROpenWorldAABB() {
        return buildDoorWorldAABB(getSideDoorROpenCX(), getSideDoorROpenCY(), getSideDoorROpenCZ(),
                getSideDoorROpenHW(), getSideDoorROpenHH(), getSideDoorROpenHD());
    }

    // ── アクティブAABB（状態によりクローズ/オープンを切り替え）──
    private AABB getActiveDoorLWorldAABB() {
        return isDoorOpen(DoorMessage.Side.LEFT) ? getDoorLOpenWorldAABB() : getDoorLCloseWorldAABB();
    }
    private AABB getActiveDoorRWorldAABB() {
        return isDoorOpen(DoorMessage.Side.RIGHT) ? getDoorROpenWorldAABB() : getDoorRCloseWorldAABB();
    }
    private AABB getActiveSideDoorLWorldAABB() {
        return isSideDoorOpen(DoorMessage.Side.LEFT) ? getSideDoorLOpenWorldAABB() : getSideDoorLCloseWorldAABB();
    }
    private AABB getActiveSideDoorRWorldAABB() {
        return isSideDoorOpen(DoorMessage.Side.RIGHT) ? getSideDoorROpenWorldAABB() : getSideDoorRCloseWorldAABB();
    }

    @Override
    public List<AABB> getAdditionalShapes() {
        List<AABB> shapes = new ArrayList<>(super.getAdditionalShapes());
        if (hasDoors()) {
            shapes.add(getActiveDoorLWorldAABB());
            shapes.add(getActiveDoorRWorldAABB());
            shapes.add(getActiveSideDoorLWorldAABB());
            shapes.add(getActiveSideDoorRWorldAABB());
        }
        return shapes;
    }

    // -------------------------------------------------------
    // バリアントID
    // -------------------------------------------------------

    /**
     * このエンティティが属するバリアントの entityId を返す。
     * 例: "bell206_blackstripe", "bell47g_blue"
     */
    public String getVariantEntityId() {
        return BuiltInRegistries.ENTITY_TYPE.getKey(getType()).getPath();
    }

    // -------------------------------------------------------
    // インタラクション（全4ドアの当たり判定）
    // -------------------------------------------------------
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {

        // ── ペイントスプレー判定 ──────────────────────────────
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

        if (hasDoors()) {
            Vec3 eyePos = player.getEyePosition();
            Vec3 endPos = eyePos.add(player.getLookAngle().scale(getDoorReach()));

            // 状態に応じたアクティブAABBでレイキャスト:
            //   閉じているとき → クローズAABBにヒットで「開く」
            //   開いているとき → オープンAABBにヒットで「閉める」
            record DoorHit(DoorMessage.Side side, double distSq) {}

            List<DoorHit> hits = new ArrayList<>();
            getActiveDoorLWorldAABB()     .clip(eyePos, endPos).ifPresent(h -> hits.add(new DoorHit(DoorMessage.Side.LEFT,       h.distanceToSqr(eyePos))));
            getActiveDoorRWorldAABB()     .clip(eyePos, endPos).ifPresent(h -> hits.add(new DoorHit(DoorMessage.Side.RIGHT,      h.distanceToSqr(eyePos))));
            getActiveSideDoorLWorldAABB() .clip(eyePos, endPos).ifPresent(h -> hits.add(new DoorHit(DoorMessage.Side.SIDE_LEFT,  h.distanceToSqr(eyePos))));
            getActiveSideDoorRWorldAABB() .clip(eyePos, endPos).ifPresent(h -> hits.add(new DoorHit(DoorMessage.Side.SIDE_RIGHT, h.distanceToSqr(eyePos))));

            if (!hits.isEmpty()) {
                DoorMessage.Side nearest = hits.stream()
                        .min(java.util.Comparator.comparingDouble(DoorHit::distSq))
                        .get().side();

                if (level().isClientSide) {
                    NetworkHandler.sendToServer(new DoorMessage(getId(), nearest));
                }
                return InteractionResult.CONSUME;
            }
        }
        return super.interact(player, hand);
    }

    // -------------------------------------------------------
    // tick
    // -------------------------------------------------------
    @Override
    public void tick() {
        super.tick();

        if (hasDoors() && level().isClientSide) {
            prevDoorLProgress     = doorLProgress;
            prevDoorRProgress     = doorRProgress;
            prevSidedoorLProgress = sidedoorLProgress;
            prevSidedoorRProgress = sidedoorRProgress;

            doorLProgress     = approach(doorLProgress,     isDoorOpen(DoorMessage.Side.LEFT)      ? 1f : 0f);
            doorRProgress     = approach(doorRProgress,     isDoorOpen(DoorMessage.Side.RIGHT)     ? 1f : 0f);
            sidedoorLProgress = approach(sidedoorLProgress, isSideDoorOpen(DoorMessage.Side.LEFT)  ? 1f : 0f);
            sidedoorRProgress = approach(sidedoorRProgress, isSideDoorOpen(DoorMessage.Side.RIGHT) ? 1f : 0f);
        }
    }

    private static float approach(float value, float target) {
        if (value < target) return Math.min(value + DOOR_SPEED, target);
        if (value > target) return Math.max(value - DOOR_SPEED, target);
        return value;
    }

    // -------------------------------------------------------
    // 重力・コントローラー・convertPower（変更なし）
    // -------------------------------------------------------
    @Override
    protected float getGravity() {
        return wasTouchingWater
                ? 0.04f
                : (1.0f - getEnginePower()) * super.getGravity();
    }

    @Override
    protected void updateController() {
        // エンジン出力: 搭乗中は自動的に全開（Quadrocopterと同様）
        if (canTurnOnEngine(getControllingPassenger())) {
            setEngineTarget(1.0f);
        }

        setYRot(getYRot() - getProperties().get(VehicleStat.YAW_SPEED)
                * movementX);

        // 垂直推力: スペース（Y軸+）で上昇、シフト（Y軸-）で降下
        float vertPower = getEnginePower()
                * getProperties().get(VehicleStat.VERTICAL_SPEED)
                * pressingInterpolatedY.getSmooth();
        Vector3f up = getTopDirection().mul(vertPower);
        setDeltaMovement(getDeltaMovement().add(up.x, up.y, up.z));

        // 前後推力: W（Z軸+）で前進、S（Z軸-）で後進
        if (getEnginePower() >= HOVER_THRESHOLD) {
            float thrust = (float) (Math.pow(getEnginePower(), 2.0)
                    * getProperties().get(VehicleStat.ENGINE_SPEED)
                    * pressingInterpolatedZ.getSmooth());
            Vector3f forward = getForwardDirection().mul(thrust);
            setDeltaMovement(getDeltaMovement().add(forward.x, forward.y, forward.z));
        }

        if (!onGround()) {
            Vec3 velocity = getDeltaMovement();
            Vector3f fwd = getForwardDirection();
            float forwardSpeed = (float) (velocity.x * fwd.x() + velocity.z * fwd.z());
            float targetPitch = forwardSpeed
                    * getProperties().get(VehicleStat.PITCH_SPEED)
                    * PITCH_VELOCITY_SCALE;
            setXRot(getXRot() + (targetPitch - getXRot())
                    * getProperties().getAdditive(VehicleStat.STABILIZER));
        } else {
            setXRot(getXRot() * 0.8f);
        }
    }

    @Override
    protected void convertPower(Vec3 direction) {
        Vec3 velocity = getDeltaMovement().multiply(1.0, 0.0, 1.0);
        if (velocity.lengthSqr() < 1e-10) return;
        double drag = Math.abs(direction.dot(velocity.normalize()));
        Vec3 newVelocity = velocity.normalize()
                .lerp(direction, getProperties().get(VehicleStat.LIFT))
                .scale(velocity.length() * (drag * getProperties().get(VehicleStat.FRICTION)
                        + (1.0 - getProperties().get(VehicleStat.FRICTION))));
        setDeltaMovement(newVelocity.x, getDeltaMovement().y, newVelocity.z);
    }

    // -------------------------------------------------------
    // BBアニメーション変数
    // -------------------------------------------------------
    @Override
    public void setAnimationVariables(float tickDelta) {
        super.setAnimationVariables(tickDelta);

        BBAnimationVariables.set("throttle", getEnginePower());
        BBAnimationVariables.set("lever_stick",   pressingInterpolatedX.getSmooth(tickDelta));
        // lever_stick_z はW/S（Z軸）の前後入力を反映
        BBAnimationVariables.set("lever_stick_z", Math.max(-0.5f, Math.min(1.0f,
                pressingInterpolatedZ.getSmooth(tickDelta))));

        if (hasDoors()) {
            BBAnimationVariables.set("door_l",     prevDoorLProgress     + (doorLProgress     - prevDoorLProgress)     * tickDelta);
            BBAnimationVariables.set("door_r",     prevDoorRProgress     + (doorRProgress     - prevDoorRProgress)     * tickDelta);
            BBAnimationVariables.set("sidedoor_l", prevSidedoorLProgress + (sidedoorLProgress - prevSidedoorLProgress) * tickDelta);
            BBAnimationVariables.set("sidedoor_r", prevSidedoorRProgress + (sidedoorRProgress - prevSidedoorRProgress) * tickDelta);
        }
    }

    // -------------------------------------------------------
    // トレイル幅
    // -------------------------------------------------------
    @Override
    public float getBaseTrailWidth(Matrix4f transform, int index, TrailDescriptor trail) {
        return Math.max(0.0f, Math.min(1.0f,
                (float) (getDeltaMovement().length() - 0.05f)));
    }

    // -------------------------------------------------------
    // NBT
    // -------------------------------------------------------
    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (hasDoors()) {
            tag.putBoolean("DoorLOpen",     entityData.get(DOOR_L_OPEN));
            tag.putBoolean("DoorROpen",     entityData.get(DOOR_R_OPEN));
            tag.putBoolean("SideDoorLOpen", entityData.get(SIDEDOOR_L_OPEN));
            tag.putBoolean("SideDoorROpen", entityData.get(SIDEDOOR_R_OPEN));
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (hasDoors()) {
            if (tag.contains("DoorLOpen")) {
                entityData.set(DOOR_L_OPEN, tag.getBoolean("DoorLOpen"));
                doorLProgress = prevDoorLProgress = isDoorOpen(DoorMessage.Side.LEFT) ? 1f : 0f;
            }
            if (tag.contains("DoorROpen")) {
                entityData.set(DOOR_R_OPEN, tag.getBoolean("DoorROpen"));
                doorRProgress = prevDoorRProgress = isDoorOpen(DoorMessage.Side.RIGHT) ? 1f : 0f;
            }
            if (tag.contains("SideDoorLOpen")) {
                entityData.set(SIDEDOOR_L_OPEN, tag.getBoolean("SideDoorLOpen"));
                sidedoorLProgress = prevSidedoorLProgress = isSideDoorOpen(DoorMessage.Side.LEFT) ? 1f : 0f;
            }
            if (tag.contains("SideDoorROpen")) {
                entityData.set(SIDEDOOR_R_OPEN, tag.getBoolean("SideDoorROpen"));
                sidedoorRProgress = prevSidedoorRProgress = isSideDoorOpen(DoorMessage.Side.RIGHT) ? 1f : 0f;
            }
        }
    }
}