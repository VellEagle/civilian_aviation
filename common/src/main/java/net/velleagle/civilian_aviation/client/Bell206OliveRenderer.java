package net.velleagle.civilian_aviation.client;

import immersive_aircraft.client.render.entity.renderer.AircraftEntityRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.BBModelRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.entity.Bell206Olive;

public class Bell206OliveRenderer extends AircraftEntityRenderer<Bell206Olive> {
    private static final ResourceLocation ID = CivilianAviation.locate("bell206_olive");
    private static final float BLUR_START = 0.35f;
    private static final float BLUR_FULL  = 0.75f;
    private static final BBModelRenderer.VertexConsumerProvider TRANSLUCENT_PROVIDER =
            (source, container, face) -> source.getBuffer(RenderType.entityTranslucent(face.texture.location));

    private final ModelPartRenderHandler<Bell206Olive> model = new ModelPartRenderHandler<Bell206Olive>()
            .add("dyed_body", (model, object, vcp, entity, ms, light, time, mpr) ->
                    renderDyed(model, object, vcp, entity, ms, light, time, false, false))
            .add("dyed_body_highlights", (model, object, vcp, entity, ms, light, time, mpr) ->
                    renderDyed(model, object, vcp, entity, ms, light, time, true, false))
            .add("propeller_main", (model, object, vcp, entity, ms, light, time, mpr) -> {
                float alpha = calcBladeAlpha(entity.getEnginePower());
                if (alpha > 0.0f) BBModelRenderer.renderObjectInner(model, object, ms, vcp, light, time, entity,
                        new ModelPartRenderHandler<Bell206Olive>().vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                        1.0f, 1.0f, 1.0f, alpha);
            })
            .add("blur_propeller_main", (model, object, vcp, entity, ms, light, time, mpr) -> {
                float alpha = calcBlurAlpha(entity.getEnginePower());
                if (alpha > 0.0f) BBModelRenderer.renderObjectInner(model, object, ms, vcp, light, time, entity,
                        new ModelPartRenderHandler<Bell206Olive>().vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                        1.0f, 1.0f, 1.0f, alpha);
            })
            .add("propeller_tail", (model, object, vcp, entity, ms, light, time, mpr) -> {
                float alpha = calcBladeAlpha(entity.getEnginePower());
                if (alpha > 0.0f) BBModelRenderer.renderObjectInner(model, object, ms, vcp, light, time, entity,
                        new ModelPartRenderHandler<Bell206Olive>().vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                        1.0f, 1.0f, 1.0f, alpha);
            })
            .add("blur_propeller_rear", (model, object, vcp, entity, ms, light, time, mpr) -> {
                float alpha = calcBlurAlpha(entity.getEnginePower());
                if (alpha > 0.0f) BBModelRenderer.renderObjectInner(model, object, ms, vcp, light, time, entity,
                        new ModelPartRenderHandler<Bell206Olive>().vertexConsumerProvider(TRANSLUCENT_PROVIDER),
                        1.0f, 1.0f, 1.0f, alpha);
            });

    private static float calcBladeAlpha(float power) {
        if (power <= BLUR_START) return 1.0f;
        if (power >= BLUR_FULL)  return 0.0f;
        return 1.0f - (power - BLUR_START) / (BLUR_FULL - BLUR_START);
    }
    private static float calcBlurAlpha(float power) {
        if (power <= BLUR_START) return 0.0f;
        if (power >= BLUR_FULL)  return 0.5f;
        return (power - BLUR_START) / (BLUR_FULL - BLUR_START);
    }

    @Override protected ResourceLocation getModelId() { return ID; }
    public Bell206OliveRenderer(EntityRendererProvider.Context context) { super(context); }
    @Override protected ModelPartRenderHandler<Bell206Olive> getModel(AircraftEntity entity) { return model; }
}
