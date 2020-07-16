package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class Radio_manager extends HashMap<UUID, Radio_terminal> {

	/**
	 *
	 */
	private static final long serialVersionUID = 6122668059776454762L;
	public static Radio_manager instance;

	public Radio_manager() {
		super();
		Radio_manager.instance = this;
	}

	public static ArrayList<String> to_uuid_string_list(Collection<UUID> known_terminal_list) {
		ArrayList<String> uuid_list = new ArrayList<String>();
		for (UUID terminal_uuid : known_terminal_list) {
			uuid_list.add(terminal_uuid.toString());
		}
		return uuid_list;
	}
}
