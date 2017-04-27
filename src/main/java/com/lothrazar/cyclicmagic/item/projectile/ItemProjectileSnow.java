package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileSnow extends BaseItemProjectile implements IHasRecipe {
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntitySnowballBolt(world, x, y, z);
  }
  @Override
  public IRecipe addRecipe() {
    return  RecipeRegistry.addShapelessRecipe(new ItemStack(this, 32),
        new ItemStack(Blocks.MOSSY_COBBLESTONE), new ItemStack(Blocks.ICE), new ItemStack(Items.SNOWBALL));
 
  }
  @Override
  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntitySnowballBolt(world, player));
  }
}
