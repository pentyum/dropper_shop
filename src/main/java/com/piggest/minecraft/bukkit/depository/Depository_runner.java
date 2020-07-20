package com.piggest.minecraft.bukkit.depository;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Async_structure_runner;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Depository_runner extends Async_structure_runner<Depository> {
	public Depository_runner(Depository_manager manager) {
		super(manager);
	}

	public boolean run_instance(Depository depository) {
		int price = depository.get_price();
		Economy economy = Dropper_shop_plugin.instance.get_economy();
		OfflinePlayer owner = depository.get_owner();
		if (economy.has(owner, price)) {
			if (owner.isOnline() == true) {
				depository.send_message((Player) owner, "存储器已扣除" + economy.format(price));
			}
			economy.withdrawPlayer(owner, price);
			depository.set_accessible(true);
		} else {
			depository.set_accessible(false);
		}
		return true;
	}

	@Override
	public int get_cycle() {
		return 20 * 3600;
	}

	@Override
	public int get_delay() {
		return 20;
	}

}
