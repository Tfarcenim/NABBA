package tfar.nabba.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import tfar.nabba.blockentity.BetterBarrelBlockEntity;
import tfar.nabba.item.UpgradeItem;
import tfar.nabba.util.Upgrades;

public class FluidBarrelRenderer implements BlockEntityRenderer<BetterBarrelBlockEntity> {

    private final EntityRenderDispatcher dispatcher;
    private final Font font;
    private final ItemRenderer itemRenderer;

    public FluidBarrelRenderer(BlockEntityRendererProvider.Context pContext) {
        dispatcher = pContext.getEntityRenderer();
        font = pContext.getFont();
        itemRenderer = pContext.getItemRenderer();
    }

    public static final double zFighting = -.0001;

    @Override
    public void render(BetterBarrelBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        renderTextAndItems(pBlockEntity, pPoseStack, pBufferSource,pPackedLight,pPackedOverlay);
    }

    protected void renderTextAndItems(BetterBarrelBlockEntity betterBarrelBlockEntity,PoseStack pPoseStack,MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {
        ItemStack stack = betterBarrelBlockEntity.getItemHandler().getStack();

        boolean infiniteVend = betterBarrelBlockEntity.hasUpgrade(Upgrades.INFINITE_VENDING);

        int cap = betterBarrelBlockEntity.getStorage() * 64;
        String toDraw = infiniteVend ? "\u221E" :stack.getCount() + " / "+ cap;

        renderText(pPoseStack, bufferSource, pPackedLight, pPackedOverlay,toDraw,14/16d, betterBarrelBlockEntity.getColor(),.0075f);
        if (Minecraft.getInstance().player.getMainHandItem().getItem() instanceof UpgradeItem upgradeItem) {
            String slots = betterBarrelBlockEntity.getUsedSlots() + " / " + betterBarrelBlockEntity.getTotalUpgradeSlots();
            renderText(pPoseStack, bufferSource, pPackedLight, pPackedOverlay, slots, 3 / 16d,
                    betterBarrelBlockEntity.canAcceptUpgrade(upgradeItem.getDataStack()) ? 0x00ffff : 0xff0000, .0075f);
        }

        renderItem(betterBarrelBlockEntity, pPoseStack, bufferSource, pPackedLight, pPackedOverlay);
    }

    protected void renderText(PoseStack pPoseStack, MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay, String text, double yHeight,int color,float dScale) {

        int width = font.width(text);
        //text starts in bottom left

        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0);
        float f2 = -width / 2f;
        int j = (int)(f1 * 255.0F) << 24;

        {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, yHeight, zFighting);
            pPoseStack.scale(-dScale, -dScale, dScale);
            Matrix4f matrix4f = pPoseStack.last().pose();
            font.drawInBatch(text, f2 + .5f, 0, color, false, matrix4f, bufferSource, false, j, LightTexture.FULL_BRIGHT);
            pPoseStack.popPose();
        }
        {
            pPoseStack.pushPose();
            pPoseStack.translate(1 - zFighting, yHeight, .5);
            pPoseStack.scale(-dScale, -dScale, dScale);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));
            Matrix4f matrix4f = pPoseStack.last().pose();
            font.drawInBatch(text, f2 + .5f, 0, color, false, matrix4f, bufferSource, false, j, LightTexture.FULL_BRIGHT);
            pPoseStack.popPose();
        }
        {
            pPoseStack.pushPose();
            pPoseStack.translate(0.5D, yHeight, 1 - zFighting);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));
            pPoseStack.scale(-dScale, -dScale, dScale);
            Matrix4f matrix4f = pPoseStack.last().pose();
            font.drawInBatch(text, f2 + .5f, 0, color, false, matrix4f, bufferSource, false, j, LightTexture.FULL_BRIGHT);
            pPoseStack.popPose();
        }
        {
            pPoseStack.pushPose();
            pPoseStack.translate(zFighting, yHeight, .5);
            pPoseStack.scale(-dScale, -dScale, dScale);
            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));
            Matrix4f matrix4f = pPoseStack.last().pose();
            font.drawInBatch(text, f2 + .5f, 0, color, false, matrix4f, bufferSource, false, j, LightTexture.FULL_BRIGHT);
            pPoseStack.popPose();
        }
    }

    protected void renderItem(BetterBarrelBlockEntity betterBarrelBlockEntity,PoseStack pPoseStack,MultiBufferSource bufferSource, int pPackedLight, int pPackedOverlay) {

        float scale = (float) betterBarrelBlockEntity.getSize();
        if (scale < .01) return;

        ItemStack stack = betterBarrelBlockEntity.getItemHandler().getStack();

        if (stack.isEmpty() && !betterBarrelBlockEntity.hasGhost()) return;

        if (stack.isEmpty())stack = betterBarrelBlockEntity.getGhost();
        if (stack.isEmpty())return;


        try {
            pPoseStack.pushPose();
            pPoseStack.translate(.5,.5,1 - zFighting);
            pPoseStack.mulPoseMatrix(Matrix4f.createScaleMatrix(scale,scale, 0.0001f));
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, betterBarrelBlockEntity.getLevel(), null, 0);
            if (bakedmodel.isGui3d())
                pPoseStack.last().normal().mul(ITEM_LIGHT_ROTATION_3D);
            else
                pPoseStack.last().normal().mul(ITEM_LIGHT_ROTATION_FLAT);
            itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, pPoseStack, bufferSource, LightTexture.FULL_BRIGHT, pPackedOverlay, bakedmodel);
        } catch (Exception e) {
            //bruh
        }
        pPoseStack.popPose();

        try {
            pPoseStack.pushPose();
            pPoseStack.translate(1 - zFighting,.5,.5);

            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(90));

            pPoseStack.mulPoseMatrix(Matrix4f.createScaleMatrix(scale,scale, 0.0001f));


            BakedModel bakedmodel = this.itemRenderer.getModel(stack, betterBarrelBlockEntity.getLevel(), null, 0);
            if (bakedmodel.isGui3d())
                pPoseStack.last().normal().mul(ITEM_LIGHT_ROTATION_3D);
            else
                pPoseStack.last().normal().mul(ITEM_LIGHT_ROTATION_FLAT);
            itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, pPoseStack, bufferSource, LightTexture.FULL_BRIGHT, pPackedOverlay, bakedmodel);
        } catch (Exception e) {
            //bruh
        }
        pPoseStack.popPose();


        try {
            pPoseStack.pushPose();
            pPoseStack.translate(.5,.5,zFighting);

            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(180));

            pPoseStack.mulPoseMatrix(Matrix4f.createScaleMatrix(scale,scale, 0.0001f));
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, betterBarrelBlockEntity.getLevel(), null, 0);
            if (bakedmodel.isGui3d())
                pPoseStack.last().normal().mul(ITEM_LIGHT_ROTATION_3D);
            else
                pPoseStack.last().normal().mul(ITEM_LIGHT_ROTATION_FLAT);
            itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, pPoseStack, bufferSource, LightTexture.FULL_BRIGHT, pPackedOverlay, bakedmodel);
        } catch (Exception e) {
            //bruh
        }
        pPoseStack.popPose();


        try {
            pPoseStack.pushPose();
            pPoseStack.translate(zFighting,.5,.5);

            pPoseStack.mulPose(Vector3f.YP.rotationDegrees(270));

            pPoseStack.mulPoseMatrix(Matrix4f.createScaleMatrix(scale,scale, 0.0001f));
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, betterBarrelBlockEntity.getLevel(), null, 0);
            if (bakedmodel.isGui3d())
                pPoseStack.last().normal().mul(ITEM_LIGHT_ROTATION_3D);
            else
                pPoseStack.last().normal().mul(ITEM_LIGHT_ROTATION_FLAT);
            itemRenderer.render(stack, ItemTransforms.TransformType.GUI, false, pPoseStack, bufferSource, LightTexture.FULL_BRIGHT, pPackedOverlay, bakedmodel);
        } catch (Exception e) {
            //bruh
        }
        pPoseStack.popPose();
    }


    private static final Quaternion ITEM_LIGHT_ROTATION_3D = Util.make(() -> {
        Quaternion quaternion = new Quaternion(Vector3f.XP, -15f, true);
        quaternion.mul(new Quaternion(Vector3f.YP, 15f, true));
        return quaternion;
    });
    private static final Quaternion ITEM_LIGHT_ROTATION_FLAT = new Quaternion(Vector3f.XP, -45f, true);

}
