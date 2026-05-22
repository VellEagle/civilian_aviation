package net.velleagle.civilian_aviation.forge;

import net.velleagle.civilian_aviation.CivilianAviation;
import net.minecraftforge.fml.common.Mod;

@Mod(CivilianAviation.MOD_ID)
public class CivilianAviationForge {

    public CivilianAviationForge() {
        // コンストラクタで登録 → Forge の DeferredRegister と同じタイミング
        CivilianAviation.init();
    }
}

// @Mod.EventBusSubscriber は別クラスに分離
@Mod.EventBusSubscriber(modid = CivilianAviation.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
class CivilianAviationForgeEvents {
    // RegisterEvent の @SubscribeEvent は不要になるので削除
}