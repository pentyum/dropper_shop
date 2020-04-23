package com.piggest.minecraft.bukkit.custom_map;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

public class Command_map_render extends Custom_map_render {
	private Queue<Pixel_setting> command_queue = new LinkedList<Pixel_setting>();

	public static class Pixel_setting {
		int x;
		int y;
		byte color;

		public Pixel_setting(int x, int y, byte color) {
			this.x = x;
			this.y = y;
			this.color = color;
		}
	}

	public void set_pixel(int x, int y, byte color) {
		Pixel_setting setting = new Pixel_setting(x, y, color);
		this.command_queue.add(setting);
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		Pixel_setting new_setting = this.command_queue.poll();
		if (new_setting == null) {
			return;
		}
		canvas.setPixel(new_setting.x, new_setting.y, new_setting.color);
	}

	@Override
	public void refresh(MapView map, MapCanvas canvas) {
		
	}
	
}
