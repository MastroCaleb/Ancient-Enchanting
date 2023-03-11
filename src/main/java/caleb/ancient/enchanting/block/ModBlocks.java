package caleb.ancient.enchanting.block;

import caleb.ancient.enchanting.MainMod;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block FRESH_MAGMA = new FreshMagmaBlock(FabricBlockSettings.copyOf(Blocks.MAGMA_BLOCK));

    public static void init(){
        Registry.register(Registries.BLOCK, new Identifier(MainMod.MOD_ID, "fresh_magma"), FRESH_MAGMA);
    }
}
