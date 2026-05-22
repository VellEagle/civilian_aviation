package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.misc.Trail;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

public class VulcanairSeagreen extends CivilianAircraftEntity {

    @Override
    public boolean hasDoors()  { return true; }

    @Override
    public net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side getOnMountCloseSide() {
        return net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.RIGHT;
    }

    @Override protected float getDoorLCX() { return  0.931f; }
    @Override protected float getDoorLCY() { return  1.52f;  }
    @Override protected float getDoorLCZ() { return  -1.553f; }
    @Override protected float getDoorLHW() { return  0.2f; }
    @Override protected float getDoorLHH() { return  0.2f; }
    @Override protected float getDoorLHD() { return  0.2f; }

    @Override protected float getDoorLOpenCX() { return  1.75f; }
    @Override protected float getDoorLOpenCY() { return  1.52f;  }
    @Override protected float getDoorLOpenCZ() { return  -1.225f; }
    @Override protected float getDoorLOpenHW() { return  0.2f; }
    @Override protected float getDoorLOpenHH() { return  0.2f; }
    @Override protected float getDoorLOpenHD() { return  0.2f; }

    @Override protected float getDoorRCX() { return  -0.916f; }
    @Override protected float getDoorRCY() { return  1.52f;  }
    @Override protected float getDoorRCZ() { return  -3.647f; }
    @Override protected float getDoorRHW() { return  0.2f; }
    @Override protected float getDoorRHH() { return  0.2f; }
    @Override protected float getDoorRHD() { return  0.2f; }

    @Override protected float getDoorROpenCX() { return  -1.564f; }
    @Override protected float getDoorROpenCY() { return  1.52f;  }
    @Override protected float getDoorROpenCZ() { return  -3.436f; }
    @Override protected float getDoorROpenHW() { return  0.2f; }
    @Override protected float getDoorROpenHH() { return  0.2f; }
    @Override protected float getDoorROpenHD() { return  0.2f; }

    public VulcanairSeagreen(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected float[][] getTrailPositions() {
        return new float[][] {
            { -6.61f,  2.3f, -1.92f,  0.15f },
            {  6.61f,  2.3f, -1.92f,  0.15f },
        };
    }

    @Override
    protected List<Trail> createTrails() {
        return List.of(new Trail(40, 1.0f), new Trail(40, 1.0f));
    }

    @Override
    public Item asItem() {
        return CivilianAviation.VULCANAIR_SEAGREEN_ITEM.get();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public double getZoom() {
        return 7.0;
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 50.0f;
    }
}
