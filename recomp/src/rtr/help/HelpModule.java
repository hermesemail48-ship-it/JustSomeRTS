package rtr.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ModuleBase;
import rtr.ProfileModule;
import rtr.SettingsParser;
import rtr.console.Console;
import rtr.gui.states.GUIControllerBase;
import rtr.gui.states.PlayStateGUIController;
import rtr.gui.states.WorldMapGUIController;
import rtr.help.tips.TipAbandonedBuildings;
import rtr.help.tips.TipAmmo;
import rtr.help.tips.TipBase;
import rtr.help.tips.TipBuildingDamage;
import rtr.help.tips.TipBuildingPriority;
import rtr.help.tips.TipBuildingSlots;
import rtr.help.tips.TipCorruption;
import rtr.help.tips.TipCullisGate;
import rtr.help.tips.TipDecayingResources;
import rtr.help.tips.TipDestroyTerrain;
import rtr.help.tips.TipDismantle;
import rtr.help.tips.TipElders;
import rtr.help.tips.TipEnergy;
import rtr.help.tips.TipEssence;
import rtr.help.tips.TipFarming;
import rtr.help.tips.TipGameSpeed;
import rtr.help.tips.TipGhosts;
import rtr.help.tips.TipHarvestingResources;
import rtr.help.tips.TipLifeExpectancy;
import rtr.help.tips.TipMating;
import rtr.help.tips.TipNoFarms;
import rtr.help.tips.TipNoHomes;
import rtr.help.tips.TipNoTowers;
import rtr.help.tips.TipNoWater;
import rtr.help.tips.TipNomad;
import rtr.help.tips.TipOverharvesting;
import rtr.help.tips.TipPregnant;
import rtr.help.tips.TipRain;
import rtr.help.tips.TipRandomLoadingTip;
import rtr.help.tips.TipRefine;
import rtr.help.tips.TipResouceManagement;
import rtr.help.tips.TipSandbox;
import rtr.help.tips.TipSeasonAutumn;
import rtr.help.tips.TipSeasonSpring;
import rtr.help.tips.TipSeasonSummer;
import rtr.help.tips.TipSeasonWinter;
import rtr.help.tips.TipSnow;
import rtr.help.tips.TipSpeedControls;
import rtr.help.tips.TipTimeDusk;
import rtr.help.tips.TipTrash;
import rtr.help.tips.TipVillageWorkersOverview;
import rtr.help.tips.TipVillagerDehydration;
import rtr.help.tips.TipVillagerFreezing;
import rtr.help.tips.TipVillagerOverheating;
import rtr.help.tips.TipVillagerSleepy;
import rtr.help.tips.TipVillagerStarving;
import rtr.help.tips.TipWallAttacked;
import rtr.help.tips.TipWelcomeCamp;
import rtr.help.tips.TipWelcomeTips;
import rtr.help.tips.TipWorkBuildings;
import rtr.help.tips.TipWorldMapMigration;
import rtr.help.tips.TipWorldMapWelcome;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.gamemodetemplates.GameModeTemplateBase;

public class HelpModule extends ModuleBase {
   private Properties viewedTips;
   private ArrayList<TipBase> activeTips = new ArrayList<>();
   private ArrayList<TipBase> allTips = new ArrayList<>();
   private int currentTip;
   private int tipTick = 0;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.viewedTips = new Properties();
      if (SettingsParser.getTipsEnabled()) {
         if (Game.getCS().getGameMode() == GameModeTemplateBase.GameMode.SANDBOX) {
            this.activeTips.add(new TipSandbox());
         } else {
            this.activeTips.add(new TipWorldMapWelcome());
            this.activeTips.add(new TipWorldMapMigration());
            this.activeTips.add(new TipWelcomeTips());
            this.activeTips.add(new TipWelcomeCamp());
            this.activeTips.add(new TipAbandonedBuildings());
            this.activeTips.add(new TipAmmo());
            this.activeTips.add(new TipBuildingDamage());
            this.activeTips.add(new TipBuildingPriority());
            this.activeTips.add(new TipBuildingSlots());
            this.activeTips.add(new TipCorruption());
            this.activeTips.add(new TipCullisGate());
            this.activeTips.add(new TipDecayingResources());
            this.activeTips.add(new TipDestroyTerrain());
            this.activeTips.add(new TipDismantle());
            this.activeTips.add(new TipElders());
            this.activeTips.add(new TipEnergy());
            this.activeTips.add(new TipEssence());
            this.activeTips.add(new TipFarming());
            this.activeTips.add(new TipGameSpeed());
            this.activeTips.add(new TipGhosts());
            this.activeTips.add(new TipHarvestingResources());
            this.activeTips.add(new TipLifeExpectancy());
            this.activeTips.add(new TipMating());
            this.activeTips.add(new TipNoFarms());
            this.activeTips.add(new TipNoHomes());
            this.activeTips.add(new TipNomad());
            this.activeTips.add(new TipNoTowers());
            this.activeTips.add(new TipNoWater());
            this.activeTips.add(new TipOverharvesting());
            this.activeTips.add(new TipPregnant());
            this.activeTips.add(new TipRain());
            this.activeTips.add(new TipRefine());
            this.activeTips.add(new TipResouceManagement());
            this.activeTips.add(new TipSeasonAutumn());
            this.activeTips.add(new TipSeasonSpring());
            this.activeTips.add(new TipSeasonSummer());
            this.activeTips.add(new TipSeasonWinter());
            this.activeTips.add(new TipSnow());
            this.activeTips.add(new TipSpeedControls());
            this.activeTips.add(new TipTimeDusk());
            this.activeTips.add(new TipTrash());
            this.activeTips.add(new TipVillagerDehydration());
            this.activeTips.add(new TipVillagerFreezing());
            this.activeTips.add(new TipVillagerOverheating());
            this.activeTips.add(new TipVillagerSleepy());
            this.activeTips.add(new TipVillagerStarving());
            this.activeTips.add(new TipVillageWorkersOverview());
            this.activeTips.add(new TipWallAttacked());
            this.activeTips.add(new TipWorkBuildings());
         }

         try {
            int activeProfile = ProfileModule.getCurrentProfileSlot();
            File profileTips = new File("profiles/profile" + activeProfile + "/profileTips.properties");
            InputStream in = new FileInputStream(profileTips);
            this.viewedTips.load(in);
            in.close();
         } catch (IOException var6) {
            System.out.println("Failed to load profile's tips. Ignoring already seen tips.");
         }

         ArrayList<TipBase> toRemove = new ArrayList<>();

         for (TipBase t : this.activeTips) {
            if (this.viewedTips.getProperty(t.getTipName(), "false").equals("true")) {
               toRemove.add(t);
            }
         }

         this.activeTips.removeAll(toRemove);
         this.allTips.addAll(this.activeTips);
      }

