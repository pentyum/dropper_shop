package com.piggest.minecraft.bukkit.nms.enchant;

import com.google.common.collect.Lists;
import com.piggest.minecraft.bukkit.material_ext.Tool_material;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftInventoryView;
import org.bukkit.craftbukkit.v1_16_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R2.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

public class Enchant_manager_1_16 implements Enchant_manager {
	Field containerProperty_i = null;

	public Enchant_manager_1_16() {
		try {
			containerProperty_i = ContainerEnchantTable.class.getDeclaredField("i");
		} catch (NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
		containerProperty_i.setAccessible(true);
	}

	public void enchant(ItemStack item, InventoryView view, EnchantmentOffer[] offers, int bonus) {
		CraftInventoryView inventory_view = (CraftInventoryView) view;
		ContainerEnchantTable enchant_table = (ContainerEnchantTable) inventory_view.getHandle();
		int j;
		Random rand = new Random();
		int[] costs = new int[3];
		int[] levels = new int[3];
		int[] enchantments = new int[3];
		ContainerProperty enchant_seed = ContainerProperty.a();
		try {
			enchant_seed = (ContainerProperty) containerProperty_i.get(enchant_table);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		for (j = 0; j < 3; j++) {
			rand.setSeed(enchant_seed.get());
			costs[j] = getEnchantmentCost(rand, j, bonus, item);
			enchantments[j] = -1;

			levels[j] = -1;

			if (costs[j] < j + 1) {
				costs[j] = 0;
			}
		}

		for (j = 0; j < 3; j++) {
			if (costs[j] > 0) {
				rand.setSeed(enchant_seed.get() + j);
				List<WeightedRandomEnchant> list = selectEnchantment(rand, item, costs[j], false);

				if (list != null && !list.isEmpty()) {
					WeightedRandomEnchant weightedrandomenchant = list.get(rand.nextInt(list.size()));

					enchantments[j] = IRegistry.ENCHANTMENT.a(weightedrandomenchant.enchantment);

					levels[j] = weightedrandomenchant.level;
				}
			}
		}
		for (j = 0; j < 3; j++) {
			if (offers[j] != null) {
				offers[j].setCost(costs[j]);
				Enchantment enchantment = (enchantments[j] >= 0) ? Enchantment.getByKey(CraftNamespacedKey
						.fromMinecraft(IRegistry.ENCHANTMENT.getKey(IRegistry.ENCHANTMENT.fromId(enchantments[j]))))
						: null;
				offers[j].setEnchantment(enchantment);
				offers[j].setEnchantmentLevel(levels[j]);
			}
		}
	}

	public static List<WeightedRandomEnchant> selectEnchantment(Random rand, ItemStack item, int var2, boolean var3) {
		List<WeightedRandomEnchant> var4 = Lists.newArrayList();

		Tool_material tool_material = Tool_material.get_tool_material(item);
		int enchantment_ability = tool_material.get_enchantment_ability();

		if (enchantment_ability <= 0) {
			return var4;
		}

		var2 += 1 + rand.nextInt(enchantment_ability / 4 + 1) + rand.nextInt(enchantment_ability / 4 + 1);

		float var7 = (rand.nextFloat() + rand.nextFloat() - 1.0F) * 0.15F;
		var2 = MathHelper.clamp(Math.round(var2 + var2 * var7), 1, 2147483647);

		List<WeightedRandomEnchant> var8 = getAvailableEnchantmentResults(var2, item, var3);
		if (!var8.isEmpty()) {
			var4.add(WeightedRandom.a(rand, var8));

			while (rand.nextInt(50) <= var2) {
				EnchantmentManager.a(var8, SystemUtils.<WeightedRandomEnchant>a(var4));

				if (var8.isEmpty()) {
					break;
				}

				var4.add(WeightedRandom.a(rand, var8));
				var2 /= 2;
			}
		}
		return var4;
	}

	public static int getEnchantmentCost(Random rand, int j, int bonus, ItemStack item) {
		Tool_material tool_material = Tool_material.get_tool_material(item);
		int enchantment_ability = tool_material.get_enchantment_ability();
		if (enchantment_ability <= 0) {
			return 0;
		}

		if (bonus > 15) {
			bonus = 15;
		}
		int var6 = rand.nextInt(8) + 1 + (bonus >> 1) + rand.nextInt(bonus + 1);
		if (j == 0) {
			return Math.max(var6 / 3, 1);
		}
		if (j == 1) {
			return var6 * 2 / 3 + 1;
		}
		return Math.max(var6, bonus * 2);
	}

	public static List<WeightedRandomEnchant> getAvailableEnchantmentResults(int var0, ItemStack item, boolean var2) {
		return EnchantmentManager.a(var0, CraftItemStack.asNMSCopy(item), var2);
	}
}
