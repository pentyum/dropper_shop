package com.piggest.minecraft.bukkit.depository;

import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Depository_item_importer extends Structure_runner {
	public Depository_item_importer(Depository_manager manager) {
		super(manager);
	}

	public void run_instance(Structure structure) {
		Depository depository = (Depository) structure;

		if (depository.is_loaded() == false) {
			return;
		}
		if (depository.is_accessible() == true) {
			int check_list[][] = {{0, 2, 0}, {0, -1, 2}, {2, -1, 0}, {0, -1, -2}, {-2, -1, 0}};
			for (int[] relative_coord : check_list) {
				BlockState block = depository.get_block(relative_coord[0], relative_coord[1], relative_coord[2])
						.getState();
				if (block instanceof Hopper) {
					org.bukkit.block.data.type.Hopper hopper_data = (org.bukkit.block.data.type.Hopper) block
							.getBlockData();
					Vector vec = hopper_data.getFacing().getDirection().multiply(2)
							.add(new Vector(relative_coord[0], relative_coord[1], relative_coord[2]));
					if (vec.getBlockX() == 0 && vec.getBlockZ() == 0) {
						Hopper hopper = (Hopper) block;
						if (hopper.getBlock().isBlockPowered()) {
							continue;
						}
						for (ItemStack item : hopper.getInventory().getContents()) {
							if (item != null && item.getType() != Material.AIR) {
								depository.add(item);
							}
						}
					}
				}
			}
		}
		if (depository.working_ticks == 15) {
			ItemStack components[] = depository.getInventory().getContents();
			for (ItemStack component : components) {
				if (Upgrade_component.get_type(component) == Upgrade_component_type.depository_upgrade) {
					if (Upgrade_component.get_process(component) < 100) {
						Upgrade_component.set_process(component, Upgrade_component.get_process(component) + 1);
					}
				}
			}
			depository.working_ticks = 0;
		}
		depository.working_ticks++;
	}

	@Override
	public int get_cycle() {
		return 8;
	}

	@Override
	public int get_delay() {
		return 10;
	}


}
