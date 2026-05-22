package net.velleagle.civilian_aviation.sound;

import immersive_aircraft.cobalt.registration.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.velleagle.civilian_aviation.CivilianAviation;

import java.util.function.Supplier;

/**
 * Allison 250 タービンエンジンサウンドイベント登録。
 * 使用機体: Bell206Blackstripe
 * CivilianAviation.init() 内で register() を呼ぶこと。
 *
 * 構造:
 *  - crankingintro / crankingintro_inside: スターター起動中（電源ON+スターターON）
 *  - running_turbine / running_turbine_inside: タービン回転（低〜高パワー全域）
 *  - running / running_inside: 低回転時（アイドル域、engine_powered clamp[0,0]に相当）
 *  - running (high) / running_inside (high): 高回転時（フルパワー域）
 *  - running_distant / running_moredistant: 遠距離外部音
 */
public final class Allison250Sounds {

    public static Supplier<SoundEvent> CRANKING_INTRO;
    public static Supplier<SoundEvent> CRANKING_INTRO_INSIDE;

    // タービン（低〜高パワー共通）
    public static Supplier<SoundEvent> RUNNING_TURBINE;
    public static Supplier<SoundEvent> RUNNING_TURBINE_INSIDE;

    // 低回転（アイドル域）
    public static Supplier<SoundEvent> RUNNING_LOW;
    public static Supplier<SoundEvent> RUNNING_LOW_INSIDE;

    // 高回転（フルパワー域）
    public static Supplier<SoundEvent> RUNNING_HIGH;
    public static Supplier<SoundEvent> RUNNING_HIGH_INSIDE;

    // 遠距離
    public static Supplier<SoundEvent> RUNNING_DISTANT;
    public static Supplier<SoundEvent> RUNNING_MOREDISTANT;

    private Allison250Sounds() {}

    public static void register() {
        CRANKING_INTRO        = reg("engineallison250_crankingintro");
        CRANKING_INTRO_INSIDE = reg("engineallison250_crankingintro_inside");

        RUNNING_TURBINE        = reg("engineallison250_running_turbine");
        RUNNING_TURBINE_INSIDE = reg("engineallison250_running_turbine_inside");

        RUNNING_LOW        = reg("engineallison250_running_low");
        RUNNING_LOW_INSIDE = reg("engineallison250_running_inside");

        RUNNING_HIGH        = reg("engineallison250_running");
        RUNNING_HIGH_INSIDE = reg("engineallison250_running_high_inside");

        RUNNING_DISTANT     = reg("engineallison250_running_distant");
        RUNNING_MOREDISTANT = reg("engineallison250_running_moredistant");
    }

    private static Supplier<SoundEvent> reg(String name) {
        ResourceLocation id = CivilianAviation.locate(name);
        return Registration.register(BuiltInRegistries.SOUND_EVENT, id,
                () -> SoundEvent.createVariableRangeEvent(id));
    }
}
