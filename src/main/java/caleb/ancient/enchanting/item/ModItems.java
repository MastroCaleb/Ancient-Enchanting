package caleb.ancient.enchanting.item;

import caleb.ancient.enchanting.MainMod;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {
    public static final Item ANCIENT_ENCHANTED_BOOK = new AncientEnchantedBookItem(new Item.Settings().maxCount(1).rarity(Rarity.EPIC));

    public static void init(){
        Registry.register(Registries.ITEM, new Identifier(MainMod.MOD_ID, "ancient_enchanted_book"), ANCIENT_ENCHANTED_BOOK);
    }
}
