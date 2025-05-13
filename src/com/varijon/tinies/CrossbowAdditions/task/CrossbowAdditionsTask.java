package com.varijon.tinies.CrossbowAdditions.task;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.varijon.tinies.CrossbowAdditions.CrossbowAdditions;
import com.varijon.tinies.CrossbowAdditions.key.NBTKeys;
import com.varijon.tinies.CrossbowAdditions.storage.ProjectileStorage;
import com.varijon.tinies.CrossbowAdditions.util.Util;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;



public class CrossbowAdditionsTask extends BukkitRunnable
{
	CrossbowAdditions plugin;
	
	public CrossbowAdditionsTask(CrossbowAdditions plugin) 
	{
		this.plugin = plugin; 
	}
	

	@Override
	public void run() 
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			ItemStack offhandCrossbow = Util.checkForRepeater(player.getInventory().getItemInOffHand());
			ItemStack mainhandCrossbow = Util.checkForRepeater(player.getInventory().getItemInMainHand());

			if(mainhandCrossbow != null || offhandCrossbow != null)
			{
//				StringBuilder sb = new StringBuilder();
//				if(offhandCrossbow != null)
//				{
//					CrossbowMeta offhandMeta = (CrossbowMeta) offhandCrossbow.getItemMeta();
//					int arrowCount = ProjectileStorage.getArrowCount(offhandMeta) + (offhandMeta.hasChargedProjectiles() ? 1 : 0);
//					if(arrowCount == 10)
//					{
//						sb.append("" + ChatColor.RED);						
//					}
//					sb.append(arrowCount);
//					sb.append("" + ChatColor.WHITE);	
//					if(mainhandCrossbow != null)
//					{
//						sb.append(" - ");
//					}
//				}
//				if(mainhandCrossbow != null)
//				{
//					CrossbowMeta mainhandMeta = (CrossbowMeta) mainhandCrossbow.getItemMeta();
//					int arrowCount = ProjectileStorage.getArrowCount(mainhandMeta) + (mainhandMeta.hasChargedProjectiles() ? 1 : 0);
//					if(arrowCount == 10)
//					{
//						sb.append("" + ChatColor.RED);						
//					}
//					sb.append(arrowCount);			
//				}
//				player.sendTitle("", sb.toString(), 0, 5, 0);
				
				if(player.isSneaking())
				{
					if(offhandCrossbow != null && mainhandCrossbow == null)
					{
						CrossbowMeta crossbowMeta = (CrossbowMeta) offhandCrossbow.getItemMeta();
						int arrowCount = ProjectileStorage.getArrowCount(crossbowMeta);
						if(arrowCount > 8)
						{
							continue;
						}
//						ProjectileStorage.appendProjectileList(crossbowMeta.getChargedProjectiles(),player.getUniqueId());

//						crossbowMeta.setChargedProjectiles(new ArrayList<ItemStack>());
						if(crossbowMeta.hasChargedProjectiles())
						{
							offhandCrossbow.setItemMeta(ProjectileStorage.moveArrowsToStorage(crossbowMeta));
							player.getWorld().playSound(player, Sound.BLOCK_LEVER_CLICK, 1f, 0.5f + (arrowCount * 0.1f));
						}
					}
					if(mainhandCrossbow != null)
					{
						CrossbowMeta crossbowMeta = (CrossbowMeta) mainhandCrossbow.getItemMeta();
//						ProjectileStorage.appendProjectileList(crossbowMeta.getChargedProjectiles(),player.getUniqueId());
						int arrowCount = ProjectileStorage.getArrowCount(crossbowMeta);
						if(arrowCount > 8)
						{
							continue;
						}

//						crossbowMeta.setChargedProjectiles(new ArrayList<ItemStack>());
						if(crossbowMeta.hasChargedProjectiles())
						{
							mainhandCrossbow.setItemMeta(ProjectileStorage.moveArrowsToStorage(crossbowMeta));	
							player.getWorld().playSound(player, Sound.BLOCK_LEVER_CLICK, 1f, 0.5f + (arrowCount * 0.1f));
						}
					}
				}
				else
				{
					if(offhandCrossbow != null && mainhandCrossbow == null)
					{
						CrossbowMeta crossbowMeta = (CrossbowMeta) offhandCrossbow.getItemMeta();
						if(!crossbowMeta.hasChargedProjectiles())
						{
							int arrowCount = ProjectileStorage.getArrowCount(crossbowMeta);
							if(arrowCount > 0)
							{
								offhandCrossbow.setItemMeta(ProjectileStorage.setArrowsFromStorage(crossbowMeta));	
								player.getWorld().playSound(player, Sound.BLOCK_DISPENSER_DISPENSE, 1.2f, 1.5f - (arrowCount * 0.1f));	
							}								
						}
					}
					if(mainhandCrossbow != null)
					{

						CrossbowMeta crossbowMeta = (CrossbowMeta) mainhandCrossbow.getItemMeta();
						if(!crossbowMeta.hasChargedProjectiles())
						{
							int arrowCount = ProjectileStorage.getArrowCount(crossbowMeta);
							if(arrowCount > 0)
							{
								mainhandCrossbow.setItemMeta(ProjectileStorage.setArrowsFromStorage(crossbowMeta));		
								player.getWorld().playSound(player, Sound.BLOCK_DISPENSER_DISPENSE, 1.2f, 1.5f - (arrowCount * 0.1f));	
							}
						}
					}
				}
			}
		}
	}
}
