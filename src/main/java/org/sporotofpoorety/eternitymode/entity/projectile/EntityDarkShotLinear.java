package org.sporotofpoorety.eternitymode.entity.projectile;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;

import org.sporotofpoorety.eternitymode.entity.projectile.EntityProjectileLinear;




public class EntityDarkShotLinear extends EntityProjectileLinear 
{

    public EntityDarkShotLinear(World worldIn) 
    {
        super(worldIn);
    }

    public EntityDarkShotLinear(World worldIn, double x, double y, double z, 
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




    @Override
    public void onUpdate()
    {
        super.onUpdate();

		if(world.isRemote)
        {
			float brightness = rand.nextFloat() * 0.2f;

			ParticleBuilder.create(Type.SPARKLE, this).time(20 + rand.nextInt(10))
			    .clr(brightness, 0.0f, brightness).spawn(world);
			ParticleBuilder.create(Type.DARK_MAGIC, this).clr(0.1f, 0.0f, 0.0f).spawn(world);
		}
    }




	@Override
	public boolean canRenderOnFire()
    {
		return false;
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
