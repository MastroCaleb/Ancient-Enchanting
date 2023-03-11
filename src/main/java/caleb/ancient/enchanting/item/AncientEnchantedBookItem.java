package caleb.ancient.enchanting.item;

import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class AncientEnchantedBookItem
extends EnchantedBookItem {

    public AncientEnchantedBookItem(Item.Settings settings) {
        super(settings);
    }

    public static ItemStack forEnchantment(EnchantmentLevelEntry info) {
        ItemStack itemStack = new ItemStack(ModItems.ANCIENT_ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(itemStack, info);
        return itemStack;
    }
}
