package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.scheduler.BukkitRunnable;

public class Teleporting_task extends BukkitRunnable {
	private int total_byte = 0;
	private int completed_byte = 0;
	private Collection<Entity> entities = null;
	private Elements_composition elements = null;
	private Player operator;
	private UUID target = null;
	
	public void set_total_byte(int total_byte) {
		this.total_byte = total_byte;
	}

	public int get_total_byte() {
		return this.total_byte;
	}

	public void set_completed_byte(int completed_byte) {
		this.completed_byte = completed_byte;
	}

	public int get_completed_byte() {
		return this.completed_byte;
	}

	public void set_entities(Collection<Entity> entities) {
		this.entities = entities;
	}

	public Collection<Entity> get_entities() {
		return this.entities;
	}
	
	public void set_elements(Elements_composition elements) {
		this.elements = elements;
	}
	
	public Elements_composition get_elements() {
		return this.elements;
	}

	public void set_operater(Player operator) {
		this.operator = operator;
	}
	
	@Nullable
	public Player get_operater() {
		return this.operator;
	}

	@Override
	public void run() {
		for (Entity entity : this.get_entities()) {
			entity.teleport(Radio_manager.instance.get(this.target).get_location().add(0, 1, 0), TeleportCause.PLUGIN);
		}
	}

	public void set_target(UUID terminal) {
		this.target = terminal;
	}
	
	public UUID get_target() {
		return this.target;
	}
}
