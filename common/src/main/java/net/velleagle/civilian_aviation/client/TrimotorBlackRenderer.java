package net.velleagle.civilian_aviation.client;

import immersive_aircraft.client.render.entity.renderer.AircraftEntityRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.BBModelRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.entity.TrimotorBlack;

public class TrimotorBlackRenderer extends AircraftEntityRenderer<TrimotorBlack> {
    private static final ResourceLocation ID = CivilianAviation.locate("trimotor_black");

    private static final float BLUR_START = 0.35f;
    private static final float BLUR_FULL  = 0.75f;

    private static final BBModelRenderer.VertexConsumerProvider TRANSLUCENT_PROVIDER =
            (source, container, face) -> source.getBuffer(RenderType.entityTranslucent(face.texture.location));

    private final ModelPartRenderHandler<TrimotorBlack> model = new ModelPartRenderHandler<TrimotorBlack>()
            .add("dyed_body",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) ->
                            renderDyed(model, object, vertexConsumerProvider, entity, matrixStack, light, time, false, false))
            .add("dyed_body_highlights",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) ->
                            renderDyed(model, object, vertexConsumerProvider, entity, matrixStack, light, time, true, false))
            .add("propeller2",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBladeAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<TrimotorBlack>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    })
            .add("blur_propeller",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBlurAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<TrimotorBlack>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    })
            .add("propeller_l2",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBladeAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<TrimotorBlack>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    })
            .add("blur_propeller_l",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBlurAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<TrimotorBlack>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    })
            .add("propeller_r2",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBladeAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<TrimotorBlack>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    })
            .add("blur_propeller_r",
                    (model, object, vertexConsumerProvider, entity, matrixStack, light, time, modelPartRenderer) -> {
                        float alpha = calcBlurAlpha(entity.getEnginePower());
                        if (alpha > 0.0f) {
                            BBModelRenderer.renderObjectInner(
                                    model, object, matrixStack, vertexConsumerProvider,
                                    light, time, entity,
                                    new ModelPartRenderHandler<TrimotorBlack>()
                                            .vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                                    1.0f, 1.0f, 1.0f, alpha);
                        }
                    });

    private static float calcBladeAlpha(float power) {
        if (power <= BLUR_START) return 1.0f;
        if (power >= BLUR_FULL)  return 0.0f;
        return 1.0f - (power - BLUR_START) / (BLUR_FULL - BLUR_START);
    }

    private static float calcBlurAlpha(float power) {
        if (power <= BLUR_START) return 0.0f;
        if (power >= BLUR_FULL)  return 0.6f;
        return (power - BLUR_START) / (BLUR_FULL - BLUR_START);
    }

    @Override
    protected ResourceLocation getModelId() {
        return ID;
    }

    public TrimotorBlackRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected ModelPartRenderHandler<TrimotorBlack> getModel(AircraftEntity entity) {
        return model;
    }
}
