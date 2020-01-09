package com.piggest.minecraft.bukkit.teleport_machine.dynmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.ChatColor;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine_manager;

public class Dynmap_manager {
	Teleport_machine_manager teleport_machine_manager;

	public DynmapAPI api;
	MarkerAPI markerapi;
	MarkerSet set;
	private Map<UUID, Marker> resareas = new HashMap<UUID, Marker>();
	private int schedId = -1;

	public Dynmap_manager(Teleport_machine_manager teleport_machine_manager) {
		this.teleport_machine_manager = teleport_machine_manager;
		Plugin dynmap = Bukkit.getPluginManager().getPlugin("dynmap");
		this.api = (DynmapAPI) dynmap;
	}

	public MarkerSet getMarkerSet() {
		return set;
	}

	private String formatInfoWindow(Teleport_machine res) {
		if (res == null)
			return null;
		if (res.getCustomName() == null)
			return null;
		String v = "<div class=\"regioninfo\"><div class=\"infowindow\"><span style=\"font-size:140%;font-weight:bold;\">%name%</span><br /> ";
		v += "所有者: " + "<span style=\"font-weight:bold;\">%playerowners%</span><br />";
		v += "天线长度: " + "<span style=\"font-weight:bold;\">%length%</span><br />";
		v += "频率信息: " + "<br /><span style=\"font-weight:bold;\">";
		v += "中心波长: <br />";
		v += "带宽: ";
		v += "</span>";
		v += "</div></div>";

		v = v.replace("%name%", res.getCustomName());
		// v = v.replace("%playerowners%", res.getOwner());
		return v;
	}
	/*
	private void addStyle(Teleport_machine res, Marker m) {
		AreaStyle as = new AreaStyle();
		int sc = 0xFF0000;
		int fc = 0xFF0000;
		try {
			sc = Integer.parseInt(as.strokecolor.substring(1), 16);
			if (plugin.getRentManager().isForRent(resid) && !plugin.getRentManager().isRented(resid))
				fc = Integer.parseInt(as.forrentstrokecolor.substring(1), 16);
			else if (plugin.getRentManager().isForRent(resid) && plugin.getRentManager().isRented(resid))
				fc = Integer.parseInt(as.rentedstrokecolor.substring(1), 16);
			else if (plugin.getTransactionManager().isForSale(resid))
				fc = Integer.parseInt(as.forsalestrokecolor.substring(1), 16);
			else
				fc = Integer.parseInt(as.fillcolor.substring(1), 16);
		} catch (NumberFormatException nfx) {
		}
		m.setLineStyle(as.strokeweight, as.strokeopacity, sc);
		m.setFillStyle(as.fillopacity, fc);
		m.setRangeY(as.y, as.y);
	}
	*/
	public void handle_teleport_machine_add(Teleport_machine res) {

		if (res == null) {
			return;
		}

		String name = res.getCustomName();
		Location loc = res.get_location();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();

		String desc = formatInfoWindow(res);

		Marker marker = null;
		MarkerIcon icon = markerapi.getMarkerIcon("compass");
		if (resareas.containsKey(res.get_uuid())) {
			marker = resareas.get(res.get_uuid());
			resareas.remove(res.get_uuid());
			marker.deleteMarker();
		}
		marker = set.createMarker(res.get_uuid().toString(), name, true, res.get_world_name(), x, y, z, icon, true);
		if (marker == null) {
			Dropper_shop_plugin.instance.getLogger().warning("Marker创建失败");
			return;
		}
		marker.setDescription(desc);
		// addStyle(res, marker);
		resareas.put(res.get_uuid(), marker);
	}

	public void handle_teleport_machine_rename(Teleport_machine res) {
		if (res == null) {
			return;
		}

		Marker marker = resareas.get(res.get_uuid());
		if (marker == null) {
			return;
		}
		String new_name = res.getCustomName();
		marker.setLabel(new_name, true);
	}

	public void handle_teleport_machine_remove(Teleport_machine res) {
		if (res == null)
			return;
		UUID id = res.get_uuid();
		if (resareas.containsKey(id)) {
			Marker marker = resareas.remove(id);
			marker.deleteMarker();
		}
	}

	public void activate() {
		try {
			markerapi = api.getMarkerAPI();
		} catch (Exception e) {
		}
		if (markerapi == null) {
			Dropper_shop_plugin.instance.getLogger().warning("dynmap api 加载错误!");
			return;
		}
		if (set != null) {
			set.deleteMarkerSet();
			set = null;
		}
		set = markerapi.getMarkerSet("teleport_machine.markerset");
		if (set == null)
			set = markerapi.createMarkerSet("teleport_machine.markerset", "传送机", null, false);
		else
			set.setMarkerSetLabel("传送机");

		if (set == null) {
			Dropper_shop_plugin.instance.getLogger().warning("Error creating marker set");
			return;
		}
		set.setLayerPriority(1);
		set.setHideByDefault(false);

		Dropper_shop_plugin.instance.getLogger().info("传送机 DynMap 支持已启动!");
	}
}