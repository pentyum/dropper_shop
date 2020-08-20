package com.piggest.minecraft.bukkit.custom_map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class Screen implements ConfigurationSerializable {
	protected int id;
	protected long time;
	protected BufferedImage raw_img;
	protected BufferedImage show_img;
	protected int height_n = 0;
	protected int width_n = 0;

	public int get_id() {
		return this.id;
	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<>();
		save.put("id", this.id);
		save.put("time", this.time);
		return save;
	}

	public BufferedImage get_section(int section) {
		return Character_section_map_render.get_section_of_image(this.show_img, this.width_n, section);
	}


	public void refresh(BufferedImage new_img) {
		this.raw_img = new_img;
		this.transform();
		this.time = System.currentTimeMillis();
	}

	private void transform() {
		int raw_width = this.raw_img.getWidth();
		int raw_height = this.raw_img.getHeight();
		int new_width;
		int new_height;
		int map_width;
		int map_height;
		int start_x = 0;
		int start_y = 0;

		if (this.width_n != 0 && this.height_n == 0) {//固定宽度占满
			new_width = Custom_map_render.pic_size * this.width_n;
			new_height = new_width * raw_height / raw_width;
			map_width = new_width;
			this.height_n = Local_image_map_render.get_another_n(this.width_n, new_width, new_height);
			map_height = Custom_map_render.pic_size * this.height_n;
			start_x = 0;
			start_y = (map_height - new_height) / 2;
		} else if (this.height_n != 0 && this.width_n == 0) {//固定高度占满
			new_height = Custom_map_render.pic_size * this.height_n;
			new_width = new_height * raw_width / raw_height;
			map_height = new_height;
			this.width_n = Local_image_map_render.get_another_n(this.height_n, new_height, new_width);
			map_width = Custom_map_render.pic_size * this.width_n;
			start_y = 0;
			start_x = (map_width - new_width) / 2;
		} else { //全部拉伸占满
			new_width = Custom_map_render.pic_size * this.width_n;
			new_height = Custom_map_render.pic_size * this.height_n;
			map_width = new_width;
			map_height = new_height;
		}

		Image scaled_image = this.raw_img.getScaledInstance(new_width, new_height, Image.SCALE_DEFAULT);
		this.show_img = new BufferedImage(map_width, map_height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = this.show_img.createGraphics();
		g.drawImage(scaled_image, start_x, start_y, null);
		g.dispose();
	}

	public void dump() {
		//TO DO
	}
}
