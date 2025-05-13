package com.varijon.tinies.CrossbowAdditions.command;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.varijon.tinies.CrossbowAdditions.CrossbowAdditions;
import com.varijon.tinies.CrossbowAdditions.util.Util;

import net.md_5.bungee.api.ChatColor;

public class RepeaterCommand implements CommandExecutor
{
	CrossbowAdditions plugin;
	public RepeaterCommand(CrossbowAdditions plugin) 
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command var2, String label, String[] args) 
	{
		if(args.length == 0)
		{
			showCommandOptions(sender);
			return true;
		}
		if(args.length > 0)
		{	
			Player target = Bukkit.getPlayer(args[0]);
			HashMap<Integer, ItemStack> excess = target.getInventory().addItem(Util.createRepeater());
			if(excess.size() > 0)
			{
				target.getWorld().dropItem(target.getLocation(), excess.get(0));				
			}
			return true;
		}
		showCommandOptions(sender);
		
		return true;
	}

	void showCommandOptions(CommandSender sender)
	{
		sender.sendMessage(ChatColor.RED + "Usage: /giverepeater <playername>");
	}


}
