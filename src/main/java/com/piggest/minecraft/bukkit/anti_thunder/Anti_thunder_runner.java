package com.piggest.minecraft.bukkit.anti_thunder;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Async_structure_runner;
import org.bukkit.OfflinePlayer;

public class Anti_thunder_runner extends Async_structure_runner<Anti_thunder> {
	public Anti_thunder_runner(Anti_thunder_manager anti_thunder_manager) {
		super(anti_thunder_manager);
	}

	@Override
	public int get_cycle() {
		return 20;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean run_instance(Anti_thunder anti_thunder) {
		if (anti_thunder.working_ticks >= anti_thunder.get_manager().get_cycle() * 20) {
			anti_thunder.working_ticks = 0;
		}
		if (anti_thunder.working_ticks == 0) {
			OfflinePlayer owner = anti_thunder.get_owner();
			anti_thunder.working_ticks = 0;
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
			anti_thunder.working_ticks += this.get_cycle();
			return true;
		} else {
			anti_thunder.working_ticks += this.get_cycle();
			return false;
		}
		//anti_thunder.remove();
		//anti_thunder.send_msg_to_owner("[防雷器]区块" + anti_thunder.get_chunk_location() + "的防雷器结构不完整，已经移除");
	}

}
