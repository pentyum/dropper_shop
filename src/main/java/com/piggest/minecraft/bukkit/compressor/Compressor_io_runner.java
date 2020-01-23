package com.piggest.minecraft.bukkit.compressor;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Compressor_io_runner extends Structure_runner {
	private Compressor compressor = null;

	public Compressor_io_runner(Compressor compressor) {
		this.compressor = compressor;
	}

	@Override
	public void run() {
		
	}

	@Override
	public int get_cycle() {
		return 8;
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
