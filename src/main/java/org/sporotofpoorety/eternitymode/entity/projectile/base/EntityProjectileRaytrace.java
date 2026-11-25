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
import org.sporotofpoorety.eternitymode.util.RotationUtil;




public class EntityProjectileRaytrace extends EntityWithOwner
{

    public boolean firstBeenShot;

    public double speedX;
    public double speedY;
    public double speedZ;

    public boolean stopsAtEntity = true;
    public boolean stopsAtBlock = true;

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
    int lifetimeMax, 
    double speedX, double speedY, double speedZ, double accelerationRate,
    boolean stopsAtEntity, boolean stopsAtBlock, float damageVal)
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

        this.stopsAtEntity = stopsAtEntity;
        this.stopsAtBlock = stopsAtBlock;
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

        compound.setBoolean("StopsAtEntity", this.stopsAtEntity);
        compound.setBoolean("StopsAtBlock", this.stopsAtBlock);

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

        if (compound.hasKey("StopsAtEntity")) { this.stopsAtEntity = compound.getBoolean("StopsAtEntity"); }
        if (compound.hasKey("StopsAtBlock")) { this.stopsAtBlock = compound.getBoolean("StopsAtBlock"); }

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
        super.onUpdate();

        
        if(!this.world.isRemote)
        {
//Check for initial speed
            this.initialSpeedCheck();
//Movement logic
            this.performBasicMovement();
            this.performOffsets();
        }
//Set and clamp rotation
        RotationUtil.setEntityRotFromMotion(this);
        RotationUtil.clampEntityPrevRot(this);

//Run collision
        if(!this.world.isRemote) { this.collisionLogic(); }
    }


    protected void collisionLogic()
    {
//Check start and end movement
        Vec3d vecStart = new Vec3d(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ);
        Vec3d vecEnd = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d tickMv = this.getTickMv();

//Sweep for entities roughly in trajectory
        List<Entity> entitySweep = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(tickMv.x, tickMv.y, tickMv.z).grow(0.5D));

//Check for then perform any collision
        RayTraceResult collisionResult = this.detectBlockCollision(vecStart, vecEnd);
        collisionResult = this.detectEntityCollision(collisionResult, vecStart, vecEnd);
//Last tests if should collide
        this.collideIfShould(collisionResult);
    }

    
    public RayTraceResult detectBlockCollision(Vec3d vecStart, Vec3d vecEnd)
    {
        if(!this.noClip)
        {
            RayTraceResult blockCollision = this.world.rayTraceBlocks(vecStart, vecEnd,
//stopOnLiquid, ignoreBlockWithoutBoundingBox, returnLastUncollidableBlock
            false, true, false);

//If a block is in the way, truncate vecEnd
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

    public RayTraceResult detectEntityCollision(RayTraceResult collisionResult, Vec3d vecStart, Vec3d vecEnd)
    {
        Vec3d mvVec = vecEnd.subtract(vecStart);
        return detectEntityCollision(collisionResult, vecStart, vecEnd, mvVec);
    }
    public RayTraceResult detectEntityCollision(RayTraceResult collisionResult, Vec3d vecStart, Vec3d vecEnd, Vec3d mvVec)
    {
//Tick's movement and sweep
        List<Entity> entitySweep = this.world.getEntitiesWithinAABBExcludingEntity(this, 
            this.getEntityBoundingBox().expand(mvVec.x, mvVec.y, mvVec.z).grow(0.5d * this.width + 0.5d, 0.5d * this.height + 0.5d, 0.5d * this.width + 0.5d));
//Closest entity and its distance
        Entity closestHitEntity = null;
        double closestHitDistSq = 0.0D;


//For each near entity
        for (Entity candidateEntity : entitySweep)
        {
//Check hit conditions
            if(!this.hitEntityConditions(candidateEntity))
            {
                continue;    
            }

//Check if entity is in expected trajectory
            AxisAlignedBB adjustedBB = candidateEntity.getEntityBoundingBox().grow(0.5d * this.width + 0.25d, 0.5d * this.height + 0.5d, 0.25d * this.width + 0.25d);
            RayTraceResult entityHitResult = adjustedBB.calculateIntercept(vecStart, vecEnd);

//Check if it's closest entity
            if (entityHitResult != null)
            {
                double candidateHitDistSq = vecStart.squareDistanceTo(entityHitResult.hitVec);

                if (closestHitDistSq == 0.0D 
                || candidateHitDistSq < closestHitDistSq)
                {
                    closestHitEntity = candidateEntity;
                    closestHitDistSq = candidateHitDistSq;
                }
            }                  
        }

//And if any, make that the collision result
        if (closestHitEntity != null)
        {
            collisionResult = new RayTraceResult(closestHitEntity);
        }

        return collisionResult;
    }

    public boolean hitEntityConditions(Entity entity)
    {
        if(entity.canBeCollidedWith()
        && DamageUtil.canTarget(this.owner, entity))
        {
            return true;
        }

        return false;
    }


//Last tests if should collide
    protected void collideIfShould(RayTraceResult collisionResult)
    {
        if (collisionResult != null)
        {
//If not going into portal
            if (collisionResult.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(collisionResult.getBlockPos()).getBlock() == Blocks.PORTAL)
            {
                this.setPortal(collisionResult.getBlockPos());
            }
//Nor being externally handled
            else if (!net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, collisionResult))
            {
//Proceed collision
                this.onHit(collisionResult);
            }
        }
    }


//Branch on entity or block collision
    public void onHit(RayTraceResult rayTraceResult)
    {
        if (rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY) 
        {
            onHitEntity(rayTraceResult, rayTraceResult.entityHit);
        } 
        else if (rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) 
        {
            BlockPos posHit = rayTraceResult.getBlockPos();
            Block blockHit = this.world.getBlockState(posHit).getBlock();

            onHitBlock(rayTraceResult, blockHit);        
        }
    }


    public void onHitEntity(RayTraceResult rayTraceResult, Entity entityHit)
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
        if(this.stopsAtEntity)
        {
            this.onTrueSetDead();
            this.setDead();
            return;
        }
    }


//Block hit logic
    public void onHitBlock(RayTraceResult rayTraceResult, Block blockHit)
    {
        if(this.stopsAtBlock)
        {
            this.motionX = this.motionY = this.motionZ = 0.0D;
            this.onTrueSetDead();
            this.setDead();
            return;
        }
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
