package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.entity.projectile.EntityWaterBolt;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemProjectileWater extends BaseItemProjectile implements IHasRecipe {
  public ItemProjectileWater() {
    super();
    this.setMaxDamage(1000);
    this.setMaxStackSize(1);
  }
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityWaterBolt(world, x, y, z);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this, 8),
        new ItemStack(Items.WATER_BUCKET),
        Blocks.ICE,
        "paper",
        "string");
  }
  @Override
  public   void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntityWaterBolt(world, player));
    UtilItemStack.damageItem(player, player.getHeldItem(hand));
  }
}
