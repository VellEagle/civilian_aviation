package net.velleagle.civilian_aviation.sound;

import immersive_aircraft.cobalt.registration.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.velleagle.civilian_aviation.CivilianAviation;

import java.util.function.Supplier;

/**
 * Pratt & Whitney PW610F ジェットエンジンサウンドイベント登録。
 * 使用機体: E500Red
 * 構造: 4段階RPM (2000/6000/10000/14000rpm) — Bristol Mercury と同様のパターン
 */
public final class Pw610fSounds {

    public static Supplier<SoundEvent> STARTING;
    public static Supplier<SoundEvent> STOPPING;
    public static Supplier<SoundEvent> SPUTTER;
    public static Supplier<SoundEvent> CRANKING;

    // 2000rpm (低回転: p < 0.45)
    public static Supplier<SoundEvent> RUNNING_2000;
    public static Supplier<SoundEvent> RUNNING_2000_INSIDE;
    public static Supplier<SoundEvent> RUNNING_2000_DISTANT;
    public static Supplier<SoundEvent> RUNNING_2000_MOREDISTANT;

    // 6000rpm (中低回転: p ≈ 0.25〜0.65)
    public static Supplier<SoundEvent> RUNNING_6000;
    public static Supplier<SoundEvent> RUNNING_6000_INSIDE;   // 6000_inside.ogg なし→2000_insideで代用
    public static Supplier<SoundEvent> RUNNING_6000_DISTANT;
    public static Supplier<SoundEvent> RUNNING_6000_MOREDISTANT;

    // 10000rpm (中高回転: p ≈ 0.4〜0.85)
    public static Supplier<SoundEvent> RUNNING_10000;
    public static Supplier<SoundEvent> RUNNING_10000_INSIDE;
    public static Supplier<SoundEvent> RUNNING_10000_DISTANT;
    public static Supplier<SoundEvent> RUNNING_10000_MOREDISTANT;

    // 14000rpm (高回転: p ≈ 0.55〜1.0)
    public static Supplier<SoundEvent> RUNNING_14000;
    public static Supplier<SoundEvent> RUNNING_14000_INSIDE;
    public static Supplier<SoundEvent> RUNNING_14000_DISTANT;
    public static Supplier<SoundEvent> RUNNING_14000_MOREDISTANT;

    private Pw610fSounds() {}

    public static void register() {
        STARTING = reg("enginepw610f_starting");
        STOPPING = reg("enginepw610f_stopping");
        SPUTTER  = reg("enginepw610f_sputter");
        CRANKING = reg("enginepw610f_cranking");

        RUNNING_2000             = reg("enginepw610f_running_2000");
        RUNNING_2000_INSIDE      = reg("enginepw610f_running_2000_inside");
        RUNNING_2000_DISTANT     = reg("enginepw610f_running_2000_distant");
        RUNNING_2000_MOREDISTANT = reg("enginepw610f_running_2000_moredistant");

        RUNNING_6000             = reg("enginepw610f_running_6000");
        RUNNING_6000_INSIDE      = reg("enginepw610f_running_6000_inside");
        RUNNING_6000_DISTANT     = reg("enginepw610f_running_6000_distant");
        RUNNING_6000_MOREDISTANT = reg("enginepw610f_running_6000_moredistant");

        RUNNING_10000             = reg("enginepw610f_running_10000");
        RUNNING_10000_INSIDE      = reg("enginepw610f_running_10000_inside");
        RUNNING_10000_DISTANT     = reg("enginepw610f_running_10000_distant");
        RUNNING_10000_MOREDISTANT = reg("enginepw610f_running_10000_moredistant");

        RUNNING_14000             = reg("enginepw610f_running_14000");
        RUNNING_14000_INSIDE      = reg("enginepw610f_running_14000_inside");
        RUNNING_14000_DISTANT     = reg("enginepw610f_running_14000_distant");
        RUNNING_14000_MOREDISTANT = reg("enginepw610f_running_14000_moredistant");
    }

    private static Supplier<SoundEvent> reg(String name) {
        ResourceLocation id = CivilianAviation.locate(name);
        return Registration.register(BuiltInRegistries.SOUND_EVENT, id,
                () -> SoundEvent.createVariableRangeEvent(id));
    }
}
