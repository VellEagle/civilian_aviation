package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.misc.Trail;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

/**
 * Comanche Blue エンティティ。
 *
 * ランディングギアの2段階シーケンスアニメーションを持つ:
 *
 *   格納（deployed -> retracted）
 *     1. landing_gear を 1.0 -> 0.0 に動かす（ギアが折り畳まれる）
 *     2. landing_gear が完了後、gear_hatch を 0.0 -> 1.0 に動かす（ハッチが閉じる）
 *
 *   展開（retracted -> deployed）
 *     1. landing_gear を 0.0 -> 1.0 に動かす（ギアが伸びる）
 *     2. landing_gear が完了後、gear_hatch を 1.0 -> 0.0 に動かす（ハッチが開放）
 *
 * デフォルト状態: 展開済み（gearProgress=1.0, hatchProgress=0.0）
 */
public class E500Red extends CivilianAircraftEntity {

    // ---- アニメーション速度 --------------------------------
    /** 1 tick あたりの進行量（10 tick で 0.0 -> 1.0 完了） */
    private static final float ANIM_SPEED = 1.0f / 10.0f;

    // ---- 同期データ ----------------------------------------
    /** true = ランディングギア展開 / false = 格納 */
    private static final EntityDataAccessor<Boolean> GEAR_DEPLOYED =
            SynchedEntityData.defineId(E500Red.class, EntityDataSerializers.BOOLEAN);

    // ---- アニメーション補間値（クライアント専用）----------
    private float gearProgress      = 0.0f;   // 初期: 展開（反転後は0.0が展開）
    private float prevGearProgress  = 0.0f;
    private float hatchProgress     = 0.0f;   // 初期: ハッチ閉
    private float prevHatchProgress = 0.0f;

    // ---- ドアAABB座標 --------------------------------------
    @Override public boolean hasDoors()           { return true; }
    @Override public boolean hasLandingGear()     { return true; }
    @Override protected double getDoorReach()      { return 3.0; }
    @Override protected float getDoorLCX()         { return 0.899f; }
    @Override protected float getDoorLCY()         { return  1.275f;  }
    @Override protected float getDoorLCZ()         { return  0.954f; }
    @Override protected float getDoorLHW()         { return  0.2f; }
    @Override protected float getDoorLHH()         { return  0.2f; }
    @Override protected float getDoorLHD()         { return  0.2f; }

    @Override protected float getDoorLOpenCX()         { return 1.209f; }
    @Override protected float getDoorLOpenCY()         { return  2.025f;  }
    @Override protected float getDoorLOpenCZ()         { return  0.919f; }
    @Override protected float getDoorLOpenHW()         { return  0.23f; }
    @Override protected float getDoorLOpenHH()         { return  0.23f; }
    @Override protected float getDoorLOpenHD()         { return  0.23f; }

    // -------------------------------------------------------


    @Override
    public net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side getOnMountCloseSide() {
        return net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.RIGHT;
    }

    public E500Red(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    // ---- SynchedEntityData --------------------------------

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(GEAR_DEPLOYED, true);
    }

    // ---- ランディングギア状態 ------------------------------

    public boolean isGearDeployed() {
        return entityData.get(GEAR_DEPLOYED);
    }

    /** サーバー側で展開 / 格納をトグル */
    public void toggleLandingGear() {
        entityData.set(GEAR_DEPLOYED, !entityData.get(GEAR_DEPLOYED));
    }

    // ---- tick ---------------------------------------------

    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) return;

        prevGearProgress  = gearProgress;
        prevHatchProgress = hatchProgress;

        if (isGearDeployed()) {
            // 展開シーケンス: gear_hatch(1->0) が完了してから landing_gear(1->0)
            if (hatchProgress > 0.0f) {
                hatchProgress = approach(hatchProgress, 0.0f);
            } else {
                gearProgress = approach(gearProgress, 0.0f);
            }
        } else {
            // 格納シーケンス: landing_gear(0->1) が完了してから gear_hatch(0->1)
            if (gearProgress < 1.0f) {
                gearProgress = approach(gearProgress, 1.0f);
            } else {
                hatchProgress = approach(hatchProgress, 1.0f);
            }
        }
    }

    private static float approach(float value, float target) {
        if (value < target) return Math.min(value + ANIM_SPEED, target);
        if (value > target) return Math.max(value - ANIM_SPEED, target);
        return value;
    }

    // ---- アニメーション変数 ---------------------------------

    @Override
    public void setAnimationVariables(float tickDelta) {
        super.setAnimationVariables(tickDelta);

        float interpGear  = prevGearProgress  + (gearProgress  - prevGearProgress)  * tickDelta;
        float interpHatch = prevHatchProgress + (hatchProgress - prevHatchProgress) * tickDelta;

        BBAnimationVariables.set("landing_gear", interpGear);
        BBAnimationVariables.set("gear_hatch",   interpHatch);

        // スロットル: エンジンパワーを 0.0〜1.0 でセット
        BBAnimationVariables.set("throttle", getEnginePower());

        // lever_stick (左右ロール): pressingInterpolatedX を -1.0〜1.0 でセット
        float stickX = pressingInterpolatedZ.getSmooth(tickDelta);
        BBAnimationVariables.set("lever_stick", stickX);

        // lever_stick_z (前後ピッチ): 前方(+)は 1.0 まで、後方(-)は -0.5 までにクランプ
        float stickZ = pressingInterpolatedX.getSmooth(tickDelta);
        float clampedZ = Math.max(-0.5f, Math.min(1.0f, stickZ));
        BBAnimationVariables.set("lever_stick_z", clampedZ);
    }

    // ---- その他 --------------------------------------------

    @Override
    protected float[][] getTrailPositions() {
        // 左エンジンナセル(index=0), 右エンジンナセル(index=1)
        return new float[][] {
            { -6.041f,  0.52f,  -0.509f,  0.15f },  // 左エンジン
            {  6.041f,  0.52f,  -0.509f,  0.15f },  // 右エンジン
        };
    }


    @Override
    protected List<Trail> createTrails() {
        // E500Red: 左右エンジンナセル後方コントレイル
        return List.of(new Trail(40, 1.0f), new Trail(40, 1.0f));
    }


    @Override
    public Item asItem() {
        return CivilianAviation.E500_RED_ITEM.get();
    }

    @Override
    public double getZoom() {
        return 7.0;
    }

    // ---- NBT 保存 / 読み込み --------------------------------

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("GearDeployed", isGearDeployed());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("GearDeployed")) {
            boolean deployed = tag.getBoolean("GearDeployed");
            entityData.set(GEAR_DEPLOYED, deployed);
            if (deployed) {
                // 展開済み: gear=0.0（反転後の展開）, hatch=0.0（閉）
                gearProgress  = prevGearProgress  = 0.0f;
                hatchProgress = prevHatchProgress = 0.0f;
            } else {
                // 格納済み: gear=1.0（反転後の格納）, hatch=1.0（開いたまま）
                gearProgress  = prevGearProgress  = 1.0f;
                hatchProgress = prevHatchProgress = 1.0f;
            }
        }
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 120.0f;
    }
}
