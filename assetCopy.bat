:: useage 
:: $ ./assetCopy.bat blockstates end_gold_ore.json copper_nether_ore.json 
:: $ ./assetCopy.bat models/block end_gold_ore.json copper_nether_ore.json
set folder=%1
set file=%2
set dest=%3

cp src/main/resources/assets/cyclicmagic/%folder%/%file% src/main/resources/assets/cyclicmagic/%folder%/%dest%
 
ECHO Created file %dest%
