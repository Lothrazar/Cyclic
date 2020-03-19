package com.lothrazar.cyclic.render;

import java.util.Arrays;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;

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

  public void setSideRender(Direction side, boolean value) {
    renderSides[side.ordinal()] = value;
  }

  public boolean shouldSideRender(Direction side) {
    return renderSides[side.ordinal()];
  }

  public void setTexture(TextureAtlasSprite tex) {
    Arrays.fill(textures, tex);
  }

  public void setTextures(TextureAtlasSprite down, TextureAtlasSprite up, TextureAtlasSprite north, TextureAtlasSprite south, TextureAtlasSprite west, TextureAtlasSprite east) {
    textures[0] = down;
    textures[1] = up;
    textures[2] = north;
    textures[3] = south;
    textures[4] = west;
    textures[5] = east;
  }
}
