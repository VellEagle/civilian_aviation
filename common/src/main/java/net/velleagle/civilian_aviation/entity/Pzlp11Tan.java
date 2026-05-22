package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

public class Pzlp11Tan extends Pzlp11 {

    public Pzlp11Tan(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Item asItem() {
        return CivilianAviation.PZLP11_TAN_ITEM.get();
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 140.0f;
    }
}
