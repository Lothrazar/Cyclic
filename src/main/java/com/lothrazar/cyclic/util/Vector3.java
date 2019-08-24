package com.lothrazar.cyclic.util;

import net.minecraft.entity.Entity;

/**
 * This class was created by <ChickenBones>. It's distributed as part of the Botania Mod. Get the Source Code in github: https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the Botania License: http://botaniamod.net/license.php
 *
 *
 * Thank you to ChickenBones and Vaskii for this file Imported into Scepter Powers by Lothrazar, December 11, 2015 with minimal modification (I ported it from minecraft forge 1.7.10 to 1.8.8). This
 * mod is open source (MIT), please respect this and the licenses and authors above. References: https://github.com/Chicken-Bones/CodeChickenLib/blob/master/src/codechicken/ lib/vec/Vector3.java
 * https://github.com/Vazkii/Botania/blob/ 9cf015ee972bb8568f65128fa7b84c12c4a7cfff/src/main/java/vazkii/botania/common/ core/helper/Vector3.java
 *
 */
public class Vector3 {

  //
  //    public static Vector3 zero = new Vector3();
  //    public static Vector3 one = new Vector3(1, 1, 1);
  //    public static Vector3 center = new Vector3(0.5, 0.5, 0.5);
  public double x;
  public double y;
  public double z;

  public Vector3() {}

  public Vector3(double d, double d1, double d2) {
    x = d;
    y = d1;
    z = d2;
  }

  public Vector3(Entity e) {
    this(e.posX, e.posY, e.posZ);
  }

  public double mag() {
    return Math.sqrt(x * x + y * y + z * z);
  }

  public Vector3 normalize() {
    double d = mag();
    if (d != 0)
      multiply(1 / d);
    return this;
  }

  public Vector3(Vector3 vec) {
    x = vec.x;
    y = vec.y;
    z = vec.z;
  }

  public Vector3 multiply(double d) {
    x *= d;
    y *= d;
    z *= d;
    return this;
  }

  public Vector3 copy() {
    return new Vector3(this);
  }

  public Vector3 subtract(Vector3 vec) {
    x -= vec.x;
    y -= vec.y;
    z -= vec.z;
    return this;
  }
}
