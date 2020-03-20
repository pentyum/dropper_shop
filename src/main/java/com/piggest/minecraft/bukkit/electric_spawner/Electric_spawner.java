package com.piggest.minecraft.bukkit.electric_spawner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;

public class Electric_spawner extends Multi_block_with_gui {
	public static final int info_indicator_slot = 9;
	public static final int synthesis_button_slot = 17;
	public static final int look_button_slot = 16;

	@Override
	public boolean completed() {
		boolean base_structure = super.completed();
		if (base_structure == false) {
			return false;
		}
		HashSet<Material> block_set = new HashSet<>();
		block_set.add(this.get_block(0, 2, 1).getType());
		block_set.add(this.get_block(0, 2, -1).getType());
		block_set.add(this.get_block(1, 2, 0).getType());
		block_set.add(this.get_block(-1, 2, 0).getType());
		if (!block_set.contains(Material.CREEPER_HEAD)) {
			return false;
		}
		if (!block_set.contains(Material.ZOMBIE_HEAD)) {
			return false;
		}
		if (!block_set.contains(Material.SKELETON_SKULL)) {
			return false;
		}
		if (!block_set.contains(Material.WITHER_SKELETON_SKULL)) {
			return false;
		}
		return true;
	}

	@Override
	public void on_button_pressed(Player player, int slot) {
		HashMap<EntityType, Integer> map = new HashMap<>();
		if (slot == synthesis_button_slot) {
			

		} else if (slot == look_button_slot) {
			int price = Dropper_shop_plugin.instance.get_price_config().get_look_electric_spawner_price();
			if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
				this.send_message(player, "已扣除" + price);
			} else {
				this.send_message(player, "查看合成概率所需的钱不够，需要" + price);
				return;
			}

		}
	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean on_put_item(Player player, ItemStack cursor_item, int slot) {
		return true;
	}

	@Override
	public boolean on_take_item(Player player, ItemStack in_item, int slot) {
		return true;
	}

	@Override
	public boolean on_exchange_item(Player player, ItemStack in_item, ItemStack cursor_item, int slot) {
		return true;
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_electric_spawner_price();
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			this.send_message(player, "已扣除" + price);
			return true;
		} else {
			this.send_message(player, "建立刷怪机所需的钱不够，需要" + price);
			return false;
		}
	}

	@Override
	protected void init_after_set_location() {
		// TODO 自动生成的方法存根

	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return null;
	}

	@Nonnull
	public static Electric_spawner deserialize(@Nonnull Map<String, Object> args) {
		Electric_spawner structure = new Electric_spawner();
		structure.set_from_save(args);
		return structure;
	}
}
