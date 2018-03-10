:: useage 
:: $ ./assetCopyORES.bat silver
 
set dest=%1

cp src/main/resources/assets/cyclicmagic/blockstates/copper_end_ore.json src/main/resources/assets/cyclicmagic/blockstates/%dest%_end_ore.json
cp src/main/resources/assets/cyclicmagic/blockstates/copper_nether_ore.json src/main/resources/assets/cyclicmagic/blockstates/%dest%_nether_ore.json
cp src/main/resources/assets/cyclicmagic/models/block/copper_end_ore.json src/main/resources/assets/cyclicmagic/models/block/%dest%_end_ore.json
cp src/main/resources/assets/cyclicmagic/models/block/copper_nether_ore.json src/main/resources/assets/cyclicmagic/models/block/%dest%_nether_ore.json
 
