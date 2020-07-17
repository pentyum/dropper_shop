package com.piggest.minecraft.bukkit.depository;

import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import com.piggest.minecraft.bukkit.structure.Has_runner;
import com.piggest.minecraft.bukkit.structure.Old_structure_runner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class Depository_manager extends Gui_structure_manager<Depository> implements Has_runner {
	public static Depository_manager instance = null;
	private final Material[][][] model = {
			{{Material.LAPIS_BLOCK, Material.IRON_BLOCK, Material.LAPIS_BLOCK},
					{Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK},
					{Material.LAPIS_BLOCK, Material.IRON_BLOCK, Material.LAPIS_BLOCK}},
			{{Material.IRON_BLOCK, Material.IRON_BARS, Material.IRON_BLOCK},
					{Material.IRON_BARS, Material.END_ROD, Material.IRON_BARS},
					{Material.IRON_BLOCK, Material.IRON_BARS, Material.IRON_BLOCK}},
			{{Material.LAPIS_BLOCK, Material.IRON_BLOCK, Material.LAPIS_BLOCK},
					{Material.IRON_BLOCK, Material.DIAMOND_BLOCK, Material.IRON_BLOCK},
					{Material.LAPIS_BLOCK, Material.IRON_BLOCK, Material.LAPIS_BLOCK}}};
	private final int[] center = new int[]{1, 1, 1};

	public Depository_manager() {
		super(Depository.class);
		Depository_manager.instance = this;
	}

	public Depository find(String player_name, Location loc, boolean new_deop) {
		int x;
		int y;
		int z;
		Depository depository;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					// Bukkit.getLogger().info("正在搜索"+check_loc.toString());
					if (material == Material.END_ROD) {
						// Bukkit.getLogger().info("在" + check_loc.toString() + "找到了末地烛");
						if (new_deop == true) {
							depository = new Depository();
							depository.set_location(check_loc);
							if (depository.completed() == true) {
								depository.set_owner(player_name);
								return depository;
							}
						} else {
							depository = this.get(check_loc);
							if (player_name != null) {
								if (depository != null && depository.get_owner_name().equalsIgnoreCase(player_name)) {
									return depository;
								}
							} else {
								if (depository != null) {
									return depository;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public String get_gui_name() {
		return "存储器";
	}

	@Override
	public int get_slot_num() {
		return 9;
	}

	// @Override
	// public InventoryType get_inventory_type() {
	// return InventoryType.HOPPER;
	// }

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int[] get_process_bar() {
		return NO_BAR;
	}

	@Override
	public Old_structure_runner[] init_runners() {
		return new Old_structure_runner[]{new Depository_item_importer(this), new Depository_runner(this)};
	}

	@Override
	public String get_permission_head() {
		return "depository";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return this.center;
	}
}
