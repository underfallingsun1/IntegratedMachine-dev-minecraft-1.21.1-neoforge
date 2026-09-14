package com.afs.integratedMachine.compartment.handle;

import com.afs.integratedMachine.compartment.Compartment;
import com.afs.integratedMachine.utils.Meta;
import com.afs.integratedMachine.utils.Utils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

public class CompartmentList extends SavedData {
    public static final Factory<CompartmentList> FACTORY =
            new Factory<>(CompartmentList::new, CompartmentList::load);

    private final Int2ObjectOpenHashMap<Compartment> compartments;
    private int id = 0;

    public CompartmentList(){
        this.compartments = new Int2ObjectOpenHashMap<>();
        this.id = 0;
    }

    public CompartmentList(int id, Int2ObjectOpenHashMap<Compartment> compartments) {
        this.compartments = compartments;
        this.id = id;
    }

    public Compartment get(int id){
        return compartments.getOrDefault(id, Compartment.EMPTY);
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
        tag.putInt("id", id);
        ListTag cs = new ListTag();
        for(var entry: compartments.int2ObjectEntrySet()){
            CompoundTag cEntry = new CompoundTag();
            cEntry.putInt("id", entry.getIntKey());
            cEntry.put("compartment", entry.getValue().save(registries));
        }
        tag.put("compartments", cs);
        return tag;
    }

    public static CompartmentList load(CompoundTag tag, HolderLookup.Provider registries){
        int id = tag.getInt("id");
        ListTag entries = tag.getList("compartments", Tag.TAG_COMPOUND);
        Int2ObjectOpenHashMap<Compartment> map = new Int2ObjectOpenHashMap<>();
        for(Tag entry: entries){
            CompoundTag e = (CompoundTag) entry;
            map.put(e.getInt("id"), Compartment.load(e.getCompound("compartment"), registries));
        }
        return new CompartmentList(id, map);
    }

    public record Reference(ResourceKey<Level> dimension, int id){
        public static final Reference EMPTY = new Reference(null, -1);

        public static final Codec<Reference> CODEC = RecordCodecBuilder.create(
                inst -> inst.group(
                        ResourceKey.codec(Registries.DIMENSION).fieldOf("dimension").forGetter(Reference::dimension),
                        Codec.INT.fieldOf("id").forGetter(Reference::id)
                ).apply(inst, Reference::new)
        );

        public static final StreamCodec<ByteBuf, Reference> STREAM_CODEC = StreamCodec.composite(
                ResourceKey.streamCodec(Registries.DIMENSION), Reference::dimension,
                ByteBufCodecs.INT, Reference::id,
                Reference::new
        );

        public Compartment get(){
            if(this == EMPTY){
                return Compartment.EMPTY;
            }
            if(Utils.inServer()){
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if(server == null){
                    throw new IllegalStateException("server should be always here!");
                }
                ServerLevel level = server.getLevel(dimension);
                if(level == null){
                    Meta.LOGGER.warn("unknown dimension in reference: {}", dimension.location());
                    return Compartment.EMPTY;
                }
                CompartmentList compartments = level.getDataStorage().computeIfAbsent(
                        CompartmentList.FACTORY, "compartments"
                );
                return compartments.get(id);
            }
            else {
                return ClientCompartmentHandle.INSTANCE.get(this);
            }
        }

        public boolean isValid(){
            if(this == EMPTY){
                return false;
            }
            if(Utils.inServer()){
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if(server == null){
                    throw new IllegalStateException("server should be always here!");
                }
                ServerLevel level = server.getLevel(dimension);
                if(level == null){
                    return false;
                }
                CompartmentList compartments = level.getDataStorage().computeIfAbsent(
                        CompartmentList.FACTORY, "compartments"
                );
                return compartments.compartments.containsKey(id);
            }
            else {
                return ClientCompartmentHandle.INSTANCE.get(this) != Compartment.EMPTY;
            }
        }
    }
}
