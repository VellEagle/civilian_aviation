package net.velleagle.civilian_aviation.entity.weapon;

import immersive_aircraft.Sounds;
import immersive_aircraft.cobalt.network.NetworkHandler;
import immersive_aircraft.config.Config;
import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.entity.bullet.BulletEntity;
import immersive_aircraft.entity.misc.WeaponMount;
import immersive_aircraft.entity.weapon.BulletWeapon;
import immersive_aircraft.entity.weapon.RotationalManager;
import immersive_aircraft.network.c2s.FireMessage;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static immersive_aircraft.Entities.BULLET;

public class Observerguns extends BulletWeapon {

    private static final float MAX_ANGLE_DEG = 20.0f;
    private static final float MAX_ANGLE_RAD = (float) Math.toRadians(MAX_ANGLE_DEG);

    private final RotationalManager rotationalManager = new RotationalManager(this);
    private float fireRollCounter = 0.0f;
    private float flashIntensity = 0.0f;
    private static final float FLASH_DECAY = 0.35f;

    // --- マウントの向きに依存しないローカルYaw（左右）アニメーション用 ---
    private float currentYawDeg = 0.0f;
    private float prevYawDeg = 0.0f;
    private float targetYawDeg = 0.0f;

    public Observerguns(VehicleEntity entity, ItemStack stack, WeaponMount mount, int slot) {
        super(entity, stack, mount, slot);
    }

    @Override
    protected Vector4f getBarrelOffset() {
        // 発射起点を機銃の根元にする
        return new Vector4f(0.0f, 0.1f, 0.0f, 1.0f);
    }

    public float getVelocity() {
        return 4.7f;
    }

    public float getInaccuracy() {
        return 0.0f;
    }

    @Override
    protected Entity getBullet(Vector4f position, Vector3f direction) {
        BulletEntity bullet = BULLET.get().create(getEntity().level());
        if (bullet == null) return null;

        // 銃身の先端から発射させるオフセット（必要に応じて数値を調整）
        float barrelLength = 1.5f;
        double spawnX = position.x() + direction.x() * barrelLength;
        double spawnY = position.y() + direction.y() * barrelLength;
        double spawnZ = position.z() + direction.z() * barrelLength;

        bullet.setPos(spawnX, spawnY, spawnZ);
        bullet.setOwner(getEntity());

        // 銃口が向いている方向（direction）にまっすぐ飛ばす
        bullet.setDeltaMovement(new Vec3(
                direction.x() * getVelocity(),
                direction.y() * getVelocity(),
                direction.z() * getVelocity()
        ));

        return bullet;
    }

    @Override
    public void tick() {
        rotationalManager.tick();

        Vector3f desired = getClampedMouseDirection();
        rotationalManager.pointTo(getEntity(), desired);

        // --- Yawの滑らかな補間処理（自前で計算） ---
        prevYawDeg = currentYawDeg;
        float diff = targetYawDeg - currentYawDeg;

        // -180〜180の最短距離にならす
        while (diff > 180.0f) diff -= 360.0f;
        while (diff < -180.0f) diff += 360.0f;

        // 追従スピード（0.3fで滑らかに動く）
        currentYawDeg += diff * 0.3f;

        if (flashIntensity > 0.0f) {
            flashIntensity = Math.max(0.0f, flashIntensity - FLASH_DECAY);
        }
    }

    private Vector3f getClampedMouseDirection() {
        VehicleEntity vehicle = getEntity();

        Vector3f globalDir = rotationalManager.screenToGlobal(vehicle);

        Matrix3f vehicleInv = new Matrix3f(vehicle.getVehicleNormalTransform()).invert();
        Matrix3f mountInv   = new Matrix3f(getMount().transform()).invert();

        Vector3f localDir = new Vector3f(globalDir);
        vehicleInv.transform(localDir);
        mountInv.transform(localDir);

        // 上下0度制限
        localDir.y = 0.0f;

        if (localDir.lengthSquared() < 1e-6f) {
            localDir.set(0.0f, 0.0f, 1.0f);
        } else {
            localDir.normalize();
        }

        float cosMax = (float) Math.cos(MAX_ANGLE_RAD);
        if (localDir.z < cosMax) {
            float sinMax = (float) Math.sin(MAX_ANGLE_RAD);
            float sign = Math.signum(localDir.x);
            localDir.set(sign * sinMax, 0.0f, cosMax);
        }

        // --- マウントに対するローカルな目標Yaw角度を計算 ---
        // ここで計算することで、設置位置が逆でも相対的に正しく動く
        targetYawDeg = (float) Math.toDegrees(Math.atan2(-localDir.x, localDir.z));

        Matrix3f mountMat   = new Matrix3f(getMount().transform());
        Matrix3f vehicleMat = new Matrix3f(vehicle.getVehicleNormalTransform());
        Vector3f globalResult = new Vector3f(localDir);
        mountMat.transform(globalResult);
        vehicleMat.transform(globalResult);
        return globalResult.normalize();
    }

    @Override
    public void fire(Vector3f direction) {
        if (spentAmmo(Config.getInstance().gunpowderAmmunition, 10)) {
            super.fire(direction);
        }
    }

    @Override
    public SoundEvent getSound() {
        return Sounds.CANNON.get();
    }

    @Override
    public void clientFire(int index) {
        float old = fireRollCounter;
        fireRollCounter += 0.25f;

        if (Math.floor(old) != Math.floor(fireRollCounter)) {
            NetworkHandler.sendToServer(new FireMessage(getSlot(), index, getClampedMouseDirection()));
            flashIntensity = 1.0f;
        }
    }

    @Override
    public <T extends VehicleEntity> void setAnimationVariables(T entity, float time) {
        super.setAnimationVariables(entity, time); // 標準の変数を一旦セット

        float tickDelta = time % 1.0f;

        // --- BBModelのアニメーション変数をローカル角度で強制的に上書き ---
        float lerpedYaw = prevYawDeg + (currentYawDeg - prevYawDeg) * tickDelta;

        BBAnimationVariables.set("yaw", lerpedYaw);
        BBAnimationVariables.set("pitch", 0.0f); // 上下は動かないので強制0
    }
}