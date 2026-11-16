package org.sporotofpoorety.eternitymode.entity.projectile;


import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.sporotofpoorety.eternitymode.entity.EntityWithOwner;
import org.sporotofpoorety.eternitymode.util.DamageUtil;




public class EntityProjectileRaytrace extends EntityWithOwner
{

    public boolean firstBeenShot;

    public double speedX;
    public double speedY;
    public double speedZ;

    public double hitCheckSize;
    public boolean collidesEntities;
    public boolean collidesBlocks;

//Niche
    public int splitProjectileCount = 50; 
    public double splitConeRadians = Math.PI;
    public int splitAimMode = 0;
    public int splitLifetime = 100; 
    public float splitDamage = 1.0F; 
    public double splitSpeed = 1.0D;
    public double splitAcceleration = 1.01D;




    public EntityProjectileRaytrace(World worldIn)
    {
        super(worldIn);
    }

    public EntityProjectileRaytrace(World worldIn, double x, double y, double z, 
    EntityLivingBase owner,
    int lifetimeMax, double speedX, double speedY, double speedZ,
    double accelerationRate, double gravitySpeed,
    double hitCheckSize, boolean collidesEntities, boolean collidesBlocks, float damageVal)
    {
        super(worldIn, x, y, z, owner);

        this.setNoGravity(true);

        this.lifetimeMax = lifetimeMax;
        this.firstBeenShot = false;

        this.speedX = speedX;
        this.speedY = speedY;
        this.speedZ = speedZ;
        this.accelerationRate = accelerationRate;
        this.accelerationTracker = accelerationRate;
        this.gravitySpeed = gravitySpeed;

        this.hitCheckSize = hitCheckSize;
        this.collidesEntities = collidesEntities;
        this.collidesBlocks = collidesBlocks;
        this.damageVal = damageVal;
    }


    protected void entityInit()
    {

    }


    @Override
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);


        compound.setBoolean("FirstBeenShot", this.firstBeenShot);

        compound.setDouble("SpeedX", this.speedX);
        compound.setDouble("SpeedY", this.speedY);
        compound.setDouble("SpeedZ", this.speedZ);

        compound.setDouble("HitCheckSize", this.hitCheckSize);
        compound.setBoolean("CollidesEntities", this.collidesEntities);
        compound.setBoolean("CollidesBlocks", this.collidesBlocks);

//Niche
        compound.setInteger("SplitProjectileCount", this.splitProjectileCount);
        compound.setDouble("SplitConeRadians", this.splitConeRadians);
        compound.setInteger("SplitAimMode", this.splitAimMode);
        compound.setInteger("SplitLifetime", this.splitLifetime);
        compound.setFloat("SplitDamage", this.splitDamage);
        compound.setDouble("SplitSpeed", this.splitSpeed);
        compound.setDouble("SplitAcceleration", this.splitAcceleration);
    }


    @Override
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);


        if (compound.hasKey("FirstBeenShot")) { this.firstBeenShot = compound.getBoolean("FirstBeenShot"); }

        if (compound.hasKey("SpeedX")) { this.speedX = compound.getDouble("SpeedX"); }
        if (compound.hasKey("SpeedY")) { this.speedY = compound.getDouble("SpeedY"); }
        if (compound.hasKey("SpeedZ")) { this.speedZ = compound.getDouble("SpeedZ"); }

        if (compound.hasKey("HitCheckSize")) { this.hitCheckSize = compound.getDouble("HitCheckSize"); }
        if (compound.hasKey("CollidesEntities")) { this.collidesEntities = compound.getBoolean("CollidesEntities"); }
        if (compound.hasKey("CollidesBlocks")) { this.collidesBlocks = compound.getBoolean("CollidesBlocks"); }

