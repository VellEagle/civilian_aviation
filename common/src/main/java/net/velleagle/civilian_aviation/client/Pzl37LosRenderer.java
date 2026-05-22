package net.velleagle.civilian_aviation.client;

import immersive_aircraft.client.render.entity.renderer.AircraftEntityRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.BBModelRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.entity.Pzl37Los;

public class Pzl37LosRenderer extends AircraftEntityRenderer<Pzl37Los> {
    private static final ResourceLocation ID = CivilianAviation.locate("pzl37los");

    // -------------------------------------------------------
    // モーションブラーのしきい値
    //
    //   BLUR_START : ここからブラーオブジェクトがフェードイン開始
    //   BLUR_FULL  : ここでブラーオブジェクトが完全表示
    //               (同時にデフォルトプロペラが完全非表示になる)
    //
    // エンジン出力 (getEnginePower()) は 0.0〜1.0 の範囲
    // -------------------------------------------------------
    private static final float BLUR_START = 0.35f;
    private static final float BLUR_FULL  = 0.75f;

    // -------------------------------------------------------
    // 半透明描画用 VertexConsumerProvider
    //
    // デフォルトの entityCutoutNoCull はアルファ二値処理のため、
    // entityTranslucent に差し替えて 0〜1 のアルファを有効にする。
    // -------------------------------------------------------
    private static final BBModelRenderer.VertexConsumerProvider TRANSLUCENT_PROVIDER =
            (source, container, face) -> source.getBuffer(RenderType.entityTranslucent(face.texture.location));

    private final ModelPartRenderHandler<Pzl37Los> model = new ModelPartRenderHandler<Pzl37Los>()
            // 染色パーツ（変更なし）
            .add("dyed_body",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) ->
                            renderDyed(model, object, vertexConsumerProvider, entity, matrixStack, light, time, false, false))
            .add("dyed_body_highlights",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) ->
                            renderDyed(model, object, vertexConsumerProvider, entity, matrixStack, light, time, true, false))

            // ── メインローター（デフォルト）propeller_main ────────────────
            // エンジン出力が上がるにつれフェードアウト
            .add("propeller_l2",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBladeAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<Pzl37Los>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    })

            // ── メインローター（ブラー）blur_propeller_main ───────────────
            // エンジン出力が上がるにつれフェードイン
            .add("blur_propeller_l",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBlurAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<Pzl37Los>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    })

            // ── メインローター（デフォルト）propeller_main ────────────────
            // エンジン出力が上がるにつれフェードアウト
            .add("propeller_r2",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBladeAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<Pzl37Los>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    })

            // ── メインローター（ブラー）blur_propeller_main ───────────────
            // エンジン出力が上がるにつれフェードイン
            .add("blur_propeller_r",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBlurAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<Pzl37Los>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    });

    // -------------------------------------------------------
    // アルファ計算
    // -------------------------------------------------------

    /**
     * デフォルトプロペラのアルファ値（出力が上がるほど薄くなる）
     * <pre>
     *   power <= BLUR_START        → 1.0（完全表示）
     *   BLUR_START 〜 BLUR_FULL    → 線形フェードアウト
     *   power >= BLUR_FULL         → 0.0（完全非表示）
     * </pre>
     */
    private static float calcBladeAlpha(float power) {
        if (power <= BLUR_START) return 1.0f;
        if (power >= BLUR_FULL)  return 0.0f;
        return 1.0f - (power - BLUR_START) / (BLUR_FULL - BLUR_START);
    }

    /**
     * ブラーオブジェクトのアルファ値（出力が上がるほど濃くなる）
     * <pre>
     *   power <= BLUR_START        → 0.0（完全非表示）
     *   BLUR_START 〜 BLUR_FULL    → 線形フェードイン
     *   power >= BLUR_FULL         → 1.0（完全表示）
     * </pre>
     */
    private static float calcBlurAlpha(float power) {
        if (power <= BLUR_START) return 0.0f;
        if (power >= BLUR_FULL)  return 0.6f;
        return (power - BLUR_START) / (BLUR_FULL - BLUR_START);
    }

    // -------------------------------------------------------

    @Override
    protected ResourceLocation getModelId() {
        return ID;
    }

    public Pzl37LosRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected ModelPartRenderHandler<Pzl37Los> getModel(AircraftEntity entity) {
        return model;
    }
}