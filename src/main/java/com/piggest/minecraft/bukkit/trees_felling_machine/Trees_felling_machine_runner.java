package com.piggest.minecraft.bukkit.trees_felling_machine;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;
import com.piggest.minecraft.bukkit.gui.Gui_slot_type;
import com.piggest.minecraft.bukkit.structure.Structure;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.grinder.Grinder;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

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
        int price = Dropper_shop_plugin.instance.get_price_config().get_start_trees_felling_machine_price();
        Inventory gui = machine.getInventory();
        ItemStack lever = gui.getItem(9);
        if (!Grinder.is_empty(lever)) {
            ItemMeta meta = lever.getItemMeta();
            meta.setDisplayName("§r伐木机开关(开启需要" + Dropper_shop_plugin.instance.get_economy().format(price) + ")");
            lever.setItemMeta(meta);
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
