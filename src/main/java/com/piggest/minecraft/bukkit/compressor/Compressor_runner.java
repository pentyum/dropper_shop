package com.piggest.minecraft.bukkit.compressor;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Compressor_runner extends Structure_runner {
	private Compressor compressor = null;
	public int working_ticks;

	public Compressor_runner(Compressor compressor) {
		this.compressor = compressor;
	}
	
	@Override
	public void run() {
		// TODO 自动生成的方法存根

	}

	@Override
	public int get_cycle() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public int get_delay() {
		// TODO 自动生成的方法存根
		return 0;
	}

	@Override
	public boolean is_asynchronously() {
		// TODO 自动生成的方法存根
		return false;
	}

}
