package com.piggest.minecraft.bukkit.teleport_machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public enum Base_material implements Has_composition {
	hygrogen_molecule(new Has_composition[] {Element.H},new int[] {2}),
	oxygen_molecule(new Has_composition[] {Element.O},new int[] {2}),
	nitrogen_molecule(new Has_composition[] {Element.N},new int[] {2}),
	chlorine_molecule(new Has_composition[] {Element.Cl},new int[] {2}),
	CO_molecule(new Has_composition[] {Element.C,Element.O},new int[] {1,1}),
	CH4_molecule(new Has_composition[] {Element.C,Element.H},new int[] {1,4}),
	NH3_molecule(new Has_composition[] {Element.N,Element.H},new int[] {1,3}),
	NO_molecule(new Has_composition[] {Element.N,Element.O},new int[] {1,1}),
	NO2_molecule(new Has_composition[] {Element.N,Element.O},new int[] {1,2}),
	SO2_molecule(new Has_composition[] {Element.S,Element.O},new int[] {1,2}),
	H2S_molecule(new Has_composition[] {Element.H,Element.S},new int[] {2,1}),
	HCl_molecule(new Has_composition[] {Element.H,Element.Cl},new int[] {1,1}),
	HCN_molecule(new Has_composition[] {Element.H,Element.C,Element.N},new int[] {1,1,1}),
	HF_molecule(new Has_composition[] {Element.H,Element.F},new int[] {1,1}),
	water_molecule(new Has_composition[] {Element.H,Element.O},new int[] {2,1}),
	SiO2_molecule(new Has_composition[] {Element.Si,Element.O},new int[] {1,2}),
	CaO_molecule(new Has_composition[] {Element.Ca,Element.O},new int[] {1,1}),
	MgO_molecule(new Has_composition[] {Element.Mg,Element.O},new int[] {1,1}),
	CO2_molecule(new Has_composition[] {Element.C,Element.O},new int[] {1,2}),
	CaCO3_molecule(new Has_composition[] {CaO_molecule,CO2_molecule},new int[] {1,1}),
	MgCO3_molecule(new Has_composition[] {MgO_molecule,CO2_molecule},new int[] {1,1}),
	Al2O3_molecule(new Has_composition[] {Element.Al,Element.O},new int[] {2,3}),
	Fe2O3_molecule(new Has_composition[] {Element.Fe,Element.O},new int[] {2,3}),
	FeO_molecule(new Has_composition[] {Element.Fe,Element.O},new int[] {1,1}),
	NaCl_molecule(new Has_composition[] {Element.Na,Element.O},new int[] {1,1}),
	Na2O_molecule(new Has_composition[] {Element.Na,Element.O},new int[] {2,2}),
	K2O_molecule(new Has_composition[] {Element.K,Element.O},new int[] {2,1}),
	KCl_molecule(new Has_composition[] {Element.K,Element.Cl},new int[] {1,1}),
	KClO3_molecule(new Has_composition[] {Element.K,Element.Cl,Element.O},new int[] {1,1,3}),
	CaF2_molecule(new Has_composition[] {Element.Ca,Element.F},new int[] {1,2}),
	CaCl2_molecule(new Has_composition[] {Element.Ca,Element.Cl},new int[] {1,2}),
	MgCl2_molecule(new Has_composition[] {Element.Mg,Element.Cl},new int[] {1,2}),
	CuS_molecule(new Has_composition[] {Element.Cu,Element.S},new int[] {1,1}),
	ZnS_molecule(new Has_composition[] {Element.Zn,Element.S},new int[] {1,1}),
	FeS2_molecule(new Has_composition[] {Element.Fe,Element.S},new int[] {1,2}),
	iron_hydroxide_molecule(new Has_composition[] {Element.Fe,Element.O,Element.H},new int[] {1,3,3}),
	sucrose_molecule(new Has_composition[] {Element.C,Element.H,Element.O},new int[] {6,10,5}),//碳水化合物
	polysaccharide_molecule(new Has_composition[] {Element.C,Element.H,Element.O},new int[] {12,22,11}), //蔗糖
	tnt_molecule(new Has_composition[] {Element.C,Element.H,Element.O,Element.N},new int[] {7,5,6,3}),
	hydroxyapatite_molecule(new Has_composition[] {Element.Ca,Element.P,Element.O,Element.H},new int[] {5,3,13,1}),  //羟基磷酸钙
	
	blaze_powder(new Has_composition[] {Element.S},new int[] {100}),
	blaze_rod(new Has_composition[] {blaze_powder},new int[] {3}),
	
	vine(new Has_composition[] {sucrose_molecule,water_molecule},new int[] {10,10}),
	slime_ball(new Has_composition[] {sucrose_molecule,water_molecule},new int[] {10,90}),
	slime_block(new Has_composition[] {slime_ball},new int[] {9}),
	magma_cream(new Has_composition[] {slime_ball,blaze_powder},new int[] {1,1}),
	
	bone_meal(new Has_composition[] {hydroxyapatite_molecule},new int[] {10}),
	bone(new Has_composition[] {bone_meal},new int[] {4}),
	bone_block(new Has_composition[] {bone_meal},new int[] {9}),
	
	white_dye(new Has_composition[] {bone_meal},new int[] {1}),
	black_dye(new Has_composition[] {Element.C},new int[] {100}),
	ink_sac(new Has_composition[] {black_dye},new int[] {1}),
	
	wool_base(new Has_composition[] {Element.C,Element.H,Element.O,Element.N,Element.S},new int[] {78,154,67,20,5}),
	carpet(new Has_composition[] {wool_base},new int[] {2}),
	wool(new Has_composition[] {wool_base},new int[] {3}),
	string(new Has_composition[] {wool_base},new int[] {1}),
	cobweb(new Has_composition[] {string},new int[] {9}),
	
	leather(new Has_composition[] {wool},new int[] {1}),
	
	stone_slab(new Has_composition[] {SiO2_molecule},new int[] {500}),
	stone(new Has_composition[] {stone_slab},new int[] {2}),
	cobblestone_slab(new Has_composition[] {stone_slab,water_molecule},new int[] {1,50}),
	cobblestone(new Has_composition[] {cobblestone_slab},new int[] {2}),
	mossy_cobblestone(new Has_composition[] {cobblestone,vine},new int[] {1,1}),
	mossy_cobblestone_slab(new Has_composition[] {mossy_cobblestone},new double[] {0.5}),
	lava(new Has_composition[] {SiO2_molecule},new int[] {1000}),
	ore(new Has_composition[] {SiO2_molecule},new int[] {400}),
	furnace(new Has_composition[] {cobblestone},new int[] {8}),
	brewing_stand(new Has_composition[] {blaze_rod,cobblestone},new int[] {1,3}),
	
	stone_bricks(new Has_composition[] {stone},new int[] {1}),
	mossy_stone_bricks(new Has_composition[] {stone,vine},new int[] {1,1}),
	cracked_stone_bricks(new Has_composition[] {stone},new int[] {1}),
	
	sand(new Has_composition[] {SiO2_molecule},new int[] {300}),
	red_sand(new Has_composition[] {SiO2_molecule,Fe2O3_molecule},new int[] {300,50}),
	glass(new Has_composition[] {sand},new int[] {1}),
	sandstone_slab(new Has_composition[] {sand},new int[] {2}),
	sandstone(new Has_composition[] {sand},new int[] {4}),
	red_sandstone_slab(new Has_composition[] {red_sand},new int[] {2}),
	red_sandstone(new Has_composition[] {red_sand},new int[] {4}),
	
	granite(new Has_composition[] {SiO2_molecule},new int[] {1000}), //花岗岩
	diorite(new Has_composition[] {SiO2_molecule},new int[] {1000}), //闪长岩
	andesite(new Has_composition[] {SiO2_molecule},new int[] {1000}), //安山岩
	
	nether_wart(new Has_composition[] {iron_hydroxide_molecule},new int[] {200}),
	netherrack(new Has_composition[] {SiO2_molecule,CuS_molecule},new int[] {500,10}),
	netherore(new Has_composition[] {SiO2_molecule,CuS_molecule},new int[] {350,5}),
	nether_brick(new Has_composition[] {netherrack},new int[] {1}),
	nether_bricks(new Has_composition[] {nether_brick},new int[] {4}),
	red_nether_bricks(new Has_composition[] {nether_brick,nether_wart},new int[] {2,2}),
	
	prismarine_shard(new Has_composition[] {SiO2_molecule,CaCO3_molecule,MgCO3_molecule},new int[] {100,50,20}),
	prismarine_crystals(new Has_composition[] {NaCl_molecule,KCl_molecule,MgCl2_molecule,CaCl2_molecule},new int[] {150,15,25,15}),
	prismarine(new Has_composition[] {prismarine_shard},new int[] {4}),
	dark_prismarine(new Has_composition[] {SiO2_molecule,black_dye},new int[] {8,1}),
	prismarine_bricks(new Has_composition[] {prismarine_shard},new int[] {9}),
	
	gunpowder(new Has_composition[] {tnt_molecule},new int[] {15}),
	tnt(new Has_composition[] {gunpowder,sand},new int[] {5,2}),
	
	snowball(new Has_composition[] {water_molecule},new int[] {100}),
	snow_block(new Has_composition[] {snowball},new int[] {4}),
	water(new Has_composition[] {water_molecule},new int[] {1000}),
	ice(new Has_composition[] {water},new int[] {1}),
	packed_ice(new Has_composition[] {ice},new int[] {9}),
	blue_ice(new Has_composition[] {packed_ice},new int[] {9}),
	
	sugar(new Has_composition[] {polysaccharide_molecule},new int[] {20}),
	paper(new Has_composition[] {sucrose_molecule},new int[] {16}),
	sugar_cane(new Has_composition[] {sugar,paper},new int[] {1,1}),
	
	sapling(new Has_composition[] {sucrose_molecule,water_molecule},new int[] {8,12}),
	leaves(new Has_composition[] {sucrose_molecule,water_molecule},new int[] {8,12}),
	bamboo(new Has_composition[] {sucrose_molecule},new int[] {4}),
	stick(new Has_composition[] {sucrose_molecule},new int[] {8}),
	planks_slab(new Has_composition[] {stick},new int[] {1}),
	planks_stairs(new Has_composition[] {stick},new double[] {4.0/3.0}),
	planks(new Has_composition[] {stick},new int[] {2}),
	wood(new Has_composition[] {planks},new int[] {4}),
	crafting_table(new Has_composition[] {planks},new int[] {4}),
	wood_fence(new Has_composition[] {stick},new int[] {3}),
	chest(new Has_composition[] {planks},new int[] {8}),
	boat(new Has_composition[] {planks},new int[] {5}),
	barrel(new Has_composition[] {planks,planks_slab},new int[] {6,2}),
	ladder(new Has_composition[] {stick},new int[] {2}),
	planks_door(new Has_composition[] {planks},new int[] {2}),
	planks_trapdoor(new Has_composition[] {planks},new int[] {3}),
	lever(new Has_composition[] {cobblestone,stick},new int[] {1,1}),
	grindstone(new Has_composition[] {planks,stone_slab,stick},new int[] {2,1,2}),
	book(new Has_composition[] {paper,leather},new int[] {3,1}),
	bookshelf(new Has_composition[] {planks,book},new int[] {6,3}),
	lectern(new Has_composition[] {planks_slab,bookshelf},new int[] {4,1}),
	
	wooden_sword(new Has_composition[] {planks,stick},new int[] {2,1}),
	wooden_shovel(new Has_composition[] {planks,stick},new int[] {1,2}),
	wooden_pickaxe(new Has_composition[] {planks,stick},new int[] {3,2}),
	wooden_axe(new Has_composition[] {planks,stick},new int[] {3,2}),
	wooden_hoe(new Has_composition[] {planks,stick},new int[] {2,2}),
	stone_sword(new Has_composition[] {cobblestone,stick},new int[] {2,1}),
	stone_shovel(new Has_composition[] {cobblestone,stick},new int[] {1,2}),
	stone_pickaxe(new Has_composition[] {cobblestone,stick},new int[] {3,2}),
	stone_axe(new Has_composition[] {cobblestone,stick},new int[] {3,2}),
	stone_hoe(new Has_composition[] {cobblestone,stick},new int[] {2,2}),
	
	glowstone_dust(new Has_composition[] {CaF2_molecule},new int[] {250}),
	glowstone(new Has_composition[] {glowstone_dust},new int[] {4}),

	charcoal(new Has_composition[] {Element.C},new int[] {1000}),
	coal_powder(new Has_composition[] {Element.C},new int[] {1000}),
	coal(new Has_composition[] {coal_powder},new int[] {1}),
	coal_ore(new Has_composition[] {coal_powder,ore},new int[] {2,1}),
	coal_block(new Has_composition[] {coal},new int[] {9}),
	torch(new Has_composition[] {coal,stick},new double[] {0.25,0.25}),
	fire_charge(new Has_composition[] {gunpowder,coal,blaze_powder},new double[] {1.0/3.0,1.0/3.0,1.0/3.0}),

	lead(new Has_composition[] {string,slime_ball},new double[] {2,0.5}),
	bow(new Has_composition[] {string,stick},new int[] {3,3}),
	fishing_rod(new Has_composition[] {string,stick},new int[] {2,3}),
	scaffolding(new Has_composition[] {string,bamboo},new double[] {1.0/6.0,1}),
	
	iron_nugget(new Has_composition[] {Element.Fe},new int[] {100}),
	iron_powder(new Has_composition[] {Element.Fe},new int[] {1000}),
	iron_ingot(new Has_composition[] {iron_powder},new int[] {1}),
	iron_block(new Has_composition[] {iron_ingot},new int[] {9}),
	iron_ore(new Has_composition[] {Fe2O3_molecule,ore},new int[] {1000,1}),
	bucket(new Has_composition[] {iron_ingot},new int[] {3}),
	water_bucket(new Has_composition[] {bucket,water},new int[] {1,1}),
	lava_bucket(new Has_composition[] {bucket,lava},new int[] {1,1}),
	iron_sword(new Has_composition[] {iron_ingot,stick},new int[] {2,1}),
	iron_shovel(new Has_composition[] {iron_ingot,stick},new int[] {1,2}),
	iron_pickaxe(new Has_composition[] {iron_ingot,stick},new int[] {3,2}),
	iron_axe(new Has_composition[] {iron_ingot,stick},new int[] {3,2}),
	iron_hoe(new Has_composition[] {iron_ingot,stick},new int[] {2,2}),
	iron_helmet(new Has_composition[] {iron_ingot},new int[] {5}),
	iron_chestplate(new Has_composition[] {iron_ingot},new int[] {8}),
	iron_leggings(new Has_composition[] {iron_ingot},new int[] {7}),
	iron_boots(new Has_composition[] {iron_ingot},new int[] {4}),
	iron_door(new Has_composition[] {iron_ingot},new int[] {2}),
	iron_trapdoor(new Has_composition[] {iron_ingot},new int[] {4}),
	anvil(new Has_composition[] {iron_block,iron_ingot},new int[] {3,4}),
	wrench(new Has_composition[] {iron_ingot},new int[] {6}),
	cauldron(new Has_composition[] {iron_ingot},new int[] {7}),
	minecart(new Has_composition[] {iron_ingot},new int[] {5}),
	chest_minecart(new Has_composition[] {chest,minecart},new int[] {1,1}),
	furnace_minecart(new Has_composition[] {furnace,minecart},new int[] {1,1}),
	tnt_minecart(new Has_composition[] {tnt,minecart},new int[] {1,1}),
	rail(new Has_composition[] {iron_ingot,stick},new double[] {6.0/16.0,1.0/16.0}),
	iron_bars(new Has_composition[] {iron_ingot},new double[] {6.0/16.0}),
	blast_furnace(new Has_composition[] {iron_ingot,furnace,stone},new int[] {5,1,3}),
	smoker(new Has_composition[] {wood,furnace},new int[] {4,1}),
	
	gold_nugget(new Has_composition[] {Element.Au},new int[] {100}),
	gold_powder(new Has_composition[] {Element.Au},new int[] {1000}),
	gold_ingot(new Has_composition[] {gold_powder},new int[] {1}),
	gold_block(new Has_composition[] {gold_ingot},new int[] {9}),
	gold_ore(new Has_composition[] {Element.Au,Element.Ag,ore},new int[] {1000,1000,1}),
	golden_sword(new Has_composition[] {gold_ingot,stick},new int[] {2,1}),
	golden_shovel(new Has_composition[] {gold_ingot,stick},new int[] {1,2}),
	golden_pickaxe(new Has_composition[] {gold_ingot,stick},new int[] {3,2}),
	golden_axe(new Has_composition[] {gold_ingot,stick},new int[] {3,2}),
	golden_hoe(new Has_composition[] {gold_ingot,stick},new int[] {2,2}),
	golden_helmet(new Has_composition[] {gold_ingot},new int[] {5}),
	golden_chestplate(new Has_composition[] {gold_ingot},new int[] {8}),
	golden_leggings(new Has_composition[] {gold_ingot},new int[] {7}),
	golden_boots(new Has_composition[] {gold_ingot},new int[] {4}),
	
	silver_powder(new Has_composition[] {Element.Ag},new int[] {1000}),
	copper_powder(new Has_composition[] {Element.Cu},new int[] {1000}),
	
	diamond(new Has_composition[] {Element.C},new int[] {1500}),
	diamond_block(new Has_composition[] {diamond},new int[] {9}),
	diamond_ore(new Has_composition[] {diamond,ore},new int[] {2,1}),
	diamond_sword(new Has_composition[] {diamond,stick},new int[] {2,1}),
	diamond_shovel(new Has_composition[] {diamond,stick},new int[] {1,2}),
	diamond_pickaxe(new Has_composition[] {diamond,stick},new int[] {3,2}),
	diamond_axe(new Has_composition[] {diamond,stick},new int[] {3,2}),
	diamond_hoe(new Has_composition[] {diamond,stick},new int[] {2,2}),
	diamond_helmet(new Has_composition[] {diamond},new int[] {5}),
	diamond_chestplate(new Has_composition[] {diamond},new int[] {8}),
	diamond_leggings(new Has_composition[] {diamond},new int[] {7}),
	diamond_boots(new Has_composition[] {diamond},new int[] {4}),
	
	quartz(new Has_composition[] {SiO2_molecule},new int[] {300}),
	quartz_block(new Has_composition[] {quartz},new int[] {4}),
	quartz_pillar(new Has_composition[] {quartz},new int[] {4}),
	nether_quartz_ore(new Has_composition[] {quartz,netherore},new int[] {2,1}),
	
	redstone(new Has_composition[] {Al2O3_molecule},new int[] {100}),
	redstone_block(new Has_composition[] {redstone},new int[] {9}),
	redstone_ore(new Has_composition[] {redstone,ore},new int[] {5,1}),
	redstone_torch(new Has_composition[] {redstone,stick},new int[] {1,1}),
	redstone_lamp(new Has_composition[] {redstone,glowstone},new int[] {4,1}),
	
	repeater(new Has_composition[] {redstone,redstone_torch,stone},new int[] {1,2,3}),
	comparator(new Has_composition[] {quartz,redstone_torch,stone},new int[] {1,3,3}),
	daylight_detector(new Has_composition[] {quartz,glass,planks_slab},new int[] {3,3,3}),
	note_block(new Has_composition[] {planks,redstone},new int[] {8,1}),
	jukebox(new Has_composition[] {planks,diamond},new int[] {8,1}),
	hopper(new Has_composition[] {iron_ingot,chest},new int[] {5,1}),
	piston(new Has_composition[] {iron_ingot,planks,cobblestone,redstone},new int[] {1,3,4,1}),
	sticky_piston(new Has_composition[] {piston,slime_ball},new int[] {1,1}),
	dropper(new Has_composition[] {cobblestone,redstone},new int[] {7,1}),
	dispenser(new Has_composition[] {dropper,bow},new int[] {1,1}),
	hopper_minecart(new Has_composition[] {hopper,minecart},new int[] {1,1}),
	powered_rail(new Has_composition[] {gold_ingot,stick,redstone},new double[] {1,1.0/6.0,1.0/6.0}),
	tripwire_hook(new Has_composition[] {iron_ingot,stick,planks},new double[] {0.5,0.5,0.5});
	
	public final HashMap<Has_composition,Double> map = new HashMap<Has_composition,Double>();
	Base_material(Has_composition[] element, double[] unit){
		for(int i=0;i<element.length;i++) {
			map.put(element[i], unit[i]);
		}
	}
	
	Base_material(Has_composition[] element, int[] unit){
		for(int i=0;i<element.length;i++) {
			map.put(element[i], (double) unit[i]);
		}
	}
	
	@Override
	public Elements_composition get_elements_composition() {
		ArrayList<Elements_composition> all_composition = new ArrayList<Elements_composition>();
		for(Entry<Has_composition,Double> entry:this.map.entrySet()) {
			Elements_composition sub_composition = entry.getKey().get_elements_composition();
			sub_composition.multiply(entry.getValue());
			all_composition.add(sub_composition);
		}
		Elements_composition result = new Elements_composition();
		for(Elements_composition compostion:all_composition) {
			result.add(compostion);
		}
		return result;
	}
	
	public static Base_material get(String name) {
		Base_material material = null;
		try {
			material = valueOf(name);
		} catch (Exception e) {
		}
		return material;
	}
}
