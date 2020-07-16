package com.piggest.minecraft.bukkit.advanced_furnace;

import com.piggest.minecraft.bukkit.depository.Upgrade_component;
import com.piggest.minecraft.bukkit.depository.Upgrade_component_type;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import org.bukkit.inventory.ItemStack;

public class Advanced_furnace_upgrade_runner extends Structure_runner {
	public Advanced_furnace_upgrade_runner(Advanced_furnace_manager manager) {
		super(manager);
	}

	@Override
	public void run_instance(Structure structure) {
		Advanced_furnace adv_furnace = (Advanced_furnace) structure;

		ItemStack upgrade_components = adv_furnace.get_gui_item(33);
		Upgrade_component_type type = Upgrade_component.get_type(upgrade_components);
		if (type == Upgrade_component_type.overload_upgrade || type == Upgrade_component_type.time_upgrade) {
			if (Upgrade_component.get_process(upgrade_components) == 100) {
				if (type == Upgrade_component_type.overload_upgrade) {
					adv_furnace.set_overload_upgrade(adv_furnace.get_overload_upgrade() + 1);
				} else {
					adv_furnace.set_time_upgrade(adv_furnace.get_time_upgrade() + 1);
				}
				upgrade_components.setAmount(0);
				return;
			}
			if (Upgrade_component.get_process(upgrade_components) < 100) {
				Upgrade_component.set_process(upgrade_components,
						Upgrade_component.get_process(upgrade_components) + 1);
			}
		}
	}

	@Override
	public int get_cycle() {
		return 120;
	}

	@Override
	public int get_delay() {
		return 60;
	}

}
