package rtr.gui.states.playstate;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ImageLoader;
import rtr.font.Text;
import rtr.gui.buttons.GUISquarePanelButton;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.PlayStateGUIData;
import rtr.gui.states.PlayStateGUIPanelBase;
import rtr.influence.SpellBase;

public class SpellSelectPanel extends PlayStateGUIPanelBase {
   private Image panel;
   private Image panelBar;
   private Rectangle mask;
   private HashMap<SpellBase.SpellType, GUISquarePanelButton> spellSelectButtons = new HashMap<>();
   private ArrayList<GUISquarePanelButton> spellSelectButtonsAid = new ArrayList<>();
   private ArrayList<GUISquarePanelButton> spellSelectButtonsDefensive = new ArrayList<>();
   private ArrayList<GUISquarePanelButton> spellSelectButtonsOffensive = new ArrayList<>();
   private ArrayList<GUISquarePanelButton> spellSelectButtonsUtility = new ArrayList<>();
   private HashMap<GUISquarePanelButton, SpellBase.SpellType> spellSelectButtonsReverseLookup = new HashMap<>();
   private GUISquarePanelButton spellSelectDivineBlessing;
   private GUISquarePanelButton spellSelectHarvest;
   private GUISquarePanelButton spellSelectHealingAura;
   private GUISquarePanelButton spellSelectHolyPotatoes;
   private GUISquarePanelButton spellSelectHolyWood;
   private GUISquarePanelButton spellSelectMend;
   private GUISquarePanelButton spellSelectMotivateLand;
   private GUISquarePanelButton spellSelectRegenerate;
   private GUISquarePanelButton spellSelectResurrect;
   private GUISquarePanelButton spellSelectCharm;
   private GUISquarePanelButton spellSelectGodTower;
   private GUISquarePanelButton spellSelectGodWall;
   private GUISquarePanelButton spellSelectSummonHolyGolem;
   private GUISquarePanelButton spellSelectBanish;
   private GUISquarePanelButton spellSelectColdAura;
   private GUISquarePanelButton spellSelectComet;
   private GUISquarePanelButton spellSelectEarthquake;
   private GUISquarePanelButton spellSelectFlame;
   private GUISquarePanelButton spellSelectLightningBolt;
   private GUISquarePanelButton spellSelectMagicBolts;
   private GUISquarePanelButton spellSelectMeteor;
   private GUISquarePanelButton spellSelectConjureEssence;
   private GUISquarePanelButton spellSelectConjureMaterial;
   private GUISquarePanelButton spellSelectConstruct;
   private GUISquarePanelButton spellSelectDispelGodStructure;
   private GUISquarePanelButton spellSelectDispelGolem;
   private GUISquarePanelButton spellSelectDissolve;
   private GUISquarePanelButton spellSelectIlluminate;
   private GUISquarePanelButton spellSelectRecall;
   private GUISquarePanelButton spellSelectStorm;
   private GUISquarePanelButton spellSelectSummonLaborGolem;
   private int spellSelectSlot = 0;

