package com.oierbravo.melter.content.melter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MelterBlockRenderer implements BlockEntityRenderer<MelterBlockEntity> {

    public MelterBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MelterBlockEntity pBlockEntity, float pPartialTick, @NotNull PoseStack pPoseStack, @NotNull MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        FluidStack fluidStack = pBlockEntity.getFluidHandler().getFluidInTank(0);
        int amount = fluidStack.getAmount();
        int total = pBlockEntity.getFluidHandler().getTankCapacity(0);
        float percent = (amount / (float) total);
        if(!fluidStack.isEmpty()){

            this.renderFluidInTank(pBlockEntity.getLevel(), pBlockEntity.getBlockPos(), fluidStack, pPoseStack, pBufferSource, percent, pPackedLight, pPackedOverlay);
        }

        ItemStack itemStack = pBlockEntity.getItemHandler().getStackInSlot(0);

        if(!itemStack.isEmpty()){
            pPoseStack.pushPose();
            if (itemStack.getItem() instanceof BlockItem) {
                pPoseStack.translate(0.5d, 0.8d * percent, 0.5d);
            }
            else {
                pPoseStack.translate(0.5d, 0.8d * percent + 0.175d, 0.6d);
                pPoseStack.rotateAround(Axis.XN.rotationDegrees(90), 0, 0, 0);
            }
            this.renderBlock(pPoseStack,pBufferSource,pPackedLight,pPackedOverlay,itemStack,pBlockEntity);
            pPoseStack.popPose();
        }
    }
    private void renderFluidInTank(BlockAndTintGetter world, BlockPos pos, FluidStack fluidStack, PoseStack matrix, MultiBufferSource buffer, float percent, int pPackedLight, int pPackedOverlay) {
        matrix.pushPose();
        matrix.translate(0.5d, 0.565d, 0.5d);
        Matrix4f matrix4f = matrix.last().pose();
        Matrix3f matrix3f = matrix.last().normal();

        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions clientFluid = IClientFluidTypeExtensions.of(fluid);
        TextureAtlasSprite fluidTexture = Minecraft.getInstance()
                .getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(clientFluid.getStillTexture(fluidStack));

        int color = clientFluid.getTintColor(fluidStack);

        VertexConsumer builder = buffer.getBuffer(RenderType.translucent());
        this.renderTopFluidFace(fluidTexture, matrix4f, matrix3f, builder, color, percent, pPackedLight, pPackedOverlay);
        matrix.popPose();

    }

    private void renderTopFluidFace(TextureAtlasSprite sprite, Matrix4f matrix4f, Matrix3f normalMatrix, VertexConsumer builder, int color, float percent, int pPackedLight, int pPackedOverlay) {
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = ((color) & 0xFF) / 255f;
        float a = ((color >> 24) & 0xFF) / 255f;

        float width = 10 / 16f;
        float height = 12 / 16f;

        float minU = sprite.getU(4F / 16F);
        float maxU = sprite.getU(16F / 16F);
        float minV = sprite.getV(4F / 16F);
        float maxV = sprite.getV(16F / 16F);

        float pY = -height / 2 + percent * height;

        builder.addVertex(matrix4f, -width / 2, pY , -width / 2).setColor(r, g, b, a)
                .setUv(minU, minV)
                .setLight(pPackedLight)
                .setOverlay(pPackedOverlay)
                .setNormal(0, 1, 0);

        builder.addVertex(matrix4f, -width / 2, pY, width / 2).setColor(r, g, b, a)
                .setUv(minU, maxV)
                .setLight(pPackedLight)
                .setOverlay(pPackedOverlay)
                .setNormal(0, 1, 0);

        builder.addVertex(matrix4f, width / 2, pY, width / 2).setColor(r, g, b, a)
                .setUv(maxU, maxV)
                .setLight(pPackedLight)
                .setOverlay(pPackedOverlay)
                .setNormal(0, 1, 0);

        builder.addVertex(matrix4f, width / 2, pY, -width / 2).setColor(r, g, b, a)
                .setUv(maxU, minV)
                .setLight(pPackedLight)
                .setOverlay(pPackedOverlay)
                .setNormal(0, 1, 0);
    }

    protected void renderBlock(PoseStack ms, MultiBufferSource buffer, int light, int overlay, ItemStack stack, MelterBlockEntity entity) {
        Minecraft.getInstance()
                .getItemRenderer()
                .renderStatic(stack, ItemDisplayContext.GROUND, light, overlay, ms, buffer,  entity.getLevel(),0);
    }
}
