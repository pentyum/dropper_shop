package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Gif_screen extends Screen {
	private final String path;
	private int frame = 0;
	private final ArrayList<BufferedImage> images = new ArrayList<>();

	public Gif_screen(String path, int width_n, int height_n, Screen.Fill_type fill_type) throws IOException {
		super(width_n, height_n, fill_type);
		ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
		File image_file = new File(Dropper_shop_plugin.instance.getDataFolder(), "images/" + path);
		ImageInputStream stream = ImageIO.createImageInputStream(image_file);
		reader.setInput(stream);

		int count = reader.getNumImages(true);
		for (int index = 0; index < count; index++) {
			BufferedImage frame = reader.read(index);
			this.images.add(frame);
		}
		this.path = path;
		this.refresh();
	}

	/*
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
	*/

	public void refresh() {
		this.frame++;
		if (this.frame >= this.images.size()) {
			this.frame = 0;
		}
		BufferedImage next_image = this.images.get(this.frame);
		this.refresh(next_image);
	}

	@Override
	public int get_refresh_interval() {
		return 5;
	}

	@Override
	public @Nonnull
	Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("path", this.path);
		return save;
	}

	public static Gif_screen deserialize(@Nonnull Map<String, Object> args) {
		String path = (String) args.get("path");
		int id = (int) args.get("id");
		int width_n = (int) args.get("width-n");
		int height_n = (int) args.get("height-n");
		Screen.Fill_type fill_type = Screen.Fill_type.valueOf((String) args.get("fill-type"));
		Gif_screen screen = null;
		try {
			screen = new Gif_screen(path, width_n, height_n, fill_type);
			screen.set_id(id);
		} catch (IOException e) {
			Dropper_shop_plugin.instance.getLogger().severe(e.toString());
		}
		return screen;
	}

	public int get_total_frames() {
		return this.images.size();
	}

}
