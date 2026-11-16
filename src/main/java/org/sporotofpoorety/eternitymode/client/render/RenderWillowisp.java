package org.sporotofpoorety.eternitymode.client.render;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;


import org.sporotofpoorety.eternitymode.client.ShaderHandler;
import org.sporotofpoorety.eternitymode.entity.EntityWillowisp;




public class RenderWillowisp extends Render<EntityWillowisp> 
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("eternitymode", "textures/entity/willowisp.png");

    public RenderWillowisp(RenderManager renderManager) 
    {
        super(renderManager);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityWillowisp entity) 
    {
        return TEXTURE;
    }

    @Override
    public void doRender(EntityWillowisp entity, double x, double y, double z, float entityYaw, float partialTicks) 
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y + (float) (entity.height / 2.0D), (float) z);

        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);

        this.bindEntityTexture(entity);

        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

//        final double pulse = 0.5 + (Math.sin(entity.ticksExisted / 15f) + 1) / 2 * 0.5;
        final double pulse = 0.75 + (Math.sin(entity.ticksExisted / 15f) + 1) / 8;
        GlStateManager.scale(pulse, pulse, pulse);

        ShaderHandler.begin(ShaderHandler.WILLOWISP);

        final float half = entity.getWispScale();
        final float red = entity.getRed();
        final float green = entity.getGreen();
        final float blue = entity.getBlue();

        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder buffer = tessellator.getBuffer();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos(-half, -half, 0).tex(0, 1).color(red, green, blue, 1.0f).endVertex();
        buffer.pos(half, -half, 0).tex(1, 1).color(red, green, blue, 1.0f).endVertex();
        buffer.pos(half, half, 0).tex(1, 0).color(red, green, blue, 1.0f).endVertex();
        buffer.pos(-half, half, 0).tex(0, 0).color(red, green, blue, 1.0f).endVertex();
        tessellator.draw();

        ShaderHandler.end();

//Put GL state back to how vanilla expects it
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();

        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}
