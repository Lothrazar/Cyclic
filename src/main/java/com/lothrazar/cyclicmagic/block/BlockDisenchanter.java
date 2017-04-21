package com.lothrazar.cyclicmagic.block;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.FishingTESR;
import com.lothrazar.cyclicmagic.block.tileentity.TileEntityDisenchanter;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
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
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDisenchanter extends BlockBaseHasTile implements IHasRecipe {
  public BlockDisenchanter() {
    super(Material.ROCK);
    this.setHardness(3F);
    this.setResistance(5F);
    this.setSoundType(SoundType.WOOD);
    this.setTickRandomly(true);
    this.setGuiId(ModGuiHandler.GUI_INDEX_DISENCH);
    this.setTranslucent();
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityDisenchanter();
  }
//  @SideOnly(Side.CLIENT)
//  public void initModel() {
//    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//    // Bind our TESR to our tile entity
//    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFishing.class, new FishingTESR(0));
//  }

  @Override
  public void addRecipe() {
//    GameRegistry.addRecipe(new ItemStack(this),
//        "pwp",
//        "wfw",
//        "pwp",
//        'w', Blocks.WEB,
//        'f', new ItemStack(Items.FISH, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()),
//        'p', Blocks.TRAPPED_CHEST);
   
  }
}
