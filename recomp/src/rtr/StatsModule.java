package rtr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.SlickException;
import rtr.console.Console;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.MobFactory;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.OrderedPair;

public class StatsModule extends ModuleBase {
   private int lastRecordedDay = -1;
   private ArrayList<Integer> villagerTotal = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerTotalByType = new HashMap<>();
   private ArrayList<Integer> villagerMale = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerMaleByType = new HashMap<>();
   private ArrayList<Integer> villagerFemale = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerFemaleByType = new HashMap<>();
   private ArrayList<Integer> villagerDeaths = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerDeathsByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> villagerDeathsThisPeriod = new HashMap<>();
   private ArrayList<Integer> villagerBirths = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerBirthsByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> villagerBirthsThisPeriod = new HashMap<>();
   private ArrayList<Integer> villagerGrownUp = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerGrownUpByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> villagerGrownUpThisPeriod = new HashMap<>();
   private ArrayList<Integer> villagerCurrentPregnancies = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerCurrentPregnanciesByType = new HashMap<>();
   private ArrayList<Integer> villagerNewPregnancies = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerNewPregnanciesByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> villagerNewPregnanciesThisPeriod = new HashMap<>();
   private ArrayList<Integer> villagerCoitus = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerCoitusByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> villagerCoitusThisPeriod = new HashMap<>();
   private ArrayList<Integer> catjeetHired = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> catjeetHiredByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> catjeetHiredThisPeriod = new HashMap<>();
   private ArrayList<Integer> nomadsJoined = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> nomadsJoinedByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> nomadsJoinedThisPeriod = new HashMap<>();
   private ArrayList<Integer> nomadsVisited = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> nomadsVisitedByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> nomadsVisitedThisPeriod = new HashMap<>();
   private ArrayList<Integer> villagerAverageHunger = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerAverageHungerByType = new HashMap<>();
   private ArrayList<Integer> villagerAverageThirst = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerAverageThirstByType = new HashMap<>();
   private ArrayList<Integer> villagerAverageEnergy = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> villagerAverageEnergyByType = new HashMap<>();
   private ArrayList<Integer> animalTotal = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> animalTotalByType = new HashMap<>();
   private ArrayList<Integer> animalMale = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> animalMaleByType = new HashMap<>();
   private ArrayList<Integer> animalFemale = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> animalFemaleByType = new HashMap<>();
   private ArrayList<Integer> animalDomesticated = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> animalDomesticatedByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> animalDomesticatedThisPeriod = new HashMap<>();
   private ArrayList<Integer> animalDeaths = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> animalDeathsByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> animalDeathsThisPeriod = new HashMap<>();
   private ArrayList<Integer> animalAverageHunger = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> animalAverageHungerByType = new HashMap<>();
   private ArrayList<Integer> animalAverageThirst = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> animalAverageThirstByType = new HashMap<>();
   private ArrayList<Integer> animalAverageEnergy = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> animalAverageEnergyByType = new HashMap<>();
   private ArrayList<Integer> corruptionTotal = new ArrayList<>();
   private ArrayList<Integer> corruptionSpreadAmount = new ArrayList<>();
   private ArrayList<Integer> corruptedBuildings = new ArrayList<>();
   private ArrayList<Integer> dronesTotal = new ArrayList<>();
   private ArrayList<Integer> monstersSpawned = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> monstersSpawnedByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> monstersSpawnedThisPeriod = new HashMap<>();
   private ArrayList<Integer> monstersKilled = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> monstersKilledByType = new HashMap<>();
   private HashMap<MobBase.MobType, Integer> monstersKilledThisPeriod = new HashMap<>();
   private ArrayList<Integer> buildingsTotal = new ArrayList<>();
   private ArrayList<Integer> buildingSlots = new ArrayList<>();
   private ArrayList<Integer> buildingsBuilt = new ArrayList<>();
   private int buildingsBuiltThisPeriod;
   private ArrayList<Integer> buildingsUpgraded = new ArrayList<>();
   private int buildingsUpgradedThisPeriod;
   private ArrayList<Integer> buildingsDestroyed = new ArrayList<>();
   private int buildingsDestroyedThisPeriod;
   private ArrayList<Integer> buildingsDismantled = new ArrayList<>();
   private int buildingsDismantledThisPeriod;
   private ArrayList<Integer> towersTotal = new ArrayList<>();
   private ArrayList<Integer> combobulatorsTotal = new ArrayList<>();
   private ArrayList<Integer> golemTotal = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> golemTotalByType = new HashMap<>();
   private ArrayList<Integer> rangerTotal = new ArrayList<>();
   private HashMap<MobBase.MobType, ArrayList<Integer>> rangerTotalByType = new HashMap<>();
   private ArrayList<Integer> wallsTotal = new ArrayList<>();
   private ArrayList<Integer> resourcesTotal = new ArrayList<>();
   private HashMap<ResourceModule.ResourceType, ArrayList<Integer>> resourcesTotalByType = new HashMap<>();
   private ArrayList<Integer> resourcesProductionRate = new ArrayList<>();
   private HashMap<ResourceModule.ResourceType, ArrayList<Integer>> resourcesProductionRateByType = new HashMap<>();
   private ArrayList<Integer> foodProductionRate = new ArrayList<>();
   private ArrayList<Integer> waterProductionRate = new ArrayList<>();
   private ArrayList<Integer> essenceGenerated = new ArrayList<>();
   private int essenceGeneratedThisPeriod;
   private ArrayList<Integer> essenceDecayed = new ArrayList<>();
   private int essenceDecayedThisPeriod;
   private ArrayList<Integer> energyAmount = new ArrayList<>();
   private ArrayList<Integer> energyMaximum = new ArrayList<>();
   private ArrayList<Integer> influenceSpent = new ArrayList<>();
   private int influenceSpentThisPeriod;
   private ArrayList<Integer> influenceGenerated = new ArrayList<>();
   private int influenceGeneratedThisPeriod;

   public ArrayList<Integer> getVillagerTotal(MobBase.MobType t) {
      return t == null ? this.villagerTotal : this.villagerTotalByType.get(t);
   }

   public ArrayList<Integer> getVillagerMale(MobBase.MobType t) {
      return t == null ? this.villagerMale : this.villagerMaleByType.get(t);
   }

   public ArrayList<Integer> getVillagerFemale(MobBase.MobType t) {
      return t == null ? this.villagerFemale : this.villagerFemaleByType.get(t);
   }

   public ArrayList<Integer> getVillagerDeaths(MobBase.MobType t) {
      return t == null ? this.villagerDeaths : this.villagerDeathsByType.get(t);
   }

