package com.piggest.minecraft.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

public class Chunk_location {
	private int x;
	private int z;
	private String world_name;

	public Chunk_location(String world_name, int x, int z) {
		this.world_name = world_name;
		this.x = x;
		this.z = z;
	}

	public Chunk_location(Chunk chunk) {
		this.world_name = chunk.getWorld().getName();
		this.x = chunk.getX();
		this.z = chunk.getZ();
	}

	public int get_x() {
		return this.x;
	}

	public int get_z() {
		return this.z;
	}

	public String get_world_name() {
		return this.world_name;
	}

	public World get_world() {
		return Bukkit.getWorld(this.world_name);
	}

	public Chunk get_chunk() {
		return this.get_world().getChunkAt(this.x, this.z);
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof Chunk_location) {
			Chunk_location other_location = (Chunk_location) other;
			if (other_location.x == this.x && other_location.z == this.z
					&& other_location.world_name.equalsIgnoreCase(this.world_name)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.world_name.length() + this.x + this.z;
	}
}
