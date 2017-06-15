package com.lothrazar.cyclicmagic.item;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.base.BaseTool;
import com.lothrazar.cyclicmagic.registry.PotionEffectRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemPlayerLauncher extends BaseTool implements IHasRecipe {
  private static final int COOLDOWN = 20;
  private static final int POTION_TIME = 10 * Const.TICKS_PER_SEC;
  private static final float POWER_UPSCALE = 5.18F;
  private static final float MAX_POWER = 6.7F;
  private static final float VERTICAL_FACTOR = 2.88F;
  private static final int TICKS_USING = 53000;
  public enum ActionType {
    FORWARD, REVERSE;
    private final static String NBT = "ActionType";
    private final static String NBTTIMEOUT = "timeout";
    public static void toggle(ItemStack wand) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
      int type = tags.getInteger(NBT);
      if (type == FORWARD.ordinal()) {
        type = REVERSE.ordinal();
      }
      else {
        type = FORWARD.ordinal();
      }
      tags.setInteger(NBT, type);
      wand.setTagCompound(tags);
    }
    public static String getName(ItemStack wand) {
      try {
        NBTTagCompound tags = UtilNBT.getItemStackNBT(wand);
        return "tool.action." + ActionType.values()[tags.getInteger(NBT)].toString().toLowerCase();
      }
      catch (Exception e) {
        return "tool.action." + FORWARD.toString().toLowerCase();
      }
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
    public static boolean isForward(ItemStack held) {
      NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
      int type = tags.getInteger(NBT);
      return type == FORWARD.ordinal();
    }
  }
  public ItemPlayerLauncher() {
    super(2000);
  }
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
    playerIn.setActiveHand(hand);
    return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
  }
  @Override
  public void onPlayerStoppedUsing(ItemStack stack, World world, EntityLivingBase entity, int chargeTimer) {
    //    if (entity.onGround == false) { return; }
    if (entity instanceof EntityPlayer == false) { return; }
    EntityPlayer player = (EntityPlayer) entity;
    if (player.getCooldownTracker().hasCooldown(stack.getItem())) { return; }
    int charge = this.getMaxItemUseDuration(stack) - chargeTimer;
    //bow konws how to say , how charged up am i, ok heres your power
    float power = Math.min(MAX_POWER, ItemBow.getArrowVelocity(charge) * POWER_UPSCALE);
    Vec3d vec = player.getLookVec().normalize();
    int rev = (ActionType.isForward(stack)) ? 1 : -1;
    power *= rev;//flip it the other way if we are going backwards
    player.addVelocity(vec.xCoord * power,
        vec.yCoord * power / VERTICAL_FACTOR,
        vec.zCoord * power);
    player.addPotionEffect(new PotionEffect(PotionEffectRegistry.BOUNCE, POTION_TIME, 0));
    UtilSound.playSound(player, player.getPosition(), SoundRegistry.bwoaaap, SoundCategory.PLAYERS, 0.1F);
    player.getCooldownTracker().setCooldown(stack.getItem(), COOLDOWN);
    super.onUse(stack, player, world, EnumHand.MAIN_HAND);
  }
  @Override
  public void onUpdate(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
    ActionType.tickTimeout(stack);
  }
  @SubscribeEvent
  public void onHit(PlayerInteractEvent.LeftClickBlock event) {
    EntityPlayer player = event.getEntityPlayer();
    ItemStack held = player.getHeldItem(event.getHand());
    if (held != null && held.getItem() == this) {
      //did we turn it off? is the visible timer still going?
      if (ActionType.getTimeout(held) > 0) { return; }
      ActionType.setTimeout(held);
      event.setCanceled(true);
      UtilSound.playSound(player, player.getPosition(), SoundRegistry.dcoin, SoundCategory.PLAYERS, 0.3F);
      if (!player.getEntityWorld().isRemote) { // server side
        ActionType.toggle(held);
        UtilChat.addChatMessage(player, UtilChat.lang(ActionType.getName(held)));
      }
    }
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    tooltip.add(TextFormatting.GREEN + UtilChat.lang(ActionType.getName(stack)));
    super.addInformation(stack, playerIn, tooltip, advanced);
  }
  @Override
  public int getMaxItemUseDuration(ItemStack stack) {
    return TICKS_USING;//bow has 72000
  }
  @Override
  public EnumAction getItemUseAction(ItemStack stack) {
    return EnumAction.BOW;//make it use cooldown
  }
  @Override
  public IRecipe addRecipe() {
    return GameRegistry.addShapedRecipe(new ItemStack(this),
        "rsq",
        " rs",
        "t r",
        't', Items.STRING,
        'r', Items.GLOWSTONE_DUST,
        's', Items.SLIME_BALL,
        'q', Items.QUARTZ);
  }
}
