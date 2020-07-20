package com.piggest.minecraft.bukkit.electric_spawner;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Async_structure_runner;
import org.bukkit.Chunk;
import org.bukkit.Difficulty;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class Electric_spawner_runner extends Async_structure_runner<Electric_spawner> {
	private Electric_spawner_manager manager;

	public Electric_spawner_runner(Electric_spawner_manager manager) {
		super(manager);
		this.manager = manager;
	}

	@Override
	public boolean run_instance(Electric_spawner spawner) {
		if (spawner.is_loaded() == false) {
			return false;
		}
		Inventory gui = spawner.getInventory();
		ItemStack look_button = gui.getItem(Electric_spawner.look_button_slot);
		if (!Grinder.is_empty(look_button)) {
			ItemMeta meta = look_button.getItemMeta();
			List<String> lore = meta.getLore();
			int price = Dropper_shop_plugin.instance.get_price_config().get_look_electric_spawner_price();
			lore.set(0, "§7需要消耗" + Dropper_shop_plugin.instance.get_economy().format(price));
		}
		Difficulty world_difficulty = spawner.get_location().getWorld().getDifficulty();
		int default_difficulty = Electric_spawner_manager.difficulty_values.get(world_difficulty);
		float local_difficulty = spawner.update_local_difficulty();
		if (Math.abs(local_difficulty) < 1e-3) {
			return true;
		}
		if (local_difficulty < default_difficulty) {
			return true;
		}
		if (!spawner.is_active()) {
			return true;
		}
		if (spawner.get_money() <= 0) {
			return true;
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
		return true;
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
