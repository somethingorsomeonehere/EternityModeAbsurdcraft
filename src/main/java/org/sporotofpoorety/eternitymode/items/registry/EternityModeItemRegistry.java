package org.sporotofpoorety.eternitymode.items.registry;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.sporotofpoorety.eternitymode.Tags;
import org.sporotofpoorety.eternitymode.items.*;
import org.sporotofpoorety.eternitymode.items.registry.EternityModeToolMaterials;




@Mod.EventBusSubscriber
public class EternityModeItemRegistry
{
	public static Map<String, Item> ITEMS = new HashMap<>();


    public static void addItem(RegistryEvent.Register<Item> event, Item item, String id, CreativeTabs tab)
    {
//Add item
		Item toAdd = item.setRegistryName(new ResourceLocation(Tags.MOD_ID, id))
			.setTranslationKey(Tags.MOD_ID + "." + id)
			.setCreativeTab(tab);
		event.getRegistry().register(toAdd);

		ITEMS.put(id, toAdd);
    }

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) 
    {
        EternityModeToolMaterials.initToolMaterials();

        Map<String, ToolMaterial> materials = EternityModeToolMaterials.toolMaterials;


        addItem(event, new ItemHarvesterScythe(materials.get("harvester")), "harvester_scythe", CreativeTabs.TOOLS);
        addItem(event, new ItemSoulsGuidance(), "souls_guidance", CreativeTabs.TOOLS);
    
	}
}
