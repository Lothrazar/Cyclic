package com.lothrazar.cyclicmagic.item.locationgps;

import java.util.List;
import org.lwjgl.input.Keyboard;
import com.lothrazar.cyclicmagic.IContent;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.data.BlockPosDim;
import com.lothrazar.cyclicmagic.data.IHasRecipe;
import com.lothrazar.cyclicmagic.guide.GuideCategory;
import com.lothrazar.cyclicmagic.item.core.BaseItem;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLocationGps extends BaseItem implements IHasRecipe, IContent {

  private static final String NBT_SIDE = "side";
  private static final String NBT_DIM = "dim";
  private boolean enabled;

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack held = player.getHeldItem(hand);
    player.swingArm(hand);
    UtilNBT.setItemStackBlockPos(held, pos);
    UtilNBT.setItemStackNBTVal(held, NBT_DIM, player.dimension);
    UtilNBT.setItemStackNBTVal(held, NBT_SIDE, side.ordinal());
    UtilNBT.setItemStackNBTVal(held, NBT_SIDE + "facing", player.getHorizontalFacing().ordinal());
    UtilChat.sendStatusMessage(player, UtilChat.lang("item.location.saved")
        + UtilChat.blockPosToString(pos));
    //
    held.getTagCompound().setFloat("hitx", hitX);
    held.getTagCompound().setFloat("hity", hitY);
    held.getTagCompound().setFloat("hitz", hitZ);
    return EnumActionResult.SUCCESS;
  }

  public static BlockPosDim getPosition(ItemStack item) {
    BlockPos p = UtilNBT.getItemStackBlockPos(item);
    if (p == null) {
      return null;
    }
    BlockPosDim dim = new BlockPosDim(0, p, UtilNBT.getItemStackNBTVal(item, NBT_DIM), "");
    try {
      if (item.getTagCompound().hasKey(NBT_SIDE + "facing")) {
        dim.setSidePlayerFacing(EnumFacing.values()[UtilNBT.getItemStackNBTVal(item, NBT_SIDE + "facing")]);
      }
      if (item.getTagCompound().hasKey(NBT_SIDE)) {
        dim.setSide(EnumFacing.values()[UtilNBT.getItemStackNBTVal(item, NBT_SIDE)]);
      }
      if (item.getTagCompound().hasKey("hitx")) {
        NBTTagCompound tag = item.getTagCompound();
        Vec3d vec = new Vec3d(
            tag.getFloat("hitx"),
            tag.getFloat("hity"),
            tag.getFloat("hitz"));
        dim.setHitVec(vec);
      }
    }
    catch (Throwable e) {
      ModCyclic.logger.error("SIde error in GPS", e);
    }
    return dim;
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
    if (player.isSneaking()) {
      deletePosition(player, hand);
    }
    return super.onItemRightClick(worldIn, player, hand);
  }

  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World player, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    BlockPosDim dim = getPosition(stack);
    if (dim != null) {
      tooltip.add(dim.toString());
      if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
        if (dim.getSide() != null) {
          String side = "S: " + dim.getSide().toString().toUpperCase();
          tooltip.add(TextFormatting.DARK_GRAY
              + side);
        }
        if (dim.getSidePlayerFacing() != null) {
          String side = "F: " + dim.getSidePlayerFacing().toString().toUpperCase();
          tooltip.add(TextFormatting.DARK_GRAY
              + side);
        }
        if (dim.getHitVec() != null) {
          tooltip.add(TextFormatting.DARK_GRAY +
              "H: " + dim.getHitVec().toString());
        }
      }
    }
    else
      super.addInformation(stack, player, tooltip, advanced);
  }

  private void deletePosition(EntityPlayer player, EnumHand hand) {
    ItemStack held = player.getHeldItem(hand);
    held.setTagCompound(null);
    UtilChat.sendStatusMessage(player, UtilChat.lang("item.location.saved")
        + "---");
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " o ", " s ", " r ",
        'o', "dyeLightBlue",
        's', Items.PAPER,
        'r', "stickWood");
  }

  @Override
  public String getContentName() {
    return "card_location";
  }

  @Override
  public void syncConfig(Configuration config) {
    enabled = config.getBoolean(getContentName(), Const.ConfigCategory.items, true, Const.ConfigCategory.contentDefaultText + "  WARNING disabling this may cause other blocks to not function (wireless nodes)");
  }

  @Override
  public void register() {
    ItemRegistry.register(this, getContentName(), GuideCategory.ITEM);
  }

  @Override
  public boolean enabled() {
    return enabled;
  }
}
