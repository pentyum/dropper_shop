package com.piggest.minecraft.bukkit.compressor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Compressor_runner extends Structure_runner {
	private Compressor compressor = null;
	public int working_ticks;

	public Compressor_runner(Compressor compressor) {
		this.compressor = compressor;
	}

	@Override
	public void run() {
		if (compressor.is_loaded() == false) {
			return;
		}
		if (!Grinder.is_empty(compressor.get_piston())) {
			if (compressor.get_piston_storage() <= 1000) {
				ItemStack piston_slot = compressor.get_piston();
				Material material = piston_slot.getType();
				int unit = Dropper_shop_plugin.instance.get_unit(material);
				if (unit != 0) {
					compressor.set_piston_storage(compressor.get_piston_storage() + unit);
					piston_slot.setAmount(piston_slot.getAmount() - 1);
				}
			}
		}
		if (Grinder.is_empty(compressor.get_raw())) {
			compressor.set_process(0);
			this.working_ticks = 0;
		} else {
			if (compressor.get_manager().get_product(Material_ext.get_full_name(compressor.get_raw()),
					compressor.get_raw().getAmount()) == null || compressor.get_piston_storage() == 0) {
				compressor.set_process(0);
				this.working_ticks = 0;
				return;
			}
			// Dropper_shop_plugin.instance.getLogger().info(Grinder.recipe_time.toString());
			int need_ticks = compressor.get_manager().get_time(Material_ext.get_full_name(compressor.get_raw()));
			if (this.working_ticks <= need_ticks) { // 工作中
				compressor.set_process(this.working_ticks * 100 / need_ticks);
				this.working_ticks++;
			} else { // 工作完成
				if (compressor.to_product() == true) {
					compressor.set_process(0);
					this.working_ticks = 0;
					compressor.set_piston_storage(compressor.get_piston_storage() - 1);
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
