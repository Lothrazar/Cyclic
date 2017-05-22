package com.lothrazar.cyclicmagic.entity;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.item.minecart.ItemGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityMinecartFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityGoldFurnaceMinecart extends EntityMinecart {
  public static ItemGoldFurnaceMinecart dropItem;
  private static final DataParameter<Boolean> POWERED = EntityDataManager.<Boolean> createKey(EntityMinecartFurnace.class, DataSerializers.BOOLEAN);
  private int fuel;
  public double pushX;
  public double pushZ;
  public EntityGoldFurnaceMinecart(World worldIn) {
    super(worldIn);
    this.setDisplayTile(this.getDefaultDisplayTile());
  }
  public EntityGoldFurnaceMinecart(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  public EntityMinecart.Type getType() {
    return EntityMinecart.Type.FURNACE;
  }
  protected void entityInit() {
    super.entityInit();
    this.dataManager.register(POWERED, Boolean.valueOf(false));
  }
  /**
   * Called to update the entity's position/logic.
   */
  public void onUpdate() {
    super.onUpdate();
    if (this.fuel > 0) {
      --this.fuel;
    }
    if (this.fuel <= 0) {
      this.pushX = 0.0D;
      this.pushZ = 0.0D;
    }
    else {//key addition. if fuel is non zero, dont let me stop!!!
      this.pushX = this.getMaximumSpeed();
      this.pushZ = this.getMaximumSpeed();
    }
    this.setMinecartPowered(this.fuel > 0);
    if (this.fuel > 0 && this.rand.nextInt(4) == 0) {
      this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY + 0.8D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
    }
  }
  /**
   * Get's the maximum speed for a minecart vanilla is 0.4D default
   */
  @Override
  protected double getMaximumSpeed() {
    return super.getMaximumSpeed() + 0.5D;
  }
  /**
   * Returns the carts max speed when traveling on rails. Carts going faster
   * than 1.1 cause issues with chunk loading. Carts cant traverse slopes or
   * corners at greater than 0.5 - 0.6. This value is compared with the rails
   * max speed and the carts current speed cap to determine the carts current
   * max speed. A normal rail's max speed is 0.4.
   *
   * @return Carts max speed.
   */
  public float getMaxCartSpeedOnRail() {
    return super.getMaxCartSpeedOnRail() + 0.1f;//super is 1.2
  }
  @Override
  public void killMinecart(DamageSource source) {
    this.setDead();
    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
      ItemStack itemstack = new ItemStack(dropItem);
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
  @Override
  protected void moveAlongTrack(BlockPos pos, IBlockState state) {
    super.moveAlongTrack(pos, state);
    double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
    //TODO:  STILL this sometimes stops randomly at a 90 DEG ANGLE WHEN pushing
    if (this.fuel > 0 && this.motionX * this.motionX < 0.001 && this.motionZ * this.motionZ < 0.001
    //          && this.motionX> 1.0E-4D  && this.motionX>1.0E-4D
    ) {
      ModCyclic.logger.info(" fueled with motion zero?");
      this.motionX = this.motionX * 2;
      this.motionZ = this.motionZ * 2;
      //if that didnt owrk
      if (this.motionX == 0 && this.posX - this.prevPosX != 0) {
        this.motionX = (this.posX - this.prevPosX) * 8;
        ModCyclic.logger.info("motionX hax" + this.motionX);
        this.pushX = (this.posX - this.prevPosX) * (this.posX - this.prevPosX);
        if (pushZ == 0) {
          pushZ = 0.5;
        }
      }
      if (this.motionZ == 0 && this.posZ - this.prevPosZ != 0) {
        this.motionZ = (this.posZ - this.prevPosZ) * 8;
        this.pushZ = (this.posZ - this.prevPosZ) * (this.posZ - this.prevPosZ);
        ModCyclic.logger.info("motionZ hax" + this.motionZ);
        if (pushX == 0) {
          pushX = 0.5;
        }
      }
    }
    //      
    if (d0 > 1.0E-4D) {
      d0 = (double) MathHelper.sqrt(d0);
      //          this.pushX /= d0;
      //          this.pushZ /= d0;
      if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0D) {
        this.pushX = 0.0D;
        this.pushZ = 0.0D;
      }
      else {
        double d1 = d0 * this.getMaximumSpeed();
        //        double d1 = d0 / this.getMaximumSpeed();
        this.pushX *= d1;
        this.pushZ *= d1;
      }
    }
  }
  protected void applyDrag() {
    if (this.fuel > 0) { return; }
    double d0 = this.pushX * this.pushX + this.pushZ * this.pushZ;
    if (d0 > 1.0E-4D) {
      d0 = (double) MathHelper.sqrt(d0);
      this.pushX /= d0;
      this.pushZ /= d0;
      double d1 = 1.0D;
      this.motionX *= 0.800000011920929D;
      this.motionY *= 0.0D;
      this.motionZ *= 0.800000011920929D;
      this.motionX += this.pushX * 1.0D;
      this.motionZ += this.pushZ * 1.0D;
    }
    else {
      this.motionX *= 0.9800000190734863D;
      this.motionY *= 0.0D;
      this.motionZ *= 0.9800000190734863D;
    }
    super.applyDrag();
  }
  public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
    ItemStack itemstack = player.getHeldItem(hand);
    if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.minecart.MinecartInteractEvent(this, player, hand))) return true;
    if (itemstack.getItem() == Items.COAL && this.fuel + 3600 <= 32000) {
      if (!player.capabilities.isCreativeMode) {
        itemstack.shrink(1);
      }
      this.fuel += 3600;
    }
    if (player.world.isRemote) {
      UtilChat.addChatMessage(player, UtilChat.lang("minecart.fuel") + this.fuel);
    }
    return true;
  }
  /**
   * (abstract) Protected helper method to write subclass entity data to NBT.
   */
  protected void writeEntityToNBT(NBTTagCompound compound) {
    super.writeEntityToNBT(compound);
    compound.setDouble("PushX", this.pushX);
    compound.setDouble("PushZ", this.pushZ);
    compound.setShort("Fuel", (short) this.fuel);
  }
  /**
   * (abstract) Protected helper method to read subclass entity data from NBT.
   */
  protected void readEntityFromNBT(NBTTagCompound compound) {
    super.readEntityFromNBT(compound);
    this.pushX = compound.getDouble("PushX");
    this.pushZ = compound.getDouble("PushZ");
    this.fuel = compound.getShort("Fuel");
  }
  protected boolean isMinecartPowered() {
    return ((Boolean) this.dataManager.get(POWERED)).booleanValue();
  }
  protected void setMinecartPowered(boolean p_94107_1_) {
    this.dataManager.set(POWERED, Boolean.valueOf(p_94107_1_));
  }
  public IBlockState getDefaultDisplayTile() {
    return (this.isMinecartPowered() ? Blocks.LIT_FURNACE : Blocks.FURNACE).getDefaultState().withProperty(BlockFurnace.FACING, EnumFacing.NORTH);
  }
  @Override
  public void applyEntityCollision(Entity entityIn) {
    double motionXprev = this.motionX;
    //    double motionprev = this.motionY;
    double motionZprev = this.motionZ;
    super.applyEntityCollision(entityIn);
    if (this.fuel > 0 &&
        entityIn instanceof EntityMinecart &&
        (this.motionX != motionXprev || this.motionZ != motionZprev)) {
      //well i am the engine, i should not get stopped in my tracks just because theres two shits in front
      ModCyclic.logger.info("undo bwahaha" + motionXprev + "_" + motionZprev);
      this.motionX = motionXprev * 8;
      this.motionZ = motionZprev * 8;
    }
  }
}
