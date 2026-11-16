package org.sporotofpoorety.eternitymode.items;


import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import org.sporotofpoorety.eternitymode.entity.projectile.EntityGreenShotHoming;




public class ItemSoulsGuidance extends Item
{

	public ItemSoulsGuidance() 
    {
		super();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) 
    {
		return false;
	}


//Trying to implement continuous attack
    @Override
    public int getMaxItemUseDuration(ItemStack stack) 
    {
        return 72000; 
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) 
    {
        return EnumAction.NONE;
    }

//On right click
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) 
    {
//Set hand active
		playerIn.setActiveHand(handIn);
//Get this itemstack
		ItemStack stack = playerIn.getHeldItem(handIn);
//And set action successful
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}


    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) 
    {
//Server side
        if (entityIn instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) entityIn;

//If actively holding the item
            if (player.isHandActive() && player.getActiveItemStack() == stack) 
            {
                int heldTicks = player.getItemInUseMaxCount();
                int projTime = 20; 
                int soulTime = 60;

                if (heldTicks > 0) 
                {
                    if(heldTicks % 10 == 0)
                    {
                        shootProj(stack, worldIn, player);
                    } 
                    if(heldTicks % 100 == 0)
                    {
                        spawnWisp(stack, worldIn, player);
                    } 
                }
            }
        }
    }

    public void shootProj(ItemStack stack, World world, EntityPlayer player) 
    {
        Vec3d playerEyesFullTick = player.getPositionEyes(1.0F);
        Vec3d playerLookDir = player.getLookVec();

/*
        player.world.playSound(null, player.posX, player.posY, player.posZ,
            EternityModeSoundEvents.ENTITY_SCYTHE_SWING, SoundCategory.PLAYERS, 6.0F, 1.0F);
*/

            EntityGreenShotHoming greenShot = new EntityGreenShotHoming
            (
                world,
                playerEyesFullTick.x, playerEyesFullTick.y, playerEyesFullTick.z,
                player,
                200,
                playerLookDir.x * 0.25D, playerLookDir.y * 0.25D, playerLookDir.z * 0.25D, 
                1.0D, 0.0D, 
                0.6D, true, true, -5.0F,
                0, false, 69420, 0.6D, 0
            );

        player.world.spawnEntity(greenShot);
    }

    public void spawnWisp(ItemStack stack, World world, EntityPlayer player) 
    {

    }

}
