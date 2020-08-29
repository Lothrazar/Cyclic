package com.lothrazar.cyclic.block.creativeitem;

import com.lothrazar.cyclic.base.TileEntityBase;
import com.lothrazar.cyclic.registry.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileItemInfinite extends TileEntityBase implements ITickableTileEntity {

  private LazyOptional<IItemHandler> inventory = LazyOptional.of(this::createHandler);

  public TileItemInfinite() {
    super(BlockRegistry.TileRegistry.item_infinite);
  }

  private IItemHandler createHandler() {
    return new ItemStackHandler(1);
  }

  private void setAnimation(boolean lit) {
    BlockState st = this.world.getBlockState(pos);
    boolean previous = st.get(BlockItemInfinite.IS_LIT);
    if (previous != lit)
      this.world.setBlockState(pos, st.with(BlockItemInfinite.IS_LIT, lit));
  }

  @Override
  public void read(BlockState bs, CompoundNBT tag) {
    super.read(bs, tag);
    inventory.ifPresent(h -> ((INBTSerializable<CompoundNBT>) h).deserializeNBT(tag.getCompound("inv")));
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    inventory.ifPresent(h -> {
      CompoundNBT compound = ((INBTSerializable<CompoundNBT>) h).serializeNBT();
      tag.put("inv", compound);
    });
    return super.write(tag);
  }

  //  @Override
  public ITextComponent getDisplayName() {
    return new StringTextComponent(getType().getRegistryName().getPath());
  }

  @Override
  public void tick() {
    //    setAnimation(isFlowing);//if item exists
  }

  @Override
  public void setField(int field, int value) {}
}
