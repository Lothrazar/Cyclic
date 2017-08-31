package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
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
import net.minecraftforge.common.config.Configuration;

public class ItemProjectileWool extends BaseItemProjectile implements IHasRecipe, IHasConfig {
  public ItemProjectileWool() {
    super();
    this.setMaxDamage(1000);
    this.setMaxStackSize(1);
  }
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityShearingBolt(world, x, y, z);
  }
  @Override
  public void syncConfig(Configuration config) {
    EntityShearingBolt.doesShearChild = config.getBoolean("Ender Shears Child", Const.ConfigCategory.items, true, "Ender shears work on child sheep");
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapelessRecipe(new ItemStack(this, 32),
        new ItemStack(Blocks.MOSSY_COBBLESTONE),
        new ItemStack(Blocks.WOOL),
        new ItemStack(Items.SHEARS));
  }
  @Override
  public   void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntityShearingBolt(world, player));

    UtilItemStack.damageItem(player,held);
  }
}
