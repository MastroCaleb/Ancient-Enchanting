package caleb.ancient.enchanting.ancient_enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;

public abstract class AncientEnchantment extends Enchantment{

    protected AncientEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    protected String getOrCreateTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.createTranslationKey("ancient_enchantment", Registries.ENCHANTMENT.getId(this));
        }
        return this.translationKey;
    }

    @Override
    public boolean isAvailableForEnchantedBookOffer() {
        return false;
    }

    @Override
    public boolean isAvailableForRandomSelection() {
        return false;
    }

    @Override
    public Text getName(int level) {
        MutableText mutableText = Text.translatable(this.getTranslationKey());
        mutableText.formatted(Formatting.GOLD);
        if (level != 1 || this.getMaxLevel() != 1) {
            mutableText.append(" ").append(Text.translatable("enchantment.level." + level));
        }
        return mutableText;
    }
}
