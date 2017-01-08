package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVectorPlate extends BlockBaseHasTile implements IHasRecipe, IHasConfig {
  private static final double BHEIGHT = 0.03125D;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, BHEIGHT, 1D);
  protected static final AxisAlignedBB COLLISION_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, 2 * BHEIGHT, 1D);
  public BlockVectorPlate() {
    super(Material.IRON);//, 
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setGuiId(ModGuiHandler.GUI_INDEX_VECTOR);
    //    this.setTooltip("tile.harvester_block.tooltip");
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileVector();
  }
  @Override
  public void addRecipe() {
    //    GameRegistry.addRecipe(new ItemStack(this), "rsr", "gbg", "ooo",
    //        'o', Blocks.OBSIDIAN,
    //        'g', Items.QUARTZ,
    //        's', Blocks.DISPENSER,
    //        'r', Items.EMERALD,
    //        'b', Items.DIAMOND);
  }
  @Override
  public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
    return AABB;
  }
  @Nullable
  @Override
  public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos) {
    return COLLISION_AABB;
  }
  @Override
  public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entity) {
    //    int yFloor = MathHelper.floor_double(entity.posY);
    //    double posWithinBlock = entity.posY - yFloor;
    //dont need this check, fix ed by collision bb ) posWithinBlock <= BHEIGHT && 
    TileVector tile = (TileVector) worldIn.getTileEntity(pos);
    if (entity instanceof EntityLivingBase && tile != null) {//not within the entire block space, just when they land
      entity.fallDistance = 0;
      UtilSound.playSound(worldIn, pos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS);
      float rotationPitch = tile.getAngle(), rotationYaw = tile.getYaw(), power = tile.getActualPower();
      UtilEntity.setVelocity(entity, rotationPitch, rotationYaw, power);
      //      entity.motionX = 0;
      //      entity.motionY = 0;
      //      entity.motionZ = 0;
      //      //      float LIMIT = 180F;
      //      double velX = (double) (-MathHelper.sin(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power);
      //      double velZ = (double) (MathHelper.cos(rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(rotationPitch / 180.0F * (float) Math.PI) * power);
      //      double velY = (double) (-MathHelper.sin((rotationPitch) / 180.0F * (float) Math.PI) * power);
      //      // launch the player up and forward at minimum angle
      //      //      // regardless of look vector
      //      if (velY < 0) {
      //        velY *= -1;// make it always up never down
      //      }
      ////      System.out.println("launch posWithinBlock" + posWithinBlock);
      ////      System.out.println("launch eh" + tile.getAngle() + "," + tile.getYaw() + "," + tile.getActualPower());
      ////      System.out.println("!set velocity " + velX + "," + velY + "," + velZ);
      //      //use set velocity instead of add. TODO maybe refactor back into utilentiyt.setMotion
      //      // 
      //      entity.setVelocity(velX, velY, velZ);
    }
  }
  @Override
  public void syncConfig(Configuration config) {
    //    String category = Const.ConfigCategory.modpackMisc;
    //    TileMachineHarvester.TIMER_FULL = config.getInt("HarvesterTime", category, 80, 10, 9999, "Number of ticks it takes to run one time, so lower is faster");
  }
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
    return true;
  }
  //disable regular drops, make my own drop that saves nbt
  @Override
  public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    return null;
  }
  @SubscribeEvent
  public void onBreakEvent(BreakEvent event) {
    if (event.getPlayer() != null && event.getPlayer().capabilities.isCreativeMode) { return; } // dont drop in creative https://github.com/PrinceOfAmber/Cyclic/issues/93
    World world = event.getWorld();
    BlockPos pos = event.getPos();
    IBlockState state = event.getState();
    TileEntity ent = world.getTileEntity(pos);
    if (ent != null && ent instanceof TileVector) {
      TileVector t = (TileVector) ent;
      ItemStack stack = new ItemStack(state.getBlock());
      UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_ANGLE, t.getAngle());
      UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_POWER, t.getPower());
      UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_YAW, t.getYaw());
      UtilItemStack.dropItemStackInWorld(world, pos, stack);
    }
  }
  /**
   * item stack data pushed into tile entity
   */
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      TileVector tile = (TileVector) worldIn.getTileEntity(pos);
      if (tile != null) {
        tile.setField(TileVector.Fields.ANGLE.ordinal(), UtilNBT.getItemStackNBTVal(stack, TileVector.NBT_ANGLE));
        tile.setField(TileVector.Fields.POWER.ordinal(), UtilNBT.getItemStackNBTVal(stack, TileVector.NBT_POWER));
        tile.setField(TileVector.Fields.YAW.ordinal(), UtilNBT.getItemStackNBTVal(stack, TileVector.NBT_YAW));
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
}
