package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.config.Map_config;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Color_utils;
import com.piggest.minecraft.bukkit.utils.Tab_list;

public class Custom_map_command_executor implements TabExecutor {

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args[0].equalsIgnoreCase("get_map")) {
			if (args.length == 2 || args.length == 5) {
				return Tab_list.color_list;
			} else if (args.length == 4) {
				Set<String> fonts_set = Dropper_shop_plugin.instance.get_fonts_manager().get_all_name();
				ArrayList<String> fonts_list = new ArrayList<String>(fonts_set);
				return Tab_list.contains(fonts_list, args[3]);
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (args[0].equalsIgnoreCase("get_map")) {
			if (args.length < 5) {
				return true;
			}
			String background_color_string = args[1];
			String font_color_string = args[4];
			Color background_color = Color_utils.string_color_map.get(background_color_string);
			Color font_color = Color_utils.string_color_map.get(font_color_string);
			int font_size = 100;
			for (int i = 0; i < args[2].length(); i++) {
				char c = args[2].charAt(i);
				ItemStack item = new ItemStack(Material.FILLED_MAP);
				ItemMeta meta = item.getItemMeta();
				MapMeta mapmeta = (MapMeta) meta;
				MapView mapview = Bukkit.getServer().createMap(player.getWorld());
				List<MapRenderer> renders = mapview.getRenderers();
				for (MapRenderer render : renders) {
					mapview.removeRenderer(render);
				}
				Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(args[3]);
				Character_map_render render = new Character_map_render(background_color, c, font, font_size,
						font_color);
				mapview.addRenderer(render);
				mapmeta.setMapView(mapview);
				mapmeta.setDisplayName(String.valueOf(c));
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(String.format("§r背景颜色: (%d,%d,%d)", background_color.getRed(), background_color.getGreen(),
						background_color.getBlue()));
				lore.add("§r字体: " + font.getFontName(Locale.SIMPLIFIED_CHINESE));
				lore.add("§r字号: " + font_size);
				lore.add(String.format("§r文字颜色: (%d,%d,%d)", font_color.getRed(), font_color.getGreen(),
						font_color.getBlue()));
				lore.add("§r部分: " + 1);
				mapmeta.setLore(lore);
				item.setItemMeta(meta);
				player.getInventory().addItem(item);
				Map_config map_config = Dropper_shop_plugin.instance.get_map_config();
				map_config.get_config().set("map_" + (mapview.getId()), render);
			}
			player.sendMessage("成功获得\"" + args[2] + "\"");
			return true;
		}
		return false;
	}

}
