package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileTNT extends BaseItemProjectile implements IHasRecipe {
  private int strength;
  public ItemProjectileTNT(int str) {
    super();
    this.strength = str;
  }
  @Override
  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntityDynamite(world, player, this.strength));
  }
  @Override
  public void addRecipe() {
    switch (this.strength) {
    case 1:
      GameRegistry.addShapelessRecipe(new ItemStack(this, 9), new ItemStack(Blocks.TNT), new ItemStack(Items.PAPER), new ItemStack(Items.CLAY_BALL), new ItemStack(Items.ENDER_PEARL));
      break;
    case 2:
      GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(ItemRegistry.ender_tnt_1), new ItemStack(ItemRegistry.ender_tnt_1), new ItemStack(Items.CLAY_BALL));
      break;
    case 3:
      GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(ItemRegistry.ender_tnt_2), new ItemStack(ItemRegistry.ender_tnt_2), new ItemStack(Items.CLAY_BALL));
      break;
    case 4:
      GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(ItemRegistry.ender_tnt_3), new ItemStack(ItemRegistry.ender_tnt_3), new ItemStack(Items.CLAY_BALL));
      break;
    case 5:
      GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(ItemRegistry.ender_tnt_4), new ItemStack(ItemRegistry.ender_tnt_4), new ItemStack(Items.CLAY_BALL));
      break;
    case 6:
      GameRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(ItemRegistry.ender_tnt_5), new ItemStack(ItemRegistry.ender_tnt_5), new ItemStack(Items.CLAY_BALL));
      break;
    }
  }
}
