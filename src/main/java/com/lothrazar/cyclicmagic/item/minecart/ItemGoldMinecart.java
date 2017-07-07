package com.lothrazar.cyclicmagic.item.minecart;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.EntityGoldMinecart;
import com.lothrazar.cyclicmagic.item.base.BaseItemMinecart;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ItemGoldMinecart extends BaseItemMinecart implements IHasRecipe {
  public ItemGoldMinecart() {
    super();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "   ",
        "gmg",
        "ggg",
        'g', "ingotGold",
        'm', Items.MINECART);
  }
  @Override
  public EntityMinecart summonMinecart(World world) {
    return new EntityGoldMinecart(world);
  }
  @Override
  public EntityMinecart summonMinecart(World world, double x, double y, double z) {
    return new EntityGoldMinecart(world, x, y, z);
  }
}
