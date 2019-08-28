package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.Collection;
import org.bukkit.entity.Entity;

public class Teleporting_task {
	private int total_byte = 0;
	private int completed_byte = 0;
	private Collection<Entity> entities = null;
	private Elements_composition elements = null;
	
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
}
