package com.piggest.minecraft.bukkit.grinder;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import org.bukkit.Material;

public class Grinder_runner extends Structure_runner {
	public Grinder_runner(Grinder_manager manager) {
		super(manager);
	}

	public void run_instance(Structure structure) {
		Grinder grinder = (Grinder) structure;
		if (grinder.is_loaded() == false) {
			return;
		}
		if (!Grinder.is_empty(grinder.get_flint())) {
			if (grinder.get_flint_storage() <= 1000) {
				Material material = grinder.get_flint().getType();
				int unit = Dropper_shop_plugin.instance.get_flint_unit(material);
				if (unit != 0) {
					grinder.set_flint_storage(grinder.get_flint_storage() + unit);
					grinder.get_flint().setAmount(grinder.get_flint().getAmount() - 1);
				}
			}
		}
		if (Grinder.is_empty(grinder.get_raw())) {
			grinder.set_process(0);
			grinder.working_ticks = 0;
		} else {
			if (grinder.get_manager().get_main_product(grinder.get_raw().getType()) == null || grinder.get_flint_storage() == 0) {
				grinder.set_process(0);
				grinder.working_ticks = 0;
				return;
			}
			// Dropper_shop_plugin.instance.getLogger().info(Grinder.recipe_time.toString());
			int need_ticks = grinder.get_manager().get_time(grinder.get_raw().getType());
			if (grinder.working_ticks <= need_ticks) { // 工作中
				grinder.set_process(grinder.working_ticks * 100 / need_ticks);
				grinder.working_ticks++;
			} else { // 工作完成
				if (grinder.to_product() == true) {
					grinder.set_process(0);
					grinder.working_ticks = 0;
					grinder.set_flint_storage(grinder.get_flint_storage() - 1);
				}
			}
		}
	}

	@Override
	public int get_cycle() {
		return 1;
	}

	@Override
	public int get_delay() {
		return 10;
	}

}
