package com.lothrazar.cyclicmagic.block.sound;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockSoundPlayer extends BlockBaseHasTile implements IHasRecipe, IContent {

  //  public static boolean playToEverybody;
  public BlockSoundPlayer() {
    super(Material.ROCK);
    super.setGuiId(ForgeGuiHandler.GUI_INDEX_SOUNDPL);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntitySoundPlayer();
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean("sound_player", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    //    BlockSoundPlayer.playToEverybody = config.getBoolean("sound_player_everybody", Const.ConfigCategory.modpackMisc, true, "If true, then this block plays sound to everybody just like a record player.  "
    //        + "If false, it only plays for the owned player.  This is useful for servers if players are using this block to grief others, you can set it to false and only the block owner will hear it.  ");
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, "sound_player", GuideCategory.BLOCK);
    GameRegistry.registerTileEntity(TileEntitySoundPlayer.class, "sound_player_te");
  }

  private boolean enabled;

  @Override
  public boolean enabled() {
    return enabled;
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "sss",
        "sns",
        "sss",
        's', Blocks.NOTEBLOCK,
        'n', Blocks.JUKEBOX);
  }
}
