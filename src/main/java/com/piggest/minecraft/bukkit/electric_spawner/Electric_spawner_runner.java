package com.piggest.minecraft.bukkit.electric_spawner;

import com.piggest.minecraft.bukkit.structure.Structure;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Electric_spawner_runner extends Structure_runner {
    private Electric_spawner_manager manager;

    public Electric_spawner_runner(Electric_spawner_manager manager) {
        super(manager);
        this.manager = manager;
    }

    @Override
    public void run_instance(Structure structure) {
        Electric_spawner spawner = (Electric_spawner) structure;

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
            if (spawner.completed_period >= spawn_period) {// 生成完成
                spawner.completed_period = 0;
                spawner.set_process(0);
                Chunk chunk = spawner.get_location().getChunk();
                long new_inhabited_time = chunk.getInhabitedTime() - spawn_config.get_inhabited_time_cost();
                if (new_inhabited_time <= 0) {
                    new_inhabited_time = 0;
                }
                chunk.setInhabitedTime(new_inhabited_time);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        spawner.spawn();
                    }
                }.runTaskLater(Dropper_shop_plugin.instance, 1);
            } else {// 继续生成
                spawner.completed_period++;
                spawner.set_process(100 * spawner.completed_period / spawn_period);
            }
        }

        if (spawner.money_cost_period >= 2) {
            spawner.money_cost_period = 0;
            spawner.set_money(spawner.get_money() - 1);
        } else {
            spawner.money_cost_period++;
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

}
