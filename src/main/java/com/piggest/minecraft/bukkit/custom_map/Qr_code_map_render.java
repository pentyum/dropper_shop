package com.piggest.minecraft.bukkit.custom_map;

import com.google.zxing.WriterException;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Qr_code_utils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class Qr_code_map_render extends Static_image_map_render implements ConfigurationSerializable {
	private String text;
	private int margin = 3;

	public Qr_code_map_render(String text, int margin) throws WriterException {
		this.image = Qr_code_utils.generate(text, margin);
		this.text = text;
		this.margin = margin;
	}

	@Override
	public @Nonnull
	Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("text", this.text);
		save.put("margin", this.margin);
		save.put("locked", this.locked);
		return save;
	}

	public static Qr_code_map_render deserialize(@Nonnull Map<String, Object> args) {
		String text = (String) args.get("text");
		int margin = (int) args.get("margin");
		boolean locked = (boolean) args.get("locked");
		Qr_code_map_render new_render = null;
		try {
			new_render = new Qr_code_map_render(text, margin);
			new_render.locked = locked;
		} catch (WriterException e) {
			Dropper_shop_plugin.instance.getLogger().severe(e.toString());
		}
		return new_render;
	}

}