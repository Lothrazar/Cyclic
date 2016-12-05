package com.lothrazar.cyclicmagic.item.projectile;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProjectileFishing extends BaseItemProjectile implements IHasRecipe {
  @Override
  public EntityThrowableDispensable getThrownEntity(World world, double x, double y, double z) {
    return new EntityFishingBolt(world, x, y, z);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapelessRecipe(new ItemStack(this, 32), new ItemStack(Items.ENDER_PEARL), new ItemStack(Items.GUNPOWDER), new ItemStack(Items.STRING));
  }
  @Override
  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntityFishingBolt(world, player));
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("item.ender_fishing.tooltip"));
  }
}
