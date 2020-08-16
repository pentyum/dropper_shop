package com.piggest.minecraft.bukkit.teleport_machine;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.exp_saver.SetExpFix;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.utils.Radio;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.function.Predicate;

public class Teleport_machine extends Multi_block_with_gui implements Radio_terminal {
	public static final int name_tag_slot = 47;
	public static final int item_to_elements_slot = 45;
	public static final int online_voltage_indicator = 41;
	public static final int working_voltage_indicator = 42;
	public static final int bandwidth_indicator = 43;
	public static final int freq_indicator = 44;
	public static final int magic_indicator = 39;
	public static final int radio_indicator = 38;
	public static final int open_switch = 27;
	public static final int auto_player_teleport_switch = 29;
	public static final int auto_entity_teleport_switch = 30;
	public static final int setting_mode_switch = 31;

	private UUID uuid;
	private String name = this.get_manager().get_gui_name();
	private int transmit_freq = 75000;
	private Radio_state state = Radio_state.OFF;
	private int channel_bandwidth = 100;
	private int n = 0;
	private int online_voltage = 12;
	private int working_voltage = 32;
	private int current_page = 1;
	ArrayList<UUID> known_terminal_list = new ArrayList<>();
	private int[] elements_amount = new int[96];
	private Inventory elements_gui = Bukkit.createInventory(this, 27, "元素存储");
	private int exp_magic_exchange_rate = 1000; // 每1000点经验转化为多少魔力
	private UUID auto_teleport_to = null;
	// private UUID current_work_with = null;
	private Teleporting_task teleport_task;
	private ItemStack custom_flag = null;
	int need_to_cost_magic_buffer = 0;

	public Teleport_machine() {
		this.gen_uuid();
		this.init_elements_gui();
		this.set_switch(open_switch, false);
		this.set_switch(auto_player_teleport_switch, false);
		this.set_switch(auto_entity_teleport_switch, false);
		this.set_switch(setting_mode_switch, false);
		this.set_process(0);
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		if (this.get_state() == Radio_state.WORKING) {
			this.send_message(player, "当前传送机处于运行中，无法进行该操作！请等待传送完成或者重启传送机。");
			return;
		}
		switch (slot) {
			case 17:// 上一页
				this.set_gui_terminal_list(this.current_page - 1);
				break;
			case 26:// 下一页
				this.set_gui_terminal_list(this.current_page + 1);
				break;
			case 28:// 搜台
				if (!player.hasPermission("teleport_machine.search")) {
					this.send_message(player, "你没有搜台的权限!");
					break;
				}
				this.known_terminal_list = this.search(player, false);
				this.set_gui_terminal_list(1);
				break;
			case 29:// 立刻刷新无线电信息
				this.set_gui_terminal_list(this.current_page);
				break;
			case 32:// 提高待机电压
				this.set_online_voltage(this.online_voltage + 1);
				break;
			case 33:// 提高发射电压
				this.set_working_voltage(this.working_voltage + 1);
				break;
			case 34:// 增加带宽
				this.set_transmit_bandwidth(this.channel_bandwidth + 100);
				break;
			case 35:// 提高载波频率
				this.set_transmit_freq(this.transmit_freq + 500);
				break;
			case 36:// 显示元素信息
				player.openInventory(this.elements_gui);
				break;
			case 37:// 传送台上实体转化为元素
				Elements_composition total_elements_add = new Elements_composition();
				for (Entity entity : this.get_entities_in_stage()) {
					if (!(entity instanceof Player)) {
						total_elements_add.add(Elements_composition.get_element_composition(entity));
						entity.remove();
					}
				}
				this.send_message(player, "总共转化元素: " + total_elements_add.toString());
				this.add(total_elements_add);
				break;
			case 40:// 玩家经验转化为魔力
				int total_exp = SetExpFix.getTotalExperience(player);
				int use_exp = 1000;
				if (total_exp < 1000) {
					use_exp = total_exp;
				}
				int add_magic = use_exp * exp_magic_exchange_rate / 1000;
				SetExpFix.setTotalExperience(player, total_exp - use_exp);
				this.set_amount(Element.Magic, this.get_amount(Element.Magic) + add_magic);
				this.send_message(player, "成功将" + use_exp + "点经验转化为" + add_magic + "点魔力");
				break;
			case 50:// 降低待机电压
				if (this.online_voltage <= 0) {
					this.set_online_voltage(0);
				} else {
					this.set_online_voltage(this.online_voltage - 1);
				}
				break;
			case 51:// 降低发射电压
				if (this.working_voltage <= 0) {
					this.set_working_voltage(0);
				} else {
					this.set_working_voltage(this.working_voltage - 1);
				}
				break;
			case 52:// 减少带宽
				this.set_transmit_bandwidth(this.channel_bandwidth - 100);
				break;
			case 53:// 降低载波频率
				this.set_transmit_freq(this.transmit_freq - 500);
				break;
			default:
				if (slot >= 9 && slot <= 25) {
					if (this.get_state() == Radio_state.OFF) {
						this.send_message(player, "关机状态不能传送");
						break;
					}
					ItemStack item = this.gui.getItem(slot);
					if (!Grinder.is_empty(item)) {
						if (item.getType() == Material.BEACON || Tag.ITEMS_BANNERS.isTagged(item.getType())) {
							if (slot < 17) {
								slot -= 9;
							} else {
								slot -= 10;
							}
							int index = slot + 16 * (this.current_page - 1);
							Radio_terminal terminal = Radio_manager.instance.get(this.known_terminal_list.get(index));
							if (terminal == null) {
								this.send_message(player, "目标传送机已经丢失");
							} else {
								if (this.is_setting_mode() == true) {// 自动模式，选择传送对象
									this.auto_teleport_to = terminal.get_uuid();
									this.send_message(player, "已经设置为自动传送至" + terminal.getCustomName());
								} else {
									this.start_teleport_to(player, terminal);
								}
							}
						}
					}
				}
				break;
		}
	}

