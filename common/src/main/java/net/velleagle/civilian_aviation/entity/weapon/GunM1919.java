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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static immersive_aircraft.Entities.BULLET;


public class GunM1919 extends BulletWeapon {
    private final RotationalManager rotationalManager = new RotationalManager(this);
    private float customRoll = 0.0f;

    public GunM1919(VehicleEntity entity, ItemStack stack, WeaponMount mount, int slot) {
        super(entity, stack, mount, slot);
    }

    @Override
    protected Vector4f getBarrelOffset() {
        return new Vector4f(0.0f, 0.1f, 0.0f, 1.0f);
    }

    public float getVelocity() {
        return 4.7f;
    }

    public float getInaccuracy() {
        return 0.0f;
    }

    // 🔹 3引数版のみ実装
    @Override
    protected Entity getBullet(Entity shooter, Vector4f position, Vector3f direction) {
        BulletEntity bullet = BULLET.get().create(shooter.level());
        if (bullet == null) return null;

        // 銃口のワールド座標
        Vector3f barrelPos = new Vector3f(position.x(), position.y(), position.z());

        // 発射した瞬間の固定された収束位置を取る
        Vector3f fixedConvergencePoint = getConvergencePoint();

        // 銃口位置から収束位置へのベクトルを作る
        Vector3f directionToConvergence = new Vector3f();
        fixedConvergencePoint.sub(barrelPos, directionToConvergence).normalize();

        // 弾を出現させる場所
        bullet.setPos(position.x(), position.y(), position.z());

        // 弾の所有者
        bullet.setOwner(shooter);

        // 弾の移動方向と速度を直接セット
        bullet.setDeltaMovement(
                new Vec3(
                        directionToConvergence.x() * getVelocity(),
                        directionToConvergence.y() * getVelocity(),
                        directionToConvergence.z() * getVelocity()
                )
        );

        return bullet;
    }

    /**
     * 機体の正面中央の座標を取得
     */
    private Vector3f getConvergencePoint() {
        VehicleEntity entity = getEntity();

        // 機体のワールド座標
        Vector3f planePosition = entity.position().toVector3f();

        Vector3f forward = new Vector3f(0, 0, 1.0f);
        Matrix3f rotation = new Matrix3f(entity.getVehicleNormalTransform());
        rotation.transform(forward);

        // 機体の前方 80m に収束点を設定
        return new Vector3f(planePosition).add(forward.mul(80.0f));
    }

    @Override
    public void tick() {
        rotationalManager.tick();
        rotationalManager.pointTo(getEntity());
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

    private Vector3f getDirection() {
        Vector3f direction = new Vector3f(0, 0, 1.0f);
        direction.mul(new Matrix3f(getMount().transform()));
        direction.mul(getEntity().getVehicleNormalTransform());
        return direction.normalize();
    }

    @Override
    public void clientFire(int index) {
        float old = customRoll;
        customRoll += 0.25f;

        if (Math.floor(old) != Math.floor(customRoll)) {
            NetworkHandler.sendToServer(new FireMessage(getSlot(), index, getDirection()));
        }
    }
}