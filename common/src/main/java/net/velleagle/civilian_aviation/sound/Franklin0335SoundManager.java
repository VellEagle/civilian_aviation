package net.velleagle.civilian_aviation.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;

/**
 * Franklin O-335 エンジンサウンドマネージャ。
 * 使用機体: Bell47g（オープンコックピット = interior音なし）
 *
 * IV の maxRPM=14000。axis=[0, 7e-5, 0] → pitch = 7e-5 * rpm
 * IAのenginePower≈rpm/maxRPM → rpm = power * 14000
 * pitch = 7e-5 * 14000 * power = 0.98 * power + offset
 * → running:     pitch = 0.98p + 0.35  (offset=0.35)
 * → distant:     pitch = 0.63p + 0.65  (axis=4.5e-5*14000=0.63, offset=0.65)
 * → moredistant: pitch = 0.84p + 0.75  (axis=6e-5*14000=0.84, offset=0.75)
 */
public class Franklin0335SoundManager {

    private static final float THRESHOLD = 0.01f;

    private final HelicopterEntity entity;

    private EngineLoop crankLoop;
    private EngineLoop running, runningIn;
    private EngineLoop distant, moreDistant;

    private boolean prevRunning = false;
    private float   prevTarget  = 0f;

    public Franklin0335SoundManager(HelicopterEntity entity) {
        this.entity = entity;
    }

    /**
     * Bell47gはオープンコックピットのため isInterior は不使用（常にfalseで呼ぶ）。
     */
    public void tick(float power, float target, double dist) {
        boolean running  = power > 0.05f;
        boolean starting = target > prevTarget + 0.05f;

        if (running  && !prevRunning) one(Franklin0335Sounds.STARTING.get());
        if (!running &&  prevRunning) one(Franklin0335Sounds.STOPPING.get());
        if (starting)                  one(Franklin0335Sounds.CRANKING_INTRO.get());

        prevRunning = running;
        prevTarget  = target;

        // クランキングループ
        crankLoop = loop(crankLoop, starting ? 0.8f : 0f, 1.0f, Franklin0335Sounds.CRANKING_LOOP.get());

        if (!running) { stopRunning(); return; }

        float p = power;

        // ピッチ (IV axis * maxRPM * enginePower + offset)
        float pRun   = Math.max(0.1f, 0.98f * p + 0.35f);
        float pDist  = Math.max(0.1f, 0.63f * p + 0.65f);
        float pMore  = Math.max(0.1f, 0.84f * p + 0.75f);

        // ボリューム
        // IV running:        minDist=0 midDist=50 maxDist=150, minV=1.0 midV=0.4
        // IV running_inside: minDist=0 midDist=10 maxDist=40,  minV=0.5 midV=0.2 (外部近距離補完)
        // IV distant:        minDist=0 midDist=120 maxDist=270, midV=0.6
        // IV moredistant:    minDist=120 midDist=185 maxDist=350, midV=0.3
        float vMain = SoundConfig.FRANKLIN_VOLUME;

        float sNear    = tri(dist,   0,   0, 1.0f,   50, 0.4f, 150, 0f);
        float sNearIn  = tri(dist,   0,   0, 0.5f,   10, 0.2f,  40, 0f);  // running_inside (外部補完)
        float sDist    = tri(dist,   0, 120, 0.6f,  270, 0.0f, 270, 0f);
        float sMore    = tri(dist, 120, 185, 0.3f,  350, 0.0f, 350, 0f);

        this.running    = loop(this.running,    vMain * sNear,   pRun,  Franklin0335Sounds.RUNNING.get());
        this.runningIn  = loop(this.runningIn,  vMain * sNearIn, pRun,  Franklin0335Sounds.RUNNING_INSIDE.get());
        this.distant    = loop(this.distant,    vMain * sDist,   pDist, Franklin0335Sounds.DISTANT.get());
        this.moreDistant= loop(this.moreDistant,vMain * sMore,   pMore, Franklin0335Sounds.MORE_DISTANT.get());
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

    private void one(SoundEvent e) {
        entity.level().playLocalSound(
                entity.getX(), entity.getY() + entity.getBbHeight() * 0.5, entity.getZ(),
                e, entity.getSoundSource(), 1f, 1f, false);
    }

    private void stopRunning() {
        running=kill(running); runningIn=kill(runningIn);
        distant=kill(distant); moreDistant=kill(moreDistant);
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

    public void stop() { crankLoop=kill(crankLoop); stopRunning(); }

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
