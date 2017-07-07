package com.lothrazar.cyclicmagic.item.minecart;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.EntityMinecartTurret;
import com.lothrazar.cyclicmagic.item.base.BaseItemMinecart;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ItemTurretMinecart extends BaseItemMinecart implements IHasRecipe {
  public ItemTurretMinecart() {
    super();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "bdb",
        "gmg",
        "ggg",
        'd', Blocks.BONE_BLOCK,
        'b', Items.BOW,
        'g', "ingotGold",
        'm', Blocks.OBSERVER);
  }
  @Override
  public EntityMinecart summonMinecart(World world) {
    return new EntityMinecartTurret(world);
  }
  @Override
  public EntityMinecart summonMinecart(World world, double x, double y, double z) {
    return new EntityMinecartTurret(world, x, y, z);
  }
}
