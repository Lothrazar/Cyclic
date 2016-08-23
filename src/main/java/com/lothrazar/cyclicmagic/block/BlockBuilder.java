package com.lothrazar.cyclicmagic.block;
import java.util.Random;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineBuilder;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockBuilder extends BlockBaseHorizontal implements IHasRecipe {
  // dont use blockContainer !!
  // http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockBuilder() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
    TileMachineBuilder tileEntity = (TileMachineBuilder) world.getTileEntity(pos);
    if (tileEntity == null || player.isSneaking()) { return false; }
    if (world.isRemote) { return true; }
    int x = pos.getX(), y = pos.getY(), z = pos.getZ();
    player.openGui(ModMain.instance, ModGuiHandler.GUI_INDEX_BUILDER, world, x, y, z);
    return true;
  }
  @Override
  public void breakBlock(World world, BlockPos pos, IBlockState state) {
    this.dropItems(world, pos, state);
    super.breakBlock(world, pos, state);
  }
  private void dropItems(World world, BlockPos pos, IBlockState state) {
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
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachineBuilder();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this), "rsr", "gbg", "ooo",
        'o', Blocks.OBSIDIAN,
        'g', Items.GHAST_TEAR,
        's', Blocks.DISPENSER,
        'r', Blocks.REDSTONE_BLOCK,
        'b', Blocks.DIAMOND_BLOCK);
  }
}