	protected void start_teleport_to_no_operater() {
		Bukkit.getScheduler().runTask(Dropper_shop_plugin.instance, new Runnable() {
			@Override
			public void run() {
				if (Teleport_machine.this.auto_teleport_to == null) {
					return;
				}
				if (Teleport_machine.this.get_state() == Radio_state.OFF) {
					return;
				}
				boolean auto_entity_teleport = Teleport_machine.this.is_auto_entity_teleport();
				boolean auto_player_teleport = Teleport_machine.this.is_auto_player_teleport();
				if (auto_entity_teleport == false && auto_player_teleport == false) {
					return;
				}
				Radio_terminal terminal = Radio_manager.instance.get(Teleport_machine.this.auto_teleport_to);
				if (terminal == null) {
					return;
				}
				if (terminal.get_location().getWorld() == null) {
					return;
				}
				if (terminal.get_state() == Radio_state.OFF) {
					return;
				}
				if (Teleport_machine.this.get_state() == Radio_state.WORKING) {
					return;
				}

				Teleporting_task task_to_do = new Teleporting_task();
				if (auto_entity_teleport == true && auto_player_teleport == true) {
					task_to_do.set_entities(Teleport_machine.this.get_entities_in_stage());
				} else if (auto_entity_teleport == false && auto_player_teleport == true) {
					task_to_do.set_entities(Teleport_machine.this.get_players_in_stage());
				} else {
					task_to_do.set_entities(Teleport_machine.this.get_entities_no_player_in_stage());
				}
				Elements_composition total_elements_cost = new Elements_composition();
				for (Entity entity : task_to_do.get_entities()) {
					total_elements_cost.add(Elements_composition.get_element_composition(entity));
				}
				if (!terminal.has_enough(total_elements_cost)) {
					return;
				}
				task_to_do.set_elements(total_elements_cost);
				terminal.minus(total_elements_cost);
				int total_byte = total_elements_cost.get_total_byte();
				task_to_do.set_total_byte(total_byte);
				task_to_do.set_target(terminal.get_uuid());
				boolean task_submit_result = Teleport_machine.this.set_current_task(task_to_do);
				Teleport_machine.this.set_state(Radio_state.WORKING);
				if (task_submit_result == false) {
					return;
				}
			}
		});

	}

