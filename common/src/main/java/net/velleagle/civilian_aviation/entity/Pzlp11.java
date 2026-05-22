package net.velleagle.civilian_aviation.entity;

import immersive_aircraft.entity.AircraftEntity;
import immersive_aircraft.entity.misc.Trail;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import java.util.List;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.velleagle.civilian_aviation.CivilianAviation;

public class Pzlp11 extends CivilianAircraftEntity {

    @Override
    public net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side getOnMountCloseSide() {
        return net.velleagle.civilian_aviation.network.c2s.DoorMessage.Side.RIGHT;
    }

    public Pzlp11(EntityType<? extends AircraftEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Override
    protected float[][] getTrailPositions() {
        // 左上翼端(index=0), 右上翼端(index=1)
        return new float[][] {
            { -5.807f,  2.754f,  -0.292f,  0.12f },  // 左上翼端
            {  5.807f,  2.754f,  -0.292f,  0.12f },  // 右上翼端
        };
    }


    @Override
    protected List<Trail> createTrails() {
        // Pzlp11: 左右上翼端コントレイル
        return List.of(new Trail(40, 1.0f), new Trail(40, 1.0f));
    }


    @Override
    public Item asItem() {
        return CivilianAviation.PZLP11_ITEM.get();
    }

    @Override
    public void setAnimationVariables(float tickDelta) {
        super.setAnimationVariables(tickDelta);

        // スロットル: エンジンパワーを 0.0〜1.0 でセット
        BBAnimationVariables.set("throttle", getEnginePower());

        // lever_stick (左右ロール): pressingInterpolatedX を -1.0〜1.0 でセット
        float stickX = pressingInterpolatedZ.getSmooth(tickDelta);
        BBAnimationVariables.set("lever_stick", stickX);

        // lever_stick_z (前後ピッチ): 前方(+)は 1.0 まで、後方(-)は -0.5 までにクランプ
        float stickZ = pressingInterpolatedX.getSmooth(tickDelta);
        float clampedZ = Math.max(-0.5f, Math.min(1.0f, stickZ));
        BBAnimationVariables.set("lever_stick_z", clampedZ);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public double getZoom() {
        return 8.0;
    }

    @Override
    protected float getEngineReactionSpeed() {
        return 140.0f;
    }
}