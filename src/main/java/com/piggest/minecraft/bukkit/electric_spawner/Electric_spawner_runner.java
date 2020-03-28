package com.piggest.minecraft.bukkit.electric_spawner;

import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.entity.EntityType;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Electric_spawner_runner extends Structure_runner {
	private Electric_spawner spawner;
	private int completed_period = 0;
	private int money_cost_period = 0;
	private Electric_spawner_manager manager;

	public Electric_spawner_runner(Electric_spawner spawner) {
		this.spawner = spawner;
		manager = (Electric_spawner_manager) spawner.get_manager();
	}

	@Override
	public void run() {
		if (spawner.is_loaded() == false) {
			return;
		}
		Difficulty world_difficulty = spawner.get_location().getWorld().getDifficulty();
		int default_difficulty = Electric_spawner_manager.difficulty_values.get(world_difficulty);
		float local_difficulty = spawner.update_local_difficulty();
		if (Math.abs(local_difficulty) < 1e-3) {
			return;
		}
		if (local_difficulty < default_difficulty) {
			return;
		}
		if (!spawner.is_active()) {
			return;
		}
		if (spawner.get_money() <= 0) {
			return;
		}
		EntityType spawn_entity_type = spawner.get_spawn_entity();
		if (spawn_entity_type != null) {
			Entity_spawn_config spawn_config = manager.spawn_config_map.get(spawn_entity_type);
			int spawn_period = spawn_config.get_spawn_ticks() / this.get_cycle();
			spawn_period = (int) (spawn_period * default_difficulty / local_difficulty);
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

		if (this.money_cost_period >= 4) {
			this.money_cost_period = 0;
			spawner.set_money(spawner.get_money() - 1);
		} else {
			this.money_cost_period++;
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
