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
 * PZL.37 Los エンティティ。
 *
 * ランディングギアの3段階シーケンスアニメーション:
 *
 *   格納（deployed → retracted）:
 *     1. landing_gear_a  0→1
 *     2. landing_gear    0→1
 *     3. gear_hatch      0→1
 *
 *   展開（retracted → deployed）:
 *     1. gear_hatch      1→0
 *     2. landing_gear    1→0
 *     3. landing_gear_a  1→0
 */
public class Pzl37Los extends CivilianAircraftEntity {

    private static final float ANIM_SPEED = 1.0f / 10.0f;

    // ---- 同期データ ----------------------------------------
    private static final EntityDataAccessor<Boolean> GEAR_DEPLOYED =
            SynchedEntityData.defineId(Pzl37Los.class, EntityDataSerializers.BOOLEAN);

    // ---- アニメーション補間値（クライアント専用）----------
    private float gearAProgress     = 0.0f;
    private float prevGearAProgress = 0.0f;
    private float gearProgress      = 0.0f;
    private float prevGearProgress  = 0.0f;
    private float hatchProgress     = 0.0f;
    private float prevHatchProgress = 0.0f;

    // ---- ドアAABB -------------------------------------------
    @Override public boolean hasDoors()               { return true; }
    @Override public boolean hasLandingGear()         { return true; }
    @Override protected float getDoorLCX()             { return  0.504f; }
    @Override protected float getDoorLCY()             { return  3.19f;   }
    @Override protected float getDoorLCZ()             { return  0.31f;  }
    @Override protected float getDoorLHW()             { return  0.25f; }
    @Override protected float getDoorLHH()             { return  0.25f; }
    @Override protected float getDoorLHD()             { return  0.25f; }

    @Override protected float getDoorLOpenCX()             { return  0.269f; }
    @Override protected float getDoorLOpenCY()             { return  3.82f;   }
    @Override protected float getDoorLOpenCZ()             { return  0.31f;  }
    @Override protected float getDoorLOpenHW()             { return  0.25f; }
    @Override protected float getDoorLOpenHH()             { return  0.25f; }
    @Override protected float getDoorLOpenHD()             { return  0.25f; }

    @Override
    public net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side getOnMountCloseSide() {
        return net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.RIGHT;
    }

    public Pzl37Los(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    // ---- SynchedEntityData ---------------------------------
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(GEAR_DEPLOYED, true);
    }

    // ---- ランディングギア ----------------------------------
    public boolean isGearDeployed() {
        return entityData.get(GEAR_DEPLOYED);
    }

    public void toggleLandingGear() {
        entityData.set(GEAR_DEPLOYED, !entityData.get(GEAR_DEPLOYED));
    }

    // ---- tick ----------------------------------------------
    @Override
    public void tick() {
        super.tick();

        if (!level().isClientSide) return;

        prevGearAProgress = gearAProgress;
        prevGearProgress  = gearProgress;
        prevHatchProgress = hatchProgress;

        if (isGearDeployed()) {
            // 展開シーケンス: hatch(1→0) → gear(1→0) → gearA(1→0)
            if (hatchProgress > 0.0f) {
                hatchProgress = approach(hatchProgress, 0.0f);
            } else if (gearProgress > 0.0f) {
                gearProgress = approach(gearProgress, 0.0f);
            } else {
                gearAProgress = approach(gearAProgress, 0.0f);
            }
        } else {
            // 格納シーケンス: gearA(0→1) → gear(0→1) → hatch(0→1)
            if (gearAProgress < 1.0f) {
                gearAProgress = approach(gearAProgress, 1.0f);
            } else if (gearProgress < 1.0f) {
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

    // ---- アニメーション変数 --------------------------------
    @Override
    public void setAnimationVariables(float tickDelta) {
        super.setAnimationVariables(tickDelta);

        float interpGearA = prevGearAProgress + (gearAProgress - prevGearAProgress) * tickDelta;
        float interpGear  = prevGearProgress  + (gearProgress  - prevGearProgress)  * tickDelta;
        float interpHatch = prevHatchProgress + (hatchProgress - prevHatchProgress) * tickDelta;

        BBAnimationVariables.set("landing_gear_a", interpGearA);
        BBAnimationVariables.set("landing_gear",   interpGear);
        BBAnimationVariables.set("gear_hatch",     interpHatch);
    }

    // ---- その他 --------------------------------------------
    @Override
    protected float[][] getTrailPositions() {
        // 左翼端(index=0), 右翼端(index=1)
        return new float[][] {
            { -7.671f,  1.789f,  -1.132f,  0.2f },  // 左翼端
            {  7.671f,  1.789f,  -1.132f,  0.2f },  // 右翼端
        };
    }


    @Override
    protected List<Trail> createTrails() {
        // Pzl37Los: 左右翼端コントレイル（大型機）
        return List.of(new Trail(40, 1.0f), new Trail(40, 1.0f));
    }


    @Override
    public Item asItem() {
        return CivilianAviation.PZL37LOS_ITEM.get();
    }

    @Override
    public double getZoom() {
        return 12.0;
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
                gearAProgress = prevGearAProgress = 0.0f;
                gearProgress  = prevGearProgress  = 0.0f;
                hatchProgress = prevHatchProgress = 0.0f;
            } else {
                gearAProgress = prevGearAProgress = 1.0f;
                gearProgress  = prevGearProgress  = 1.0f;
                hatchProgress = prevHatchProgress = 1.0f;
            }
        }
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 160.0f;
    }
}
