package org.sporotofpoorety.eternitymode.util;


import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;


public class RotateAt
{
    public Vec3d relX;
    public Vec3d relY;
    public double scale;
    public double angInc;
    public double angAt;


    public RotateAt(Vec3d relX, Vec3d relY, double scale, int angSteps, double angAt) 
    {
        this.relX = relX;
        this.relY = relY;
        this.scale = scale;
        this.angInc = 2.0D * Math.PI / ((double)angSteps);
        this.angAt = angAt;
    }

    public RotateAt(Vec3d relX, Vec3d relY, double scale, double angInc, double angAt) 
    {
        this.relX = relX;
        this.relY = relY;
        this.scale = scale;
        this.angInc = angInc;
        this.angAt = angAt;
    }

    public void rotInc()
    {
        this.angAt += this.angInc;
    }

    public Vec3d getDirAt()
    {
        return relX.scale(Math.cos(angAt)).add(relY.scale(Math.sin(angAt)));
    }

    
    public static void writeToNBT(NBTTagCompound compound, RotateAt rotAt)
    {
        if(rotAt != null)
        {
            compound.setDouble("RotAtRelXX", rotAt.relX.x);
            compound.setDouble("RotAtRelXY", rotAt.relX.y);
            compound.setDouble("RotAtRelXZ", rotAt.relX.z);
            compound.setDouble("RotAtRelYX", rotAt.relY.x);
            compound.setDouble("RotAtRelYY", rotAt.relY.y);
            compound.setDouble("RotAtRelYZ", rotAt.relY.z);
            compound.setDouble("RotAtScale", rotAt.scale);
            compound.setDouble("RotAtAngInc", rotAt.angInc);
            compound.setDouble("RotAtAngAt", rotAt.angAt);
        }
    }

    public static RotateAt readFromNBT(NBTTagCompound compound)
    {
        if(compound.hasKey("RotAtAngInc"))
        {
            return new RotateAt(new Vec3d(compound.getDouble("RotAtRelXX"), compound.getDouble("RotAtRelXY"), compound.getDouble("RotAtRelXZ")), 
                new Vec3d(compound.getDouble("RotAtRelYX"), compound.getDouble("RotAtRelYY"), compound.getDouble("RotAtRelYZ")),
                compound.getDouble("RotAtScale"), compound.getDouble("RotAtAngInc"), compound.getDouble("RotAtAngAt")); 
        }
        else
        {
            return null;
        }
    }
}
