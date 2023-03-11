package caleb.ancient.enchanting.entity;

import java.util.Optional;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;

public class SkeletonMinionEntity
extends SkeletonEntity {

    private static final int TOTAL_LIFETIME = 300;
    private static final TrackedData<Boolean> SHAKING = DataTracker.registerData(SkeletonMinionEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(SkeletonMinionEntity.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    public static final String LIFETIME_KEY = "LifeTime";
    private LivingEntity owner;
    private int lifeTime;

    public SkeletonMinionEntity(EntityType<? extends SkeletonMinionEntity> entityType, World world) {
        super((EntityType<? extends SkeletonEntity>)entityType, world);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(3, new FleeEntityGoal<WolfEntity>(this, WolfEntity.class, 6.0f, 1.0, 1.2));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(3, new ActiveTargetNoOwnerGoal<LivingEntity>((MobEntity)this, LivingEntity.class, false));
    }

    public int level = 0;

    public void setLevel(int level){
        this.level = level;
        this.initEquipment(random, world.getLocalDifficulty(this.getBlockPos()));
    }

    @Override
    protected void initEquipment(Random random, LocalDifficulty localDifficulty) {
        if(this.level == 1){
            this.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
        }
        else if(this.level == 2){
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
            this.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.CHAINMAIL_HELMET));
        }
        else if(this.level == 3){
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD));
            this.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
        }
        else if(this.level == 4){
            this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
            this.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        }
        else{
            return;
        }
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.dataTracker.get(OWNER_UUID).orElse(null);
    }

    public void setOwnerUuid(@Nullable UUID uuid) {
        this.dataTracker.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public void setOwner(LivingEntity owner) {
        if(owner instanceof PlayerEntity player){
            this.setOwnerUuid(player.getUuid());
        }
        this.owner = owner;
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uUID = this.getOwnerUuid();
            if (uUID == null) {
                return null;
            }
            return this.world.getPlayerByUuid(uUID);
        } catch (IllegalArgumentException illegalArgumentException) {
            return owner;
        }
    }

    @Override
    public boolean canTarget(LivingEntity target) {
        if (this.isOwner(target)) {
            return false;
        }
        return super.canTarget(target);
    }

    public boolean isOwner(LivingEntity entity) {
        return entity == this.getOwner();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SHAKING, false);
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
    }

    public void setShaking(boolean shaking) {
        this.dataTracker.set(SHAKING, shaking);
    }

    @Override
    public boolean isShaking() {
        return this.dataTracker.get(SHAKING);
    }

    @Override
    public void tick() {
        if (!this.world.isClient && this.isAlive() && !this.isAiDisabled()) {
            if(this.getOwner() == null){
                this.kill();
            }

            lifeTime++;

            if(lifeTime>=(TOTAL_LIFETIME/2)){
                this.setShaking(true);
            }

            if(lifeTime>=TOTAL_LIFETIME){
                this.kill();
            }
        }
        super.tick();
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getOwnerUuid() != null) {
            nbt.putUuid("Owner", this.getOwnerUuid());
        }
        nbt.putInt(LIFETIME_KEY, this.lifeTime);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        UUID uUID;
        super.readCustomDataFromNbt(nbt);
        if (nbt.containsUuid("Owner")) {
            uUID = nbt.getUuid("Owner");
        } else {
            String string = nbt.getString("Owner");
            uUID = ServerConfigHandler.getPlayerUuidByName(this.getServer(), string);
        }
        if (uUID != null) {
            try {
                this.setOwnerUuid(uUID);
            } 
            catch (Throwable throwable) {
            }
        }
        if (nbt.contains(LIFETIME_KEY, NbtElement.NUMBER_TYPE) && nbt.getInt(LIFETIME_KEY) > -1) {
            this.setLifeTime(nbt.getInt(LIFETIME_KEY));
        }
    }

    private void setLifeTime(int time) {
        this.lifeTime = time;
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_SKELETON_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_SKELETON_DEATH;
    }

    @Override
    protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
        CreeperEntity creeperEntity;
        super.dropEquipment(source, lootingMultiplier, allowDrops);
        Entity entity = source.getAttacker();
        if (entity instanceof CreeperEntity && (creeperEntity = (CreeperEntity)entity).shouldDropHead()) {
            creeperEntity.onHeadDropped();
            this.dropItem(Items.SKELETON_SKULL);
        }
    }

    public class ActiveTargetNoOwnerGoal<T extends LivingEntity> extends ActiveTargetGoal<T>{

        public ActiveTargetNoOwnerGoal(MobEntity mob, Class<T> targetClass, boolean checkVisibility) {
            super(mob, targetClass, checkVisibility);
        }

        @Override
        public boolean canStart() {
            if (this.reciprocalChance > 0 && this.mob.getRandom().nextInt(this.reciprocalChance) != 0) {
                return false;
            }
            this.findClosestTarget();
            if((this.targetEntity instanceof SkeletonMinionEntity)){
                return false;
            }
            return this.targetEntity != null || !isOwner(targetEntity);
        }

    }
}
