package net.velleagle.civilian_aviation.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.velleagle.civilian_aviation.CivilianAviationClient;
import net.velleagle.civilian_aviation.client.CivilianAviationKeyBindings;
import net.velleagle.civilian_aviation.sound.CivilianSoundHandler;

/**
 * Fabric クライアント側の初期化クラス。
 *
 * 修正内容:
 *   - SkyhawkSoundHandler（Skyhawk専用）を CivilianSoundHandler（全機体統括）に置き換え。
 *     Forge 側はもともと CivilianSoundHandler を使っていたが、Fabric 側は
 *     SkyhawkSoundHandler のみ登録されていたため Skyhawk 以外の機体で
 *     エンジン音が一切再生されないバグがあった。
 *   - CLIENT_STOPPING イベントでも CivilianSoundHandler.stopAll() を呼ぶように変更。
 *     ワールドを離れた際にループサウンドが残らないようにする。
 */
public class CivilianAviationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CivilianAviationClient.init();

        // 全機体のエンジンサウンド・ドアサウンドをクライアント tick に登録
        // （旧: SkyhawkSoundHandler → 新: CivilianSoundHandler で全機体対応）
        ClientTickEvents.END_CLIENT_TICK.register(client ->
                CivilianSoundHandler.INSTANCE.tick());

        // ワールドを離れたときに全サウンドを止める
        ClientLifecycleEvents.CLIENT_STOPPING.register(client ->
                CivilianSoundHandler.INSTANCE.stopAll());

        // キーバインド登録
        CivilianAviationKeyBindings.list.forEach(KeyBindingHelper::registerKeyBinding);
    }
}
