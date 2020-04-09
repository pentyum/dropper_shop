package com.piggest.minecraft.bukkit.custom_map;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.annotation.Nonnull;

import com.piggest.minecraft.bukkit.utils.Color_utils;

public class Analog_clock_background_map_render extends Background_map_render {
	protected int size;
	protected org.bukkit.Color line_color;
	protected java.awt.Color awt_line_color;
	public final BasicStroke bstroke1;
	public final BasicStroke bstroke2;
	public final BasicStroke bstroke3;

	public Analog_clock_background_map_render(org.bukkit.Color background_color, org.bukkit.Color line_color,
			int size) {
		super(background_color);
		this.size = size;
		this.line_color = line_color;
		this.awt_line_color = Color_utils.bukkit_to_awt(line_color);
		bstroke1 = new BasicStroke((float) size / 100);
		bstroke2 = new BasicStroke((float) size / 60);
		bstroke3 = new BasicStroke((float) size / 50);
		
		int side_amount = Character_section_map_render.get_side_amount(size);
		int pic_size = Custom_map_render.pic_size * side_amount;
		
		BufferedImage bi = new BufferedImage(pic_size, pic_size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setBackground(awt_background_color);
		g.clearRect(0, 0, pic_size, pic_size);

		double alfa;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(this.awt_line_color);

		for (int i = 0; i <= 360; i += 6) {
			alfa = Math.toRadians(i); // 角度用弧度表示
			int xBegin = pic_size / 2 + (int) (0.43 * size * Math.sin(alfa));
			int yBegin = pic_size / 2 - (int) (0.43 * size * Math.cos(alfa));
			int xBegin_long = pic_size / 2 + (int) (0.37 * size * Math.sin(alfa));
			int yBegin_long = pic_size / 2 - (int) (0.37 * size * Math.cos(alfa));
			int xEnd = pic_size / 2 + (int) (0.47 * size * Math.sin(alfa));
			int yEnd = pic_size / 2 - (int) (0.47 * size * Math.cos(alfa));

			g.setStroke(bstroke1);
			g.drawLine(xBegin, yBegin, xEnd, yEnd);

			if (i % 30 == 0) {
				g.setStroke(bstroke2);
				if (i % 90 == 0) {
					g.setStroke(bstroke3);
				}
				g.drawLine(xBegin_long, yBegin_long, xEnd, yEnd);
			}
		}

		g.dispose();
		this.image_cache = bi;
	}

	@Override
	public @Nonnull Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("size", this.size);
		save.put("line-color", this.line_color);
		return save;
	}

	public static Analog_clock_background_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color background_color = (org.bukkit.Color) args.get("background-color");
		int size = (int) args.get("size");
		org.bukkit.Color line_color = (org.bukkit.Color) args.get("line-color");
		Analog_clock_background_map_render new_render = new Analog_clock_background_map_render(background_color,
				line_color, size);
		return new_render;
	}

	@Override
	public BufferedImage get_image(int pic_size_x, int pic_size_y) {
		return this.get_image_cache_copy();
	}
}
