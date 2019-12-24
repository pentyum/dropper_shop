package com.piggest.minecraft.bukkit.lottery_pool;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Lotter_pool_info_gui implements Inventory {
	private Inventory internal;

	public Lotter_pool_info_gui(Player player) {
		internal = Bukkit.createInventory(player, 36, "奖品列表");
	}

	public int getSize() {
		return internal.getSize();
	}

	public int getMaxStackSize() {
		return internal.getMaxStackSize();
	}

	public void setMaxStackSize(int size) {
		internal.setMaxStackSize(size);
	}

	public ItemStack getItem(int index) {
		return internal.getItem(index);
	}

	public void setItem(int index, ItemStack item) {
		internal.setItem(index, item);
	}

	public HashMap<Integer, ItemStack> addItem(ItemStack... items) throws IllegalArgumentException {
		return internal.addItem(items);
	}

	public HashMap<Integer, ItemStack> removeItem(ItemStack... items) throws IllegalArgumentException {
		return internal.removeItem(items);
	}

	public ItemStack[] getContents() {
		return internal.getContents();
	}

	public void setContents(ItemStack[] items) throws IllegalArgumentException {
		internal.setContents(items);
	}

	public ItemStack[] getStorageContents() {
		return internal.getStorageContents();
	}

	public void setStorageContents(ItemStack[] items) throws IllegalArgumentException {
		internal.setStorageContents(items);
	}

	public boolean contains(Material material) throws IllegalArgumentException {
		return internal.contains(material);
	}

	public boolean contains(ItemStack item) {
		return internal.contains(item);
	}

	public boolean contains(Material material, int amount) throws IllegalArgumentException {
		return internal.contains(material, amount);
	}

	public boolean contains(ItemStack item, int amount) {
		return internal.contains(item, amount);
	}

	public boolean containsAtLeast(ItemStack item, int amount) {
		return internal.containsAtLeast(item, amount);
	}

	public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
		return internal.all(material);
	}

	public HashMap<Integer, ? extends ItemStack> all(ItemStack item) {
		return internal.all(item);
	}

	public int first(Material material) throws IllegalArgumentException {
		return internal.first(material);
	}

	public int first(ItemStack item) {
		return internal.first(item);
	}

	public int firstEmpty() {
		return internal.firstEmpty();
	}

	public void remove(Material material) throws IllegalArgumentException {
		internal.remove(material);
	}

	public void remove(ItemStack item) {
		internal.remove(item);
	}

	public void clear(int index) {
		internal.clear(index);
	}

	public void clear() {
		internal.clear();
	}

	public List<HumanEntity> getViewers() {
		return internal.getViewers();
	}

	public InventoryType getType() {
		return internal.getType();
	}

	public InventoryHolder getHolder() {
		return internal.getHolder();
	}

	public ListIterator<ItemStack> iterator() {
		return internal.iterator();
	}

	public ListIterator<ItemStack> iterator(int index) {
		return internal.iterator(index);
	}

	public Location getLocation() {
		return internal.getLocation();
	}
}
