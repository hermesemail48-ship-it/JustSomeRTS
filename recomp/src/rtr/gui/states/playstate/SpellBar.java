package rtr.gui.states.playstate;

import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.SettingsParser;
import rtr.font.Text;
import rtr.gui.GUIEnums;
import rtr.gui.buttons.GUIButtonSettingsDots;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.influence.SpellBase;

public class SpellBar extends PlayStateGUIPanelBase {
   private HashMap<SpellBase.SpellType, GUISquarePanelButton> spellButtons = new HashMap<>();
   private GUISquarePanelButton spellGrab;
   private GUISquarePanelButton spellDivineBlessing;
   private GUISquarePanelButton spellHarvest;
   private GUISquarePanelButton spellHealingAura;
   private GUISquarePanelButton spellHolyPotatoes;
   private GUISquarePanelButton spellHolyWood;
   private GUISquarePanelButton spellMend;
   private GUISquarePanelButton spellMotivateLand;
   private GUISquarePanelButton spellRegenerate;
   private GUISquarePanelButton spellResurrect;
   private GUISquarePanelButton spellCharm;
   private GUISquarePanelButton spellGodTower;
   private GUISquarePanelButton spellGodWall;
   private GUISquarePanelButton spellSummonHolyGolem;
   private GUISquarePanelButton spellBanish;
   private GUISquarePanelButton spellColdAura;
   private GUISquarePanelButton spellComet;
   private GUISquarePanelButton spellEarthquake;
   private GUISquarePanelButton spellFlame;
   private GUISquarePanelButton spellLightningBolt;
   private GUISquarePanelButton spellMagicBolts;
   private GUISquarePanelButton spellMeteor;
   private GUISquarePanelButton spellConjureEssence;
   private GUISquarePanelButton spellConjureMaterial;
   private GUISquarePanelButton spellConstruct;
   private GUISquarePanelButton spellDispelGodStructure;
   private GUISquarePanelButton spellDispelGolem;
   private GUISquarePanelButton spellDissolve;
   private GUISquarePanelButton spellIlluminate;
   private GUISquarePanelButton spellRecall;
   private GUISquarePanelButton spellStorm;
   private GUISquarePanelButton spellSummonLaborGolem;
   private GUIButtonSettingsDots[] changeSpell = new GUIButtonSettingsDots[5];
   private GUISquarePanelButton[] activeSpell = new GUISquarePanelButton[5];
   private SpellBase.SpellType highlightedSpell;

