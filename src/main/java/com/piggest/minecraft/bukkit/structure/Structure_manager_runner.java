package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Structure_manager_runner extends BukkitRunnable {

	@Override
	public abstract void run();

	/**
	 * 获得线程运行周期
	 *
	 * @return 运行周期，单位为tick(50ms)
	 */
	public abstract int get_cycle();

	/**
	 * 获得线程启动的延迟
	 *
	 * @return 启动延迟，单位为tick(50ms)
	 */
	public abstract int get_delay();

	public abstract boolean is_asynchronously();

	public void start() {
		if (this.is_asynchronously()) {
			this.runTaskTimerAsynchronously(Dropper_shop_plugin.instance, this.get_delay(), this.get_cycle());
		} else {
			this.runTaskTimer(Dropper_shop_plugin.instance, this.get_delay(), this.get_cycle());
		}
	}
}
