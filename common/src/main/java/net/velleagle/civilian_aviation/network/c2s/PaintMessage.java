package net.velleagle.civilian_aviation.network.c2s;

import immersive_aircraft.cobalt.network.Message;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;
import net.velleagle.civilian_aviation.item.AircraftVariant;

import java.util.ArrayList;
import java.util.List;

/**
 * クライアント → サーバー: 機体の色バリアント変更を要求する。
 *
 * 色変更はエンティティの差し替えで実現する。
 * 各カラーバリアントは独立したエンティティタイプ/BBmodelとして定義されているため、
 * setDyeColor() による着色ではなくエンティティ自体を入れ替えてモデルを切り替える。
 */
public class PaintMessage extends Message {

    private final int    entityId;
    private final String targetVariantId;

    public PaintMessage(int entityId, String targetVariantId) {
        this.entityId        = entityId;
        this.targetVariantId = targetVariantId;
    }

    public PaintMessage(FriendlyByteBuf b) {
        this.entityId        = b.readInt();
        this.targetVariantId = b.readUtf();
    }

    @Override
    public void encode(FriendlyByteBuf b) {
        b.writeInt(entityId);
        b.writeUtf(targetVariantId);
    }

    @Override
    public void receive(Player player) {
        Level level  = player.level();
        Entity entity = level.getEntity(entityId);

        // CivilianAircraftEntity と HelicopterEntity の両方に対応
        AircraftEntity aircraft;
        String currentVariantId;
        if (entity instanceof CivilianAircraftEntity ca) {
            aircraft        = ca;
            currentVariantId = ca.getVariantEntityId();
        } else if (entity instanceof HelicopterEntity he) {
            aircraft        = he;
            currentVariantId = he.getVariantEntityId();
        } else {
            return;
        }

        // 近距離チェック（不正防止: 8ブロック以内）
        if (player.distanceToSqr(aircraft) > 64.0) return;

        // 同じバリアントなら何もしない
        if (currentVariantId.equals(targetVariantId)) return;

        // グループ内に target が存在するか確認
        List<AircraftVariant> group = AircraftVariant.findGroup(currentVariantId);
        boolean targetInGroup = group.stream().anyMatch(v -> v.entityId().equals(targetVariantId));
        if (!targetInGroup) return;

        // 変更先エンティティタイプを取得
        ResourceLocation targetTypeId = new ResourceLocation("civilian_aviation", targetVariantId);
        EntityType<?>    targetType   = BuiltInRegistries.ENTITY_TYPE.get(targetTypeId);
        if (targetType == null) return;

        // 新エンティティを生成
        Entity newEntity = targetType.create(level);
        if (newEntity == null) return;

        // 位置・向き・速度・体力・色を引き継ぐ
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

        // 搭乗者を引き継ぐ
        for (Entity passenger : new ArrayList<>(aircraft.getPassengers())) {
            passenger.stopRiding();
            passenger.startRiding(newEntity, true);
        }

        // 旧エンティティを除去して新エンティティをスポーン
        aircraft.discard();
        level.addFreshEntity(newEntity);
    }
}
