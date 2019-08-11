package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.utils.Radio;

public class Teleport_machine extends Multi_block_with_gui implements Radio_terminal {
	public static final int name_tag_slot = 47;
	public static final int online_voltage_indicator = 41;
	public static final int working_voltage_indicator = 42;
	public static final int bandwidth_indicator = 43;
	public static final int freq_indicator = 44;

	private String name = "定点传送台";
	private int channel_freq = 0;
	private Radio_state state = Radio_state.OFF;
	private int channel_bandwidth = 0;
	private int n = 0;
	private int online_voltage = 1;
	private int working_voltage = 12;
	private int current_page = 1;
	private ArrayList<Radio_terminal> known_terminal_list = new ArrayList<Radio_terminal>();

	public Teleport_machine() {
		Radio_manager.instance.add(this);
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
		case 32:// 提高待机电压
			this.set_online_voltage(this.online_voltage + 1);
			break;
		case 33:// 提高发射电压
			this.set_working_voltage(this.online_voltage + 1);
			break;
		case 34:// 增加带宽
		case 35:// 提高载波频率
		case 37:// 传送台上实体转化为元素
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
		case 53:// 降低载波频率
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
			meta.setDisplayName(terminal.get_display_name());
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
				this.state = Radio_state.OFF;
			} else {
				this.state = Radio_state.ONLINE;
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
		lore.set(0, "§7 " + voltage + " V");
	}

	private void set_working_voltage(int voltage) {
		this.working_voltage = voltage;
		ItemStack item = this.gui.getItem(working_voltage_indicator);
		List<String> lore = item.getItemMeta().getLore();
		lore.set(0, "§7 " + voltage + " V");
	}

	@Override
	public void set_channel_freq(int freq) {
		if (Radio.check_channel_vaild(freq, this.channel_bandwidth, this.n)) {
			this.channel_freq = freq;
			ItemStack item = this.gui.getItem(freq_indicator);
			List<String> lore = item.getItemMeta().getLore();
			lore.set(0, "§7 " + freq + " kHz");
		}
	}

	@Override
	public void set_channel_bandwidth(int bandwidth) {
		if (Radio.check_channel_vaild(this.channel_freq, bandwidth, this.n)) {
			this.channel_bandwidth = bandwidth;
			ItemStack item = this.gui.getItem(bandwidth_indicator);
			List<String> lore = item.getItemMeta().getLore();
			lore.set(0, "§7 " + bandwidth + " kHz");
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
				this.set_display_name(meta.getDisplayName());
				item.setAmount(0);
			}
		}
	}

	@Override
	public void set_display_name(String name) {
		this.name = name;
	}

	@Override
	public String get_display_name() {
		return this.name;
	}
}
