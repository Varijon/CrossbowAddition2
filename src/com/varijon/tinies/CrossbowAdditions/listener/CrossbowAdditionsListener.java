package com.varijon.tinies.CrossbowAdditions.listener;

import java.util.Collection;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.damage.DamageSource;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Piglin;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.entity.Trident;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.loot.LootContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import com.varijon.tinies.CrossbowAdditions.CrossbowAdditions;
import com.varijon.tinies.CrossbowAdditions.key.NBTKeys;
import com.varijon.tinies.CrossbowAdditions.loot.PiglinLoot;
import com.varijon.tinies.CrossbowAdditions.util.Util;

public class CrossbowAdditionsListener implements Listener
{
	CrossbowAdditions plugin;
	
	public CrossbowAdditionsListener(CrossbowAdditions plugin) 
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPiglinKill(EntityDeathEvent event)
	{
		if(event.getDamageSource().getCausingEntity() == null)
		{
			return;
		}
		if (!(event.getDamageSource().getCausingEntity() instanceof Player)) 
		{
            return;
        }
		if (event.getEntity().getType().equals(EntityType.PIGLIN)) 
		{ 
			Piglin piglin = (Piglin) event.getEntity();
			if(piglin.getEquipment().getItemInMainHand() == null)
			{
				return;
			}
			if(piglin.getEquipment().getItemInMainHand().getType() != Material.CROSSBOW)
			{
				return;
			}
			Ageable agePiglin = piglin;
			if(!agePiglin.isAdult())
			{
				return;
			}
			
            final Player player = (Player) event.getDamageSource().getCausingEntity(); // We can safely cast the last damage cause to a player because we already made an instanceof check.

            LootContext context =
                    new LootContext.Builder(event.getEntity().getLocation()) // We have to pass the location of the entity otherwise we don't know where to drop the loot.
                            .killer(player) // Pass the player so they get kill credit.
                            .lootedEntity(event.getEntity()) // The entity that we are looting.
                            .lootingModifier(player.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING)) // We get the level of Looting on the player's held item.
                            .build(); // Build the LootContext so it's usable.
            final Collection<ItemStack> extraItems = new PiglinLoot().populateLoot(new Random(), context); // We now call our class that extends LootTable so we can get our loot for us
            // All of the logic that is inside of our populateLoot method is ran and we now have our list of extra items.
            
            event.getDrops().addAll(extraItems); // We now add the contents of our drop list to the drops of the entity.
            // Should you only want to drop items from your LootTable you can clear the drop list and then add your list to the entities drop with the same method above.
            // With how it's currently setup it should drop the zombies normal loot alongside our loot.
            // Here's an example of what I'm talking about.
            /*
           
            event.getDrops().clear(); // Clears the list of all entries.
            event.getDrops().addAll(extraItems); // Add our items to the drop list.
             */
        }
	}

