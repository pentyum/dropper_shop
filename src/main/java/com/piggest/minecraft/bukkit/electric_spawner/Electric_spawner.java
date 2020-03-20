package com.piggest.minecraft.bukkit.electric_spawner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;

public class Electric_spawner extends Multi_block_with_gui {
	public static final int info_indicator_slot = 9;
	public static final int synthesis_button_slot = 17;
	public static final int look_button_slot = 16;
	private Random rand = new Random();
	@Override
	public boolean completed() {
		boolean base_structure = super.completed();
		if (base_structure == false) {
			return false;
		}
		HashSet<Material> block_set = new HashSet<>();
		block_set.add(this.get_block(0, 2, 1).getType());
		block_set.add(this.get_block(0, 2, -1).getType());
		block_set.add(this.get_block(1, 2, 0).getType());
		block_set.add(this.get_block(-1, 2, 0).getType());
		if (!block_set.contains(Material.CREEPER_HEAD)) {
			return false;
		}
		if (!block_set.contains(Material.ZOMBIE_HEAD)) {
			return false;
		}
		if (!block_set.contains(Material.SKELETON_SKULL)) {
			return false;
		}
		if (!block_set.contains(Material.WITHER_SKELETON_SKULL)) {
			return false;
		}
		return true;
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		Electric_spawner_manager manager = (Electric_spawner_manager) this.get_manager();
		HashMap<EntityType, Integer> probability_map = new HashMap<>();
		for (int i = 11; i < 15; i++) {
			ItemStack item = this.gui.getItem(i);
			if (!Grinder.is_empty(item)) {
				Entity_probability[] probabilities = manager.probability_map.get(Material_ext.get_full_name(item));
				if (probabilities != null) {
					for (Entity_probability probability : probabilities) {
						EntityType type = probability.first;
						int p = probability.second;
						if (probability_map.containsKey(type)) {
							probability_map.put(probability.first, probability_map.get(type) + p);
						} else {
							probability_map.put(probability.first, p);
						}
					}
				}
			}
		}
		ArrayList<Entry<EntityType, Integer>> probability_list = new ArrayList<Entry<EntityType, Integer>>(
				probability_map.entrySet());
		probability_list.sort(new Comparator<Entry<EntityType, Integer>>() {
			public int compare(Entry<EntityType, Integer> o1, Entry<EntityType, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		ArrayList<Entity_probability> new_probability_list = new ArrayList<Entity_probability>();
		int total_probability = 0;
		for (Entry<EntityType, Integer> entry : probability_list) {
			total_probability += entry.getValue();
			if (total_probability > 1000) {
				new_probability_list
						.add(new Entity_probability(entry.getKey(), entry.getValue() + 1000 - total_probability));
			} else {
				new_probability_list.add(new Entity_probability(entry.getKey(), entry.getValue()));
			}
		}
		if (slot == synthesis_button_slot) {
			int r = rand.nextInt(1000);
		} else if (slot == look_button_slot) {
			int price = Dropper_shop_plugin.instance.get_price_config().get_look_electric_spawner_price();
			if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
				this.send_message(player, "已扣除" + price);
			} else {
				this.send_message(player, "查看合成概率所需的钱不够，需要" + price);
				return;
			}
			String msg = String.format("总成功概率: %.1f%%", (float) total_probability / 10);
			for (Entity_probability probability : new_probability_list) {
				msg += String.format("%s: %.1f%%", probability.first.name(), (float) probability.second / 10);
			}
			this.send_message(player, msg);
		}
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean on_put_item(Player player, ItemStack cursor_item, int slot) {
		return true;
	}

	@Override
	public boolean on_take_item(Player player, ItemStack in_item, int slot) {
		return true;
	}

	@Override
	public boolean on_exchange_item(Player player, ItemStack in_item, ItemStack cursor_item, int slot) {
		return true;
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_electric_spawner_price();
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			this.send_message(player, "已扣除" + price);
			return true;
		} else {
			this.send_message(player, "建立刷怪机所需的钱不够，需要" + price);
			return false;
		}
	}

	@Override
	protected void init_after_set_location() {
		// TODO 自动生成的方法存根

	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return null;
	}

	@Nonnull
	public static Electric_spawner deserialize(@Nonnull Map<String, Object> args) {
		Electric_spawner structure = new Electric_spawner();
		structure.set_from_save(args);
		return structure;
	}
}
