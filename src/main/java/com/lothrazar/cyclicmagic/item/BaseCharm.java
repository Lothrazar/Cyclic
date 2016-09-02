package com.lothrazar.cyclicmagic.item;

public class BaseCharm extends BaseItem{

  public BaseCharm(int durability) {
    this.setMaxStackSize(1);
    this.setMaxDamage(durability);
  }
}
