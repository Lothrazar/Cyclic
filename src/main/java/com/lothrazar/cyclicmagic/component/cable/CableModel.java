package com.lothrazar.cyclicmagic.component.cable;

import com.lothrazar.cyclicmagic.SimpleCable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = SimpleCable.ID, value = Side.CLIENT)
public final class CableModel {

    @SideOnly(Side.CLIENT)
    private static final ModelResourceLocation ITEM_MODEL = new ModelResourceLocation(
            new ResourceLocation(SimpleCable.ID, "cable"), "inventory"
    );

    @SideOnly(Side.CLIENT)
    private static final IStateMapper STATE_MAPPER = new StateMapperBase() {
        @Override
        protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
            return new ModelResourceLocation(state.getBlock().getRegistryName(), "normal");
        }
    };

    private CableModel() {}

    @SubscribeEvent
    public static void onModelRegistry(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(SimpleCable.CABLE_ITEM, 0, ITEM_MODEL);
        ModelLoader.setCustomStateMapper(SimpleCable.CABLE_BLOCK, STATE_MAPPER);
    }

}
