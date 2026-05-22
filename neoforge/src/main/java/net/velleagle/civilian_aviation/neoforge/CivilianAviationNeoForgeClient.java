package net.velleagle.civilian_aviation.neoforge;

import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

import static immersive_aircraft.ItemColors.getDyeColor;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.CivilianAviationClient;
import net.velleagle.civilian_aviation.client.CivilianAviationKeyBindings;
import net.velleagle.civilian_aviation.sound.CivilianSoundHandler;

// ---- MOD バス（初期化イベント） ----------------------------------------
@SuppressWarnings("unused")
@EventBusSubscriber(
        modid = CivilianAviation.MOD_ID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.MOD)
public class CivilianAviationNeoForgeClient {

    @SubscribeEvent
    public static void setup(FMLClientSetupEvent event) {
        CivilianAviationClient.init();
    }

    @SubscribeEvent
    public static void initItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(getDyeColor(0xFFFFFFFF),
                CivilianAviation.E500_RED_ITEM.get(), CivilianAviation.E500_BLACKRED_ITEM.get(),
                CivilianAviation.E500_BLUE_ITEM.get(), CivilianAviation.E500_EXTRAVAGANT_ITEM.get(),
                CivilianAviation.E500_GREEN_ITEM.get(), CivilianAviation.E500_RUSTY_ITEM.get(),
                CivilianAviation.E500_SILVER_ITEM.get(), CivilianAviation.E500_YELLOW_ITEM.get(),
                CivilianAviation.BELL206_BLACKSTRIPE_ITEM.get(), CivilianAviation.BELL206_BLACK_ITEM.get(),
                CivilianAviation.BELL206_BLANK_ITEM.get(), CivilianAviation.BELL206_BLUE_ITEM.get(),
                CivilianAviation.BELL206_BROWN_ITEM.get(), CivilianAviation.BELL206_GRAY_ITEM.get(),
                CivilianAviation.BELL206_GREEN_ITEM.get(), CivilianAviation.BELL206_OLIVE_ITEM.get(),
                CivilianAviation.BELL206_ORANGE_ITEM.get(), CivilianAviation.BELL206_POLICE_ITEM.get(),
                CivilianAviation.BELL206_RED_ITEM.get(), CivilianAviation.BELL206_SEAGREEN_ITEM.get(),
                CivilianAviation.BELL206_SKYBLUE_ITEM.get(), CivilianAviation.BELL206_YELLOW_ITEM.get(),
                CivilianAviation.BELL47G_ITEM.get(), CivilianAviation.BELL47G_BLACK_ITEM.get(),
                CivilianAviation.BELL47G_BLUE_ITEM.get(), CivilianAviation.BELL47G_OLIVE_ITEM.get(),
                CivilianAviation.PZLP11_ITEM.get(), CivilianAviation.PZLP11_BROWN_ITEM.get(),
                CivilianAviation.PZLP11_GREEN_ITEM.get(), CivilianAviation.PZLP11_TAN_ITEM.get(),
                CivilianAviation.PZL37LOS_ITEM.get(), CivilianAviation.PZL37LOS_ARCTIC_ITEM.get(),
                CivilianAviation.PZL37LOS_BROWN_ITEM.get(), CivilianAviation.PZL37LOS_GREEN_ITEM.get(),
                CivilianAviation.PZL37LOS_TAN_ITEM.get(),
                CivilianAviation.TRIMOTOR_BLUE_ITEM.get(), CivilianAviation.TRIMOTOR_BLACK_ITEM.get(),
                CivilianAviation.TRIMOTOR_RED_ITEM.get(), CivilianAviation.TRIMOTOR_WHITE_ITEM.get(),
                CivilianAviation.VULCANAIR_RED_ITEM.get(), CivilianAviation.VULCANAIR_BLACKRED_ITEM.get(),
                CivilianAviation.VULCANAIR_BLACKYELLOW_ITEM.get(), CivilianAviation.VULCANAIR_BLANK_ITEM.get(),
                CivilianAviation.VULCANAIR_BLUE_ITEM.get(), CivilianAviation.VULCANAIR_BLUESTRIPE_ITEM.get(),
                CivilianAviation.VULCANAIR_COW_ITEM.get(), CivilianAviation.VULCANAIR_GRAY_ITEM.get(),
                CivilianAviation.VULCANAIR_GREEN_ITEM.get(), CivilianAviation.VULCANAIR_ORANGE_ITEM.get(),
                CivilianAviation.VULCANAIR_POLICE_ITEM.get(), CivilianAviation.VULCANAIR_REDSNAIL_ITEM.get(),
                CivilianAviation.VULCANAIR_REDYELLOW_ITEM.get(), CivilianAviation.VULCANAIR_SEAGREEN_ITEM.get(),
                CivilianAviation.VULCANAIR_WHITE_ITEM.get(), CivilianAviation.VULCANAIR_WINGED_ITEM.get(),
                CivilianAviation.VULCANAIR_YELLOW_ITEM.get(),
                CivilianAviation.COMANCHE_RED_ITEM.get(), CivilianAviation.COMANCHE_BLUE_ITEM.get(),
                CivilianAviation.COMANCHE_BLACKREDSTRIPE_ITEM.get(), CivilianAviation.COMANCHE_YELLOW_ITEM.get(),
                CivilianAviation.COMANCHE_ORANGEBROWN_ITEM.get(), CivilianAviation.COMANCHE_SEAGREEN_ITEM.get(),
                CivilianAviation.COMANCHE_BLANK_ITEM.get(),
                CivilianAviation.SKYHAWK_PR_RED_ITEM.get(), CivilianAviation.SKYHAWK_PR_COFFEE_ITEM.get(),
                CivilianAviation.SKYHAWK_PR_BLUESTRIPE_ITEM.get(), CivilianAviation.SKYHAWK_PR_GREEN_ITEM.get(),
                CivilianAviation.SKYHAWK_PR_BLACKORANGE_ITEM.get(), CivilianAviation.SKYHAWK_PR_BLACKRED_ITEM.get(),
                CivilianAviation.SKYHAWK_PR_BLACKYELLOW_ITEM.get(), CivilianAviation.SKYHAWK_PR_BLANK_ITEM.get(),
                CivilianAviation.SKYHAWK_PR_BLUE_ITEM.get(), CivilianAviation.SKYHAWK_PR_BLUERED_ITEM.get(),
                CivilianAviation.SKYHAWK_PR_BROWN_ITEM.get(), CivilianAviation.SKYHAWK_PR_BUTTER_ITEM.get(),
                CivilianAviation.SKYHAWK_PR_RED2_ITEM.get(),
                CivilianAviation.SKYHAWK_RED_ITEM.get(), CivilianAviation.SKYHAWK_COFFEE_ITEM.get(),
                CivilianAviation.SKYHAWK_BLUESTRIPE_ITEM.get(), CivilianAviation.SKYHAWK_GREEN_ITEM.get(),
                CivilianAviation.SKYHAWK_BLACKORANGE_ITEM.get(), CivilianAviation.SKYHAWK_BLACKRED_ITEM.get(),
                CivilianAviation.SKYHAWK_BLACKYELLOW_ITEM.get(), CivilianAviation.SKYHAWK_BLANK_ITEM.get(),
                CivilianAviation.SKYHAWK_BLUE_ITEM.get(), CivilianAviation.SKYHAWK_BLUERED_ITEM.get(),
                CivilianAviation.SKYHAWK_BROWN_ITEM.get(), CivilianAviation.SKYHAWK_BUTTER_ITEM.get(),
                CivilianAviation.SKYHAWK_RED2_ITEM.get()
        );
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        CivilianAviationKeyBindings.list.forEach(event::register);
    }
}

// ---- GAME バス（クライアント tick & HUD） ------------------------------
@SuppressWarnings("unused")
@EventBusSubscriber(
        modid = CivilianAviation.MOD_ID,
        value = Dist.CLIENT,
        bus = EventBusSubscriber.Bus.GAME)
class CivilianAviationNeoForgeClientEvents {

    @SubscribeEvent
    public static void onClientTick(net.neoforged.neoforge.event.tick.PlayerTickEvent.Post event) {
        if (event.getEntity().level().isClientSide()) {
            CivilianSoundHandler.INSTANCE.tick();
        }
    }
}
