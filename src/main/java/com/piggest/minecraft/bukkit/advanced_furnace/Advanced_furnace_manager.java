package com.piggest.minecraft.bukkit.advanced_furnace;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import com.piggest.minecraft.bukkit.structure.Has_runner;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import javax.annotation.Nonnull;

public class Advanced_furnace_manager extends Gui_structure_manager<Advanced_furnace> implements Has_runner {
	public static Advanced_furnace_manager instance = null;
	private final Material[][][] model = {
			{{Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK},
					{Material.IRON_BLOCK, Material.IRON_BLOCK, Material.IRON_BLOCK},
					{Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK}},
			{{Material.IRON_BLOCK, Material.IRON_BARS, Material.IRON_BLOCK},
					{Material.IRON_BARS, Material.FURNACE, Material.IRON_BARS},
					{Material.IRON_BLOCK, Material.IRON_BARS, Material.IRON_BLOCK}},
			{{Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK},
					{Material.IRON_BLOCK, null, Material.IRON_BLOCK},
					{Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.GOLD_BLOCK}}};
	private final int[] center = new int[]{1, 1, 1};

	public Advanced_furnace_manager() {
		super(Advanced_furnace.class);
		Advanced_furnace_manager.instance = this;
		this.set_gui(1, Material.BLUE_STAINED_GLASS_PANE, "§r说明",
				new String[]{"§7层数为从下面往上数", "§7温度下降速率由傅里叶热传导定律和普朗克黑体辐射定律决定", "§7熔炉反应速率由阿伦尼乌斯定律决定", "§7发钱效率由卡诺热机定律决定"},
				Gui_slot_type.Indicator);
		this.set_gui(10, Material.BLUE_STAINED_GLASS_PANE, "§r左边放固体原料", new String[]{"§7在第3层中间放置漏斗可以自动添加"},
				Gui_slot_type.Indicator);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r左边放气体原料", Gui_slot_type.Indicator);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r左边放液体原料", Gui_slot_type.Indicator);
		this.set_gui(16, Material.BLUE_STAINED_GLASS_PANE, "§r右边放燃料", new String[]{"§7在第1层中间放置漏斗可以自动添加"},
				Gui_slot_type.Indicator);
		this.set_gui(19, Material.BLUE_STAINED_GLASS_PANE, "§r左边为固体产品", new String[]{"§7在第1层金块边上放置箱子可以自动输出"},
				Gui_slot_type.Indicator);
		this.set_gui(21, Material.BLUE_STAINED_GLASS_PANE, "§r左边为气体产品", Gui_slot_type.Indicator);
		this.set_gui(23, Material.BLUE_STAINED_GLASS_PANE, "§r左边为液体产品", Gui_slot_type.Indicator);
		this.set_gui(25, Material.BLUE_STAINED_GLASS_PANE, "§r右边为温度", Gui_slot_type.Indicator);
		this.set_gui(0, Material.CRAFTING_TABLE, "§e内部信息", Gui_slot_type.Indicator);
		this.set_gui(2, Material.HOPPER_MINECART, "§e固体产品自动提取", Gui_slot_type.Switch);
		this.set_gui(3, Material.CHEST_MINECART, "§r立刻取出固体", Gui_slot_type.Button);
		this.set_gui(4, Material.MINECART, "§r清除全部固体", Gui_slot_type.Button);
		this.set_gui(5, Material.GLASS_BOTTLE, "§e敞口反应", Gui_slot_type.Switch);
		this.set_gui(6, Material.DISPENSER, "§r清除全部气体", Gui_slot_type.Button);
		this.set_gui(Advanced_furnace.make_money_switch, Material.CHEST, "§e货币制造", Gui_slot_type.Switch);
		this.set_gui(26, Material.FURNACE, "§e燃料信息", Gui_slot_type.Indicator);
		this.set_gui(27, Material.IRON_PICKAXE, "§e货币容量升级", Gui_slot_type.Button);
		this.set_gui(28, Material.IRON_CHESTPLATE, "§e保温升级", Gui_slot_type.Indicator);
		this.set_gui(29, Material.GLOWSTONE_DUST, "§e高速燃烧升级", Gui_slot_type.Indicator);
		this.set_gui(30, Material.REDSTONE, "§e长时燃烧升级", Gui_slot_type.Indicator);
		this.set_gui(31, Material.CLOCK, "§e升级信息", Gui_slot_type.Indicator);
		this.set_gui(32, Material.BLUE_STAINED_GLASS_PANE, "§r右边为升级组件加入区",
				new String[]{"§7读条完成后即升级成功", "§7读条期间取出将重置进度"}, Gui_slot_type.Indicator);
		this.set_gui(34, Material.BLUE_STAINED_GLASS_PANE, "§r右边为燃料燃烧产品", Gui_slot_type.Indicator);

