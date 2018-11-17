package com.piggest.minecraft.bukkit.grinder;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure_manager;

public class Grinder_manager extends Structure_manager<Grinder> {

	public Grinder_manager() {
		super(Grinder.class);
	}

	@Override
	public void add(Grinder grinder) {
		grinder.get_runner().runTaskTimerAsynchronously(Dropper_shop_plugin.instance, 10, 1);
		grinder.get_io_runner().runTaskTimerAsynchronously(Dropper_shop_plugin.instance, 10, 10);
		super.add(grinder);
	}

	@Override
	public void remove(Grinder grinder) {
		grinder.get_runner().cancel();
		grinder.get_io_runner().cancel();
		super.remove(grinder);
	}
}
