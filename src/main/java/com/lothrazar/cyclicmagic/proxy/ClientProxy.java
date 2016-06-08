package com.lothrazar.cyclicmagic.proxy;

import org.lwjgl.input.Keyboard;

import com.lothrazar.cyclicmagic.entity.projectile.EntityBlazeBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDungeonEye;
import com.lothrazar.cyclicmagic.entity.projectile.EntityDynamite;
import com.lothrazar.cyclicmagic.entity.projectile.EntityFishingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityHarvestBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityLightningballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityShearingBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntitySnowballBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityTorchBolt;
import com.lothrazar.cyclicmagic.entity.projectile.EntityWaterBolt;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.registry.CapabilityRegistry.IPlayerExtendedProperties;
import com.lothrazar.cyclicmagic.util.Const;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy {
	public static KeyBinding	keyShiftUp;
	public static KeyBinding	keyShiftDown;
	public static KeyBinding	keyBarUp;
	public static KeyBinding	keyBarDown;

	static final String				keyCategoryInventory	= "key.categories.inventorycontrol";
 
	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}	
	
	@Override
	public void register() {

		registerModels();

		registerKeys();

		registerEntities();
	}

	private void registerKeys() {

		//  public KeyBinding(String description, net.minecraftforge.client.settings.IKeyConflictContext keyConflictContext, net.minecraftforge.client.settings.KeyModifier keyModifier, int keyCode, String category)
	    
		keyShiftUp = new KeyBinding("key.columnshiftup", Keyboard.KEY_Y, keyCategoryInventory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyShiftUp);

		keyShiftDown = new KeyBinding("key.columnshiftdown", Keyboard.KEY_H, keyCategoryInventory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyShiftDown);

		keyBarUp = new KeyBinding("key.columnbarup", Keyboard.KEY_Y, keyCategoryInventory);
		keyBarUp.setKeyModifierAndCode(KeyModifier.SHIFT, Keyboard.KEY_Y);
		ClientRegistry.registerKeyBinding(ClientProxy.keyBarUp);

		keyBarDown = new KeyBinding("key.columnbardown", Keyboard.KEY_H, keyCategoryInventory);
		keyBarDown.setKeyModifierAndCode(KeyModifier.SHIFT, Keyboard.KEY_H);
		ClientRegistry.registerKeyBinding(ClientProxy.keyBarDown);
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
	private void registerEntities() {

		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		RenderItem ri = Minecraft.getMinecraft().getRenderItem();

		// works similar to vanilla which is like
		// Minecraft.getMinecraft().getRenderManager().entityRenderMap.put(EntitySoulstoneBolt.class,
		// new RenderSnowball(Minecraft.getMinecraft().getRenderManager(),
		// ItemRegistry.soulstone,
		// Minecraft.getMinecraft().getRenderItem()));

		RenderingRegistry.registerEntityRenderingHandler(EntityLightningballBolt.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_lightning"), ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityHarvestBolt.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_harvest"), ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterBolt.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_water"), ri));
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowballBolt.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_snow"), ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityTorchBolt.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_torch"), ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityFishingBolt.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_fishing"), ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityShearingBolt.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_wool"), ri));
		//RenderingRegistry.registerEntityRenderingHandler(EntityHomeBolt.class, new RenderSnowball(rm, ItemRegistry.ModItems.ender_bed, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityDungeonEye.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_dungeon"), ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_tnt_1"), ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, new RenderSnowball(rm, ItemRegistry.itemMap.get("ender_blaze"), ri));

	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumFacing getSideMouseover(int max) {

		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if (mouseOver != null) {

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			return mouseOver.sideHit;

		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockPos getBlockMouseoverExact(int max) {

		// Get the player and their held item

		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if (mouseOver != null) {

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			return mouseOver.getBlockPos();

		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockPos getBlockMouseoverOffset(int max) {

		// Get the player and their held item
		EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

		// int max = 50;
		RayTraceResult mouseOver = Minecraft.getMinecraft().getRenderViewEntity().rayTrace(max, 1f);
		// now get whatever block position we are mousing over if anything

		if (mouseOver != null && mouseOver.sideHit != null) {

			// Get the block position and make sure it is a block
			// World world = player.worldObj;
			BlockPos blockPos = mouseOver.getBlockPos();

			if (blockPos != null && player.worldObj.getBlockState(blockPos) != null) {

			return blockPos.offset(mouseOver.sideHit); }
		}
		return null;
	}

	private void registerModels() {

		// with help from
		// http://www.minecraftforge.net/forum/index.php?topic=32492.0
		// https://github.com/TheOnlySilverClaw/Birdmod/blob/master/src/main/java/silverclaw/birds/client/ClientProxyBirds.java
		// More info on proxy rendering
		// http://www.minecraftforge.net/forum/index.php?topic=27684.0
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2272349-lessons-from-my-first-mc-1-8-mod

		ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		String name;
		Item item;
		for (Block b : BlockRegistry.blocks) {
			item = Item.getItemFromBlock(b);
			name = Const.MODRES + b.getUnlocalizedName().replaceAll("tile.", "");

			mesher.register(item, 0, new ModelResourceLocation(name, "inventory"));
		}
 
		for (String key : ItemRegistry.itemMap.keySet()) {
			item =  ItemRegistry.itemMap.get(key);
			name = Const.MODRES + item.getUnlocalizedName().replaceAll("item.", "");

			mesher.register(item, 0, new ModelResourceLocation(name, "inventory"));
		}
	}
 
	@SideOnly(Side.CLIENT)
	public void setClientPlayerData(NBTTagCompound tags) {
 
		IPlayerExtendedProperties props = CapabilityRegistry.getPlayerProperties(Minecraft.getMinecraft().thePlayer);
		if(props != null){
			props.setDataFromNBT(tags);
		}
	} 
	
//	@SideOnly(Side.CLIENT)
//	public static boolean isKeyDown(KeyBinding keybinding) {
//
//		// inside a GUI , we have to check the keyboard directly
//		// thanks to Inventory tweaks, reminding me of alternate way to check
//		// keydown while in config
//		// https://github.com/Inventory-Tweaks/inventory-tweaks/blob/develop/src/main/java/invtweaks/InvTweaks.java
//
//		return keybinding.isPressed() || Keyboard.isKeyDown(keybinding.getKeyCode());
//	}
}
