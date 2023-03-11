package caleb.ancient.enchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import caleb.ancient.enchanting.ancient_enchantment.AncientEnchantments;
import caleb.ancient.enchanting.ancient_enchantment.enchantments.HellWalkerEnchantment;
import caleb.ancient.enchanting.entity.ModEntities;
import caleb.ancient.enchanting.entity.SkeletonMinionEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.fluid.Fluid;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

@Mixin(LivingEntity.class)
public class LivingEntityMixin{

    LivingEntity entity = (LivingEntity)(Object)this;

    public boolean jumping;
    private int jumpingCooldown;
    private int c=1;
    
    public void applyMovementEffects(BlockPos pos) {
        int i = EnchantmentHelper.getEquipmentLevel(AncientEnchantments.HELL_WALKER, entity);
        if (i > 0) {
            HellWalkerEnchantment.solidLava(entity, entity.world, pos, i);
        }
    }

    @Inject(at = @At(value = "RETURN"), method = "damage", cancellable = true)
    public void damageInj(DamageSource source, float amount, CallbackInfoReturnable inf){
        if(EnchantmentHelper.getLevel(AncientEnchantments.SUMMONERS_CALL, entity.getEquippedStack(EquipmentSlot.CHEST))>0){
            if(entity.canTakeDamage()){
                this.spawnMinionInRange(entity.getBlockPos(), 3);
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "tickMovement", cancellable = true)
    public void injMovement(CallbackInfo inf){
        if(entity.isOnGround()){
            c=1;
        }

        if (this.jumping && this.shouldSwimInFluids()) {
            int jumps = EnchantmentHelper.getEquipmentLevel(AncientEnchantments.AIR_JUMPER, entity);
            double k = entity.isInLava() ? entity.getFluidHeight(FluidTags.LAVA) : entity.getFluidHeight(FluidTags.WATER);
            boolean bl = entity.isTouchingWater() && k > 0.0;
            double l = entity.getSwimHeight();
            if (bl && (!entity.isOnGround() || k > l)) {
                this.swimUpward(FluidTags.WATER);
            } else if (entity.isInLava() && (!entity.isOnGround() || k > l)) {
                this.swimUpward(FluidTags.LAVA);
            } else if (((entity.isOnGround() || (jumps>0 && c<=jumps)) || bl && k <= l) && this.jumpingCooldown == 0) {
                this.jump();
                c++;
                this.jumpingCooldown = 10;
            }
        } else {
            this.jumpingCooldown = 0;
        }
    }

    public void spawnMinionInRange(BlockPos blockPos, int range){
        Random random = entity.world.getRandom();
        
        java.util.Random r = new java.util.Random();

        int level = EnchantmentHelper.getLevel(AncientEnchantments.SUMMONERS_CALL, entity.getEquippedStack(EquipmentSlot.CHEST));

        float chance = 1.5f*level;

        if(r.nextFloat(0, 1) < (float)(chance/10)){
            int x = random.nextBetween(0, range);
            int z = random.nextBetween(0, range);

            BlockPos pos = new BlockPos(blockPos.getX()+x, blockPos.getY(), blockPos.getZ()+z);

            double d = (double)pos.getX() + random.nextDouble();
            double e = (double)pos.getY() + random.nextDouble();
            double f = (double)pos.getZ() + random.nextDouble();
            entity.world.addParticle(ParticleTypes.SMOKE, d, e, f, 0.0, 0.0, 0.0);
            entity.world.addParticle(ParticleTypes.FLAME, d, e, f, 0.0, 0.0, 0.0);

            SkeletonMinionEntity m = new SkeletonMinionEntity(ModEntities.SKELETON_MINION, entity.world);
            m.setLevel(level);
            m.setOwner(entity);
            m.setPos(pos.getX(), pos.getY()+1, pos.getZ());

            entity.getWorld().spawnEntity(m);
        }
    }



    public void jump() {
        double d = (double)this.getJumpVelocity() + entity.getJumpBoostVelocityModifier();
        Vec3d vec3d = entity.getVelocity();
        entity.setVelocity(vec3d.x, d, vec3d.z);
        if (entity.isSprinting()) {
            float f = entity.getYaw() * ((float)Math.PI / 180);
            entity.setVelocity(entity.getVelocity().add(-MathHelper.sin(f) * 0.2f, 0.0, MathHelper.cos(f) * 0.2f));
        }
        entity.velocityDirty = true;
    }

    public float getJumpVelocity() {
        return 0.42f * this.getJumpVelocityMultiplier();
    }

    public float getJumpVelocityMultiplier() {
        float f = entity.world.getBlockState(entity.getBlockPos()).getBlock().getJumpVelocityMultiplier();
        float g = entity.world.getBlockState(this.getVelocityAffectingPos()).getBlock().getJumpVelocityMultiplier();
        return (double)f == 1.0 ? g : f;
    }

    public BlockPos getVelocityAffectingPos() {
        return new BlockPos(entity.getPos().x, entity.getBoundingBox().minY - 0.5000001, entity.getPos().z);
    }

    public void swimUpward(TagKey<Fluid> fluid) {
        entity.setVelocity(entity.getVelocity().add(0.0, 0.04f, 0.0));
    }

    public boolean shouldSwimInFluids() {
        return true;
    }
}
