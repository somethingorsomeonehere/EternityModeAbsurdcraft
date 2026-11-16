package org.sporotofpoorety.eternitymode.items.registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;


import org.sporotofpoorety.eternitymode.Tags;
import org.sporotofpoorety.eternitymode.items.registry.EternityModeItemRegistry;




@EventBusSubscriber(modid = Tags.MOD_ID, value = Side.CLIENT)
public class EternityModeItemModels 
{

	private static final List<Item> REGISTERED_ITEM_MODELS = new ArrayList<>();

	@SubscribeEvent
	public static void registerItemModels(ModelRegistryEvent event) 
    {
        EternityModeItemRegistry.ITEMS.forEach((id, item)
        -> {
			if (!REGISTERED_ITEM_MODELS.contains(item)) 
            {
				registerItemModel(item);
			}
        });
	}

	private static void registerItemModel(Item item) 
    {
		registerCustomItemModel(item, 0, item.getRegistryName().toString(), "inventory");
	}

	private static void registerCustomItemModel(Item item, int meta, String modelLocation, String variant) 
    {
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(modelLocation, variant));
		REGISTERED_ITEM_MODELS.add(item);
	}

}
