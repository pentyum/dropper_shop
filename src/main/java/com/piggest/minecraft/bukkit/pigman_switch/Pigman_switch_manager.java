package com.piggest.minecraft.bukkit.pigman_switch;

import com.piggest.minecraft.bukkit.structure.Structure_manager;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Pigman_switch_manager extends Structure_manager<Pigman_switch> {
	private final Material[][][] model = {
			{{Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE},
					{Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE},
					{Material.SANDSTONE, Material.SANDSTONE, Material.SANDSTONE}},
			{{null, null, null}, {null, Material.OBSIDIAN, null}, {null, null, null}}};
	private final int center_x = 1;
	private final int center_y = 1;
	private final int center_z = 1;
	public static Pigman_switch_manager instance;

	public Pigman_switch_manager() {
		super(Pigman_switch.class);
		instance = this;
	}
	/*
	@Override
	public Pigman_switch find(String player_name, Location loc, boolean new_structure) {
		int x;
		int y;
		int z;
		Pigman_switch pigman_switch;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					// Bukkit.getLogger().info("正在搜索"+check_loc.toString());
					if (material == Material.OBSIDIAN) {
						if (new_structure == true) {
							pigman_switch = new Pigman_switch();
							pigman_switch.set_location(check_loc);
							if (pigman_switch.completed() == true) {
								return pigman_switch;
							}
						} else {
							pigman_switch = (Pigman_switch) this.get(check_loc);
							if (pigman_switch != null) {
								return pigman_switch;
							}
						}
					}
				}
			}
		}
		return null;
	}
	*/

	@Nonnull
	@Override
	public String get_permission_head() {
		return "pigman_switch";
	}

	@Nonnull
	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return new int[]{this.center_x, this.center_y, this.center_z};
	}

	@Nonnull
	@Override
	public String get_gui_name() {
		return "猪人生成抑制器";
	}
}
