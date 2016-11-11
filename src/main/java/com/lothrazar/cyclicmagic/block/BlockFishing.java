package com.lothrazar.cyclicmagic.block;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.ModMain;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFishing extends Block  implements IHasRecipe {
  public BlockFishing() {
    super(Material.WOOD);
    this.setHardness(3F);
    this.setResistance(5F);
    //    this.setStepSound(soundTypeWood);
    this.setTickRandomly(true);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityFishing();//"tile.block_fishing.name"
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
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
      if (world.isRemote) { return true; }
      int x = pos.getX(), y = pos.getY(), z = pos.getZ();
      player.openGui(ModMain.instance, ModGuiHandler.GUI_INDEX_FISHER, world, x, y, z);
      return true;
    }
    return false;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "pwp",
        "wfw",
        "pwp",
        'w', Blocks.WEB,
        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()),
        'p', Blocks.TRAPPED_CHEST);
    GameRegistry.addRecipe(new ItemStack(this),
        "pwp",
        "wfw",
        "pwp",
        'w', Blocks.WEB,
        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()),
        'p', Blocks.TRAPPED_CHEST);
  }
  @Override
  public boolean isOpaqueCube(IBlockState state) {
    return false; // http://greyminecraftcoder.blogspot.ca/2014/12/transparent-blocks-18.html
  }
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
}
