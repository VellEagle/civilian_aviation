package net.velleagle.civilian_aviation.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;
import net.velleagle.civilian_aviation.network.c2s.DoorMessage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 全機体のドア開閉サウンドを管理するハンドラ。
 *
 * ---- サウンドタイミングの設計 ----
 *
 * 「開く音」: アニメーション開始直後（progress が 0 → 正）に鳴らす。
 *             ドアが動き始めた瞬間に聞こえるのが自然。
 *
 * 「閉まる音」: アニメーション完了時（progress が 0 に到達した tick）に鳴らす。
 *              こうすることで
 *                1) 手動クローズ: 閉まりきった後に「バタン」と鳴る
 *                2) 発進自動クローズ: closeAllDoors() が呼ばれると
 *                   isDoorOpen() が false になり、クライアント側で
 *                   progress が 0 に向けてアニメーションする。
 *                   progress が 0 に達した tick に音が鳴るので
 *                   自動クローズでも正しく動作する。
 *
 * progress 値は CivilianAircraftEntity / HelicopterEntity の
 * getDoorXProgress() / getPrevDoorXProgress() で取得する（クライアント専用）。
 */
public final class DoorSoundHandler {

    public static final DoorSoundHandler INSTANCE = new DoorSoundHandler();

    /**
     * 前 tick の progress をキャッシュする必要はない。
     * getPrevDoorXProgress() がエンティティ側で管理しているため。
     * ここでは「前 tick に progress > 0 だったか」を管理するだけで十分だが、
     * エンティティが getPrevDoorXProgress() を公開しているので
     * キャッシュ不要でシンプルに実装できる。
     */
    private DoorSoundHandler() {}

    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;

        mc.level.entitiesForRendering().forEach(entity -> {
            if (entity instanceof CivilianAircraftEntity aircraft && aircraft.hasDoors()) {
                tickAircraft(aircraft);
            } else if (entity instanceof HelicopterEntity heli && heli.hasDoors()) {
                tickHeli(heli);
            }
        });
    }

    // ---- 固定翼機 ---------------------------------------------------------

    private void tickAircraft(CivilianAircraftEntity aircraft) {
        String path = BuiltInRegistries.ENTITY_TYPE.getKey(aircraft.getType()).getPath();

        SoundEvent openL, closeL, openR, closeR;
        if (path.equals("pzl37los")) {
            openL  = DoorSounds.HATCH_OPEN.get();
            closeL = DoorSounds.PLANE_CLOSE.get();
            openR  = DoorSounds.HATCH_OPEN.get();
            closeR = DoorSounds.PLANE_CLOSE.get();
        } else if (path.startsWith("e500")) {
            openL  = DoorSounds.CAR_OPEN.get();
            closeL = DoorSounds.CAR_CLOSE.get();
            openR  = DoorSounds.CAR_OPEN.get();
            closeR = DoorSounds.CAR_CLOSE.get();
        } else {
            // Skyhawk, Comanche, Vulcanair
            openL  = DoorSounds.PLANE_OPEN.get();
            closeL = DoorSounds.PLANE_CLOSE.get();
            openR  = DoorSounds.PLANE_OPEN.get();
            closeR = DoorSounds.PLANE_CLOSE.get();
        }

        float prevL = aircraft.getPrevDoorLProgress();
        float currL = aircraft.getDoorLProgress();
        float prevR = aircraft.getPrevDoorRProgress();
        float currR = aircraft.getDoorRProgress();

        // 開く: prev == 0 かつ curr > 0 (アニメーション開始直後)
        if (prevL == 0f && currL > 0f) playAt(aircraft, openL);
        // 閉まる: prev > 0 かつ curr == 0 (アニメーション完了)
        if (prevL > 0f  && currL == 0f) playAt(aircraft, closeL);

        if (prevR == 0f && currR > 0f) playAt(aircraft, openR);
        if (prevR > 0f  && currR == 0f) playAt(aircraft, closeR);
    }

    // ---- ヘリコプター -----------------------------------------------------

    private void tickHeli(HelicopterEntity heli) {
        SoundEvent open  = DoorSounds.PLANE_OPEN_B.get();
        SoundEvent close = DoorSounds.PLANE_CLOSE_B.get();

        float prevDL  = heli.getPrevDoorLProgress();
        float currDL  = heli.getDoorLProgress();
        float prevDR  = heli.getPrevDoorRProgress();
        float currDR  = heli.getDoorRProgress();
        float prevSDL = heli.getPrevSidedoorLProgress();
        float currSDL = heli.getSidedoorLProgress();
        float prevSDR = heli.getPrevSidedoorRProgress();
        float currSDR = heli.getSidedoorRProgress();

        if (prevDL  == 0f && currDL  > 0f) playAt(heli, open);
        if (prevDL  > 0f  && currDL  == 0f) playAt(heli, close);
        if (prevDR  == 0f && currDR  > 0f) playAt(heli, open);
        if (prevDR  > 0f  && currDR  == 0f) playAt(heli, close);
        if (prevSDL == 0f && currSDL > 0f) playAt(heli, open);
        if (prevSDL > 0f  && currSDL == 0f) playAt(heli, close);
        if (prevSDR == 0f && currSDR > 0f) playAt(heli, open);
        if (prevSDR > 0f  && currSDR == 0f) playAt(heli, close);
    }

    // ---- ユーティリティ ---------------------------------------------------

    private static void playAt(Entity entity, SoundEvent sound) {
        entity.level().playLocalSound(
                entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                sound, entity.getSoundSource(), 1.0f, 1.0f, false);
    }

    public void stopAll() {
        // progress ベース管理になったためキャッシュクリア不要だが
        // インターフェース互換のため残す
    }
}