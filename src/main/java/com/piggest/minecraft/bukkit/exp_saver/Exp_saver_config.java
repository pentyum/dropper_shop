package com.piggest.minecraft.bukkit.exp_saver;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.gui.Gui_config;
import com.piggest.minecraft.bukkit.gui.Gui_slot_type;

public class Exp_saver_config extends Gui_config {
	private int[] buttons = new int[] { 9, 10, 11, 15, 16, 17 };
	
	public Exp_saver_config() {
		this.set_gui(buttons[0], Material.FIREWORK_ROCKET, "§r取出1级", Gui_slot_type.Button);
		this.set_gui(buttons[1], Material.FIREWORK_ROCKET, "§r取出5级", Gui_slot_type.Button);
		this.set_gui(buttons[2], Material.FIREWORK_ROCKET, "§r取出10级", Gui_slot_type.Button);
		this.set_gui(buttons[3], Material.HOPPER, "§r存入1级", Gui_slot_type.Button);
		this.set_gui(buttons[4], Material.HOPPER, "§r存入5级", Gui_slot_type.Button);
		this.set_gui(buttons[5], Material.HOPPER, "§r存入10级", Gui_slot_type.Button);
	}
	
	@Override
	public String get_gui_name() {
		return "经验存储器";
	}
	
	public int[] get_buttons() {
		return buttons;
	}
}
