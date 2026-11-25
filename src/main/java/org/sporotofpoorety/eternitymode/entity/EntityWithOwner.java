package org.sporotofpoorety.eternitymode.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import org.sporotofpoorety.eternitymode.util.EntitiesWithinChunks;
import org.sporotofpoorety.eternitymode.util.RotateAt;
import org.sporotofpoorety.eternitymode.util.SinWave;




public abstract class EntityWithOwner extends Entity
{

    public boolean justReloaded;

    public EntityLivingBase owner = null;
    public UUID ownerUUID;

    public EntityLiving cachedTarget;

    public int lifetimeMax = -1;
    public int realTicksExisted = 0;

    public int transTimer = -1;

    public float damageVal;

    public double gravitySpeed = 0.08D;
    public boolean acceleratesVertically = true;
    public double accelerationRate = 1.0D;
    public double accelerationTracker = 1.0D;

    public boolean anchorUse;
    public Vec3d anchor;

    public boolean rotMoves;
    public RotateAt rotAt;
    public boolean sinMoves;
    public SinWave sinWv;




    public EntityWithOwner(World worldIn)
    {
        super(worldIn);

        this.setSize(1.0F, 1.0F);
    }


    public EntityWithOwner(World worldIn, double x, double y, double z, EntityLivingBase owner)
    {
        super(worldIn);
        this.setSize(1.0F, 1.0F);
        this.setPosition(x, y, z);
        this.prevPosX = x;
        this.prevPosY = y;
        this.prevPosZ = z;
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;

//Just safeguarding defaults
        this.owner = owner;
        if(this.owner != null) { this.ownerUUID = owner.getUniqueID(); }
    }


    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        if(this.ownerUUID != null) { compound.setUniqueId("OwnerUUID", this.ownerUUID); }

        compound.setInteger("LifetimeMax", this.lifetimeMax);
        compound.setInteger("RealTicksExisted", this.realTicksExisted);

        compound.setInteger("TransTimer", this.transTimer);

        compound.setFloat("DamageVal", this.damageVal);

        compound.setDouble("GravitySpeed", this.gravitySpeed);
        compound.setBoolean("AcceleratesVertically", this.acceleratesVertically);
        compound.setDouble("AccelerationRate", this.accelerationRate);
        compound.setDouble("AccelerationTracker", this.accelerationTracker);

        compound.setBoolean("AnchorUse", this.anchorUse);
        if(anchor != null)
        {
            compound.setDouble("AnchorX", anchor.x);
            compound.setDouble("AnchorY", anchor.y);
            compound.setDouble("AnchorZ", anchor.z);
        }

