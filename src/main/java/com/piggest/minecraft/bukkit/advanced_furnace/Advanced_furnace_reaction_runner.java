package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.scheduler.BukkitRunnable;

public class Advanced_furnace_reaction_runner extends BukkitRunnable {
	private Advanced_furnace advanced_furnace;

	public Advanced_furnace_reaction_runner(Advanced_furnace advanced_furnace) {
		this.advanced_furnace = advanced_furnace;
	}

	public void run() {
		Reaction_container reaction_container = this.advanced_furnace.get_reaction_container();
		reaction_container.run_all_reactions();
		ArrayList<String> lore = new ArrayList<String>();
		HashMap<Chemical, Integer> all_chemical = reaction_container.get_all_chemical();
		for (Entry<Chemical, Integer> entry : all_chemical.entrySet()) {
			lore.add("§r" + entry.getKey().get_displayname() + ": " + entry.getValue() + " ("
					+ reaction_container.get_rate(entry.getKey()) * 2 + "/s)");
		}
		advanced_furnace.set_reactor_info(lore);
		for (Entry<Chemical, Integer> entry : all_chemical.entrySet()) {
			Chemical chemical = entry.getKey();
			if (chemical instanceof Solid) {
				Solid solid = (Solid) chemical;
				if (reaction_container.get_rate(solid) > 0) {
					advanced_furnace.solid_to_product(solid);
				}
			}
		}
	}

}
