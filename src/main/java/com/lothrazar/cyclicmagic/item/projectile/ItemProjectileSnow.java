package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemProjectileSnow extends BaseItemProjectile implements IHasRecipe {
  public ItemProjectileSnow() {
    super();
    this.setMaxDamage(1000);
    this.setMaxStackSize(1);
  }
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntitySnowballBolt(world, x, y, z);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this, 32),
        "cobblestone",
        new ItemStack(Blocks.ICE),
        new ItemStack(Items.SNOWBALL));
  }
  @Override
  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    EntitySnowballBolt proj = new EntitySnowballBolt(world, player);
    this.doThrow(world, player, hand, proj);
    EntitySnowballBolt projUp = new EntitySnowballBolt(world, player);
    projUp.posY += 2;
    this.doThrow(world, player, hand, projUp);
    EntitySnowballBolt projDown = new EntitySnowballBolt(world, player);
    projDown.posY -= 2;
    this.doThrow(world, player, hand, projDown);
    UtilItemStack.damageItem(player, held);
  }
}
