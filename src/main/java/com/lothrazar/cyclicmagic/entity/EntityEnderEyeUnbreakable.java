package com.lothrazar.cyclicmagic.entity;
import java.lang.reflect.Field;
import com.lothrazar.cyclicmagic.entity.projectile.RenderBall;
import com.lothrazar.cyclicmagic.util.UtilReflection;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class EntityEnderEyeUnbreakable extends EntityEnderEye {
  public static class FactoryMissile implements IRenderFactory<EntityEnderEyeUnbreakable> {
    @Override
    public Render<? super EntityEnderEyeUnbreakable> createRenderFor(RenderManager rm) {
      return new RenderBall<EntityEnderEyeUnbreakable>(rm, "ender_eye_orb");
    }
  }
  public EntityEnderEyeUnbreakable(World worldIn) {
    super(worldIn);
    this.setSize(0.25F, 0.25F);
  }
  public EntityEnderEyeUnbreakable(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }
  public void moveTowards(BlockPos pos) {
    super.moveTowards(pos);
    this.shatterOff();
  }
  /**
   * Called to update the entity's position/logic.
   */
  @Override
  public void onUpdate() {
    this.shatterOff();
    super.onUpdate();
  }
  private void shatterOff() {
    Field f = UtilReflection.getPrivateField("shatterOrDrop", "field_70221_f", EntityEnderEye.class);
    try {
      f.set(this, false);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
