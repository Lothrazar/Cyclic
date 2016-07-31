package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.module.EmeraldArmorModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemEmeraldSword extends ItemSword implements IHasRecipe {
  public static final String name = "emerald_sword";
  public ItemEmeraldSword() {
    super(EmeraldArmorModule.TOOL_MATERIAL_EMERALD);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this), " e ", " e ", " s ", 'e', new ItemStack(EmeraldArmorModule.REPAIR_EMERALD), 's', new ItemStack(Items.STICK));
  }
}
