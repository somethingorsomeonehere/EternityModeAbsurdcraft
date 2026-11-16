package org.sporotofpoorety.eternitymode.entity.projectile;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import org.sporotofpoorety.eternitymode.entity.projectile.EntityProjectileLinear;
import org.sporotofpoorety.eternitymode.util.ProjectileUtil;



public class EntityProjectileBouncing extends EntityProjectileLinear 
{

    public boolean isBouncing;
    public boolean bounceUpExtra;
    public double bounceUpForce;
    public int bounceExtraEffectCooldown;


    public EntityProjectileBouncing(World worldIn) 
    {
        super(worldIn);
    }


    public EntityProjectileBouncing(World worldIn, double x, double y, double z, 
    EntityLivingBase owner,
    int lifetimeMax, double speedX, double speedY, double speedZ, 
    double accelerationRate, double gravitySpeed, 
    double hitCheckSize, boolean collidesEntities, boolean collidesBlocks, float damageVal,
    boolean acceleratesVertically, boolean bounceUpExtra, double bounceUpForce)
    {
        super(worldIn, x, y, z, 
        owner,
        lifetimeMax, speedX, speedY, speedZ,
        accelerationRate, gravitySpeed, 
        hitCheckSize, collidesEntities, collidesBlocks, damageVal);

        this.acceleratesVertically = acceleratesVertically;
        this.isBouncing = false;
        this.bounceUpExtra = bounceUpExtra;
        this.bounceUpForce = bounceUpForce;
        this.bounceExtraEffectCooldown = 0;
    }




    @Override
    public void onUpdate()
    {
        super.onUpdate();


        if(!this.world.isRemote)
        {
            if(this.bounceExtraEffectCooldown > 0)
            {
                --this.bounceExtraEffectCooldown;
            }
        }
    }


//Bounce off blocks
    @Override
    public void onHitBlock(RayTraceResult rayTraceResult, Block blockHit)
    {
        if(!world.isRemote)
        {
            this.isBouncing = true;
            switch (rayTraceResult.sideHit.getAxis()) 
            {
                case X:
                    this.motionX *= -1.0D;

                break;

                case Y:
                    this.motionY *= -1.0D;

//If bounce up, slight safety offset, and optional min-bounce
                    if(this.motionY > 0.0D)
                    {
                        this.posY += 0.05D;

                        if(this.bounceUpExtra && this.motionY < bounceUpForce)
                        {
                            this.motionY += bounceUpForce;
                        }
                    }

                break;

                case Z:
                    this.motionZ *= -1.0D;

                break;
            }
        }
    }


    public void projectileBounceSound()
    {
        this.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.0F);
    }




    protected void writeEntityToNBT(NBTTagCompound compound) 
    {
        super.writeEntityToNBT(compound);

        compound.setBoolean("IsBouncing", isBouncing);
        compound.setBoolean("BounceUpExtra", bounceUpExtra);
        compound.setDouble("BounceUpForce", bounceUpForce);
        compound.setInteger("BounceExtraEffectCooldown", bounceExtraEffectCooldown);
    }


    protected void readEntityFromNBT(NBTTagCompound compound) 
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("IsBouncing")) { isBouncing = compound.getBoolean("IsBouncing"); }
        if (compound.hasKey("BounceUpExtra")) { bounceUpExtra = compound.getBoolean("BounceUpExtra"); }
        if (compound.hasKey("BounceUpForce")) { bounceUpForce = compound.getDouble("BounceUpForce"); }
        if (compound.hasKey("BounceExtraEffectCooldown")) { bounceExtraEffectCooldown = compound.getInteger("BounceExtraEffectCooldown"); }
    }

}
