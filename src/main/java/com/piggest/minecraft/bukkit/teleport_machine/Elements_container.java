package com.piggest.minecraft.bukkit.teleport_machine;

import org.bukkit.inventory.Inventory;

import javax.annotation.Nullable;

public interface Elements_container {
	public int get_amount(Element element);

	public void set_amount(Element element, int amount);

	public Inventory get_elements_gui();

	public default void add(@Nullable Elements_container other) {
		if (other != null) {
			for (Element element : Element.values()) {
				this.set_amount(element, this.get_amount(element) + other.get_amount(element));
			}
		}
	}

	public default void minus(Elements_container other) {
		for (Element element : Element.values()) {
			this.set_amount(element, this.get_amount(element) - other.get_amount(element));
		}
	}

	public default boolean has_enough(Elements_container other) {
		for (Element element : Element.values()) {
			if (this.get_amount(element) < other.get_amount(element)) {
				return false;
			}
		}
		return true;
	}

}
