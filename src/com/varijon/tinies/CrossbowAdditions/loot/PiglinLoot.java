package com.varijon.tinies.CrossbowAdditions.loot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.loot.LootContext;
import org.bukkit.loot.LootTable;
import org.bukkit.plugin.java.JavaPlugin;

import com.varijon.tinies.CrossbowAdditions.CrossbowAdditions;
import com.varijon.tinies.CrossbowAdditions.util.Util;

public class PiglinLoot implements LootTable {
    @Override
    public Collection<ItemStack> populateLoot(Random random, LootContext lootContext) {
        int loot = lootContext.getLootingModifier(); // You'll need to create your own LootContext for this.
        double lootingMod = loot * .01; // This will add an extra 1% chance on the drop for each level of looting (for a maximum of 3%)

        final List<ItemStack> items = new ArrayList<>();
        double chance = random.nextDouble();
        if (chance <= (.02 + lootingMod)) {
            // We get the next double from the Random instance passed to the method.
            // If you only want on roll on all of the items you make a variable out of the random.nextDouble() to keep the double consistent throughout the method.
            // If the double is less than or exactly equal the default chance (5%) plus the looting modifier we add the drops to the items list.
        	ItemStack droppedItem = Util.createRepeater();
        	Damageable itemMeta = (Damageable) droppedItem.getItemMeta();
        	itemMeta.setDamage(random.nextInt(20, (int) droppedItem.getType().getMaxDurability()));
        	droppedItem.setItemMeta(itemMeta);
            items.add(droppedItem);
        }

        return items; // Return the list of items that we want to add.
    }

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JavaPlugin.getProvidingPlugin(CrossbowAdditions.class), "extra_piglin_drop");
    }

	@Override
	public void fillInventory(Inventory inventory, Random random, LootContext context) {
		// TODO Auto-generated method stub
		
	}
}