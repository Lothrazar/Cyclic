package com.lothrazar.cyclicmagic.block.cablewireless;

import java.util.List;
import com.lothrazar.cyclicmagic.data.ITilePreviewToggle;
import com.lothrazar.cyclicmagic.util.RenderUtil.LaserConfig;

public interface ILaserTarget extends ITilePreviewToggle {

  List<LaserConfig> getTarget();
}
