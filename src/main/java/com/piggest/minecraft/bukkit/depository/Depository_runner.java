package com.piggest.minecraft.bukkit.depository;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

public class Depository_runner extends Structure_runner {
	public Depository_runner(Depository_manager manager) {
		super(manager);
	}

	public void run_instance(Structure structure) {
		Depository depository = (Depository) structure;
		int price = depository.get_price();
		Economy economy = Dropper_shop_plugin.instance.get_economy();
		if (economy.has(depository.get_owner(), price)) {
			if (depository.get_owner().isOnline() == true) {
				((Player) depository.get_owner()).sendMessage("存储器已扣除" + economy.format(price));
			}
			economy.withdrawPlayer(depository.get_owner(), price);
			depository.set_accessible(true);
		} else {
			depository.set_accessible(false);
		}
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
