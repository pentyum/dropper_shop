package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.grinder.Grinder;

public class Advanced_furnace_reaction_runner extends BukkitRunnable {
	private Advanced_furnace advanced_furnace;

	public Advanced_furnace_reaction_runner(Advanced_furnace advanced_furnace) {
		this.advanced_furnace = advanced_furnace;
	}

	public void run() {
		ItemStack reactant_slot = advanced_furnace.get_solid_reactant_slot();
		if (!Grinder.is_empty(reactant_slot)) {
			Solid solid = Solid.get_solid(reactant_slot);
			if (Solid.get_solid(reactant_slot) != null) {
				reactant_slot.setAmount(reactant_slot.getAmount() - 1);
				advanced_furnace.get_reaction_container().set_unit(solid,
						advanced_furnace.get_reaction_container().get_unit(solid) + solid.get_unit());
			}
		}
		Reaction_container reaction_container = this.advanced_furnace.get_reaction_container();
		reaction_container.run_all_reactions();
		ArrayList<String> lore = new ArrayList<String>();
		HashMap<Chemical, Integer> all_chemical = reaction_container.get_all_chemical();
		for (Entry<Chemical, Integer> entry : all_chemical.entrySet()) {
			lore.add("§r" + entry.getKey().get_displayname() + ": " + entry.getValue() + " ("
					+ reaction_container.get_rate(entry.getKey()) * (20 / this.get_cycle()) + "/s)");
		}
		advanced_furnace.set_reactor_info(lore);
		if (advanced_furnace.get_auto_product() == true) {
			Iterator<Entry<Chemical, Integer>> iterator = all_chemical.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Chemical, Integer> entry = iterator.next();
				Chemical chemical = entry.getKey();
				if (chemical instanceof Solid) {
					Solid solid = (Solid) chemical;
					if (reaction_container.get_rate(solid) > 0) {
						advanced_furnace.solid_to_product(solid, iterator);
					}
				}
			}
		}
		if (advanced_furnace.pressed_to_product() == true) {
			Iterator<Entry<Chemical, Integer>> iterator = all_chemical.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Chemical, Integer> entry = iterator.next();
				Chemical chemical = entry.getKey();
				if (chemical instanceof Solid) {
					Solid solid = (Solid) chemical;
					advanced_furnace.solid_to_product(solid, iterator);
				}

			}
			advanced_furnace.unpress_to_product();
		}
	}

	public int get_cycle() {
		return 5;
	}

}
