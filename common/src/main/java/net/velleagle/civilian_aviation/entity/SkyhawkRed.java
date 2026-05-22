package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.misc.Trail;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

public class SkyhawkRed extends CivilianAircraftEntity {

    @Override
    public boolean hasDoors()  { return true; }

    @Override protected float getDoorLCX() { return  1.0f; }
    @Override protected float getDoorLCY() { return  1.76f;  }
    @Override protected float getDoorLCZ() { return  -0.191f; }
    @Override protected float getDoorLHW() { return  0.2f; }
    @Override protected float getDoorLHH() { return  0.2f; }
    @Override protected float getDoorLHD() { return  0.2f; }

    @Override protected float getDoorLOpenCX() { return  1.765f; }
    @Override protected float getDoorLOpenCY() { return  1.76f;  }
    @Override protected float getDoorLOpenCZ() { return  0.0f; }
    @Override protected float getDoorLOpenHW() { return  0.2f; }
    @Override protected float getDoorLOpenHH() { return  0.2f; }
    @Override protected float getDoorLOpenHD() { return  0.2f; }

    @Override protected float getDoorRCX() { return -1.0f; }
    @Override protected float getDoorRCY() { return  1.76f;  }
    @Override protected float getDoorRCZ() { return  -0.191f; }
    @Override protected float getDoorRHW() { return  0.2f; }
    @Override protected float getDoorRHH() { return  0.2f; }
    @Override protected float getDoorRHD() { return  0.2f; }

    @Override protected float getDoorROpenCX() { return -1.765f; }
    @Override protected float getDoorROpenCY() { return  1.76f;  }
    @Override protected float getDoorROpenCZ() { return  0.0f; }
    @Override protected float getDoorROpenHW() { return  0.2f; }
    @Override protected float getDoorROpenHH() { return  0.2f; }
    @Override protected float getDoorROpenHD() { return  0.2f; }


    @Override
    public net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side getOnMountCloseSide() {
        return net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.RIGHT;
    }

    public SkyhawkRed(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected float[][] getTrailPositions() {
        // 左翼端(index=0), 右翼端(index=1)
        // { x, y, z, halfWidth }
        return new float[][] {
                { -6.818f,  2.48f,  -0.07f,  0.15f },  // 左翼端
                {  6.818f,  2.48f,  -0.07f,  0.15f },  // 右翼端
        };
    }


    @Override
    protected List<Trail> createTrails() {
        // Skyhawk: 左右翼端コントレイル
        return List.of(new Trail(40, 1.0f), new Trail(40, 1.0f));
    }


    @Override
    public Item asItem() {
        return CivilianAviation.SKYHAWK_RED_ITEM.get();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public double getZoom() {
        return 3.0;
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 120.0f;
    }
}