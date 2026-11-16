package org.sporotofpoorety.eternitymode.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import org.sporotofpoorety.eternitymode.entity.EntityWithOwner;
import org.sporotofpoorety.eternitymode.util.SinWave;




public class EntityWillowisp extends EntityWithOwner
{

    private static final DataParameter<Float> COLOR_R 
        = EntityDataManager.<Float>createKey(EntityWillowisp.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_G 
        = EntityDataManager.<Float>createKey(EntityWillowisp.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> COLOR_B 
        = EntityDataManager.<Float>createKey(EntityWillowisp.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> WISP_SCALE 
        = EntityDataManager.<Float>createKey(EntityWillowisp.class, DataSerializers.FLOAT);

    public String wispMode = "none";



    @Override
    public void onUpdate()
    {
        super.onUpdate();


        if(!this.world.isRemote)
        {
            if(wispMode.equals("playerorbit"))
            {
                if(owner instanceof EntityPlayer)
                {
/*
                    this.posX =
                    this.posY =
                    this.posZ = 
*/
                }
            }
        }
    }
    

    public EntityWillowisp(World worldIn) 
    {
        super(worldIn);
        
        isImmuneToFire = true;
        noClip = true;
        setNoGravity(true);
        
        setSize(0.5f, 0.5f);  
    }

    public EntityWillowisp(World worldIn, double x, double y, double z, EntityLivingBase owner,
    float r, float g, float b, float scale, String wispMode) 
    {
        super(worldIn, x, y, z, owner);

        isImmuneToFire = true;
        noClip = true;
        setNoGravity(true);
        
        setColor(r, g, b);
        setSize(0.5f, 0.5f);
        this.wispMode = wispMode;  
    }


    @Override
    protected void entityInit() 
    {
        this.dataManager.register(COLOR_R, 0.1f);
        this.dataManager.register(COLOR_G, 1.0f);
        this.dataManager.register(COLOR_B, 0.1f);
        this.dataManager.register(WISP_SCALE, 2.0f);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) 
    {
        super.writeEntityToNBT(compound);
        compound.setFloat("ColorR", this.getRed());
        compound.setFloat("ColorG", this.getGreen());
        compound.setFloat("ColorB", this.getBlue());
        compound.setFloat("WispScale", this.getWispScale());
    }
    
    @Override
    public void readEntityFromNBT(NBTTagCompound compound) 
    {
        super.readEntityFromNBT(compound);

        this.setColor
        (
            compound.getFloat("ColorR"),
            compound.getFloat("ColorG"),
            compound.getFloat("ColorB")
        );
        this.setWispScale(compound.getFloat("WispScale"));
    }
    
    
    @Override
    public boolean isEntityInvulnerable(DamageSource source)
    {
        return true;
    }


    public void setColor(float r, float g, float b) 
    {
        this.dataManager.set(COLOR_R, r);
        this.dataManager.set(COLOR_G, g);
        this.dataManager.set(COLOR_B, b);
    }
    public void setWispScale(float wispScale) 
    {
        this.dataManager.set(WISP_SCALE, wispScale);
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
    public float getWispScale() 
    {
        return this.dataManager.get(WISP_SCALE);
    }
    
}
