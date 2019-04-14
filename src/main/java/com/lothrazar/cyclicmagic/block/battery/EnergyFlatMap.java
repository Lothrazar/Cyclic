package com.lothrazar.cyclicmagic.block.battery;

import net.minecraft.util.IStringSerializable;

enum EnergyFlatMap implements IStringSerializable {
  AMOUNT_G0("g0"), AMOUNT_G1("g1"), AMOUNT_G2("g2"), AMOUNT_G3("g3"), AMOUNT_G4("g4"), AMOUNT_G5("g5"), AMOUNT_G6("g6"), AMOUNT_G7("g7"), AMOUNT_G8("g8"), AMOUNT_G9("g9"), AMOUNT_G10("g10"), AMOUNT_G11("g11"), AMOUNT_G12("g12"), AMOUNT_G13("g13"), AMOUNT_G14("g14"), AMOUNT_G15("g15"), AMOUNT_G16("g16");

  private final String name;

  EnergyFlatMap(String name) {
    this.name = name;
  }

  @Override
  public String getName() {
    return name;
  }
}