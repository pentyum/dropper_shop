package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.HashSet;

public class Radio_manager extends HashSet<Radio_terminal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6122668059776454762L;
	public static Radio_manager instance;

	public Radio_manager() {
		super();
		Radio_manager.instance = this;
	}
}
