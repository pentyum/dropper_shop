package com.piggest.minecraft.bukkit.structure;

import org.bukkit.scheduler.BukkitTask;

public abstract class Structure_runner<T extends Structure> implements Runnable {
	protected BukkitTask task;

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

	public abstract boolean run_instance(T structure);

	public abstract void start();

	public void cancel() {
		this.task.cancel();
	}
}
