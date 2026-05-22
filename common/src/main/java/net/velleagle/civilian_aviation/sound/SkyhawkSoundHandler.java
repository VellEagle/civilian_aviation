package net.velleagle.civilian_aviation.sound;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public final class SkyhawkSoundHandler {

    public static final SkyhawkSoundHandler INSTANCE = new SkyhawkSoundHandler();

    private final Map<Integer, SkyhawkEngineSoundManager> managers = new HashMap<>();

    private SkyhawkSoundHandler() {}

    public void tick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) { stopAll(); return; }

        Player listener = mc.player;

        // 一人称視点かどうか（IVの isInterior / isExterior 判定に使う）
        boolean isFirstPerson = mc.options.getCameraType() == CameraType.FIRST_PERSON;

        mc.level.entitiesForRendering().forEach(entity -> {
            if (!(entity instanceof CivilianAircraftEntity aircraft)) return;
            if (!isSkyhawk(aircraft)) return;

            int id = aircraft.getId();
            SkyhawkEngineSoundManager mgr = managers.get(id);
            if (mgr == null) {
                mgr = new SkyhawkEngineSoundManager(aircraft);
                managers.put(id, mgr);
            }

            float enginePower  = aircraft.getEnginePower();
            float engineTarget = aircraft.getEngineTarget();

            // 搭乗中かどうか
            boolean isRider = listener != null && aircraft.hasPassenger(listener);

            // IVの isInterior 条件: 搭乗中 かつ 一人称視点
            boolean isInterior = isRider && isFirstPerson;

            double dist = listener != null ? aircraft.distanceTo(listener) : Double.MAX_VALUE;

            mgr.tick(enginePower, engineTarget, isRider, isInterior, dist);
        });

        Iterator<Map.Entry<Integer, SkyhawkEngineSoundManager>> it =
                managers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, SkyhawkEngineSoundManager> entry = it.next();
            Entity e = mc.level.getEntity(entry.getKey());
            if (!(e instanceof CivilianAircraftEntity)) {
                entry.getValue().stop();
                it.remove();
            }
        }
    }

    public void stopAll() {
        managers.values().forEach(SkyhawkEngineSoundManager::stop);
        managers.clear();
    }

    private static boolean isSkyhawk(CivilianAircraftEntity entity) {
        String path = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath();
        return path.startsWith("skyhawk");
    }
}
