package com.lothrazar.cyclic.item.transporter;

import java.util.List;
import java.util.function.Consumer;
import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.config.ConfigRegistry;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.library.util.ChatUtil;
import com.lothrazar.library.util.ItemStackUtil;
import com.lothrazar.library.util.SoundUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;


public class TileTransporterItem extends ItemBaseCyclic {

  public static final String KEY_BLOCKID = "block";
  public static final String KEY_BLOCKTILE = "tile";
  public static final String KEY_BLOCKNAME = "blockname";
  public static final String KEY_BLOCKSTATE = "blockstate";

  public TileTransporterItem(Properties prop) {
    super(prop);
  }

  /**
   * Called when a Block is right-clicked with this Item
   */
  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    ItemStack stack = context.getItemInHand();
    BlockPos pos = context.getClickedPos();
    Direction side = context.getClickedFace();
    Level world = context.getLevel();
    BlockPos offset = pos.relative(side);
    if (world.isEmptyBlock(offset) == false) {
      return InteractionResult.FAIL;
    }
    if (placeStoredTileEntity(player, stack, offset)) {
      player.setItemInHand(context.getHand(), ItemStack.EMPTY);
      SoundUtil.playSound(player, SoundRegistry.THUNK.get());
      if (player.isCreative() == false) {
        ItemStackUtil.dropItemStackMotionless(world, player.blockPosition(), new ItemStack(ItemRegistry.TILE_TRANSPORTER_EMPTY.get()));
      }
    }
    return InteractionResult.SUCCESS;
  }

  private boolean placeStoredTileEntity(Player player, ItemStack heldChestSack, BlockPos pos) {
    CompoundTag itemData = heldChestSack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
    Identifier res =   Identifier.parse(itemData.getStringOr(KEY_BLOCKID, ""));
    java.util.Optional<Holder.Reference<Block>> blockRef = BuiltInRegistries.BLOCK.get(res);
    if (blockRef.isEmpty()) {
      heldChestSack = ItemStack.EMPTY;
      ChatUtil.addChatMessage(player, "Invalid block id " + res);
      return false;
    }
    Block block = blockRef.get().value();
    BlockState toPlace = NbtUtils.readBlockState(player.level().holderLookup(Registries.BLOCK), itemData.getCompoundOrEmpty(KEY_BLOCKSTATE));
    if (ConfigRegistry.OVERRIDE_TRANSPORTER_SINGLETON.get()) {
      if (toPlace.hasProperty(BlockStateProperties.CHEST_TYPE)
          && toPlace.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE) {
        toPlace = toPlace.setValue(BlockStateProperties.CHEST_TYPE, ChestType.SINGLE);
      }
    }
    //maybe get from player direction or offset face, but instead rely on that from saved data
    Level world = player.level();
    try {
      world.setBlockAndUpdate(pos, toPlace);
      BlockEntity tile = world.getBlockEntity(pos);
      if (tile != null) {
        CompoundTag tileData = itemData.getCompoundOrEmpty(TileTransporterItem.KEY_BLOCKTILE);
        tileData.putInt("x", pos.getX());
        tileData.putInt("y", pos.getY());
        tileData.putInt("z", pos.getZ());
        tile.loadWithComponents(net.minecraft.world.level.storage.TagValueInput.create(net.minecraft.util.ProblemReporter.DISCARDING, world.registryAccess(), tileData)); // can cause errors in 3rd party mod
        //example at extracells.tileentity.TileEntityFluidFiller.func_145839_a(TileEntityFluidFiller.java:302) ~
        tile.setChanged();
        world.blockEntityChanged(pos);
        //        world.blockEntityChangedWithoutNeighborUpdates(pos);
      }
    }
    catch (Exception e) {
      ModCyclic.LOGGER.error("Error attempting to place block in world", e);
      ChatUtil.sendStatusMessage(player, "chest_sack.error.place");
      world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
      return false;
    }
    heldChestSack = ItemStack.EMPTY;
    heldChestSack.remove(DataComponents.CUSTOM_DATA);
    return true;
  }

  @Override
  public void appendHoverText(ItemStack itemStack, Item.TooltipContext worldIn, TooltipDisplay tooltipDisplay, Consumer<Component> list, TooltipFlag flagIn) {
    if (itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() != null
        && itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().contains(KEY_BLOCKNAME)) {
      String blockname = itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getStringOr(KEY_BLOCKNAME, "");
      if (blockname != null && blockname.length() > 0) {
        MutableComponent t = Component.translatable(ChatUtil.lang(blockname));
        t.withStyle(ChatFormatting.DARK_GREEN);
        list.accept(t);
      }
    }
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return stack.has(DataComponents.CUSTOM_DATA);
  }
}
