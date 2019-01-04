package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.Material;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Grinder_runner extends Structure_runner {
	private Grinder grinder;
	public int working_ticks;

	public Grinder_runner(Grinder grinder) {
		this.grinder = grinder;
	}

	public void run() {
		if (grinder.get_location().getChunk().isLoaded() == false) {
			return;
		}
		if (!Grinder.is_empty(grinder.get_flint())) {
			if (grinder.get_flint_storage() <= 1000) {
				Material material = grinder.get_flint().getType();
				int unit = Dropper_shop_plugin.instance.get_unit(material);
				if (unit != 0) {
					grinder.set_flint_storge(grinder.get_flint_storage() + unit);
					grinder.get_flint().setAmount(grinder.get_flint().getAmount() - 1);
				}
			}
		}
		if (Grinder.is_empty(grinder.get_raw())) {
			grinder.set_process(0);
			this.working_ticks = 0;
		} else {
			if (Grinder.recipe.get(grinder.get_raw().getType()) == null || grinder.get_flint_storage() == 0) {
				grinder.set_process(0);
				this.working_ticks = 0;
				return;
			}
			// Dropper_shop_plugin.instance.getLogger().info(Grinder.recipe_time.toString());
			int need_ticks = Grinder.recipe_time.get(grinder.get_raw().getType());
			if (this.working_ticks <= need_ticks) { // 工作中
				grinder.set_process(this.working_ticks * 100 / need_ticks);
				this.working_ticks++;
			} else { // 工作完成
				if (grinder.to_product() == true) {
					grinder.set_process(0);
					this.working_ticks = 0;
					grinder.set_flint_storge(grinder.get_flint_storage() - 1);
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

	@Override
	public boolean is_asynchronously() {
		return true;
	}
}
