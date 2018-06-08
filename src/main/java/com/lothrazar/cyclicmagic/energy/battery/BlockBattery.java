package com.lothrazar.cyclicmagic.energy.battery;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.block.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class BlockBattery extends BlockBaseHasTile implements IHasRecipe {

  public static final PropertyEnum<ModularAmountOverlay> AMOUNT = PropertyEnum.create("amount", ModularAmountOverlay.class);
  private boolean isCreative;

  enum ModularAmountOverlay implements IStringSerializable {
    AMOUNT_NONE("none"), AMOUNT_EMPTY("empty"), AMOUNT_G0("g0"), AMOUNT_G1("g1"), AMOUNT_G2("g2"), AMOUNT_G3("g3"), AMOUNT_G4("g4"), AMOUNT_G5("g5"), AMOUNT_G6("g6"), AMOUNT_G7("g7"), AMOUNT_R0("r0"), AMOUNT_R1("r1"), AMOUNT_R2("r2"), AMOUNT_R3("r3"), AMOUNT_R4("r4"), AMOUNT_R5("r5"), AMOUNT_R6("r6"), AMOUNT_R7("r7");

    private final String name;

    ModularAmountOverlay(String name) {
      this.name = name;
    }

    @Override
    public String getName() {
      return name;
    }
  }

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
        "cbc",
        "bab",
        "cbc",
        'c', Items.CLAY_BALL,
        'b', Blocks.GLASS,
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
  public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
    TileEntity tile = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
    if (tile instanceof TileEntityBattery) {
      ModularAmountOverlay p = ModularAmountOverlay.AMOUNT_G3;
      return state.withProperty(AMOUNT, p);
      //return state;
    }
    return super.getActualState(state, world, pos);
  }

  @Override
  protected BlockStateContainer createBlockState() {
    //  return super.createBlockState();
    return new BlockStateContainer(this, new IProperty[] { AMOUNT });
  }

  @Override
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState();
  }

  @Override
  public int getMetaFromState(IBlockState state) {
    return 0;
  }

  @Override
  public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
    return super.canRenderInLayer(state, layer);
    //     return layer == BlockRenderLayer.SOLID ;
    // return layer == BlockRenderLayer.CUTOUT;
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
