package net.velleagle.civilian_aviation.neoforge;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.sound.*;

import java.util.function.Supplier;

/**
 * NeoForge エントリーポイント。
 *
 * サウンドイベントは immersive_aircraft の Registration.register() ではなく
 * NeoForge ネイティブの DeferredRegister<SoundEvent> で登録する。
 * immersive_aircraft の Registration.INSTANCE は IA 自身の mod event bus に
 * バインドされているため、civilian_aviation のサウンドを IA の Registration で
 * 登録すると RegistrySupplier が IA のパイプラインに登録されてしまい、
 * civilian_aviation 側の .get() 呼び出しで "Registry Object not present" が発生する。
 *
 * NeoForge 1.21.1 では DeferredRegister.register() が DeferredHolder を返すが、
 * DeferredHolder は Supplier<T> を実装しているためそのまま代入可能。
 */
@Mod(CivilianAviation.MOD_ID)
@EventBusSubscriber(modid = CivilianAviation.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CivilianAviationNeoForge {

    private static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, CivilianAviation.MOD_ID);

    public CivilianAviationNeoForge(IEventBus modBus) {
        registerSounds();
        SOUND_EVENTS.register(modBus);
    }

    // ------------------------------------------------------------------ //
    //  サウンド登録
    // ------------------------------------------------------------------ //

    private static Supplier<SoundEvent> sound(String name) {
        ResourceLocation id = CivilianAviation.locate(name);
        // DeferredHolder<SoundEvent,SoundEvent> は Supplier<SoundEvent> を実装している
        DeferredHolder<SoundEvent, SoundEvent> holder = SOUND_EVENTS.register(name,
                () -> SoundEvent.createVariableRangeEvent(id));
        return holder;
    }

    private static void registerSounds() {
        // ---- SkyhawkEngineSounds (Lycoming O-360) ----
        SkyhawkEngineSounds.STARTING        = sound("enginelycomingo360_starting");
        SkyhawkEngineSounds.STOPPING        = sound("enginelycomingo360_stopping");
        SkyhawkEngineSounds.SPUTTER         = sound("enginelycomingo360_sputter");
        SkyhawkEngineSounds.CRANKING_INTRO  = sound("enginelycomingo360_crankingintro");
        SkyhawkEngineSounds.CRANKING_LOOP   = sound("enginelycomingo360_crankingloop");
        SkyhawkEngineSounds.CRANKING_OUTRO  = sound("enginelycomingo360_crankingoutro");
        SkyhawkEngineSounds.CRANKING        = sound("enginelycomingo360_cranking");
        SkyhawkEngineSounds.RUNNING_800             = sound("enginelycomingo360_running_800");
        SkyhawkEngineSounds.RUNNING_800_INSIDE      = sound("enginelycomingo360_running_800_inside");
        SkyhawkEngineSounds.RUNNING_800_DISTANT     = sound("enginelycomingo360_running_800_distant");
        SkyhawkEngineSounds.RUNNING_800_MOREDISTANT = sound("enginelycomingo360_running_800_moredistant");
        SkyhawkEngineSounds.RUNNING_1600             = sound("enginelycomingo360_running_1600");
        SkyhawkEngineSounds.RUNNING_1600_INSIDE      = sound("enginelycomingo360_running_1600_inside");
        SkyhawkEngineSounds.RUNNING_1600_DISTANT     = sound("enginelycomingo360_running_1600_distant");
        SkyhawkEngineSounds.RUNNING_1600_MOREDISTANT = sound("enginelycomingo360_running_1600_moredistant");
        SkyhawkEngineSounds.RUNNING_2000             = sound("enginelycomingo360_running_2000");
        SkyhawkEngineSounds.RUNNING_2000_INSIDE      = sound("enginelycomingo360_running_2000_inside");
        SkyhawkEngineSounds.RUNNING_2000_DISTANT     = sound("enginelycomingo360_running_2000_distant");
        SkyhawkEngineSounds.RUNNING_2000_MOREDISTANT = sound("enginelycomingo360_running_2000_moredistant");
        SkyhawkEngineSounds.RUNNING_2500             = sound("enginelycomingo360_running_2500");
        SkyhawkEngineSounds.RUNNING_2500_INSIDE      = sound("enginelycomingo360_running_2500_inside");
        SkyhawkEngineSounds.RUNNING_2500_DISTANT     = sound("enginelycomingo360_running_2500_distant");
        SkyhawkEngineSounds.RUNNING_2500_MOREDISTANT = sound("enginelycomingo360_running_2500_moredistant");

        // ---- BristolMercurySounds ----
        BristolMercurySounds.STARTING          = sound("enginebristolmercury_starting");
        BristolMercurySounds.STOPPING          = sound("enginebristolmercury_stopping");
        BristolMercurySounds.SPUTTER           = sound("enginebristolmercury_sputter");
        BristolMercurySounds.CRANKING          = sound("enginebristolmercury_cranking");
        BristolMercurySounds.RUNNING_800        = sound("enginebristolmercury_running_800rpm");
        BristolMercurySounds.RUNNING_800_INSIDE = sound("enginebristolmercury_running_800rpm_inside");
        BristolMercurySounds.RUNNING_800B             = sound("enginebristolmercury_running_800rpm_b");
        BristolMercurySounds.RUNNING_800B_INSIDE      = sound("enginebristolmercury_running_800rpm_b_inside");
        BristolMercurySounds.RUNNING_800B_DISTANT     = sound("enginebristolmercury_running_800rpm_b_distant");
        BristolMercurySounds.RUNNING_800B_MOREDISTANT = sound("enginebristolmercury_running_800rpm_b_moredistant");
        BristolMercurySounds.RUNNING_1300             = sound("enginebristolmercury_running_1300rpm");
        BristolMercurySounds.RUNNING_1300_INSIDE      = sound("enginebristolmercury_running_1300rpm_inside");
        BristolMercurySounds.RUNNING_1300_DISTANT     = sound("enginebristolmercury_running_1300rpm_distant");
        BristolMercurySounds.RUNNING_1300_MOREDISTANT = sound("enginebristolmercury_running_1300rpm_moredistant");
        BristolMercurySounds.RUNNING_1800             = sound("enginebristolmercury_running_1800rpm");
        BristolMercurySounds.RUNNING_1800_INSIDE      = sound("enginebristolmercury_running_1800rpm_inside");
        BristolMercurySounds.RUNNING_1800_DISTANT     = sound("enginebristolmercury_running_1800rpm_distant");
        BristolMercurySounds.RUNNING_1800_MOREDISTANT = sound("enginebristolmercury_running_1800rpm_moredistant");
        BristolMercurySounds.RUNNING_2300             = sound("enginebristolmercury_running_2300rpm");
        BristolMercurySounds.RUNNING_2300_INSIDE      = sound("enginebristolmercury_running_2300rpm_inside");
        BristolMercurySounds.RUNNING_2300_DISTANT     = sound("enginebristolmercury_running_2300rpm_distant");
        BristolMercurySounds.RUNNING_2300_MOREDISTANT = sound("enginebristolmercury_running_2300rpm_moredistant");

        // ---- Allison250Sounds ----
        Allison250Sounds.CRANKING_INTRO        = sound("engineallison250_crankingintro");
        Allison250Sounds.CRANKING_INTRO_INSIDE = sound("engineallison250_crankingintro_inside");
        Allison250Sounds.RUNNING_TURBINE        = sound("engineallison250_running_turbine");
        Allison250Sounds.RUNNING_TURBINE_INSIDE = sound("engineallison250_running_turbine_inside");
        Allison250Sounds.RUNNING_LOW            = sound("engineallison250_running_low");
        Allison250Sounds.RUNNING_LOW_INSIDE     = sound("engineallison250_running_inside");
        Allison250Sounds.RUNNING_HIGH           = sound("engineallison250_running");
        Allison250Sounds.RUNNING_HIGH_INSIDE    = sound("engineallison250_running_high_inside");
        Allison250Sounds.RUNNING_DISTANT        = sound("engineallison250_running_distant");
        Allison250Sounds.RUNNING_MOREDISTANT    = sound("engineallison250_running_moredistant");

        // ---- Franklin0335Sounds ----
        Franklin0335Sounds.STARTING       = sound("enginefranklin0335_starting");
        Franklin0335Sounds.STOPPING       = sound("enginefranklin0335_stopping");
        Franklin0335Sounds.SPUTTER        = sound("enginefranklin0335_sputter");
        Franklin0335Sounds.CRANKING_INTRO = sound("enginefranklin0335_crankingintro");
        Franklin0335Sounds.CRANKING_LOOP  = sound("enginefranklin0335_crankingloop");
        Franklin0335Sounds.RUNNING        = sound("enginefranklin0335_running");
        Franklin0335Sounds.RUNNING_INSIDE = sound("enginefranklin0335_running_inside");
        Franklin0335Sounds.DISTANT        = sound("enginefranklin0335_distant");
        Franklin0335Sounds.MORE_DISTANT   = sound("enginefranklin0335_more_distant");

        // ---- Pw610fSounds ----
        Pw610fSounds.STARTING = sound("enginepw610f_starting");
        Pw610fSounds.STOPPING = sound("enginepw610f_stopping");
        Pw610fSounds.SPUTTER  = sound("enginepw610f_sputter");
        Pw610fSounds.CRANKING = sound("enginepw610f_cranking");
        Pw610fSounds.RUNNING_2000             = sound("enginepw610f_running_2000");
        Pw610fSounds.RUNNING_2000_INSIDE      = sound("enginepw610f_running_2000_inside");
        Pw610fSounds.RUNNING_2000_DISTANT     = sound("enginepw610f_running_2000_distant");
        Pw610fSounds.RUNNING_2000_MOREDISTANT = sound("enginepw610f_running_2000_moredistant");
        Pw610fSounds.RUNNING_6000             = sound("enginepw610f_running_6000");
        Pw610fSounds.RUNNING_6000_INSIDE      = sound("enginepw610f_running_6000_inside");
        Pw610fSounds.RUNNING_6000_DISTANT     = sound("enginepw610f_running_6000_distant");
        Pw610fSounds.RUNNING_6000_MOREDISTANT = sound("enginepw610f_running_6000_moredistant");
        Pw610fSounds.RUNNING_10000             = sound("enginepw610f_running_10000");
        Pw610fSounds.RUNNING_10000_INSIDE      = sound("enginepw610f_running_10000_inside");
        Pw610fSounds.RUNNING_10000_DISTANT     = sound("enginepw610f_running_10000_distant");
        Pw610fSounds.RUNNING_10000_MOREDISTANT = sound("enginepw610f_running_10000_moredistant");
        Pw610fSounds.RUNNING_14000             = sound("enginepw610f_running_14000");
        Pw610fSounds.RUNNING_14000_INSIDE      = sound("enginepw610f_running_14000_inside");
        Pw610fSounds.RUNNING_14000_DISTANT     = sound("enginepw610f_running_14000_distant");
        Pw610fSounds.RUNNING_14000_MOREDISTANT = sound("enginepw610f_running_14000_moredistant");

        // ---- DoorSounds ----
        DoorSounds.PLANE_OPEN    = sound("doorplaneopen2");
        DoorSounds.PLANE_CLOSE   = sound("doorplaneclose");
        DoorSounds.PLANE_OPEN_B  = sound("doorplaneopen_b");
        DoorSounds.PLANE_CLOSE_B = sound("doorplaneclose_b");
        DoorSounds.HATCH_OPEN    = sound("doorplaneopenb");
        DoorSounds.CAR_OPEN      = sound("doorcaropen");
        DoorSounds.CAR_CLOSE     = sound("doorcarclose");
    }

    // ------------------------------------------------------------------ //
    //  アイテム・エンティティ登録
    // ------------------------------------------------------------------ //

    private static boolean registered = false;

    @SubscribeEvent
    public static void onRegistryEvent(RegisterEvent event) {
        if (!registered) {
            registered = true;
            CivilianAviation.initWithoutSounds();
        }
    }
}
