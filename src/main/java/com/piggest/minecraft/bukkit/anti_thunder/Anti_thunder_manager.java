package com.piggest.minecraft.bukkit.anti_thunder;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Has_runner;
import com.piggest.minecraft.bukkit.structure.Structure_manager;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import org.bukkit.Material;

public class Anti_thunder_manager extends Structure_manager<Anti_thunder> implements Has_runner {
	public static Anti_thunder_manager instance;
	private final Material[][][] model = {
			{{Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK},
					{Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK},
					{Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK}},
			{{null, null, null}, {null, Material.PISTON, null}, {null, null, null}}};
	private final int[] center = new int[]{1, 1, 1};

	public Anti_thunder_manager() {
		super(Anti_thunder.class);
		instance = this;
	}

	@Override
	public String get_permission_head() {
		return "anti_thunder";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return this.center;
	}

	public int get_cycle() {
		return Dropper_shop_plugin.instance.get_config().getInt("anti-thunder-cycle");
	}

	public int get_price() {
		return Dropper_shop_plugin.instance.get_price_config().get_anti_thunder_price();
	}

	@Override
	public Structure_runner[] init_runners() {
		return new Structure_runner[0];
	}
}
