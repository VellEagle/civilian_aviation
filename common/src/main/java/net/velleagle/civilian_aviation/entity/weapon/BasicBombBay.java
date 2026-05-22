package net.velleagle.civilian_aviation.entity.weapon;

import immersive_aircraft.cobalt.network.NetworkHandler;
import immersive_aircraft.config.Config;
import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.entity.misc.WeaponMount;
import immersive_aircraft.entity.weapon.BulletWeapon;
import immersive_aircraft.network.c2s.FireMessage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.velleagle.civilian_aviation.entity.bullet.BasicBombEntity;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import static net.velleagle.civilian_aviation.CivilianAviation.BASIC_BOMB;

public class BasicBombBay extends BulletWeapon {
    private static final float MAX_COOLDOWN = 1.0f;
    private float cooldown = 0.0f;

    public BasicBombBay(VehicleEntity entity, ItemStack stack, WeaponMount mount, int slot) {
        super(entity, stack, mount, slot);
    }

    @Override
    protected float getBarrelLength() {
        return 0.25f;
    }

    @Override
    protected Vector4f getBarrelOffset() {
        return new Vector4f(0.0f, -5.0f, 0.0f, 1.0f);
    }

    public float getVelocity() {
        return 0.0f;
    }

    @Override
    protected Entity getBullet(Vector4f position, Vector3f direction) {
        BasicBombEntity bomb = new BasicBombEntity(BASIC_BOMB.get(), getEntity().level());
        bomb.setPos(position.x(), position.y(), position.z());

        // 速度を機体向きに合わせて設定
        direction.normalize().mul(0.4f); // 初速（調整可能）
        bomb.setDeltaMovement(direction.x, direction.y, direction.z);

        // 機体の回転を爆弾に適用
        bomb.setYRot(getEntity().getYRot());
        bomb.setXRot(getEntity().getXRot());

        return bomb;
    }

    @Override
    public void tick() {
        cooldown -= 1.0f / 20.0f;
    }

    @Override
    public void fire(Vector3f direction) {
        if (spentAmmo(Config.getInstance().bombBayAmmunition, 20)) {
            super.fire(direction);
        }
    }

    @Override
    public void clientFire(int index) {
        if (cooldown <= 0.0f) {
            cooldown = MAX_COOLDOWN;
            NetworkHandler.sendToServer(new FireMessage(getSlot(), index, getDirection()));
        }
    }

    private Vector3f getDirection() {
        Vector3f direction = new Vector3f(0, 1.0f, 0);
        direction.mul(new Matrix3f(getMount().transform()));
        direction.mul(getEntity().getVehicleNormalTransform());
        return direction;
    }
}
