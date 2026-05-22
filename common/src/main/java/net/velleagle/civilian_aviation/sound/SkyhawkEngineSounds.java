package net.velleagle.civilian_aviation.sound;

import immersive_aircraft.cobalt.registration.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.velleagle.civilian_aviation.CivilianAviation;

import java.util.function.Supplier;

/**
 * Skyhawk (Lycoming O-360) エンジンサウンドイベント登録クラス。
 *
 * <p><b>必須:</b> {@code CivilianAviation.init()} 内（MODローディングフェーズ中）で
 * {@code SkyhawkEngineSounds.register()} を呼ぶこと。
 *
 * <p>Forge では Registration.register() は FMLJavaModLoadingContext が
 * 有効な MOD ローディングフェーズ中にしか呼べない。
 * このクラスを interface にすると static フィールドの初期化が
 * クラスロード時（クライアント tick 中）に遅延実行されてしまい、
 * FMLJavaModLoadingContext.get() が null になってクラッシュする。
 * そのため class にして register() で明示的に登録する。
 */
public final class SkyhawkEngineSounds {

    // ---- 起動・停止・特殊 ----------------------------------------
    public static Supplier<SoundEvent> STARTING;
    public static Supplier<SoundEvent> STOPPING;
    public static Supplier<SoundEvent> SPUTTER;

    // ---- クランキング -------------------------------------------
    public static Supplier<SoundEvent> CRANKING_INTRO;
    public static Supplier<SoundEvent> CRANKING_LOOP;
    public static Supplier<SoundEvent> CRANKING_OUTRO;
    public static Supplier<SoundEvent> CRANKING;

    // ---- ランニング 800 RPM -------------------------------------
    public static Supplier<SoundEvent> RUNNING_800;
    public static Supplier<SoundEvent> RUNNING_800_INSIDE;
    public static Supplier<SoundEvent> RUNNING_800_DISTANT;
    public static Supplier<SoundEvent> RUNNING_800_MOREDISTANT;

    // ---- ランニング 1600 RPM ------------------------------------
    public static Supplier<SoundEvent> RUNNING_1600;
    public static Supplier<SoundEvent> RUNNING_1600_INSIDE;
    public static Supplier<SoundEvent> RUNNING_1600_DISTANT;
    public static Supplier<SoundEvent> RUNNING_1600_MOREDISTANT;

    // ---- ランニング 2000 RPM ------------------------------------
    public static Supplier<SoundEvent> RUNNING_2000;
    public static Supplier<SoundEvent> RUNNING_2000_INSIDE;
    public static Supplier<SoundEvent> RUNNING_2000_DISTANT;
    public static Supplier<SoundEvent> RUNNING_2000_MOREDISTANT;

    // ---- ランニング 2500 RPM ------------------------------------
    public static Supplier<SoundEvent> RUNNING_2500;
    public static Supplier<SoundEvent> RUNNING_2500_INSIDE;
    public static Supplier<SoundEvent> RUNNING_2500_DISTANT;
    public static Supplier<SoundEvent> RUNNING_2500_MOREDISTANT;

    private SkyhawkEngineSounds() {}

    /**
     * MOD ローディングフェーズ中（{@code CivilianAviation.init()} 内）で呼ぶ。
     * Forge では FMLJavaModLoadingContext が有効なこのタイミング以外で
     * Registration.register() を呼ぶとクラッシュする。
     */
    public static void register() {
        STARTING        = reg("enginelycomingo360_starting");
        STOPPING        = reg("enginelycomingo360_stopping");
        SPUTTER         = reg("enginelycomingo360_sputter");

        CRANKING_INTRO  = reg("enginelycomingo360_crankingintro");
        CRANKING_LOOP   = reg("enginelycomingo360_crankingloop");
        CRANKING_OUTRO  = reg("enginelycomingo360_crankingoutro");
        CRANKING        = reg("enginelycomingo360_cranking");

        RUNNING_800             = reg("enginelycomingo360_running_800");
        RUNNING_800_INSIDE      = reg("enginelycomingo360_running_800_inside");
        RUNNING_800_DISTANT     = reg("enginelycomingo360_running_800_distant");
        RUNNING_800_MOREDISTANT = reg("enginelycomingo360_running_800_moredistant");

        RUNNING_1600             = reg("enginelycomingo360_running_1600");
        RUNNING_1600_INSIDE      = reg("enginelycomingo360_running_1600_inside");
        RUNNING_1600_DISTANT     = reg("enginelycomingo360_running_1600_distant");
        RUNNING_1600_MOREDISTANT = reg("enginelycomingo360_running_1600_moredistant");

        RUNNING_2000             = reg("enginelycomingo360_running_2000");
        RUNNING_2000_INSIDE      = reg("enginelycomingo360_running_2000_inside");
        RUNNING_2000_DISTANT     = reg("enginelycomingo360_running_2000_distant");
        RUNNING_2000_MOREDISTANT = reg("enginelycomingo360_running_2000_moredistant");

        RUNNING_2500             = reg("enginelycomingo360_running_2500");
        RUNNING_2500_INSIDE      = reg("enginelycomingo360_running_2500_inside");
        RUNNING_2500_DISTANT     = reg("enginelycomingo360_running_2500_distant");
        RUNNING_2500_MOREDISTANT = reg("enginelycomingo360_running_2500_moredistant");
    }

    private static Supplier<SoundEvent> reg(String name) {
        // sounds.json のキーは "engine/..." なしで登録し、
        // sounds.json 側でパスを合わせる
        ResourceLocation id = CivilianAviation.locate(name);
        return Registration.register(
                BuiltInRegistries.SOUND_EVENT, id,
                () -> SoundEvent.createVariableRangeEvent(id));
    }
}
