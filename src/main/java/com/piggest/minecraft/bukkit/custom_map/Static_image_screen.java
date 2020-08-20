package com.piggest.minecraft.bukkit.custom_map;

import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import java.util.stream.IntStream;

public abstract class Static_image_screen extends Screen {
	public Static_image_screen(int width_n, int height_n, Fill_type fill_type) {
		super(width_n, height_n, fill_type);
	}

	@Override
	public int get_refresh_interval() {
		return 0;//永不自动刷新
	}

	@Override
	public void refresh() {
		refresh(this.raw_img);
	}

	public static interface IntToByteFunction {
		byte applyAsByte(int i);
	}

	public static void parallelSetAll_byte(byte[] array, IntToByteFunction generator) {
		Objects.requireNonNull(generator);
		IntStream.range(0, array.length).parallel().forEach(i -> array[i] = generator.applyAsByte(i));
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
