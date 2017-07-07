package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemProjectileFishing extends BaseItemProjectile implements IHasRecipe {
  @Override
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityFishingBolt(world, x, y, z);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this, 32), "enderpearl", "gunpowder", "string");
  }
  @Override
  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntityFishingBolt(world, player));
  }
}
