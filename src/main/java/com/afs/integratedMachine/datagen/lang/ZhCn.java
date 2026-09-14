package com.afs.integratedMachine.datagen.lang;

import com.afs.integratedMachine.block.IMBlocks;
import com.afs.integratedMachine.item.IMItems;
import com.afs.integratedMachine.utils.Meta;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import static com.afs.integratedMachine.utils.LangComps.*;

public class ZhCn extends LanguageProvider {
    public ZhCn(PackOutput output) {
        super(output, Meta.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add(TAB_TITLE.key(), "集成机械");

        add(XP_GEM_TOOLTIP.key(), "经验: %s/%s");

        add(UTILS_GROUP.key(), "杂项");
        add(XP_GEM_CAPACITY.key(), "经验宝石容量");
        add(EXPERIENCE_CONVERTER_FLUID_CACHE.key(), "经验转换器内部流体缓存");
        add(EXPERIENCE_CONVERTER_BUFFER.key(), "经验转换器缓存容量");
        add(EXPERIENCE_CONVERTER.key(), "经验转换器");

        add(ON.key(), "开");
        add(OFF.key(), "关");

        add(IMItems.POWERED_INGOT.get(), "充能金属锭");
        add(IMItems.SHINING_INGOT.get(), "发光金属锭");
        add(IMItems.END_INGOT.get(), "末影金属锭");
        add(IMItems.CRYSTALLIZED_INGOT.get(), "晶化金属锭");
        add(IMItems.EXTREME_INGOT.get(), "终极金属锭");
        add(IMItems.STEEL_INGOT.get(), "钢锭");

        add(IMItems.XP_GEM.get(), "经验宝石");

        add(IMItems.LIQUID_EXPERIENCE_BUCKET.get(), "液态经验桶");
        add(LIQUID_EXPERIENCE_FLUID.key(), "液态经验");

        add(IMBlocks.BASIC_COMPARTMENT_CONTROLLER.get(), "仓室控制器(基础)");
        add(IMBlocks.IRON_WALL.get(), "铁质墙壁");
        add(IMBlocks.XP_LANTERN.get(), "经验灯");
        add(IMBlocks.EXPERIENCE_CONVERTER.get(), "经验转换器");

        add(XP_STEP_INFINITY.key(), "无限制");
        add(XP_STEP_1L.key(), "1");
        add(XP_STEP_10L.key(), "10");
        add(XP_STEP_100L.key(), "100");
        add(XP_GEM_EXTRACT_MODE.key(), "吸收模式: %s");
        add(XP_GEM_XP_STEP.key(), "存入/取出的经验等级: %s");

        add(EXPERIENCE_CONVERTER_STORE.key(), "存入 %s 级经验");
        add(EXPERIENCE_CONVERTER_TAKE.key(), "取出 %s 级经验");
        add(EXPERIENCE_CONVERTER_ALL.key(), "全部");
        add(EXPERIENCE_CONVERTER_FLUID.key(), "液态经验: %s/%s mB");
        add(EXPERIENCE_CONVERTER_PRIORITY_FLUID.key(), "流体");
        add(EXPERIENCE_CONVERTER_PRIORITY_GEM.key(), "宝石");
        add(EXPERIENCE_CONVERTER_PRIORITY_TOOLTIP.key(), "优先级：优先填入流体容器并从经验宝石取出 / 优先填入经验宝石并从流体容器取出");
    }
}
