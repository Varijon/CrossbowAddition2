package com.varijon.tinies.CrossbowAdditions.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.jeff_media.morepersistentdatatypes.DataType;
import com.varijon.tinies.CrossbowAdditions.key.NBTKeys;

import net.md_5.bungee.api.chat.hover.content.Item;

public class ProjectileStorage 
{

//	static HashMap<UUID, List<ItemStack>> projectileStorage;
//	
//	public static void initProjectileStorage()
//	{
//		projectileStorage = new HashMap<UUID, List<ItemStack>>();
//	}
//
//	public static HashMap<UUID, List<ItemStack>> getProjectileStorage() {
//		return projectileStorage;
//	}
//
//	public static void setProjectileStorage(HashMap<UUID, List<ItemStack>> projectileStorage) {
//		ProjectileStorage.projectileStorage = projectileStorage;
//	}
//	
//	public static List<ItemStack> getProjectileList(UUID uuid)
//	{
//		return projectileStorage.get(uuid);
//	}
//	
//	public static void appendProjectileList(List<ItemStack> projectileList, UUID uuid)
//	{
//		List<ItemStack> completeProjectileList;
//		completeProjectileList = getProjectileList(uuid);
//		if(completeProjectileList == null)
//		{
//			projectileStorage.put(uuid, projectileList);
//		}
//		else
//		{
//			ArrayList<ItemStack> completeProjectileArrayList = new ArrayList<>(completeProjectileList);
//			completeProjectileArrayList.addAll(projectileList);
//			projectileStorage.put(uuid, completeProjectileArrayList);			
//		}
//	}
	
	public static int getArrowCount(CrossbowMeta crossbowMeta)
	{
		PersistentDataContainer container = crossbowMeta.getPersistentDataContainer();
		ArrayList<ItemStack> arrowList = container.get(NBTKeys.storedArrows, DataType.asArrayList(DataType.ITEM_STACK));
		if(arrowList == null)
		{
			return 0;
		}
		return arrowList.size();
	}
	
	public static CrossbowMeta moveArrowsToStorage(CrossbowMeta crossbowMeta)
	{
		PersistentDataContainer container = crossbowMeta.getPersistentDataContainer();
//		if(!container.has(NBTKeys.storedArrows))
//		{
//			container.set(NBTKeys.storedArrows, DataType.ITEM_STACK_ARRAY, new ItemStack[1]);
//		}
		ArrayList<ItemStack> arrowList = container.get(NBTKeys.storedArrows, DataType.asArrayList(DataType.ITEM_STACK));
		if(arrowList == null)
		{
			arrowList = new ArrayList<ItemStack>();
		}
		arrowList.add(convertListToItemStack(crossbowMeta.getChargedProjectiles()));
		crossbowMeta.setChargedProjectiles(new ArrayList<ItemStack>());
		container.set(NBTKeys.storedArrows, DataType.asArrayList(DataType.ITEM_STACK), arrowList);
		return crossbowMeta;
	}
	
	public static CrossbowMeta setArrowsFromStorage(CrossbowMeta crossbowMeta)
	{
		PersistentDataContainer container = crossbowMeta.getPersistentDataContainer();
		if(!container.has(NBTKeys.storedArrows))
		{
			return crossbowMeta;
		}
		ArrayList<ItemStack> arrowList = container.get(NBTKeys.storedArrows, DataType.asArrayList(DataType.ITEM_STACK));
		if(arrowList == null)
		{
			return crossbowMeta;
		}
		crossbowMeta.setChargedProjectiles(convertItemStackToList(arrowList.get(0)));
		arrowList.remove(0);
		container.set(NBTKeys.storedArrows, DataType.asArrayList(DataType.ITEM_STACK), arrowList);
		return crossbowMeta;
	}
	
	public static ItemStack convertListToItemStack(List<ItemStack> arrowList)
	{
		ItemStack mainItem = arrowList.get(0);
		mainItem.setAmount(arrowList.size());
		return mainItem;
	}	
	
	public static ArrayList<ItemStack> convertItemStackToList(ItemStack arrows)
	{
		ArrayList<ItemStack> arrowList = new ArrayList<ItemStack>();
		ItemStack singleArrow = arrows.clone();
		singleArrow.setAmount(1);
		for(int x = 0; x < arrows.getAmount(); x++)
		{
			arrowList.add(singleArrow.clone());
		}
		if(arrowList.size() == 3)
		{
			ItemMeta meta = arrowList.get(1).getItemMeta();
			meta.getPersistentDataContainer().set(NBTKeys.isMultiArrow, PersistentDataType.INTEGER, 1);
			arrowList.get(1).setItemMeta(meta);
			
			ItemMeta meta2 = arrowList.get(2).getItemMeta();
			meta2.getPersistentDataContainer().set(NBTKeys.isMultiArrow, PersistentDataType.INTEGER, 1);
			arrowList.get(2).setItemMeta(meta2);
		}
		return arrowList;
	}
}