        compound.setBoolean("RotMoves", this.rotMoves);
        RotateAt.writeToNBT(compound, this.rotAt);
        compound.setBoolean("SinMoves", this.sinMoves);
        SinWave.writeToNBT(compound, this.sinWv);
    }

    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        this.justReloaded = true;

        if (compound.hasUniqueId("OwnerUUID")) 
        { 
            this.ownerUUID = compound.getUniqueId("OwnerUUID"); 
        }

        if (compound.hasKey("LifetimeMax")) { this.lifetimeMax = compound.getInteger("LifetimeMax"); }
        if (compound.hasKey("RealTicksExisted")) { this.realTicksExisted = compound.getInteger("RealTicksExisted"); }

        if (compound.hasKey("TransTimer")) { this.transTimer = compound.getInteger("TransTimer"); }

        if (compound.hasKey("DamageVal")) { this.damageVal = compound.getFloat("DamageVal"); }

        if (compound.hasKey("GravitySpeed")) { this.gravitySpeed = compound.getDouble("GravitySpeed"); }
        if (compound.hasKey("AcceleratesVertically")) { this.acceleratesVertically = compound.getBoolean("AcceleratesVertically"); }
        if (compound.hasKey("AccelerationRate")) { this.accelerationRate = compound.getDouble("AccelerationRate"); }
        if (compound.hasKey("AccelerationTracker")) { this.accelerationTracker = compound.getDouble("AccelerationTracker"); }

        if (compound.hasKey("AnchorUse")) { this.anchorUse = compound.getBoolean("AnchorUse"); }
        if (compound.hasKey("AnchorX")) 
        { 
            anchor = new Vec3d(compound.getDouble("AnchorX"), compound.getDouble("AnchorY"), compound.getDouble("AnchorZ")); 
        }

        if (compound.hasKey("RotMoves")) { this.rotMoves = compound.getBoolean("RotMoves"); }
        this.rotAt = RotateAt.readFromNBT(compound);
        if (compound.hasKey("SinMoves")) { this.sinMoves = compound.getBoolean("SinMoves"); }
        this.sinWv = SinWave.readFromNBT(compound);
    }


    public void setMovement(double speedX, double speedY, double speedZ, 
    double gravitySpeed, boolean acceleratesVertically, double accelerationRate)
    {
        this.motionX = speedX;
        this.motionY = speedY;
        this.motionZ = speedZ;


        this.gravitySpeed = gravitySpeed;
        this.acceleratesVertically = acceleratesVertically;
        this.accelerationRate = accelerationRate;
    }




    public void onUpdate()
    {

//If just reloaded, restore owner
        if (!this.world.isRemote && this.justReloaded) 
        {
            this.justReloaded = false;
            this.validateOwner();
        }



//Yes, this has to be set manually
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;


//Entity update
        super.onUpdate();


//Reliable ticks existed
        ++this.realTicksExisted;


//Server side lifetime and owner validation
        if(!this.world.isRemote)
        {
            if(this.realTicksExisted == this.lifetimeMax) { this.onLifetimeExpire(); return; }

            if(this.realTicksExisted % 20 == 0)
            {
                this.performOwnerValidation();
            }
        }    
    }

    public void resetAnchor()
    {
        this.anchor = new Vec3d(this.posX, this.posY, this.posZ);
    }

    public void moveAdjust(double x, double y, double z)
    {
        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        if(this.anchorUse) { anchor = anchor.add(x, y, z); }
    }

    public void performBasicMovement()
    {
        this.moveAdjust(this.motionX, this.motionY, this.motionZ);

        this.accelerationApply();

        if (!this.hasNoGravity())
        {
            this.motionY -= this.gravitySpeed;
        }
    }

    public void performOffsets()
    {

    }

    public void accelerationApply()
    {
        this.motionX *= this.accelerationRate;
        if(this.acceleratesVertically) { this.motionY *= this.accelerationRate; }
        this.motionZ *= this.accelerationRate;


//Increase acceleration tracker
        this.accelerationTracker *= this.accelerationRate;
    }



    public void transCheck()
    {
        if(transTimer > 0 && --transTimer == 0)
        {
            transDo();    
        }
    }

    public void transDo()
    {

    }


    public void cacheTarget()
    {
        if (!this.world.isRemote && owner instanceof EntityPlayer) 
        {
            if (realTicksExisted % 20 == 0 && (cachedTarget == null || cachedTarget.isDead)) 
            {
                cachedTarget = EntitiesWithinChunks.getNearestHostile(this.world, this.posX, this.posZ, 4);
            } 
        }
    }

    public void onLifetimeExpire()
    {
        this.onTrueSetDead();
        this.setDead();
    }

    public void onTrueSetDead()
    {

    }

    public Vec3d getTickMv()
    {
        return new Vec3d(this.posX - this.lastTickPosX, this.posY - this.lastTickPosY, this.posZ - this.lastTickPosZ);
    }


    @Nullable
    public EntityLivingBase getOwnerTarget() 
    {
        if (owner instanceof EntityLiving) 
        {
            return ((EntityLiving) owner).getAttackTarget();
        }
        else
        {
            return null;
        }
    }


    public boolean performOwnerValidation()
    {
        return this.validateOwner();
    }


    public boolean ownerValidConditions(Entity toValidate)
    {
        return (toValidate instanceof EntityLivingBase);
    }


//Validate owner and return if successful
    public boolean validateOwner()
    {
//If there is a owner UUID
        if(this.ownerUUID != null && this.world instanceof WorldServer)
        {
//But no owner
            if(owner == null)
            {
//Try to get owner from UUID
                EntityLivingBase foundEntity  
                = (EntityLivingBase) ((WorldServer)this.world).getEntityFromUuid(this.ownerUUID);

//If owner found
//and owner conditions met
                if(foundEntity != null && this.ownerValidConditions(foundEntity))
                {
//Restore owner
                    this.owner = foundEntity;
//Check successful
                    return true;
                }
            }
//If both owner and owner UUID, check successful
            else
            {
                return true;
            }
        }


//If no UUID, check failed
        return false;

    }

}
