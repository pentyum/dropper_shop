package com.piggest.minecraft.bukkit.custom_map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public abstract class Screen implements ConfigurationSerializable, Runnable {
	protected int id = 0;
	protected long time = 0;
	protected BufferedImage raw_img;
	protected BufferedImage show_img;
	protected int width_n = 0;
	protected int height_n = 0;
	protected Fill_type fill_type;

	public Screen(int width_n, int height_n, Fill_type fill_type) {
		this.height_n = height_n;
		this.width_n = width_n;
		this.fill_type = fill_type;
	}

	public int get_id() {
		return this.id;
	}

	public void set_id(int id) {
		this.id = id;
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<>();
		save.put("id", this.id);
		save.put("height-n", this.height_n);
		save.put("width-n", this.width_n);
		save.put("fill-type", this.fill_type.name());
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

		if (this.fill_type == Fill_type.WIDTH) {//固定宽度占满，此时height_n无效
			new_width = Custom_map_render.pic_size * this.width_n;
			new_height = new_width * raw_height / raw_width;
			map_width = new_width;
			this.height_n = Local_image_map_render.get_another_n(this.width_n, new_width, new_height);
			map_height = Custom_map_render.pic_size * this.height_n;
			start_x = 0;
			start_y = (map_height - new_height) / 2;
		} else if (this.fill_type == Fill_type.HEIGHT) {//固定高度占满，此时width_n无效
			new_height = Custom_map_render.pic_size * this.height_n;
			new_width = new_height * raw_width / raw_height;
			map_height = new_height;
			this.width_n = Local_image_map_render.get_another_n(this.height_n, new_height, new_width);
			map_width = Custom_map_render.pic_size * this.width_n;
			start_y = 0;
			start_x = (map_width - new_width) / 2;
		} else { //全部拉伸占满，完全由height_n和width_n定义
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

	@Override
	public void run() {
		this.refresh();
	}

	public abstract void refresh();

	public void dump() {
		//TO DO
	}

	public int get_show_width_n() {
		return this.width_n;
	}

	public int get_show_height_n() {
		return this.height_n;
	}

	public Screen_map_render[] generate_renders() {
		int map_quantity = this.get_show_width_n() * this.get_show_height_n();
		Screen_map_render[] renders = new Screen_map_render[map_quantity];
		Arrays.setAll(renders, i -> new Screen_map_render(i, this.id));
		return renders;
	}

	/*
	获取刷新间隔，单位为tick
	 */
	public abstract int get_refresh_interval();

	enum Fill_type {
		WIDTH,
		HEIGHT,
		FULL
	}
}
