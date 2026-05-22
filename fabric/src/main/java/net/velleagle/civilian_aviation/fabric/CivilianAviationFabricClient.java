package net.velleagle.civilian_aviation.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.velleagle.civilian_aviation.CivilianAviationClient;
import net.velleagle.civilian_aviation.client.CivilianAviationKeyBindings;
import net.velleagle.civilian_aviation.sound.CivilianSoundHandler;

public class CivilianAviationFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CivilianAviationClient.init();

        // エンジンサウンドをクライアント tick に登録
        ClientTickEvents.END_CLIENT_TICK.register(client ->
                CivilianSoundHandler.INSTANCE.tick());

        // ワールドを離れたときに全サウンドを止める
        ClientLifecycleEvents.CLIENT_STOPPING.register(client ->
                CivilianSoundHandler.INSTANCE.stopAll());

        // キーバインド登録
        CivilianAviationKeyBindings.list.forEach(KeyBindingHelper::registerKeyBinding);
    }
}
