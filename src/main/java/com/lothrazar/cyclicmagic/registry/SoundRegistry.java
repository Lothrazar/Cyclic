package com.lothrazar.cyclicmagic.registry;
import com.lothrazar.cyclicmagic.data.Const;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class SoundRegistry {
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
  }
  private static SoundEvent registerSound(String name) {
    //thanks for the help: https://github.com/Choonster/TestMod3/tree/162914a163c7fcb6bdd992917fcbc699584e40de/src/main/java/com/choonster/testmod3
    // and http://www.minecraftforge.net/forum/index.php?topic=38076.0
    final ResourceLocation res = new ResourceLocation(Const.MODID, name);//new ResourceLocation(Const.MODID, "sounds/" + UtilSound.Own.crackle+".ogg");
    SoundEvent sound = new SoundEvent(res);
    sound.setRegistryName(res);
    GameRegistry.register(sound);
    return sound;
  }
}
