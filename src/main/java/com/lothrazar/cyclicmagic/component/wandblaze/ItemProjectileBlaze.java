package com.lothrazar.cyclicmagic.component.wandblaze;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ItemProjectileBlaze extends BaseItemChargeScepter implements IHasRecipe {
  public ItemProjectileBlaze() {
    super(1000);
  }
  @Override
  public EntityBlazeBolt createBullet(World world, EntityPlayer player, float dmg) {
    EntityBlazeBolt s = new EntityBlazeBolt(world, player);
//    s.setDamage(dmg);
    return s;
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this, 16), new ItemStack(Items.FIRE_CHARGE), new ItemStack(Items.BLAZE_POWDER), new ItemStack(Items.FLINT));
  }
  @Override
  public SoundEvent getSound() {
    return SoundRegistry.firelaunch;
  }
}
