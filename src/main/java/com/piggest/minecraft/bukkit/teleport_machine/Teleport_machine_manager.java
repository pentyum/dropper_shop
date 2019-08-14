package com.piggest.minecraft.bukkit.teleport_machine;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Teleport_machine_manager extends Gui_structure_manager<Teleport_machine> {
	private final int[] process_bar = new int[] { 0 };
	private final int[] center = new int[] { 2, 0, 2 };
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
			{ { Material.QUARTZ_PILLAR, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS,
					Material.SMOOTH_QUARTZ_STAIRS, Material.QUARTZ_PILLAR },
					{ Material.SMOOTH_QUARTZ_STAIRS, null, null, null, Material.SMOOTH_QUARTZ_STAIRS },
					{ Material.SMOOTH_QUARTZ_STAIRS, null, null, null, Material.SMOOTH_QUARTZ_STAIRS },
					{ Material.SMOOTH_QUARTZ_STAIRS, null, null, null, Material.SMOOTH_QUARTZ_STAIRS },
					{ Material.QUARTZ_PILLAR, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS,
							Material.SMOOTH_QUARTZ_STAIRS, Material.QUARTZ_PILLAR } },
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
		this.set_gui(17, Material.ORANGE_STAINED_GLASS_PANE, "§r上一页", Gui_slot_type.Button);
		this.set_gui(18, null, null, Gui_slot_type.Button);
		this.set_gui(19, null, null, Gui_slot_type.Button);
		this.set_gui(20, null, null, Gui_slot_type.Button);
		this.set_gui(21, null, null, Gui_slot_type.Button);
		this.set_gui(22, null, null, Gui_slot_type.Button);
		this.set_gui(23, null, null, Gui_slot_type.Button);
		this.set_gui(24, null, null, Gui_slot_type.Button);
		this.set_gui(25, null, null, Gui_slot_type.Button);
		this.set_gui(26, Material.ORANGE_STAINED_GLASS_PANE, "§r下一页", Gui_slot_type.Button);

		this.set_gui(Teleport_machine.open_switch, Material.LEVER, "§r无线魔术开关", Gui_slot_type.Switch);
		this.set_gui(28, Material.REDSTONE_TORCH, "§r搜索无线魔术终端", Gui_slot_type.Button);
		this.set_gui(29, Material.PAPER, "§r立刻刷新无线魔术信息", Gui_slot_type.Button);
		this.set_gui(Teleport_machine.radio_indicator, Material.END_ROD, "§r当前无线魔术信息",
				new String[] { "§7终端名称: "+this.get_gui_name(), "§7运行状态: " + Radio_state.OFF.display_name, "§7当前输入功率: 0 W",
						"§7当前辐射功率: 0 W", "§7天线长度: 1 m", "§7中心频率: kHz", "§7天线频宽: kHz", "§7辐射魔阻: Ω", "§7辐射魔抗: Ω","§7输入阻抗: Ω" },
				Gui_slot_type.Indicator);

		this.set_gui(36, Material.CHEST, "§r元素信息", Gui_slot_type.Button);
		this.set_gui(37, Material.MINECART, "§r传送台上实体转化为元素", Gui_slot_type.Button);
		this.set_gui(Teleport_machine.magic_indicator, Material.EXPERIENCE_BOTTLE, "§r魔力信息",
				new String[] { "§7剩余: 0 单位" }, Gui_slot_type.Indicator);
		this.set_gui(40, Material.PLAYER_HEAD, "§r玩家经验转化为魔力", new String[] { "§71点经验转化为1Wh魔力", "§7点击立刻转化1000点经验为魔力" },
				Gui_slot_type.Button);

		this.set_gui(45, null, "item-slot", Gui_slot_type.Item_store);
		this.set_gui(46, Material.BLUE_STAINED_GLASS_PANE, "§r左边放置物品转化为元素", Gui_slot_type.Indicator);
		this.set_gui(Teleport_machine.name_tag_slot, null, "name-tag-slot", Gui_slot_type.Item_store);
		this.set_gui(48, Material.BLUE_STAINED_GLASS_PANE, "§r左边放置命名牌给本传送机命名", Gui_slot_type.Indicator);

		this.set_gui(32, Material.RED_STAINED_GLASS_PANE, "§r提高待机魔压", new String[] { "§7+1 V" }, Gui_slot_type.Button);
		this.set_gui(33, Material.RED_STAINED_GLASS_PANE, "§r提高发射魔压", new String[] { "§7+1 V" }, Gui_slot_type.Button);
		this.set_gui(34, Material.RED_STAINED_GLASS_PANE, "§r增加带宽", new String[] { "§7+100 kHz" },
				Gui_slot_type.Button);
		this.set_gui(35, Material.RED_STAINED_GLASS_PANE, "§r提高载波频率", new String[] { "§7+500 kHz" },
				Gui_slot_type.Button);

		this.set_gui(Teleport_machine.online_voltage_indicator, Material.COMPASS, "§r当前待机魔压", new String[] { "§70 V" },
				Gui_slot_type.Indicator);
		this.set_gui(Teleport_machine.working_voltage_indicator, Material.COMPASS, "§r当前发射魔压", new String[] { "§70 V" },
				Gui_slot_type.Indicator);
		this.set_gui(Teleport_machine.bandwidth_indicator, Material.COMPASS, "§r当前带宽", new String[] { "§70 kHz" },
				Gui_slot_type.Indicator);
		this.set_gui(Teleport_machine.freq_indicator, Material.COMPASS, "§r当前载波频率", new String[] { "§70 kHz" },
				Gui_slot_type.Indicator);

		this.set_gui(50, Material.BLUE_STAINED_GLASS_PANE, "§r降低待机魔压", new String[] { "§7-1 V" }, Gui_slot_type.Button);
		this.set_gui(51, Material.BLUE_STAINED_GLASS_PANE, "§r降低发射魔压", new String[] { "§7-1 V" }, Gui_slot_type.Button);
		this.set_gui(52, Material.BLUE_STAINED_GLASS_PANE, "§r减少带宽", new String[] { "§7-100 kHz" },
				Gui_slot_type.Button);
		this.set_gui(53, Material.BLUE_STAINED_GLASS_PANE, "§r降低载波频率", new String[] { "§7-500 kHz" },
				Gui_slot_type.Button);
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
		return "魔术传送机";
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
