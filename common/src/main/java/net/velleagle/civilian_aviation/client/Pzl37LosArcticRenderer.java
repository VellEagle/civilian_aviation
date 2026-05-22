package net.velleagle.civilian_aviation.client;

import immersive_aircraft.client.render.entity.renderer.AircraftEntityRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.BBModelRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.entity.Pzl37LosArctic;

public class Pzl37LosArcticRenderer extends AircraftEntityRenderer<Pzl37LosArctic> {
    private static final ResourceLocation ID = CivilianAviation.locate("pzl37los_arctic");
    private static final float BLUR_START = 0.35f;
    private static final float BLUR_FULL  = 0.75f;
    private static final BBModelRenderer.VertexConsumerProvider TRANSLUCENT_PROVIDER =
            (source, container, face) -> source.getBuffer(RenderType.entityTranslucent(face.texture.location));

    private final ModelPartRenderHandler<Pzl37LosArctic> model = new ModelPartRenderHandler<Pzl37LosArctic>()
            .add("dyed_body", (model, object, vcp, entity, ms, light, time, mpr) ->
                    renderDyed(model, object, vcp, entity, ms, light, time, false, false))
            .add("dyed_body_highlights", (model, object, vcp, entity, ms, light, time, mpr) ->
                    renderDyed(model, object, vcp, entity, ms, light, time, true, false))
            .add("propeller_l2", (model, object, vcp, entity, ms, light, time, mpr) -> {
                float alpha = calcBladeAlpha(entity.getEnginePower());
                if (alpha > 0.0f) BBModelRenderer.renderObjectInner(model, object, ms, vcp, light, time, entity,
                        new ModelPartRenderHandler<Pzl37LosArctic>().vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                        1.0f, 1.0f, 1.0f, alpha);
            })
            .add("blur_propeller_l", (model, object, vcp, entity, ms, light, time, mpr) -> {
                float alpha = calcBlurAlpha(entity.getEnginePower());
                if (alpha > 0.0f) BBModelRenderer.renderObjectInner(model, object, ms, vcp, light, time, entity,
                        new ModelPartRenderHandler<Pzl37LosArctic>().vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                        1.0f, 1.0f, 1.0f, alpha);
            })
            .add("propeller_r2", (model, object, vcp, entity, ms, light, time, mpr) -> {
                float alpha = calcBladeAlpha(entity.getEnginePower());
                if (alpha > 0.0f) BBModelRenderer.renderObjectInner(model, object, ms, vcp, light, time, entity,
                        new ModelPartRenderHandler<Pzl37LosArctic>().vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                        1.0f, 1.0f, 1.0f, alpha);
            })
            .add("blur_propeller_r", (model, object, vcp, entity, ms, light, time, mpr) -> {
                float alpha = calcBlurAlpha(entity.getEnginePower());
                if (alpha > 0.0f) BBModelRenderer.renderObjectInner(model, object, ms, vcp, light, time, entity,
                        new ModelPartRenderHandler<Pzl37LosArctic>().vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                        1.0f, 1.0f, 1.0f, alpha);
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

    @Override protected ResourceLocation getModelId() { return ID; }
    public Pzl37LosArcticRenderer(EntityRendererProvider.Context context) { super(context); }
    @Override protected ModelPartRenderHandler<Pzl37LosArctic> getModel(AircraftEntity entity) { return model; }
}
