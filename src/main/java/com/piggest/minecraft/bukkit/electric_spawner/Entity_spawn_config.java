package com.piggest.minecraft.bukkit.electric_spawner;

public class Entity_spawn_config {
	private int spawn_ticks;
	private int inhabited_time_cost;

	public Entity_spawn_config(int spawn_ticks, int inhabited_time_cost) {
		this.spawn_ticks = spawn_ticks;
		this.inhabited_time_cost = inhabited_time_cost;
	}

	public int get_spawn_ticks() {
		return this.spawn_ticks;
	}

	public int get_inhabited_time_cost() {
		return this.inhabited_time_cost;
	}
}
