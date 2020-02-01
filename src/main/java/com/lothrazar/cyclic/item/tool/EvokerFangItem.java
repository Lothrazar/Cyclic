package com.lothrazar.cyclic.item.tool;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.MathHelper;

public class EvokerFangItem extends ItemBase {

  public EvokerFangItem(Properties properties) {
    super(properties.maxDamage(256 * 4));
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    this.summonFangRay(player.getPosition().getX(), player.getPosition().getZ(), player, context.getHitVec().getX(), context.getHitVec().getY(), context.getHitVec().getZ());
    UtilItemStack.damageItem(context.getItem());
    return super.onItemUse(context);
  }

  private static final int MAX_RANGE = 16;

  private void summonFangRay(double startX, double startZ, PlayerEntity player, double posX, double posY, double posZ) {
    double minY = posY;//Math.min(posY, caster.posY);
    //double d1 = Math.max(posY,caster.posY) ;
    double tposX = player.getPosX();
    double tposY = player.getPosY();
    double tposZ = player.getPosZ();
    float arctan = (float) MathHelper.atan2(posZ - tposZ, posX - tposX);
    for (int i = 0; i < MAX_RANGE; ++i) {
      double fract = 1.25D * (i + 1);
      this.summonFangSingle(player,
          startX + MathHelper.cos(arctan) * fract,
          minY,
          startZ + MathHelper.sin(arctan) * fract,
          arctan, i);
    }
    //cooldown etc
    //    onCastSuccess(caster);
  }

  private void summonFangSingle(PlayerEntity caster, double x, double y, double z, float yaw, int delay) {
    EvokerFangsEntity entityevokerfangs = new EvokerFangsEntity(caster.world, x, y, z, yaw, delay, caster);
    caster.world.addEntity(entityevokerfangs);
    // so. WE are using this hack because the entity has a MAGIC NUMBER of 6.0F hardcoded in a few places deep inside methods and if statements
    //this number is the damage that it deals.  ( It should be a property )
    //    UtilNBT.setEntityBoolean(entityevokerfangs, NBT_FANG_FROMPLAYER);
  }
}