//Niche
        if (compound.hasKey("SplitProjectileCount")) { this.splitProjectileCount = compound.getInteger("SplitProjectileCount"); }
        if (compound.hasKey("SplitConeRadians")) { this.splitConeRadians = compound.getDouble("SplitConeRadians"); } 
        if (compound.hasKey("SplitAimMode")) { this.splitAimMode = compound.getInteger("SplitAimMode"); }
        if (compound.hasKey("SplitLifetime")) { this.splitLifetime = compound.getInteger("SplitLifetime"); }
        if (compound.hasKey("SplitDamage")) { this.splitDamage = compound.getFloat("SplitDamage"); }
        if (compound.hasKey("SplitSpeed")) { this.splitSpeed = compound.getDouble("SplitSpeed"); } 
        if (compound.hasKey("SplitAcceleration")) { this.splitAcceleration = compound.getDouble("SplitAcceleration"); } 
    }




    public void onUpdate()
    {
//EntityWithOwner update
        super.onUpdate();


//Apply initial speed
        this.initialSpeedCheck();

//Check for collision
        RayTraceResult collisionResult = this.collideTotal();

//Make collision decision
        this.decideCollision(collisionResult);

//Set and clamp rotation
        this.setProjectileRotation();
        this.clampProjectileRotation();

//Projectile movement logic
        this.performBasicMovement();
    }


//Check for initial speed apply
    public void initialSpeedCheck()
    {
        if(!this.firstBeenShot)
        {
            this.motionX = this.speedX;
            this.motionY = this.speedY;
            this.motionZ = this.speedZ;

            this.firstBeenShot = true;
        }
    }


//Check and return collision   
    protected RayTraceResult collideTotal()
    {
        Vec3d vecStart = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vecEnd = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

//Check blocks and entities
        RayTraceResult collisionResult = this.collideBlocks(vecStart, vecEnd);
        collisionResult = this.collideEntities(collisionResult, vecStart, vecEnd);

        return collisionResult;
    }


//Check collision (blocks)
    public RayTraceResult collideBlocks(Vec3d vecStart, Vec3d vecEnd)
    {
        if(!this.noClip)
        {
            RayTraceResult blockCollision = this.world.rayTraceBlocks(vecStart, vecEnd,
//stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock
            false, true, false);

//If this collided, truncate vecEnd
            if (blockCollision != null)
            {
                vecEnd = new Vec3d(blockCollision.hitVec.x, blockCollision.hitVec.y, blockCollision.hitVec.z);
            }

            return blockCollision;
        }
        else
        {
            return null;
        }
    }

//Check collision (entities)
    public RayTraceResult collideEntities(RayTraceResult collisionResult, Vec3d vecStart, Vec3d vecEnd)
    {
//Closest hit entity null by default
        Entity closestHitEntity = null;

//Entities within BB + motion + 1 block of lenience
        List<Entity> entitiesNearby = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(this.hitCheckSize + 0.5D));

//Closest hit entity distance squared
        double closestHitDistanceSq = 0.0D;


//For each nearby entity
        for (Entity candidateEntity : entitiesNearby)
        {
//Check this projectile's hit entity conditions
            if(!this.projectileHitEntityConditions(candidateEntity))
            {
                continue;    
            }

//Get and grow entity bounding box (hitcheck)
                AxisAlignedBB entityBB = candidateEntity.getEntityBoundingBox().grow(this.hitCheckSize);
//Calc if entity BB is in projectile's current motion path
                RayTraceResult entityHitResult = entityBB.calculateIntercept(vecStart, vecEnd);


//If entity is in motion path
            if (entityHitResult != null)
            {
//Get distance sq
                double candidateHitDistanceSq = vecStart.squareDistanceTo(entityHitResult.hitVec);

//If this is the first near enough candidate
                if (closestHitDistanceSq == 0.0D 
//Or candidate closer than current closest candidate
                || candidateHitDistanceSq < closestHitDistanceSq)
                {
//This is now the closest hit entity
                    closestHitEntity = candidateEntity;
//And set closest candidate distance to this candidate's
                    closestHitDistanceSq = candidateHitDistanceSq;
                }
            }                  
        }

//If an entity got hit at all
        if (closestHitEntity != null)
        {
//Override block collision with entity collision
            collisionResult = new RayTraceResult(closestHitEntity);
        }

        return collisionResult;
    }


    public boolean projectileHitEntityConditions(Entity entityToCheck)
    {
//If entity can't be collided
        if (!entityToCheck.canBeCollidedWith()
//Or is not EntityLivingBase
        || !(entityToCheck instanceof EntityLivingBase)
//Or is owner of projectile
        || entityToCheck == this.owner
//Or is on projectile's team
        || entityToCheck.isOnSameTeam(this)
//Or is on (non-null) owner's team
        || (this.owner != null && (entityToCheck.isOnSameTeam(this.owner))))
        {
//Then it does not meet conditions
            return false;
        }

//Else it does
        return true;
    }


