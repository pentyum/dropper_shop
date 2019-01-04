package com.piggest.minecraft.bukkit.grinder;

import org.bukkit.Material;

import com.piggest.minecraft.bukkit.gui.Gui_config;
import com.piggest.minecraft.bukkit.gui.Gui_slot_type;

public class Grinder_config extends Gui_config {
	public Grinder_config() {
		this.set_gui(10, Material.BLUE_STAINED_GLASS_PANE, "§r左边放原料", Gui_slot_type.Indicator);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r左边放燧石或者黑曜石", Gui_slot_type.Indicator);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r左边为产品", Gui_slot_type.Indicator);
		this.set_gui(16, Material.BLUE_STAINED_GLASS_PANE, "§r右边为燧石单元储量", Gui_slot_type.Indicator);
	}

	public String get_gui_name() {
		return "磨粉机";
	}
}
