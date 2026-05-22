package net.velleagle.civilian_aviation.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.velleagle.civilian_aviation.entity.CivilianAircraftEntity;

/**
 * Bristol Mercury エンジンサウンドマネージャ。
 * 使用機体: TrimotorBlue, Pzl37Los（interior有）, Pzlp11（interior無）
 *
 * IV の engine_rpm 段階:
 *   800rpm  (clamp[0,0]): アイドル域 — enginePower ≈ 0〜0.15
 *   800rpm  (clamp[1,1]): 低回転     — enginePower ≈ 0.1〜0.45  pitch=5p+0.4, vol=-2p+1.25
 *   1300rpm (clamp[1,1]): 中低回転   — enginePower ≈ 0.25〜0.65 pitch=2.385p, vol複合
 *   1800rpm (clamp[1,1]): 中高回転   — enginePower ≈ 0.4〜0.85  pitch=1.575p, vol複合
 *   2300rpm (clamp[1,1]): 高回転     — enginePower ≈ 0.55〜1.0  pitch=1.35p,  vol複合
 *
 */
public class BristolMercurySoundManager {

    private static final float THRESHOLD = 0.01f;

    private final CivilianAircraftEntity entity;
    private final boolean hasInterior;

    private EngineLoop crank;
    // アイドル域 800rpm
    private EngineLoop idle, idleIn;
    // 低回転 800rpm-b
    private EngineLoop r800b, r800bIn, r800bDist, r800bMore;
    // 中低回転 1300rpm
    private EngineLoop r1300, r1300In, r1300Dist, r1300More;
    // 中高回転 1800rpm
    private EngineLoop r1800, r1800In, r1800Dist, r1800More;
    // 高回転 2300rpm
    private EngineLoop r2300, r2300In, r2300Dist, r2300More;

    private boolean prevRunning = false;
    private float   prevTarget  = 0f;

    public BristolMercurySoundManager(CivilianAircraftEntity entity, boolean hasInterior) {
        this.entity      = entity;
        this.hasInterior = hasInterior;
    }

