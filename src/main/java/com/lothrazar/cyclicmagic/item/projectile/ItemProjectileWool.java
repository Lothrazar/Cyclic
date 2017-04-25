package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemProjectileWool extends BaseItemProjectile implements IHasRecipe, IHasConfig {
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityShearingBolt(world, x, y, z);
  }
  @Override
  public void syncConfig(Configuration config) {
    EntityShearingBolt.doesShearChild = config.getBoolean("Ender Shears Child", Const.ConfigCategory.items, true, "Ender shears work on child sheep");
    //		EntityShearingBolt.doesShearChild = config.getBoolean("wool.child", category, true, "Does shear child sheep as well.");
  }
  @Override
  public IRecipe addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this, 32), new ItemStack(Blocks.MOSSY_COBBLESTONE), new ItemStack(Blocks.WOOL), new ItemStack(Items.SHEARS));
    return null;
  }
  @Override
  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntityShearingBolt(world, player));
  }
}
