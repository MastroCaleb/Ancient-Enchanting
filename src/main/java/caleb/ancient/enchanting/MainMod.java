package caleb.ancient.enchanting;

import net.fabricmc.api.ModInitializer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import caleb.ancient.enchanting.ancient_enchantment.AncientEnchantments;
import caleb.ancient.enchanting.block.ModBlocks;
import caleb.ancient.enchanting.entity.ModEntities;
import caleb.ancient.enchanting.item.ModItems;

public class MainMod implements ModInitializer {
	public static final String MOD_ID = "ancient_enchanting";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModBlocks.init();
		AncientEnchantments.init();
		ModEntities.init();
		ModItems.init();
		
		LOGGER.info("Hello Fabric world!");
	}
}
