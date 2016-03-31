package com.lothrazar.cyclicmagic.proxy;

import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.RayTraceResult;
import com.lothrazar.cyclicmagic.ItemRegistry;
import com.lothrazar.cyclicmagic.Const;
import com.lothrazar.cyclicmagic.gui.GuiSpellbook;
import com.lothrazar.cyclicmagic.item.ItemCyclicWand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;

public class ClientProxy extends CommonProxy{

	@Override
	public void register(){

		registerModels();
 
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayGuiSpellbook(){

		Minecraft.getMinecraft().displayGuiScreen(new GuiSpellbook(Minecraft.getMinecraft().thePlayer));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumFacing getSideMouseover(int max){

		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if(mouseOver != null){

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			return mouseOver.sideHit;

		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockPos getBlockMouseoverExact(int max){

		// Get the player and their held item

		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if(mouseOver != null){

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			return mouseOver.getBlockPos();

		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockPos getBlockMouseoverOffset(int max){

		// Get the player and their held item
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		// int max = 50;
		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if(mouseOver != null && mouseOver.sideHit != null){

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			BlockPos blockPos = mouseOver.getBlockPos();

			if(blockPos != null && player.worldObj.getBlockState(blockPos) != null){

				return blockPos.offset(mouseOver.sideHit);
			}
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	private void registerModels(){

		// with help from
		// http://www.minecraftforge.net/forum/index.php?topic=32492.0
		// https://github.com/TheOnlySilverClaw/Birdmod/blob/master/src/main/java/silverclaw/birds/client/ClientProxyBirds.java
		// More info on proxy rendering
		// http://www.minecraftforge.net/forum/index.php?topic=27684.0
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod

		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		String name;

		for(Item i : ItemRegistry.items){
			name = Const.TEXTURE_LOCATION + i.getUnlocalizedName().replaceAll("item.", "");

			mesher.register(i, 0, new ModelResourceLocation(name, "inventory"));
		}
/*
		ArrayList<String> variants = new ArrayList<String>();

		for(ItemCyclicWand.Variant wandType : ItemCyclicWand.Variant.values()){
			name = wandType.getResource();
			variants.add(name);
			mesher.register(ItemRegistry.cyclic_wand, wandType.getMetadata(), new ModelResourceLocation(name, "inventory"));
		}

		System.out.println("TODO: need resource locs here to register");
		*/
		
		//ModelBakery.registerItemVariants(ItemRegistry.cyclic_wand, variants.toArray(new String[variants.size()]));

	}
}
