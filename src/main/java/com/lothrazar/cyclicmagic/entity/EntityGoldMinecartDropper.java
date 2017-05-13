package com.lothrazar.cyclicmagic.entity;
import java.util.Random;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityGoldMinecartDropper extends EntityMinecartChest {
  public EntityGoldMinecartDropper(World worldIn) {
    super(worldIn);
  }
  public EntityGoldMinecartDropper(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  public int getSizeInventory() {
    return 9;
  }
  public IBlockState getDefaultDisplayTile() {
    return Blocks.DROPPER.getDefaultState();//.withProperty(BlockChest.FACING, EnumFacing.NORTH);
  }
  public String getGuiID() {
    //minecraft:dropper
    return "minecraft:dropper";
  }
  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    this.addLoot(playerIn);
    return new ContainerDispenser(playerInventory, this);
  }
  BehaviorMinecartDropItem drop = new BehaviorMinecartDropItem();
  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
//    System.out.println("receivingPower"+receivingPower);
    if (receivingPower) {
  
      //      this.minecartContainerItems.get(p_get_1_) 
      this.dispense(this.world, new BlockPos(x, y, z));
    }
  }
  

  protected void dispense(World worldIn, BlockPos pos) {
 
    BlockSourceImpl source = new BlockSourceImpl(worldIn, pos);
 
    int i = this.getDispenseSlot(world.rand);
    if (i < 0) {
      worldIn.playEvent(1001, pos, 0);
    }
    else {
      ItemStack itemstack = this.getStackInSlot(i); 
      this.setInventorySlotContents(i, this.drop.dispense(source, itemstack));
    }
 
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
}
