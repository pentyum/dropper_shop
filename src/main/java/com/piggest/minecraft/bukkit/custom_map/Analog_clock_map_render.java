package com.piggest.minecraft.bukkit.custom_map;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Clock_utils;

public class Analog_clock_map_render extends Digital_clock_map_render {
	private String style;
	private int pic_size = Character_map_render.pic_size;
	public final BasicStroke bstroke_sec = new BasicStroke((float)pic_size/100);
	public final BasicStroke bstroke_min = new BasicStroke((float)pic_size/64);
	public final BasicStroke bstroke_hr = new BasicStroke((float)pic_size/43);

	public Analog_clock_map_render(Color background_color, String style, Font font, int font_size, Color font_color,
			String world_name) {
		super(background_color, "HH:mm:ss", font, font_size, font_color, world_name);
		this.style = style;
	}

	@Override
	public void refresh(MapView map, MapCanvas canvas, Player player) {
		image = new BufferedImage(pic_size, pic_size, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();

		g.setBackground(awt_background_color);
		g.clearRect(0, 0, pic_size, pic_size);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(awt_font_color);

		int hr = this.date.get(Calendar.HOUR);
		int min = this.date.get(Calendar.MINUTE);
		int sec = this.date.get(Calendar.SECOND);
		Clock_utils.Clock_pos_data pos_data = new Clock_utils.Clock_pos_data(hr, min, sec, pic_size, 0.2, 0.3, 0.4,
				0.05);

		g.setStroke(bstroke_hr);
		g.drawLine(pic_size / 2, pic_size / 2, pos_data.hr_head_x, pos_data.hr_head_y); // 画时针

		g.setStroke(bstroke_min);
		g.drawLine(pic_size / 2, pic_size / 2, pos_data.min_head_x, pos_data.min_head_y); // 画分针

		if (this.world_name == null) {
			g.setStroke(bstroke_sec);
			g.drawLine(pos_data.sec_tail_x, pos_data.sec_tail_y, pos_data.sec_head_x, pos_data.sec_head_y); // 画秒针
		}
		
		g.dispose();
		canvas.drawImage(0, 0, image);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("style", this.style);
		return save;
	}

	public static Analog_clock_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color background_color = (org.bukkit.Color) args.get("background-color");
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		String world_name = (String) args.get("world");
		String style = null;
		Analog_clock_map_render new_render = new Analog_clock_map_render(background_color, style, font, font_size,
				font_color, world_name);
		return new_render;
	}
}