	private void start_teleport_to(Player operator, Radio_terminal terminal) {
		if (terminal == null) {
			this.send_message(operator, "目标已经丢失");
			return;
		}
		if (terminal.get_location().getWorld() == null) {
			this.send_message(operator, "目标世界未加载");
			return;
		}
		if (terminal.get_state() == Radio_state.OFF) {
			this.send_message(operator, "目标没有开机");
			return;
		}
		if (terminal.get_state() == Radio_state.WORKING) {
			this.send_message(operator, "目标正在运行中，请等待目标运行完成");
			return;
		}
		if (this.get_state() == Radio_state.WORKING) {
			this.send_message(operator, "当前传送机已经在工作了");
			return;
		}

		Teleporting_task task_to_do = new Teleporting_task();
		this.send_message(operator, "开始传送至" + terminal.getCustomName());
		task_to_do.set_entities(this.get_entities_in_stage());
		Elements_composition total_elements_cost = new Elements_composition();
		if (!operator.hasPermission("teleport_machine.no_consume")) {
			for (Entity entity : task_to_do.get_entities()) {
				total_elements_cost.add(Elements_composition.get_element_composition(entity));
			}
			operator.sendMessage("待传送实体数量: " + task_to_do.get_entities().size());
			operator.sendMessage("总共需要: " + total_elements_cost.toString());
			if (!terminal.has_enough(total_elements_cost)) {
				this.send_message(operator, "目标元素材料不足");
				return;
			}
		}
		task_to_do.set_elements(total_elements_cost);
		task_to_do.set_operater(operator);
		terminal.minus(total_elements_cost);
		int total_byte = total_elements_cost.get_total_byte();
		operator.sendMessage("数据总量: " + total_byte / 1024 + " kB");
		task_to_do.set_total_byte(total_byte);
		task_to_do.set_target(terminal.get_uuid());
		boolean task_submit_result = this.set_current_task(task_to_do);
		this.set_state(Radio_state.WORKING);
		if (task_submit_result == false) {
			this.send_message(operator, "不支持目标接收频段或者目标信息有误");
			return;
		}
	}

	public void complete_teleport_to(Radio_terminal terminal) {
		this.teleport_task.runTaskLater(Dropper_shop_plugin.instance, 1);
		this.add(this.teleport_task.get_elements());
		Player operator = this.teleport_task.get_operater();
		if (operator != null) {
			this.send_message(operator, "已完成传送");
		}
		this.set_state(Radio_state.ONLINE);
		this.set_process(0);
	}

	public void clean_gui_terminal_list() {
		for (int slot = 9; slot < 26; slot++) {
			if (slot == 17) {
				continue;
			}
			this.gui.setItem(slot, null);
		}
	}

