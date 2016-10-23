package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import com.lothrazar.cyclicmagic.ModMain;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockBaseFacingInventory extends BlockBaseFacing {
  private int guiID;
  public BlockBaseFacingInventory(Material materialIn, int pguiIndex) {
    super(materialIn);
    guiID = pguiIndex;
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (player.isSneaking()) { return false; }
    if (world.isRemote) { return true; }
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    player.openGui(ModMain.instance, this.guiID, world, x, y, z);
    return true;
  }
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    this.dropItems(world, pos, state);
    super.breakBlock(world, pos, state);
  }
  protected void dropItems(World world, BlockPos pos, IBlockState state) {
    Random rand = world.rand;
    TileEntity tile = world.getTileEntity(pos);
    if (tile instanceof IInventory == false) { return; }
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    IInventory inventory = (IInventory) tile;
    for (int i = 0; i < inventory.getSizeInventory(); i++) {
      ItemStack item = inventory.getStackInSlot(i);
      if (item != null && item.stackSize > 0) {
        float rx = rand.nextFloat() * 0.8F + 0.1F;
        float ry = rand.nextFloat() * 0.8F + 0.1F;
        float rz = rand.nextFloat() * 0.8F + 0.1F;
        EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.getItem(), item.stackSize, item.getItemDamage()));
        if (item.hasTagCompound()) {
          entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
        }
        float f = 0.05F;
        entityItem.motionX = rand.nextGaussian() * f;
        entityItem.motionY = rand.nextGaussian() * f + 0.2F;
        entityItem.motionZ = rand.nextGaussian() * f;
        world.spawnEntityInWorld(entityItem);
        item.stackSize = 0;
      }
    }
  }
  public boolean hasComparatorInputOverride(IBlockState state) {
    return true;
  }
  public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
    return Container.calcRedstone(worldIn.getTileEntity(pos));
  }
}
