package com.piggest.minecraft.bukkit.flying_item;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class Flying_item implements TabExecutor {
	public final static NamespacedKey flyting_time_namespacedkey = new NamespacedKey(Dropper_shop_plugin.instance,
			"flying_time");

	public static void init() {
		// NamespacedKey flying_item_key = new
		// NamespacedKey(Dropper_shop_plugin.instance, "flying_item");
		// Dropper_shop_plugin.instance.getLogger().info("开始初始化飞行道具");
		// Material_ext.register(flying_item_key, new
		// ItemStack(Material.FIREWORK_ROCKET));
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("flying_item")) {
			if (args.length == 0) {
				player.sendMessage("");
				return true;
			}
			if (args[0].equalsIgnoreCase("get")) {
				if (!player.hasPermission("flying_item.get")) {
					player.sendMessage("你没有直接获得飞行道具的权限");
					return true;
				}
				int time = 300;
				try {
					time = Integer.parseInt(args[1]);
				} catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
					player.sendMessage("没有填写时间，自动设为" + time);
				}
				ItemStack item = new ItemStack(Material.FIREWORK_ROCKET);
				ItemMeta meta = item.getItemMeta();
				PersistentDataContainer container = meta.getPersistentDataContainer();
				container.set(flyting_time_namespacedkey, PersistentDataType.INTEGER, time);
				ChatColor color = ChatColor.RESET;
				try {
					color = ChatColor.valueOf(args[2]);
				} catch (ArrayIndexOutOfBoundsException | IllegalArgumentException ignored) {
				}
				meta.setDisplayName(color + String.valueOf(time) + "秒飞行火箭");
				item.setItemMeta(meta);
				player.getInventory().addItem(item);
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return null;
	}
}
