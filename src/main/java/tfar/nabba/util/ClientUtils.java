package tfar.nabba.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import tfar.nabba.client.FluidSpriteCache;

public class ClientUtils {

    public static void renderFluid(PoseStack matrices, int x, int y, FluidStack fluidStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        int color = renderProperties.getTintColor(fluidStack);
        TextureAtlasSprite sprite = FluidSpriteCache.getStillTexture(fluidStack);
        RenderSystem.setShaderColor((color >> 16 & 0xff) / 255f, (color >> 8 & 0xff) / 255f, (color & 0xff) / 255f,1);
        RenderSystem.enableDepthTest();

        GuiComponent.blit(matrices,x,y, 0, 16, 16, sprite);

        drawSmallNumbers(matrices,x,y,0,fluidStack);

    }

    public static void renderFluidTooltip(PoseStack matrices, int x, int y, FluidStack fluidStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        int color = renderProperties.getTintColor(fluidStack);
        TextureAtlasSprite sprite = FluidSpriteCache.getStillTexture(fluidStack);
        RenderSystem.setShaderColor((color >> 16 & 0xff) / 255f, (color >> 8 & 0xff) / 255f, (color & 0xff) / 255f,1);

        RenderSystem.enableDepthTest();

        GuiComponent.blit(matrices,x,y, 400, 16, 16, sprite);

        drawSmallNumbers(matrices,x,y,500,fluidStack);
    }

    public static void drawSmallNumbers(PoseStack matrices, int x, int y,int z, FluidStack fluidStack) {
        PoseStack viewModelPose = RenderSystem.getModelViewStack();
        viewModelPose.pushPose();
        viewModelPose.translate(x + 16, y + 12, z);
        float scale = .5f;
        viewModelPose.scale(scale, scale, scale);
        viewModelPose.translate(-1 * x, -1 * y, 0);
        RenderSystem.applyModelViewMatrix();
        String s = Utils.formatLargeNumber(fluidStack.getAmount());

        Minecraft.getInstance().font.drawShadow(matrices,s,x - Minecraft.getInstance().font.width(s),y ,0xffffff);
        viewModelPose.popPose();
        RenderSystem.applyModelViewMatrix();
    }
}
