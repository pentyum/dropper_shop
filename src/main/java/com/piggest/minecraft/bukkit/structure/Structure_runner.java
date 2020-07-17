package com.piggest.minecraft.bukkit.structure;

public interface Structure_runner {
	/**
	 * 获得线程运行周期
	 *
	 * @return 运行周期，单位为tick(50ms)
	 */
	public int get_cycle();

	/**
	 * 获得线程启动的延迟
	 *
	 * @return 启动延迟，单位为tick(50ms)
	 */
	public int get_delay();

	public boolean run_instance(Structure structure);
}
