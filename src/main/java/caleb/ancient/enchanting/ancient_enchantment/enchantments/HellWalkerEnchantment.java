package caleb.ancient.enchanting.ancient_enchantment.enchantments;

import caleb.ancient.enchanting.ancient_enchantment.AncientEnchantment;
import caleb.ancient.enchanting.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.Material;
import net.minecraft.block.ShapeContext;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class HellWalkerEnchantment
extends AncientEnchantment {
    public HellWalkerEnchantment(AncientEnchantment.Rarity rarity, EnchantmentTarget enchantmentTarget, EquipmentSlot ... equipmentSlots) {
        super(rarity, enchantmentTarget, equipmentSlots);
    }

    @Override
    public int getMinPower(int level) {
        return level * 10;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 15;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    public static void solidLava(LivingEntity entity, World world, BlockPos blockPos, int level) {
        if (!entity.isOnGround()) {
            return;
        }
        BlockState blockState = ModBlocks.FRESH_MAGMA.getDefaultState();
        float f = Math.min(16, 2 + level);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-f, -1.0, -f), blockPos.add(f, -1.0, f))) {
            BlockState blockState3;
            if (!blockPos2.isWithinDistance(entity.getPos(), (double)f)) continue;
            mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
            BlockState blockState2 = world.getBlockState(mutable);
            if (!blockState2.isAir() || (blockState3 = world.getBlockState(blockPos2)).getMaterial() != Material.LAVA || blockState3.get(FluidBlock.LEVEL) != 0 || !blockState.canPlaceAt(world, blockPos2) || !world.canPlace(blockState, blockPos2, ShapeContext.absent())) continue;
            world.setBlockState(blockPos2, blockState);
            world.scheduleBlockTick(blockPos2, ModBlocks.FRESH_MAGMA, MathHelper.nextInt(entity.getRandom(), 60, 120));
        }
    }
}