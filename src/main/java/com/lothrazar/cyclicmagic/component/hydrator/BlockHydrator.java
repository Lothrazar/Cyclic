package com.lothrazar.cyclicmagic.component.hydrator;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.component.hydrator.TileEntityHydrator.Fields;
import com.lothrazar.cyclicmagic.component.pylonexp.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.component.pylonexp.XpPylonTESR;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

public class BlockHydrator extends BlockBaseHasTile implements IHasRecipe, IBlockHasTESR {
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
   // RecipeSorter.register(Const.MODID + ":recipe_hydrator", RecipeHydrate.class, Category.SHAPELESS, "");
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.DIRT), new ItemStack(Blocks.FARMLAND)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.GRASS), new ItemStack(Blocks.GRASS_PATH)));
    recipeList.add(new RecipeHydrate(new ItemStack(Items.BRICK), new ItemStack(Items.CLAY_BALL)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.STONE, 1, 0), new ItemStack(Blocks.COBBLESTONE, 1, 0)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.COBBLESTONE, 1, 0), new ItemStack(Blocks.MOSSY_COBBLESTONE, 1, 0)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.COBBLESTONE_WALL, 1, 0), new ItemStack(Blocks.COBBLESTONE_WALL, 1, 1)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.STONEBRICK, 1, 0), new ItemStack(Blocks.STONEBRICK, 1, 1)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.HARDENED_CLAY), new ItemStack(Blocks.CLAY)));
   
    //GRAVEL JUST FOR FUN EH
    recipeList.add(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT), new ItemStack(Items.FLINT) }, 
        new ItemStack(Blocks.GRAVEL)));
   
    recipeList.add(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW) }, new ItemStack(Blocks.ICE)));
    for (EnumDyeColor col : EnumDyeColor.values()) {
      recipeList.add(new RecipeHydrate(new ItemStack(Blocks.CONCRETE_POWDER, 1, col.getMetadata()), new ItemStack(Blocks.CONCRETE, 1, col.getMetadata())));
    }
    for (EnumDyeColor col : EnumDyeColor.values()) {
      if (col.getMetadata() != EnumDyeColor.WHITE.getMetadata())
        recipeList.add(new RecipeHydrate(new ItemStack(Blocks.WOOL, 1, col.getMetadata()), new ItemStack(Blocks.WOOL, 1, EnumDyeColor.WHITE.getMetadata())));
    }
    //they didnt use metadata for glazed because of facing direction i guess
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.BLACK_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLACK.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.BLUE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BLUE.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.BROWN_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.BROWN.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.CYAN_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.CYAN.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.GREEN_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.GREEN.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.LIGHT_BLUE.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.LIME_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.LIME.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.MAGENTA_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.MAGENTA.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.ORANGE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.ORANGE.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.PINK_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.PINK.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.PURPLE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.PURPLE.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.RED_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.RED.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.SILVER_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.SILVER.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.WHITE_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.WHITE.getMetadata())));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.YELLOW_GLAZED_TERRACOTTA), new ItemStack(Blocks.STAINED_HARDENED_CLAY, 1, EnumDyeColor.YELLOW.getMetadata())));
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
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHydrator.class, new HydratorTESR(0, 4));
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
      if (!world.isRemote) {
        int currentFluid = te.getField(Fields.FLUID.ordinal());
        UtilChat.sendStatusMessage(player, UtilChat.lang("cyclic.fluid.amount") + currentFluid);
      }
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
}
