package net.velleagle.civilian_aviation;

import immersive_aircraft.cobalt.network.NetworkHandler;
import net.velleagle.civilian_aviation.network.c2s.DoorMessage;
import net.velleagle.civilian_aviation.network.c2s.LandingGearMessage;
import net.velleagle.civilian_aviation.network.c2s.PaintMessage;

public class CivilianAviationMessages {
    public static void loadMessages() {
        NetworkHandler.registerMessage(
                CivilianAviation.MOD_ID,
                DoorMessage.TYPE,
                DoorMessage.STREAM_CODEC
        );
        NetworkHandler.registerMessage(
                CivilianAviation.MOD_ID,
                PaintMessage.TYPE,
                PaintMessage.STREAM_CODEC
        );
        NetworkHandler.registerMessage(
                CivilianAviation.MOD_ID,
                LandingGearMessage.TYPE,
                LandingGearMessage.STREAM_CODEC
        );
    }
}
