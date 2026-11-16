package org.sporotofpoorety.eternitymode.util;


import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import org.sporotofpoorety.eternitymode.interfacemixins.IMixinEntityMob;




public final class DamageUtil 
{

    public static boolean isHostile(Entity entity)
    {
        if((entity instanceof EntityMob && !((IMixinEntityMob) entity).isTamed()) 
        || (entity instanceof IMob && !(entity instanceof EntityMob)))
        {
            return true;
        }        
        return false;
    }

    public static List<EntityLivingBase> nearTargets(World world, Entity attacker, AxisAlignedBB within)
    {
        List<EntityLivingBase> near = new ArrayList<>();


        if(attacker != null)
        {
            if(isHostile(attacker))
            {
                near = nearPassives(world, attacker, within);
            }
            else
            {
                near = nearHostiles(world, attacker, within);
            }
        }

        return near;
    }

    public static List<EntityLivingBase> nearPassives(World world, Entity attacker, AxisAlignedBB within)
    {
        List<Entity> nearEntities = world.getEntitiesWithinAABB(Entity.class, within);
        List<EntityLivingBase> targets = new ArrayList<>();


        for(Entity nearEntity : nearEntities)
        {
            if(!isHostile(nearEntity))
            {
                targets.add((EntityLivingBase) nearEntity);
            }
        }

        return targets;
    }

    public static List<EntityLivingBase> nearHostiles(World world, Entity attacker, AxisAlignedBB within)
    {
        List<Entity> nearEntities = world.getEntitiesWithinAABB(Entity.class, within);
        List<EntityLivingBase> targets = new ArrayList<>();


        for(Entity nearEntity : nearEntities)
        {
            if(isHostile(nearEntity))
            {
                targets.add((EntityLivingBase) nearEntity);
            }
        }

        return targets;
    }




    public static List<EntityLivingBase> nearTargets(World world, Entity attacker, 
    double x, double y, double z, double xS, double yS, double zS)
    {
        List<EntityLivingBase> near = new ArrayList<>();


        if(attacker != null)
        {
            if(isHostile(attacker))
            {
                near = nearPassives(world, attacker, x, y, z, xS, yS, zS);
            }
            else
            {
                near = nearHostiles(world, attacker, x, y, z, xS, yS, zS);
            }
        }

        return near;
    }

    public static List<EntityLivingBase> nearPassives(World world, Entity attacker, 
    double x, double y, double z, double xS, double yS, double zS)
    {
        List<Entity> nearEntities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x - xS, y - yS, z - zS, x + xS, y + yS, z + zS));
        List<EntityLivingBase> targets = new ArrayList<>();


        for(Entity nearEntity : nearEntities)
        {
            if(!isHostile(nearEntity))
            {
                targets.add((EntityLivingBase) nearEntity);
            }
        }

        return targets;
    }

    public static List<EntityLivingBase> nearHostiles(World world, Entity attacker, 
    double x, double y, double z, double xS, double yS, double zS)
    {
        List<Entity> nearEntities = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(x - xS, y - yS, z - zS, x + xS, y + yS, z + zS));
        List<EntityLivingBase> targets = new ArrayList<>();


        for(Entity nearEntity : nearEntities)
        {
            if(isHostile(nearEntity))
            {
                targets.add((EntityLivingBase) nearEntity);
            }
        }

        return targets;
    }

}
