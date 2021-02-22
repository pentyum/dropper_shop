package com.piggest.minecraft.bukkit.dropper_shop;

import com.piggest.minecraft.bukkit.structure.Structure_manager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Dropper_shop_manager extends Structure_manager<Dropper_shop> {
	public static Dropper_shop_manager instance = null;

	public Dropper_shop_manager() {
		super(Dropper_shop.class);
		Dropper_shop_manager.instance = this;
	}

	/*
	@Override
	public Dropper_shop find(String player_name, Location loc, boolean new_structure) {
		if (new_structure == false) {
			Dropper_shop shop = this.get(loc);
			if (shop == null) {
				return null;
			}
			if (player_name != null) {
				if (!shop.get_owner_name().equalsIgnoreCase(player_name)) {
					return null;
				}
			}
			return shop;
		} else {
			if (loc.getBlock().getType() == Material.DROPPER) {
				Dropper_shop shop = new Dropper_shop();
				shop.set_location(loc);
				shop.set_owner(player_name);
				return shop;
			} else {
				return null;
			}
		}
	}
	*/
	@Override
	public Dropper_shop find_and_make(Player player, Location loc) {
		if (loc.getBlock().getType() == Material.DROPPER) {
			Dropper_shop shop = new Dropper_shop();
			shop.set_location(loc);
			shop.set_owner(player.getName());
			return shop;
		} else {
			return null;
		}
	}

	@Override
	public Dropper_shop find_existed(Location loc) {
		Dropper_shop shop = this.get(loc);
		if (shop == null) {
			return null;
		}
		return shop;
	}

	@Nonnull
	@Override
	public String get_permission_head() {
		return "dropper_shop";
	}

	@Override
	public Material[][][] get_model() {
		return null;
	}

	@Override
	public int[] get_center() {
		return null;
	}

	@Nullable
	@Override
	public String get_gui_name() {
		return null;
	}
}