      boolean forcedTips = false;

      for (TipBase tx : this.activeTips) {
         if (tx.isForcePopup()) {
            forcedTips = true;
            break;
         }
      }

      if (!forcedTips) {
         this.activeTips.add(new TipRandomLoadingTip());
      }

      if (Boolean.getBoolean("rtr.noTips")) {
         // Automated UI checks: no modal tips.
         this.activeTips.clear();
      }
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.activeTips.clear();
      this.viewedTips.clear();
   }

   public ArrayList<TipBase> getActiveTips() {
      return this.activeTips;
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
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
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void update() throws SlickException {
      boolean hasActiveTip = false;
      if (Game.getCS().getGameMode() == GameModeTemplateBase.GameMode.WORLD_MAP) {
         WorldMapGUIController gui = (WorldMapGUIController)Game.getCS().getGUI();
         if (gui.getTipsPanel().getActiveTip() == null) {
            for (TipBase t : this.activeTips) {
               if (t.isWorldMapTip()) {
                  this.removeTip(t);
                  gui.getTipsPanel().setTip(t);
                  break;
               }
            }
         } else {
            hasActiveTip = true;
         }
      } else {
         PlayStateGUIController gui = (PlayStateGUIController)Game.getCS().getGUI();
         if (gui.getTipsPanel().getActiveTip() == null) {
            for (TipBase tx : this.activeTips) {
               if (tx.isForcePopup()) {
                  this.removeTip(tx);
                  gui.getTipsPanel().setTip(tx);
                  break;
               }
            }
         } else {
            hasActiveTip = true;
         }
      }

      if (!hasActiveTip) {
         this.tipTick++;
         if (this.tipTick > 10) {
            if (this.activeTips.size() > 0) {
               if (this.activeTips.size() <= this.currentTip) {
                  this.currentTip = 0;
               } else {
                  if (Game.getCS().getGameMode() != GameModeTemplateBase.GameMode.WORLD_MAP
                     || Game.getCS().getGameMode() == GameModeTemplateBase.GameMode.WORLD_MAP && this.activeTips.get(this.currentTip).isWorldMapTip()) {
                     this.activeTips.get(this.currentTip).updateTipConditions();
                  }

                  this.currentTip++;
               }
            }

            this.tipTick = 0;
         }
      }

      for (TipBase txx : this.activeTips) {
         txx.update();
      }

      for (TipBase txx : this.allTips) {
         txx.loadVideo();
      }
   }

   public void render(Graphics g, Rectangle mouse, boolean debug) throws SlickException {
      GUIControllerBase gui = Game.getCS().getGUI();
      if (gui instanceof PlayStateGUIController) {
         PlayStateGUIController playGUI = (PlayStateGUIController)gui;
         if (playGUI.getTipsPanel().getActiveTip() == null) {
            for (TipBase t : this.activeTips) {
               t.render(g, mouse, debug);
            }

            for (TipBase t : this.activeTips) {
               t.renderToolTip(g, mouse);
            }
         }
      }
   }

   public TipBase getIntersectingTip(Rectangle mouse) {
      for (TipBase t : this.activeTips) {
         if (t.getTipButton().intersects(mouse)) {
            return t;
         }
      }

      return null;
   }

   public void removeTip(TipBase t) {
      if (!t.doNotSave()) {
         this.viewedTips.setProperty(t.getTipName(), "true");
         this.saveTips();
      }

      this.activeTips.remove(t);
   }

   public void saveTips() {
      try {
         int activeProfile = ProfileModule.getCurrentProfileSlot();
         File profileTips = new File("profiles/profile" + activeProfile + "/profileTips.properties");
         FileOutputStream out = new FileOutputStream(profileTips);
         this.viewedTips.store(out, "tips");
         out.flush();
         out.close();
      } catch (IOException var4) {
         Console.out("Failed to write tips file to profile folder!", true);
      }
   }
}
