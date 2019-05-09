package com.lothrazar.cyclicmagic.block.batterycell;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.battery.ItemBlockBattery;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.capability.EnergyStore;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockBatteryCell extends BlockBaseHasTile implements IHasRecipe, IContent {

  public static final int MAX_SMALL = 1000000;
  public static final int MAX_MED = 16 * MAX_SMALL;
  public static final int MAX_LRG = 64 * MAX_SMALL;
  private int capacity;

  public BlockBatteryCell(int capacity) {
    super(Material.ROCK);
    this.capacity = capacity;
  }

  @Override
  public String getContentName() {
    return "battery_cell";
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, new ItemBlockBattery(this), getContentName(), GuideCategory.BLOCKMACHINE);
    BlockRegistry.registerTileEntity(TileEntityBatteryCell.class, Const.MODID + getContentName() + "_te");
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean(getContentName(), Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        "cbc",
        "bab",
        "cbc",
        'c', Items.CLAY_BALL,
        'b', "obsidian",
        'a', "blockRedstone");
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityBatteryCell(capacity);
  }

  //start of 'fixing getDrops to not have null tile entity', using pattern from forge BlockFlowerPot patch
  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    if (willHarvest) {
      return true;
    } //If it will harvest, delay deletion of the block until after getDrops
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }

  @Override
  public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool) {
    super.harvestBlock(world, player, pos, state, te, tool);
    world.setBlockToAir(pos);
  }
  //end of fixing getdrops

  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    //?? TE null? http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677315-solved-tileentity-returning-null
    //http://www.minecraftforge.net/forum/index.php?/topic/38048-19-solved-blockgetdrops-and-tileentity/
    List<ItemStack> ret = new ArrayList<ItemStack>();
    Item item = Item.getItemFromBlock(this);//this.getItemDropped(state, rand, fortune);
    TileEntity ent = world.getTileEntity(pos);
    ItemStack stack = new ItemStack(item);
    if (ent != null && ent.hasCapability(CapabilityEnergy.ENERGY, null)) {
      IEnergyStorage handlerHere = ent.getCapability(CapabilityEnergy.ENERGY, null);
      if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
        EnergyStore storage = (EnergyStore) stack.getCapability(CapabilityEnergy.ENERGY, null);
        storage.setEnergyStored(handlerHere.getEnergyStored());
      }
    }
    ret.add(stack);
    return ret;
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (stack.hasCapability(CapabilityEnergy.ENERGY, null)) {
      IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
      TileEntityBatteryCell container = (TileEntityBatteryCell) world.getTileEntity(pos);
      container.setEnergyCurrent(storage.getEnergyStored());
    }
    else if (stack.getTagCompound() != null && world.getTileEntity(pos) instanceof TileEntityBatteryCell) {
      NBTTagCompound tags = stack.getTagCompound();
      int energy = tags.getInteger(ItemBlockBattery.ENERGY);
      TileEntityBatteryCell container = (TileEntityBatteryCell) world.getTileEntity(pos);
      container.setEnergyCurrent(energy);
    }
  }
}
