package com.afs.integratedMachine.compartment.property;

import com.afs.integratedMachine.utils.Meta;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CompartmentPropertyTypes {
    public static final DeferredRegister<MapCodec<? extends CompartmentProperty>> COMPARTMENT_PROPERTY_TYPES
            = DeferredRegister.create(CompartmentProperty.COMPARTMENT_PROPERTY_TYPE_REGISTRY, Meta.MODID);

    public static final Supplier<MapCodec<CompartmentPropertyRequirement>> REQUIREMENT =
            COMPARTMENT_PROPERTY_TYPES.register("requirement", () -> CompartmentPropertyRequirement.CODEC);

    public static final Supplier<MapCodec<CompartmentPropertyCalculator>> CALCULATOR =
            COMPARTMENT_PROPERTY_TYPES.register("calculator", () -> CompartmentPropertyCalculator.CODEC);

    public static final Supplier<MapCodec<CompartmentPropertyReference>> REFERENCE =
            COMPARTMENT_PROPERTY_TYPES.register("reference", () -> CompartmentPropertyReference.CODEC);

    public static final Supplier<MapCodec<CompartmentPropertyConstant>> CONSTANT =
            COMPARTMENT_PROPERTY_TYPES.register("constant", () -> CompartmentPropertyConstant.CODEC);

    public static final Supplier<MapCodec<CompartmentPropertyMapper>> MAPPER =
            COMPARTMENT_PROPERTY_TYPES.register("mapper", () -> CompartmentPropertyMapper.CODEC);

    public static final Supplier<MapCodec<CompartmentPropertyExpression>> EXPRESSION =
            COMPARTMENT_PROPERTY_TYPES.register("expression", () -> CompartmentPropertyExpression.CODEC);

    public static final Supplier<MapCodec<CompartmentPropertyClamp>> CLAMP =
            COMPARTMENT_PROPERTY_TYPES.register("clamp", () -> CompartmentPropertyClamp.CODEC);

    public static final Supplier<MapCodec<CompartmentPropertyOperator>> OPERATOR =
            COMPARTMENT_PROPERTY_TYPES.register("operator", () -> CompartmentPropertyOperator.CODEC);
}
