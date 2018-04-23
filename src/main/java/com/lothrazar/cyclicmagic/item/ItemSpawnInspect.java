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
package com.lothrazar.cyclicmagic.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.lothrazar.cyclicmagic.IHasRecipe;
import com.lothrazar.cyclicmagic.core.item.BaseTool;
import com.lothrazar.cyclicmagic.core.registry.RecipeRegistry;
import com.lothrazar.cyclicmagic.util.UtilChat;
import com.lothrazar.cyclicmagic.util.data.Const;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;

public class ItemSpawnInspect extends BaseTool implements IHasRecipe {

  private static final int DURABILITY = 2000;
  private static final int COOLDOWN = 10;

  public ItemSpawnInspect() {
    super(DURABILITY);
  }

  @Override
  public EnumActionResult onItemUse(EntityPlayer player, World worldObj, BlockPos posIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
    ItemStack stack = player.getHeldItem(hand);
    if (side == null || posIn == null || side != EnumFacing.UP) {
      if (!worldObj.isRemote) {
        UtilChat.addChatMessage(player, "item.tool_spawn_inspect.up");
      }
      return super.onItemUse(player, worldObj, posIn, hand, side, hitX, hitY, hitZ);
    }
    boolean showOdds = player.isSneaking();
    if (!worldObj.isRemote) {
      ChunkProviderServer s = (ChunkProviderServer) worldObj.getChunkProvider();
      BlockPos pos = posIn.offset(side);
      Chunk chunk = worldObj.getChunkFromBlockCoords(pos);
      if (worldObj.getChunkProvider() instanceof ChunkProviderServer) {
        List<SpawnDetail> names = new ArrayList<SpawnDetail>();
        for (EnumCreatureType creatureType : EnumCreatureType.values()) {
          List<Biome.SpawnListEntry> list = s.getPossibleCreatures(creatureType, pos);
          for (Biome.SpawnListEntry entry : list) {
            if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntitySpawnPlacementRegistry.getPlacementForEntity(entry.entityClass), worldObj, pos)) {
              names.add(new SpawnDetail(entry, creatureType));
              //hack since witherskeleton is not its own class/entry, just a mob modifier like zombietypes or villagertypes
              if (entry.entityClass.equals(EntitySkeleton.class) && player.dimension == Const.Dimension.nether) {
                names.add(new SpawnDetail("WitherSkeleton", creatureType, entry.itemWeight));
              }
            }
          }
        }
        if (names.size() > 0) {
          String strLight = "Light: " + chunk.getLightSubtracted(pos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, pos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, pos) + " block)";
          UtilChat.addChatMessage(player, strLight);
          Collections.sort(names, new Comparator<SpawnDetail>() {

            @Override
            public int compare(SpawnDetail o1, SpawnDetail o2) {
              return o1.getSortBy().compareTo(o2.getSortBy());
            }
          });
          List<String> csv = new ArrayList<String>();
          for (SpawnDetail detail : names) {
            csv.add(detail.toString(showOdds));
          }
          UtilChat.addChatMessage(player, String.join(", ", csv));
        }
        else {
          UtilChat.addChatMessage(player, "item.tool_spawn_inspect.empty");
        }
      }
    }
    player.getCooldownTracker().setCooldown(this, COOLDOWN);
    super.onUse(stack, player, worldObj, hand);
    return super.onItemUse(player, worldObj, posIn, hand, side, hitX, hitY, hitZ);
  }

  @Override
  public IRecipe addRecipe() {
    return RecipeRegistry.addShapedRecipe(new ItemStack(this),
        " sg",
        " bs",
        "b  ",
        'b', new ItemStack(Items.BLAZE_ROD),
        's', new ItemStack(Items.FLINT),
        'g', "blockGlassPurple");
  }

  public static class SpawnDetail {

    private int itemWeight;
    private String displayName;
    private String creatureTypeName;

    //    private boolean lightEnabled = true;
    public SpawnDetail(Biome.SpawnListEntry entry, EnumCreatureType creatureType) {
      itemWeight = entry.itemWeight;
      creatureTypeName = creatureType.name();
      displayName = entry.entityClass.getSimpleName().replace("Entity", "");
      //one caveat: the above canSpawn ignores light level
      //      if (creatureType != EnumCreatureType.MONSTER) {
      //        //ambient/water/passive, all ignore light
      //        lightEnabled = true;
      //      }
      //      else {
      //        int reqLight = entry.entityClass == EntityBlaze.class ? Const.LIGHT_MOBSPAWN_BLAZE : Const.LIGHT_MOBSPAWN;
      //        if (currentLightLevel <= reqLight) {
      //          lightEnabled = true;
      //        }
      //      }
    }

    public SpawnDetail(String n, EnumCreatureType creatureType, int odds) {
      //special case of JUST witherywither
      itemWeight = odds;
      displayName = n;
      creatureTypeName = creatureType.name();
      //      if (currentLightLevel <= Const.LIGHT_MOBSPAWN) {
      //        lightEnabled = true;
      //      }
    }

    public String getSortBy() {
      return displayName;
    }

    public String toString(boolean showOdds) {
      //      TextFormatting color = (lightEnabled) ? TextFormatting.WHITE : TextFormatting.DARK_GRAY;
      if (showOdds) // todo; super.tostring here?
        return "[" + creatureTypeName + ", " + String.format("%03d", itemWeight) + "] " + displayName;
      else
        return displayName;
    }
  }
}