	private void set_gui_terminal_item(int slot, @Nullable Radio_terminal terminal) {
		ItemStack item = new ItemStack(Material.BEACON);
		;
		ItemMeta meta = item.getItemMeta();
		if (terminal == null) {
			meta.setDisplayName("§c此传送机已丢失!");
		} else {
			if (terminal.get_custom_flag() != null) {
				item = terminal.get_custom_flag().clone();
				meta = item.getItemMeta();
			}
			meta.setDisplayName("传送至 " + terminal.getCustomName());
			ArrayList<String> lore = new ArrayList<String>();
			Location loc = terminal.get_location();
			lore.add("§7位置: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ","
					+ loc.getWorld().getName());
			lore.add("§7距离: " + (int) (Radio.get_distance(loc, this.get_location())) + " m");
			lore.add("§7频率: " + terminal.get_transmit_freq() + " kHz");
			lore.add("§7带宽: " + terminal.get_transmit_bandwidth() + " kHz");
			if (loc.getWorld() != null) {
				double signal = this.get_signal(terminal);
				double noise = this.get_noise(terminal);
				// Bukkit.getLogger().info(signal + "/" + noise);
				int snr = (int) (10 * Math.log10(signal / noise));
				if (snr == Integer.MIN_VALUE) {
					lore.add("§7当前接收目标发射强度: NaN dB");
				} else {
					lore.add("§7当前接收目标发射强度: " + snr + " dB");
				}
				signal = terminal.get_signal(this, Radio_state.WORKING);
				noise = terminal.get_noise(this);
				// Bukkit.getLogger().info(signal + "/" + noise);
				snr = (int) (10 * Math.log10(signal / noise));
				lore.add("§7预计发射目标接收强度: " + snr + " dB");
				int rate = terminal.get_working_speed(this);
				lore.add(String.format("§7预计传输速率: %d kB/s", rate * 8));
			} else {
				lore.add("§c目标世界未被加载!");
			}
			meta.setLore(lore);
		}
		item.setItemMeta(meta);
		this.gui.setItem(slot, item);
	}

