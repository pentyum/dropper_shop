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

	/*
	 * private String formatInfoWindow(Teleport_machine res) { if (res == null)
	 * return null; if (res.getCustomName() == null) return null; String v =
	 * "<div class=\"regioninfo\"><div class=\"infowindow\"><span style=\"font-size:140%;font-weight:bold;\">%regionname%</span><br /> "
	 * + ChatColor.stripColor(plugin.msg(lm.General_Owner, "")) +
	 * "<span style=\"font-weight:bold;\">%playerowners%</span><br />";
	 * 
	 * if (plugin.getConfigManager().DynMapShowFlags) v +=
	 * ChatColor.stripColor(plugin.msg(lm.General_ResidenceFlags, "")) +
	 * "<br /><span style=\"font-weight:bold;\">%flags%</span>"; v +=
	 * "</div></div>";
	 * 
	 * if (plugin.getRentManager().isForRent(res.getCustomName())) v =
	 * "<div class=\"regioninfo\"><div class=\"infowindow\">" +
	 * ChatColor.stripColor(plugin.msg(lm.Rentable_Land, "")) +
	 * "<span style=\"font-size:140%;font-weight:bold;\">%regionname%</span><br />"
	 * + ChatColor.stripColor(plugin.msg(lm.General_Owner, "")) +
	 * "<span style=\"font-weight:bold;\">%playerowners%</span><br />" +
	 * ChatColor.stripColor(plugin.msg(lm.Residence_RentedBy, "")) +
	 * "<span style=\"font-weight:bold;\">%renter%</span><br /> " +
	 * ChatColor.stripColor(plugin.msg(lm.General_LandCost, "")) +
	 * "<span style=\"font-weight:bold;\">%rent%</span><br /> " +
	 * ChatColor.stripColor(plugin.msg(lm.Rent_Days, "")) +
	 * "<span style=\"font-weight:bold;\">%rentdays%</span><br /> " +
	 * ChatColor.stripColor(plugin.msg(lm.Rentable_AllowRenewing, "")) +
	 * "<span style=\"font-weight:bold;\">%renew%</span><br /> " +
	 * ChatColor.stripColor(plugin.msg(lm.Rent_Expire, "")) +
	 * "<span style=\"font-weight:bold;\">%expire%</span></div></div>";
	 * 
	 * if (plugin.getTransactionManager().isForSale(res.getCustomName())) v =
	 * "<div class=\"regioninfo\"><div class=\"infowindow\">" +
	 * ChatColor.stripColor(plugin.msg(lm.Economy_LandForSale, " ")) +
	 * "<span style=\"font-size:140%;font-weight:bold;\">%regionname%</span><br /> "
	 * + ChatColor.stripColor(plugin.msg(lm.General_Owner, "")) +
	 * "<span style=\"font-weight:bold;\">%playerowners%</span><br />" +
	 * ChatColor.stripColor(plugin.msg(lm.Economy_SellAmount, "")) +
	 * "<span style=\"font-weight:bold;\">%price%</span><br /></div></div>";
	 * 
	 * v = v.replace("%regionname%", resName); v = v.replace("%playerowners%",
	 * res.getOwner()); String m = res.getEnterMessage(); v =
	 * v.replace("%entermsg%", (m != null) ? m : ""); m = res.getLeaveMessage(); v =
	 * v.replace("%leavemsg%", (m != null) ? m : ""); ResidencePermissions p =
	 * res.getPermissions(); String flgs = "";
	 * 
	 * // remake Map<String, Boolean> all =
	 * plugin.getPermissionManager().getAllFlags().getFlags(); String[] FLAGS = new
	 * String[all.size()]; int ii = 0; for (Entry<String, Boolean> one :
	 * all.entrySet()) { FLAGS[ii] = one.getKey(); ii++; }
	 * 
	 * for (int i = 0; i < FLAGS.length; i++) { if (p.isSet(FLAGS[i])) { if
	 * (flgs.length() > 0) flgs += "<br/>"; boolean f = p.has(FLAGS[i], false); flgs
	 * += FLAGS[i] + ": " + f; v = v.replace("%flag." + FLAGS[i] + "%",
	 * Boolean.toString(f)); } else v = v.replace("%flag." + FLAGS[i] + "%", ""); }
	 * v = v.replace("%flags%", flgs); RentManager rentmgr =
	 * plugin.getRentManager(); TransactionManager transmgr =
	 * plugin.getTransactionManager();
	 * 
	 * if (rentmgr.isForRent(res.getCustomName())) { boolean isrented =
	 * rentmgr.isRented(resid); v = v.replace("%isrented%",
	 * Boolean.toString(isrented)); String id = ""; if (isrented) id =
	 * rentmgr.getRentingPlayer(resid); v = v.replace("%renter%", id);
	 * 
	 * v = v.replace("%rent%", rentmgr.getCostOfRent(resid) + ""); v =
	 * v.replace("%rentdays%", rentmgr.getRentDays(resid) + ""); boolean renew =
	 * rentmgr.getRentableRepeatable(resid); v = v.replace("%renew%", renew + "");
	 * String expire = ""; if (isrented) { long time =
	 * rentmgr.getRentedLand(resid).endTime; if (time != 0L) expire =
	 * GetTime.getTime(time); } v = v.replace("%expire%", expire); }
	 * 
	 * if (transmgr.isForSale(res.getCustomName())) { boolean forsale =
	 * transmgr.isForSale(resid); v = v.replace("%isforsale%",
	 * Boolean.toString(transmgr.isForSale(resid))); String price = ""; if (forsale)
	 * price = Integer.toString(transmgr.getSaleAmount(resid)); v =
	 * v.replace("%price%", price); }
	 * 
	 * return v; }
	 * 
	 * private void addStyle(Teleport_machine res, Marker m) { AreaStyle as = new
	 * AreaStyle(); int sc = 0xFF0000; int fc = 0xFF0000; try { sc =
	 * Integer.parseInt(as.strokecolor.substring(1), 16); if
	 * (plugin.getRentManager().isForRent(resid) &&
	 * !plugin.getRentManager().isRented(resid)) fc =
	 * Integer.parseInt(as.forrentstrokecolor.substring(1), 16); else if
	 * (plugin.getRentManager().isForRent(resid) &&
	 * plugin.getRentManager().isRented(resid)) fc =
	 * Integer.parseInt(as.rentedstrokecolor.substring(1), 16); else if
	 * (plugin.getTransactionManager().isForSale(resid)) fc =
	 * Integer.parseInt(as.forsalestrokecolor.substring(1), 16); else fc =
	 * Integer.parseInt(as.fillcolor.substring(1), 16); } catch
	 * (NumberFormatException nfx) { } m.setLineStyle(as.strokeweight,
	 * as.strokeopacity, sc); m.setFillStyle(as.fillopacity, fc); m.setRangeY(as.y,
	 * as.y); }
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

		// String desc = formatInfoWindow(res);

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
		// marker.setDescription(desc);
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