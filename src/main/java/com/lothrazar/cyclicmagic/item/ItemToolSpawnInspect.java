package com.lothrazar.cyclicmagic.item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.util.Const;
import com.lothrazar.cyclicmagic.util.UtilChat;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemToolSpawnInspect extends BaseTool implements IHasRecipe {
  private static final int durability = 2000;
  private static final int cooldown = 10;
  public ItemToolSpawnInspect() {
    super(durability);
  }
  @Override
  public EnumActionResult onItemUse(ItemStack stack, EntityPlayer player, World worldObj, BlockPos posIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    if(side == null || posIn == null){
      return super.onItemUse(stack, player, worldObj, posIn, hand, side, hitX, hitY, hitZ);
    }

    boolean showOdds = player.isSneaking();
    
    if (!worldObj.isRemote) {
      if(worldObj.getChunkProvider() instanceof ChunkProviderServer){
        ChunkProviderServer s = (ChunkProviderServer)worldObj.getChunkProvider();
        BlockPos pos = posIn.offset(side);

        int light = worldObj.getLight(pos);
        System.out.println("l "+light);
        List<SpawnDetail> names = new ArrayList<SpawnDetail>();
        for(EnumCreatureType creatureType : EnumCreatureType.values()){
          List<Biome.SpawnListEntry> list = s.getPossibleCreatures(creatureType, pos);
          for(Biome.SpawnListEntry entry : list){
            if(WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(entry.entityClass),  worldObj,pos     )){
              
              names.add(new SpawnDetail(entry, creatureType, light ));
           
            }
          }
        }
        if(names.size() > 0){
          Collections.sort(names, new Comparator<SpawnDetail>() {
            @Override
            public int compare(SpawnDetail o1, SpawnDetail o2) {
                return o1.getSortBy().compareTo(o2.getSortBy());
            }
          });
          for(SpawnDetail detail : names){
           UtilChat.addChatMessage(player, detail.toString(showOdds)); 
          }
        }
      }
    }
    player.getCooldownTracker().setCooldown(this, cooldown);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(stack, player, worldObj, posIn, hand, side, hitX, hitY, hitZ);
  }
  @Override
  public void addRecipe() {
    GameRegistry.addShapedRecipe(new ItemStack(this),
        "  g",
        " b ",
        "b  ",
        'b', new ItemStack(Items.BLAZE_ROD),
        'g', new ItemStack(Blocks.GLASS));
  }

  public static class SpawnDetail{
    private Biome.SpawnListEntry entry;
    private String displayName;
    private EnumCreatureType creatureType;
    private boolean lightEnabled = false;
    public SpawnDetail(Biome.SpawnListEntry pentry, EnumCreatureType ptype, int currentLightLevel){
      entry = pentry;
      creatureType = ptype;
      displayName = entry.entityClass.getSimpleName().replace("Entity", "");
      //one caveat: the above canSpawn ignores light level
      if(creatureType != EnumCreatureType.MONSTER){
        //ambient/water/passive, all ignore light
        lightEnabled = true;
      }
      else{
        int reqLight = entry.entityClass == EntityBlaze.class ? Const.LIGHT_MOBSPAWN_BLAZE : Const.LIGHT_MOBSPAWN;
        
        if( currentLightLevel  <= reqLight){
          lightEnabled = true;
        }
      }
    }
    public String getSortBy(){
      return displayName;
    }
    public String toString(boolean showOdds){
      TextFormatting color = (lightEnabled) ? TextFormatting.WHITE : TextFormatting.DARK_GRAY;
      if(showOdds)
        return  color + "["+creatureType.toString() +", "+String.format("%03d", entry.itemWeight) +"] " + displayName;
      else
        return  color +  displayName;
    }
  }
}
