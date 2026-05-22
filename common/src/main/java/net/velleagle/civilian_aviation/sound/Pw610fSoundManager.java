package net.velleagle.civilian_aviation.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;

/**
 * PW610F ジェットエンジンサウンドマネージャ。
 * 使用機体: E500Red（interior有）
 *
 * IV の構造（Bristol Mercury と完全に同じパターン）:
 *   2000rpm  (clamp[1,1]): 低回転  — pitch=5p+0.4,   vol=-2p+1.25
 *   6000rpm  (clamp[1,1]): 中低回転 — pitch=2.385p,   vol複合
 *   10000rpm (clamp[1,1]): 中高回転 — pitch=1.575p,   vol複合
 *   14000rpm (clamp[1,1]): 高回転  — pitch=1.35p,    vol複合
 */
public class Pw610fSoundManager {

    private static final float THRESHOLD = 0.01f;

    private final CivilianAircraftEntity entity;

    private EngineLoop crank;
    private EngineLoop r2000, r2000i, r2000d, r2000m;
    private EngineLoop r6000, r6000i, r6000d, r6000m;
    private EngineLoop r10000, r10000i, r10000d, r10000m;
    private EngineLoop r14000, r14000i, r14000d, r14000m;

    private boolean prevRunning = false;
    private float   prevTarget  = 0f;

    public Pw610fSoundManager(CivilianAircraftEntity entity) {
        this.entity = entity;
    }

