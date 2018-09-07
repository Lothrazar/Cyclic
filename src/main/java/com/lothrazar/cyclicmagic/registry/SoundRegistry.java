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
package com.lothrazar.cyclicmagic.registry;

import java.util.ArrayList;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundRegistry {

  public static ArrayList<SoundEvent> sounds = new ArrayList<SoundEvent>();
  public static SoundEvent sack_holding;
  public static SoundEvent inventory_upgrade;
  public static SoundEvent enchant_launch;
  public static SoundEvent heart_container;
  public static SoundEvent chest_sack_capture;
  public static SoundEvent dungeonfinder;
  public static SoundEvent uncraft;
  public static SoundEvent metal_pitch;
  public static SoundEvent liquid_evaporate;
  public static SoundEvent block_scaffolding;
  public static SoundEvent chaos_reaper;
  public static SoundEvent fireball_explode;
  public static SoundEvent fireball_staff_launch;
  public static SoundEvent frost_staff_launch;
  public static SoundEvent lightning_staff_launch;
  public static SoundEvent machine_launch;
  public static SoundEvent monster_ball_capture;
  public static SoundEvent monster_ball_release;
  public static SoundEvent spikes_off;
  public static SoundEvent spikes_on;
  public static SoundEvent spirit_seeker;
  public static SoundEvent step_height_down;
  public static SoundEvent step_height_up;
  public static SoundEvent tool_mode;
  public static SoundEvent warp;
  public static SoundEvent dcoin;
  public static SoundEvent doorbell_mikekoenig;
  public static SoundEvent dice_mikekoenig;

  public static void register() {
    //old sounds kept - lothrazar originals
    sack_holding = registerSound("basey");
    dungeonfinder = registerSound("dungeonfinder");
    inventory_upgrade = registerSound("bwewe");
    heart_container = registerSound("fill");
    enchant_launch = registerSound("bwoaaap");
    uncraft = registerSound("crack");
    chest_sack_capture = registerSound("thunk");
    liquid_evaporate = registerSound("pschew_fire");
    // new sounds - Ithronyar
    block_scaffolding = registerSound("block_scaffolding");
    chaos_reaper = registerSound("chaos_reaper");//wand_hypno=chaos reaper
    fireball_explode = registerSound("fireball_explode");
    fireball_staff_launch = registerSound("fireball_staff_launch");
    frost_staff_launch = registerSound("frost_staff_launch");
    lightning_staff_launch = registerSound("lightning_staff_launch");
    machine_launch = registerSound("machine_launch");
    monster_ball_capture = registerSound("monster_ball_capture");
    monster_ball_release = registerSound("monster_ball_release");
    spikes_off = registerSound("spikes_off");
    spikes_on = registerSound("spikes_on");
    spirit_seeker = registerSound("spirit_seeker");//wand_missile=SpiritSeeker
    step_height_down = registerSound("step_height_down");
    step_height_up = registerSound("step_height_up");
    tool_mode = registerSound("tool_mode");
    warp = registerSound("warp_echo");
    dcoin = registerSound("dcoin");
    //ohter sounds NOT BY ME 
    // http://soundbible.com/1599-Store-Door-Chime.html
    //    License: Attribution 3.0 
    //    Recorded by Mike Koenig 
    doorbell_mikekoenig = registerSound("doorbell_mikekoenig");
    //http://soundbible.com/182-Shake-And-Roll-Dice.html
    //    License: Attribution 3.0 
    //    Recorded by Mike Koenig 
    dice_mikekoenig = registerSound("dice_mike_koenig");
  }

  private static SoundEvent registerSound(String name) {
    //thanks for the help: https://github.com/Choonster/TestMod3/tree/162914a163c7fcb6bdd992917fcbc699584e40de/src/main/java/com/choonster/testmod3
    // and http://www.minecraftforge.net/forum/index.php?topic=38076.0
    final ResourceLocation res = new ResourceLocation(Const.MODID, name);
    SoundEvent sound = new SoundEvent(res);
    sound.setRegistryName(res);
    sounds.add(sound);
    return sound;
  }

  @SubscribeEvent
  public static void onRegistryEvent(RegistryEvent.Register<SoundEvent> event) {
    for (SoundEvent b : sounds) {
      event.getRegistry().register(b);
    }
  }
}
