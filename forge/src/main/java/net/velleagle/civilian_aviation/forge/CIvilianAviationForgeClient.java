package net.velleagle.civilian_aviation.forge;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.CivilianAviationClient;
import net.velleagle.civilian_aviation.client.CivilianAviationKeyBindings;
import net.velleagle.civilian_aviation.sound.CivilianSoundHandler;

// ---- MOD バス（初期化イベント） ----------------------------------------
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(
        modid = CivilianAviation.MOD_ID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public class CIvilianAviationForgeClient {

    @SubscribeEvent
    public static void setup(EntityRenderersEvent.RegisterRenderers event) {
        CivilianAviationClient.init();
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        CivilianAviationKeyBindings.list.forEach(event::register);
    }
}

// ---- FORGE バス（クライアント tick & HUD） ------------------------------
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(
        modid = CivilianAviation.MOD_ID,
        value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.FORGE)
class CivilianAviationForgeClientEvents {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            CivilianSoundHandler.INSTANCE.tick();
        }
    }
}
