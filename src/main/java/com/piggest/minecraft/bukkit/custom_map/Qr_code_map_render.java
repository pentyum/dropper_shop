package com.piggest.minecraft.bukkit.custom_map;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Qr_code_map_render extends Static_image_map_render implements ConfigurationSerializable {
	private String text;
	private int margin = 3;

	public Qr_code_map_render(String text, int margin) throws WriterException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.MARGIN, margin);
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, Custom_map_render.pic_size,
				Custom_map_render.pic_size, hints);
		this.image = MatrixToImageWriter.toBufferedImage(bitMatrix);
		this.text = text;
		this.margin = margin;
	}

	@Override
	public @Nonnull Map<String, Object> serialize() {
		HashMap<String, Object> save = new HashMap<String, Object>();
		save.put("text", this.text);
		save.put("margin", this.margin);
		return save;
	}

	public static Qr_code_map_render deserialize(@Nonnull Map<String, Object> args) {
		String text = (String) args.get("text");
		int margin = (int) args.get("margin");
		Qr_code_map_render new_render = null;
		try {
			new_render = new Qr_code_map_render(text, margin);
		} catch (WriterException e) {
			Dropper_shop_plugin.instance.getLogger().severe(e.toString());
		}
		return new_render;
	}
}