   public void increaseVillagerDeathsThisPeriod(MobBase.MobType t) {
      if (this.villagerDeathsThisPeriod.containsKey(t)) {
         this.villagerDeathsThisPeriod.put(t, this.villagerDeathsThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getVillagerBirths(MobBase.MobType t) {
      return t == null ? this.villagerBirths : this.villagerBirthsByType.get(t);
   }

   public void increaseVillagerBirthsThisPeriod(MobBase.MobType t) {
      if (this.villagerBirthsThisPeriod.containsKey(t)) {
         this.villagerBirthsThisPeriod.put(t, this.villagerBirthsThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getVillagerGrownUp(MobBase.MobType t) {
      return t == null ? this.villagerGrownUp : this.villagerGrownUpByType.get(t);
   }

   public void increaseVillagerGrownUpThisPeriod(MobBase.MobType t) {
      if (this.villagerGrownUpThisPeriod.containsKey(t)) {
         this.villagerGrownUpThisPeriod.put(t, this.villagerGrownUpThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getVillagerCurrentPregnancies(MobBase.MobType t) {
      return t == null ? this.villagerCurrentPregnancies : this.villagerCurrentPregnanciesByType.get(t);
   }

   public ArrayList<Integer> getVillagerNewPregnancies(MobBase.MobType t) {
      return t == null ? this.villagerNewPregnancies : this.villagerNewPregnanciesByType.get(t);
   }

   public void increaseVillagerNewPregnanciesThisPeriod(MobBase.MobType t) {
      if (this.villagerNewPregnanciesThisPeriod.containsKey(t)) {
         this.villagerNewPregnanciesThisPeriod.put(t, this.villagerNewPregnanciesThisPeriod.get(t) + 1);
      }
   }

   public void decreaseVillagerNewPregnanciesThisPeriod(MobBase.MobType t) {
      if (this.villagerNewPregnanciesThisPeriod.containsKey(t)) {
         this.villagerNewPregnanciesThisPeriod.put(t, this.villagerNewPregnanciesThisPeriod.get(t) - 1);
      }
   }

   public ArrayList<Integer> getVillagerCoitus(MobBase.MobType t) {
      return t == null ? this.villagerCoitus : this.villagerCoitusByType.get(t);
   }

   public void increaseVillagerCoitusThisPeriod(MobBase.MobType t) {
      if (this.villagerCoitusThisPeriod.containsKey(t)) {
         this.villagerCoitusThisPeriod.put(t, this.villagerCoitusThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getCatjeetHired(MobBase.MobType t) {
      return t == null ? this.catjeetHired : this.catjeetHiredByType.get(t);
   }

   public void increaseCatjeetHiredThisPeriod(MobBase.MobType t) {
      if (this.catjeetHiredThisPeriod.containsKey(t)) {
         this.catjeetHiredThisPeriod.put(t, this.catjeetHiredThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getNomadsJoined(MobBase.MobType t) {
      return t == null ? this.nomadsJoined : this.nomadsJoinedByType.get(t);
   }

   public void increaseNomadsJoinedThisPeriod(MobBase.MobType t) {
      if (this.nomadsJoinedThisPeriod.containsKey(t)) {
         this.nomadsJoinedThisPeriod.put(t, this.nomadsJoinedThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getNomadsVisited(MobBase.MobType t) {
      return t == null ? this.nomadsVisited : this.nomadsVisitedByType.get(t);
   }

   public void increaseNomadsVisitedThisPeriod(MobBase.MobType t) {
      if (this.nomadsVisitedThisPeriod.containsKey(t)) {
         this.nomadsVisitedThisPeriod.put(t, this.nomadsVisitedThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getVillagerAverageHunger(MobBase.MobType t) {
      return t == null ? this.villagerAverageHunger : this.villagerAverageHungerByType.get(t);
   }

   public ArrayList<Integer> getVillagerAverageThirst(MobBase.MobType t) {
      return t == null ? this.villagerAverageThirst : this.villagerAverageThirstByType.get(t);
   }

   public ArrayList<Integer> getVillagerAverageEnergy(MobBase.MobType t) {
      return t == null ? this.villagerAverageEnergy : this.villagerAverageEnergyByType.get(t);
   }

   public ArrayList<Integer> getAnimalTotal(MobBase.MobType t) {
      return t == null ? this.animalTotal : this.animalTotalByType.get(t);
   }

   public ArrayList<Integer> getAnimalMale(MobBase.MobType t) {
      return t == null ? this.animalMale : this.animalMaleByType.get(t);
   }

   public ArrayList<Integer> getAnimalFemale(MobBase.MobType t) {
      return t == null ? this.animalFemale : this.animalFemaleByType.get(t);
   }

   public ArrayList<Integer> getAnimalDomesticated(MobBase.MobType t) {
      return t == null ? this.animalDomesticated : this.animalDomesticatedByType.get(t);
   }

   public void increaseAnimalDomesticatedThisPeriod(MobBase.MobType t) {
      if (this.animalDomesticatedThisPeriod.containsKey(t)) {
         this.animalDomesticatedThisPeriod.put(t, this.animalDomesticatedThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getAnimalDeaths(MobBase.MobType t) {
      return t == null ? this.animalDeaths : this.animalDeathsByType.get(t);
   }

   public void increaseAnimalDeathsThisPeriod(MobBase.MobType t) {
      if (this.animalDeathsThisPeriod.containsKey(t)) {
         this.animalDeathsThisPeriod.put(t, this.animalDeathsThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getAnimalAverageHunger(MobBase.MobType t) {
      return t == null ? this.animalAverageHunger : this.animalAverageHungerByType.get(t);
   }

   public ArrayList<Integer> getAnimalAverageThirst(MobBase.MobType t) {
      return t == null ? this.animalAverageThirst : this.animalAverageThirstByType.get(t);
   }

   public ArrayList<Integer> getAnimalAverageEnergy(MobBase.MobType t) {
      return t == null ? this.animalAverageEnergy : this.animalAverageEnergyByType.get(t);
   }

   public ArrayList<Integer> getCorruptionTotal() {
      return this.corruptionTotal;
   }

   public ArrayList<Integer> getCorruptionSpreadAmount() {
      return this.corruptionSpreadAmount;
   }

   public ArrayList<Integer> getCorruptedBuildings() {
      return this.corruptedBuildings;
   }

   public ArrayList<Integer> getDronesTotal() {
      return this.dronesTotal;
   }

   public ArrayList<Integer> getMonstersSpawned(MobBase.MobType t) {
      return t == null ? this.monstersSpawned : this.monstersSpawnedByType.get(t);
   }

   public void increaseMonstersSpawnedThisPeriod(MobBase.MobType t) {
      if (this.monstersSpawnedThisPeriod.containsKey(t)) {
         this.monstersSpawnedThisPeriod.put(t, this.monstersSpawnedThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getMonstersKilled(MobBase.MobType t) {
      return t == null ? this.monstersKilled : this.monstersKilledByType.get(t);
   }

   public void increaseMonstersKilledThisPeriod(MobBase.MobType t) {
      if (this.monstersKilledThisPeriod.containsKey(t)) {
         this.monstersKilledThisPeriod.put(t, this.monstersKilledThisPeriod.get(t) + 1);
      }
   }

   public ArrayList<Integer> getBuildingsTotal() {
      return this.buildingsTotal;
   }

   public ArrayList<Integer> getBuildingsSlots() {
      return this.buildingSlots;
   }

   public ArrayList<Integer> getBuildingsBuilt() {
      return this.buildingsBuilt;
   }

   public void increaseBuildingsBuiltThisPeriod() {
      this.buildingsBuiltThisPeriod++;
   }

   public ArrayList<Integer> getBuildingsUpgraded() {
      return this.buildingsUpgraded;
   }

   public void increaseBuildingsUpgradedThisPeriod() {
      this.buildingsUpgradedThisPeriod++;
   }

   public ArrayList<Integer> getBuildingsDestroyed() {
      return this.buildingsDestroyed;
   }

   public void increaseBuildingsDestroyedThisPeriod() {
      this.buildingsDestroyedThisPeriod++;
   }

   public ArrayList<Integer> getBuildingsDismantled() {
      return this.buildingsDismantled;
   }

   public void increaseBuildingsDismantledThisPeriod() {
      this.buildingsDismantledThisPeriod++;
   }

   public ArrayList<Integer> getTowersTotal() {
      return this.towersTotal;
   }

   public ArrayList<Integer> getCombobulatorsTotal() {
      return this.combobulatorsTotal;
   }

   public ArrayList<Integer> getGolemTotal(MobBase.MobType t) {
      return t == null ? this.golemTotal : this.golemTotalByType.get(t);
   }

   public ArrayList<Integer> getRangerTotal(MobBase.MobType t) {
      return t == null ? this.rangerTotal : this.rangerTotalByType.get(t);
   }

   public ArrayList<Integer> getWallsTotal() {
      return this.wallsTotal;
   }

   public ArrayList<Integer> getResourcesTotal(ResourceModule.ResourceType t) {
      return t == null ? this.resourcesTotal : this.resourcesTotalByType.get(t);
   }

   public ArrayList<Integer> getResourcesProductionRate(ResourceModule.ResourceType t) {
      return t == null ? this.resourcesProductionRate : this.resourcesProductionRateByType.get(t);
   }

   public ArrayList<Integer> getFoodProductionRate() {
      return this.foodProductionRate;
   }

   public ArrayList<Integer> getWaterProductionRate() {
      return this.waterProductionRate;
   }

   public ArrayList<Integer> getEssenceGenerated() {
      return this.essenceGenerated;
   }

   public void increaseEssenceGeneratedThisPeriod(int amount) {
      this.essenceGeneratedThisPeriod += amount;
   }

   public ArrayList<Integer> getEssenceDecayed() {
      return this.essenceDecayed;
   }

   public void increaseEssenceDecayedThisPeriod(int amount) {
      this.essenceDecayedThisPeriod += amount;
   }

   public ArrayList<Integer> getEnergyAmount() {
      return this.energyAmount;
   }

   public ArrayList<Integer> getEnergyMaximum() {
      return this.energyMaximum;
   }

   public ArrayList<Integer> getInfluenceSpent() {
      return this.influenceSpent;
   }

   public void increaseInfluenceSpentThisPeriod(int amount) {
      this.influenceSpentThisPeriod += amount;
   }

   public ArrayList<Integer> getInfluenceGenerated() {
      return this.influenceGenerated;
   }

   public void increaseInfluenceGeneratedThisPeriod(int amount) {
      this.influenceGeneratedThisPeriod += amount;
   }

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.lastRecordedDay = -1;

      MobBase.MobType[] var6;
      for (MobBase.MobType t : MobBase.MobType.values()) {
         MobBase.MobGroup g = MobFactory.getMobInstance(t).getMobGroup();
         if (g == MobBase.MobGroup.VILLAGER) {
            this.villagerTotalByType.put(t, new ArrayList<>());
            this.villagerMaleByType.put(t, new ArrayList<>());
            this.villagerFemaleByType.put(t, new ArrayList<>());
            this.villagerDeathsByType.put(t, new ArrayList<>());
            this.villagerDeathsThisPeriod.put(t, 0);
            this.villagerBirthsByType.put(t, new ArrayList<>());
            this.villagerBirthsThisPeriod.put(t, 0);
            this.villagerGrownUpByType.put(t, new ArrayList<>());
            this.villagerGrownUpThisPeriod.put(t, 0);
            this.villagerCurrentPregnanciesByType.put(t, new ArrayList<>());
            this.villagerNewPregnanciesByType.put(t, new ArrayList<>());
            this.villagerNewPregnanciesThisPeriod.put(t, 0);
            this.villagerCoitusByType.put(t, new ArrayList<>());
            this.villagerCoitusThisPeriod.put(t, 0);
            this.villagerAverageHungerByType.put(t, new ArrayList<>());
            this.villagerAverageThirstByType.put(t, new ArrayList<>());
            this.villagerAverageEnergyByType.put(t, new ArrayList<>());
            this.rangerTotalByType.put(t, new ArrayList<>());
         }

         if (g == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
            this.animalTotalByType.put(t, new ArrayList<>());
            this.animalMaleByType.put(t, new ArrayList<>());
            this.animalFemaleByType.put(t, new ArrayList<>());
            this.animalDomesticatedByType.put(t, new ArrayList<>());
            this.animalDomesticatedThisPeriod.put(t, 0);
            this.animalDeathsByType.put(t, new ArrayList<>());
            this.animalDeathsThisPeriod.put(t, 0);
            this.animalAverageHungerByType.put(t, new ArrayList<>());
            this.animalAverageThirstByType.put(t, new ArrayList<>());
            this.animalAverageEnergyByType.put(t, new ArrayList<>());
         }

         if (g == MobBase.MobGroup.PROVISIONER) {
            this.catjeetHiredByType.put(t, new ArrayList<>());
            this.catjeetHiredThisPeriod.put(t, 0);
         }

         if (g == MobBase.MobGroup.NOMAD) {
            this.nomadsJoinedByType.put(t, new ArrayList<>());
            this.nomadsJoinedThisPeriod.put(t, 0);
            this.nomadsVisitedByType.put(t, new ArrayList<>());
            this.nomadsVisitedThisPeriod.put(t, 0);
         }

         if (g == MobBase.MobGroup.DOMESTICATED_ANIMAL) {
            this.animalTotalByType.put(t, new ArrayList<>());
         }

         if (g == MobBase.MobGroup.MONSTER) {
            this.monstersSpawnedByType.put(t, new ArrayList<>());
            this.monstersSpawnedThisPeriod.put(t, 0);
            this.monstersKilledByType.put(t, new ArrayList<>());
            this.monstersKilledThisPeriod.put(t, 0);
         }

         if (g == MobBase.MobGroup.GOLEM) {
            this.golemTotalByType.put(t, new ArrayList<>());
         }
      }

      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         this.resourcesTotalByType.put(t, new ArrayList<>());
         this.resourcesProductionRateByType.put(t, new ArrayList<>());
      }

      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.lastRecordedDay = -1;
      this.villagerTotal.clear();
      this.villagerTotalByType.clear();
      this.villagerMale.clear();
      this.villagerMaleByType.clear();
      this.villagerFemale.clear();
      this.villagerFemaleByType.clear();
      this.villagerDeaths.clear();
      this.villagerDeathsByType.clear();
      this.villagerDeathsThisPeriod.clear();
      this.villagerBirths.clear();
      this.villagerBirthsByType.clear();
      this.villagerBirthsThisPeriod.clear();
      this.villagerGrownUp.clear();
      this.villagerGrownUpByType.clear();
      this.villagerGrownUpThisPeriod.clear();
      this.villagerCurrentPregnancies.clear();
      this.villagerCurrentPregnanciesByType.clear();
      this.villagerNewPregnancies.clear();
      this.villagerNewPregnanciesByType.clear();
      this.villagerNewPregnanciesThisPeriod.clear();
      this.villagerCoitus.clear();
      this.villagerCoitusByType.clear();
      this.villagerCoitusThisPeriod.clear();
      this.catjeetHired.clear();
      this.catjeetHiredByType.clear();
      this.catjeetHiredThisPeriod.clear();
      this.nomadsJoined.clear();
      this.nomadsJoinedByType.clear();
      this.nomadsJoinedThisPeriod.clear();
      this.nomadsVisited.clear();
      this.nomadsVisitedByType.clear();
      this.nomadsVisitedThisPeriod.clear();
      this.villagerAverageHunger.clear();
      this.villagerAverageHungerByType.clear();
      this.villagerAverageThirst.clear();
      this.villagerAverageThirstByType.clear();
      this.villagerAverageEnergy.clear();
      this.villagerAverageEnergyByType.clear();
      this.animalTotal.clear();
      this.animalTotalByType.clear();
      this.animalMale.clear();
      this.animalMaleByType.clear();
      this.animalFemale.clear();
      this.animalFemaleByType.clear();
      this.animalDomesticated.clear();
      this.animalDomesticatedByType.clear();
      this.animalDomesticatedThisPeriod.clear();
      this.animalDeaths.clear();
      this.animalDeathsByType.clear();
      this.animalDeathsThisPeriod.clear();
      this.animalAverageHunger.clear();
      this.animalAverageHungerByType.clear();
      this.animalAverageThirst.clear();
      this.animalAverageThirstByType.clear();
      this.animalAverageEnergy.clear();
      this.animalAverageEnergyByType.clear();
      this.corruptionTotal.clear();
      this.corruptionSpreadAmount.clear();
      this.corruptedBuildings.clear();
      this.dronesTotal.clear();
      this.monstersSpawned.clear();
      this.monstersSpawnedByType.clear();
      this.monstersKilled.clear();
      this.monstersKilledByType.clear();
      this.monstersKilledThisPeriod.clear();
      this.buildingsTotal.clear();
      this.buildingSlots.clear();
      this.buildingsBuilt.clear();
      this.buildingsBuiltThisPeriod = 0;
      this.buildingsUpgraded.clear();
      this.buildingsUpgradedThisPeriod = 0;
      this.buildingsDestroyed.clear();
      this.buildingsDestroyedThisPeriod = 0;
      this.buildingsDismantled.clear();
      this.buildingsDismantledThisPeriod = 0;
      this.towersTotal.clear();
      this.combobulatorsTotal.clear();
      this.golemTotal.clear();
      this.golemTotalByType.clear();
      this.rangerTotal.clear();
      this.rangerTotalByType.clear();
      this.wallsTotal.clear();
      this.resourcesTotal.clear();
      this.resourcesTotalByType.clear();
      this.resourcesProductionRate.clear();
      this.resourcesProductionRateByType.clear();
      this.foodProductionRate.clear();
      this.waterProductionRate.clear();
      this.essenceGenerated.clear();
      this.essenceGeneratedThisPeriod = 0;
      this.essenceDecayed.clear();
      this.essenceDecayedThisPeriod = 0;
      this.energyAmount.clear();
      this.energyMaximum.clear();
      this.influenceSpent.clear();
      this.influenceSpentThisPeriod = 0;
      this.influenceGenerated.clear();
      this.influenceGeneratedThisPeriod = 0;
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
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
      this.lastRecordedDay = Integer.parseInt(dataMap.getOrDefault("lastRecordedDay", "-1"));
      this.processSavedString(this.villagerTotal, dataMap.getOrDefault("villagerTotal", "0"));
      this.processSavedStringMobTypeMap(this.villagerTotalByType, dataMap.getSubMap("villagerTotalByType"));
      this.processSavedString(this.villagerMale, dataMap.getOrDefault("villagerMale", "0"));
      this.processSavedStringMobTypeMap(this.villagerMaleByType, dataMap.getSubMap("villagerMaleByType"));
      this.processSavedString(this.villagerFemale, dataMap.getOrDefault("villagerFemale", "0"));
      this.processSavedStringMobTypeMap(this.villagerFemaleByType, dataMap.getSubMap("villagerFemaleByType"));
      this.processSavedString(this.villagerDeaths, dataMap.getOrDefault("villagerDeaths", "0"));
      this.processSavedStringMobTypeMap(this.villagerDeathsByType, dataMap.getSubMap("villagerDeathsByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.villagerDeathsThisPeriod, dataMap.getSubMap("villagerDeathsThisPeriod"));
      this.processSavedString(this.villagerBirths, dataMap.getOrDefault("villagerBirths", "0"));
      this.processSavedStringMobTypeMap(this.villagerBirthsByType, dataMap.getSubMap("villagerBirthsByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.villagerBirthsThisPeriod, dataMap.getSubMap("villagerBirthsThisPeriod"));
      this.processSavedString(this.villagerGrownUp, dataMap.getOrDefault("villagerGrownUp", "0"));
      this.processSavedStringMobTypeMap(this.villagerGrownUpByType, dataMap.getSubMap("villagerGrownUpByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.villagerGrownUpThisPeriod, dataMap.getSubMap("villagerGrownUpThisPeriod"));
      this.processSavedString(this.villagerCurrentPregnancies, dataMap.getOrDefault("villagerCurrentPregnancies", "0"));
      this.processSavedStringMobTypeMap(this.villagerCurrentPregnanciesByType, dataMap.getSubMap("villagerCurrentPregnanciesByType"));
      this.processSavedString(this.villagerNewPregnancies, dataMap.getOrDefault("villagerNewPregnancies", "0"));
      this.processSavedStringMobTypeMap(this.villagerNewPregnanciesByType, dataMap.getSubMap("villagerNewPregnanciesByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.villagerNewPregnanciesThisPeriod, dataMap.getSubMap("villagerNewPregnanciesThisPeriod"));
      this.processSavedString(this.villagerCoitus, dataMap.getOrDefault("villagerCoitus", "0"));
      this.processSavedStringMobTypeMap(this.villagerCoitusByType, dataMap.getSubMap("villagerCoitusByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.villagerCoitusThisPeriod, dataMap.getSubMap("villagerCoitusThisPeriod"));
      this.processSavedString(this.catjeetHired, dataMap.getOrDefault("catjeetHired", "0"));
      this.processSavedStringMobTypeMap(this.catjeetHiredByType, dataMap.getSubMap("catjeetHiredByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.catjeetHiredThisPeriod, dataMap.getSubMap("catjeetHiredThisPeriod"));
      this.processSavedString(this.nomadsJoined, dataMap.getOrDefault("nomadsJoined", "0"));
      this.processSavedStringMobTypeMap(this.nomadsJoinedByType, dataMap.getSubMap("nomadsJoinedByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.nomadsJoinedThisPeriod, dataMap.getSubMap("nomadsJoinedThisPeriod"));
      this.processSavedString(this.nomadsVisited, dataMap.getOrDefault("nomadsVisited", "0"));
      this.processSavedStringMobTypeMap(this.nomadsVisitedByType, dataMap.getSubMap("nomadsVisitedByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.nomadsVisitedThisPeriod, dataMap.getSubMap("nomadsVisitedThisPeriod"));
      this.processSavedString(this.villagerAverageHunger, dataMap.getOrDefault("villagerAverageHunger", "0"));
      this.processSavedStringMobTypeMap(this.villagerAverageHungerByType, dataMap.getSubMap("villagerAverageHungerByType"));
      this.processSavedString(this.villagerAverageThirst, dataMap.getOrDefault("villagerAverageThirst", "0"));
      this.processSavedStringMobTypeMap(this.villagerAverageThirstByType, dataMap.getSubMap("villagerAverageThirstByType"));
      this.processSavedString(this.villagerAverageEnergy, dataMap.getOrDefault("villagerAverageEnergy", "0"));
      this.processSavedStringMobTypeMap(this.villagerAverageEnergyByType, dataMap.getSubMap("villagerAverageEnergyByType"));
      this.processSavedString(this.animalTotal, dataMap.getOrDefault("animalTotal", "0"));
      this.processSavedStringMobTypeMap(this.animalTotalByType, dataMap.getSubMap("animalTotalByType"));
      this.processSavedString(this.animalMale, dataMap.getOrDefault("animalMale", "0"));
      this.processSavedStringMobTypeMap(this.animalMaleByType, dataMap.getSubMap("animalMaleByType"));
      this.processSavedString(this.animalFemale, dataMap.getOrDefault("animalFemale", "0"));
      this.processSavedStringMobTypeMap(this.animalFemaleByType, dataMap.getSubMap("animalFemaleByType"));
      this.processSavedString(this.animalDomesticated, dataMap.getOrDefault("animalDomesticated", "0"));
      this.processSavedStringMobTypeMap(this.animalDomesticatedByType, dataMap.getSubMap("animalDomesticatedByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.animalDomesticatedThisPeriod, dataMap.getSubMap("animalDomesticatedThisPeriod"));
      this.processSavedString(this.animalDeaths, dataMap.getOrDefault("animalDeaths", "0"));
      this.processSavedStringMobTypeMap(this.animalDeathsByType, dataMap.getSubMap("animalDeathsByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.animalDeathsThisPeriod, dataMap.getSubMap("animalDeathsThisPeriod"));
      this.processSavedString(this.animalAverageHunger, dataMap.getOrDefault("animalAverageHunger", "0"));
      this.processSavedStringMobTypeMap(this.animalAverageHungerByType, dataMap.getSubMap("animalAverageHungerByType"));
      this.processSavedString(this.animalAverageThirst, dataMap.getOrDefault("animalAverageThirst", "0"));
      this.processSavedStringMobTypeMap(this.animalAverageThirstByType, dataMap.getSubMap("animalAverageThirstByType"));
      this.processSavedString(this.animalAverageEnergy, dataMap.getOrDefault("animalAverageEnergy", "0"));
      this.processSavedStringMobTypeMap(this.animalAverageEnergyByType, dataMap.getSubMap("animalAverageEnergyByType"));
      this.processSavedString(this.corruptionTotal, dataMap.getOrDefault("corruptionTotal", "0"));
      this.processSavedString(this.corruptionSpreadAmount, dataMap.getOrDefault("corruptionSpreadAmount", "0"));
      this.processSavedString(this.corruptedBuildings, dataMap.getOrDefault("corruptedBuildings", "0"));
      this.processSavedString(this.dronesTotal, dataMap.getOrDefault("dronesTotal", "0"));
      this.processSavedString(this.monstersSpawned, dataMap.getOrDefault("monstersSpawned", "0"));
      this.processSavedStringMobTypeMap(this.monstersSpawnedByType, dataMap.getSubMap("monstersSpawnedByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.monstersSpawnedThisPeriod, dataMap.getSubMap("monstersSpawnedThisPeriod"));
      this.processSavedString(this.monstersKilled, dataMap.getOrDefault("monstersKilled", "0"));
      this.processSavedStringMobTypeMap(this.monstersKilledByType, dataMap.getSubMap("monstersKilledByType"));
      this.processSavedStringMobTypeMapThisPeriod(this.monstersKilledThisPeriod, dataMap.getSubMap("monstersKilledThisPeriod"));
      this.processSavedString(this.buildingsTotal, dataMap.getOrDefault("buildingsTotal", "0"));
      this.processSavedString(this.buildingSlots, dataMap.getOrDefault("buildingSlots", "0"));
      this.processSavedString(this.buildingsBuilt, dataMap.getOrDefault("buildingsBuilt", "0"));
      this.buildingsBuiltThisPeriod = Integer.parseInt(dataMap.getOrDefault("buildingsBuiltThisPeriod", "0"));
      this.processSavedString(this.buildingsUpgraded, dataMap.getOrDefault("buildingsUpgraded", "0"));
      this.buildingsUpgradedThisPeriod = Integer.parseInt(dataMap.getOrDefault("buildingsUpgradedThisPeriod", "0"));
      this.processSavedString(this.buildingsDestroyed, dataMap.getOrDefault("buildingsDestroyed", "0"));
      this.buildingsDestroyedThisPeriod = Integer.parseInt(dataMap.getOrDefault("buildingsDestroyedThisPeriod", "0"));
      this.processSavedString(this.buildingsDismantled, dataMap.getOrDefault("buildingsDismantled", "0"));
      this.buildingsDismantledThisPeriod = Integer.parseInt(dataMap.getOrDefault("buildingsDismantledThisPeriod", "0"));
      this.processSavedString(this.towersTotal, dataMap.getOrDefault("towersTotal", "0"));
      this.processSavedString(this.combobulatorsTotal, dataMap.getOrDefault("combobulatorsTotal", "0"));
      this.processSavedString(this.golemTotal, dataMap.getOrDefault("golemTotal", "0"));
      this.processSavedStringMobTypeMap(this.golemTotalByType, dataMap.getSubMap("golemTotalByType"));
      if (dataMap.containsMap("guardTotalByType")) {
         Console.saveConverterOut(this, "InDev 33f", "InDev 34 Unstable 2", "Converting old guardTotal and guardTotalByType.");
         this.processSavedString(this.rangerTotal, dataMap.getOrDefault("guardTotal", "0"));
         this.processSavedStringMobTypeMap(this.rangerTotalByType, dataMap.getSubMap("guardTotalByType"));
      } else {
         this.processSavedString(this.rangerTotal, dataMap.getOrDefault("rangerTotal", "0"));
         this.processSavedStringMobTypeMap(this.rangerTotalByType, dataMap.getSubMap("rangerTotalByType"));
      }

      this.processSavedString(this.wallsTotal, dataMap.getOrDefault("wallsTotal", "0"));
      this.processSavedString(this.resourcesTotal, dataMap.getOrDefault("resourcesTotal", "0"));
      this.processSavedStringResourceTypeMap(this.resourcesTotalByType, dataMap.getSubMap("resourcesTotalByType"));
      this.processSavedString(this.resourcesProductionRate, dataMap.getOrDefault("resourcesProductionRate", "0"));
      this.processSavedStringResourceTypeMap(this.resourcesProductionRateByType, dataMap.getSubMap("resourcesProductionRateByType"));
      this.processSavedString(this.foodProductionRate, dataMap.getOrDefault("foodProductionRate", "0"));
      this.processSavedString(this.waterProductionRate, dataMap.getOrDefault("waterProductionRate", "0"));
      this.processSavedString(this.essenceGenerated, dataMap.getOrDefault("essenceGenerated", "0"));
      this.essenceGeneratedThisPeriod = Integer.parseInt(dataMap.getOrDefault("essenceGeneratedThisPeriod", "0"));
      this.processSavedString(this.essenceDecayed, dataMap.getOrDefault("essenceDecayed", "0"));
      this.essenceDecayedThisPeriod = Integer.parseInt(dataMap.getOrDefault("essenceDecayedThisPeriod", "0"));
      this.processSavedString(this.energyAmount, dataMap.getOrDefault("energyAmount", "0"));
      this.processSavedString(this.energyMaximum, dataMap.getOrDefault("energyMaximum", "0"));
      this.processSavedString(this.influenceSpent, dataMap.getOrDefault("influenceSpent", "0"));
      this.influenceSpentThisPeriod = Integer.parseInt(dataMap.getOrDefault("influenceSpentThisPeriod", "0"));
      this.processSavedString(this.influenceGenerated, dataMap.getOrDefault("influenceGenerated", "0"));
      this.influenceGeneratedThisPeriod = Integer.parseInt(dataMap.getOrDefault("influenceGeneratedThisPeriod", "0"));
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   private void processSavedStringMobTypeMap(HashMap<MobBase.MobType, ArrayList<Integer>> hashMap, YMLDataMap dataMap) {
      if (dataMap != null) {
         for (MobBase.MobType t : hashMap.keySet()) {
            this.processSavedString(hashMap.get(t), dataMap.getOrDefault(t.toString(), "0"));
         }
      }
   }

   private void processSavedStringMobTypeMapThisPeriod(HashMap<MobBase.MobType, Integer> hashMap, YMLDataMap dataMap) {
      if (dataMap != null) {
         for (MobBase.MobType t : hashMap.keySet()) {
            hashMap.put(t, Integer.parseInt(dataMap.getOrDefault(t.toString(), "0")));
         }
      }
   }

   private void processSavedStringResourceTypeMap(HashMap<ResourceModule.ResourceType, ArrayList<Integer>> hashMap, YMLDataMap dataMap) {
      if (dataMap != null) {
         for (ResourceModule.ResourceType t : hashMap.keySet()) {
            this.processSavedString(hashMap.get(t), dataMap.getOrDefault(t.toString(), "0"));
         }
      }
   }

   private void processSavedString(ArrayList<Integer> array, String arrayString) {
      if (arrayString != null && !arrayString.equals("[]")) {
         arrayString = arrayString.replaceAll("[ \\[\\]]", "");
         String[] inArray = arrayString.split(",");

         for (int i = 0; i < inArray.length; i++) {
            array.add(Integer.parseInt(inArray[i]));
         }
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      dataMap.put("lastRecordedDay", this.lastRecordedDay);
      dataMap.put("villagerTotal", this.villagerTotal.toString());
      dataMap.put("villagerTotalByType", this.getRegionalSaveDataMobMap(this.villagerTotalByType));
      dataMap.put("villagerMale", this.villagerMale.toString());
      dataMap.put("villagerMaleByType", this.getRegionalSaveDataMobMap(this.villagerMaleByType));
      dataMap.put("villagerFemale", this.villagerFemale.toString());
      dataMap.put("villagerFemaleByType", this.getRegionalSaveDataMobMap(this.villagerFemaleByType));
      dataMap.put("villagerDeaths", this.villagerDeaths.toString());
      dataMap.put("villagerDeathsByType", this.getRegionalSaveDataMobMap(this.villagerDeathsByType));
      dataMap.put("villagerDeathsThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.villagerDeathsThisPeriod));
      dataMap.put("villagerBirths", this.villagerBirths.toString());
      dataMap.put("villagerBirthsByType", this.getRegionalSaveDataMobMap(this.villagerBirthsByType));
      dataMap.put("villagerBirthsThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.villagerBirthsThisPeriod));
      dataMap.put("villagerGrownUp", this.villagerGrownUp.toString());
      dataMap.put("villagerGrownUpByType", this.getRegionalSaveDataMobMap(this.villagerGrownUpByType));
      dataMap.put("villagerGrownUpThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.villagerGrownUpThisPeriod));
      dataMap.put("villagerCurrentPregnancies", this.villagerCurrentPregnancies.toString());
      dataMap.put("villagerCurrentPregnanciesByType", this.getRegionalSaveDataMobMap(this.villagerCurrentPregnanciesByType));
      dataMap.put("villagerNewPregnancies", this.villagerNewPregnancies.toString());
      dataMap.put("villagerNewPregnanciesByType", this.getRegionalSaveDataMobMap(this.villagerNewPregnanciesByType));
      dataMap.put("villagerNewPregnanciesThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.villagerNewPregnanciesThisPeriod));
      dataMap.put("villagerCoitus", this.villagerCoitus.toString());
      dataMap.put("villagerCoitusByType", this.getRegionalSaveDataMobMap(this.villagerCoitusByType));
      dataMap.put("villagerCoitusThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.villagerCoitusThisPeriod));
      dataMap.put("catjeetHired", this.catjeetHired.toString());
      dataMap.put("catjeetHiredByType", this.getRegionalSaveDataMobMap(this.catjeetHiredByType));
      dataMap.put("catjeetHiredThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.catjeetHiredThisPeriod));
      dataMap.put("nomadsJoined", this.nomadsJoined.toString());
      dataMap.put("nomadsJoinedByType", this.getRegionalSaveDataMobMap(this.nomadsJoinedByType));
      dataMap.put("nomadsJoinedThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.nomadsJoinedThisPeriod));
      dataMap.put("nomadsVisited", this.nomadsVisited.toString());
      dataMap.put("nomadsVisitedByType", this.getRegionalSaveDataMobMap(this.nomadsVisitedByType));
      dataMap.put("nomadsVisitedThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.nomadsVisitedThisPeriod));
      dataMap.put("villagerAverageHunger", this.villagerAverageHunger.toString());
      dataMap.put("villagerAverageHungerByType", this.getRegionalSaveDataMobMap(this.villagerAverageHungerByType));
      dataMap.put("villagerAverageThirst", this.villagerAverageThirst.toString());
      dataMap.put("villagerAverageThirstByType", this.getRegionalSaveDataMobMap(this.villagerAverageThirstByType));
      dataMap.put("villagerAverageEnergy", this.villagerAverageEnergy.toString());
      dataMap.put("villagerAverageEnergyByType", this.getRegionalSaveDataMobMap(this.villagerAverageEnergyByType));
      dataMap.put("animalTotal", this.animalTotal.toString());
      dataMap.put("animalTotalByType", this.getRegionalSaveDataMobMap(this.animalTotalByType));
      dataMap.put("animalMale", this.animalMale.toString());
      dataMap.put("animalMaleByType", this.getRegionalSaveDataMobMap(this.animalMaleByType));
      dataMap.put("animalFemale", this.animalFemale.toString());
      dataMap.put("animalFemaleByType", this.getRegionalSaveDataMobMap(this.animalFemaleByType));
      dataMap.put("animalDomesticated", this.animalDomesticated.toString());
      dataMap.put("animalDomesticatedByType", this.getRegionalSaveDataMobMap(this.animalDomesticatedByType));
      dataMap.put("animalDomesticatedThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.animalDomesticatedThisPeriod));
      dataMap.put("animalDeaths", this.animalDeaths.toString());
      dataMap.put("animalDeathsByType", this.getRegionalSaveDataMobMap(this.animalDeathsByType));
      dataMap.put("animalDeathsThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.animalDeathsThisPeriod));
      dataMap.put("animalAverageHunger", this.animalAverageHunger.toString());
      dataMap.put("animalAverageHungerByType", this.getRegionalSaveDataMobMap(this.animalAverageHungerByType));
      dataMap.put("animalAverageThirst", this.animalAverageThirst.toString());
      dataMap.put("animalAverageThirstByType", this.getRegionalSaveDataMobMap(this.animalAverageThirstByType));
      dataMap.put("animalAverageEnergy", this.animalAverageEnergy.toString());
      dataMap.put("animalAverageEnergyByType", this.getRegionalSaveDataMobMap(this.animalAverageEnergyByType));
      dataMap.put("corruptionTotal", this.corruptionTotal.toString());
      dataMap.put("corruptionSpreadAmount", this.corruptionSpreadAmount.toString());
      dataMap.put("corruptedBuildings", this.corruptedBuildings.toString());
      dataMap.put("dronesTotal", this.dronesTotal.toString());
      dataMap.put("monstersSpawned", this.monstersSpawned.toString());
      dataMap.put("monstersSpawnedByType", this.getRegionalSaveDataMobMap(this.monstersSpawnedByType));
      dataMap.put("monstersSpawnedThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.monstersSpawnedThisPeriod));
      dataMap.put("monstersKilled", this.monstersKilled.toString());
      dataMap.put("monstersKilledByType", this.getRegionalSaveDataMobMap(this.monstersKilledByType));
      dataMap.put("monstersKilledThisPeriod", this.getRegionalSaveDataMobMapThisPeriod(this.monstersKilledThisPeriod));
      dataMap.put("buildingsTotal", this.buildingsTotal.toString());
      dataMap.put("buildingSlots", this.buildingSlots.toString());
      dataMap.put("buildingsBuilt", this.buildingsBuilt.toString());
      dataMap.put("buildingsBuiltThisPeriod", this.buildingsBuiltThisPeriod);
      dataMap.put("buildingsUpgraded", this.buildingsUpgraded.toString());
      dataMap.put("buildingsUpgradedThisPeriod", this.buildingsUpgradedThisPeriod);
      dataMap.put("buildingsDestroyed", this.buildingsDestroyed.toString());
      dataMap.put("buildingsDestroyedThisPeriod", this.buildingsDestroyedThisPeriod);
      dataMap.put("buildingsDismantled", this.buildingsDismantled.toString());
      dataMap.put("buildingsDismantledThisPeriod", this.buildingsDismantledThisPeriod);
      dataMap.put("towersTotal", this.towersTotal.toString());
      dataMap.put("combobulatorsTotal", this.combobulatorsTotal.toString());
      dataMap.put("golemTotal", this.golemTotal.toString());
      dataMap.put("golemTotalByType", this.getRegionalSaveDataMobMap(this.golemTotalByType));
      dataMap.put("rangerTotal", this.rangerTotal.toString());
      dataMap.put("rangerTotalByType", this.getRegionalSaveDataMobMap(this.rangerTotalByType));
      dataMap.put("wallsTotal", this.wallsTotal.toString());
      dataMap.put("resourcesTotal", this.resourcesTotal.toString());
      dataMap.put("resourcesTotalByType", this.getRegionalSaveDataResourceMap(this.resourcesTotalByType));
      dataMap.put("resourcesProductionRate", this.resourcesProductionRate.toString());
      dataMap.put("resourcesProductionRateByType", this.getRegionalSaveDataResourceMap(this.resourcesProductionRateByType));
      dataMap.put("foodProductionRate", this.foodProductionRate.toString());
      dataMap.put("waterProductionRate", this.waterProductionRate.toString());
      dataMap.put("essenceGenerated", this.essenceGenerated.toString());
      dataMap.put("essenceGeneratedThisPeriod", this.essenceGeneratedThisPeriod);
      dataMap.put("essenceDecayed", this.essenceDecayed.toString());
      dataMap.put("essenceDecayedThisPeriod", this.essenceDecayedThisPeriod);
      dataMap.put("energyAmount", this.energyAmount.toString());
      dataMap.put("energyMaximum", this.energyMaximum.toString());
      dataMap.put("influenceSpent", this.influenceSpent.toString());
      dataMap.put("influenceSpentThisPeriod", this.influenceSpentThisPeriod);
      dataMap.put("influenceGenerated", this.influenceGenerated.toString());
      dataMap.put("influenceGeneratedThisPeriod", this.influenceGeneratedThisPeriod);
      return dataMap;
   }

   private Map<String, Object> getRegionalSaveDataMobMap(HashMap<MobBase.MobType, ArrayList<Integer>> hashMap) {
      Map<String, Object> thisMap = new HashMap<>();

      for (MobBase.MobType t : hashMap.keySet()) {
         thisMap.put(t.toString(), hashMap.get(t).toString());
      }

      return thisMap;
   }

   private Map<String, Object> getRegionalSaveDataMobMapThisPeriod(HashMap<MobBase.MobType, Integer> hashMap) {
      Map<String, Object> thisMap = new HashMap<>();

      for (MobBase.MobType t : hashMap.keySet()) {
         thisMap.put(t.toString(), hashMap.get(t).toString());
      }

      return thisMap;
   }

   private Map<String, Object> getRegionalSaveDataResourceMap(HashMap<ResourceModule.ResourceType, ArrayList<Integer>> hashMap) {
      Map<String, Object> thisMap = new HashMap<>();

      for (ResourceModule.ResourceType r : hashMap.keySet()) {
         thisMap.put(r.toString(), hashMap.get(r).toString());
      }

      return thisMap;
   }

   public void update() {
      int currentDay = this.time.getDay();
      if (currentDay != this.lastRecordedDay) {
         this.processVillagerData();
         this.processAnimalData();
         this.processCorruptionData();
         this.processBuildingData();
         this.processDefensesData();
         this.processResourcesData();
         this.processMagicAndEnergyData();
         this.lastRecordedDay = currentDay;
      }
   }

   private void processVillagerData() {
      ArrayList<MobBase> villagers = this.mob.getMobsByGroup(MobBase.MobGroup.VILLAGER, false);
      this.villagerTotal.add(villagers.size());
      this.villagerAverageHunger.add(this.mob.getAverageHunger(MobBase.MobGroup.VILLAGER));
      this.villagerAverageThirst.add(this.mob.getAverageThirst(MobBase.MobGroup.VILLAGER));
      this.villagerAverageEnergy.add(this.mob.getAverageEnergy(MobBase.MobGroup.VILLAGER));
      int maleTotal = 0;
      int femaleTotal = 0;

      for (MobBase.MobType t : this.villagerTotalByType.keySet()) {
         ArrayList<MobBase> mobs = this.mob.getMobsByType(t, false);
         this.villagerTotalByType.get(t).add(mobs.size());
         int male = 0;
         int female = 0;

         for (MobBase m : this.mob.getMobsByType(t, false)) {
            if (m.getMobType() == t) {
               if (m.getGender() == MobBase.MobGender.MALE) {
                  male++;
                  maleTotal++;
               } else if (m.getGender() == MobBase.MobGender.FEMALE) {
                  female++;
                  femaleTotal++;
               }
            }
         }

         this.villagerMaleByType.get(t).add(male);
         this.villagerFemaleByType.get(t).add(female);
         this.villagerAverageHungerByType.get(t).add(this.mob.getAverageHunger(t));
         this.villagerAverageThirstByType.get(t).add(this.mob.getAverageThirst(t));
         this.villagerAverageEnergyByType.get(t).add(this.mob.getAverageEnergy(t));
      }

      this.villagerMale.add(maleTotal);
      this.villagerFemale.add(femaleTotal);
      int deathsTotal = 0;

      for (MobBase.MobType t : this.villagerDeathsThisPeriod.keySet()) {
         this.villagerDeathsByType.get(t).add(this.villagerDeathsThisPeriod.get(t));
         deathsTotal += this.villagerDeathsThisPeriod.get(t);
         this.villagerDeathsThisPeriod.put(t, 0);
      }

      this.villagerDeaths.add(deathsTotal);
      int birthsTotal = 0;

      for (MobBase.MobType t : this.villagerBirthsThisPeriod.keySet()) {
         this.villagerBirthsByType.get(t).add(this.villagerBirthsThisPeriod.get(t));
         birthsTotal += this.villagerBirthsThisPeriod.get(t);
         this.villagerBirthsThisPeriod.put(t, 0);
      }

      this.villagerBirths.add(birthsTotal);
      int grownUpTotal = 0;

      for (MobBase.MobType t : this.villagerGrownUpThisPeriod.keySet()) {
         this.villagerGrownUpByType.get(t).add(this.villagerGrownUpThisPeriod.get(t));
         grownUpTotal += this.villagerGrownUpThisPeriod.get(t);
         this.villagerGrownUpThisPeriod.put(t, 0);
      }

      this.villagerGrownUp.add(grownUpTotal);
      int pregnancyTotal = 0;

      for (MobBase.MobType t : this.villagerCurrentPregnanciesByType.keySet()) {
         int pregnancyTotalByType = 0;

         for (MobBase mx : this.mob.getMobsByType(t, false)) {
            if (mx.isPregnant()) {
               pregnancyTotalByType++;
               pregnancyTotal++;
            }
         }

         this.villagerCurrentPregnanciesByType.get(t).add(pregnancyTotalByType);
      }

      this.villagerCurrentPregnancies.add(pregnancyTotal);
      int pregnanciesTotal = 0;

      for (MobBase.MobType t : this.villagerNewPregnanciesThisPeriod.keySet()) {
         this.villagerNewPregnanciesByType.get(t).add(this.villagerNewPregnanciesThisPeriod.get(t));
         pregnanciesTotal += this.villagerNewPregnanciesThisPeriod.get(t);
         this.villagerNewPregnanciesThisPeriod.put(t, 0);
      }

      this.villagerNewPregnancies.add(pregnanciesTotal);
      int coitusTotal = 0;

      for (MobBase.MobType t : this.villagerCoitusThisPeriod.keySet()) {
         this.villagerCoitusByType.get(t).add(this.villagerCoitusThisPeriod.get(t));
         coitusTotal += this.villagerCoitusThisPeriod.get(t);
         this.villagerCoitusThisPeriod.put(t, 0);
      }

      this.villagerCoitus.add(coitusTotal);
      int catjeetHiredTotal = 0;

      for (MobBase.MobType t : this.catjeetHiredThisPeriod.keySet()) {
         this.catjeetHiredByType.get(t).add(this.catjeetHiredThisPeriod.get(t));
         catjeetHiredTotal += this.catjeetHiredThisPeriod.get(t);
         this.catjeetHiredThisPeriod.put(t, 0);
      }

      this.catjeetHired.add(catjeetHiredTotal);
      int nomadsJoinedTotal = 0;

      for (MobBase.MobType t : this.nomadsJoinedThisPeriod.keySet()) {
         this.nomadsJoinedByType.get(t).add(this.nomadsJoinedThisPeriod.get(t));
         nomadsJoinedTotal += this.nomadsJoinedThisPeriod.get(t);
         this.nomadsJoinedThisPeriod.put(t, 0);
      }

      this.nomadsJoined.add(nomadsJoinedTotal);
      int nomadsVisitedTotal = 0;

      for (MobBase.MobType t : this.nomadsVisitedThisPeriod.keySet()) {
         this.nomadsVisitedByType.get(t).add(this.nomadsVisitedThisPeriod.get(t));
         nomadsVisitedTotal += this.nomadsVisitedThisPeriod.get(t);
         this.nomadsVisitedThisPeriod.put(t, 0);
      }

      this.nomadsVisited.add(nomadsVisitedTotal);
   }

   private void processAnimalData() {
      ArrayList<MobBase> animals = this.mob.getMobsByGroup(MobBase.MobGroup.DOMESTICATED_ANIMAL, false);
      this.animalTotal.add(animals.size());
      this.animalAverageHunger.add(this.mob.getAverageHunger(MobBase.MobGroup.DOMESTICATED_ANIMAL));
      this.animalAverageThirst.add(this.mob.getAverageThirst(MobBase.MobGroup.DOMESTICATED_ANIMAL));
      this.animalAverageEnergy.add(this.mob.getAverageEnergy(MobBase.MobGroup.DOMESTICATED_ANIMAL));
      int maleTotal = 0;
      int femaleTotal = 0;

      for (MobBase.MobType t : this.animalTotalByType.keySet()) {
         ArrayList<MobBase> mobs = this.mob.getMobsByType(t, false);
         this.animalTotalByType.get(t).add(mobs.size());
         int male = 0;
         int female = 0;

         for (MobBase m : this.mob.getMobsByType(t, false)) {
            if (m.getMobType() == t) {
               if (m.getGender() == MobBase.MobGender.MALE) {
                  male++;
                  maleTotal++;
               } else if (m.getGender() == MobBase.MobGender.FEMALE) {
                  female++;
                  femaleTotal++;
               }
            }
         }

         this.animalMaleByType.get(t).add(male);
         this.animalFemaleByType.get(t).add(female);
         this.animalAverageHungerByType.get(t).add(this.mob.getAverageHunger(t));
         this.animalAverageThirstByType.get(t).add(this.mob.getAverageThirst(t));
         this.animalAverageEnergyByType.get(t).add(this.mob.getAverageEnergy(t));
      }

      this.animalMale.add(maleTotal);
      this.animalFemale.add(femaleTotal);
      int domesticatedTotal = 0;

      for (MobBase.MobType t : this.animalDomesticatedThisPeriod.keySet()) {
         this.animalDomesticatedByType.get(t).add(this.animalDomesticatedThisPeriod.get(t));
         domesticatedTotal += this.animalDomesticatedThisPeriod.get(t);
         this.animalDomesticatedThisPeriod.put(t, 0);
      }

      this.animalDomesticated.add(domesticatedTotal);
      int deathsTotal = 0;

      for (MobBase.MobType t : this.animalDeathsThisPeriod.keySet()) {
         this.animalDeathsByType.get(t).add(this.animalDeathsThisPeriod.get(t));
         deathsTotal += this.animalDeathsThisPeriod.get(t);
         this.animalDeathsThisPeriod.put(t, 0);
      }

      this.animalDeaths.add(deathsTotal);
   }

   private void processCorruptionData() {
      ArrayList<OrderedPair> corruptionTiles = this.corruption.getCorruptionTilesArray();
      if (this.corruptionSpreadAmount.size() <= 0) {
         this.corruptionSpreadAmount.add(corruptionTiles.size());
      } else {
         this.corruptionSpreadAmount.add(corruptionTiles.size() - this.corruptionTotal.get(this.corruptionTotal.size() - 1));
      }

      this.corruptionTotal.add(corruptionTiles.size());
      this.corruptedBuildings.add(this.object.getObjectsByGroup(MobBase.MobGroup.MONSTER).size());
      this.dronesTotal.add(this.mob.getMobsByType(MobBase.MobType.DRONE, false).size());
      int spawnsTotal = 0;

      for (MobBase.MobType t : this.monstersSpawnedThisPeriod.keySet()) {
         this.monstersSpawnedByType.get(t).add(this.monstersSpawnedThisPeriod.get(t));
         spawnsTotal += this.monstersSpawnedThisPeriod.get(t);
         this.monstersSpawnedThisPeriod.put(t, 0);
      }

      this.monstersSpawned.add(spawnsTotal);
      int killsTotal = 0;

      for (MobBase.MobType t : this.monstersKilledThisPeriod.keySet()) {
         this.monstersKilledByType.get(t).add(this.monstersKilledThisPeriod.get(t));
         killsTotal += this.monstersKilledThisPeriod.get(t);
         this.monstersKilledThisPeriod.put(t, 0);
      }

      this.monstersKilled.add(killsTotal);
   }

   private void processBuildingData() {
      this.buildingsTotal.add(this.object.getActiveBuildings());
      this.buildingSlots.add(this.object.getMaximumBuildings());
      this.buildingsBuilt.add(this.buildingsBuiltThisPeriod);
      this.buildingsBuiltThisPeriod = 0;
      this.buildingsUpgraded.add(this.buildingsUpgradedThisPeriod);
      this.buildingsUpgradedThisPeriod = 0;
      this.buildingsDestroyed.add(this.buildingsDestroyedThisPeriod);
      this.buildingsDestroyedThisPeriod = 0;
      this.buildingsDismantled.add(this.buildingsDismantledThisPeriod);
      this.buildingsDismantledThisPeriod = 0;
   }

   private void processDefensesData() {
      int wallCount = 0;
      int towerCount = 0;
      int combobulatorsCount = 0;

      for (ObjectBase o : this.object.getObjectMasterBuildingPriorityList()) {
         if (o.getObjectFlags().getBaseType().getTileSetTemplate().getType() == MapTilesLoader.TileSetType.WALL) {
            wallCount++;
         }

         if (o.getTower() != null) {
            towerCount++;
         }

         if (o.getObjectFlags().isCombobulator()) {
            combobulatorsCount++;
         }
      }

      this.wallsTotal.add(wallCount);
      this.towersTotal.add(towerCount);
      this.combobulatorsTotal.add(combobulatorsCount);
      ArrayList<MobBase> golems = this.mob.getMobsByGroup(MobBase.MobGroup.GOLEM, false);
      this.golemTotal.add(golems.size());

      for (MobBase.MobType t : this.golemTotalByType.keySet()) {
         ArrayList<MobBase> mobs = this.mob.getMobsByType(t, false);
         this.golemTotalByType.get(t).add(mobs.size());
      }

      ArrayList<MobBase> rangers = this.mob.getMobsByJob(MobJobBase.MobJobType.RANGER, false);
      this.rangerTotal.add(rangers.size());

      for (MobBase.MobType t : this.rangerTotalByType.keySet()) {
         int totalThisType = 0;

         for (MobBase m : rangers) {
            if (m.getMobType() == t) {
               totalThisType++;
            }
         }

         this.rangerTotalByType.get(t).add(totalThisType);
      }
   }

   private void processResourcesData() {
      int totalResources = 0;

      ResourceModule.ResourceType[] var5;
      for (ResourceModule.ResourceType t : ResourceModule.ResourceType.values()) {
         int thisTotal = this.resource.getResourceTotal(t, true);
         if (this.resourcesProductionRateByType.get(t).size() <= 0) {
            this.resourcesProductionRateByType.get(t).add(thisTotal);
         } else {
            this.resourcesProductionRateByType.get(t).add(thisTotal - this.resourcesTotalByType.get(t).get(this.resourcesTotalByType.get(t).size() - 1));
         }
      }

      for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
         int thisTotal = this.resource.getResourceTotal(tx, true);
         totalResources += thisTotal;
         this.resourcesTotalByType.get(tx).add(thisTotal);
      }

      if (this.resourcesProductionRate.size() <= 0) {
         this.resourcesProductionRate.add(totalResources);
      } else {
         this.resourcesProductionRate.add(totalResources - this.resourcesTotal.get(this.resourcesTotal.size() - 1));
      }

      this.resourcesTotal.add(totalResources);
      int totalFood = 0;
      int totalWater = 0;

      ResourceModule.ResourceType[] var7;
      for (ResourceModule.ResourceType tx : ResourceModule.ResourceType.values()) {
         if (tx.getTemplate().getWaterPerUse() > 0) {
            totalWater += this.resourcesProductionRateByType.get(tx).get(this.resourcesProductionRateByType.get(tx).size() - 1);
         }

         if (tx.getTemplate().getFoodPerUse() > 0) {
            totalFood += this.resourcesProductionRateByType.get(tx).get(this.resourcesProductionRateByType.get(tx).size() - 1);
         }
      }

      this.waterProductionRate.add(totalWater);
      this.foodProductionRate.add(totalFood);
   }

   private void processMagicAndEnergyData() {
      this.essenceGenerated.add(this.essenceGeneratedThisPeriod);
      this.essenceGeneratedThisPeriod = 0;
      this.essenceDecayed.add(this.essenceDecayedThisPeriod);
      this.essenceDecayedThisPeriod = 0;
      this.energyAmount.add(this.resource.getTotalAvailableEnergy());
      this.energyMaximum.add(this.resource.getMaximumAvailableEnergy());
      this.influenceSpent.add(this.influenceSpentThisPeriod);
      this.influenceSpentThisPeriod = 0;
      this.influenceGenerated.add(this.influenceGeneratedThisPeriod);
      this.influenceGeneratedThisPeriod = 0;
   }
}
