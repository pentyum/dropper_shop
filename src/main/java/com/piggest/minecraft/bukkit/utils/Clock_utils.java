package com.piggest.minecraft.bukkit.utils;

public class Clock_utils {
	public static double get_sec_pa(int sec) {
		return (double) sec * 2 * Math.PI / 60;
	}

	public static double get_min_pa(int min, int sec) {
		return ((double) min + (double) sec / 60) * 2 * Math.PI / 60;
	}

	public static double get_hr_pa(int hr, int min) {
		return ((double) hr + (double) min / 60) * 2 * Math.PI / 12;
	}

	public static class Clock_pos_data {
		public int sec_head_x;
		public int sec_head_y;
		public int min_head_x;
		public int min_head_y;
		public int hr_head_x;
		public int hr_head_y;
		public int sec_tail_x;
		public int sec_tail_y;

		public Clock_pos_data(int hr, int min, int sec, int size, double hr_rsize, double min_rsize, double sec_rsize,
							  double tail_rsize) {
			double a_sec = get_sec_pa(sec);
			double a_min = get_min_pa(min, sec);
			double a_hour = get_hr_pa(hr, min);

			double sec_length = size * sec_rsize;
			double min_length = size * min_rsize;
			double hr_length = size * hr_rsize;
			double tail_length = size * tail_rsize;

			sec_head_x = size / 2 + (int) (sec_length * Math.sin(a_sec));
			sec_head_y = size / 2 - (int) (sec_length * Math.cos(a_sec));
			min_head_x = size / 2 + (int) (min_length * Math.sin(a_min));
			min_head_y = size / 2 - (int) (min_length * Math.cos(a_min));
			hr_head_x = size / 2 + (int) (hr_length * Math.sin(a_hour));
			hr_head_y = size / 2 - (int) (hr_length * Math.cos(a_hour));

			sec_tail_x = size / 2 - (int) (tail_length * Math.sin(a_sec));
			sec_tail_y = size / 2 + (int) (tail_length * Math.cos(a_sec));
			// min_tail_x=size/2-(int)(15*Math.sin(a_min));
			// min_tail_y=size/2+(int)(15*Math.cos(a_min));
			// hr_tail_x =size/2-(int)(5 *Math.sin(a_hour));
			// hr_tail_y =size/2+(int)(5 *Math.cos(a_hour));
		}
	}
}