//Decide what collision to do
    protected void decideCollision(RayTraceResult collisionResult)
    {
//If anything got hit at all
        if (collisionResult != null)
        {
//Ignore portals
            if (collisionResult.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(collisionResult.getBlockPos()).getBlock() == Blocks.PORTAL)
            {
//              this.setPortal(collisionResult.getBlockPos());
            }
//Otherwise performs collision
            else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, collisionResult))
            {
                this.onHit(collisionResult);
            }
        }
    }


//When projectile collides
    public void onHit(RayTraceResult rayTraceResult)
    {
//If raytrace hit an entity
        if (rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY) 
        {
//Ensure an entity was actually hit
            if(rayTraceResult.entityHit != null)
            {
//Perform on hitting entity logic
                onHitEntity(rayTraceResult, rayTraceResult.entityHit);
            }
        } 
//If raytrace hit a block
        else if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) 
        {
//Get block pos
            BlockPos posHit = rayTraceResult.getBlockPos();
//Get block hit
            Block blockHit = this.world.getBlockState(posHit).getBlock();

//Perform on hitting block logic
            onHitBlock(rayTraceResult, blockHit);        
        }
    }


//Already preemptively null-checked to enable
//projectile-specific logic to safely run before this setDead()
    public void onHitEntity(RayTraceResult rayTraceResult, Entity entityHit)
    {
        if(!world.isRemote)
        {
            boolean ownerHostile = DamageUtil.isHostile(owner);
            boolean targetHostile = DamageUtil.isHostile(entityHit);

//If owner is not null
            if (this.owner != null && (ownerHostile != targetHostile) && this.damageVal > 0.1F)
            {
//Attack entity
                entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.owner).setProjectile(), this.damageVal);
            }

//Can set whether projectile stops at entities
            if(this.collidesEntities)
            {
                this.onTrueSetDead();
                this.setDead();
                return;
            }
        }
    }


//Block hit logic
    public void onHitBlock(RayTraceResult rayTraceResult, Block blockHit)
    {
//Can set whether projectile stops at blocks
        if(!world.isRemote && !this.noClip)
        {
            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.onTrueSetDead();
            this.setDead();
            return;
        }
    }




//Set projectile rotation
    public void setProjectileRotation()
    {
//Get horizontal speed
        float horizontalSpeed = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

//Set horizontal rotation
        this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * 360D / (2 * Math.PI));
//Set vertical rotation
        this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) horizontalSpeed) * 360D / (2 * Math.PI));

//Rotation smoothing
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    }


//Clamp projectile rotation
    public void clampProjectileRotation()
    {
//If previous vertical rotation above positive bound
        while (this.rotationPitch - this.prevRotationPitch < -180.0F)
        {
//Adjust it negatively
            this.prevRotationPitch -= 360.0F;
        }

//If previous vertical rotation below negative bound
        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
//Adjust it positively
            this.prevRotationPitch += 360.0F;
        }

//If previous horizontal rotation above positive bound
        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
//Adjust it negatively
            this.prevRotationYaw -= 360.0F;
        }

//If previous horizontal rotation below negative bound
        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
//Adjust it positively
            this.prevRotationYaw += 360.0F;
        }
    }


    @Override
    public void performBasicMovement()
    {
        double movedX = this.motionX * this.accelerationTracker, movedY = this.motionY * (this.acceleratesVertically ? this.accelerationTracker : 1.0D), movedZ = this.motionZ * this.accelerationTracker;

//Have to apply motion myself yes
        this.posX += movedX;
        this.posY += movedY;
        this.posZ += movedZ;
//Update anchor and position
        if(this.anchorUse) { anchor = anchor.add(movedX, movedY, movedZ); }
        this.setPosition(this.posX, this.posY, this.posZ);

//Apply acceleration and gravity
        this.accelerationApply();

        if (!this.hasNoGravity())
        {
            this.motionY -= this.gravitySpeed;
        }
    }




//This is apparently universal
//to every vanilla entity, initial rotation
    @SideOnly(Side.CLIENT)
    public void setVelocity(double x, double y, double z)
    {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt(x * x + z * z);
            this.rotationPitch = (float)(MathHelper.atan2(y, f) * (180D / Math.PI));
            this.rotationYaw = (float)(MathHelper.atan2(x, z) * (180D / Math.PI));
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

}
