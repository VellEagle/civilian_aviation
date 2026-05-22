package net.velleagle.civilian_aviation;

import immersive_aircraft.Items;
import immersive_aircraft.WeaponRegistry;
import immersive_aircraft.cobalt.registration.Registration;
import immersive_aircraft.entity.misc.WeaponMount;
import immersive_aircraft.item.DyeableAircraftItem;
import immersive_aircraft.item.WeaponItem;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.velleagle.civilian_aviation.entity.*;
import net.velleagle.civilian_aviation.entity.bullet.BasicBombEntity;
import net.velleagle.civilian_aviation.entity.weapon.BasicBombBay;
import net.velleagle.civilian_aviation.entity.weapon.GunM1919;
import net.velleagle.civilian_aviation.entity.weapon.Observerguns;
import net.velleagle.civilian_aviation.item.AircraftVariant;
import net.velleagle.civilian_aviation.item.PaintSprayItem;
import net.velleagle.civilian_aviation.sound.*;

import java.util.List;
import java.util.function.Supplier;

import static immersive_aircraft.Items.baseProps;

public class CivilianAviation {
    public static final String MOD_ID = "civilian_aviation";

    // ---- 機体アイテム ------------------------------------
    public static Supplier<Item> E500_RED_ITEM;
    // E500 color variants
    public static Supplier<Item> E500_BLACKRED_ITEM;
    public static Supplier<Item> E500_BLUE_ITEM;
    public static Supplier<Item> E500_EXTRAVAGANT_ITEM;
    public static Supplier<Item> E500_GREEN_ITEM;
    public static Supplier<Item> E500_RUSTY_ITEM;
    public static Supplier<Item> E500_SILVER_ITEM;
    public static Supplier<Item> E500_YELLOW_ITEM;
    public static Supplier<Item> BELL206_BLACKSTRIPE_ITEM;
    // Bell206 color variants
    public static Supplier<Item> BELL206_BLACK_ITEM;
    public static Supplier<Item> BELL206_BLANK_ITEM;
    public static Supplier<Item> BELL206_BLUE_ITEM;
    public static Supplier<Item> BELL206_BROWN_ITEM;
    public static Supplier<Item> BELL206_GRAY_ITEM;
    public static Supplier<Item> BELL206_GREEN_ITEM;
    public static Supplier<Item> BELL206_OLIVE_ITEM;
    public static Supplier<Item> BELL206_ORANGE_ITEM;
    public static Supplier<Item> BELL206_POLICE_ITEM;
    public static Supplier<Item> BELL206_RED_ITEM;
    public static Supplier<Item> BELL206_SEAGREEN_ITEM;
    public static Supplier<Item> BELL206_SKYBLUE_ITEM;
    public static Supplier<Item> BELL206_YELLOW_ITEM;
    public static Supplier<Item> BELL47G_ITEM;
    // Bell47g color variants
    public static Supplier<Item> BELL47G_BLACK_ITEM;
    public static Supplier<Item> BELL47G_BLUE_ITEM;
    public static Supplier<Item> BELL47G_OLIVE_ITEM;
    public static Supplier<Item> BASIC_BOMB_BAY;
    public static Supplier<Item> GUN_OBSERVER;
    public static Supplier<Item> GUN_M1919;
    public static Supplier<Item> PZL37LOS_ITEM;
    // Pzl37Los color variants
    public static Supplier<Item> PZL37LOS_ARCTIC_ITEM;
    public static Supplier<Item> PZL37LOS_BROWN_ITEM;
    public static Supplier<Item> PZL37LOS_GREEN_ITEM;
    public static Supplier<Item> PZL37LOS_TAN_ITEM;
    public static Supplier<Item> PZLP11_ITEM;
    // Pzlp11 color variants
    public static Supplier<Item> PZLP11_BROWN_ITEM;
    public static Supplier<Item> PZLP11_GREEN_ITEM;
    public static Supplier<Item> PZLP11_TAN_ITEM;
    public static Supplier<Item> TRIMOTOR_BLUE_ITEM;
    public static Supplier<Item> TRIMOTOR_BLACK_ITEM;
    public static Supplier<Item> TRIMOTOR_RED_ITEM;
    public static Supplier<Item> TRIMOTOR_WHITE_ITEM;
    public static Supplier<Item> VULCANAIR_RED_ITEM;
    public static Supplier<Item> VULCANAIR_BLACKRED_ITEM;
    public static Supplier<Item> VULCANAIR_BLACKYELLOW_ITEM;
    public static Supplier<Item> VULCANAIR_BLANK_ITEM;
    public static Supplier<Item> VULCANAIR_BLUE_ITEM;
    public static Supplier<Item> VULCANAIR_BLUESTRIPE_ITEM;
    public static Supplier<Item> VULCANAIR_COW_ITEM;
    public static Supplier<Item> VULCANAIR_GRAY_ITEM;
    public static Supplier<Item> VULCANAIR_GREEN_ITEM;
    public static Supplier<Item> VULCANAIR_ORANGE_ITEM;
    public static Supplier<Item> VULCANAIR_POLICE_ITEM;
    public static Supplier<Item> VULCANAIR_REDSNAIL_ITEM;
    public static Supplier<Item> VULCANAIR_REDYELLOW_ITEM;
    public static Supplier<Item> VULCANAIR_SEAGREEN_ITEM;
    public static Supplier<Item> VULCANAIR_WHITE_ITEM;
    public static Supplier<Item> VULCANAIR_WINGED_ITEM;
    public static Supplier<Item> VULCANAIR_YELLOW_ITEM;
    public static Supplier<Item> COMANCHE_RED_ITEM;
    public static Supplier<Item> COMANCHE_BLUE_ITEM;
    public static Supplier<Item> COMANCHE_BLACKREDSTRIPE_ITEM;
    public static Supplier<Item> COMANCHE_YELLOW_ITEM;
    public static Supplier<Item> COMANCHE_ORANGEBROWN_ITEM;
    public static Supplier<Item> COMANCHE_SEAGREEN_ITEM;
    public static Supplier<Item> COMANCHE_BLANK_ITEM;
    public static Supplier<Item> SKYHAWK_PR_RED_ITEM;
    public static Supplier<Item> SKYHAWK_PR_COFFEE_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BLUESTRIPE_ITEM;
    public static Supplier<Item> SKYHAWK_PR_GREEN_ITEM;
    public static Supplier<Item> SKYHAWK_RED_ITEM;
    public static Supplier<Item> SKYHAWK_COFFEE_ITEM;
    public static Supplier<Item> SKYHAWK_BLUESTRIPE_ITEM;
    public static Supplier<Item> SKYHAWK_GREEN_ITEM;

    public static Supplier<Item> SKYHAWK_BLACKORANGE_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BLACKORANGE_ITEM;
    public static Supplier<Item> SKYHAWK_BLACKRED_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BLACKRED_ITEM;
    public static Supplier<Item> SKYHAWK_BLACKYELLOW_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BLACKYELLOW_ITEM;
    public static Supplier<Item> SKYHAWK_BLANK_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BLANK_ITEM;
    public static Supplier<Item> SKYHAWK_BLUE_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BLUE_ITEM;
    public static Supplier<Item> SKYHAWK_BLUERED_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BLUERED_ITEM;
    public static Supplier<Item> SKYHAWK_BROWN_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BROWN_ITEM;
    public static Supplier<Item> SKYHAWK_BUTTER_ITEM;
    public static Supplier<Item> SKYHAWK_PR_BUTTER_ITEM;
    public static Supplier<Item> SKYHAWK_RED2_ITEM;
    public static Supplier<Item> SKYHAWK_PR_RED2_ITEM;

    // ---- ペイントスプレー --------------------------------
    public static Supplier<Item> PAINT_SPRAY_ITEM;

