package com.lothrazar.cyclic.block.conveyor;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.CapabilityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

public class TileConveyor extends TileBlockEntityCyclic {

  private static int extractRate() { return ConfigRegistry.CONVEYOR_EXTRACT_RATE == null ? 20 : ConfigRegistry.CONVEYOR_EXTRACT_RATE.get(); }
  private int extractCooldown = 0;

  public TileConveyor(BlockPos pos, BlockState state) {
    super(TileRegistry.CONVEYOR.get(), pos, state);
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileConveyor e) {
    e.tick();
    // Convert any regular ItemEntities on the belt to ConveyorItemEntities
    List<ItemEntity> regularItems = level.getEntitiesOfClass(ItemEntity.class, new AABB(blockPos).expandTowards(0.0F, 0.5F, 0.0F));
    for (ItemEntity item : regularItems) {
      if (!(item instanceof ConveyorItemEntity)) {
        ConveyorItemEntity wrapped = new ConveyorItemEntity(level, item.getX(), item.getY(), item.getZ(), item.getItem().copy());
        level.addFreshEntity(wrapped);
        item.discard();
      }
    }
    // Try to insert ConveyorItemEntities at the exit edge into the adjacent block
    List<ConveyorItemEntity> beltItems = level.getEntitiesOfClass(ConveyorItemEntity.class, new AABB(blockPos).expandTowards(0.0F, 0.5F, 0.0F));
    for (ConveyorItemEntity item : beltItems) {
      tryInsertIntoOutput(item, blockState, blockPos, level);
    }
    // Periodically extract one item from the block at the belt's input end
    if (e.extractCooldown > 0) {
      e.extractCooldown--;
    } else {
      e.extractCooldown = extractRate();
      tryExtractFromInput(blockState, blockPos, level);
    }
  }

  private static void tryInsertIntoOutput(ConveyorItemEntity item, BlockState bs, BlockPos pos, Level world) {
    Direction facing = bs.getValue(BlockStateProperties.HORIZONTAL_FACING);
    double nX = item.getX() - pos.getX();
    double nZ = item.getZ() - pos.getZ();
    boolean nearExit = switch (facing) {
      case NORTH -> nZ < 0.2;
      case SOUTH -> nZ > 0.8;
      case WEST  -> nX < 0.2;
      case EAST  -> nX > 0.8;
      default -> false;
    };
    if (!nearExit) {
      return;
    }
    BlockPos nextPos = pos.relative(facing);
    if (world.getBlockState(nextPos).getBlock() instanceof BlockConveyor) {
      return;
    }
    IItemHandler handler = CapabilityUtil.item(world, nextPos, facing.getOpposite());
    if (handler == null) {
      return;
    }
    ItemStack remaining = ItemHandlerHelper.insertItem(handler, item.getItem().copy(), false);
    if (remaining.getCount() < item.getItem().getCount()) {
      item.setItem(remaining);
      if (remaining.isEmpty()) {
        item.discard();
      }
    }
  }

  private static void tryExtractFromInput(BlockState bs, BlockPos pos, Level world) {
    if (world.hasNeighborSignal(pos)) {
      return;
    }
    Direction facing = bs.getValue(BlockStateProperties.HORIZONTAL_FACING);
    BlockPos inputPos = pos.relative(facing.getOpposite());
    if (world.getBlockState(inputPos).getBlock() instanceof BlockConveyor) {
      return;
    }
    IItemHandler handler = CapabilityUtil.item(world, inputPos, facing);
    if (handler == null) {
      return;
    }
    for (int slot = 0; slot < handler.getSlots(); slot++) {
      ItemStack simulated = handler.extractItem(slot, 1, true);
      if (!simulated.isEmpty()) {
        handler.extractItem(slot, 1, false);
        double spawnX = pos.getX() + 0.5 - facing.getStepX() * 0.3;
        double spawnY = pos.getY() + 0.25;
        double spawnZ = pos.getZ() + 0.5 - facing.getStepZ() * 0.3;
        world.addFreshEntity(new ConveyorItemEntity(world, spawnX, spawnY, spawnZ, simulated));
        break;
      }
    }
  }

  private static void syncEntityPos(Entity entity, double x, double y, double z) {
    if (entity instanceof ServerPlayer sp) {
      sp.connection.teleport(x, y, z, sp.getYRot(), sp.getXRot());
    } else {
      entity.setPos(x, y, z);
    }
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileConveyor e) {
    e.tick();
  }

