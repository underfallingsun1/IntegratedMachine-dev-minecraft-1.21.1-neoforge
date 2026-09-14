package com.afs.integratedMachine.client.model.connectModel;

import com.afs.integratedMachine.utils.Meta;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConnectedModel implements IDynamicBakedModel {
    private static final ModelProperty<Long> QUAD_INDEX = new ModelProperty<>();
    public static final BlockPos[] ITER_LIST = new BlockPos[]{
            new BlockPos(-1, -1, -1), new BlockPos(-1, -1, 0),
            new BlockPos(-1, -1, 1), new BlockPos(-1, 0, -1),
            new BlockPos(-1, 0, 0), new BlockPos(-1, 0, 1),
            new BlockPos(-1, 1, -1), new BlockPos(-1, 1, 0),
            new BlockPos(-1, 1, 1), new BlockPos(0, -1, -1),
            new BlockPos(0, -1, 0), new BlockPos(0, -1, 1),
            new BlockPos(0, 0, -1), new BlockPos(0, 0, 1),
            new BlockPos(0, 1, -1), new BlockPos(0, 1, 0),
            new BlockPos(0, 1, 1), new BlockPos(1, -1, -1),
            new BlockPos(1, -1, 0), new BlockPos(1, -1, 1),
            new BlockPos(1, 0, -1), new BlockPos(1, 0, 0),
            new BlockPos(1, 0, 1), new BlockPos(1, 1, -1),
            new BlockPos(1, 1, 0), new BlockPos(1, 1, 1)
    };
    public static final long[] VALUES = new long[]{
            0L, 537395200L, 0L, 32800L, 1074020416L, 8320L, 0L, 2147614720L, 0L, 8933531975680L,
            17661175005184L, 35218731827200L, 4672924418320L, 70385924050945L, 2748779069440L,
            1103823437824L, 140746078289920L, 0L, 136314880L, 0L, 520L, 71304196L, 2050L,
            0L, 41943040L, 0L
    };
    public static final long[] FILTER_VALUES = new long[]{
            137439477792L, 68719476800L, 34896609408L, 274878169088L, 0L, 18253611008L, 549755977728L,
            4294983680L, 10737426432L, 1048592L, 0L, 268435457L, 0L, 0L, 65792L, 0L, 16781312L,
            8796095119368L, 17592186044420L, 35184506306562L, 4398050705408L, 0L, 70368811286528L,
            2199031644672L, 1099511628800L, 140737521911808L
    };
    public static final BlockPos[] NON_CORNER_ITER_LIST = new BlockPos[]{
            new BlockPos(-1, -1, 0), new BlockPos(-1, 0, -1),
            new BlockPos(-1, 0, 0), new BlockPos(-1, 0, 1),
            new BlockPos(-1, 1, 0), new BlockPos(0, -1, -1),
            new BlockPos(0, -1, 0), new BlockPos(0, -1, 1),
            new BlockPos(0, 0, -1), new BlockPos(0, 0, 1),
            new BlockPos(0, 1, -1), new BlockPos(0, 1, 0),
            new BlockPos(0, 1, 1), new BlockPos(1, -1, 0),
            new BlockPos(1, 0, -1), new BlockPos(1, 0, 0),
            new BlockPos(1, 0, 1), new BlockPos(1, 1, 0),
    };

    public static final long[] NON_CORNER_VALUES = new long[]{
            537395200L, 32800L, 1074020416L, 8320L, 2147614720L, 8933531975680L, 17661175005184L,
            35218731827200L, 4672924418320L, 70385924050945L, 2748779069440L, 1103823437824L,
            140746078289920L, 136314880L, 520L, 71304196L, 2050L, 41943040L
    };
    /*
    for the QUAD_INDEX, every 8 bits set for a direction, witch is below
    -------------------------------------------------------
    | Direction | DOWN | UP | NORTH | SOUTH | WEST | EAST |
    |-----------+------+----+-------+-------+------+------|
    | Slot      | 0    | 1  | 2     | 3     | 4    | 5    |
    -------------------------------------------------------
     */

    private final boolean useAO;
    private final boolean isGui3d;
    private final boolean useBlockLight;
    private final TextureAtlasSprite particle;
    private final List<ConnectPredicate> predicates;
    private final BakedQuad[][] quadMap;
    private final ItemOverrides overrides;
    private final boolean cornerCut;

    public ConnectedModel(boolean useAO, boolean isGui3d, boolean useBlockLight, TextureAtlasSprite particle,
                          List<ConnectPredicate> predicates, BakedQuad[][] quadMap, ItemOverrides overrides, boolean cornerCut) {
        this.useAO = useAO;
        this.isGui3d = isGui3d;
        this.useBlockLight = useBlockLight;
        this.particle = particle;
        this.predicates = predicates;
        this.quadMap = quadMap;
        this.overrides = overrides;
        this.cornerCut = cornerCut;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                    RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
        if(side == null || !extraData.has(QUAD_INDEX)){
            return List.of();
        }
        else{
            int index = (int)((extraData.get(QUAD_INDEX) >> (side.ordinal() * 8)) & 0xff);
            return List.of(quadMap[side.ordinal()][index]);
        }
    }

    @Override
    public ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData modelData) {

        long res = 0;
        if(cornerCut){
            long fv = 0;
            for(int i = 0;i < 26;i ++){
                if(mayConnect(level.getBlockState(pos.offset(ITER_LIST[i])))){
                    res |= VALUES[i];
                    fv |= FILTER_VALUES[i];
                }
            }
            res &= ~fv;
        }
        else{
            for(int i = 0;i < 18;i ++){
                if(mayConnect(level.getBlockState(pos.offset(NON_CORNER_ITER_LIST[i])))){
                    res |= NON_CORNER_VALUES[i];
                }
            }
        }
        return modelData.derive().with(QUAD_INDEX, res).build();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return useAO;
    }

    @Override
    public boolean isGui3d() {
        return isGui3d;
    }

    @Override
    public boolean usesBlockLight() {
        return useBlockLight;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particle;
    }

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }

    //only one predicate need to be fit.
    private boolean mayConnect(BlockState state){
        for(ConnectPredicate cp: predicates){
            if(cp.test(state)){
                return true;
            }
        }
        return false;
    }
}
