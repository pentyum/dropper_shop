package com.piggest.minecraft.bukkit.exp_saver;

import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Exp_saver_runner extends Structure_runner {
	Exp_saver exp_saver = null;

	public Exp_saver_runner(Exp_saver exp_saver) {
		this.exp_saver = exp_saver;
	}

	@Override
	public void run() {
		Collection<Entity> near_entities = exp_saver.get_location().getWorld()
				.getNearbyEntities(exp_saver.get_location(), 3, 3, 3);
		for (Entity entity : near_entities) {
			if (entity instanceof ExperienceOrb) {
				ExperienceOrb exp_orb = (ExperienceOrb) entity;
				exp_saver.add_exp(exp_orb.getExperience());
				exp_orb.remove();
			}
		}
		int[] levels = new int[] { 1, 5, 10, 1, 5, 10 };
		int[] buttons = Exp_saver.get_buttons();
		for (int i = 0; i < buttons.length; i++) {
			if (exp_saver.pressed_button(buttons[i])) {
				ItemStack item = exp_saver.getInventory().getItem(buttons[i]);
				ItemMeta meta = item.getItemMeta();
				List<String> lore = meta.getLore();
				Player player = Bukkit.getPlayer(lore.get(0));
				if (i < 3) {
					exp_saver.output_exp(levels[i], player);
					exp_saver.unpress_button(buttons[i]);
					break;
				} else {
					exp_saver.input_exp(levels[i], player);
					exp_saver.unpress_button(buttons[i]);
					break;
				}
			}
			exp_saver.unpress_button(buttons[i]);
		}
	}

	@Override
	public int get_cycle() {
		return 10;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}

}
