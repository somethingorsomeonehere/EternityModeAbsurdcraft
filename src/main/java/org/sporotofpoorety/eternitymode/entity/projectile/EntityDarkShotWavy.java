package org.sporotofpoorety.eternitymode.entity.projectile;


import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


import org.sporotofpoorety.eternitymode.entity.projectile.EntityDarkShotLinear;
import org.sporotofpoorety.eternitymode.util.RotateAt;
import org.sporotofpoorety.eternitymode.util.SinWave;




public class EntityDarkShotWavy extends EntityDarkShotLinear 
{

    public EntityDarkShotWavy(World worldIn) 
    {
        super(worldIn);
    }

    public EntityDarkShotWavy(World worldIn, double x, double y, double z, 
    EntityLivingBase owner,
    int lifetimeMax, 
    double speedX, double speedY, double speedZ, double accelerationRate, 
    boolean stopsAtEntity, boolean stopsAtBlock, float damageVal,
    RotateAt rotAt, SinWave sinWv)
    {
        super(worldIn, x, y, z,
        owner,
        lifetimeMax, 
        speedX, speedY, speedZ, accelerationRate, 
        stopsAtEntity, stopsAtBlock, damageVal);
        this.rotMoves = true;
        this.sinMoves = true;

        this.rotAt = rotAt;
        this.sinWv = sinWv;
    }




    @Override
    public void onUpdate()
    {
        super.onUpdate();

		if(!world.isRemote)
        {
//Rotate and go in and out, like rotmg staff
            if(realTicksExisted > 20 && rotMoves && sinMoves)
            {
                Vec3d combo = rotAt.getDirAt().scale(sinWv.valAt * sinWv.scale);

                move(MoverType.SELF, anchor.x + combo.x - this.posX, anchor.y + combo.y - this.posY, anchor.z + combo.z - this.posZ);
            
                rotAt.rotInc();
                sinWv.sinInc();
            }
        }
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
