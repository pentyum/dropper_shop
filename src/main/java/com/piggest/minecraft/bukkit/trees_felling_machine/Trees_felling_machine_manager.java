package com.piggest.minecraft.bukkit.trees_felling_machine;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;
import com.piggest.minecraft.bukkit.structure.Has_runner;
import com.piggest.minecraft.bukkit.structure.Old_structure_runner;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class Trees_felling_machine_manager extends Gui_structure_manager<Trees_felling_machine> implements Has_runner {
	private final Material[][][] model = {
			{{Material.CHISELED_QUARTZ_BLOCK, Material.QUARTZ_PILLAR, Material.CHISELED_QUARTZ_BLOCK},
					{Material.QUARTZ_PILLAR, null, Material.QUARTZ_PILLAR},
					{Material.CHISELED_QUARTZ_BLOCK, Material.QUARTZ_PILLAR, Material.CHISELED_QUARTZ_BLOCK}},
			{{Material.QUARTZ_PILLAR, Material.GLASS_PANE, Material.QUARTZ_PILLAR},
					{Material.GLASS_PANE, Material.STONECUTTER, Material.GLASS_PANE},
					{Material.QUARTZ_PILLAR, Material.GLASS_PANE, Material.QUARTZ_PILLAR}},
			{{Material.CHISELED_QUARTZ_BLOCK, Material.QUARTZ_PILLAR, Material.CHISELED_QUARTZ_BLOCK},
					{Material.QUARTZ_PILLAR, null, Material.QUARTZ_PILLAR},
					{Material.CHISELED_QUARTZ_BLOCK, Material.QUARTZ_PILLAR, Material.CHISELED_QUARTZ_BLOCK}}};
	private final int[] center = new int[]{1, 1, 1};

	public Trees_felling_machine_manager() {
		super(Trees_felling_machine.class);
		int price = Dropper_shop_plugin.instance.get_price_config().get_start_trees_felling_machine_price();
		this.set_gui(9, Material.LEVER, "§r伐木机开关(开启需要" + price + ")", Gui_slot_type.Switch);
		this.set_gui(10, Material.PAPER, "§r重置", Gui_slot_type.Button);
		this.set_gui(12, Material.BLUE_STAINED_GLASS_PANE, "§r右边放入伐木斧头", Gui_slot_type.Indicator);
		this.set_gui(13, null, "axe", Gui_slot_type.Item_store);
		this.set_gui(14, Material.BLUE_STAINED_GLASS_PANE, "§r耐久附魔能减少耐久损耗", Gui_slot_type.Indicator);
		this.set_gui(Trees_felling_machine.owner_indicator, Material.PLAYER_HEAD, "§r当前控制者: null",
				new String[]{"§7注:启动伐木机的玩家即为控制者", "§7控制者不在线时，伐木机暂停工作"}, Gui_slot_type.Indicator);
	}
	/*
	 * @Override public Trees_felling_machine find(String player_name, Location loc,
	 * boolean new_structure) { return this.find(loc, new_structure); }
	 *
	 * public Trees_felling_machine find(Location loc, boolean new_structure) { int
	 * x; int y; int z; Trees_felling_machine machine; for (x = -1; x <= 1; x++) {
	 * for (y = -1; y <= 1; y++) { for (z = -1; z <= 1; z++) { Location check_loc =
	 * loc.clone().add(x, y, z); Material material = check_loc.getBlock().getType();
	 * if (material == Material.STONECUTTER) { if (new_structure == true) { machine
	 * = new Trees_felling_machine(); machine.set_location(check_loc); if
	 * (machine.completed() == true) { machine.init_after_set_location(); return
	 * machine; } } else { machine = this.get(check_loc); if (machine != null) {
	 * return machine; } } } } } } return null; }
	 */

	@Override
	public String get_gui_name() {
		return "砍树机";
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int get_slot_num() {
		return 27;
	}

	@Override
	public int[] get_process_bar() {
		return new int[]{0};
	}

	@Override
	public Old_structure_runner[] init_runners() {
		return new Old_structure_runner[]{new Trees_felling_machine_runner(this)};
	}

	@Override
	public String get_permission_head() {
		return "trees_felling_machine";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return this.center;
	}
}
