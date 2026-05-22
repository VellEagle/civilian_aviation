package net.velleagle.civilian_aviation.network.c2s;

import immersive_aircraft.cobalt.network.Message;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;

/**
 * クライアント → サーバー: ランディングギアのトグル要求。
 *
 * プレイヤーが G キーを押したときに送信される。
 * hasLandingGear() が true の全機体に対応する。
 */
public class LandingGearMessage extends Message {

    private final int entityId;

    public LandingGearMessage(int entityId) {
        this.entityId = entityId;
    }

    public LandingGearMessage(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    @Override
    public void receive(Player player) {
        Entity entity = player.level().getEntity(entityId);
        if (entity instanceof CivilianAircraftEntity aircraft
                && aircraft.hasLandingGear()
                && player.equals(aircraft.getFirstPassenger())) {
            aircraft.toggleLandingGear();
        }
    }
}
