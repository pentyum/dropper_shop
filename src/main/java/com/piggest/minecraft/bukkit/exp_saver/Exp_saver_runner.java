package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.depository.Upgrade_component;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Exp_saver_runner extends Structure_runner {
	Exp_saver exp_saver = null;

	public Exp_saver_runner(Exp_saver exp_saver) {
		this.exp_saver = exp_saver;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (this.exp_saver.is_loaded() == false) {
			return;
		}
		ItemStack mending = exp_saver.get_mending();
		if (!Grinder.is_empty(mending)) {
			if (!Upgrade_component.is_component(mending)) {
				if (mending.getDurability() > 0) {
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
