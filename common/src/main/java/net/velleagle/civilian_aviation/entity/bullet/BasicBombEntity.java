package net.velleagle.civilian_aviation.entity.bullet;

import immersive_aircraft.config.Config;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BasicBombEntity extends PrimedTnt {
    public static final float EXPLOSION_POWER = 6.0f;
    private boolean initVelocity = false; // 初速設定済みフラグ

    public BasicBombEntity(EntityType<? extends PrimedTnt> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        // 初速は1回だけ設定
        if (!initVelocity && this.getOwner() != null) {
            // 親の速度は無視して初速のみ設定
            Vec3 look = this.getOwner().getLookAngle();
            Vec3 adjusted = new Vec3(look.x, Math.min(0, look.y), look.z).normalize();

            // 完全に速度をリセットして初速を設定
            this.setDeltaMovement(adjusted.scale(0.3));
            initVelocity = true;
        }

        // 落下処理（重力加算 + 移動）
        if (!this.isNoGravity()) {
            // 速度上限を設定して落下を遅くする
            Vec3 motion = this.getDeltaMovement();
            double maxFallSpeed = -1.2; // 落下速度の上限
            if (motion.y + -0.08 < maxFallSpeed) {
                motion = new Vec3(motion.x, maxFallSpeed, motion.z);
            } else {
                motion = motion.add(0.0, -0.08, 0.0);
            }
            this.setDeltaMovement(motion);
        }

        // 移動方向に回転を合わせる
        Vec3 motion = this.getDeltaMovement();
        if (!motion.equals(Vec3.ZERO)) {
            float yaw = (float) (Math.atan2(motion.z, motion.x) * 180.0D / Math.PI) - 90.0F;
            float pitch = (float) (-(Math.atan2(motion.y, Math.sqrt(motion.x * motion.x + motion.z * motion.z)) * 180.0D / Math.PI));
            this.setYRot(yaw);
            this.setXRot(pitch);
        }

        // 地面に落ちたらFuseをゼロにして即爆発
        if (onGround()) {
            this.setFuse(0);
        }

        // 爆発カウントダウン
        if (this.getFuse() <= 0) {
            this.discard();
            if (!this.level().isClientSide) {
                this.boom();
            }
        } else {
            this.updateInWaterStateAndDoFluidPushing();
            if (this.level().isClientSide) {
                this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }

    private void boom() {
        // 実際の爆発処理
        this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), EXPLOSION_POWER,
                Config.getInstance().weaponsAreDestructive ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.BLOCK);

        // クライアント側のみ追加エフェクトを発生させる
        if (this.level().isClientSide) {
            int particleCount = (int)(EXPLOSION_POWER * 250); // 威力に応じて数を増やす
            for (int i = 0; i < particleCount; i++) {
                double offsetX = (this.random.nextDouble() - 0.5) * 6.0;
                double offsetY = (this.random.nextDouble()) * 3.0;
                double offsetZ = (this.random.nextDouble() - 0.5) * 6.0;

                double speedX = (this.random.nextDouble() - 0.5) * 0.5;
                double speedY = (this.random.nextDouble()) * 0.5;
                double speedZ = (this.random.nextDouble() - 0.5) * 0.5;

                this.level().addParticle(ParticleTypes.EXPLOSION_EMITTER,
                        this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                        speedX, speedY, speedZ);
            }
        }
    }
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        // 描画距離を大きめに固定
        double renderDistance = 4096.0; // 64ブロックくらいの距離
        return distance < renderDistance;
    }

}

