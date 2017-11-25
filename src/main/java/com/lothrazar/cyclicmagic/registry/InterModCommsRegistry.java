package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.component.playerext.crafting.ContainerPlayerExtWorkbench;
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
    NBTTagCompound tagCompound = new NBTTagCompound();
    tagCompound.setString("ContainerClass", ContainerPlayerExtWorkbench.class.getName());
    tagCompound.setInteger("GridSlotNumber", 6);
    tagCompound.setInteger("GridSize", 9);
    FMLInterModComms.sendMessage("craftingtweaks", "RegisterProvider", tagCompound);
  }
}
