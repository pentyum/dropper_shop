package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Gif_map_render extends Static_image_map_render implements ConfigurationSerializable {
	private String path;
	private long timer = System.currentTimeMillis();
	private int frame = 0;
	private ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();

	public Gif_map_render(String path) throws IOException {
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		File image_file = new File(Dropper_shop_plugin.instance.getDataFolder(), "images/" + path);
		ImageInputStream stream = ImageIO.createImageInputStream(image_file);
		reader.setInput(stream);

		int count = reader.getNumImages(true);
		for (int index = 0; index < count; index++) {
			BufferedImage frame = reader.read(index);
			frame = resize(frame);
			this.images.add(frame);
		}
		this.image = this.images.get(0);
		this.path = path;
	}

	public static BufferedImage resize(BufferedImage image) {
		int height = image.getHeight();
		int width = image.getWidth();
		int new_height;
		int new_width;
		int start_x;
		int start_y;
		double ratio = (double) width / height;
		if (height >= width) {
			new_height = Custom_map_render.pic_size;
			new_width = (int) ((double) Custom_map_render.pic_size * ratio);
			start_y = 0;
			start_x = (Custom_map_render.pic_size - new_width) / 2;
		} else {
			new_width = Custom_map_render.pic_size;
			new_height = (int) ((double) Custom_map_render.pic_size / ratio);
			start_x = 0;
			start_y = (Custom_map_render.pic_size - new_height) / 2;
		}
		Image scaled_image = image.getScaledInstance(new_width, new_height, Image.SCALE_DEFAULT);
		BufferedImage new_image = new BufferedImage(Custom_map_render.pic_size, Custom_map_render.pic_size,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = new_image.createGraphics();
		g.drawImage(scaled_image, start_x, start_y, null);
		g.dispose();
		return new_image;
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		long current_time = System.currentTimeMillis();
		if (current_time - this.timer < 100) {
			return;
		}
		this.frame++;
		if (this.frame >= this.images.size()) {
			this.frame = 0;
		}
		this.image = this.images.get(this.frame);
		this.refresh(map, canvas);
		this.timer = current_time;
	}

	@Override
	public @Nonnull
	Map<String, Object> serialize() {
		Map<String, Object> save = new HashMap<String, Object>();
		save.put("path", this.path);
		save.put("locked", this.locked);
		return save;
	}

	public static Gif_map_render deserialize(@Nonnull Map<String, Object> args) {
		String path = (String) args.get("path");
		boolean locked = (boolean) args.get("locked");
		Gif_map_render render = null;
		try {
			render = new Gif_map_render(path);
			render.locked = locked;
		} catch (IOException e) {
			Dropper_shop_plugin.instance.getLogger().severe(e.toString());
		}
		return render;
	}

	public int get_total_frames() {
		return this.images.size();
	}

}
