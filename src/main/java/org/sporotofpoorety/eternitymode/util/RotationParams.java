package org.sporotofpoorety.eternitymode.util;


import net.minecraft.util.math.Vec3d;




public class RotationParams 
{

    public Vec3d axisForRotation; 

    public double cosOfRotation; 
    public double sinOfRotation;


    public RotationParams(Vec3d axisForRotation, double cosOfRotation, double sinOfRotation) 
    {
        this.axisForRotation = axisForRotation;
     
        this.cosOfRotation = cosOfRotation;
        this.sinOfRotation = sinOfRotation;
    }

}
