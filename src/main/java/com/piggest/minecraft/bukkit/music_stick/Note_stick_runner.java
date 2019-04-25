package com.piggest.minecraft.bukkit.music_stick;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.bukkit.scheduler.BukkitRunnable;

public class Note_stick_runner extends BukkitRunnable {
	Queue<Note_setting> queue = new LinkedBlockingQueue<Note_setting>();
	@Override
	public void run() {
		Note_setting setting = this.queue.poll();
		if(setting != null) {
			setting.block.setBlockData(setting.note_block);
		}
	}

}
