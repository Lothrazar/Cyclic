package com.lothrazar.cyclicmagic.item.gear;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.item.IHasClickToggle;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

@SuppressWarnings("incomplete-switch")
public class ItemPowerArmor extends ItemArmor implements IHasRecipe, IHasClickToggle {
  private static final float SNEAKSPEED = 0.077F;
  public static final String NBT_GLOW = Const.MODID + "_glow";
  public static final String NBT_STEP = Const.MODID + "_step";
  private final static String NBT_STATUS = "onoff";
  public ItemPowerArmor(ArmorMaterial material, EntityEquipmentSlot armorType) {
    super(material, 0, armorType);
  }
  @Override
  public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
    boolean isTurnedOn = this.isOn(itemStack);
    switch (this.armorType) {
      case CHEST:
        if (isTurnedOn)
          setSneakspeed(player);
      break;
      case FEET:
        if (isTurnedOn)
          setLiquidWalk(world, player);
      break;
      case HEAD:
        setGlowing(player, isTurnedOn);
        if (isTurnedOn)
          setNightVision(player);
      break;
      case LEGS:
        setStepHeight(player, isTurnedOn);
      break;
    }
  }
  private void setNightVision(EntityPlayer player) {
    player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 20 * Const.TICKS_PER_SEC, 0));
  }
  private void setSneakspeed(EntityPlayer player) {
    if (player.isSneaking() && player.moveForward > 0) {
      UtilEntity.speedupEntity(player, SNEAKSPEED);
    }
  }
  private void setLiquidWalk(World world, EntityPlayer player) {
    BlockPos belowPos = player.getPosition().down();
    if (world.containsAnyLiquid(new AxisAlignedBB(belowPos)) && world.isAirBlock(player.getPosition()) && player.motionY < 0
        && !player.isSneaking()) {// let them slip down into it when sneaking
      player.motionY = 0;// stop falling
      player.onGround = true; // act as if on solid ground
    }
  }
  public static void setStepHeight(EntityPlayer player, boolean on) {
    player.stepHeight = (on) ? 1.0F : 0.5F;
    player.getEntityData().setBoolean(NBT_STEP, on);
  }
  public static void checkIfLegsOff(EntityPlayer player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player, EntityEquipmentSlot.LEGS);
    if (player.getEntityData().getBoolean(ItemPowerArmor.NBT_STEP) &&
        (itemInSlot == null || !(itemInSlot instanceof ItemPowerArmor))) {
      //turn it off once, from the message
      setStepHeight(player, false);
    }
  }
  public static void checkIfHelmOff(EntityPlayer player) {
    Item itemInSlot = UtilPlayer.getItemArmorSlot(player, EntityEquipmentSlot.HEAD);
    if (player.getEntityData().getBoolean(ItemPowerArmor.NBT_GLOW) &&
        (itemInSlot == null || !(itemInSlot instanceof ItemPowerArmor))) {
      //turn it off once, from the message
      setGlowing(player, false);
    }
  }
  public static void setGlowing(EntityPlayer player, boolean hidden) {
    player.setGlowing(hidden);//hidden means dont render
    //flag it so we know the purple glow was from this item, not something else
    player.getEntityData().setBoolean(NBT_GLOW, hidden);
  }
  @SideOnly(Side.CLIENT)
  @Override
  public void addInformation(ItemStack held, World player, List<String> list,net.minecraft.client.util.ITooltipFlag par4) {
    list.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
    String onoff = this.isOn(held) ? "on" : "off";
    list.add(UtilChat.lang("item.cantoggle.tooltip.info") + UtilChat.lang("item.cantoggle.tooltip." + onoff));
    super.addInformation(held, player, list, par4);
  }
  @Override
  public IRecipe addRecipe() {
    switch (this.armorType) {
      case CHEST:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "p p", "oio", "ooo",
            'i', new ItemStack(Items.CHAINMAIL_CHESTPLATE, 1, OreDictionary.WILDCARD_VALUE),
            'o', "obsidian",
            'p', "dyePurple");
      case FEET:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "   ", "p p", "oio",
            'i', new ItemStack(Items.CHAINMAIL_BOOTS, 1, OreDictionary.WILDCARD_VALUE),
            'o', "obsidian",
            'p', "dyePurple");
      case HEAD:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "oio", "p p", "   ",
            'i', new ItemStack(Items.CHAINMAIL_HELMET, 1, OreDictionary.WILDCARD_VALUE),
            'o', "obsidian",
            'p', "dyePurple");
      case LEGS:
        return RecipeRegistry.addShapedRecipe(new ItemStack(this),
            "oio", "p p", "o o",
            'i', new ItemStack(Items.CHAINMAIL_LEGGINGS, 1, OreDictionary.WILDCARD_VALUE),
            'o', "obsidian",
            'p', "dyePurple");
    }
    return null;
  }
  public void toggle(EntityPlayer player, ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    int vnew = isOn(held) ? 0 : 1;
    tags.setInteger(NBT_STATUS, vnew);
  }
  public boolean isOn(ItemStack held) {
    NBTTagCompound tags = UtilNBT.getItemStackNBT(held);
    if (tags.hasKey(NBT_STATUS) == false) { return true;//default for newlycrafted//legacy items
    }
    return tags.getInteger(NBT_STATUS) == 1;
  }
}
