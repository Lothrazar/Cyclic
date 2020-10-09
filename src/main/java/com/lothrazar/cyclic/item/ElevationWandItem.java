package com.lothrazar.cyclic.item;

import com.lothrazar.cyclic.ModCyclic;
import com.lothrazar.cyclic.base.ItemBase;
import com.lothrazar.cyclic.util.UtilEntity;
import com.lothrazar.cyclic.util.UtilSound;
import com.lothrazar.cyclic.util.UtilWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ElevationWandItem extends ItemBase {
    public ElevationWandItem(Properties properties) {
        super(properties);
    }

    @Override
    @Nonnull
    public ActionResultType itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity target, Hand hand) {
        return tryTeleport(playerIn.world, playerIn, target, stack) ? ActionResultType.SUCCESS : ActionResultType.CONSUME;
    }

    @Override
    @Nonnull
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        return tryTeleport(worldIn, playerIn, playerIn, playerIn.getHeldItem(handIn)) ?
                ActionResult.resultSuccess(playerIn.getHeldItem(handIn)) : ActionResult.resultConsume(playerIn.getHeldItem(handIn));
    }

    private boolean tryTeleport(World world, PlayerEntity playerIn, LivingEntity target, ItemStack stack) {
        if (target == null || stack == null)
            return false;
        BlockPos destination = UtilWorld.getFirstBlockAbove(world, target.getPosition());
        if (destination != null) {
            if (playerIn.world.isRemote)
                UtilSound.playSound(playerIn, target.getPosition(), SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT);
            else {
                UtilEntity.teleportWallSafe(target, world, destination);
                stack.attemptDamageItem(1, world.rand, (ServerPlayerEntity) playerIn);
            }
            return true;
        }
        if (playerIn.world.isRemote)
            UtilSound.playSound(playerIn, target.getPosition(), SoundEvents.BLOCK_FIRE_EXTINGUISH);
        return false;
    }
}
