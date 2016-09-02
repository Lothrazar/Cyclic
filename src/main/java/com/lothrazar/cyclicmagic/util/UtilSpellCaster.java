package com.lothrazar.cyclicmagic.util;
import com.lothrazar.cyclicmagic.item.tool.ItemCyclicWand;
import com.lothrazar.cyclicmagic.registry.SpellRegistry;
import com.lothrazar.cyclicmagic.spell.ISpell;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilSpellCaster {
  public static ItemStack getPlayerWandIfHeld(EntityPlayer player) {
    ItemStack wand = player.getHeldItemMainhand();
    if (wand != null && wand.getItem() instanceof ItemCyclicWand) { return wand; }
    wand = player.getHeldItemOffhand();
    if (wand != null && wand.getItem() instanceof ItemCyclicWand) { return wand; }
    return null;
  }
  public static boolean spellsEnabled(EntityPlayer player) {
    // current requirement is only a wand
    return UtilSpellCaster.getPlayerWandIfHeld(player) != null;
  }
  public static boolean tryCastCurrent(World world, EntityPlayer player, BlockPos pos, EnumFacing side, ItemStack wand, EnumHand hand) {
    return tryCast(getPlayerCurrentISpell(player), world, player, pos, side, wand, hand);
  }
  public static boolean tryCast(ISpell spell, World world, EntityPlayer player, BlockPos pos, EnumFacing side, ItemStack wand, EnumHand hand) {
    //ItemStack wand = getPlayerWandIfHeld(player);
    if (wand == null) { return false; }
    //		if (ItemCyclicWand.Timer.isBlockedBySpellTimer(wand)) { return false; }
    if (spell.canPlayerCast(world, player, pos)) {
      if (spell.cast(world, player, wand, pos, side)) {
        //				castSuccess(spell, world, player, pos);
        if (hand != null) {
          player.swingArm(hand);
        }
        return true;
      }
      return false;
      // else the spell was cast, but it had no result
      // failure does not trigger here. it was cast just didnt work
      // so maybe just was no valid target, or position was blocked/in use
    }
    else {
      // not enough XP (resources)
      spell.onCastFailure(world, player, pos);
      return false;
    }
  }
  //	public static void castSuccess(ISpell spell, World world, EntityPlayer player, BlockPos pos) {
  //
  //		// succes should do things like: drain resources, play sounds
  //		// and particles
  ////		spell.payCost(world, player, pos);
  ////
  ////		ItemCyclicWand.Energy.setCooldownCounter(getPlayerWandIfHeld(player),  world.getTotalWorldTime());
  ////
  ////		ItemCyclicWand.Timer.setSpellTimer(getPlayerWandIfHeld(player), spell.getCastCooldown());
  //	}
  public static void shiftLeft(EntityPlayer player) {
    ItemStack wand = getPlayerWandIfHeld(player);
    ISpell curr = ItemCyclicWand.Spells.getSpellCurrent(wand);
    int left = SpellRegistry.prev(wand, curr).getID();
    ItemCyclicWand.Spells.setSpellCurrent(wand, left);
  }
  public static void shiftRight(EntityPlayer player) {
    ItemStack wand = getPlayerWandIfHeld(player);
    ISpell curr = ItemCyclicWand.Spells.getSpellCurrent(wand);
    int right = SpellRegistry.next(wand, curr).getID();
    ItemCyclicWand.Spells.setSpellCurrent(wand, right);
  }
  public static ISpell getPlayerCurrentISpell(EntityPlayer player) {
    ItemStack wand = getPlayerWandIfHeld(player);
    ISpell current = SpellRegistry.getSpellFromID(ItemCyclicWand.Spells.getSpellIDCurrent(wand));
    if (current == null) {
      current = SpellRegistry.getSpellbook(wand).get(0);
    }
    return current;
  }
  public static void rechargeWithExp(EntityPlayer player) {
    /*
     * ItemStack wand = getPlayerWandIfHeld(player);
     * 
     * 
     * int MAX = ItemCyclicWand.Energy.getMaximum(wand);
     * 
     * if(player.capabilities.isCreativeMode){ // always set full
     * ItemCyclicWand.Energy.setCurrent(wand, MAX); } else
     * if(Energy.RECHARGE_EXP_COST < UtilExperience.getExpTotal(player) &&
     * ItemCyclicWand.Energy.getCurrent(wand) + Energy.RECHARGE_MANA_AMT <=
     * MAX){
     * 
     * ItemCyclicWand.Energy.rechargeBy(wand, Energy.RECHARGE_MANA_AMT);
     * 
     * UtilExperience.drainExp(player, Energy.RECHARGE_EXP_COST);
     * UtilSound.playSound(player.worldObj, player.getPosition(),
     * UtilSound.Own.fill); } else{ UtilSound.playSound(player.worldObj,
     * player.getPosition(), UtilSound.Own.buzzp); }
     */
  }
}
