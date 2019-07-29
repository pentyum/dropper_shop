package com.piggest.minecraft.bukkit.lottery_pool;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;

public class Lottery_pool extends Multi_block_structure {
	Lottery_pool_timer timer = null;
	EnderCrystal[] ender_crystal_list = new EnderCrystal[8];
	boolean running = false;

	@Override
	public void on_right_click(Player player) {
		if (this.completed() == false) {
			this.remove();
			return;
		}
		if (this.running == false) {
			if (!player.hasPermission("lottery.use")) {
				player.sendMessage("[抽奖机]你没有进行抽奖的权限");
				return;
			}
			this.start_lottery(player);
		}
		return;
	}

	@Override
	public boolean completed() {
		if (this.get_block(0, 0, 0).getType() != Material.DIAMOND_BLOCK) {
			return false;
		}
		if (this.get_block(6, 0, 0).getType() != Material.OBSIDIAN) {
			return false;
		}
		if (this.get_block(-6, 0, 0).getType() != Material.OBSIDIAN) {
			return false;
		}
		if (this.get_block(0, 0, 6).getType() != Material.OBSIDIAN) {
			return false;
		}
		if (this.get_block(0, 0, -6).getType() != Material.OBSIDIAN) {
			return false;
		}
		if (this.get_block(4, 0, 4).getType() != Material.OBSIDIAN) {
			return false;
		}
		if (this.get_block(-4, 0, 4).getType() != Material.OBSIDIAN) {
			return false;
		}
		if (this.get_block(4, 0, -4).getType() != Material.OBSIDIAN) {
			return false;
		}
		if (this.get_block(-4, 0, -4).getType() != Material.OBSIDIAN) {
			return false;
		}
		World world = this.get_location().getWorld();
		Predicate<Entity> predicate = new Predicate<Entity>() {
			@Override
			public boolean test(Entity entity) {
				if (entity.getType() == EntityType.ENDER_CRYSTAL) {
					return true;
				}
				return false;
			}
		};
		Collection<Entity> crystals = world.getNearbyEntities(this.get_location(), 6, 2, 6, predicate);
		//Dropper_shop_plugin.instance.getLogger().info("末影水晶数量" + crystals.size());
		if (crystals.size() != 8) {
			return false;
		} else {
			int i = 0;
			for (Entity crystal : crystals) {
				this.ender_crystal_list[i] = (EnderCrystal) crystal;
				i++;
			}
		}
		//Dropper_shop_plugin.instance.getLogger().info("检测通过");
		return true;
	}

	@Override
	public boolean in_structure(Location loc) {
		if (loc.equals(this.get_location())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected boolean on_break(Player player) {
		return true;
	}

	@Override
	public boolean create_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_make_lottery_pool_price();
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			player.sendMessage("[抽奖机]已扣除" + price);
			return true;
		} else {
			player.sendMessage("[抽奖机]建立抽奖机所需的钱不够，需要" + price);
			return false;
		}
	}

	public boolean use_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_price_config().get_lottery_price();
		if (Dropper_shop_plugin.instance.cost_player_money(price, player)) {
			player.sendMessage("[抽奖机]已扣除" + price);
			return true;
		} else {
			player.sendMessage("[抽奖机]抽奖钱不够，需要" + price);
			return false;
		}
	}

	public void start_lottery(Player player) {
		for (EnderCrystal ender_crystal : ender_crystal_list) {
			if (ender_crystal == null) {
				player.sendMessage("[抽奖机]末影水晶结构错误");
				return;
			}
		}
		if (this.running == true) {
			player.sendMessage("[抽奖机]抽奖机已经在运行了");
			return;
		}
		if (!this.use_condition(player)) {
			return;
		}
		for (EnderCrystal ender_crystal : ender_crystal_list) { // 设置特效光柱
			ender_crystal.setBeamTarget(this.get_location().add(0, -1, 0));
		}
		this.timer = new Lottery_pool_timer(this, player);
		this.timer.runTaskLater(Dropper_shop_plugin.instance, 20 * 5);
		this.running = true;
	}

	public void end_lottery(Player player) {
		FileConfiguration config = Dropper_shop_plugin.instance.get_lottery_config();
		World world = this.get_location().getWorld();
		for (EnderCrystal ender_crystal : ender_crystal_list) { // 设置特效光柱
			ender_crystal.setBeamTarget(null);
		}
		world.spawnParticle(Particle.EXPLOSION_HUGE, this.get_location(), 1);
		@SuppressWarnings("unchecked")
		List<ItemStack> item_list = (List<ItemStack>) config.getList("pool");
		List<Integer> possibility_list = config.getIntegerList("possibility");
		List<Boolean> broadcast_list = config.getBooleanList("broadcast");
		int i = 0, j = 0, k = 0;
		int[] pool = new int[1000];
		for (k = 0; k < possibility_list.size(); k++) {
			for (j = 0; j < possibility_list.get(k) && i < 1000; j++) {
				pool[i] = k;
				i++;
			}
		}
		Random rand = new Random();
		int num = rand.nextInt(1000);
		if (num < i) {
			ItemStack item = item_list.get(pool[num]).clone();
			world.spawnParticle(Particle.VILLAGER_HAPPY, this.get_location(), 50, 1, 1, 1);
			if (player != null) {
				player.sendMessage("[抽奖机]恭喜你抽到了" + item.getAmount() + "个" + Material_ext.get_display_name(item));
			}
			world.dropItem(this.get_location().add(0, 1, 0), item);
			if (broadcast_list.get(pool[num]) == true) {
				Dropper_shop_plugin.instance.getServer().broadcastMessage(
						"[抽奖机]恭喜" + player.getName() + "抽到了" + item.getAmount() + "个" + Material_ext.get_display_name(item));
			}
		} else {
			if (player != null) {
				player.sendMessage("[抽奖机]很遗憾你没有抽到任何物品");
			}
		}
		this.running = false;
	}

	@Override
	public void init_after_set_location() {
		return;
	}
}
