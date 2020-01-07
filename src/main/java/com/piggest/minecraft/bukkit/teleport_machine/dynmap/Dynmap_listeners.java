package com.piggest.minecraft.bukkit.teleport_machine.dynmap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

public class Dynmap_listeners implements Listener {

    private Dropper_shop_plugin plugin;

    public Dynmap_listeners(Dropper_shop_plugin plugin) {
	this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceAreaAdd(ResidenceAreaAddEvent event) {
	plugin.getDynManager().fireUpdateAdd(event.getResidence(), event.getResidence().getSubzoneDeep());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceAreaDelete(ResidenceAreaDeleteEvent event) {
	plugin.getDynManager().fireUpdateRemove(event.getResidence(), event.getResidence().getSubzoneDeep());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceSubZoneCreate(ResidenceSubzoneCreationEvent event) {
	plugin.getDynManager().fireUpdateAdd(event.getResidence(), event.getResidence().getSubzoneDeep());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceFlagChange(ResidenceFlagChangeEvent event) {
	plugin.getDynManager().fireUpdateAdd(event.getResidence(), event.getResidence().getSubzoneDeep());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceDelete(ResidenceDeleteEvent event) {
	plugin.getDynManager().fireUpdateRemove(event.getResidence(), event.getResidence().getSubzoneDeep());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceOwnerChange(ResidenceOwnerChangeEvent event) {
	plugin.getDynManager().fireUpdateAdd(event.getResidence(), event.getResidence().getSubzoneDeep());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceRename(ResidenceRenameEvent event) {
	plugin.getDynManager().handleResidenceRemove(event.getOldResidenceName(), event.getResidence(), event.getResidence().getSubzoneDeep());
	plugin.getDynManager().fireUpdateAdd(event.getResidence(), event.getResidence().getSubzoneDeep());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceRent(ResidenceRentEvent event) {
	plugin.getDynManager().fireUpdateAdd(event.getResidence(), event.getResidence().getSubzoneDeep());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResidenceSizeChange(ResidenceSizeChangeEvent event) {
	plugin.getDynManager().fireUpdateAdd(event.getResidence(), event.getResidence().getSubzoneDeep());
    }
}