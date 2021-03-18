package com.piggest.minecraft.bukkit.structure;

import com.piggest.minecraft.bukkit.dropper_shop.Dropper_shop_plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Structure_manager_logger extends Logger {
	//private final String log_prefix;

	public Structure_manager_logger(Structure_manager<? extends Structure> structure_manager) {
		super(structure_manager.get_gui_name(), null);
		this.setParent(Dropper_shop_plugin.instance.getLogger());
		this.setLevel(Level.ALL);
        /*
        if (structure_manager.get_gui_name() != null) {
            this.log_prefix = "[" + structure_manager.get_gui_name() + "] ";
        } else {
            this.log_prefix = "[" + structure_manager.get_permission_head() + "] ";
        }
        */
	}

    /*
    @Override
    public void log(@Nonnull LogRecord logRecord) {
        logRecord.setMessage(this.log_prefix + logRecord.getMessage());
        super.log(logRecord);
    }

    @Nonnull
    public String get_predix() {
        return this.log_prefix;
    }

     */
}
