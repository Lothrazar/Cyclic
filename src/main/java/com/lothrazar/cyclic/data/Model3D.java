package com.lothrazar.cyclic.data;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

/**
 * Source from MIT open source https://github.com/mekanism/Mekanism/tree/1.15x
 * 
 * https://github.com/mekanism/Mekanism/blob/1.15x/LICENSE
 * 
 * Map which uses FluidStacks as keys, ignoring amount. Primary use: caching FluidStack aware fluid rendering (NBT, yay)
 */
public class Model3D {

  public double minX, minY, minZ;
  public double maxX, maxY, maxZ;
  public TextureAtlasSprite[] textures = new TextureAtlasSprite[6];
  public boolean[] renderSides = new boolean[] { true, true, true, true, true, true, false };

  public final void setBlockBounds(double xNeg, double yNeg, double zNeg, double xPos, double yPos, double zPos) {
    minX = xNeg;
    minY = yNeg;
    minZ = zNeg;
    maxX = xPos;
    maxY = yPos;
    maxZ = zPos;
  }

  public double sizeX() {
    return maxX - minX;
  }

  public double sizeY() {
    return maxY - minY;
  }

  public double sizeZ() {
    return maxZ - minZ;
  }

  public boolean shouldSideRender(Direction side) {
    return renderSides[side.ordinal()];
  }

  public void setTexture(TextureAtlasSprite tex) {
    Arrays.fill(textures, tex);
  }
}