	public void set_gui_terminal_list(int page) {
		if (page <= 0) {
			return;
		}
		int slot = 9;
		int start = (page - 1) * 16;
		for (; start < page * 16; start++) {
			if (start >= this.known_terminal_list.size()) {
				this.gui.setItem(slot, null);
			} else {
				UUID terminal_uuid = this.known_terminal_list.get(start);
				Radio_terminal terminal = Radio_manager.instance.get(terminal_uuid);
				this.set_gui_terminal_item(slot, terminal);
			}
			slot++;
			if (slot == 17) {
				slot++;
			}
		}
		this.current_page = page;
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		switch (slot) {
			case 27:
				if (on == false) {
					this.set_state(Radio_state.OFF);
				} else {
					this.set_state(Radio_state.ONLINE);
				}
				return true;
			case setting_mode_switch:
			case auto_entity_teleport_switch:
			case auto_player_teleport_switch:
				return true;
			default:
				return false;
		}
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_teleport_machine_price();
		String format_price = Dropper_shop_plugin.instance.get_economy().format(price);
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			this.send_message(player, "已扣除" + format_price);
			return true;
		} else {
			this.send_message(player, "建立传送机所需的钱不够，需要" + format_price);
			return false;
		}
	}

	@Override
	protected void init_after_set_location() {
		Radio_manager.instance.put(this.get_uuid(), this);
		this.set_n(1);
		this.set_online_voltage(this.online_voltage);
		this.set_working_voltage(this.working_voltage);
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public int get_n() {
		return this.n;
	}


	@Override
	public int get_transmit_freq() {
		return this.transmit_freq;
	}


	@Override
	public int get_transmit_bandwidth() {
		return this.channel_bandwidth;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		if (this.teleport_task != null) {
			save.put("total-bytes", this.teleport_task.get_total_byte());
			save.put("completed-bytes", this.teleport_task.get_completed_byte());
			save.put("current-working-with", this.teleport_task.get_target().toString());
		}
		if (this.auto_teleport_to != null) {
			save.put("auto-teleport-to", this.auto_teleport_to.toString());
		}
		save.put("is-auto-player-teleport", this.is_auto_player_teleport());
		save.put("is-auto-entity-teleport", this.is_auto_entity_teleport());
		save.put("is-setting-mode", this.is_setting_mode());
		save.put("state", this.state.name());
		save.put("channel-bandwidth", this.channel_bandwidth);
		save.put("channel-freq", this.transmit_freq);
		save.put("working-voltage", this.working_voltage);
		save.put("online-voltage", this.online_voltage);
		save.put("known-terminal-list", Radio_manager.to_uuid_string_list(this.known_terminal_list));
		save.put("custom-flag", this.custom_flag);
		return save;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void set_from_save(Map<String, Object> save) {
		super.set_from_save(save);
		if (save.get("auto-teleport-to") != null) {
			this.auto_teleport_to = UUID.fromString((String) save.get("auto-teleport-to"));
		}
		if (save.get("is-auto-player-teleport") != null) {
			this.set_switch(auto_player_teleport_switch, (boolean) save.get("is-auto-player-teleport"));
		}
		if (save.get("is-auto-entity-teleport") != null) {
			this.set_switch(auto_entity_teleport_switch, (boolean) save.get("is-auto-entity-teleport"));
		}
		if (save.get("is-setting-mode") != null) {
			this.set_switch(setting_mode_switch, (boolean) save.get("is-setting-mode"));
		}
		if (save.get("current-working-with") != null) {
			this.teleport_task = new Teleporting_task();
			teleport_task.set_total_byte((int) save.get("total-bytes"));
			teleport_task.set_completed_byte((int) save.get("completed-bytes"));
			teleport_task.set_entities(this.get_entities_in_stage());
			this.teleport_task.set_target(UUID.fromString((String) save.get("current-working-with")));
		}
		String state_string = (String) save.get("state");
		this.set_state(Radio_state.valueOf(state_string));
		if (this.state == Radio_state.OFF) {
			this.set_switch(open_switch, false);
		} else {
			this.set_switch(open_switch, true);
		}
		this.set_transmit_bandwidth((int) save.get("channel-bandwidth"));
		this.set_transmit_freq((int) save.get("channel-freq"));
		this.set_working_voltage((int) save.get("working-voltage"));
		this.set_online_voltage((int) save.get("online-voltage"));
		ArrayList<String> uuid_string_list = (ArrayList<String>) save.get("known-terminal-list");
		for (String uuid_string : uuid_string_list) {
			this.known_terminal_list.add(UUID.fromString(uuid_string));
		}
		if (save.get("custom-flag") != null) {
			this.custom_flag = (ItemStack) save.get("custom-flag");
		}
	}

	@Override
	public Radio_state get_state() {
		return this.state;
	}

	public void set_state(Radio_state state) {
		this.state = state;
		if (state != Radio_state.WORKING) {
			this.teleport_task = null;
		}
		ItemStack item = this.gui.getItem(radio_indicator);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(1, "§7运行状态: " + state.display_name);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public Biome get_biome() {
		return this.get_biome(0, 0, 0);
	}


	@Override
	public int get_voltage(Radio_state state) {
		switch (state) {
			case OFF:
				return 0;
			case ONLINE:
				return this.online_voltage;
			default:
				return this.working_voltage;
		}
	}

	protected void set_online_voltage(int voltage) {
		if (voltage > 1000) {
			voltage = 1000;
		}
		if (voltage < 0) {
			voltage = 0;
		}
		this.online_voltage = voltage;
		ItemStack item = this.gui.getItem(online_voltage_indicator);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§7" + voltage + " V");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	protected void set_working_voltage(int voltage) {
		if (voltage > 10000) {
			voltage = 10000;
		}
		if (voltage < 0) {
			voltage = 0;
		}
		this.working_voltage = voltage;
		ItemStack item = this.gui.getItem(working_voltage_indicator);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§7" + voltage + " V");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public void set_transmit_freq(int freq) {
		if (Radio.check_channel_vaild(freq, this.channel_bandwidth, this.n)) {
			this.transmit_freq = freq;
			ItemStack item = this.gui.getItem(freq_indicator);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			lore.set(0, "§7" + freq + " kHz");
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}

	@Override
	public void set_transmit_bandwidth(int bandwidth) {
		if (Radio.check_channel_vaild(this.transmit_freq, bandwidth, this.n)) {
			this.channel_bandwidth = bandwidth;
			ItemStack item = this.gui.getItem(bandwidth_indicator);
			ItemMeta meta = item.getItemMeta();
			List<String> lore = meta.getLore();
			lore.set(0, "§7" + bandwidth + " kHz");
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}

	@Override
	public boolean on_put_item(Player player, ItemStack cursor_item, int slot) {
		BukkitRunnable remover;
		switch (slot) {
			case name_tag_slot:
				remover = new Name_tag_remover(this);
				remover.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, 1);
				return true;
			case item_to_elements_slot:
				remover = new Item_remover(this);
				remover.runTaskLaterAsynchronously(Dropper_shop_plugin.instance, 1);
				return true;
			default:
				break;
		}
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
	public String get_display_name() {
		return this.name;
	}

	@Override
	public int get_amount(Element element) {
		return this.elements_amount[element.atomic_number];
	}

	@Override
	public void set_amount(Element element, int amount) {
		this.elements_amount[element.atomic_number] = amount;
		ItemStack item;
		String unit;
		if (element == Element.Magic) {
			item = this.gui.getItem(magic_indicator);
			unit = " kJ";
		} else {
			item = this.elements_gui.getItem(element.order_id);
			unit = " 单位";
		}
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§7剩余: " + amount + unit);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	private void init_elements_gui() {
		for (Element element : Element.values()) {
			if (element.order_id < 0) {
				continue;
			}
			ItemStack item = new ItemStack(Material.SUGAR);
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§7剩余: 0 单位");
			meta.setLore(lore);
			meta.setDisplayName("§r" + element.name() + " 元素");
			meta.getPersistentDataContainer().set(Element.namespacedkey, PersistentDataType.INTEGER,
					element.atomic_number);
			meta.setCustomModelData(Dropper_shop_plugin.custom_model_data_offset + element.atomic_number);
			item.setItemMeta(meta);
			this.elements_gui.setItem(element.order_id, item);
		}
		ItemStack back_item = new ItemStack(Material.REDSTONE_LAMP);
		ItemMeta meta = back_item.getItemMeta();
		meta.setDisplayName("§6返回传送机界面");
		back_item.setItemMeta(meta);
		this.elements_gui.setItem(this.elements_gui.getSize() - 1, back_item);
	}

	@Override
	public String getCustomName() {
		return this.get_display_name();
	}

	@Override
	public void setCustomName(String name) {
		this.name = name;
		ItemStack item = this.gui.getItem(radio_indicator);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§7终端名称: " + name);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public void set_n(int n) {
		this.n = n;
		ItemStack item = this.gui.getItem(radio_indicator);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(4, "§7天线长度: " + n + " m");
		int central_freq = Radio.get_central_freq(n);
		lore.set(5, "§7中心频率: " + central_freq + " kHz");
		lore.set(6, "§7天线频宽: " + (int) (central_freq * 2 * Radio.antenna_bandwidth) + " kHz");
		meta.setLore(lore);
		item.setItemMeta(meta);
		this.transmit_freq = central_freq;
		this.channel_bandwidth = 100;
		this.set_transmit_freq(central_freq);
		this.set_transmit_bandwidth(100);
	}

	@Override
	@Nullable
	public Teleporting_task get_current_task() {
		return this.teleport_task;
	}

	@Override
	public boolean set_current_task(Teleporting_task task) {
		if (task == null) {
			this.teleport_task = null;
			return true;
		}
		Radio_terminal terminal = Radio_manager.instance.get(task.get_target());
		if (terminal == null) {
			this.teleport_task = null;
			return false;
		}
		if (!Radio.check_channel_vaild(terminal.get_transmit_freq(), terminal.get_transmit_bandwidth(),
				this.n)) {
			return false;
		}
		this.teleport_task = task;
		return true;
	}

	@Override
	public Inventory get_elements_gui() {
		return this.elements_gui;
	}

	public void set_process(int process) {
		this.set_process(0, 0, "§e传输已完成%d%%", process);
	}

	public void refresh_power_info() {
		ItemStack item = this.gui.getItem(radio_indicator);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(2, String.format("§7当前输入功率: %.3f W", this.get_current_input_power()));
		lore.set(3, String.format("§7当前辐射功率: %.3f W", this.get_current_radiant_power() * this.channel_bandwidth));
		lore.set(7, String.format("§7辐射魔阻: %.3f Ω", this.get_radiant_r()));
		lore.set(8, String.format("§7辐射魔抗: %.3f Ω", this.get_x()));
		lore.set(9, String.format("§7输入阻抗: %.3f Ω", this.get_z()));
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public UUID get_uuid() {
		return this.uuid;
	}

	@Override
	public void set_uuid(UUID uuid) {
		this.uuid = uuid;
	}

	protected int get_current_page() {
		return this.current_page;
	}

	public Collection<Entity> get_entities_in_stage() {
		Location loc = this.get_location();
		World world = loc.getWorld();
		Predicate<Entity> filter = new Predicate<Entity>() {
			@Override
			public boolean test(Entity entity) {
				EntityType type = entity.getType();
				return type != EntityType.FALLING_BLOCK && type != EntityType.ENDER_CRYSTAL
						&& type != EntityType.PRIMED_TNT && type != EntityType.FISHING_HOOK;
			}
		};
		return world.getNearbyEntities(loc, 2, 2, 2, filter);
	}

	public Collection<Entity> get_entities_no_player_in_stage() {
		Location loc = this.get_location();
		World world = loc.getWorld();
		Predicate<Entity> fliter = new Predicate<Entity>() {
			@Override
			public boolean test(Entity entity) {
				EntityType type = entity.getType();
				return type != EntityType.PLAYER && type != EntityType.FALLING_BLOCK && type != EntityType.ENDER_CRYSTAL
						&& type != EntityType.PRIMED_TNT && type != EntityType.FISHING_HOOK;
			}
		};
		return world.getNearbyEntities(loc, 2, 2, 2, fliter);
	}

	public Collection<Entity> get_players_in_stage() {
		Location loc = this.get_location();
		World world = loc.getWorld();
		return world.getNearbyEntities(loc, 2, 2, 2, e -> e.getType() == EntityType.PLAYER);
	}

	public Teleporting_task get_teleporting_task() {
		return this.teleport_task;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return null;
	}

	public boolean is_auto_player_teleport() {
		return this.get_switch(auto_player_teleport_switch);
	}

	public boolean is_auto_entity_teleport() {
		return this.get_switch(auto_entity_teleport_switch);
	}

	public boolean is_setting_mode() {
		return this.get_switch(setting_mode_switch);
	}

	protected void set_custom_flag(ItemStack flag) {
		this.custom_flag = flag;
	}

	public ItemStack get_custom_flag() {
		return this.custom_flag;
	}

	@Nonnull
	public static Teleport_machine deserialize(@Nonnull Map<String, Object> args) {
		Teleport_machine structure = new Teleport_machine();
		structure.set_from_save(args);
		return structure;
	}
}

class Name_tag_remover extends BukkitRunnable {
	private Teleport_machine machine;

	public Name_tag_remover(Teleport_machine machine) {
		this.machine = machine;
	}

	@Override
	public void run() {
		ItemStack item = machine.getInventory().getItem(Teleport_machine.name_tag_slot);
		if (Grinder.is_empty(item)) {
			return;
		}
		String id_name = Material_ext.get_id_name(item);
		if (id_name.equals("name_tag")) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName()) {
				machine.setCustomName(meta.getDisplayName());
				item.setAmount(item.getAmount() - 1);
			}
		} else if (Tag.ITEMS_BANNERS.isTagged(item.getType())) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName()) {
				machine.setCustomName(meta.getDisplayName());
			}
			ItemStack custom_flag = item.clone();
			custom_flag.setAmount(1);
			machine.set_custom_flag(custom_flag);
			item.setAmount(item.getAmount() - 1);
		}
	}
}

class Item_remover extends BukkitRunnable {
	private Teleport_machine machine;

	public Item_remover(Teleport_machine machine) {
		this.machine = machine;
	}

	@Override
	public void run() {
		ItemStack item = machine.getInventory().getItem(Teleport_machine.item_to_elements_slot);
		if (!Grinder.is_empty(item)) {
			Elements_composition composition = Elements_composition.get_element_composition(item);
			machine.add(composition);
			item.setAmount(0);
		}
	}
}