package com.lothrazar.cyclic.block.fishing;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.util.List;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileFisher extends TileEntityBase implements MenuProvider {

  private static final int RADIUS = 12;
  private static final double CHANCE = 0.1;
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.is(DataTags.FISHING_RODS);
    }
  };
  LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  static enum Fields {
    REDSTONE;
  }

  public TileFisher(BlockPos pos, BlockState state) {
    super(TileRegistry.fisher, pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileFisher e) {
    e.tick();
  }

  public static <E extends BlockEntity> void clientTick(Level level, BlockPos blockPos, BlockState blockState, TileFisher e) {
    e.tick();
  }

  @Override
  public Component getDisplayName() {
    return new TextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerFisher(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void load(CompoundTag tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.load(tag);
  }

  @Override
  public CompoundTag save(CompoundTag tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.save(tag);
  }

  //  @Override
  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    ItemStack stack = inventory.getStackInSlot(0);
    if (stack.is(DataTags.FISHING_RODS)) {
      int x = worldPosition.getX() + level.random.nextInt(RADIUS * 2) - RADIUS;
      int y = worldPosition.getY();
      int z = worldPosition.getZ() + level.random.nextInt(RADIUS * 2) - RADIUS;
      BlockPos center = new BlockPos(x, y, z);
      if (this.isWater(center)) {
        try {
          this.doFishing(stack, center);
        } //loot tables are explosive
        catch (Exception e) {
          ModCyclic.LOGGER.error("Fishing Block: Loot table failed", e);
        }
      }
    }
  }

  private boolean isWater(BlockPos center) {
    return this.level.getBlockState(center).getBlock() == Blocks.WATER;
  }

  private void doFishing(ItemStack fishingRod, BlockPos center) {
    Level world = this.getLevel();
    Random rand = world.random;
    if (rand.nextDouble() < CHANCE && world instanceof ServerLevel) {
      LootTables manager = world.getServer().getLootTables();
      if (manager == null) {
        return;
      }
      LootTable table = manager.get(BuiltInLootTables.FISHING);
      if (table == null) {
        return;
      }
      //got it
      int luck = EnchantmentHelper.getItemEnchantmentLevel(
          Enchantments.FISHING_LUCK, fishingRod) + 1;
      Vec3 fffffffffff = new Vec3(center.getX(), center.getY(), center.getZ());
      LootContext lootContext = new LootContext.Builder((ServerLevel) world)
          .withLuck(luck).withRandom(rand).withParameter(LootContextParams.ORIGIN, fffffffffff)
          .withParameter(LootContextParams.TOOL, fishingRod)
          .create(LootContextParamSets.FISHING);
      List<ItemStack> lootDrops = table.getRandomItems(lootContext);
      if (lootDrops != null && lootDrops.size() > 0) {
        UtilItemStack.damageItem(fishingRod);
        UtilItemStack.drop(world, center, lootDrops);
      }
    }
  }

  @Override
  public void setField(int field, int value) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        this.needsRedstone = value % 2;
        break;
    }
  }

  @Override
  public int getField(int field) {
    switch (Fields.values()[field]) {
      case REDSTONE:
        return this.needsRedstone;
    }
    return 0;
  }
}
