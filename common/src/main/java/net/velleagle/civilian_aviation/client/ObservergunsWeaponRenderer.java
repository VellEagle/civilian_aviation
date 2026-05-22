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
        matrixStack.mulPoseMatrix(weapon.getMount().transform());

        BBModel model = BBModelLoader.MODELS.get(modelId);
        if (model == null) {
            matrixStack.popPose();
            return;
        }

        // アニメーション変数をセット（pitch/yaw/muzzle_flash など）
        weapon.setAnimationVariables(entity, time);

        // フラッシュ強度を取得（setAnimationVariables でセット済み）
        float flashIntensity = (float) BBAnimationVariables.REGISTRY
                .getOrDefault("muzzle_flash", new org.mariuszgromada.math.mxparser.Argument("variable_muzzle_flash", 0))
                .getArgumentValue();

        // ModelPartRenderHandler でフラッシュボーンだけカスタム描画
        ModelPartRenderHandler<T> handler = buildHandler(entity, vertexConsumerProvider, light, time, flashIntensity);

        BBModelRenderer.renderModel(model, matrixStack, vertexConsumerProvider, light, time, entity, handler, 1.0f, 1.0f, 1.0f, 1.0f);

        matrixStack.popPose();
    }

    // ──────────────────────────────────────────────
    // フラッシュボーン用ハンドラ構築
    // ──────────────────────────────────────────────

    private <T extends VehicleEntity> ModelPartRenderHandler<T> buildHandler(
            T entity,
            MultiBufferSource vertexConsumerProvider,
            int light,
            float time,
            float flashIntensity) {

        return new ModelPartRenderHandler<T>()
                // 外フラッシュ（大きめ・オレンジ）
                .add("mflash",
                        (model, object, vcp, ent, ms, lt, t, mph) ->
                                renderFlashBone(model, object, vcp, ms,
                                        1.0f, 0.55f, 0.05f, // orange
                                        flashIntensity))
                // 内フラッシュ（小さめ・白黄色）
                .add("mflash_inner",
                        (model, object, vcp, ent, ms, lt, t, mph) ->
                                renderFlashBone(model, object, vcp, ms,
                                        1.0f, 0.95f, 0.5f, // bright yellow-white
                                        Math.min(1.0f, flashIntensity * 1.3f))); // 内側は少し明るく
    }

    // ──────────────────────────────────────────────
    // フラッシュ単一ボーンの描画
    // ──────────────────────────────────────────────

    /**
     * テクスチャなし発光ポリゴンとしてボーン内の全フェイスを描画する。
     *
     * RenderType として beaconBeam (additive, transparent=true) を使用するため
     * テクスチャは必要だが内容は無視される。ここでは 1×1 の白テクスチャを借用。
     *
     * alpha が 0 以下のときはスキップして描画コストをゼロにする。
     */
    private static final ResourceLocation WHITE_TEXTURE =
            new ResourceLocation("immersive_aircraft", "textures/entity/trail.png");

    private void renderFlashBone(
            BBModel model,
            BBObject object,
            MultiBufferSource vcp,
            PoseStack ms,
            float r, float g, float b,
            float alpha) {

        if (alpha <= 0.0f) {
            // フラッシュが消えているときは何も描かない（ボーンを非表示に）
            return;
        }

        // beaconBeam は加算合成 (additive) かつ常に発光 (light=MAX)
        RenderType renderType = RenderType.beaconBeam(WHITE_TEXTURE, true);
        VertexConsumer consumer = vcp.getBuffer(renderType);

        // フルブライト（15728640 = LightTexture.FULL_BRIGHT）
        int fullBright = 0xF000F0;

        // BBFaceContainer であれば faces を直接描画する
        BBModelRenderer.renderFaces(
                (BBFaceContainer) object,
                ms, vcp, fullBright,
                r, g, b, alpha,
                // カスタム VertexConsumer プロバイダ: テクスチャに関わらず beaconBeam バッファを返す
                (source, container, face) -> consumer
        );
    }
}