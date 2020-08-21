package com.piggest.minecraft.bukkit.custom_map;

import com.google.zxing.WriterException;
import com.piggest.minecraft.bukkit.config.Map_config;
import com.piggest.minecraft.bukkit.config.Screen_config;
import com.piggest.minecraft.bukkit.custom_map.clock.Analog_clock_background_map_render;
import com.piggest.minecraft.bukkit.custom_map.clock.Analog_clock_screen;
import com.piggest.minecraft.bukkit.custom_map.clock.Digital_clock_screen;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Color_utils;
import com.piggest.minecraft.bukkit.utils.Inventory_io;
import com.piggest.minecraft.bukkit.utils.Tab_list;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Custom_map_command_executor implements TabExecutor {
	static Map_config map_config = Dropper_shop_plugin.instance.get_map_config();
	static Screen_config screen_config = Dropper_shop_plugin.instance.get_screen_config();

	private static final ArrayList<String> sub_cmd = new ArrayList<>() {
		private static final long serialVersionUID = -6116323601639805386L;

		{
			add("get_char");
			add("get_digital_clock");
			add("get_analog_clock");
			add("get_cdm");
			add("set_cdm");
			add("get_image");
			add("get_gif");
			add("get_qr_code");
			add("get_rolling_subtitle");
			add("get_stock_subtitle");
			add("reload");
		}
	};
	private static final ArrayList<String> size_list = new ArrayList<>() {
		private static final long serialVersionUID = -4372329620166289408L;

		{
			add("18");
			add("20");
			add("22");
			add("24");
			add("26");
			add("28");
			add("32");
			add("36");
			add("48");
			add("60");
			add("72");
			add("86");
			add("100");
			add("120");
			add("150");
			add("200");
		}
	};
	private static final ArrayList<String> n_list = new ArrayList<>() {
		private static final long serialVersionUID = 3906690741866082030L;

		{
			add("1");
			add("2");
			add("3");
			add("4");
			add("5");
		}
	};

	@Override
	public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, String[] args) {
		if (args.length == 1) {
			return Tab_list.contains(sub_cmd, args[0]);
		}
		if (args[0].equalsIgnoreCase("get_char") || args[0].equalsIgnoreCase("get_digital_clock")
				|| args[0].equalsIgnoreCase("get_analog_clock") || args[0].equalsIgnoreCase("get_rolling_subtitle")) {
			if (args.length == 2 || args.length == 6) { //颜色
				return Tab_list.color_list;
			} else if (args.length == 4) { //字体
				Set<String> fonts_set = Dropper_shop_plugin.instance.get_fonts_manager().get_all_name();
				ArrayList<String> fonts_list = new ArrayList<>(fonts_set);
				return Tab_list.contains(fonts_list, args[3]);
			} else if (args.length == 3) { //内容
				if (args[0].equalsIgnoreCase("get_digital_clock")) {
					return Tab_list.time_format;
				} else if (args[0].equalsIgnoreCase("get_analog_clock")) {
					return null;
				}
			} else if (args.length == 5) { //字号
				return size_list;
			} else if (args.length == 7) { //世界名称
				if (args[0].equalsIgnoreCase("get_digital_clock") || args[0].equalsIgnoreCase("get_analog_clock")) {
					return Tab_list.world_name_list;
				}
			}
		} else if (args[0].equalsIgnoreCase("get_stock_subtitle")) {
			if (args.length == 3) { //字体
				Set<String> fonts_set = Dropper_shop_plugin.instance.get_fonts_manager().get_all_name();
				ArrayList<String> fonts_list = new ArrayList<>(fonts_set);
				return Tab_list.contains(fonts_list, args[2]);
			} else if (args.length == 4) { //字号
				return size_list;
			}
		} else if (args[0].equalsIgnoreCase("set_cdm")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				try {
					ItemStack item = player.getInventory().getItemInMainHand();
					MapMeta meta = (MapMeta) item.getItemMeta();
					int id = meta.getMapView().getId();
					ArrayList<String> id_list = new ArrayList<>();
					id_list.add(String.valueOf(id));
					return id_list;
				} catch (Exception e) {
					return null;
				}
			}
		} else if (args[0].equalsIgnoreCase("get_image") || args[0].equalsIgnoreCase("get_gif")) {
			if (sender instanceof Player) {
				if (args.length == 2) {
					File font_folder = new File(Dropper_shop_plugin.instance.getDataFolder(), "images");
					ArrayList<String> file_name_list = new ArrayList<>();
					for (File file : font_folder.listFiles()) {
						file_name_list.add(file.getName());
					}
					return file_name_list;
				} else if (args.length == 3) {
					return n_list;
				} else if (args.length == 4) {
					return Tab_list.true_false_list;
				}
			}
		}
		return null;
	}

	public static ItemStack[] generate_char_maps(Player player, Color background_color, char c, Font font,
												 int font_size, Color font_color) {
		Background_map_render background = new Background_map_render(background_color);
		Character_screen screen = new Character_screen(background, c, font, font_size, font_color);
		Screen_map_render[] renders = screen.generate_renders();
		ItemStack[] maps = new ItemStack[renders.length];
		for (int i = 0; i < renders.length; i++) {
			Screen_map_render render = renders[i];
			ItemStack item = new ItemStack(Material.FILLED_MAP);
			ItemMeta meta = item.getItemMeta();
			MapMeta mapmeta = (MapMeta) meta;
			MapView mapview = map_config.create_new_map(player.getWorld(), render, null);
			mapmeta.setMapView(mapview);
			mapmeta.setDisplayName(String.valueOf(c));
			ArrayList<String> lore = new ArrayList<>();
			lore.add(String.format("§r背景颜色: (%d,%d,%d)", background_color.getRed(), background_color.getGreen(),
					background_color.getBlue()));
			lore.add("§r字体: " + font.getFontName(Locale.SIMPLIFIED_CHINESE));
			lore.add("§r字号: " + font_size);
			lore.add(String.format("§r文字颜色: (%d,%d,%d)", font_color.getRed(), font_color.getGreen(),
					font_color.getBlue()));
			if (renders.length > 1) {
				int y = render.get_y();
				int x = render.get_x();
				lore.add(String.format("§r部分: (%d,%d)", x, y));
				lore.add("§r共 " + renders.length + " 张");
			}
			mapmeta.setLore(lore);
			item.setItemMeta(meta);
			maps[i] = item;
		}
		return maps;
	}

	public static ItemStack generate_digital_clock_map(Player player, Color background_color, String format, Font font,
													   int font_size, Color font_color, String world_name) {
		ItemStack item = new ItemStack(Material.FILLED_MAP);
		ItemMeta meta = item.getItemMeta();
		MapMeta mapmeta = (MapMeta) meta;
		Background_map_render background = new Background_map_render(background_color);
		Digital_clock_screen screen = new Digital_clock_screen(background, format, font, font_size, font_color,
				world_name, 1, 1);
		Screen_map_render render = screen.generate_renders()[0];
		MapView mapview = map_config.create_new_map(player.getWorld(), render, null);
		mapmeta.setMapView(mapview);
		mapmeta.setDisplayName("时钟");
		ArrayList<String> lore = new ArrayList<>();
		lore.add(String.format("§r背景颜色: (%d,%d,%d)", background_color.getRed(), background_color.getGreen(),
				background_color.getBlue()));
		lore.add(String.format("§r格式: %s", format));
		lore.add("§r字体: " + font.getFontName(Locale.SIMPLIFIED_CHINESE));
		lore.add("§r字号: " + font_size);
		lore.add(String.format("§r文字颜色: (%d,%d,%d)", font_color.getRed(), font_color.getGreen(), font_color.getBlue()));
		lore.add(String.format("§r世界: %s", world_name == null ? "真实世界" : world_name));
		mapmeta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack[] generate_analog_clock_maps(Player player, Color background_color, String style, Font font,
														 int font_size, Color font_color, String world_name) {
		Analog_clock_background_map_render background = new Analog_clock_background_map_render(background_color,
				font_color, font_size);
		Analog_clock_screen screen = new Analog_clock_screen(background, style, font, font_size,
				font_color, world_name);
		Screen_map_render[] renders = screen.generate_renders();
		ItemStack[] maps = new ItemStack[renders.length];
		for (int i = 0; i < renders.length; i++) {
			Screen_map_render render = renders[i];
			ItemStack item = new ItemStack(Material.FILLED_MAP);
			ItemMeta meta = item.getItemMeta();
			MapMeta mapmeta = (MapMeta) meta;
			MapView mapview = map_config.create_new_map(player.getWorld(), render, null);
			mapmeta.setMapView(mapview);
			mapmeta.setDisplayName("时钟");
			ArrayList<String> lore = new ArrayList<>();
			lore.add(String.format("§r背景颜色: (%d,%d,%d)", background_color.getRed(), background_color.getGreen(),
					background_color.getBlue()));
			lore.add(String.format("§r样式: %s", style));
			lore.add("§r字体: " + font.getFontName(Locale.SIMPLIFIED_CHINESE));
			lore.add("§r字号: " + font_size);
			lore.add(String.format("§r文字颜色: (%d,%d,%d)", font_color.getRed(), font_color.getGreen(),
					font_color.getBlue()));
			lore.add(String.format("§r世界: %s", world_name == null ? "真实世界" : world_name));
			if (renders.length > 1) {
				int y = render.get_y();
				int x = render.get_x();
				lore.add(String.format("§r部分: (%d,%d)", x, y));
				lore.add("§r共 " + renders.length + " 张");
			}
			mapmeta.setLore(lore);
			item.setItemMeta(meta);
			maps[i] = item;
		}
		return maps;
	}

	public static ItemStack generate_qr_code_map(Player player, @Nullable String title, String text, int margin)
			throws WriterException {
		ItemStack item = new ItemStack(Material.FILLED_MAP);
		ItemMeta meta = item.getItemMeta();
		MapMeta mapmeta = (MapMeta) meta;
		Qr_code_screen screen = new Qr_code_screen(text, margin);
		Screen_map_render render = screen.generate_renders()[0];
		MapView mapview = map_config.create_new_map(player.getWorld(), render, null);
		mapmeta.setMapView(mapview);
		if (title == null) {
			title = "二维码";
		}
		mapmeta.setDisplayName(title);
		ArrayList<String> lore = new ArrayList<>();
		String content = text.length() > 16 ? (text.substring(0, 16) + "...") : text;
		lore.add(String.format("§r内容: %s", content));
		lore.add(String.format("§r边框宽度: %d", margin));
		mapmeta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack[] generate_pic_maps(Player player, String pic_name, int n, boolean lock_width, boolean is_gif)
			throws IOException {
		Screen.Fill_type fill_type;
		int width_n = 0;
		int height_n = 0;
		if (lock_width == true) {
			fill_type = Screen.Fill_type.WIDTH;
			width_n = n;
		} else {
			fill_type = Screen.Fill_type.HEIGHT;
			height_n = n;
		}
		Screen screen;
		if (is_gif == true) {
			screen = new Gif_screen(pic_name, width_n, height_n, fill_type);
		} else {
			screen = new Local_image_screen(pic_name, width_n, height_n, fill_type);
		}
		Screen_map_render[] renders = screen.generate_renders();
		screen_config.add_screen(screen);
		ItemStack[] map_list = new ItemStack[renders.length];
		for (int i = 0; i < renders.length; i++) {
			Screen_map_render render = renders[i];
			ItemStack item = new ItemStack(Material.FILLED_MAP);
			ItemMeta meta = item.getItemMeta();
			MapMeta mapmeta = (MapMeta) meta;
			MapView mapview = map_config.create_new_map(player.getWorld(), render, null);
			mapmeta.setMapView(mapview);
			mapmeta.setDisplayName(pic_name);
			ArrayList<String> lore = new ArrayList<>();
			lore.add(String.format("§r文件名: %s", pic_name));
			if (is_gif == true) {
				lore.add(String.format("§r帧数: %d", ((Gif_screen) screen).get_total_frames()));
			}
			if (renders.length > 1) {
				int x = render.get_x();
				int y = render.get_y();
				lore.add(String.format("§r部分: (%d,%d)", x, y));
				lore.add("§r共 " + renders.length + " 张");
			}
			mapmeta.setLore(lore);
			item.setItemMeta(meta);
			map_list[i] = item;
		}
		return map_list;
	}

	public static ItemStack[] generate_rolling_subtitle_maps(Player player, Color background_color, String str, Font font,
															 int font_size, Color font_color, int length_n, float speed) {
		Background_map_render background = new Background_map_render(background_color);
		Rolling_subtitle_screen screen = new Rolling_subtitle_screen(background, str, font, font_size, font_color, length_n, speed);
		Screen_map_render[] renders = screen.generate_renders();
		ItemStack[] maps = new ItemStack[renders.length];
		for (int i = 0; i < renders.length; i++) {
			Screen_map_render render = renders[i];
			ItemStack item = new ItemStack(Material.FILLED_MAP);
			ItemMeta meta = item.getItemMeta();
			MapMeta mapmeta = (MapMeta) meta;
			MapView mapview = map_config.create_new_map(player.getWorld(), render, null);
			mapmeta.setMapView(mapview);
			mapmeta.setDisplayName("滚动字幕");
			ArrayList<String> lore = new ArrayList<>();
			lore.add(String.format("§r内容: %s", str));
			lore.add(String.format("§r速度: %.1f", screen.get_speed()));
			lore.add(String.format("§r背景颜色: (%d,%d,%d)", background_color.getRed(), background_color.getGreen(),
					background_color.getBlue()));
			lore.add("§r字体: " + font.getFontName(Locale.SIMPLIFIED_CHINESE));
			lore.add("§r字号: " + font_size);
			lore.add(String.format("§r文字颜色: (%d,%d,%d)", font_color.getRed(), font_color.getGreen(), font_color.getBlue()));

			if (renders.length > 1) {
				int x = render.get_x();
				int y = render.get_y();
				lore.add(String.format("§r部分: (%d,%d)", x, y));
				lore.add("§r共 " + renders.length + " 张");
			}
			mapmeta.setLore(lore);
			item.setItemMeta(meta);
			maps[i] = item;
		}
		return maps;
	}

	public static ItemStack[] generate_stock_subtitle_maps(Player player, String[] stock_ids, Font font, int font_size, int length_n, float speed) {
		Background_map_render background = new Background_map_render(Color.BLACK);
		Stock_subtitle_screen screen = new Stock_subtitle_screen(background, stock_ids, font, font_size, Color.YELLOW, length_n, speed);
		Screen_map_render[] renders = screen.generate_renders();
		ItemStack[] maps = new ItemStack[renders.length];
		for (int i = 0; i < renders.length; i++) {
			Screen_map_render render = renders[i];
			ItemStack item = new ItemStack(Material.FILLED_MAP);
			ItemMeta meta = item.getItemMeta();
			MapMeta mapmeta = (MapMeta) meta;
			MapView mapview = map_config.create_new_map(player.getWorld(), render, null);
			mapmeta.setMapView(mapview);
			mapmeta.setDisplayName("行情");
			ArrayList<String> lore = new ArrayList<>();
			//lore.add(String.format("§r内容: %s", str));
			lore.add(String.format("§r速度: %.1f", screen.get_speed()));
			lore.add("§r字体: " + font.getFontName(Locale.SIMPLIFIED_CHINESE));
			lore.add("§r字号: " + font_size);
			if (renders.length > 1) {
				int x = render.get_x();
				int y = render.get_y();
				lore.add(String.format("§r部分: (%d,%d)", x, y));
				lore.add("§r共 " + renders.length + " 张");
			}
			mapmeta.setLore(lore);
			item.setItemMeta(meta);
			maps[i] = item;
		}
		return maps;
	}

	public static void set_command_def_map(ItemStack item, Player player) {
		MapMeta meta = (MapMeta) item.getItemMeta();
		MapView map;
		if (meta.hasMapView()) {
			map = meta.getMapView();
			map_config.replace_render(map, new Command_map_render(), null);
		} else {
			map = map_config.create_new_map(player.getWorld(), new Command_map_render(), null);
		}
		meta.setMapView(map);
		ArrayList<String> lore = new ArrayList<>();
		lore.add("§r输入指令/custom_map set_cdm id X Y color可以设置地图像素");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		if (args[0].equalsIgnoreCase("get_char")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;
			if (args.length < 6) {
				player.sendMessage("/custom_map get_char <背景色> <文字内容> <字体> <字号> <文字颜色>。一张图为128*128");
				return true;
			}
			String background_color_string = args[1];
			Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(args[3]);
			int font_size = 100;
			try {
				font_size = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				player.sendMessage("字号格式错误，已设置为" + font_size);
			}
			String font_color_string = args[5];
			Color background_color = Color_utils.string_color_map.get(background_color_string);
			Color font_color = Color_utils.string_color_map.get(font_color_string);

			if (background_color == null) {
				player.sendMessage("背景颜色错误");
				return true;
			}
			if (font_color == null) {
				player.sendMessage("字体颜色错误");
				return true;
			}

			int total = 0;
			for (int i = 0; i < args[2].length(); i++) {
				char c = args[2].charAt(i);
				ItemStack[] maps = generate_char_maps(player, background_color, c, font, font_size, font_color);
				for (ItemStack map : maps) {
					total++;
				}
				Inventory_io.give_item_to_player(player, maps);
			}
			player.sendMessage("成功获得\"" + args[2] + "\"，共" + total + "张图");
			return true;
		} else if (args[0].equalsIgnoreCase("get_digital_clock")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;
			if (args.length < 6) {
				player.sendMessage("/custom_map get_digital_clock <背景色> <格式> <字体> <字号> <文字颜色> <世界名称(可选)>。");
				return true;
			}
			String background_color_string = args[1];
			Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(args[3]);
			int font_size = 28;
			try {
				font_size = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				player.sendMessage("字号格式错误，已设置为" + font_size);
			}
			String font_color_string = args[5];
			Color background_color = Color_utils.string_color_map.get(background_color_string);
			Color font_color = Color_utils.string_color_map.get(font_color_string);
			if (background_color == null) {
				player.sendMessage("背景颜色错误");
				return true;
			}
			if (font_color == null) {
				player.sendMessage("字体颜色错误");
				return true;
			}

			String world_name = null;
			if (args.length >= 7) {
				world_name = args[6];
			}
			ItemStack item = generate_digital_clock_map(player, background_color, args[2], font, font_size, font_color,
					world_name);
			Inventory_io.give_item_to_player(player, item);
			return true;
		} else if (args[0].equalsIgnoreCase("get_analog_clock")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;
			if (args.length < 6) {
				player.sendMessage("/custom_map get_analog_clock <背景色> <样式种类> <字体> <尺寸> <线条颜色> <世界名称(可选)>。");
				return true;
			}
			String background_color_string = args[1];
			Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(args[3]);
			int font_size = 128;
			try {
				font_size = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				player.sendMessage("尺寸格式错误，已设置为" + font_size);
			}
			String font_color_string = args[5];
			Color background_color = Color_utils.string_color_map.get(background_color_string);
			Color font_color = Color_utils.string_color_map.get(font_color_string);
			if (background_color == null) {
				player.sendMessage("背景颜色错误");
				return true;
			}
			if (font_color == null) {
				player.sendMessage("字体颜色错误");
				return true;
			}

			String world_name = null;
			if (args.length >= 7) {
				world_name = args[6];
			}
			ItemStack[] item = generate_analog_clock_maps(player, background_color, args[2], font, font_size,
					font_color, world_name);
			Inventory_io.give_item_to_player(player, item);
			return true;
		} else if (args[0].equalsIgnoreCase("get_cdm")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;
			ItemStack item = player.getInventory().getItemInMainHand();
			try {
				set_command_def_map(item, player);
			} catch (Exception e) {
				item = new ItemStack(Material.FILLED_MAP);
				set_command_def_map(item, player);
				Inventory_io.give_item_to_player(player, item);
			}
			return true;
		} else if (args[0].equalsIgnoreCase("set_cdm")) {
			if (args.length < 5) {
				sender.sendMessage("参数数量错误");
				return true;
			}
			int id = 0;
			int x;
			int y;
			byte color;
			try {
				id = Integer.parseInt(args[1]);
			} catch (NumberFormatException e) {
				sender.sendMessage("地图编号不是数字");
				return true;
			}
			MapRenderer content_render = map_config.get_content_from_map(id);
			if (content_render == null) {
				sender.sendMessage("地图不存在");
				return true;
			} else if (!(content_render instanceof Command_map_render)) {
				sender.sendMessage("该地图不是命令控制地图");
				return true;
			}
			Command_map_render cmr = (Command_map_render) content_render;
			try {
				x = Integer.parseInt(args[2]);
				y = Integer.parseInt(args[3]);
				color = Byte.parseByte(args[4]);
			} catch (NumberFormatException e) {
				sender.sendMessage("坐标或颜色不是数字");
				return true;
			}
			if (x >= Custom_map_render.pic_size || y >= Custom_map_render.pic_size || x < 0 || y < 0) {
				sender.sendMessage("坐标必须在0~127的范围内");
				return true;
			}
			cmr.set_pixel(x, y, color);
			return true;
		} else if (args[0].equalsIgnoreCase("get_image") | args[0].equalsIgnoreCase("get_gif")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;
			if (args.length < 4) {
				player.sendMessage("/custom_map get_image|get_gif <文件名> <格数> <锁定宽度>。锁定宽度表示图片宽度填满格数，否则为图片高度。");
				return true;
			}
			String file_name = args[1];
			int n = 1;
			boolean lock_width = true;
			try {
				n = Integer.parseInt(args[2]);
				lock_width = Boolean.parseBoolean(args[3]);
			} catch (Exception e) {
				player.sendMessage("格式错误");
				return true;
			}
			if (n > 5) {
				player.sendMessage("图片格数不能大于5");
				return true;
			}
			ItemStack[] item = null;
			try {
				if (args[0].equalsIgnoreCase("get_gif")) {
					item = generate_pic_maps(player, file_name, n, lock_width, true);
				} else {
					item = generate_pic_maps(player, file_name, n, lock_width, false);

				}
			} catch (IOException e) {
				player.sendMessage("文件错误" + e.toString());
				return true;
			}
			Inventory_io.give_item_to_player(player, item);
			return true;
		} else if (args[0].equalsIgnoreCase("get_qr_code")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;
			if (args.length < 2) {
				player.sendMessage("/custom_map get_qr_code <内容> <标题>");
				return true;
			}
			String text = args[1];
			String title = null;
			if (args.length >= 3) {
				title = args[2];
			}
			ItemStack item = null;
			try {
				item = generate_qr_code_map(player, title, text, 3);
			} catch (WriterException e) {
				player.sendMessage("二维码生成错误" + e.toString());
				return true;
			}
			Inventory_io.give_item_to_player(player, item);
			return true;
		} else if (args[0].equalsIgnoreCase("get_rolling_subtitle")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;

			if (args.length < 8) {
				player.sendMessage("/custom_map get_rolling_subtitle <背景色> <内容> <字体> <字号> <文字颜色> <长度> <移动速度>。");
				return true;
			}
			String background_color_string = args[1];
			Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(args[3]);
			int font_size = 28;
			try {
				font_size = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				player.sendMessage("字号格式错误，已设置为" + font_size);
			}
			String font_color_string = args[5];
			Color background_color = Color_utils.string_color_map.get(background_color_string);
			Color font_color = Color_utils.string_color_map.get(font_color_string);
			if (background_color == null) {
				player.sendMessage("背景颜色错误");
				return true;
			}
			if (font_color == null) {
				player.sendMessage("字体颜色错误");
				return true;
			}
			int length_n = 5;
			try {
				length_n = Integer.parseInt(args[6]);
			} catch (NumberFormatException e) {
				player.sendMessage("长度格式错误，已设置为" + length_n);
			}

			float speed = 5;
			try {
				speed = Float.parseFloat(args[7]);
			} catch (NumberFormatException e) {
				player.sendMessage("速度格式错误，已设置为" + speed);
			}

			ItemStack[] items = generate_rolling_subtitle_maps(player, background_color, args[2], font, font_size, font_color, length_n, speed);
			Inventory_io.give_item_to_player(player, items);
		} else if (args[0].equalsIgnoreCase("get_stock_subtitle")) {
			if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
				sender.sendMessage("必须由玩家执行该命令");
				return true;
			}
			Player player = (Player) sender;

			if (args.length < 6) {
				player.sendMessage("/custom_map get_stock_subtitle <内容> <字体> <字号> <长度> <移动速度>。");
				return true;
			}
			String[] stock_ids = args[1].split(",");
			Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(args[2]);
			int font_size = 28;
			try {
				font_size = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				player.sendMessage("字号格式错误，已设置为" + font_size);
			}
			int length_n = 5;
			try {
				length_n = Integer.parseInt(args[4]);
			} catch (NumberFormatException e) {
				player.sendMessage("长度格式错误，已设置为" + length_n);
			}

			float speed = 5;
			try {
				speed = Float.parseFloat(args[5]);
			} catch (NumberFormatException e) {
				player.sendMessage("速度格式错误，已设置为" + speed);
			}

			ItemStack[] items = generate_stock_subtitle_maps(player, stock_ids, font, font_size, length_n, speed);
			Inventory_io.give_item_to_player(player, items);
		} else if (args[0].equalsIgnoreCase("reload")) {
			screen_config.reload();
			map_config.reload();
			sender.sendMessage("自定义地图配置重载成功");
			return true;
		}
		return false;
	}

}
