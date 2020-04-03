package com.piggest.minecraft.bukkit.custom_map;

import java.awt.image.BufferedImage;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapView;

public abstract class Static_image_map_render extends Custom_map_render {
	protected BufferedImage image;
	protected boolean render_completed = false;
	
	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		if (this.render_completed == false) {
			canvas.drawImage(0, 0, image);
			this.render_completed = true;
		}
	}
}
