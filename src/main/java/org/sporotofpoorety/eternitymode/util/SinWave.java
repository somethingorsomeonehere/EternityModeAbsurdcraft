package org.sporotofpoorety.eternitymode.util;


import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;

import org.sporotofpoorety.eternitymode.entity.EntityWithOwner;


public class SinWave
{
    public Vec3d sinWvAxis;
    public double scale;
    public double angInc;
    public double angAt;
    public double valAt;


    public SinWave(double scale, int angSteps, double angAt, double valAt) 
    {
        this.scale = scale;
        this.angInc = (2.0D * Math.PI) / angSteps;
        this.angAt = angAt;
        this.valAt = valAt;
    }
    public SinWave(double scale, double angInc, double angAt, double valAt) 
    {
        this.scale = scale;
        this.angInc = angInc;
        this.angAt = angAt;
        this.valAt = valAt;
    }

    public SinWave(Vec3d sinWvAxis, double scale, int angSteps, double angAt, double valAt) 
    {
        this.sinWvAxis = sinWvAxis;
        this.scale = scale;
        this.angInc = (2.0D * Math.PI) / angSteps;
        this.angAt = angAt;
        this.valAt = valAt;
    }
    public SinWave(Vec3d sinWvAxis, double scale, double angInc, double angAt, double valAt) 
    {
        this.sinWvAxis = sinWvAxis;
        this.scale = scale;
        this.angInc = angInc;
        this.angAt = angAt;
        this.valAt = valAt;
    }

    public void sinInc()
    {
        this.angAt += this.angInc;
        this.valAt = Math.sin(angAt);
    }

    public void sinIncAndMove(EntityWithOwner entity)
    {
        double valPrev = Math.sin(angAt);
        this.sinInc();
        double currStep = (valAt - valPrev) * this.scale;
        
        entity.moveAdjust(currStep * this.sinWvAxis.x, currStep * this.sinWvAxis.y, currStep * this.sinWvAxis.z);
    }

    
    public static void writeToNBT(NBTTagCompound compound, SinWave wave)
    {
        if(wave != null)
        {
            if(wave.sinWvAxis != null)
            {
                compound.setDouble("SinWaveAxisX", wave.sinWvAxis.x);
                compound.setDouble("SinWaveAxisY", wave.sinWvAxis.y);
                compound.setDouble("SinWaveAxisZ", wave.sinWvAxis.z);
            }
            compound.setDouble("SinWaveScale", wave.scale);
            compound.setDouble("SinWaveAngInc", wave.angInc);
            compound.setDouble("SinWaveAngAt", wave.angAt);
            compound.setDouble("SinWaveValAt", wave.valAt);
        }
    }

    public static SinWave readFromNBT(NBTTagCompound compound)
    {
        if(compound.hasKey("SinWaveAngInc"))
        {
            SinWave wave;

            if(compound.hasKey("SinWaveAxisX"))
            {
                Vec3d savedWaveAxis = new Vec3d(compound.getDouble("SinWaveAxisX"), compound.getDouble("SinWaveAxisY"), compound.getDouble("SinWaveAxisZ"));

                wave = new SinWave(savedWaveAxis, compound.getDouble("SinWaveScale"), compound.getDouble("SinWaveAngInc"), 
                    compound.getDouble("SinWaveAngAt"), compound.getDouble("SinWaveValAt"));  
            }
            else
            {
                wave = new SinWave(compound.getDouble("SinWaveScale"), compound.getDouble("SinWaveAngInc"), 
                    compound.getDouble("SinWaveAngAt"), compound.getDouble("SinWaveValAt"));  
            }

            return wave;
        }
        else
        {
            return null;
        }
    }
}
