package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.UUID;

public interface Unique {
	public UUID get_uuid();

	public void set_uuid(UUID uuid);

	public default UUID gen_uuid() {
		UUID uuid = UUID.randomUUID();
		this.set_uuid(uuid);
		return uuid;
	}
}
