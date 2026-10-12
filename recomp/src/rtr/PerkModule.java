package rtr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;

public class PerkModule extends ModuleBase {
   private EnumMap<PerkModule.Perk, Boolean> allUnlockedPerks;
   private EnumMap<PerkModule.PerkType, Double> perkTypeTotal;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.allUnlockedPerks = new EnumMap<>(PerkModule.Perk.class);
      this.perkTypeTotal = new EnumMap<>(PerkModule.PerkType.class);

      PerkModule.Perk[] var6;
      for (PerkModule.Perk p : PerkModule.Perk.values()) {
         this.allUnlockedPerks.put(p, false);
      }

      for (PerkModule.PerkType t : PerkModule.PerkType.values()) {
         this.perkTypeTotal.put(t, 0.0);
      }

      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();

      PerkModule.Perk[] var4;
      for (PerkModule.Perk p : PerkModule.Perk.values()) {
         this.allUnlockedPerks.put(p, false);
      }

      for (PerkModule.PerkType t : PerkModule.PerkType.values()) {
         this.perkTypeTotal.put(t, 0.0);
      }
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
   }

   @Override
   protected void loadPlay() throws SlickException {
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.WORLD_MAP && !this.save.getActiveWorldSave().isNewGame()) {
         this.loadWorldSaveData();
      }
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
      this.updateAllPerks();
   }

   @Override
   protected void loadWorldMap() throws SlickException {
      this.loadWorldSaveData();
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
      YMLDataMap dataMap = this.save.getActiveWorldSave().getModuleSaveData(this.moduleType);

      PerkModule.Perk[] var5;
      for (PerkModule.Perk p : PerkModule.Perk.values()) {
         this.allUnlockedPerks.put(p, Boolean.parseBoolean(dataMap.getOrDefault(p.toString(), false)));
      }

      this.updateAllPerks();
   }

   @Override
   public YMLDataMap getWorldMapSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());

      for (PerkModule.Perk p : this.allUnlockedPerks.keySet()) {
         dataMap.put(p.toString(), this.allUnlockedPerks.get(p));
      }

      return dataMap;
   }

   public void updateAllPerks() {
      PerkModule.PerkType[] var4;
      for (PerkModule.PerkType t : PerkModule.PerkType.values()) {
         this.perkTypeTotal.put(t, 0.0);
         double total = 0.0;

         for (PerkModule.Perk p : this.allUnlockedPerks.keySet()) {
            if (this.allUnlockedPerks.get(p) && p.getPerkType() == t) {
               total += p.getValue();
            }
         }

         this.perkTypeTotal.put(t, total);
      }
   }

   public boolean hasPerk(PerkModule.Perk p) {
      return this.allUnlockedPerks.get(p);
   }

   public PerkModule.Perk generateNewRandomPerk() {
      ArrayList<PerkModule.Perk> availablePerks = new ArrayList<>();

      PerkModule.Perk[] var5;
      for (PerkModule.Perk p : PerkModule.Perk.values()) {
         if (!this.hasPerk(p)) {
            availablePerks.add(p);
         }
      }

      if (availablePerks.size() > 0) {
         Collections.shuffle(availablePerks);
         return availablePerks.get(0);
      } else {
         return null;
      }
   }

   public void addSinglePerk(PerkModule.Perk p) {
      this.allUnlockedPerks.put(p, true);
      this.updateAllPerks();
   }

   public double getPerkTypeTotal(PerkModule.PerkType t) {
      return this.perkTypeTotal.get(t);
   }

   public ArrayList<PerkModule.Perk> generateAndGiveNewRandomPerks(int perksAmount) {
      ArrayList<PerkModule.Perk> newPerks = new ArrayList<>();

      for (int x = 0; x < perksAmount; x++) {
         PerkModule.Perk p = this.generateNewRandomPerk();
         if (p != null) {
            newPerks.add(p);
            this.allUnlockedPerks.put(p, true);
         }
      }

      this.updateAllPerks();
      return newPerks;
   }

   public EnumMap<PerkModule.Perk, Boolean> getAllUnlockedPerks() {
      return this.allUnlockedPerks;
   }

   public static enum Perk {
      MAXIMUM_HEALTH_1(PerkModule.PerkType.MAXIMUM_HEALTH, Text.getText("perk.farellesConstitution"), 0.05),
      MAXIMUM_HEALTH_2(PerkModule.PerkType.MAXIMUM_HEALTH, Text.getText("perk.kevorkianBloom"), 0.05),
      MAXIMUM_HEALTH_3(PerkModule.PerkType.MAXIMUM_HEALTH, Text.getText("perk.stensForm"), 0.05),
      MAXIMUM_HEALTH_4(PerkModule.PerkType.MAXIMUM_HEALTH, Text.getText("perk.hardinessofMergen"), 0.1),
      MAXIMUM_HEALTH_5(PerkModule.PerkType.MAXIMUM_HEALTH, Text.getText("perk.vitalityofFecht"), 0.1),
      HEALTH_RECOVERY_SPEED_1(PerkModule.PerkType.HEALTH_RECOVERY_SPEED, Text.getText("perk.shohunsRecovery"), 0.05),
      HEALTH_RECOVERY_SPEED_2(PerkModule.PerkType.HEALTH_RECOVERY_SPEED, Text.getText("perk.kanesathsResumption"), 0.05),
      HEALTH_RECOVERY_SPEED_3(PerkModule.PerkType.HEALTH_RECOVERY_SPEED, Text.getText("perk.sulansRehabilitation"), 0.05),
      HEALTH_RECOVERY_SPEED_4(PerkModule.PerkType.HEALTH_RECOVERY_SPEED, Text.getText("perk.lunnithunsReformation"), 0.1),
      HEALTH_RECOVERY_SPEED_5(PerkModule.PerkType.HEALTH_RECOVERY_SPEED, Text.getText("perk.sythumsRestoration"), 0.1),
      MAXIMUM_HUNGER_1(PerkModule.PerkType.MAXIMUM_HUNGER, Text.getText("perk.cravingsofJokur"), 0.05),
      MAXIMUM_HUNGER_2(PerkModule.PerkType.MAXIMUM_HUNGER, Text.getText("perk.karachosLonging"), 0.05),
      MAXIMUM_HUNGER_3(PerkModule.PerkType.MAXIMUM_HUNGER, Text.getText("perk.namsansGluttony"), 0.05),
      MAXIMUM_HUNGER_4(PerkModule.PerkType.MAXIMUM_HUNGER, Text.getText("perk.dixonpuffsCraving"), 0.1),
      MAXIMUM_HUNGER_5(PerkModule.PerkType.MAXIMUM_HUNGER, Text.getText("perk.riyasAppetite"), 0.1),
      HUNGER_DECAY_RATE_1(PerkModule.PerkType.HUNGER_DECAY_RATE, Text.getText("perk.sagewingsGill"), 0.05),
      HUNGER_DECAY_RATE_2(PerkModule.PerkType.HUNGER_DECAY_RATE, Text.getText("perk.stonestandsGlut"), 0.05),
      HUNGER_DECAY_RATE_3(PerkModule.PerkType.HUNGER_DECAY_RATE, Text.getText("perk.brisksongsWholeness"), 0.05),
      HUNGER_DECAY_RATE_4(PerkModule.PerkType.HUNGER_DECAY_RATE, Text.getText("perk.ositansPlenty"), 0.1),
      HUNGER_DECAY_RATE_5(PerkModule.PerkType.HUNGER_DECAY_RATE, Text.getText("perk.mullarmsAmpleness"), 0.1),
      MAXIMUM_THIRST_1(PerkModule.PerkType.MAXIMUM_THIRST, Text.getText("perk.alamarsThirst"), 0.05),
      MAXIMUM_THIRST_2(PerkModule.PerkType.MAXIMUM_THIRST, Text.getText("perk.tearsofRaevyr"), 0.05),
      MAXIMUM_THIRST_3(PerkModule.PerkType.MAXIMUM_THIRST, Text.getText("perk.jaycesonsSucculences"), 0.05),
      MAXIMUM_THIRST_4(PerkModule.PerkType.MAXIMUM_THIRST, Text.getText("perk.napthasSplashing"), 0.1),
      MAXIMUM_THIRST_5(PerkModule.PerkType.MAXIMUM_THIRST, Text.getText("perk.dobmmonsWetness"), 0.1),
      THIRST_DECAY_RATE_1(PerkModule.PerkType.THIRST_DECAY_RATE, Text.getText("perk.gavtarlsSurplus"), 0.05),
      THIRST_DECAY_RATE_2(PerkModule.PerkType.THIRST_DECAY_RATE, Text.getText("perk.thranuraksSaliva"), 0.05),
      THIRST_DECAY_RATE_3(PerkModule.PerkType.THIRST_DECAY_RATE, Text.getText("perk.gerardsLibation"), 0.05),
      THIRST_DECAY_RATE_4(PerkModule.PerkType.THIRST_DECAY_RATE, Text.getText("perk.beasantsPreservative"), 0.1),
      THIRST_DECAY_RATE_5(PerkModule.PerkType.THIRST_DECAY_RATE, Text.getText("perk.burtonsBroth"), 0.1),
      MAXIMUM_FAITH_1(PerkModule.PerkType.MAXIMUM_FAITH, Text.getText("perk.cloftsMightySplendor"), 0.05),
      MAXIMUM_FAITH_2(PerkModule.PerkType.MAXIMUM_FAITH, Text.getText("perk.zoreksFilteredEssence"), 0.05),
      MAXIMUM_FAITH_3(PerkModule.PerkType.MAXIMUM_FAITH, Text.getText("perk.terrysTabernac"), 0.05),
      MAXIMUM_FAITH_4(PerkModule.PerkType.MAXIMUM_FAITH, Text.getText("perk.dransDoubtfulBelief"), 0.1),
      MAXIMUM_FAITH_5(PerkModule.PerkType.MAXIMUM_FAITH, Text.getText("perk.raysBookOfLies"), 0.1),
      FAITH_DECAY_RATE_1(PerkModule.PerkType.FAITH_DECAY_RATE, Text.getText("perk.daniasDivineLongevity"), 0.05),
      FAITH_DECAY_RATE_2(PerkModule.PerkType.FAITH_DECAY_RATE, Text.getText("perk.pintOfDoubt"), 0.05),
      FAITH_DECAY_RATE_3(PerkModule.PerkType.FAITH_DECAY_RATE, Text.getText("perk.raysEndlessLists"), 0.05),
      FAITH_DECAY_RATE_4(PerkModule.PerkType.FAITH_DECAY_RATE, Text.getText("perk.saintsAmbition"), 0.1),
      FAITH_DECAY_RATE_5(PerkModule.PerkType.FAITH_DECAY_RATE, Text.getText("perk.temptationsTransience"), 0.1),
      MAXIMUM_ENERGY_1(PerkModule.PerkType.MAXIMUM_ENERGY, Text.getText("perk.enduranceofXandal"), 0.05),
      MAXIMUM_ENERGY_2(PerkModule.PerkType.MAXIMUM_ENERGY, Text.getText("perk.ilirithsActivity"), 0.05),
      MAXIMUM_ENERGY_3(PerkModule.PerkType.MAXIMUM_ENERGY, Text.getText("perk.shiraRestlessness"), 0.05),
      MAXIMUM_ENERGY_4(PerkModule.PerkType.MAXIMUM_ENERGY, Text.getText("perk.dashofFabian"), 0.1),
      MAXIMUM_ENERGY_5(PerkModule.PerkType.MAXIMUM_ENERGY, Text.getText("perk.namerusExertion"), 0.1),
      ENERGY_DECAY_RATE_1(PerkModule.PerkType.ENERGY_DECAY_RATE, Text.getText("perk.dittmarsLiveliness"), 0.05),
      ENERGY_DECAY_RATE_2(PerkModule.PerkType.ENERGY_DECAY_RATE, Text.getText("perk.ridleysEnterprise"), 0.05),
      ENERGY_DECAY_RATE_3(PerkModule.PerkType.ENERGY_DECAY_RATE, Text.getText("perk.anselsEnergy"), 0.05),
      ENERGY_DECAY_RATE_4(PerkModule.PerkType.ENERGY_DECAY_RATE, Text.getText("perk.appletonsFlurry"), 0.1),
      ENERGY_DECAY_RATE_5(PerkModule.PerkType.ENERGY_DECAY_RATE, Text.getText("perk.andreasAlertness"), 0.1),
      MAXIMUM_SOCIAL_1(PerkModule.PerkType.MAXIMUM_SOCIAL, Text.getText("perk.sinestrasIntellect"), 0.05),
      MAXIMUM_SOCIAL_2(PerkModule.PerkType.MAXIMUM_SOCIAL, Text.getText("perk.cordialofHansard"), 0.05),
      MAXIMUM_SOCIAL_3(PerkModule.PerkType.MAXIMUM_SOCIAL, Text.getText("perk.narsFriendliness"), 0.05),
      MAXIMUM_SOCIAL_4(PerkModule.PerkType.MAXIMUM_SOCIAL, Text.getText("perk.morladsSociablility"), 0.1),
      MAXIMUM_SOCIAL_5(PerkModule.PerkType.MAXIMUM_SOCIAL, Text.getText("perk.tiberiumkylesWisdom"), 0.1),
      LIFE_EXPECTANCY_1(PerkModule.PerkType.LIFE_EXPECTANCY, Text.getText("perk.musisLilly"), 0.05),
      LIFE_EXPECTANCY_2(PerkModule.PerkType.LIFE_EXPECTANCY, Text.getText("perk.hachimasLifeblood"), 0.05),
      LIFE_EXPECTANCY_3(PerkModule.PerkType.LIFE_EXPECTANCY, Text.getText("perk.dundarsDetermination"), 0.05),
      LIFE_EXPECTANCY_4(PerkModule.PerkType.LIFE_EXPECTANCY, Text.getText("perk.soteriasGift"), 0.05),
      LIFE_EXPECTANCY_5(PerkModule.PerkType.LIFE_EXPECTANCY, Text.getText("perk.dudasMortality"), 0.1),
      MOVEMENT_SPEED_1(PerkModule.PerkType.MOVEMENT_SPEED, Text.getText("perk.speedofTheOterions"), 0.01),
      MOVEMENT_SPEED_2(PerkModule.PerkType.MOVEMENT_SPEED, Text.getText("perk.hasteofGriseus"), 0.02),
      MOVEMENT_SPEED_3(PerkModule.PerkType.MOVEMENT_SPEED, Text.getText("perk.nonimadsVelocity"), 0.03),
      MOVEMENT_SPEED_4(PerkModule.PerkType.MOVEMENT_SPEED, Text.getText("perk.enicarsAgility"), 0.04),
      MOVEMENT_SPEED_5(PerkModule.PerkType.MOVEMENT_SPEED, Text.getText("perk.owensPace"), 0.05),
      DAMAGE_RESISTANCE_1(PerkModule.PerkType.DAMAGE_RESISTANCE, Text.getText("perk.lillysProtection"), 0.05),
      DAMAGE_RESISTANCE_2(PerkModule.PerkType.DAMAGE_RESISTANCE, Text.getText("perk.giosToughness"), 0.05),
      DAMAGE_RESISTANCE_3(PerkModule.PerkType.DAMAGE_RESISTANCE, Text.getText("perk.skinofPriamus"), 0.05),
      DAMAGE_RESISTANCE_4(PerkModule.PerkType.DAMAGE_RESISTANCE, Text.getText("perk.rivaresWard"), 0.1),
      DAMAGE_RESISTANCE_5(PerkModule.PerkType.DAMAGE_RESISTANCE, Text.getText("perk.mushusBarrier"), 0.1),
      MELEE_DAMAGE_1(PerkModule.PerkType.MELEE_DAMAGE, Text.getText("perk.bargsSkirmish"), 0.05),
      MELEE_DAMAGE_2(PerkModule.PerkType.MELEE_DAMAGE, Text.getText("perk.filiconsStrike"), 0.05),
      MELEE_DAMAGE_3(PerkModule.PerkType.MELEE_DAMAGE, Text.getText("perk.samansAggression"), 0.05),
      MELEE_DAMAGE_4(PerkModule.PerkType.MELEE_DAMAGE, Text.getText("perk.shansealaRage"), 0.1),
      MELEE_DAMAGE_5(PerkModule.PerkType.MELEE_DAMAGE, Text.getText("perk.magmelsFall"), 0.1),
      RANGED_DAMAGE_1(PerkModule.PerkType.RANGED_DAMAGE, Text.getText("perk.darlonsDispatch"), 0.05),
      RANGED_DAMAGE_2(PerkModule.PerkType.RANGED_DAMAGE, Text.getText("perk.exonsStrike"), 0.05),
      RANGED_DAMAGE_3(PerkModule.PerkType.RANGED_DAMAGE, Text.getText("perk.skalvernsCharge"), 0.05),
      RANGED_DAMAGE_4(PerkModule.PerkType.RANGED_DAMAGE, Text.getText("perk.norominsWrath"), 0.1),
      RANGED_DAMAGE_5(PerkModule.PerkType.RANGED_DAMAGE, Text.getText("perk.flamesofBivattus"), 0.1),
      MAXIMUM_CARRIED_RESOURCES_1(PerkModule.PerkType.MAXIMUM_CARRIED_RESOURCES, Text.getText("perk.daggarothsLift"), 1.0),
      MAXIMUM_CARRIED_RESOURCES_2(PerkModule.PerkType.MAXIMUM_CARRIED_RESOURCES, Text.getText("perk.ginjasArms"), 1.0),
      MAXIMUM_CARRIED_RESOURCES_3(PerkModule.PerkType.MAXIMUM_CARRIED_RESOURCES, Text.getText("perk.zerosGravitas"), 1.0),
      FERTILITY_1(PerkModule.PerkType.FERTILITY, Text.getText("perk.bibikisStamina"), 0.05),
      FERTILITY_2(PerkModule.PerkType.FERTILITY, Text.getText("perk.clerothsLustiness"), 0.05),
      FERTILITY_3(PerkModule.PerkType.FERTILITY, Text.getText("perk.embraceofSpritus"), 0.05),
      FERTILITY_4(PerkModule.PerkType.FERTILITY, Text.getText("perk.snobosSatisfaction"), 0.1),
      FERTILITY_5(PerkModule.PerkType.FERTILITY, Text.getText("perk.rainsFlower"), 0.1),
      GHOST_APPEARANCE_CHANCE_1(PerkModule.PerkType.GHOST_APPEARANCE_CHANCE, Text.getText("perk.sylmansCurse"), 0.1),
      GHOST_APPEARANCE_CHANCE_2(PerkModule.PerkType.GHOST_APPEARANCE_CHANCE, Text.getText("perk.bustersFearlessness"), 0.1),
      GHOST_APPEARANCE_CHANCE_3(PerkModule.PerkType.GHOST_APPEARANCE_CHANCE, Text.getText("perk.chaucersDespair"), 0.1),
      GHOST_APPEARANCE_CHANCE_4(PerkModule.PerkType.GHOST_APPEARANCE_CHANCE, Text.getText("perk.zuulsPosession"), 0.1),
      GHOST_APPEARANCE_CHANCE_5(PerkModule.PerkType.GHOST_APPEARANCE_CHANCE, Text.getText("perk.hadesDeal"), 0.1),
      VILLAGER_COLD_RESISTANCE_1(PerkModule.PerkType.VILLAGER_COLD_RESISTANCE, Text.getText("perk.bawnsFire"), 1.0),
      VILLAGER_COLD_RESISTANCE_2(PerkModule.PerkType.VILLAGER_COLD_RESISTANCE, Text.getText("perk.roachsFortitude"), 1.0),
      VILLAGER_COLD_RESISTANCE_3(PerkModule.PerkType.VILLAGER_COLD_RESISTANCE, Text.getText("perk.ornnsHearth"), 1.0),
      VILLAGER_COLD_RESISTANCE_4(PerkModule.PerkType.VILLAGER_COLD_RESISTANCE, Text.getText("perk.yuukisBlood"), 1.0),
      VILLAGER_COLD_RESISTANCE_5(PerkModule.PerkType.VILLAGER_COLD_RESISTANCE, Text.getText("perk.albertanAdvantage"), 1.0),
      VILLAGER_HEAT_RESISTANCE_1(PerkModule.PerkType.VILLAGER_HEAT_RESISTANCE, Text.getText("perk.eirsConditioning"), 1.0),
      VILLAGER_HEAT_RESISTANCE_2(PerkModule.PerkType.VILLAGER_HEAT_RESISTANCE, Text.getText("perk.dragonsHeart"), 1.0),
      VILLAGER_HEAT_RESISTANCE_3(PerkModule.PerkType.VILLAGER_HEAT_RESISTANCE, Text.getText("perk.darwinsLaw"), 1.0),
      VILLAGER_HEAT_RESISTANCE_4(PerkModule.PerkType.VILLAGER_HEAT_RESISTANCE, Text.getText("perk.yomisBody"), 1.0),
      VILLAGER_HEAT_RESISTANCE_5(PerkModule.PerkType.VILLAGER_HEAT_RESISTANCE, Text.getText("perk.demideusSkin"), 1.0),
      VILLAGER_LIFESPANS_1(PerkModule.PerkType.VILLAGER_LIFESPANS, Text.getText("perk.jornkersHealthcare"), 0.05),
      VILLAGER_LIFESPANS_2(PerkModule.PerkType.VILLAGER_LIFESPANS, Text.getText("perk.golkarasFountain"), 0.05),
      VILLAGER_LIFESPANS_3(PerkModule.PerkType.VILLAGER_LIFESPANS, Text.getText("perk.elishasProvidence"), 0.05),
      VILLAGER_LIFESPANS_4(PerkModule.PerkType.VILLAGER_LIFESPANS, Text.getText("perk.poncesPotion"), 0.05),
      VILLAGER_LIFESPANS_5(PerkModule.PerkType.VILLAGER_LIFESPANS, Text.getText("perk.drMcCoysScotch"), 0.05),
      WISER_ELDERS_1(PerkModule.PerkType.WISER_ELDERS, Text.getText("perk.dadsJoke"), 0.1),
      WISER_ELDERS_2(PerkModule.PerkType.WISER_ELDERS, Text.getText("perk.timsTestimony"), 0.1),
      WISER_ELDERS_3(PerkModule.PerkType.WISER_ELDERS, Text.getText("perk.jornkersBrain"), 0.1),
      WISER_ELDERS_4(PerkModule.PerkType.WISER_ELDERS, Text.getText("perk.bismarksShame"), 0.1),
      WISER_ELDERS_5(PerkModule.PerkType.WISER_ELDERS, Text.getText("perk.jornkersSarcasm"), 0.1),
      IMPROVED_STORAGE_1(PerkModule.PerkType.IMPROVED_STORAGE, Text.getText("perk.eldsResourcefulness"), 0.05),
      IMPROVED_STORAGE_2(PerkModule.PerkType.IMPROVED_STORAGE, Text.getText("perk.ciphersCataloguing"), 0.05),
      IMPROVED_STORAGE_3(PerkModule.PerkType.IMPROVED_STORAGE, Text.getText("perk.svarogsArrangement"), 0.05),
      IMPROVED_STORAGE_4(PerkModule.PerkType.IMPROVED_STORAGE, Text.getText("perk.voxsStacking"), 0.05),
      IMPROVED_STORAGE_5(PerkModule.PerkType.IMPROVED_STORAGE, Text.getText("perk.serakersArranging"), 0.05),
      EFFICIENT_HOUSING_1(PerkModule.PerkType.EFFICIENT_HOUSING, Text.getText("perk.vargsHabitation"), 1.0),
      EFFICIENT_HOUSING_2(PerkModule.PerkType.EFFICIENT_HOUSING, Text.getText("perk.shelterofCingularis"), 1.0),
      EFFICIENT_HOUSING_3(PerkModule.PerkType.EFFICIENT_HOUSING, Text.getText("perk.koshkasQuarterage"), 1.0),
      EFFICIENT_HOUSING_4(PerkModule.PerkType.EFFICIENT_HOUSING, Text.getText("perk.xentiransHospitality"), 1.0),
      EFFICIENT_HOUSING_5(PerkModule.PerkType.EFFICIENT_HOUSING, Text.getText("perk.alendrusAccomodations"), 1.0),
      EFFICIENT_CONSTRUCTION_1(PerkModule.PerkType.EFFICIENT_CONSTRUCTION, Text.getText("perk.fusrohsIntensity"), 0.03),
      EFFICIENT_CONSTRUCTION_2(PerkModule.PerkType.EFFICIENT_CONSTRUCTION, Text.getText("perk.powerofCeris"), 0.03),
      EFFICIENT_CONSTRUCTION_3(PerkModule.PerkType.EFFICIENT_CONSTRUCTION, Text.getText("perk.handinessofZacharias"), 0.03),
      EFFICIENT_CONSTRUCTION_4(PerkModule.PerkType.EFFICIENT_CONSTRUCTION, Text.getText("perk.aarkisEfficaciousness"), 0.03),
      EFFICIENT_CONSTRUCTION_5(PerkModule.PerkType.EFFICIENT_CONSTRUCTION, Text.getText("perk.torlightUsefulness"), 0.03),
      FARM_GROWTH_SPEED_1(PerkModule.PerkType.FARM_GROWTH_SPEED, Text.getText("perk.leonhardsProliferation"), 0.05),
      FARM_GROWTH_SPEED_2(PerkModule.PerkType.FARM_GROWTH_SPEED, Text.getText("perk.brutaniansSprouting"), 0.05),
      FARM_GROWTH_SPEED_3(PerkModule.PerkType.FARM_GROWTH_SPEED, Text.getText("perk.growthoftheEverbloom"), 0.05),
      FARM_GROWTH_SPEED_4(PerkModule.PerkType.FARM_GROWTH_SPEED, Text.getText("perk.lorrainesGrace"), 0.05),
      FARM_GROWTH_SPEED_5(PerkModule.PerkType.FARM_GROWTH_SPEED, Text.getText("perk.tranyasProduce"), 0.05),
      GLOBAL_WORK_SPEED_1(PerkModule.PerkType.GLOBAL_WORK_SPEED, Text.getText("perk.eulersAlacrity"), 0.02),
      GLOBAL_WORK_SPEED_2(PerkModule.PerkType.GLOBAL_WORK_SPEED, Text.getText("perk.bormacsHeadway"), 0.02),
      GLOBAL_WORK_SPEED_3(PerkModule.PerkType.GLOBAL_WORK_SPEED, Text.getText("perk.wrismokesHustle"), 0.03),
      GLOBAL_WORK_SPEED_4(PerkModule.PerkType.GLOBAL_WORK_SPEED, Text.getText("perk.raphimasBreeze"), 0.04),
      GLOBAL_WORK_SPEED_5(PerkModule.PerkType.GLOBAL_WORK_SPEED, Text.getText("perk.shrogersBriskness"), 0.04),
      HARVESTING_SPEED_1(PerkModule.PerkType.HARVESTING_SPEED, Text.getText("perk.rivarynsZeal"), 0.02),
      HARVESTING_SPEED_2(PerkModule.PerkType.HARVESTING_SPEED, Text.getText("perk.borasRush"), 0.02),
      HARVESTING_SPEED_3(PerkModule.PerkType.HARVESTING_SPEED, Text.getText("perk.returnofSilentis"), 0.03),
      HARVESTING_SPEED_4(PerkModule.PerkType.HARVESTING_SPEED, Text.getText("perk.ellongsYield"), 0.04),
      HARVESTING_SPEED_5(PerkModule.PerkType.HARVESTING_SPEED, Text.getText("perk.reyekorasReaping"), 0.04),
      REFINING_SPEED_1(PerkModule.PerkType.REFINING_SPEED, Text.getText("perk.shogunsPassion"), 0.02),
      REFINING_SPEED_2(PerkModule.PerkType.REFINING_SPEED, Text.getText("perk.blacksIntensity"), 0.02),
      REFINING_SPEED_3(PerkModule.PerkType.REFINING_SPEED, Text.getText("perk.solairesForging"), 0.03),
      REFINING_SPEED_4(PerkModule.PerkType.REFINING_SPEED, Text.getText("perk.raindonsMolding"), 0.04),
      REFINING_SPEED_5(PerkModule.PerkType.REFINING_SPEED, Text.getText("perk.webbshinesTooling"), 0.04),
      BUILDING_SPEED_1(PerkModule.PerkType.BUILDING_SPEED, Text.getText("perk.ariannasEagerness"), 0.02),
      BUILDING_SPEED_2(PerkModule.PerkType.BUILDING_SPEED, Text.getText("perk.wuggysErection"), 0.02),
      BUILDING_SPEED_3(PerkModule.PerkType.BUILDING_SPEED, Text.getText("perk.xaerysArchitecture"), 0.03),
      BUILDING_SPEED_4(PerkModule.PerkType.BUILDING_SPEED, Text.getText("perk.visersterSkill"), 0.04),
      BUILDING_SPEED_5(PerkModule.PerkType.BUILDING_SPEED, Text.getText("perk.stewarbowCraftsmanship"), 0.04),
      ROAD_CONSTRUCTION_SPEED_1(PerkModule.PerkType.ROAD_CONSTRUCTION_SPEED, Text.getText("perk.lascerandisFabrication"), 0.02),
      ROAD_CONSTRUCTION_SPEED_2(PerkModule.PerkType.ROAD_CONSTRUCTION_SPEED, Text.getText("perk.shapingofSayres"), 0.02),
      ROAD_CONSTRUCTION_SPEED_3(PerkModule.PerkType.ROAD_CONSTRUCTION_SPEED, Text.getText("perk.thollsJoining"), 0.03),
      ROAD_CONSTRUCTION_SPEED_4(PerkModule.PerkType.ROAD_CONSTRUCTION_SPEED, Text.getText("perk.lollikersEngineering"), 0.04),
      ROAD_CONSTRUCTION_SPEED_5(PerkModule.PerkType.ROAD_CONSTRUCTION_SPEED, Text.getText("perk.caclopsApex"), 0.04),
      DISMANTLING_SPEED_1(PerkModule.PerkType.DISMANTLING_SPEED, Text.getText("perk.lyteniansDismembering"), 0.02),
      DISMANTLING_SPEED_2(PerkModule.PerkType.DISMANTLING_SPEED, Text.getText("perk.araxielsWrack"), 0.02),
      DISMANTLING_SPEED_3(PerkModule.PerkType.DISMANTLING_SPEED, Text.getText("perk.dobgibsonsRuin"), 0.03),
      DISMANTLING_SPEED_4(PerkModule.PerkType.DISMANTLING_SPEED, Text.getText("perk.hawksSwiftness"), 0.04),
      DISMANTLING_SPEED_5(PerkModule.PerkType.DISMANTLING_SPEED, Text.getText("perk.jamellsMania"), 0.04),
      CLEARING_SPEED_1(PerkModule.PerkType.CLEARING_SPEED, Text.getText("perk.amaraesUrgency"), 0.02),
      CLEARING_SPEED_2(PerkModule.PerkType.CLEARING_SPEED, Text.getText("perk.anonlolisExpanse"), 0.02),
      CLEARING_SPEED_3(PerkModule.PerkType.CLEARING_SPEED, Text.getText("perk.levelingofDracoreyes"), 0.03),
      CLEARING_SPEED_4(PerkModule.PerkType.CLEARING_SPEED, Text.getText("perk.glittersmisHeadroom"), 0.04),
      CLEARING_SPEED_5(PerkModule.PerkType.CLEARING_SPEED, Text.getText("perk.tuxiaClearcutting"), 0.04),
      REPAIRING_SPEED_1(PerkModule.PerkType.REPAIRING_SPEED, Text.getText("perk.aeonsPatching"), 0.02),
      REPAIRING_SPEED_2(PerkModule.PerkType.REPAIRING_SPEED, Text.getText("perk.andreisReconstruction"), 0.02),
      REPAIRING_SPEED_3(PerkModule.PerkType.REPAIRING_SPEED, Text.getText("perk.jimelightsReformation"), 0.03),
      REPAIRING_SPEED_4(PerkModule.PerkType.REPAIRING_SPEED, Text.getText("perk.wookorasMending"), 0.04),
      REPAIRING_SPEED_5(PerkModule.PerkType.REPAIRING_SPEED, Text.getText("perk.silvergarsOverhaul"), 0.04),
      MAINTAINING_ROAD_SPEED_1(PerkModule.PerkType.MAINTAINING_ROAD_SPEED, Text.getText("perk.fuguesRectification"), 0.02),
      MAINTAINING_ROAD_SPEED_2(PerkModule.PerkType.MAINTAINING_ROAD_SPEED, Text.getText("perk.anastrianasGrind"), 0.02),
      MAINTAINING_ROAD_SPEED_3(PerkModule.PerkType.MAINTAINING_ROAD_SPEED, Text.getText("perk.droghoffmansService"), 0.03),
      MAINTAINING_ROAD_SPEED_4(PerkModule.PerkType.MAINTAINING_ROAD_SPEED, Text.getText("perk.spiritofValiya"), 0.04),
      MAINTAINING_ROAD_SPEED_5(PerkModule.PerkType.MAINTAINING_ROAD_SPEED, Text.getText("perk.thollsReform"), 0.04),
      FARMING_SPEED_1(PerkModule.PerkType.FARMING_SPEED, Text.getText("perk.kiplingsHoeing"), 0.02),
      FARMING_SPEED_2(PerkModule.PerkType.FARMING_SPEED, Text.getText("perk.alvinsAgriculture"), 0.02),
      FARMING_SPEED_3(PerkModule.PerkType.FARMING_SPEED, Text.getText("perk.woogsCultivation"), 0.03),
      FARMING_SPEED_4(PerkModule.PerkType.FARMING_SPEED, Text.getText("perk.tranyasAgronomy"), 0.04),
      FARMING_SPEED_5(PerkModule.PerkType.FARMING_SPEED, Text.getText("perk.rhaelmartinsSeeding"), 0.04),
      MEDIC_WORK_SPEED_1(PerkModule.PerkType.MEDIC_WORK_SPEED, Text.getText("perk.leviathansCuring"), 0.02),
      MEDIC_WORK_SPEED_2(PerkModule.PerkType.MEDIC_WORK_SPEED, Text.getText("perk.alathasInvigoration"), 0.02),
      MEDIC_WORK_SPEED_3(PerkModule.PerkType.MEDIC_WORK_SPEED, Text.getText("perk.cralightsPlacebo"), 0.03),
      MEDIC_WORK_SPEED_4(PerkModule.PerkType.MEDIC_WORK_SPEED, Text.getText("perk.bakerolasRecovery"), 0.04),
      MEDIC_WORK_SPEED_5(PerkModule.PerkType.MEDIC_WORK_SPEED, Text.getText("perk.millsellsReparation"), 0.04),
      PRAYING_SPEED_1(PerkModule.PerkType.PRAYING_SPEED, Text.getText("perk.elanofsCharisma"), 0.02),
      PRAYING_SPEED_2(PerkModule.PerkType.PRAYING_SPEED, Text.getText("perk.fredwardsFervor"), 0.02),
      PRAYING_SPEED_3(PerkModule.PerkType.PRAYING_SPEED, Text.getText("perk.yulasZeal"), 0.03),
      PRAYING_SPEED_4(PerkModule.PerkType.PRAYING_SPEED, Text.getText("perk.raymondsReign"), 0.04),
      PRAYING_SPEED_5(PerkModule.PerkType.PRAYING_SPEED, Text.getText("perk.annasAlacrity"), 0.04),
      CAPTURING_SOULS_SPEED_1(PerkModule.PerkType.CAPTURING_SOULS_SPEED, Text.getText("perk.demisesLoss"), 0.02),
      CAPTURING_SOULS_SPEED_2(PerkModule.PerkType.CAPTURING_SOULS_SPEED, Text.getText("perk.elhaelsWisdom"), 0.02),
      CAPTURING_SOULS_SPEED_3(PerkModule.PerkType.CAPTURING_SOULS_SPEED, Text.getText("perk.hayfathersHumanity"), 0.03),
      CAPTURING_SOULS_SPEED_4(PerkModule.PerkType.CAPTURING_SOULS_SPEED, Text.getText("perk.slimersCage"), 0.04),
      CAPTURING_SOULS_SPEED_5(PerkModule.PerkType.CAPTURING_SOULS_SPEED, Text.getText("perk.rayParkerJrsStyle"), 0.04),
      TEND_ANIMALS_SPEED_1(PerkModule.PerkType.TEND_ANIMALS_SPEED, Text.getText("perk.galanahsWhisper"), 0.02),
      TEND_ANIMALS_SPEED_2(PerkModule.PerkType.TEND_ANIMALS_SPEED, Text.getText("perk.dransFoodPreparation"), 0.02),
      TEND_ANIMALS_SPEED_3(PerkModule.PerkType.TEND_ANIMALS_SPEED, Text.getText("perk.civilisationsGuidance"), 0.03),
      TEND_ANIMALS_SPEED_4(PerkModule.PerkType.TEND_ANIMALS_SPEED, Text.getText("perk.hayfathersGuidance"), 0.04),
      TEND_ANIMALS_SPEED_5(PerkModule.PerkType.TEND_ANIMALS_SPEED, Text.getText("perk.irwinsKhakis"), 0.04),
      WATER_STORAGE_1(PerkModule.PerkType.WATER_STORAGE, Text.getText("perk.sagarsBottle"), 0.02),
      WATER_STORAGE_2(PerkModule.PerkType.WATER_STORAGE, Text.getText("perk.destonsCanteen"), 0.02),
      WATER_STORAGE_3(PerkModule.PerkType.WATER_STORAGE, Text.getText("perk.ronjasCapsule"), 0.03),
      WATER_STORAGE_4(PerkModule.PerkType.WATER_STORAGE, Text.getText("perk.venettasTank"), 0.04),
      WATER_STORAGE_5(PerkModule.PerkType.WATER_STORAGE, Text.getText("perk.damiasTub"), 0.04),
      WOOD_GOLEM_LEVEL_1(PerkModule.PerkType.WOOD_GOLEM_LEVEL, Text.getText("perk.oliviasDevelopment"), 1.0),
      WOOD_GOLEM_LEVEL_2(PerkModule.PerkType.WOOD_GOLEM_LEVEL, Text.getText("perk.rivasEscalation"), 1.0),
      WOOD_GOLEM_LEVEL_3(PerkModule.PerkType.WOOD_GOLEM_LEVEL, Text.getText("perk.alyshasUpgrade"), 1.0),
      WOOD_GOLEM_CAPACITY_1(PerkModule.PerkType.WOOD_GOLEM_CAPACITY, Text.getText("perk.yseultsInflation"), 1.0),
      WOOD_GOLEM_CAPACITY_2(PerkModule.PerkType.WOOD_GOLEM_CAPACITY, Text.getText("perk.tyndallsExaggeration"), 1.0),
      WOOD_GOLEM_CAPACITY_3(PerkModule.PerkType.WOOD_GOLEM_CAPACITY, Text.getText("perk.tiffaniesSpread"), 1.0),
      WOOD_GOLEM_CHARGE_TIME_1(PerkModule.PerkType.WOOD_GOLEM_CHARGE_TIME, Text.getText("perk.shelleysUpsurge"), 0.1),
      WOOD_GOLEM_CHARGE_TIME_2(PerkModule.PerkType.WOOD_GOLEM_CHARGE_TIME, Text.getText("perk.lickashsOptimization"), 0.1),
      WOOD_GOLEM_CHARGE_TIME_3(PerkModule.PerkType.WOOD_GOLEM_CHARGE_TIME, Text.getText("perk.krunluksFlow"), 0.1),
      WOOD_GOLEM_CHARGE_TIME_4(PerkModule.PerkType.WOOD_GOLEM_CHARGE_TIME, Text.getText("perk.glithasStimulation"), 0.1),
      WOOD_GOLEM_CHARGE_TIME_5(PerkModule.PerkType.WOOD_GOLEM_CHARGE_TIME, Text.getText("perk.bellowflowsInvigoration"), 0.1),
      STONE_GOLEM_LEVEL_1(PerkModule.PerkType.STONE_GOLEM_LEVEL, Text.getText("perk.finespansBoost"), 1.0),
      STONE_GOLEM_LEVEL_2(PerkModule.PerkType.STONE_GOLEM_LEVEL, Text.getText("perk.wigglespellsAccrual"), 1.0),
      STONE_GOLEM_LEVEL_3(PerkModule.PerkType.STONE_GOLEM_LEVEL, Text.getText("perk.bunameenksWaxing"), 1.0),
      STONE_GOLEM_CAPACITY_1(PerkModule.PerkType.STONE_GOLEM_CAPACITY, Text.getText("perk.ublathozzsBuild-up"), 1.0),
      STONE_GOLEM_CAPACITY_2(PerkModule.PerkType.STONE_GOLEM_CAPACITY, Text.getText("perk.thylinklazsAddition"), 1.0),
      STONE_GOLEM_CAPACITY_3(PerkModule.PerkType.STONE_GOLEM_CAPACITY, Text.getText("perk.klerosNumbers"), 1.0),
      STONE_GOLEM_CHARGE_TIME_1(PerkModule.PerkType.STONE_GOLEM_CHARGE_TIME, Text.getText("perk.noldicksAmassing"), 0.1),
      STONE_GOLEM_CHARGE_TIME_2(PerkModule.PerkType.STONE_GOLEM_CHARGE_TIME, Text.getText("perk.fabiansInfusion"), 0.1),
      STONE_GOLEM_CHARGE_TIME_3(PerkModule.PerkType.STONE_GOLEM_CHARGE_TIME, Text.getText("perk.bemarinsBuild"), 0.1),
      STONE_GOLEM_CHARGE_TIME_4(PerkModule.PerkType.STONE_GOLEM_CHARGE_TIME, Text.getText("perk.inemeonsEnergizing"), 0.1),
      STONE_GOLEM_CHARGE_TIME_5(PerkModule.PerkType.STONE_GOLEM_CHARGE_TIME, Text.getText("perk.jovimaviasAccumulation"), 0.1),
      CRYSTAL_GOLEM_LEVEL_1(PerkModule.PerkType.CRYSTAL_GOLEM_LEVEL, Text.getText("perk.zanliasGain"), 1.0),
      CRYSTAL_GOLEM_LEVEL_2(PerkModule.PerkType.CRYSTAL_GOLEM_LEVEL, Text.getText("perk.hodisAugmentation"), 1.0),
      CRYSTAL_GOLEM_LEVEL_3(PerkModule.PerkType.CRYSTAL_GOLEM_LEVEL, Text.getText("perk.balthisEnlargement"), 1.0),
      CRYSTAL_GOLEM_CAPACITY_1(PerkModule.PerkType.CRYSTAL_GOLEM_CAPACITY, Text.getText("perk.jeremiahsEnflanginator"), 1.0),
      CRYSTAL_GOLEM_CAPACITY_2(PerkModule.PerkType.CRYSTAL_GOLEM_CAPACITY, Text.getText("perk.drengundsExtension"), 1.0),
      CRYSTAL_GOLEM_CAPACITY_3(PerkModule.PerkType.CRYSTAL_GOLEM_CAPACITY, Text.getText("perk.bhuldemsSupplements"), 1.0),
      CRYSTAL_GOLEM_CHARGE_TIME_1(PerkModule.PerkType.CRYSTAL_GOLEM_CHARGE_TIME, Text.getText("perk.egusSurge"), 0.1),
      CRYSTAL_GOLEM_CHARGE_TIME_2(PerkModule.PerkType.CRYSTAL_GOLEM_CHARGE_TIME, Text.getText("perk.uglasCharge"), 0.1),
      CRYSTAL_GOLEM_CHARGE_TIME_3(PerkModule.PerkType.CRYSTAL_GOLEM_CHARGE_TIME, Text.getText("perk.omarsExciter"), 0.1),
      CRYSTAL_GOLEM_CHARGE_TIME_4(PerkModule.PerkType.CRYSTAL_GOLEM_CHARGE_TIME, Text.getText("perk.rowansVitalization"), 0.1),
      CRYSTAL_GOLEM_CHARGE_TIME_5(PerkModule.PerkType.CRYSTAL_GOLEM_CHARGE_TIME, Text.getText("perk.ramseysAccumulation"), 0.1),
      BUILDING_SLOTS_1(PerkModule.PerkType.BUILDING_SLOTS, Text.getText("perk.mynesCraft"), 1.0),
      BUILDING_SLOTS_2(PerkModule.PerkType.BUILDING_SLOTS, Text.getText("perk.rosBlocks"), 2.0),
      BUILDING_SLOTS_3(PerkModule.PerkType.BUILDING_SLOTS, Text.getText("perk.dwarvenIngenuity"), 3.0),
      BUILDING_SLOTS_4(PerkModule.PerkType.BUILDING_SLOTS, Text.getText("perk.greellasCapacity"), 4.0),
      BUILDING_SLOTS_5(PerkModule.PerkType.BUILDING_SLOTS, Text.getText("perk.harmvisTerritory"), 5.0),
      ANCILLARY_SLOTS_1(PerkModule.PerkType.ANCILLARY_SLOTS, Text.getText("perk.areasArea"), 1.0),
      ANCILLARY_SLOTS_2(PerkModule.PerkType.ANCILLARY_SLOTS, Text.getText("perk.alexeysStacking"), 1.0),
      ANCILLARY_SLOTS_3(PerkModule.PerkType.ANCILLARY_SLOTS, Text.getText("perk.dwarvenTools"), 1.0),
      ANCILLARY_SLOTS_4(PerkModule.PerkType.ANCILLARY_SLOTS, Text.getText("perk.brainsrasVolume"), 1.0),
      ANCILLARY_SLOTS_5(PerkModule.PerkType.ANCILLARY_SLOTS, Text.getText("perk.hermibinsonsTurf"), 1.0),
      MARKET_RESTOCK_AMOUNT_1(PerkModule.PerkType.MARKET_RESTOCK_AMOUNT, Text.getText("perk.midasHand"), 0.1),
      MARKET_RESTOCK_AMOUNT_2(PerkModule.PerkType.MARKET_RESTOCK_AMOUNT, Text.getText("perk.robinsAvarice"), 0.1),
      MARKET_RESTOCK_AMOUNT_3(PerkModule.PerkType.MARKET_RESTOCK_AMOUNT, Text.getText("perk.fredgarsResplendence"), 0.1),
      MARKET_RESTOCK_AMOUNT_4(PerkModule.PerkType.MARKET_RESTOCK_AMOUNT, Text.getText("perk.benedictsBenefactor"), 0.1),
      MARKET_RESTOCK_AMOUNT_5(PerkModule.PerkType.MARKET_RESTOCK_AMOUNT, Text.getText("perk.wendrdansAbundance"), 0.1),
      MARKET_CATJEET_CAPACITY_1(PerkModule.PerkType.MARKET_CATJEET_CAPACITY, Text.getText("perk.adrotagiasDiligence"), 1.0),
      MARKET_CATJEET_CAPACITY_2(PerkModule.PerkType.MARKET_CATJEET_CAPACITY, Text.getText("perk.jujisBrush"), 1.0),
      MARKET_CATJEET_CAPACITY_3(PerkModule.PerkType.MARKET_CATJEET_CAPACITY, Text.getText("perk.nativsHospitality"), 1.0),
      MARKET_CATJEET_CAPACITY_4(PerkModule.PerkType.MARKET_CATJEET_CAPACITY, Text.getText("perk.dibblesWares"), 1.0),
      MARKET_CATJEET_CAPACITY_5(PerkModule.PerkType.MARKET_CATJEET_CAPACITY, Text.getText("perk.maiqsSkooma"), 1.0),
      ESSENCE_COLLECTOR_CAPACITY_1(PerkModule.PerkType.ESSENCE_COLLECTOR_CAPACITY, Text.getText("perk.shaysBlessing"), 0.05),
      ESSENCE_COLLECTOR_CAPACITY_2(PerkModule.PerkType.ESSENCE_COLLECTOR_CAPACITY, Text.getText("perk.hirotsQuota"), 0.05),
      ESSENCE_COLLECTOR_CAPACITY_3(PerkModule.PerkType.ESSENCE_COLLECTOR_CAPACITY, Text.getText("perk.gonzalelasVolume"), 0.05),
      ESSENCE_COLLECTOR_CAPACITY_4(PerkModule.PerkType.ESSENCE_COLLECTOR_CAPACITY, Text.getText("perk.coobiellasQuantity"), 0.05),
      ESSENCE_COLLECTOR_CAPACITY_5(PerkModule.PerkType.ESSENCE_COLLECTOR_CAPACITY, Text.getText("perk.rosepopsLoad"), 0.05),
      EFFICIENT_TRASH_BURNING_1(PerkModule.PerkType.EFFICIENT_TRASH_BURNING, Text.getText("perk.dantesInferno"), 0.1),
      EFFICIENT_TRASH_BURNING_2(PerkModule.PerkType.EFFICIENT_TRASH_BURNING, Text.getText("perk.dumpstersFire"), 0.1),
      EFFICIENT_TRASH_BURNING_3(PerkModule.PerkType.EFFICIENT_TRASH_BURNING, Text.getText("perk.elonsBlaze"), 0.1),
      EFFICIENT_TRASH_BURNING_4(PerkModule.PerkType.EFFICIENT_TRASH_BURNING, Text.getText("perk.smrettsChar"), 0.1),
      EFFICIENT_TRASH_BURNING_5(PerkModule.PerkType.EFFICIENT_TRASH_BURNING, Text.getText("perk.fogansKindle"), 0.1),
      BUILDING_HEALTH_1(PerkModule.PerkType.BUILDING_HEALTH, Text.getText("perk.guardiansProtection"), 0.05),
      BUILDING_HEALTH_2(PerkModule.PerkType.BUILDING_HEALTH, Text.getText("perk.athomirsBulwark"), 0.05),
      BUILDING_HEALTH_3(PerkModule.PerkType.BUILDING_HEALTH, Text.getText("perk.titansBlessing"), 0.05),
      BUILDING_HEALTH_4(PerkModule.PerkType.BUILDING_HEALTH, Text.getText("perk.scottysHammer"), 0.05),
      BUILDING_HEALTH_5(PerkModule.PerkType.BUILDING_HEALTH, Text.getText("perk.dwarvenResilience"), 0.05),
      INCREASED_CULLIS_GATE_STABILITY_1(PerkModule.PerkType.INCREASED_CULLIS_GATE_STABILITY, Text.getText("perk.crowsBindings"), 0.05),
      INCREASED_CULLIS_GATE_STABILITY_2(PerkModule.PerkType.INCREASED_CULLIS_GATE_STABILITY, Text.getText("perk.tiamatsChaos"), 0.05),
      INCREASED_CULLIS_GATE_STABILITY_4(PerkModule.PerkType.INCREASED_CULLIS_GATE_STABILITY, Text.getText("perk.deadnesBalance"), 0.05),
      INCREASED_CULLIS_GATE_STABILITY_5(PerkModule.PerkType.INCREASED_CULLIS_GATE_STABILITY, Text.getText("perk.cheekybursConstancy"), 0.1),
      HOLY_GOLEM_LEVEL_1(PerkModule.PerkType.HOLY_GOLEM_LEVEL, Text.getText("perk.braktarsRighteousness"), 1.0),
      HOLY_GOLEM_LEVEL_2(PerkModule.PerkType.HOLY_GOLEM_LEVEL, Text.getText("perk.glendaysInflation"), 1.0),
      HOLY_GOLEM_LEVEL_3(PerkModule.PerkType.HOLY_GOLEM_LEVEL, Text.getText("perk.serarosAccession"), 1.0),
      LABOR_GOLEM_LEVEL_1(PerkModule.PerkType.LABOR_GOLEM_LEVEL, Text.getText("perk.additionsofAngerris"), 1.0),
      LABOR_GOLEM_LEVEL_2(PerkModule.PerkType.LABOR_GOLEM_LEVEL, Text.getText("perk.agerrasBuildup"), 1.0),
      LABOR_GOLEM_LEVEL_3(PerkModule.PerkType.LABOR_GOLEM_LEVEL, Text.getText("perk.pixrtonsGains"), 1.0),
      HOLY_GOLEM_SPEED_1(PerkModule.PerkType.HOLY_GOLEM_SPEED, Text.getText("perk.bloomsGlorification"), 0.1),
      HOLY_GOLEM_SPEED_2(PerkModule.PerkType.HOLY_GOLEM_SPEED, Text.getText("perk.acurasAcceleration"), 0.1),
      HOLY_GOLEM_SPEED_3(PerkModule.PerkType.HOLY_GOLEM_SPEED, Text.getText("perk.galmisExpansion"), 0.1),
      HOLY_GOLEM_SPEED_4(PerkModule.PerkType.HOLY_GOLEM_SPEED, Text.getText("perk.lolliwoodsSurge"), 0.1),
      HOLY_GOLEM_SPEED_5(PerkModule.PerkType.HOLY_GOLEM_SPEED, Text.getText("perk.drogverasUpgrade"), 0.1),
      LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES_1(PerkModule.PerkType.LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES, Text.getText("perk.solairesAugmentation"), 1.0),
      LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES_2(PerkModule.PerkType.LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES, Text.getText("perk.wendusContinuance"), 1.0),
      LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES_3(PerkModule.PerkType.LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES, Text.getText("perk.raindonsPerpetuation"), 1.0),
      LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES_4(PerkModule.PerkType.LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES, Text.getText("perk.gibsonshinesStretch"), 1.0),
      LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES_5(PerkModule.PerkType.LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES, Text.getText("perk.redsPerpetuation"), 1.0),
      MORE_MINIMUM_INFLUENCE_1(PerkModule.PerkType.MORE_MINIMUM_INFLUENCE, Text.getText("perk.mittensIndignation"), 50.0),
      MORE_MINIMUM_INFLUENCE_2(PerkModule.PerkType.MORE_MINIMUM_INFLUENCE, Text.getText("perk.gregorysSlapdash"), 50.0),
      MORE_MINIMUM_INFLUENCE_3(PerkModule.PerkType.MORE_MINIMUM_INFLUENCE, Text.getText("perk.selmasProvidence"), 50.0),
      MORE_MINIMUM_INFLUENCE_4(PerkModule.PerkType.MORE_MINIMUM_INFLUENCE, Text.getText("perk.shawnsShrimp"), 50.0),
      MORE_MINIMUM_INFLUENCE_5(PerkModule.PerkType.MORE_MINIMUM_INFLUENCE, Text.getText("perk.wilmasNatural"), 100.0),
      MORE_INFLUENCE_PER_ANIMAL_1(PerkModule.PerkType.MORE_INFLUENCE_PER_ANIMAL, Text.getText("perk.almasWhimsy"), 1.0),
      MORE_INFLUENCE_PER_ANIMAL_2(PerkModule.PerkType.MORE_INFLUENCE_PER_ANIMAL, Text.getText("perk.henrysHungering"), 1.0),
      MORE_INFLUENCE_PER_ANIMAL_3(PerkModule.PerkType.MORE_INFLUENCE_PER_ANIMAL, Text.getText("perk.selinettesSanctity"), 1.0),
      MORE_INFLUENCE_PER_ANIMAL_4(PerkModule.PerkType.MORE_INFLUENCE_PER_ANIMAL, Text.getText("perk.johnsVolition"), 1.0),
      MORE_INFLUENCE_PER_ANIMAL_5(PerkModule.PerkType.MORE_INFLUENCE_PER_ANIMAL, Text.getText("perk.delilahsDelight"), 1.0),
      MORE_INFLUENCE_PER_VILLAGER_1(PerkModule.PerkType.MORE_INFLUENCE_PER_VILLAGER, Text.getText("perk.patstricksAllurement"), 1.0),
      MORE_INFLUENCE_PER_VILLAGER_2(PerkModule.PerkType.MORE_INFLUENCE_PER_VILLAGER, Text.getText("perk.divinationofAidos"), 1.0),
      MORE_INFLUENCE_PER_VILLAGER_3(PerkModule.PerkType.MORE_INFLUENCE_PER_VILLAGER, Text.getText("perk.aronsAscension"), 1.0),
      MORE_INFLUENCE_PER_VILLAGER_4(PerkModule.PerkType.MORE_INFLUENCE_PER_VILLAGER, Text.getText("perk.hughsGambit"), 1.0),
      MORE_INFLUENCE_PER_VILLAGER_5(PerkModule.PerkType.MORE_INFLUENCE_PER_VILLAGER, Text.getText("perk.vigorofAngerris"), 1.0),
      INCREASED_INFLUENCE_GENERATION_1(PerkModule.PerkType.INCREASED_INFLUENCE_GENERATION, Text.getText("perk.dalinarsTemerity"), 0.1),
      INCREASED_INFLUENCE_GENERATION_2(PerkModule.PerkType.INCREASED_INFLUENCE_GENERATION, Text.getText("perk.theodoresGift"), 0.1),
      INCREASED_INFLUENCE_GENERATION_3(PerkModule.PerkType.INCREASED_INFLUENCE_GENERATION, Text.getText("perk.arlaggsFaith"), 0.1),
      INCREASED_INFLUENCE_GENERATION_4(PerkModule.PerkType.INCREASED_INFLUENCE_GENERATION, Text.getText("perk.saratogasProphecy"), 0.1),
      INCREASED_INFLUENCE_GENERATION_5(PerkModule.PerkType.INCREASED_INFLUENCE_GENERATION, Text.getText("perk.kristathansAcclamation"), 0.1),
      BANISH_SPELL_COST_1(PerkModule.PerkType.BANISH_SPELL_COST, Text.getText("perk.baesRejection"), 0.1),
      BANISH_SPELL_COST_2(PerkModule.PerkType.BANISH_SPELL_COST, Text.getText("perk.napoleonsVigor"), 0.1),
      BANISH_SPELL_COST_3(PerkModule.PerkType.BANISH_SPELL_COST, Text.getText("perk.musksDismiss"), 0.1),
      BANISH_SPELL_COOLDOWN_1(PerkModule.PerkType.BANISH_SPELL_COOLDOWN, Text.getText("perk.kilosFatedBetrayal"), 0.5),
      BANISH_SPELL_COOLDOWN_2(PerkModule.PerkType.BANISH_SPELL_COOLDOWN, Text.getText("perk.deusVult"), 0.5),
      BANISH_SPELL_COOLDOWN_3(PerkModule.PerkType.BANISH_SPELL_COOLDOWN, Text.getText("perk.badbasExile"), 0.5),
      BANISH_SPELL_DURATION_1(PerkModule.PerkType.BANISH_SPELL_DURATION, Text.getText("perk.returnedsFavor"), 0.5),
      BANISH_SPELL_DURATION_2(PerkModule.PerkType.BANISH_SPELL_DURATION, Text.getText("perk.yeetsCannon"), 0.5),
      BANISH_SPELL_DURATION_3(PerkModule.PerkType.BANISH_SPELL_DURATION, Text.getText("perk.jornkersReverse"), 0.5),
      BANISH_SPELL_RADIUS_1(PerkModule.PerkType.BANISH_SPELL_RADIUS, Text.getText("perk.emichosCharisma"), 1.0),
      BANISH_SPELL_RADIUS_2(PerkModule.PerkType.BANISH_SPELL_RADIUS, Text.getText("perk.petersReach"), 1.0),
      BANISH_SPELL_RADIUS_3(PerkModule.PerkType.BANISH_SPELL_RADIUS, Text.getText("perk.waltersVoid"), 2.0),
      CHARM_SPELL_COST_1(PerkModule.PerkType.CHARM_SPELL_COST, Text.getText("perk.lazerusWrappings"), 0.1),
      CHARM_SPELL_COST_2(PerkModule.PerkType.CHARM_SPELL_COST, Text.getText("perk.zoroastersOrder"), 0.1),
      CHARM_SPELL_COST_3(PerkModule.PerkType.CHARM_SPELL_COST, Text.getText("perk.jornkersBidding"), 0.1),
      CHARM_SPELL_COOLDOWN_1(PerkModule.PerkType.CHARM_SPELL_COOLDOWN, Text.getText("perk.aphroditesBeauty"), 1.0),
      CHARM_SPELL_COOLDOWN_2(PerkModule.PerkType.CHARM_SPELL_COOLDOWN, Text.getText("perk.ushasLust"), 2.0),
      CHARM_SPELL_COOLDOWN_3(PerkModule.PerkType.CHARM_SPELL_COOLDOWN, Text.getText("perk.freyjasPerfection"), 2.0),
      COLD_AURA_SPELL_COST_1(PerkModule.PerkType.COLD_AURA_SPELL_COST, Text.getText("perk.demetersWrath"), 0.1),
      COLD_AURA_SPELL_COST_2(PerkModule.PerkType.COLD_AURA_SPELL_COST, Text.getText("perk.amaterusHiding"), 0.1),
      COLD_AURA_SPELL_COST_3(PerkModule.PerkType.COLD_AURA_SPELL_COST, Text.getText("perk.friggasImprisonment"), 0.1),
      COLD_AURA_SPELL_COOLDOWN_1(PerkModule.PerkType.COLD_AURA_SPELL_COOLDOWN, Text.getText("perk.hermesAuxiliary"), 1.0),
      COLD_AURA_SPELL_COOLDOWN_2(PerkModule.PerkType.COLD_AURA_SPELL_COOLDOWN, Text.getText("perk.underworldsMessenger"), 1.0),
      COLD_AURA_SPELL_COOLDOWN_3(PerkModule.PerkType.COLD_AURA_SPELL_COOLDOWN, Text.getText("perk.quicksQuips"), 1.0),
      COLD_AURA_SPELL_DURATION_1(PerkModule.PerkType.COLD_AURA_SPELL_DURATION, Text.getText("perk.foxsPerpetuation"), 2.0),
      COLD_AURA_SPELL_DURATION_2(PerkModule.PerkType.COLD_AURA_SPELL_DURATION, Text.getText("perk.reksContinuity"), 2.0),
      COLD_AURA_SPELL_DURATION_3(PerkModule.PerkType.COLD_AURA_SPELL_DURATION, Text.getText("perk.wallacesContinuation"), 2.0),
      COLD_AURA_SPELL_RADIUS_1(PerkModule.PerkType.COLD_AURA_SPELL_RADIUS, Text.getText("perk.bearsAmbit"), 1.0),
      COLD_AURA_SPELL_RADIUS_2(PerkModule.PerkType.COLD_AURA_SPELL_RADIUS, Text.getText("perk.lyteniansReach"), 1.0),
      COLD_AURA_SPELL_RADIUS_3(PerkModule.PerkType.COLD_AURA_SPELL_RADIUS, Text.getText("perk.moosesSpan"), 2.0),
      COMET_SPELL_COST_1(PerkModule.PerkType.COMET_SPELL_COST, Text.getText("perk.shivasDestructiveImparting"), 0.1),
      COMET_SPELL_COST_2(PerkModule.PerkType.COMET_SPELL_COST, Text.getText("perk.hallysPromptReturn"), 0.1),
      COMET_SPELL_COST_3(PerkModule.PerkType.COMET_SPELL_COST, Text.getText("perk.greenturnersComet"), 0.1),
      CONJURE_MATERIAL_SPELL_COST_1(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_COST, Text.getText("perk.redbloomsExpenditure"), 0.1),
      CONJURE_MATERIAL_SPELL_COST_2(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_COST, Text.getText("perk.rivarynsSqueeze"), 0.1),
      CONJURE_MATERIAL_SPELL_COST_3(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_COST, Text.getText("perk.veintidosValue"), 0.1),
      CONJURE_MATERIAL_SPELL_AMOUNT_1(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_AMOUNT, Text.getText("perk.timsLuck"), 1.0),
      CONJURE_MATERIAL_SPELL_AMOUNT_2(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_AMOUNT, Text.getText("perk.orbansMachinery"), 1.0),
      CONJURE_MATERIAL_SPELL_AMOUNT_3(PerkModule.PerkType.CONJURE_MATERIAL_SPELL_AMOUNT, Text.getText("perk.cammysStuff"), 1.0),
      CONSTRUCT_SPELL_COST_1(PerkModule.PerkType.CONSTRUCT_SPELL_COST, Text.getText("perk.rinsAspiration"), 0.1),
      CONSTRUCT_SPELL_COST_2(PerkModule.PerkType.CONSTRUCT_SPELL_COST, Text.getText("perk.bobsFixing"), 0.1),
      CONSTRUCT_SPELL_COST_3(PerkModule.PerkType.CONSTRUCT_SPELL_COST, Text.getText("perk.corbusiersInventiveness"), 0.1),
      CONSTRUCT_SPELL_COOLDOWN_1(PerkModule.PerkType.CONSTRUCT_SPELL_COOLDOWN, Text.getText("perk.kvothesResolve"), 2.0),
      CONSTRUCT_SPELL_COOLDOWN_2(PerkModule.PerkType.CONSTRUCT_SPELL_COOLDOWN, Text.getText("perk.hermesBlessing"), 2.0),
      CONSTRUCT_SPELL_COOLDOWN_3(PerkModule.PerkType.CONSTRUCT_SPELL_COOLDOWN, Text.getText("perk.gropiusCreativity"), 2.0),
      CONSTRUCT_SPELL_DURATION_1(PerkModule.PerkType.CONSTRUCT_SPELL_DURATION, Text.getText("perk.serenitysAugment"), 2.0),
      CONSTRUCT_SPELL_DURATION_2(PerkModule.PerkType.CONSTRUCT_SPELL_DURATION, Text.getText("perk.wrightsPhilosophy"), 2.0),
      CONSTRUCT_SPELL_DURATION_3(PerkModule.PerkType.CONSTRUCT_SPELL_DURATION, Text.getText("perk.fastidiousFortitude"), 2.0),
      CONSTRUCT_SPELL_RADIUS_1(PerkModule.PerkType.CONSTRUCT_SPELL_RADIUS, Text.getText("perk.sejimasProficiency"), 1.0),
      CONSTRUCT_SPELL_RADIUS_2(PerkModule.PerkType.CONSTRUCT_SPELL_RADIUS, Text.getText("perk.nishizawasExpertise"), 1.0),
      CONSTRUCT_SPELL_RADIUS_3(PerkModule.PerkType.CONSTRUCT_SPELL_RADIUS, Text.getText("perk.chompersArchitecture"), 2.0),
      DISSOLVE_SPELL_COST_1(PerkModule.PerkType.DISSOLVE_SPELL_COST, Text.getText("perk.cinthsVacate"), 0.1),
      DISSOLVE_SPELL_COST_2(PerkModule.PerkType.DISSOLVE_SPELL_COST, Text.getText("perk.joelsSolvent"), 0.1),
      DISSOLVE_SPELL_COST_3(PerkModule.PerkType.DISSOLVE_SPELL_COST, Text.getText("perk.hydrasSolution"), 0.1),
      DISSOLVE_SPELL_COOLDOWN_1(PerkModule.PerkType.DISSOLVE_SPELL_COOLDOWN, Text.getText("perk.melfsConduit"), 0.5),
      DISSOLVE_SPELL_COOLDOWN_2(PerkModule.PerkType.DISSOLVE_SPELL_COOLDOWN, Text.getText("perk.jansFade"), 0.5),
      DISSOLVE_SPELL_COOLDOWN_3(PerkModule.PerkType.DISSOLVE_SPELL_COOLDOWN, Text.getText("perk.semoresCollapse"), 0.5),
      DISSOLVE_SPELL_DURATION_1(PerkModule.PerkType.DISSOLVE_SPELL_DURATION, Text.getText("perk.verminsJudgement"), 0.5),
      DISSOLVE_SPELL_DURATION_2(PerkModule.PerkType.DISSOLVE_SPELL_DURATION, Text.getText("perk.ursasOrbit"), 0.5),
      DISSOLVE_SPELL_DURATION_3(PerkModule.PerkType.DISSOLVE_SPELL_DURATION, Text.getText("perk.vinceysLimit"), 0.5),
      DISSOLVE_SPELL_RADIUS_1(PerkModule.PerkType.DISSOLVE_SPELL_RADIUS, Text.getText("perk.aridesSemidiameter"), 1.0),
      DISSOLVE_SPELL_RADIUS_2(PerkModule.PerkType.DISSOLVE_SPELL_RADIUS, Text.getText("perk.edaisBoundary"), 1.0),
      DISSOLVE_SPELL_RADIUS_3(PerkModule.PerkType.DISSOLVE_SPELL_RADIUS, Text.getText("perk.xaerysReach"), 2.0),
      DIVINE_BLESSING_SPELL_COST_1(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COST, Text.getText("perk.bibliosBalm"), 0.1),
      DIVINE_BLESSING_SPELL_COST_2(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COST, Text.getText("perk.clerothsLotion"), 0.1),
      DIVINE_BLESSING_SPELL_COST_3(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COST, Text.getText("perk.gildadsSalve"), 0.1),
      DIVINE_BLESSING_SPELL_COOLDOWN_1(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COOLDOWN, Text.getText("perk.actiosCerate"), 0.5),
      DIVINE_BLESSING_SPELL_COOLDOWN_2(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COOLDOWN, Text.getText("perk.madarasDressing"), 0.5),
      DIVINE_BLESSING_SPELL_COOLDOWN_3(PerkModule.PerkType.DIVINE_BLESSING_SPELL_COOLDOWN, Text.getText("perk.zimsMedicine"), 1.0),
      EARTHQUAKE_SPELL_COST_1(PerkModule.PerkType.EARTHQUAKE_SPELL_COST, Text.getText("perk.aquinasHubris"), 0.1),
      EARTHQUAKE_SPELL_COST_2(PerkModule.PerkType.EARTHQUAKE_SPELL_COST, Text.getText("perk.ziggysMetal"), 0.1),
      EARTHQUAKE_SPELL_COST_3(PerkModule.PerkType.EARTHQUAKE_SPELL_COST, Text.getText("perk.abomsRubble"), 0.1),
      EARTHQUAKE_SPELL_COOLDOWN_1(PerkModule.PerkType.EARTHQUAKE_SPELL_COOLDOWN, Text.getText("perk.irasRage"), 1.0),
      EARTHQUAKE_SPELL_COOLDOWN_2(PerkModule.PerkType.EARTHQUAKE_SPELL_COOLDOWN, Text.getText("perk.troglodeitysRedemption"), 1.0),
      EARTHQUAKE_SPELL_COOLDOWN_3(PerkModule.PerkType.EARTHQUAKE_SPELL_COOLDOWN, Text.getText("perk.aehansMineral"), 2.0),
      FLAME_SPELL_COST_1(PerkModule.PerkType.FLAME_SPELL_COST, Text.getText("perk.lozmeratesFlame"), 0.2),
      FLAME_SPELL_COST_2(PerkModule.PerkType.FLAME_SPELL_COST, Text.getText("perk.alexandersInferno"), 0.2),
      FLAME_SPELL_COST_3(PerkModule.PerkType.FLAME_SPELL_COST, Text.getText("perk.davesCrust"), 0.2),
      GOD_TOWER_SPELL_COOLDOWN_1(PerkModule.PerkType.GOD_TOWER_SPELL_COOLDOWN, Text.getText("perk.babelsBlessing"), 1.0),
      GOD_TOWER_SPELL_COOLDOWN_2(PerkModule.PerkType.GOD_TOWER_SPELL_COOLDOWN, Text.getText("perk.hephaestusHammer"), 1.0),
      GOD_TOWER_SPELL_COOLDOWN_3(PerkModule.PerkType.GOD_TOWER_SPELL_COOLDOWN, Text.getText("perk.wurzburgsStoutfastness"), 1.0),
      GOD_TOWER_SPELL_COST_1(PerkModule.PerkType.GOD_TOWER_SPELL_COST, Text.getText("perk.zeldasLegend"), 0.1),
      GOD_TOWER_SPELL_COST_2(PerkModule.PerkType.GOD_TOWER_SPELL_COST, Text.getText("perk.docksStronghold"), 0.1),
      GOD_TOWER_SPELL_COST_3(PerkModule.PerkType.GOD_TOWER_SPELL_COST, Text.getText("perk.hydesColumn"), 0.1),
      GOD_WALL_SPELL_COST_1(PerkModule.PerkType.GOD_WALL_SPELL_COST, Text.getText("perk.drewsFortification"), 0.1),
      GOD_WALL_SPELL_COST_2(PerkModule.PerkType.GOD_WALL_SPELL_COST, Text.getText("perk.filiconsPillar"), 0.1),
      GOD_WALL_SPELL_COST_3(PerkModule.PerkType.GOD_WALL_SPELL_COST, Text.getText("perk.gehnsObelisk"), 0.1),
      HARVEST_SPELL_COST_1(PerkModule.PerkType.HARVEST_SPELL_COST, Text.getText("perk.slysGatheringSwarm"), 0.1),
      HARVEST_SPELL_COST_2(PerkModule.PerkType.HARVEST_SPELL_COST, Text.getText("perk.fizzordsAutumn"), 0.1),
      HARVEST_SPELL_COST_3(PerkModule.PerkType.HARVEST_SPELL_COST, Text.getText("perk.gnusMoon"), 0.1),
      HARVEST_SPELL_COOLDOWN_1(PerkModule.PerkType.HARVEST_SPELL_COOLDOWN, Text.getText("perk.jacksLumber"), 1.0),
      HARVEST_SPELL_COOLDOWN_2(PerkModule.PerkType.HARVEST_SPELL_COOLDOWN, Text.getText("perk.hachisGathering"), 2.0),
      HARVEST_SPELL_COOLDOWN_3(PerkModule.PerkType.HARVEST_SPELL_COOLDOWN, Text.getText("perk.joeysSandwich"), 3.0),
      HARVEST_SPELL_DURATION_1(PerkModule.PerkType.HARVEST_SPELL_DURATION, Text.getText("perk.appletonsBounty"), 1.0),
      HARVEST_SPELL_DURATION_2(PerkModule.PerkType.HARVEST_SPELL_DURATION, Text.getText("perk.jonansYield"), 1.0),
      HARVEST_SPELL_DURATION_3(PerkModule.PerkType.HARVEST_SPELL_DURATION, Text.getText("perk.kensOutput"), 1.0),
      HARVEST_SPELL_RADIUS_1(PerkModule.PerkType.HARVEST_SPELL_RADIUS, Text.getText("perk.kinesCrop"), 1.0),
      HARVEST_SPELL_RADIUS_2(PerkModule.PerkType.HARVEST_SPELL_RADIUS, Text.getText("perk.lucasFruit"), 1.0),
      HARVEST_SPELL_RADIUS_3(PerkModule.PerkType.HARVEST_SPELL_RADIUS, Text.getText("perk.lugzansReaping"), 2.0),
      HEALING_AURA_SPELL_COST_1(PerkModule.PerkType.HEALING_AURA_SPELL_COST, Text.getText("perk.alluringAlliteration"), 0.1),
      HEALING_AURA_SPELL_COST_2(PerkModule.PerkType.HEALING_AURA_SPELL_COST, Text.getText("perk.pepesSpice"), 0.1),
      HEALING_AURA_SPELL_COST_3(PerkModule.PerkType.HEALING_AURA_SPELL_COST, Text.getText("perk.kibasSwathe"), 0.1),
      HEALING_AURA_SPELL_COOLDOWN_1(PerkModule.PerkType.HEALING_AURA_SPELL_COOLDOWN, Text.getText("perk.michkasTruss"), 0.5),
      HEALING_AURA_SPELL_COOLDOWN_2(PerkModule.PerkType.HEALING_AURA_SPELL_COOLDOWN, Text.getText("perk.samsBindings"), 0.5),
      HEALING_AURA_SPELL_COOLDOWN_3(PerkModule.PerkType.HEALING_AURA_SPELL_COOLDOWN, Text.getText("perk.skalvernsDressings"), 0.5),
      HEALING_AURA_SPELL_DURATION_1(PerkModule.PerkType.HEALING_AURA_SPELL_DURATION, Text.getText("perk.divinitysReach"), 1.0),
      HEALING_AURA_SPELL_DURATION_2(PerkModule.PerkType.HEALING_AURA_SPELL_DURATION, Text.getText("perk.hakimodosWrap"), 1.0),
      HEALING_AURA_SPELL_DURATION_3(PerkModule.PerkType.HEALING_AURA_SPELL_DURATION, Text.getText("perk.quacksOil"), 2.0),
      HEALING_AURA_SPELL_RADIUS_1(PerkModule.PerkType.HEALING_AURA_SPELL_RADIUS, Text.getText("perk.pyritesPharmaceuticals"), 1.0),
      HEALING_AURA_SPELL_RADIUS_2(PerkModule.PerkType.HEALING_AURA_SPELL_RADIUS, Text.getText("perk.samotsRemedy"), 1.0),
      HEALING_AURA_SPELL_RADIUS_3(PerkModule.PerkType.HEALING_AURA_SPELL_RADIUS, Text.getText("perk.snakesOil"), 2.0),
      HOLY_POTATOES_SPELL_COST_1(PerkModule.PerkType.HOLY_POTATOES_SPELL_COST, Text.getText("perk.twentsYam"), 0.1),
      HOLY_POTATOES_SPELL_COST_2(PerkModule.PerkType.HOLY_POTATOES_SPELL_COST, Text.getText("perk.voidsTater"), 0.1),
      HOLY_POTATOES_SPELL_COST_3(PerkModule.PerkType.HOLY_POTATOES_SPELL_COST, Text.getText("perk.mootsSpud"), 0.1),
      HOLY_POTATOES_SPELL_COOLDOWN_1(PerkModule.PerkType.HOLY_POTATOES_SPELL_COOLDOWN, Text.getText("perk.mylonsMischief"), 1.0),
      HOLY_POTATOES_SPELL_COOLDOWN_2(PerkModule.PerkType.HOLY_POTATOES_SPELL_COOLDOWN, Text.getText("perk.wakasPlants"), 1.0),
      HOLY_POTATOES_SPELL_COOLDOWN_3(PerkModule.PerkType.HOLY_POTATOES_SPELL_COOLDOWN, Text.getText("perk.voxsRoots"), 1.0),
      HOLY_POTATOES_SPELL_DURATION_1(PerkModule.PerkType.HOLY_POTATOES_SPELL_DURATION, Text.getText("perk.vitosStems"), 1.0),
      HOLY_POTATOES_SPELL_DURATION_2(PerkModule.PerkType.HOLY_POTATOES_SPELL_DURATION, Text.getText("perk.xylphosStock"), 1.0),
      HOLY_POTATOES_SPELL_DURATION_3(PerkModule.PerkType.HOLY_POTATOES_SPELL_DURATION, Text.getText("perk.zendarsGreens"), 1.0),
      HOLY_POTATOES_SPELL_RADIUS_1(PerkModule.PerkType.HOLY_POTATOES_SPELL_RADIUS, Text.getText("perk.potatoMastersHarvest"), 1.0),
      HOLY_POTATOES_SPELL_RADIUS_2(PerkModule.PerkType.HOLY_POTATOES_SPELL_RADIUS, Text.getText("perk.carrionsGluttony"), 1.0),
      HOLY_POTATOES_SPELL_RADIUS_3(PerkModule.PerkType.HOLY_POTATOES_SPELL_RADIUS, Text.getText("perk.gillaugsSpread"), 2.0),
      HOLY_WOOD_SPELL_COST_1(PerkModule.PerkType.HOLY_WOOD_SPELL_COST, Text.getText("perk.biggusFiccus"), 0.1),
      HOLY_WOOD_SPELL_COST_2(PerkModule.PerkType.HOLY_WOOD_SPELL_COST, Text.getText("perk.wallasRaise"), 0.1),
      HOLY_WOOD_SPELL_COST_3(PerkModule.PerkType.HOLY_WOOD_SPELL_COST, Text.getText("perk.halightsLumber"), 0.1),
      HOLY_WOOD_SPELL_COOLDOWN_1(PerkModule.PerkType.HOLY_WOOD_SPELL_COOLDOWN, Text.getText("perk.luxuriasLust"), 1.0),
      HOLY_WOOD_SPELL_COOLDOWN_2(PerkModule.PerkType.HOLY_WOOD_SPELL_COOLDOWN, Text.getText("perk.werotsCopse"), 1.0),
      HOLY_WOOD_SPELL_COOLDOWN_3(PerkModule.PerkType.HOLY_WOOD_SPELL_COOLDOWN, Text.getText("perk.rottoshersTrees"), 1.0),
      HOLY_WOOD_SPELL_DURATION_1(PerkModule.PerkType.HOLY_WOOD_SPELL_DURATION, Text.getText("perk.nichonsteinsSeeds"), 1.0),
      HOLY_WOOD_SPELL_DURATION_2(PerkModule.PerkType.HOLY_WOOD_SPELL_DURATION, Text.getText("perk.jazogsDuration"), 1.0),
      HOLY_WOOD_SPELL_DURATION_3(PerkModule.PerkType.HOLY_WOOD_SPELL_DURATION, Text.getText("perk.gogmallersStamina"), 1.0),
      HOLY_WOOD_SPELL_RADIUS_1(PerkModule.PerkType.HOLY_WOOD_SPELL_RADIUS, Text.getText("perk.darkLordsFarsight"), 1.0),
      HOLY_WOOD_SPELL_RADIUS_2(PerkModule.PerkType.HOLY_WOOD_SPELL_RADIUS, Text.getText("perk.tendoninsPlayfulness"), 1.0),
      HOLY_WOOD_SPELL_RADIUS_3(PerkModule.PerkType.HOLY_WOOD_SPELL_RADIUS, Text.getText("perk.viserkellysRange"), 2.0),
      ILLUMINATE_SPELL_COST_1(PerkModule.PerkType.ILLUMINATE_SPELL_COST, Text.getText("perk.foxlafsKindle"), 0.1),
      ILLUMINATE_SPELL_COST_2(PerkModule.PerkType.ILLUMINATE_SPELL_COST, Text.getText("perk.lollirdonsShine"), 0.1),
      ILLUMINATE_SPELL_COST_3(PerkModule.PerkType.ILLUMINATE_SPELL_COST, Text.getText("perk.russelamsBeacon"), 0.1),
      ILLUMINATE_SPELL_DURATION_1(PerkModule.PerkType.ILLUMINATE_SPELL_DURATION, Text.getText("perk.hamsCandle"), 5.0),
      ILLUMINATE_SPELL_DURATION_2(PerkModule.PerkType.ILLUMINATE_SPELL_DURATION, Text.getText("perk.badharsDaylight"), 5.0),
      ILLUMINATE_SPELL_DURATION_3(PerkModule.PerkType.ILLUMINATE_SPELL_DURATION, Text.getText("perk.ramosooksAurora"), 5.0),
      ILLUMINATE_SPELL_RADIUS_1(PerkModule.PerkType.ILLUMINATE_SPELL_RADIUS, Text.getText("perk.merlinsAmbience"), 1.0),
      ILLUMINATE_SPELL_RADIUS_2(PerkModule.PerkType.ILLUMINATE_SPELL_RADIUS, Text.getText("perk.picardsLights"), 1.0),
      ILLUMINATE_SPELL_RADIUS_3(PerkModule.PerkType.ILLUMINATE_SPELL_RADIUS, Text.getText("perk.meksFlare"), 2.0),
      LIGHTNING_BOLT_SPELL_COST_1(PerkModule.PerkType.LIGHTNING_BOLT_SPELL_COST, Text.getText("perk.hufootsEffulgence"), 0.1),
      LIGHTNING_BOLT_SPELL_COST_2(PerkModule.PerkType.LIGHTNING_BOLT_SPELL_COST, Text.getText("perk.jonebielsLamp"), 0.1),
      LIGHTNING_BOLT_SPELL_COST_3(PerkModule.PerkType.LIGHTNING_BOLT_SPELL_COST, Text.getText("perk.corisLantern"), 0.1),
      MAGIC_BOLTS_SPELL_COST_1(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COST, Text.getText("perk.parionsWizardry"), 0.1),
      MAGIC_BOLTS_SPELL_COST_2(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COST, Text.getText("perk.wabellsFulmination"), 0.1),
      MAGIC_BOLTS_SPELL_COST_3(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COST, Text.getText("perk.tinkerhamsBolts"), 0.1),
      MAGIC_BOLTS_SPELL_COUNT_1(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COUNT, Text.getText("perk.kiufsJudgement"), 1.0),
      MAGIC_BOLTS_SPELL_COUNT_2(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COUNT, Text.getText("perk.cantripsKnowledge"), 1.0),
      MAGIC_BOLTS_SPELL_COUNT_3(PerkModule.PerkType.MAGIC_BOLTS_SPELL_COUNT, Text.getText("perk.merlinsSoul"), 1.0),
      MEND_SPELL_COST_1(PerkModule.PerkType.MEND_SPELL_COST, Text.getText("perk.clabergsFix"), 0.1),
      MEND_SPELL_COST_2(PerkModule.PerkType.MEND_SPELL_COST, Text.getText("perk.smwardsRepairs"), 0.1),
      MEND_SPELL_COST_3(PerkModule.PerkType.MEND_SPELL_COST, Text.getText("perk.babimsMaintenance"), 0.1),
      MEND_SPELL_DURATION_1(PerkModule.PerkType.MEND_SPELL_DURATION, Text.getText("perk.dlaracrudsReforgement"), 1.0),
      MEND_SPELL_DURATION_2(PerkModule.PerkType.MEND_SPELL_DURATION, Text.getText("perk.malafiasMachine"), 1.0),
      MEND_SPELL_DURATION_3(PerkModule.PerkType.MEND_SPELL_DURATION, Text.getText("perk.wiyasDevice"), 1.0),
      MEND_SPELL_RADIUS_1(PerkModule.PerkType.MEND_SPELL_RADIUS, Text.getText("perk.hendersContraption"), 1.0),
      MEND_SPELL_RADIUS_2(PerkModule.PerkType.MEND_SPELL_RADIUS, Text.getText("perk.reelasTools"), 1.0),
      MEND_SPELL_RADIUS_3(PerkModule.PerkType.MEND_SPELL_RADIUS, Text.getText("perk.williaritasWidget"), 2.0),
      METEOR_SPELL_COST_1(PerkModule.PerkType.METEOR_SPELL_COST, Text.getText("perk.titachusSteel"), 0.1),
      METEOR_SPELL_COST_2(PerkModule.PerkType.METEOR_SPELL_COST, Text.getText("perk.rishusShootingstar"), 0.1),
      METEOR_SPELL_COST_3(PerkModule.PerkType.METEOR_SPELL_COST, Text.getText("perk.dwaynesReturn"), 0.1),
      METEOR_SPELL_AMOUNT_1(PerkModule.PerkType.METEOR_SPELL_AMOUNT, Text.getText("perk.grabtharsHammer"), 1.0),
      METEOR_SPELL_AMOUNT_2(PerkModule.PerkType.METEOR_SPELL_AMOUNT, Text.getText("perk.roadmansRedemption"), 1.0),
      METEOR_SPELL_AMOUNT_3(PerkModule.PerkType.METEOR_SPELL_AMOUNT, Text.getText("perk.royasDestruction"), 1.0),
      MOTIVATE_LAND_SPELL_COST_1(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COST, Text.getText("perk.dotoriasMotivation"), 0.1),
      MOTIVATE_LAND_SPELL_COST_2(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COST, Text.getText("perk.primsEncouragement"), 0.1),
      MOTIVATE_LAND_SPELL_COST_3(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COST, Text.getText("perk.kingsCultivation"), 0.1),
      MOTIVATE_LAND_SPELL_COOLDOWN_1(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COOLDOWN, Text.getText("perk.rileyinsExcitement"), 0.5),
      MOTIVATE_LAND_SPELL_COOLDOWN_2(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COOLDOWN, Text.getText("perk.firesuttonsSway"), 0.5),
      MOTIVATE_LAND_SPELL_COOLDOWN_3(PerkModule.PerkType.MOTIVATE_LAND_SPELL_COOLDOWN, Text.getText("perk.caropheliasGrowth"), 0.5),
      MOTIVATE_LAND_SPELL_DURATION_1(PerkModule.PerkType.MOTIVATE_LAND_SPELL_DURATION, Text.getText("perk.carphirasSpread"), 1.0),
      MOTIVATE_LAND_SPELL_DURATION_2(PerkModule.PerkType.MOTIVATE_LAND_SPELL_DURATION, Text.getText("perk.jordeliasPullulation"), 1.0),
      MOTIVATE_LAND_SPELL_DURATION_3(PerkModule.PerkType.MOTIVATE_LAND_SPELL_DURATION, Text.getText("perk.surcollinsGains"), 1.0),
      MOTIVATE_LAND_SPELL_RADIUS_1(PerkModule.PerkType.MOTIVATE_LAND_SPELL_RADIUS, Text.getText("perk.naturesEvolution"), 1.0),
      MOTIVATE_LAND_SPELL_RADIUS_2(PerkModule.PerkType.MOTIVATE_LAND_SPELL_RADIUS, Text.getText("perk.lofangsGermination"), 1.0),
      MOTIVATE_LAND_SPELL_RADIUS_3(PerkModule.PerkType.MOTIVATE_LAND_SPELL_RADIUS, Text.getText("perk.kokafordsLength"), 2.0),
      RECALL_SPELL_COST_1(PerkModule.PerkType.RECALL_SPELL_COST, Text.getText("perk.frasersTeleportation"), 0.1),
      RECALL_SPELL_COST_2(PerkModule.PerkType.RECALL_SPELL_COST, Text.getText("perk.lollissellsBeam"), 0.1),
      RECALL_SPELL_COST_3(PerkModule.PerkType.RECALL_SPELL_COST, Text.getText("perk.killipsTransmission"), 0.1),
      RECALL_SPELL_COOLDOWN_1(PerkModule.PerkType.RECALL_SPELL_COOLDOWN, Text.getText("perk.hunazzosSummon"), 0.5),
      RECALL_SPELL_COOLDOWN_2(PerkModule.PerkType.RECALL_SPELL_COOLDOWN, Text.getText("perk.claricalsCall"), 0.5),
      RECALL_SPELL_COOLDOWN_3(PerkModule.PerkType.RECALL_SPELL_COOLDOWN, Text.getText("perk.rogersAssemble"), 0.5),
      RECALL_SPELL_DURATION_1(PerkModule.PerkType.RECALL_SPELL_DURATION, Text.getText("perk.gongantasRepository"), 0.5),
      RECALL_SPELL_DURATION_2(PerkModule.PerkType.RECALL_SPELL_DURATION, Text.getText("perk.seramarsDepot"), 0.5),
      RECALL_SPELL_DURATION_3(PerkModule.PerkType.RECALL_SPELL_DURATION, Text.getText("perk.danierysCache"), 1.0),
      RECALL_SPELL_RADIUS_1(PerkModule.PerkType.RECALL_SPELL_RADIUS, Text.getText("perk.echinasForce"), 1.0),
      RECALL_SPELL_RADIUS_2(PerkModule.PerkType.RECALL_SPELL_RADIUS, Text.getText("perk.teulmatisSay"), 1.0),
      RECALL_SPELL_RADIUS_3(PerkModule.PerkType.RECALL_SPELL_RADIUS, Text.getText("perk.hermesBoots"), 2.0),
      REGENERATION_SPELL_COST_1(PerkModule.PerkType.REGENERATION_SPELL_COST, Text.getText("perk.helraysReclamation"), 0.1),
      REGENERATION_SPELL_COST_2(PerkModule.PerkType.REGENERATION_SPELL_COST, Text.getText("perk.nuamesMetamorphosis"), 0.1),
      REGENERATION_SPELL_COST_3(PerkModule.PerkType.REGENERATION_SPELL_COST, Text.getText("perk.ramogsReversal"), 0.1),
      REGENERATION_SPELL_DURATION_1(PerkModule.PerkType.REGENERATION_SPELL_DURATION, Text.getText("perk.lightsongsBoldness"), 1.0),
      REGENERATION_SPELL_DURATION_2(PerkModule.PerkType.REGENERATION_SPELL_DURATION, Text.getText("perk.mirasReflection"), 2.0),
      REGENERATION_SPELL_DURATION_3(PerkModule.PerkType.REGENERATION_SPELL_DURATION, Text.getText("perk.dakedesBlessing"), 3.0),
      RESSURECT_SPELL_COST_1(PerkModule.PerkType.RESSURECT_SPELL_COST, Text.getText("perk.eiwasDoppelganger"), 0.1),
      RESSURECT_SPELL_COST_2(PerkModule.PerkType.RESSURECT_SPELL_COST, Text.getText("perk.linuxsIngenuity"), 0.1),
      RESSURECT_SPELL_COST_3(PerkModule.PerkType.RESSURECT_SPELL_COST, Text.getText("perk.morazztsRebirth"), 0.1),
      RESSURECT_SPELL_COOLDOWN_1(PerkModule.PerkType.RESSURECT_SPELL_COOLDOWN, Text.getText("perk.gurwalsTrick"), 0.5),
      RESSURECT_SPELL_COOLDOWN_2(PerkModule.PerkType.RESSURECT_SPELL_COOLDOWN, Text.getText("perk.dezraysDefiance"), 0.5),
      RESSURECT_SPELL_COOLDOWN_3(PerkModule.PerkType.RESSURECT_SPELL_COOLDOWN, Text.getText("perk.bellasWitchery"), 0.5),
      RESURRECT_SPELL_SUCCESS_CHANCE_1(PerkModule.PerkType.RESURRECT_SPELL_SUCCESS_CHANCE, Text.getText("perk.dovinsBan"), 0.2),
      RESURRECT_SPELL_SUCCESS_CHANCE_2(PerkModule.PerkType.RESURRECT_SPELL_SUCCESS_CHANCE, Text.getText("perk.xanthidussTutorship"), 0.2),
      RESURRECT_SPELL_SUCCESS_CHANCE_3(PerkModule.PerkType.RESURRECT_SPELL_SUCCESS_CHANCE, Text.getText("perk.jedediahsPrayers"), 0.2),
      STORM_SPELL_COST_1(PerkModule.PerkType.STORM_SPELL_COST, Text.getText("perk.yennefersGeralt"), 0.1),
      STORM_SPELL_COST_2(PerkModule.PerkType.STORM_SPELL_COST, Text.getText("perk.aktzinsGrace"), 0.1),
      STORM_SPELL_COST_3(PerkModule.PerkType.STORM_SPELL_COST, Text.getText("perk.gothrogersDance"), 0.1),
      STORM_SPELL_COOLDOWN_1(PerkModule.PerkType.STORM_SPELL_COOLDOWN, Text.getText("perk.kennensMaelstrom"), 1.0),
      STORM_SPELL_COOLDOWN_2(PerkModule.PerkType.STORM_SPELL_COOLDOWN, Text.getText("perk.xolotlsIncarnation"), 2.0),
      STORM_SPELL_COOLDOWN_3(PerkModule.PerkType.STORM_SPELL_COOLDOWN, Text.getText("perk.hospellsRain"), 3.0),
      HOLY_GOLEM_SPELL_COST_1(PerkModule.PerkType.HOLY_GOLEM_SPELL_COST, Text.getText("perk.endersGame"), 0.1),
      HOLY_GOLEM_SPELL_COST_2(PerkModule.PerkType.HOLY_GOLEM_SPELL_COST, Text.getText("perk.bloodstonesManifestation"), 0.1),
      HOLY_GOLEM_SPELL_COST_3(PerkModule.PerkType.HOLY_GOLEM_SPELL_COST, Text.getText("perk.homunculusExpertise"), 0.1),
      HOLY_GOLEM_SPELL_COOLDOWN_1(PerkModule.PerkType.HOLY_GOLEM_SPELL_COOLDOWN, Text.getText("perk.golletsCatching"), 1.0),
      HOLY_GOLEM_SPELL_COOLDOWN_2(PerkModule.PerkType.HOLY_GOLEM_SPELL_COOLDOWN, Text.getText("perk.nimdoksEnchatments"), 1.0),
      HOLY_GOLEM_SPELL_COOLDOWN_3(PerkModule.PerkType.HOLY_GOLEM_SPELL_COOLDOWN, Text.getText("perk.cardinsCreations"), 2.0),
      LABOR_GOLEM_SPELL_COST_1(PerkModule.PerkType.LABOR_GOLEM_SPELL_COST, Text.getText("perk.gizbensInsight"), 0.1),
      LABOR_GOLEM_SPELL_COST_2(PerkModule.PerkType.LABOR_GOLEM_SPELL_COST, Text.getText("perk.artificersCompanion"), 0.1),
      LABOR_GOLEM_SPELL_COST_3(PerkModule.PerkType.LABOR_GOLEM_SPELL_COST, Text.getText("perk.dwarvenGuidelines"), 0.1),
      LABOR_GOLEM_SPELL_COOLDOWN_1(PerkModule.PerkType.LABOR_GOLEM_SPELL_COOLDOWN, Text.getText("perk.dakedesFirstWord"), 0.5),
      LABOR_GOLEM_SPELL_COOLDOWN_2(PerkModule.PerkType.LABOR_GOLEM_SPELL_COOLDOWN, Text.getText("perk.clardazzlesIndustry"), 0.5),
      LABOR_GOLEM_SPELL_COOLDOWN_3(PerkModule.PerkType.LABOR_GOLEM_SPELL_COOLDOWN, Text.getText("perk.twinkleyesDiligence"), 0.5),
      ATTRACT_TOWER_RANGE_1(PerkModule.PerkType.ATTRACT_TOWER_RANGE, Text.getText("perk.belindasRadius"), 2.0),
      ATTRACT_TOWER_RANGE_2(PerkModule.PerkType.ATTRACT_TOWER_RANGE, Text.getText("perk.jaedensBringing"), 2.0),
      ATTRACT_TOWER_RANGE_3(PerkModule.PerkType.ATTRACT_TOWER_RANGE, Text.getText("perk.redfieldsDraw"), 2.0),
      ATTRACT_TOWER_FIRE_RATE_1(PerkModule.PerkType.ATTRACT_TOWER_FIRE_RATE, Text.getText("perk.jeremiasReload"), 0.02),
      ATTRACT_TOWER_FIRE_RATE_2(PerkModule.PerkType.ATTRACT_TOWER_FIRE_RATE, Text.getText("perk.reubensAcceleration"), 0.02),
      ATTRACT_TOWER_FIRE_RATE_3(PerkModule.PerkType.ATTRACT_TOWER_FIRE_RATE, Text.getText("perk.clarksMomentum"), 0.03),
      ATTRACT_TOWER_FIRE_RATE_4(PerkModule.PerkType.ATTRACT_TOWER_FIRE_RATE, Text.getText("perk.cristobalsRenewal"), 0.03),
      ATTRACT_TOWER_FIRE_RATE_5(PerkModule.PerkType.ATTRACT_TOWER_FIRE_RATE, Text.getText("perk.goransPace"), 0.05),
      ATTRACT_TOWER_ENERGY_COST_1(PerkModule.PerkType.ATTRACT_TOWER_ENERGY_COST, Text.getText("perk.marcosRubio"), 0.02),
      ATTRACT_TOWER_ENERGY_COST_2(PerkModule.PerkType.ATTRACT_TOWER_ENERGY_COST, Text.getText("perk.godfreysExpenditure"), 0.02),
      ATTRACT_TOWER_ENERGY_COST_3(PerkModule.PerkType.ATTRACT_TOWER_ENERGY_COST, Text.getText("perk.rutlandsConsumption"), 0.03),
      ATTRACT_TOWER_ENERGY_COST_4(PerkModule.PerkType.ATTRACT_TOWER_ENERGY_COST, Text.getText("perk.edgesAssimilation"), 0.03),
      ATTRACT_TOWER_ENERGY_COST_5(PerkModule.PerkType.ATTRACT_TOWER_ENERGY_COST, Text.getText("perk.fleurettesRetention"), 0.05),
      BANISH_TOWER_RANGE_1(PerkModule.PerkType.BANISH_TOWER_RANGE, Text.getText("perk.lizettesVision"), 2.0),
      BANISH_TOWER_RANGE_2(PerkModule.PerkType.BANISH_TOWER_RANGE, Text.getText("perk.belindasField"), 2.0),
      BANISH_TOWER_RANGE_3(PerkModule.PerkType.BANISH_TOWER_RANGE, Text.getText("perk.astleysTerritory"), 2.0),
      BANISH_TOWER_FIRE_RATE_1(PerkModule.PerkType.BANISH_TOWER_FIRE_RATE, Text.getText("perk.hametsRestock"), 0.02),
      BANISH_TOWER_FIRE_RATE_2(PerkModule.PerkType.BANISH_TOWER_FIRE_RATE, Text.getText("perk.knotleysFleetness"), 0.02),
      BANISH_TOWER_FIRE_RATE_3(PerkModule.PerkType.BANISH_TOWER_FIRE_RATE, Text.getText("perk.xixsExpeditiousness"), 0.03),
      BANISH_TOWER_FIRE_RATE_4(PerkModule.PerkType.BANISH_TOWER_FIRE_RATE, Text.getText("perk.xandersSwiftness"), 0.03),
      BANISH_TOWER_FIRE_RATE_5(PerkModule.PerkType.BANISH_TOWER_FIRE_RATE, Text.getText("perk.farksRush"), 0.05),
      BANISH_TOWER_ENERGY_COST_1(PerkModule.PerkType.BANISH_TOWER_ENERGY_COST, Text.getText("perk.malinsInvestment"), 0.02),
      BANISH_TOWER_ENERGY_COST_2(PerkModule.PerkType.BANISH_TOWER_ENERGY_COST, Text.getText("perk.marianesUtilization"), 0.02),
      BANISH_TOWER_ENERGY_COST_3(PerkModule.PerkType.BANISH_TOWER_ENERGY_COST, Text.getText("perk.angelettasFusion"), 0.03),
      BANISH_TOWER_ENERGY_COST_4(PerkModule.PerkType.BANISH_TOWER_ENERGY_COST, Text.getText("perk.lillynsAdaptability"), 0.03),
      BANISH_TOWER_ENERGY_COST_5(PerkModule.PerkType.BANISH_TOWER_ENERGY_COST, Text.getText("perk.glowvalesQuantity"), 0.05),
      BALLISTA_TOWER_RANGE_1(PerkModule.PerkType.BALLISTA_TOWER_RANGE, Text.getText("perk.lynnesRange"), 2.0),
      BALLISTA_TOWER_RANGE_2(PerkModule.PerkType.BALLISTA_TOWER_RANGE, Text.getText("perk.kowalskisScope"), 2.0),
      BALLISTA_TOWER_RANGE_3(PerkModule.PerkType.BALLISTA_TOWER_RANGE, Text.getText("perk.hannahsLength"), 2.0),
      BALLISTA_TOWER_FIRE_RATE_1(PerkModule.PerkType.BALLISTA_TOWER_FIRE_RATE, Text.getText("perk.jakesReplacing"), 0.02),
      BALLISTA_TOWER_FIRE_RATE_2(PerkModule.PerkType.BALLISTA_TOWER_FIRE_RATE, Text.getText("perk.daxtersPromptitude"), 0.02),
      BALLISTA_TOWER_FIRE_RATE_3(PerkModule.PerkType.BALLISTA_TOWER_FIRE_RATE, Text.getText("perk.kamilsAlacrity"), 0.03),
      BALLISTA_TOWER_FIRE_RATE_4(PerkModule.PerkType.BALLISTA_TOWER_FIRE_RATE, Text.getText("perk.horseysHustle"), 0.03),
      BALLISTA_TOWER_FIRE_RATE_5(PerkModule.PerkType.BALLISTA_TOWER_FIRE_RATE, Text.getText("perk.huntersUrgency"), 0.05),
      BOW_TOWER_RANGE_1(PerkModule.PerkType.BOW_TOWER_RANGE, Text.getText("perk.foxsLimits"), 2.0),
      BOW_TOWER_RANGE_2(PerkModule.PerkType.BOW_TOWER_RANGE, Text.getText("perk.gwenaelsDomain"), 2.0),
      BOW_TOWER_RANGE_3(PerkModule.PerkType.BOW_TOWER_RANGE, Text.getText("perk.albertonsSwing"), 2.0),
      BOW_TOWER_FIRE_RATE_1(PerkModule.PerkType.BOW_TOWER_FIRE_RATE, Text.getText("perk.sporksReplenishment"), 0.02),
      BOW_TOWER_FIRE_RATE_2(PerkModule.PerkType.BOW_TOWER_FIRE_RATE, Text.getText("perk.benjaminsScale"), 0.02),
      BOW_TOWER_FIRE_RATE_3(PerkModule.PerkType.BOW_TOWER_FIRE_RATE, Text.getText("perk.costellosProgression"), 0.03),
      BOW_TOWER_FIRE_RATE_4(PerkModule.PerkType.BOW_TOWER_FIRE_RATE, Text.getText("perk.stephaniesInsistence"), 0.03),
      BOW_TOWER_FIRE_RATE_5(PerkModule.PerkType.BOW_TOWER_FIRE_RATE, Text.getText("perk.lonnebroodsExigency"), 0.05),
      BULLET_TOWER_RANGE_1(PerkModule.PerkType.BULLET_TOWER_RANGE, Text.getText("perk.albertsSpan"), 2.0),
      BULLET_TOWER_RANGE_2(PerkModule.PerkType.BULLET_TOWER_RANGE, Text.getText("perk.johnsonsWidth"), 2.0),
      BULLET_TOWER_RANGE_3(PerkModule.PerkType.BULLET_TOWER_RANGE, Text.getText("perk.poindextersBreadth"), 2.0),
      BULLET_TOWER_FIRE_RATE_1(PerkModule.PerkType.BULLET_TOWER_FIRE_RATE, Text.getText("perk.belsamethsProficiency"), 0.02),
      BULLET_TOWER_FIRE_RATE_2(PerkModule.PerkType.BULLET_TOWER_FIRE_RATE, Text.getText("perk.jadesDestruction"), 0.02),
      BULLET_TOWER_FIRE_RATE_3(PerkModule.PerkType.BULLET_TOWER_FIRE_RATE, Text.getText("perk.nedrasProductivity"), 0.03),
      BULLET_TOWER_FIRE_RATE_4(PerkModule.PerkType.BULLET_TOWER_FIRE_RATE, Text.getText("perk.sengoretsAbundance"), 0.03),
      BULLET_TOWER_FIRE_RATE_5(PerkModule.PerkType.BULLET_TOWER_FIRE_RATE, Text.getText("perk.elesiasAmplitude"), 0.05),
      ELEMENTAL_BOLT_TOWER_RANGE_1(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_RANGE, Text.getText("perk.loransFarness"), 2.0),
      ELEMENTAL_BOLT_TOWER_RANGE_2(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_RANGE, Text.getText("perk.stephensHorizon"), 2.0),
      ELEMENTAL_BOLT_TOWER_RANGE_3(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_RANGE, Text.getText("perk.rydwaltsSweep"), 2.0),
      ELEMENTAL_BOLT_TOWER_FIRE_RATE_1(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_FIRE_RATE, Text.getText("perk.gregorsProgression"), 0.02),
      ELEMENTAL_BOLT_TOWER_FIRE_RATE_2(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_FIRE_RATE, Text.getText("perk.rowsdowersDivvy"), 0.02),
      ELEMENTAL_BOLT_TOWER_FIRE_RATE_3(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_FIRE_RATE, Text.getText("perk.maximiliansTempo"), 0.03),
      ELEMENTAL_BOLT_TOWER_FIRE_RATE_4(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_FIRE_RATE, Text.getText("perk.guybrushsPace"), 0.03),
      ELEMENTAL_BOLT_TOWER_FIRE_RATE_5(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_FIRE_RATE, Text.getText("perk.mcAllistersSkills"), 0.05),
      ELEMENTAL_BOLT_TOWER_ENERGY_COST_1(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_ENERGY_COST, Text.getText("perk.threepwoodsFaculty"), 0.02),
      ELEMENTAL_BOLT_TOWER_ENERGY_COST_2(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_ENERGY_COST, Text.getText("perk.ajarnsStandard"), 0.02),
      ELEMENTAL_BOLT_TOWER_ENERGY_COST_3(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_ENERGY_COST, Text.getText("perk.chartsScale"), 0.03),
      ELEMENTAL_BOLT_TOWER_ENERGY_COST_4(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_ENERGY_COST, Text.getText("perk.martinsOvercharge"), 0.03),
      ELEMENTAL_BOLT_TOWER_ENERGY_COST_5(PerkModule.PerkType.ELEMENTAL_BOLT_TOWER_ENERGY_COST, Text.getText("perk.nathansPotential"), 0.05),
      MOTIVATOR_TOWER_RANGE_1(PerkModule.PerkType.MOTIVATOR_TOWER_RANGE, Text.getText("perk.marcsMuscle"), 2.0),
      MOTIVATOR_TOWER_RANGE_2(PerkModule.PerkType.MOTIVATOR_TOWER_RANGE, Text.getText("perk.camillesPush"), 2.0),
      MOTIVATOR_TOWER_RANGE_3(PerkModule.PerkType.MOTIVATOR_TOWER_RANGE, Text.getText("perk.vichetsRange"), 2.0),
      MOTIVATOR_TOWER_FIRE_RATE_1(PerkModule.PerkType.MOTIVATOR_TOWER_FIRE_RATE, Text.getText("perk.alexandersQuota"), 0.02),
      MOTIVATOR_TOWER_FIRE_RATE_2(PerkModule.PerkType.MOTIVATOR_TOWER_FIRE_RATE, Text.getText("perk.qinsApportionment"), 0.02),
      MOTIVATOR_TOWER_FIRE_RATE_3(PerkModule.PerkType.MOTIVATOR_TOWER_FIRE_RATE, Text.getText("perk.johsRate"), 0.03),
      MOTIVATOR_TOWER_FIRE_RATE_4(PerkModule.PerkType.MOTIVATOR_TOWER_FIRE_RATE, Text.getText("perk.earnestsDispatchment"), 0.03),
      MOTIVATOR_TOWER_FIRE_RATE_5(PerkModule.PerkType.MOTIVATOR_TOWER_FIRE_RATE, Text.getText("perk.troysCelerity"), 0.05),
      MOTIVATOR_TOWER_ENERGY_COST_1(PerkModule.PerkType.MOTIVATOR_TOWER_ENERGY_COST, Text.getText("perk.morikhanninsPowder"), 0.02),
      MOTIVATOR_TOWER_ENERGY_COST_2(PerkModule.PerkType.MOTIVATOR_TOWER_ENERGY_COST, Text.getText("perk.gaborsSupercharge"), 0.02),
      MOTIVATOR_TOWER_ENERGY_COST_3(PerkModule.PerkType.MOTIVATOR_TOWER_ENERGY_COST, Text.getText("perk.ushalgsClout"), 0.03),
      MOTIVATOR_TOWER_ENERGY_COST_4(PerkModule.PerkType.MOTIVATOR_TOWER_ENERGY_COST, Text.getText("perk.billsShifting"), 0.03),
      MOTIVATOR_TOWER_ENERGY_COST_5(PerkModule.PerkType.MOTIVATOR_TOWER_ENERGY_COST, Text.getText("perk.wesleysCreation"), 0.05),
      PHANTOM_DART_TOWER_RANGE_1(PerkModule.PerkType.PHANTOM_DART_TOWER_RANGE, Text.getText("perk.bondesExpansion"), 2.0),
      PHANTOM_DART_TOWER_RANGE_2(PerkModule.PerkType.PHANTOM_DART_TOWER_RANGE, Text.getText("perk.roguesSpread"), 2.0),
      PHANTOM_DART_TOWER_RANGE_3(PerkModule.PerkType.PHANTOM_DART_TOWER_RANGE, Text.getText("perk.hatschimakisFoundation"), 2.0),
      PHANTOM_DART_TOWER_FIRE_RATE_1(PerkModule.PerkType.PHANTOM_DART_TOWER_FIRE_RATE, Text.getText("perk.uptonsDuress"), 0.02),
      PHANTOM_DART_TOWER_FIRE_RATE_2(PerkModule.PerkType.PHANTOM_DART_TOWER_FIRE_RATE, Text.getText("perk.jonasFury"), 0.02),
      PHANTOM_DART_TOWER_FIRE_RATE_3(PerkModule.PerkType.PHANTOM_DART_TOWER_FIRE_RATE, Text.getText("perk.leonidsExhaustion"), 0.03),
      PHANTOM_DART_TOWER_FIRE_RATE_4(PerkModule.PerkType.PHANTOM_DART_TOWER_FIRE_RATE, Text.getText("perk.damianosFormulation"), 0.03),
      PHANTOM_DART_TOWER_FIRE_RATE_5(PerkModule.PerkType.PHANTOM_DART_TOWER_FIRE_RATE, Text.getText("perk.raissasProduction"), 0.05),
      PHANTOM_DART_TOWER_ENERGY_COST_1(PerkModule.PerkType.PHANTOM_DART_TOWER_ENERGY_COST, Text.getText("perk.lilosReprisal"), 0.02),
      PHANTOM_DART_TOWER_ENERGY_COST_2(PerkModule.PerkType.PHANTOM_DART_TOWER_ENERGY_COST, Text.getText("perk.hildasCache"), 0.02),
      PHANTOM_DART_TOWER_ENERGY_COST_3(PerkModule.PerkType.PHANTOM_DART_TOWER_ENERGY_COST, Text.getText("perk.charmainesReserve"), 0.03),
      PHANTOM_DART_TOWER_ENERGY_COST_4(PerkModule.PerkType.PHANTOM_DART_TOWER_ENERGY_COST, Text.getText("perk.fridasAuxiliary"), 0.03),
      PHANTOM_DART_TOWER_ENERGY_COST_5(PerkModule.PerkType.PHANTOM_DART_TOWER_ENERGY_COST, Text.getText("perk.faithofPompeii"), 0.05),
      RECOMBOBULATOR_TOWER_RANGE_1(PerkModule.PerkType.RECOMBOBULATOR_TOWER_RANGE, Text.getText("perk.brigettesMotility"), 2.0),
      RECOMBOBULATOR_TOWER_RANGE_2(PerkModule.PerkType.RECOMBOBULATOR_TOWER_RANGE, Text.getText("perk.ceciliasEstate"), 2.0),
      RECOMBOBULATOR_TOWER_RANGE_3(PerkModule.PerkType.RECOMBOBULATOR_TOWER_RANGE, Text.getText("perk.almasAdjuvant"), 2.0),
      RECOMBOBULATOR_TOWER_FIRE_RATE_1(PerkModule.PerkType.RECOMBOBULATOR_TOWER_FIRE_RATE, Text.getText("perk.jordansEquity"), 0.02),
      RECOMBOBULATOR_TOWER_FIRE_RATE_2(PerkModule.PerkType.RECOMBOBULATOR_TOWER_FIRE_RATE, Text.getText("perk.marquisasAllotment"), 0.02),
      RECOMBOBULATOR_TOWER_FIRE_RATE_3(PerkModule.PerkType.RECOMBOBULATOR_TOWER_FIRE_RATE, Text.getText("perk.piasPower"), 0.03),
      RECOMBOBULATOR_TOWER_FIRE_RATE_4(PerkModule.PerkType.RECOMBOBULATOR_TOWER_FIRE_RATE, Text.getText("perk.madalenesDisembark"), 0.03),
      RECOMBOBULATOR_TOWER_FIRE_RATE_5(PerkModule.PerkType.RECOMBOBULATOR_TOWER_FIRE_RATE, Text.getText("perk.huldasFleetness"), 0.05),
      RECOMBOBULATOR_TOWER_ENERGY_COST_1(PerkModule.PerkType.RECOMBOBULATOR_TOWER_ENERGY_COST, Text.getText("perk.olympesGenesis"), 0.02),
      RECOMBOBULATOR_TOWER_ENERGY_COST_2(PerkModule.PerkType.RECOMBOBULATOR_TOWER_ENERGY_COST, Text.getText("perk.ashleesAnchor"), 0.02),
      RECOMBOBULATOR_TOWER_ENERGY_COST_3(PerkModule.PerkType.RECOMBOBULATOR_TOWER_ENERGY_COST, Text.getText("perk.lorrainasAccess"), 0.03),
      RECOMBOBULATOR_TOWER_ENERGY_COST_4(PerkModule.PerkType.RECOMBOBULATOR_TOWER_ENERGY_COST, Text.getText("perk.mckenziesattainanance"), 0.03),
      RECOMBOBULATOR_TOWER_ENERGY_COST_5(PerkModule.PerkType.RECOMBOBULATOR_TOWER_ENERGY_COST, Text.getText("perk.parksSubservience"), 0.05),
      SLING_TOWER_RANGE_1(PerkModule.PerkType.SLING_TOWER_RANGE, Text.getText("perk.corinsWeight"), 2.0),
      SLING_TOWER_RANGE_2(PerkModule.PerkType.SLING_TOWER_RANGE, Text.getText("perk.roswellsMobility"), 2.0),
      SLING_TOWER_RANGE_3(PerkModule.PerkType.SLING_TOWER_RANGE, Text.getText("perk.hammettsVoyaging"), 2.0),
      SLING_TOWER_FIRE_RATE_1(PerkModule.PerkType.SLING_TOWER_FIRE_RATE, Text.getText("perk.wadesPromptness"), 0.02),
      SLING_TOWER_FIRE_RATE_2(PerkModule.PerkType.SLING_TOWER_FIRE_RATE, Text.getText("perk.nottleysBriskness"), 0.02),
      SLING_TOWER_FIRE_RATE_3(PerkModule.PerkType.SLING_TOWER_FIRE_RATE, Text.getText("perk.colbysBustle"), 0.03),
      SLING_TOWER_FIRE_RATE_4(PerkModule.PerkType.SLING_TOWER_FIRE_RATE, Text.getText("perk.pellsFlight"), 0.03),
      SLING_TOWER_FIRE_RATE_5(PerkModule.PerkType.SLING_TOWER_FIRE_RATE, Text.getText("perk.chestersEmbarkment"), 0.05),
      SPRAY_TOWER_RANGE_1(PerkModule.PerkType.SPRAY_TOWER_RANGE, Text.getText("perk.brooksOrbit"), 2.0),
      SPRAY_TOWER_RANGE_2(PerkModule.PerkType.SPRAY_TOWER_RANGE, Text.getText("perk.xaverinesCompass"), 2.0),
      SPRAY_TOWER_RANGE_3(PerkModule.PerkType.SPRAY_TOWER_RANGE, Text.getText("perk.mariechensHorizon"), 2.0),
      STATIC_TOWER_RANGE_1(PerkModule.PerkType.STATIC_TOWER_RANGE, Text.getText("perk.mildenhallsGrowth"), 2.0),
      STATIC_TOWER_RANGE_2(PerkModule.PerkType.STATIC_TOWER_RANGE, Text.getText("perk.finjasFarness"), 2.0),
      STATIC_TOWER_RANGE_3(PerkModule.PerkType.STATIC_TOWER_RANGE, Text.getText("perk.stampersExtent"), 2.0),
      STATIC_TOWER_FIRE_RATE_1(PerkModule.PerkType.STATIC_TOWER_FIRE_RATE, Text.getText("perk.mikklesEagerness"), 0.02),
      STATIC_TOWER_FIRE_RATE_2(PerkModule.PerkType.STATIC_TOWER_FIRE_RATE, Text.getText("perk.ahmedsGait"), 0.02),
      STATIC_TOWER_FIRE_RATE_3(PerkModule.PerkType.STATIC_TOWER_FIRE_RATE, Text.getText("perk.gavinsPromptitude"), 0.03),
      STATIC_TOWER_FIRE_RATE_4(PerkModule.PerkType.STATIC_TOWER_FIRE_RATE, Text.getText("perk.howellsExertion"), 0.03),
      STATIC_TOWER_FIRE_RATE_5(PerkModule.PerkType.STATIC_TOWER_FIRE_RATE, Text.getText("perk.dalyngriggesMoxie"), 0.05),
      STATIC_TOWER_ENERGY_COST_1(PerkModule.PerkType.STATIC_TOWER_ENERGY_COST, Text.getText("perk.igorsEnergy"), 0.02),
      STATIC_TOWER_ENERGY_COST_2(PerkModule.PerkType.STATIC_TOWER_ENERGY_COST, Text.getText("perk.kilosDrive"), 0.02),
      STATIC_TOWER_ENERGY_COST_3(PerkModule.PerkType.STATIC_TOWER_ENERGY_COST, Text.getText("perk.caseysEfficiency"), 0.03),
      STATIC_TOWER_ENERGY_COST_4(PerkModule.PerkType.STATIC_TOWER_ENERGY_COST, Text.getText("perk.mcChrystalsFlurry"), 0.03),
      STATIC_TOWER_ENERGY_COST_5(PerkModule.PerkType.STATIC_TOWER_ENERGY_COST, Text.getText("perk.boltonsRush"), 0.05),
      TOWER_STORAGE_1(PerkModule.PerkType.TOWER_STORAGE, Text.getText("perk.edwardsMunitions"), 0.02),
      TOWER_STORAGE_2(PerkModule.PerkType.TOWER_STORAGE, Text.getText("perk.middlecoatsArms"), 0.02),
      TOWER_STORAGE_3(PerkModule.PerkType.TOWER_STORAGE, Text.getText("perk.caseysOrdnance"), 0.04),
      TOWER_STORAGE_4(PerkModule.PerkType.TOWER_STORAGE, Text.getText("perk.oslauthsDepository"), 0.06),
      TOWER_STORAGE_5(PerkModule.PerkType.TOWER_STORAGE, Text.getText("perk.manlysArsenal"), 0.06),
      NOMAD_APPEARANCE_RATE_1(PerkModule.PerkType.NOMAD_APPEARANCE_RATE, Text.getText("perk.terimortsAbridgment"), 0.02),
      NOMAD_APPEARANCE_RATE_2(PerkModule.PerkType.NOMAD_APPEARANCE_RATE, Text.getText("perk.unveilingofVaelophis"), 0.02),
      NOMAD_APPEARANCE_RATE_3(PerkModule.PerkType.NOMAD_APPEARANCE_RATE, Text.getText("perk.leyasCompression"), 0.04),
      NOMAD_APPEARANCE_RATE_4(PerkModule.PerkType.NOMAD_APPEARANCE_RATE, Text.getText("perk.scottaugsIntroduction"), 0.06),
      NOMAD_APPEARANCE_RATE_5(PerkModule.PerkType.NOMAD_APPEARANCE_RATE, Text.getText("perk.serarosActualization"), 0.06),
      NOMAD_AMOUNT_1(PerkModule.PerkType.NOMAD_AMOUNT, Text.getText("perk.avenarMultiplication"), 1.0),
      NOMAD_AMOUNT_2(PerkModule.PerkType.NOMAD_AMOUNT, Text.getText("perk.nyxsExaggeration"), 1.0),
      NOMAD_AMOUNT_3(PerkModule.PerkType.NOMAD_AMOUNT, Text.getText("perk.garkneesAugmentation"), 1.0),
      NOMAD_AMOUNT_4(PerkModule.PerkType.NOMAD_AMOUNT, Text.getText("perk.drogcartersAccumulation"), 1.0),
      NOMAD_AMOUNT_5(PerkModule.PerkType.NOMAD_AMOUNT, Text.getText("perk.visermyersRise"), 1.0),
      ANIMAL_APPEARANCE_RATE_1(PerkModule.PerkType.ANIMAL_APPEARANCE_RATE, Text.getText("perk.gingersSpirit"), 0.02),
      ANIMAL_APPEARANCE_RATE_2(PerkModule.PerkType.ANIMAL_APPEARANCE_RATE, Text.getText("perk.rainesGrowth"), 0.02),
      ANIMAL_APPEARANCE_RATE_3(PerkModule.PerkType.ANIMAL_APPEARANCE_RATE, Text.getText("perk.blessingofNoveriah"), 0.04),
      ANIMAL_APPEARANCE_RATE_4(PerkModule.PerkType.ANIMAL_APPEARANCE_RATE, Text.getText("perk.perookiesScent"), 0.06),
      ANIMAL_APPEARANCE_RATE_5(PerkModule.PerkType.ANIMAL_APPEARANCE_RATE, Text.getText("perk.hadrinasHabitat"), 0.06),
      BETTER_MARKET_PRICES_1(PerkModule.PerkType.BETTER_MARKET_PRICES, Text.getText("perk.limbysThrift"), 0.05),
      BETTER_MARKET_PRICES_2(PerkModule.PerkType.BETTER_MARKET_PRICES, Text.getText("perk.deductionofSalvak"), 0.05),
      BETTER_MARKET_PRICES_3(PerkModule.PerkType.BETTER_MARKET_PRICES, Text.getText("perk.noteofBlasphemy"), 0.05),
      BETTER_MARKET_PRICES_4(PerkModule.PerkType.BETTER_MARKET_PRICES, Text.getText("perk.dayaphimasBarter"), 0.05),
      BETTER_MARKET_PRICES_5(PerkModule.PerkType.BETTER_MARKET_PRICES, Text.getText("perk.tamanesRecession"), 0.05),
      RESOURCE_DECAY_RATE_1(PerkModule.PerkType.RESOURCE_DECAY_RATE, Text.getText("perk.keithsFlourishing"), 0.02),
      RESOURCE_DECAY_RATE_2(PerkModule.PerkType.RESOURCE_DECAY_RATE, Text.getText("perk.nizzsStrengthening"), 0.02),
      RESOURCE_DECAY_RATE_3(PerkModule.PerkType.RESOURCE_DECAY_RATE, Text.getText("perk.robertsUnwrinkling"), 0.04),
      RESOURCE_DECAY_RATE_4(PerkModule.PerkType.RESOURCE_DECAY_RATE, Text.getText("perk.rugmuksIndelibility"), 0.06),
      RESOURCE_DECAY_RATE_5(PerkModule.PerkType.RESOURCE_DECAY_RATE, Text.getText("perk.austinsFiber"), 0.06),
      MORE_LOOT_IN_LOOT_BOXES_1(PerkModule.PerkType.MORE_LOOT_IN_LOOT_BOXES, Text.getText("perk.domorosTake"), 0.02),
      MORE_LOOT_IN_LOOT_BOXES_2(PerkModule.PerkType.MORE_LOOT_IN_LOOT_BOXES, Text.getText("perk.marcelosHaul"), 0.02),
      MORE_LOOT_IN_LOOT_BOXES_3(PerkModule.PerkType.MORE_LOOT_IN_LOOT_BOXES, Text.getText("perk.kittenheartsBooty"), 0.04),
      MORE_LOOT_IN_LOOT_BOXES_4(PerkModule.PerkType.MORE_LOOT_IN_LOOT_BOXES, Text.getText("perk.pettigrewsSqueeze"), 0.06),
      MORE_LOOT_IN_LOOT_BOXES_5(PerkModule.PerkType.MORE_LOOT_IN_LOOT_BOXES, Text.getText("perk.sungwoosPillages"), 0.06),
      TOOL_DURABILITY_1(PerkModule.PerkType.TOOL_DURABILITY, Text.getText("perk.eitrisIngenuity"), 0.05),
      TOOL_DURABILITY_2(PerkModule.PerkType.TOOL_DURABILITY, Text.getText("perk.hephaestusSmithery"), 0.05),
      TOOL_DURABILITY_3(PerkModule.PerkType.TOOL_DURABILITY, Text.getText("perk.dresdinsWit"), 0.05),
      TOOL_DURABILITY_4(PerkModule.PerkType.TOOL_DURABILITY, Text.getText("perk.beepsGutsiness"), 0.05),
      TOOL_DURABILITY_5(PerkModule.PerkType.TOOL_DURABILITY, Text.getText("perk.dwarvenDurability"), 0.05);

      private PerkModule.PerkType perkType;
      private String perkName;
      private double value;

      private Perk(PerkModule.PerkType perkCategory, String perkName, double value) {
         this.perkType = perkCategory;
         this.perkName = perkName;
         this.value = value;
      }

      public PerkModule.PerkType getPerkType() {
         return this.perkType;
      }

      public String getPerkName() {
         return this.perkName;
      }

      public double getValue() {
         return this.value;
      }
   }

   public static enum PerkCategory {
      REGULAR,
      DEFENSE,
      GOD,
      VILLAGE;
   }

   public static enum PerkDataFormat {
      WHOLE_NUMBER,
      PERCENT,
      PERCENT_NEGATIVE,
      SECONDS,
      SECONDS_NEGATIVE;
   }

   public static enum PerkType {
      MAXIMUM_HEALTH(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.maximumHealth"), "iconHitPoints", PerkModule.PerkDataFormat.PERCENT),
      HEALTH_RECOVERY_SPEED(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.healthRecoverySpeed"), "iconHitPoints", PerkModule.PerkDataFormat.PERCENT),
      MAXIMUM_HUNGER(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.maximumHunger"), "iconHunger", PerkModule.PerkDataFormat.PERCENT),
      HUNGER_DECAY_RATE(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.hungerDecayRate"), "iconHunger", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      MAXIMUM_THIRST(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.maximumThirst"), "iconWater", PerkModule.PerkDataFormat.PERCENT),
      THIRST_DECAY_RATE(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.thirstDecayRate"), "iconWater", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      MAXIMUM_FAITH(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.maximumFaith"), "iconFaith", PerkModule.PerkDataFormat.PERCENT),
      FAITH_DECAY_RATE(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.faithDecayRate"), "iconFaith", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      MAXIMUM_ENERGY(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.maximumEnergy"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT),
      ENERGY_DECAY_RATE(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.energyDecayRate"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      MAXIMUM_SOCIAL(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.maximumSocial"), "iconChatBubble1", PerkModule.PerkDataFormat.PERCENT),
      LIFE_EXPECTANCY(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.lifeExpectancy"), "iconLifeExpectancy", PerkModule.PerkDataFormat.PERCENT),
      MOVEMENT_SPEED(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.movementSpeed"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT),
      DAMAGE_RESISTANCE(
         PerkModule.PerkCategory.VILLAGE,
         Text.getText("perkType.damageResistance"),
         ResourceModule.ResourceType.LEATHER_SHIELD,
         PerkModule.PerkDataFormat.PERCENT
      ),
      MELEE_DAMAGE(
         PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.meleeDamage"), ResourceModule.ResourceType.IRON_SWORD, PerkModule.PerkDataFormat.PERCENT
      ),
      RANGED_DAMAGE(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.rangedDamage"), ResourceModule.ResourceType.BOW, PerkModule.PerkDataFormat.PERCENT),
      MAXIMUM_CARRIED_RESOURCES(
         PerkModule.PerkCategory.VILLAGE,
         Text.getText("perkType.maximumCarriedResources"),
         ResourceModule.ResourceType.ROCK,
         PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      FERTILITY(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.fertility"), "iconChild", PerkModule.PerkDataFormat.PERCENT),
      GHOST_APPEARANCE_CHANCE(
         PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.ghostAppearanceChance"), "iconVillager", PerkModule.PerkDataFormat.PERCENT
      ),
      VILLAGER_COLD_RESISTANCE(
         PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.villagerColdResistance"), "iconVillager", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      VILLAGER_HEAT_RESISTANCE(
         PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.villagerHeatResistance"), "iconVillager", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      VILLAGER_LIFESPANS(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.villagerLifespans"), "iconAge", PerkModule.PerkDataFormat.PERCENT),
      WISER_ELDERS(PerkModule.PerkCategory.VILLAGE, Text.getText("perkType.wiserElders"), "iconChatBubble22", PerkModule.PerkDataFormat.PERCENT),
      IMPROVED_STORAGE(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.improvedStorage"), "iconChest", PerkModule.PerkDataFormat.PERCENT),
      EFFICIENT_HOUSING(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.efficientHousing"), "iconHome", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      EFFICIENT_CONSTRUCTION(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.efficientConstruction"), ResourceModule.ResourceType.HAMMER, PerkModule.PerkDataFormat.PERCENT
      ),
      FARM_GROWTH_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.farmGrowthSpeed"), ResourceModule.ResourceType.HOE, PerkModule.PerkDataFormat.PERCENT
      ),
      GLOBAL_WORK_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.globalWorkSpeed"), ResourceModule.ResourceType.PICK_AXE, PerkModule.PerkDataFormat.PERCENT
      ),
      HARVESTING_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.harvestingSpeed"), ResourceModule.ResourceType.AXE, PerkModule.PerkDataFormat.PERCENT
      ),
      REFINING_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.refiningSpeed"), ResourceModule.ResourceType.HAMMER, PerkModule.PerkDataFormat.PERCENT
      ),
      BUILDING_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.buildingSpeed"), ResourceModule.ResourceType.HAMMER, PerkModule.PerkDataFormat.PERCENT
      ),
      ROAD_CONSTRUCTION_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.roadConstructionSpeed"), ResourceModule.ResourceType.SHOVEL, PerkModule.PerkDataFormat.PERCENT
      ),
      DISMANTLING_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.dismantlingSpeed"), ResourceModule.ResourceType.HAMMER, PerkModule.PerkDataFormat.PERCENT
      ),
      CLEARING_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.clearingSpeed"), ResourceModule.ResourceType.AXE, PerkModule.PerkDataFormat.PERCENT
      ),
      REPAIRING_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.repairingSpeed"), ResourceModule.ResourceType.HAMMER, PerkModule.PerkDataFormat.PERCENT
      ),
      MAINTAINING_ROAD_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.maintainingRoadSpeed"), ResourceModule.ResourceType.HAMMER, PerkModule.PerkDataFormat.PERCENT
      ),
      FARMING_SPEED(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.farmingSpeed"), ResourceModule.ResourceType.HOE, PerkModule.PerkDataFormat.PERCENT),
      MEDIC_WORK_SPEED(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.medicWorkSpeed"), ResourceModule.ResourceType.MEDKIT, PerkModule.PerkDataFormat.PERCENT
      ),
      PRAYING_SPEED(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.prayingSpeed"), "iconFaith", PerkModule.PerkDataFormat.PERCENT),
      CAPTURING_SOULS_SPEED(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.capturingSoulsSpeed"), "iconFaith", PerkModule.PerkDataFormat.PERCENT),
      TEND_ANIMALS_SPEED(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.tendAnimalsSpeed"), "iconOwner", PerkModule.PerkDataFormat.PERCENT),
      WATER_STORAGE(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.waterStorage"), "iconWater", PerkModule.PerkDataFormat.PERCENT),
      WOOD_GOLEM_LEVEL(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.woodGolemLevel"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      WOOD_GOLEM_CAPACITY(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.woodGolemCapacity"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      WOOD_GOLEM_CHARGE_TIME(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.woodGolemChargeTime"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      STONE_GOLEM_LEVEL(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.stoneGolemLevel"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      STONE_GOLEM_CAPACITY(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.stoneGolemCapacity"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      STONE_GOLEM_CHARGE_TIME(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.stoneGolemChargeTime"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      CRYSTAL_GOLEM_LEVEL(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.crystalGolemLevel"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      CRYSTAL_GOLEM_CAPACITY(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.crystalGolemCapacity"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      CRYSTAL_GOLEM_CHARGE_TIME(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.crystalGolemChargeTime"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      BUILDING_SLOTS(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.buildingSlots"), "iconCivic", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      ANCILLARY_SLOTS(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.ancillarySlots"), "iconCivic", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      MARKET_RESTOCK_AMOUNT(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.marketRestockAmount"), "iconGoldCoins", PerkModule.PerkDataFormat.PERCENT),
      MARKET_CATJEET_CAPACITY(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.marketCatjeetCapacity"), "iconGoldCoins", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      ESSENCE_COLLECTOR_CAPACITY(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.essenceCollectorCapacity"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT
      ),
      EFFICIENT_TRASH_BURNING(
         PerkModule.PerkCategory.REGULAR,
         Text.getText("perkType.efficientTrashBurning"),
         ResourceModule.ResourceType.TRASHY_TRASH,
         PerkModule.PerkDataFormat.PERCENT
      ),
      BUILDING_HEALTH(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.buildingHealth"), "iconHitPoints", PerkModule.PerkDataFormat.PERCENT),
      INCREASED_CULLIS_GATE_STABILITY(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.increasedCullisGateStability"), "iconHitPoints", PerkModule.PerkDataFormat.PERCENT
      ),
      ATTRACT_TOWER_RANGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.attractTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      ATTRACT_TOWER_FIRE_RATE(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.attractTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT
      ),
      ATTRACT_TOWER_ENERGY_COST(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.attractTowerEnergyCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      BANISH_TOWER_RANGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.banishTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      BANISH_TOWER_FIRE_RATE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.banishTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT),
      BANISH_TOWER_ENERGY_COST(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.banishTowerEnergyCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      BALLISTA_TOWER_RANGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.ballistaTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      BALLISTA_TOWER_FIRE_RATE(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.ballistaTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT
      ),
      BOW_TOWER_RANGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.bowTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      BOW_TOWER_FIRE_RATE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.bowTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT),
      BULLET_TOWER_RANGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.bulletTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      BULLET_TOWER_FIRE_RATE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.bulletTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT),
      ELEMENTAL_BOLT_TOWER_RANGE(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.elementalBoltTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      ELEMENTAL_BOLT_TOWER_FIRE_RATE(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.elementalBoltTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT
      ),
      ELEMENTAL_BOLT_TOWER_ENERGY_COST(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.elementalBoltTowerEnergyCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      MOTIVATOR_TOWER_RANGE(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.motivatorTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      MOTIVATOR_TOWER_FIRE_RATE(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.motivatorTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT
      ),
      MOTIVATOR_TOWER_ENERGY_COST(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.motivatorTowerEnergyCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      PHANTOM_DART_TOWER_RANGE(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.phantomDartTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      PHANTOM_DART_TOWER_FIRE_RATE(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.phantomDartTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT
      ),
      PHANTOM_DART_TOWER_ENERGY_COST(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.phantomDartTowerEnergyCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      RECOMBOBULATOR_TOWER_RANGE(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.recombobulatorTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      RECOMBOBULATOR_TOWER_FIRE_RATE(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.recombobulatorTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT
      ),
      RECOMBOBULATOR_TOWER_ENERGY_COST(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.recombobulatorTowerEnergyCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      SLING_TOWER_RANGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.slingTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      SLING_TOWER_FIRE_RATE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.slingTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT),
      SPRAY_TOWER_RANGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.sprayTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      STATIC_TOWER_RANGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.staticTowerRange"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      STATIC_TOWER_FIRE_RATE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.staticTowerFireRate"), "iconDexterity", PerkModule.PerkDataFormat.PERCENT),
      STATIC_TOWER_ENERGY_COST(
         PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.staticTowerEnergyCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      TOWER_STORAGE(PerkModule.PerkCategory.DEFENSE, Text.getText("perkType.towerStorage"), "iconAmmo", PerkModule.PerkDataFormat.PERCENT),
      HOLY_GOLEM_LEVEL(PerkModule.PerkCategory.GOD, Text.getText("perkType.holyGolemLevel"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      LABOR_GOLEM_LEVEL(PerkModule.PerkCategory.GOD, Text.getText("perkType.laborGolemLevel"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      HOLY_GOLEM_SPEED(PerkModule.PerkCategory.GOD, Text.getText("perkType.holyGolemSpeed"), "iconGolem", PerkModule.PerkDataFormat.PERCENT),
      LABOR_GOLEM_MAXIMUM_CARRIED_RESOURCES(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.laborGolemMaximumCarriedResources"), "iconGolem", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      MORE_MINIMUM_INFLUENCE(PerkModule.PerkCategory.GOD, Text.getText("perkType.moreMinimumInfluence"), "iconFaithUp", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      MORE_INFLUENCE_PER_ANIMAL(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.moreInfluencePerAnimal"), "iconFaithUp", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      MORE_INFLUENCE_PER_VILLAGER(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.moreInfluencePerVillager"), "iconFaithUp", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      INCREASED_INFLUENCE_GENERATION(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.increasedInfluenceGeneration"), "iconGodXP", PerkModule.PerkDataFormat.PERCENT
      ),
      BANISH_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.banishSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      BANISH_SPELL_COOLDOWN(PerkModule.PerkCategory.GOD, Text.getText("perkType.banishSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE),
      BANISH_SPELL_DURATION(PerkModule.PerkCategory.GOD, Text.getText("perkType.banishSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS),
      BANISH_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.banishSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      CHARM_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.charmSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      CHARM_SPELL_COOLDOWN(PerkModule.PerkCategory.GOD, Text.getText("perkType.charmSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE),
      COLD_AURA_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.coldAuraSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      COLD_AURA_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.coldAuraSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      COLD_AURA_SPELL_DURATION(PerkModule.PerkCategory.GOD, Text.getText("perkType.coldAuraSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS),
      COLD_AURA_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.coldAuraSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      COMET_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.cometSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      CONJURE_MATERIAL_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.conjureMaterialSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      CONJURE_MATERIAL_SPELL_AMOUNT(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.conjureMaterialSpellAmount"), "iconLevel", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      CONSTRUCT_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.constructSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      CONSTRUCT_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.constructSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      CONSTRUCT_SPELL_DURATION(PerkModule.PerkCategory.GOD, Text.getText("perkType.constructSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS),
      CONSTRUCT_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.constructSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      DISSOLVE_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.dissolveSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      DISSOLVE_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.dissolveSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      DISSOLVE_SPELL_DURATION(PerkModule.PerkCategory.GOD, Text.getText("perkType.dissolveSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS),
      DISSOLVE_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.dissolveSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      DIVINE_BLESSING_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.divineBlessingSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      DIVINE_BLESSING_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.divineBlessingSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      EARTHQUAKE_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.earthquakeSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      EARTHQUAKE_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.earthquakeSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      FLAME_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.flameSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      GOD_TOWER_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.godTowerSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      GOD_TOWER_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.godTowerSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      GOD_WALL_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.godWallSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      HARVEST_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.harvestSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      HARVEST_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.harvestSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      HARVEST_SPELL_DURATION(PerkModule.PerkCategory.GOD, Text.getText("perkType.harvestSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS),
      HARVEST_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.harvestSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      HEALING_AURA_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.healingAuraSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      HEALING_AURA_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.healingAuraSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      HEALING_AURA_SPELL_DURATION(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.healingAuraSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS
      ),
      HEALING_AURA_SPELL_RADIUS(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.healingAuraSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      HOLY_POTATOES_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.holyPotatoesSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      HOLY_POTATOES_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.holyPotatoesSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      HOLY_POTATOES_SPELL_DURATION(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.holyPotatoesSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS
      ),
      HOLY_POTATOES_SPELL_RADIUS(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.holyPotatoesSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      HOLY_WOOD_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.holyWoodSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      HOLY_WOOD_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.holyWoodSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      HOLY_WOOD_SPELL_DURATION(PerkModule.PerkCategory.GOD, Text.getText("perkType.holyWoodSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS),
      HOLY_WOOD_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.holyWoodSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      ILLUMINATE_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.illuminateSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      ILLUMINATE_SPELL_DURATION(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.illuminateSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS
      ),
      ILLUMINATE_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.illuminateSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      LIGHTNING_BOLT_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.lightningBoltSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      MAGIC_BOLTS_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.magicBoltsSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      MAGIC_BOLTS_SPELL_COUNT(PerkModule.PerkCategory.GOD, Text.getText("perkType.magicBoltsSpellCount"), "iconGodXP", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      MEND_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.mendSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      MEND_SPELL_DURATION(PerkModule.PerkCategory.GOD, Text.getText("perkType.mendSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS),
      MEND_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.mendSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      METEOR_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.meteorSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      METEOR_SPELL_AMOUNT(PerkModule.PerkCategory.GOD, Text.getText("perkType.meteorSpellAmount"), "iconLevel", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      MOTIVATE_LAND_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.motivateLandSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      MOTIVATE_LAND_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.motivateLandSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      MOTIVATE_LAND_SPELL_DURATION(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.motivateLandSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS
      ),
      MOTIVATE_LAND_SPELL_RADIUS(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.motivateLandSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER
      ),
      RECALL_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.recallSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      RECALL_SPELL_COOLDOWN(PerkModule.PerkCategory.GOD, Text.getText("perkType.recallSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE),
      RECALL_SPELL_DURATION(PerkModule.PerkCategory.GOD, Text.getText("perkType.recallSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS),
      RECALL_SPELL_RADIUS(PerkModule.PerkCategory.GOD, Text.getText("perkType.recallSpellRadius"), "iconRange", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      REGENERATION_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.regenerationSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      REGENERATION_SPELL_DURATION(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.regenerationSpellDuration"), "iconDuration", PerkModule.PerkDataFormat.SECONDS
      ),
      RESSURECT_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.ressurectSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      RESSURECT_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.ressurectSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      RESURRECT_SPELL_SUCCESS_CHANCE(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.resurrectSpellSuccessChance"), "iconGodXP", PerkModule.PerkDataFormat.PERCENT
      ),
      STORM_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.stormSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      STORM_SPELL_COOLDOWN(PerkModule.PerkCategory.GOD, Text.getText("perkType.stormSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE),
      HOLY_GOLEM_SPELL_COST(PerkModule.PerkCategory.GOD, Text.getText("perkType.holyGolemSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE),
      HOLY_GOLEM_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.holyGolemSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      LABOR_GOLEM_SPELL_COST(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.laborGolemSpellCost"), "iconEnergy", PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      LABOR_GOLEM_SPELL_COOLDOWN(
         PerkModule.PerkCategory.GOD, Text.getText("perkType.laborGolemSpellCooldown"), "iconGodXP", PerkModule.PerkDataFormat.SECONDS_NEGATIVE
      ),
      NOMAD_APPEARANCE_RATE(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.nomadAppearanceRate"), "iconVillager", PerkModule.PerkDataFormat.PERCENT),
      NOMAD_AMOUNT(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.nomadAmount"), "iconVillager", PerkModule.PerkDataFormat.WHOLE_NUMBER),
      ANIMAL_APPEARANCE_RATE(PerkModule.PerkCategory.REGULAR, Text.getText("perkType.animalAppearanceRate"), "iconOwner", PerkModule.PerkDataFormat.PERCENT),
      BETTER_MARKET_PRICES(
         PerkModule.PerkCategory.REGULAR,
         Text.getText("perkType.betterMarketPrices"),
         ResourceModule.ResourceType.GOLD_COIN_SACK,
         PerkModule.PerkDataFormat.PERCENT
      ),
      RESOURCE_DECAY_RATE(
         PerkModule.PerkCategory.REGULAR,
         Text.getText("perkType.resourceDecayRate"),
         ResourceModule.ResourceType.WOOD,
         PerkModule.PerkDataFormat.PERCENT_NEGATIVE
      ),
      MORE_LOOT_IN_LOOT_BOXES(
         PerkModule.PerkCategory.REGULAR,
         Text.getText("perkType.moreLootinLootBoxes"),
         ResourceModule.ResourceType.GOLD_INGOT,
         PerkModule.PerkDataFormat.PERCENT
      ),
      TOOL_DURABILITY(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.toolDurability"), ResourceModule.ResourceType.AXE, PerkModule.PerkDataFormat.PERCENT
      ),
      FOOD_AND_WATER_VALUE(
         PerkModule.PerkCategory.REGULAR, Text.getText("perkType.foodAndWaterValue"), ResourceModule.ResourceType.RATION, PerkModule.PerkDataFormat.PERCENT
      );

      private PerkModule.PerkCategory perkCategory;
      private String perkName;
      private String iconName;
      private ResourceModule.ResourceType resourceIconType;
      private PerkModule.PerkDataFormat dataFormat;

      private PerkType(PerkModule.PerkCategory perkCategory, String perkName, String iconName, PerkModule.PerkDataFormat dataTypeFormat) {
         this.perkCategory = perkCategory;
         this.perkName = perkName;
         this.iconName = iconName;
         this.dataFormat = dataTypeFormat;
      }

      private PerkType(
         PerkModule.PerkCategory perkCategory, String perkName, ResourceModule.ResourceType resourceIconType, PerkModule.PerkDataFormat dataTypeFormat
      ) {
         this.perkCategory = perkCategory;
         this.perkName = perkName;
         this.resourceIconType = resourceIconType;
         this.dataFormat = dataTypeFormat;
      }

      public PerkModule.PerkCategory getPerkCategory() {
         return this.perkCategory;
      }

      public String getPerkName() {
         return this.perkName;
      }

      public String getIconName() {
         return this.iconName;
      }

      public ResourceModule.ResourceType getResourceIconType() {
         return this.resourceIconType;
      }

      public PerkModule.PerkDataFormat getDataFormat() {
         return this.dataFormat;
      }
   }
}
