package com.piggest.minecraft.bukkit.trees_felling_machine;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.HasRunner;
import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Trees_felling_machine extends Multi_block_with_gui implements HasRunner {
	private int current_x;
	private int current_y;
	private int current_z;
	private int r = 48;
	private Trees_felling_machine_runner runner = new Trees_felling_machine_runner(this);

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
					if (Math.abs(x) == 1 && Math.abs(y) == 1 && Math.abs(z) == 1
							&& material != Material.CHISELED_QUARTZ_BLOCK) {
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

	@Override
	public Structure_runner[] get_runner() {
		return new Structure_runner[] { this.runner };
	}

	public Location get_current_pointer_location() {
		return new Location(this.get_location().getWorld(), this.current_x, this.current_y, this.current_z);
	}

	private Location pointer_move_to_next() {
		// to do
		return this.get_current_pointer_location();
	}

	public void do_next() {
		ItemStack axe = this.get_axe();
		if (axe == null) {
			return;
		}
		Material current_material = Trees_felling_machine.in_tree(this.get_current_pointer_location());
		if (current_material != null) {
			this.get_current_pointer_location().getBlock().setType(Material.AIR);
			this.use_axe();
			// to do
		}
		this.pointer_move_to_next();
	}

	public ItemStack get_axe() {
		ItemStack item = this.gui.getItem(13);
		if (item == null) {
			return null;
		}
		Material type = item.getType();
		if (type == Material.DIAMOND_AXE || type == Material.GOLDEN_AXE || type == Material.IRON_AXE
				|| type == Material.STONE_AXE || type == Material.WOODEN_AXE) {
			return item;
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	public void use_axe() {
		ItemStack axe = this.get_axe();
		int durability_level = axe.getEnchantmentLevel(Enchantment.DURABILITY);
		Random rand = new Random();
		int num = rand.nextInt(durability_level + 1);
		if (num == 0) {
			axe.setDurability((short) (axe.getDurability() + 1));
		}
	}

	public static Material in_tree(Location location) {
		Block block = location.getBlock();
		Material material = block.getType();
		if (material != Material.OAK_LOG && material != Material.SPRUCE_LOG && material != Material.BIRCH_LOG
				&& material != Material.JUNGLE_LOG && material != Material.ACACIA_LOG
				&& material != Material.DARK_OAK_LOG) {
			return null;
		} else {
			for (int y = location.getBlockY(); y < 256 && y < location.getBlockY() + 40; y++) {
				Block find_block = location.getWorld().getBlockAt(location.getBlockX(), y, location.getBlockZ());
				Material find_material = find_block.getType();
				if (find_material == Material.OAK_LEAVES || find_material == Material.SPRUCE_LEAVES
						|| find_material == Material.BIRCH_LEAVES || find_material == Material.JUNGLE_LEAVES
						|| find_material == Material.ACACIA_LEAVES || find_material == Material.DARK_OAK_LEAVES) {
					return material;
				}
			}
			return null;
		}
	}

	@Override
	public void set_from_save(Map<?, ?> shop_save) {
		super.set_from_save(shop_save);
		this.current_x = ((int) shop_save.get("current-x"));
		this.current_y = ((int) shop_save.get("current-y"));
		this.current_z = ((int) shop_save.get("current-z"));
	}

	@Override
	public HashMap<String, Object> get_save() {
		HashMap<String, Object> save = super.get_save();
		save.put("current-x", this.current_x);
		save.put("current-y", this.current_y);
		save.put("current-z", this.current_z);
		return save;
	}
}
