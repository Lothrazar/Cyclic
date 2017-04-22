package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.XpPylonTESR;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityXpPylon;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockXpPylon extends BlockBaseFacingInventory implements IHasRecipe, IBlockHasTESR {
  //block rotation in json http://www.minecraftforge.net/forum/index.php?topic=32753.0
  public BlockXpPylon() {
    super(Material.ROCK, ModGuiHandler.GUI_INDEX_XP);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.GLASS);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityXpPylon();
  }
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityXpPylon.class, new XpPylonTESR(0, 1));
  }
  @Override
  public boolean isSideSolid(IBlockState base_state, IBlockAccess world, BlockPos pos, EnumFacing side) {
    return side == EnumFacing.DOWN;
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        "vhv",
        "grg",
        "sis",
        'v', Blocks.VINE,
        'h', Blocks.HOPPER,
        'i', Items.GOLD_INGOT,
        'g', new ItemStack(Blocks.STAINED_GLASS, 1, EnumDyeColor.LIME.getMetadata()),
        'r', Items.FIRE_CHARGE,
        's', Items.NETHERBRICK);
  }
}
