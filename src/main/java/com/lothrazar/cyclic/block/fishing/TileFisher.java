package com.lothrazar.cyclic.block.fishing;

import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.util.UtilItemStack;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileFisher extends TileEntityBase implements ITickableTileEntity {

  private static final int RADIUS = 8;
  public static final Tag<Item> RODS = new ItemTags.Wrapper(new ResourceLocation(ModCyclic.MODID, "fishing_rods"));
  private static final double CHANCE = 0.1;
  LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  public TileFisher() {
    super(BlockRegistry.Tiles.fisher);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
  }
  //INamedContainerProvider
  //  @Override
  //  public ITextComponent getDisplayName() {
  //    return new StringTextComponent(getType().getRegistryName().getPath());
  //  }
  //
  //  @Nullable
  //  @Override
  //  public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
  //    return new ContainerAnvil(i, world, pos, playerInventory, playerEntity);
  //  }

  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
    if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
      return inventory.cast();
    }
    return super.getCapability(cap, side);
  }

  @Override
  public void read(CompoundNBT tag) {
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
    super.read(tag);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  @Override
  public void tick() {
    inventory.ifPresent(inv -> {
      ItemStack stack = inv.getStackInSlot(0);
      if (stack.getItem().isIn(RODS)) {
        int x = pos.getX() + world.rand.nextInt(RADIUS * 2) - RADIUS;
        int y = pos.getY();
        int z = pos.getZ() + world.rand.nextInt(RADIUS * 2) - RADIUS;
        BlockPos center = new BlockPos(x, y, z);
        if (this.isWater(center)) {
          try {
            this.doFishing(stack, center);
          } //loot tables are explosive
          catch (Exception e) {
            ModCyclic.LOGGER.error("Loot table failed", e);
          }
        }
      }
    });
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
      LootContext lootContext = new LootContext.Builder((ServerWorld) world)
          .withLuck(luck).withRandom(rand).withParameter(LootParameters.POSITION, center)
          .withParameter(LootParameters.TOOL, fishingRod)
          .build(LootParameterSets.FISHING);
      List<ItemStack> lootDrops = table.generate(lootContext);
      if (lootDrops != null && lootDrops.size() > 0) {
        UtilItemStack.damageItem(fishingRod);
        System.out.println(fishingRod.getDamage() + "");
        for (ItemStack dropMe : lootDrops) {
          UtilItemStack.drop(world, center, dropMe);
        }
      }
    }
  }

  @Override
  public void setField(int field, int value) {}
}
