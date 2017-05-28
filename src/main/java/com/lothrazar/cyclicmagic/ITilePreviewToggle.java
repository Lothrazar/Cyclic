package com.lothrazar.cyclicmagic;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.util.math.BlockPos;

public interface ITilePreviewToggle {
  public void togglePreview();
  public boolean isPreviewVisible();
  public @Nonnull List<BlockPos> getShape();
}
