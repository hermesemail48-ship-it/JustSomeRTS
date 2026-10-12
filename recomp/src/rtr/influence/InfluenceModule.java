package rtr.influence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class InfluenceModule extends ModuleBase {
   private static int MINIMUM_INFLUENCE = 200;
   private static int INFLUENCE_GAIN_PERIOD = 40;
   private int influenceAmount;
   private int influenceAmountMax;
   private int influenceAmountUsableMax;
   private int influenceMaintainingAmount;
   private int gainPeriodTick;
   private int grabRotationPeriod = 100;
   private int grabRotationPeriodTick;
   private ResourceModule.ResourceType grabRotationResourceType = ResourceModule.ResourceType.WOOD;
   private MobBase mobGrabbed;
   private int mobGrabbedID = -1;
   private ResourceBase resourceGrabbed;
   private int resourceGrabbedID = -1;
   private int grabDrainTick;
   private SpellBase.SpellType selectedSpell;
   private SpellBase.SpellType[] activeSpell = new SpellBase.SpellType[5];
   private ArrayList<SpellBase> activeSpells = new ArrayList<>();
   private ArrayList<SpellBase> activeSpellsRemoval = new ArrayList<>();
   private SpellFactory spellFactory;
   private HashMap<SpellBase.SpellType, Integer> spellCooldowns = new HashMap<>();
   private boolean showedError;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.spellFactory == null) {
         this.spellFactory = new SpellFactory();
      }

      this.activeSpell[0] = SpellBase.SpellType.METEOR;
      this.activeSpell[1] = SpellBase.SpellType.MOTIVATE_LAND;
      this.activeSpell[2] = SpellBase.SpellType.HEALING_AURA;
      this.activeSpell[3] = SpellBase.SpellType.SUMMON_HOLY_GOLEM;
      this.activeSpell[4] = SpellBase.SpellType.RECALL;

      SpellBase.SpellType[] var6;
      for (SpellBase.SpellType s : SpellBase.SpellType.values()) {
         this.spellCooldowns.put(s, 0);
      }

      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.influenceAmount = 0;
      this.influenceAmountMax = 0;
      this.influenceAmountUsableMax = 0;
      this.influenceMaintainingAmount = 0;
      this.gainPeriodTick = 0;
      this.activeSpell[0] = SpellBase.SpellType.METEOR;
      this.activeSpell[1] = SpellBase.SpellType.MOTIVATE_LAND;
      this.activeSpell[2] = SpellBase.SpellType.HEALING_AURA;
      this.activeSpell[3] = SpellBase.SpellType.SUMMON_HOLY_GOLEM;
      this.activeSpell[4] = SpellBase.SpellType.RECALL;

      SpellBase.SpellType[] var4;
      for (SpellBase.SpellType s : SpellBase.SpellType.values()) {
         this.spellCooldowns.put(s, 0);
      }

      this.activeSpells.clear();
      this.activeSpellsRemoval.clear();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
      this.calculateInfluenceMax();
      this.influenceAmountUsableMax = this.influenceAmountMax;
      this.influenceAmount = this.influenceAmountMax;
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
      YMLDataMap dataMap = this.save.getActiveRegionalSave().getAndUnloadModuleSaveData(ModuleBase.ModuleType.INFLUENCE);
      this.influenceAmount = Integer.parseInt(dataMap.getOrDefault("influenceAmount", 0));
      this.influenceAmountMax = Integer.parseInt(dataMap.getOrDefault("influenceAmountMax", 0));
      this.influenceAmountUsableMax = Integer.parseInt(dataMap.getOrDefault("influenceAmountUsableMax", 0));
      this.influenceMaintainingAmount = Integer.parseInt(dataMap.getOrDefault("influenceMaintainingAmount", 0));
      this.gainPeriodTick = Integer.parseInt(dataMap.getOrDefault("gainPeriodTick", 0));
      this.mobGrabbedID = Integer.parseInt(dataMap.getOrDefault("mobGrabbedID", -1));
      this.resourceGrabbedID = Integer.parseInt(dataMap.getOrDefault("resourceGrabbedID", -1));
      this.grabDrainTick = Integer.parseInt(dataMap.getOrDefault("grabDrainTick", 0));
      YMLDataMap activeSpellDataMap = dataMap.getSubMap("activeSpell");

      for (int i = 0; i < this.activeSpell.length; i++) {
         String activeSpellString = activeSpellDataMap.getOrDefault(Integer.toString(i), SpellBase.SpellType.METEOR.toString());

         SpellBase.SpellType[] var8;
         for (SpellBase.SpellType t : SpellBase.SpellType.values()) {
            if (t.toString().equals(activeSpellString)) {
               this.activeSpell[i] = t;
               break;
            }
         }
      }

      YMLDataMap spellCooldownsMap = dataMap.getSubMap("spellCooldowns");
      if (spellCooldownsMap != null) {
         SpellBase.SpellType[] var13;
         for (SpellBase.SpellType s : SpellBase.SpellType.values()) {
            this.spellCooldowns.put(s, Integer.parseInt(spellCooldownsMap.getOrDefault(s.toString(), 0)));
         }
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public void initLoadSync() {
      this.synced = true;
      if (this.mobGrabbedID != -1) {
         this.mobGrabbed = this.mob.getMobHashMap().get(this.mobGrabbedID);
      }

      if (this.resourceGrabbedID != -1) {
         this.resourceGrabbed = this.resource.getResource(this.resourceGrabbedID);
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.influenceAmount != 0) {
         dataMap.put("influenceAmount", this.influenceAmount);
      }

      if (this.influenceAmountMax != 0) {
         dataMap.put("influenceAmountMax", this.influenceAmountMax);
      }

      if (this.influenceAmountUsableMax != 0) {
         dataMap.put("influenceAmountUsableMax", this.influenceAmountUsableMax);
      }

      if (this.influenceMaintainingAmount != 0) {
         dataMap.put("influenceMaintainingAmount", this.influenceMaintainingAmount);
      }

      if (this.gainPeriodTick != 0) {
         dataMap.put("gainPeriodTick", this.gainPeriodTick);
      }

      if (this.mobGrabbed != null) {
         dataMap.put("mobGrabbedID", this.mobGrabbed.getID());
      }

      if (this.resourceGrabbed != null) {
         dataMap.put("resourceGrabbedID", this.resourceGrabbed.getID());
      }

      if (this.grabDrainTick != 0) {
         dataMap.put("grabDrainTick", this.grabDrainTick);
      }

      Map<String, Object> activeSpellDataMap = new HashMap<>();

      for (int i = 0; i < this.activeSpell.length; i++) {
         activeSpellDataMap.put(Integer.toString(i), this.activeSpell[i] != null ? this.activeSpell[i].toString() : "null");
      }

      dataMap.put("activeSpell", activeSpellDataMap);
      Map<String, Object> spellCooldownsMap = new HashMap<>();

      SpellBase.SpellType[] var7;
      for (SpellBase.SpellType s : SpellBase.SpellType.values()) {
         spellCooldownsMap.put(s.toString(), this.spellCooldowns.get(s));
      }

      dataMap.put("spellCooldowns", spellCooldownsMap);
      return dataMap;
   }

   public void update() throws SlickException {
      if (!StateBase.getInput().isMouseButtonDown(0)) {
         this.showedError = false;
      }

      this.gainPeriodTick++;
      int influenceGainPeriodModified = INFLUENCE_GAIN_PERIOD;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.INCREASED_INFLUENCE_GENERATION) > 0.0) {
         influenceGainPeriodModified = (int)(
            influenceGainPeriodModified - influenceGainPeriodModified * this.perk.getPerkTypeTotal(PerkModule.PerkType.INCREASED_INFLUENCE_GENERATION)
         );
      }

      if (this.gainPeriodTick > influenceGainPeriodModified) {
         int amount = (int)(this.influenceAmountMax * 0.001);
         if (amount < 1) {
            amount = 1;
         }

         this.increaseInfluenceAmount(amount);
         this.gainPeriodTick = 0;
      }

      SpellBase.SpellType[] s;
      for (SpellBase.SpellType sx : s = SpellBase.SpellType.values()) {
         if (this.spellCooldowns.get(sx) > 0) {
            this.spellCooldowns.put(sx, this.spellCooldowns.get(sx) - 1);
         }
      }

      ObjectBase selectedObject = null;
      if (!Utilities.isOutOfBoundsOfUsableMap(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY())) {
         selectedObject = this.object.getBuildingAtLocation(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY());
      }

      if (selectedObject != null) {
         if (this.grabRotationPeriodTick > this.grabRotationPeriod || selectedObject.getResourceCount(this.grabRotationResourceType) <= 0) {
            if (selectedObject.getTotalResourcesCount() > 0) {
               if (this.grabRotationResourceType.ordinal() != ResourceModule.ResourceType.values().length - 1) {
                  this.grabRotationResourceType = ResourceModule.ResourceType.values()[this.grabRotationResourceType.ordinal() + 1];
               } else {
                  this.grabRotationResourceType = ResourceModule.ResourceType.values()[0];
               }

               while (selectedObject.getResourceCount(this.grabRotationResourceType) <= 0) {
                  if (this.grabRotationResourceType.ordinal() != ResourceModule.ResourceType.values().length - 1) {
                     this.grabRotationResourceType = ResourceModule.ResourceType.values()[this.grabRotationResourceType.ordinal() + 1];
                  } else {
                     this.grabRotationResourceType = ResourceModule.ResourceType.values()[0];
                  }
               }
            }

            this.grabRotationPeriodTick = 0;
         }

         this.grabRotationPeriodTick++;
      } else {
         this.grabRotationPeriodTick = -50;
      }

      if (this.mobGrabbed == null && this.resourceGrabbed == null) {
         this.grabDrainTick = 0;
      } else {
         this.grabDrainTick++;
         if (this.grabDrainTick > 120) {
            if (this.grabDrainTick % 2 == 0) {
               this.influenceAmount--;
            }

            if (this.influenceAmount <= 0) {
               if (this.mobGrabbed != null) {
                  this.goal.incrementGoal(GoalModule.GoalType.WHOOPS_SORRY);
               }

               this.forceCastSpell(0, 0, SpellBase.SpellType.DROP);
               this.influenceAmount = 0;
            }
         }
      }

      if (this.activeSpells.size() > 0) {
         for (SpellBase sxx : this.activeSpells) {
            sxx.update();
         }
      }

      if (this.activeSpellsRemoval.size() > 0) {
         for (SpellBase sxx : this.activeSpellsRemoval) {
            this.activeSpells.remove(sxx);
         }

         this.activeSpellsRemoval.clear();
      }

      this.calculateInfluenceMax();
      this.influenceMaintainingAmount = 0;

      for (MobBase m : this.mob.getMobArray()) {
         if (!m.isDead() && m.getMobFlags().getInfluenceMaintainCost() > 0) {
            this.influenceMaintainingAmount = this.influenceMaintainingAmount + m.getMobFlags().getInfluenceMaintainCost();
         }
      }

      for (ObjectBase o : this.object.getObjectsArrayList()) {
         if (o.getObjectFlags().getInfluenceMaintainCost() > 0) {
            this.influenceMaintainingAmount = this.influenceMaintainingAmount + o.getObjectFlags().getInfluenceMaintainCost();
         }
      }

      for (SpellBase sxx : this.activeSpells) {
         if (sxx.getMaintainCost() > 0) {
            this.influenceMaintainingAmount = this.influenceMaintainingAmount + sxx.getMaintainCost();
         }
      }

      this.influenceAmountUsableMax = this.influenceAmountMax;
      this.influenceAmountUsableMax = this.influenceAmountUsableMax - this.influenceMaintainingAmount;
      if (this.influenceAmountUsableMax < 0) {
         this.influenceAmountUsableMax = 0;
      }

      if (this.influenceMaintainingAmount > this.influenceAmountMax) {
         this.influenceMaintainingAmount = this.influenceAmountMax;
      }

      if (this.influenceAmount > this.influenceAmountUsableMax) {
         this.influenceAmount = this.influenceAmountUsableMax;
      }
   }

   public void removeSpell(SpellBase s) {
      this.activeSpellsRemoval.add(s);
   }

   public int getInfluenceAmount() {
      return this.influenceAmount;
   }

   public void setInfluenceAmount(int i) {
      this.influenceAmount = i;
   }

   public void increaseInfluenceAmount(int i) throws SlickException {
      if (this.influenceAmount < this.influenceAmountUsableMax) {
         this.influenceAmount += i;
         this.stats.increaseInfluenceGeneratedThisPeriod(i);
         if (this.influenceAmount > this.influenceAmountUsableMax) {
            this.influenceAmount = this.influenceAmountUsableMax;
         }
      }
   }

   public void decreaseInfluenceAmount(int i) throws SlickException {
      if (this.influenceAmount > 0) {
         this.influenceAmount -= i;
         this.stats.increaseInfluenceSpentThisPeriod(i);
         if (this.influenceAmount < 0) {
            this.influenceAmount = 0;
         }
      }
   }

   public int getInfluenceAmountMax() {
      return this.influenceAmountMax;
   }

   public int getInfluenceAmountUsableMax() {
      return this.influenceAmountUsableMax;
   }

   public int getInfluenceMaintainingAmount() {
      return this.influenceMaintainingAmount;
   }

   public boolean canCastSelectedSpell() throws SlickException {
      return this.canCast(this.selectedSpell);
   }

   public boolean canCast(SpellBase.SpellType spellType) throws SlickException {
      return this.spellFactory.getSpellInstance(spellType).canCast();
   }

   public int getSelectedCastCost() {
      return this.getCastCost(this.selectedSpell);
   }

   public int getCastCost(SpellBase.SpellType spellType) {
      return this.spellFactory.getSpellInstance(spellType).getCastCost();
   }

   public int getSelectedMaintainCost() {
      return this.getMaintainCost(this.selectedSpell);
   }

   public int getMaintainCost(SpellBase.SpellType spellType) {
      return this.spellFactory.getSpellInstance(spellType).getMaintainCost();
   }

   public void castSelectedSpell(int x, int y) throws SlickException {
      this.castSpell(x, y, this.selectedSpell);
   }

   public void castSpell(int x, int y, SpellBase.SpellType spellType) throws SlickException {
      if (this.canCast(spellType)) {
         if (x >= 4096) {
            x = 4095;
         }

         if (x < 0) {
            x = 0;
         }

         if (y >= 4096) {
            y = 4095;
         }

         if (y < 0) {
            y = 0;
         }

         SpellBase s = this.spellFactory.getNewInstance(spellType);
         s.config(x, y, false);
         this.activeSpells.add(s);
         this.spellCooldowns.put(spellType, s.getCooldown());
      } else if (!this.showedError) {
         this.showedError = true;
         if (Utilities.isOutOfBoundsOfUsableMap(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY())) {
            this.font.newFloater(Text.getText("influence.castFail.outOfBounds"), x, y);
         } else if (this.map.getTileId(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY(), 0) == 0) {
            this.font.newFloater(Text.getText("influence.castFail.inVoid"), x, y);
         } else if (this.corruption.isTileCorrupted(Game.getCS().getMouseTileX(), Game.getCS().getMouseTileY())) {
            this.font.newFloater(Text.getText("influence.castFail.inCorruption"), x, y);
         } else if (!this.spellFactory.getSpellInstance(spellType).allowContinuousCast() && this.getSpellCooldown(spellType) > 0) {
            this.font.newFloater(Text.getText("influence.castFail.notCooledDown"), x, y);
         } else if (this.influenceAmountMax - this.influenceMaintainingAmount < this.spellFactory.getSpellInstance(spellType).getMaintainCost()) {
            this.font.newFloater(Text.getText("influence.castFail.canNotMaintain"), x, y);
         } else if (this.influenceAmount < this.spellFactory.getSpellInstance(spellType).getCastCost()) {
            this.font.newFloater(Text.getText("influence.castFail.noInfluence"), x, y);
         }
      }
   }

   public void forceCastSpell(int x, int y, SpellBase.SpellType spellType) {
      if (x >= 4096) {
         x = 4095;
      }

      if (x < 0) {
         x = 0;
      }

      if (y >= 4096) {
         y = 4095;
      }

      if (y < 0) {
         y = 0;
      }

      SpellBase s = this.spellFactory.getNewInstance(spellType);
      s.config(x, y, true);
      this.activeSpells.add(s);
   }

   private void calculateInfluenceMax() {
      ArrayList<MobBase> allMobs = this.mob.getMobArray();
      this.influenceAmountMax = MINIMUM_INFLUENCE;
      if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_MINIMUM_INFLUENCE) > 0.0) {
         this.influenceAmountMax = (int)(this.influenceAmountMax + this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_MINIMUM_INFLUENCE));
      }

      for (MobBase m : allMobs) {
         if (!m.isDead()) {
            MobFlagsBase mobFlags = m.getMobFlags();
            if (mobFlags.getFaithInfluenceFixed() > 0) {
               int influencePerMob = mobFlags.getFaithInfluenceFixed();
               if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_INFLUENCE_PER_ANIMAL) > 0.0) {
                  influencePerMob = (int)(influencePerMob + this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_INFLUENCE_PER_ANIMAL));
               }

               this.influenceAmountMax += influencePerMob;
            }

            if (mobFlags.getFaithInfluenceMinimum() > 0 || mobFlags.getFaithInfluenceMaximum() > 0) {
               int influenceMax = mobFlags.getFaithInfluenceMaximum();
               if (this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_INFLUENCE_PER_VILLAGER) > 0.0) {
                  influenceMax = (int)(influenceMax + this.perk.getPerkTypeTotal(PerkModule.PerkType.MORE_INFLUENCE_PER_VILLAGER));
               }

               double faithPercentage = Utilities.getPercentage(m.getFaith(), m.getFaithMax(), 100.0);
               faithPercentage /= 100.0;
               double finalFaith = influenceMax * faithPercentage;
               if (finalFaith < mobFlags.getFaithInfluenceMinimum()) {
                  finalFaith = mobFlags.getFaithInfluenceMinimum();
               }

               this.influenceAmountMax = (int)(this.influenceAmountMax + influenceMax * faithPercentage);
            }
         }
      }
   }

   public SpellFactory getSpellFactory() {
      return this.spellFactory;
   }

   public MobBase getMobGrabbed() {
      return this.mobGrabbed;
   }

   public void setMobGrabbed(MobBase m) {
      this.mobGrabbed = m;
   }

   public ResourceBase getResourceGrabbed() {
      return this.resourceGrabbed;
   }

   public void setResourceGrabbed(ResourceBase r) {
      this.resourceGrabbed = r;
   }

   public void setSelectedSpell(SpellBase.SpellType s) {
      this.selectedSpell = s;
   }

   public SpellBase.SpellType getSelectedSpell() {
      return this.selectedSpell;
   }

   public int getGrabRotationPeriod() {
      return this.grabRotationPeriod;
   }

   public int getGrabRotationPeriodTick() {
      return this.grabRotationPeriodTick;
   }

   public void resetGrabRotationPeriodTick() {
      this.grabRotationPeriodTick = 0;
   }

   public ResourceModule.ResourceType getGrabRotationResourceType() {
      return this.grabRotationResourceType;
   }

   public SpellBase.SpellType getActiveSpell(int i) {
      return this.activeSpell[i];
   }

   public void setActiveSpell(int i, SpellBase.SpellType s) {
      this.activeSpell[i] = s;
   }

   public int getSpellCooldown(SpellBase.SpellType spellType) {
      return this.spellCooldowns.get(spellType);
   }
}
