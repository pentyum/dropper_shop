package com.piggest.minecraft.bukkit.anti_thunder;

import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Anti_thunder_runner extends BukkitRunnable {
	private Anti_thunder anti_thunder = null;
	private boolean started = false;

	public Anti_thunder_runner(Anti_thunder anti_thunder) {
		this.anti_thunder = anti_thunder;
	}

	public void start() {
		this.started = true;
	}

	@Override
	public void run() {
		OfflinePlayer owner = anti_thunder.get_owner();
		if (anti_thunder.completed() == true) {
			if (anti_thunder.is_active() == true) {
				int price = anti_thunder.get_manager().get_price();
				String format_price = Dropper_shop_plugin.instance.get_economy().format(price);
				if (Dropper_shop_plugin.instance.cost_player_money(price, owner)) {
					anti_thunder.send_msg_to_owner("[防雷器]已扣除" + format_price);
				} else {
					anti_thunder.send_msg_to_owner("[防雷器]金钱不够，防雷器已经暂停");
					anti_thunder.activate(false);
				}
			}
		} else {
			anti_thunder.remove();
			anti_thunder.send_msg_to_owner("[防雷器]区块" + anti_thunder.get_chunk_location() + "的防雷器结构不完整，已经移除");
		}
	}

	public boolean started() {
		return this.started;
	}
}
