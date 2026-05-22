package net.velleagle.civilian_aviation.client;

import immersive_aircraft.client.render.entity.renderer.AircraftEntityRenderer;
import immersive_aircraft.client.render.entity.renderer.utils.ModelPartRenderHandler;
import immersive_aircraft.entity.AircraftEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.velleagle.civilian_aviation.CivilianAviation;
import net.velleagle.civilian_aviation.entity.E500Extravagant;

public class E500ExtravagantRenderer extends AircraftEntityRenderer<E500Extravagant> {
    private static final ResourceLocation ID = CivilianAviation.locate("e500_extravagant");

    private final ModelPartRenderHandler<E500Extravagant> model = new ModelPartRenderHandler<E500Extravagant>()
            .add("dyed_body", (model, object, vcp, entity, ms, light, time, mpr) ->
                    renderDyed(model, object, vcp, entity, ms, light, time, false, false))
            .add("dyed_body_highlights", (model, object, vcp, entity, ms, light, time, mpr) ->
                    renderDyed(model, object, vcp, entity, ms, light, time, true, false));

    @Override protected ResourceLocation getModelId() { return ID; }
    public E500ExtravagantRenderer(EntityRendererProvider.Context context) { super(context); }
    @Override protected ModelPartRenderHandler<E500Extravagant> getModel(AircraftEntity entity) { return model; }
}
