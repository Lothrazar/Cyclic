package com.lothrazar.cyclicmagic.item.projectile;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemProjectileTNT extends BaseItemProjectile {
  private int strength;
  public ItemProjectileTNT(int str) {
    super();
    this.strength = str;
  }
  @Override
  void onItemThrow(ItemStack held, World world, EntityPlayer player, EnumHand hand) {
    this.doThrow(world, player, hand, new EntityDynamite(world, player, this.strength));
  }
}
