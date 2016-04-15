package com.lothrazar.cyclicmagic.proxy;

import java.util.Collection;
import org.lwjgl.input.Keyboard;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import com.google.common.collect.Ordering;
import com.lothrazar.cyclicmagic.entity.projectile.*;
import com.lothrazar.cyclicmagic.gui.GuiSpellWheel;
import com.lothrazar.cyclicmagic.inventory.EventGuiInventory;
import com.lothrazar.cyclicmagic.potion.PotionCustom;
import com.lothrazar.cyclicmagic.registry.BlockRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.settings.KeyBinding;

public class ClientProxy extends CommonProxy {
	public static KeyBinding	keyShiftUp;
	public static KeyBinding	keyShiftDown;
	public static KeyBinding	keyBarUp;
	public static KeyBinding	keyBarDown;

	static final String				keyCategoryInventory	= "key.categories.inventorycontrol";

	private boolean doRenderPotions = false;

	@Override
	public World getClientWorld() {
		return FMLClientHandler.instance().getClient().theWorld;
	}	
	@Override
	public void registerEvents() {

		MinecraftForge.EVENT_BUS.register(new EventGuiInventory());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderPotions() {

		if(!doRenderPotions){
			return;
		}
		
		//the INTENTION of this was to.. fix/force the top right potion rendering.
		//somehow it doesnt work

    Collection<PotionEffect> collection = Minecraft.getMinecraft().thePlayer.getActivePotionEffects();
    Minecraft mc =  Minecraft.getMinecraft();

    ScaledResolution resolution = new ScaledResolution(mc);
 
        if (!collection.isEmpty())
        {
            GlStateManager.enableBlend();
            int i = 0;
            int j = 0;

            for (PotionEffect potioneffect : Ordering.natural().reverse().sortedCopy(collection))
            {
                Potion potion = potioneffect.getPotion();

                if (potion instanceof PotionCustom)
                {
                  
                    int xLoc = resolution.getScaledWidth()/2;
                    int yLoc = 1    +8;
                    //int i1 = potion.getStatusIconIndex();
                    float f = 1.0F;

                    if (potion.func_188408_i())
                    {
                        ++i;
                        xLoc = xLoc - 25 * i;
                    }
                    else
                    {
                        ++j;
                        xLoc = xLoc - 25 * j;
                        yLoc += 26;
                    }

                    
                    //??testing
                   
                    mc.getTextureManager().bindTexture(GuiContainer.inventoryBackground);
                    GlStateManager.enableBlend();
                    GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                    if (potioneffect.getIsAmbient())
                    {
                    	//the background with no border
                        mc.ingameGUI.drawTexturedModalRect(xLoc, yLoc, 165, 166, 24, 24);
                    }
                    else
                    {
                    	// background with blue border
                    	mc.ingameGUI.drawTexturedModalRect(xLoc, yLoc, 141, 166, 24, 24);

                        if (potioneffect.getDuration() <= 200)
                        {
                            int j1 = 10 - potioneffect.getDuration() / 20;
                            f = MathHelper.clamp_float((float)potioneffect.getDuration() / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + MathHelper.cos((float)potioneffect.getDuration() * (float)Math.PI / 5.0F) * MathHelper.clamp_float((float)j1 / 10.0F * 0.25F, 0.0F, 0.25F);
                        }
                    }

                    ResourceLocation pot = ((PotionCustom) potion).getIcon();
                  
                    mc.getTextureManager().bindTexture(  pot  	  );
                    GlStateManager.color(1.0F, 1.0F, 1.0F, f);
                    //dont hack in my potion texture to the bottom of inventory. we have standalone textures LIKE A BOSS
                    // i1 % 8 * 18, 198 + i1 / 8 * 18 	
                    mc.ingameGUI.drawTexturedModalRect(xLoc + 3, yLoc + 3,   0,0, 16, 16);
                }
            }
        }
    
    
		
	}
	
	@Override
	public void register() {

		registerModels();

		registerKeys();

		registerEntities();
	}

	private void registerKeys() {

		keyShiftUp = new KeyBinding("key.columnshiftup", Keyboard.KEY_Y, keyCategoryInventory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyShiftUp);

		keyShiftDown = new KeyBinding("key.columnshiftdown", Keyboard.KEY_H, keyCategoryInventory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyShiftDown);

		keyBarUp = new KeyBinding("key.columnbarup", Keyboard.KEY_U, keyCategoryInventory);
		ClientRegistry.registerKeyBinding(ClientProxy.keyBarUp);

		keyBarDown = new KeyBinding("key.columnbardown", Keyboard.KEY_J, keyCategoryInventory);
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

		RenderingRegistry.registerEntityRenderingHandler(EntityLightningballBolt.class, new RenderSnowball(rm, ItemRegistry.ender_lightning, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityHarvestBolt.class, new RenderSnowball(rm, ItemRegistry.ender_harvest, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityWaterBolt.class, new RenderSnowball(rm, ItemRegistry.ender_water, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowballBolt.class, new RenderSnowball(rm, ItemRegistry.ender_snow, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityTorchBolt.class, new RenderSnowball(rm, ItemRegistry.ender_torch, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityFishingBolt.class, new RenderSnowball(rm, ItemRegistry.ender_fishing, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityShearingBolt.class, new RenderSnowball(rm, ItemRegistry.ender_wool, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityHomeBolt.class, new RenderSnowball(rm, ItemRegistry.ender_bed, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityDungeonEye.class, new RenderSnowball(rm, ItemRegistry.ender_dungeon, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityDynamite.class, new RenderSnowball(rm, ItemRegistry.ender_tnt_1, ri));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeBolt.class, new RenderSnowball(rm, ItemRegistry.ender_blaze, ri));

	}

	@SideOnly(Side.CLIENT)
	@Override
	public void displayGuiSpellbook() {

		Minecraft.getMinecraft().displayGuiScreen(new GuiSpellWheel(Minecraft.getMinecraft().thePlayer));
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

		for (Item i : ItemRegistry.items) {
			name = Const.MODRES + i.getUnlocalizedName().replaceAll("item.", "");

			mesher.register(i, 0, new ModelResourceLocation(name, "inventory"));
		}
	}

	@SideOnly(Side.CLIENT)
	public static boolean isKeyDown(KeyBinding keybinding) {

		// inside a GUI , we have to check the keyboard directly
		// thanks to Inventory tweaks, reminding me of alternate way to check
		// keydown while in config
		// https://github.com/Inventory-Tweaks/inventory-tweaks/blob/develop/src/main/java/invtweaks/InvTweaks.java

		return keybinding.isPressed() || Keyboard.isKeyDown(keybinding.getKeyCode());
	}
}
