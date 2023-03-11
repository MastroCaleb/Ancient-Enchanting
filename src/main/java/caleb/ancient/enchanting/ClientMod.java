package caleb.ancient.enchanting;

import caleb.ancient.enchanting.entity.ModEntities;
import caleb.ancient.enchanting.entity.SkeletonMinionEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;

public class ClientMod implements ClientModInitializer{

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.SKELETON_MINION, SkeletonMinionEntityRenderer::new);
    }
    
}
