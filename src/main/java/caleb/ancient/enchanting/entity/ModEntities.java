package caleb.ancient.enchanting.entity;

import caleb.ancient.enchanting.MainMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<SkeletonMinionEntity> SKELETON_MINION = Registry.register(
        Registries.ENTITY_TYPE, new Identifier(MainMod.MOD_ID, "skeleton_minion"), FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, SkeletonMinionEntity::new).dimensions(EntityDimensions.changing(0.6f, 1.99f)).build()
    );

    public static void init(){
        FabricDefaultAttributeRegistry.register(SKELETON_MINION, AbstractSkeletonEntity.createAbstractSkeletonAttributes());
    }
}
