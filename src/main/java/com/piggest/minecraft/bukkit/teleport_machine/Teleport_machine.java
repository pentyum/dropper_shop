package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.exp_saver.SetExpFix;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.nms.NMS_manager;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.utils.Radio;

public class Teleport_machine extends Multi_block_with_gui implements Radio_terminal, Elements_container {
	public static final int name_tag_slot = 47;
	public static final int online_voltage_indicator = 41;
	public static final int working_voltage_indicator = 42;
	public static final int bandwidth_indicator = 43;
	public static final int freq_indicator = 44;
	public static final int magic_indicator = 39;
	public static final int radio_indicator = 30;

	private String name = "魔术传送台";
	private int channel_freq = 0;
	private Radio_state state = Radio_state.OFF;
	private int channel_bandwidth = 0;
	private int n = 0;
	private int online_voltage = 1;
	private int working_voltage = 12;
	private int current_page = 1;
	private ArrayList<Radio_terminal> known_terminal_list = new ArrayList<Radio_terminal>();
	private int[] elements_amount = new int[96];
	private Inventory elements_gui = Bukkit.createInventory(this, 27);
	private int exp_magic_exchange_rate = 1000; // 每1000点经验转化为多少魔力
	private Radio_terminal current_work_with = null;

	public Teleport_machine() {
		Radio_manager.instance.add(this);
		this.init_elements_gui();
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		switch (slot) {
		case 17:// 上一页
			this.set_gui_terminal_list(this.current_page - 1);
			break;
		case 26:// 下一页
			this.set_gui_terminal_list(this.current_page + 1);
			break;
		case 28:// 搜台
			this.known_terminal_list = this.search();
			this.set_gui_terminal_list(1);
			break;
		case 29:// 立刻刷新无线电信息
			break;
		case 32:// 提高待机电压
			this.set_online_voltage(this.online_voltage + 1);
			break;
		case 33:// 提高发射电压
			this.set_working_voltage(this.online_voltage + 1);
			break;
		case 34:// 增加带宽
			this.set_channel_bandwidth(this.get_channel_bandwidth() + 100);
			break;
		case 35:// 提高载波频率
			this.set_channel_freq(this.get_channel_freq() + 500);
			break;
		case 36:// 显示元素信息
			player.openInventory(this.elements_gui);
			break;
		case 37:// 传送台上实体转化为元素
			break;
		case 40:// 玩家经验转化为魔力
			int total_exp = SetExpFix.getTotalExperience(player);
			int use_exp = 1000;
			if (total_exp < 1000) {
				use_exp = total_exp;
			}
			int add_magic = use_exp * exp_magic_exchange_rate / 1000;
			this.set_amount(Element.Magic, this.get_amount(Element.Magic) + add_magic);
			player.sendMessage("[传送机]成功将" + use_exp + "点经验转化为" + add_magic + "点魔力");
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
				this.set_working_voltage(this.online_voltage - 1);
			}
			break;
		case 52:// 减少带宽
			this.set_channel_bandwidth(this.get_channel_bandwidth() - 100);
			break;
		case 53:// 降低载波频率
			this.set_channel_freq(this.get_channel_freq() - 500);
			break;
		default:
			break;
		}
	}

	public void set_gui_terminal_list(int page) {
		if (page <= 0) {
			return;
		}
		int slot = 9;
		int start = (page - 1) * 18;
		if (start >= this.known_terminal_list.size()) {
			return;
		}
		for (Radio_terminal terminal = this.known_terminal_list.get(start); start < this.known_terminal_list.size()
				&& start < page * 18; start++) {
			ItemStack item = new ItemStack(Material.END_ROD);
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("传送至 " + terminal.getCustomName());
			ArrayList<String> lore = new ArrayList<String>();
			Location loc = terminal.get_location();
			lore.add("§7位置: " + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ",");
			lore.add("§7距离: " + (int) (loc.distance(this.get_location())) + "m");
			lore.add("§7频率: " + terminal.get_channel_freq() + "kHz");
			lore.add("§7载波带宽: " + terminal.get_channel_bandwidth() + "kHz");
			double signal = this.get_signal(terminal, terminal.get_state());
			double noise = this.get_noise(terminal);
			int snr = (int) (10 * Math.log10(signal / noise));
			lore.add("§7当前接收目标发射强度: " + snr + "dB");
			signal = terminal.get_signal(this, Radio_state.WORKING,true);
			noise = terminal.get_noise(this);
			snr = (int) (10 * Math.log10(signal / noise));
			lore.add("§7预计发射目标接收强度: " + snr + "dB");
			//lore.add("§7预计传输速率: kB/s");
			meta.setLore(lore);
			item.setItemMeta(meta);
			this.gui.setItem(slot, item);
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
		default:
			return false;
		}
	}

	@Override
	public boolean create_condition(Player player) {
		return true;
	}

	@Override
	protected void init_after_set_location() {
		this.n = 1;
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
	public int get_channel_freq() {
		return this.channel_freq;
	}

	@Override
	public int get_channel_bandwidth() {
		return this.channel_bandwidth;
	}

	@Override
	protected HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		save.put("state", this.state.name());
		return save;
	}

	@Override
	protected void set_from_save(Map<?, ?> save) {
		super.set_from_save(save);
		String state_string = (String) save.get("state");
		this.state = Radio_state.valueOf(state_string);
		this.channel_bandwidth = (int) save.get("channel-bandwidth");
		this.channel_freq = (int) save.get("channel-freq");
		this.working_voltage = (int) save.get("working-voltage");
		this.online_voltage = (int) save.get("online_voltage");
	}

	@Override
	public Radio_state get_state() {
		return this.state;
	}

	public void set_state(Radio_state state) {
		this.state = state;
		ItemStack item = this.gui.getItem(radio_indicator);
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(1, "§7运行状态: " + state.display_name);
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public int get_voltage(Radio_state state) {
		switch (state) {
		case WORKING:
			return 0;
		case ONLINE:
			return this.online_voltage;
		default:
			return this.working_voltage;
		}
	}

	private void set_online_voltage(int voltage) {
		this.online_voltage = voltage;
		ItemStack item = this.gui.getItem(online_voltage_indicator);
		List<String> lore = item.getItemMeta().getLore();
		lore.set(0, "§7" + voltage + " V");
	}

	private void set_working_voltage(int voltage) {
		this.working_voltage = voltage;
		ItemStack item = this.gui.getItem(working_voltage_indicator);
		List<String> lore = item.getItemMeta().getLore();
		lore.set(0, "§7" + voltage + " V");
	}

	@Override
	public void set_channel_freq(int freq) {
		if (Radio.check_channel_vaild(freq, this.channel_bandwidth, this.n)) {
			this.channel_freq = freq;
			ItemStack item = this.gui.getItem(freq_indicator);
			List<String> lore = item.getItemMeta().getLore();
			lore.set(0, "§7" + freq + " kHz");
		}
	}

	@Override
	public void set_channel_bandwidth(int bandwidth) {
		if (Radio.check_channel_vaild(this.channel_freq, bandwidth, this.n)) {
			this.channel_bandwidth = bandwidth;
			ItemStack item = this.gui.getItem(bandwidth_indicator);
			List<String> lore = item.getItemMeta().getLore();
			lore.set(0, "§7" + bandwidth + " kHz");
		}
	}

	@Override
	public boolean on_put_item(Player player, ItemStack cursor_item, int slot) {
		switch (slot) {
		case name_tag_slot:
			this.set_name_by_name_tag(cursor_item);
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

	private void set_name_by_name_tag(ItemStack item) {
		String id_name = Material_ext.get_id_name(item);
		if (id_name.equals("name_tag")) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName()) {
				this.setCustomName(meta.getDisplayName());
				item.setAmount(0);
			}
		}
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
		if (element == Element.Magic) {
			item = this.gui.getItem(magic_indicator);

		} else {
			item = this.elements_gui.getItem(element.order_id);
		}
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		lore.set(0, "§7剩余: " + amount + "单位");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	private void init_elements_gui() {
		for (Element element : Element.values()) {
			ItemStack item = new ItemStack(Material.CHEST);
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = new ArrayList<String>();
			lore.add("§7剩余: 0单位");
			meta.setLore(lore);
			meta.setDisplayName("§r" + element.name() + " 元素");
			item.setItemMeta(meta);
			NMS_manager.element_type_provider.set_element_id(item, element.atomic_number);
			this.elements_gui.setItem(element.order_id, item);
		}
		ItemStack back_item = new ItemStack(Material.REDSTONE_LAMP);
		ItemMeta meta = back_item.getItemMeta();
		meta.setDisplayName("返回传送机界面");
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
		lore.set(4, "§7天线长度: " + n + "m");
		int central_freq = Radio.get_central_freq(n);
		lore.set(5, "§7中心频率: " + central_freq + "kHz");
		lore.set(6, "§7天线频宽: " + (int) (central_freq * 2 * Radio.antenna_bandwidth) + "kHz");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public Radio_terminal get_current_work_with() {
		return this.current_work_with;
	}

	@Override
	public void set_current_work_with(Radio_terminal terminal) {
		this.current_work_with = terminal;
	}
}
