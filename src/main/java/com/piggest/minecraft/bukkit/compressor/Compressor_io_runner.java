package com.piggest.minecraft.bukkit.compressor;

import com.piggest.minecraft.bukkit.structure.Structure;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;

import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Compressor_io_runner extends Structure_runner {

    public Compressor_io_runner(Compressor_manager manager) {
        super(manager);
    }

    @Override
    public void run_instance(Structure structure) {
        Compressor compressor = (Compressor) structure;

        if (compressor.is_loaded() == false) {
            return;
        }
        Hopper solid_hopper = compressor.get_reactant_hopper();
        if (solid_hopper != null) {
            for (ItemStack item : solid_hopper.getInventory().getContents()) {
                if (!Grinder.is_empty(item)) {
                    if (compressor.add_a_raw(item) == true) {
                        break;
                    }
                }
            }
        }
        Hopper fuel_hopper = compressor.get_piston_hopper();
        if (fuel_hopper != null) {
            for (ItemStack item : fuel_hopper.getInventory().getContents()) {
                if (!Grinder.is_empty(item)) {
                    if (compressor.add_a_piston(item) == true) {
                        break;
                    }
                }
            }
        }

        Chest product_chest = compressor.get_chest();
        if (product_chest != null) { // 输出固体产品
            Inventory_io.move_item_to_inventoryholder(compressor.getInventory(), Compressor.product_slot,
                    product_chest);

        }
    }

    @Override
    public int get_cycle() {
        return 8;
    }

    @Override
    public int get_delay() {
        return 10;
    }


}
