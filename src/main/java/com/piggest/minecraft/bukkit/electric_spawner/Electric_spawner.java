package com.piggest.minecraft.bukkit.electric_spawner;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.nms.NMS_manager;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.utils.language.Entity_zh_cn;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.Predicate;

public class Electric_spawner extends Multi_block_with_gui {
	public static final int info_indicator_slot = 9;
	public static final int synthesis_button_slot = 17;
	public static final int look_button_slot = 16;
	private Random rand = new Random();
	private EntityType entity_type = null;
	private int money = 0;
	private boolean minecart_upgrade = false;

	int completed_period = 0;
	int money_cost_period = 0;

	public Electric_spawner() {
		this.set_process(0);
	}

	public void set_spawn_entity(EntityType type) {
		this.entity_type = type;
		ItemStack indicator = this.gui.getItem(info_indicator_slot);
		ItemMeta meta = indicator.getItemMeta();
		List<String> lore = meta.getLore();
		if (type == null) {
			lore.set(1, "§r生成: 无");
		} else {
			lore.set(1, "§r生成: " + Entity_zh_cn.get_entity_name(type));
		}
		meta.setLore(lore);
		indicator.setItemMeta(meta);
	}

	public void set_money(int money) {
		this.money = money;
		ItemStack indicator = this.gui.getItem(info_indicator_slot);
		ItemMeta meta = indicator.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(2, "§r剩余金钱: " + money);
		meta.setLore(lore);
		indicator.setItemMeta(meta);
	}

