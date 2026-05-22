package net.velleagle.civilian_aviation.client;

import immersive_aircraft.client.render.entity.renderer.AircraftEntityRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.entity.E500Silver;

public class E500SilverRenderer extends AircraftEntityRenderer<E500Silver> {
    private static final ResourceLocation ID = CivilianAviation.locate("e500_silver");

    private final ModelPartRenderHandler<E500Silver> model = new ModelPartRenderHandler<E500Silver>()
            .add("dyed_body", (model, object, vcp, entity, ms, light, time, mpr) ->
                    renderDyed(model, object, vcp, entity, ms, light, time, false, false))
            .add("dyed_body_highlights", (model, object, vcp, entity, ms, light, time, mpr) ->
                    renderDyed(model, object, vcp, entity, ms, light, time, true, false));

    @Override protected ResourceLocation getModelId() { return ID; }
    public E500SilverRenderer(EntityRendererProvider.Context context) { super(context); }
    @Override protected ModelPartRenderHandler<E500Silver> getModel(AircraftEntity entity) { return model; }
}
