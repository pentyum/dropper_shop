package com.piggest.minecraft.bukkit.advanced_furnace;

import com.piggest.minecraft.bukkit.structure.Structure;
import org.bukkit.inventory.ItemStack;
import com.piggest.minecraft.bukkit.material_ext.Material_ext;
import com.piggest.minecraft.bukkit.structure.Structure_runner;
import com.piggest.minecraft.bukkit.utils.Inventory_io;

public class Advanced_furnace_temp_runner extends Structure_runner {
    public Advanced_furnace_temp_runner(Advanced_furnace_manager manager) {
        super(manager);
    }

    public int get_cycle() {
        return 2;
    }

    @Override
    public void run_instance(Structure structure) {
        Advanced_furnace adv_furnace = (Advanced_furnace) structure;
        if (adv_furnace.is_loaded() == false) {
            return;
        }
        synchronized (adv_furnace.fuel_info) {
            double current_temp = adv_furnace.get_temperature();
            double p = adv_furnace.get_power();
            double loss = adv_furnace.get_power_loss();
            double d_current_temp = p - loss;
            adv_furnace.set_temperature(current_temp + this.get_cycle() * d_current_temp);
            if (adv_furnace.get_fuel() != null) {
                // Bukkit.getLogger().info("当前燃料存在");
                this.run_fuel(adv_furnace);
            } else {
                // Bukkit.getLogger().info(Thread.currentThread().getId()+"当前燃料为null，准备吸取燃料");
                this.get_fuel(adv_furnace);
            }
        }

    }

    private void run_fuel(Advanced_furnace adv_furnace) {
        Fuel fuel = adv_furnace.get_fuel();
        int max_ticks = 0;
        switch (fuel.status) {
            case solid:
                max_ticks = (int) (fuel.ticks * adv_furnace.get_time_modify());
                break;
            case liquid:
            case gas:
                max_ticks = (int) (fuel.ticks * adv_furnace.fuel_info.fuel_amount * adv_furnace.get_time_modify());
                break;
            default:
                max_ticks = (int) (fuel.ticks * adv_furnace.get_time_modify());
        }
        int last_sec = (max_ticks - adv_furnace.fuel_info.fuel_ticks) / 20;
        adv_furnace.set_last_sec(last_sec);
        if (adv_furnace.fuel_info.fuel_ticks >= max_ticks) {
            adv_furnace.set_fuel(null, 0);
            adv_furnace.fuel_info.fuel_ticks = 0;
            return;
        }
        adv_furnace.fuel_info.fuel_ticks += this.get_cycle();
    }

    private void get_fuel(Advanced_furnace adv_furnace) {
        ItemStack fuel_item = adv_furnace.get_gui_item(Advanced_furnace.fuel_slot);
        if (!Inventory_io.is_empty(fuel_item)) {
            Fuel fuel = Fuel.get_fuel(fuel_item);
            if (fuel != null) {
                int default_amount = 0;
                if (fuel.status != Status.solid) {
                    synchronized (adv_furnace.getInventory()) {
                        ItemStack bucket = Material_ext.get_empty_container(fuel_item);
                        if (!Inventory_io.try_move_item_to_slot(bucket, 1, adv_furnace.getInventory(),
                                Advanced_furnace.fuel_product_slot)) {
                            return;
                        }
                        if (fuel.status == Status.gas) {
                            default_amount = Gas.get_item_unit(fuel_item, (Gas) fuel.to_chemical());
                        } else if (fuel.status == Status.liquid) {
                            default_amount = Liquid.get_item_unit(fuel_item);
                        }
                        if (Inventory_io.item_remove(fuel_item, 1) != null) {
                            adv_furnace.set_fuel(fuel, default_amount);
                            Inventory_io.move_item_to_slot(bucket, 1, adv_furnace.getInventory(),
                                    Advanced_furnace.fuel_product_slot);
                        }
                    }
                } else {
                    if (Inventory_io.item_remove(fuel_item, 1) != null) {
                        // Bukkit.getLogger().info(Thread.currentThread().getId()+"开始设置燃料为"+fuel.name());
                        adv_furnace.set_fuel(fuel, default_amount);
                    }
                }
            }
        }
    }

    @Override
    public int get_delay() {
        return 10;
    }

}
