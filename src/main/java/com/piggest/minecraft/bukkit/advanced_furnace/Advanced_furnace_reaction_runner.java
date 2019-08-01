package com.piggest.minecraft.bukkit.advanced_furnace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Advanced_furnace_reaction_runner extends Structure_runner {
	private Advanced_furnace advanced_furnace;
	private int money_times = 0;

	public Advanced_furnace_reaction_runner(Advanced_furnace advanced_furnace) {
		this.advanced_furnace = advanced_furnace;
	}

	public void run() {
		if (this.advanced_furnace.is_loaded() == false) {
			return;
		}
		ItemStack solid_reactant_slot = advanced_furnace.get_gui_item(Advanced_furnace.solid_reactant_slot);
		ItemStack gas_reactant_slot = advanced_furnace.get_gui_item(Advanced_furnace.gas_reactant_slot);
		ItemStack liquid_reactant_slot = advanced_furnace.get_gui_item(Advanced_furnace.liquid_reactant_slot);

		Reaction_container reaction_container = this.advanced_furnace.get_reaction_container();

		if (!Grinder.is_empty(solid_reactant_slot)) { // 固体进入反应器
			Solid solid = Solid.get_solid(solid_reactant_slot);
			if (solid != null) {
				int current_unit = reaction_container.get_unit(solid);
				if (current_unit < solid.get_unit() * 64) {
					solid_reactant_slot.setAmount(solid_reactant_slot.getAmount() - 1);
					reaction_container.set_unit(solid, current_unit + solid.get_unit());
				}
			}
		}

		if (!Grinder.is_empty(liquid_reactant_slot)) { // 液体进入反应器
			if (Liquid.is_empty_liquid_container(liquid_reactant_slot)) {// 处理空容器
				int capacity = Liquid.get_container_max_unit(liquid_reactant_slot);
				HashMap<Chemical, Integer> all_chemical = reaction_container.get_all_chemical();
				for (Entry<Chemical, Integer> entry : all_chemical.entrySet()) {
					Chemical chemical = entry.getKey();
					if (chemical instanceof Liquid) {
						Liquid liquid = (Liquid) chemical;
						int unit = entry.getValue();
						if (unit >= capacity) {
							ItemStack filled = Liquid.get_fill_container(liquid, liquid_reactant_slot);
							if (Inventory_io.move_a_item_to_slot(filled, this.advanced_furnace.getInventory(),
									Advanced_furnace.liquid_product_slot)) {
								reaction_container.set_unit(liquid, reaction_container.get_unit(liquid) - capacity);
								liquid_reactant_slot.setAmount(liquid_reactant_slot.getAmount() - 1);
							}
							break;
						}
					}
				}
			} else { // 处理其他容器
				Liquid liquid = Liquid.get_liquid(liquid_reactant_slot);
				if (liquid != null) { // 检测到合法液体容器
					ItemStack new_empty_bucket = Material_ext.get_empty_container(liquid_reactant_slot);
					if (Inventory_io.move_a_item_to_slot(new_empty_bucket, this.advanced_furnace.getInventory(),
							Advanced_furnace.liquid_product_slot)) { // 产品槽允许空桶放入则添加进内部
						reaction_container.set_unit(liquid,
								reaction_container.get_unit(liquid) + Liquid.get_item_unit(liquid_reactant_slot));
						liquid_reactant_slot.setAmount(liquid_reactant_slot.getAmount() - 1);
					}
				}
			}
		}

		if (Gas_bottle.is_gas_bottle(gas_reactant_slot)) { // 气体相关
			ItemStack gas_product_slot = advanced_furnace.get_gui_item(Advanced_furnace.gas_product_slot);
			if (Gas_bottle.calc_capacity(gas_reactant_slot) == 0) {// 处理空瓶，取出气体
				if (Grinder.is_empty(gas_product_slot)) {
					int inside_gas_capacity = 0;
					HashMap<Chemical, Integer> all_chemical = reaction_container.get_all_chemical();
					ArrayList<Gas> gas_list = new ArrayList<Gas>();
					ArrayList<Integer> need_to_move = new ArrayList<Integer>();
					for (Entry<Chemical, Integer> entry : all_chemical.entrySet()) {
						Chemical chemical = entry.getKey();
						if (chemical instanceof Gas) {
							gas_list.add((Gas) chemical);
							need_to_move.add(entry.getValue());
							inside_gas_capacity += entry.getValue();
						}
					}
					if (inside_gas_capacity > 1000) {
						for (int i = 0; i < gas_list.size(); i++) {
							need_to_move.set(i, need_to_move.get(i) * 1000 / inside_gas_capacity);
						}
					}
					ItemStack new_bottle = Gas_bottle.get_new_empty_bottle(); // 瓶子转移到产品槽
					for (int i = 0; i < gas_list.size(); i++) {
						Gas gas = gas_list.get(i);
						int move = need_to_move.get(i);
						reaction_container.set_unit(gas, reaction_container.get_unit(gas) - move);
						Gas_bottle.set_contents(new_bottle, gas, move);
					}
					advanced_furnace.set_gas_product_slot(new_bottle);
					gas_reactant_slot.setAmount(gas_reactant_slot.getAmount() - 1);
				}

			} else { // 非空瓶，气体进入
				boolean flag = false;

				if (Grinder.is_empty(gas_product_slot)) {
					advanced_furnace.set_gas_product_slot(Gas_bottle.get_new_empty_bottle());
					flag = true;
				} else {
					if (gas_product_slot.isSimilar(Gas_bottle.get_new_empty_bottle())) {
						int new_num = gas_product_slot.getAmount() + 1;
						if (new_num <= Gas_bottle.get_new_empty_bottle().getMaxStackSize()) {
							gas_product_slot.setAmount(new_num);
							flag = true;
						}
					}
				}
				if (flag == true) {
					HashMap<Gas, Integer> gas_map = Gas_bottle.get_gas_map(gas_reactant_slot);
					for (Entry<Gas, Integer> entry : gas_map.entrySet()) {
						Chemical chemical = entry.getKey();
						if (chemical instanceof Gas) {
							reaction_container.set_unit(chemical,
									reaction_container.get_unit(chemical) + entry.getValue());
						}
					}
					gas_reactant_slot.setAmount(gas_reactant_slot.getAmount() - 1);
				}
			}
		}
		if (advanced_furnace.is_open()) { // 敞口容器，气体全部替换成空气
			advanced_furnace.on_button_pressed(null, 6);
			for (Entry<Gas, Integer> entry : Dropper_shop_plugin.instance
					.get_air(advanced_furnace.get_location().getWorld().getName()).entrySet()) {
				reaction_container.set_unit(entry.getKey(), entry.getValue());
			}
		}
		if (advanced_furnace.get_make_money() == false) { // 不产钱
			reaction_container.run_all_reactions();
		} else {
			if (this.money_times >= 120) { // 产线
				advanced_furnace.add_money(advanced_furnace.get_make_money_rate());
				this.money_times = 0;
			} else {
				this.money_times++;
			}
		}

		ArrayList<String> lore = new ArrayList<String>();
		HashMap<Chemical, Integer> all_chemical = reaction_container.get_all_chemical();
		for (Entry<Chemical, Integer> entry : all_chemical.entrySet()) { // 计算反应速率
			lore.add("§r" + entry.getKey().get_displayname() + ": " + entry.getValue() + " ("
					+ reaction_container.get_rate(entry.getKey()) * (20 / this.get_cycle()) + "/s)");
		}
		advanced_furnace.set_reactor_info(lore);
		if (advanced_furnace.get_auto_product() == true) { // 自动弹出产品
			Iterator<Entry<Chemical, Integer>> iterator = all_chemical.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<Chemical, Integer> entry = iterator.next();
				Chemical chemical = entry.getKey();
				if (chemical instanceof Solid) {
					Solid solid = (Solid) chemical;
					if (reaction_container.get_rate(solid) > 0) {
						advanced_furnace.solid_to_product(solid, iterator);
					}
				}
			}
		}
	}

	public int get_cycle() {
		return 5;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}

}