    public void tick(float power, float target, boolean isRider, boolean isInterior, double dist) {
        boolean running  = power > 0.05f;
        boolean starting = target > prevTarget + 0.05f;

        if (running  && !prevRunning) one(BristolMercurySounds.STARTING.get());
        if (!running &&  prevRunning) one(BristolMercurySounds.STOPPING.get());
        if (starting)                  one(BristolMercurySounds.CRANKING.get());

        prevRunning = running;
        prevTarget  = target;

        // クランキングループ
        crank = loop(crank, starting ? 0.8f : 0f, 1.0f, BristolMercurySounds.CRANKING.get());

        if (!running) { stopAll(); return; }

        float p = power;

        // =========================================================
        // ピッチ計算 (IV pitchAnimations axis[Y] * enginePower)
        // =========================================================
        // アイドル800: axis≈0.001 → ほぼ固定ピッチ
        float pIdle = 1.0f;
        // 低回転800b: axis=5, offset=0.4
        float p800b  = Math.max(0.1f, 5.0f   * p + 0.4f);
        // 中低1300:  axis=2.385, offset=0
        float p1300  = Math.max(0.1f, 2.385f  * p);
        // 中高1800:  axis=1.575, offset=0
        float p1800  = Math.max(0.1f, 1.575f  * p);
        // 高回転2300: axis=1.35, offset=0
        float p2300  = Math.max(0.1f, 1.35f   * p);

        // =========================================================
        // ボリューム計算 (IV volumeAnimations 再現 × MASTER_VOLUME)
        //
        // 800(idle): axis=[0,0.001,0] offset=-0.01 → ほぼ0（clamp[0,0]域なので
        //            IAではengineTargetが上昇直後の僅かな時間だけ）
        //            簡略化: p < 0.15 のときのみ微量
        // 800b: vol = -2*p + 1.25  (p=0 → 1.25→clamp1.0, p=0.625 → 0)
        // 1300: vol複合 = (clamp(-11p+1*p+0.3p+0.8)) * (0.25p)
        //       = (clamp((-9.7p+0.8)) * (0.25p)   y軸成分が主体
        //       近似: ピーク p≈0.45 の山形
        // 1800: vol複合 = (clamp(-10p+1p+0.6p+1)) * (-0.15p)
        //       y軸: 1p+1, x軸: -10p → 合算 (1-10)*p = -9p+1 → 0になるのはp≈0.11
        //       掛け算: (-9p+1) * (-0.15p) → 負値になる…
        //       IVのvolumeAnimationsは複数定義時に乗算。axis多軸はスカラーで合算。
        //       axis=[-10,1,0.6] → y成分1.0*p+1, これにaxis=[0,-0.15,0]を乗算:
        //       vol = (1.0*p+1) * (-0.15*p) → 常に負 → クランプで0
        //       → 実際はoffset=1が別途加算: vol = (-0.15p)*(p+1)+1
        //       近似: ピーク p≈0.5 の山形 max(0, 1-|p-0.5|/0.25)
        // 2300: vol複合 axis=[-7.5→skip,1.75,0] offset=-1, axis=[0,0.5,0]
        //       = (1.75p-1) * (0.5p) = 0.875p²-0.5p → ピーク p=1: 0.375
        //       近似: max(0, 1.75p-1) × max(0,0.5p) — 単純化
        // =========================================================

        // =========================================================
        // ボリューム計算 (IV volumeAnimations 再現 × 外部/内部音量係数)
        // =========================================================
        float extVol = SoundConfig.BRISTOL_EXTERIOR_VOLUME;
        float intVol = SoundConfig.BRISTOL_INTERIOR_VOLUME;

        float vIdle = (p < 0.15f) ? clamp((p / 0.15f) * 0.6f) * extVol : 0f;
        // 修正: 低回転レイヤーをLycomingのv800と同様に全域で残す (-p+0.95)
        // 旧式: -2.0f*p+1.25f → p=0.625で消滅し、v2300がまだ小さいためデッドゾーン発生
        float v800b  = clamp(-p + 0.95f)                               * extVol;
        float v1300  = clamp(1.0f - Math.abs(p - 0.45f) / 0.25f)      * extVol;
        float v1800  = clamp(1.0f - Math.abs(p - 0.55f) / 0.28f)      * extVol;
        // 修正: 高回転レイヤーをLycomingのv2500と同様の緩やかな立ち上がりに (1.5p-0.35)
        // 旧式: (1.75p-1.0)*(0.5p)*4.0 → p≈0.85まで極小のためデッドゾーンの一因
        float v2300  = clamp(1.5f * p - 0.35f)                        * extVol;

        // 内部音量 (hasInterior=true のとき専用INSIDEファイルを使用)
        float vIdleIn = (p < 0.15f) ? clamp((p / 0.15f) * 0.6f) * intVol : 0f;
        float v800bIn  = clamp(-p + 0.95f)                               * intVol;
        float v1300In  = clamp(1.0f - Math.abs(p - 0.45f) / 0.25f)      * intVol;
        float v1800In  = clamp(1.0f - Math.abs(p - 0.55f) / 0.28f)      * intVol;
        float v2300In  = clamp(1.5f * p - 0.35f)                        * intVol;

        // 距離スケール (isInterior 中は外部音すべてOFF)
        float sN    = isInterior ? 0f : tri(dist, 0, 0, 1.0f, 20, 0.3f, 100, 0f);
        float sD1   = isInterior ? 0f : tri(dist, 40, 100, 0.6f, 140, 0f, 140, 0f);   // 800b distant
        float sD13  = isInterior ? 0f : tri(dist, 50, 120, 0.6f, 230, 0f, 230, 0f);   // 1300 distant
        float sD18  = isInterior ? 0f : tri(dist, 40, 140, 0.6f, 240, 0f, 240, 0f);   // 1800 distant
        float sD23  = isInterior ? 0f : tri(dist, 50, 150, 0.6f, 250, 0f, 250, 0f);   // 2300 distant
        float sM1   = isInterior ? 0f : tri(dist,100, 140, 0.4f, 260, 0f, 260, 0f);   // 800b more
        float sM13  = isInterior ? 0f : tri(dist,120, 230, 0.4f, 350, 0f, 350, 0f);
        float sM18  = isInterior ? 0f : tri(dist,140, 240, 0.4f, 380, 0f, 380, 0f);
        float sM23  = isInterior ? 0f : tri(dist,150, 250, 0.4f, 400, 0f, 400, 0f);

        // 内部スケール:
        //   hasInterior=true  → 専用 INSIDE ファイルを使用（Trimotor / Pzl37Los）
        //   hasInterior=false → オープンコックピット相当（Pzlp11）:
        //                       一人称時も機外の音をそのまま近距離音として聴こえる。
        //                       外部ループを一人称でもONにするため sN を上書きする。
        float sI;
        if (hasInterior) {
            // 専用 INSIDE サウンドあり: isInterior のときのみ INSIDE ループを有効化
            sI = isInterior ? 1.0f : 0f;
        } else {
            // INSIDE サウンドなし (Pzlp11): 一人称でも外部近距離音をそのまま聴かせる
            // → 外部音を isInterior でも止めない（sN を再計算）
            if (isInterior) {
                // 一人称搭乗中: 距離0扱いで近距離外部音をフル音量で流す
                sN = 1.0f;
            }
            sI = 0f; // INSIDE ループは使わない
        }

        // アイドル域
        // hasInterior=false (Pzlp11) のとき: 一人称でも外部音をそのまま鳴らす (sN で制御済み)
        float idleExtScale = hasInterior ? (isInterior ? 0f : 1f) : 1f;
        idle   = loop(idle,   vIdle   * idleExtScale, pIdle, BristolMercurySounds.RUNNING_800.get());
        idleIn = loop(idleIn, vIdleIn * sI,           pIdle, BristolMercurySounds.RUNNING_800_INSIDE.get());

        // 低回転800b
        r800b     = loop(r800b,     v800b   * sN,  p800b, BristolMercurySounds.RUNNING_800B.get());
        r800bIn   = loop(r800bIn,   v800bIn * sI,  p800b, BristolMercurySounds.RUNNING_800B_INSIDE.get());
        r800bDist = loop(r800bDist, v800b   * sD1, p800b, BristolMercurySounds.RUNNING_800B_DISTANT.get());
        r800bMore = loop(r800bMore, v800b   * sM1, p800b, BristolMercurySounds.RUNNING_800B_MOREDISTANT.get());

        // 中低回転1300
        r1300     = loop(r1300,     v1300   * sN,   p1300, BristolMercurySounds.RUNNING_1300.get());
        r1300In   = loop(r1300In,   v1300In * sI,   p1300, BristolMercurySounds.RUNNING_1300_INSIDE.get());
        r1300Dist = loop(r1300Dist, v1300   * sD13, p1300, BristolMercurySounds.RUNNING_1300_DISTANT.get());
        r1300More = loop(r1300More, v1300   * sM13, p1300, BristolMercurySounds.RUNNING_1300_MOREDISTANT.get());

        // 中高回転1800
        r1800     = loop(r1800,     v1800   * sN,   p1800, BristolMercurySounds.RUNNING_1800.get());
        r1800In   = loop(r1800In,   v1800In * sI,   p1800, BristolMercurySounds.RUNNING_1800_INSIDE.get());
        r1800Dist = loop(r1800Dist, v1800   * sD18, p1800, BristolMercurySounds.RUNNING_1800_DISTANT.get());
        r1800More = loop(r1800More, v1800   * sM18, p1800, BristolMercurySounds.RUNNING_1800_MOREDISTANT.get());

        // 高回転2300
        r2300     = loop(r2300,     v2300   * sN,   p2300, BristolMercurySounds.RUNNING_2300.get());
        r2300In   = loop(r2300In,   v2300In * sI,   p2300, BristolMercurySounds.RUNNING_2300_INSIDE.get());
        r2300Dist = loop(r2300Dist, v2300   * sD23, p2300, BristolMercurySounds.RUNNING_2300_DISTANT.get());
        r2300More = loop(r2300More, v2300   * sM23, p2300, BristolMercurySounds.RUNNING_2300_MOREDISTANT.get());
    }

