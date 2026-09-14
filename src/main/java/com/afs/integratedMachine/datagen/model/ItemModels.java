package com.afs.integratedMachine.datagen.model;

import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.item.XpGemItem;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ItemModels extends ItemModelProvider {
    public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Meta.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(IMItems.POWERED_INGOT.get());
        basicItem(IMItems.SHINING_INGOT.get());
        basicItem(IMItems.END_INGOT.get());
        basicItem(IMItems.CRYSTALLIZED_INGOT.get());
        basicItem(IMItems.EXTREME_INGOT.get());
        basicItem(IMItems.STEEL_INGOT.get());
        basicItem(IMItems.LIQUID_EXPERIENCE_BUCKET.get());
        xpGen();
    }

    private void xpGen(){
        ResourceLocation item = IMItems.XP_GEM.getKey().location();
        getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc("item/empty_" + item.getPath()))
                .override()
                .predicate(XpGemItem.XP_GEM_PERCENTAGE, 0.2f)
                .model(
                        getBuilder(item.withPrefix("item/xp_gem/almost_empty_").toString())
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", modLoc("item/almost_empty_" + item.getPath()))
                )
                .end()
                .override()
                .predicate(XpGemItem.XP_GEM_PERCENTAGE, 0.5f)
                .model(
                        getBuilder(item.withPrefix("item/xp_gem/half_").toString())
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", modLoc("item/half_" + item.getPath()))
                )
                .end()
                .override()
                .predicate(XpGemItem.XP_GEM_PERCENTAGE, 0.8f)
                .model(
                        getBuilder(item.withPrefix("item/xp_gem/almost_full").toString())
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", modLoc("item/almost_full_" + item.getPath()))
                )
                .end()
                .override()
                .predicate(XpGemItem.XP_GEM_PERCENTAGE, 1.0f)
                .model(
                        getBuilder(item.withPrefix("item/xp_gem/full_").toString())
                                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                                .texture("layer0", modLoc("item/" + item.getPath()))
                )
                .end();;
    }
}
