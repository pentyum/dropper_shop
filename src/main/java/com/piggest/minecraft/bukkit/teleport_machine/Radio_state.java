package com.piggest.minecraft.bukkit.teleport_machine;

public enum Radio_state {
	OFF("关闭"), ONLINE("待机"), WORKING("运行中");

	public final String display_name;

	Radio_state(String display_name) {
		this.display_name = display_name;
	}
}
