package com.lothrazar.cyclic.block.magnet;

import java.util.List;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.item.datacard.filter.FilterCardItem;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.data.Vector3;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import static com.lothrazar.cyclic.block.BlockCyclic.LIT;

public class TileInsertingMagnet extends TileBlockEntityCyclic implements MenuProvider {

  private static final double ENTITY_PULL_DIST = 0.2; //closer than this and nothing happens
  private static final double ENTITY_PULL_SPEED_CUTOFF = 2; //closer than this and it slows
  private static final float ITEMSPEEDFAR = 0.8F;
  private static final float ITEMSPEEDCLOSE = 0.09F;

  ItemStackHandler filter = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem() == ItemRegistry.FILTER_DATA.get();
    }

    @Override
    public int getSlotLimit(int slot) {
      return 1;
    }
  };

  public TileInsertingMagnet(BlockPos pos, BlockState state) {
    super(TileRegistry.MAGNET.get(), pos, state);
    this.needsRedstone = 0;
  }

  @Override
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.loadAdditional(tag, registries);
    if (tag.contains("filter")) {
      filter.deserializeNBT(registries, tag.getCompound("filter"));
    }
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
    super.saveAdditional(tag, registries);
    tag.put("filter", filter.serializeNBT(registries));
  }

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return filter;
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.MAGNET_BLOCK.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerMagnet(i, level, worldPosition, playerInventory, playerEntity);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileInsertingMagnet e) {
    if (blockState.getValue(LIT)) {
      final int radius = BlockMagnetPanel.RADIUS.get();
      int vradius = 0;
      int x = blockPos.getX();
      int y = blockPos.getY();
      int z = blockPos.getZ();
      AABB axisalignedbb = (new AABB(x, y, z, x + 1, y + 1, z + 1)).inflate(radius, vradius, radius);
      List<ItemEntity> list = level.getEntitiesOfClass(ItemEntity.class, axisalignedbb);
      ItemStack filterCard = e.filter.getStackInSlot(0);
      pullEntityList(x + 0.5, y + 0.1, z + 0.5, true, list, filterCard);
    }
  }

  public static int pullEntityList(double x, double y, double z, boolean towardsPos, List<ItemEntity> all, ItemStack filterCard) {
    int moved = 0;
    double hdist, xDist, zDist;
    float speed;
    int direction = (towardsPos) ? 1 : -1;
    //negative to flip the vector and push it away
    for (ItemEntity entity : all) {
      if (entity == null) {
        continue;
      }
      if (!FilterCardItem.filterAllowsExtract(filterCard, entity.getItem())) {
        continue;
      }
      //being paranoid
      BlockPos p = entity.blockPosition();
      xDist = Math.abs(x - p.getX());
      zDist = Math.abs(z - p.getZ());
      hdist = Math.sqrt(xDist * xDist + zDist * zDist);
      if (hdist > ENTITY_PULL_DIST) {
        speed = (hdist > ENTITY_PULL_SPEED_CUTOFF) ? ITEMSPEEDFAR : ITEMSPEEDCLOSE;
        setEntityMotionFromVector(entity, x, y, z, direction * speed);
        moved++;
      }
      //else its basically on it, no point
    }
    return moved;
  }

  public static void setEntityMotionFromVector(Entity entity, double x, double y, double z, float modifier) {
    Vector3 originalPosVector = new Vector3(x, y, z);
    Vector3 entityVector = new Vector3(entity);
    Vector3 finalVector = originalPosVector.copy().subtract(entityVector);
    if (finalVector.mag() > 1) {
      finalVector.normalize();
    }
    double motionX = finalVector.x * modifier;
    double motionY = finalVector.y * modifier;
    double motionZ = finalVector.z * modifier;
    entity.setDeltaMovement(motionX, motionY, motionZ);
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileInsertingMagnet e) {
    //
  }

  @Override
  public void setField(int field, int value) {}

  @Override
  public int getField(int field) {
    return 0;
  }
}
