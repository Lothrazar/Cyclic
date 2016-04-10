package com.lothrazar.cyclicmagic.registry;

import java.lang.reflect.Field;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.EntityHorse;

public class ReflectionRegistry {

	public static IAttribute horseJumpStrength = null;

	public static void register() {

		// version 1.1.0
		// new item for speed
		// new item for jump

		for (Field f : EntityHorse.class.getDeclaredFields()) {
			try {
				// if(f.get(null) instanceof IAttribute)
				// System.out.println("== "+f.getName()+" ** "+f.getType());

				// interface net.minecraft.entity.ai.attributes.IAttribute

				// field_76425_a
				if (f.getName().equals("horseJumpStrength") || f.getName().equals("field_110270_bw") || "interface net.minecraft.entity.ai.attributes.IAttribute".equals(f.getType() + "")) {
					f.setAccessible(true);
					// save pointer to the obj so we can reference it later
					horseJumpStrength = (IAttribute) f.get(null);
					// System.err.println("FOUND FOUND FOUND");
					break;
				}
			} catch (Exception e) {
				System.err.println("Severe error, please report this to the mod author:");
				System.err.println(e);
			}
		}

		if (horseJumpStrength == null) {
			System.err.println(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
			System.err.println(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
			System.err.println(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
			System.err.println(Const.MODID + ":horseJumpStrength: Error - field not found using reflection");
		}
	}
}