    // ---- 機体エンティティタイプ --------------------------
    public static Supplier<EntityType<E500Red>>  E500_RED_ENTITY;
    // E500 color variants
    public static Supplier<EntityType<E500Blackred>>     E500_BLACKRED_ENTITY;
    public static Supplier<EntityType<E500Blue>>         E500_BLUE_ENTITY;
    public static Supplier<EntityType<E500Extravagant>>  E500_EXTRAVAGANT_ENTITY;
    public static Supplier<EntityType<E500Green>>        E500_GREEN_ENTITY;
    public static Supplier<EntityType<E500Rusty>>        E500_RUSTY_ENTITY;
    public static Supplier<EntityType<E500Silver>>       E500_SILVER_ENTITY;
    public static Supplier<EntityType<E500Yellow>>       E500_YELLOW_ENTITY;
    public static Supplier<EntityType<Bell206Blackstripe>>  BELL206_BLACKSTRIPE_ENTITY;
    // Bell206 color variants
    public static Supplier<EntityType<Bell206Black>>     BELL206_BLACK_ENTITY;
    public static Supplier<EntityType<Bell206Blank>>     BELL206_BLANK_ENTITY;
    public static Supplier<EntityType<Bell206Blue>>      BELL206_BLUE_ENTITY;
    public static Supplier<EntityType<Bell206Brown>>     BELL206_BROWN_ENTITY;
    public static Supplier<EntityType<Bell206Gray>>      BELL206_GRAY_ENTITY;
    public static Supplier<EntityType<Bell206Green>>     BELL206_GREEN_ENTITY;
    public static Supplier<EntityType<Bell206Olive>>     BELL206_OLIVE_ENTITY;
    public static Supplier<EntityType<Bell206Orange>>    BELL206_ORANGE_ENTITY;
    public static Supplier<EntityType<Bell206Police>>    BELL206_POLICE_ENTITY;
    public static Supplier<EntityType<Bell206Red>>       BELL206_RED_ENTITY;
    public static Supplier<EntityType<Bell206Seagreen>>  BELL206_SEAGREEN_ENTITY;
    public static Supplier<EntityType<Bell206Skyblue>>   BELL206_SKYBLUE_ENTITY;
    public static Supplier<EntityType<Bell206Yellow>>    BELL206_YELLOW_ENTITY;
    public static Supplier<EntityType<Bell47g>>             BELL47G_ENTITY;
    // Bell47g color variants
    public static Supplier<EntityType<Bell47gBlack>>     BELL47G_BLACK_ENTITY;
    public static Supplier<EntityType<Bell47gBlue>>      BELL47G_BLUE_ENTITY;
    public static Supplier<EntityType<Bell47gOlive>>     BELL47G_OLIVE_ENTITY;
    public static Supplier<EntityType<Pzlp11>>             PZLP11_ENTITY;
    // Pzlp11 color variants
    public static Supplier<EntityType<Pzlp11Brown>>      PZLP11_BROWN_ENTITY;
    public static Supplier<EntityType<Pzlp11Green>>      PZLP11_GREEN_ENTITY;
    public static Supplier<EntityType<Pzlp11Tan>>        PZLP11_TAN_ENTITY;
    public static Supplier<EntityType<Pzl37Los>>             PZL37LOS_ENTITY;
    // Pzl37Los color variants
    public static Supplier<EntityType<Pzl37LosArctic>>   PZL37LOS_ARCTIC_ENTITY;
    public static Supplier<EntityType<Pzl37LosBrown>>    PZL37LOS_BROWN_ENTITY;
    public static Supplier<EntityType<Pzl37LosGreen>>    PZL37LOS_GREEN_ENTITY;
    public static Supplier<EntityType<Pzl37LosTan>>      PZL37LOS_TAN_ENTITY;
    public static Supplier<EntityType<TrimotorBlue>>             TRIMOTOR_BLUE_ENTITY;
    public static Supplier<EntityType<TrimotorBlack>>            TRIMOTOR_BLACK_ENTITY;
    public static Supplier<EntityType<TrimotorRed>>              TRIMOTOR_RED_ENTITY;
    public static Supplier<EntityType<TrimotorWhite>>            TRIMOTOR_WHITE_ENTITY;
    public static Supplier<EntityType<VulcanairRed>>             VULCANAIR_RED_ENTITY;
    public static Supplier<EntityType<VulcanairBlackred>>        VULCANAIR_BLACKRED_ENTITY;
    public static Supplier<EntityType<VulcanairBlackyellow>>     VULCANAIR_BLACKYELLOW_ENTITY;
    public static Supplier<EntityType<VulcanairBlank>>           VULCANAIR_BLANK_ENTITY;
    public static Supplier<EntityType<VulcanairBlue>>            VULCANAIR_BLUE_ENTITY;
    public static Supplier<EntityType<VulcanairBluestripe>>      VULCANAIR_BLUESTRIPE_ENTITY;
    public static Supplier<EntityType<VulcanairCow>>             VULCANAIR_COW_ENTITY;
    public static Supplier<EntityType<VulcanairGray>>            VULCANAIR_GRAY_ENTITY;
    public static Supplier<EntityType<VulcanairGreen>>           VULCANAIR_GREEN_ENTITY;
    public static Supplier<EntityType<VulcanairOrange>>          VULCANAIR_ORANGE_ENTITY;
    public static Supplier<EntityType<VulcanairPolice>>          VULCANAIR_POLICE_ENTITY;
    public static Supplier<EntityType<VulcanairRedsnail>>        VULCANAIR_REDSNAIL_ENTITY;
    public static Supplier<EntityType<VulcanairRedyellow>>       VULCANAIR_REDYELLOW_ENTITY;
    public static Supplier<EntityType<VulcanairSeagreen>>        VULCANAIR_SEAGREEN_ENTITY;
    public static Supplier<EntityType<VulcanairWhite>>           VULCANAIR_WHITE_ENTITY;
    public static Supplier<EntityType<VulcanairWinged>>          VULCANAIR_WINGED_ENTITY;
    public static Supplier<EntityType<VulcanairYellow>>          VULCANAIR_YELLOW_ENTITY;
    public static Supplier<EntityType<ComancheRed>>             COMANCHE_RED_ENTITY;
    public static Supplier<EntityType<ComancheBlue>>            COMANCHE_BLUE_ENTITY;
    public static Supplier<EntityType<ComancheBlackredstripe>>  COMANCHE_BLACKREDSTRIPE_ENTITY;
    public static Supplier<EntityType<ComancheYellow>>          COMANCHE_YELLOW_ENTITY;
    public static Supplier<EntityType<ComancheOrangebrown>>     COMANCHE_ORANGEBROWN_ENTITY;
    public static Supplier<EntityType<ComancheSeagreen>>        COMANCHE_SEAGREEN_ENTITY;
    public static Supplier<EntityType<ComancheBlank>>           COMANCHE_BLANK_ENTITY;
    public static Supplier<EntityType<SkyhawkPrRed>>        SKYHAWK_PR_RED_ENTITY;
    public static Supplier<EntityType<SkyhawkPrCoffee>>     SKYHAWK_PR_COFFEE_ENTITY;
    public static Supplier<EntityType<SkyhawkPrBlueStripe>> SKYHAWK_PR_BLUESTRIPE_ENTITY;
    public static Supplier<EntityType<SkyhawkPrGreen>> SKYHAWK_PR_GREEN_ENTITY;
    public static Supplier<EntityType<SkyhawkRed>>          SKYHAWK_RED_ENTITY;
    public static Supplier<EntityType<SkyhawkCoffee>>       SKYHAWK_COFFEE_ENTITY;
    public static Supplier<EntityType<SkyhawkBlueStripe>>   SKYHAWK_BLUESTRIPE_ENTITY;
    public static Supplier<EntityType<SkyhawkGreen>>   SKYHAWK_GREEN_ENTITY;

