package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.misc.Trail;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

public class TrimotorBlue extends CivilianAircraftEntity {


    @Override
    public net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side getOnMountCloseSide() {
        return net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.RIGHT;
    }

    public TrimotorBlue(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected float[][] getTrailPositions() {
        // 左翼エンジン(index=0), 右翼エンジン(index=1), 機首エンジン(index=2)
        return new float[][] {
            { -10.9f,  3.773f,  -1.02f,  0.15f },  // 左翼エンジン
            {  10.9f,  3.773f,  -1.02f,  0.15f },  // 右翼エンジン
        };
    }


    @Override
    protected List<Trail> createTrails() {
        // TrimotorBlue: 左右翼エンジン コントレイル
        return List.of(new Trail(40, 1.0f), new Trail(40, 1.0f));
    }


    @Override
    public Item asItem() {
        return CivilianAviation.TRIMOTOR_BLUE_ITEM.get();
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public double getZoom() {
        return 15.0;
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 180.0f;
    }
}