package net.velleagle.civilian_aviation;

import immersive_aircraft.cobalt.network.NetworkHandler;
import net.velleagle.civilian_aviation.network.c2s.DoorMessage;
import net.velleagle.civilian_aviation.network.c2s.LandingGearMessage;
import net.velleagle.civilian_aviation.network.c2s.PaintMessage;

public class CivilianAviationMessages {
    public static void loadMessages() {
        NetworkHandler.registerMessage(
                CivilianAviation.MOD_ID,
                DoorMessage.class,
                DoorMessage::new
        );
        NetworkHandler.registerMessage(
                CivilianAviation.MOD_ID,
                PaintMessage.class,
                PaintMessage::new
        );
        NetworkHandler.registerMessage(
                CivilianAviation.MOD_ID,
                LandingGearMessage.class,
                LandingGearMessage::new
        );
    }
}
