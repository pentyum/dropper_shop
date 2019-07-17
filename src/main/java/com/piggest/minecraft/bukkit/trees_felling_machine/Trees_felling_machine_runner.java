package com.piggest.minecraft.bukkit.trees_felling_machine;

import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Trees_felling_machine_runner extends Structure_runner {
	private Trees_felling_machine machine = null;

	public Trees_felling_machine_runner(Trees_felling_machine machine) {
		this.machine = machine;
	}

	@Override
	public void run() {
		if (!machine.is_loaded()) {
			return;
		}
		Hopper hopper = machine.get_axe_hopper();
		if (hopper != null) {
			for (ItemStack item : hopper.getInventory().getContents()) {
				if (!Grinder.is_empty(item)) {
					this.machine.add_a_axe(item);
				}
			}
		}
		if (this.machine.is_working()) {
			this.machine.do_next();
		}
	}

	@Override
	public int get_cycle() {
		return 8;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return false;
	}

}
