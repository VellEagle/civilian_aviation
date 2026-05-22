package net.velleagle.civilian_aviation.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;

/**
 * Skyhawk / Lycoming O-360 エンジンサウンドマネージャ（v6）。
 *
 * <h2>Interior / Exterior の切り替え（IV 仕様準拠）</h2>
 * <ul>
 *   <li><b>isInterior</b>（一人称 かつ 搭乗中）:
 *       interior ループ ON、exterior ループ OFF</li>
 *   <li><b>exterior</b>（三人称 or 非搭乗）:
 *       interior ループ OFF、exterior ループ ON</li>
 * </ul>
 */
public class SkyhawkEngineSoundManager {

    private static final float THRESHOLD = 0.01f;

    private final CivilianAircraftEntity entity;

    // クランキング
    private EngineLoop crankInt, crankExt;

    // ランニング 外部近距離（isExterior 相当: 一人称搭乗中は OFF）
    private EngineLoop r800, r1600, r2000, r2500;

    // ランニング 内部（isInterior 相当: 一人称搭乗中のみ ON）
    private EngineLoop r800i, r1600i, r2000i, r2500i;

    // ランニング 遠距離・超遠距離（視点に関係なく距離で制御）
    private EngineLoop r800d, r1600d, r2000d, r2500d;
    private EngineLoop r800m, r1600m, r2000m, r2500m;

    private boolean prevRunning = false;
    private float   prevTarget  = 0f;

    public SkyhawkEngineSoundManager(CivilianAircraftEntity entity) {
        this.entity = entity;
    }

    // =========================================================================
    // メイン更新
    // @param isRider    搭乗中かどうか（視点問わず）
    // @param isInterior 搭乗中 かつ 一人称視点（IVのisInterior条件）
    // =========================================================================
    public void tick(float power, float target, boolean isRider, boolean isInterior, double dist) {

        boolean running  = power > 0.05f;
        boolean starting = target > prevTarget + 0.05f;

        // ワンショット
        if (running && !prevRunning) one(SkyhawkEngineSounds.STARTING.get());
        if (!running && prevRunning)  one(SkyhawkEngineSounds.STOPPING.get());
        if (starting)                  one(SkyhawkEngineSounds.CRANKING_INTRO.get());

        prevRunning = running;
        prevTarget  = target;

        // クランキングループ
        // crankInt = isInterior のとき（IVの cranking_loop は isInterior=true）
        // crankExt = isExterior のとき（IVの cranking は isExterior=true）
        float cp = 1.0f + power * 0.8f;
        crankInt = loop(crankInt, starting && isInterior  ? 1f : 0f, cp, SkyhawkEngineSounds.CRANKING_LOOP.get());
        crankExt = loop(crankExt, starting && !isInterior ? 1f : 0f, cp, SkyhawkEngineSounds.CRANKING.get());

        if (!running) { stopRunning(); return; }

        // ピッチ（IV pitchAnimations 再現）
        float p = power;
        float p800  = Math.max(0.1f, 2.5636f * p);
        float p1600 = Math.max(0.1f, 1.798f  * p);
        float p2000 = Math.max(0.1f, 1.4036f * p);
        float p2500 = Math.max(0.1f, 1.16f   * p);

        // ボリューム（IV volumeAnimations 再現 × 外部/内部音量係数）
        // 外部と内部で別々に音量を管理するため、外部スケール用の基底値を計算
        float v800base  = clamp(-p + 0.95f);
        float v1600base = clamp(1f - Math.abs(p - 0.55f)  / 0.22f);
        float v2000base = clamp(1f - Math.abs(p - 0.625f) / 0.22f);
        float v2500base = clamp(1.5f * p - 0.35f);

        float extVol = SoundConfig.LYCOMING_EXTERIOR_VOLUME;
        float intVol = SoundConfig.LYCOMING_INTERIOR_VOLUME;

        float v800  = v800base  * extVol;
        float v1600 = v1600base * extVol;
        float v2000 = v2000base * extVol;
        float v2500 = v2500base * extVol;
        // 内部音量（INSIDEループで使用）
        float v800i_vol  = v800base  * intVol;
        float v1600i_vol = v1600base * intVol;
        float v2000i_vol = v2000base * intVol;
        float v2500i_vol = v2500base * intVol;

        // =========================================================================
        // 距離スケール
        //
        // 外部系（近距離・遠距離・超遠距離）は isInterior のとき全て OFF にする。
        // isInterior 中に遠距離ループが鳴り続けると、三人称→一人称切替後に
        // 「内部音 + 遠距離音」が重なって音量が増す問題が起きるため。
        // =========================================================================

        // 外部近距離: isInterior なら OFF
        float sN = isInterior ? 0f : tri(dist, 0, 0, 1.0f, 20, 0.3f, 100, 0f);

        // 遠距離・超遠距離: isInterior なら OFF（内部音と重ならないようにする）
        float sD = isInterior ? 0f : tri(dist,  40, 100, 0.6f, 140, 0.0f, 140, 0f);
        float sM = isInterior ? 0f : tri(dist, 100, 200, 0.4f, 260, 0.0f, 260, 0f);

        // 内部スケール: isInterior のときのみフル音量
        float sI = isInterior ? 1.0f : 0f;

        // 外部近距離
        r800  = loop(r800,  v800  * sN, p800,  SkyhawkEngineSounds.RUNNING_800.get());
        r1600 = loop(r1600, v1600 * sN, p1600, SkyhawkEngineSounds.RUNNING_1600.get());
        r2000 = loop(r2000, v2000 * sN, p2000, SkyhawkEngineSounds.RUNNING_2000.get());
        r2500 = loop(r2500, v2500 * sN, p2500, SkyhawkEngineSounds.RUNNING_2500.get());

        // 内部（一人称搭乗中のみ）
        r800i  = loop(r800i,  v800i_vol  * sI, p800,  SkyhawkEngineSounds.RUNNING_800_INSIDE.get());
        r1600i = loop(r1600i, v1600i_vol * sI, p1600, SkyhawkEngineSounds.RUNNING_1600_INSIDE.get());
        r2000i = loop(r2000i, v2000i_vol * sI, p2000, SkyhawkEngineSounds.RUNNING_2000_INSIDE.get());
        r2500i = loop(r2500i, v2500i_vol * sI, p2500, SkyhawkEngineSounds.RUNNING_2500_INSIDE.get());

        // 遠距離
        r800d  = loop(r800d,  v800  * sD, p800,  SkyhawkEngineSounds.RUNNING_800_DISTANT.get());
        r1600d = loop(r1600d, v1600 * sD, p1600, SkyhawkEngineSounds.RUNNING_1600_DISTANT.get());
        r2000d = loop(r2000d, v2000 * sD, p2000, SkyhawkEngineSounds.RUNNING_2000_DISTANT.get());
        r2500d = loop(r2500d, v2500 * sD, p2500, SkyhawkEngineSounds.RUNNING_2500_DISTANT.get());

        // 超遠距離
        r800m  = loop(r800m,  v800  * sM, p800,  SkyhawkEngineSounds.RUNNING_800_MOREDISTANT.get());
        r1600m = loop(r1600m, v1600 * sM, p1600, SkyhawkEngineSounds.RUNNING_1600_MOREDISTANT.get());
        r2000m = loop(r2000m, v2000 * sM, p2000, SkyhawkEngineSounds.RUNNING_2000_MOREDISTANT.get());
        r2500m = loop(r2500m, v2500 * sM, p2500, SkyhawkEngineSounds.RUNNING_2500_MOREDISTANT.get());
    }

