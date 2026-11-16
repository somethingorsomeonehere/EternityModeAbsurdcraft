package org.sporotofpoorety.eternitymode.packets;


import java.util.ArrayList;

import io.netty.buffer.ByteBuf;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import org.sporotofpoorety.eternitymode.interfacemixins.IMixinEntityLivingBase;
import org.sporotofpoorety.eternitymode.util.LaserData;




public class LaserDataPacket implements IMessage 
{

    private int entityId;
    private ArrayList<LaserData> laserList;
    
    public LaserDataPacket() {} 
    
    public LaserDataPacket(int entityId, ArrayList<LaserData> laserList) 
    {
        this.entityId = entityId;
        this.laserList = laserList;
    }
    

    @Override
    public void toBytes(ByteBuf buf) 
    {
//Write entity id
        buf.writeInt(entityId);

//Write laser list size
        buf.writeInt(laserList.size());
        
//Write each LaserData
        for (LaserData laser : laserList) 
        {
            buf.writeInt(laser.laserType);
            buf.writeDouble(laser.laserLen);
            buf.writeDouble(laser.laserDir.x);
            buf.writeDouble(laser.laserDir.y);
            buf.writeDouble(laser.laserDir.z);
            buf.writeFloat(laser.red);
            buf.writeFloat(laser.green);
            buf.writeFloat(laser.blue);
        }
    }
    
    @Override
    public void fromBytes(ByteBuf buf) 
    {
//Read entity id
        this.entityId = buf.readInt();
       
//Prepare laser list
        int laserCount = buf.readInt();
        this.laserList = new ArrayList<>();
        
//Fill it
        for (int i = 0; i < laserCount; i++) 
        {
            int type = buf.readInt();
            double len = buf.readDouble();
            Vec3d dir = new Vec3d
            (
                buf.readDouble(),
                buf.readDouble(),
                buf.readDouble()
            );
            float r = buf.readFloat();
            float g = buf.readFloat();
            float b = buf.readFloat();
            
            this.laserList.add(new LaserData(type, len, dir, r, g, b));
        }
    }
    
//Packet handler
    public static class Handler implements IMessageHandler<LaserDataPacket, IMessage> 
    {
//On receiving message
        @Override
        public IMessage onMessage(LaserDataPacket msg, MessageContext ctx) 
        {
//Schedule client task
            Minecraft.getMinecraft().addScheduledTask
            (
//Lambda
                () -> {
//Get client world
                    World worldIn = Minecraft.getMinecraft().world;
//Get entity from numeric id
                    Entity entity = worldIn.getEntityByID(msg.entityId);
//If entity valid
                    if (entity != null && entity instanceof EntityLivingBase) 
                    {
                        ((IMixinEntityLivingBase) entity).setLaserBeams(msg.laserList);
                    }
                }
            );

            return null;
        }
    }
}
