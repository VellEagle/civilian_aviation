package net.velleagle.civilian_aviation.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

import java.util.LinkedList;
import java.util.List;

/**
 * Civilian Aviation のキーバインド定義。
 *
 * キーはゲーム設定画面の「Civilian Aviation」カテゴリに表示される。
 *
 * --- プラットフォーム側での登録 ---
 *
 * Fabric の場合（ClientModInitializer#onInitializeClient）:
 *   CivilianAviationKeyBindings.list.forEach(KeyBindingHelper::registerKeyBinding);
 *
 * Forge の場合（RegisterKeyMappingsEvent）:
 *   CivilianAviationKeyBindings.list.forEach(event::register);
 */
public class CivilianAviationKeyBindings {

    public static final String CATEGORY = "key.categories.civilian_aviation";

    /** プラットフォーム側でこのリストを読んで登録する */
    public static final List<KeyMapping> list = new LinkedList<>();

    /** ランディングギア トグルキー（デフォルト: G） */
    public static final KeyMapping landingGear;

    static {
        landingGear    = newKey("landing_gear",    GLFW.GLFW_KEY_G);
    }

    private static KeyMapping newKey(String name, int defaultKey) {
        KeyMapping key = new KeyMapping(
                "key.civilian_aviation." + name,
                InputConstants.Type.KEYSYM,
                defaultKey,
                CATEGORY
        );
        list.add(key);
        return key;
    }
}

