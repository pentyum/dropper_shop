package com.piggest.minecraft.bukkit.electric_spawner;

import org.bukkit.entity.EntityType;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Electric_spawner_runner extends Structure_runner {
	private Electric_spawner spawner;
	private int completed_ticks = 0;
	private Electric_spawner_manager manager;

	public Electric_spawner_runner(Electric_spawner spawner) {
		this.spawner = spawner;
		manager = (Electric_spawner_manager) spawner.get_manager();
	}

	@Override
	public void run() {
		if (spawner.is_active()) {
			EntityType spawn_entity_type = spawner.get_spawn_entity();
			if (spawn_entity_type != null) {
				Entity_spawn_config spawn_config = manager.spawn_config_map.get(spawn_entity_type);
				if (this.completed_ticks >= spawn_config.get_spawn_ticks()) {
					this.completed_ticks = 0;
					spawner.set_process(0);
				} else {
					this.completed_ticks++;
					spawner.set_process(100 * this.completed_ticks / spawn_config.get_spawn_ticks());
				}
			}
		}
	}

	@Override
	public int get_cycle() {
		return 1;
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
