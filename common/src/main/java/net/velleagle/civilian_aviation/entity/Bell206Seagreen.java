package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.network.c2s.DoorMessage;

public class Bell206Seagreen extends HelicopterEntity {

    public Bell206Seagreen(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Item asItem() {
        return CivilianAviation.BELL206_SEAGREEN_ITEM.get();
    }

    @Override
    public double getZoom() {
        return 6.0;
    }

    @Override
    public boolean hasDoors() { return true; }

    @Override
    public DoorMessage.Side getOnMountCloseSide() {
        return DoorMessage.Side.LEFT;
    }

    @Override protected float getDoorLCX() { return  1.03f; }
    @Override protected float getDoorLCY() { return  1.563f; }
    @Override protected float getDoorLCZ() { return  1.398f; }
    @Override protected float getDoorLHW() { return  0.2f; }
    @Override protected float getDoorLHH() { return  0.2f; }
    @Override protected float getDoorLHD() { return  0.2f; }
    @Override protected float getDoorLOpenCX() { return  1.613f; }
    @Override protected float getDoorLOpenCY() { return  1.563f; }
    @Override protected float getDoorLOpenCZ() { return  1.607f; }
    @Override protected float getDoorLOpenHW() { return  0.2f; }
    @Override protected float getDoorLOpenHH() { return  0.2f; }
    @Override protected float getDoorLOpenHD() { return  0.2f; }
    @Override protected float getDoorRCX() { return -1.03f; }
    @Override protected float getDoorRCY() { return  1.563f; }
    @Override protected float getDoorRCZ() { return  1.398f; }
    @Override protected float getDoorRHW() { return  0.2f; }
    @Override protected float getDoorRHH() { return  0.2f; }
    @Override protected float getDoorRHD() { return  0.2f; }
    @Override protected float getDoorROpenCX() { return -1.613f; }
    @Override protected float getDoorROpenCY() { return  1.563f; }
    @Override protected float getDoorROpenCZ() { return  1.607f; }
    @Override protected float getDoorROpenHW() { return  0.2f; }
    @Override protected float getDoorROpenHH() { return  0.2f; }
    @Override protected float getDoorROpenHD() { return  0.2f; }
    @Override protected float getSideDoorLCX() { return  1.03f; }
    @Override protected float getSideDoorLCY() { return  1.513f; }
    @Override protected float getSideDoorLCZ() { return -0.088f; }
    @Override protected float getSideDoorLHW() { return  0.2f; }
    @Override protected float getSideDoorLHH() { return  0.2f; }
    @Override protected float getSideDoorLHD() { return  0.2f; }
    @Override protected float getSideDoorLOpenCX() { return  1.869f; }
    @Override protected float getSideDoorLOpenCY() { return  1.513f; }
    @Override protected float getSideDoorLOpenCZ() { return  0.321f; }
    @Override protected float getSideDoorLOpenHW() { return  0.2f; }
    @Override protected float getSideDoorLOpenHH() { return  0.2f; }
    @Override protected float getSideDoorLOpenHD() { return  0.2f; }
    @Override protected float getSideDoorRCX() { return -1.03f; }
    @Override protected float getSideDoorRCY() { return  1.513f; }
    @Override protected float getSideDoorRCZ() { return -0.088f; }
    @Override protected float getSideDoorRHW() { return  0.2f; }
    @Override protected float getSideDoorRHH() { return  0.2f; }
    @Override protected float getSideDoorRHD() { return  0.2f; }
    @Override protected float getSideDoorROpenCX() { return -1.869f; }
    @Override protected float getSideDoorROpenCY() { return  1.513f; }
    @Override protected float getSideDoorROpenCZ() { return  0.321f; }
    @Override protected float getSideDoorROpenHW() { return  0.2f; }
    @Override protected float getSideDoorROpenHH() { return  0.2f; }
    @Override protected float getSideDoorROpenHD() { return  0.2f; }

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
        return 60.0f;
    }
}
