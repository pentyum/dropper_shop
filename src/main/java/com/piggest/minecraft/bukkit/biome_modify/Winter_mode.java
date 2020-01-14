package com.piggest.minecraft.bukkit.biome_modify;

import java.util.HashMap;

import org.bukkit.block.Biome;

public class Winter_mode {
	HashMap<Biome,Biome> winter_biome_map = new HashMap<>();
	public Winter_mode() {
		this.winter_biome_map.put(Biome.BEACH, Biome.SNOWY_BEACH);
		this.winter_biome_map.put(Biome.STONE_SHORE, Biome.SNOWY_BEACH);
		this.winter_biome_map.put(Biome.RIVER, Biome.FROZEN_RIVER);
		
		this.winter_biome_map.put(Biome.OCEAN, Biome.FROZEN_OCEAN);
		this.winter_biome_map.put(Biome.DEEP_OCEAN, Biome.DEEP_FROZEN_OCEAN);
		this.winter_biome_map.put(Biome.COLD_OCEAN, Biome.FROZEN_OCEAN);
		this.winter_biome_map.put(Biome.DEEP_COLD_OCEAN, Biome.DEEP_FROZEN_OCEAN);
		this.winter_biome_map.put(Biome.LUKEWARM_OCEAN, Biome.FROZEN_OCEAN);
		this.winter_biome_map.put(Biome.DEEP_LUKEWARM_OCEAN, Biome.DEEP_FROZEN_OCEAN);
		this.winter_biome_map.put(Biome.WARM_OCEAN, Biome.COLD_OCEAN);
		
		this.winter_biome_map.put(Biome.PLAINS, Biome.SNOWY_TUNDRA);
		this.winter_biome_map.put(Biome.SUNFLOWER_PLAINS, Biome.SNOWY_TUNDRA);
		
		this.winter_biome_map.put(Biome.MOUNTAINS, Biome.SNOWY_MOUNTAINS);
		this.winter_biome_map.put(Biome.GRAVELLY_MOUNTAINS, Biome.SNOWY_MOUNTAINS);
		this.winter_biome_map.put(Biome.MODIFIED_GRAVELLY_MOUNTAINS, Biome.SNOWY_MOUNTAINS);
		
		this.winter_biome_map.put(Biome.TAIGA, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.TAIGA_HILLS, Biome.SNOWY_TAIGA_HILLS);
		this.winter_biome_map.put(Biome.TAIGA_MOUNTAINS, Biome.SNOWY_TAIGA_MOUNTAINS);
		
		this.winter_biome_map.put(Biome.GIANT_SPRUCE_TAIGA, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.GIANT_SPRUCE_TAIGA_HILLS, Biome.SNOWY_TAIGA_HILLS);
		this.winter_biome_map.put(Biome.GIANT_TREE_TAIGA, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.GIANT_TREE_TAIGA_HILLS, Biome.SNOWY_TAIGA_HILLS);
		
		this.winter_biome_map.put(Biome.FOREST, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.BIRCH_FOREST, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.BIRCH_FOREST_HILLS, Biome.SNOWY_TAIGA_HILLS);
		this.winter_biome_map.put(Biome.DARK_FOREST, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.DARK_FOREST_HILLS, Biome.SNOWY_TAIGA_HILLS);
		this.winter_biome_map.put(Biome.FLOWER_FOREST, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.TALL_BIRCH_FOREST, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.SWAMP, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.SWAMP_HILLS, Biome.SNOWY_TAIGA_HILLS);
		
		this.winter_biome_map.put(Biome.MUSHROOM_FIELDS, Biome.SNOWY_TAIGA);
		this.winter_biome_map.put(Biome.MUSHROOM_FIELD_SHORE, Biome.SNOWY_BEACH);
		
		this.winter_biome_map.put(Biome.BADLANDS, Biome.SNOWY_MOUNTAINS);
		this.winter_biome_map.put(Biome.BADLANDS_PLATEAU, Biome.SNOWY_MOUNTAINS);
		this.winter_biome_map.put(Biome.MODIFIED_BADLANDS_PLATEAU, Biome.SNOWY_MOUNTAINS);
		this.winter_biome_map.put(Biome.ERODED_BADLANDS, Biome.SNOWY_MOUNTAINS);
		this.winter_biome_map.put(Biome.WOODED_BADLANDS_PLATEAU, Biome.SNOWY_TAIGA_MOUNTAINS);
		this.winter_biome_map.put(Biome.MODIFIED_WOODED_BADLANDS_PLATEAU, Biome.SNOWY_TAIGA_MOUNTAINS);
		
		this.winter_biome_map.put(Biome.JUNGLE, Biome.TAIGA);
		this.winter_biome_map.put(Biome.JUNGLE_HILLS, Biome.TAIGA_HILLS);
		this.winter_biome_map.put(Biome.JUNGLE_EDGE, Biome.TAIGA);
		this.winter_biome_map.put(Biome.MODIFIED_JUNGLE, Biome.TAIGA);
		this.winter_biome_map.put(Biome.MODIFIED_JUNGLE_EDGE, Biome.TAIGA);
		this.winter_biome_map.put(Biome.BAMBOO_JUNGLE, Biome.TAIGA);
		this.winter_biome_map.put(Biome.BAMBOO_JUNGLE_HILLS, Biome.TAIGA_HILLS);
		
		this.winter_biome_map.put(Biome.SAVANNA, Biome.TAIGA);
		this.winter_biome_map.put(Biome.SAVANNA_PLATEAU, Biome.TAIGA_HILLS);
		this.winter_biome_map.put(Biome.SHATTERED_SAVANNA, Biome.TAIGA);
		this.winter_biome_map.put(Biome.SHATTERED_SAVANNA_PLATEAU, Biome.TAIGA_HILLS);
		
		this.winter_biome_map.put(Biome.DESERT, Biome.SNOWY_TUNDRA);
		this.winter_biome_map.put(Biome.DESERT_HILLS, Biome.SNOWY_TUNDRA);
		this.winter_biome_map.put(Biome.DESERT_LAKES, Biome.FROZEN_RIVER);
	}
}
