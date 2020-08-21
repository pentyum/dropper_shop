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
	private BufferedImage show_img;
	protected int width_n = 0;
	protected int height_n = 0;
	protected Fill_type fill_type;
	protected boolean locked = false;

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

	public boolean is_locked() {
		return this.locked;
	}

	public void set_locked(boolean locked) {
		this.locked = locked;
	}

	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<>();
		save.put("id", this.id);
		save.put("height-n", this.height_n);
		save.put("width-n", this.width_n);
		save.put("fill-type", this.fill_type.name());
		save.put("locked", this.locked);
		return save;
	}

	public BufferedImage get_section(int section) {
		return get_section_of_image(this.show_img, this.width_n, section);
	}


	public void refresh(BufferedImage new_img) {
		this.raw_img = new_img;
		this.transform();
		this.time = System.currentTimeMillis();
	}

	protected void transform() {
		if (this.fill_type != Fill_type.NONE) {
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
				this.height_n = get_another_n(this.width_n, new_width, new_height);
				map_height = Custom_map_render.pic_size * this.height_n;
				start_x = 0;
				start_y = (map_height - new_height) / 2;
			} else if (this.fill_type == Fill_type.HEIGHT) {//固定高度占满，此时width_n无效
				new_height = Custom_map_render.pic_size * this.height_n;
				new_width = new_height * raw_width / raw_height;
				map_height = new_height;
				this.width_n = get_another_n(this.height_n, new_height, new_width);
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
		} else {
			this.show_img = this.raw_img;
		}
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

	public int get_show_width() {
		return this.width_n * Custom_map_render.pic_size;
	}

	public int get_show_height() {
		return this.height_n * Custom_map_render.pic_size;
	}

	public Screen_map_render[] generate_renders() {
		assert this.id > 0;
		int map_quantity = this.get_show_width_n() * this.get_show_height_n();
		Screen_map_render[] renders = new Screen_map_render[map_quantity];
		Arrays.setAll(renders, i -> new Screen_map_render(i, this.id));
		return renders;
	}

	/*
	获取刷新间隔，单位为tick
	 */
	public abstract int get_refresh_interval();

	public static int get_another_n(int n, int n_length, int another_length) {
		return (int) Math.ceil((double) n * another_length / n_length);
	}

	public static BufferedImage get_section_of_image(BufferedImage bi, int side_amount, int section) {
		int start_y = (section / side_amount) * Custom_map_render.pic_size;
		int start_x = (section % side_amount) * Custom_map_render.pic_size;
		return bi.getSubimage(start_x, start_y, Custom_map_render.pic_size, Custom_map_render.pic_size);
	}

	public BufferedImage get_current_raw_img() {
		return this.raw_img;
	}

	protected enum Fill_type {
		WIDTH,
		HEIGHT,
		FULL,
		NONE
	}

}