   public SpellBar(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.spellGrab = new GUISquarePanelButton("spellGrab", SettingsParser.getKeyGrabSpellName(), 2, "spellGrab");
      this.spellButtons.put(SpellBase.SpellType.GRAB, this.spellGrab);
      this.guiButtons.add(this.spellGrab);
      this.spellDivineBlessing = new GUISquarePanelButton("spellDivineBlessing", 2, "spellDivineBlessing");
      this.spellButtons.put(SpellBase.SpellType.DIVINE_BLESSING, this.spellDivineBlessing);
      this.guiButtons.add(this.spellDivineBlessing);
      this.spellHarvest = new GUISquarePanelButton("spellHarvest", 2, "spellHarvest");
      this.spellButtons.put(SpellBase.SpellType.HARVEST, this.spellHarvest);
      this.guiButtons.add(this.spellHarvest);
      this.spellHealingAura = new GUISquarePanelButton("spellHealingAura", 2, "spellHealingAura");
      this.spellButtons.put(SpellBase.SpellType.HEALING_AURA, this.spellHealingAura);
      this.guiButtons.add(this.spellHealingAura);
      this.spellHolyPotatoes = new GUISquarePanelButton("spellHolyPotatoes", 2, "spellHolyPotatoes");
      this.spellButtons.put(SpellBase.SpellType.HOLY_POTATOES, this.spellHolyPotatoes);
      this.guiButtons.add(this.spellHolyPotatoes);
      this.spellHolyWood = new GUISquarePanelButton("spellHolyWood", 2, "spellHolyWood");
      this.spellButtons.put(SpellBase.SpellType.HOLY_WOOD, this.spellHolyWood);
      this.guiButtons.add(this.spellHolyWood);
      this.spellMend = new GUISquarePanelButton("spellMend", 2, "spellMend");
      this.spellButtons.put(SpellBase.SpellType.MEND, this.spellMend);
      this.guiButtons.add(this.spellMend);
      this.spellMotivateLand = new GUISquarePanelButton("spellMotivateLand", 2, "spellMotivateLand");
      this.spellButtons.put(SpellBase.SpellType.MOTIVATE_LAND, this.spellMotivateLand);
      this.guiButtons.add(this.spellMotivateLand);
      this.spellRegenerate = new GUISquarePanelButton("spellRegenerate", 2, "spellRegenerate");
      this.spellButtons.put(SpellBase.SpellType.REGENERATE, this.spellRegenerate);
      this.guiButtons.add(this.spellRegenerate);
      this.spellResurrect = new GUISquarePanelButton("spellResurrect", 2, "spellResurrect");
      this.spellButtons.put(SpellBase.SpellType.RESURRECT, this.spellResurrect);
      this.guiButtons.add(this.spellResurrect);
      this.spellCharm = new GUISquarePanelButton("spellCharm", 2, "spellCharm");
      this.spellButtons.put(SpellBase.SpellType.CHARM, this.spellCharm);
      this.guiButtons.add(this.spellCharm);
      this.spellGodTower = new GUISquarePanelButton("spellGodTower", 2, "spellGodTower");
      this.spellButtons.put(SpellBase.SpellType.GOD_TOWER, this.spellGodTower);
      this.guiButtons.add(this.spellGodTower);
      this.spellGodWall = new GUISquarePanelButton("spellGodWall", 2, "spellGodWall");
      this.spellButtons.put(SpellBase.SpellType.GOD_WALL, this.spellGodWall);
      this.guiButtons.add(this.spellGodWall);
      this.spellSummonHolyGolem = new GUISquarePanelButton("spellSummonHolyGolem", 2, "spellSummonHolyGolem");
      this.spellButtons.put(SpellBase.SpellType.SUMMON_HOLY_GOLEM, this.spellSummonHolyGolem);
      this.guiButtons.add(this.spellSummonHolyGolem);
      this.spellBanish = new GUISquarePanelButton("spellBanish", 2, "spellBanish");
      this.spellButtons.put(SpellBase.SpellType.BANISH, this.spellBanish);
      this.guiButtons.add(this.spellBanish);
      this.spellColdAura = new GUISquarePanelButton("spellColdAura", 2, "spellColdAura");
      this.spellButtons.put(SpellBase.SpellType.COLD_AURA, this.spellColdAura);
      this.guiButtons.add(this.spellColdAura);
      this.spellComet = new GUISquarePanelButton("spellComet", 2, "spellComet");
      this.spellButtons.put(SpellBase.SpellType.COMET, this.spellComet);
      this.guiButtons.add(this.spellComet);
      this.spellEarthquake = new GUISquarePanelButton("spellEarthquake", 2, "spellEarthquake");
      this.spellButtons.put(SpellBase.SpellType.EARTHQUAKE, this.spellEarthquake);
      this.guiButtons.add(this.spellEarthquake);
      this.spellFlame = new GUISquarePanelButton("spellFlame", 2, "spellFlame");
      this.spellButtons.put(SpellBase.SpellType.FLAME, this.spellFlame);
      this.guiButtons.add(this.spellFlame);
      this.spellLightningBolt = new GUISquarePanelButton("spellLightningBolt", 2, "spellLightningBolt");
      this.spellButtons.put(SpellBase.SpellType.LIGHTNING_BOLT, this.spellLightningBolt);
      this.guiButtons.add(this.spellLightningBolt);
      this.spellMagicBolts = new GUISquarePanelButton("spellMagicBolts", 2, "spellMagicBolts");
      this.spellButtons.put(SpellBase.SpellType.MAGIC_BOLTS, this.spellMagicBolts);
      this.guiButtons.add(this.spellMagicBolts);
      this.spellMeteor = new GUISquarePanelButton("spellMeteor", 2, "spellMeteor");
      this.spellButtons.put(SpellBase.SpellType.METEOR, this.spellMeteor);
      this.guiButtons.add(this.spellMeteor);
      this.spellConjureEssence = new GUISquarePanelButton("spellConjureEssence", 2, "spellConjureEssence");
      this.spellButtons.put(SpellBase.SpellType.CONJURE_ESSENCE, this.spellConjureEssence);
      this.guiButtons.add(this.spellConjureEssence);
      this.spellConjureMaterial = new GUISquarePanelButton("spellConjureMaterial", 2, "spellConjureMaterial");
      this.spellButtons.put(SpellBase.SpellType.CONJURE_MATERIAL, this.spellConjureMaterial);
      this.guiButtons.add(this.spellConjureMaterial);
      this.spellConstruct = new GUISquarePanelButton("spellConstruct", 2, "spellConstruct");
      this.spellButtons.put(SpellBase.SpellType.CONSTRUCT, this.spellConstruct);
      this.guiButtons.add(this.spellConstruct);
      this.spellDispelGodStructure = new GUISquarePanelButton("spellDispelGodStructure", 2, "spellDispelGodStructure");
      this.spellButtons.put(SpellBase.SpellType.DISPEL_GOD_STRUCTURE, this.spellDispelGodStructure);
      this.guiButtons.add(this.spellDispelGodStructure);
      this.spellDispelGolem = new GUISquarePanelButton("spellDispelGolem", 2, "spellDispelGolem");
      this.spellButtons.put(SpellBase.SpellType.DISPEL_GOLEM, this.spellDispelGolem);
      this.guiButtons.add(this.spellDispelGolem);
      this.spellDissolve = new GUISquarePanelButton("spellDissolve", 2, "spellDissolve");
      this.spellButtons.put(SpellBase.SpellType.DISSOLVE, this.spellDissolve);
      this.guiButtons.add(this.spellDissolve);
      this.spellIlluminate = new GUISquarePanelButton("spellIlluminate", 2, "spellIlluminate");
      this.spellButtons.put(SpellBase.SpellType.ILLUMINATE, this.spellIlluminate);
      this.guiButtons.add(this.spellIlluminate);
      this.spellRecall = new GUISquarePanelButton("spellRecall", 2, "spellRecall");
      this.spellButtons.put(SpellBase.SpellType.RECALL, this.spellRecall);
      this.guiButtons.add(this.spellRecall);
      this.spellStorm = new GUISquarePanelButton("spellStorm", 2, "spellStorm");
      this.spellButtons.put(SpellBase.SpellType.STORM, this.spellStorm);
      this.guiButtons.add(this.spellStorm);
      this.spellSummonLaborGolem = new GUISquarePanelButton("spellSummonLaborGolem", 2, "spellSummonLaborGolem");
      this.spellButtons.put(SpellBase.SpellType.SUMMON_LABOR_GOLEM, this.spellSummonLaborGolem);
      this.guiButtons.add(this.spellSummonLaborGolem);

      for (int i = 0; i < this.changeSpell.length; i++) {
         this.changeSpell[i] = new GUIButtonSettingsDots("changeSpell" + i);
         Text.setVariableText("slotNumber", i + 1);
         this.changeSpell[i].setToolTip(Text.getText("playSpellBar.changeSpell.header"), Text.getText("playSpellBar.changeSpell.tooltip"));
         this.guiButtons.add(this.changeSpell[i]);
      }

      for (int i = 0; i < this.activeSpell.length; i++) {
         this.activeSpell[i] = this.spellButtons.get(this.influence.getActiveSpell(i));
      }
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getResourceAndInfluenceBar().getLeftX();
      this.y = 87;
      int alignX = 0;
      this.spellGrab.render(this.g, this.mouse, this.x + alignX, this.y, false, debug);
      alignX += this.spellGrab.getWidth() * 2;

      for (int i = 0; i < this.activeSpell.length; i++) {
         this.activeSpell[i] = this.spellButtons.get(this.influence.getActiveSpell(i));
         if (this.controller.getGUIData().getInterfaceMode() == GUIEnums.InterfaceMode.INFLUENCE
            && this.influence.getSelectedSpell() == this.influence.getActiveSpell(i)) {
            this.activeSpell[i].select();
         } else {
            this.activeSpell[i].deselect();
         }

         SpellBase s = this.influence.getSpellFactory().getSpellInstance(this.influence.getActiveSpell(i));
         int cooldownPercent = (int)Math.ceil((float)this.influence.getSpellCooldown(this.influence.getActiveSpell(i)) / s.getCooldown() * 360.0F);
         SpellBase spell = this.influence.getSpellFactory().getSpellInstance(this.influence.getActiveSpell(i));
         boolean canCast = true;
         if (spell.getCastCost() > 0 || spell.getMaintainCost() > 0) {
            int percentCast = (int)Math.ceil((float)spell.getCastCost() / this.influence.getInfluenceAmountMax() * 100.0);
            int percentMaintain = (int)Math.ceil((float)spell.getMaintainCost() / this.influence.getInfluenceAmountMax() * 100.0);
            if (percentCast > 100 || percentMaintain > 100) {
               canCast = false;
            }
         }

         this.activeSpell[i].render(this.g, this.mouse, this.x + alignX, this.y, !canCast, cooldownPercent, debug);
         this.changeSpell[i].render(this.g, this.mouse, this.x + alignX, this.y + this.activeSpell[i].getHeight() - 4, false, debug);
         alignX += this.activeSpell[i].getWidth();
      }

      this.activeSpell[0].setKey(SettingsParser.getKeyActiveSpell1Name());
      this.activeSpell[1].setKey(SettingsParser.getKeyActiveSpell2Name());
      this.activeSpell[2].setKey(SettingsParser.getKeyActiveSpell3Name());
      this.activeSpell[3].setKey(SettingsParser.getKeyActiveSpell4Name());
      this.activeSpell[4].setKey(SettingsParser.getKeyActiveSpell5Name());
      boolean foundSpell = false;

      for (SpellBase.SpellType t : this.spellButtons.keySet()) {
         GUISquarePanelButton b = this.spellButtons.get(t);
         if (b.intersects(this.mouse)) {
            foundSpell = true;
            this.highlightedSpell = t;
            break;
         }
      }

      if (!foundSpell) {
         this.highlightedSpell = null;
      }
   }

   public boolean intersectsActiveSpell(int i) {
      return this.activeSpell[i].intersects(this.mouse);
   }

   public SpellBase.SpellType getIntersectsActiveSpell(int i) {
      if (this.activeSpell[i].intersects(this.mouse)) {
         SpellBase.SpellType[] var5;
         for (SpellBase.SpellType t : SpellBase.SpellType.values()) {
            if (this.spellButtons.get(t) == this.activeSpell[i]) {
               return t;
            }
         }
      }

      return null;
   }

   public int getActiveSpellBarLength() {
      return this.activeSpell.length;
   }

   public SpellBase.SpellType getHighlightedSpell() {
      return this.highlightedSpell;
   }

   public void setHighlightedSpell(SpellBase.SpellType s) {
      this.highlightedSpell = s;
   }

   public SpellBase.SpellType getIntersectingSpellType() {
      for (SpellBase.SpellType t : this.spellButtons.keySet()) {
         if (this.spellButtons.get(t).intersects(this.mouse, true)) {
            return t;
         }
      }

      return null;
   }

   @Override
   public boolean intersectsGUIMask() {
      return super.intersectsGUIMask() ? true : this.getIntersectingSpellType() != null;
   }
}
