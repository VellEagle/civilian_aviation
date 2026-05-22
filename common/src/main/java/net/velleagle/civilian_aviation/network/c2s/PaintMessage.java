package net.velleagle.civilian_aviation.network.c2s;

import immersive_aircraft.cobalt.network.Message;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;
import net.velleagle.civilian_aviation.item.AircraftVariant;

import java.util.ArrayList;
import java.util.List;

/**
 * クライアント → サーバー: 機体の色バリアント変更を要求する。
 */
public class PaintMessage extends Message {

    public static final CustomPacketPayload.Type<PaintMessage> TYPE = Message.createType("paint");
    public static final StreamCodec<RegistryFriendlyByteBuf, PaintMessage> STREAM_CODEC =
            StreamCodec.ofMember(PaintMessage::encode, PaintMessage::new);

    @Override
    public CustomPacketPayload.Type<PaintMessage> type() {
        return TYPE;
    }

    private final int    entityId;
    private final String targetVariantId;

    public PaintMessage(int entityId, String targetVariantId) {
        this.entityId        = entityId;
        this.targetVariantId = targetVariantId;
    }

    public PaintMessage(RegistryFriendlyByteBuf b) {
        this.entityId        = b.readInt();
        this.targetVariantId = b.readUtf();
    }

    @Override
    public void encode(RegistryFriendlyByteBuf b) {
        b.writeInt(entityId);
        b.writeUtf(targetVariantId);
    }

    @Override
    public void receiveServer(ServerPlayer player) {
        Level level  = player.level();
        Entity entity = level.getEntity(entityId);

        AircraftEntity aircraft;
        String currentVariantId;
        if (entity instanceof CivilianAircraftEntity ca) {
            aircraft         = ca;
            currentVariantId = ca.getVariantEntityId();
        } else if (entity instanceof HelicopterEntity he) {
            aircraft         = he;
            currentVariantId = he.getVariantEntityId();
        } else {
            return;
        }

        if (player.distanceToSqr(aircraft) > 64.0) return;
        if (currentVariantId.equals(targetVariantId)) return;

        List<AircraftVariant> group = AircraftVariant.findGroup(currentVariantId);
        boolean targetInGroup = group.stream().anyMatch(v -> v.entityId().equals(targetVariantId));
        if (!targetInGroup) return;

        ResourceLocation targetTypeId = ResourceLocation.fromNamespaceAndPath("civilian_aviation", targetVariantId);
        EntityType<?>    targetType   = BuiltInRegistries.ENTITY_TYPE.get(targetTypeId);
        if (targetType == null) return;

        Entity newEntity = targetType.create(level);
        if (newEntity == null) return;

        newEntity.copyPosition(aircraft);
        newEntity.setDeltaMovement(aircraft.getDeltaMovement());
        newEntity.setYRot(aircraft.getYRot());
        newEntity.setXRot(aircraft.getXRot());
        newEntity.yRotO = aircraft.yRotO;
        newEntity.xRotO = aircraft.xRotO;
        if (newEntity instanceof AircraftEntity newAircraft) {
            newAircraft.setHealth(aircraft.getHealth());
            newAircraft.setDyeColor(aircraft.getDyeColor());
        }

        for (Entity passenger : new ArrayList<>(aircraft.getPassengers())) {
            passenger.stopRiding();
            passenger.startRiding(newEntity, true);
        }

        aircraft.discard();
        level.addFreshEntity(newEntity);
    }
}
