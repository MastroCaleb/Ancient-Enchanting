package caleb.ancient.enchanting.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;
import caleb.ancient.enchanting.ancient_enchantment.AncientEnchantment;
import caleb.ancient.enchanting.item.AncientEnchantedBookItem;

@Mixin(ItemGroups.class)
public class ItemGroupMixin {

    @SuppressWarnings("unused")
	private static void addAllLevelEnchantedBooks(ItemGroup.Entries entries, Set<EnchantmentTarget> targets, ItemGroup.StackVisibility visibility) {
        for (Enchantment enchantment : Registries.ENCHANTMENT) {
            if(enchantment instanceof AncientEnchantment ancient){
                if (!targets.contains((Object)enchantment.type)) continue;
                for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                    entries.add(AncientEnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i)), visibility);
                }
            }
            else{
                if(enchantment instanceof AncientEnchantment ancient) return;
                if (!targets.contains((Object)enchantment.type)) continue;
                for (int i = enchantment.getMinLevel(); i <= enchantment.getMaxLevel(); ++i) {
                    entries.add(EnchantedBookItem.forEnchantment(new EnchantmentLevelEntry(enchantment, i)), visibility);
                }  
            }
        }
    }
	
}
