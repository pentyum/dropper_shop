package com.piggest.minecraft.bukkit.wrench;

import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;

public class Wrench_command_executor implements CommandExecutor {
	private Use_wrench_listener item_listener = new Use_wrench_listener(this);
	private final NamespacedKey namespace = Dropper_shop_plugin.get_key("wrench");
	private ConfigurationSection price = null;

	public boolean is_wrench(ItemStack item) {
		if (item == null) {
			return false;
		}
		String ext_id = Material_ext.get_id_name(item);
		if (ext_id == null) {
			return false;
		}
		return ext_id.equals("wrench");
	}

	public void init_wrench_item() {
		ItemStack item = new ItemStack(Material.IRON_PICKAXE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r扳手");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7右键方块使方块");
		lore.add("§7正面转至该面");
		lore.add("§7(潜行状态下相反)");
		meta.setLore(lore);
		meta.setCustomModelData(Dropper_shop_plugin.custom_model_data_offset + 1);
		item.setItemMeta(meta);
		Material_ext.register("wrench", item);
	}

	private void set_recipe() {
		ShapedRecipe sr1 = new ShapedRecipe(this.namespace, Material_ext.new_item("wrench", 1));
		sr1.shape("i i", "iii", " i ");
		sr1.setIngredient('i', Material.IRON_INGOT);
		Dropper_shop_plugin.instance.add_recipe(sr1);
		Dropper_shop_plugin.instance.getLogger().info("[扳手]扳手合成表已添加");
	}

	public boolean use_eco(Player player) {
		String world_name = player.getWorld().getName();
		int world_price = 0;
		if (price.getKeys(false).contains(world_name)) {
			world_price = price.getInt(world_name);
		} else {
			world_price = price.getInt("other");
		}

		if (Dropper_shop_plugin.instance.get_economy().has(player, world_price)) {
			Dropper_shop_plugin.instance.get_economy().withdrawPlayer(player, world_price);
			player.sendMessage("[扳手]已扣除" + world_price);
			return true;
		} else {
			player.sendMessage("[扳手]你的金钱不够");
			return false;
		}
	}

	public void init() {
		price = Dropper_shop_plugin.instance.get_config().getConfigurationSection("wrench-price");
		init_wrench_item();
		PluginManager pm = Dropper_shop_plugin.instance.getServer().getPluginManager();
		pm.registerEvents(item_listener, Dropper_shop_plugin.instance);
		set_recipe();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("wrench")) {
			if (args.length == 0) { // 没有参数
				if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
					sender.sendMessage("[扳手]这个指令只能让玩家使用。");
				} else {
					sender.sendMessage("[扳手]合成扳手后右键使用(潜行状态相反)\n/wrench setprice <世界名称> <价格>\n/wrench get 获得一个扳手");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("setprice")) { // 设置价格
				int newprice = 0;
				String world_name = "world";
				if (args.length == 3) {
					world_name = args[1];
				} else if (args.length == 2) {
					if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
						sender.sendMessage("[扳手]在控制台使用该命令必须指定世界名称");
						return true;
					} else {
						Player player = (Player) sender;
						world_name = player.getWorld().getName();
					}
				}
				if (sender.hasPermission("wrench.changeprice")) {
					try {
						newprice = Integer.parseInt(args[args.length - 1]);
					} catch (Exception e) {
						sender.sendMessage("[扳手]请输入整数");
						return true;
					}
					price.set(world_name, newprice);
					Dropper_shop_plugin.instance.get_config().set("wrench-price", price);
					Dropper_shop_plugin.instance.saveConfig();
					sender.sendMessage("[扳手]价格设置成功");
				} else {
					sender.sendMessage("[扳手]你没有权限设置价格");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("get")) { // 获得一个扳手
				if (sender.hasPermission("wrench.changeprice")) {
					if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
						sender.sendMessage("[扳手]只有玩家才能获得扳手");
					} else {
						Player player = (Player) sender;
						player.getInventory().addItem(Material_ext.new_item("wrench", 1));
					}
				} else {
					sender.sendMessage("[扳手]你没有权限直接获得扳手");
				}
			}
		}
		return false;
	}
}
