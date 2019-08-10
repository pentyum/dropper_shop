package com.piggest.minecraft.bukkit.teleport_machine;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Teleport_machine_manager extends Gui_structure_manager<Teleport_machine> {
	private final int[] process_bar = new int[] { 0 };
	private final int[] center = new int[] { 0, 2, 2 };
	private Material[][][] model = new Material[][][] {
			{ { Material.QUARTZ_PILLAR, Material.SMOOTH_QUARTZ, Material.SMOOTH_QUARTZ, Material.SMOOTH_QUARTZ,
					Material.QUARTZ_PILLAR },
					{ Material.SMOOTH_QUARTZ, Material.CHISELED_QUARTZ_BLOCK, Material.SMOOTH_QUARTZ,
							Material.CHISELED_QUARTZ_BLOCK, Material.SMOOTH_QUARTZ },
					{ Material.SMOOTH_QUARTZ, Material.SMOOTH_QUARTZ, Material.SEA_LANTERN, Material.SMOOTH_QUARTZ,
							Material.SMOOTH_QUARTZ },
					{ Material.SMOOTH_QUARTZ, Material.CHISELED_QUARTZ_BLOCK, Material.SMOOTH_QUARTZ,
							Material.CHISELED_QUARTZ_BLOCK, Material.SMOOTH_QUARTZ },
					{ Material.QUARTZ_PILLAR, Material.SMOOTH_QUARTZ, Material.SMOOTH_QUARTZ, Material.SMOOTH_QUARTZ,
							Material.QUARTZ_PILLAR } },
			{ { Material.QUARTZ_PILLAR, Material.QUARTZ_STAIRS, Material.QUARTZ_STAIRS, Material.QUARTZ_STAIRS,
					Material.QUARTZ_PILLAR }, { Material.QUARTZ_STAIRS, null, null, null, Material.QUARTZ_STAIRS },
					{ Material.QUARTZ_STAIRS, null, null, null, Material.QUARTZ_STAIRS },
					{ Material.QUARTZ_STAIRS, null, null, null, Material.QUARTZ_STAIRS },
					{ Material.QUARTZ_PILLAR, Material.QUARTZ_STAIRS, Material.QUARTZ_STAIRS, Material.QUARTZ_STAIRS,
							Material.QUARTZ_PILLAR } },
			{ { Material.QUARTZ_PILLAR, null, null, null, Material.QUARTZ_PILLAR }, { null, null, null, null, null },
					{ null, null, null, null, null }, { null, null, null, null, null },
					{ Material.QUARTZ_PILLAR, null, null, null, Material.QUARTZ_PILLAR } },
			{ { Material.REDSTONE_LAMP, null, null, null, Material.REDSTONE_LAMP }, { null, null, null, null, null },
					{ null, null, null, null, null }, { null, null, null, null, null },
					{ Material.REDSTONE_LAMP, null, null, null, Material.REDSTONE_LAMP } } };

	public Teleport_machine_manager() {
		super(Teleport_machine.class);
		this.set_gui(9, null, null, Gui_slot_type.Button);
		this.set_gui(10, null, null, Gui_slot_type.Button);
		this.set_gui(11, null, null, Gui_slot_type.Button);
		this.set_gui(12, null, null, Gui_slot_type.Button);
		this.set_gui(13, null, null, Gui_slot_type.Button);
		this.set_gui(14, null, null, Gui_slot_type.Button);
		this.set_gui(15, null, null, Gui_slot_type.Button);
		this.set_gui(16, null, null, Gui_slot_type.Button);
		this.set_gui(17, Material.WHITE_STAINED_GLASS_PANE, "§r上一页", Gui_slot_type.Button);
		this.set_gui(18, null, null, Gui_slot_type.Button);
		this.set_gui(19, null, null, Gui_slot_type.Button);
		this.set_gui(20, null, null, Gui_slot_type.Button);
		this.set_gui(21, null, null, Gui_slot_type.Button);
		this.set_gui(22, null, null, Gui_slot_type.Button);
		this.set_gui(23, null, null, Gui_slot_type.Button);
		this.set_gui(24, null, null, Gui_slot_type.Button);
		this.set_gui(25, null, null, Gui_slot_type.Button);
		this.set_gui(26, Material.WHITE_STAINED_GLASS_PANE, "§r下一页", Gui_slot_type.Button);

		this.set_gui(27, Material.LEVER, "§r无线电开关", Gui_slot_type.Switch);
		this.set_gui(28, Material.REDSTONE_TORCH, "§r搜索无线电终端", Gui_slot_type.Button);
		this.set_gui(29, Material.PAPER, "§r立刻刷新无线电信息", Gui_slot_type.Button);
		this.set_gui(30, Material.END_ROD, "§r当前无线电信息", Gui_slot_type.Indicator);

		this.set_gui(36, Material.CHEST, "§r元素信息", Gui_slot_type.Indicator);
		this.set_gui(37, Material.MINECART, "§r传送台上实体转化为元素", Gui_slot_type.Button);

		this.set_gui(45, Material.EXPERIENCE_BOTTLE, "§r魔力信息", Gui_slot_type.Indicator);
		this.set_gui(46, Material.PLAYER_HEAD, "§r玩家经验转化为魔力", Gui_slot_type.Button);

		this.set_gui(32, Material.RED_STAINED_GLASS_PANE, "§r提高待机电压", Gui_slot_type.Button);
		this.set_gui(33, Material.RED_STAINED_GLASS_PANE, "§r提高发射电压", Gui_slot_type.Button);
		this.set_gui(34, Material.RED_STAINED_GLASS_PANE, "§r增加带宽", Gui_slot_type.Button);
		this.set_gui(35, Material.RED_STAINED_GLASS_PANE, "§r提高载波频率", Gui_slot_type.Button);

		this.set_gui(41, Material.COMPASS, "§r当前待机电压", Gui_slot_type.Indicator);
		this.set_gui(42, Material.COMPASS, "§r当前发射电压", Gui_slot_type.Indicator);
		this.set_gui(43, Material.COMPASS, "§r当前带宽", Gui_slot_type.Indicator);
		this.set_gui(44, Material.COMPASS, "§r当前载波频率", Gui_slot_type.Indicator);

		this.set_gui(50, Material.BLUE_STAINED_GLASS_PANE, "§r降低待机电压", Gui_slot_type.Button);
		this.set_gui(51, Material.BLUE_STAINED_GLASS_PANE, "§r降低发射电压", Gui_slot_type.Button);
		this.set_gui(52, Material.BLUE_STAINED_GLASS_PANE, "§r减少带宽", Gui_slot_type.Button);
		this.set_gui(53, Material.BLUE_STAINED_GLASS_PANE, "§r降低载波频率", Gui_slot_type.Button);
	}

	@Override
	public String get_permission_head() {
		return "teleport_machine";
	}

	@Override
	public Material[][][] get_model() {
		return this.model;
	}

	@Override
	public int[] get_center() {
		return this.center;
	}

	@Override
	public String get_gui_name() {
		return "传送机";
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int get_slot_num() {
		return 54;
	}

	@Override
	public int[] get_process_bar() {
		return process_bar;
	}
}
