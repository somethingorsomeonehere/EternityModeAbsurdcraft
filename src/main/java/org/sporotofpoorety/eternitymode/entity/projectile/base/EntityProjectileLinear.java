package org.sporotofpoorety.eternitymode.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.sporotofpoorety.eternitymode.entity.projectile.EntityProjectileRaytrace;



public class EntityProjectileLinear extends EntityProjectileRaytrace {


    public EntityProjectileLinear(World worldIn) 
    {
        super(worldIn);
    }

    public EntityProjectileLinear(World worldIn, double x, double y, double z, 
    EntityLivingBase owner,
    int lifetimeMax, 
    double speedX, double speedY, double speedZ, double accelerationRate, 
    boolean stopsAtEntity, boolean stopsAtBlock, float damageVal)
    {
        super(worldIn, x, y, z,
        owner,
        lifetimeMax, 
        speedX, speedY, speedZ, accelerationRate,
        stopsAtEntity, stopsAtBlock, damageVal);
    }




    public void onUpdate()
    {
        super.onUpdate();
    }




    protected void writeEntityToNBT(NBTTagCompound compound) 
    {
        super.writeEntityToNBT(compound);
    }


    protected void readEntityFromNBT(NBTTagCompound compound) 
    {
        super.readEntityFromNBT(compound);
    }

}