    public static Supplier<EntityType<SkyhawkBlackorange>>   SKYHAWK_BLACKORANGE_ENTITY;
    public static Supplier<EntityType<SkyhawkPrBlackorange>> SKYHAWK_PR_BLACKORANGE_ENTITY;
    public static Supplier<EntityType<SkyhawkBlackred>>      SKYHAWK_BLACKRED_ENTITY;
    public static Supplier<EntityType<SkyhawkPrBlackred>>    SKYHAWK_PR_BLACKRED_ENTITY;
    public static Supplier<EntityType<SkyhawkBlackyellow>>   SKYHAWK_BLACKYELLOW_ENTITY;
    public static Supplier<EntityType<SkyhawkPrBlackyellow>> SKYHAWK_PR_BLACKYELLOW_ENTITY;
    public static Supplier<EntityType<SkyhawkBlank>>         SKYHAWK_BLANK_ENTITY;
    public static Supplier<EntityType<SkyhawkPrBlank>>       SKYHAWK_PR_BLANK_ENTITY;
    public static Supplier<EntityType<SkyhawkBlue>>          SKYHAWK_BLUE_ENTITY;
    public static Supplier<EntityType<SkyhawkPrBlue>>        SKYHAWK_PR_BLUE_ENTITY;
    public static Supplier<EntityType<SkyhawkBluered>>       SKYHAWK_BLUERED_ENTITY;
    public static Supplier<EntityType<SkyhawkPrBluered>>     SKYHAWK_PR_BLUERED_ENTITY;
    public static Supplier<EntityType<SkyhawkBrown>>         SKYHAWK_BROWN_ENTITY;
    public static Supplier<EntityType<SkyhawkPrBrown>>       SKYHAWK_PR_BROWN_ENTITY;
    public static Supplier<EntityType<SkyhawkButter>>        SKYHAWK_BUTTER_ENTITY;
    public static Supplier<EntityType<SkyhawkPrButter>>      SKYHAWK_PR_BUTTER_ENTITY;
    public static Supplier<EntityType<SkyhawkRed2>>          SKYHAWK_RED2_ENTITY;
    public static Supplier<EntityType<SkyhawkPrRed2>>        SKYHAWK_PR_RED2_ENTITY;

    public static Supplier<EntityType<BasicBombEntity>> BASIC_BOMB;