		this.set_gui(Advanced_furnace.fuel_slot, null, "fuel-slot", Gui_slot_type.Item_store);
		this.set_gui(Advanced_furnace.solid_product_slot, null, "solid-product-slot", Gui_slot_type.Item_store);
		this.set_gui(Advanced_furnace.solid_reactant_slot, null, "solid-reactant-slot", Gui_slot_type.Item_store);
		this.set_gui(Advanced_furnace.liquid_product_slot, null, "liquid-product-slot", Gui_slot_type.Item_store);
		this.set_gui(Advanced_furnace.liquid_reactant_slot, null, "liquid-reactant-slot", Gui_slot_type.Item_store);
		this.set_gui(Advanced_furnace.gas_product_slot, null, "gas-product-slot", Gui_slot_type.Item_store);
		this.set_gui(Advanced_furnace.gas_reactant_slot, null, "gas-reactant-slot", Gui_slot_type.Item_store);
		this.set_gui(Advanced_furnace.fuel_product_slot, null, "fuel-product-slot", Gui_slot_type.Item_store);
		this.set_gui(Advanced_furnace.upgrade_component_slot, null, "upgrade-component-slot", Gui_slot_type.Item_store);
	}

	/*
	 * public Advanced_furnace find(Location loc, boolean new_deop) { int x; int y;
	 * int z; Advanced_furnace adv_furnace; for (x = -1; x <= 1; x++) { for (y = -1;
	 * y <= 1; y++) { for (z = -1; z <= 1; z++) { Location check_loc =
	 * loc.clone().add(x, y, z); Material material = check_loc.getBlock().getType();
	 * // Bukkit.getLogger().info("正在搜索"+check_loc.toString()); if (material ==
	 * Material.FURNACE) { // Bukkit.getLogger().info("在" + check_loc.toString() +
	 * "找到了末地烛"); if (new_deop == true) { adv_furnace = new Advanced_furnace();
	 * adv_furnace.set_location(check_loc); if (adv_furnace.completed() == true) {
	 * adv_furnace.init_after_set_location(); return adv_furnace; } } else {
	 * adv_furnace = this.get(check_loc); if (adv_furnace != null) { return
	 * adv_furnace; } } } } } } return null; }
	 *
	 * @Override public Advanced_furnace find(String player_name, Location loc,
	 * boolean new_structure) { return this.find(loc, new_structure); }
	 */

	@Override
	public String get_gui_name() {
		return "高级熔炉";
	}

	@Override
	public int get_slot_num() {
		return 36;
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
	public Structure_runner[] init_runners() {
		return new Structure_runner[]{new Advanced_furnace_io_runner(this), new Advanced_furnace_reaction_runner(this), new Advanced_furnace_temp_runner(this), new Advanced_furnace_upgrade_runner(this)};
	}

	@Nonnull
	@Override
	public String get_permission_head() {
		return "adv_furnace";
	}

	@Nonnull
	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return this.center;
	}

	public int get_time_add_per_time_upgrade() {
		return 10;
	}

	public int get_time_loss_per_overload_upgrade() {
		return 9;
	}

	public int get_power_add_per_overload_upgrade() {
		return 9;
	}

	public int get_power_loss_per_time_upgrade() {
		return 5;
	}
}
