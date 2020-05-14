package com.piggest.minecraft.bukkit.custom_map;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

public class Command_map_render extends Custom_map_render {
	private Queue<Map_operation> command_queue = new LinkedList<>();

	public abstract static class Map_operation {
		byte color;
		public abstract void draw(MapCanvas canvas);
	}

	public static class Pixel_setting extends Map_operation {
		int x;
		int y;
		public Pixel_setting(int x, int y, byte color) {
			this.x = x;
			this.y = y;
			this.color = color;
		}
		@Override
		public void draw(MapCanvas canvas) {
			canvas.setPixel(x, y, color);
		}
	}

	public static class Line_setting extends Map_operation {
		int start_x;
		int start_y;
		int end_x;
		int end_y;
		public Line_setting(int start_x, int start_y, int end_x,int end_y, byte color) {
			this.start_x = start_x;
			this.start_y = start_y;
			this.end_x = end_x;
			this.end_y = end_y;
			this.color = color;
		}
		@Override
		public void draw(MapCanvas canvas) {
			// TO DO
		}
	}

	public static class Circle_setting extends Map_operation {
		int x;
		int y;
		int r;
		public Circle_setting(int x, int y, int r, byte color) {
			this.x = x;
			this.y = y;
			this.r = r;
			this.color = color;
		}
		@Override
		public void draw(MapCanvas canvas) {
			// TO DO
		}
	}

	public void set_pixel(int x, int y, byte color) {
		Pixel_setting setting = new Pixel_setting(x, y, color);
		this.command_queue.add(setting);
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		Map_operation operation = this.command_queue.poll();
		if (operation == null) {
			return;
		}
		operation.draw(canvas);
	}

	@Override
	public void refresh(MapView map, MapCanvas canvas) {
		
	}
	
}
