package net.velleagle.civilian_aviation.network.c2s;

import immersive_aircraft.cobalt.network.Message;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;

/**
 * クライアント → サーバー: ランディングギアのトグル要求。
 */
public class LandingGearMessage extends Message {

    public static final CustomPacketPayload.Type<LandingGearMessage> TYPE = Message.createType("landing_gear");
    public static final StreamCodec<RegistryFriendlyByteBuf, LandingGearMessage> STREAM_CODEC =
            StreamCodec.ofMember(LandingGearMessage::encode, LandingGearMessage::new);

    @Override
    public CustomPacketPayload.Type<LandingGearMessage> type() {
        return TYPE;
    }

    private final int entityId;

    public LandingGearMessage(int entityId) {
        this.entityId = entityId;
    }

    public LandingGearMessage(RegistryFriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public void receiveServer(ServerPlayer player) {
        Entity entity = player.level().getEntity(entityId);
        if (entity instanceof CivilianAircraftEntity aircraft
                && aircraft.hasLandingGear()
                && player.equals(aircraft.getFirstPassenger())) {
            aircraft.toggleLandingGear();
        }
    }
}
