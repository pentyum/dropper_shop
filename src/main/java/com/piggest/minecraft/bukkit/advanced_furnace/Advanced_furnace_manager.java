package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Advanced_furnace_manager extends Gui_structure_manager {
	public static Advanced_furnace_manager instance = null;

	public Advanced_furnace_manager() {
		super(Advanced_furnace.class);
		Advanced_furnace_manager.instance = this;
		this.set_gui(10, Material.BLUE_STAINED_GLASS_PANE, "§r左边放固体原料", Gui_slot_type.Indicator);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r左边放气体原料", Gui_slot_type.Indicator);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r左边放液体原料", Gui_slot_type.Indicator);
		this.set_gui(16, Material.BLUE_STAINED_GLASS_PANE, "§r右边放燃料", Gui_slot_type.Indicator);
		this.set_gui(19, Material.BLUE_STAINED_GLASS_PANE, "§r左边为固体产品", Gui_slot_type.Indicator);
		this.set_gui(21, Material.BLUE_STAINED_GLASS_PANE, "§r左边为气体产品", Gui_slot_type.Indicator);
		this.set_gui(23, Material.BLUE_STAINED_GLASS_PANE, "§r左边为液体产品", Gui_slot_type.Indicator);
		this.set_gui(25, Material.BLUE_STAINED_GLASS_PANE, "§r右边为温度", Gui_slot_type.Indicator);
		this.set_gui(0, Material.CRAFTING_TABLE, "§e内部信息", Gui_slot_type.Indicator);
		this.set_gui(2, Material.HOPPER_MINECART, "§e固体产品自动提取", Gui_slot_type.Switch);
		this.set_gui(3, Material.CHEST_MINECART, "§r立刻取出固体", Gui_slot_type.Button);
		this.set_gui(4, Material.MINECART, "§r清除全部固体", Gui_slot_type.Button);
		this.set_gui(5, Material.GLASS_BOTTLE, "§e敞口反应", Gui_slot_type.Switch);
		this.set_gui(6, Material.DISPENSER, "§r清除全部气体", Gui_slot_type.Button);
		this.set_gui(8, Material.CHEST, "§e金币制造", Gui_slot_type.Switch);
		this.set_gui(26, Material.FURNACE, "§e信息", Gui_slot_type.Indicator);
	}

	public Advanced_furnace find(Location loc, boolean new_deop) {
		int x;
		int y;
		int z;
		Advanced_furnace adv_furnace;
		for (x = -1; x <= 1; x++) {
			for (y = -1; y <= 1; y++) {
				for (z = -1; z <= 1; z++) {
					Location check_loc = loc.clone().add(x, y, z);
					Material material = check_loc.getBlock().getType();
					// Bukkit.getLogger().info("正在搜索"+check_loc.toString());
					if (material == Material.FURNACE) {
						// Bukkit.getLogger().info("在" + check_loc.toString() + "找到了末地烛");
						if (new_deop == true) {
							adv_furnace = new Advanced_furnace();
							adv_furnace.set_location(check_loc);
							adv_furnace.set_temperature(adv_furnace.get_base_temperature());
							if (adv_furnace.completed() > 0) {
								return adv_furnace;
							}
						} else {
							adv_furnace = this.get(check_loc);
							if (adv_furnace != null) {
								return adv_furnace;
							}
						}
					}
				}
			}
		}
		return null;
	}

	@Override
	public Advanced_furnace find(String player_name, Location loc, boolean new_structure) {
		return this.find(loc, new_structure);
	}
	
	public Advanced_furnace get(Location location) {
		return (Advanced_furnace) super.get(location);
	}
	
	@Override
	public String get_gui_name() {
		return "高级熔炉";
	}

	@Override
	public int get_slot_num() {
		return 27;
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int[] get_process_bar() {
		return NO_BAR;
	}

	@Override
	public String get_permission_head() {
		return "adv_furnace";
	}
}
