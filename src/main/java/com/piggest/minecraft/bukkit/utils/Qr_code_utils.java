package com.piggest.minecraft.bukkit.utils;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.piggest.minecraft.bukkit.custom_map.Custom_map_render;

public class Qr_code_utils {
	public static String scan(BufferedImage image) throws NotFoundException {
		MultiFormatReader formatReader = new MultiFormatReader();
		BinaryBitmap binaryBitmap = new BinaryBitmap(
				new HybridBinarizer(new BufferedImageLuminanceSource(image)));
		com.google.zxing.Result qr_result;
		qr_result = formatReader.decode(binaryBitmap);
		image.flush();
		return qr_result.getText();
	}

	public static BufferedImage generate(String text, int margin) throws WriterException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		hints.put(EncodeHintType.MARGIN, margin);
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, Custom_map_render.pic_size,
				Custom_map_render.pic_size, hints);
		return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
}
