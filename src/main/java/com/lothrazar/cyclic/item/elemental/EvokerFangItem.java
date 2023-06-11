package com.lothrazar.cyclic.item.elemental;

import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.ItemStackUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.item.context.UseOnContext;

public class EvokerFangItem extends ItemBaseCyclic {

  private static final int COOLDOWN = 10;

  public EvokerFangItem(Properties properties) {
    super(properties.durability(256 * 4));
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    if (player.getCooldowns().isOnCooldown(this)) {
      return super.useOn(context);
    }
    player.getCooldowns().addCooldown(context.getItemInHand().getItem(), COOLDOWN);
    player.swing(context.getHand());
    this.summonFangRay(player.blockPosition().getX(), player.blockPosition().getZ(), player, context.getClickLocation().x(), context.getClickLocation().y(), context.getClickLocation().z());
    ItemStackUtil.damageItem(player, context.getItemInHand());
    return super.useOn(context);
  }

  private static final int MAX_RANGE = 16;

  private void summonFangRay(double startX, double startZ, Player player, double posX, double posY, double posZ) {
    double minY = posY; //Math.min(posY, caster.posY);
    //double d1 = Math.max(posY,caster.posY) ;
    double tposX = player.getX();
    //    double tposY = player.getPosY();
    double tposZ = player.getZ();
    float arctan = (float) Mth.atan2(posZ - tposZ, posX - tposX);
    for (int i = 0; i < MAX_RANGE; ++i) {
      double fract = 1.25D * (i + 1);
      this.summonFangSingle(player,
          startX + Mth.cos(arctan) * fract,
          minY,
          startZ + Mth.sin(arctan) * fract,
          arctan, i);
    }
    //cooldown etc
    //    onCastSuccess(caster);
  }

  private void summonFangSingle(Player caster, double x, double y, double z, float yaw, int delay) {
    EvokerFangs entityevokerfangs = new EvokerFangs(caster.level(), x, y, z, yaw, delay, caster);
    caster.level().addFreshEntity(entityevokerfangs);
    // so. WE are using this hack because the entity has a MAGIC NUMBER of 6.0F hardcoded in a few places deep inside methods and if statements
    //this number is the damage that it deals.  ( It should be a property )
    //    UtilNBT.setEntityBoolean(entityevokerfangs, NBT_FANG_FROMPLAYER);
  }
}
