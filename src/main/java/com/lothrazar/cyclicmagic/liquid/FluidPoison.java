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
package com.lothrazar.cyclicmagic.liquid;

import com.lothrazar.cyclicmagic.util.data.Const;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

/**
 * I learned how to do this thanks to @elucent https://github.com/RootsTeam/Embers/blob/6e75e7c5c19e6dc6f9eb91a75f56c938b64a9898/src/main/java/teamroots/embers/fluid/FluidMoltenIron.java
 * 
 * @author Sam
 */
public class FluidPoison extends Fluid {

  public FluidPoison() {
    super("poison", new ResourceLocation(Const.MODID, "blocks/fluid_poison_base"), new ResourceLocation(Const.MODID, "blocks/fluid_poison_flowing"));
    setViscosity(1200);//water is 1000, lava is 6000
    setDensity(1200);//water is 1000, lava is 3000
    this.setLuminosity(6);
    setUnlocalizedName("poison");
  }
}
