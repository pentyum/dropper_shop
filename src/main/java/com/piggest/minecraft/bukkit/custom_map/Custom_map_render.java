package com.piggest.minecraft.bukkit.custom_map;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Custom_map_render extends MapRenderer {
	public static final int pic_size = 128;

	@Override
	public void initialize(@Nonnull MapView map) {
		super.initialize(map);
		map.setLocked(true);
	}

	@Nullable
	public static Custom_map_render get_render_from_item(@Nullable ItemStack item) {
		if (!Grinder.is_empty(item)) {
			if (item.getType() == Material.FILLED_MAP) {
				MapMeta meta = (MapMeta) item.getItemMeta();
				if (meta != null) {
					if (meta.hasMapView()) {
						MapRenderer render = meta.getMapView().getRenderers().get(0);
						if (render instanceof Custom_map_render) {
							return (Custom_map_render) render;
						}
					}
				}
			}
		}
		return null;
	}

}
