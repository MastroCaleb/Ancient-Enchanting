package caleb.ancient.enchanting.ancient_enchantment;

import caleb.ancient.enchanting.MainMod;
import caleb.ancient.enchanting.ancient_enchantment.enchantments.AirJumperEnchantment;
import caleb.ancient.enchanting.ancient_enchantment.enchantments.FarmersFortuneEnchantment;
import caleb.ancient.enchanting.ancient_enchantment.enchantments.HellWalkerEnchantment;
import caleb.ancient.enchanting.ancient_enchantment.enchantments.SummonersCallEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantment.Rarity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class AncientEnchantments {
    public static final Enchantment HELL_WALKER = new HellWalkerEnchantment(Rarity.RARE, EnchantmentTarget.ARMOR_FEET, EquipmentSlot.FEET);
    public static final Enchantment AIR_JUMPER = new AirJumperEnchantment(Rarity.RARE, EnchantmentTarget.ARMOR_LEGS, EquipmentSlot.LEGS);
    public static final Enchantment FARMERS_FORTUNE = new FarmersFortuneEnchantment(Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
    public static final Enchantment SUMMONERS_CALL = new SummonersCallEnchantment(Rarity.RARE, EnchantmentTarget.ARMOR_CHEST, EquipmentSlot.CHEST);


    public static void init(){
        Registry.register(Registries.ENCHANTMENT, new Identifier(MainMod.MOD_ID, "hell_walker"), HELL_WALKER);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MainMod.MOD_ID, "air_jumper"), AIR_JUMPER);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MainMod.MOD_ID, "farmers_fortune"), FARMERS_FORTUNE);
        Registry.register(Registries.ENCHANTMENT, new Identifier(MainMod.MOD_ID, "summoners_call"), SUMMONERS_CALL);
    }
}
