package com.piggest.minecraft.bukkit.gui;

import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Gui_runner extends Structure_runner {

	public Gui_runner(Multi_block_with_gui multi_block_with_gui) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public int get_cycle() {
		return 2;
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
