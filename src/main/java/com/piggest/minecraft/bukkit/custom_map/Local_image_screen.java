package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Local_image_screen extends Static_image_screen {
	private final String path;

	/**
	 * 读取本地图片文件
	 *
	 * @throws IOException
	 */
	public Local_image_screen(String path, int width_n, int height_n, Screen.Fill_type fill_type) throws IOException {
		super(width_n, height_n, fill_type);
		this.path = path;

		File image_file = new File(Dropper_shop_plugin.instance.getDataFolder(), "images/" + path);
		this.raw_img = ImageIO.read(image_file);
		this.refresh();
	}


	@Nonnull
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("path", this.path);
		return save;
	}


	public static Local_image_screen deserialize(@Nonnull Map<String, Object> args) {
		String path = (String) args.get("path");
		int id = (int) args.get("id");
		int width_n = (int) args.get("width-n");
		int height_n = (int) args.get("height-n");
		Screen.Fill_type fill_type = Screen.Fill_type.valueOf((String) args.get("fill-type"));
		Local_image_screen screen = null;
		try {
			screen = new Local_image_screen(path, width_n, height_n, fill_type);
			screen.set_id(id);
		} catch (IOException e) {
			Dropper_shop_plugin.instance.getLogger().severe(e.toString());
		}
		return screen;
	}

}