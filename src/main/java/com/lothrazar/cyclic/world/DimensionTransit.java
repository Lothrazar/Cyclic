package com.lothrazar.cyclic.world;

import java.util.function.Function;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.util.LevelWorldUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

public class DimensionTransit implements ITeleporter {

  protected ServerLevel world;
  private BlockPosDim target;

  public DimensionTransit(ServerLevel world, BlockPosDim target) {
    this.world = world;
    this.target = target;
  }

  @Override
  public PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
    BlockPos myPos = moveToSafeCoords(destWorld, target.getPos());
    return new PortalInfo(new Vec3(myPos.getX() + 0.5F, myPos.getY() + 0.5F, myPos.getZ() + 0.5F), Vec3.ZERO, entity.getYRot(), entity.getXRot());
  }

  private BlockPos moveToSafeCoords(ServerLevel world, BlockPos pos) {
    int tries = 10;
    while (tries > 0) {
      tries--;
      if (world.getBlockState(pos).isSolid()) {
        pos = pos.above();
      }
    }
    return pos;
  }

  @Override
  public Entity placeEntity(Entity newEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
    if (newEntity instanceof LivingEntity) {
      ((LivingEntity) newEntity).addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 200, false, false));
      ((LivingEntity) newEntity).addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 20, 20, false, false));
    }
    newEntity.fallDistance = 0;
    return repositionEntity.apply(false); //Must be false or we fall on vanilla. thanks /Mrbysco/TelePastries/
  }

  public void teleport(Player player) {
    if (!player.isCreative() && !player.level().isClientSide) {
      player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 200, false, false));
    }
    if (this.world != null) {
      //      ServerLevel dim = getTargetLevel();
      this.world.playSound(player, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, SoundEvents.PORTAL_TRAVEL, SoundSource.MASTER, 0.25F, this.world.random.nextFloat() * 0.4F + 0.8F);
      //      player.changeDimension(dim, this);
    }
  }

  public ServerLevel getTargetLevel() {
    return world == null ? null : world.getServer().getLevel(LevelWorldUtil.stringToDimension(target.getDimension()));
  }
}
