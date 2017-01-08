package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.tileentity.TileVector;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVectorPlate extends BlockBaseHasTile {
  private static final double BHEIGHT = 0.03125D;
  private static final double COLLISION_HEIGHT = 2 * BHEIGHT;
  protected static final AxisAlignedBB AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, BHEIGHT, 1D);
  protected static final AxisAlignedBB COLLISION_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1D, COLLISION_HEIGHT, 1D);
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
    int yFloor = MathHelper.floor_double(entity.posY);
    double posWithinBlock = entity.posY - yFloor;
    ModCyclic.logger.info("posWithinBlock:" + posWithinBlock);
    TileVector tile = (TileVector) worldIn.getTileEntity(pos);
    if (posWithinBlock <= COLLISION_HEIGHT && entity instanceof EntityLivingBase && tile != null) {//not within the entire block space, just when they land
      entity.fallDistance = 0;
      entity.onGround = false;
      UtilSound.playSound(worldIn, pos, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS);
      float rotationPitch = tile.getAngle(), rotationYaw = tile.getYaw(), power = tile.getActualPower();
      UtilEntity.centerEntityHoriz(entity, pos);
      UtilEntity.setVelocity(entity, rotationPitch, rotationYaw, power);
    }
  }
  @SubscribeEvent
  public void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
    BlockPos pos = event.getPos();
    World world = event.getWorld();
    EntityPlayer player = event.getEntityPlayer();
    ItemStack stack = event.getItemStack();//    ItemStack stack = player.getHeldItem(event.getHand());
    if (player.isSneaking() == false && world.getTileEntity(pos) instanceof TileVector
        && stack != null && Block.getBlockFromItem(stack.getItem()) instanceof BlockVectorPlate) {
      IBlockState iblockstate = world.getBlockState(pos);
      Block block = iblockstate.getBlock();
      TileVector tile = (TileVector) world.getTileEntity(pos);
      if (stack.hasTagCompound()) {
        ((BlockVectorPlate) block).saveStackDataTotile(stack, tile);
        if (world.isRemote)
          UtilChat.addChatMessage(player, "tile.plate_vector.copied");
      }
    }
  }
  @Override
  public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
    ItemStack stack = super.getPickBlock(state, target, world, pos, player);
    if (stack == null || !(world.getTileEntity(pos) instanceof TileVector)) { return null; }
    TileVector tile = (TileVector) world.getTileEntity(pos);
    saveTileDataToStack(stack, tile);
    return stack;
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
      saveTileDataToStack(stack, t);
      UtilItemStack.dropItemStackInWorld(world, pos, stack);
    }
  }
  private void saveTileDataToStack(ItemStack stack, TileVector tile) {
    UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_ANGLE, tile.getAngle());
    UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_POWER, tile.getPower());
    UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_YAW, tile.getYaw());
  }
  private void saveStackDataTotile(ItemStack stack, TileVector tile) {
    if (stack.hasTagCompound()) {
      tile.setField(TileVector.Fields.ANGLE.ordinal(), UtilNBT.getItemStackNBTVal(stack, TileVector.NBT_ANGLE));
      tile.setField(TileVector.Fields.POWER.ordinal(), UtilNBT.getItemStackNBTVal(stack, TileVector.NBT_POWER));
      tile.setField(TileVector.Fields.YAW.ordinal(), UtilNBT.getItemStackNBTVal(stack, TileVector.NBT_YAW));
    }
  }
  public static void saveStackDefault(ItemStack stack) {
    UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_ANGLE, TileVector.DEFAULT_ANGLE);
    UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_POWER, TileVector.DEFAULT_POWER);
    UtilNBT.setItemStackNBTVal(stack, TileVector.NBT_YAW, TileVector.DEFAULT_YAW);
  }
  /**
   * item stack data pushed into tile entity
   */
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    stack.getItem().updateItemStackNBT(stack.getTagCompound());
    TileVector tile = (TileVector) worldIn.getTileEntity(pos);
    if (tile != null) {
      saveStackDataTotile(stack, tile);
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
}
