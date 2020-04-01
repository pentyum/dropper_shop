package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public class Custom_map_render extends MapRenderer {
	File srcImgFile = new File("F:/Users/yaoyao/Desktop/xieyan.png");
	Image image;

	public Custom_map_render() {
		try {
			image = ImageIO.read(srcImgFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		canvas.drawImage(0, 0, image);
	}

}