    public void tick(float power, float target, boolean isRider, boolean isInterior, double dist) {
        boolean running  = power > 0.05f;
        boolean starting = target > prevTarget + 0.05f;

        // ---------------------------------------------------------------
        // 停止音のトリガー設計について
        //
        // getEnginePower()  = エンジンの実際の回転数（スムーズ値）
        //                     スロットルを切ってもジェットは慣性でゆっくり落ちる
        //                     → power が 0.05 を下回るまで数秒かかる = 遅延の原因
        //
        // getEngineTarget() = スロットル目標値（プレイヤーの操作に即反応する）
        //                     エンジン停止操作をした瞬間に 0 に落ちる
        //
        // 正しい発火条件:
        //   始動音 → target が 0 から上昇した瞬間  (= starting と同義)
        //   停止音 → target が 0 に落ちた瞬間      (= prevTarget>閾値 && target≈0)
        //
        // これにより停止音は「エンジン停止操作と同時」に鳴り、
        // その後ゆっくり power が落ちていく（スプールダウン音）と自然に重なる。
        // ---------------------------------------------------------------

        boolean justStarted  = running && !prevRunning;
        // target が 0.05 より大きい状態から 0.05 以下に落ちた = 停止操作した瞬間
        boolean justShutdown = prevTarget > 0.05f && target <= 0.05f;

        if (justStarted)  {
            one(Pw610fSounds.STARTING.get(), SoundConfig.PW610F_STARTING_VOLUME);
        }
        if (justShutdown) {
            one(Pw610fSounds.STOPPING.get(), SoundConfig.PW610F_STOPPING_VOLUME);
        }
        if (starting) one(Pw610fSounds.CRANKING.get(), SoundConfig.PW610F_CRANKING_VOLUME);

        prevRunning = running;
        prevTarget  = target;

        float cp = 1.0f + power * 0.8f;
        crank = loop(crank, starting ? SoundConfig.PW610F_CRANKING_VOLUME : 0f, cp, Pw610fSounds.CRANKING.get());

        if (!running) { stopAll(); return; }

        float p = power;

        // ピッチ (IV pitchAnimations 再現: Bristol Mercury と同一パターン)
        float p2000  = Math.max(0.1f, 5.0f   * p + 0.4f);
        float p6000  = Math.max(0.1f, 2.385f  * p);
        float p10000 = Math.max(0.1f, 1.575f  * p);
        float p14000 = Math.max(0.1f, 1.35f   * p);

        // ボリューム (IV volumeAnimations 再現 × 外部/内部音量係数)
        float extVol = SoundConfig.PW610F_EXTERIOR_VOLUME;
        float intVol = SoundConfig.PW610F_INTERIOR_VOLUME;

        // 修正: 低回転レイヤーをLycomingのv800と同様に全域で残す (-p+0.95)
        // 旧式: -2.0f*p+1.25f → p=0.625で消滅し、v14000がまだ小さいためデッドゾーン発生
        float v2000  = clamp(-p + 0.95f)                              * extVol;
        float v6000  = clamp(1.0f - Math.abs(p - 0.45f) / 0.25f)     * extVol;
        float v10000 = clamp(1.0f - Math.abs(p - 0.55f) / 0.28f)     * extVol;
        // 修正: 高回転レイヤーをLycomingのv2500と同様の緩やかな立ち上がりに (1.5p-0.35)
        // 旧式: (1.75p-1.0)*(0.5p)*4.0 → p≈0.85まで極小のためデッドゾーンの一因
        float v14000 = clamp(1.5f * p - 0.35f)                       * extVol;

        float v2000i  = clamp(-p + 0.95f)                              * intVol;
        float v6000i  = clamp(1.0f - Math.abs(p - 0.45f) / 0.25f)     * intVol;
        float v10000i = clamp(1.0f - Math.abs(p - 0.55f) / 0.28f)     * intVol;
        float v14000i = clamp(1.5f * p - 0.35f)                       * intVol;

        // 距離スケール (isInterior 中は外部音すべてOFF)
        float sN     = isInterior ? 0f : tri(dist, 0, 0, 1.0f, 60, 0.1f, 120, 0f);
        float sD2    = isInterior ? 0f : tri(dist, 40, 100, 0.5f, 140, 0f, 140, 0f);
        float sD6    = isInterior ? 0f : tri(dist, 50, 130, 0.5f, 230, 0f, 230, 0f);
        float sD10   = isInterior ? 0f : tri(dist, 40, 140, 0.5f, 250, 0f, 250, 0f);
        float sD14   = isInterior ? 0f : tri(dist, 50, 150, 0.5f, 300, 0f, 300, 0f);
        float sM2    = isInterior ? 0f : tri(dist,100, 140, 0.3f, 260, 0f, 260, 0f);
        float sM6    = isInterior ? 0f : tri(dist,120, 230, 0.3f, 350, 0f, 350, 0f);
        float sM10   = isInterior ? 0f : tri(dist,140, 240, 0.3f, 380, 0f, 380, 0f);
        float sM14   = isInterior ? 0f : tri(dist,150, 250, 0.3f, 550, 0f, 550, 0f);
        float sI     = isInterior ? 1.0f : 0f;

        r2000   = loop(r2000,   v2000  * sN,   p2000,  Pw610fSounds.RUNNING_2000.get());
        r2000i  = loop(r2000i,  v2000i * sI,   p2000,  Pw610fSounds.RUNNING_2000_INSIDE.get());
        r2000d  = loop(r2000d,  v2000  * sD2,  p2000,  Pw610fSounds.RUNNING_2000_DISTANT.get());
        r2000m  = loop(r2000m,  v2000  * sM2,  p2000,  Pw610fSounds.RUNNING_2000_MOREDISTANT.get());

        r6000   = loop(r6000,   v6000  * sN,   p6000,  Pw610fSounds.RUNNING_6000.get());
        r6000i  = loop(r6000i,  v6000i * sI,   p6000,  Pw610fSounds.RUNNING_6000_INSIDE.get());
        r6000d  = loop(r6000d,  v6000  * sD6,  p6000,  Pw610fSounds.RUNNING_6000_DISTANT.get());
        r6000m  = loop(r6000m,  v6000  * sM6,  p6000,  Pw610fSounds.RUNNING_6000_MOREDISTANT.get());

        r10000  = loop(r10000,  v10000  * sN,   p10000, Pw610fSounds.RUNNING_10000.get());
        r10000i = loop(r10000i, v10000i * sI,   p10000, Pw610fSounds.RUNNING_10000_INSIDE.get());
        r10000d = loop(r10000d, v10000  * sD10, p10000, Pw610fSounds.RUNNING_10000_DISTANT.get());
        r10000m = loop(r10000m, v10000  * sM10, p10000, Pw610fSounds.RUNNING_10000_MOREDISTANT.get());

        r14000  = loop(r14000,  v14000  * sN,   p14000, Pw610fSounds.RUNNING_14000.get());
        r14000i = loop(r14000i, v14000i * sI,   p14000, Pw610fSounds.RUNNING_14000_INSIDE.get());
        r14000d = loop(r14000d, v14000  * sD14, p14000, Pw610fSounds.RUNNING_14000_DISTANT.get());
        r14000m = loop(r14000m, v14000  * sM14, p14000, Pw610fSounds.RUNNING_14000_MOREDISTANT.get());
    }

