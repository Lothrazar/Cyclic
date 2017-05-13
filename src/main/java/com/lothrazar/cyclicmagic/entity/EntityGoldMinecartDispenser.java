package com.lothrazar.cyclicmagic.entity;
import java.util.Random;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.item.ItemMinecart;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityGoldMinecartDispenser extends EntityMinecartChest {
  public EntityGoldMinecartDispenser(World worldIn) {
    super(worldIn);
  }
  public EntityGoldMinecartDispenser(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  public int getSizeInventory() {
    return 9;
  }
  public IBlockState getDefaultDisplayTile() {
    return Blocks.DISPENSER.getDefaultState();//.withProperty(BlockChest.FACING, EnumFacing.NORTH);
  }
  public String getGuiID() {
    return "minecraft:dispenser";
  }
  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    this.addLoot(playerIn);
    return new ContainerDispenser(playerInventory, this);
  }
  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    if (receivingPower) {
      //      this.minecartContainerItems.get(p_get_1_) 
      //this.dispense(this.world, new BlockPos(x, y, z));
    }
  }
  /**
   * pulled from BlockDispenser
   * 
   * @param worldIn
   * @param pos
   */
  protected void dispense(World worldIn, BlockPos pos) {
//    System.out.println(pos);
//    System.out.println(worldIn.getBlockState(pos));
    BlockSourceImpl source = new BlockSourceImpl(worldIn, pos);
//    System.out.println(source);
    //      worldIn.getBlockState(pos).value
//    EnumFacing enumfacing = (EnumFacing) source.getBlockState().getValue(BlockDispenser.FACING);
//    System.out.println(enumfacing);
    //      TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.getBlockTileEntity();
    //
    //      if (tileentitydispenser != null)
    //      {
    //we need to use
    //      this.getAdjustedHorizontalFacing();
    //      somehow
    int i = this.getDispenseSlot(world.rand);
    if (i < 0) {
      worldIn.playEvent(1001, pos, 0);
    }
    else {
      ItemStack itemstack = this.getStackInSlot(i);
      IBehaviorDispenseItem ibehaviordispenseitem = this.getBehavior(itemstack);
      this.setInventorySlotContents(i, ibehaviordispenseitem.dispense(source, itemstack));
    }
    //      }
  }
  /**
   * from TileEntityDispenser
   * 
   * @return
   */
  public int getDispenseSlot(Random rand) {
    int i = -1;
    int j = 1;
    for (int k = 0; k < this.getSizeInventory(); ++k) {
      if (!((ItemStack) this.getStackInSlot(k)).isEmpty() && rand.nextInt(j++) == 0) {
        i = k;
      }
    }
    return i;
  }
  /**
   * 
   * pulled from BlockDispenser
   * 
   * @param stack
   * @return
   */
  protected IBehaviorDispenseItem getBehavior(ItemStack stack) {
    return IBehaviorDispenseItem.DEFAULT_BEHAVIOR;//(IBehaviorDispenseItem)BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(stack.getItem());
  }
}
