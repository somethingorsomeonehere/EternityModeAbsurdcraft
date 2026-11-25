package org.sporotofpoorety.eternitymode.entity.projectile;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import org.sporotofpoorety.eternitymode.entity.projectile.EntityProjectileRaytrace;
import org.sporotofpoorety.eternitymode.util.AimUtil;



public class EntityProjectileHoming extends EntityProjectileRaytrace 
{

    public double homingSpeed;
    public int homingMode;

    public boolean isPreHoming;
    public int timePreHomingMax;

    public boolean isHoming;
    public int homingTimeMax;

    public boolean isPostHoming;


    public EntityProjectileHoming(World worldIn) 
    {
        super(worldIn);
    }

    public EntityProjectileHoming(World worldIn, double x, double y, double z, 
    EntityLivingBase owner,
    int lifetimeMax, 
    double speedX, double speedY, double speedZ, double accelerationRate, 
    boolean stopsAtEntity, boolean stopsAtBlock, float damageVal,
    double homingSpeed, int homingMode, int timePreHomingMax, int homingTimeMax) 
    {
        super(worldIn, x, y, z,
        owner, 
        lifetimeMax, 
        speedX, speedY, speedZ, accelerationRate,
        stopsAtEntity, stopsAtBlock, damageVal);

        this.homingSpeed = homingSpeed;

        this.isPreHoming = false;
        this.timePreHomingMax = timePreHomingMax;
        this.isHoming = false;
        this.homingTimeMax = homingTimeMax;

        this.homingMode = homingMode;
        this.isPostHoming = false;
    }




    @Override
    public void onUpdate()
    {
        super.onUpdate();


        if(!this.world.isRemote)
        {
            cacheTarget();

//Pre-homing logic
            if(this.isPreHoming) { this.projectilePreHoming(); }
        
//Homing logic
            if(this.isHoming) 
            { 
                if(this.homingMode == 0) { this.projectileHomingAbsolute(); }
                if(this.homingMode == 1) { this.projectileHomingRelative(); }
            }

//Post-homing logic
            if(this.isPostHoming) { this.projectilePostHoming(); }
        }
    }




    public void projectilePreHoming()
    {
        if(transTimer == 0)
        {
            projectileHomingStart();
        }
        --transTimer;
    }


    public void projectileHomingStart()
    {
        isPreHoming = false;
        isHoming = true;
        transTimer = homingTimeMax;
    }

//Linear homing
    public void projectileHomingAbsolute()
    {
        EntityLivingBase ownerTarget;

        if(owner instanceof EntityPlayer)
        {
            ownerTarget = cachedTarget;
        }
        else
        {
            ownerTarget = getOwnerTarget();
        }

        if(ownerTarget != null)
        {
            Vec3d targetDistNormalized = AimUtil.targetCenter(this, ownerTarget).normalize();

            this.motionX = targetDistNormalized.x * this.homingSpeed * this.accelerationTracker;
            this.motionY = targetDistNormalized.y * this.homingSpeed * this.accelerationTracker;
            this.motionZ = targetDistNormalized.z * this.homingSpeed * this.accelerationTracker;
        }
        
        if(transTimer > 0 && --transTimer == 0)
        {
            this.projectilePostHoming();          
        }
    }

//Accelerate based on how far target is
    public void projectileHomingRelative()
    {
        EntityLivingBase ownerTarget;

        if(owner instanceof EntityPlayer)
        {
            ownerTarget = cachedTarget;
        }
        else
        {
            ownerTarget = this.getOwnerTarget();
        }


        if(ownerTarget != null)
        {
            Vec3d targetDist = AimUtil.targetCenter(this, ownerTarget);

            this.motionX += targetDist.x * (0.01D * this.homingSpeed);
            this.motionY += targetDist.y * (0.01D * this.homingSpeed);
            this.motionZ += targetDist.z * (0.01D * this.homingSpeed);
        }

        if(transTimer > 0 && --transTimer == 0)
        {
            this.projectilePostHomingStart();          
        }
    }


    public void projectilePostHomingStart()
    {
        this.isHoming = false;
        this.isPostHoming = true;
    }

    public void projectilePostHoming()
    {

    }




    public void initialSpeedCheck()
    {
        super.initialSpeedCheck();        


//Set if pre-homing or not
        if(timePreHomingMax > 0)
        {
            isPreHoming = true;
            transTimer = timePreHomingMax;
        }
        else
        {
            projectileHomingStart();
        }
    }


    public void accelerationApply()
    {
//Only uses acceleration if direct homing
        if(!(this.isHoming && this.homingMode == 1))
        {
            this.accelerationTracker *= this.accelerationRate;
        }
    }




    protected void writeEntityToNBT(NBTTagCompound compound) 
    {
        super.writeEntityToNBT(compound);

        compound.setDouble("HomingSpeed", homingSpeed);
        compound.setInteger("HomingMode", homingMode);

        compound.setBoolean("IsPreHoming", isPreHoming);
        compound.setInteger("TimePreHomingMax", timePreHomingMax);

        compound.setBoolean("IsHoming", isHoming);
        compound.setInteger("HomingTimeMax", homingTimeMax);

        compound.setBoolean("IsPostHoming", isPostHoming);
    }


    protected void readEntityFromNBT(NBTTagCompound compound) 
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("HomingSpeed")) { homingSpeed = compound.getDouble("HomingSpeed"); }
        if (compound.hasKey("HomingMode")) { homingMode = compound.getInteger("HomingMode"); }

        if (compound.hasKey("IsPreHoming")) { isPreHoming = compound.getBoolean("IsPreHoming"); }
        if (compound.hasKey("TimePreHomingMax")) { timePreHomingMax = compound.getInteger("TimePreHomingMax"); }

        if (compound.hasKey("IsHoming")) { isHoming = compound.getBoolean("IsHoming"); }
        if (compound.hasKey("HomingTimeMax")) { homingTimeMax = compound.getInteger("HomingTimeMax"); }

        if (compound.hasKey("IsPostHoming")) { isPostHoming = compound.getBoolean("IsPostHoming"); }
    }

}
