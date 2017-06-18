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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ItemFangs extends BaseTool {
  private static final int DURABILITY = 100;
  private static final int MAX_RANGE = 16;
  public ItemFangs() {
    super(DURABILITY);
  }
  @Override
  public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
    summonFangRay(player, entity.posX, entity.posY, entity.posZ);
    UtilItemStack.damageItem(player, player.getHeldItem(hand));
    return true;
  }
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    summonFangRay(player, pos.getX() + hitX, pos.getY() + hitY + 1, pos.getZ() + hitZ);
    UtilItemStack.damageItem(player, player.getHeldItem(hand));
    return EnumActionResult.SUCCESS;
  }
  private void summonFangRay(EntityPlayer caster, double posX, double posY, double posZ) {
    double minY = Math.min(posY, caster.posY);
    //double d1 = Math.max(posY,caster.posY) ;
    float arctan = (float) MathHelper.atan2(posZ - caster.posZ, posX - caster.posX);
    for (int i = 0; i < MAX_RANGE; ++i) {
      double fract = 1.25D * (double) (i + 1);
      this.summonFangSingle(caster,
          caster.posX + (double) MathHelper.cos(arctan) * fract,
          minY,
          caster.posZ + (double) MathHelper.sin(arctan) * fract,
          arctan, i);
    }
  }
  private void summonFangSingle(EntityPlayer caster, double x, double y, double z, float yaw, int delay) {
    //i was Y plus this + yHeight
    EntityEvokerFangs entityevokerfangs = new EntityEvokerFangs(caster.world, x, y, z, yaw, delay, caster);
    caster.world.spawnEntity(entityevokerfangs);
  }
}
