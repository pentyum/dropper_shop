package com.piggest.minecraft.bukkit.teleport_machine;

import com.piggest.minecraft.bukkit.structure.Structure;
import com.piggest.minecraft.bukkit.structure.Old_structure_runner;

public class Teleport_machine_runner extends Old_structure_runner {
	public Teleport_machine_runner(Teleport_machine_manager manager) {
		super(manager);
	}

	@Override
	public void run_instance(Structure structure) {
		Teleport_machine teleport_machine = (Teleport_machine) structure;
		teleport_machine.refresh_power_info();
		if (teleport_machine.get_state() == Radio_state.OFF) {
			return;
		}
		if (teleport_machine.get_amount(Element.Magic) <= 0) {
			teleport_machine.set_switch(Teleport_machine.open_switch, false);
			teleport_machine.set_state(Radio_state.OFF);
			return;
		}
		int need_to_cost_kj = 0;
		teleport_machine.need_to_cost_magic_buffer += teleport_machine.get_current_input_power();
		if (teleport_machine.need_to_cost_magic_buffer >= 1000) {
			need_to_cost_kj = teleport_machine.need_to_cost_magic_buffer / 1000;
			int current_magic = teleport_machine.get_amount(Element.Magic);
			int new_magic = current_magic - need_to_cost_kj;
			if (new_magic < 0) {
				teleport_machine.set_switch(Teleport_machine.open_switch, false);
				teleport_machine.set_state(Radio_state.OFF);
				return;
			} else {
				teleport_machine.set_amount(Element.Magic, new_magic);
				teleport_machine.need_to_cost_magic_buffer -= need_to_cost_kj * 1000;
			}
		}
		// 上，能耗部分；下，自动传送部分
		teleport_machine.start_teleport_to_no_operater();
		// 上，自动传送部分；下，传送进度部分
		if (teleport_machine.get_state() == Radio_state.WORKING) {
			Teleporting_task task = teleport_machine.get_teleporting_task();
			if (task.get_completed_byte() >= task.get_total_byte()) {
				teleport_machine.complete_teleport_to(teleport_machine.get_current_working_with());
				return;
			}
			int current_speed = teleport_machine.get_current_working_with().get_working_speed(teleport_machine);
			int add_byte = current_speed * 1024;
			int new_byte = task.get_completed_byte() + add_byte;
			if (new_byte > task.get_total_byte()) {
				new_byte = task.get_total_byte();
			}
			teleport_machine.set_process(100 * new_byte / task.get_total_byte());
			task.set_completed_byte(new_byte);
		}
	}

	@Override
	public int get_cycle() {
		return 20;
	}

	@Override
	public int get_delay() {
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}
}
