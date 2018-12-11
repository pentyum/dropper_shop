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
	private int money_times = 0;

	public Advanced_furnace_reaction_runner(Advanced_furnace advanced_furnace) {
		this.advanced_furnace = advanced_furnace;
	}

	public void run() {
		ItemStack solid_reactant_slot = advanced_furnace.get_gui_item(advanced_furnace.get_solid_reactant_slot());
		ItemStack gas_reactant_slot = advanced_furnace.get_gui_item(advanced_furnace.get_gas_reactant_slot());
		Reaction_container reaction_container = this.advanced_furnace.get_reaction_container();
		if (!Grinder.is_empty(solid_reactant_slot)) {
			Solid solid = Solid.get_solid(solid_reactant_slot);
			if (solid != null) {
				solid_reactant_slot.setAmount(solid_reactant_slot.getAmount() - 1);
				reaction_container.set_unit(solid, reaction_container.get_unit(solid) + solid.get_unit());
			}
		}

		if (Gas_bottle.is_gas_bottle(gas_reactant_slot)) {
			ItemStack gas_product_slot = advanced_furnace.get_gui_item(advanced_furnace.get_gas_product_slot());
			if (Gas_bottle.calc_capacity(gas_reactant_slot) == 0) {// 处理空瓶
				if (Grinder.is_empty(gas_product_slot)) {
					int inside_gas_capacity = 0;
					HashMap<Chemical, Integer> all_chemical = reaction_container.get_all_chemical();
					ArrayList<Gas> gas_list = new ArrayList<Gas>();
					ArrayList<Integer> need_to_move = new ArrayList<Integer>();
					for (Entry<Chemical, Integer> entry : all_chemical.entrySet()) {
						Chemical chemical = entry.getKey();
						if (chemical instanceof Gas) {
							gas_list.add((Gas) chemical);
							need_to_move.add(entry.getValue());
							inside_gas_capacity += entry.getValue();
						}
					}
					if (inside_gas_capacity > 1000) {
						for (int i = 0; i < gas_list.size(); i++) {
							need_to_move.set(i, need_to_move.get(i) * 1000 / inside_gas_capacity);
						}
					}
					ItemStack new_bottle = Gas_bottle.item.clone();
					for (int i = 0; i < gas_list.size(); i++) {
						Gas gas = gas_list.get(i);
						int move = need_to_move.get(i);
						reaction_container.set_unit(gas, reaction_container.get_unit(gas) - move);
						Gas_bottle.set_contents(new_bottle, gas, move);
					}
					advanced_furnace.set_gas_product_slot(new_bottle);
					gas_reactant_slot.setAmount(gas_reactant_slot.getAmount() - 1);
				}

			} else { // 非空瓶
				boolean flag = false;

				if (Grinder.is_empty(gas_product_slot)) {
					advanced_furnace.set_gas_product_slot(Gas_bottle.item.clone());
					flag = true;
				} else {
					if (gas_product_slot.isSimilar(Gas_bottle.item)) {
						int new_num = gas_product_slot.getAmount() + 1;
						if (new_num <= Gas_bottle.item.getMaxStackSize()) {
							gas_product_slot.setAmount(new_num);
							flag = true;
						}
					}
				}
				if (flag == true) {
					HashMap<Gas, Integer> gas_map = Gas_bottle.get_gas_map(gas_reactant_slot);
					for (Entry<Gas, Integer> entry : gas_map.entrySet()) {
						Chemical chemical = entry.getKey();
						if (chemical instanceof Gas) {
							reaction_container.set_unit(chemical,
									reaction_container.get_unit(chemical) + entry.getValue());
						}
					}
					gas_reactant_slot.setAmount(gas_reactant_slot.getAmount() - 1);
				}
			}
		}

		if (advanced_furnace.get_make_money() == false) {
			reaction_container.run_all_reactions();
		} else {
			if (this.money_times >= 120) {
				advanced_furnace.add_money(advanced_furnace.get_make_money_rate());
				this.money_times = 0;
			} else {
				this.money_times++;
			}
		}

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
		if (advanced_furnace.pressed_clean_solid() == true) {
			Iterator<Entry<Chemical, Integer>> iterator = all_chemical.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Chemical, Integer> entry = iterator.next();
				Chemical chemical = entry.getKey();
				if (chemical instanceof Solid) {
					iterator.remove();
				}
			}
			advanced_furnace.unpress_clean_solid();
		}
		if (advanced_furnace.pressed_clean_gas() == true) {
			Iterator<Entry<Chemical, Integer>> iterator = all_chemical.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Chemical, Integer> entry = iterator.next();
				Chemical chemical = entry.getKey();
				if (chemical instanceof Gas) {
					iterator.remove();
				}
			}
			advanced_furnace.unpress_clean_gas();
		}
	}

	public int get_cycle() {
		return 5;
	}

}
