package com.piggest.minecraft.bukkit.anti_thunder;

import org.bukkit.Location;
import org.bukkit.Material;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Anti_thunder_manager extends Structure_manager {
	public static Anti_thunder_manager instance;
	private Material[][][] model = {
			{ { Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK },
				{ Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK },
				{ Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK } },
			{ { null, null, null },
				{ null, Material.PISTON, null },
				{ null, null, null } }
		};
	private int center_x = 1;
	private int center_y = 1;
	private int center_z = 1;
	
	public Anti_thunder_manager() {
		super(Anti_thunder.class);
		instance = this;
	}
	
	@Override
	public Structure find(String player_name, Location loc, boolean new_structure) {
		// TODO 自动生成的方法存根
		return null;
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
		return new int[] {this.center_x,this.center_y,this.center_z};
	}
	
	public int get_cycle() {
		return Dropper_shop_plugin.instance.get_config().getInt("anti-thunder-cycle");
	}
	
	public int get_price() {
		return Dropper_shop_plugin.instance.get_price_config().get_anti_thunder_price();
	}
}
