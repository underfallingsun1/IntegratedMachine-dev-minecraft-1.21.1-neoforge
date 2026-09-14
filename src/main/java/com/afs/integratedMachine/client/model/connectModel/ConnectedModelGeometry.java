package com.afs.integratedMachine.client.model.connectModel;

import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;

import java.util.List;
import java.util.function.Function;

public class ConnectedModelGeometry implements IUnbakedGeometry<ConnectedModelGeometry> {
    private final String textureSetId;
    private final List<ConnectPredicate> predicates;
    private final boolean cornerCut;

    public ConnectedModelGeometry(String textureSetId, List<ConnectPredicate> predicates, boolean cornerCut) {
        this.textureSetId = textureSetId;
        this.predicates = predicates;
        this.cornerCut = cornerCut;
    }

    /*
    for index n, the meaning of each bit is:
    --------------------------------------------------------------------------------------
    |       | TOP_LEFT | LEFT | DOWN_LEFT | DOWN | DOWN_RIGHT | RIGHT | TOP_RIGHT | TOP  |
    |-------+----------+------+-----------+------+------------+-------+-----------+------|
    | index | 7        | 6    | 5         | 4    | 3          | 2     | 1         | 0    |
    --------------------------------------------------------------------------------------
    as 0 in not connected and 1 is connected.
     */

    public static final int[] actualIndex =
            new int[]{0, 1, 0, 1, 4, 5, 4, 7, 0, 1, 0, 1, 4, 5, 4, 7,
                    16, 17, 16, 17, 20, 21, 20, 23, 16, 17, 16, 17, 28, 29, 28, 31,
                    0, 1, 0, 1, 4, 5, 4, 7, 0, 1, 0, 1, 4, 5, 4, 7,
                    16, 17, 16, 17, 20, 21, 20, 23, 16, 17, 16, 17, 28, 29, 28, 31,
                    64, 65, 64, 65, 68, 69, 68, 71, 64, 65, 64, 65, 68, 69, 68, 71,
                    80, 81, 80, 81, 84, 85, 84, 87, 80, 81, 80, 81, 92, 93, 92, 95,
                    64, 65, 64, 65, 68, 69, 68, 71, 64, 65, 64, 65, 68, 69, 68, 71,
                    112, 113, 112, 113, 116, 117, 116, 119, 112, 113, 112, 113, 124, 125, 124, 127,
                    0, 1, 0, 1, 4, 5, 4, 7, 0, 1, 0, 1, 4, 5, 4, 7,
                    16, 17, 16, 17, 20, 21, 20, 23, 16, 17, 16, 17, 28, 29, 28, 31,
                    0, 1, 0, 1, 4, 5, 4, 7, 0, 1, 0, 1, 4, 5, 4, 7,
                    16, 17, 16, 17, 20, 21, 20, 23, 16, 17, 16, 17, 28, 29, 28, 31,
                    64, 193, 64, 193, 68, 197, 68, 199, 64, 193, 64, 193, 68, 197, 68, 199,
                    80, 209, 80, 209, 84, 213, 84, 215, 80, 209, 80, 209, 92, 221, 92, 223,
                    64, 193, 64, 193, 68, 197, 68, 199, 64, 193, 64, 193, 68, 197, 68, 199,
                    112, 241, 112, 241, 116, 245, 116, 247, 112, 241, 112, 241, 124, 253, 124, 255};

    public static final int[] indexOfTexture =
            new int[]{0, 1, 0, 1, 2, 3, 2, 4, 0, 1, 0, 1, 2, 3, 2, 4,
                    5, 6, 5, 6, 7, 8, 7, 9, 5, 6, 5, 6, 10, 11, 10, 12,
                    0, 1, 0, 1, 2, 3, 2, 4, 0, 1, 0, 1, 2, 3, 2, 4,
                    5, 6, 5, 6, 7, 8, 7, 9, 5, 6, 5, 6, 10, 11, 10, 12,
                    13, 14, 13, 14, 15, 16, 15, 17, 13, 14, 13, 14, 15, 16, 15, 17,
                    18, 19, 18, 19, 20, 21, 20, 22, 18, 19, 18, 19, 23, 24, 23, 25,
                    13, 14, 13, 14, 15, 16, 15, 17, 13, 14, 13, 14, 15, 16, 15, 17,
                    26, 27, 26, 27, 28, 29, 28, 30, 26, 27, 26, 27, 31, 32, 31, 33,
                    0, 1, 0, 1, 2, 3, 2, 4, 0, 1, 0, 1, 2, 3, 2, 4,
                    5, 6, 5, 6, 7, 8, 7, 9, 5, 6, 5, 6, 10, 11, 10, 12,
                    0, 1, 0, 1, 2, 3, 2, 4, 0, 1, 0, 1, 2, 3, 2, 4,
                    5, 6, 5, 6, 7, 8, 7, 9, 5, 6, 5, 6, 10, 11, 10, 12,
                    13, 34, 13, 34, 15, 35, 15, 36, 13, 34, 13, 34, 15, 35, 15, 36,
                    18, 37, 18, 37, 20, 38, 20, 39, 18, 37, 18, 37, 23, 40, 23, 41,
                    13, 34, 13, 34, 15, 35, 15, 36, 13, 34, 13, 34, 15, 35, 15, 36,
                    26, 42, 26, 42, 28, 43, 28, 44, 26, 42, 26, 42, 31, 45, 31, 46};

