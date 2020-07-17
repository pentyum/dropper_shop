package com.piggest.minecraft.bukkit.exp_saver;

import com.piggest.minecraft.bukkit.depository.Upgrade_component;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Custom_durability;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Old_structure_runner;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Exp_saver_runner extends Old_structure_runner {
	public Exp_saver_runner(Exp_saver_manager manager) {
		super(manager);
	}

	@Override
	public void run_instance(Structure structure) {
		Exp_saver exp_saver = (Exp_saver) structure;
		if (exp_saver.is_loaded() == false) {
			return;
		}
		ItemStack mending = exp_saver.get_mending();
		if (!Grinder.is_empty(mending)) {
			if (!Upgrade_component.is_component(mending)) {
				if (Custom_durability.get_durability(mending) > 0) {
					exp_saver.repair_mending_slot();
				} else {
					if (mending.getType() == Material.ANVIL) {
						if (exp_saver.get_anvil_count() < Dropper_shop_plugin.instance
								.get_exp_saver_anvil_upgrade_need()) {
							mending.setAmount(mending.getAmount() - 1);
							exp_saver.set_anvil_count(exp_saver.get_anvil_count() + 1,
									exp_saver.get_chipped_anvil_count(), exp_saver.get_damaged_anvil_count());
						}
					} else if (mending.getType() == Material.CHIPPED_ANVIL) {
						if (exp_saver.get_chipped_anvil_count() < Dropper_shop_plugin.instance
								.get_exp_saver_anvil_upgrade_need()) {
							mending.setAmount(mending.getAmount() - 1);
							exp_saver.set_anvil_count(exp_saver.get_anvil_count(),
									exp_saver.get_chipped_anvil_count() + 1, exp_saver.get_damaged_anvil_count());
						}
					} else if (mending.getType() == Material.DAMAGED_ANVIL) {
						if (exp_saver.get_damaged_anvil_count() < Dropper_shop_plugin.instance
								.get_exp_saver_anvil_upgrade_need()) {
							mending.setAmount(mending.getAmount() - 1);
							exp_saver.set_anvil_count(exp_saver.get_anvil_count(), exp_saver.get_chipped_anvil_count(),
									exp_saver.get_damaged_anvil_count() + 1);
						}
					}
				}
			}
		}
	}

	@Override
	public int get_cycle() {
		return 10;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}
}
