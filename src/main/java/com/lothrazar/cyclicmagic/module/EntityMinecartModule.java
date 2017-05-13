package com.lothrazar.cyclicmagic.module;
import com.lothrazar.cyclicmagic.IHasConfig;
import com.lothrazar.cyclicmagic.entity.EntityGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.entity.EntityGoldMinecart;
import com.lothrazar.cyclicmagic.entity.EntityGoldMinecartChest;
import com.lothrazar.cyclicmagic.entity.EntityGoldMinecartDispenser;
import com.lothrazar.cyclicmagic.entity.EntityGoldMinecartDropper;
import com.lothrazar.cyclicmagic.entity.EntityMinecartTurret;
import com.lothrazar.cyclicmagic.entity.EntityStoneMinecart;
import com.lothrazar.cyclicmagic.item.ItemGoldFurnaceMinecart;
import com.lothrazar.cyclicmagic.item.ItemGoldMinecart;
import com.lothrazar.cyclicmagic.item.ItemStoneMinecart;
import com.lothrazar.cyclicmagic.item.ItemTurretMinecart;
import com.lothrazar.cyclicmagic.registry.EntityProjectileRegistry;
import com.lothrazar.cyclicmagic.registry.ItemRegistry;
import com.lothrazar.cyclicmagic.util.Const;
import net.minecraftforge.common.config.Configuration;

public class EntityMinecartModule extends BaseModule implements IHasConfig {
  private boolean goldMinecart;
  private boolean stoneMinecart;
  private boolean chestMinecart;
  private boolean dropperMinecart;
  private boolean dispenserMinecart;
  private boolean turretMinecart;
  @Override
  public void onPreInit() {
    if (goldMinecart) {
      ItemGoldMinecart gold_minecart = new ItemGoldMinecart();
      ItemRegistry.register(gold_minecart, "gold_minecart");
      EntityGoldMinecart.dropItem = gold_minecart;
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecart.class, "goldminecart", 1100);
      ItemGoldFurnaceMinecart gold_furnace_minecart = new ItemGoldFurnaceMinecart();
      ItemRegistry.register(gold_furnace_minecart, "gold_furnace_minecart");
      EntityGoldFurnaceMinecart.dropItem = gold_furnace_minecart;
      EntityProjectileRegistry.registerModEntity(EntityGoldFurnaceMinecart.class, "goldfurnaceminecart", 1101);
    }
    if (stoneMinecart) {
      ItemStoneMinecart stone_minecart = new ItemStoneMinecart();
      ItemRegistry.register(stone_minecart, "stone_minecart");
      EntityStoneMinecart.dropItem = stone_minecart;
      EntityProjectileRegistry.registerModEntity(EntityStoneMinecart.class, "stoneminecart", 1102);
    }
    if (chestMinecart) {
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecartChest.class, "goldchestminecart", 1103);
    }
    if (dropperMinecart) {
      //BROKEN:
      //it spawns entity in the world. so like an arrow, it flies to the arget but then magically teleports back o teh  cart position
      //stop for now
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecartDropper.class, "golddropperminecart", 1104);
    }
    if (dispenserMinecart) {
      EntityProjectileRegistry.registerModEntity(EntityGoldMinecartDispenser.class, "golddispenserminecart", 1105);
    }
    if(turretMinecart){

      ItemTurretMinecart turret_minecart = new ItemTurretMinecart();
      ItemRegistry.register(turret_minecart, "turret_minecart");
      EntityMinecartTurret.dropItem = turret_minecart;
      
    EntityProjectileRegistry.registerModEntity(EntityMinecartTurret.class, "turretminecart", 1106);
    
    
    }
    //if i have a mob on a LEAD< i can put it in a minecart with thehit
    //maybe 2 passengers..?? idk
    //connect together??
    //DISPENSERR minecart
    //??FLUID CART?
    //TURRET CART:? shoots arrows
    //ONE THAT CAN HOLD ANY ITEM
  }
  @Override
  public void syncConfig(Configuration config) {
    chestMinecart = false;// config.getBoolean("GoldChestMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dropperMinecart = false;//config.getBoolean("GoldDropperMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    dispenserMinecart = false;//config.getBoolean("GoldDispenserMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    turretMinecart = config.getBoolean("GoldTurretMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    goldMinecart = config.getBoolean("GoldMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
    stoneMinecart = config.getBoolean("StoneMinecart", Const.ConfigCategory.content, true, Const.ConfigCategory.contentDefaultText);
  }
}
