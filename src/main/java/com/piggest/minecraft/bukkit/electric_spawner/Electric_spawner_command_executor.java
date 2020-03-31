package com.piggest.minecraft.bukkit.electric_spawner;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.utils.Tab_list;
import com.piggest.minecraft.bukkit.utils.language.Entity_zh_cn;

public class Electric_spawner_command_executor implements TabExecutor {
	public static final int max_money = 100000000;

	private static final ArrayList<String> cmd = new ArrayList<String>() {
		private static final long serialVersionUID = 4632683284690778242L;
		{
			add("charge");
			add("set_entity");
			add("unset_entity");
		}
	};

	private static final ArrayList<String> entity_type = new ArrayList<String>() {
		private static final long serialVersionUID = -10739381756686327L;

		{
			add(EntityType.SLIME.getKey().toString());
			add(EntityType.MAGMA_CUBE.getKey().toString());
			add(EntityType.ZOMBIE.getKey().toString());
			add(EntityType.ZOMBIE_VILLAGER.getKey().toString());
			add(EntityType.HUSK.getKey().toString());
			add(EntityType.DROWNED.getKey().toString());
			add(EntityType.SKELETON.getKey().toString());
			add(EntityType.STRAY.getKey().toString());
			add(EntityType.WITHER_SKELETON.getKey().toString());
			add(EntityType.CREEPER.getKey().toString());
			add(EntityType.SPIDER.getKey().toString());
			add(EntityType.CAVE_SPIDER.getKey().toString());
			add(EntityType.ENDERMAN.getKey().toString());
			add(EntityType.ENDERMITE.getKey().toString());
			add(EntityType.PHANTOM.getKey().toString());
			add(EntityType.SILVERFISH.getKey().toString());
			add(EntityType.SHULKER.getKey().toString());
			add(EntityType.BLAZE.getKey().toString());
			add(EntityType.GUARDIAN.getKey().toString());
			add(EntityType.VINDICATOR.getKey().toString());
			add(EntityType.PILLAGER.getKey().toString());
			add(EntityType.WITCH.getKey().toString());
			add(EntityType.RAVAGER.getKey().toString());
			add(EntityType.EVOKER.getKey().toString());
			add(EntityType.GHAST.getKey().toString());

			add(EntityType.IRON_GOLEM.getKey().toString());
			add(EntityType.SNOWMAN.getKey().toString());

			add(EntityType.POLAR_BEAR.getKey().toString());
			add(EntityType.PANDA.getKey().toString());
			add(EntityType.BEE.getKey().toString());
			add(EntityType.RABBIT.getKey().toString());
			add(EntityType.PARROT.getKey().toString());
			add(EntityType.CAT.getKey().toString());
			add(EntityType.COW.getKey().toString());
			add(EntityType.MUSHROOM_COW.getKey().toString());
			add(EntityType.PIG.getKey().toString());
			add(EntityType.WOLF.getKey().toString());
			add(EntityType.FOX.getKey().toString());
			add(EntityType.SHEEP.getKey().toString());
			add(EntityType.CHICKEN.getKey().toString());
			add(EntityType.BAT.getKey().toString());
			add(EntityType.LLAMA.getKey().toString());
			add(EntityType.TRADER_LLAMA.getKey().toString());
			add(EntityType.TURTLE.getKey().toString());
			add(EntityType.OCELOT.getKey().toString());
			add(EntityType.HORSE.getKey().toString());
			add(EntityType.SKELETON_HORSE.getKey().toString());
			add(EntityType.DONKEY.getKey().toString());
			add(EntityType.MULE.getKey().toString());
			add(EntityType.VILLAGER.getKey().toString());
			add(EntityType.WANDERING_TRADER.getKey().toString());

			add(EntityType.DOLPHIN.getKey().toString());
			add(EntityType.SQUID.getKey().toString());
			add(EntityType.COD.getKey().toString());
			add(EntityType.SALMON.getKey().toString());
			add(EntityType.PUFFERFISH.getKey().toString());
			add(EntityType.TROPICAL_FISH.getKey().toString());
		}
	};

	private static final ArrayList<String> charge_money = new ArrayList<String>() {
		private static final long serialVersionUID = -10739381756686327L;
		{
			add("100");
			add("1000");
			add("10000");
			add("100000");
		}
	};

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		if (args.length == 1) {
			return cmd;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("charge")) {
				return charge_money;
			}
			if (args[0].equalsIgnoreCase("set_entity")) {
				if (sender.hasPermission(this.get_permission_head() + ".set")) {
					return Tab_list.contains(entity_type, args[1]);
				}
			}
		}
		return null;
	}

	public String get_permission_head() {
		return Dropper_shop_plugin.instance.get_electric_spawner_manager().get_permission_head();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) { // 如果sender与Player类不匹配
			sender.sendMessage("[刷怪机]必须由玩家执行该命令");
			return true;
		}
		Player player = (Player) sender;
		if (command.getName().equalsIgnoreCase("electric_spawner")) {
			if (args.length > 0) {
				Block look_block = player.getTargetBlockExact(4);
				if (look_block == null) {
					player.sendMessage("[刷怪机]请指向方块");
					return true;
				}
				Electric_spawner electric_spawner = Dropper_shop_plugin.instance.get_electric_spawner_manager()
						.find_existed(look_block.getLocation());
				if (electric_spawner == null) {
					player.sendMessage("[刷怪机]没有检测到完整的刷怪机结构");
					return true;
				}
				if (args[0].equalsIgnoreCase("charge")) {
					int charge_quantity = 0;
					try {
						charge_quantity = Integer.parseInt(args[1]);
					} catch (Exception e) {
						electric_spawner.send_message(player, "数量格式错误");
						return true;
					}
					int current_money = electric_spawner.get_money();
					if (current_money + charge_quantity > max_money) {
						charge_quantity = max_money - current_money;
						electric_spawner.send_message(player, "金币充值超出上限，数量修改为" + charge_quantity);
					}
					if (!player.hasPermission(this.get_permission_head() + ".set")) {
						boolean cost_result = Dropper_shop_plugin.instance.cost_player_money(charge_quantity, player);
						if (cost_result == false) {
							electric_spawner.send_message(player, "你的金币不足" + charge_quantity);
							return true;
						}
					}
					electric_spawner.set_money(current_money + charge_quantity);
					electric_spawner.send_message(player, "成功给刷怪机充值" + charge_quantity + "金币");
				} else if (args[0].equalsIgnoreCase("set_entity")) {
					if (!sender.hasPermission(this.get_permission_head() + ".set")) {
						electric_spawner.send_message(player, "你没有权限直接设置生成实体!");
						return true;
					}
					EntityType new_entity_type;
					try {
						String entity_name = args[1].split(":")[1];
						new_entity_type = EntityType.fromName(entity_name);
					} catch (Exception e) {
						electric_spawner.send_message(player, "实体名称错误");
						return true;
					}
					electric_spawner.set_spawn_entity(new_entity_type);
					electric_spawner.send_message(player, "成功设置刷怪机为" + Entity_zh_cn.get_entity_name(new_entity_type));
				} else if (args[0].equalsIgnoreCase("unset_entity")) {
					if (!sender.hasPermission(this.get_permission_head() + ".set")) {
						electric_spawner.send_message(player, "你没有权限直接设置生成实体!");
						return true;
					}
					electric_spawner.set_spawn_entity(null);
					electric_spawner.send_message(player, "成功设置刷怪机为无");
				}
			} else {
				player.sendMessage("/electric_spawner charge <数量> 给刷怪机充钱");
			}
		}
		return true;
	}

}
