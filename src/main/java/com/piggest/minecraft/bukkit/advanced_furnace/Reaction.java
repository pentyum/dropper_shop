package com.piggest.minecraft.bukkit.advanced_furnace;

public class Reaction {
	private Chemical[] reactants_chemicals;
	private Chemical[] products_chemicals;
	private int[] reactants_coefficients;
	private int[] products_coefficients;
	private boolean reversible;
	private double positive_rate;
	private double negative_rate;
	private double ea_p;
	private double ea_n;

	/*
	 * 新建反应，reversible表示是否可逆，positive_rate为正反应速率系数，ea_p为正反应活化能(J/mol)，
	 * negative_rate为逆反应速率系数，ea_n为逆反应活化能(J/mol)
	 */
	public Reaction(boolean reversible, double positive_rate, double ea_p, double negative_rate, double ea_n) {
		this.reversible = reversible;
		this.positive_rate = positive_rate;
		this.negative_rate = negative_rate;
		this.ea_n = ea_n;
		this.ea_p = ea_p;
	}

	/*
	 * 新建不可逆反应，positive_rate为正反应速率系数，positive_rate为正反应速率系数，ea_p为正反应活化能(J/mol)，
	 * ea_n为逆反应活化能(J/mol)
	 */
	public Reaction(double positive_rate, double ea_p, double ea_n) {
		this.reversible = false;
		this.positive_rate = positive_rate;
		this.negative_rate = 0;
		this.ea_n = ea_n;
		this.ea_p = ea_p;
	}

	public void set_reactants(Chemical... chemicals) {
		this.reactants_chemicals = chemicals;
	}

	public void set_products(Chemical... chemicals) {
		this.products_chemicals = chemicals;
	}

	public void set_reactants_coef(int... coefs) {
		this.reactants_coefficients = coefs;
	}

	public void set_products_coef(int... coefs) {
		this.products_coefficients = coefs;
	}

	public Chemical[] get_reactants() {
		return this.reactants_chemicals;
	}

	public int[] get_reactants_coef() {
		return this.reactants_coefficients;
	}

	public Chemical[] get_products() {
		return this.products_chemicals;
	}

	public int[] get_products_coef() {
		return this.products_coefficients;
	}

	/*
	 * 反应速率：每单位每0.25秒
	 */
	public int get_current_positive_rate(double[] c, double temp) {
		if (c.length != reactants_chemicals.length) {
			return 0;
		}
		double k = this.positive_rate * Math.exp(-ea_p / 8.314 / temp);
		for (int i = 0; i < this.reactants_chemicals.length; i++) {
			if (this.reactants_chemicals[i] instanceof Gas) {
				k *= Math.pow(c[i], this.reactants_coefficients[i]);
			}
			i++;
		}
		return (int) k;
	}

	public int get_current_negative_rate(double[] c, double temp) {
		if (this.reversible == false) {
			return 0;
		} else {
			if (c.length != products_chemicals.length) {
				return 0;
			}
			double k = this.negative_rate * Math.exp(-ea_n / 8.314 / temp);
			for (int i = 0; i < this.products_chemicals.length; i++) {
				if (this.products_chemicals[i] instanceof Gas) {
					k *= Math.pow(c[i], this.products_coefficients[i]);
				}
				i++;
			}
			return (int) k;
		}
	}
}