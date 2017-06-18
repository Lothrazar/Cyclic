package com.lothrazar.cyclicmagic.item;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityEvokerFangs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemFangs extends BaseTool {
  public ItemFangs() {
    super(100);
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    summonFang(player, entity.posX, entity.posY, entity.posZ);
    UtilItemStack.damageItem(player, player.getHeldItem(hand));
    return true;
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    summonFang(player, pos.getX() + hitX, pos.getY() + hitY + 1, pos.getZ() + hitZ);
    UtilItemStack.damageItem(player, player.getHeldItem(hand));
    return EnumActionResult.SUCCESS;
  }
  private void summonFang(EntityPlayer caster, double posX, double posY, double posZ) {
    EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(caster.world, posX, posY, posZ, caster.rotationYaw, 0, caster);
    caster.world.spawnEntity(entityevokerfangs);
  }
}
