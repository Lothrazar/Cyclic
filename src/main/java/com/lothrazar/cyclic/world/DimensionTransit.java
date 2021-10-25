package com.lothrazar.cyclic.world;

import java.util.function.Function;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
    return new PortalInfo(new Vec3(target.getX(), target.getY(), target.getZ()), Vec3.ZERO, entity.getYRot(), entity.getXRot());
  }

  @Override
  public Entity placeEntity(Entity newEntity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
    newEntity.fallDistance = 0;
    //    newEntity.setPosition(target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D);
    //    newEntity.moveToBlockPosAndAngles(target, yaw, newEntity.rotationPitch); 
    return repositionEntity.apply(false); //Must be false or we fall on vanilla. thanks /Mrbysco/TelePastries/
  }

  public void teleport(Player player) {
    if (!player.isCreative() && !player.level.isClientSide) {
      player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 200, false, false));
    }
    if (this.world != null) {
      //      ServerLevel dim = getTargetLevel();
      this.world.playSound(player, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, SoundEvents.PORTAL_TRAVEL, SoundSource.MASTER, 0.25F, this.world.random.nextFloat() * 0.4F + 0.8F);
      //      player.changeDimension(dim, this);
    }
  }

  public ServerLevel getTargetLevel() {
    return world.getServer().getLevel(UtilWorld.stringToDimension(target.getDimension()));
  }
}
