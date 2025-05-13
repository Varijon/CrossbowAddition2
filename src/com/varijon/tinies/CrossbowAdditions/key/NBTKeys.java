package com.varijon.tinies.CrossbowAdditions.key;

import org.bukkit.NamespacedKey;

import com.varijon.tinies.CrossbowAdditions.CrossbowAdditions;

public class NBTKeys 
{
	public static NamespacedKey isRepeater;
	public static NamespacedKey isMultiArrow;
	public static NamespacedKey storedArrows;
	public static NamespacedKey isRepeaterArrow;
	
	public static void createNBTKeys(CrossbowAdditions plugin)
	{
		isRepeater = new NamespacedKey(plugin, "isrepeater");
		isMultiArrow = new NamespacedKey(plugin, "ismultiarrow");
		storedArrows = new NamespacedKey(plugin, "storedarrows");
		isRepeaterArrow = new NamespacedKey(plugin, "isrepeaterarrow");
	}
}
