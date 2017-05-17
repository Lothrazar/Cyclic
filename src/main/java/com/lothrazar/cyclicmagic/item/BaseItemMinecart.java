package com.lothrazar.cyclicmagic.item;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BaseItemMinecart extends BaseItem {
  public BaseItemMinecart() {
    super();
    this.maxStackSize = 16;
    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, CYCLICMINECART_DISPENSER_BEHAVIOR);
  }
  public abstract EntityMinecart summonMinecart(World world);
  public abstract EntityMinecart summonMinecart(World world, double x, double y, double z);
  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
    IBlockState iblockstate = worldIn.getBlockState(pos);
    if (!BlockRailBase.isRailBlock(iblockstate)) {
      return EnumActionResult.FAIL;
    }
    else {
      ItemStack itemstack = player.getHeldItem(hand);
      if (!worldIn.isRemote) {
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate.getBlock()).getRailDirection(worldIn, pos, iblockstate, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        double d0 = 0.0D;
        if (blockrailbase$enumraildirection.isAscending()) {
          d0 = 0.5D;
        }
        EntityMinecart entityminecart = summonMinecart(worldIn);//new EntityMinecartTurret(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D);
        entityminecart.setPosition((double) pos.getX() + 0.5D, (double) pos.getY() + 0.0625D + d0, (double) pos.getZ() + 0.5D);
        if (itemstack.hasDisplayName()) {
          entityminecart.setCustomNameTag(itemstack.getDisplayName());
        }
        worldIn.spawnEntity(entityminecart);
      }
      itemstack.shrink(1);
      return EnumActionResult.SUCCESS;
    }
  }
  public static final IBehaviorDispenseItem CYCLICMINECART_DISPENSER_BEHAVIOR = new BehaviorDefaultDispenseItem() {
    private final BehaviorDefaultDispenseItem behaviourDefaultDispenseItem = new BehaviorDefaultDispenseItem();
    /**
     * Dispense the specified stack, play the dispense sound and spawn
     * particles.
     */
    public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
      if (stack.getItem() instanceof BaseItemMinecart == false) { return stack; }
      EnumFacing enumfacing = (EnumFacing) source.getBlockState().getValue(BlockDispenser.FACING);
      World world = source.getWorld();
      double d0 = source.getX() + (double) enumfacing.getFrontOffsetX() * 1.125D;
      double d1 = Math.floor(source.getY()) + (double) enumfacing.getFrontOffsetY();
      double d2 = source.getZ() + (double) enumfacing.getFrontOffsetZ() * 1.125D;
      BlockPos blockpos = source.getBlockPos().offset(enumfacing);
      IBlockState iblockstate = world.getBlockState(blockpos);
      BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate.getBlock()).getRailDirection(world, blockpos, iblockstate, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
      double d3;
      if (BlockRailBase.isRailBlock(iblockstate)) {
        if (blockrailbase$enumraildirection.isAscending()) {
          d3 = 0.6D;
        }
        else {
          d3 = 0.1D;
        }
      }
      else {
        if (iblockstate.getMaterial() != Material.AIR || !BlockRailBase.isRailBlock(world.getBlockState(blockpos.down()))) { return this.behaviourDefaultDispenseItem.dispense(source, stack); }
        IBlockState iblockstate1 = world.getBlockState(blockpos.down());
        BlockRailBase.EnumRailDirection blockrailbase$enumraildirection1 = iblockstate1.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate1.getBlock()).getRailDirection(world, blockpos.down(), iblockstate1, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
        if (enumfacing != EnumFacing.DOWN && blockrailbase$enumraildirection1.isAscending()) {
          d3 = -0.4D;
        }
        else {
          d3 = -0.9D;
        }
      }
      double x = d0, y = d1 + d3, z = d2;
      EntityMinecart entityminecart = ((BaseItemMinecart) stack.getItem()).summonMinecart(world, x, y, z);
      if (stack.hasDisplayName()) {
        entityminecart.setCustomNameTag(stack.getDisplayName());
      }
      world.spawnEntity(entityminecart);
      stack.shrink(1);
      return stack;
    }
    /**
     * Play the dispense sound from the specified block.
     */
    protected void playDispenseSound(IBlockSource source) {
      source.getWorld().playEvent(1000, source.getBlockPos(), 0);
    }
  };
}