//	@EventHandler
//	public void onCrossbowLoad(EntityLoadCrossbowEvent ev) 
//	{
//		if(ev.getEntity() instanceof Player)
//		{
//			Player player = (Player) ev.getEntity();
//			ItemStack crossbow = null;
//			
//			boolean offhand = false;
//			boolean mainhand = false;
//			
//			if(player.getInventory().getItemInOffHand().getType() == Material.CROSSBOW)
//			{
//				offhand = true;
//			}
//			if(player.getInventory().getItemInMainHand().getType() == Material.CROSSBOW)
//			{
//				mainhand = true;
//			}
//			if(mainhand)
//			{
//				crossbow = checkForRepeater(player.getInventory().getItemInMainHand());				
//			}
//			else
//			{
//				crossbow = checkForRepeater(player.getInventory().getItemInOffHand());
//			}
//			
//			if(crossbow != null)
//			{
//				CrossbowMeta crossbowMeta = (CrossbowMeta) crossbow.getItemMeta();
//				if(crossbowMeta.hasChargedProjectiles())
//				{
//					ItemStack arrow = crossbowMeta.getChargedProjectiles().get(0);
//					PersistentDataContainer container = crossbowMeta.getPersistentDataContainer();
//					ArrayList<ItemStack> loadedArrows = new ArrayList<ItemStack>(Arrays.asList(container.get(NBTKeys.storedArrows, DataType.ITEM_STACK_ARRAY)));
//					loadedArrows.add(arrow);
//					container.set(NBTKeys.storedArrows, DataType.ITEM_STACK_ARRAY, loadedArrows.toArray(new ItemStack[loadedArrows.size()]));
//					crossbow.setItemMeta(crossbowMeta);
//				}
//
//				NBTTagCompound nbt = crossbow.getTag();
//				if(nbt != null)
//				{
//					runNBTLoadAddLater(crossbow, bukkitPlayer, offhand, mainhand);			
//
//					if(nbt.getInt("Charged") == 1)
//					{
//						NBTList<NBTBase> projectiles = nbt.getList("ChargedProjectiles", 0);
//						NBTTagCompound arrow = new NBTTagCompound();
//						arrow.setString("id", "minecraft:arrow");
//						arrow.setInt("Count", 1);
//						projectiles.add(arrow);
//						nbt.set("ChargedProjectiles", projectiles);
//						nbt.setInt("arrowClip", 7);	
//						nbt.setInt("Charged", 1);	
//						crossbow.setTag(nbt);		
//						
//						if(offhand && !mainhand)
//						{
//							bukkitPlayer.getInventory().setItemInOffHand(CraftItemStack.asCraftMirror(crossbow));
//						}
//						if(offhand && mainhand)
//						{
//							bukkitPlayer.getInventory().setItemInMainHand(CraftItemStack.asCraftMirror(crossbow));
//						}
//						if(!offhand && mainhand)
//						{
//							bukkitPlayer.getInventory().setItemInMainHand(CraftItemStack.asCraftMirror(crossbow));
//						}
//					}
//				}
//				//if not working, using copy
//			}
//		}
//		
//	}
	@EventHandler
	public void onPlayerCrossbowFire(EntityShootBowEvent ev) 
	{
		if(!(ev.getEntity() instanceof Player))
		{
			return;
		}
		ItemStack repeater = Util.checkForRepeater(ev.getBow());
		if(repeater == null)
		{
			return;
		}
		Entity projectile = ev.getProjectile();
		if(projectile instanceof Arrow)
		{
			Arrow arrow = (Arrow) projectile;
			if(arrow.getItem().hasItemMeta())
			{
				ItemMeta meta = arrow.getItem().getItemMeta();
				if(meta.getPersistentDataContainer().has(NBTKeys.isMultiArrow))
				{
					arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);					
				}				
			}
		}
		if(projectile instanceof SpectralArrow)
		{
			SpectralArrow arrow = (SpectralArrow) projectile;
			if(arrow.getItem().hasItemMeta())
			{
				ItemMeta meta = arrow.getItem().getItemMeta();
				if(meta.getPersistentDataContainer().has(NBTKeys.isMultiArrow))
				{
					arrow.setPickupStatus(PickupStatus.CREATIVE_ONLY);					
				}				
			}
		}
		projectile.getPersistentDataContainer().set(NBTKeys.isRepeaterArrow, PersistentDataType.INTEGER, 1);
	}
	
	@EventHandler
	public void onArrowDamage(EntityDamageByEntityEvent ev) 
	{
		if(!(ev.getDamager() instanceof Arrow) && !(ev.getDamager() instanceof SpectralArrow))
		{
			return;
		}
		if(ev.getDamager() instanceof Arrow)
		{
			Arrow arrow = (Arrow) ev.getDamager();
			if(arrow.getBasePotionType() == PotionType.HARMING || arrow.getBasePotionType() == PotionType.STRONG_HARMING || arrow.getBasePotionType() == PotionType.HEALING || arrow.getBasePotionType() == PotionType.STRONG_HEALING)
			{
				return;
			}
		}
		if(!(ev.getEntity() instanceof LivingEntity))
		{
			return;
		}
		Projectile projectile = (Projectile) ev.getDamager();
		if(!(projectile.getShooter() instanceof Player))
		{
			return;
		}
		if(projectile.getPersistentDataContainer().has(NBTKeys.isRepeaterArrow))
		{
			LivingEntity entity = (LivingEntity) ev.getEntity();
			runSetNoDamageTicksLater(entity);
		}
	}
	
	void runSetNoDamageTicksLater(LivingEntity entity)
	{
		Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
	    {
			@Override
			public void run() 
			{
				entity.setNoDamageTicks(4);
			}
	    },1L);
	}
	
