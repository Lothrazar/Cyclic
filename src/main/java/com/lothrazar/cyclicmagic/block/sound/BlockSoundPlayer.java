package com.lothrazar.cyclicmagic.block.sound;

import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.block.core.BlockBaseHasTile;
import com.lothrazar.cyclicmagic.block.password.IPlayerClaimed;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.gui.ForgeGuiHandler;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockSoundPlayer extends BlockBaseHasTile implements IHasRecipe, IContent {

  public static boolean playToEverybody = true;

  public BlockSoundPlayer() {
    super(Material.ROCK);
    super.setGuiId(ForgeGuiHandler.GUI_INDEX_SOUNDPL);
  }

  @Override
  public TileEntity createTileEntity(World worldIn, IBlockState state) {
    return new TileEntitySoundPlayer();
  }

  @Override
  public String getName() {
    return "sound_player";
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean(getName(), Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    BlockSoundPlayer.playToEverybody = config.getBoolean(getName() + "_everybody", Const.ConfigCategory.modpackMisc, true,
        "If true, then this block plays sound to everybody just like a record player.  "
            + "If false, it only plays for the player who most recently opened the block.  "
            + "This is useful for servers if players are using this block to grief others, "
            + "you can set it to false and only the block owner will hear it.  ");
  }

  @Override
  public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    TileEntity te = world.getTileEntity(pos);
    if (te != null && te instanceof IPlayerClaimed) {
      ((IPlayerClaimed) te).toggleClaimedHash(player);
    }
    return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
  }

  @Override
  public void register() {
    BlockRegistry.registerBlock(this, getName(), GuideCategory.BLOCK);
    GameRegistry.registerTileEntity(TileEntitySoundPlayer.class, getName() + "_te");
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
