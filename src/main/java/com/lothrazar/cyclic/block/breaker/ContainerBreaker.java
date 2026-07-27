package com.lothrazar.cyclic.block.breaker;

import com.lothrazar.cyclic.util.CapabilityUtil;
import com.lothrazar.cyclic.gui.ContainerBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import com.lothrazar.cyclic.registry.MenuTypeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class ContainerBreaker extends ContainerBase {


  private final Level level;
  private final BlockPos pos;
  public TileBreaker tile;

  public ContainerBreaker(int windowId, Level world, BlockPos pos, Inventory playerInventory, Player player) {
    super(MenuTypeRegistry.BREAKER.get(), windowId);
    this.level=world;
    this.pos=pos;
    this.tile = (TileBreaker) world.getBlockEntity(pos);
    this.playerEntity = player;
    this.playerInventory = playerInventory;
    var h = CapabilityUtil.item(world,pos);
      this.endInv = h.getSlots();
      addSlot(new SlotItemHandler(h, 0, 81, 31) {

        // 26.1: CapabilityUtil.item(...) returns a bridge (IItemHandler.of(resourceHandler)) that no
        // longer implements IItemHandlerModifiable - the base SlotItemHandler#set() hard-casts to it
        // and crashes the container on open (container-content sync calls this for every slot).
        // Emulate "overwrite this slot" via extract-then-insert instead.
        @Override
        public void set(ItemStack stack) {
          IItemHandler handler = getItemHandler();
          handler.extractItem(index, handler.getStackInSlot(index).getCount(), false);
          if (!stack.isEmpty()) {
            handler.insertItem(index, stack, false);
          }
          setChanged();
        }
      });
    layoutPlayerInventorySlots(8, 84);
    trackEnergy(this.tile);
    this.trackAllIntFields(this.tile, TileBreaker.Fields.values().length);
  }

  @Override
  public boolean stillValid(Player playerIn) {
    return stillValid(ContainerLevelAccess.create(level,pos), playerEntity, BlockRegistry.BREAKER.get());
  }
}
