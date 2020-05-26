package com.piggest.minecraft.bukkit.respawn_price;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class Respawn_listener implements Listener {
	@EventHandler
	public void on_player_respawn(EntityDeathEvent event) {
		if(!(event.getEntity() instanceof Player)){
			return;
		}
		FileConfiguration config = Dropper_shop_plugin.instance.get_config();
		int respawn_price_threshold = config.getInt("respawn-price-threshold");
		if (respawn_price_threshold < 0) {
			return;
		}
		Economy eco = Dropper_shop_plugin.instance.get_economy();
		Player player = (Player) event.getEntity();
		int mode = config.getInt("respawn-price-mode");
		int price = config.getInt("respawn-price");
		int current_money = (int) eco.getBalance(player);
		String msg = "[死亡惩罚]由于你的资产大于等于" + eco.format(respawn_price_threshold) + "，因此复活需要消耗";
		if (current_money >= respawn_price_threshold) {
			int cost_money = 0;
			if (mode == 0) {
				cost_money = price;
				msg += eco.format(price) + "。";
			} else {
				cost_money = current_money * price / 100;
				msg += price + "%" + "的资产，共计" + eco.format(cost_money);
			}
			if (cost_money > current_money) {
				cost_money = current_money;
			}
			eco.withdrawPlayer(player, cost_money);
			player.sendMessage(msg);
		} else {
			player.sendMessage("[死亡惩罚]由于你的资产小于" + eco.format(respawn_price_threshold) + "因此不需消耗。");
		}
	}
}
