package com.afs.integratedMachine.datagen.recipes;

import com.afs.integratedMachine.datagen.advancement.AdvancementUtils;
import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.utils.Utils;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

public class ShapedRecipeSubProvider {
    public static void run(RecipeOutput output){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, IMItems.EXPERIENCE_CONVERTER.get())
                .pattern("IGI")
                .pattern("IXI")
                .pattern("IRI")
                .define('I', IMItems.STEEL_INGOT.get())
                .define('G', IMItems.XP_GEM.get())
                .define('X', Items.EXPERIENCE_BOTTLE)
                .define('R', Items.REDSTONE_BLOCK)
                .unlockedBy("has_xp_gem", AdvancementUtils.hasItem(IMItems.XP_GEM.get()))
                .save(output);
    }
}
