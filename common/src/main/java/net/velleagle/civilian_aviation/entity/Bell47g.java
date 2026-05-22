package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

public class Bell47g extends HelicopterEntity {

    public Bell47g(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Item asItem() {
        return CivilianAviation.BELL47G_ITEM.get();
    }

    @Override
    public double getZoom() {
        return 6.0;
    }

    // -------------------------------------------------------
    // BBアニメーション変数
    // -------------------------------------------------------
    @Override
    public void setAnimationVariables(float tickDelta) {
        super.setAnimationVariables(tickDelta);

        // lever_stick (A/D / テールローター): -1.0〜1.0
        float stickX = pressingInterpolatedZ.getSmooth(tickDelta);
        BBAnimationVariables.set("lever_stick", stickX);

        // lever_stick_z (W/S / 前後推力): 前+1.0, 後-0.5 にクランプ
        float stickZ = pressingInterpolatedX.getSmooth(tickDelta);
        float clampedZ = Math.max(-0.5f, Math.min(1.0f, stickZ));
        BBAnimationVariables.set("lever_stick_z", clampedZ);
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 100.0f;
    }
}
