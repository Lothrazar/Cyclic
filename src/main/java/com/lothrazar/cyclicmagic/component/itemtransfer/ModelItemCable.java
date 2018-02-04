package com.lothrazar.cyclicmagic.component.itemtransfer;
import com.lothrazar.cyclicmagic.component.cable.BlockBaseCable.EnumConnectType;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * TODO: shared base class with fluid
 *
 */
public class ModelItemCable extends ModelBase {
  public ModelRenderer south;
  public ModelRenderer cube;
  public ModelRenderer north;
  public ModelRenderer west;
  public ModelRenderer east;
  public ModelRenderer up;
  public ModelRenderer down;
  public ModelRenderer southC;
  public ModelRenderer northC;
  public ModelRenderer westC;
  public ModelRenderer eastC;
  public ModelRenderer upC;
  public ModelRenderer downC;
  public ModelItemCable() {
    this.textureWidth = 32;
    this.textureHeight = 32;
    this.upC = new ModelRenderer(this, 16, 0);
    this.upC.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.upC.addBox(-3.0F, -3.0F, 7.0F, 6, 6, 1, 0.0F);
    this.setRotateAngle(upC, 1.5707963267948966F, 0.0F, 0.0F);
    this.downC = new ModelRenderer(this, 16, 0);
    this.downC.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.downC.addBox(-3.0F, -3.0F, 7.0F, 6, 6, 1, 0.0F);
    this.setRotateAngle(downC, -1.5707963267948966F, 0.0F, 0.0F);
    this.south = new ModelRenderer(this, 0, 0);
    this.south.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.south.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 8, 0.0F);
    this.southC = new ModelRenderer(this, 16, 0);
    this.southC.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.southC.addBox(-3.0F, -3.0F, 7.0F, 6, 6, 1, 0.0F);
    this.westC = new ModelRenderer(this, 16, 0);
    this.westC.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.westC.addBox(-3.0F, -3.0F, 7.0F, 6, 6, 1, 0.0F);
    this.setRotateAngle(westC, 0.0F, 1.5707963267948966F, 0.0F);
    this.north = new ModelRenderer(this, 0, 0);
    this.north.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.north.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 8, 0.0F);
    this.setRotateAngle(north, 3.141592653589793F, 0.0F, 0.0F);
    this.northC = new ModelRenderer(this, 16, 0);
    this.northC.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.northC.addBox(-3.0F, -3.0F, 7.0F, 6, 6, 1, 0.0F);
    this.setRotateAngle(northC, 3.141592653589793F, 0.0F, 0.0F);
    this.eastC = new ModelRenderer(this, 16, 0);
    this.eastC.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.eastC.addBox(-3.0F, -3.0F, 7.0F, 6, 6, 1, 0.0F);
    this.setRotateAngle(eastC, 0.0F, -1.5707963267948966F, 0.0F);
    this.cube = new ModelRenderer(this, 0, 12);
    this.cube.setRotationPoint(-3.0F, -3.0F, -3.0F);
    this.cube.addBox(0.0F, 0.0F, 0.0F, 6, 6, 6, 0.0F);
    this.up = new ModelRenderer(this, 0, 0);
    this.up.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.up.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 8, 0.0F);
    this.setRotateAngle(up, 1.5707963267948966F, 0.0F, 0.0F);
    this.east = new ModelRenderer(this, 0, 0);
    this.east.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.east.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 8, 0.0F);
    this.setRotateAngle(east, 0.0F, -1.5707963267948966F, 0.0F);
    this.west = new ModelRenderer(this, 0, 0);
    this.west.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.west.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 8, 0.0F);
    this.setRotateAngle(west, 0.0F, 1.5707963267948966F, 0.0F);
    this.down = new ModelRenderer(this, 0, 0);
    this.down.setRotationPoint(0.0F, 0.0F, 0.0F);
    this.down.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 8, 0.0F);
    this.setRotateAngle(down, -1.5707963267948966F, 0.0F, 0.0F);
  }
  private boolean detectConnections(TileEntityItemCable tile) {
    boolean a = connected(tile.north) && connected(tile.south) && !connected(tile.west) && !connected(tile.east) && !connected(tile.up) && !connected(tile.down);
    boolean b = !connected(tile.north) && !connected(tile.south) && connected(tile.west) && connected(tile.east) && !connected(tile.up) && !connected(tile.down);
    boolean c = !connected(tile.north) && !connected(tile.south) && !connected(tile.west) && !connected(tile.east) && connected(tile.up) && connected(tile.down);
    return (a ^ b ^ c);
  }
  private boolean connected(EnumConnectType c) {
    return c == EnumConnectType.STORAGE || c == EnumConnectType.CONNECT;
  }
  public void render(TileEntityItemCable tile) {
    float f5 = 0.0625F;
    if (tile.north == EnumConnectType.CONNECT) {
      this.north.render(f5);
    }
    else if (tile.north == EnumConnectType.STORAGE) {
      this.north.render(f5);
      this.northC.render(f5);
    }
    if (tile.south == EnumConnectType.CONNECT) {
      this.south.render(f5);
    }
    else if (tile.south == EnumConnectType.STORAGE) {
      this.south.render(f5);
      this.southC.render(f5);
    }
    if (tile.east == EnumConnectType.CONNECT) {
      this.east.render(f5);
    }
    else if (tile.east == EnumConnectType.STORAGE) {
      this.east.render(f5);
      this.eastC.render(f5);
    }
    if (tile.west == EnumConnectType.CONNECT) {
      this.west.render(f5);
    }
    else if (tile.west == EnumConnectType.STORAGE) {
      this.west.render(f5);
      this.westC.render(f5);
    }
    if (tile.up == EnumConnectType.CONNECT) {
      this.up.render(f5);
    }
    else if (tile.up == EnumConnectType.STORAGE) {
      this.up.render(f5);
      this.upC.render(f5);
    }
    if (tile.down == EnumConnectType.CONNECT) {
      this.down.render(f5);
    }
    else if (tile.down == EnumConnectType.STORAGE) {
      this.down.render(f5);
      this.downC.render(f5);
    }
    if (!detectConnections(tile))//|| tile.getKind() != CableKind.kabel
      this.cube.render(f5);
  }
  // @Override
  @Override
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {}
  /**
   * This is a helper function from Tabula to set the rotation of model parts
   */
  public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }
}