  public void tick() {
    if (level == null || worldPosition == null) {
      return;
    }
    List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(worldPosition).expandTowards(0.0F, 0.5F, 0.0F));
    for (Entity e : entities) {
      makeEntitiesTravel(e, this.getBlockState(), this.worldPosition, level);
    }
  }

  public static void makeEntitiesTravel(Entity entity, BlockState bs, BlockPos pos, Level world) {
    if (entity instanceof Player) {
      Player p = (Player) entity;
      if (p.isCrouching()) {
        return;
      }
    }
    double normalizedX = entity.getX() - pos.getX();
    double normalizedZ = entity.getZ() - pos.getZ();
    final double offside = 0.01D;
    //if the normalized values are >1 or <0, they entity is right at the border so dont apply it now
    Direction facing = bs.getValue(BlockStateProperties.HORIZONTAL_FACING);
    if (facing.getAxis() == Axis.Z && (normalizedX > 1 - offside || normalizedX < 0 + offside)) {
      return;
    }
    if (facing.getAxis() == Axis.X && (normalizedZ > 1 - offside || normalizedZ < 0 + offside)) {
      return;
    }
    ConveyorType type = bs.getValue(BlockConveyor.TYPE);
    double heightLimit = (type.isVertical()) ? pos.getY() + 1.3D : pos.getY() + 0.125D;
    double speed = bs.getValue(BlockConveyor.SPEED).getSpeed(); //0.08D; //temp variable, replace with speed from blockstate later
    double xSpeed = 0.0D, zSpeed = 0.0D, ySpeed = 0.0D;
    boolean isItem = entity instanceof ItemEntity;
    if (isItem) {
      // Let items fall naturally until they are near the belt surface before applying horizontal push.
      // The extraction spawn is pos.getY()+0.25, so 0.4 gives a small buffer above that.
      if (entity.getY() > pos.getY() + 0.4) {
        return;
      }
    } else if (entity.getY() > heightLimit) {
      return;
    }
    xSpeed = facing.getStepX() * speed;
    ySpeed = 0.0D;
    zSpeed = facing.getStepZ() * speed;
    if (type.isCorner()) {
      //      Direction rotated = type == ConveyorType.CORNER_RIGHT ? facing.rotateYCCW() : facing.rotateY();
      //
      //      if (Math.random() < 0.1)
      //      if ((facing == Direction.NORTH && normalizedZ < 0.5D) || (facing == Direction.SOUTH && normalizedZ > 0.5D)) {
      //        ModCyclic.LOGGER.info("first half");
      //        xSpeed = rotated.getXOffset() * speed;
      //                zSpeed = 0.0D;
      //      }
      //      if ((facing == Direction.WEST && normalizedX < 0.5D) || (facing == Direction.EAST && normalizedX > 0.5D)) {
      //        xSpeed = 0.0D;
      //        zSpeed = rotated.getZOffset() * speed;
      //      }
      //FIX their centering when going around corners
      if (facing.getAxis() == Axis.Z && (normalizedX < 0.4 || normalizedX > 0.6)) {
        syncEntityPos(entity, Math.floor(entity.getX()) + 0.5, entity.getY(), entity.getZ());
      }
      if (facing.getAxis() == Axis.X && (normalizedZ < 0.4 || normalizedZ > 0.6)) {
        //centralize Z
        syncEntityPos(entity, entity.getX(), entity.getY(), Math.floor(entity.getZ()) + 0.5);
      }
    }
    if (type.isVertical()) {
      double hackEdge = 0.1;
      if (normalizedX < hackEdge || normalizedZ < hackEdge
          || normalizedX > 1 - hackEdge || normalizedZ > 1 - hackEdge) {
        // ?? : investigate jump hacks here
        entity.setPos(entity.getX(), entity.getY() + .2, entity.getZ());
      }
      ySpeed = speed * 1.3;
      if (type == ConveyorType.DOWN) {
        ySpeed *= -1;
      }
    }
    if (isItem && !type.isVertical()) {
      // For items on a horizontal belt, only override X/Z so gravity remains in control of Y.
      // Zeroing Y velocity causes oscillation as it fights the entity's gravity each tick.
      if (xSpeed != 0.0D || zSpeed != 0.0D) {
        Vec3 cur = entity.getDeltaMovement();
        entity.setDeltaMovement(xSpeed, cur.y, zSpeed);
      }
    } else if (xSpeed != 0.0D || ySpeed != 0.0D || zSpeed != 0.0D) {
      entity.setDeltaMovement(xSpeed, ySpeed, zSpeed);
    }
  }
}