//	@EventHandler
//	public void onPlayerCrossbowFire(ProjectileLaunchEvent ev) 
//	{
//		if(!(ev.getEntity().getShooter() instanceof Player))
//		{
//			return;
//		}
//		Player bukkitPlayer = (Player) ev.getEntity().getShooter();
//		
//
//		EntityProjectile projectile = (EntityProjectile) ((CraftProjectile) ev.getEntity()).getHandle();
//		NBTTagCompound nbtProj = new NBTTagCompound();
//		projectile.save(nbtProj);
//		
//		if(nbtProj.hasKey("multishot"))
//		{
//			//tipped arrow cast error
//			System.out.println("skipped");
//			return;
//		}
//		
//		
//		System.out.println("pew");
//		
//		boolean offhand = false;
//		boolean mainhand = false;
//		
//		if(bukkitPlayer.getInventory().getItemInOffHand().getType() == Material.CROSSBOW)
//		{
//			offhand = true;
//		}
//		if(bukkitPlayer.getInventory().getItemInMainHand().getType() == Material.CROSSBOW)
//		{
//			mainhand = true;
//		}
//		
//		if(mainhand || offhand)
//		{
//			ItemStack heldCrossbow = null;
//			EntityPlayer targetPlayer = (EntityPlayer) ((CraftPlayer) bukkitPlayer).getHandle();
//			
//			if(mainhand)
//			{
//				heldCrossbow = checkForRepeater(targetPlayer.getItemInMainHand());				
//			}
//			else
//			{
//				heldCrossbow = checkForRepeater(targetPlayer.getItemInOffHand());
//			}
//			
//
//			ItemStack crossbow = checkForRepeater(heldCrossbow);
//			
//			if(crossbow != null)
//			{
//				NBTTagCompound nbt = crossbow.getTag();
//				if(nbt != null)
//				{
//					runNBTFiringAddLater(crossbow, bukkitPlayer, offhand, mainhand);			
//				}
//			}
//		}
//	}
//	
//	void runNBTLoadAddLater(ItemStack crossbow, Player player, boolean offhand, boolean mainhand)
//	{
//		Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
//	    {
//			@Override
//			public void run() 
//			{
//				NBTTagCompound nbt = crossbow.getTag();
//				NBTBase projectiles = nbt.get("ChargedProjectiles");
//				NBTTagList storedProjectiles = null;
//				if(!nbt.hasKey("StoredProjectiles"))
//				{
//					storedProjectiles = new NBTTagList();
//				}
//				else
//				{
//					storedProjectiles = (NBTTagList) nbt.get("StoredProjectiles");
//				}
//				
//				if(nbt.hasKey("Enchantments"))
//				{
//					NBTTagList enchants = (NBTTagList) nbt.get("Enchantments");
//					for(NBTBase enchant : enchants)
//					{
//						NBTTagCompound enchantTag = (NBTTagCompound) enchant;
//						if(enchantTag.getString("id").contains("multishot"))
//						{
//							NBTTagList projList = (NBTTagList) projectiles;
//							((NBTTagCompound) projList.get(0)).setInt("multishot", 1);
//							((NBTTagCompound) projList.get(1)).setInt("multishot", 1);
//						}
//					}
//				}
//				
//				storedProjectiles.add(projectiles);
//				
//				nbt.set("StoredProjectiles", storedProjectiles);
//				crossbow.setTag(nbt);
//
////				if(offhand && !mainhand)
////				{
////					player.getInventory().setItemInOffHand(CraftItemStack.asBukkitCopy(crossbow));
////				}
////				if(offhand && mainhand)
////				{
////					player.getInventory().setItemInMainHand(CraftItemStack.asBukkitCopy(crossbow));
////				}
////				if(!offhand && mainhand)
////				{
////					player.getInventory().setItemInMainHand(CraftItemStack.asBukkitCopy(crossbow));
////				}
//			}
//
//	    },1L);
//	}
//	void runNBTFiringAddLater(ItemStack crossbow, Player player, boolean offhand, boolean mainhand)
//	{
//		Bukkit.getScheduler().runTaskLater(plugin, new Runnable()
//	    {
//			@Override
//			public void run() 
//			{
//				NBTTagCompound nbt = crossbow.getTag();
//				if(nbt.hasKey("StoredProjectiles"))
//				{
//					NBTTagList storedProjectiles = (NBTTagList) nbt.get("StoredProjectiles");
//					if(storedProjectiles.size() > 0)
//					{
//						nbt.set("ChargedProjectiles",storedProjectiles.get(0));
//						nbt.setInt("Charged", 1);	
//						storedProjectiles.remove(0);
//						nbt.set("StoredProjectiles",storedProjectiles);
//						crossbow.setTag(nbt);
//					}
//				}
//			}
//			
//
//	    },1L);
//	}

}
