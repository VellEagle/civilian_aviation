package net.velleagle.civilian_aviation.sound;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * civilian_aviation 全機体のエンジンサウンドとドアサウンドを統括するハンドラ。
 * CIvilianAviationForgeClient の onClientTick() から tick() を呼ぶこと。
 */
public final class CivilianSoundHandler {

    public static final CivilianSoundHandler INSTANCE = new CivilianSoundHandler();

    private enum EngineType {
        LYCOMING, BRISTOL_INTERIOR, BRISTOL_NO_INTERIOR, ALLISON250, FRANKLIN, PW610F
    }

    private final Map<Integer, Object>     managers = new HashMap<>();
    private final Map<Integer, EngineType> types    = new HashMap<>();

    private CivilianSoundHandler() {}

    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            stopAll();
            DoorSoundHandler.INSTANCE.stopAll();
            return;
        }

        // ドアサウンドを先に処理
        DoorSoundHandler.INSTANCE.tick();

        Player listener    = mc.player;
        boolean firstPerson = mc.options.getCameraType() == CameraType.FIRST_PERSON;

        mc.level.entitiesForRendering().forEach(entity -> {
            if (entity instanceof CivilianAircraftEntity aircraft) {
                String path = BuiltInRegistries.ENTITY_TYPE.getKey(aircraft.getType()).getPath();
                EngineType et = classifyAircraft(path);
                if (et == null) return;

                int id = aircraft.getId();
                ensureManager(id, et, aircraft, null);

                float power   = aircraft.getEnginePower();
                float target  = aircraft.getEngineTarget();
                boolean isRider    = listener != null && aircraft.hasPassenger(listener);
                boolean isInterior = isRider && firstPerson;
                double  dist = listener != null ? aircraft.distanceTo(listener) : Double.MAX_VALUE;

                dispatchAircraft(id, et, power, target, isRider, isInterior, dist);

            } else if (entity instanceof HelicopterEntity heli) {
                String path = BuiltInRegistries.ENTITY_TYPE.getKey(heli.getType()).getPath();
                EngineType et = classifyHeli(path);
                if (et == null) return;

                int id = heli.getId();
                ensureManager(id, et, null, heli);

                float power   = heli.getEnginePower();
                float target  = heli.getEngineTarget();
                boolean isRider    = listener != null && heli.hasPassenger(listener);
                boolean isInterior = isRider && firstPerson;
                double  dist = listener != null ? heli.distanceTo(listener) : Double.MAX_VALUE;

                dispatchHeli(id, et, power, target, isRider, isInterior, dist);
            }
        });

        // 消えたエンティティのクリーンアップ
        Iterator<Map.Entry<Integer, Object>> it = managers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Object> entry = it.next();
            Entity e = mc.level.getEntity(entry.getKey());
            boolean exists = (e instanceof CivilianAircraftEntity) || (e instanceof HelicopterEntity);
            if (!exists) {
                stopManager(entry.getValue());
                types.remove(entry.getKey());
                it.remove();
            }
        }
    }

    // ---- 機体分類 ---------------------------------------------------------

    private static EngineType classifyAircraft(String path) {
        if (path.startsWith("skyhawk") || path.startsWith("comanche") || path.startsWith("vulcanair"))
            return EngineType.LYCOMING;
        if (path.startsWith("trimotor") || path.startsWith("pzl37los"))
            return EngineType.BRISTOL_INTERIOR;
        if (path.startsWith("pzlp11"))
            return EngineType.BRISTOL_NO_INTERIOR;
        if (path.startsWith("e500"))
            return EngineType.PW610F;
        return null;
    }

    private static EngineType classifyHeli(String path) {
        if (path.startsWith("bell206"))  return EngineType.ALLISON250;
        if (path.startsWith("bell47g"))              return EngineType.FRANKLIN;
        return null;
    }

    // ---- マネージャ生成 ---------------------------------------------------

    private void ensureManager(int id, EngineType et,
                               CivilianAircraftEntity aircraft, HelicopterEntity heli) {
        if (managers.containsKey(id)) return;
        Object mgr;
        switch (et) {
            case LYCOMING:            mgr = new SkyhawkEngineSoundManager(aircraft);         break;
            case BRISTOL_INTERIOR:    mgr = new BristolMercurySoundManager(aircraft, true);  break;
            case BRISTOL_NO_INTERIOR: mgr = new BristolMercurySoundManager(aircraft, false); break;
            case ALLISON250:          mgr = new Allison250SoundManager(heli);                break;
            case FRANKLIN:            mgr = new Franklin0335SoundManager(heli);              break;
            case PW610F:              mgr = new Pw610fSoundManager(aircraft);                break;
            default:                  return;
        }
        managers.put(id, mgr);
        types.put(id, et);
    }

    // ---- ディスパッチ -----------------------------------------------------

    private void dispatchAircraft(int id, EngineType et,
                                  float power, float target,
                                  boolean isRider, boolean isInterior, double dist) {
        Object mgr = managers.get(id);
        if (mgr == null) return;
        if (et == EngineType.LYCOMING) {
            ((SkyhawkEngineSoundManager) mgr).tick(power, target, isRider, isInterior, dist);
        } else if (et == EngineType.BRISTOL_INTERIOR || et == EngineType.BRISTOL_NO_INTERIOR) {
            ((BristolMercurySoundManager) mgr).tick(power, target, isRider, isInterior, dist);
        } else if (et == EngineType.PW610F) {
            ((Pw610fSoundManager) mgr).tick(power, target, isRider, isInterior, dist);
        }
    }

    private void dispatchHeli(int id, EngineType et,
                              float power, float target,
                              boolean isRider, boolean isInterior, double dist) {
        Object mgr = managers.get(id);
        if (mgr == null) return;
        if (et == EngineType.ALLISON250) {
            ((Allison250SoundManager) mgr).tick(power, target, isRider, isInterior, dist);
        } else if (et == EngineType.FRANKLIN) {
            ((Franklin0335SoundManager) mgr).tick(power, target, dist);
        }
    }

    // ---- クリーンアップ ---------------------------------------------------

    public void stopAll() {
        managers.values().forEach(this::stopManager);
        managers.clear();
        types.clear();
        DoorSoundHandler.INSTANCE.stopAll();
    }

    private void stopManager(Object mgr) {
        if (mgr instanceof SkyhawkEngineSoundManager   m) m.stop();
        else if (mgr instanceof BristolMercurySoundManager  m) m.stop();
        else if (mgr instanceof Allison250SoundManager      m) m.stop();
        else if (mgr instanceof Franklin0335SoundManager    m) m.stop();
        else if (mgr instanceof Pw610fSoundManager          m) m.stop();
    }
}
