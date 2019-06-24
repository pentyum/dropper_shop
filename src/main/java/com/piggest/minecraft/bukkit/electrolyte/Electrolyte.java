package com.piggest.minecraft.bukkit.electrolyte;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Electrolyte extends Multi_block_with_gui implements HasRunner {

	@Override
	public Structure_runner[] get_runner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int completed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean in_structure(Location loc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean create_condition(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean on_break(Player player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		// TODO 自动生成的方法存根
		return false;
	}

}
