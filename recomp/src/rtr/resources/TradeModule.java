package rtr.resources;

import java.util.ArrayList;
import java.util.Collections;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.TimeModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.objects.rangebuilder.RangeBuilder;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class TradeModule extends ModuleBase {
   private static final int TRADE_AMOUNT_MAX = 2000;
   private static final int CATJEET_LABOR_BASE_PRICE = 1500;
   private static final int PRICE_UPDATE_DELAY = 10000;
   private int[] tradeMode;
   private int[] tradeAmounts;
   private int[] currentBuyPrice;
   private int[] currentSellPrice;
   private int tradeLaborMode;
   private int tradeLaborAmount;
   private int currentLaborBuyPrice;
   private int priceUpdateTick;
   private boolean addedStockToday;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.tradeMode = new int[ResourceModule.ResourceType.values().length];
      this.tradeAmounts = new int[ResourceModule.ResourceType.values().length];
      this.currentBuyPrice = new int[ResourceModule.ResourceType.values().length];
      this.currentSellPrice = new int[ResourceModule.ResourceType.values().length];
      this.tradeLaborMode = 0;
      this.tradeLaborAmount = 0;
      this.currentLaborBuyPrice = 0;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.tradeMode = new int[ResourceModule.ResourceType.values().length];
      this.tradeAmounts = new int[ResourceModule.ResourceType.values().length];
      this.currentBuyPrice = new int[ResourceModule.ResourceType.values().length];
      this.currentSellPrice = new int[ResourceModule.ResourceType.values().length];
      this.tradeLaborMode = 0;
      this.tradeLaborAmount = 0;
      this.currentLaborBuyPrice = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      this.priceUpdateTick = 10000;
   }

   @Override
   protected void loadPlay() throws SlickException {
      this.loadRegionalSaveData();
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(this.moduleType);

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.tradeMode[t.ordinal()] = Integer.parseInt(dataMap.getOrDefault("tradeMode" + t, 0));
         this.tradeAmounts[t.ordinal()] = Integer.parseInt(dataMap.getOrDefault("tradeAmounts" + t, 0));
         this.currentBuyPrice[t.ordinal()] = Integer.parseInt(dataMap.getOrDefault("currentBuyPrice" + t, 1));
         this.currentSellPrice[t.ordinal()] = Integer.parseInt(dataMap.getOrDefault("currentSellPrice" + t, 1));
      }

      this.tradeLaborMode = Integer.parseInt(dataMap.getOrDefault("tradeLaborMode", 0));
      this.tradeLaborAmount = Integer.parseInt(dataMap.getOrDefault("tradeLaborAmount", 0));
      this.currentLaborBuyPrice = Integer.parseInt(dataMap.getOrDefault("currentLaborBuyPrice", 0));
      this.priceUpdateTick = Integer.parseInt(dataMap.getOrDefault("priceUpdateTick", 0));
      this.addedStockToday = Boolean.parseBoolean(dataMap.getOrDefault("addedStockToday", false));
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.tradeMode[t.ordinal()] != 0) {
            dataMap.put("tradeMode" + t, this.tradeMode[t.ordinal()]);
         }

         if (this.tradeAmounts[t.ordinal()] != 0) {
            dataMap.put("tradeAmounts" + t, this.tradeAmounts[t.ordinal()]);
         }

         if (this.currentBuyPrice[t.ordinal()] != 1) {
            dataMap.put("currentBuyPrice" + t, this.currentBuyPrice[t.ordinal()]);
         }

         if (this.currentSellPrice[t.ordinal()] != 1) {
            dataMap.put("currentSellPrice" + t, this.currentSellPrice[t.ordinal()]);
         }
      }

      if (this.tradeLaborMode != 0) {
         dataMap.put("tradeLaborMode", this.tradeLaborMode);
      }

      if (this.tradeLaborAmount != 0) {
         dataMap.put("tradeLaborAmount", this.tradeLaborAmount);
      }

      if (this.currentLaborBuyPrice != 0) {
         dataMap.put("currentLaborBuyPrice", this.currentLaborBuyPrice);
      }

      if (this.priceUpdateTick != 0) {
         dataMap.put("priceUpdateTick", this.priceUpdateTick);
      }

      if (this.addedStockToday) {
         dataMap.put("addedStockToday", this.addedStockToday);
      }

      return dataMap;
   }

   public void update() throws SlickException {
      this.priceUpdateTick++;
      if (this.priceUpdateTick > 10000) {
         this.updatePrices();
         this.priceUpdateTick = 0;
      }

      ArrayList<MobBase> provisionersPosted = this.mob.getMobsByAssignment(Assignment.AssignmentType.CATJEET_SELLING_WARES, false);
      if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.DAWN && !this.addedStockToday && provisionersPosted.size() > 0) {
         int addStockAmount = Utilities.randomInt(provisionersPosted.size() * 20) + provisionersPosted.size() * 40;
         if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MARKET_RESTOCK_AMOUNT) > 0.0) {
            addStockAmount = (int)(addStockAmount + addStockAmount * this.perk.getPerkTypeTotal(PerkModule.PerkType.MARKET_RESTOCK_AMOUNT));
         }

         this.addStock(addStockAmount);
         this.addedStockToday = true;
      } else if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.MORNING) {
         this.addedStockToday = false;
      }

      this.runCatjeetSpawnChance(MobBase.MobType.CATJEET_PROVISIONER, 20000);
      this.runCatjeetSpawnChance(MobBase.MobType.CATJEET_LABORER, 150000);
   }

   private void updatePrices() {
      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
         if (type.getTemplate().canTrade()) {
            double maxValue = 2500.0;
            double villageSupply = this.resource.getResourceTotal(type, true);
            if (villageSupply > maxValue) {
               villageSupply = maxValue;
            }

            double villageDemandPercent = villageSupply / maxValue * -1.0;
            villageDemandPercent += 0.5;
            double catjeetSupply = this.resource.getProvisionerCount(type);
            if (catjeetSupply > maxValue) {
               catjeetSupply = maxValue;
            }

            double catjeetDemandPercent = catjeetSupply / maxValue * -1.0;
            catjeetDemandPercent += 0.5;
            double baseValue = type.getTemplate().getBaseValue();
            double adjustedPrice = baseValue + baseValue * villageDemandPercent + baseValue * catjeetDemandPercent;
            double buyPriceModifier = 1.35;
            if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BETTER_MARKET_PRICES) > 0.0) {
               buyPriceModifier -= this.perk.getPerkTypeTotal(PerkModule.PerkType.BETTER_MARKET_PRICES);
            }

            double sellPriceModifier = 0.65;
            if (this.perk.getPerkTypeTotal(PerkModule.PerkType.BETTER_MARKET_PRICES) > 0.0) {
               buyPriceModifier += this.perk.getPerkTypeTotal(PerkModule.PerkType.BETTER_MARKET_PRICES);
            }

            this.currentBuyPrice[type.ordinal()] = (int)(adjustedPrice * (buyPriceModifier + Utilities.randomFloat() / 4.0F));
            this.currentSellPrice[type.ordinal()] = (int)(adjustedPrice * (sellPriceModifier - Utilities.randomFloat() / 4.0F));
            if (this.currentBuyPrice[type.ordinal()] < 1) {
               this.currentBuyPrice[type.ordinal()] = 1;
            }

            if (this.currentSellPrice[type.ordinal()] < 1) {
               this.currentSellPrice[type.ordinal()] = 1;
            }
         }
      }

      int laborCount = this.mob.getMobsByAssignment(Assignment.AssignmentType.CATJEET_SELLING_LABOR, false).size();
      double percentOff = laborCount / 50.0;
      this.currentLaborBuyPrice = (int)((1500.0 - 1500.0 * percentOff) * (1.0 + (Utilities.randomFloat() / 4.0F - Utilities.randomFloat() / 4.0F)));
      if (this.currentLaborBuyPrice < 375) {
         this.currentLaborBuyPrice = 375;
      }
   }

   private void runCatjeetSpawnChance(MobBase.MobType t, int chance) throws SlickException {
      if (this.object.getMaximumCatjeetProvisioners() > 0
         && this.time.getTimeOfDay() != TimeModule.TimeOfDay.NIGHT
         && this.time.getTimeOfDay() != TimeModule.TimeOfDay.DUSK) {
         ArrayList<MobBase> workers = this.mob.getMobsByType(t, false);
         chance -= (this.object.getMaximumCatjeetProvisioners() - workers.size()) * 1000;
         if (this.time.getTimeOfDay() == TimeModule.TimeOfDay.DAWN || this.time.getTimeOfDay() == TimeModule.TimeOfDay.MORNING) {
            chance /= 2;
         }

         if (chance < 1000) {
            chance = 1000;
         }

         if (Utilities.randomInt(chance) == 0) {
            int catjeetCount = this.mob.getMobsByType(t, false).size();
            if (catjeetCount < this.object.getMaximumCatjeetProvisioners()) {
               OrderedPair validCoordinates = this.getValidProvisionerSpawnCoordinates(t);
               int randomX = validCoordinates.getX();
               int randomY = validCoordinates.getY();
               this.mob.newMob(randomX, randomY, t, true);
            }
         }
      }
   }

   private OrderedPair getValidProvisionerSpawnCoordinates(MobBase.MobType t) throws SlickException {
      MobBase n = MobFactory.getMobInstance(t);
      ArrayList<OrderedPair> edgeTiles = this.map.getMapData().getEdgeTiles();
      ArrayList<OrderedPair> edgeTilesShuffled = new ArrayList<>();
      edgeTilesShuffled.addAll(edgeTiles);
      Collections.shuffle(edgeTilesShuffled);
      OrderedPair spawnCoordinates = null;

      for (OrderedPair o : edgeTilesShuffled) {
         int x = o.getX();
         int y = o.getY();
         if (this.map.getMapData().getBlockMap(n.getMobFlags().getBlockMapGroup())[x][y] < 1
            && !this.object.isInRange(x, y)
            && !this.corruption.isTileCorrupted(x, y)
            && this.map.getTileId(x, y, 6) == 0
            && this.map.getTileId(x, y, 9) == 0
            && this.object.getRangeMap(RangeBuilder.RangeMapLayer.REACH_MAP)[x][y] != 0) {
            spawnCoordinates = OrderedPair.getOrderedPair(x, y);
            break;
         }
      }

      if (spawnCoordinates == null) {
         int randomX = Utilities.randomInt(256);
         int randomY = Utilities.randomInt(256);

         for (int attempts = 0;
            this.map.getMapData().getBlockMap(n.getMobFlags().getBlockMapGroup())[randomX][randomY] > 0
               || attempts < 500 && this.object.isInRange(randomX, randomY)
               || this.map.getTileId(randomX, randomY, 6) > 0
               || this.map.getTileId(randomX, randomY, 9) > 0
               || attempts < 100 && !this.object.isInRange(randomX, randomY);
            attempts++
         ) {
            randomX = Utilities.randomInt(256);
            randomY = Utilities.randomInt(256);
         }

         spawnCoordinates = OrderedPair.getOrderedPair(randomX, randomY);
      }

      return spawnCoordinates;
   }

   public void addStock(int amount) {
      while (amount > 0) {
         ResourceModule.ResourceType[] var5;
         for (ResourceModule.ResourceType type : ResourceModule.ResourceType.values()) {
            if (type.getTemplate().canTrade() && Utilities.randomInt(this.getBuyPrice(type) * 10 + this.resource.getProvisionerCount(type)) == 0) {
               this.resource.createResourceProvisioner(type);
               amount--;
            }
         }
      }
   }

   public int getTradeAmount(ResourceModule.ResourceType t) {
      return this.tradeAmounts[t.ordinal()];
   }

   public void increaseTradeAmount(ResourceModule.ResourceType t, int amount) {
      this.tradeAmounts[t.ordinal()] += amount;
      if (this.tradeAmounts[t.ordinal()] > 2000) {
         this.tradeAmounts[t.ordinal()] = 2000;
      }
   }

   public void decreaseTradeAmount(ResourceModule.ResourceType t, int amount) {
      this.tradeAmounts[t.ordinal()] -= amount;
      if (this.tradeAmounts[t.ordinal()] < 0) {
         this.tradeAmounts[t.ordinal()] = 0;
      }
   }

   public void resetTradeAmount(ResourceModule.ResourceType t) {
      this.tradeAmounts[t.ordinal()] = 0;
   }

   public int getLaborTradeAmount() {
      return this.tradeLaborAmount;
   }

   public void increaseLaborTradeAmount(int amount) {
      this.tradeLaborAmount += amount;
      if (this.tradeLaborAmount > 2000) {
         this.tradeLaborAmount = 2000;
      }
   }

   public void decreaseLaborTradeAmount(int amount) {
      this.tradeLaborAmount -= amount;
      if (this.tradeLaborAmount < 0) {
         this.tradeLaborAmount = 0;
      }
   }

   public void resetLaborTradeAmount() {
      this.tradeLaborAmount = 0;
   }

   public int getLaborBuyPrice() {
      return this.currentLaborBuyPrice;
   }

   public boolean isLaborModeBuy() {
      return this.tradeLaborMode == 1;
   }

   public void resetLaborTradeMode() {
      this.tradeLaborMode = 0;
   }

   public void setLaborTradeModeBuy() {
      this.tradeLaborMode = 1;
   }

   public int getBuyPrice(ResourceModule.ResourceType t) {
      return this.currentBuyPrice[t.ordinal()];
   }

   public int getSellPrice(ResourceModule.ResourceType t) {
      return this.currentSellPrice[t.ordinal()];
   }

   public boolean isModeBuy(ResourceModule.ResourceType t) {
      return this.tradeMode[t.ordinal()] == 1;
   }

   public boolean isModeSell(ResourceModule.ResourceType t) {
      return this.tradeMode[t.ordinal()] == 2;
   }

   public void resetTradeMode(ResourceModule.ResourceType t) {
      this.tradeMode[t.ordinal()] = 0;
   }

   public void setTradeModeBuy(ResourceModule.ResourceType t) {
      this.tradeMode[t.ordinal()] = 1;
   }

   public void setTradeModeSell(ResourceModule.ResourceType t) {
      this.tradeMode[t.ordinal()] = 2;
   }

   public boolean isBuyOrders() {
      ResourceModule.ResourceType[] var4;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         if (this.isModeBuy(t)) {
            return true;
         }
      }

      return false;
   }

   public int getAcceptSellCount(ResourceModule.ResourceType resourceType, int mobID) {
      if (this.isModeSell(resourceType) && this.getTradeAmount(resourceType) > 0) {
         int delivererCount = 0;

         for (MobBase m : this.mob.getMobsByAssignment(Assignment.AssignmentType.SELL_RESOURCE, false)) {
            if (m.getAssignment() != null && m.getID() != mobID) {
               ArrayList<ResourceBase> pickupResources = m.getAssignment().getPickupResources();
               if (pickupResources.size() != 0 || m.hasCarriedResources()) {
                  if (pickupResources.size() > 0 && (resourceType == null || pickupResources.get(0).getType() == resourceType)) {
                     delivererCount += pickupResources.size();
                  }

                  if (m.hasCarriedResources() && (resourceType == null || m.getCarriedResourcesListType() == resourceType)) {
                     delivererCount += m.getCarriedResourcesList().size();
                  }
               }
            }
         }

         return this.getTradeAmount(resourceType) - delivererCount;
      } else {
         return 0;
      }
   }
}