    private void addVertexAndNormalForConsumer(Direction direction, QuadBakingVertexConsumer consumer,
                                               float minU, float maxU, float minV, float maxV) {
        float nx = direction.getStepX();
        float ny = direction.getStepY();
        float nz = direction.getStepZ();

        switch (direction) {
            case DOWN -> {
                vertex(consumer, 0, 0, 0, minU, maxV, nx, ny, nz);
                vertex(consumer, 1, 0, 0, maxU, maxV, nx, ny, nz);
                vertex(consumer, 1, 0, 1, maxU, minV, nx, ny, nz);
                vertex(consumer, 0, 0, 1, minU, minV, nx, ny, nz);
            }

            case UP -> {
                vertex(consumer, 0, 1, 0, minU, minV, nx, ny, nz);
                vertex(consumer, 0, 1, 1, minU, maxV, nx, ny, nz);
                vertex(consumer, 1, 1, 1, maxU, maxV, nx, ny, nz);
                vertex(consumer, 1, 1, 0, maxU, minV, nx, ny, nz);
            }

            case NORTH -> {
                vertex(consumer, 0, 0, 0, maxU, maxV, nx, ny, nz);
                vertex(consumer, 0, 1, 0, maxU, minV, nx, ny, nz);
                vertex(consumer, 1, 1, 0, minU, minV, nx, ny, nz);
                vertex(consumer, 1, 0, 0, minU, maxV, nx, ny, nz);
            }

            case SOUTH -> {
                vertex(consumer, 0, 0, 1, minU, maxV, nx, ny, nz);
                vertex(consumer, 1, 0, 1, maxU, maxV, nx, ny, nz);
                vertex(consumer, 1, 1, 1, maxU, minV, nx, ny, nz);
                vertex(consumer, 0, 1, 1, minU, minV, nx, ny, nz);
            }

            case WEST -> {
                vertex(consumer, 0, 0, 0, minU, maxV, nx, ny, nz);
                vertex(consumer, 0, 0, 1, maxU, maxV, nx, ny, nz);
                vertex(consumer, 0, 1, 1, maxU, minV, nx, ny, nz);
                vertex(consumer, 0, 1, 0, minU, minV, nx, ny, nz);
            }

            case EAST -> {
                vertex(consumer, 1, 0, 0, maxU, maxV, nx, ny, nz);
                vertex(consumer, 1, 1, 0, maxU, minV, nx, ny, nz);
                vertex(consumer, 1, 1, 1, minU, minV, nx, ny, nz);
                vertex(consumer, 1, 0, 1, minU, maxV, nx, ny, nz);
            }
        }
    }

    private void vertex(QuadBakingVertexConsumer consumer,
                        float x, float y, float z,
                        float u, float v,
                        float nx, float ny, float nz) {

        consumer.addVertex(x, y, z)
                .setColor(-1)
                .setUv(u, v)
                .setOverlay(0)
                .setLight(LightTexture.FULL_SKY)
                .setNormal(nx, ny, nz);
    }

    private BakedQuad bakeQuad(TextureAtlasSprite texture, int idIndex, Direction direction, QuadBakingVertexConsumer consumer){
        int u = idIndex % 8;
        int v = idIndex / 8;
        float minU = texture.getU(((float)u)/8);
        float maxU = texture.getU(((float)u + 1)/8);
        float minV = texture.getV(((float)v)/8);
        float maxV = texture.getV(((float)v + 1)/8);
        addVertexAndNormalForConsumer(direction, consumer, minU, maxU, minV, maxV);
        consumer.setSprite(texture);
        return consumer.bakeQuad();
    }

    private BakedQuad[][] bakeQuads(TextureAtlasSprite texture){
        QuadBakingVertexConsumer consumer = new QuadBakingVertexConsumer();
        BakedQuad[][] quads = new BakedQuad[6][256];
        for(Direction direction: Direction.values()){
            BakedQuad[] directionalQuads = new BakedQuad[256];
            for(int i = 0;i < 256;i ++){
                if(actualIndex[i] == i){
                    directionalQuads[i] = bakeQuad(texture, indexOfTexture[i], direction, consumer);
                }
                else {
                    directionalQuads[i] = directionalQuads[actualIndex[i]];
                }
            }
            quads[direction.ordinal()] = directionalQuads;
        }
        return quads;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material,
            TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
        TextureAtlasSprite particle = spriteGetter.apply(context.getMaterial("particle"));
        TextureAtlasSprite textureSet = spriteGetter.apply(context.getMaterial(textureSetId));
        return new ConnectedModel(context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight(),
                particle, predicates, bakeQuads(textureSet), overrides, cornerCut);
    }
}
