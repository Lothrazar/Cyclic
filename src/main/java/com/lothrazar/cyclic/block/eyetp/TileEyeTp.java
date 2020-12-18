package com.lothrazar.cyclic.block.eyetp;

import java.util.List;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;

public class TileEyeTp extends TileEntityBase implements ITickableTileEntity {

  int maxRange = 32;//TODO config

  public TileEyeTp() {
    super(TileRegistry.eye_teleport);
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (world.isRemote || world.getGameTime() % 10 != 0) {
      return;// the %10 means only check every half second
    }
    List<PlayerEntity> players = world.getEntitiesWithinAABB(PlayerEntity.class, new AxisAlignedBB(this.pos.getX() - maxRange, this.pos.getY() - maxRange, this.pos.getZ() - maxRange, this.pos.getX() + maxRange, this.pos.getY() + maxRange, this.pos.getZ() + maxRange));
    boolean lookingPlayer = false;
    for (PlayerEntity player : players) {
      //am i looking
      Vector3d positionEyes = player.getEyePosition(1F);
      Vector3d look = player.getLook(1F);
      //take the player eye position. draw a vector from the eyes, in the direction they are looking
      //of LENGTH equal to the range
      //
      Vector3d visionWithLength = positionEyes.add(look.x * maxRange, look.y * maxRange, look.z * maxRange);
      //
      //ray trayce from eyes, along the vision vec
      BlockRayTraceResult rayTrace = this.world.rayTraceBlocks(new RayTraceContext(positionEyes, visionWithLength, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
      //
      if (this.pos.equals(rayTrace.getPos())
          && player.getHeldItem(Hand.MAIN_HAND).getItem() == Items.ENDER_PEARL) {
        //at least one is enough
        lookingPlayer = true;
        //TODO: five second countdown
        UtilEntity.enderTeleportEvent(player, world, this.pos.up());
        break;
      }
    }
    this.setLitProperty(lookingPlayer);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
