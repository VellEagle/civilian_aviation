package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

public class Pzl37LosTan extends Pzl37Los {

    public Pzl37LosTan(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    public Item asItem() {
        return CivilianAviation.PZL37LOS_TAN_ITEM.get();
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 160.0f;
    }
}
