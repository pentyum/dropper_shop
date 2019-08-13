package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.ArrayList;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.Nameable;
import org.bukkit.block.Biome;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Radio;

public interface Radio_terminal extends Nameable {

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

	public void set_state(Radio_state state);

	/*
	 * 获得其他信号源导致的噪声(总强度)
	 */
	public default double get_other_noise(Radio_terminal source) {
		double total = 0;
		for (Radio_terminal terminal : Radio_manager.instance) {
			if (terminal == source) {
				continue;
			}
			total = this.get_signal(terminal, terminal.get_state());
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
		double signal = this.get_signal(source, Radio_state.WORKING);
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
				double target_signal = this.get_signal(terminal, terminal.get_state());
				double target_noise = this.get_noise(terminal);
				if (target_signal > target_noise) {
					result.add(terminal);
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
			return Radio.get_signal_at(source.get_location(), source.get_radiant_power(state), this.get_channel_freq(),
					this.get_channel_bandwidth(), this.get_location(), this.get_channel_freq(),
					this.get_channel_bandwidth());
		}
	}
}
