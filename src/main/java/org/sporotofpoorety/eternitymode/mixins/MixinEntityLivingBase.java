package org.sporotofpoorety.eternitymode.mixins;


import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

import org.sporotofpoorety.eternitymode.interfacemixins.IMixinEntityLivingBase;
import org.sporotofpoorety.eternitymode.packets.LaserDataPacket;
import org.sporotofpoorety.eternitymode.util.LaserData;
import org.sporotofpoorety.eternitymode.util.PacketUtil;





//Mixin this class
@Mixin(value = EntityLivingBase.class, remap = true)
//Abstract since mixins should not be instantiated
public abstract class MixinEntityLivingBase implements IMixinEntityLivingBase
{

    @Unique
    private static final DataParameter<Boolean> HAS_AFTERIMAGES = EntityDataManager.<Boolean>createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);
    @Unique
    private static final DataParameter<Float> AFTERIMAGE_SCALE = EntityDataManager.<Float>createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    @Unique
    private ArrayList<LaserData> laserBeams = new ArrayList<>();




    @Inject
    (
        method = "entityInit",
        at = @At("TAIL")
    )
//On entity init
    private void entityInitNewDataParameter(CallbackInfo callInfo)
    {
        Entity selfEntity = (Entity) (Object) this;
//Register the new data parameters
        selfEntity.getDataManager().register(HAS_AFTERIMAGES, Boolean.valueOf(false));
        selfEntity.getDataManager().register(AFTERIMAGE_SCALE, Float.valueOf(1.0F));         
    }



//New misc methods

    public void syncLaserStateToClient()
    {
        Entity selfEntity = (Entity) (Object) this;
        PacketUtil.sendPacketToNearbyPlayers(selfEntity.world, selfEntity.posX, selfEntity.posY, selfEntity.posZ, 300.0D, 
            new LaserDataPacket(selfEntity.getEntityId(), this.getLaserBeams()));
    }


//New getters

    public boolean getHasAfterimages()
    {
        Entity selfEntity = (Entity) (Object) this;
        return ((Boolean)selfEntity.getDataManager().get(HAS_AFTERIMAGES)).booleanValue();
    }

    public float getAfterimageScale()
    {
        Entity selfEntity = (Entity) (Object) this;
        return ((Float)selfEntity.getDataManager().get(AFTERIMAGE_SCALE)).floatValue();
    }

    public ArrayList<LaserData> getLaserBeams()
    {
        return this.laserBeams;
    }




//New setters

    public void setHasAfterimages(boolean hasAfterimages)
    {
        Entity selfEntity = (Entity) (Object) this;
        selfEntity.getDataManager().set(HAS_AFTERIMAGES, Boolean.valueOf(hasAfterimages)); 
    }

    public void setAfterimageScale(float scale) 
    {
        Entity selfEntity = (Entity) (Object) this;
        selfEntity.getDataManager().set(AFTERIMAGE_SCALE, Float.valueOf(scale));
    }

    public void setLaserBeams(ArrayList<LaserData> laserBeams)
    {
        this.laserBeams = laserBeams;
    }

}
