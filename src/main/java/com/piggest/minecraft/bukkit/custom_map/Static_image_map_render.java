package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

public abstract class Static_image_map_render extends Custom_map_render {
	protected BufferedImage image;
	protected boolean render_completed = false;

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		if (this.render_completed == false) {
			this.refresh(map, canvas);
			this.render_completed = true;
		}
	}

	@Override
	public void refresh(MapView map, MapCanvas canvas) {
		draw_image(canvas, 0, 0, image);
	}

	public BufferedImage get_image() {
		return this.image;
	}

	public static interface IntToByteFunction {
		byte applyAsByte(int i);
	}

	public static void parallelSetAll_byte(byte[] array, IntToByteFunction generator) {
		Objects.requireNonNull(generator);
		IntStream.range(0, array.length).parallel().forEach(i -> {
			array[i] = generator.applyAsByte(i);
		});
	}

	@SuppressWarnings("deprecation")
	public static byte[] imageToBytes(@Nonnull Image image) {
		BufferedImage temp = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = temp.createGraphics();
		graphics.drawImage(image, 0, 0, null);
		graphics.dispose();

		int[] pixels = new int[temp.getWidth() * temp.getHeight()];
		temp.getRGB(0, 0, temp.getWidth(), temp.getHeight(), pixels, 0, temp.getWidth());

		byte[] result = new byte[temp.getWidth() * temp.getHeight()];

		parallelSetAll_byte(result, i -> MapPalette.matchColor(new Color(pixels[i], true)));
		return result;
	}

	public static void draw_image_not_parallel(MapCanvas canvas, int x, int y, Image image) {
		byte[] bytes = imageToBytes(image);
		for (int x2 = 0; x2 < image.getWidth(null); ++x2) {
			for (int y2 = 0; y2 < image.getHeight(null); ++y2) {
				byte color = bytes[y2 * image.getWidth(null) + x2];
				if (color != 0) {
					canvas.setPixel(x + x2, y + y2, color);
				}
			}
		}
	}

	public static void draw_image(MapCanvas canvas, int x, int y, Image image) {
		byte[] bytes = imageToBytes(image);
		int width = image.getWidth(null);
		IntStream.range(0, bytes.length).parallel().forEach(i -> {
			int y2 = i / width;
			int x2 = i % width;
			byte color = bytes[i];
			if (color != 0) {
				canvas.setPixel(x + x2, y + y2, color);
			}
		});
	}
}
