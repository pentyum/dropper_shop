package com.piggest.minecraft.bukkit.custom_map;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Color;

public class Analog_clock_background_map_render extends Background_map_render {
	protected BufferedImage image;
	protected int size;
	public final BasicStroke bstroke1;
	public final BasicStroke bstroke2;
	public final BasicStroke bstroke3;

	public Analog_clock_background_map_render(Color background_color, int size) {
		super(background_color);
		this.size = size;

		bstroke1 = new BasicStroke((float) size / 100);
		bstroke2 = new BasicStroke((float) size / 60);
		bstroke3 = new BasicStroke((float) size / 50);

		BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setBackground(awt_background_color);
		g.clearRect(0, 0, size, size);

		double alfa;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(java.awt.Color.BLACK);

		for (int i = 0; i <= 360; i += 6) {
			alfa = Math.toRadians(i); // 角度用弧度表示
			int xBegin = size / 2 + (int) (0.43 * size * Math.sin(alfa));
			int yBegin = size / 2 - (int) (0.43 * size * Math.cos(alfa));
			int xBegin_long = size / 2 + (int) (0.37 * size * Math.sin(alfa));
			int yBegin_long = size / 2 - (int) (0.37 * size * Math.cos(alfa));
			int xEnd = size / 2 + (int) (0.47 * size * Math.sin(alfa));
			int yEnd = size / 2 - (int) (0.47 * size * Math.cos(alfa));

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
		this.image = bi;
	}

	@Override
	public @Nonnull Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("size", this.size);
		return save;
	}

	public static Analog_clock_background_map_render deserialize(@Nonnull Map<String, Object> args) {
		org.bukkit.Color background_color = (org.bukkit.Color) args.get("background-color");
		int size = (int) args.get("size");
		Analog_clock_background_map_render new_render = new Analog_clock_background_map_render(background_color, size);
		return new_render;
	}

	@Override
	public BufferedImage get_image(int pic_size_x, int pic_size_y) {
		int w = image.getWidth(null);
		int h = image.getHeight(null);
		BufferedImage new_img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = new_img.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
		return new_img;
	}
}
