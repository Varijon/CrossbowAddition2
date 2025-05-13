package com.varijon.tinies.CrossbowAdditions;



import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.varijon.tinies.CrossbowAdditions.command.RepeaterCommand;
import com.varijon.tinies.CrossbowAdditions.key.NBTKeys;
import com.varijon.tinies.CrossbowAdditions.listener.CrossbowAdditionsListener;
import com.varijon.tinies.CrossbowAdditions.storage.ProjectileStorage;
import com.varijon.tinies.CrossbowAdditions.task.CrossbowAdditionsTask;


public class CrossbowAdditions extends JavaPlugin
{
	@Override
	public void onEnable() 
	{
//		ProjectileStorage.initProjectileStorage();
		NBTKeys.createNBTKeys(this);
		this.getCommand("giverepeater").setExecutor(new RepeaterCommand(this));
		Bukkit.getPluginManager().registerEvents(new CrossbowAdditionsListener(this), this);
		new CrossbowAdditionsTask(this).runTaskTimer(this, 0, 1);
	}
}