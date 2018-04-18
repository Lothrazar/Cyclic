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
package com.lothrazar.cyclicmagic.data;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class Location {

  public double X;
  public double Y;
  public double Z;
  public int index;
  public int dimension = 0;// : this is unused right now
  public String name;

  public Location(BlockPos pos) {
    X = pos.getX();
    Y = pos.getY();
    Z = pos.getZ();
    index = 0;
    dimension = 0;
    name = "";
  }

  public Location(BlockPos pos, int dim, int idx, String pname) {
    X = pos.getX();
    Y = pos.getY();
    Z = pos.getZ();
    index = idx;
    dimension = dim;
    name = pname;
  }

  public Location(int idx, double pX, double pY, double pZ, int d, String pname) {
    X = pX;
    Y = pY;
    Z = pZ;
    index = idx;
    dimension = d;
    name = pname;
    if (name == null)
      name = "";
  }

  public Location(int idx, EntityPlayer p, String pname) {
    X = p.posX;
    Y = p.posY;
    Z = p.posZ;
    index = idx;
    dimension = p.dimension;
    name = pname; // is often the Biome Name
    if (name == null) {
      name = "";
    }
  }

  public Location(String csv) {
    String[] pts = csv.split(",");
    X = Double.parseDouble(pts[0]);
    Y = Double.parseDouble(pts[1]);
    Z = Double.parseDouble(pts[2]);
    dimension = Integer.parseInt(pts[3]);
    if (pts.length > 4)
      name = pts[4];
    if (name == null)
      name = "";
  }

  public String toCSV() {
    if (name == null)
      name = "";
    return X + "," + Y + "," + Z + "," + dimension + "," + name;
  }

  public String toDisplay()// different from toCSV, since we round off the
  // numbers and format
  {
    if (name == null)
      name = "";
    String showName = " ";
    if (name != null && name.isEmpty() == false)
      showName = "  :  " + name;
    // "[" + index + "] " +
    return Math.round(X) + ", " + Math.round(Y) + ", " + Math.round(Z) + showName;
  }

  public String toDisplayShort() {
    if (name == null)
      name = "";
    String showName = " ";
    if (name != null && name.isEmpty() == false)
      showName = "  :  " + name;
    return showName + Math.round(X) + ", " + Math.round(Y) + ", " + Math.round(Z);
  }

  public String toDisplayNoCoords() {
    return name + " (y = " + MathHelper.floor(Y) + ")";
  }

  public BlockPos toBlockPos() {
    return new BlockPos(X, Y, Z);
  }
}
