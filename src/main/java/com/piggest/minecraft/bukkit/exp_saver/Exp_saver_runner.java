package com.piggest.minecraft.bukkit.exp_saver;

import java.util.Collection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.inventory.ItemStack;

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
		Collection<Entity> near_entities = exp_saver.get_location().getWorld()
				.getNearbyEntities(exp_saver.get_location(), 3, 3, 3);
		for (Entity entity : near_entities) {
			if (entity instanceof ExperienceOrb) {
				ExperienceOrb exp_orb = (ExperienceOrb) entity;
				exp_saver.add_exp(exp_orb.getExperience());
				exp_orb.remove();
			}
		}
		ItemStack mending = exp_saver.get_mending();
		if (!Grinder.is_empty(mending)) {
			if (mending.getDurability() > 0) {
				mending.setDurability((short) (mending.getDurability() - exp_saver.remove_exp(1)));
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
