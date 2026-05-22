package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

public class Bell47gBlack extends HelicopterEntity {

    public Bell47gBlack(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Item asItem() {
        return CivilianAviation.BELL47G_BLACK_ITEM.get();
    }

    @Override
    public double getZoom() {
        return 6.0;
    }

    @Override
    public void setAnimationVariables(float tickDelta) {
        super.setAnimationVariables(tickDelta);
        float stickX = pressingInterpolatedZ.getSmooth(tickDelta);
        BBAnimationVariables.set("lever_stick", stickX);
        float stickZ = pressingInterpolatedX.getSmooth(tickDelta);
        float clampedZ = Math.max(-0.5f, Math.min(1.0f, stickZ));
        BBAnimationVariables.set("lever_stick_z", clampedZ);
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 100.0f;
    }
}
