package com.piggest.minecraft.bukkit.lottery_pool;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
		if (this.completed() == 0) {
			this.get_manager().remove(this);
			return;
		}
		if (this.running == false) {
			this.start_lottery(player);
		}
		return;
	}

	@Override
	public int completed() {
		if (this.get_block(0, 0, 0).getType() != Material.DIAMOND_BLOCK) {
			return 0;
		}
		if (this.get_block(6, 0, 0).getType() != Material.OBSIDIAN) {
			return 0;
		}
		if (this.get_block(-6, 0, 0).getType() != Material.OBSIDIAN) {
			return 0;
		}
		if (this.get_block(0, 0, 6).getType() != Material.OBSIDIAN) {
			return 0;
		}
		if (this.get_block(0, 0, -6).getType() != Material.OBSIDIAN) {
			return 0;
		}
		if (this.get_block(4, 0, 4).getType() != Material.OBSIDIAN) {
			return 0;
		}
		if (this.get_block(-4, 0, 4).getType() != Material.OBSIDIAN) {
			return 0;
		}
		if (this.get_block(4, 0, -4).getType() != Material.OBSIDIAN) {
			return 0;
		}
		if (this.get_block(-4, 0, -4).getType() != Material.OBSIDIAN) {
			return 0;
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
		Dropper_shop_plugin.instance.getLogger().info("末影水晶数量" + crystals.size());
		if (crystals.size() != 8) {
			return 0;
		} else {
			int i = 0;
			for (Entity crystal : crystals) {
				this.ender_crystal_list[i] = (EnderCrystal) crystal;
				i++;
			}
		}
		Dropper_shop_plugin.instance.getLogger().info("检测通过");
		return 1;
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
		int price = Dropper_shop_plugin.instance.get_make_lottery_pool_price();
		if (Dropper_shop_plugin.instance.get_economy().has(player, price)) {
			Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player, price);
			player.sendMessage("已扣除" + price);
			return true;
		} else {
			player.sendMessage("建立抽奖机的钱不够，需要" + price);
			return false;
		}
	}

	public boolean use_condition(Player player) {
		int price = Dropper_shop_plugin.instance.get_lottery_price();
		if (Dropper_shop_plugin.instance.get_economy().has(player, price)) {
			Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player, price);
			player.sendMessage("已扣除" + price);
			return true;
		} else {
			player.sendMessage("抽奖钱不够，需要" + price);
			return false;
		}
	}

	public void start_lottery(Player player) {
		for (EnderCrystal ender_crystal : ender_crystal_list) {
			if (ender_crystal == null) {
				player.sendMessage("末影水晶结构错误");
				return;
			}
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
		ItemStack[] pool = new ItemStack[100];
		World world = this.get_location().getWorld();
		for (EnderCrystal ender_crystal : ender_crystal_list) { // 设置特效光柱
			ender_crystal.setBeamTarget(null);
		}
		world.spawnParticle(Particle.EXPLOSION_HUGE, this.get_location(), 1);
		Set<String> item_types = config.getKeys(false);
		int i = 0, j = 0;
		for (String item_name : item_types) {
			List<Integer> item_info = config.getIntegerList(item_name);
			for (j = 0; j < item_info.get(1) && i < 100; j++) {
				pool[i] = Material_ext.new_item(item_name, item_info.get(0));
				i++;
			}
		}
		Random rand = new Random();
		int num = rand.nextInt(100);
		ItemStack item = pool[num];
		if (item != null) {
			world.spawnParticle(Particle.VILLAGER_HAPPY, this.get_location(), 50, 1, 1, 1);
			player.sendMessage("恭喜你抽到了" + item.getAmount() + "个" + Material_ext.get_display_name(item));
			world.dropItem(this.get_location().add(0, 1, 0), item);
		} else {
			player.sendMessage("很遗憾你没有抽到任何物品");
		}
		this.running = false;
	}
}
