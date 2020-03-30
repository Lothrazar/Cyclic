package com.lothrazar.cyclic.item;

import java.util.List;
import javax.annotation.Nullable;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.net.PacketSwapBlock;
import com.lothrazar.cyclic.registry.PacketRegistry;
import com.lothrazar.cyclic.util.UtilChat;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BuilderItem extends ItemBase {

  public enum ActionType {

    SINGLE, X3, X5, X7, X9, X91, X19;

    private static final String NBTBLOCKSTATE = "blockstate";
    private final static String NBT = "ActionType";
    private final static String NBTTIMEOUT = "timeout";

    public static int getTimeout(ItemStack wand) {
      return wand.getOrCreateTag().getInt(NBTTIMEOUT);
    }

    public static void setTimeout(ItemStack wand) {
      wand.getOrCreateTag().putInt(NBTTIMEOUT, 15);//less than one tick
    }

    public static void tickTimeout(ItemStack wand) {
      CompoundNBT tags = wand.getOrCreateTag();
      int t = tags.getInt(NBTTIMEOUT);
      if (t > 0) {
        wand.getOrCreateTag().putInt(NBTTIMEOUT, t - 1);
      }
    }

    public static int get(ItemStack wand) {
      if (wand.isEmpty()) {
        return 0;
      }
      CompoundNBT tags = wand.getOrCreateTag();
      return tags.getInt(NBT);
    }

    public static String getName(ItemStack wand) {
      try {
        CompoundNBT tags = wand.getOrCreateTag();
        return "tool.action." + ActionType.values()[tags.getInt(NBT)].toString().toLowerCase();
      }
      catch (Exception e) {
        return "tool.action." + SINGLE.toString().toLowerCase();
      }
    }

    public static void toggle(ItemStack wand) {
      CompoundNBT tags = wand.getOrCreateTag();
      int type = tags.getInt(NBT);
      type++;
      if (type >= ActionType.values().length) {
        type = SINGLE.ordinal();
      }
      tags.putInt(NBT, type);
      wand.setTag(tags);
    }

    public static void setBlockState(ItemStack wand, CompoundNBT encoded) {
      System.out.println(encoded);
      wand.getOrCreateTag().put(NBTBLOCKSTATE, encoded);
    }

    @Nullable
    public static BlockState getBlockState(ItemStack wand) {
      if (!wand.getOrCreateTag().contains(NBTBLOCKSTATE)) {
        return null;
      }
      return NBTUtil.readBlockState(wand.getOrCreateTag().getCompound(NBTBLOCKSTATE));
    }
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    String msg = TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack));
    tooltip.add(new TranslationTextComponent(msg));
    //    String bname = ActionType.ge
    BlockState target = ActionType.getBlockState(stack);
    String block = "scepter.cyclic.nothing";
    if (target != null) {
      block = target.getBlock().getTranslationKey();
    }
    tooltip.add(new TranslationTextComponent(TextFormatting.AQUA + UtilChat.lang(block)));
    super.addInformation(stack, worldIn, tooltip, flagIn);
  }

  public BuilderItem(Properties properties) {
    super(properties.maxStackSize(1));
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    //
    ItemStack stack = context.getItem();
    BlockPos pos = context.getPos();
    Direction side = context.getFace();
    //TODO: INSIDE building no offset
    // on top of selected = do offset
    //    if (side != null) {
    //      pos = pos.offset(side);
    //    }
    if (context.getWorld().isRemote) {
      ActionType type = getActionType(stack);
      PacketSwapBlock message = new PacketSwapBlock(pos, type, side, context.getHand());
      PacketRegistry.INSTANCE.sendToServer(message);
    }
    return super.onItemUse(context);
  }

  public static ActionType getActionType(ItemStack stack) {
    ActionType type = ActionType.values()[ActionType.get(stack)];
    return type;
  }

  public static ItemStack getIfHeld(PlayerEntity player) {
    ItemStack heldItem = player.getHeldItemMainhand();
    if (heldItem.getItem() instanceof BuilderItem) {
      return heldItem;
    }
    heldItem = player.getHeldItemOffhand();
    if (heldItem.getItem() instanceof BuilderItem) {
      return heldItem;
    }
    return ItemStack.EMPTY;
  }
}
