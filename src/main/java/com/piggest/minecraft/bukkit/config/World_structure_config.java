package com.piggest.minecraft.bukkit.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.io.Files;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class World_structure_config {
	private World world;
	private File config_file;
	private YamlConfiguration config;

	public World_structure_config(World world) {
		this.world = world;
	}

	public void load() {
		this.config_file = new File(Dropper_shop_plugin.instance.getDataFolder(), "shops_" + world.getName() + ".yml");
		this.config = YamlConfiguration.loadConfiguration(config_file);
	}

	public boolean backup() {
		File config_file_backup = new File(config_file.getAbsolutePath() + ".bak");
		try {
			if (config_file_backup.exists()) {
				config_file_backup.delete();
				config_file_backup.createNewFile();
			}
			Files.copy(config_file, config_file_backup);
		} catch (IOException e) {
			Dropper_shop_plugin.instance.getLogger().severe("原结构配置备份失败");
			return false;
		}
		Dropper_shop_plugin.instance.getLogger().info("原结构配置已保存至" + config_file_backup.getAbsolutePath());
		return true;
	}

	public boolean save() {
		try {
			config.save(this.config_file);
		} catch (IOException e) {
			Dropper_shop_plugin.instance.getLogger().severe("结构文件保存错误!");
			return false;
		}
		return true;
	}

	public List<Map<?, ?>> getMapList(String structure_name) {
		return this.config.getMapList(structure_name);
	}

	public void set(String structure_name, ArrayList<HashMap<String, Object>> value) {
		this.config.set(structure_name, value);
	}
}
