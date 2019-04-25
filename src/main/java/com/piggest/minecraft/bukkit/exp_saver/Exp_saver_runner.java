package com.piggest.minecraft.bukkit.exp_saver;

import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.depository.Update_component;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Exp_saver_runner extends Structure_runner {
	Exp_saver exp_saver = null;

	public Exp_saver_runner(Exp_saver exp_saver) {
		this.exp_saver = exp_saver;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		Location loc = this.exp_saver.get_location();
		if (loc.getChunk().isLoaded() == false) {
			return;
		}
		Collection<Entity> near_entities = loc.getWorld().getNearbyEntities(loc, 3, 3, 3);
		for (Entity entity : near_entities) {
			if (entity instanceof ExperienceOrb) {
				ExperienceOrb exp_orb = (ExperienceOrb) entity;
				exp_saver.add_exp(exp_orb.getExperience());
				exp_orb.remove();
			}
		}
		ItemStack mending = exp_saver.get_mending();
		if (!Grinder.is_empty(mending)) {
			if (!Update_component.is_component(mending)) {
				if (mending.getDurability() > 0) {
					mending.setDurability((short) (mending.getDurability() - exp_saver.remove_exp(1)));
				}
			}
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
