package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.scheduler.BukkitRunnable;

public class Advanced_furnace_temp_runner extends BukkitRunnable {
	private Advanced_furnace adv_furnace;

	public Advanced_furnace_temp_runner(Advanced_furnace adv_furnace) {
		this.adv_furnace = adv_furnace;
	}

	public void run() {
		if (this.adv_furnace.get_location().getChunk().isLoaded() == false) {
			return;
		}
		double current_temp = adv_furnace.get_temperature();
		double p = adv_furnace.get_power();
		double loss = adv_furnace.get_power_loss();
		double d_current_temp = p - loss;
		adv_furnace.set_temperature(current_temp + d_current_temp);
	}
}
