package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Biome;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Radio;

public interface Radio_terminal {

	/*
	 * 获得天线段数，每段长度为1m，对应1/4波长
	 */
	public int get_n();

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
	public default int get_current_radiant_power() {
		return Radio.get_power(this.get_voltage(this.get_state()), this.get_n(), this.get_channel_bandwidth(),
				this.get_channel_freq());
	}

	/*
	 * 获得特定状态下的发射功率
	 */
	public default int get_radiant_power(Radio_state state) {
		return Radio.get_power(this.get_voltage(state), this.get_n(), this.get_channel_bandwidth(),
				this.get_channel_freq());

	}

	public int get_voltage(Radio_state state);

	public Radio_state get_state();

	/*
	 * 获得其他信号源导致的噪声(总强度)
	 */
	public default double get_other_noise(Radio_terminal source) {
		double total = 0;
		for (Radio_terminal terminal : Radio_manager.instance) {
			if (terminal == source) {
				continue;
			}
			int noise_radiant_power = terminal.get_current_radiant_power();
			int noise_central_freq = terminal.get_channel_freq();
			int noise_bandwidth = terminal.get_channel_bandwidth();
			double power = Radio.get_power_at(terminal.get_location(), noise_radiant_power, noise_central_freq,
					this.get_location());
			int sigal_central_freq = source.get_channel_freq();
			int sigal_bandwidth = source.get_channel_bandwidth();
			int sigal_start_freq = sigal_central_freq - sigal_bandwidth / 2;
			int sigal_end_freq = sigal_central_freq + sigal_bandwidth / 2;
			int noise_start_freq = noise_central_freq - noise_bandwidth / 2;
			int noise_end_freq = noise_central_freq + noise_bandwidth / 2;
			if (noise_end_freq > sigal_start_freq || noise_start_freq < sigal_end_freq) {
				total += power
						* (Math.min(sigal_end_freq, noise_end_freq) - Math.max(sigal_start_freq, noise_start_freq));
			}
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
			return 0.001;
		default:
			return 0.001;
		}
	}

	/*
	 * 获得接收某个发射源信号的噪声强度(总强度)
	 */
	public default double get_noise(Radio_terminal source) {
		return this.get_biome_noise() * this.get_channel_bandwidth() + this.get_other_noise(source);
	}

	/*
	 * 获得某个发射源以一定功率(单位频率)发射到此处的信号强度
	 */
	public default double get_signal(Radio_terminal source, int radiant_power) {
		double power = Radio.get_power_at(source.get_location(), radiant_power, source.get_channel_freq(),
				this.get_location());
		return power * source.get_channel_bandwidth();
	}

	/*
	 * 获得传输速率，单位为kbit/s
	 */
	public default int get_working_speed(Radio_terminal source) {
		double noise = this.get_noise(source);
		double signal = this.get_signal(source, source.get_radiant_power(Radio_state.WORKING));
		return Radio.Shannon_equation(source.get_channel_bandwidth(), (int) (signal / noise));
	}

	/*
	 * 搜台
	 */
	public default ArrayList<Radio_terminal> search() {
		ArrayList<Radio_terminal> result = new ArrayList<Radio_terminal>();
		Radio_manager manager = Dropper_shop_plugin.instance.get_radio_manager();
		for (Radio_terminal terminal : manager) {
			if (terminal != this) {
				double target_signal = this.get_signal(terminal, terminal.get_current_radiant_power());
				double target_noise = this.get_noise(terminal);
				if (target_signal > target_noise) {
					result.add(terminal);
				}
			}
		}
		return result;
	}
	
	public String get_display_name();
	
	public void set_display_name(String name);
}
