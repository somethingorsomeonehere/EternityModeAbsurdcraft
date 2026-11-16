package org.sporotofpoorety.eternitymode.util;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import electroblob.wizardry.Wizardry;




public final class PlayerEffectsUtil 
{

    public static boolean isPosCloseToAnyPlayer(World world, double atX, double atZ, double distLimit, boolean countSpectator)
    {
        EntityPlayer closestPlayer = world.getClosestPlayer(atX, 127.0D, atZ, 69420.0D, countSpectator);

        return isPosCloseToPlayer(world, closestPlayer, atX, atZ, distLimit);
    }


    public static boolean isPosCloseToPlayer(World world, EntityPlayer player, double atX, double atZ, double distLimit)
    {
        if(player == null)
        {
            return false;
        }
        else
        {
            if(Math.sqrt(Math.pow(player.posX - atX, 2) + Math.pow(player.posZ - atZ, 2)) < distLimit)
            {
                return true;
            }
            else
            {
                return false;
            }
        } 
    }


    public static void screenShakeForNearbyPlayers(int shakeIntensity)
    {
        EntityPlayer localPlayer = Minecraft.getMinecraft().player;
        Wizardry.proxy.shakeScreen(localPlayer, shakeIntensity);
    }


//Necessary for server side sound
    public static void sendPacketToNearSound(World worldIn, double atX, double atY, double atZ, double maxDist,
    SoundEvent soundEvent, SoundCategory category, float volume, float pitch)
    {
        for(EntityPlayer player : worldIn.playerEntities)
        {
            if(player instanceof EntityPlayerMP)
            {
                if(player.getDistance(atX, atY, atZ) <= maxDist)
                {
                    ((EntityPlayerMP)player).connection.sendPacket
                    (
                        new SPacketSoundEffect
                        (
                            soundEvent, category, atX, atY, atZ, volume, pitch
                        ) 
                    );
                }
            }
        }
    }


    public static void verticalPlayerDamage(World worldIn, EntityLivingBase source, 
    double atX, double atZ, double maxDist, float dmg)
    {
        for(EntityPlayer player : worldIn.playerEntities)
        {
            if(player instanceof EntityPlayerMP)
            {
                if(Math.sqrt(Math.pow(player.posX - atX, 2) + Math.pow(player.posZ - atZ, 2)) < maxDist)
                {
                    player.attackEntityFrom(DamageSource.causeMobDamage(source), dmg);
                }
            }
        }
    }
}

