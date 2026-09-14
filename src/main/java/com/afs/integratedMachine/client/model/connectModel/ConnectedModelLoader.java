package com.afs.integratedMachine.client.model.connectModel;

import com.afs.integratedMachine.utils.Utils;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import org.jline.utils.Log;

import java.util.ArrayList;
import java.util.List;

public class ConnectedModelLoader implements IGeometryLoader<ConnectedModelGeometry> {
    public static final ResourceLocation ID = Utils.modLoc("connect_model");
    public static final ConnectedModelLoader INSTANCE = new ConnectedModelLoader();

    @Override
    public ConnectedModelGeometry read(JsonObject jsonObject, JsonDeserializationContext deserializationContext)
            throws JsonParseException {
        String textureSetId = jsonObject.get("texture_set").getAsString();
        List<ConnectPredicate> predicates;
        JsonElement predicatesJson = jsonObject.get("requirements");
        if(predicatesJson == null){
            predicates = List.of();
        }
        else if(predicatesJson.isJsonArray()){
            predicates = new ArrayList<>();
            for(JsonElement e: predicatesJson.getAsJsonArray()){
                DataResult<Pair<ConnectPredicate, JsonElement>> data = ConnectPredicate.CODEC.decode(JsonOps.INSTANCE, e);
                if(data.isError()) Log.error("some error happens in connect property load!");
                else predicates.add(data.getOrThrow().getFirst());
            }
        }
        else if(predicatesJson.isJsonObject()){
            DataResult<Pair<ConnectPredicate, JsonElement>> data =
                    ConnectPredicate.CODEC.decode(JsonOps.INSTANCE, predicatesJson);
            if(data.isError()){
                Log.error("some error happens in connect property load!");
                predicates = List.of();
            }
            else predicates = List.of(data.getOrThrow().getFirst());
        }
        else {
            predicates = List.of();
        }
        boolean cornerCut = jsonObject.get("corner_cut").getAsBoolean();
        return new ConnectedModelGeometry(textureSetId, predicates, cornerCut);
    }
}
