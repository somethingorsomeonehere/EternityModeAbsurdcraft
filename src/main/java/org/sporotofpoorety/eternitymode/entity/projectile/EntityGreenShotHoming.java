package org.sporotofpoorety.eternitymode.entity.projectile;

import java.util.List;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import electroblob.wizardry.registry.WizardrySounds;
import electroblob.wizardry.util.MagicDamage;
import electroblob.wizardry.util.MagicDamage.DamageType;
import electroblob.wizardry.util.ParticleBuilder;
import electroblob.wizardry.util.ParticleBuilder.Type;

import org.sporotofpoorety.eternitymode.util.DamageUtil;


public class EntityGreenShotHoming extends EntityProjectileHoming 
{

    public EntityGreenShotHoming(World worldIn) 
    {
        super(worldIn);

        this.setSize(2.0F, 2.0F);
    }

    public EntityGreenShotHoming(World worldIn, double x, double y, double z, 
    EntityLivingBase owner,
    int lifetimeMax, double speedX, double speedY, double speedZ, 
    double accelerationRate, double gravitySpeed, 
    double hitCheckSize, boolean collidesEntities, boolean collidesBlocks, float damageVal,
    int timePreHomingMax, boolean homingTimeHasMax, int homingTimeMax, double homingSpeed, int homingMode)
    {
        super(worldIn, x, y, z,
        owner, 
        lifetimeMax, speedX, speedY, speedZ,
        accelerationRate, gravitySpeed, 
        hitCheckSize, collidesEntities, collidesBlocks, damageVal,
        timePreHomingMax, homingTimeHasMax, homingTimeMax, homingSpeed, homingMode);

        this.setSize(2.0F, 2.0F);
    }


    public void setDead()
    {
        super.setDead();

//Force orb particle effect
		if(this.world.isRemote)
        {
			for(int j = 0; j < 20; j++)
            {
				float brightness = 0.5f + (rand.nextFloat() / 2);
				ParticleBuilder.create(Type.SPARKLE, rand, posX, posY, posZ, 0.25, true).time(6)
				.clr(brightness, 1.0f, brightness + 0.2f).spawn(world);
			}
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 0, 0, 0);
		}
    }


    public void onTrueSetDead()
    {
		if(!this.world.isRemote)
        {
//Force orb hit sound
		    this.playSound(WizardrySounds.ENTITY_FORCE_ORB_HIT, 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

//2 gives a cool flanging effect!
			float pitch = this.rand.nextFloat() * 0.2F + 0.3F;
			this.playSound(WizardrySounds.ENTITY_FORCE_ORB_HIT_BLOCK, 1.5F, pitch);
			this.playSound(WizardrySounds.ENTITY_FORCE_ORB_HIT_BLOCK, 1.5F, pitch - 0.01f);
		}
    }


    @Override
    public void onUpdate()
    {
        super.onUpdate();
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
