package com.lothrazar.cyclicmagic.villager.sage;

import java.util.List;
import java.util.Random;
import com.lothrazar.cyclicmagic.registry.VillagerProfRegistry;
import com.lothrazar.cyclicmagic.villager.VillagerCreateModule;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class VillageStructureSage extends StructureVillagePieces.House1 {

  private static final int XSIZE = 8;
  private static final int YSIZE = 8;
  private static final int ZSIZE = 8;

  public VillageStructureSage() {}

  public VillageStructureSage(StructureBoundingBox boundingBox, EnumFacing par5) {
    this.setCoordBaseMode(par5);
    this.boundingBox = boundingBox;
  }

  /**
   * This particular function Inspired by Ellpeck Actually Additions
   * 
   * https://github.com/Ellpeck/ActuallyAdditions/blob/29534764a952a34ebf4d599142a5254342e2596f/src/main/java/de/ellpeck/actuallyadditions/mod/gen/village/component/VillageComponentJamHouse.java#L230
   * 
   * @param pieces
   * @param p1
   * @param p2
   * @param p3
   * @param p4
   * @return
   */
  public static VillageStructureSage buildComponent(List<StructureComponent> pieces, int p1, int p2, int p3, EnumFacing p4) {
    StructureBoundingBox boundingBox = StructureBoundingBox.getComponentToAddBoundingBox(p1, p2, p3, 0, 0, 0, XSIZE, YSIZE, ZSIZE, p4);
    return canVillageGoDeeper(boundingBox) && StructureComponent.findIntersecting(pieces, boundingBox) == null ? new VillageStructureSage(boundingBox, p4) : null;
  }

  @Override
  public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
    int x = 6, y = 1, z = 6;
    this.spawnVillagers(world, sbb, x, y, z, VillagerCreateModule.sageCount);
    return true;
  }

  @Override
  protected VillagerProfession chooseForgeProfession(int count, VillagerProfession prof) {
    return VillagerProfRegistry.SAGE;
  }
}
