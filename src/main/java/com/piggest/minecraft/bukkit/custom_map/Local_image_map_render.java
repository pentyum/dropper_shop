package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Local_image_map_render extends Static_image_map_render implements ConfigurationSerializable {
	private String path;
	private int width_n = 0;
	private int height_n = 0;
	private int section = 1;
	private int total_maps = 1;

	public static int get_another_n(int n, int n_length, int another_length) {
		return (int) Math.ceil((double) n * another_length / n_length);
	}

	public static BufferedImage resize(BufferedImage image, int width_n, int height_n) {
		// TO DO
		return null;
	}

	/**
	 * 读取本地图片文件
	 *
	 * @throws IndexOutOfBoundsException
	 * @throws IOException
	 */
	public Local_image_map_render(String path, int width_n, int height_n, int section)
			throws IndexOutOfBoundsException, IOException {
		this.path = path;
		this.width_n = width_n;
		this.height_n = height_n;
		this.section = section;

		File image_file = new File(Dropper_shop_plugin.instance.getDataFolder(), "images/" + path);

		this.image = ImageIO.read(image_file);

		int old_width = this.image.getWidth();
		int old_height = this.image.getHeight();
		int new_width;
		int new_height;
		int map_width;
		int map_height;
		int start_x = 0;
		int start_y = 0;

		if (this.width_n != 0) {
			new_width = Custom_map_render.pic_size * this.width_n;
			new_height = new_width * old_height / old_width;
			map_width = new_width;
			this.height_n = get_another_n(this.width_n, new_width, new_height);
			map_height = Custom_map_render.pic_size * this.height_n;
			start_x = 0;
			start_y = (map_height - new_height) / 2;
		} else {
			new_height = Custom_map_render.pic_size * this.height_n;
			new_width = new_height * old_width / old_height;
			map_height = new_height;
			this.width_n = get_another_n(this.height_n, new_height, new_width);
			map_width = Custom_map_render.pic_size * this.width_n;
			start_y = 0;
			start_x = (map_width - new_width) / 2;
		}

		this.total_maps = this.width_n * this.height_n;
		if (this.section >= total_maps) {
			throw new ArrayIndexOutOfBoundsException("下标" + this.section + "越界，图片总共" + total_maps + "张");
		}

		Image scaled_image = this.image.getScaledInstance(new_width, new_height, Image.SCALE_DEFAULT);
		this.image = new BufferedImage(map_width, map_height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = this.image.createGraphics();
		g.drawImage(scaled_image, start_x, start_y, null);
		g.dispose();
		this.image = Character_section_map_render.get_section_of_image(image, this.width_n, this.section);
	}

	public int get_height_n() {
		return this.height_n;
	}

	public int get_width_n() {
		return this.width_n;
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = new HashMap<>();
		save.put("path", this.path);
		save.put("width-n", this.width_n);
		save.put("height-n", this.height_n);
		save.put("section", this.section);
		save.put("locked", this.locked);
		return save;
	}

	public static Local_image_map_render deserialize(@Nonnull Map<String, Object> args) {
		String path = (String) args.get("path");
		int section = (int) args.get("section");
		int width_n = (int) args.get("width-n");
		int height_n = (int) args.get("height-n");
		boolean locked = (boolean) args.get("locked");
		Local_image_map_render new_render = null;
		try {
			new_render = new Local_image_map_render(path, width_n, height_n, section);
			new_render.locked = locked;
		} catch (ArrayIndexOutOfBoundsException | IOException e) {
			Dropper_shop_plugin.instance.getLogger().severe(e.toString());
		}
		return new_render;
	}

}