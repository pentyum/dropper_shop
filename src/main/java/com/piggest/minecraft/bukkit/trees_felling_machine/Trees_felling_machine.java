package com.piggest.minecraft.bukkit.trees_felling_machine;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;

public class Trees_felling_machine extends Multi_block_with_gui {

	@Override
	public void on_button_pressed(Player player, int slot) {
		// TODO 自动生成的方法存根

	}

	@Override
	public int completed() {
		int x;
		int y;
		int z;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Material material = this.get_block(x, y, z).getType();
					if (x == 0 && y == 0 && z == 0 && material != Material.STONECUTTER) {
						Bukkit.getLogger().info("切石机不对");
						return 0;
					}
					if (Math.abs(x) == 1 && Math.abs(y) == 1 && Math.abs(z) == 1 && material != Material.CHISELED_QUARTZ_BLOCK) {
						Bukkit.getLogger().info("堑制石英不对");
						return 0;
					}
					if (Math.abs(x) + Math.abs(z) + Math.abs(y) == 2 && material != Material.QUARTZ_PILLAR) {
						Bukkit.getLogger().info("竖纹石英不对");
						return 0;
					}
					if (Math.abs(x) == 0 && Math.abs(z) == 0 && y == -1 && material != Material.SMOOTH_QUARTZ) {
						Bukkit.getLogger().info("平滑石英不对");
						return 0;
					}
				}
			}
		}
		return 1;
	}

	@Override
	public boolean in_structure(Location loc) {
		int r_x = loc.getBlockX() - this.x;
		int r_y = loc.getBlockY() - this.y;
		int r_z = loc.getBlockZ() - this.z;
		if (Math.abs(r_x) <= 1 && Math.abs(r_y) <= 1 && Math.abs(r_z) <= 1) {
			return true;
		}
		return false;
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_make_grinder_price();
		if (!player.hasPermission("trees_felling_machine.make")) {
			player.sendMessage("你没有建立伐木机的权限");
			return false;
		}
		if (Dropper_shop_plugin.instance.get_economy().has(player, price)) {
			Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player, price);
			player.sendMessage("已扣除" + price);
			return true;
		} else {
			player.sendMessage("建立伐木机所需的钱不够，需要" + price);
			return false;
		}
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

}
