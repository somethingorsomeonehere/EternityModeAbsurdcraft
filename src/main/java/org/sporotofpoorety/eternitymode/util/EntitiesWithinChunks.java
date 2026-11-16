package org.sporotofpoorety.eternitymode.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.util.ClassInheritanceMultiMap;

import org.sporotofpoorety.eternitymode.interfacemixins.IMixinEntityMob;
import org.sporotofpoorety.eternitymode.util.DamageUtil;


public final class EntitiesWithinChunks
{

    public static EntityLiving getNearestHostile(World world, double x, double z, int chunkRadius)
    {
        ArrayList<EntityLiving> entities = getMobsInChunkRadius(world, x, z, chunkRadius);

        EntityLiving nearest = null;
        double minDist = 69420.0D;


        for(EntityLiving entity : entities)
        {
            double distSq = Math.sqrt((entity.posX - x) * (entity.posX - x) + (entity.posZ - z) * (entity.posZ - z));

            if (distSq < minDist) 
            {
                minDist = distSq;
                nearest = entity;
            }
        }

        return nearest;
    }

//Get list of mobs within chunk radius
    public static ArrayList<EntityLiving> getMobsInChunkRadius(World world, double x, double z, int chunkRadius)
    {
//Just server sided
        if (!(world instanceof WorldServer))
        {
//Client side return empty list lol
            return null;
        }


//List to fill with mobs
        ArrayList<EntityLiving> mobsList = new ArrayList<>();

//This is what vanilla does to 
//convert xyz to chunk coords btw, first
//it floors the position then it bit shifts (faster way to divide by 16)
        int centerChunkX = MathHelper.floor(x) >> 4;
        int centerChunkZ = MathHelper.floor(z) >> 4;


//Get world server and chunk provider
        WorldServer worldServer = (WorldServer) world;
        ChunkProviderServer chunkProvider = worldServer.getChunkProvider();


//Iterate chunk X and Z
        for (int currentChunkX = centerChunkX - chunkRadius; currentChunkX <= centerChunkX + chunkRadius; currentChunkX++)
        {
            for (int currentChunkZ = centerChunkZ - chunkRadius; currentChunkZ <= centerChunkZ + chunkRadius; currentChunkZ++)
            {
//Get loaded chunk from chunk provider
                Chunk currentChunk = chunkProvider.getLoadedChunk(currentChunkX, currentChunkZ);

//If chunk null then skip
                if (currentChunk == null)
                {
                    continue;
                }

//ClassInheritanceMultimap is 
//a weird Minecraft specific collection

//They're multimaps where 
//the keys correspond to classes,
//the entities are grouped under multiple keys
//by their inheritance tree, allowing getting class-sorted entity lists


//For each entity multimap in this chunk's list of multimaps
                for (ClassInheritanceMultiMap<Entity> sortedEntityList : currentChunk.getEntityLists())
                {
//Get only hostile mobs
                    for (EntityLiving entity : sortedEntityList.getByClass(EntityLiving.class))
                    {
                        if(DamageUtil.isHostile(entity))
                        {
                            mobsList.add(entity);
                        }
                    }
                }
            }
        }

        return mobsList;
    }

}


