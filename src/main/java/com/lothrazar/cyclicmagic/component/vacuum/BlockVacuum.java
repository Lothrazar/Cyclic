package com.lothrazar.cyclicmagic.component.vacuum;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.IBlockHasTESR;
import com.lothrazar.cyclicmagic.block.base.BlockBaseFacingInventory;
import com.lothrazar.cyclicmagic.block.base.MachineTESR;
import com.lothrazar.cyclicmagic.component.harvester.TileEntityHarvester;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockVacuum extends BlockBaseFacingInventory implements IHasRecipe, IBlockHasTESR {
  //block rotation in json http://www.minecraftforge.net/forum/index.php?topic=32753.0
  public BlockVacuum() {
    super(Material.ROCK, ForgeGuiHandler.GUI_INDEX_VACUUM);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.STONE);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityVacuum();
  }
 

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "vhv",
        "grg",
        "sis",
        'v', "vine",
        'h', Blocks.DROPPER,
        'i', "ingotGold",
        'g', "dyeLime",
        'r', Items.FIRE_CHARGE,
        's', "ingotBrickNether");
  }

  @SideOnly(Side.CLIENT)
  public void initModel() {
     ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//    // Bind our TESR to our tile entity
     ClientRegistry.bindTileEntitySpecialRenderer(TileEntityVacuum.class, new MachineTESR(this.getUnlocalizedName()));
  }
 
}
