package com.piggest.minecraft.bukkit.teleport_machine;

import com.piggest.minecraft.bukkit.structure.Structure_runner;

public class Teleport_machine_runner extends Structure_runner {
	private Teleport_machine teleport_machine;
	private int need_to_cost_magic = 0;
	
	public Teleport_machine_runner(Teleport_machine teleport_machine) {
		this.teleport_machine = teleport_machine;
	}

	@Override
	public void run() {
		if (teleport_machine.get_amount(Element.Magic) <= 0) {
			teleport_machine.set_state(Radio_state.OFF);
			return;
		}
		int need_to_cost_kj = 0;
		this.need_to_cost_magic += teleport_machine.get_current_input_power();
		if (this.need_to_cost_magic >= 1000) {
			need_to_cost_kj = this.need_to_cost_magic / 1000;
			int current_magic = teleport_machine.get_amount(Element.Magic);
			int new_magic = current_magic - need_to_cost_kj;
			if (new_magic < 0) {
				teleport_machine.set_state(Radio_state.OFF);
				return;
			} else {
				teleport_machine.set_amount(Element.Magic, new_magic);
				this.need_to_cost_magic -= need_to_cost_kj * 1000;
			}
		}
	}

	@Override
	public int get_cycle() {
		return 20;
	}

	@Override
	public int get_delay() {
		// TODO 自动生成的方法存根
		return 10;
	}

	@Override
	public boolean is_asynchronously() {
		return true;
	}

}
