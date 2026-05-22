package net.velleagle.civilian_aviation.sound;

import immersive_aircraft.cobalt.registration.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.velleagle.civilian_aviation.CivilianAviation;

import java.util.function.Supplier;

/**
 * Franklin O-335 エンジンサウンドイベント登録。
 * 使用機体: Bell47g（オープンコックピット = interior音なし）
 * CivilianAviation.init() 内で register() を呼ぶこと。
 */
public final class Franklin0335Sounds {

    public static Supplier<SoundEvent> STARTING;
    public static Supplier<SoundEvent> STOPPING;
    public static Supplier<SoundEvent> SPUTTER;
    public static Supplier<SoundEvent> CRANKING_INTRO;
    public static Supplier<SoundEvent> CRANKING_LOOP;

    // メインループ（外部）
    public static Supplier<SoundEvent> RUNNING;
    public static Supplier<SoundEvent> RUNNING_INSIDE;  // IVでは EXT として定義されているが近距離補完用
    // 遠距離
    public static Supplier<SoundEvent> DISTANT;
    public static Supplier<SoundEvent> MORE_DISTANT;

    private Franklin0335Sounds() {}

    public static void register() {
        STARTING       = reg("enginefranklin0335_starting");
        STOPPING       = reg("enginefranklin0335_stopping");
        SPUTTER        = reg("enginefranklin0335_sputter");
        CRANKING_INTRO = reg("enginefranklin0335_crankingintro");
        CRANKING_LOOP  = reg("enginefranklin0335_crankingloop");

        RUNNING        = reg("enginefranklin0335_running");
        RUNNING_INSIDE = reg("enginefranklin0335_running_inside");
        DISTANT        = reg("enginefranklin0335_distant");
        MORE_DISTANT   = reg("enginefranklin0335_more_distant");
    }

    private static Supplier<SoundEvent> reg(String name) {
        ResourceLocation id = CivilianAviation.locate(name);
        return Registration.register(BuiltInRegistries.SOUND_EVENT, id,
                () -> SoundEvent.createVariableRangeEvent(id));
    }
}
