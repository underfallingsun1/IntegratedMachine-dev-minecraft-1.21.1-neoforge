package com.afs.integratedMachine.compartment.property;

import com.mojang.serialization.Codec;

import java.util.List;
import java.util.Map;

public enum AggregationType {
    SUM("sum"), AVERAGE("average"), MIN("min"), MAX("max"),
    MEDIAN("median"), MODE("mode");
    final String id;
    AggregationType(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }
    public static AggregationType of(String id){
        for (AggregationType aggregation : values()) {
            if (aggregation.id.equals(id)) {
                return aggregation;
            }
        }
        throw new IllegalArgumentException("unknown type: "+ id);
    }
    public int calculate(List<Integer> values){
        if (values.isEmpty()) {
            return 0;
        }
        switch (this) {
            case SUM:
                int sum = 0;
                for (int value : values) {
                    sum += value;
                }
                return sum;
            case AVERAGE:
                int total = 0;
                for (int value : values) {
                    total += value;
                }
                return total / values.size();
            case MIN:
                int min = values.get(0);
                for (int value : values) {
                    if (value < min) {
                        min = value;
                    }
                }
                return min;
            case MAX:
                int max = values.get(0);
                for (int value : values) {
                    if (value > max) {
                        max = value;
                    }
                }
                return max;
            case MEDIAN:
                List<Integer> sorted = values.stream().sorted().toList();
                int size = sorted.size();
                if (size % 2 == 0) {
                    return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2;
                }
                return sorted.get(size / 2);
            case MODE:
                Map<Integer, Integer> counts = new java.util.HashMap<>();
                for (int value : values) {
                    counts.merge(value, 1, Integer::sum);
                }
                int mode = values.get(0);
                int maxCount = 0;
                for (Map.Entry<Integer, Integer> entry : counts.entrySet()) {
                    if (entry.getValue() > maxCount) {
                        maxCount = entry.getValue();
                        mode = entry.getKey();
                    }
                }
                return mode;
            default:
                return 0;
        }
    }

    public static final Codec<AggregationType> CODEC = Codec.stringResolver(
            AggregationType::getId, AggregationType::of
    );
}
