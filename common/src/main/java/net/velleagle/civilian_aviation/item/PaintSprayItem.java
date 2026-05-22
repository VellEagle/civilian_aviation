package net.velleagle.civilian_aviation.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;

/**
 * ペイントスプレーアイテム。
 *
 * 機体への使用は CivilianAircraftEntity#interact() で検出し、
 * PaintSprayScreen を開く。このクラス自体はアイテムとして登録するためだけに存在する。
 */
public class PaintSprayItem extends Item {

    public PaintSprayItem(Properties properties) {
        super(properties);
    }

    /** ブロックへの使用は何もしない */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        return InteractionResult.PASS;
    }
}
