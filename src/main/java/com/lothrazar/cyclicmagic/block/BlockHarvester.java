package com.lothrazar.cyclicmagic.block;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.block.tileentity.TileMachineHarvester;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockHarvester extends BlockBaseFacing implements IHasRecipe, IHasConfig {
  // dont use blockContainer !!
  // http://www.minecraftforge.net/forum/index.php?topic=31953.0
  public BlockHarvester() {
    super(Material.IRON);
    this.setHardness(3.0F).setResistance(5.0F);
    this.setSoundType(SoundType.METAL);
    this.setTickRandomly(true);
  }
  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileMachineHarvester();
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
    GameRegistry.addRecipe(new ItemStack(this), "rsr", "gbg", "ooo",
        'o', Blocks.OBSIDIAN,
        'g', Items.QUARTZ,
        's', Blocks.DISPENSER,
        'r', Items.EMERALD,
        'b', Items.DIAMOND);
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("tile.harvester_block.tooltip"));
  } 
  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    TileMachineHarvester.TIMER_FULL = config.getInt("HarvesterTime", category, 80, 10, 9999, "Number of ticks it takes to run one time, so lower is faster");
    TileMachineHarvester.HARVEST_RADIUS = config.getInt("HarvesterBlockRadius", Const.ConfigCategory.modpackMisc, 16, 4, 128, "Maximum radius of harvester area (remember its not centered on the block, it harvests in front)");
    }
}
