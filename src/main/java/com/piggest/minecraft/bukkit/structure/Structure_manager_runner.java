package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Structure_manager_runner extends BukkitRunnable {
	protected final Structure_manager<? extends Structure> manager;
	protected final int max_time;

	public Structure_manager_runner(Structure_manager<? extends Structure> manager) {
		this.manager = manager;
		this.max_time = this.get_cycle() * 50;
	}

	@Override
	public void run() {
		long start_time = System.currentTimeMillis();
		this.exec();
		long sleep_time = max_time - (System.currentTimeMillis() - start_time);
		if (sleep_time < 0) {
			manager.get_logger().warning(this.getClass().getSimpleName() + "线程执行超时" + (-sleep_time) + "ms");
		}
	}

	public abstract void exec();

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
