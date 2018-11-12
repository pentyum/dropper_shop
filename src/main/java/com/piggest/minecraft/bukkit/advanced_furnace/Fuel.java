package com.piggest.minecraft.bukkit.advanced_furnace;

public enum Fuel {
	coal(80, Status.solid, 1000),
	charcoal(60, Status.solid, 800),
	coal_block(800, Status.solid, 1000),
	lava_bucket(1000, Status.liquid, 1200),
	oil(2000, Status.liquid, 1500),
	hydrogen(10000, Status.gas, 3000),
	CO(1500, Status.gas, 2000),
	CH4(3000, Status.gas, 2000);

	private Status status;
	private int temperature;
	private int energy;

	private Fuel(int energy, Status status, int temperature) {
		this.energy = energy;
		this.status = status;
		this.temperature = temperature;
	}
	
	public int get_energy() {
		return this.energy;
	}
	
	public Status get_status() {
		return this.status;
	}
	
	public int get_temperature() {
		return this.temperature;
	}
}
