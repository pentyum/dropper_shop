package com.piggest.minecraft.bukkit.music_stick;

import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.block.data.type.NoteBlock;

public class Note_stick_listener implements Listener {
	public static String[] note_id1 = new String[] { ".F#", ".G", ".G#", ".A", ".A#", ".B", "C", "C#", "D", "D#", "E",
			"F", "F#", "G", "G#", "A", "A#", "B", "C.", "C#.", "D.", "D#.", "E.", "F.", "F#." };
	public static String[] note_id2 = new String[] { ".4#", ".5", ".5#", ".6", ".6#", ".7", "1", "1#", "2", "2#", "3",
			"4", "4#", "5", "5#", "6", "6#", "7", "1.", "1#.", "2.", "2#.", "3.", "4.", "4#." };
	public Note_stick_runner runner = new Note_stick_runner();
	
	public static int get_id(String str) {
		int i;
		for (i = 0; i < 25; i++) {
			if (str.equals(note_id1[i])) {
				return i;
			}
		}
		for (i = 0; i < 25; i++) {
			if (str.equals(note_id2[i])) {
				return i;
			}
		}
		return -1;
	}

	@EventHandler
	public void on_look(PlayerInteractEvent event) {
		if (event.isCancelled() == true) {
			return;
		}
		Player player = event.getPlayer();
		if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (event.getItem().getType() == Material.STICK
					&& event.getClickedBlock().getType() == Material.NOTE_BLOCK) {
				player.sendMessage(
						"请输入音调，从低到高依次为.4# .5 .5# .6 .6# .7 1 1# 2 2# 3 4 4# 5 5# 6 6# 7 1. 1#. 2. 2#. 3. 4. 4#.");
			}
		}
	}

	@EventHandler
	public void on_set(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getInventory().getItemInMainHand();
		Block block = player.getTargetBlockExact(5);
		if (item.getType() == Material.STICK && block.getType() == Material.NOTE_BLOCK) {
			NoteBlock note_block = (NoteBlock) block.getBlockData();
			int id = get_id(event.getMessage());
			if (id == -1) {
				player.sendMessage("你输入的音调不正确");
			} else {
				note_block.setNote(new Note(id));
				Note_setting setting = new Note_setting(block,note_block);
				runner.queue.add(setting);
				player.sendMessage("已设置音调为" + event.getMessage());
			}
			event.setCancelled(true);
		}
	}
}
