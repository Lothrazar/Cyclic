package com.lothrazar.cyclicmagic.item.tool;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.item.BaseTool;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolSpelunker extends BaseTool implements IHasRecipe, IHasConfig {
  private static final int DURABILITY = 2000;
  private static final int COOLDOWN = 12;
  private static int range = 32;
  public ItemToolSpelunker() {
    super(DURABILITY);
  }
  @Override
  public EnumActionResult onItemUse( EntityPlayer player, World worldObj, BlockPos posIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    
    if (side == null || posIn == null) { return super.onItemUse( player, worldObj, posIn, hand, side, hitX, hitY, hitZ); }
    //    boolean showOdds = player.isSneaking();
    boolean found = false;
    if (!worldObj.isRemote) {
      EnumFacing direction = side.getOpposite();
      BlockPos pos = posIn.offset(direction);
      BlockPos current = pos;
      for (int i = 1; i <= range; i++) {
        current = current.offset(direction);
        if (worldObj.isAirBlock(current)) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.cave") + i);
          found = true;
        }
        else if (worldObj.getBlockState(current) == Blocks.WATER.getDefaultState()
            || worldObj.getBlockState(current) == Blocks.FLOWING_WATER.getDefaultState()) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.water") + i);
          found = true;
        }
        else if (worldObj.getBlockState(current) == Blocks.LAVA.getDefaultState()
            || worldObj.getBlockState(current) == Blocks.FLOWING_LAVA.getDefaultState()) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.lava") + i);
          found = true;
        }
        if (found) {
          break;//stop looping
        }
      }
      if (found == false) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool_spelunker.none") + range);
      }
    }
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse( player, worldObj, posIn, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        " sg",
        " bs",
        "b  ",
        'b', new ItemStack(Items.STICK),
        's', new ItemStack(Items.FLINT),
        'g', new ItemStack(Blocks.STAINED_GLASS, 1, EnumDyeColor.BLUE.getMetadata()));
  }
  @Override
  public void syncConfig(Configuration config) {
    range = config.getInt("CavefinderRange", Const.ConfigCategory.modpackMisc, 32, 2, 256, "Block Range it will search onclick");
  }
}
