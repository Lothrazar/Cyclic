package com.lothrazar.cyclic.item.magicnet;

import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.registry.ItemRegistry;
import com.lothrazar.cyclic.registry.SoundRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import java.util.List;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemMobContainer extends ItemBase {

  public ItemMobContainer(Properties properties) {
    super(properties);
  }

  @Override
  @OnlyIn(Dist.CLIENT)
  public void addInformation(ItemStack stack, World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
    if (stack.hasTag()) {
      TranslationTextComponent t = new TranslationTextComponent(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID));
      t.mergeStyle(TextFormatting.GRAY);
      tooltip.add(t);
    }
    else {
      super.addInformation(stack, worldIn, tooltip, flagIn);
    }
  }

  @Override
  public ActionResultType onItemUse(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    ItemStack stack = player.getHeldItem(context.getHand());
    if (!stack.hasTag()) {
      return ActionResultType.PASS;
    }
    BlockPos pos = context.getPos();
    if (context.getFace() != null) {
      pos = pos.offset(context.getFace());
    }
    World world = context.getWorld();
    UtilSound.playSound(player, SoundRegistry.MONSTER_BALL_RELEASE, 0.3F, 1F);
    if (!world.isRemote) {
      Entity entity = ForgeRegistries.ENTITIES.getValue(new ResourceLocation(stack.getTag().getString(EntityMagicNetEmpty.NBT_ENTITYID)))
          .create(world);
      //    entity.egg
      entity.read(stack.getTag());
      entity.setPosition(pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5);
      if (world.addEntity(entity)) {
        //eat up that stack
        stack.setTag(null);
        stack.shrink(1);
        if (stack.isEmpty()) {
          player.setHeldItem(context.getHand(), ItemStack.EMPTY);
        }
        if (!player.isCreative()) {
          //and replace with empty 
          //if config says drop?
          player.dropItem(new ItemStack(ItemRegistry.magic_net), true);
        }
        return ActionResultType.SUCCESS;
      }
    }
    return ActionResultType.PASS;
  }
}
