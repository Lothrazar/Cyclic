package com.lothrazar.cyclic.item;

import java.util.ArrayList;
import java.util.List;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.util.ChatUtil;
import com.lothrazar.cyclic.util.ItemStackUtil;
import com.lothrazar.cyclic.util.LevelWorldUtil;
import com.lothrazar.cyclic.util.ShapeUtil;
import com.lothrazar.cyclic.util.TagDataUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.common.Tags;

public class OreProspector extends ItemBaseCyclic {

  private static final int CD = 10;
  private static final String ORESIZE = "oresize";
  private static final String NBT_DIM = "dim";
  public static IntValue RANGE;
  public static IntValue HEIGHT;

  public OreProspector(Properties properties) {
    super(properties);
  }

  @Override
  public InteractionResultHolder<ItemStack> use(Level worldIn, Player player, InteractionHand handIn) {
    ItemStack itemstack = player.getItemInHand(handIn);
    if (player.isCrouching()) {
      ItemStackUtil.deleteTag(itemstack);
      player.swing(handIn);
    }
    return InteractionResultHolder.fail(itemstack);
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Player player = context.getPlayer();
    InteractionHand hand = context.getHand();
    ItemStack held = player.getItemInHand(hand);
    if (player.getCooldowns().isOnCooldown(held.getItem())) {
      return InteractionResult.PASS;
    }
    player.getCooldowns().addCooldown(held.getItem(), CD);
    //first delete old pos
    held.setTag(null);
    BlockPos pos = context.getClickedPos();
    List<BlockPos> shape = ShapeUtil.cubeSquareBase(pos.below(), RANGE.get(), HEIGHT.get());
    List<BlockPos> ores = new ArrayList<>();
    Level world = context.getLevel();
    for (BlockPos p : shape) {
      if (world.getBlockState(p).is(Tags.Blocks.ORES)) { //donzo
        ores.add(p);
      }
    }
    held.getOrCreateTag().putString(NBT_DIM, LevelWorldUtil.dimensionToString(player.level()));
    int i = 0;
    for (BlockPos p : ores) {
      CompoundTag tag = new CompoundTag();
      TagDataUtil.putBlockPos(tag, p);
      held.getTag().put("tag" + i, tag);
      i++;
    }
    held.getTag().putInt(ORESIZE, i);
    player.swing(hand);
    ItemStackUtil.damageItem(player, held);
    ChatUtil.sendStatusMessage(player, "" + i);
    return InteractionResult.SUCCESS;
  }

  public static ItemStack getIfHeld(Player player) {
    ItemStack heldItem = player.getMainHandItem();
    if (heldItem.getItem() instanceof OreProspector) {
      return heldItem;
    }
    heldItem = player.getOffhandItem();
    if (heldItem.getItem() instanceof OreProspector) {
      return heldItem;
    }
    return ItemStack.EMPTY;
  }

  public static ArrayList<BlockPosDim> getPosition(ItemStack item) {
    ArrayList<BlockPosDim> list = new ArrayList<BlockPosDim>();
    if (!item.hasTag() || !item.getTag().contains(ORESIZE)) {
      return list;
    }
    int size = item.getTag().getInt(ORESIZE);
    String dim = item.getTag().getString(NBT_DIM);
    for (int i = 0; i < size; i++) {
      BlockPos pos = TagDataUtil.getBlockPos(item.getTag().getCompound("tag" + i));
      list.add(new BlockPosDim(pos, dim, item.getTag()));
    }
    //    this.read  
    return list;
  }
}
