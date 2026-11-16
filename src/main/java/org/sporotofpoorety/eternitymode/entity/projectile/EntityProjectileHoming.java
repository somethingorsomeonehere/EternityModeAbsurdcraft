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

    public boolean isPreHoming;
    public int timePreHoming;
    public int timePreHomingMax;
    public boolean isHoming;
    public int homingTime;
    public boolean homingTimeHasMax;
    public int homingTimeMax;
    public double homingSpeed;
    public int homingMode;
    public boolean isPostHoming;


    public EntityProjectileHoming(World worldIn) 
    {
        super(worldIn);
    }

    public EntityProjectileHoming(World worldIn, double x, double y, double z, 
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
        hitCheckSize, collidesEntities, collidesBlocks, damageVal);

        this.isPreHoming = false;
        this.timePreHoming = 0;
        this.timePreHomingMax = timePreHomingMax;
        this.isHoming = false;
        this.homingTime = 0;
        this.homingTimeHasMax = homingTimeHasMax;
        this.homingTimeMax = homingTimeMax;
        this.homingSpeed = homingSpeed;
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
        if(++this.timePreHoming >= this.timePreHomingMax)
        {
            this.projectileHomingStart();
        }
    }


    public void projectileHomingStart()
    {
        this.isPreHoming = false;
        this.isHoming = true;
    }


    public void projectileHomingAbsolute()
    {
//If valid target
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
//Get distance vector (normalized)
            Vec3d targetDistNormalized = AimUtil.targetCenter(this, ownerTarget).normalize();

//Simple homing
            this.motionX = targetDistNormalized.x * this.homingSpeed * this.accelerationTracker;
            this.motionY = targetDistNormalized.y * this.homingSpeed * this.accelerationTracker;
            this.motionZ = targetDistNormalized.z * this.homingSpeed * this.accelerationTracker;
        }
        
        if(this.homingTimeHasMax && (++this.homingTime >= this.homingTimeMax))
        {
            this.projectilePostHoming();
        }
    }


    public void projectileHomingRelative()
    {
//If valid target
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
//Get distance
            Vec3d targetDist = AimUtil.targetCenter(this, ownerTarget);

//Accelerate based on how far target is
            this.motionX += targetDist.x * (0.01D * this.homingSpeed);
            this.motionY += targetDist.y * (0.01D * this.homingSpeed);
            this.motionZ += targetDist.z * (0.01D * this.homingSpeed);
        }

        if(this.homingTimeHasMax && (++this.homingTime > this.homingTimeMax))
        {
            this.projectilePostHoming();
        }
    }


    public void projectilePostHoming()
    {
        this.isHoming = false;
        this.isPostHoming = true;
    }




    public void initialSpeedCheck()
    {
        super.initialSpeedCheck();        


//If it has a pre-homing phase
        if(this.timePreHomingMax > 0)
        {
//Set to pre-homing
            this.isPreHoming = true;
        }
//If it has no pre-homing phase
        else
        {
//Set to be homing
            this.isHoming = true;
        }
    }


    public void accelerationApply()
    {
//If not relative homing
        if(!(this.isHoming && this.homingMode == 1))
        {
//Just increase acceleration tracker
            this.accelerationTracker *= this.accelerationRate;
        }
    }




    protected void writeEntityToNBT(NBTTagCompound compound) 
    {
        super.writeEntityToNBT(compound);


        compound.setBoolean("IsPreHoming", isPreHoming);
        compound.setInteger("TimePreHoming", timePreHoming);
        compound.setInteger("TimePreHomingMax", timePreHomingMax);
        compound.setBoolean("IsHoming", isHoming);
        compound.setInteger("HomingTime", homingTime);
        compound.setBoolean("HomingTimeHasMax", homingTimeHasMax);
        compound.setInteger("HomingTimeMax", homingTimeMax);
        compound.setDouble("HomingSpeed", homingSpeed);
        compound.setInteger("HomingMode", homingMode);
        compound.setBoolean("IsPostHoming", isPostHoming);
    }


    protected void readEntityFromNBT(NBTTagCompound compound) 
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("IsPreHoming")) { isPreHoming = compound.getBoolean("IsPreHoming"); }
        if (compound.hasKey("TimePreHoming")) { timePreHoming = compound.getInteger("TimePreHoming"); }
        if (compound.hasKey("TimePreHomingMax")) { timePreHomingMax = compound.getInteger("TimePreHomingMax"); }
        if (compound.hasKey("IsHoming")) { isHoming = compound.getBoolean("IsHoming"); }
        if (compound.hasKey("HomingTime")) { homingTime = compound.getInteger("HomingTime"); }
        if (compound.hasKey("HomingTimeHasMax")) { homingTimeHasMax = compound.getBoolean("HomingTimeHasMax"); }
        if (compound.hasKey("HomingTimeMax")) { homingTimeMax = compound.getInteger("HomingTimeMax"); }
        if (compound.hasKey("HomingSpeed")) { homingSpeed = compound.getDouble("HomingSpeed"); }
        if (compound.hasKey("HomingMode")) { homingMode = compound.getInteger("HomingMode"); }
        if (compound.hasKey("IsPostHoming")) { isPostHoming = compound.getBoolean("IsPostHoming"); }
    }

}
