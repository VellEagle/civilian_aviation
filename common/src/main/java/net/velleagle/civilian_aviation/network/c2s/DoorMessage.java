package net.velleagle.civilian_aviation.network.c2s;

import immersive_aircraft.cobalt.network.Message;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;

/**
 * クライアント → サーバー: 指定エンティティの指定ドアの開閉トグルを要求する。
 */
public class DoorMessage extends Message {

    public static final CustomPacketPayload.Type<DoorMessage> TYPE = Message.createType("door");
    public static final StreamCodec<RegistryFriendlyByteBuf, DoorMessage> STREAM_CODEC =
            StreamCodec.ofMember(DoorMessage::encode, DoorMessage::new);

    @Override
    public CustomPacketPayload.Type<DoorMessage> type() {
        return TYPE;
    }

    public enum Side {
        LEFT, RIGHT, SIDE_LEFT, SIDE_RIGHT
    }

    private final int entityId;
    private final Side side;

    public DoorMessage(int entityId, Side side) {
        this.entityId = entityId;
        this.side = side;
    }

    public DoorMessage(RegistryFriendlyByteBuf b) {
        this.entityId = b.readInt();
        this.side = Side.values()[b.readInt()];
    }

    @Override
    public void encode(RegistryFriendlyByteBuf b) {
        b.writeInt(entityId);
        b.writeInt(side.ordinal());
    }

    @Override
    public void receiveServer(ServerPlayer player) {
        Entity entity = player.level().getEntity(entityId);

        if (entity instanceof CivilianAircraftEntity aircraft && aircraft.hasDoors()) {
            if (player.distanceToSqr(aircraft) < 64.0) {
                if (side == Side.LEFT || side == Side.RIGHT) {
                    aircraft.toggleDoor(side == Side.LEFT ? Side.LEFT : Side.RIGHT);
                }
            }
            return;
        }

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
