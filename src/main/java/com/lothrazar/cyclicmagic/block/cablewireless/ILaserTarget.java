package com.lothrazar.cyclicmagic.block.cablewireless;

import com.lothrazar.cyclicmagic.util.RenderUtil.LaserConfig;

public interface ILaserTarget {

  boolean isVisible();

  LaserConfig getTarget();
}
