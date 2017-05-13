package com.lothrazar.cyclicmagic.entity;
import java.util.Random;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityGoldMinecartDispenser extends EntityMinecartChest {
  private static final int TIME_BTW_DROPS = 20;
  private int timeSinceDropped = 0;
  private FakeWorld fakeWorld;
  public EntityGoldMinecartDispenser(World worldIn) {
    super(worldIn);
    fakeWorld = new FakeWorld(worldIn, this);
  }
  public EntityGoldMinecartDispenser(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
    fakeWorld = new FakeWorld(worldIn, this);
  }
  public int getSizeInventory() {
    return 9;
  }
  public IBlockState getDefaultDisplayTile() {
    return Blocks.DISPENSER.getDefaultState();//.withProperty(BlockChest.FACING, EnumFacing.NORTH);
  }
  @Override
  protected void writeEntityToNBT(NBTTagCompound compound) {
    compound.setInteger("tdr", timeSinceDropped);
    super.writeEntityToNBT(compound);
  }
  @Override
  protected void readEntityFromNBT(NBTTagCompound compound) {
    timeSinceDropped = compound.getInteger("tdr");
    super.readEntityFromNBT(compound);
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
    EnumFacing fac = this.getAdjustedHorizontalFacing();//.getOpposite();
    this.setDisplayTile(getDefaultDisplayTile().withProperty(BlockDispenser.FACING, fac));
    //ModCyclic.logger.info("this.getAdjustedHorizontalFacing()"+fac);
    if (receivingPower) {
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
    if (this.timeSinceDropped > 0) {
      this.timeSinceDropped--;
      return;
    }
    BlockSourceImpl source = new BlockSourceImpl(worldIn, pos);
    int i = this.getDispenseSlot(worldIn.rand);
    if (i < 0) {
      world.playEvent(1001, pos, 0);
    }
    else {
      ItemStack itemstack = this.getStackInSlot(i);
      IBehaviorDispenseItem ibehaviordispenseitem = this.getBehavior(itemstack);
      //    ModCyclic.logger.info("BEHAVIOR "+ibehaviordispenseitem);
      ItemStack result = ibehaviordispenseitem.dispense(source, itemstack);
      this.setInventorySlotContents(i, result);
      this.timeSinceDropped = TIME_BTW_DROPS;
    }
  }
  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (source.getEntity() == this || source.getEntity() instanceof EntityArrow) {
      amount = 0;
      return false;
    }
    return true;
  }
  @Override
  public AxisAlignedBB getCollisionBox(Entity entityIn) {
    if (entityIn instanceof EntityArrow) { return new AxisAlignedBB(this.getPosition(), this.getPosition()); }
    return super.getCollisionBox(entityIn);
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
    //    ModCyclic.logger.info("BEHAVIOR??? "+ stack);
    return BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.getObject(stack.getItem());
  }
}
