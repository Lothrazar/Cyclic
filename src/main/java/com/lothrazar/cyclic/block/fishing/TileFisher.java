package com.lothrazar.cyclic.block.fishing;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.block.TileBlockEntityCyclic;
import com.lothrazar.cyclic.data.DataTags;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.library.util.ItemStackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;

import java.util.List;

public class TileFisher extends TileBlockEntityCyclic implements MenuProvider {

  public static IntValue RADIUS;
  public static DoubleValue CHANCE;
  ItemStackHandler inventory = new ItemStackHandler(1) {

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
      return stack.is(DataTags.FISHING_RODS);
    }
  };

  static enum Fields {
    REDSTONE;
  }

  public TileFisher(BlockPos pos, BlockState state) {
    super(TileRegistry.FISHER.get(), pos, state);
    this.needsRedstone = 0;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState, TileFisher e) {
    e.tick();
  }

  @Override
  public Component getDisplayName() {
    return BlockRegistry.FISHER.get().getName();
  }

  @Override
  public AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player playerEntity) {
    return new ContainerFisher(i, level, worldPosition, playerInventory, playerEntity);
  }

  @Override
  public void loadAdditional(ValueInput input) {
    inventory.deserialize(input.childOrEmpty(NBTINV));
    super.loadAdditional(input);
  }

  @Override
  public void saveAdditional(ValueOutput output) {
    inventory.serialize(output.child(NBTINV));
    super.saveAdditional(output);
  }

  public void tick() {
    if (this.requiresRedstone() && !this.isPowered()) {
      return;
    }
    final int radius = RADIUS.get();
    ItemStack stack = inventory.getStackInSlot(0);
    if (stack.is(DataTags.FISHING_RODS)) {
      int x = worldPosition.getX() + level.getRandom().nextInt(radius * 2) - radius;
      int y = worldPosition.getY();
      int z = worldPosition.getZ() + level.getRandom().nextInt(radius * 2) - radius;
      BlockPos center = new BlockPos(x, y, z);
      if (isWater(this.level, center)) {
        try {
          this.doFishing(stack, center);
        } //loot tables are explosive
        catch (Exception e) {
          ModCyclic.LOGGER.error("Fishing Block: Loot table failed", e);
        }
        updateComparatorOutputLevel();
      }
    }
  }

  public static boolean isWater(Level level, BlockPos center) {
    return level.getBlockState(center).getBlock() == Blocks.WATER;
  }

  private void doFishing(ItemStack fishingRod, BlockPos center) {

    Level world = this.getLevel();
    RandomSource rand = world.getRandom();
    if (rand.nextDouble() < CHANCE.get() && world instanceof ServerLevel) {
      LootTable table = level.getServer().reloadableRegistries().getLootTable(BuiltInLootTables.FISHING);
      if (table == null) {
        return;
      }
      //got it
      HolderLookup.RegistryLookup<Enchantment> enchLookup = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
      Holder<Enchantment> luckHolder = enchLookup.getOrThrow(Enchantments.LUCK_OF_THE_SEA);
      Holder<Enchantment> mendingHolder = enchLookup.getOrThrow(Enchantments.MENDING);
      int luck = EnchantmentHelper.getItemEnchantmentLevel(luckHolder, fishingRod) + 1;
      LootParams lootContext = new LootParams.Builder((ServerLevel) world)
          .withLuck(luck)//.withRandom(rand)
          .withParameter(LootContextParams.ORIGIN,
              new Vec3(center.getX(), center.getY(), center.getZ()))
          .withParameter(LootContextParams.TOOL, fishingRod)
          .create(LootContextParamSets.FISHING);
      List<ItemStack> lootDrops = table.getRandomItems(lootContext);
      if (lootDrops != null && lootDrops.size() > 0) {
        ItemStackUtil.drop(world, center, lootDrops);
        if (fishingRod.isDamageableItem()) {
          int mending = EnchantmentHelper.getItemEnchantmentLevel(mendingHolder, fishingRod);
          if (mending == 0) {
            ItemStackUtil.damageItem(null, fishingRod);
          }
          else { // https://github.com/Lothrazar/Cyclic/blob/trunk/1.12/src/main/java/com/lothrazar/cyclicmagic/block/fishing/TileEntityFishing.java#L209
            //copy alg from MC 1.12.2 version 
            if (rand.nextDouble() < 0.25) { //25% chance damage
              ItemStackUtil.damageItem(null, fishingRod);
            }
            else if (rand.nextDouble() < 0.66) { //66-25 = chance repair
              if (fishingRod.getDamageValue() > 0) {
                // mimics getting damaged and repaired right away
                fishingRod.setDamageValue(fishingRod.getDamageValue() - rand.nextInt(2, 5));
              }
            }
          }
        } // else fishing rod cannot be damaged (supreme/diamond/other mods)
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

  @Override
  public IItemHandler getItemHandler(Direction side) {
    return inventory;
  }

}
