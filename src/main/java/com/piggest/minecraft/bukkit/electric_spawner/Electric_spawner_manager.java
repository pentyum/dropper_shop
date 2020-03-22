package com.piggest.minecraft.bukkit.electric_spawner;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.inventory.InventoryType;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.gui.Gui_structure_manager;

public class Electric_spawner_manager extends Gui_structure_manager<Electric_spawner> {
	private Material[][][] model = {
			{ { Material.CHISELED_QUARTZ_BLOCK, Material.LAPIS_BLOCK, Material.CHISELED_QUARTZ_BLOCK },
					{ Material.LAPIS_BLOCK, Material.DIAMOND_BLOCK, Material.LAPIS_BLOCK },
					{ Material.CHISELED_QUARTZ_BLOCK, Material.LAPIS_BLOCK, Material.CHISELED_QUARTZ_BLOCK } },
			{ { Material.IRON_BARS, Material.IRON_BARS, Material.IRON_BARS },
					{ Material.IRON_BARS, Material.BEACON, Material.IRON_BARS },
					{ Material.IRON_BARS, Material.IRON_BARS, Material.IRON_BARS } },
			{ { Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS },
					{ Material.SMOOTH_QUARTZ_STAIRS, Material.DIAMOND_BLOCK, Material.SMOOTH_QUARTZ_STAIRS },
					{ Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS, Material.SMOOTH_QUARTZ_STAIRS } },
			{ { null, null, null }, { null, Material.ENCHANTING_TABLE, null }, { null, null, null } } };
	private int[] center = new int[] { 1, 1, 1 };
	public final HashMap<String, Entity_probability[]> probability_map = new HashMap<>();

	public Electric_spawner_manager() {
		super(Electric_spawner.class);
		this.set_gui(10, Material.BLUE_STAINED_GLASS_PANE, "§e右边放合成召唤物", Gui_slot_type.Indicator);
		this.set_gui(11, null, "raw-slot-1", Gui_slot_type.Item_store);
		this.set_gui(12, null, "raw-slot-2", Gui_slot_type.Item_store);
		this.set_gui(13, null, "raw-slot-3", Gui_slot_type.Item_store);
		this.set_gui(14, null, "raw-slot-4", Gui_slot_type.Item_store);
		this.set_gui(15, Material.BLUE_STAINED_GLASS_PANE, "§e左边放合成召唤物", Gui_slot_type.Indicator);
		this.set_gui(Electric_spawner.look_button_slot, Material.ENCHANTED_BOOK, "§e查看召唤概率",
				new String[] { "§7需要消耗"
						+ Dropper_shop_plugin.instance.get_price_config().get_look_electric_spawner_price() + "金币" },
				Gui_slot_type.Button);
		this.set_gui(Electric_spawner.info_indicator_slot, Material.SPAWNER, "§e刷怪信息", Gui_slot_type.Indicator);
		this.set_gui(Electric_spawner.synthesis_button_slot, Material.CRAFTING_TABLE, "§e召唤", Gui_slot_type.Button);

		this.init_recipe();
	}

	private void init_recipe() {
		Entity_probability[] probability_list;

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.ZOMBIE, 3),
				new Entity_probability(EntityType.ZOMBIE_VILLAGER, 1), new Entity_probability(EntityType.HUSK, 1),
				new Entity_probability(EntityType.PIG_ZOMBIE, 1), new Entity_probability(EntityType.DROWNED, 1) };
		this.probability_map.put(Material.ROTTEN_FLESH.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SKELETON, 3),
				new Entity_probability(EntityType.SKELETON_HORSE, 1), new Entity_probability(EntityType.STRAY, 1) };
		this.probability_map.put(Material.BONE.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SKELETON, 1) };
		this.probability_map.put(Material.BONE_MEAL.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SKELETON_HORSE, 2),
				new Entity_probability(EntityType.STRAY, 1) };
		this.probability_map.put(Material.BONE_BLOCK.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SKELETON, 2),
				new Entity_probability(EntityType.STRAY, 1), new Entity_probability(EntityType.PILLAGER, 1) };
		this.probability_map.put(Material.ARROW.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SKELETON, 20),
				new Entity_probability(EntityType.STRAY, 20) };
		this.probability_map.put(Material.BOW.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.PILLAGER, 30) };
		this.probability_map.put(Material.CROSSBOW.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SPIDER, 2),
				new Entity_probability(EntityType.CAVE_SPIDER, 2) };
		this.probability_map.put(Material.SPIDER_EYE.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SPIDER, 1),
				new Entity_probability(EntityType.CAVE_SPIDER, 1), new Entity_probability(EntityType.CAT, 1) };
		this.probability_map.put(Material.STRING.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.ENDERMAN, 2),
				new Entity_probability(EntityType.ENDERMITE, 1) };
		this.probability_map.put(Material.ENDER_PEARL.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SLIME, 2) };
		this.probability_map.put(Material.SLIME_BALL.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SLIME, 2) };
		this.probability_map.put(Material.SLIME_BLOCK.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.BLAZE, 2) };
		this.probability_map.put(Material.BLAZE_ROD.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.GUARDIAN, 1) };
		this.probability_map.put(Material.PRISMARINE_SHARD.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.GUARDIAN, 1) };
		this.probability_map.put(Material.PRISMARINE_CRYSTALS.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.PHANTOM, 2) };
		this.probability_map.put(Material.PHANTOM_MEMBRANE.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.VINDICATOR, 2) };
		this.probability_map.put(Material.EMERALD.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.HUSK, 1) };
		this.probability_map.put(Material.SAND.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.SQUID, 2) };
		this.probability_map.put(Material.INK_SAC.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.PANDA, 1) };
		this.probability_map.put(Material.BAMBOO.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.PIG, 1) };
		this.probability_map.put(Material.PORKCHOP.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.TURTLE, 1) };
		this.probability_map.put(Material.SEAGRASS.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.TURTLE, 2) };
		this.probability_map.put(Material.SCUTE.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.POLAR_BEAR, 1) };
		this.probability_map.put(Material.COD.getKey().toString(), probability_list);

		probability_list = new Entity_probability[] { new Entity_probability(EntityType.POLAR_BEAR, 1) };
		this.probability_map.put(Material.SALMON.getKey().toString(), probability_list);
	}

	@Override
	public String get_gui_name() {
		return "魔力刷怪笼";
	}

	@Override
	public InventoryType get_inventory_type() {
		return InventoryType.CHEST;
	}

	@Override
	public int get_slot_num() {
		return 18;
	}

	@Override
	public int[] get_process_bar() {
		return new int[] { 0 };
	}

	@Override
	public String get_permission_head() {
		return "electric_spawner";
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
