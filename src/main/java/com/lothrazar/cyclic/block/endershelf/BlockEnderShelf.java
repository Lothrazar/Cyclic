package com.lothrazar.cyclic.block.endershelf;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.registry.TileRegistry;
import com.lothrazar.cyclic.util.UtilBlockstates;
import com.lothrazar.cyclic.util.UtilEnchant;
import java.util.Map;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class BlockEnderShelf extends BlockBase {

  public BlockEnderShelf(Properties properties, boolean isController) {
    super(properties.hardnessAndResistance(1.8F));
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void registerClient() {
    ClientRegistry.bindTileEntityRenderer(TileRegistry.ender_shelf, EnderShelfRenderer::new);
    ClientRegistry.bindTileEntityRenderer(TileRegistry.ender_controller, EnderShelfRenderer::new);
  }

  @Override
  protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(BlockStateProperties.HORIZONTAL_FACING);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileEnderShelf();
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity entity, ItemStack stack) {
    if (entity != null) {
      world.setBlockState(pos, state.with(BlockStateProperties.HORIZONTAL_FACING, UtilBlockstates.getFacingFromEntityHorizontal(pos, entity)), 2);
      if (world.getTileEntity(pos) != null && world.getTileEntity(pos) instanceof TileEnderShelf) {
        TileEnderShelf shelf = (TileEnderShelf) world.getTileEntity(pos);
        BlockPos controllerPos = null;
        TileEnderShelf controller = null;
        if (EnderShelfHelper.isController(state)) {
          shelf.setControllerLocation(pos);
          controllerPos = pos;
          controller = shelf;
        }
        else if (EnderShelfHelper.isShelf(state)) {
          controllerPos = EnderShelfHelper.findConnectedController(world, pos);
          shelf.setControllerLocation(controllerPos);
          controller = getTileEntity(world, controllerPos);
        }
        if (controllerPos != null && controller != null) {
          Set<BlockPos> shelves = EnderShelfHelper.findConnectedShelves(world, controllerPos);
          controller.setShelves(shelves);
        }
      }
    }
  }

  @Override
  public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
    boolean isCurrentlyShelf = EnderShelfHelper.isShelf(state);
    boolean isNewShelf = EnderShelfHelper.isShelf(newState);
    TileEnderShelf te = getTileEntity(worldIn, pos);
    if (isCurrentlyShelf && !isNewShelf && te != null && te.getControllerLocation() != null) {
      //trigger controller reindex
      te.setShelves(EnderShelfHelper.findConnectedShelves(worldIn, pos));
    }
    if (state.getBlock() != newState.getBlock()) {
      TileEntity tileentity = worldIn.getTileEntity(pos);
      if (tileentity != null) {
        IItemHandler items = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
        if (items != null) {
          for (int i = 0; i < items.getSlots(); ++i) {
            ItemStack is = items.getStackInSlot(i);
            while (!is.isEmpty()) {
              InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), is.split(1));
            }
          }
          worldIn.updateComparatorOutputLevel(pos, this);
          worldIn.removeTileEntity(pos);
        }
      }
    }
  }

  @Override
  public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
    ItemStack heldItem = player.getHeldItem(hand);
    if (hand != Hand.MAIN_HAND && heldItem.isEmpty()) {
      //if your hand is empty, dont process if its the OFF hand
      //otherwise: main hand inserts, off hand takes out right away
      return ActionResultType.PASS;
    }
    Direction face = hit.getFace();
    Vector3d hitVec = hit.getHitVec();
    int slot = getSlotFromHitVec(pos, face, hitVec);
    if (world.getTileEntity(pos) instanceof TileEnderShelf) {
      TileEnderShelf shelf = getTileEntity(world, pos);
      if (EnderShelfHelper.isShelf(state) && hit.getFace() == state.get(BlockStateProperties.HORIZONTAL_FACING)) {
        //
        // single shelf
        //
        if (heldItem.getItem() == Items.ENCHANTED_BOOK) {
          shelf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            if (h.getStackInSlot(slot) == ItemStack.EMPTY || UtilEnchant.doBookEnchantmentsMatch(h.getStackInSlot(slot), heldItem)) {
              if (!world.isRemote) {
                ItemStack remaining = h.insertItem(slot, heldItem, false);
                player.setHeldItem(hand, remaining);
              }
            }
          });
        }
        else if (heldItem.isEmpty()) {
          shelf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            ItemStack retrievedBook = h.extractItem(slot, 1, false);
            player.setHeldItem(hand, retrievedBook);
          });
        }
      }
      else if (EnderShelfHelper.isController(state)) {
        //
        // controller
        //
        if (heldItem.getItem() == Items.ENCHANTED_BOOK) {
          shelf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(h -> {
            insertIntoController(player, hand, heldItem, h);
          });
        }
        return ActionResultType.CONSUME;
      }
    }
    return ActionResultType.PASS;
  }

  private void insertIntoController(PlayerEntity player, Hand hand, ItemStack heldItem, IItemHandler h) {
    Map<Enchantment, Integer> allofthem = EnchantmentHelper.getEnchantments(heldItem);
    if (allofthem.size() == 1) {
      ItemStack insertResult = h.insertItem(0, heldItem, false);
      player.setHeldItem(hand, insertResult);
    }
    else {
      //loop and make books of each
      Enchantment[] flatten = allofthem.keySet().toArray(new Enchantment[0]);
      for (Enchantment entry : flatten) {
        // try it
        ItemStack fake = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(fake, new EnchantmentData(entry, allofthem.get(entry)));
        ItemStack insertResult = h.insertItem(0, fake, false);
        if (insertResult.isEmpty()) {
          //ok it worked, so REMOVE that from the og set
          allofthem.remove(entry);
        }
      }
      //now set it back into the book
      if (allofthem.isEmpty()) {
        player.setHeldItem(hand, ItemStack.EMPTY);
      }
      else {
        //        apply all to the book and give the book back
        ItemStack newFake = new ItemStack(Items.ENCHANTED_BOOK);
        for (Enchantment newentry : allofthem.keySet()) {
          EnchantedBookItem.addEnchantment(newFake, new EnchantmentData(newentry, allofthem.get(newentry)));
        }
        player.setHeldItem(hand, newFake);
      }
    }
  }

  private int getSlotFromHitVec(BlockPos pos, Direction face, Vector3d hitVec) {
    double normalizedY = hitVec.getY() - pos.getY();
    //    double normalizedX = hitVec.getX() - pos.getX();
    //    if (face == Direction.EAST || face == Direction.WEST)
    //      normalizedX = hitVec.getZ() - pos.getZ();
    return (int) Math.floor(normalizedY / 0.20);
  }

  public TileEnderShelf getTileEntity(World world, BlockPos pos) {
    if (pos == null) {
      return null;
    }
    BlockState state = world.getBlockState(pos);
    return EnderShelfHelper.isShelf(state) || EnderShelfHelper.isController(state) ? (TileEnderShelf) world.getTileEntity(pos) : null;
  }
}
