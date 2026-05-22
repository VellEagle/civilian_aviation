package net.velleagle.civilian_aviation.neoforge;

import net.velleagle.civilian_aviation.CivilianAviation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;

@Mod(CivilianAviation.MOD_ID)
@EventBusSubscriber(modid = CivilianAviation.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CivilianAviationNeoForge {
    private static boolean registered = false;

    @SubscribeEvent
    public static void onRegistryEvent(RegisterEvent event) {
        if (!registered) {
            registered = true;
            CivilianAviation.init();
        }
    }
}
