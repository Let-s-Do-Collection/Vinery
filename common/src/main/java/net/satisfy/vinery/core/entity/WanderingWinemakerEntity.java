package net.satisfy.vinery.core.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.satisfy.vinery.core.registry.ObjectRegistry;

import java.util.HashMap;

public class WanderingWinemakerEntity extends WanderingTrader {
	public static final HashMap<Integer, VillagerTrades.ItemListing[]> TRADES = createTrades();

	private static  HashMap<Integer, VillagerTrades.ItemListing[]> createTrades() {
		HashMap<Integer, VillagerTrades.ItemListing[]> trades = new HashMap<>();
		trades.put(1, new VillagerTrades.ItemListing[]{
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.RED_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.WHITE_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.TAIGA_RED_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.TAIGA_WHITE_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.SAVANNA_RED_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.SAVANNA_WHITE_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.JUNGLE_RED_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.JUNGLE_WHITE_GRAPE_SEEDS.get(), 1, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.DARK_CHERRY_SAPLING.get(), 3, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.APPLE_TREE_SAPLING.get(), 5, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.RED_GRAPE.get(), 2, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.RED_GRAPEJUICE.get(), 4, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.WHITE_GRAPEJUICE.get(), 4, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.RED_SAVANNA_GRAPEJUICE.get(), 4, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.WHITE_TAIGA_GRAPEJUICE.get(), 4, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.RED_JUNGLE_GRAPEJUICE.get(), 4, 1, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.COARSE_DIRT_SLAB.get(), 1, 3, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.GRASS_SLAB.get(), 1, 3, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.DARK_CHERRY_PLANKS.get(), 3, 4, 8, 1),
				new VillagerTrades.ItemsForEmeralds(ObjectRegistry.CHERRY_WINE.get(), 1, 1, 8, 1)
		});
		return trades;
	}

	public WanderingWinemakerEntity(EntityType<? extends WanderingWinemakerEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	protected void updateTrades() {
		if (this.offers == null) {
			this.offers = new MerchantOffers();
		}
		this.addOffersFromItemListings(this.offers, TRADES.get(1), 8);
	}
}