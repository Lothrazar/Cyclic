package com.lothrazar.cyclicmagic.item.minecart;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.EntityStoneMinecart;
import com.lothrazar.cyclicmagic.item.BaseItemMinecart;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemStoneMinecart extends BaseItemMinecart implements IHasRecipe {
  public ItemStoneMinecart() {
    super();
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        "   ",
        "gmg",
        "ggg",
        'g', Blocks.COBBLESTONE,
        'm', Items.MINECART);
  }
  @Override
  public EntityMinecart summonMinecart(World world) {
    return new EntityStoneMinecart(world);
  }
}
