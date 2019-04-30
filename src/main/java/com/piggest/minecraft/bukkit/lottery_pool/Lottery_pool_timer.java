package com.piggest.minecraft.bukkit.lottery_pool;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Lottery_pool_timer extends BukkitRunnable {
	private Lottery_pool lottery_pool;
	private Player player;
	public Lottery_pool_timer(Lottery_pool lottery_pool, Player player) {
		this.lottery_pool = lottery_pool;
		this.player = player;
	}

	@Override
	public void run() {
		lottery_pool.end_lottery(this.player);
	}

}
