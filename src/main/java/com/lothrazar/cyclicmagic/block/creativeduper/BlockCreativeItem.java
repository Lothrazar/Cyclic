package com.lothrazar.cyclicmagic.block.creativeduper;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

public class BlockCreativeItem extends BlockBaseHasTile implements IContent, IHasConfig {

  public BlockCreativeItem() {
    super(Material.BARRIER);
  }

  @Override
  public String getContentName() {
    return "creative_items";
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileCreativeItem();
  }

  @Override
  public void syncConfig(Configuration config) {
    //default to false unlike most blocks
    enabled = config.getBoolean(getContentName(),
        Const.ConfigCategory.content, false,
        getContentName() + Const.ConfigCategory.contentDefaultText);
  }


  @Override
  public void register() {
    BlockRegistry.registerBlock(this, getContentName(), null);
    BlockRegistry.registerTileEntity(TileCreativeItem.class, Const.MODID + getContentName() + "_te");
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

}
