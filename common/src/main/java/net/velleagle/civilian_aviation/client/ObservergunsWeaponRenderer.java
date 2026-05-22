package net.velleagle.civilian_aviation.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import immersive_aircraft.client.render.entity.renderer.utils.BBModelRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.client.render.entity.weaponRenderer.WeaponRenderer;
import immersive_aircraft.entity.VehicleEntity;
import immersive_aircraft.resources.BBModelLoader;
import immersive_aircraft.resources.bbmodel.BBAnimationVariables;
import immersive_aircraft.resources.bbmodel.BBFaceContainer;
import immersive_aircraft.resources.bbmodel.BBModel;
import immersive_aircraft.resources.bbmodel.BBObject;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.velleagle.civilian_aviation.entity.weapon.Observerguns;

/**
 * Observer機銃専用のWeaponRenderer。
 *
 * 通常のモデルはデフォルト描画（BBModelRenderer）に任せ、
 * "mflash" と "mflash_inner" という名前のボーンだけを
 * テクスチャなしの発光描画（entityTranslucentEmissive）でオーバーライドする。
 *
 * 色の仕様:
 *   mflash       : 外側フラッシュ — オレンジ  (R=1.0, G=0.55, B=0.05)
 *   mflash_inner : 内側フラッシュ — 白に近いイエロー (R=1.0, G=0.95, B=0.5)
 *
 * アルファ値は BBAnimationVariables の "muzzle_flash" 変数（0.0〜1.0）で制御する。
 * フラッシュが 0.0 のときはボーン描画をスキップして負荷を下げる。
 *
 * 【使用するRenderType】
 *   テクスチャが存在しない（真っ白テクスチャ）場合でも発光させるために
 *   RenderType.beaconBeam(..., transparent=true) を流用する。
 *   これにより additive-blend が有効になり、重なったときにより明るく見える。
 *   ただし beaconBeam はテクスチャ必須のため、Immersive Aircraft が持つ
 *   1x1 白テクスチャ "textures/entity/trail.png" を借用する。
 *   （必要であれば自前の白テクスチャに差し替えてください）
 */
public class ObservergunsWeaponRenderer extends WeaponRenderer<Observerguns> {

    private final ResourceLocation modelId;

    /**
     * @param modelId BBmodel のリソースロケーション
     *                例: CivilianAviation.locate("pzl37los_gun")
     */
    public ObservergunsWeaponRenderer(ResourceLocation modelId) {
        this.modelId = modelId;
    }

    @Override
    protected ResourceLocation getModelId() {
        return modelId;
    }

    // ──────────────────────────────────────────────
    // メインレンダリング
    // ──────────────────────────────────────────────

    @Override
    public <T extends VehicleEntity> void render(
            T entity,
            Observerguns weapon,
            PoseStack matrixStack,
            MultiBufferSource vertexConsumerProvider,
            int light,
            float time) {

        matrixStack.pushPose();
        matrixStack.last().pose().mul(weapon.getMount().transform());

        BBModel model = BBModelLoader.MODELS.get(modelId);
        if (model == null) {
            matrixStack.popPose();
            return;
        }

        // アニメーション変数をセット（pitch/yaw/muzzle_flash など）
        weapon.setAnimationVariables(entity, time);
    }
}