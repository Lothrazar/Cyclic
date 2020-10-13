/*******************************************************************************
 * The MIT License (MIT)
 *
 * Copyright (C) 2014-2018 Sam Bassett (aka Lothrazar)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.lothrazar.cyclic.block.peatfarm;

import com.lothrazar.cyclic.base.BlockBase;
import com.lothrazar.cyclic.block.user.ScreenUser;
import com.lothrazar.cyclic.registry.ContainerScreenRegistry;
import com.lothrazar.cyclic.util.UtilSound;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockPeatFarm extends BlockBase {

    public BlockPeatFarm(Properties properties) {
        super(properties.harvestTool(ToolType.PICKAXE).hardnessAndResistance(1.2F)
                .notSolid());
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void registerClient() {
        ScreenManager.registerFactory(ContainerScreenRegistry.peat_farm, ScreenPeatFarm::new);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TilePeatFarm();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
        if (!world.isRemote) {
            TileEntity tankHere = world.getTileEntity(pos);
            if (tankHere != null) {
                IFluidHandler handler = tankHere.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, hit.getFace()).orElse(null);
                if (handler != null
                        && FluidUtil.interactWithFluidHandler(player, hand, handler)
                        && handler.getFluidInTank(0) != null) {
                    player.sendStatusMessage(new TranslationTextComponent(""
                        + handler.getFluidInTank(0).getAmount() + "/" + handler.getTankCapacity(0)), true);
                    if (player instanceof ServerPlayerEntity) {
                        UtilSound.playSoundFromServer((ServerPlayerEntity) player, SoundEvents.ITEM_BUCKET_FILL);
                    }
                }
                else {
                    TileEntity tileEntity = world.getTileEntity(pos);
                    if (tileEntity instanceof INamedContainerProvider) {
                        NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, tileEntity.getPos());
                    }
                    else {
                        throw new IllegalStateException("Our named container provider is missing!");
                    }
                    return ActionResultType.SUCCESS;
                }
            }
        }
        if (FluidUtil.getFluidHandler(player.getHeldItem(hand)).isPresent()) {
            return ActionResultType.SUCCESS;
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }
}
