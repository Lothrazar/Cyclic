package com.lothrazar.cyclicmagic.block;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityFishing;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockFishing extends BlockBaseHasTile implements IHasRecipe {
  public BlockFishing() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.WOOD);
    this.setTickRandomly(true);
    this.setGuiId(ModGuiHandler.GUI_INDEX_FISHER);
    this.setTranslucent();
    this.setTooltip("tile.block_fishing.tooltip");// TODO: replace name with tooltip "tile.block_fishing.name"
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityFishing();
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
      return super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
      //      if (world.isRemote) { return true; }
      //      int x = pos.getX(), y = pos.getY(), z = pos.getZ();
      //      player.openGui(ModCyclic.instance, ModGuiHandler.GUI_INDEX_FISHER, world, x, y, z);
      //      return true;
    }
    return false;
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
}
