package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.Collection;

@Deprecated
public abstract class Old_structure_runner extends Thread {
	Structure_manager<? extends Structure> manager;

	public Old_structure_runner(Structure_manager<? extends Structure> manager) {
		this.manager = manager;
		this.setName(this.manager.get_permission_head() + ":" + this.getClass().getSimpleName());
	}

	private boolean unload_run = false;

	public boolean unload_run() {
		return this.unload_run;
	}

	public void set_unload_run(boolean unload_run) {
		this.unload_run = unload_run;
	}

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

	@Override
	public void run() {
		if (this.is_asynchronously() == true) {
			try {
				Thread.sleep(this.get_delay() * 50);
			} catch (InterruptedException e) {
				return;
			}
			while (!this.isInterrupted()) {
				long start_time = System.currentTimeMillis();
				int count = 0;
				for (World world : Bukkit.getWorlds()) {
					Collection<?> structures = manager.get_all_structures_in_world(world);
					for (Object structure : structures) {
						this.run_instance((Structure) structure);
						count++;
					}
				}
				int max_time = this.get_cycle() * 50;
				try {
					long sleep_time = max_time - (System.currentTimeMillis() - start_time);
					if (sleep_time > 0) {
						Thread.sleep(sleep_time);
					} else {
						Dropper_shop_plugin.instance.getLogger().warning("线程执行超时" + (-sleep_time) + "ms" + ",实例数量" + count);
					}
				} catch (InterruptedException e) {
					return;
				}
			}
		} else {
			for (World world : Bukkit.getWorlds()) {
				Collection<?> structures = manager.get_all_structures_in_world(world);
				for (Object structure : structures) {
					this.run_instance((Structure) structure);
				}
			}
		}
	}

	public abstract void run_instance(Structure structure);

	public abstract boolean is_asynchronously();
}
