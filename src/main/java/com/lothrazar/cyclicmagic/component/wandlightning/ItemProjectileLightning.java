package com.lothrazar.cyclicmagic.component.wandlightning;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ItemProjectileLightning extends BaseItemChargeScepter implements IHasRecipe {
  public ItemProjectileLightning() {
    super(20);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " qg",
        "leq",
        "el ",
        'e', "enderpearl",
        'l', "dyeLime",
        'q', "glowstone",
        'g', new ItemStack(Items.GHAST_TEAR));
  }
  @Override
  public EntityLightningballBolt createBullet(World world, EntityPlayer player, float dmg) {
    EntityLightningballBolt s = new EntityLightningballBolt(world, player);
    //  s.setDamage(dmg);
    return s;
  }
  @Override
  public SoundEvent getSound() {
    return SoundRegistry.laserbeanpew;
  }
}
