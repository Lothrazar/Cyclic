package com.lothrazar.cyclicmagic.item.bauble;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.base.BaseCharm;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilPlaceBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

public class ItemAutoTorch extends BaseCharm implements IHasRecipe, IHasConfig {
  private static final int durability = 256;
  private static int lightLimit = 7;
  public ItemAutoTorch() {
    super(durability);
    this.repairedBy = new ItemStack(Blocks.TORCH);
  }
  @Override
  public void onTick(ItemStack stack, EntityPlayer living) {
    if (!this.canTick(stack)) {
      return;
    }
    World world = living.world;
    BlockPos pos = living.getPosition();
    if (world.getLight(pos, true) < lightLimit
        && living.isSpectator() == false
        && world.isSideSolid(pos.down(), EnumFacing.UP)
        && world.isAirBlock(pos)) { // dont overwrite liquids 
      if (UtilPlaceBlocks.placeStateSafe(world, living, pos, Blocks.TORCH.getDefaultState())) {
        super.damageCharm(living, stack);
      }
    }
  }
  @Override
  public IRecipe addRecipe() {
    RecipeRegistry.addShapelessRecipe(new ItemStack(this), new ItemStack(this, 1, OreDictionary.WILDCARD_VALUE), "blockCoal", "blockCoal", "blockCoal");
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        "cic",
        " i ",
        "cic",
        'c', "blockCoal",
        'i', Blocks.IRON_BARS);
  }
  @Override
  public void syncConfig(Configuration config) {
    lightLimit = config.getInt("AutoTorchLightLevel", Const.ConfigCategory.modpackMisc, 7, 1, 14, "At which light level will auto torch place.  Set to 7 means it will place a torch 7 or darker.  (15 is full light, 0 is full dark)");
  }
}
