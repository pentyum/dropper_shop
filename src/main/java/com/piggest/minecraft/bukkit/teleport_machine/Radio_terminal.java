package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.block.Biome;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Radio;

public interface Radio_terminal extends Nameable, Unique, Elements_container {

	/*
	 * 获得天线段数，每段长度为1m，对应1/4波长
	 */
	public int get_n();

	public void set_n(int n);

	/*
	 * 获得信道中心频率，单位kHz
	 */
	public int get_channel_freq();

	/*
	 * 获得信道带宽，单位kHz
	 */
	public int get_channel_bandwidth();

	/*
	 * 设置信道中心频率，单位kHz
	 */
	public void set_channel_freq(int freq);

	/*
	 * 设置信道带宽，单位kHz
	 */
	public void set_channel_bandwidth(int bandwidth);

	public Location get_location();

	/*
	 * 获得当前单位频率的发射功率
	 */
	public default double get_current_radiant_power() {
		return Radio.get_radiant_power(this.get_voltage(this.get_state()), this.get_n(), this.get_channel_bandwidth(),
				this.get_channel_freq());
	}

	/*
	 * 获得当前总输入功率
	 */
	public default double get_current_input_power() {
		return Radio.get_input_power(this.get_voltage(this.get_state()), this.get_n(), this.get_channel_bandwidth(),
				this.get_channel_freq());
	}

	/*
	 * 获得特定状态下的发射功率
	 */
	public default double get_radiant_power(Radio_state state) {
		return Radio.get_radiant_power(this.get_voltage(state), this.get_n(), this.get_channel_bandwidth(),
				this.get_channel_freq());
	}

	public default double get_radiant_power(Radio_state state, int channel_bandwidth, int channel_freq) {
		return Radio.get_radiant_power(this.get_voltage(state), this.get_n(), channel_bandwidth, channel_freq);
	}

	public int get_voltage(Radio_state state);

	public Radio_state get_state();

	public void set_state(Radio_state state);

	/*
	 * 获得其他信号源导致的噪声(总强度)
	 */
	public default double get_other_noise(Radio_terminal source) {
		double total = 0;
		for (Radio_terminal terminal : Radio_manager.instance.values()) {
			if (terminal == source || terminal == this) {
				continue;
			}
			total += this.get_signal(terminal, terminal.get_state());
		}
		return total;
	}

	/*
	 * 获得本底噪声(单位频率强度)
	 */
	public default double get_biome_noise() {
		Biome biome = this.get_location().getBlock().getBiome();
		switch (biome) {
		case BADLANDS:
			return 1E-11;
		default:
			return 1E-11;
		}
	}

	/*
	 * 获得接收某个发射源信号的噪声强度(总强度)
	 */
	public default double get_noise(Radio_terminal source) {
		return this.get_biome_noise() * this.get_channel_bandwidth() + this.get_other_noise(source);
	}

	/*
	 * 获得某个发射源以此时发射到此处的信号强度
	 */
	public default double get_signal(Radio_terminal source, Radio_state state) {
		return this.get_signal(source, state, false);
	}

	/*
	 * 获得传输速率，单位为kbit/s
	 */
	public default int get_working_speed(Radio_terminal source) {
		double noise = this.get_noise(source);
		double signal = this.get_signal(source, Radio_state.WORKING, true);
		return Radio.Shannon_equation(this.get_channel_bandwidth(), (int) (signal / noise));
	}

	/*
	 * 搜台
	 */
	public default ArrayList<UUID> search() {
		ArrayList<UUID> result = new ArrayList<UUID>();
		Radio_manager manager = Dropper_shop_plugin.instance.get_radio_manager();
		for (Radio_terminal terminal : manager.values()) {
			if (terminal != this) {
				double target_signal = this.get_signal(terminal, terminal.get_state());
				double target_noise = this.get_noise(terminal);
				Bukkit.getLogger().info(terminal.getCustomName() + ": " + target_signal + "/" + target_noise);
				if (target_signal > target_noise) {
					result.add(terminal.get_uuid());
				}
			}
		}
		return result;
	}

	@Nullable
	public Radio_terminal get_current_work_with();

	public void set_current_work_with(Radio_terminal terminal);

	public default double get_signal(Radio_terminal source, Radio_state state, boolean b) {
		if (b == false) {// 关闭频段匹配
			return Radio.get_signal_at(source.get_location(), source.get_radiant_power(state),
					source.get_channel_freq(), source.get_channel_bandwidth(), this.get_location(),
					this.get_channel_freq(), this.get_channel_bandwidth());
		} else {// 开启频段匹配
			return Radio.get_signal_at(source.get_location(),
					source.get_radiant_power(state, this.get_channel_bandwidth(), this.get_channel_freq()),
					this.get_channel_freq(), this.get_channel_bandwidth(), this.get_location(), this.get_channel_freq(),
					this.get_channel_bandwidth());
		}
	}

	public default double get_x() {
		return Radio.x(this.get_n(), this.get_channel_freq());
	}

	public default double get_z() {
		return Radio.z(this.get_n(), this.get_channel_freq());
	}

	public default double get_radiant_r() {
		return Radio.radiation_r(this.get_n(), this.get_channel_freq());
	}

}
