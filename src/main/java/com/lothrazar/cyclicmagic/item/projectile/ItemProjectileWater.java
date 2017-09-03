package com.lothrazar.cyclicmagic.item.projectile;
import java.util.ArrayList;
import java.util.List;
import com.google.common.collect.Lists;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.entity.projectile.EntityThrowableDispensable;
import com.lothrazar.cyclicmagic.item.base.BaseItemProjectile;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.item.base.BaseItemChargeScepter.ActionType;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilShape;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemProjectileWater extends BaseTool implements IHasRecipe {
  private static final int HEIGHT = 3;
  private static final int RADIUS = 4;
  public static enum ActionType {
    WATER, LAVA, GENERIC;
    private final static String NBT = "ActionType";
    private final static String NBTTIMEOUT = "timeout";
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT) + 1;
      if (type >= ActionType.values().length) {
        type = 0;
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
    public static ActionType getAction(ItemStack wand) {
      try {
        NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
        return ActionType.values()[tags.getInteger(NBT)];
      }
      catch (Exception e) {
        return WATER;
      }
    }
    public static String getName(ItemStack wand) {
      return "wand.liquid." + ActionType.getAction(wand).toString().toLowerCase();
    }
    public static void setTimeout(ItemStack wand) {
      UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, 15);//less than one tick
    }
    public static int getTimeout(ItemStack wand) {
      return UtilNBT.getItemStackNBT(wand).getInteger(NBTTIMEOUT);
    }
    public static void tickTimeout(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int t = tags.getInteger(NBTTIMEOUT);
      if (t > 0) {
        UtilNBT.getItemStackNBT(wand).setInteger(NBTTIMEOUT, t - 1);
      }
    }
  }
  public ItemProjectileWater() {
    super(2000);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    ItemStack stack = playerIn.getHeldItem(hand);
    List<BlockPos> shape = UtilShape.cubeFilled(playerIn.getPosition().down(), RADIUS, HEIGHT);
    List<BlockPos> queuedToUpdate = new ArrayList<BlockPos>();
    int success = 0;
    for (BlockPos pos : shape) {
      if (isValidForMode(stack, worldIn.getBlockState(pos))) {
        if (worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2))
          success++;
      }
    }
    if (success > 0) {
      UtilItemStack.damageItem(playerIn, playerIn.getHeldItem(hand), success);
      UtilSound.playSound(playerIn, SoundRegistry.pschew_fire);
      playerIn.swingArm(hand);
      //mimic what BlockSponge does : set block with status 2 so dont notify, then later notify them all at once
      //this prevents insta-fillins from neighbours as remvoed
      for (BlockPos blockpos2 : queuedToUpdate) {
        worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.AIR, false);
      }
    }
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
  }
  private boolean isValidForMode(ItemStack held, IBlockState state) {
    switch (ActionType.getAction(held)) {
      case GENERIC:
        return state.getMaterial() == Material.WATER || state.getMaterial() == Material.LAVA;
      case WATER:
        return state.getBlock() == Blocks.WATER || state.getBlock() == Blocks.FLOWING_WATER;
      case LAVA:
        return state.getBlock() == Blocks.LAVA || state.getBlock() == Blocks.FLOWING_LAVA;
    }
    return false;
  }
  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedOreRecipe(new ItemStack(this),
        "iii",
        " c ",
        "bcb",
        'b', Items.SUGAR,
        'c', Blocks.SPONGE,
        'i', "dyeBlue");
  }
  //toggling modes
  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held.getItem() == this) {
      //did we turn it off? is the visible timer still going?
      if (ActionType.getTimeout(held) > 0) { return; }
      ActionType.setTimeout(held);
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.dcoin, SoundCategory.PLAYERS, 0.1F);
      if (!player.getEntityWorld().isRemote) { // server side
        ActionType.toggle(held);
        UtilChat.addChatMessage(player, UtilChat.lang(ActionType.getName(held)));
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltip.add(TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack)));
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
  }
  //end toggling
}
