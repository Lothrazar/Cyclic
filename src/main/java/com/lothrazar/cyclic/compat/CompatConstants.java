package com.lothrazar.cyclic.compat;

import net.minecraft.util.ResourceLocation;

public class CompatConstants {

  public static final String RESYNTH_GROWTH_STAGE = "growth_stage";
  public static final String RESYNTH = "resynth";
  public static final String CRAFTTWEAKER = "crafttweaker";
  public static final String CURIOS = "curios";
  public static final String TCONSTRUCT = "tconstruct";
  //
  //compat with Repurposed Structures Mod see #1517
  public static final String RS_MODID = "repurposed_structures";
  public static final String RS_STRONGHOLD_ID = "stronghold_stonebrick";
  public static final String RS_NETHER_STRONGHOLD_ID = "stronghold_nether";
  public static final String YUSTRONG_MODID = "betterstrongholds"; // https://github.com/yungnickyoung/YUNGs-Better-Strongholds
  public static final ResourceLocation RS_RESOURCE_LOCATION = new ResourceLocation(RS_MODID, RS_STRONGHOLD_ID);
  public static final ResourceLocation RS_NETHER_RESOURCE_LOCATION = new ResourceLocation(RS_MODID, RS_NETHER_STRONGHOLD_ID);
  public static final ResourceLocation YUNG_STRONGHOLD_LOCATION = new ResourceLocation(YUSTRONG_MODID, "stronghold");
}
