package com.piggest.minecraft.bukkit.custom_map.clock;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Calendar;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Color;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.custom_map.Character_section_map_render;
import com.piggest.minecraft.bukkit.custom_map.Custom_map_render;
import com.piggest.minecraft.bukkit.custom_map.Static_image_map_render;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Clock_utils;

public class Analog_clock_map_render extends Digital_clock_map_render {
	private String style;
	private BasicStroke bstroke_sec;
	private BasicStroke bstroke_min;
	private BasicStroke bstroke_hr;
	private int section = 1;

	private int pic_size;
	private int side_amount;

	public Analog_clock_map_render(Analog_clock_background_map_render background, String style, Font font, int size,
			Color font_color, String world_name, int section) {
		super(background, "HH:mm:ss", font, size, font_color, world_name);
		this.style = style;
		this.section = section;
		bstroke_sec = new BasicStroke((float) this.font_size / 100);
		bstroke_min = new BasicStroke((float) this.font_size / 58);
		bstroke_hr = new BasicStroke((float) this.font_size / 43);

		this.side_amount = Character_section_map_render.get_side_amount(this.font_size);
		this.pic_size = Custom_map_render.pic_size * side_amount;
	}

	@Override
	public void refresh(MapView map, MapCanvas canvas) {
		this.image = this.background.get_image_cache_copy();

		Graphics2D g = image.createGraphics();

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(awt_font_color);

		int hr = this.date.get(Calendar.HOUR);
		int min = this.date.get(Calendar.MINUTE);
		int sec = this.date.get(Calendar.SECOND);

		Clock_utils.Clock_pos_data pos_data = new Clock_utils.Clock_pos_data(hr, min, sec, this.pic_size, 0.2, 0.3, 0.4,
				0.05);

		g.setStroke(bstroke_hr);
		g.drawLine(this.pic_size / 2, this.pic_size / 2, pos_data.hr_head_x, pos_data.hr_head_y); // 画时针

		g.setStroke(bstroke_min);
		g.drawLine(this.pic_size / 2, this.pic_size / 2, pos_data.min_head_x, pos_data.min_head_y); // 画分针

		if (this.world_name == null) {
			g.setStroke(bstroke_sec);
			g.drawLine(pos_data.sec_tail_x, pos_data.sec_tail_y, pos_data.sec_head_x, pos_data.sec_head_y); // 画秒针
		}

		g.dispose();

		this.image = Character_section_map_render.get_section_of_image(image, side_amount, this.section);
		Static_image_map_render.draw_image(canvas, 0, 0, image);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("style", this.style);
		save.put("section", this.section);
		return save;
	}

	public static Analog_clock_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color font_color = (org.bukkit.Color) args.get("font-color");
		String font_name = (String) args.get("font-name");
		Font font = Dropper_shop_plugin.instance.get_fonts_manager().get_font(font_name);
		int font_size = (int) args.get("font-size");
		String world_name = (String) args.get("world");
		String style = null;
		int section = (int) args.get("section");
		Analog_clock_background_map_render background = (Analog_clock_background_map_render) args.get("background");
		Analog_clock_map_render new_render = new Analog_clock_map_render(background, style, font, font_size, font_color,
				world_name, section);
		return new_render;
	}
}