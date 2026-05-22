package net.velleagle.civilian_aviation.sound;

import immersive_aircraft.cobalt.registration.Registration;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.velleagle.civilian_aviation.CivilianAviation;

import java.util.function.Supplier;

/**
 * 機体ドアサウンドイベント登録。
 *
 * IV の animatedObjects forwardsStartSound / reverseEndSound から抽出:
 *   doorplaneopen2   : Skyhawk, Comanche(door_r), Vulcanair  — 軽快な開扉音
 *   doorplaneclose   : Skyhawk, Comanche(door_r), Vulcanair  — 閉扉音
 *   doorplaneopen_b  : Bell206(全ドア), Comanche(baggage)    — 重い開扉音
 *   doorplaneclose_b : Bell206(全ドア), Comanche(baggage)    — 重い閉扉音
 *   doorplaneopen    : PZL37Los(hatch)                       — ハッチ開音
 *   doorplaneclose   : PZL37Los(hatch)                       — ハッチ閉音（共用）
 *   doorcaropen      : E500(door_l)                          — 車型ドア開音
 *   doorcarclose     : E500(door_l)                          — 車型ドア閉音
 *
 * これらのサウンドファイルは ImmersiveVehicles のリソースパック
 * (mtsofficialpack) から .ogg を取得して配置する。
 */
public final class DoorSounds {

    // 軽快な開閉 (Skyhawk, Comanche door_r, Vulcanair)
    public static Supplier<SoundEvent> PLANE_OPEN;
    public static Supplier<SoundEvent> PLANE_CLOSE;

    // 重い開閉 (Bell206 全ドア, Comanche baggage)
    public static Supplier<SoundEvent> PLANE_OPEN_B;
    public static Supplier<SoundEvent> PLANE_CLOSE_B;

    // ハッチ開閉 (PZL37Los)
    public static Supplier<SoundEvent> HATCH_OPEN;
    // HATCH_CLOSE は PLANE_CLOSE と同じ音なので共用

    // 車型ドア開閉 (E500)
    public static Supplier<SoundEvent> CAR_OPEN;
    public static Supplier<SoundEvent> CAR_CLOSE;

    private DoorSounds() {}

    public static void register() {
        PLANE_OPEN   = reg("doorplaneopen2");
        PLANE_CLOSE  = reg("doorplaneclose");
        PLANE_OPEN_B = reg("doorplaneopen_b");
        PLANE_CLOSE_B= reg("doorplaneclose_b");
        HATCH_OPEN   = reg("doorplaneopenb");   // pzl37losはdoorplaneopenを使用
        CAR_OPEN     = reg("doorcaropen");
        CAR_CLOSE    = reg("doorcarclose");
    }

    private static Supplier<SoundEvent> reg(String name) {
        ResourceLocation id = CivilianAviation.locate(name);
        return Registration.register(BuiltInRegistries.SOUND_EVENT, id,
                () -> SoundEvent.createVariableRangeEvent(id));
    }
}
