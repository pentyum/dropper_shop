package com.piggest.minecraft.bukkit.electrolyte;

import com.piggest.minecraft.bukkit.structure.Structure_manager;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Electrolyte_manager extends Structure_manager<Electrolyte> {

	public static Electrolyte_manager instance = null;

	public Electrolyte_manager() {
		super(Electrolyte.class);
		Electrolyte_manager.instance = this;
	}

	@Nonnull
	@Override
	public String get_permission_head() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public Material[][][] get_model() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Override
	public int[] get_center() {
		// TODO 自动生成的方法存根
		return null;
	}

	@Nullable
	@Override
	public String get_gui_name() {
		return null;
	}
}
