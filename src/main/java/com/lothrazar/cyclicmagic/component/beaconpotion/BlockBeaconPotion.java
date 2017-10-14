package com.lothrazar.cyclicmagic.component.beaconpotion;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.base.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.base.IBlockHasTESR;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBeaconPotion extends BlockBaseHasTile implements IBlockHasTESR, IHasRecipe, IHasConfig {
  public BlockBeaconPotion() {
    super(Material.IRON);
    this.setGuiId(ForgeGuiHandler.GUI_INDEX_BEACON);
  }
  @SideOnly(Side.CLIENT)
  public BlockRenderLayer getBlockLayer() {
    return BlockRenderLayer.CUTOUT;
  }
  public EnumBlockRenderType getRenderType(IBlockState state) {
    return EnumBlockRenderType.MODEL;
  }
  public boolean isOpaqueCube(IBlockState state) {
    return false;
  }
  public boolean isFullCube(IBlockState state) {
    return false;
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntityBeaconPotion();
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void initModel() {
    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBeaconPotion.class, new TileEntityBeaconPotionRenderer());
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "sss",
        "sgs",
        "ooo",
        'o', "blockEmerald",
        's', Blocks.END_STONE,
        'g', Items.NETHER_STAR);
  }
  @Override
  public void syncConfig(Configuration config) {
    TileEntityBeaconPotion.doesConsumePotions = config.getBoolean("PharosBeaconDoesConsumePotions", Const.ConfigCategory.modpackMisc, true, "Set to make Pharos Beacon free and perpetual, so it will not consume potions.  However if this set false, once it reads an effect from a potion, you must break and replace the beacon to wipe out its current effect. ");
    String[] defList = new String[] {
        "minecraft:instant_health",
        "minecraft:instant_damage",
        "minecraft:wither",
        "minecraft:poison"
    };
    String[] blacklist = config.getStringList("PharosBeaconBlacklist", Const.ConfigCategory.modpackMisc, defList, "Potions that are blacklisted from this beacon");
    TileEntityBeaconPotion.blacklist = Arrays.asList(blacklist);
  }
}
