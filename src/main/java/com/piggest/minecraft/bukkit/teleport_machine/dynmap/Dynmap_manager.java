package com.piggest.minecraft.bukkit.teleport_machine.dynmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.ChatColor;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine;
import com.piggest.minecraft.bukkit.teleport_machine.Teleport_machine_manager;

public class Dynmap_manager {
	Teleport_machine_manager teleport_machine_manager;

	public DynmapAPI api;
	MarkerAPI markerapi;
	MarkerSet set;
	private Map<String, AreaMarker> resareas = new HashMap<String, AreaMarker>();
	private int schedId = -1;

	public Dynmap_manager(Teleport_machine_manager teleport_machine_manager) {
		this.teleport_machine_manager = teleport_machine_manager;
	}

	public MarkerSet getMarkerSet() {
		return set;
	}

	public void fireUpdateAdd(final Teleport_machine res, final int deep) {
		if (api == null || set == null)
			return;
		if (res == null)
			return;

		if (schedId != -1)
			Bukkit.getServer().getScheduler().cancelTask(schedId);

		schedId = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Dropper_shop_plugin.instance,
				new Runnable() {
					@Override
					public void run() {
						schedId = -1;

						handle_teleport_machine_add(res.getCustomName(), res, deep);
						return;
					}
				}, 10L);
	}

	public void fireUpdateRemove(final Teleport_machine res, final int deep) {
		if (api == null || set == null)
			return;
		if (res == null)
			return;

		handle_teleport_machine_remove(res.getCustomName(), res, deep);
	}

	private String formatInfoWindow(String resid, Teleport_machine res, String resName) {
		if (res == null)
			return null;
		if (res.getCustomName() == null)
			return null;
		String v = "<div class=\"regioninfo\"><div class=\"infowindow\"><span style=\"font-size:140%;font-weight:bold;\">%regionname%</span><br /> "
				+ ChatColor.stripColor(plugin.msg(lm.General_Owner, ""))
				+ "<span style=\"font-weight:bold;\">%playerowners%</span><br />";

		if (plugin.getConfigManager().DynMapShowFlags)
			v += ChatColor.stripColor(plugin.msg(lm.General_ResidenceFlags, ""))
					+ "<br /><span style=\"font-weight:bold;\">%flags%</span>";
		v += "</div></div>";

		if (plugin.getRentManager().isForRent(res.getCustomName()))
			v = "<div class=\"regioninfo\"><div class=\"infowindow\">"
					+ ChatColor.stripColor(plugin.msg(lm.Rentable_Land, ""))
					+ "<span style=\"font-size:140%;font-weight:bold;\">%regionname%</span><br />"
					+ ChatColor.stripColor(plugin.msg(lm.General_Owner, ""))
					+ "<span style=\"font-weight:bold;\">%playerowners%</span><br />"
					+ ChatColor.stripColor(plugin.msg(lm.Residence_RentedBy, ""))
					+ "<span style=\"font-weight:bold;\">%renter%</span><br /> "
					+ ChatColor.stripColor(plugin.msg(lm.General_LandCost, ""))
					+ "<span style=\"font-weight:bold;\">%rent%</span><br /> "
					+ ChatColor.stripColor(plugin.msg(lm.Rent_Days, ""))
					+ "<span style=\"font-weight:bold;\">%rentdays%</span><br /> "
					+ ChatColor.stripColor(plugin.msg(lm.Rentable_AllowRenewing, ""))
					+ "<span style=\"font-weight:bold;\">%renew%</span><br /> "
					+ ChatColor.stripColor(plugin.msg(lm.Rent_Expire, ""))
					+ "<span style=\"font-weight:bold;\">%expire%</span></div></div>";

		if (plugin.getTransactionManager().isForSale(res.getCustomName()))
			v = "<div class=\"regioninfo\"><div class=\"infowindow\">"
					+ ChatColor.stripColor(plugin.msg(lm.Economy_LandForSale, " "))
					+ "<span style=\"font-size:140%;font-weight:bold;\">%regionname%</span><br /> "
					+ ChatColor.stripColor(plugin.msg(lm.General_Owner, ""))
					+ "<span style=\"font-weight:bold;\">%playerowners%</span><br />"
					+ ChatColor.stripColor(plugin.msg(lm.Economy_SellAmount, ""))
					+ "<span style=\"font-weight:bold;\">%price%</span><br /></div></div>";

		v = v.replace("%regionname%", resName);
		v = v.replace("%playerowners%", res.getOwner());
		String m = res.getEnterMessage();
		v = v.replace("%entermsg%", (m != null) ? m : "");
		m = res.getLeaveMessage();
		v = v.replace("%leavemsg%", (m != null) ? m : "");
		ResidencePermissions p = res.getPermissions();
		String flgs = "";

		// remake
		Map<String, Boolean> all = plugin.getPermissionManager().getAllFlags().getFlags();
		String[] FLAGS = new String[all.size()];
		int ii = 0;
		for (Entry<String, Boolean> one : all.entrySet()) {
			FLAGS[ii] = one.getKey();
			ii++;
		}

		for (int i = 0; i < FLAGS.length; i++) {
			if (p.isSet(FLAGS[i])) {
				if (flgs.length() > 0)
					flgs += "<br/>";
				boolean f = p.has(FLAGS[i], false);
				flgs += FLAGS[i] + ": " + f;
				v = v.replace("%flag." + FLAGS[i] + "%", Boolean.toString(f));
			} else
				v = v.replace("%flag." + FLAGS[i] + "%", "");
		}
		v = v.replace("%flags%", flgs);
		RentManager rentmgr = plugin.getRentManager();
		TransactionManager transmgr = plugin.getTransactionManager();

		if (rentmgr.isForRent(res.getCustomName())) {
			boolean isrented = rentmgr.isRented(resid);
			v = v.replace("%isrented%", Boolean.toString(isrented));
			String id = "";
			if (isrented)
				id = rentmgr.getRentingPlayer(resid);
			v = v.replace("%renter%", id);

			v = v.replace("%rent%", rentmgr.getCostOfRent(resid) + "");
			v = v.replace("%rentdays%", rentmgr.getRentDays(resid) + "");
			boolean renew = rentmgr.getRentableRepeatable(resid);
			v = v.replace("%renew%", renew + "");
			String expire = "";
			if (isrented) {
				long time = rentmgr.getRentedLand(resid).endTime;
				if (time != 0L)
					expire = GetTime.getTime(time);
			}
			v = v.replace("%expire%", expire);
		}

		if (transmgr.isForSale(res.getCustomName())) {
			boolean forsale = transmgr.isForSale(resid);
			v = v.replace("%isforsale%", Boolean.toString(transmgr.isForSale(resid)));
			String price = "";
			if (forsale)
				price = Integer.toString(transmgr.getSaleAmount(resid));
			v = v.replace("%price%", price);
		}

		return v;
	}

	private boolean isVisible(String id, String worldname) {
		List<String> visible = plugin.getConfigManager().DynMapVisibleRegions;
		List<String> hidden = plugin.getConfigManager().DynMapHiddenRegions;
		if (visible != null && visible.size() > 0) {
			if ((visible.contains(id) == false) && (visible.contains("world:" + worldname) == false)) {
				return false;
			}
		}
		if (hidden != null && hidden.size() > 0) {
			if (hidden.contains(id) || hidden.contains("world:" + worldname))
				return false;
		}
		return true;
	}

	private void addStyle(String resid, AreaMarker m) {
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

	private void handle_teleport_machine_add(String resid, Teleport_machine res, int depth) {

		if (res == null)
			return;

		for (Entry<String, CuboidArea> oneArea : res.getAreaMap().entrySet()) {

			String id = oneArea.getKey() + "." + resid;

			String name = res.getCustomName();
			double[] x = new double[4];
			double[] z = new double[4];

			String resName = res.getCustomName();

			if (res.getAreaMap().size() > 1) {
				resName = res.getCustomName() + " (" + oneArea.getKey() + ")";
			}

			String desc = formatInfoWindow(resid, res, resName);

			if (!isVisible(resid, res.getWorld()))
				return;

			Location l0 = oneArea.getValue().getLowLoc();
			Location l1 = oneArea.getValue().getHighLoc();

//	    x[0] = l0.getX();
//	    z[0] = l0.getZ();
//	    x[1] = l1.getX() + 1.0;
//	    z[1] = l1.getZ() + 1.0;

			x[0] = l0.getX();
			z[0] = l0.getZ();
			x[1] = l0.getX();
			z[1] = l1.getZ() + 1.0;
			x[2] = l1.getX() + 1.0;
			z[2] = l1.getZ() + 1.0;
			x[3] = l1.getX() + 1.0;
			z[3] = l0.getZ();

			AreaMarker marker = null;

			if (resareas.containsKey(id)) {
				marker = resareas.get(id);
				resareas.remove(id);
				marker.deleteMarker();
			}

			marker = set.createAreaMarker(id, name, true, res.getWorld(), x, z, true);
			if (marker == null)
				return;

			if (plugin.getConfigManager().DynMapLayer3dRegions)
				marker.setRangeY(l1.getY(), l0.getY());

			marker.setDescription(desc);
			addStyle(resid, marker);
			resareas.put(id, marker);

			if (depth <= plugin.getConfigManager().DynMapLayerSubZoneDepth) {
				List<ClaimedResidence> subids = res.getSubzones();
				for (ClaimedResidence one : subids) {
					handleResidenceAdd(one.getName(), one, depth + 1);
				}
			}
		}
	}

	public void handle_teleport_machine_remove(String resid, Teleport_machine res, int depth) {

		if (resid == null)
			return;
		if (res == null)
			return;

		for (Entry<String, CuboidArea> oneArea : res.getAreaMap().entrySet()) {
			String id = oneArea.getKey() + "." + resid;
			if (resareas.containsKey(id)) {
				AreaMarker marker = resareas.remove(id);
				marker.deleteMarker();
			}
			if (depth <= plugin.getConfigManager().DynMapLayerSubZoneDepth + 1) {
				List<ClaimedResidence> subids = res.getSubzones();
				for (ClaimedResidence one : subids) {
					handleResidenceRemove(one.getName(), one, depth + 1);
				}
			}
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
			set = markerapi.createMarkerSet("teleport_machine.markerset", "teleport_machine", null, false);
		else
			set.setMarkerSetLabel("teleport_machine");

		if (set == null) {
			Dropper_shop_plugin.instance.getLogger().warning("Error creating marker set");
			return;
		}
		set.setLayerPriority(1);
		set.setHideByDefault(false);

		Dropper_shop_plugin.instance.getLogger().info("传送机 DynMap 支持已启动!");

		for (Teleport_machine machine : this.teleport_machine_manager) {
			fireUpdateAdd(machine, machine.getSubzoneDeep());
			handle_teleport_machine_add(machine.getCustomName(), machine, machine.getSubzoneDeep());
		}
	}
}