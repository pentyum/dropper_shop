package com.piggest.minecraft.bukkit.trees_felling_machine;

import com.piggest.minecraft.bukkit.structure.Structure;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Trees_felling_machine_runner extends Structure_runner {
    public Trees_felling_machine_runner(Trees_felling_machine_manager manager) {
        super(manager);
    }

    @Override
    public void run_instance(Structure structure) {
        Trees_felling_machine machine = (Trees_felling_machine) structure;
        if (!machine.is_loaded()) {
            return;
        }
        Hopper hopper = machine.get_axe_hopper();
        if (hopper != null) {
            for (ItemStack item : hopper.getInventory().getContents()) {
                if (!Grinder.is_empty(item)) {
                    machine.add_a_axe(item);
                }
            }
        }
        if (machine.is_working()) {
            machine.do_next();
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
