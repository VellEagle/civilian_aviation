package net.velleagle.civilian_aviation.network.c2s;

import immersive_aircraft.cobalt.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;

/**
 * クライアント → サーバー: 指定エンティティの指定ドアの開閉トグルを要求する。
 *
 * Side:
 *   LEFT       … door_l  （CivilianAircraftEntity / HelicopterEntity 共通）
 *   RIGHT      … door_r  （同上）
 *   SIDE_LEFT  … sidedoor_l  （HelicopterEntity 専用）
 *   SIDE_RIGHT … sidedoor_r  （HelicopterEntity 専用）
 */
public class DoorMessage extends Message {

    public enum Side {
        LEFT,
        RIGHT,
        SIDE_LEFT,
        SIDE_RIGHT
    }

    private final int entityId;
    private final Side side;

    public DoorMessage(int entityId, Side side) {
        this.entityId = entityId;
        this.side = side;
    }

    public DoorMessage(FriendlyByteBuf b) {
        this.entityId = b.readInt();
        this.side = Side.values()[b.readInt()];
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(entityId);
        b.writeInt(side.ordinal());
    }

    @Override
    public void receive(Player player) {
        Entity entity = player.level().getEntity(entityId);

        // CivilianAircraftEntity（固定翼機など）: LEFT / RIGHT のみ対応
        if (entity instanceof CivilianAircraftEntity aircraft && aircraft.hasDoors()) {
            if (player.distanceToSqr(aircraft) < 64.0) {
                if (side == Side.LEFT || side == Side.RIGHT) {
                    aircraft.toggleDoor(side == Side.LEFT
                            ? net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.LEFT
                            : net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.RIGHT);
                }
            }
            return;
        }

        // HelicopterEntity: 全4種に対応
        if (entity instanceof HelicopterEntity heli && heli.hasDoors()) {
            if (player.distanceToSqr(heli) < 64.0) {
                switch (side) {
                    case LEFT       -> heli.toggleDoor(Side.LEFT);
                    case RIGHT      -> heli.toggleDoor(Side.RIGHT);
                    case SIDE_LEFT  -> heli.toggleSideDoor(Side.LEFT);
                    case SIDE_RIGHT -> heli.toggleSideDoor(Side.RIGHT);
                }
            }
        }
    }
}
