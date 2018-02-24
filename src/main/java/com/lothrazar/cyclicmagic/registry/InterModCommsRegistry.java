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
import com.lothrazar.cyclicmagic.component.crafter.ContainerCrafter;
import com.lothrazar.cyclicmagic.component.playerext.crafting.ContainerPlayerExtWorkbench;
import com.lothrazar.cyclicmagic.component.workbench.ContainerWorkBench;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class InterModCommsRegistry {
  /**
   * All IMC registrations go here
   */
  public static void register() {
    registerCraftingTweaks();
  }
  /**
   * 
   * https://minecraft.curseforge.com/projects/crafting-tweaks https://github.com/blay09/CraftingTweaks/blob/1.12/README.md
   */
  private static void registerCraftingTweaks() {
    //first the players inventory 
    NBTTagCompound tagCompound = new NBTTagCompound();
    tagCompound.setString("ContainerClass", ContainerPlayerExtWorkbench.class.getName());
    tagCompound.setInteger("GridSlotNumber", 6);
    //    tagCompound.setInteger("GridSize", 9);
    FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tagCompound);
    //then the grey workbench 
    tagCompound = new NBTTagCompound();
    tagCompound.setString("ContainerClass", ContainerWorkBench.class.getName());
    tagCompound.setInteger("GridSlotNumber", 1);
    FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tagCompound);
    //then the purple autocrafter
    tagCompound = new NBTTagCompound();
    tagCompound.setString("ContainerClass", ContainerCrafter.class.getName());
    tagCompound.setInteger("GridSlotNumber", 10);
    FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tagCompound);
  }
}
