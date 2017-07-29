package com.lothrazar.cyclicmagic.enchantment;
import java.util.ArrayList;
import java.util.Arrays;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.net.PacketPlayerFalldamage;
import com.lothrazar.cyclicmagic.registry.GuideRegistry;
import com.lothrazar.cyclicmagic.registry.SoundRegistry;
import com.lothrazar.cyclicmagic.util.UtilEntity;
import com.lothrazar.cyclicmagic.util.UtilItemStack;
import com.lothrazar.cyclicmagic.util.UtilNBT;
import com.lothrazar.cyclicmagic.util.UtilParticle;
import com.lothrazar.cyclicmagic.util.UtilSound;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EnchantLaunch extends EnchantBase {
  private static final float power = 1.05F;
  private static final int rotationPitch = 70;
  private static final int cooldown = 5 * 20;
  private static final String NBT_USES = "launchuses";
  public EnchantLaunch() {
    super("launch", Rarity.COMMON, EnumEnchantmentType.ARMOR_FEET, new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET });
    GuideRegistry.register(this, new ArrayList<String>(Arrays.asList(cooldown + "")));
  }
  @Override
  public int getMaxLevel() {
    return 5;
  }
  @Override
  public boolean canApply(ItemStack stack) {
    //anything that goes on your feet
    boolean yes = stack.getItem() == Items.BOOK ||
        (stack.getItem() instanceof ItemArmor)
            && ((ItemArmor) stack.getItem()).armorType == EntityEquipmentSlot.FEET;
    return yes;
  }
  @Override
  public boolean canApplyAtEnchantingTable(ItemStack stack) {
    return this.canApply(stack);
  }
  @SubscribeEvent
  public void onEntityUpdate(LivingUpdateEvent event) {
    if (event.getEntity() instanceof EntityPlayer) {
      EntityPlayer p = (EntityPlayer) event.getEntity();
      ItemStack feet = p.getItemStackFromSlot(EntityEquipmentSlot.FEET);
      if (feet.isEmpty()) { return; }
      //if you are on the ground (or not airborne, should be same thing
      if ((p.isAirBorne == false || p.onGround) &&
          UtilNBT.getItemStackNBTVal(feet, NBT_USES) > 0) {
        //you have landed on the ground, dont count previous jumps
        UtilNBT.setItemStackNBTVal(feet, NBT_USES, 0);
      }
    }
  }
  @SideOnly(Side.CLIENT)
  @SubscribeEvent
  public void onKeyInput(KeyInputEvent event) {
    EntityPlayer p = Minecraft.getMinecraft().player;
    ItemStack feet = p.getItemStackFromSlot(EntityEquipmentSlot.FEET);
    if (feet == null || feet.isEmpty() || p.isSneaking()) { return; } //sneak to not double jump
    if (EnchantmentHelper.getEnchantments(feet).containsKey(this) == false) { return; }
    if (p.getCooldownTracker().hasCooldown(feet.getItem())) { return; }
    if (FMLClientHandler.instance().getClient().gameSettings.keyBindJump.isKeyDown()
        && p.posY < p.lastTickPosY && p.isAirBorne && p.isInWater() == false) {
      //JUMP IS pressed and you are moving down
      int level = EnchantmentHelper.getEnchantments(feet).get(this);
      int uses = UtilNBT.getItemStackNBTVal(feet, NBT_USES);
      p.fallDistance = 0;
      float angle = (p.motionX == 0 && p.motionZ == 0) ? 90 : rotationPitch;
      UtilEntity.launch(p, angle, power);
      UtilParticle.spawnParticle(p.getEntityWorld(), EnumParticleTypes.CRIT_MAGIC, p.getPosition());
      UtilSound.playSound(p, p.getPosition(), SoundRegistry.bwoaaap, SoundCategory.PLAYERS, UtilSound.VOLUME / 8);
      UtilItemStack.damageItem(p, feet);
      uses++;
      if (uses >= level) { // level is maxuses
        //now block useage for a while
        if (!feet.isEmpty()) {
          p.getCooldownTracker().setCooldown(feet.getItem(), cooldown);
        }
        uses = 0;
      }
      UtilNBT.setItemStackNBTVal(feet, NBT_USES, uses);
      p.fallDistance = 0;
      ModCyclic.network.sendToServer(new PacketPlayerFalldamage());//reset at bottom of jump
    }
  }
}
