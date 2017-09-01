package com.lothrazar.cyclicmagic.component.wandice;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.base.BaseItemChargeScepter;
import com.lothrazar.cyclicmagic.item.base.BaseItemRapidScepter;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class ItemProjectileSnow extends BaseItemRapidScepter implements IHasRecipe {
  public ItemProjectileSnow() {
    super(1000);
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this),
        "cobblestone",
        new ItemStack(Blocks.ICE),
        new ItemStack(Items.SNOWBALL));
  }
  @Override
  public EntitySnowballBolt createBullet(World world, EntityPlayer player, float dmg) {
    EntitySnowballBolt s = new EntitySnowballBolt(world, player);
    s.setDamage(dmg);
    return s;
  }
}
