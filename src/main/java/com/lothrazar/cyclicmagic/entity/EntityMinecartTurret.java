package com.lothrazar.cyclicmagic.entity;
import java.util.Random;
import com.lothrazar.cyclicmagic.ModCyclic;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockRailPowered;
import net.minecraft.block.BlockSourceImpl;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerDispenser;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityMinecartTurret extends EntityMinecartChest {
  public static Item dropItem = Items.MINECART;//override with gold minecart on registry, this is here just for nonnull
  private static final int TIME_BTW_DROPS = 40;
  private int timeSinceDropped = 0;
  public EntityMinecartTurret(World worldIn) {
    super(worldIn);
    
    this.setDisplayTile(getDefaultDisplayTile());
  }
  public EntityMinecartTurret(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
    this.setDisplayTile(getDefaultDisplayTile());
  }
  public int getSizeInventory() {
    return 0;
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
    return "";// "minecraft:dispenser";
  }
  @Override
  public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
    this.addLoot(playerIn);
    return new ContainerDispenser(playerInventory, this);
  }
  @Override
  public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
    //    EnumFacing fac = this.getAdjustedHorizontalFacing();//.getOpposite();
    //    this.setDisplayTile(getDefaultDisplayTile().withProperty(BlockDispenser.FACING, fac));
    //ModCyclic.logger.info("this.getAdjustedHorizontalFacing()"+fac);
    if (receivingPower) {
      this.dispense( new BlockPos(x, y, z));
    }
  }
  /**
   * pulled from BlockDispenser
   * 
   * @param worldIn
   * @param pos
   */
  protected void dispense(BlockPos pos) {
    if (this.timeSinceDropped > 0) {
      this.timeSinceDropped--;
      return;
    }
 
  
        this.timeSinceDropped = TIME_BTW_DROPS;
        
        
        EnumFacing enumfacing = this.getDisplayTile().getValue(BlockDispenser.FACING);
        
        shootThisDirection( enumfacing);
        shootThisDirection( enumfacing.getOpposite());
      
  
  }
  public void shootThisDirection(  EnumFacing enumfacing) {
    BlockPos position = this.getPosition().up().offset(enumfacing,2);
 
    EntityTippedArrow entitytippedarrow = new EntityTippedArrow(world, position.getX(), position.getY(), position.getZ());
//    entitytippedarrow.setPotionEffect(itemstack);
    entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
 
 
//    IProjectile iprojectile = entitytippedarrow;//this.getProjectileEntity(world, iposition, stack);
    entitytippedarrow.setThrowableHeading((double)enumfacing.getFrontOffsetX(),0.1F, (double)enumfacing.getFrontOffsetZ(), this.getProjectileVelocity(), this.getProjectileInaccuracy());
    world.spawnEntity(entitytippedarrow);
  
  }
  protected float getProjectileInaccuracy()
  {
      return 6.0F;
  }

  protected float getProjectileVelocity()
  {
      return 1.1F;
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
 
  @Override
  protected void moveAlongTrack(BlockPos pos, IBlockState state) {
    BlockRailBase blockrailbase = (BlockRailBase) state.getBlock();
    if(blockrailbase != Blocks.ACTIVATOR_RAIL){
      this.timeSinceDropped=0;
    }
    //force DISPENSER to face sime direction as my movemene
    //      double slopeAdjustment = getSlopeAdjustment();
    BlockRailBase.EnumRailDirection raildirection = blockrailbase.getRailDirection(world, pos, state, this);
    EnumFacing fac = null;
    switch (raildirection) {
      case ASCENDING_EAST:
        fac = EnumFacing.EAST;
      break;
      case ASCENDING_WEST:
        fac = EnumFacing.WEST;
      break;
      case ASCENDING_NORTH:
        fac = EnumFacing.NORTH;
      break;
      case ASCENDING_SOUTH:
        fac = EnumFacing.SOUTH;
      case EAST_WEST:
        fac = (this.motionX > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH;
//        fac = (this.motionX < 0) ? EnumFacing.WEST : EnumFacing.EAST;
        break;
      
      case NORTH_SOUTH:
//        fac = (this.motionZ > 0) ? EnumFacing.SOUTH : EnumFacing.NORTH;
        fac = (this.motionZ < 0) ? EnumFacing.WEST : EnumFacing.EAST;
        break;
  
      default:
        break;
    }

    super.moveAlongTrack(pos, state);
    if (fac != null){
//      ModCyclic.logger.info(raildirection+" setDisplayTile  "+fac);
      this.setDisplayTile(getDefaultDisplayTile().withProperty(BlockDispenser.FACING, fac));
    }
  }
  

  @Override
  public void killMinecart(DamageSource source) {
    this.setDead();
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
 
      ItemStack itemstack = getCartItem();
      if (this.hasCustomName()) {
        itemstack.setStackDisplayName(this.getCustomNameTag());
      }
      this.entityDropItem(itemstack, 0.0F);
    }
  }
  @Override
  public ItemStack getCartItem() {
    return new ItemStack(dropItem);
  }
}
