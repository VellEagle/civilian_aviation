package net.velleagle.civilian_aviation.client;

import com.mojang.blaze3d.vertex.PoseStack;
import immersive_aircraft.client.render.entity.renderer.utils.BBModelRenderer;
import immersive_aircraft.resources.BBModelLoader;
import immersive_aircraft.resources.bbmodel.BBModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.velleagle.civilian_aviation.entity.bullet.BasicBombEntity;
import org.jetbrains.annotations.NotNull;

public class BasicBombRenderer extends EntityRenderer<BasicBombEntity> {
    public BasicBombRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.2f;
    }

    @Override
    public void render(@NotNull BasicBombEntity entity, float entityYaw, float partialTicks,
                       PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight) {
        matrixStack.pushPose();

        // 爆弾の位置オフセット
        matrixStack.translate(0.0, 0.1, 0.0);

        // 回転を適用
        float yaw = entity.getYRot();
        float pitch = entity.getXRot();
        matrixStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(-yaw));   // Yaw
        matrixStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));  // Pitch

        // モデルを取得して描画
        BBModel model = BBModelLoader.MODELS.get(ResourceLocation.fromNamespaceAndPath("immersive_aircraft", "basicbomb"));
        if (model != null) {
            BBModelRenderer.renderModel(model, matrixStack, buffer, packedLight, partialTicks,
                    null, null, 1.0f, 1.0f, 1.0f, 1.0f);
        }

        matrixStack.popPose();
        super.render(entity, entityYaw, partialTicks, matrixStack, buffer, packedLight);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BasicBombEntity entity) {
        return ResourceLocation.fromNamespaceAndPath("immersive_aircraft", "textures/entity/tiny_tnt.png");
    }
}
