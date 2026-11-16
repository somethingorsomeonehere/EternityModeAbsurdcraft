package org.sporotofpoorety.eternitymode.client.render;

import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

import org.sporotofpoorety.eternitymode.entity.EntityLightningCustom;




public class RenderLightningCustom extends Render<EntityLightningCustom>
{
    public RenderLightningCustom(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityLightningCustom entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder vertexBuffer = tessellator.getBuffer();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        double[] sumOffsetsX = new double[16];
        double[] sumOffsetsZ = new double[16];
        double currentSumOffsetX = 0.0D;
        double currentSumOffsetZ = 0.0D;
        Random pathRandom = new Random(entity.boltVertex);

        for (int i = 0; i < 16; ++i)
        {
            sumOffsetsX[i] = currentSumOffsetX;
            sumOffsetsZ[i] = currentSumOffsetZ;
            currentSumOffsetX += (double)(pathRandom.nextInt(11) - 5);
            currentSumOffsetZ += (double)(pathRandom.nextInt(11) - 5);
        }

        for (int overlapAt = 0; overlapAt < 4; ++overlapAt)
        {
            Random layerRandom = new Random(entity.boltVertex);


            for (int branchType = 0; branchType < 1; ++branchType)
            {
                int startSeg = 0;
                int endSeg = 15;


                double segOffsetX = sumOffsetsX[startSeg];
                double segOffsetZ = sumOffsetsZ[startSeg];

                for (int levelAt = 0; levelAt < 16; ++levelAt)
                {
                    double previousSegOffsetX = segOffsetX;
                    double previousSegOffsetZ = segOffsetZ;

                    if (levelAt > 0)
                    {
                        segOffsetX += (double)(layerRandom.nextInt(11) - 5);
                        segOffsetZ += (double)(layerRandom.nextInt(11) - 5);
                    }

                    vertexBuffer.begin(5, DefaultVertexFormats.POSITION_COLOR);

                    double segUpperRadius = 0.1D + (double)overlapAt * 0.2D;
                        segUpperRadius *= ((double)levelAt / 2.0D) * 0.1D + 1.0D;

                    double segLowerRadius = 0.1D + (double)overlapAt * 0.2D;
                        segLowerRadius *= ((double)(levelAt - 1) / 2.0D) * 0.1D + 1.0D;


                    for (int cornerIndex = 0; cornerIndex < 5; ++cornerIndex)
                    {
                        double upperCornerX = x + 0.5D - segUpperRadius;
                        double upperCornerZ = z + 0.5D - segUpperRadius;

                        if (cornerIndex == 1 || cornerIndex == 2)
                        {
                            upperCornerX += segUpperRadius * 2.0D;
                        }

                        if (cornerIndex == 2 || cornerIndex == 3)
                        {
                            upperCornerZ += segUpperRadius * 2.0D;
                        }

                        double lowerCornerX = x + 0.5D - segLowerRadius;
                        double lowerCornerZ = z + 0.5D - segLowerRadius;

                        if (cornerIndex == 1 || cornerIndex == 2)
                        {
                            lowerCornerX += segLowerRadius * 2.0D;
                        }

                        if (cornerIndex == 2 || cornerIndex == 3)
                        {
                            lowerCornerZ += segLowerRadius * 2.0D;
                        }

                        vertexBuffer.pos(lowerCornerX + previousSegOffsetX, y + (double)(levelAt * 16), lowerCornerZ + previousSegOffsetZ).color(entity.getRed(), entity.getGreen(), entity.getBlue(), 0.3F).endVertex();
                        vertexBuffer.pos(upperCornerX + segOffsetX, y + (double)((levelAt + 1) * 16), upperCornerZ + segOffsetZ).color(entity.getRed(), entity.getGreen(), entity.getBlue(), 0.3F).endVertex();
                    }

                    tessellator.draw();
                }
            }




            for (int branchType = 1; branchType < 3; ++branchType)
            {
                int startSeg = 15 - branchType;
                int endSeg = startSeg - 4;


                double segOffsetX = sumOffsetsX[startSeg] - currentSumOffsetX;
                double segOffsetZ = sumOffsetsZ[startSeg] - currentSumOffsetZ;

                for (int levelAt = startSeg; levelAt >= endSeg; --levelAt)
                {
                    double previousSegOffsetX = segOffsetX;
                    double previousSegOffsetZ = segOffsetZ;

                    segOffsetX += (double)(layerRandom.nextInt(31) - 15);
                    segOffsetZ += (double)(layerRandom.nextInt(31) - 15);

                    vertexBuffer.begin(5, DefaultVertexFormats.POSITION_COLOR);
                    double segUpperRadius = 0.1D + (double)overlapAt * 0.2D;

                    double segLowerRadius = 0.1D + (double)overlapAt * 0.2D;

                    for (int cornerIndex = 0; cornerIndex < 5; ++cornerIndex)
                    {
                        double upperCornerX = x + 0.5D - segUpperRadius;
                        double upperCornerZ = z + 0.5D - segUpperRadius;

                        if (cornerIndex == 1 || cornerIndex == 2)
                        {
                            upperCornerX += segUpperRadius * 2.0D;
                        }

                        if (cornerIndex == 2 || cornerIndex == 3)
                        {
                            upperCornerZ += segUpperRadius * 2.0D;
                        }

                        double lowerCornerX = x + 0.5D - segLowerRadius;
                        double lowerCornerZ = z + 0.5D - segLowerRadius;

                        if (cornerIndex == 1 || cornerIndex == 2)
                        {
                            lowerCornerX += segLowerRadius * 2.0D;
                        }

                        if (cornerIndex == 2 || cornerIndex == 3)
                        {
                            lowerCornerZ += segLowerRadius * 2.0D;
                        }

                        vertexBuffer.pos(lowerCornerX + segOffsetX, y + (double)(levelAt * 16), lowerCornerZ + segOffsetZ).color(entity.getRed(), entity.getGreen(), entity.getBlue(), 0.3F).endVertex();
                        vertexBuffer.pos(upperCornerX + previousSegOffsetX, y + (double)((levelAt + 1) * 16), upperCornerZ + previousSegOffsetZ).color(entity.getRed(), entity.getGreen(), entity.getBlue(), 0.3F).endVertex();
                    }

                    tessellator.draw();
                }
            }
        }

        GlStateManager.disableBlend();
        GlStateManager.enableLighting();
        GlStateManager.enableTexture2D();
    }

    @Nullable

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLightningCustom entity)
    {
        return null;
    }
}
