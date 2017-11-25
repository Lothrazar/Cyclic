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
   * https://minecraft.curseforge.com/projects/crafting-tweaks
   * https://github.com/blay09/CraftingTweaks/blob/1.12/README.md
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
