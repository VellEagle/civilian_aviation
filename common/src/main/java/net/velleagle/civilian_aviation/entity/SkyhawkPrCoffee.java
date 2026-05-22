package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.Sounds;
import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.misc.Trail;
import immersive_aircraft.item.upgrade.VehicleStat;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class SkyhawkPrCoffee extends CivilianAircraftEntity {

    @Override
    public boolean hasDoors()  { return true; }

    @Override protected float getDoorLCX() { return  1.0f; }
    @Override protected float getDoorLCY() { return  2.22f;  }
    @Override protected float getDoorLCZ() { return  -0.191f; }
    @Override protected float getDoorLHW() { return  0.2f; }
    @Override protected float getDoorLHH() { return  0.2f; }
    @Override protected float getDoorLHD() { return  0.2f; }

    @Override protected float getDoorLOpenCX() { return  1.765f; }
    @Override protected float getDoorLOpenCY() { return  2.22f;  }
    @Override protected float getDoorLOpenCZ() { return  0.0f; }
    @Override protected float getDoorLOpenHW() { return  0.2f; }
    @Override protected float getDoorLOpenHH() { return  0.2f; }
    @Override protected float getDoorLOpenHD() { return  0.2f; }

    @Override protected float getDoorRCX() { return -1.0f; }
    @Override protected float getDoorRCY() { return  2.22f;  }
    @Override protected float getDoorRCZ() { return  -0.191f; }
    @Override protected float getDoorRHW() { return  0.2f; }
    @Override protected float getDoorRHH() { return  0.2f; }
    @Override protected float getDoorRHD() { return  0.2f; }

    @Override protected float getDoorROpenCX() { return -1.765f; }
    @Override protected float getDoorROpenCY() { return  2.22f;  }
    @Override protected float getDoorROpenCZ() { return  0.0f; }
    @Override protected float getDoorROpenHW() { return  0.2f; }
    @Override protected float getDoorROpenHH() { return  0.2f; }
    @Override protected float getDoorROpenHD() { return  0.2f; }

    @Override
    public net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side getOnMountCloseSide() {
        return net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.RIGHT;
    }

    public SkyhawkPrCoffee(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        super.tick();

        float water = (float) fluidHeight.getDouble(FluidTags.WATER);
        if (water > 0) {
            emitSplashParticle(1.5204f, water, -0.5f, 0.0f, 0.0f, 0.0f);
            emitSplashParticle(-1.5204f, water, -0.5f, 0.0f, 0.0f, 0.0f);
        }
    }

    public void emitSplashParticle(float x, float y, float z, float nx, float ny, float nz) {
        if (!isWithinParticleRange() || !level().isClientSide) return;
        Matrix4f transform = getVehicleTransform();
        double length = Math.min(100, getSpeedVector().length() * 20.0f);
        while (length > 1.0) {
            length--;
            if (length > random.nextFloat()) {
                Vector4f p = transformPosition(transform,
                        x + (random.nextFloat() - 0.5f), y, z - random.nextFloat());
                level().addParticle(ParticleTypes.BUBBLE, p.x, p.y, p.z, nx, ny, nz);
                level().addParticle(ParticleTypes.SPLASH, p.x, p.y, p.z, nx, ny, nz);
            }
        }
    }

    @Override
    public double getDefaultGravity() {
        float water = (float) getFluidHeight(FluidTags.WATER);
        return water > 0
                ? -0.04 * water
                : (1.0 - getEnginePower()) * super.getDefaultGravity();
    }

    @Override
    protected float[][] getTrailPositions() {
        return new float[][] {
                { -6.818f,  2.48f,  -0.55f,  0.15f },
                {  6.818f,  2.48f,  -0.55f,  0.15f },
        };
    }

    @Override
    protected List<Trail> createTrails() {
        return List.of(new Trail(40, 1.0f), new Trail(40, 1.0f));
    }

    @Override
    protected void updateVelocity() {
        super.updateVelocity();
        if (wasTouchingWater) {
            setXRot((getXRot() + getProperties().get(VehicleStat.GROUND_PITCH)) * 0.9f
                    - getProperties().get(VehicleStat.GROUND_PITCH));
        }
    }

    @Override public boolean worksUnderWater() { return true; }
    protected float getDismountRotation() { return 0.0f; }
    @Override public double getZoom() { return 6.0; }

    @Override
    public Item asItem() {
        return CivilianAviation.SKYHAWK_PR_COFFEE_ITEM.get();
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 120.0f;
    }
}
