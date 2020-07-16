package com.piggest.minecraft.bukkit.sync_realtime;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Server_date;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Calendar;

public class Sync_realtime_placeholder_expansion extends PlaceholderExpansion {
	private Dropper_shop_plugin plugin;

	public Sync_realtime_placeholder_expansion(Dropper_shop_plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public boolean canRegister() {
		return true;
	}

	@Override
	public String getAuthor() {
		return plugin.getDescription().getAuthors().toString();
	}

	/**
	 * The placeholder identifier should go here. <br>
	 * This is what tells PlaceholderAPI to call our onRequest method to obtain a
	 * value if a placeholder starts with our identifier. <br>
	 * This must be unique and can not contain % or _
	 *
	 * @return The identifier in {@code %<identifier>_<value>%} as String.
	 */
	@Override
	public String getIdentifier() {
		return "droppershop.syncrealtime";
	}

	@Override
	public String getVersion() {
		return plugin.getDescription().getVersion();
	}

	/**
	 * This is the method called when a placeholder with our identifier is found and
	 * needs a value. <br>
	 * We specify the value identifier in this method. <br>
	 * Since version 2.9.1 can you use OfflinePlayers in your requests.
	 *
	 * @param player     A {@link org.bukkit.Player Player}.
	 * @param identifier A String containing the identifier/value.
	 * @return possibly-null String of the requested identifier.
	 */
	@Override
	public String onPlaceholderRequest(Player player, String identifier) {
		String[] values = identifier.split(".");
		if (values.length > 0) {
			World world = null;
			if (values.length > 1) {
				world = Bukkit.getWorld(values[1]);
				if (world == null) {
					return null;
				}
			}
			Calendar date = Server_date.get_world_date(world);
			switch (values[0]) {
				case "year":
					return String.valueOf(date.get(Calendar.YEAR));
				case "month":
					return String.valueOf(date.get(Calendar.MONTH));
				case "day":
					return String.valueOf(date.get(Calendar.DAY_OF_MONTH));
				case "hour":
					return String.valueOf(date.get(Calendar.HOUR_OF_DAY));
				case "hour12":
					return String.valueOf(date.get(Calendar.HOUR));
				case "hour24":
					return String.valueOf(date.get(Calendar.HOUR_OF_DAY));
				case "minute":
					return String.valueOf(date.get(Calendar.MINUTE));
				case "second":
					return String.valueOf(date.get(Calendar.SECOND));
			}
		}
		return null;
	}

}
