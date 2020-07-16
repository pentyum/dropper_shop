package com.piggest.minecraft.bukkit.exp_saver;

import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;

import java.util.Collection;

public class Exp_saver_exp_importer extends Structure_runner {
	public Exp_saver_exp_importer(Exp_saver_manager manager) {
		super(manager);
	}

	@Override
	public void run_instance(Structure structure) {
		Exp_saver exp_saver = (Exp_saver) structure;

		if (exp_saver.is_loaded() == false) {
			return;
		}
		Location loc = exp_saver.get_location();
		Collection<Entity> near_entities = loc.getWorld().getNearbyEntities(loc, 3, 3, 3,
				e -> e.getType() == EntityType.EXPERIENCE_ORB);

		for (Entity entity : near_entities) {
			ExperienceOrb exp_orb = (ExperienceOrb) entity;
			exp_saver.add_exp(exp_orb.getExperience());
			exp_orb.remove();
		}
	}

	@Override
	public int get_cycle() {
		return 20;
	}

	@Override
	public int get_delay() {
		return 10;
	}

}
