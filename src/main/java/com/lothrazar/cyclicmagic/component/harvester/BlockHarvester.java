package com.lothrazar.cyclicmagic.component.harvester;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.base.MachineTESR;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHarvester extends BlockBaseFacingInventory implements IHasRecipe, IBlockHasTESR {
  // dont use blockContainer !! http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockHarvester() {
    super(Material.IRON, ForgeGuiHandler.GUI_INDEX_HARVESTER);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityHarvester();
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this), "rsr", "gbg", "ooo",
        'o', "obsidian",
        'g', "gemQuartz",
        's', Blocks.DISPENSER,
        'r', "gemEmerald",
        'b', "gemDiamond");
  }
  @SideOnly(Side.CLIENT)
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    // Bind our TESR to our tile entity
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHarvester.class, new MachineTESR(this));
  }
}
