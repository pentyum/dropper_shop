package com.piggest.minecraft.bukkit.biome_modify;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Biome;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.nms.NMS_manager;

public class Biome_modify implements TabExecutor {
	private HashMap<Biome, Float> custom_temp = new HashMap<>();
	public static final HashMap<Biome, Float> original_temp = new HashMap<>();
	private HashMap<Biome, Biome> pretend_biome = new HashMap<>();
	private Winter_mode winter_mode = new Winter_mode();
	private String[] subcommand_list = new String[] { "show_biome_temp", "set_biome_temp", "winter_mode" };

	public void set_pretend_biome(Biome biome, Biome pretend_biome) {
		this.pretend_biome.put(biome, pretend_biome);
		float temp = NMS_manager.biome_modifier.get_temperature(pretend_biome);
		this.custom_temp.put(biome, temp);
		NMS_manager.biome_modifier.set_temperature(biome, temp);
	}

	public void reset_all() {
		this.pretend_biome = new HashMap<Biome, Biome>();
		this.custom_temp = new HashMap<Biome, Float>();
	}

	public void set_custom_temp(Biome biome, float temp) {
		custom_temp.put(biome, temp);
		NMS_manager.biome_modifier.set_temperature(biome, temp);
		Biome pretend = biome;
		if (temp <= -0.5) {
			pretend = Biome.SNOWY_TAIGA;
		} else if (temp <= 0) {
			pretend = Biome.SNOWY_TUNDRA;
		} else if (temp <= 0.2) {
			pretend = Biome.MOUNTAINS;
		} else if (temp <= 0.25) {
			pretend = Biome.TAIGA;
		} else if (temp <= 0.3) {
			pretend = Biome.GIANT_TREE_TAIGA;
		} else if (temp <= 0.5) {
			pretend = Biome.RIVER;
		} else if (temp <= 0.6) {
			pretend = Biome.BIRCH_FOREST;
		} else if (temp <= 0.7) {
			pretend = Biome.FOREST;
		} else if (temp <= 0.8) {
			pretend = Biome.PLAINS;
		} else if (temp <= 0.95) {
			pretend = Biome.JUNGLE;
		} else if (temp <= 1.1) {
			pretend = Biome.SHATTERED_SAVANNA;
		} else if (temp <= 1.2) {
			pretend = Biome.SAVANNA;
		} else if (temp <= 2) {
			pretend = Biome.DESERT;
		}
		pretend_biome.put(biome, pretend);
	}

	public float get_custom_temp(Biome biome) {
		Float temp = this.custom_temp.get(biome);
		if (temp == null) {
			return original_temp.get(biome);
		}
		return temp;
	}

	public Biome get_pretend_biome(Biome biome) {
		Biome pretend = this.pretend_biome.get(biome);
		if (pretend == null) {
			return biome;
		}
		return pretend;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("biome_modify")) {
			if (args.length == 1) {
				return Arrays.asList(this.subcommand_list);
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("biome_modify")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("[生物群系修改器]只能由玩家执行");
				return true;
			}
			Player player = (Player) sender;
			if (args[0].equalsIgnoreCase("show_biome_temp")) {
				Biome biome = player.getLocation().getBlock().getBiome();
				float temp = NMS_manager.biome_modifier.get_temperature(biome);
				float origin_temp = original_temp.get(biome);
				player.sendMessage(String.format("[生物群系修改器]%s的当前的基础温度为:%.2f, 原版基础温度为:%.2f", biome.getKey().toString(),
						temp, origin_temp));
				return true;
			} else if (args[0].equalsIgnoreCase("set_biome_temp")) {
				if (!player.hasPermission("biome_modify.set_biome_temp")) {
					player.sendMessage("[生物群系修改器]你没有设置温度的权限!");
					return true;
				}
				Biome biome = player.getLocation().getBlock().getBiome();
				try {
					float temp = Float.parseFloat(args[1]);
					this.set_custom_temp(biome, temp);
					player.sendMessage("[生物群系修改器]" + biome.getKey().toString() + "的基础温度已设置为:" + temp);
				} catch (Exception e) {
					player.sendMessage("[生物群系修改器]格式错误");
				}
				return true;
			} else if (args[0].equalsIgnoreCase("winter_mode")) {
				if (!player.hasPermission("biome_modify.set_biome_temp")) {
					player.sendMessage("[生物群系修改器]你没有设置温度的权限!");
					return true;
				}
				try {
					boolean enable = Boolean.parseBoolean(args[1]);
					if (enable) {
						player.sendMessage("[生物群系修改器]已开启冬天模式");
						this.winter_mode.enable(this);
					} else {
						player.sendMessage("[生物群系修改器]已关闭冬天模式");
						this.winter_mode.disable(this);
					}
					return true;
				} catch (Exception e) {
					player.sendMessage("[生物群系修改器]格式错误(true/false)");
					return true;
				}
			}
		}
		return false;
	}

	public Winter_mode get_winter_mode() {
		return this.winter_mode;
	}
}
