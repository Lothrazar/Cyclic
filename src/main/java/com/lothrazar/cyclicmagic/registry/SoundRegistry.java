package com.lothrazar.cyclicmagic.registry;
import java.util.ArrayList;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SoundRegistry {
  public static ArrayList<SoundEvent> sounds = new ArrayList<SoundEvent>();
  public static SoundEvent crackle;
  public static SoundEvent basey;
  public static SoundEvent bip;
  public static SoundEvent buzzp;
  public static SoundEvent bwewe;
  public static SoundEvent bwoaaap;
  public static SoundEvent byeaa;
  public static SoundEvent dcoin;
  public static SoundEvent fill;
  public static SoundEvent pew;
  public static SoundEvent pow;
  public static SoundEvent thunk;
  public static SoundEvent warp;
  public static SoundEvent spikes_in;
  public static SoundEvent spikes_out;
  public static SoundEvent dungeonfinder;
  public static SoundEvent firelaunch;
  public static SoundEvent goodlaunch;
  public static SoundEvent laserbeanpew;
  public static SoundEvent powerupscales;
  public static SoundEvent spikemaybe;
  public static SoundEvent bass_echo;
  public static SoundEvent coin;
  public static SoundEvent crack;
  public static SoundEvent explosm;
  public static SoundEvent guitar;
  public static SoundEvent hovering;
  public static SoundEvent magic_missile;
  public static SoundEvent metal_pitch;
  public static SoundEvent pew_long;
  public static SoundEvent pschew_fire;
  public static void register() {
    basey = registerSound("basey");//used by storage bag deposit
    bip = registerSound("bip");//cyclic wand GUI rotation
    buzzp = registerSound("buzzp");//carbon paper & invo food & heart-eat-fail
    bwewe = registerSound("bwewe"); //invo food success
    bwoaaap = registerSound("bwoaaap");//vector plate & launch enchant
    byeaa = registerSound("byeaa");//magic net on release&spawn
    crackle = registerSound("crackle");//scaffolding
    dcoin = registerSound("dcoin");//tool toggles: auto torch, piston, randomize, exchange scepters
    fill = registerSound("fill");//heart food success
    pew = registerSound("pew");//magic net on catch (previously was base spell thrown)
    pow = registerSound("pow");//UNUSED
    thunk = registerSound("thunk");//ChestSack 
    warp = registerSound("warp");//Ender Wings & Book 
    spikes_out = registerSound("spikes_out");
    spikes_in = registerSound("spikes_in");
    dungeonfinder = registerSound("dungeonfinder");
    firelaunch = registerSound("firelaunch");
    goodlaunch = registerSound("goodlaunch");
    laserbeanpew = registerSound("laserbeanpew");
    powerupscales = registerSound("powerupscales");
    bass_echo = registerSound("bass_echo");
    coin = registerSound("coin");
    crack = registerSound("crack");
    explosm = registerSound("explosm");
    guitar = registerSound("guitar");
    hovering = registerSound("hovering");
    magic_missile = registerSound("magic_missile");
    metal_pitch = registerSound("metal_pitch");
    pew_long = registerSound("pew_long");
    pschew_fire = registerSound("pschew_fire");
    spikemaybe = registerSound("spikemaybe");
  }
  private static SoundEvent registerSound(String name) {
    //thanks for the help: https://github.com/Choonster/TestMod3/tree/162914a163c7fcb6bdd992917fcbc699584e40de/src/main/java/com/choonster/testmod3
    // and http://www.minecraftforge.net/forum/index.php?topic=38076.0
    final ResourceLocation res = new ResourceLocation(Const.MODID, name);//new ResourceLocation(Const.MODID, "sounds/" + UtilSound.Own.crackle+".ogg");
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
