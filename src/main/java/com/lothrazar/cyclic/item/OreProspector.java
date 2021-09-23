package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.data.BlockPosDim;
import com.lothrazar.cyclic.util.UtilItemStack;
import com.lothrazar.cyclic.util.UtilNBT;
import com.lothrazar.cyclic.util.UtilShape;
import com.lothrazar.cyclic.util.UtilWorld;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class OreProspector extends ItemBase {

  private static final String ORESIZE = "oresize";
  private static final String NBT_DIM = "dim";

  public OreProspector(Properties properties) {
    super(properties);
  }

  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity player, Hand handIn) {
    ItemStack itemstack = player.getHeldItem(handIn);
    if (player.isCrouching()) {
      UtilItemStack.deleteTag(itemstack);
      player.swingArm(handIn);
    }
    return ActionResult.resultFail(itemstack);
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    ItemStack held = player.getHeldItem(hand);
    if (player.getCooldownTracker().hasCooldown(held.getItem())) {
      return ActionResultType.PASS;
    }
    player.getCooldownTracker().setCooldown(held.getItem(), 10);
    BlockPos pos = context.getPos();
    int radius = 32;
    List<BlockPos> shape = UtilShape.cubeSquareBase(pos.down(), radius, 2);
    List<BlockPos> ores = new ArrayList<>();
    World world = context.getWorld();
    for (BlockPos p : shape) {
      if (world.getBlockState(p).isIn(Tags.Blocks.ORES)) {
        //donzo
        ores.add(p);
      }
    }
    held.getOrCreateTag().putString(NBT_DIM, UtilWorld.dimensionToString(player.world));
    int i = 0;
    for (BlockPos p : ores) {
      CompoundNBT tag = new CompoundNBT();
      UtilNBT.putBlockPos(tag, p);
      held.getTag().put("tag" + i, tag);
      i++;
    }
    held.getTag().putInt(ORESIZE, i);
    player.swingArm(hand);
    UtilItemStack.damageItem(player, held);
    //    UtilChat.sendStatusMessage(player, UtilChat.lang("item.location.saved")      + UtilChat.blockPosToString(pos));
    return ActionResultType.SUCCESS;
  }

  public static ArrayList<BlockPosDim> getPosition(ItemStack item) {
    ArrayList<BlockPosDim> list = new ArrayList<BlockPosDim>();
    if (!item.hasTag() || !item.getTag().contains(ORESIZE)) {
      return list;
    }
    int size = item.getTag().getInt(ORESIZE);
    String dim = item.getTag().getString(NBT_DIM);
    for (int i = 0; i < size; i++) {
      BlockPos pos = UtilNBT.getBlockPos(item.getTag().getCompound("tag" + i));
      list.add(new BlockPosDim(pos, dim, item.getTag()));
    }
    //    this.read  
    return list;
  }
}
