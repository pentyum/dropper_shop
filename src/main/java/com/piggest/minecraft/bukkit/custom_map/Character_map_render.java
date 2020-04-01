package com.piggest.minecraft.bukkit.custom_map;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Character_map_render extends MapRenderer {
	private char character;
	private BufferedImage image;

	public Character_map_render(char character, Font font) {
		File temp_file = new File(Dropper_shop_plugin.instance.getDataFolder(),"temp.png");
		BufferedImage bi = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = bi.createGraphics();
		g.setBackground(Color.WHITE);
		g.clearRect(0, 0, 128, 128);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		int size = 100;
		g.setColor(Color.BLACK);
		font = font.deriveFont(Font.PLAIN, size);
		g.setFont(font);
		g.drawString(String.valueOf(character), (128 - size) / 2, (100 + size) / 2);
		g.dispose();
		this.character = character;
		this.image = bi;
	}

	@Override
	public void render(MapView map, MapCanvas canvas, Player player) {
		canvas.drawImage(0, 0, image);
	}

}
