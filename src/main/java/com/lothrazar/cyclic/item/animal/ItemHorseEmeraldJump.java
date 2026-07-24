package com.lothrazar.cyclic.item.animal;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.item.ItemBaseCyclic;
import com.lothrazar.cyclic.util.HorseFeedUtil;
import com.lothrazar.library.core.IEntityInteractable;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.equine.Horse;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class ItemHorseEmeraldJump extends ItemBaseCyclic implements IEntityInteractable {

  public static final String NBT_COUNT = ModCyclic.MODID + "_carrot_emerald_count";
  private static final int JUMP_MAX = 10;
  private static final double JUMP_AMT = 0.08;
  private static final Identifier MODIFIER_ID = Identifier.fromNamespaceAndPath(ModCyclic.MODID, "horse_emerald_jump");

  public ItemHorseEmeraldJump(Properties prop) {
    super(prop);
  }

  @Override
  public void interactWith(PlayerInteractEvent.EntityInteract event) {
    if (event.getItemStack().getItem() != this
        || !(event.getTarget() instanceof Horse ahorse)) {
      return;
    }
    AttributeInstance jump = ahorse.getAttribute(Attributes.JUMP_STRENGTH);
    if (jump == null || jump.getValue() >= JUMP_MAX) {
      return;
    }
    AttributeModifier old = jump.getModifier(MODIFIER_ID);
    double newAmount = (old == null) ? JUMP_AMT : old.amount() + JUMP_AMT;
    jump.removeModifier(MODIFIER_ID);
    jump.addPermanentModifier(new AttributeModifier(MODIFIER_ID, newAmount, AttributeModifier.Operation.ADD_VALUE));
    ahorse.getPersistentData().putInt(NBT_COUNT, ahorse.getPersistentData().getInt(NBT_COUNT) + 1);
    HorseFeedUtil.finishFeed(event, ahorse);
  }
}
