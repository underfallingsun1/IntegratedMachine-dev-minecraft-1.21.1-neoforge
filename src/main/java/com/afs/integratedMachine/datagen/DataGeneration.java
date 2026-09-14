package com.afs.integratedMachine.datagen;

import com.afs.integratedMachine.datagen.lang.EnUs;
import com.afs.integratedMachine.datagen.lang.ZhCn;
import com.afs.integratedMachine.datagen.lootTables.BlockLootTables;
import com.afs.integratedMachine.datagen.model.BlockModels;
import com.afs.integratedMachine.datagen.model.ItemModels;
import com.afs.integratedMachine.datagen.recipes.Recipes;
import com.afs.integratedMachine.datagen.tag.BTags;
import com.afs.integratedMachine.datagen.tag.FTags;
import com.afs.integratedMachine.datagen.tag.ITags;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = Meta.MODID)
public class DataGeneration {
    @SubscribeEvent
    public static void generateData(GatherDataEvent e){
        e.createProvider(EnUs::new);
        e.createProvider(ZhCn::new);
        e.createProvider(output -> new ItemModels(output, e.getExistingFileHelper()));
        e.createProvider(output -> new BlockModels(output, e.getExistingFileHelper()));
        e.createBlockAndItemTags((output, registry)
                -> new BTags(output, registry, e.getExistingFileHelper()), ITags::new);
        e.createProvider((output, registry)
                -> new FTags(output, registry, e.getExistingFileHelper()));
        e.createProvider((output, registry) -> new LootTableProvider(
                output, Set.of(), List.of(
                            new LootTableProvider.SubProviderEntry(BlockLootTables::new, LootContextParamSets.BLOCK)
                        ), registry
        ));
        e.createProvider(Recipes::new);
        e.createProvider(DataMaps::new);
    }
}
