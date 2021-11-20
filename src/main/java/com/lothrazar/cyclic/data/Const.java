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
package com.lothrazar.cyclic.data;
public class Const {

  public static final String MODID = "cyclicmagic";
  public static final int SQ = 18;
  // not a regular propert. : class ItemSkull:  {"skeleton", "wither", "zombie", "char", "creeper", "dragon"};
  public static final int TICKS_PER_SEC = 20;
  public static final int TPS = TICKS_PER_SEC;
  public static final int CHUNK_SIZE = 16;
  public static final String SKULLOWNER = "SkullOwner";

  public class Potions {

    public static final int I = 0;
    public static final int II = 1;
    public static final int III = 2;
    public static final int IV = 3;
    public static final int V = 4;
  }

  public static final int WORLDHEIGHT = 256;
  //http://minecraft.gamepedia.com/Light#Mobs
  //this or lower
  //  public static final int LIGHT_MOBSPAWN = 7;
  //  public static final int LIGHT_MOBSPAWN_BLAZE = 11;
  public static final int PAD = 8;
  /**
   * defined by vanilla item stacks on things like grindstone/anvi/enchanting table for increasing repair costs
   */
  //  public static final String NBT_REPAIR_COST = "RepairCost";
}
