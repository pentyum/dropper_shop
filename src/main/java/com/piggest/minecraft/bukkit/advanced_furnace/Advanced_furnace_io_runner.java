package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Advanced_furnace_io_runner extends Structure_runner {
	private Advanced_furnace adv_furnace;

	public Advanced_furnace_io_runner(Advanced_furnace advanced_furnace) {
		this.adv_furnace = advanced_furnace;
	}

	@Override
	public void run() {
		if (this.adv_furnace.is_loaded() == false) {
			return;
		}
		Hopper solid_hopper = this.adv_furnace.get_solid_reactant_hopper();
		if (solid_hopper != null) {
			for (ItemStack item : solid_hopper.getInventory().getContents()) {
				if (item != null && item.getType() != Material.AIR) {
					this.adv_furnace.add_a_solid(item);
				}
			}
		}
		Hopper fuel_hopper = this.adv_furnace.get_fuel_hopper();
		if (fuel_hopper != null) {
			for (ItemStack item : fuel_hopper.getInventory().getContents()) {
				if (item != null && item.getType() != Material.AIR) {
					this.adv_furnace.add_a_fuel(item);
				}
			}
		}
		int[][] solid_product_check_list = { { 1, -1, 2 }, { 2, -1, 1 }, { -1, -1, 2 }, { 2, -1, -1 }, { 1, -1, -2 },
				{ -2, -1, 1 }, { -2, -1, -1 }, { -1, -1, -2 } }; // 检查固体产品输出
		for (int[] relative_coord : solid_product_check_list) {
			BlockState block = this.adv_furnace.get_block(relative_coord[0], relative_coord[1], relative_coord[2])
					.getState();
			if (block instanceof Chest) { // 输出固体产品
				boolean output_result = Inventory_io.move_item_to_inventoryholder(adv_furnace.getInventory(),
						Advanced_furnace.solid_product_slot, (InventoryHolder) block);
				if (output_result == true) { // 成功输出则跳出循环，否则继续找箱子
					break;
				}
			}
		}
		if (this.adv_furnace.get_temperature() > 4000) {
			Location loc = this.adv_furnace.get_location();
			loc.getWorld().createExplosion(loc, 8);
			this.adv_furnace.remove();
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
