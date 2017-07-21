package com.lothrazar.cyclicmagic.component.hydrator;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.component.hydrator.TileEntityHydrator.Fields;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.net.PacketTileSetFieldClient;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class BlockHydrator extends BlockBaseHasTile implements IHasRecipe {
  public static ArrayList<IRecipe> recipeList = new ArrayList<IRecipe>();
  public BlockHydrator() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setTickRandomly(true);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_HYDRATOR);
    this.setTranslucent();
    this.addAllRecipes();
  }
  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return side == EnumFacing.DOWN;
  }
  private void addAllRecipes() {
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.FARMLAND)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.GRASS), new ItemStack(Blocks.GRASS_PATH)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.STONE, 1, 0), new ItemStack(Blocks.COBBLESTONE, 1, 0)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.HARDENED_CLAY), new ItemStack(Blocks.CLAY)));
    for (EnumDyeColor col : EnumDyeColor.values()) {
      recipeList.add(new RecipeHydrate(new ItemStack(Blocks.CONCRETE_POWDER, 1, col.getDyeDamage()), new ItemStack(Blocks.CONCRETE, 1, col.getDyeDamage())));
    }
    for (IRecipe r : recipeList) {
      RecipeRegistry.register(r);
    }
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityHydrator();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "rsr",
        "ogo",
        " o ",
        'o', Blocks.MOSSY_COBBLESTONE,
        'g', Blocks.IRON_BLOCK,
        's', Blocks.DROPPER,
        'r', Items.WATER_BUCKET);
  }
  @Override
  public int getComparatorInputOverride(IBlockState blockState, World world, BlockPos pos) {
    TileEntity te = world.getTileEntity(pos);
    if (te instanceof TileEntityHydrator) {
      float fill = ((TileEntityHydrator) te).getFillRatio();
      return (int) (15 * fill);
    }
    return 0;
  }
  /**
   * with thanks and gratitude @KnightMiner
   * https://github.com/KnightMiner/Ceramics/blob/1.11/src/main/java/knightminer/ceramics/blocks/BlockBarrel.java
   * 
   * 
   */
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    // check the TE
    TileEntityHydrator te = (TileEntityHydrator) world.getTileEntity(pos);
    boolean success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
    if (te != null) {
      if (world.isRemote == false) {
       ModCyclic.network.sendTo(new PacketTileSetFieldClient(pos, TileEntityHydrator.Fields.FLUID.ordinal(), te.getField(TileEntityHydrator.Fields.FLUID.ordinal())), (EntityPlayerMP) player);
      }
      //    if (te == null || !te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) { return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ); }
      //    IFluidHandler fluidHandler = te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
      int currentFluid = te.getField(Fields.FLUID.ordinal());
      // display the level of the barrel
      //    if (!world.isRemote && player.isSneaking()) {//so this is serverside.. AHA!!1... i see
      //      FluidStack fluid = fluidHandler.getTankProperties()[0].getContents();
      //      if (fluid == null) {
      //        UtilChat.sendStatusMessage(player, "cyclic.fluid.empty");
      //      }
      //      else {
      UtilChat.sendStatusMessage(player, UtilChat.lang("cyclic.fluid.amount") + currentFluid + ":isClient=" + world.isRemote);
      UtilChat.addChatMessage(player, UtilChat.lang("cyclic.fluid.amount") + currentFluid + ":isClient=" + world.isRemote);
      //      }
      //    }
     // te.markDirty();
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
}
