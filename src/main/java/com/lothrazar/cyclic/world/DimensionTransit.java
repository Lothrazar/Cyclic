package com.lothrazar.cyclic.world;

import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.function.Function;
import net.minecraft.block.PortalInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

public class DimensionTransit implements ITeleporter {

  protected ServerWorld world;
  private BlockPosDim target;

  public DimensionTransit(ServerWorld world, BlockPosDim target) {
    this.world = world;
    this.target = target;
  }

  @Override
  public PortalInfo getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, PortalInfo> defaultPortalInfo) {
    return new PortalInfo(new Vector3d(target.getX(), target.getY(), target.getZ()), Vector3d.ZERO, entity.rotationYaw, entity.rotationPitch);
  }

  @Override
  public Entity placeEntity(Entity newEntity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
    if (newEntity instanceof LivingEntity) {
      ((LivingEntity) newEntity).addPotionEffect(new EffectInstance(Effects.RESISTANCE, 200, 200, false, false));
    }
    newEntity.fallDistance = 0;
    return repositionEntity.apply(false); //Must be false or we fall on vanilla. thanks /Mrbysco/TelePastries/
  }

  public void teleport(PlayerEntity player) {
    if (this.world != null && this.world.getServer() != null) {
      this.world.playSound(null, target.getX() + 0.5D, target.getY() + 0.5D, target.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.MASTER, 0.25F, this.world.rand.nextFloat() * 0.4F + 0.8F);
    }
  }

  public ServerWorld getTargetWorld() {
    return world.getServer().getWorld(UtilWorld.stringToDimension(target.getDimension()));
  }
}
