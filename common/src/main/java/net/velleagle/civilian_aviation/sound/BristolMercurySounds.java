package net.velleagle.civilian_aviation.sound;

import immersive_aircraft.cobalt.registration.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.velleagle.civilian_aviation.CivilianAviation;

import java.util.function.Supplier;

/**
 * Bristol Mercury エンジンサウンドイベント登録。
 * 使用機体: TrimotorBlue, Pzl37Los, Pzlp11
 * CivilianAviation.init() 内で register() を呼ぶこと。
 */
public final class BristolMercurySounds {

    public static Supplier<SoundEvent> STARTING;
    public static Supplier<SoundEvent> STOPPING;
    public static Supplier<SoundEvent> SPUTTER;
    public static Supplier<SoundEvent> CRANKING;

    // 800rpm  (engine_powered clamp[0,0] = アイドル近辺)
    public static Supplier<SoundEvent> RUNNING_800;
    public static Supplier<SoundEvent> RUNNING_800_INSIDE;

    // 800rpm  (engine_powered clamp[1,1] = フルパワー近辺)
    public static Supplier<SoundEvent> RUNNING_800B;
    public static Supplier<SoundEvent> RUNNING_800B_INSIDE;
    public static Supplier<SoundEvent> RUNNING_800B_DISTANT;
    public static Supplier<SoundEvent> RUNNING_800B_MOREDISTANT;

    // 1300rpm
    public static Supplier<SoundEvent> RUNNING_1300;
    public static Supplier<SoundEvent> RUNNING_1300_INSIDE;
    public static Supplier<SoundEvent> RUNNING_1300_DISTANT;
    public static Supplier<SoundEvent> RUNNING_1300_MOREDISTANT;

    // 1800rpm
    public static Supplier<SoundEvent> RUNNING_1800;
    public static Supplier<SoundEvent> RUNNING_1800_INSIDE;
    public static Supplier<SoundEvent> RUNNING_1800_DISTANT;
    public static Supplier<SoundEvent> RUNNING_1800_MOREDISTANT;

    // 2300rpm
    public static Supplier<SoundEvent> RUNNING_2300;
    public static Supplier<SoundEvent> RUNNING_2300_INSIDE;
    public static Supplier<SoundEvent> RUNNING_2300_DISTANT;
    public static Supplier<SoundEvent> RUNNING_2300_MOREDISTANT;

    private BristolMercurySounds() {}

    public static void register() {
        STARTING = reg("enginebristolmercury_starting");
        STOPPING = reg("enginebristolmercury_stopping");
        SPUTTER  = reg("enginebristolmercury_sputter");
        CRANKING = reg("enginebristolmercury_cranking");

        RUNNING_800        = reg("enginebristolmercury_running_800rpm");
        RUNNING_800_INSIDE = reg("enginebristolmercury_running_800rpm_inside");

        RUNNING_800B             = reg("enginebristolmercury_running_800rpm_b");
        RUNNING_800B_INSIDE      = reg("enginebristolmercury_running_800rpm_b_inside");
        RUNNING_800B_DISTANT     = reg("enginebristolmercury_running_800rpm_b_distant");
        RUNNING_800B_MOREDISTANT = reg("enginebristolmercury_running_800rpm_b_moredistant");

        RUNNING_1300             = reg("enginebristolmercury_running_1300rpm");
        RUNNING_1300_INSIDE      = reg("enginebristolmercury_running_1300rpm_inside");
        RUNNING_1300_DISTANT     = reg("enginebristolmercury_running_1300rpm_distant");
        RUNNING_1300_MOREDISTANT = reg("enginebristolmercury_running_1300rpm_moredistant");

        RUNNING_1800             = reg("enginebristolmercury_running_1800rpm");
        RUNNING_1800_INSIDE      = reg("enginebristolmercury_running_1800rpm_inside");
        RUNNING_1800_DISTANT     = reg("enginebristolmercury_running_1800rpm_distant");
        RUNNING_1800_MOREDISTANT = reg("enginebristolmercury_running_1800rpm_moredistant");

        RUNNING_2300             = reg("enginebristolmercury_running_2300rpm");
        RUNNING_2300_INSIDE      = reg("enginebristolmercury_running_2300rpm_inside");
        RUNNING_2300_DISTANT     = reg("enginebristolmercury_running_2300rpm_distant");
        RUNNING_2300_MOREDISTANT = reg("enginebristolmercury_running_2300rpm_moredistant");
    }

    private static Supplier<SoundEvent> reg(String name) {
        ResourceLocation id = CivilianAviation.locate(name);
        return Registration.register(BuiltInRegistries.SOUND_EVENT, id,
                () -> SoundEvent.createVariableRangeEvent(id));
    }
}
