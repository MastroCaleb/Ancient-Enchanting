package caleb.ancient.enchanting.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.enchantment.EnchantmentHelper;
import java.util.Map;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @Inject(at = @At(value = "HEAD"), method = "get", cancellable = true)
    private static void getInj(ItemStack stack, CallbackInfoReturnable<Map<Enchantment, Integer>> r) {
        NbtList nbtList = (stack.getItem() instanceof EnchantedBookItem) ? EnchantedBookItem.getEnchantmentNbt(stack) : stack.getEnchantments();
        r.setReturnValue(EnchantmentHelper.fromNbt(nbtList));
    }

    @Inject(at = @At(value = "HEAD"), method = "set", cancellable = true)
    private static void setInj(Map<Enchantment, Integer> enchantments, ItemStack stack, CallbackInfo r) {
        NbtList nbtList = new NbtList();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            if (enchantment == null) continue;
            int i = entry.getValue();
            nbtList.add(EnchantmentHelper.createNbt(EnchantmentHelper.getEnchantmentId(enchantment), i));
            if (!(stack.getItem() instanceof EnchantedBookItem)) continue;
            EnchantedBookItem.addEnchantment(stack, new EnchantmentLevelEntry(enchantment, i));
        }
        if (nbtList.isEmpty()) {
            stack.removeSubNbt("Enchantments");
        } else if (!(stack.getItem() instanceof EnchantedBookItem)) {
            stack.setSubNbt("Enchantments", nbtList);
        }
        return;
    }
}