	public float update_local_difficulty() {
		float local_difficulty = this.get_local_difficulty();
		ItemStack indicator = this.gui.getItem(info_indicator_slot);
		ItemMeta meta = indicator.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, String.format("§r运行状态: %s", this.is_active() ? "开启" : "关闭"));
		lore.set(3, String.format("§r区域难度: %.4f", local_difficulty));
		meta.setLore(lore);
		indicator.setItemMeta(meta);
		return local_difficulty;
	}

	public float get_local_difficulty() {
		return NMS_manager.local_difficulty.get_local_difficulty(this.get_location());
	}

	public int get_money() {
		return this.money;
	}

	public EntityType get_spawn_entity() {
		return this.entity_type;
	}

	public void spawn() {
		if (this.entity_type == null) {
			return;
		}
		Location loc = this.get_location();
		World world = loc.getWorld();
		if (this.minecart_upgrade == true) {
			Collection<Entity> nearby_minecart_list = world.getNearbyEntities(loc, 3, 2, 3, new Predicate<Entity>() {
				@Override
				public boolean test(Entity e) {
					if (e.getType() != EntityType.MINECART && e.getType() != EntityType.BOAT) {
						return false;
					}
					List<Entity> passengers = e.getPassengers();
					if (passengers != null) {
						if (passengers.size() > 0) {
							return false;
						}
					}
					return true;
				}
			});
		}
		ArrayList<Location> nearby_air_list = new ArrayList<Location>();
		for (int x = -3; x <= 3; x++) {
			for (int y = -2; y <= 2; y++) {
				for (int z = -3; z <= 3; z++) {
					Block block = world.getBlockAt(loc.getBlockX() + x, loc.getBlockY() + y, loc.getBlockZ() + z);
					if (block.getType() == Material.AIR || block.getType() == Material.CAVE_AIR) {
						nearby_air_list.add(block.getLocation());
					}
				}
			}
		}
		int size = nearby_air_list.size();
		if (size > 0) {
			int spawn_loc_index = this.rand.nextInt(size);
			world.spawnEntity(nearby_air_list.get(spawn_loc_index), this.entity_type);
		} else {
			// world.spawnParticle(particle, location, count);
		}
	}

	@Override
	protected void set_from_save(Map<String, Object> save) {
		super.set_from_save(save);
		String spawn_entity_name = (String) save.get("spawn-entity");
		if (spawn_entity_name != null) {
			this.set_spawn_entity(EntityType.valueOf(spawn_entity_name));
		}
		this.set_minecart_upgrade((boolean) save.get("minecart-upgrade"));
		this.set_money((int) save.get("money"));
	}

	public void set_minecart_upgrade(boolean minecart_upgrade) {
		this.minecart_upgrade = minecart_upgrade;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		if (this.entity_type != null) {
			save.put("spawn-entity", this.entity_type.name());
		}
		save.put("minecart-upgrade", this.minecart_upgrade);
		save.put("money", this.money);
		return save;
	}

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

	private HashMap<EntityType, Integer> get_probability_map() {
		Electric_spawner_manager manager = (Electric_spawner_manager) this.get_manager();
		HashMap<EntityType, Integer> probability_map = new HashMap<>();
		Random rand = new Random(this.get_chunk_location().get_slime_seed());
		for (int i = 11; i < 15; i++) {
			ItemStack item = this.gui.getItem(i);
			if (!Grinder.is_empty(item)) {
				int quantity = item.getAmount();
				Entity_probability[] probabilities = manager.probability_map.get(Material_ext.get_full_name(item));
				if (probabilities != null) {
					for (Entity_probability probability : probabilities) {
						EntityType type = probability.first;
						double p = probability.second;
						double offset = rand.nextGaussian();
						p = p + p / 2 * offset;
						if (p < 0) {
							p = 0;
						}
						if (probability_map.containsKey(type)) {
							probability_map.put(probability.first, probability_map.get(type) + (int) (p * quantity));
						} else {
							probability_map.put(probability.first, (int) (p * quantity));
						}
					}
				}
			}
		}
		return probability_map;
	}

	private ArrayList<Entity_probability> get_probability_list(Map<EntityType, Integer> probability_map,
															   double total_modifier) {
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
			int item_probability = (int) (entry.getValue() * total_modifier);
			total_probability += item_probability;
			if (item_probability == 0) {
				break;
			}
			if (total_probability >= 1000) {
				new_probability_list
						.add(new Entity_probability(entry.getKey(), item_probability + 1000 - total_probability));
				break;
			} else {
				new_probability_list.add(new Entity_probability(entry.getKey(), item_probability));
			}
		}
		return new_probability_list;
	}

	private double get_total_probability_modifier() {
		double total_modifier = 0.12;
		for (int i = 11; i < 15; i++) {
			ItemStack item = this.gui.getItem(i);
			if (!Grinder.is_empty(item)) {
				if (item.getType() == Material.LAPIS_BLOCK) {
					int quantity = item.getAmount();
					total_modifier += quantity * 0.02;
				}
			}
		}
		if (total_modifier > 1.4) {
			total_modifier = 1.4;
		}
		return total_modifier;
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		HashMap<EntityType, Integer> probability_map = this.get_probability_map();
		ArrayList<Entity_probability> new_probability_list = this.get_probability_list(probability_map,
				this.get_total_probability_modifier());
		if (slot == synthesis_button_slot) {
			int i = 0, j = 0, k = 0;
			int[] pool = new int[1000];
			for (k = 0; k < new_probability_list.size(); k++) {
				for (j = 0; j < new_probability_list.get(k).second && i < 1000; j++) {
					pool[i] = k;
					i++;
				}
			}
			int num = rand.nextInt(1000);
			if (num < i) {
				EntityType type = new_probability_list.get(pool[num]).first;
				this.set_spawn_entity(type);
				this.send_message(player, "成功召唤" + Entity_zh_cn.get_entity_name(type));
			} else {
				this.send_message(player, "召唤失败");
			}
			for (i = 11; i < 15; i++) {
				ItemStack item = this.gui.getItem(i);
				if (!Grinder.is_empty(item)) {
					this.gui.setItem(i, null);
				}
			}
		} else if (slot == look_button_slot) {
			int price = Dropper_shop_plugin.instance.get_price_config().get_look_electric_spawner_price();
			String format_price = Dropper_shop_plugin.instance.get_economy().format(price);
			if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
				this.send_message(player, "已扣除" + format_price);
			} else {
				this.send_message(player, "查看召唤概率所需的钱不够，需要" + format_price);
				return;
			}
			int total_probability = 0;
			StringBuilder msg = new StringBuilder("--------以下为可能召唤出的实体的概率--------");
			for (Entity_probability probability : new_probability_list) {
				msg.append(String.format("\n%s: %.1f%%", Entity_zh_cn.get_entity_name(probability.first),
						(float) probability.second / 10));
				total_probability += probability.second;
			}
			msg.append(String.format("\n总召唤成功概率: %.1f%%", (float) total_probability / 10));
			player.closeInventory();
			this.send_message(player, msg.toString());
		}
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
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
		String format_price = Dropper_shop_plugin.instance.get_economy().format(price);
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			this.send_message(player, "已扣除" + format_price);
			return true;
		} else {
			this.send_message(player, "建立刷怪机所需的钱不够，需要" + format_price);
			return false;
		}
	}

	@Override
	protected void init_after_set_location() {
		this.update_local_difficulty();
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return new ItemStack[]{this.gui.getItem(11), this.gui.getItem(12), this.gui.getItem(13),
				this.gui.getItem(14)};
	}

	public synchronized void set_process(int process) {
		this.set_process(0, process, "§e生成进度: %d %%", process);
	}

	public boolean is_active() {
		if (this.get_block(-1, -1, 0).isBlockPowered()) {
			return true;
		}
		if (this.get_block(1, -1, 0).isBlockPowered()) {
			return true;
		}
		if (this.get_block(0, -1, -1).isBlockPowered()) {
			return true;
		}
		if (this.get_block(0, -1, 1).isBlockPowered()) {
			return true;
		}
		return false;
	}


	@Nonnull
	public static Electric_spawner deserialize(@Nonnull Map<String, Object> args) {
		Electric_spawner structure = new Electric_spawner();
		structure.set_from_save(args);
		return structure;
	}

}
