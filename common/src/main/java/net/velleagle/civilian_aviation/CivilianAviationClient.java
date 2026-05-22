package net.velleagle.civilian_aviation;

import immersive_aircraft.WeaponRendererRegistry;
import immersive_aircraft.client.render.entity.weaponRenderer.SimpleWeaponRenderer;
import immersive_aircraft.cobalt.registration.Registration;
import net.minecraft.client.gui.GuiGraphics;
import net.velleagle.civilian_aviation.client.*;


public class CivilianAviationClient {
    public static void init() {
        // キーバインドを初期化（static ブロックをトリガー）
        // list に追加された KeyMapping は Fabric/Forge 側で登録される
        CivilianAviationKeyBindings.list.forEach(k -> { /* registration handled by platform */ });

        Registration.register(CivilianAviation.E500_RED_ENTITY.get(), E500RedRenderer::new);
        Registration.register(CivilianAviation.E500_BLACKRED_ENTITY.get(), E500BlackredRenderer::new);
        Registration.register(CivilianAviation.E500_BLUE_ENTITY.get(), E500BlueRenderer::new);
        Registration.register(CivilianAviation.E500_EXTRAVAGANT_ENTITY.get(), E500ExtravagantRenderer::new);
        Registration.register(CivilianAviation.E500_GREEN_ENTITY.get(), E500GreenRenderer::new);
        Registration.register(CivilianAviation.E500_RUSTY_ENTITY.get(), E500RustyRenderer::new);
        Registration.register(CivilianAviation.E500_SILVER_ENTITY.get(), E500SilverRenderer::new);
        Registration.register(CivilianAviation.E500_YELLOW_ENTITY.get(), E500YellowRenderer::new);
        Registration.register(CivilianAviation.BELL206_BLACKSTRIPE_ENTITY.get(), Bell206BlackstripeRenderer::new);
        Registration.register(CivilianAviation.BELL206_BLACK_ENTITY.get(), Bell206BlackRenderer::new);
        Registration.register(CivilianAviation.BELL206_BLANK_ENTITY.get(), Bell206BlankRenderer::new);
        Registration.register(CivilianAviation.BELL206_BLUE_ENTITY.get(), Bell206BlueRenderer::new);
        Registration.register(CivilianAviation.BELL206_BROWN_ENTITY.get(), Bell206BrownRenderer::new);
        Registration.register(CivilianAviation.BELL206_GRAY_ENTITY.get(), Bell206GrayRenderer::new);
        Registration.register(CivilianAviation.BELL206_GREEN_ENTITY.get(), Bell206GreenRenderer::new);
        Registration.register(CivilianAviation.BELL206_OLIVE_ENTITY.get(), Bell206OliveRenderer::new);
        Registration.register(CivilianAviation.BELL206_ORANGE_ENTITY.get(), Bell206OrangeRenderer::new);
        Registration.register(CivilianAviation.BELL206_POLICE_ENTITY.get(), Bell206PoliceRenderer::new);
        Registration.register(CivilianAviation.BELL206_RED_ENTITY.get(), Bell206RedRenderer::new);
        Registration.register(CivilianAviation.BELL206_SEAGREEN_ENTITY.get(), Bell206SeagreenRenderer::new);
        Registration.register(CivilianAviation.BELL206_SKYBLUE_ENTITY.get(), Bell206SkyblueRenderer::new);
        Registration.register(CivilianAviation.BELL206_YELLOW_ENTITY.get(), Bell206YellowRenderer::new);
        Registration.register(CivilianAviation.BELL47G_ENTITY.get(), Bell47gRenderer::new);
        Registration.register(CivilianAviation.BELL47G_BLACK_ENTITY.get(), Bell47gBlackRenderer::new);
        Registration.register(CivilianAviation.BELL47G_BLUE_ENTITY.get(), Bell47gBlueRenderer::new);
        Registration.register(CivilianAviation.BELL47G_OLIVE_ENTITY.get(), Bell47gOliveRenderer::new);
        Registration.register(CivilianAviation.PZLP11_ENTITY.get(), Pzlp11Renderer::new);
        Registration.register(CivilianAviation.PZLP11_BROWN_ENTITY.get(), Pzlp11BrownRenderer::new);
        Registration.register(CivilianAviation.PZLP11_GREEN_ENTITY.get(), Pzlp11GreenRenderer::new);
        Registration.register(CivilianAviation.PZLP11_TAN_ENTITY.get(), Pzlp11TanRenderer::new);
        Registration.register(CivilianAviation.PZL37LOS_ENTITY.get(), Pzl37LosRenderer::new);
        Registration.register(CivilianAviation.PZL37LOS_ARCTIC_ENTITY.get(), Pzl37LosArcticRenderer::new);
        Registration.register(CivilianAviation.PZL37LOS_BROWN_ENTITY.get(), Pzl37LosBrownRenderer::new);
        Registration.register(CivilianAviation.PZL37LOS_GREEN_ENTITY.get(), Pzl37LosGreenRenderer::new);
        Registration.register(CivilianAviation.PZL37LOS_TAN_ENTITY.get(), Pzl37LosTanRenderer::new);
        Registration.register(CivilianAviation.TRIMOTOR_BLUE_ENTITY.get(), TrimotorBlueRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_RED_ENTITY.get(), VulcanairRedRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_BLACKRED_ENTITY.get(), VulcanairBlackredRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_BLACKYELLOW_ENTITY.get(), VulcanairBlackyellowRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_BLANK_ENTITY.get(), VulcanairBlankRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_BLUE_ENTITY.get(), VulcanairBlueRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_BLUESTRIPE_ENTITY.get(), VulcanairBluestripeRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_COW_ENTITY.get(), VulcanairCowRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_GRAY_ENTITY.get(), VulcanairGrayRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_GREEN_ENTITY.get(), VulcanairGreenRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_ORANGE_ENTITY.get(), VulcanairOrangeRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_POLICE_ENTITY.get(), VulcanairPoliceRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_REDSNAIL_ENTITY.get(), VulcanairRedsnailRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_REDYELLOW_ENTITY.get(), VulcanairRedyellowRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_SEAGREEN_ENTITY.get(), VulcanairSeagreenRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_WHITE_ENTITY.get(), VulcanairWhiteRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_WINGED_ENTITY.get(), VulcanairWingedRenderer::new);
        Registration.register(CivilianAviation.VULCANAIR_YELLOW_ENTITY.get(), VulcanairYellowRenderer::new);
        Registration.register(CivilianAviation.TRIMOTOR_BLACK_ENTITY.get(), TrimotorBlackRenderer::new);
        Registration.register(CivilianAviation.TRIMOTOR_RED_ENTITY.get(), TrimotorRedRenderer::new);
        Registration.register(CivilianAviation.TRIMOTOR_WHITE_ENTITY.get(), TrimotorWhiteRenderer::new);
        Registration.register(CivilianAviation.COMANCHE_RED_ENTITY.get(), ComancheRedRenderer::new);
        Registration.register(CivilianAviation.COMANCHE_BLUE_ENTITY.get(), ComancheBlueRenderer::new);
        Registration.register(CivilianAviation.COMANCHE_BLACKREDSTRIPE_ENTITY.get(), ComancheBlackredstripeRenderer::new);
        Registration.register(CivilianAviation.COMANCHE_YELLOW_ENTITY.get(), ComancheYellowRenderer::new);
        Registration.register(CivilianAviation.COMANCHE_ORANGEBROWN_ENTITY.get(), ComancheOrangebrownRenderer::new);
        Registration.register(CivilianAviation.COMANCHE_SEAGREEN_ENTITY.get(), ComancheSeagreenRenderer::new);
        Registration.register(CivilianAviation.COMANCHE_BLANK_ENTITY.get(), ComancheBlankRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_RED_ENTITY.get(), SkyhawkPrRedRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_COFFEE_ENTITY.get(), SkyhawkPrCoffeeRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BLUESTRIPE_ENTITY.get(), SkyhawkPrBlueStripeRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_GREEN_ENTITY.get(), SkyhawkPrGreenRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BLACKORANGE_ENTITY.get(), SkyhawkPrBlackorangeRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BLACKRED_ENTITY.get(), SkyhawkPrBlackredRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BLACKYELLOW_ENTITY.get(), SkyhawkPrBlackyellowRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BLANK_ENTITY.get(), SkyhawkPrBlankRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BLUE_ENTITY.get(), SkyhawkPrBlueRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BLUERED_ENTITY.get(), SkyhawkPrBlueredRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BROWN_ENTITY.get(), SkyhawkPrBrownRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_BUTTER_ENTITY.get(), SkyhawkPrButterRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_PR_RED2_ENTITY.get(), SkyhawkPrRed2Renderer::new);
        Registration.register(CivilianAviation.SKYHAWK_RED_ENTITY.get(), SkyhawkRedRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_COFFEE_ENTITY.get(), SkyhawkCoffeeRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BLUESTRIPE_ENTITY.get(), SkyhawkBlueStripeRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_GREEN_ENTITY.get(), SkyhawkGreenRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BLACKORANGE_ENTITY.get(), SkyhawkBlackorangeRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BLACKRED_ENTITY.get(), SkyhawkBlackredRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BLACKYELLOW_ENTITY.get(), SkyhawkBlackyellowRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BLANK_ENTITY.get(), SkyhawkBlankRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BLUE_ENTITY.get(), SkyhawkBlueRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BLUERED_ENTITY.get(), SkyhawkBlueredRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BROWN_ENTITY.get(), SkyhawkBrownRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_BUTTER_ENTITY.get(), SkyhawkButterRenderer::new);
        Registration.register(CivilianAviation.SKYHAWK_RED2_ENTITY.get(), SkyhawkRed2Renderer::new);
        Registration.register(CivilianAviation.BASIC_BOMB.get(), BasicBombRenderer::new);

        WeaponRendererRegistry.register(CivilianAviation.locate("gunobserver"), new SimpleWeaponRenderer("gunobserver"));
        WeaponRendererRegistry.register(CivilianAviation.locate("gunm1919"), new SimpleWeaponRenderer("gunm1919"));
        WeaponRendererRegistry.register(CivilianAviation.locate("basicbomb_hardpoint"), new SimpleWeaponRenderer("basicbomb_hardpoint"));


    }
}