   public SpellSelectPanel(PlayStateGUIController controller, PlayStateGUIData guiData, Graphics g, GameContainer gc, Rectangle mouse) throws SlickException {
      super(controller, guiData, g, gc, mouse);
      this.panel = ImageLoader.getImage("res/GUI/gameState/spellSelectPanel.png");
      this.panelBar = ImageLoader.getImage("res/GUI/gameState/spellSelectPanelBar.png");
      this.mask = new Rectangle(0.0F, 0.0F, this.panel.getWidth(), this.panel.getHeight());
      this.masks.add(this.mask);
      this.spellSelectDivineBlessing = new GUISquarePanelButton("spellSelectDivineBlessing", 2, "spellDivineBlessing");
      this.loadButton(SpellBase.SpellType.DIVINE_BLESSING, this.spellSelectDivineBlessing, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectHarvest = new GUISquarePanelButton("spellSelectHarvest", 2, "spellHarvest");
      this.loadButton(SpellBase.SpellType.HARVEST, this.spellSelectHarvest, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectHealingAura = new GUISquarePanelButton("spellSelectHealingAura", 2, "spellHealingAura");
      this.loadButton(SpellBase.SpellType.HEALING_AURA, this.spellSelectHealingAura, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectHolyPotatoes = new GUISquarePanelButton("spellSelectHolyPotatoes", 2, "spellHolyPotatoes");
      this.loadButton(SpellBase.SpellType.HOLY_POTATOES, this.spellSelectHolyPotatoes, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectHolyWood = new GUISquarePanelButton("spellSelectHolyWood", 2, "spellHolyWood");
      this.loadButton(SpellBase.SpellType.HOLY_WOOD, this.spellSelectHolyWood, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectMend = new GUISquarePanelButton("spellSelectMend", 2, "spellMend");
      this.loadButton(SpellBase.SpellType.MEND, this.spellSelectMend, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectMotivateLand = new GUISquarePanelButton("spellSelectMotivateLand", 2, "spellMotivateLand");
      this.loadButton(SpellBase.SpellType.MOTIVATE_LAND, this.spellSelectMotivateLand, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectRegenerate = new GUISquarePanelButton("spellSelectRegenerate", 2, "spellRegenerate");
      this.loadButton(SpellBase.SpellType.REGENERATE, this.spellSelectRegenerate, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectResurrect = new GUISquarePanelButton("spellSelectResurrect", 2, "spellResurrect");
      this.loadButton(SpellBase.SpellType.RESURRECT, this.spellSelectResurrect, SpellSelectPanel.SelectPanelButtonCategory.AID);
      this.spellSelectCharm = new GUISquarePanelButton("spellSelectCharm", 2, "spellCharm");
      this.loadButton(SpellBase.SpellType.CHARM, this.spellSelectCharm, SpellSelectPanel.SelectPanelButtonCategory.DEFENSIVE);
      this.spellSelectGodTower = new GUISquarePanelButton("spellSelectGodTower", 2, "spellGodTower");
      this.loadButton(SpellBase.SpellType.GOD_TOWER, this.spellSelectGodTower, SpellSelectPanel.SelectPanelButtonCategory.DEFENSIVE);
      this.spellSelectGodWall = new GUISquarePanelButton("spellSelectGodWall", 2, "spellGodWall");
      this.loadButton(SpellBase.SpellType.GOD_WALL, this.spellSelectGodWall, SpellSelectPanel.SelectPanelButtonCategory.DEFENSIVE);
      this.spellSelectSummonHolyGolem = new GUISquarePanelButton("spellSelectSummonHolyGolem", 2, "spellSummonHolyGolem");
      this.loadButton(SpellBase.SpellType.SUMMON_HOLY_GOLEM, this.spellSelectSummonHolyGolem, SpellSelectPanel.SelectPanelButtonCategory.DEFENSIVE);
      this.spellSelectBanish = new GUISquarePanelButton("spellSelectBanish", 2, "spellBanish");
      this.loadButton(SpellBase.SpellType.BANISH, this.spellSelectBanish, SpellSelectPanel.SelectPanelButtonCategory.OFFENSIVE);
      this.spellSelectColdAura = new GUISquarePanelButton("spellSelectColdAura", 2, "spellColdAura");
      this.loadButton(SpellBase.SpellType.COLD_AURA, this.spellSelectColdAura, SpellSelectPanel.SelectPanelButtonCategory.OFFENSIVE);
      this.spellSelectComet = new GUISquarePanelButton("spellSelectComet", 2, "spellComet");
      this.loadButton(SpellBase.SpellType.COMET, this.spellSelectComet, SpellSelectPanel.SelectPanelButtonCategory.OFFENSIVE);
      this.spellSelectEarthquake = new GUISquarePanelButton("spellSelectEarthquake", 2, "spellEarthquake");
      this.loadButton(SpellBase.SpellType.EARTHQUAKE, this.spellSelectEarthquake, SpellSelectPanel.SelectPanelButtonCategory.OFFENSIVE);
      this.spellSelectFlame = new GUISquarePanelButton("spellSelectFlame", 2, "spellFlame");
      this.loadButton(SpellBase.SpellType.FLAME, this.spellSelectFlame, SpellSelectPanel.SelectPanelButtonCategory.OFFENSIVE);
      this.spellSelectLightningBolt = new GUISquarePanelButton("spellSelectLightningBolt", 2, "spellLightningBolt");
      this.loadButton(SpellBase.SpellType.LIGHTNING_BOLT, this.spellSelectLightningBolt, SpellSelectPanel.SelectPanelButtonCategory.OFFENSIVE);
      this.spellSelectMagicBolts = new GUISquarePanelButton("spellSelectMagicBolts", 2, "spellMagicBolts");
      this.loadButton(SpellBase.SpellType.MAGIC_BOLTS, this.spellSelectMagicBolts, SpellSelectPanel.SelectPanelButtonCategory.OFFENSIVE);
      this.spellSelectMeteor = new GUISquarePanelButton("spellSelectMeteor", 2, "spellMeteor");
      this.loadButton(SpellBase.SpellType.METEOR, this.spellSelectMeteor, SpellSelectPanel.SelectPanelButtonCategory.OFFENSIVE);
      this.spellSelectConjureEssence = new GUISquarePanelButton("spellSelectConjureEssence", 2, "spellConjureEssence");
      this.loadButton(SpellBase.SpellType.CONJURE_ESSENCE, this.spellSelectConjureEssence, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectConjureMaterial = new GUISquarePanelButton("spellSelectConjureMaterial", 2, "spellConjureMaterial");
      this.loadButton(SpellBase.SpellType.CONJURE_MATERIAL, this.spellSelectConjureMaterial, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectConstruct = new GUISquarePanelButton("spellSelectConstruct", 2, "spellConstruct");
      this.loadButton(SpellBase.SpellType.CONSTRUCT, this.spellSelectConstruct, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectDispelGodStructure = new GUISquarePanelButton("spellSelectDispelGodStructure", 2, "spellDispelGodStructure");
      this.loadButton(SpellBase.SpellType.DISPEL_GOD_STRUCTURE, this.spellSelectDispelGodStructure, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectDispelGolem = new GUISquarePanelButton("spellSelectDispelGolem", 2, "spellDispelGolem");
      this.loadButton(SpellBase.SpellType.DISPEL_GOLEM, this.spellSelectDispelGolem, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectDissolve = new GUISquarePanelButton("spellSelectDissolve", 2, "spellDissolve");
      this.loadButton(SpellBase.SpellType.DISSOLVE, this.spellSelectDissolve, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectIlluminate = new GUISquarePanelButton("spellSelectIlluminate", 2, "spellIlluminate");
      this.loadButton(SpellBase.SpellType.ILLUMINATE, this.spellSelectIlluminate, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectRecall = new GUISquarePanelButton("spellSelectRecall", 2, "spellRecall");
      this.loadButton(SpellBase.SpellType.RECALL, this.spellSelectRecall, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectStorm = new GUISquarePanelButton("spellSelectStorm", 2, "spellStorm");
      this.loadButton(SpellBase.SpellType.STORM, this.spellSelectStorm, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
      this.spellSelectSummonLaborGolem = new GUISquarePanelButton("spellSelectSummonLaborGolem", 2, "spellSummonLaborGolem");
      this.loadButton(SpellBase.SpellType.SUMMON_LABOR_GOLEM, this.spellSelectSummonLaborGolem, SpellSelectPanel.SelectPanelButtonCategory.UTILITY);
   }

   private void loadButton(SpellBase.SpellType t, GUISquarePanelButton b, SpellSelectPanel.SelectPanelButtonCategory c) {
      this.spellSelectButtons.put(t, b);
      switch (c) {
         case AID:
            this.spellSelectButtonsAid.add(b);
            break;
         case DEFENSIVE:
            this.spellSelectButtonsDefensive.add(b);
            break;
         case OFFENSIVE:
            this.spellSelectButtonsOffensive.add(b);
            break;
         case UTILITY:
            this.spellSelectButtonsUtility.add(b);
      }

      this.spellSelectButtonsReverseLookup.put(b, t);
      this.guiButtons.add(b);
   }

   public void render(boolean debug) throws SlickException {
      this.x = this.controller.getResourceAndInfluenceBar().getLeftX();
      this.y = 151;
      this.mask.setX(this.x);
      this.mask.setY(this.y);
      this.panel.draw(this.x, this.y);
      int alignX = 0;
      int alignY = 0;
      this.panelBar.draw(this.x + 20, this.y + 20 + alignY);
      this.font.drawString(this.x + 25, this.y + 20 + alignY, Text.getText("playSpellSelectPanel.aid"), Text.FontType.BODY, 2);
      alignY += 24;

      for (GUISquarePanelButton b : this.spellSelectButtonsAid) {
         if (alignX >= b.getWidth() * 8) {
            alignX = 0;
            alignY += b.getHeight();
         }

         SpellBase spell = this.influence.getSpellFactory().getSpellInstance(this.spellSelectButtonsReverseLookup.get(b));
         boolean canCast = true;
         if (spell.getCastCost() > 0 || spell.getMaintainCost() > 0) {
            int percentCast = (int)Math.ceil((float)spell.getCastCost() / this.influence.getInfluenceAmountMax() * 100.0);
            int percentMaintain = (int)Math.ceil((float)spell.getMaintainCost() / this.influence.getInfluenceAmountMax() * 100.0);
            if (percentCast > 100 || percentMaintain > 100) {
               canCast = false;
            }
         }

         b.render(this.g, this.mouse, this.x + 20 + alignX, this.y + 20 + alignY, !canCast, debug);
         alignX += b.getWidth();
      }

      alignX = 0;
      alignY += 54;
      this.panelBar.draw(this.x + 20, this.y + 20 + alignY);
      this.font.drawString(this.x + 25, this.y + 20 + alignY, Text.getText("playSpellSelectPanel.defensive"), Text.FontType.BODY, 2);
      alignY += 24;

      for (GUISquarePanelButton b : this.spellSelectButtonsDefensive) {
         if (alignX >= b.getWidth() * 8) {
            alignX = 0;
            alignY += b.getHeight();
         }

         SpellBase spell = this.influence.getSpellFactory().getSpellInstance(this.spellSelectButtonsReverseLookup.get(b));
         boolean canCast = true;
         if (spell.getCastCost() > 0 || spell.getMaintainCost() > 0) {
            int percentCast = (int)Math.ceil((float)spell.getCastCost() / this.influence.getInfluenceAmountMax() * 100.0);
            int percentMaintain = (int)Math.ceil((float)spell.getMaintainCost() / this.influence.getInfluenceAmountMax() * 100.0);
            if (percentCast > 100 || percentMaintain > 100) {
               canCast = false;
            }
         }

         b.render(this.g, this.mouse, this.x + 20 + alignX, this.y + 20 + alignY, !canCast, debug);
         alignX += b.getWidth();
      }

      alignX = 0;
      alignY += 54;
      this.panelBar.draw(this.x + 20, this.y + 20 + alignY);
      this.font.drawString(this.x + 25, this.y + 20 + alignY, Text.getText("playSpellSelectPanel.offensive"), Text.FontType.BODY, 2);
      alignY += 24;

      for (GUISquarePanelButton b : this.spellSelectButtonsOffensive) {
         if (alignX >= b.getWidth() * 8) {
            alignX = 0;
            alignY += b.getHeight();
         }

         SpellBase spell = this.influence.getSpellFactory().getSpellInstance(this.spellSelectButtonsReverseLookup.get(b));
         boolean canCast = true;
         if (spell.getCastCost() > 0 || spell.getMaintainCost() > 0) {
            int percentCast = (int)Math.ceil((float)spell.getCastCost() / this.influence.getInfluenceAmountMax() * 100.0);
            int percentMaintain = (int)Math.ceil((float)spell.getMaintainCost() / this.influence.getInfluenceAmountMax() * 100.0);
            if (percentCast > 100 || percentMaintain > 100) {
               canCast = false;
            }
         }

         b.render(this.g, this.mouse, this.x + 20 + alignX, this.y + 20 + alignY, !canCast, debug);
         alignX += b.getWidth();
      }

      alignX = 0;
      alignY += 54;
      this.panelBar.draw(this.x + 20, this.y + 20 + alignY);
      this.font.drawString(this.x + 25, this.y + 20 + alignY, Text.getText("playSpellSelectPanel.utility"), Text.FontType.BODY, 2);
      alignY += 24;

      for (GUISquarePanelButton b : this.spellSelectButtonsUtility) {
         if (alignX >= b.getWidth() * 8) {
            alignX = 0;
            alignY += b.getHeight();
         }

         SpellBase spell = this.influence.getSpellFactory().getSpellInstance(this.spellSelectButtonsReverseLookup.get(b));
         boolean canCast = true;
         if (spell.getCastCost() > 0 || spell.getMaintainCost() > 0) {
            int percentCast = (int)Math.ceil((float)spell.getCastCost() / this.influence.getInfluenceAmountMax() * 100.0);
            int percentMaintain = (int)Math.ceil((float)spell.getMaintainCost() / this.influence.getInfluenceAmountMax() * 100.0);
            if (percentCast > 100 || percentMaintain > 100) {
               canCast = false;
            }
         }

         b.render(this.g, this.mouse, this.x + 20 + alignX, this.y + 20 + alignY, !canCast, debug);
         alignX += b.getWidth();
      }

      boolean foundSpell = false;

      for (SpellBase.SpellType t : this.spellSelectButtons.keySet()) {
         GUISquarePanelButton b = this.spellSelectButtons.get(t);
         if (b.intersects(this.mouse)) {
            foundSpell = true;
            this.controller.getSpellBar().setHighlightedSpell(t);
         }
      }

      if (!foundSpell) {
         this.controller.getSpellBar().setHighlightedSpell(null);
      }
   }

   public boolean intersectsSpellSelectSpell() {
      for (GUISquarePanelButton b : this.spellSelectButtons.values()) {
         if (b.intersects(this.mouse)) {
            return true;
         }
      }

      return false;
   }

   public SpellBase.SpellType getIntersectingSpellType() {
      for (SpellBase.SpellType t : this.spellSelectButtons.keySet()) {
         if (this.spellSelectButtons.get(t).intersects(this.mouse, true)) {
            return t;
         }
      }

      return null;
   }

   public SpellBase.SpellType setSpellSlot() {
      for (SpellBase.SpellType t : this.spellSelectButtons.keySet()) {
         GUISquarePanelButton b = this.spellSelectButtons.get(t);
         if (b.intersects(this.mouse)) {
            for (int i = 0; i < this.controller.getSpellBar().getActiveSpellBarLength(); i++) {
               SpellBase.SpellType thisActiveSpell = this.influence.getActiveSpell(i);
               if (thisActiveSpell == t && i != this.spellSelectSlot) {
                  this.influence.setActiveSpell(i, this.influence.getActiveSpell(this.spellSelectSlot));
                  break;
               }
            }

            this.influence.setActiveSpell(this.spellSelectSlot, t);
            return t;
         }
      }

      return null;
   }

   public void setSpellSelectSlot(int i) {
      this.spellSelectSlot = i;
   }

   @Override
   public int getCenterX() {
      return this.x + this.panel.getWidth() / 2;
   }

   @Override
   public int getCenterY() {
      return this.y + this.panel.getHeight() / 2;
   }

   @Override
   public int getLeftX() {
      return this.x;
   }

   @Override
   public int getTopY() {
      return this.y;
   }

   @Override
   public int getRightX() {
      return this.x + this.panel.getWidth();
   }

   @Override
   public int getBottomY() {
      return this.y + this.panel.getHeight();
   }

   private static enum SelectPanelButtonCategory {
      AID,
      DEFENSIVE,
      OFFENSIVE,
      UTILITY;
   }
}
