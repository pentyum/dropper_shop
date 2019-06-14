package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Exp_saver_manager extends Gui_structure_manager {
	public static Exp_saver_manager instance = null;

	private int[] buttons = new int[] { 9, 10, 11, 15, 16, 17 };

	public Exp_saver_manager() {
		super(Exp_saver.class);
		Exp_saver_manager.instance = this;
		this.set_gui(buttons[0], Material.FIREWORK_ROCKET, "§r取出10点经验", Gui_slot_type.Button);
		this.set_gui(buttons[1], Material.FIREWORK_ROCKET, "§r取出100点经验", Gui_slot_type.Button);
		this.set_gui(buttons[2], Material.FIREWORK_ROCKET, "§r取出1000点经验", Gui_slot_type.Button);
		this.set_gui(buttons[3], Material.HOPPER, "§r存入10点经验", Gui_slot_type.Button);
		this.set_gui(buttons[4], Material.HOPPER, "§r存入100点经验", Gui_slot_type.Button);
		this.set_gui(buttons[5], Material.HOPPER, "§r存入1000点经验", Gui_slot_type.Button);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r右边进行经验修补", Gui_slot_type.Indicator);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r每点经验修复一点耐久值", Gui_slot_type.Indicator);
	}

	@Override
	public Exp_saver find(String player_name, Location loc, boolean new_structure) {
		return this.find(loc, new_structure);
	}

	public Exp_saver find(Location loc, boolean new_structure) {
		int x;
		int y;
		int z;
		Exp_saver exp_saver;
		for (x = -1; x <= 1; x++) {
			for (y = -2; y <= 0; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					if (material == Material.DIAMOND_BLOCK) {
						if (new_structure == true) {
							exp_saver = new Exp_saver();
							exp_saver.set_location(check_loc);
							if (exp_saver.completed() > 0) {
								return exp_saver;
							}
						} else {
							exp_saver = (Exp_saver) this.get(check_loc);
							if (exp_saver != null) {
								return exp_saver;
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
		return "经验存储器";
	}

	public int[] get_buttons() {
		return buttons;
	}

	@Override
	public int get_slot_num() {
		return 27;
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int[] get_process_bar() {
		return new int[] { 0 };
	}

}
