package org.sporotofpoorety.eternitymode.items.registry;


import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;

import net.minecraftforge.common.util.EnumHelper;




public class EternityModeToolMaterials
{

	public static Map<String, ToolMaterial> toolMaterials = new HashMap<>();


    public static void initToolMaterials()
    {
//Name, harvest, uses, efficiency, damage, enchantability
		toolMaterials.put("harvester", EnumHelper.addToolMaterial("harvester", 3, 1561, 8.0F, 6.0F, 22));
    }
}
