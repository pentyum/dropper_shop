package com.piggest.minecraft.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.Arrays;

public class Chunk_location {
	private final int x;
	private final int z;
	private final String world_name;

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

	public boolean is_loaded() {
		if (this.get_world() == null) {
			return false;
		}
		return this.get_world().isChunkLoaded(get_x(), get_z());
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

	@Override
	public String toString() {
		return "(" + this.world_name + "," + this.x + "," + this.z + ")";
	}

	public long get_slime_seed() {
		long seed = this.get_world().getSeed();
		seed = seed + (long) (x * x * 0x4c1906) + (long) (x * 0x5ac0db) + (long) (z * z) * 0x4307a7L
				+ (long) (z * 0x5f24f) ^ 0x3ad8025f;
		return seed;
	}

	/**
	 * 基于Chunk[]批量建立Chunk_location对象
	 *
	 * @param chunks 区块数组
	 * @return Chunk_location数组
	 */
	public static Chunk_location[] create_chunk_locations(Chunk[] chunks) {
		Chunk_location[] locations = new Chunk_location[chunks.length];
		Arrays.parallelSetAll(locations, i -> new Chunk_location(chunks[i]));
		return locations;
	}
}
