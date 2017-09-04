package com.lothrazar.cyclicmagic.component.hydrator;
import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.component.hydrator.TileEntityHydrator.Fields;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    recipeList.add(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.TALLGRASS, 1, 1), new ItemStack(Blocks.DIRT), ItemStack.EMPTY, ItemStack.EMPTY },
        new ItemStack(Blocks.GRASS)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.GRASS), new ItemStack(Blocks.GRASS_PATH)));
    recipeList.add(new RecipeHydrate(new ItemStack(Items.BRICK), new ItemStack(Items.CLAY_BALL)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.STONE, 1, 0), new ItemStack(Blocks.COBBLESTONE, 1, 0)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.COBBLESTONE, 1, 0), new ItemStack(Blocks.MOSSY_COBBLESTONE, 1, 0)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.COBBLESTONE_WALL, 1, 0), new ItemStack(Blocks.COBBLESTONE_WALL, 1, 1)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.STONEBRICK, 1, 0), new ItemStack(Blocks.STONEBRICK, 1, 1)));
    recipeList.add(new RecipeHydrate(new ItemStack(Blocks.HARDENED_CLAY), new ItemStack(Blocks.CLAY)));
    //GRAVEL JUST FOR FUN EH
    //IDEAS: bones, rotten flesh, mushrooms, leather??
    recipeList.add(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT), new ItemStack(Blocks.DIRT), new ItemStack(Items.FLINT) },
        new ItemStack(Blocks.GRAVEL)));
    recipeList.add(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.DIRT, 1, 1), new ItemStack(Blocks.RED_MUSHROOM_BLOCK), new ItemStack(Blocks.BROWN_MUSHROOM_BLOCK), ItemStack.EMPTY },
        new ItemStack(Blocks.MYCELIUM)));
    recipeList.add(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW), new ItemStack(Blocks.SNOW) },
        new ItemStack(Blocks.ICE)));
    recipeList.add(new RecipeHydrate(
        new ItemStack[] { new ItemStack(Blocks.ICE), new ItemStack(Blocks.ICE), new ItemStack(Blocks.ICE), new ItemStack(Blocks.ICE) },
        new ItemStack(Blocks.PACKED_ICE)));
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
    FluidStack bucket = FluidUtil.getFluidContained(player.getHeldItem(hand));
    boolean success = false;
    if (bucket == null || bucket.getFluid() == FluidRegistry.WATER) {//THEN let fluid insert happen
      success = FluidUtil.interactWithFluidHandler(player, hand, world, pos, side);
    }
    if (te != null) {
      if (!world.isRemote) {
        int currentFluid = te.getField(Fields.FLUID.ordinal());
        UtilChat.sendStatusMessage(player, UtilChat.lang("cyclic.fluid.amount") + currentFluid);
      }
    }
    // otherwise return true if it is a fluid handler to prevent in world placement
    return success || FluidUtil.getFluidHandler(player.getHeldItem(hand)) != null || super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }
  //for fluid and itemblock storage
  @Override
  public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
    //?? TE null? http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677315-solved-tileentity-returning-null
    //http://www.minecraftforge.net/forum/index.php?/topic/38048-19-solved-blockgetdrops-and-tileentity/
    List<ItemStack> ret = new ArrayList<ItemStack>();
    Item item = Item.getItemFromBlock(this);//this.getItemDropped(state, rand, fortune);
    TileEntity ent = world.getTileEntity(pos);
    ItemStack stack = new ItemStack(item);
    if (ent != null && ent instanceof TileEntityHydrator) {
      TileEntityHydrator te = (TileEntityHydrator) ent;
      FluidStack fs = te.getCurrentFluidStack();
      if (fs != null) {
        UtilNBT.setItemStackNBTVal(stack, NBT_FLUIDSIZE, fs.amount);
        String resourceStr = FluidRegistry.getFluidName(fs.getFluid());
        UtilNBT.setItemStackNBTVal(stack, NBT_FLUIDTYPE, resourceStr);
      }
    }
    ret.add(stack);
    return ret;
  }
  //start of 'fixing getDrops to not have null tile entity', using pattern from forge BlockFlowerPot patch
  @Override
  public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
    if (willHarvest) { return true; } //If it will harvest, delay deletion of the block until after getDrops
    return super.removedByPlayer(state, world, pos, player, willHarvest);
  }
  @Override
  public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack tool) {
    super.harvestBlock(world, player, pos, state, te, tool);
    world.setBlockToAir(pos);
  }
  //end of fixing getdrops
  @Override
  public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
    if (stack.getTagCompound() != null) {
      NBTTagCompound tags = stack.getTagCompound();
      int fluidAmt = tags.getInteger(NBT_FLUIDSIZE);
      String resourceStr = tags.getString(NBT_FLUIDTYPE);
      TileEntityHydrator container = (TileEntityHydrator) worldIn.getTileEntity(pos);
      Fluid fluidObj = FluidRegistry.getFluid(resourceStr);//should never be null if fluidAmt > 0 
      if (fluidObj != null)
        container.fill(new FluidStack(fluidObj, fluidAmt), true);
    }
  }
}
