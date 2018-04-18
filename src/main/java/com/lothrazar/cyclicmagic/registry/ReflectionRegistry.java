/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.registry;

import java.lang.reflect.Field;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.passive.AbstractHorse;

public class ReflectionRegistry {

  //  public static Field fieldCareer;// = UtilReflection.getPrivateField("careerId", "field_175563_bv", merchant);
  public static IAttribute horseJumpStrength = null;

  public static void register() {
    for (Field f : AbstractHorse.class.getDeclaredFields()) {
      try {
        if (f.getName().equals("JUMP_STRENGTH") || f.getName().equals("field_110270_bw") || "interface net.minecraft.entity.ai.attributes.IAttribute".equals(f.getType() + "")) {
          f.setAccessible(true);
          // save pointer to the obj so we can reference it later
          horseJumpStrength = (IAttribute) f.get(null);
          break;
        }
      }
      catch (Exception e) {
        ModCyclic.logger.error("Severe error, please report this to the mod author [ JUMP_STRENGTH ]:");
        ModCyclic.logger.error(e.getStackTrace().toString());
      }
    }
    if (horseJumpStrength == null) {
      ModCyclic.logger.error(Const.MODID + ": JUMP_STRENGTH : Error - field not found using reflection");
    }
  }
}