    public static void init() {

        SkyhawkEngineSounds.register();
        BristolMercurySounds.register();
        Allison250Sounds.register();
        Franklin0335Sounds.register();
        Pw610fSounds.register();
        DoorSounds.register();

        // ---- 機体アイテム登録 ----------------------------

        GUN_M1919 = register("gunm1919", () ->
                new WeaponItem(baseProps().stacksTo(1), WeaponMount.Type.FRONT));

        GUN_OBSERVER = register("gunobserver", () ->
                new WeaponItem(baseProps().stacksTo(1), WeaponMount.Type.FRONT));

        BASIC_BOMB_BAY = register("basicbomb_hardpoint", () ->
                new WeaponItem(baseProps().stacksTo(1), WeaponMount.Type.DROP));

        E500_RED_ITEM = register("e500_red", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new E500Red(E500_RED_ENTITY.get(), world)));
        E500_BLACKRED_ITEM = register("e500_blackred", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new E500Blackred(E500_BLACKRED_ENTITY.get(), world)));
        E500_BLUE_ITEM = register("e500_blue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new E500Blue(E500_BLUE_ENTITY.get(), world)));
        E500_EXTRAVAGANT_ITEM = register("e500_extravagant", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new E500Extravagant(E500_EXTRAVAGANT_ENTITY.get(), world)));
        E500_GREEN_ITEM = register("e500_green", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new E500Green(E500_GREEN_ENTITY.get(), world)));
        E500_RUSTY_ITEM = register("e500_rusty", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new E500Rusty(E500_RUSTY_ENTITY.get(), world)));
        E500_SILVER_ITEM = register("e500_silver", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new E500Silver(E500_SILVER_ENTITY.get(), world)));
        E500_YELLOW_ITEM = register("e500_yellow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new E500Yellow(E500_YELLOW_ENTITY.get(), world)));
        BELL206_BLACKSTRIPE_ITEM = register("bell206_blackstripe", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Blackstripe(BELL206_BLACKSTRIPE_ENTITY.get(), world)));
        BELL206_BLACK_ITEM = register("bell206_black", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Black(BELL206_BLACK_ENTITY.get(), world)));
        BELL206_BLANK_ITEM = register("bell206_blank", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Blank(BELL206_BLANK_ENTITY.get(), world)));
        BELL206_BLUE_ITEM = register("bell206_blue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Blue(BELL206_BLUE_ENTITY.get(), world)));
        BELL206_BROWN_ITEM = register("bell206_brown", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Brown(BELL206_BROWN_ENTITY.get(), world)));
        BELL206_GRAY_ITEM = register("bell206_gray", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Gray(BELL206_GRAY_ENTITY.get(), world)));
        BELL206_GREEN_ITEM = register("bell206_green", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Green(BELL206_GREEN_ENTITY.get(), world)));
        BELL206_OLIVE_ITEM = register("bell206_olive", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Olive(BELL206_OLIVE_ENTITY.get(), world)));
        BELL206_ORANGE_ITEM = register("bell206_orange", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Orange(BELL206_ORANGE_ENTITY.get(), world)));
        BELL206_POLICE_ITEM = register("bell206_police", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Police(BELL206_POLICE_ENTITY.get(), world)));
        BELL206_RED_ITEM = register("bell206_red", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Red(BELL206_RED_ENTITY.get(), world)));
        BELL206_SEAGREEN_ITEM = register("bell206_seagreen", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Seagreen(BELL206_SEAGREEN_ENTITY.get(), world)));
        BELL206_SKYBLUE_ITEM = register("bell206_skyblue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Skyblue(BELL206_SKYBLUE_ENTITY.get(), world)));
        BELL206_YELLOW_ITEM = register("bell206_yellow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell206Yellow(BELL206_YELLOW_ENTITY.get(), world)));
        BELL47G_ITEM = register("bell47g", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell47g(BELL47G_ENTITY.get(), world)));
        BELL47G_BLACK_ITEM = register("bell47g_black", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell47gBlack(BELL47G_BLACK_ENTITY.get(), world)));
        BELL47G_BLUE_ITEM = register("bell47g_blue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell47gBlue(BELL47G_BLUE_ENTITY.get(), world)));
        BELL47G_OLIVE_ITEM = register("bell47g_olive", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Bell47gOlive(BELL47G_OLIVE_ENTITY.get(), world)));
        PZLP11_ITEM = register("pzlp11", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzl37Los(PZLP11_ENTITY.get(), world)));
        PZLP11_BROWN_ITEM = register("pzlp11_brown", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzlp11Brown(PZLP11_BROWN_ENTITY.get(), world)));
        PZLP11_GREEN_ITEM = register("pzlp11_green", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzlp11Green(PZLP11_GREEN_ENTITY.get(), world)));
        PZLP11_TAN_ITEM = register("pzlp11_tan", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzlp11Tan(PZLP11_TAN_ENTITY.get(), world)));
        PZL37LOS_ITEM = register("pzl37los", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzl37Los(PZL37LOS_ENTITY.get(), world)));
        PZL37LOS_ARCTIC_ITEM = register("pzl37los_arctic", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzl37LosArctic(PZL37LOS_ARCTIC_ENTITY.get(), world)));
        PZL37LOS_BROWN_ITEM = register("pzl37los_brown", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzl37LosBrown(PZL37LOS_BROWN_ENTITY.get(), world)));
        PZL37LOS_GREEN_ITEM = register("pzl37los_green", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzl37LosGreen(PZL37LOS_GREEN_ENTITY.get(), world)));
        PZL37LOS_TAN_ITEM = register("pzl37los_tan", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new Pzl37LosTan(PZL37LOS_TAN_ENTITY.get(), world)));
        TRIMOTOR_BLUE_ITEM = register("trimotor_blue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new TrimotorBlue(TRIMOTOR_BLUE_ENTITY.get(), world)));
        VULCANAIR_RED_ITEM = register("vulcanair_red", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairRed(VULCANAIR_RED_ENTITY.get(), world)));
        VULCANAIR_BLACKRED_ITEM = register("vulcanair_blackred", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairBlackred(VULCANAIR_BLACKRED_ENTITY.get(), world)));
        VULCANAIR_BLACKYELLOW_ITEM = register("vulcanair_blackyellow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairBlackyellow(VULCANAIR_BLACKYELLOW_ENTITY.get(), world)));
        VULCANAIR_BLANK_ITEM = register("vulcanair_blank", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairBlank(VULCANAIR_BLANK_ENTITY.get(), world)));
        VULCANAIR_BLUE_ITEM = register("vulcanair_blue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairBlue(VULCANAIR_BLUE_ENTITY.get(), world)));
        VULCANAIR_BLUESTRIPE_ITEM = register("vulcanair_bluestripe", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairBluestripe(VULCANAIR_BLUESTRIPE_ENTITY.get(), world)));
        VULCANAIR_COW_ITEM = register("vulcanair_cow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairCow(VULCANAIR_COW_ENTITY.get(), world)));
        VULCANAIR_GRAY_ITEM = register("vulcanair_gray", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairGray(VULCANAIR_GRAY_ENTITY.get(), world)));
        VULCANAIR_GREEN_ITEM = register("vulcanair_green", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairGreen(VULCANAIR_GREEN_ENTITY.get(), world)));
        VULCANAIR_ORANGE_ITEM = register("vulcanair_orange", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairOrange(VULCANAIR_ORANGE_ENTITY.get(), world)));
        VULCANAIR_POLICE_ITEM = register("vulcanair_police", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairPolice(VULCANAIR_POLICE_ENTITY.get(), world)));
        VULCANAIR_REDSNAIL_ITEM = register("vulcanair_redsnail", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairRedsnail(VULCANAIR_REDSNAIL_ENTITY.get(), world)));
        VULCANAIR_REDYELLOW_ITEM = register("vulcanair_redyellow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairRedyellow(VULCANAIR_REDYELLOW_ENTITY.get(), world)));
        VULCANAIR_SEAGREEN_ITEM = register("vulcanair_seagreen", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairSeagreen(VULCANAIR_SEAGREEN_ENTITY.get(), world)));
        VULCANAIR_WHITE_ITEM = register("vulcanair_white", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairWhite(VULCANAIR_WHITE_ENTITY.get(), world)));
        VULCANAIR_WINGED_ITEM = register("vulcanair_winged", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairWinged(VULCANAIR_WINGED_ENTITY.get(), world)));
        VULCANAIR_YELLOW_ITEM = register("vulcanair_yellow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new VulcanairYellow(VULCANAIR_YELLOW_ENTITY.get(), world)));
        TRIMOTOR_BLACK_ITEM = register("trimotor_black", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new TrimotorBlack(TRIMOTOR_BLACK_ENTITY.get(), world)));
        TRIMOTOR_RED_ITEM = register("trimotor_red", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new TrimotorRed(TRIMOTOR_RED_ENTITY.get(), world)));
        TRIMOTOR_WHITE_ITEM = register("trimotor_white", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new TrimotorWhite(TRIMOTOR_WHITE_ENTITY.get(), world)));
        COMANCHE_RED_ITEM = register("comanche_red", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new ComancheRed(COMANCHE_RED_ENTITY.get(), world)));
        COMANCHE_BLUE_ITEM = register("comanche_blue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new ComancheBlue(COMANCHE_BLUE_ENTITY.get(), world)));
        COMANCHE_BLACKREDSTRIPE_ITEM = register("comanche_blackredstripe", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new ComancheBlackredstripe(COMANCHE_BLACKREDSTRIPE_ENTITY.get(), world)));
        COMANCHE_YELLOW_ITEM = register("comanche_yellow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new ComancheYellow(COMANCHE_YELLOW_ENTITY.get(), world)));
        COMANCHE_ORANGEBROWN_ITEM = register("comanche_orangebrown", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new ComancheOrangebrown(COMANCHE_ORANGEBROWN_ENTITY.get(), world)));
        COMANCHE_SEAGREEN_ITEM = register("comanche_seagreen", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new ComancheSeagreen(COMANCHE_SEAGREEN_ENTITY.get(), world)));
        COMANCHE_BLANK_ITEM = register("comanche_blank", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new ComancheBlank(COMANCHE_BLANK_ENTITY.get(), world)));
        SKYHAWK_PR_RED_ITEM = register("skyhawk_pr_red", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrRed(SKYHAWK_PR_RED_ENTITY.get(), world)));
        SKYHAWK_PR_COFFEE_ITEM = register("skyhawk_pr_coffee", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrCoffee(SKYHAWK_PR_COFFEE_ENTITY.get(), world)));
        SKYHAWK_PR_BLUESTRIPE_ITEM = register("skyhawk_pr_bluestripe", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBlueStripe(SKYHAWK_PR_BLUESTRIPE_ENTITY.get(), world)));
        SKYHAWK_PR_GREEN_ITEM = register("skyhawk_pr_green", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBlueStripe(SKYHAWK_PR_GREEN_ENTITY.get(), world)));
        SKYHAWK_RED_ITEM = register("skyhawk_red", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkRed(SKYHAWK_RED_ENTITY.get(), world)));
        SKYHAWK_COFFEE_ITEM = register("skyhawk_coffee", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkCoffee(SKYHAWK_COFFEE_ENTITY.get(), world)));
        SKYHAWK_BLUESTRIPE_ITEM = register("skyhawk_bluestripe", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkBlueStripe(SKYHAWK_BLUESTRIPE_ENTITY.get(), world)));
        SKYHAWK_GREEN_ITEM = register("skyhawk_green", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkGreen(SKYHAWK_GREEN_ENTITY.get(), world)));

        // ---- ペイントスプレー登録 -----------------------
        PAINT_SPRAY_ITEM = register("paint_spray", () -> new PaintSprayItem(baseProps().stacksTo(1)));

        // ---- 新規カラー アイテム登録 --------------------
        SKYHAWK_BLACKORANGE_ITEM = register("skyhawk_blackorange", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkBlackorange(SKYHAWK_BLACKORANGE_ENTITY.get(), world)));
        SKYHAWK_PR_BLACKORANGE_ITEM = register("skyhawk_pr_blackorange", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBlackorange(SKYHAWK_PR_BLACKORANGE_ENTITY.get(), world)));
        SKYHAWK_BLACKRED_ITEM = register("skyhawk_blackred", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkBlackred(SKYHAWK_BLACKRED_ENTITY.get(), world)));
        SKYHAWK_PR_BLACKRED_ITEM = register("skyhawk_pr_blackred", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBlackred(SKYHAWK_PR_BLACKRED_ENTITY.get(), world)));
        SKYHAWK_BLACKYELLOW_ITEM = register("skyhawk_blackyellow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkBlackyellow(SKYHAWK_BLACKYELLOW_ENTITY.get(), world)));
        SKYHAWK_PR_BLACKYELLOW_ITEM = register("skyhawk_pr_blackyellow", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBlackyellow(SKYHAWK_PR_BLACKYELLOW_ENTITY.get(), world)));
        SKYHAWK_BLANK_ITEM = register("skyhawk_blank", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkBlank(SKYHAWK_BLANK_ENTITY.get(), world)));
        SKYHAWK_PR_BLANK_ITEM = register("skyhawk_pr_blank", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBlank(SKYHAWK_PR_BLANK_ENTITY.get(), world)));
        SKYHAWK_BLUE_ITEM = register("skyhawk_blue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkBlue(SKYHAWK_BLUE_ENTITY.get(), world)));
        SKYHAWK_PR_BLUE_ITEM = register("skyhawk_pr_blue", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBlue(SKYHAWK_PR_BLUE_ENTITY.get(), world)));
        SKYHAWK_BLUERED_ITEM = register("skyhawk_bluered", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkBluered(SKYHAWK_BLUERED_ENTITY.get(), world)));
        SKYHAWK_PR_BLUERED_ITEM = register("skyhawk_pr_bluered", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBluered(SKYHAWK_PR_BLUERED_ENTITY.get(), world)));
        SKYHAWK_BROWN_ITEM = register("skyhawk_brown", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkBrown(SKYHAWK_BROWN_ENTITY.get(), world)));
        SKYHAWK_PR_BROWN_ITEM = register("skyhawk_pr_brown", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrBrown(SKYHAWK_PR_BROWN_ENTITY.get(), world)));
        SKYHAWK_BUTTER_ITEM = register("skyhawk_butter", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkButter(SKYHAWK_BUTTER_ENTITY.get(), world)));
        SKYHAWK_PR_BUTTER_ITEM = register("skyhawk_pr_butter", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrButter(SKYHAWK_PR_BUTTER_ENTITY.get(), world)));
        SKYHAWK_RED2_ITEM = register("skyhawk_red2", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkRed2(SKYHAWK_RED2_ENTITY.get(), world)));
        SKYHAWK_PR_RED2_ITEM = register("skyhawk_pr_red2", () ->
                new DyeableAircraftItem(baseProps().stacksTo(1),
                        world -> new SkyhawkPrRed2(SKYHAWK_PR_RED2_ENTITY.get(), world)));


        // ---- 機体エンティティタイプ登録 ------------------
        E500_RED_ENTITY = register("e500_red", EntityType.Builder
                .of(E500Red::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        E500_BLACKRED_ENTITY = register("e500_blackred", EntityType.Builder
                .of(E500Blackred::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        E500_BLUE_ENTITY = register("e500_blue", EntityType.Builder
                .of(E500Blue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        E500_EXTRAVAGANT_ENTITY = register("e500_extravagant", EntityType.Builder
                .of(E500Extravagant::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        E500_GREEN_ENTITY = register("e500_green", EntityType.Builder
                .of(E500Green::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        E500_RUSTY_ENTITY = register("e500_rusty", EntityType.Builder
                .of(E500Rusty::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        E500_SILVER_ENTITY = register("e500_silver", EntityType.Builder
                .of(E500Silver::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        E500_YELLOW_ENTITY = register("e500_yellow", EntityType.Builder
                .of(E500Yellow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_BLACKSTRIPE_ENTITY = register("bell206_blackstripe", EntityType.Builder
                .of(Bell206Blackstripe::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_BLACK_ENTITY = register("bell206_black", EntityType.Builder
                .of(Bell206Black::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_BLANK_ENTITY = register("bell206_blank", EntityType.Builder
                .of(Bell206Blank::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_BLUE_ENTITY = register("bell206_blue", EntityType.Builder
                .of(Bell206Blue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_BROWN_ENTITY = register("bell206_brown", EntityType.Builder
                .of(Bell206Brown::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_GRAY_ENTITY = register("bell206_gray", EntityType.Builder
                .of(Bell206Gray::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_GREEN_ENTITY = register("bell206_green", EntityType.Builder
                .of(Bell206Green::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_OLIVE_ENTITY = register("bell206_olive", EntityType.Builder
                .of(Bell206Olive::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_ORANGE_ENTITY = register("bell206_orange", EntityType.Builder
                .of(Bell206Orange::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_POLICE_ENTITY = register("bell206_police", EntityType.Builder
                .of(Bell206Police::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_RED_ENTITY = register("bell206_red", EntityType.Builder
                .of(Bell206Red::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_SEAGREEN_ENTITY = register("bell206_seagreen", EntityType.Builder
                .of(Bell206Seagreen::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_SKYBLUE_ENTITY = register("bell206_skyblue", EntityType.Builder
                .of(Bell206Skyblue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL206_YELLOW_ENTITY = register("bell206_yellow", EntityType.Builder
                .of(Bell206Yellow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL47G_ENTITY = register("bell47g", EntityType.Builder
                .of(Bell47g::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL47G_BLACK_ENTITY = register("bell47g_black", EntityType.Builder
                .of(Bell47gBlack::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL47G_BLUE_ENTITY = register("bell47g_blue", EntityType.Builder
                .of(Bell47gBlue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        BELL47G_OLIVE_ENTITY = register("bell47g_olive", EntityType.Builder
                .of(Bell47gOlive::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZLP11_ENTITY = register("pzlp11", EntityType.Builder
                .of(Pzlp11::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZLP11_BROWN_ENTITY = register("pzlp11_brown", EntityType.Builder
                .of(Pzlp11Brown::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZLP11_GREEN_ENTITY = register("pzlp11_green", EntityType.Builder
                .of(Pzlp11Green::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZLP11_TAN_ENTITY = register("pzlp11_tan", EntityType.Builder
                .of(Pzlp11Tan::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZL37LOS_ENTITY = register("pzl37los", EntityType.Builder
                .of(Pzl37Los::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZL37LOS_ARCTIC_ENTITY = register("pzl37los_arctic", EntityType.Builder
                .of(Pzl37LosArctic::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZL37LOS_BROWN_ENTITY = register("pzl37los_brown", EntityType.Builder
                .of(Pzl37LosBrown::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZL37LOS_GREEN_ENTITY = register("pzl37los_green", EntityType.Builder
                .of(Pzl37LosGreen::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        PZL37LOS_TAN_ENTITY = register("pzl37los_tan", EntityType.Builder
                .of(Pzl37LosTan::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        TRIMOTOR_BLUE_ENTITY = register("trimotor_blue", EntityType.Builder
                .of(TrimotorBlue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_RED_ENTITY = register("vulcanair_red", EntityType.Builder
                .of(VulcanairRed::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_BLACKRED_ENTITY = register("vulcanair_blackred", EntityType.Builder
                .of(VulcanairBlackred::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_BLACKYELLOW_ENTITY = register("vulcanair_blackyellow", EntityType.Builder
                .of(VulcanairBlackyellow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_BLANK_ENTITY = register("vulcanair_blank", EntityType.Builder
                .of(VulcanairBlank::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_BLUE_ENTITY = register("vulcanair_blue", EntityType.Builder
                .of(VulcanairBlue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_BLUESTRIPE_ENTITY = register("vulcanair_bluestripe", EntityType.Builder
                .of(VulcanairBluestripe::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_COW_ENTITY = register("vulcanair_cow", EntityType.Builder
                .of(VulcanairCow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_GRAY_ENTITY = register("vulcanair_gray", EntityType.Builder
                .of(VulcanairGray::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_GREEN_ENTITY = register("vulcanair_green", EntityType.Builder
                .of(VulcanairGreen::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_ORANGE_ENTITY = register("vulcanair_orange", EntityType.Builder
                .of(VulcanairOrange::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_POLICE_ENTITY = register("vulcanair_police", EntityType.Builder
                .of(VulcanairPolice::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_REDSNAIL_ENTITY = register("vulcanair_redsnail", EntityType.Builder
                .of(VulcanairRedsnail::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_REDYELLOW_ENTITY = register("vulcanair_redyellow", EntityType.Builder
                .of(VulcanairRedyellow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_SEAGREEN_ENTITY = register("vulcanair_seagreen", EntityType.Builder
                .of(VulcanairSeagreen::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_WHITE_ENTITY = register("vulcanair_white", EntityType.Builder
                .of(VulcanairWhite::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_WINGED_ENTITY = register("vulcanair_winged", EntityType.Builder
                .of(VulcanairWinged::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        VULCANAIR_YELLOW_ENTITY = register("vulcanair_yellow", EntityType.Builder
                .of(VulcanairYellow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        TRIMOTOR_BLACK_ENTITY = register("trimotor_black", EntityType.Builder
                .of(TrimotorBlack::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        TRIMOTOR_RED_ENTITY = register("trimotor_red", EntityType.Builder
                .of(TrimotorRed::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        TRIMOTOR_WHITE_ENTITY = register("trimotor_white", EntityType.Builder
                .of(TrimotorWhite::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        COMANCHE_RED_ENTITY = register("comanche_red", EntityType.Builder
                .of(ComancheRed::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        COMANCHE_BLUE_ENTITY = register("comanche_blue", EntityType.Builder
                .of(ComancheBlue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        COMANCHE_BLACKREDSTRIPE_ENTITY = register("comanche_blackredstripe", EntityType.Builder
                .of(ComancheBlackredstripe::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        COMANCHE_YELLOW_ENTITY = register("comanche_yellow", EntityType.Builder
                .of(ComancheYellow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        COMANCHE_ORANGEBROWN_ENTITY = register("comanche_orangebrown", EntityType.Builder
                .of(ComancheOrangebrown::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        COMANCHE_SEAGREEN_ENTITY = register("comanche_seagreen", EntityType.Builder
                .of(ComancheSeagreen::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        COMANCHE_BLANK_ENTITY = register("comanche_blank", EntityType.Builder
                .of(ComancheBlank::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_RED_ENTITY = register("skyhawk_pr_red", EntityType.Builder
                .of(SkyhawkPrRed::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_COFFEE_ENTITY = register("skyhawk_pr_coffee", EntityType.Builder
                .of(SkyhawkPrCoffee::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BLUESTRIPE_ENTITY = register("skyhawk_pr_bluestripe", EntityType.Builder
                .of(SkyhawkPrBlueStripe::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_GREEN_ENTITY = register("skyhawk_pr_green", EntityType.Builder
                .of(SkyhawkPrGreen::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_RED_ENTITY = register("skyhawk_red", EntityType.Builder
                .of(SkyhawkRed::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_COFFEE_ENTITY = register("skyhawk_coffee", EntityType.Builder
                .of(SkyhawkCoffee::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_BLUESTRIPE_ENTITY = register("skyhawk_bluestripe", EntityType.Builder
                .of(SkyhawkBlueStripe::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_GREEN_ENTITY = register("skyhawk_green", EntityType.Builder
                .of(SkyhawkGreen::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());

        // ---- 新規カラー エンティティタイプ登録 ----------
        SKYHAWK_BLACKORANGE_ENTITY = register("skyhawk_blackorange", EntityType.Builder
                .of(SkyhawkBlackorange::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BLACKORANGE_ENTITY = register("skyhawk_pr_blackorange", EntityType.Builder
                .of(SkyhawkPrBlackorange::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_BLACKRED_ENTITY = register("skyhawk_blackred", EntityType.Builder
                .of(SkyhawkBlackred::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BLACKRED_ENTITY = register("skyhawk_pr_blackred", EntityType.Builder
                .of(SkyhawkPrBlackred::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_BLACKYELLOW_ENTITY = register("skyhawk_blackyellow", EntityType.Builder
                .of(SkyhawkBlackyellow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BLACKYELLOW_ENTITY = register("skyhawk_pr_blackyellow", EntityType.Builder
                .of(SkyhawkPrBlackyellow::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_BLANK_ENTITY = register("skyhawk_blank", EntityType.Builder
                .of(SkyhawkBlank::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BLANK_ENTITY = register("skyhawk_pr_blank", EntityType.Builder
                .of(SkyhawkPrBlank::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_BLUE_ENTITY = register("skyhawk_blue", EntityType.Builder
                .of(SkyhawkBlue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BLUE_ENTITY = register("skyhawk_pr_blue", EntityType.Builder
                .of(SkyhawkPrBlue::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_BLUERED_ENTITY = register("skyhawk_bluered", EntityType.Builder
                .of(SkyhawkBluered::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BLUERED_ENTITY = register("skyhawk_pr_bluered", EntityType.Builder
                .of(SkyhawkPrBluered::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_BROWN_ENTITY = register("skyhawk_brown", EntityType.Builder
                .of(SkyhawkBrown::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BROWN_ENTITY = register("skyhawk_pr_brown", EntityType.Builder
                .of(SkyhawkPrBrown::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_BUTTER_ENTITY = register("skyhawk_butter", EntityType.Builder
                .of(SkyhawkButter::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_BUTTER_ENTITY = register("skyhawk_pr_butter", EntityType.Builder
                .of(SkyhawkPrButter::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_RED2_ENTITY = register("skyhawk_red2", EntityType.Builder
                .of(SkyhawkRed2::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());
        SKYHAWK_PR_RED2_ENTITY = register("skyhawk_pr_red2", EntityType.Builder
                .of(SkyhawkPrRed2::new, MobCategory.MISC)
                .sized(1.0f, 1.0f).clientTrackingRange(12).fireImmune());



        BASIC_BOMB = register("basicbomb", EntityType.Builder
                .of(BasicBombEntity::new, MobCategory.MISC)
                .sized(0.25f, 0.25f)
                .clientTrackingRange(10)
                .updateInterval(10)
                .fireImmune()
        );

        // ---- バリアントグループ登録 ----------------------
        // 新しい色を追加するときは対応するグループに1行追加するだけ。
        // 新しい機体フレームを追加するときは registerGroup() を新規追加する。

        // Bell206 グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("bell206_blackstripe",
                        "variant.civilian_aviation.bell206_blackstripe",
                        0x111111, () -> BELL206_BLACKSTRIPE_ITEM.get()),
                new AircraftVariant("bell206_black",
                        "variant.civilian_aviation.bell206_black",
                        0x222222, () -> BELL206_BLACK_ITEM.get()),
                new AircraftVariant("bell206_blank",
                        "variant.civilian_aviation.bell206_blank",
                        0xF0F0F0, () -> BELL206_BLANK_ITEM.get()),
                new AircraftVariant("bell206_blue",
                        "variant.civilian_aviation.bell206_blue",
                        0x2255CC, () -> BELL206_BLUE_ITEM.get()),
                new AircraftVariant("bell206_brown",
                        "variant.civilian_aviation.bell206_brown",
                        0x7B4F2E, () -> BELL206_BROWN_ITEM.get()),
                new AircraftVariant("bell206_gray",
                        "variant.civilian_aviation.bell206_gray",
                        0x888888, () -> BELL206_GRAY_ITEM.get()),
                new AircraftVariant("bell206_green",
                        "variant.civilian_aviation.bell206_green",
                        0x5A962E, () -> BELL206_GREEN_ITEM.get()),
                new AircraftVariant("bell206_olive",
                        "variant.civilian_aviation.bell206_olive",
                        0x6B6B35, () -> BELL206_OLIVE_ITEM.get()),
                new AircraftVariant("bell206_orange",
                        "variant.civilian_aviation.bell206_orange",
                        0xFF6600, () -> BELL206_ORANGE_ITEM.get()),
                new AircraftVariant("bell206_police",
                        "variant.civilian_aviation.bell206_police",
                        0x003399, () -> BELL206_POLICE_ITEM.get()),
                new AircraftVariant("bell206_red",
                        "variant.civilian_aviation.bell206_red",
                        0xCC3333, () -> BELL206_RED_ITEM.get()),
                new AircraftVariant("bell206_seagreen",
                        "variant.civilian_aviation.bell206_seagreen",
                        0x2E8B57, () -> BELL206_SEAGREEN_ITEM.get()),
                new AircraftVariant("bell206_skyblue",
                        "variant.civilian_aviation.bell206_skyblue",
                        0x87CEEB, () -> BELL206_SKYBLUE_ITEM.get()),
                new AircraftVariant("bell206_yellow",
                        "variant.civilian_aviation.bell206_yellow",
                        0xFFDD00, () -> BELL206_YELLOW_ITEM.get())
        ));

        // Bell47g グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("bell47g",
                        "variant.civilian_aviation.bell47g",
                        0xF0F0F0, () -> BELL47G_ITEM.get()),
                new AircraftVariant("bell47g_black",
                        "variant.civilian_aviation.bell47g_black",
                        0x222222, () -> BELL47G_BLACK_ITEM.get()),
                new AircraftVariant("bell47g_blue",
                        "variant.civilian_aviation.bell47g_blue",
                        0x2255CC, () -> BELL47G_BLUE_ITEM.get()),
                new AircraftVariant("bell47g_olive",
                        "variant.civilian_aviation.bell47g_olive",
                        0x6B6B35, () -> BELL47G_OLIVE_ITEM.get())
        ));

        // E500 グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("e500_red",
                        "variant.civilian_aviation.e500_red",
                        0xCC3333, () -> E500_RED_ITEM.get()),
                new AircraftVariant("e500_blackred",
                        "variant.civilian_aviation.e500_blackred",
                        0xCC0000, () -> E500_BLACKRED_ITEM.get()),
                new AircraftVariant("e500_blue",
                        "variant.civilian_aviation.e500_blue",
                        0x2255CC, () -> E500_BLUE_ITEM.get()),
                new AircraftVariant("e500_extravagant",
                        "variant.civilian_aviation.e500_extravagant",
                        0xCC00CC, () -> E500_EXTRAVAGANT_ITEM.get()),
                new AircraftVariant("e500_green",
                        "variant.civilian_aviation.e500_green",
                        0x5A962E, () -> E500_GREEN_ITEM.get()),
                new AircraftVariant("e500_rusty",
                        "variant.civilian_aviation.e500_rusty",
                        0x8B4513, () -> E500_RUSTY_ITEM.get()),
                new AircraftVariant("e500_silver",
                        "variant.civilian_aviation.e500_silver",
                        0xC0C0C0, () -> E500_SILVER_ITEM.get()),
                new AircraftVariant("e500_yellow",
                        "variant.civilian_aviation.e500_yellow",
                        0xFFDD00, () -> E500_YELLOW_ITEM.get())
        ));

        // Pzlp11 グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("pzlp11",
                        "variant.civilian_aviation.pzlp11",
                        0xF0F0F0, () -> PZLP11_ITEM.get()),
                new AircraftVariant("pzlp11_brown",
                        "variant.civilian_aviation.pzlp11_brown",
                        0x7B4F2E, () -> PZLP11_BROWN_ITEM.get()),
                new AircraftVariant("pzlp11_green",
                        "variant.civilian_aviation.pzlp11_green",
                        0x5A962E, () -> PZLP11_GREEN_ITEM.get()),
                new AircraftVariant("pzlp11_tan",
                        "variant.civilian_aviation.pzlp11_tan",
                        0xD2B48C, () -> PZLP11_TAN_ITEM.get())
        ));

        // Pzl37Los グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("pzl37los",
                        "variant.civilian_aviation.pzl37los",
                        0x4A5A4A, () -> PZL37LOS_ITEM.get()),
                new AircraftVariant("pzl37los_arctic",
                        "variant.civilian_aviation.pzl37los_arctic",
                        0xE8EEF0, () -> PZL37LOS_ARCTIC_ITEM.get()),
                new AircraftVariant("pzl37los_brown",
                        "variant.civilian_aviation.pzl37los_brown",
                        0x7B4F2E, () -> PZL37LOS_BROWN_ITEM.get()),
                new AircraftVariant("pzl37los_green",
                        "variant.civilian_aviation.pzl37los_green",
                        0x5A962E, () -> PZL37LOS_GREEN_ITEM.get()),
                new AircraftVariant("pzl37los_tan",
                        "variant.civilian_aviation.pzl37los_tan",
                        0xD2B48C, () -> PZL37LOS_TAN_ITEM.get())
        ));

        // Comanche グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("comanche_red",
                        "variant.civilian_aviation.comanche_red",
                        0xCC3333, () -> COMANCHE_RED_ITEM.get()),
                new AircraftVariant("comanche_blue",
                        "variant.civilian_aviation.comanche_blue",
                        0x2255CC, () -> COMANCHE_BLUE_ITEM.get()),
                new AircraftVariant("comanche_blackredstripe",
                        "variant.civilian_aviation.comanche_blackredstripe",
                        0xAA0000, () -> COMANCHE_BLACKREDSTRIPE_ITEM.get()),
                new AircraftVariant("comanche_yellow",
                        "variant.civilian_aviation.comanche_yellow",
                        0xFFDD00, () -> COMANCHE_YELLOW_ITEM.get()),
                new AircraftVariant("comanche_orangebrown",
                        "variant.civilian_aviation.comanche_orangebrown",
                        0xC46200, () -> COMANCHE_ORANGEBROWN_ITEM.get()),
                new AircraftVariant("comanche_seagreen",
                        "variant.civilian_aviation.comanche_seagreen",
                        0x2E8B57, () -> COMANCHE_SEAGREEN_ITEM.get()),
                new AircraftVariant("comanche_blank",
                        "variant.civilian_aviation.comanche_blank",
                        0xFFFFFF, () -> COMANCHE_BLANK_ITEM.get())
        ));

        // Skyhawk Pr グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("skyhawk_pr_red",
                        "variant.civilian_aviation.skyhawk_pr_red",
                        0xCC3333, () -> SKYHAWK_PR_RED_ITEM.get()),
                new AircraftVariant("skyhawk_pr_coffee",
                        "variant.civilian_aviation.skyhawk_pr_coffee",
                        0x8B5E3C, () -> SKYHAWK_PR_COFFEE_ITEM.get()),
                new AircraftVariant("skyhawk_pr_bluestripe",
                        "variant.civilian_aviation.skyhawk_pr_bluestripe",
                        0x3355AA, () -> SKYHAWK_PR_BLUESTRIPE_ITEM.get()),
                new AircraftVariant("skyhawk_pr_green",
                        "variant.civilian_aviation.skyhawk_pr_green",
                        0x5A962E, () -> SKYHAWK_PR_GREEN_ITEM.get()),
                new AircraftVariant("skyhawk_pr_blackorange",
                        "variant.civilian_aviation.skyhawk_pr_blackorange",
                        0xFF6600, () -> SKYHAWK_PR_BLACKORANGE_ITEM.get()),
                new AircraftVariant("skyhawk_pr_blackred",
                        "variant.civilian_aviation.skyhawk_pr_blackred",
                        0xCC0000, () -> SKYHAWK_PR_BLACKRED_ITEM.get()),
                new AircraftVariant("skyhawk_pr_blackyellow",
                        "variant.civilian_aviation.skyhawk_pr_blackyellow",
                        0xFFDD00, () -> SKYHAWK_PR_BLACKYELLOW_ITEM.get()),
                new AircraftVariant("skyhawk_pr_blank",
                        "variant.civilian_aviation.skyhawk_pr_blank",
                        0xF0F0F0, () -> SKYHAWK_PR_BLANK_ITEM.get()),
                new AircraftVariant("skyhawk_pr_blue",
                        "variant.civilian_aviation.skyhawk_pr_blue",
                        0x2255CC, () -> SKYHAWK_PR_BLUE_ITEM.get()),
                new AircraftVariant("skyhawk_pr_bluered",
                        "variant.civilian_aviation.skyhawk_pr_bluered",
                        0xAA1122, () -> SKYHAWK_PR_BLUERED_ITEM.get()),
                new AircraftVariant("skyhawk_pr_brown",
                        "variant.civilian_aviation.skyhawk_pr_brown",
                        0x7B4F2E, () -> SKYHAWK_PR_BROWN_ITEM.get()),
                new AircraftVariant("skyhawk_pr_butter",
                        "variant.civilian_aviation.skyhawk_pr_butter",
                        0xF5E642, () -> SKYHAWK_PR_BUTTER_ITEM.get()),
                new AircraftVariant("skyhawk_pr_red2",
                        "variant.civilian_aviation.skyhawk_pr_red2",
                        0xDD2244, () -> SKYHAWK_PR_RED2_ITEM.get())
        ));

        // Skyhawk グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("skyhawk_red",
                        "variant.civilian_aviation.skyhawk_red",
                        0xCC3333, () -> SKYHAWK_RED_ITEM.get()),
                new AircraftVariant("skyhawk_coffee",
                        "variant.civilian_aviation.skyhawk_coffee",
                        0x8B5E3C, () -> SKYHAWK_COFFEE_ITEM.get()),
                new AircraftVariant("skyhawk_bluestripe",
                        "variant.civilian_aviation.skyhawk_bluestripe",
                        0x3355AA, () -> SKYHAWK_BLUESTRIPE_ITEM.get()),
                new AircraftVariant("skyhawk_green",
                        "variant.civilian_aviation.skyhawk_green",
                        0x5A962E, () -> SKYHAWK_GREEN_ITEM.get()),
                new AircraftVariant("skyhawk_blackorange",
                        "variant.civilian_aviation.skyhawk_blackorange",
                        0xFF6600, () -> SKYHAWK_BLACKORANGE_ITEM.get()),
                new AircraftVariant("skyhawk_blackred",
                        "variant.civilian_aviation.skyhawk_blackred",
                        0xCC0000, () -> SKYHAWK_BLACKRED_ITEM.get()),
                new AircraftVariant("skyhawk_blackyellow",
                        "variant.civilian_aviation.skyhawk_blackyellow",
                        0xFFDD00, () -> SKYHAWK_BLACKYELLOW_ITEM.get()),
                new AircraftVariant("skyhawk_blank",
                        "variant.civilian_aviation.skyhawk_blank",
                        0xF0F0F0, () -> SKYHAWK_BLANK_ITEM.get()),
                new AircraftVariant("skyhawk_blue",
                        "variant.civilian_aviation.skyhawk_blue",
                        0x2255CC, () -> SKYHAWK_BLUE_ITEM.get()),
                new AircraftVariant("skyhawk_bluered",
                        "variant.civilian_aviation.skyhawk_bluered",
                        0xAA1122, () -> SKYHAWK_BLUERED_ITEM.get()),
                new AircraftVariant("skyhawk_brown",
                        "variant.civilian_aviation.skyhawk_brown",
                        0x7B4F2E, () -> SKYHAWK_BROWN_ITEM.get()),
                new AircraftVariant("skyhawk_butter",
                        "variant.civilian_aviation.skyhawk_butter",
                        0xF5E642, () -> SKYHAWK_BUTTER_ITEM.get()),
                new AircraftVariant("skyhawk_red2",
                        "variant.civilian_aviation.skyhawk_red2",
                        0xDD2244, () -> SKYHAWK_RED2_ITEM.get())
        ));

        // Vulcanair グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("vulcanair_red",
                        "variant.civilian_aviation.vulcanair_red",
                        0xCC3333, () -> VULCANAIR_RED_ITEM.get()),
                new AircraftVariant("vulcanair_blackred",
                        "variant.civilian_aviation.vulcanair_blackred",
                        0xCC0000, () -> VULCANAIR_BLACKRED_ITEM.get()),
                new AircraftVariant("vulcanair_blackyellow",
                        "variant.civilian_aviation.vulcanair_blackyellow",
                        0xFFDD00, () -> VULCANAIR_BLACKYELLOW_ITEM.get()),
                new AircraftVariant("vulcanair_blank",
                        "variant.civilian_aviation.vulcanair_blank",
                        0xF0F0F0, () -> VULCANAIR_BLANK_ITEM.get()),
                new AircraftVariant("vulcanair_blue",
                        "variant.civilian_aviation.vulcanair_blue",
                        0x2255CC, () -> VULCANAIR_BLUE_ITEM.get()),
                new AircraftVariant("vulcanair_bluestripe",
                        "variant.civilian_aviation.vulcanair_bluestripe",
                        0x3355AA, () -> VULCANAIR_BLUESTRIPE_ITEM.get()),
                new AircraftVariant("vulcanair_cow",
                        "variant.civilian_aviation.vulcanair_cow",
                        0xFFFFFF, () -> VULCANAIR_COW_ITEM.get()),
                new AircraftVariant("vulcanair_gray",
                        "variant.civilian_aviation.vulcanair_gray",
                        0x888888, () -> VULCANAIR_GRAY_ITEM.get()),
                new AircraftVariant("vulcanair_green",
                        "variant.civilian_aviation.vulcanair_green",
                        0x5A962E, () -> VULCANAIR_GREEN_ITEM.get()),
                new AircraftVariant("vulcanair_orange",
                        "variant.civilian_aviation.vulcanair_orange",
                        0xFF6600, () -> VULCANAIR_ORANGE_ITEM.get()),
                new AircraftVariant("vulcanair_police",
                        "variant.civilian_aviation.vulcanair_police",
                        0x003399, () -> VULCANAIR_POLICE_ITEM.get()),
                new AircraftVariant("vulcanair_redsnail",
                        "variant.civilian_aviation.vulcanair_redsnail",
                        0xDD2244, () -> VULCANAIR_REDSNAIL_ITEM.get()),
                new AircraftVariant("vulcanair_redyellow",
                        "variant.civilian_aviation.vulcanair_redyellow",
                        0xFF3300, () -> VULCANAIR_REDYELLOW_ITEM.get()),
                new AircraftVariant("vulcanair_seagreen",
                        "variant.civilian_aviation.vulcanair_seagreen",
                        0x2E8B57, () -> VULCANAIR_SEAGREEN_ITEM.get()),
                new AircraftVariant("vulcanair_white",
                        "variant.civilian_aviation.vulcanair_white",
                        0xFFFFFF, () -> VULCANAIR_WHITE_ITEM.get()),
                new AircraftVariant("vulcanair_winged",
                        "variant.civilian_aviation.vulcanair_winged",
                        0xCCCCCC, () -> VULCANAIR_WINGED_ITEM.get()),
                new AircraftVariant("vulcanair_yellow",
                        "variant.civilian_aviation.vulcanair_yellow",
                        0xFFDD00, () -> VULCANAIR_YELLOW_ITEM.get())
        ));

        // Trimotor グループ
        AircraftVariant.registerGroup(List.of(
                new AircraftVariant("trimotor_blue",
                        "variant.civilian_aviation.trimotor_blue",
                        0x2255CC, () -> TRIMOTOR_BLUE_ITEM.get()),
                new AircraftVariant("trimotor_black",
                        "variant.civilian_aviation.trimotor_black",
                        0x222222, () -> TRIMOTOR_BLACK_ITEM.get()),
                new AircraftVariant("trimotor_red",
                        "variant.civilian_aviation.trimotor_red",
                        0xCC3333, () -> TRIMOTOR_RED_ITEM.get()),
                new AircraftVariant("trimotor_white",
                        "variant.civilian_aviation.trimotor_white",
                        0xFFFFFF, () -> TRIMOTOR_WHITE_ITEM.get())
        ));

        // ---- BBmodel アニメーション変数登録 --------------
        BBAnimationVariables.register("door_l");
        BBAnimationVariables.register("door_r");
        BBAnimationVariables.register("sidedoor_l");
        BBAnimationVariables.register("sidedoor_r");
        BBAnimationVariables.register("stick");
        BBAnimationVariables.register("lever_stick");
        BBAnimationVariables.register("lever_stick_z");
        BBAnimationVariables.register("throttle");
        BBAnimationVariables.register("mgun_yaw");
        BBAnimationVariables.register("mgun_pitch");
        BBAnimationVariables.register("landing_gear");
        BBAnimationVariables.register("landing_gear_a");
        BBAnimationVariables.register("gear_hatch");
        //WeaponRegistry
        WeaponRegistry.register(locate("gunobserver"), Observerguns::new);
        WeaponRegistry.register(locate("gunm1919"), GunM1919::new);
        WeaponRegistry.register(locate("basicbomb_hardpoint"), BasicBombBay::new);


        // ---- ネットワークメッセージ登録 ------------------
        CivilianAviationMessages.loadMessages();
    }

    static Supplier<Item> register(String name, Supplier<Item> item) {
        Supplier<Item> s = Registration.register(BuiltInRegistries.ITEM, locate(name), item);
        Items.items.add(s);
        return s;
    }

    static Supplier<Item> registerItem(String name, Supplier<Item> item) {
        return Registration.register(BuiltInRegistries.ITEM, locate(name), item);
    }

    static <T extends Entity> Supplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        ResourceLocation id = locate(name);
        return Registration.register(BuiltInRegistries.ENTITY_TYPE, id, () -> builder.build(id.toString()));
    }

    public static ResourceLocation locate(String name) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name);
    }
}
