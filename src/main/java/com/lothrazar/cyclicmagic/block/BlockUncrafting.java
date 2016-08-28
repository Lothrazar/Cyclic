package com.lothrazar.cyclicmagic.block;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineUncrafter;
import com.lothrazar.cyclicmagic.gui.ModGuiHandler;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilUncraft;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockUncrafting extends BlockBaseFacingInventory implements IHasRecipe, IHasConfig {
  // http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockUncrafting() {
    super(Material.IRON, ModGuiHandler.GUI_INDEX_UNCRAFTING);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachineUncrafter();
  }
  @Override
  public boolean hasTileEntity() {
    return true;
  }
  @Override
  public boolean hasTileEntity(IBlockState state) {
    return hasTileEntity();
  }
  @Override
  public void addRecipe() {
    GameRegistry.addRecipe(new ItemStack(this),
        " r ",
        "fdf",
        " o ",
        'o', Blocks.OBSIDIAN, 'f', Blocks.FURNACE, 'r', Blocks.DROPPER, 'd', Blocks.DIAMOND_BLOCK);
  }
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.uncrafter;
    UtilUncraft.dictionaryFreedom = config.getBoolean("PickFirstMeta", category, true, "If you change this to true, then the uncrafting will just take the first of many options in any recipe that takes multiple input types.  For example, false means chests cannot be uncrafted, but true means chests will ALWAYS give oak wooden planks.");
    config.addCustomCategoryComment(category, "Here you can blacklist any thing, vanilla or modded.  Mostly for creating modpacks.  Input means you cannot uncraft it at all.  Output means it will not come out of a recipe.");
    // so when uncrafting cake, you do not get milk buckets back
    String def = "";
    String csv = config.getString("BlacklistInput", category, def, "Items that cannot be uncrafted; not allowed in the slots.  EXAMPLE : 'item.stick,tile.hayBlock,tile.chest'  ");
    // [item.stick, tile.cloth]
    UtilUncraft.blacklistInput = (List<String>) Arrays.asList(csv.split(","));
    if (UtilUncraft.blacklistInput == null) {
      UtilUncraft.blacklistInput = new ArrayList<String>();
    }
    def = "item.milk";
    csv = config.getString("BlacklistOutput", category, def, "Comma seperated items that cannot come out of crafting recipes.  For example, if milk is in here, then cake is uncrafted you get all items except the milk buckets.  ");
    UtilUncraft.blacklistOutput = (List<String>) Arrays.asList(csv.split(","));
    if (UtilUncraft.blacklistOutput == null) {
      UtilUncraft.blacklistOutput = new ArrayList<String>();
    }
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    String s = UtilChat.lang("tile.uncrafting_block.tooltip");
    tooltip.add(s);
  }
}
