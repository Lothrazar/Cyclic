/*******************************************************************************
 * The MIT License (MIT)
 * 
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclicmagic.playerupgrade;

import java.util.List;
import com.lothrazar.cyclicmagic.ModCyclic;
import com.lothrazar.cyclicmagic.capability.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.config.IHasConfig;
import com.lothrazar.cyclicmagic.core.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.ItemFoodCreative;
import com.lothrazar.cyclicmagic.core.util.Const;
import com.lothrazar.cyclicmagic.core.util.UtilChat;
import com.lothrazar.cyclicmagic.core.util.UtilEntity;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.RecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameType;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemNoclipGhost extends ItemFoodCreative implements IHasRecipe, IHasConfig {

  //revived from https://github.com/PrinceOfAmber/Cyclic/blob/d2f91d1f97b9cfba47786a30b427fbfdcd714212/src/main/java/com/lothrazar/cyclicmagic/spell/SpellGhost.java
  public static int GHOST_SECONDS;
  public static int POTION_SECONDS;
  private static final int numFood = 2;

  public ItemNoclipGhost() {
    super(numFood, false);
    this.setAlwaysEdible();
  }

  @Override
  protected void onFoodEaten(ItemStack par1ItemStack, World world, EntityPlayer player) {
    setPlayerGhostMode(player, world);
  }

  @Override
  @SideOnly(Side.CLIENT)
  public void addInformation(ItemStack stack, World playerIn, List<String> tooltips, net.minecraft.client.util.ITooltipFlag advanced) {
    tooltips.add(UtilChat.lang(this.getUnlocalizedName() + ".tooltip"));
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this, 3),
        "lal", "lal", "lal",
        'l', Items.FERMENTED_SPIDER_EYE,
        'a', Items.CHORUS_FRUIT);
  }

  @Override
  public void syncConfig(Configuration config) {
    String category = Const.ConfigCategory.modpackMisc;
    GHOST_SECONDS = config.getInt("CorruptedChorusSeconds", category, 10, 1, 60, "How long you can noclip after eating corrupted chorus");
    POTION_SECONDS = config.getInt("CorruptedChorusPotions", category, 10, 1, 60, "How long the negative potion effects last after a corrupted chorus teleports you");
  }

  private void setPlayerGhostMode(EntityPlayer player, World par2World) {
    if (par2World.isRemote == false) {
      player.setGameType(GameType.SPECTATOR);
    }
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    props.setChorusTimer(GHOST_SECONDS * Const.TICKS_PER_SEC);
    props.setChorusOn(true);
    props.setChorusStart(player.getPosition());
    props.setChorusDim(player.dimension);
  }

  @SubscribeEvent
  public void onPlayerUpdate(LivingUpdateEvent event) {
    if (event.getEntityLiving() instanceof EntityPlayer == false) {
      return;
    }
    EntityPlayer player = (EntityPlayer) event.getEntityLiving();
    World world = player.getEntityWorld();
    IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(player);
    if (props.getChorusOn()) {
      int playerGhost = props.getChorusTimer();
      if (playerGhost > 0) {
        ModCyclic.proxy.closeSpectatorGui();
        props.setChorusTimer(playerGhost - 1);
      }
      else {
        //times up!
        props.setChorusOn(false);
        if (props.getChorusDim() != player.dimension) {
          // if the player changed dimension while a ghost, thats not
          // allowed. dont tp them back
          player.setGameType(GameType.SURVIVAL);
          player.attackEntityFrom(DamageSource.MAGIC, 50);
        }
        else {
          BlockPos currentPos = player.getPosition();
          BlockPos sourcePos = props.getChorusStart();//new BlockPos(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
          if (world.isAirBlock(currentPos) && world.isAirBlock(currentPos.up())) {
            //then we can stay, but add nausea
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, Const.TICKS_PER_SEC * POTION_SECONDS));
            player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, Const.TICKS_PER_SEC * POTION_SECONDS));
          }
          else {
            //teleport back home	
            UtilEntity.teleportWallSafe(player, world, sourcePos);
            //player.setPositionAndUpdate(Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]));
          }
          player.fallDistance = 0.0F;
          player.setGameType(GameType.SURVIVAL);
        }
      }
    }
  }
}
