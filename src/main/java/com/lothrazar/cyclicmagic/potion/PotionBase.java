package com.lothrazar.cyclicmagic.potion;
import javax.annotation.Nullable;
import com.lothrazar.cyclicmagic.data.Const;
import com.lothrazar.cyclicmagic.util.UtilTextureRender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class PotionBase extends Potion {
  private ResourceLocation icon;
  private boolean beneficial;
  public PotionBase(String name, boolean b, int potionColor) {
    super(false, potionColor);
    //this.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn, amplifier);
    this.beneficial = b;
    this.setIcon(new ResourceLocation(Const.MODID, "textures/potions/" + name + ".png"));
    this.setPotionName("potion." + name);
  }
  @Override
  public void affectEntity(@Nullable Entity source, @Nullable Entity indirectSource, EntityLivingBase entityLivingBaseIn, int amplifier, double health) {
    super.affectEntity(source, indirectSource, entityLivingBaseIn, amplifier, health);
  }
  @SideOnly(Side.CLIENT)
  public boolean isBeneficial() {
    return this.beneficial;//decides top or bottom row
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) {
    UtilTextureRender.drawTextureSquare(getIcon(), x + 6, y + 6, Const.SQ - 2);
  }
  @Override
  @SideOnly(Side.CLIENT)
  public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) {
    if (mc.gameSettings.showDebugInfo == false)//dont texture on top of the debug text
      UtilTextureRender.drawTextureSquare(getIcon(), x + 4, y + 3, Const.SQ - 2);
  }
  public ResourceLocation getIcon() {
    return icon;
  }
  public void setIcon(ResourceLocation icon) {
    this.icon = icon;
  }
  public void tick(EntityLivingBase entity) {};
}