    // =========================================================================
    // ユーティリティ
    // =========================================================================

    private EngineLoop loop(EngineLoop current, float vol, float pitch, SoundEvent sound) {
        vol = clamp(vol);
        if (vol > THRESHOLD) {
            if (current == null || current.isStopped()) {
                current = new EngineLoop(sound, entity);
                Minecraft.getInstance().getSoundManager().play(current);
            }
            current.setVolume(vol);
            current.setPitch(pitch);
            return current;
        } else {
            if (current != null && !current.isStopped()) current.markStopped();
            return null;
        }
    }

    private void one(SoundEvent e) {
        entity.level().playLocalSound(
                entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                e, entity.getSoundSource(), 1f, 1f, false);
    }

    private void stopRunning() {
        r800  = kill(r800);  r1600  = kill(r1600);  r2000  = kill(r2000);  r2500  = kill(r2500);
        r800i = kill(r800i); r1600i = kill(r1600i); r2000i = kill(r2000i); r2500i = kill(r2500i);
        r800d = kill(r800d); r1600d = kill(r1600d); r2000d = kill(r2000d); r2500d = kill(r2500d);
        r800m = kill(r800m); r1600m = kill(r1600m); r2000m = kill(r2000m); r2500m = kill(r2500m);
    }

    private static EngineLoop kill(EngineLoop l) {
        if (l != null && !l.isStopped()) l.markStopped();
        return null;
    }

    private static float clamp(float v) { return Math.max(0f, Math.min(1f, v)); }

    private static float tri(double d, double s, double m, float vm, double e, float ve, double u, float u2) {
        if (d <= s) return vm;
        if (d >= e) return 0f;
        if (d <= m) return (float)((d - s) / Math.max(1e-4, m - s)) * vm;
        return vm + (float)((d - m) / Math.max(1e-4, e - m)) * (ve - vm);
    }

    public void stop() {
        crankInt = kill(crankInt); crankExt = kill(crankExt);
        stopRunning();
    }

    // =========================================================================
    // EngineLoop
    // =========================================================================
    public static final class EngineLoop extends AbstractTickableSoundInstance {
        private final Entity entity;
        private boolean dead = false;

        EngineLoop(SoundEvent sound, Entity entity) {
            super(sound, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
            this.entity      = entity;
            this.looping     = true;
            this.delay       = 0;
            this.volume      = 0.05f;
            this.pitch       = 1f;
            this.attenuation = Attenuation.NONE;
            sync();
        }

        @Override public void tick() { if (entity.isRemoved() || dead) { stop(); return; } sync(); }
        @Override public boolean isStopped() { return dead || entity.isRemoved() || super.isStopped(); }

        private void sync() {
            this.x = (float) entity.getX();
            this.y = (float) entity.getY();
            this.z = (float) entity.getZ();
        }

        public void setVolume(float v) { this.volume = Math.max(0.001f, Math.min(1f, v)); }
        public void setPitch(float p)  { this.pitch  = Math.max(0.01f, p); }
        public void markStopped()      { this.dead = true; }
    }
}