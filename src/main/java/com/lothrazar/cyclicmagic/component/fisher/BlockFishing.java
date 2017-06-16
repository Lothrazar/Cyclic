package com.lothrazar.cyclicmagic.component.fisher;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFishing extends BlockBaseHasTile implements IHasRecipe, IBlockHasTESR {
  public BlockFishing() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.WOOD);
    this.setTickRandomly(true);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_FISHER);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityFishing();
  }
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFishing.class, new FishingTESR(0));
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (player.isSneaking()) {
      TileEntityFishing tile = (TileEntityFishing) world.getTileEntity(pos);
      if (world.isRemote && tile != null) {
        if (tile.isValidPosition() == false) {
          UtilChat.addChatMessage(player, "tile.block_fishing.invalidpos");
        }
        else if (tile.isEquipmentValid() == false) {
          UtilChat.addChatMessage(player, "tile.block_fishing.invalidequip");
        }
      }
    }
    else {
      return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
      //      if (world.isRemote) { return true; }
      //      int x = pos.getX(), y = pos.getY(), z = pos.getZ();
      //      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_FISHER, world, x, y, z);
      //      return true;
    }
    return false;
  }
  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "pwp",
        "wfw",
        "pwp",
        'w', Blocks.WEB,
        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()),
        'p', Blocks.TRAPPED_CHEST);
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "pwp",
        "wfw",
        "pwp",
        'w', Blocks.WEB,
        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
        'p', Blocks.TRAPPED_CHEST);
  }
}
