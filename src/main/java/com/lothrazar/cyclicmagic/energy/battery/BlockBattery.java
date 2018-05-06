package com.lothrazar.cyclicmagic.energy.battery;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockBattery extends BlockBaseHasTile implements IHasRecipe {

  private boolean isCreative;

  public BlockBattery(boolean creat) {
    super(Material.ROCK);
    this.isCreative = creat;
    if (isCreative == false)
      this.setGuiId(ForgeGuiHandler.GUI_INDEX_BATTERY);
  }

  @Override
  public IRecipe addRecipe() {
    if (isCreative)
      return RecipeRegistry.addShapelessOreRecipe(new ItemStack(this),
          new ItemStack(Blocks.COMMAND_BLOCK), new ItemStack(Blocks.BARRIER));
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        "aaa",
        "aaa",
        "aaa",
        'a', "blockRedstone");
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    if (isCreative)
      return new TileEntityBatteryInfinite();
    else
      return new TileEntityBattery();
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
      NBTTagInt tags = (NBTTagInt) CapabilityEnergy.ENERGY.writeNBT(handlerHere, null);
      NBTTagCompound lol = new NBTTagCompound();
      lol.setInteger("energy", tags.getInt());//NBTTagInt
      lol.setInteger("energyMAX", handlerHere.getMaxEnergyStored());
      stack.setTagCompound(lol);

    }
    ret.add(stack);
    return ret;
  }

  @Override
  public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (stack.getTagCompound() != null && world.getTileEntity(pos) instanceof TileEntityBattery) {
      NBTTagCompound tags = stack.getTagCompound();

      int energy = tags.getInteger("energy");

      //  IEnergyStorage handlerHere =  world.getTileEntity(pos).getCapability(CapabilityEnergy.ENERGY, null);
      TileEntityBattery container = (TileEntityBattery) world.getTileEntity(pos);
      container.setEnergyCurrent(energy);

    }
  }
}