    private EngineLoop loop(EngineLoop cur, float vol, float pitch, SoundEvent sound) {
        vol = clamp(vol);
        if (vol > THRESHOLD) {
            if (cur == null || cur.isStopped()) {
                cur = new EngineLoop(sound, entity);
                Minecraft.getInstance().getSoundManager().play(cur);
            }
            cur.setVolume(vol); cur.setPitch(pitch); return cur;
        }
        if (cur != null && !cur.isStopped()) cur.markStopped();
        return null;
    }

    private void one(SoundEvent e, float volume) {
        entity.level().playLocalSound(
                entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                e, entity.getSoundSource(), volume, 1f, false);
    }

    private void stopAll() {
        r2000=kill(r2000); r2000i=kill(r2000i); r2000d=kill(r2000d); r2000m=kill(r2000m);
        r6000=kill(r6000); r6000i=kill(r6000i); r6000d=kill(r6000d); r6000m=kill(r6000m);
        r10000=kill(r10000); r10000i=kill(r10000i); r10000d=kill(r10000d); r10000m=kill(r10000m);
        r14000=kill(r14000); r14000i=kill(r14000i); r14000d=kill(r14000d); r14000m=kill(r14000m);
    }

    private static EngineLoop kill(EngineLoop l) {
        if (l != null && !l.isStopped()) l.markStopped(); return null;
    }
    private static float clamp(float v) { return Math.max(0f, Math.min(1f, v)); }
    private static float tri(double d, double s, double m, float vm, double e, float ve, double u, float u2) {
        if (d<=s) return vm; if (d>=e) return 0f;
        if (d<=m) return (float)((d-s)/Math.max(1e-4,m-s))*vm;
        return vm+(float)((d-m)/Math.max(1e-4,e-m))*(ve-vm);
    }

    public void stop() { crank=kill(crank); stopAll(); }

    public static final class EngineLoop extends AbstractTickableSoundInstance {
        private final Entity entity; private boolean dead=false;
        EngineLoop(SoundEvent s, Entity e) {
            super(s,SoundSource.NEUTRAL,SoundInstance.createUnseededRandom());
            entity=e; looping=true; delay=0; volume=0.05f; pitch=1f;
            attenuation=Attenuation.NONE; sync();
        }
        @Override public void tick(){if(entity.isRemoved()||dead){stop();return;}sync();}
        @Override public boolean isStopped(){return dead||entity.isRemoved()||super.isStopped();}
        private void sync(){x=(float)entity.getX();y=(float)entity.getY();z=(float)entity.getZ();}
        public void setVolume(float v){this.volume=Math.max(0.001f,Math.min(1f,v));}
        public void setPitch(float p){this.pitch=Math.max(0.01f,p);}
        public void markStopped(){dead=true;}
    }
}
