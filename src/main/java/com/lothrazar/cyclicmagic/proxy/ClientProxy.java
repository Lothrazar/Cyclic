package com.lothrazar.cyclicmagic.proxy;

import java.util.ArrayList;
import net.minecraft.item.Item;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import com.lothrazar.cyclicmagic.projectile.*;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.EntityList;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void register() {
		
		registerModels();

		registerEntities();
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private void registerEntities() {
		
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		RenderItem ri = Minecraft.getMinecraft().getRenderItem();
	    /**
	     * Register an entity rendering handler. This will, after mod initialization, be inserted into the main
	     * render map for entities.
	     * Call this during Initialization phase.
	     *
	     * @deprecated use the factory version during Preinitialization.
	     * Will be removed in 1.9.
	     */
		RenderingRegistry.registerEntityRenderingHandler(EntityTorchBolt.class, new RenderSnowball(rm, EntityTorchBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityFishingBolt.class, new RenderSnowball(rm, EntityFishingBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, new RenderSnowball(rm, EntityBlazeBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityLightningballBolt.class, new RenderSnowball(rm, EntityLightningballBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowballBolt.class, new RenderSnowball(rm, EntitySnowballBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, new RenderSnowball(rm, EntityBlazeBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, new RenderSnowball(rm, EntityDynamite.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterBolt.class, new RenderSnowball(rm, EntityWaterBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityShearingBolt.class, new RenderSnowball(rm, EntityShearingBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityHarvestBolt.class, new RenderSnowball(rm, EntityHarvestBolt.item, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityRespawnEgg.class, new RenderSnowball(rm, EntityRespawnEgg.item, ri));
	}

	@SuppressWarnings("deprecation")
	private void registerModels() {
		
		//with help from 
		// http://www.minecraftforge.net/forum/index.php?topic=32492.0
		//https://github.com/TheOnlySilverClaw/Birdmod/blob/master/src/main/java/silverclaw/birds/client/ClientProxyBirds.java
		// More info on proxy rendering
		// http://www.minecraftforge.net/forum/index.php?topic=27684.0
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod

		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		String name;

		for (Item i : ItemRegistry.items) {
			name = Const.TEXTURE_LOCATION + i.getUnlocalizedName().replaceAll("item.", "");

			mesher.register(i, 0, new ModelResourceLocation(name, "inventory"));
		}
		
		ArrayList<String> variants = new ArrayList<String>();

		for(ItemCyclicWand.Variant wandType : ItemCyclicWand.Variant.values()) {
        	name = wandType.getResource();
        	variants.add(name);
        	mesher.register(ItemRegistry.cyclic_wand, wandType.getMetadata(), new ModelResourceLocation(name , "inventory"));	
        }
        
        ModelBakery.addVariantName(ItemRegistry.cyclic_wand, variants.toArray(new String[variants.size()]));
        
		if(ItemRegistry.respawn_egg != null) { 
			for(Object key : EntityList.entityEggs.keySet()) {
				mesher.register(ItemRegistry.respawn_egg, 
						(Integer)key, 
						new ModelResourceLocation(Const.TEXTURE_LOCATION + "respawn_egg" ,"inventory")); 
			} 
		}
	}
}
