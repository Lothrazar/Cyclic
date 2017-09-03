package com.lothrazar.cyclicmagic.component.wandice;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.item.base.BaseItemRapidScepter;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class ItemProjectileSnow extends BaseItemRapidScepter implements IHasRecipe {
  public ItemProjectileSnow() {
    super(1000);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        " sc",
        " rs",
        "i  ",
        'c', Blocks.ICE,
        's', Blocks.SNOW,
        'r', "dustRedstone",
        'i', "ingotIron");
  }
  @Override
  public EntitySnowballBolt createBullet(World world, EntityPlayer player, float dmg) {
    EntitySnowballBolt s = new EntitySnowballBolt(world, player);
    s.setDamage(dmg);
    return s;
  }
  @Override
  public SoundEvent getSound() {
    return SoundRegistry.goodlaunch;
  }
}
