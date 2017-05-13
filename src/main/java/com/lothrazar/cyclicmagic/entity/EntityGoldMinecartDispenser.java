package com.lothrazar.cyclicmagic.entity;
import java.util.Random;
import com.lothrazar.cyclicmagic.ModCyclic;
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
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityGoldMinecartDispenser extends EntityMinecartChest {
  public BlockPos ORIGIN_POS = new BlockPos(0, 0, 0);
  private FakeWorld fakeWorld;
  public EntityGoldMinecartDispenser(World worldIn) {
    super(worldIn);
    fakeWorld = new FakeWorld(worldIn,this);
  }
  public EntityGoldMinecartDispenser(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
    fakeWorld = new FakeWorld(worldIn,this);
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
      this.dispense(this.fakeWorld, new BlockPos(x, y, z));
    }
  }
  /**
   * pulled from BlockDispenser
   * 
   * @param worldIn
   * @param pos
   */
  protected void dispense(World worldIn, BlockPos pos) {
 
    BlockSourceImpl source = new BlockSourceImpl(worldIn, pos);
 
    int i = this.getDispenseSlot(worldIn.rand);
    if (i < 0) {
      worldIn.playEvent(1001, pos, 0);
    }
    else {
      ItemStack itemstack = this.getStackInSlot(i);
      IBehaviorDispenseItem ibehaviordispenseitem = this.getBehavior(itemstack);
    ModCyclic.logger.info("BEHAVIOR "+ibehaviordispenseitem);
      try{
        ItemStack result = ibehaviordispenseitem.dispense(source, itemstack);
      this.setInventorySlotContents(i, result);
      }catch(Exception e){
        System.out.println("yep error eh "+e.getMessage());
      }
    }
    //      }
  }
  @Override


  public void killMinecart(DamageSource source)
  {
    ModCyclic.logger.info("killMinecart "+source);
      super.killMinecart(source);
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
    ModCyclic.logger.info("BEHAVIOR??? "+ stack);
    return BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(stack.getItem());
  }
}
