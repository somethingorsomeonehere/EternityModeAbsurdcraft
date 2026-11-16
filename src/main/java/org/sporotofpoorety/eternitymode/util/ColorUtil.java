package org.sporotofpoorety.eternitymode.util;


import net.minecraft.world.World;



public final class ColorUtil 
{

/*
//Set GLStateManager current blending
    private static void configureBlending(String blendMode) 
    {
        switch (mode) 
        {
            case "alpha":
                GlStateManager.tryBlendFuncSeparate
                (
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ZERO
                );
            break;
            case "additive":
                GlStateManager.tryBlendFuncSeparate
                (
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE,
                    GlStateManager.SourceFactor.ZERO,
                    GlStateManager.DestFactor.ONE
                );
            break;
            case "multiply":
                GlStateManager.tryBlendFuncSeparate
                (
                    GlStateManager.SourceFactor.DST_COLOR,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ZERO,
                    GlStateManager.DestFactor.ONE
                );
            break;
        }
    }
*/



  
    public static float[][] rainbowColors =
    {
//Red
        {1.0f, 0.0f, 0.0f},
//Orange
        {1.0f, 0.5f, 0.0f},
//Yellow
        {1.0f, 1.0f, 0.0f},
//Lime green
        {0.5f, 1.0f, 0.0f},
//Green
        {0.0f, 1.0f, 0.0f},
//Spring green
        {0.0f, 1.0f, 0.5f},
//Cyan
        {0.0f, 1.0f, 1.0f},
//Azure blue
        {0.0f, 0.5f, 1.0f},
//Blue
        {0.0f, 0.0f, 1.0f},
//Violet
        {0.5f, 0.0f, 1.0f},
//Magenta
        {1.0f, 0.0f, 1.0f},
//Rose
        {1.0f, 0.0f, 0.5f}
    };


    public static float[] pickRandomRainbowColor(World world)
    {
        return rainbowColors[world.rand.nextInt(12)];
    }


    public static float[] interpolateColor(int intervalAt, int intervalLen,
    float atRed, float atGreen, float atBlue, float nextRed, float nextGreen, float nextBlue)
    {
//Previously had int truncation issues
        float colorProgress = (float)intervalAt / (float)intervalLen;        
        float interpRed = atRed + ((nextRed - atRed) * colorProgress);        
        float interpGreen = atGreen + ((nextGreen - atGreen) * colorProgress);        
        float interpBlue = atBlue + ((nextBlue - atBlue) * colorProgress);        
        

        float[] interpColor = {interpRed, interpGreen, interpBlue};
        return interpColor;
    }

}
