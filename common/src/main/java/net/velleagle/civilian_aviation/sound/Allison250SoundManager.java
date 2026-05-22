package net.velleagle.civilian_aviation.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.velleagle.civilian_aviation.entity.HelicopterEntity;

/**
 * Allison 250 タービンエンジンサウンドマネージャ。
 * 使用機体: Bell206Blackstripe (HelicopterEntity)
 *
 * IV の構造:
 *   crankingintro: スターター中（外部＋内部）
 *   running_turbine: タービン音、enginePower 0〜1 全域、pitch=4p, vol=clamp(2.5p,0.4)
 *   running_low: アイドル域(power低め)、pitch=2.5p+0.1, vol=clamp(10p-1.5)
 *   running_high: フルパワー域、pitch=p+0.5, vol≈距離比例
 *   running_distant / moredistant: 遠距離外部
 */
public class Allison250SoundManager {

    private static final float THRESHOLD = 0.01f;

    private final HelicopterEntity entity;

    // クランキング
    private EngineLoop crankExt, crankInt;

    // タービン（全域）
    private EngineLoop turbine, turbineIn;

    // 低回転（アイドル域）
    private EngineLoop runLow, runLowIn;

    // 高回転（フルパワー域）
    private EngineLoop runHigh, runHighIn;

    // 遠距離
    private EngineLoop runDist, runMore;

    private boolean prevRunning = false;
    private float   prevTarget  = 0f;

    public Allison250SoundManager(HelicopterEntity entity) {
        this.entity = entity;
    }

    public void tick(float power, float target, boolean isRider, boolean isInterior, double dist) {
        boolean running  = power > 0.03f;
        boolean starting = target > prevTarget + 0.05f;

        if (starting) one(Allison250Sounds.CRANKING_INTRO.get());

        prevRunning = running;
        prevTarget  = target;

        // クランキングループ
        crankExt = loop(crankExt, starting && !isInterior ? 1.0f : 0f, 1.0f, Allison250Sounds.CRANKING_INTRO.get());
        crankInt = loop(crankInt, starting &&  isInterior ? 1.0f : 0f, 1.0f, Allison250Sounds.CRANKING_INTRO_INSIDE.get());

        if (!running) { stopAll(); return; }

        float p = power;

        // =========================================================
        // ピッチ (IV pitchAnimations axis[Y] * p + offset)
        // タービン: axis=4.0, offset=0
        // 低回転:   axis=2.5, offset=0.1
        // 高回転:   axis=1.0, offset=0.5
        // 遠距離:   axis=1.0, offset=0.4
        // 超遠距離: axis=0.92, offset=0.46
        // =========================================================
        float pTurb  = Math.max(0.1f, 4.0f  * p);
        float pLow   = Math.max(0.1f, 2.5f  * p + 0.1f);
        float pHigh  = Math.max(0.1f, 1.0f  * p + 0.5f);
        float pDist  = Math.max(0.1f, 1.0f  * p + 0.4f);
        float pMore  = Math.max(0.1f, 0.92f * p + 0.46f);

        // =========================================================
        // ボリューム (IV volumeAnimations 再現)
        // タービン: vol=clamp(2.5p, max=0.4)
        // 低回転:   vol=clamp(10p-1.5) → p>0.15で有効
        // 高回転:   IV では距離定義のみ（vol固定1.0）
        // =========================================================
        float extVol = SoundConfig.ALLISON250_EXTERIOR_VOLUME;
        float intVol = SoundConfig.ALLISON250_INTERIOR_VOLUME;

        float vTurb = clamp(Math.min(0.4f, 2.5f * p))  * extVol;
        float vLow  = clamp(10.0f * p - 1.5f)           * extVol;
        float vHigh = clamp(p > 0.3f ? (p - 0.3f) / 0.7f : 0f) * extVol;
        float vTurbIn = clamp(Math.min(0.4f, 2.5f * p)) * intVol;
        float vLowIn  = clamp(10.0f * p - 1.5f)          * intVol;
        float vHighIn = clamp(p > 0.3f ? (p - 0.3f) / 0.7f : 0f) * intVol;

        // 距離スケール (isInterior 中は外部音すべてOFF)
        float sN    = isInterior ? 0f : tri(dist,  0,   0, 1.0f,  45, 0.1f,  80, 0f);
        float sDist = isInterior ? 0f : tri(dist,  0,  80, 0.2f, 150, 0.0f, 150, 0f);
        float sMore = isInterior ? 0f : tri(dist,120, 185, 0.3f, 350, 0.0f, 350, 0f);
        float sTurb = isInterior ? 0f : tri(dist,  0,  35, 0.1f,  80, 0.0f,  80, 0f);
        float sI    = isInterior ? 1.0f : 0f;

        // タービン（全域）
        turbine   = loop(turbine,   vTurb * (isInterior ? 0f : 1f), pTurb, Allison250Sounds.RUNNING_TURBINE.get());
        turbineIn = loop(turbineIn, vTurbIn * sI,                    pTurb, Allison250Sounds.RUNNING_TURBINE_INSIDE.get());

        // 低回転
        runLow   = loop(runLow,   vLow * sN, pLow, Allison250Sounds.RUNNING_LOW.get());
        runLowIn = loop(runLowIn, vLowIn * sI, pLow, Allison250Sounds.RUNNING_LOW_INSIDE.get());

        // 高回転
        runHigh   = loop(runHigh,   vHigh * sN, pHigh, Allison250Sounds.RUNNING_HIGH.get());
        runHighIn = loop(runHighIn, vHighIn * sI, pHigh, Allison250Sounds.RUNNING_HIGH_INSIDE.get());

        // 遠距離
        runDist = loop(runDist, vHigh * sDist, pDist, Allison250Sounds.RUNNING_DISTANT.get());
        runMore = loop(runMore, vHigh * sMore, pMore, Allison250Sounds.RUNNING_MOREDISTANT.get());
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

    private void stopAll() {
        turbine=kill(turbine); turbineIn=kill(turbineIn);
        runLow=kill(runLow); runLowIn=kill(runLowIn);
        runHigh=kill(runHigh); runHighIn=kill(runHighIn);
        runDist=kill(runDist); runMore=kill(runMore);
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

    public void stop() {
        crankExt=kill(crankExt); crankInt=kill(crankInt); stopAll();
    }

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
