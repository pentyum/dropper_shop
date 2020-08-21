package com.piggest.minecraft.bukkit.custom_map;

import com.google.zxing.WriterException;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Qr_code_utils;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.Map;

public class Qr_code_screen extends Static_image_screen implements ConfigurationSerializable {
	private final String text;
	private int margin = 3;

	public Qr_code_screen(String text, int margin) throws WriterException {
		super(1, 1, Fill_type.NONE);
		this.raw_img = Qr_code_utils.generate(text, margin);
		this.text = text;
		this.margin = margin;
	}

	@Override
	public @Nonnull
	Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("text", this.text);
		save.put("margin", this.margin);
		save.put("locked", this.locked);
		return save;
	}

	public static Qr_code_screen deserialize(@Nonnull Map<String, Object> args) {
		String text = (String) args.get("text");
		int margin = (int) args.get("margin");
		boolean locked = (boolean) args.get("locked");
		Qr_code_screen new_render = null;
		try {
			new_render = new Qr_code_screen(text, margin);
			new_render.locked = locked;
		} catch (WriterException e) {
			Dropper_shop_plugin.instance.getLogger().severe(e.toString());
		}
		return new_render;
	}

}