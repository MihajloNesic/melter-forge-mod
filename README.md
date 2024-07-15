# Melter

A simple melter block that turn blocks into liquid.

Intended for modpacks. Use as you see fit.

Not providing any recipe at the moment.

Designed to work with create but it's optional.


Huge thanks to [MihajloNesic](https://github.com/MihajloNesic) for the big contribution to the mod.

## Heat sources.
- See JEI

### Heat source config
```
#Settings for the melter
[melter]
#How much liquid fits into the melter, in mB
#Range: > 1
capacity = 1000
#How much liquid generates per tick, in mB
#Range: > 1
liquidPerTick = 2
#List of heat source blocks or fluids. Each element in a list must follow the order: type (block, fluid), name, heat level (1-10), additional information shown in JEI
heatSources = [
	["block", "minecraft:torch", "1", ""],
	["block", "minecraft:soul_torch", "1", ""],
	["block", "minecraft:wall_torch", "1", ""],
	["block", "minecraft:soul_wall_torch", "1", ""],
	["block", "minecraft:fire", "2", ""],
	["block", "minecraft:soul_fire", "2", ""],
	["block", "minecraft:campfire", "2", ""],
	["block", "minecraft:soul_campfire", "2", ""],
	["block", "minecraft:magma_block", "3", ""],
	["fluid", "minecraft:lava", "4", ""],
	["block", "create:lit_blaze_burner", "2", "Lit"],
	["block", "create:blaze_burner/fading", "3", "Heated"],
	["block", "create:blaze_burner/kindled", "3", "Heated"],
	["block", "create:blaze_burner/seething", "5", "Super-Heated"]
]
```

## Recipe example:
```
{
  "type": "melter:melting",
  "input": {
    "tag": "forge:cobblestone",
    "count": 1
  },
  "output": {
    "fluid": "minecraft:lava",
    "amount": 250
  },
  "processingTime": 500
}
```

### with Minimum Heat
```
{
  "type": "melter:melting",
  "input": {
    "tag": "forge:cobblestone",
    "count": 1
  },
  "output": {
    "fluid": "minecraft:lava",
    "amount": 250
  },
  "processingTime": 500,
  "minimumHeat": 8
}
```

## KubeJS Integration
```
//.melterMelting(OUTPUT_FLUID,INPUT_BLOCK).processingTime(INT);
//.minimumHeat(INT) OPTIONAL
event.recipes.melterMelting(Fluid.of('minecraft:water', 200),"#minecraft:leaves").processingTime(200); //Water generator
event.recipes.melterMelting(Fluid.of('minecraft:lava', 250),"#forge:cobblestone").processingTime(1000); //Lava generator
event.recipes.melterMelting(Fluid.of('minecraft:lava', 250),"#forge:cobblestone").processingTime(1000).minimumHeat(8); //Lava generator
```

## CraftTweaker Integration (1.19.2, 1.20.1)
```

//addRecipe(String name, FluidStack outputFluid,Item inputItem, int processingTime, int minimumHeat)

<recipetype:melter:melting>.addRecipe("test_recipe",<fluid:minecraft:water> * 500,<item:minecraft:gravel>, 1000,8);
<recipetype:melter:melting>.addRecipe("test_recipe_2",<fluid:minecraft:lava> * 500,<item:minecraft:sand>, 200,2);

```