package org.sporotofpoorety.eternitymode.util;


import net.minecraft.util.math.Vec3d;




public class LaserData
{
    public int laserType;
    public double laserLen;
    public Vec3d laserDir;
    public float red, green, blue;

//I don't think
//this needs to be in the packet
    public float atRed, atGreen, atBlue;
    public float nextRed, nextGreen, nextBlue;


    public LaserData(int laserType, double laserLen, Vec3d laserDir, float red, float green, float blue) 
    {
        this.laserType = laserType;
        this.laserLen = laserLen;
        this.laserDir = laserDir;
        this.red = red; this.green = green; this.blue = blue;
    }
}
