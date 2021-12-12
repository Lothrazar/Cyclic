package com.lothrazar.cyclic.block.fishing;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileFisher extends TileEntityBase implements ITickableTileEntity, INamedContainerProvider {

  private static final int RADIUS = 12;
  private static final double CHANCE = 0.1;
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.getItem().isIn(DataTags.FISHING_RODS);
    }
  };
  LazyOptional<IItemHandler> inventoryCap = LazyOptional.of(() -> inventory);

  static enum Fields {
    REDSTONE;
  }

  public TileFisher() {
    super(TileRegistry.fisher);
    this.needsRedstone = 0;
  }

  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
    return new ContainerFisher(i, world, pos, playerInventory, playerEntity);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventoryCap.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void invalidateCaps() {
    inventoryCap.invalidate();
    super.invalidateCaps();
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    inventory.deserializeNBT(tag.getCompound(NBTINV));
    super.read(bs, tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    tag.put(NBTINV, inventory.serializeNBT());
    return super.write(tag);
  }

  @Override
  public void tick() {
    if (world == null || world.isRemote) {
      return;
    }
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    ItemStack stack = inventory.getStackInSlot(0);
    if (stack.getItem().isIn(DataTags.FISHING_RODS)) {
      int x = pos.getX() + world.rand.nextInt(RADIUS * 2) - RADIUS;
      int y = pos.getY();
      int z = pos.getZ() + world.rand.nextInt(RADIUS * 2) - RADIUS;
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
    return this.world.getBlockState(center).getBlock() == Blocks.WATER;
  }

  private void doFishing(ItemStack fishingRod, BlockPos center) {
    World world = this.getWorld();
    Random rand = world.rand;
    if (rand.nextDouble() < CHANCE && world instanceof ServerWorld) {
      LootTableManager manager = world.getServer().getLootTableManager();
      if (manager == null) {
        return;
      }
      LootTable table = manager.getLootTableFromLocation(LootTables.GAMEPLAY_FISHING);
      if (table == null) {
        return;
      }
      //got it
      int luck = EnchantmentHelper.getEnchantmentLevel(
          Enchantments.LUCK_OF_THE_SEA, fishingRod) + 1;
      Vector3d fffffffffff = new Vector3d(center.getX(), center.getY(), center.getZ());
      LootContext lootContext = new LootContext.Builder((ServerWorld) world)
          .withLuck(luck).withRandom(rand).withParameter(LootParameters.field_237457_g_, fffffffffff)
          .withParameter(LootParameters.TOOL, fishingRod)
          .build(LootParameterSets.FISHING);
      List<ItemStack> lootDrops = table.generate(lootContext);
      if (lootDrops != null && lootDrops.size() > 0) {
        UtilItemStack.damageItem(null, fishingRod);
        UtilItemStack.drop(world, center, lootDrops);
      }
    }
  }

  @Override
  public void setField(int field, int value) {
    if (Fields.values()[field] == Fields.REDSTONE) {
      this.needsRedstone = value % 2;
    }
  }

  @Override
  public int getField(int field) {
    if (Fields.values()[field] == Fields.REDSTONE) {
      return this.needsRedstone;
    }
    return 0;
  }
}
