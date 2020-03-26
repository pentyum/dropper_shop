package com.piggest.minecraft.bukkit.electric_spawner;

import org.bukkit.Chunk;
import org.bukkit.entity.EntityType;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Electric_spawner_runner extends Structure_runner {
	private Electric_spawner spawner;
	private int completed_period = 0;
	private Electric_spawner_manager manager;

	public Electric_spawner_runner(Electric_spawner spawner) {
		this.spawner = spawner;
		manager = (Electric_spawner_manager) spawner.get_manager();
	}

	@Override
	public void run() {
		spawner.update_local_difficulty();
		if (spawner.is_active()) {
			EntityType spawn_entity_type = spawner.get_spawn_entity();
			if (spawn_entity_type != null) {
				Entity_spawn_config spawn_config = manager.spawn_config_map.get(spawn_entity_type);
				int spawn_period = spawn_config.get_spawn_ticks() / this.get_cycle();
				float local_difficulty = spawner.get_local_difficulty();
				spawn_period = (int) ((float) spawn_period / (local_difficulty / 2.5));
				if (this.completed_period >= spawn_period) {// 生成完成
					this.completed_period = 0;
					spawner.set_process(0);
					Chunk chunk = spawner.get_location().getChunk();
					long new_inhabited_time = chunk.getInhabitedTime() - spawn_config.get_inhabited_time_cost();
					if (new_inhabited_time <= 0) {
						new_inhabited_time = 0;
					}
					chunk.setInhabitedTime(new_inhabited_time);
				} else {// 继续生成
					this.completed_period++;
					spawner.set_process(100 * this.completed_period / spawn_period);
				}
			}
		}
	}

	@Override
	public int get_cycle() {
		return 10;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}

}
