package com.afs.integratedMachine.client.fluid;

import com.afs.integratedMachine.fluid.IMFluids;
import com.afs.integratedMachine.utils.Meta;
import com.afs.integratedMachine.utils.Utils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

@EventBusSubscriber(modid = Meta.MODID, value = Dist.CLIENT)
public class LiquidExperienceClientExtensions {
    private static final IClientFluidTypeExtensions EXTENSIONS = new IClientFluidTypeExtensions() {
        @Override
        public ResourceLocation getStillTexture(){
            return Utils.modLoc("block/liquid_experience_still");
        }

        @Override
        public ResourceLocation getFlowingTexture(){
            return Utils.modLoc("block/liquid_experience_flow");
        }
    };

    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event){
        event.registerFluidType(EXTENSIONS, IMFluids.LIQUID_EXPERIENCE_TYPE.value());
    }
}
