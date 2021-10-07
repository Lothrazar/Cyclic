package com.lothrazar.cyclic.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.Direction;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

/**
 * as of minecraft 1.16 much of this file contains code from this mod which is MIT License, the same as this project
 * 
 * https://github.com/jaquadro/StorageDrawers/blob/1.16/LICENSE
 * 
 */
public class UtilRenderText {

  static final float[] SIDE_ROTATION = { 0, 0, 2, 0, 3, 1 };

  public static void alignRendering(PoseStack matrix, Direction side) {
    // Rotate to face the correct direction for the drawer's orientation.
    matrix.translate(.5f, .5f, .5f);
    matrix.mulPose(new Quaternion(Vector3f.YP, getRotationYForSide2D(side), true));
    matrix.translate(-.5f, -.5f, -.5f);
  }

  public static float getRotationYForSide2D(Direction side) {
    return SIDE_ROTATION[side.ordinal()] * 90;
  }
}
