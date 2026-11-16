package org.sporotofpoorety.eternitymode.entity;

import java.util.List;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;




public class EntityLightningCustom extends Entity
{
//Declares which state the lightning bolt is in. Whether it's in the air, hit the ground, etc.
    private int outerTimer;

//Determines the time before the EntityLightningCustom is destroyed. It is a random integer decremented over time.
    private int innerTimer;

//A random long that is used to change the vertex of the lightning rendered in RenderLightningCustom
    public long boltVertex;

    private static final DataParameter<Float> COLOR_R 
        = EntityDataManager.<Float>createKey(EntityLightningCustom.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_G 
        = EntityDataManager.<Float>createKey(EntityLightningCustom.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_B 
        = EntityDataManager.<Float>createKey(EntityLightningCustom.class, DataSerializers.FLOAT);



    public EntityLightningCustom(World worldIn)
    {
        super(worldIn);
        this.ignoreFrustumCheck = true;
        this.outerTimer = 2;
        this.innerTimer = this.rand.nextInt(3) + 1;
        this.boltVertex = this.rand.nextLong();
    }

    public EntityLightningCustom(World worldIn, double x, double y, double z)
    {
        super(worldIn);
        this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
        this.ignoreFrustumCheck = true;
        this.outerTimer = 2;
        this.innerTimer = this.rand.nextInt(3) + 1;
        this.boltVertex = this.rand.nextLong();
    }


    @Override
    protected void entityInit() 
    {
        this.dataManager.register(COLOR_R, 0.45f);
        this.dataManager.register(COLOR_G, 0.45f);
        this.dataManager.register(COLOR_B, 0.50f);
    }


    public void setColor(float r, float g, float b) 
    {
        this.dataManager.set(COLOR_R, r);
        this.dataManager.set(COLOR_G, g);
        this.dataManager.set(COLOR_B, b);
    }


    public float getRed() 
    {
        return this.dataManager.get(COLOR_R);
    }
    public float getGreen() 
    {
        return this.dataManager.get(COLOR_G);
    }
    public float getBlue() 
    {
        return this.dataManager.get(COLOR_B);
    }


    public SoundCategory getSoundCategory()
    {
        return SoundCategory.WEATHER;
    }


    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        --this.outerTimer;

        if (this.outerTimer < 0)
        {
            if (this.innerTimer == 0)
            {
                this.setDead();
            }
            else if (this.outerTimer < -this.rand.nextInt(10))
            {
                --this.innerTimer;
                this.outerTimer = 1;

                if (!this.world.isRemote)
                {
                    this.boltVertex = this.rand.nextLong();
                }
            }
        }
    }


    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound compound)
    {
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound compound)
    {
    }


    @Override
    public boolean isInRangeToRenderDist(double distance)
    {
        return true;
    }

    @Override
    public boolean isInRangeToRender3d(double x, double y, double z)
    {
        return true;
    }

    @Override
    public boolean shouldRenderInPass(int pass)
    {
        return true;
    }


    @Override
    public AxisAlignedBB getRenderBoundingBox()
    {
        return new AxisAlignedBB
        (
            posX - 69420.0D, posY, posZ - 69420.0D,
            posX + 69420.0D, posY + 69420.0D, posZ + 69420.0D
        );
    }
}
