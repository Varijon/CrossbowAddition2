package com.varijon.tinies.CrossbowAdditions.util;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.varijon.tinies.CrossbowAdditions.key.NBTKeys;

import net.md_5.bungee.api.ChatColor;

public class Util 
{

	public static ItemStack checkForRepeater(ItemStack item)
	{
		if(item != null)
		{
			if(item.hasItemMeta())
			{
				if(item.getItemMeta().getPersistentDataContainer().has(NBTKeys.isRepeater))
				{
					return item;
				}
			}
		}
		return null;
	}
	
	public static ItemStack createRepeater()
	{
		ItemStack crossbow = new ItemStack(Material.CROSSBOW);
		ItemMeta meta = crossbow.getItemMeta();
		meta.getPersistentDataContainer().set(NBTKeys.isRepeater, PersistentDataType.INTEGER, 1);
//		meta.getPersistentDataContainer().set(NBTKeys.storedArrows, DataType.ITEM_STACK_ARRAY,null);
//		meta.getPersistentDataContainer().set(NBTKeys.storedArrows, DataType.ITEM_STACK_ARRAY,new ItemStack[1]);
		meta.setItemName(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "Cog Crossbow");
		meta.setRarity(ItemRarity.EPIC);
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.RESET + "" + ChatColor.GRAY + "Hold sneak to load more projectiles");
		meta.setLore(lore);
		crossbow.setItemMeta(meta);
		
		return crossbow;
	}
}
