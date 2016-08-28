package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemToolProspector extends BaseTool implements IHasRecipe, IHasConfig {
  private static final int DURABILITY = 2000;
  private static final int COOLDOWN = 12;
  private static int range = 16;
  public ItemToolProspector() {
    super(DURABILITY);
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if (side == null || pos == null) { return super.onItemUse(stack, player, worldObj, pos, hand, side, hitX, hitY, hitZ); }
    //    boolean showOdds = player.isSneaking();
    boolean found = false;
    if (!worldObj.isRemote) {
      EnumFacing direction = side.getOpposite();
      BlockPos current = pos;
      IBlockState at = worldObj.getBlockState(current);
      for (int i = 0; i <= range; i++) {
        if (at != null && at.getBlock() != null && at.getBlock() instanceof BlockOre) {
          UtilChat.addChatMessage(player, UtilChat.lang("tool_prospector.found") + at.getBlock().getLocalizedName());
          found = true;
          break;
        }
        current = current.offset(direction);
        at = worldObj.getBlockState(current);
      }
      if (found == false) {
        UtilChat.addChatMessage(player, UtilChat.lang("tool_prospector.none") + range);
      }
    }
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(stack, player, worldObj, pos, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        " sg",
        " bs",
        "b  ",
        'b', new ItemStack(Items.BLAZE_ROD),
        's', new ItemStack(Items.DIAMOND),
        'g', new ItemStack(Blocks.STAINED_GLASS, 1, EnumDyeColor.LIGHT_BLUE.getMetadata()));
  }
  @Override
  public void syncConfig(Configuration config) {
    ItemToolProspector.range = config.getInt("ProspectorRange", Const.ConfigCategory.modpackMisc, 16, 2, 256, "Block Range it will search onclick");
  }
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(UtilChat.lang("item.tool_prospector.tooltip"));
  }
}
