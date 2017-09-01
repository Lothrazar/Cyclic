package com.lothrazar.cyclicmagic.component.wandlightning;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

@SuppressWarnings("unused")
public class ItemProjectileLightning extends BaseItemChargeScepter implements IHasRecipe {
  public ItemProjectileLightning() {
    super(20);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this),
        "enderpearl",
        "gemQuartz",
        new ItemStack(Items.GHAST_TEAR));
  }
  @Override
  public EntityLightningballBolt createBullet(World world, EntityPlayer player, float dmg) {
    EntityLightningballBolt s = new EntityLightningballBolt(world, player);
    //  s.setDamage(dmg);
    return s;
  }
}