    // ---- ユーティリティ -------------------------------------------------------
    private EngineLoop loop(EngineLoop cur, float vol, float pitch, SoundEvent sound) {
        vol = clamp(vol);
        if (vol > THRESHOLD) {
            if (cur == null || cur.isStopped()) {
                cur = new EngineLoop(sound, entity);
                Minecraft.getInstance().getSoundManager().play(cur);
            }
            cur.setVolume(vol); cur.setPitch(pitch);
            return cur;
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
        idle=kill(idle); idleIn=kill(idleIn);
        r800b=kill(r800b); r800bIn=kill(r800bIn); r800bDist=kill(r800bDist); r800bMore=kill(r800bMore);
        r1300=kill(r1300); r1300In=kill(r1300In); r1300Dist=kill(r1300Dist); r1300More=kill(r1300More);
        r1800=kill(r1800); r1800In=kill(r1800In); r1800Dist=kill(r1800Dist); r1800More=kill(r1800More);
        r2300=kill(r2300); r2300In=kill(r2300In); r2300Dist=kill(r2300Dist); r2300More=kill(r2300More);
    }

    private static EngineLoop kill(EngineLoop l) {
        if (l != null && !l.isStopped()) l.markStopped(); return null;
    }
    private static float clamp(float v) { return Math.max(0f, Math.min(1f, v)); }
    private static float tri(double d, double s, double m, float vm, double e, float ve, double u, float u2) {
        if (d <= s) return vm;
        if (d >= e) return 0f;
        if (d <= m) return (float)((d-s)/Math.max(1e-4,m-s))*vm;
        return vm+(float)((d-m)/Math.max(1e-4,e-m))*(ve-vm);
    }

    public void stop() { crank=kill(crank); stopAll(); }

    // ---- EngineLoop -----------------------------------------------------------
    public static final class EngineLoop extends AbstractTickableSoundInstance {
        private final Entity entity; private boolean dead=false;
        EngineLoop(SoundEvent s, Entity e) {
            super(s,SoundSource.NEUTRAL,SoundInstance.createUnseededRandom());
            entity=e; looping=true; delay=0; volume=0.05f; pitch=1f;
            attenuation=Attenuation.NONE; sync();
        }
        @Override public void tick() { if(entity.isRemoved()||dead){stop();return;} sync(); }
        @Override public boolean isStopped(){return dead||entity.isRemoved()||super.isStopped();}
        private void sync(){x=(float)entity.getX();y=(float)entity.getY();z=(float)entity.getZ();}
        public void setVolume(float v){this.volume=Math.max(0.001f,Math.min(1f,v));}
        public void setPitch(float p){this.pitch=Math.max(0.01f,p);}
        public void markStopped(){dead=true;}
    }
}
