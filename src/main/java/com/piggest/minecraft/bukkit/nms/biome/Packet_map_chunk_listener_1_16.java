package com.piggest.minecraft.bukkit.nms.biome;

import com.comphenix.protocol.events.PacketEvent;

public class Packet_map_chunk_listener_1_16 {
	//public class Packet_map_chunk_listener_1_16 extends PacketAdapter {
	Biome_modifier_1_16 biome_modifier;

	public Packet_map_chunk_listener_1_16() {
		/*
		super(Dropper_shop_plugin.instance, ListenerPriority.NORMAL, PacketType.Play.Server.MAP_CHUNK);
		this.biome_modifier = (Biome_modifier_1_16) NMS_manager.biome_modifier;
		 */
	}

	//@Override
	public void onPacketSending(PacketEvent event) {/*
		PacketContainer packet = event.getPacket();
		StructureModifier<BiomeStorage> modifier = packet.getSpecificModifier(BiomeStorage.class);
		BiomeStorage biome_storge = modifier.read(0);
		Biome_storage_modifier_1_16 provider = (Biome_storage_modifier_1_16) NMS_manager.biome_modifier
				.get_biome_storge_modifier();
		BiomeBase[] biomes = provider.get_biomes(biome_storge);
		if (biomes == null) {
			return;
		}
		Arrays.parallelSetAll(biomes, new IntFunction<BiomeBase>() {
			@Override
			public BiomeBase apply(int i) {
				Biome biome = biome_modifier.get_biome(biomes[i]);
				Biome pretend = Dropper_shop_plugin.instance.get_biome_modify().get_pretend_biome(biome);
				return biome_modifier.get_biomebase(pretend);
			}
		});
		modifier.write(0, biome_storge);
		*/
	}
}
