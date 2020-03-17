package com.piggest.minecraft.bukkit.anti_thunder;

import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Piston;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.structure.Multi_block_structure;
import com.piggest.minecraft.bukkit.structure.Ownable;
import net.milkbowl.vault.economy.Economy;

public class Anti_thunder extends Multi_block_structure implements Ownable {
	private String owner;
	private boolean active = false;
	private Anti_thunder_runner runner = new Anti_thunder_runner(this);

	@Override
	public boolean completed() {
		boolean super_structure = super.completed();
		if (super_structure == false) {
			return false;
		}
		Location core_loc = this.get_location();
		int i = 0;
		Piston core_piston_data = this.get_core_piston();
		if (core_piston_data == null) {
			return false;
		}
		if (core_piston_data.getFacing().getModY() != 1) {
			return false;
		}
		Location check_loc = core_loc.clone();
		check_loc.setY(core_loc.getY() + 1);
		for (i = 1; i <= 5; i++) {
			Block pole_block = check_loc.add(0, 1, 0).getBlock();
			if (pole_block.getType() != Material.END_ROD) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected void on_right_click(Player player) {
		return;
	}

	@Override
	public boolean create_condition(Player player) {
		return true;
	}

	@Override
	protected boolean on_break(Player player) {
		this.runner.cancel();
		return true;
	}

	@Override
	public void set_owner(String owner) {
		this.owner = owner;
	}

	@SuppressWarnings("deprecation")
	@Override
	public OfflinePlayer get_owner() {
		return Bukkit.getOfflinePlayer(owner);
	}

	@Override
	public String get_owner_name() {
		return this.owner;
	}

	public boolean is_active() {
		return this.active;
	}

	public boolean activate(boolean active) {
		if (active == true) {
			if (this.completed() == true) {
				if (runner.started() == false) {
					runner.start();
					Dropper_shop_plugin.instance.getLogger().info("[防雷器]已启动扣钱线程");
					runner.runTaskTimerAsynchronously(Dropper_shop_plugin.instance, 0,
							this.get_manager().get_cycle() * 20);
				} else {
					OfflinePlayer owner = this.get_owner();
					Economy economy = Dropper_shop_plugin.instance.get_economy();
					int price = this.get_manager().get_price();
					if (!economy.has(owner, price)) {
						this.send_msg_to_owner("[防雷器]你的钱不够，不能启动防雷器");
						active = false;
						return false;
					}
				}
			} else {
				this.remove();
				this.send_msg_to_owner("[防雷器]区块" + get_chunk_location() + "的防雷器结构不完整，已经移除");
				return false;
			}
		}
		this.active = active;
		return true;
	}

	public void send_msg_to_owner(String msg) {
		if (this.get_owner().isOnline()) {
			this.get_owner().getPlayer().sendMessage(msg);
		} else {
			Dropper_shop_plugin.instance.getLogger().info("[" + this.owner + "]" + msg);
		}
	}

	public Piston get_core_piston() {
		BlockData core_data = this.get_location().getBlock().getBlockData();
		if (!(core_data instanceof Piston)) {
			return null;
		}
		return (Piston) core_data;
	}

	public void close() {
		if (this.runner.started() == true) {
			if (this.runner.isCancelled() == false) {
				Dropper_shop_plugin.instance.getLogger().info("[防雷器]停止扣钱线程");
				this.runner.cancel();
			}
		}
	}

	@Override
	protected void set_from_save(Map<String, Object> shop_save) {
		super.set_from_save(shop_save);
		boolean active = (boolean) shop_save.get("active");
		this.activate(active);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> save = super.serialize();
		save.put("active", this.is_active());
		return save;
	}

	@Override
	public Anti_thunder_manager get_manager() {
		return (Anti_thunder_manager) super.get_manager();
	}

	@Override
	public void init_after_set_location() {
		return;
	}

	@Override
	public ItemStack[] get_drop_items() {
		return null;
	}
	
	@Nonnull
    public static Anti_thunder deserialize(@Nonnull Map<String, Object> args) {
		Anti_thunder structure = new Anti_thunder();
		structure.set_from_save(args);
		return structure;
    }
}
