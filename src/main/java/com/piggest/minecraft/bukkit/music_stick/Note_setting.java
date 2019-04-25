package com.piggest.minecraft.bukkit.music_stick;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;

public class Note_setting {
	public Note_setting(Block block, NoteBlock note_block) {
		this.block = block;
		this.note_block = note_block;
	}

	public Block block;
	public NoteBlock note_block;
}
