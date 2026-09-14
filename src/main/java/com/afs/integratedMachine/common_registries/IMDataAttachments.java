package com.afs.integratedMachine.common_registries;

import com.afs.integratedMachine.compartment.handle.CompartmentManager;
import com.afs.integratedMachine.utils.Meta;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class IMDataAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Meta.MODID);

    public static final Supplier<AttachmentType<CompartmentManager>> COMPARTMENTS =
            ATTACHMENT_TYPES.register("compartments", () -> AttachmentType.serializable(
                    CompartmentManager::new
            ).build());
}
