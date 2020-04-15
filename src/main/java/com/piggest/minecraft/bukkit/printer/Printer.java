package com.piggest.minecraft.bukkit.printer;

import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.structure.Multi_block_with_gui;

public class Printer extends Multi_block_with_gui {
	private boolean color_upgrade = false;
	private boolean clock_upgrade = false;
	private boolean image_upgrade = false;
	private boolean qr_code_upgrade = false;

	@Override
	public void on_button_pressed(Player player, int slot) {
		// TODO 自动生成的方法存根

	}

	@Override
	public boolean on_switch_pressed(Player player, int slot, boolean on) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean on_put_item(Player player, ItemStack cursor_item, int slot) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean on_take_item(Player player, ItemStack in_item, int slot) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean on_exchange_item(Player player, ItemStack in_item, ItemStack cursor_item, int slot) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public boolean create_condition(Player player) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	protected void init_after_set_location() {
		// TODO 自动生成的方法存根

	}

	@Override
	protected boolean on_break(Player player) {
		// TODO 自动生成的方法存根
		return false;
	}

	@Override
	public ItemStack[] get_drop_items() {
		// TODO 自动生成的方法存根
		return null;
	}

	public void set_qr_code_upgrade(boolean qr_code_upgrade) {
		// TODO 自动生成的方法存根
		this.qr_code_upgrade = qr_code_upgrade;
	}

	public void set_image_upgrade(boolean image_upgrade) {
		// TODO 自动生成的方法存根
		this.image_upgrade = image_upgrade;
	}

	public void set_clock_upgrade(boolean clock_upgrade) {
		// TODO 自动生成的方法存根
		this.clock_upgrade = clock_upgrade;
	}

	public void set_color_upgrade(boolean color_upgrade) {
		// TODO 自动生成的方法存根
		this.color_upgrade = color_upgrade;
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("color-upgrade", this.color_upgrade);
		save.put("clock-upgrade", this.clock_upgrade);
		save.put("image-upgrade", this.image_upgrade);
		save.put("qr-code-upgrade", this.qr_code_upgrade);
		return save;
	}

	@Override
	protected void set_from_save(Map<String, Object> save) {
		super.set_from_save(save);
		boolean color_upgrade = (boolean) save.get("color-upgrade");
		boolean clock_upgrade = (boolean) save.get("clock-upgrade");
		boolean image_upgrade = (boolean) save.get("image-upgrade");
		boolean qr_code_upgrade = (boolean) save.get("qr-code-upgrade");
		this.set_color_upgrade(color_upgrade);
		this.set_clock_upgrade(clock_upgrade);
		this.set_image_upgrade(image_upgrade);
		this.set_qr_code_upgrade(qr_code_upgrade);
	}

	public static Printer deserialize(@Nonnull Map<String, Object> args) {
		Printer printer = new Printer();
		printer.set_from_save(args);
		return printer;
	}
}
