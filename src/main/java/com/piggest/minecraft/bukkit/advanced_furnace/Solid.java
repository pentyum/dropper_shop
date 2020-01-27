package com.piggest.minecraft.bukkit.advanced_furnace;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.depository.Reader;
import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.teleport_machine.Elements_composition;

public enum Solid implements Chemical {
	iron_ore(1000, "铁矿石"), iron_powder(1000, "铁粉"), iron_ingot(1000, "铁锭"), gold_ore(1000, "金矿石"),
	gold_powder(1000, "金粉"), gold_ingot(1000, "金锭"), lapis_ore(1000, "青金石矿石"), lapis_powder(1000, "青金石粉"),
	lapis_lazuli(1000, "青金石"), log(1000, "原木"), charcoal(1000, "木炭"), coal(1000, "煤炭"), cobblestone(1000, "圆石"),
	stone(1000, "石头"), glass(1000, "玻璃"), sand(1000, "沙子"), clay(1000, "粘土块"), terracotta(1000, "陶瓦"),
	clay_ball(1000, "粘土"), brick(1000, "红砖"), netherrack(1000, "地狱岩"), nether_brick(1000, "地狱砖"),
	copper_powder(1000, "铜粉"),copper_ingot(1000, "铜锭"), silver_powder(1000, "银粉"),silver_ingot(1000, "银锭"), gravel(1000, "砂砾"), flint(1000, "燧石"), quartz(1000, "石英"),
	obsidian(1000, "黑曜石"), redstone(1000, "红石"), aluminium_oxide(1000, "三氧化二铝"), granite(1000, "花岗岩"),
	quartz_block(1000, "石英块"), smooth_quartz(1000, "平滑石英"), smooth_stone(1000, "平滑石头"), ice(1000, "冰");

	private int unit;
	private String display_name;

	Solid(int unit, String display_name) {
		this.unit = unit;
		this.display_name = display_name;
	}

	public NamespacedKey get_namespacedkey() {
		if (Material.getMaterial(this.name()) == null) {
			return Dropper_shop_plugin.get_key(this.name());
		} else {
			return NamespacedKey.minecraft(this.name().toLowerCase());
		}
	}

	public static Solid get_solid(ItemStack itemstack) {
		String name = Material_ext.get_id_name(itemstack);
		if (name.contains("_log")) {
			return log;
		}
		if (name.equals(Reader.id_name)) {
			name = Reader.get_content_id_name(itemstack);
		}
		return Solid.get_solid(name);
	}

	public int get_unit() {
		return this.unit;
	}

	public ItemStack get_item_stack() {
		if (this == log) {
			return new ItemStack(Material.OAK_LOG);
		}
		return Material_ext.new_item(this.name(), 1);
	}

	public String get_displayname() { // 获取显示名称
		return this.display_name + "(s)";
	}

	public String get_name() { // 获取内部ID名称
		return this.name();
	}

	public static Solid get_solid(String name) {
		Solid solid = null;
		try {
			solid = Solid.valueOf(name);
		} catch (Exception e) {
		} finally {
		}
		return solid;
	}

	@Override
	public Elements_composition get_elements_composition() {
		// TODO 自动生成的方法存根
		return null;
	}
}
