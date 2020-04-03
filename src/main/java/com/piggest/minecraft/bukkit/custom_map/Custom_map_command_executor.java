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
		if (args[0].equalsIgnoreCase("get_map") || args[0].equalsIgnoreCase("get_clock")) {
			if (args.length == 2 || args.length == 6) {
				return Tab_list.color_list;
			} else if (args.length == 4) {
				Set<String> fonts_set = Dropper_shop_plugin.instance.get_fonts_manager().get_all_name();
				ArrayList<String> fonts_list = new ArrayList<String>(fonts_set);
				return Tab_list.contains(fonts_list, args[3]);
			} else if (args.length == 3 && args[0].equalsIgnoreCase("get_clock")) {
				return Tab_list.time_format;
			}
		}
		return null;
	}

	public static ItemStack[] generate_maps(Player player, Color background_color, char c, Font font, int font_size,
			Color font_color) {
		int side_amount = Character_section_map_render.get_side_amount(font_size);
		int map_amount = side_amount * side_amount;
		ItemStack[] maps = new ItemStack[map_amount];
		for (int i = 0; i < map_amount; i++) {
			ItemStack item = new ItemStack(Material.FILLED_MAP);
			ItemMeta meta = item.getItemMeta();
			MapMeta mapmeta = (MapMeta) meta;
			MapView mapview = Bukkit.getServer().createMap(player.getWorld());
			List<MapRenderer> renders = mapview.getRenderers();
			for (MapRenderer render : renders) {
				mapview.removeRenderer(render);
			}
			Character_map_render render = new Character_section_map_render(background_color, c, font, font_size,
					font_color, i);
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
			int y = (i / side_amount);
			int x = (i % side_amount);
			if (map_amount > 1) {
				lore.add(String.format("§r部分: (%d,%d)", x, y));
				lore.add("§r共 " + map_amount + " 张");
			}
			mapmeta.setLore(lore);
			item.setItemMeta(meta);
			Map_config map_config = Dropper_shop_plugin.instance.get_map_config();
			map_config.get_config().set("map_" + (mapview.getId()), render);
			maps[i] = item;
		}
		return maps;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (args[0].equalsIgnoreCase("get_map")) {
			if (args.length < 6) {
				player.sendMessage("/custom_map get_map <背景色> <文字内容> <字体> <字号> <文字颜色>。一张图为128*128");
				return true;
			}
			String background_color_string = args[1];
			Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(args[3]);
			int font_size = 100;
			try {
				font_size = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				player.sendMessage("字号格式错误，已设置为100");
			}
			String font_color_string = args[5];
			Color background_color = Color_utils.string_color_map.get(background_color_string);
			Color font_color = Color_utils.string_color_map.get(font_color_string);

			int total = 0;
			for (int i = 0; i < args[2].length(); i++) {
				char c = args[2].charAt(i);
				ItemStack[] maps = generate_maps(player, background_color, c, font, font_size, font_color);
				for (ItemStack map : maps) {
					player.getInventory().addItem(map);
					total++;
				}
			}
			player.sendMessage("成功获得\"" + args[2] + "\"，共" + total + "张图");
			return true;
		} else if (args[0].equalsIgnoreCase("get_clock")) {
			if (args.length < 6) {
				player.sendMessage("/custom_map get_clock <背景色> <格式> <字体> <字号> <文字颜色>。");
				return true;
			}
			String background_color_string = args[1];
			Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(args[3]);
			int font_size = 28;
			try {
				font_size = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				player.sendMessage("字号格式错误，已设置为28");
			}
			String font_color_string = args[5];
			Color background_color = Color_utils.string_color_map.get(background_color_string);
			Color font_color = Color_utils.string_color_map.get(font_color_string);

			ItemStack item = new ItemStack(Material.FILLED_MAP);
			ItemMeta meta = item.getItemMeta();
			MapMeta mapmeta = (MapMeta) meta;
			MapView mapview = Bukkit.getServer().createMap(player.getWorld());
			List<MapRenderer> renders = mapview.getRenderers();
			for (MapRenderer render : renders) {
				mapview.removeRenderer(render);
			}
			Clock_map_render render = new Clock_map_render(background_color, args[2], font, font_size, font_color);
			mapview.addRenderer(render);
			mapmeta.setMapView(mapview);
			mapmeta.setDisplayName("时钟");
			ArrayList<String> lore = new ArrayList<String>();
			lore.add(String.format("§r背景颜色: (%d,%d,%d)", background_color.getRed(), background_color.getGreen(),
					background_color.getBlue()));
			lore.add(String.format("§r格式: %s", args[2]));
			lore.add("§r字体: " + font.getFontName(Locale.SIMPLIFIED_CHINESE));
			lore.add("§r字号: " + font_size);
			lore.add(String.format("§r文字颜色: (%d,%d,%d)", font_color.getRed(), font_color.getGreen(),
					font_color.getBlue()));
			mapmeta.setLore(lore);
			item.setItemMeta(meta);
			Map_config map_config = Dropper_shop_plugin.instance.get_map_config();
			map_config.get_config().set("map_" + (mapview.getId()), render);
			player.getInventory().addItem(item);
		}
		return false;
	}

}
