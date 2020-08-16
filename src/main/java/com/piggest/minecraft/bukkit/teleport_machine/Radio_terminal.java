package com.piggest.minecraft.bukkit.teleport_machine;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Radio;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.block.Biome;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;

public interface Radio_terminal extends Nameable, Unique, Elements_container {

	/*
	 * 获得天线段数，每段长度为1m，对应1/4波长
	 */
	public int get_n();

	public void set_n(int n);

	/*
	 * 获得发射频率，单位kHz
	 */
	public int get_transmit_freq();

	/*
	 * 获得发射带宽，单位kHz
	 */
	public int get_transmit_bandwidth();

	/*
	 * 设置信道中心频率，单位kHz
	 */
	public void set_transmit_freq(int freq);

	/*
	 * 设置信道带宽，单位kHz
	 */
	public void set_transmit_bandwidth(int bandwidth);

	public Location get_location();

	/*
	 * 获得当前单位频率的发射功率，单位W
	 */
	public default double get_current_radiant_power() {
		return Radio.get_radiant_power(this.get_voltage(this.get_state()), this.get_n(),
				this.get_transmit_bandwidth(), this.get_transmit_freq());
	}

	/*
	 * 获得当前总输入功率，单位W
	 */
	public default double get_current_input_power() {
		return Radio.get_input_power(this.get_voltage(this.get_state()), this.get_n(),
				this.get_transmit_bandwidth(), this.get_transmit_freq());
	}

	/*
	 * 获得特定状态下的发射总发射功率，单位W
	 */
	public default double get_radiant_power(Radio_state state) {
		return Radio.get_radiant_power(this.get_voltage(state), this.get_n(), this.get_transmit_bandwidth(),
				this.get_transmit_freq());
	}

	public default double get_radiant_power(Radio_state state, int bandwidth, int freq) {
		return Radio.get_radiant_power(this.get_voltage(state), this.get_n(), bandwidth, freq);
	}

	public int get_voltage(Radio_state state);

	public Radio_state get_state();

	public void set_state(Radio_state state);

	/*
	 * 获得其他信号源导致的噪声(总强度)
	 */
	public default double get_other_noise(Radio_terminal source) {
		double total = 0;
		for (Radio_terminal other_terminal : Radio_manager.instance.values()) {
			if (other_terminal == source || other_terminal == this) {
				continue;
			}
			total += Radio.get_signal_at(other_terminal.get_location(), other_terminal.get_current_radiant_power(), other_terminal.get_transmit_freq(), other_terminal.get_transmit_bandwidth(), this.get_location(), source.get_transmit_freq(), source.get_transmit_bandwidth());
		}
		return total;
	}

	/*
	 * 获得本底噪声(单位频率强度)
	 */
	public default double get_biome_noise() {
		Biome biome = this.get_biome();
		switch (biome) {
			case BADLANDS:
				return 5E-11;
			default:
				return 5E-11;
		}
	}

	public Biome get_biome();

	/*
	 * 获得接收某个发射源信号的噪声强度(总强度)
	 */
	public default double get_noise(Radio_terminal source) {
		return this.get_biome_noise() * source.get_transmit_bandwidth() + this.get_other_noise(source);
	}

	/*
	 * 获得某个发射源以此时发射到此处的信号强度
	 */
	public default double get_signal(Radio_terminal source) {
		return Radio.get_signal_at(source.get_location(), source.get_current_radiant_power(), source.get_transmit_freq(), source.get_transmit_bandwidth(), this.get_location(), source.get_transmit_freq(), source.get_transmit_bandwidth());
	}

	/*
	 * 获得某个发射源特定状态下以此时发射到此处的信号强度
	 */
	public default double get_signal(Radio_terminal source, Radio_state state) {
		return Radio.get_signal_at(source.get_location(), source.get_radiant_power(state), source.get_transmit_freq(), source.get_transmit_bandwidth(), this.get_location(), source.get_transmit_freq(), source.get_transmit_bandwidth());
	}

	/*
	 * 获得传输速率，单位为kbit/s
	 */
	public default int get_working_speed(Radio_terminal source) {
		double noise = this.get_noise(source);
		double signal = this.get_signal(source);
		return Radio.Shannon_equation(this.get_transmit_bandwidth(), (int) (signal / noise));
	}

	/*
	 * 搜台
	 */
	public default ArrayList<UUID> search(CommandSender searcher, boolean debug) {
		ArrayList<UUID> result = new ArrayList<UUID>();
		Radio_manager manager = Dropper_shop_plugin.instance.get_radio_manager();
		int central_freq = Radio.get_central_freq(this.get_n());
		for (int scan_freq = (int) (central_freq
				- Radio.antenna_bandwidth * central_freq); scan_freq <= (int) (central_freq
				+ Radio.antenna_bandwidth * central_freq); scan_freq += 100) {
			for (Radio_terminal terminal : manager.values()) {
				if (terminal != this) {
					double target_signal = this.get_signal(terminal);
					double target_noise = this.get_noise(terminal);
					if (debug == true) {
						String debug_msg = terminal.getCustomName() + "(" + terminal.get_location() + ")" + ": "
								+ target_signal + "/" + target_noise + "(" + scan_freq + ")";
						searcher.sendMessage(debug_msg);
						Dropper_shop_plugin.instance.getLogger().info(debug_msg);
					}
					// Bukkit.getLogger().info(terminal.getCustomName() + ": " + target_signal + "/"
					// + target_noise);
					if (target_signal > target_noise) {
						if (!result.contains(terminal.get_uuid())) {
							result.add(terminal.get_uuid());
						}
					}
				}
			}
		}
		return result;
	}

	@Nullable
	public Teleporting_task get_current_task();

	public boolean set_current_task(Teleporting_task terminal);

	@Nullable
	public default Radio_terminal get_current_working_with() {
		Teleporting_task task = this.get_current_task();
		if (task == null) {
			return null;
		}
		return Radio_manager.instance.get(task.get_target());
	}

	/*
	 * 电抗
	 */
	public default double get_x() {
		return Radio.x(this.get_n(), this.get_transmit_freq());
	}

	/*
	 * 阻抗
	 */
	public default double get_z() {
		return Radio.z(this.get_n(), this.get_transmit_freq());
	}

	public default double get_radiant_r() {
		return Radio.radiation_r(this.get_n(), this.get_transmit_freq());
	}

	public ItemStack get_custom_flag();
}
