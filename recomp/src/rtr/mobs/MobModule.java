package rtr.mobs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.SettingsParser;
import rtr.console.Console;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.map.MapData;
import rtr.mobs.jobs.MobJobBase;
import rtr.mobs.pathfinder.PathFinder;
import rtr.mobs.pathfinder.PathMapper;
import rtr.mobs.spritesheet.MobSprite;
import rtr.mobs.spritesheet.MobSpriteLoader;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.save.SaveConversionUtilities;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;
import rtr.system.ScaleControl;
import rtr.utilities.ArrayFillUtil;
import rtr.utilities.Assignment;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class MobModule extends ModuleBase {
   private MobSpriteLoader mobSpriteLoader;
   private ArrayList<MobBase> mobArray = new ArrayList<>();
   private HashMap<Integer, MobBase> mobMaster = new HashMap<>();
   private ArrayList<MobBase> mobAdd = new ArrayList<>();
   private int mobLastID;
   private static final int NODE_SIZE = 8;
   private ArrayList<MobBase>[][] mobNodes;
   private EnumMap<MobBase.MobType, ArrayList<MobBase>[][]> mobNodesByType;
   private EnumMap<MobBase.MobGroup, ArrayList<MobBase>[][]> mobNodesByGroup;
   private EnumMap<MobBase.MobType, ArrayList<MobBase>> mobsByTypeAll = new EnumMap<>(MobBase.MobType.class);
   private EnumMap<MobBase.MobType, ArrayList<MobBase>> mobsByTypeAlive = new EnumMap<>(MobBase.MobType.class);
   private EnumMap<MobBase.MobType, ArrayList<MobBase>> mobsByTypeDead = new EnumMap<>(MobBase.MobType.class);
   private EnumMap<MobBase.MobGroup, ArrayList<MobBase>> mobsByGroupAll = new EnumMap<>(MobBase.MobGroup.class);
   private EnumMap<MobBase.MobGroup, ArrayList<MobBase>> mobsByGroupAlive = new EnumMap<>(MobBase.MobGroup.class);
   private EnumMap<MobBase.MobGroup, ArrayList<MobBase>> mobsByGroupDead = new EnumMap<>(MobBase.MobGroup.class);
   private EnumMap<MobJobBase.MobJobType, ArrayList<MobBase>> mobsByJobAll = new EnumMap<>(MobJobBase.MobJobType.class);
   private EnumMap<MobJobBase.MobJobType, ArrayList<MobBase>> mobsByJobAlive = new EnumMap<>(MobJobBase.MobJobType.class);
   private EnumMap<MobJobBase.MobJobType, ArrayList<MobBase>> mobsByJobDead = new EnumMap<>(MobJobBase.MobJobType.class);
   private EnumMap<Assignment.AssignmentType, ArrayList<MobBase>> mobsByAssignmentAll = new EnumMap<>(Assignment.AssignmentType.class);
   private EnumMap<Assignment.AssignmentType, ArrayList<MobBase>> mobsByAssignmentAlive = new EnumMap<>(Assignment.AssignmentType.class);
   private EnumMap<Assignment.AssignmentType, ArrayList<MobBase>> mobsByAssignmentDead = new EnumMap<>(Assignment.AssignmentType.class);
   private ArrayList<MobBase> mobsWithDeliveryAssignmentAll = new ArrayList<>();
   private ArrayList<MobBase> mobsWithDeliveryAssignmentAlive = new ArrayList<>();
   private ArrayList<MobBase> mobsWithDeliveryAssignmentDead = new ArrayList<>();
   private int totalAvailableFaith = 0;
   private int maximumAvailableFaith = 0;
   private EnumMap<MobModule.MobNameType, EnumMap<MobBase.MobGender, ArrayList<String>>> names = new EnumMap<>(MobModule.MobNameType.class);
   private EnumMap<MobBase.MobGender, ArrayList<String>> deathMessages = new EnumMap<>(MobBase.MobGender.class);
   private EnumMap<MobBase.MobGender, ArrayList<String>> eerieVesselMessages = new EnumMap<>(MobBase.MobGender.class);
   private byte[][] mobPositionMap;
   private Image[][] pathArrows;
   private PathFinder pathFinder;
   private PathMapper pathMapper;
   private int mainMenuKillCount = 0;
   private MobTwitchData mobTwitchData;
   private boolean debugResetAI;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      if (this.pathFinder == null) {
         this.pathFinder = new PathFinder();
      }

      if (this.pathMapper == null) {
         this.pathMapper = new PathMapper();
      }

      if (this.mobSpriteLoader == null) {
         this.mobSpriteLoader = new MobSpriteLoader();
      }

      this.mobTwitchData = new MobTwitchData(SettingsParser.getTwitchChannel());
      this.loadNames();
      this.loadDeathMessages();
      this.loadEerieVesselMessages();
      if (this.mobPositionMap == null) {
         this.mobPositionMap = new byte[256][256];
      }

      if (this.pathArrows == null) {
         SpriteSheet pathArrowsSheet = new SpriteSheet(ImageLoader.getImage("res/GUI/pathArrows.png"), 16, 16);
         this.pathArrows = new Image[pathArrowsSheet.getHorizontalCount()][pathArrowsSheet.getVerticalCount()];

         for (int x = 0; x < this.pathArrows.length; x++) {
            for (int y = 0; y < this.pathArrows[x].length; y++) {
               this.pathArrows[x][y] = pathArrowsSheet.getSprite(x, y);
            }
         }
      }

      if (this.mobNodes == null) {
         this.mobNodes = new ArrayList[32][32];

         for (int x = 0; x < this.mobNodes.length; x++) {
            for (int y = 0; y < this.mobNodes[x].length; y++) {
               this.mobNodes[x][y] = new ArrayList<>();
            }
         }

         this.mobNodesByType = new EnumMap<>(MobBase.MobType.class);

         MobBase.MobType[] var6;
         for (MobBase.MobType t : MobBase.MobType.values()) {
            this.mobNodesByType.put(t, new ArrayList[32][32]);

            for (int x = 0; x < this.mobNodes.length; x++) {
               for (int y = 0; y < this.mobNodes[x].length; y++) {
                  this.mobNodesByType.get(t)[x][y] = new ArrayList();
               }
            }
         }

         this.mobNodesByGroup = new EnumMap<>(MobBase.MobGroup.class);

         for (MobBase.MobGroup g : MobBase.MobGroup.values()) {
            this.mobNodesByGroup.put(g, new ArrayList[32][32]);

            for (int x = 0; x < this.mobNodes.length; x++) {
               for (int y = 0; y < this.mobNodes[x].length; y++) {
                  this.mobNodesByGroup.get(g)[x][y] = new ArrayList();
               }
            }
         }
      }

      MobFactory.initialize();
      this.initMobArrays();
      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.mobArray.clear();
      this.mobMaster.clear();
      this.mobsByTypeAll.clear();
      this.mobsByTypeAlive.clear();
      this.mobsByTypeDead.clear();
      this.mobsByGroupAll.clear();
      this.mobsByGroupAlive.clear();
      this.mobsByGroupDead.clear();
      this.mobsByJobAll.clear();
      this.mobsByJobAlive.clear();
      this.mobsByJobDead.clear();
      this.mobsByAssignmentAll.clear();
      this.mobsByAssignmentAlive.clear();
      this.mobsByAssignmentDead.clear();
      this.mobsWithDeliveryAssignmentAll.clear();
      this.mobsWithDeliveryAssignmentAlive.clear();
      this.mobsWithDeliveryAssignmentDead.clear();
      this.mobAdd.clear();
      this.mobLastID = 0;

      for (int x = 0; x < this.mobNodes.length; x++) {
         for (int y = 0; y < this.mobNodes[x].length; y++) {
            this.mobNodes[x][y].clear();
         }
      }

      MobBase.MobType[] var4;
      for (MobBase.MobType t : MobBase.MobType.values()) {
         for (int x = 0; x < this.mobNodes.length; x++) {
            for (int y = 0; y < this.mobNodes[x].length; y++) {
               this.mobNodesByType.get(t)[x][y].clear();
            }
         }
      }

      for (MobBase.MobGroup g : MobBase.MobGroup.values()) {
         for (int x = 0; x < this.mobNodes.length; x++) {
            for (int y = 0; y < this.mobNodes[x].length; y++) {
               this.mobNodesByGroup.get(g)[x][y].clear();
            }
         }
      }

      ArrayFillUtil.fill(this.mobPositionMap, (byte)0);
   }

   @Override
   protected void newMainMenu() throws SlickException {
      for (int x = 0; x < 512; x++) {
         int randomX = Utilities.randomInt(256);
         int randomY = Utilities.randomInt(256);
         if (this.map.getMapData().getBlockMap(MapData.BlockMapGroup.FIRE_ELEMENTALS)[randomX][randomY] <= 0) {
            int randomMob = Utilities.randomInt(8);
            switch (randomMob) {
               case 0:
                  this.newMob(randomX, randomY, MobBase.MobType.ZOMBIE, false);
                  break;
               case 1:
                  this.newMob(randomX, randomY, MobBase.MobType.ZOMBIE_CHILD, false);
                  break;
               case 2:
                  this.newMob(randomX, randomY, MobBase.MobType.SKELETON, false);
                  break;
               case 3:
                  this.newMob(randomX, randomY, MobBase.MobType.SLIME, false);
                  break;
               case 4:
                  this.newMob(randomX, randomY, MobBase.MobType.HEADLESS, false);
                  break;
               case 5:
                  this.newMob(randomX, randomY, MobBase.MobType.SPECTER, false);
                  break;
               case 6:
                  this.newMob(randomX, randomY, MobBase.MobType.SPECTER_CHILD, false);
                  break;
               case 7:
                  if (Utilities.randomInt(10) == 0) {
                     this.newMob(randomX, randomY, MobBase.MobType.FIRE_ELEMENTAL, false);
                  }
            }
         }
      }

      this.updateMobSortData();
   }

   @Override
   protected void newPlay() throws SlickException {
      EnumMap<MobBase.MobType, MobBase> mobInstances = MobFactory.getMobInstanceList();
      if (this.save.getActiveRegionalSave().getGameType() == StateBase.GameType.SKIRMISH) {
         int randomX = Utilities.randomInt(256);
         int randomY = Utilities.randomInt(256);

         for (MobFlagsBase m = mobInstances.get(MobBase.MobType.HUMAN_VILLAGER).getMobFlags();
            this.map.getMapData().getBlockMap(m.getBlockMapGroup())[randomX][randomY] > 0
               || this.map.getTileId(randomX, randomY, 6) > 0
               || this.map.getTileId(randomX, randomY, 9) > 0
               || this.pathFinder.getCoordinatesInRange(randomX, randomY, 32, false, m.getBlockMapGroup()).size() < 64;
            randomY = Utilities.randomInt(256)
         ) {
            randomX = Utilities.randomInt(256);
         }

         int startVillagers = Utilities.randomInt(-2, 2) + 20;

         for (int x = 0; x < startVillagers; x++) {
            this.newMob(randomX, randomY, MobBase.MobType.HUMAN_VILLAGER, false);
         }

         this.map.setMapPosition(randomX * 16 * -1 + ScaleControl.getWorldWidth() / 2, randomY * 16 * -1 + ScaleControl.getWorldHeight() / 2);
      }

      this.updateMobSortData();
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
      this.mobLastID = Integer.parseInt(dataMap.getOrDefault("mobLastID", 0));
      if (!dataMap.getOrDefault("version", "Unknown").equals(Game.getVersion())) {
         Console.saveConverterOut(this, dataMap.getOrDefault("version", "Unknown"), Game.getVersion(), "Old patch detected, resetting AI.");
         this.debugResetAI = true;
      }

      for (int count = 0; dataMap.containsKey("mob" + count); count++) {
         YMLDataMap thisMap = dataMap.getSubMap("mob" + count);
         MobBase.MobType type = null;
         String typeString = thisMap.getOrDefault("mobType", "null");
         typeString = SaveConversionUtilities.mobTypeConversion(this, typeString);

         MobBase.MobType[] var9;
         for (MobBase.MobType t : MobBase.MobType.values()) {
            if (t.toString().equals(typeString)) {
               type = t;
               break;
            }
         }

         if (type != null) {
            this.loadMob(thisMap, type);
         }
      }

      this.updateMobSortData();
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public void initLoadSync() throws SlickException {
      this.synced = true;

      for (int e = 0; e < this.mobArray.size(); e++) {
         this.mobArray.get(e).loadSync();
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      this.updateMobSortData();
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());
      if (this.mobArray.size() == 0) {
         dataMap.put("mobLastID", 0);
      } else {
         int count = 0;

         for (MobBase m : this.mobArray) {
            dataMap.put("mob" + count, m.getRegionalSaveData());
            count++;
         }

         dataMap.put("mobLastID", this.mobLastID);
      }

      return dataMap;
   }

   public void update(int mouseTileX, int mouseTileY) throws SlickException {
      boolean updateRingAlpha = false;
      if (this.debugResetAI) {
         for (MobBase m : this.mobArray) {
            m.leaveBuilding();
            m.interruptNode(m.getAIStart());
         }

         this.debugResetAI = false;
      }

      for (int i = this.mobArray.size() - 1; i >= 0; i--) {
         MobBase m = this.mobArray.get(i);
         m.update();
         updateRingAlpha = Utilities.coordinatesWithin(mouseTileX, mouseTileY, m.getTileX(), m.getTileY(), 6);
         m.updateRingAlpha(updateRingAlpha);
      }

      for (int i = 0; i < this.mobPositionMap.length; i++) {
         Arrays.fill(this.mobPositionMap[i], (byte)0);
      }

      for (MobBase m : this.mobArray) {
         this.mobPositionMap[m.getTileX()][m.getTileY()] = 1;
      }

      EnumMap<MobBase.MobType, MobBase> mobInstances = MobFactory.getMobInstanceList();

      for (MobBase.MobType t : mobInstances.keySet()) {
         mobInstances.get(t).setPosition(MobBase.MobPosition.WEST);
         mobInstances.get(t).setPositionWalk();
         mobInstances.get(t).getSprite().update();
      }

      this.updateMobSortData();
      this.mobTwitchData.update();
      this.pathMapper.update();
   }

   public void render(Graphics g, boolean paused, boolean hideUnderlay) throws SlickException {
      Collections.sort(this.mobArray, new Comparator<MobBase>() {
         public int compare(MobBase m1, MobBase m2) {
            return m1 == m2 ? 0 : (int)m1.getMobY() - (int)m2.getMobY();
         }
      });
      if (this.mobArray.size() > 0) {
         if (!hideUnderlay) {
            this.mobArray.get(0).getMobRing().startUse();

            for (MobBase m : this.mobArray) {
               m.renderUnderlay();
            }

            this.mobArray.get(0).getMobRing().endUse();
         }

         for (MobBase m : this.mobArray) {
            m.render(g, paused);
         }
      }
   }

   public void renderMapWriter() throws SlickException {
      Collections.sort(this.mobArray, new Comparator<MobBase>() {
         public int compare(MobBase m1, MobBase m2) {
            return m1 == m2 ? 0 : (int)m1.getMobY() - (int)m2.getMobY();
         }
      });
      if (this.mobArray.size() > 0) {
         for (MobBase m : this.mobArray) {
            m.renderMapWriter();
         }
      }
   }

   public void renderOverlay(Graphics g, boolean debug) throws SlickException {
      for (int x = 0; x < this.mobArray.size(); x++) {
         this.mobArray.get(x).renderOverlay(g, debug);
      }
   }

   public void renderMobPath(MobBase m) {
      ArrayList<OrderedPair> path = m.getActiveNode().getPathCoordinatesList();
      this.pathArrows[0][0].startUse();
      if (path != null && path.size() > 0) {
         for (int p = m.getActiveNode().getPathStep() - 1; p >= 0; p--) {
            float x = this.map.getMapX() + path.get(p).getX() * 16;
            float y = this.map.getMapY() + path.get(p).getY() * 16;
            Color colorOut = Color.green.brighter(3.0F);
            if (!m.getMobJobType().getJob().isNotCounted()) {
               colorOut = m.getMobJobType().getJob().getBaseColor().brighter(3.0F);
               colorOut.a = 0.5F;
            }

            if (p == 0) {
               this.pathArrows[1][1].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, Color.green);
            } else {
               OrderedPair pathCurrent = path.get(p);
               OrderedPair pathNext = path.get(p - 1);
               if (pathCurrent.getX() > pathNext.getX() && pathCurrent.getY() > pathNext.getY()) {
                  this.pathArrows[0][0].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
               } else if (pathCurrent.getX() < pathNext.getX() && pathCurrent.getY() < pathNext.getY()) {
                  this.pathArrows[2][2].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
               } else if (pathCurrent.getX() > pathNext.getX() && pathCurrent.getY() < pathNext.getY()) {
                  this.pathArrows[0][2].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
               } else if (pathCurrent.getX() < pathNext.getX() && pathCurrent.getY() > pathNext.getY()) {
                  this.pathArrows[2][0].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
               } else if (pathCurrent.getX() < pathNext.getX() && pathCurrent.getY() == pathNext.getY()) {
                  this.pathArrows[2][1].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
               } else if (pathCurrent.getX() > pathNext.getX() && pathCurrent.getY() == pathNext.getY()) {
                  this.pathArrows[0][1].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
               } else if (pathCurrent.getX() == pathNext.getX() && pathCurrent.getY() < pathNext.getY()) {
                  this.pathArrows[1][2].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
               } else if (pathCurrent.getX() == pathNext.getX() && pathCurrent.getY() > pathNext.getY()) {
                  this.pathArrows[1][0].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, colorOut);
               } else {
                  this.pathArrows[1][1].drawEmbedded(x, y, x + 16.0F, y + 16.0F, 0.0F, 0.0F, 16.0F, 16.0F, Color.red);
               }
            }
         }
      }

      this.pathArrows[0][0].endUse();
   }

   private void initMobArrays() {
      MobBase.MobType[] var4;
      for (MobBase.MobType t : MobBase.MobType.values()) {
         this.mobsByTypeAll.put(t, new ArrayList<>());
         this.mobsByTypeAlive.put(t, new ArrayList<>());
         this.mobsByTypeDead.put(t, new ArrayList<>());
      }

      for (MobBase.MobGroup g : MobBase.MobGroup.values()) {
         this.mobsByGroupAll.put(g, new ArrayList<>());
         this.mobsByGroupAlive.put(g, new ArrayList<>());
         this.mobsByGroupDead.put(g, new ArrayList<>());
      }

      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         this.mobsByJobAll.put(t, new ArrayList<>());
         this.mobsByJobAlive.put(t, new ArrayList<>());
         this.mobsByJobDead.put(t, new ArrayList<>());
      }

      for (Assignment.AssignmentType t : Assignment.AssignmentType.values()) {
         this.mobsByAssignmentAll.put(t, new ArrayList<>());
         this.mobsByAssignmentAlive.put(t, new ArrayList<>());
         this.mobsByAssignmentDead.put(t, new ArrayList<>());
      }

      this.mobsWithDeliveryAssignmentAll = new ArrayList<>();
      this.mobsWithDeliveryAssignmentAlive = new ArrayList<>();
      this.mobsWithDeliveryAssignmentDead = new ArrayList<>();
   }

   private void updateMobSortData() {
      if (this.mobAdd.size() > 0) {
         for (int e = 0; e < this.mobAdd.size(); e++) {
            this.mobArray.add(this.mobAdd.get(e));
            this.mobMaster.put(this.mobAdd.get(e).getID(), this.mobAdd.get(e));
         }

         this.mobAdd.clear();
      }

      for (int x = 0; x < this.mobNodes.length; x++) {
         for (int y = 0; y < this.mobNodes[x].length; y++) {
            this.mobNodes[x][y].clear();
         }
      }

      MobBase.MobType[] nodeY;
      for (MobBase.MobType t : nodeY = MobBase.MobType.values()) {
         ArrayList[][] mobNodeByType = this.mobNodesByType.get(t);

         for (int x = 0; x < mobNodeByType.length; x++) {
            for (int y = 0; y < mobNodeByType[x].length; y++) {
               mobNodeByType[x][y].clear();
            }
         }

         this.mobsByTypeAll.get(t).clear();
         this.mobsByTypeAlive.get(t).clear();
         this.mobsByTypeDead.get(t).clear();
      }

      for (MobBase.MobGroup g : MobBase.MobGroup.values()) {
         ArrayList[][] mobNodeByGroup = this.mobNodesByGroup.get(g);

         for (int x = 0; x < mobNodeByGroup.length; x++) {
            for (int y = 0; y < mobNodeByGroup[x].length; y++) {
               mobNodeByGroup[x][y].clear();
            }
         }

         this.mobsByGroupAll.get(g).clear();
         this.mobsByGroupAlive.get(g).clear();
         this.mobsByGroupDead.get(g).clear();
      }

      for (MobJobBase.MobJobType t : MobJobBase.MobJobType.values()) {
         this.mobsByJobAll.get(t).clear();
         this.mobsByJobAlive.get(t).clear();
         this.mobsByJobDead.get(t).clear();
      }

      for (Assignment.AssignmentType t : Assignment.AssignmentType.values()) {
         this.mobsByAssignmentAll.get(t).clear();
         this.mobsByAssignmentAlive.get(t).clear();
         this.mobsByAssignmentDead.get(t).clear();
      }

      this.mobsWithDeliveryAssignmentAll.clear();
      this.mobsWithDeliveryAssignmentAlive.clear();
      this.mobsWithDeliveryAssignmentDead.clear();
      this.totalAvailableFaith = 0;
      this.maximumAvailableFaith = 0;

      for (MobBase m : this.mobArray) {
         int nodeX = m.getTileX() / 8;
         int nodeYx = m.getTileY() / 8;
         this.mobNodes[nodeX][nodeYx].add(m);
         this.mobNodesByType.get(m.getMobType())[nodeX][nodeYx].add(m);
         this.mobNodesByGroup.get(m.getMobGroup())[nodeX][nodeYx].add(m);
         this.mobsByTypeAll.get(m.getMobType()).add(m);
         this.mobsByGroupAll.get(m.getMobGroup()).add(m);
         this.mobsByJobAll.get(m.getMobJobType()).add(m);
         if (!m.isDead()) {
            this.mobsByTypeAlive.get(m.getMobType()).add(m);
            this.mobsByGroupAlive.get(m.getMobGroup()).add(m);
            this.mobsByJobAlive.get(m.getMobJobType()).add(m);
         } else {
            this.mobsByTypeDead.get(m.getMobType()).add(m);
            this.mobsByGroupDead.get(m.getMobGroup()).add(m);
            this.mobsByJobDead.get(m.getMobJobType()).add(m);
         }

         if (m.getMobFlags().getAllowFaith()) {
            this.totalAvailableFaith = this.totalAvailableFaith + m.getFaith();
            this.maximumAvailableFaith = this.maximumAvailableFaith + m.getFaithMax();
         }

         this.addWorkAssigned(m);
      }
   }

   public void removeWorkAssigned(MobBase m) {
      if (m.getAssignment() != null && m.getAssignment().getAssignmentType() != null) {
         this.mobsByAssignmentAll.get(m.getAssignment().getAssignmentType()).remove(m);
         if (!m.isDead()) {
            this.mobsByAssignmentAlive.get(m.getAssignment().getAssignmentType()).remove(m);
         } else {
            this.mobsByAssignmentDead.get(m.getAssignment().getAssignmentType()).remove(m);
         }
      }
   }

   public void addWorkAssigned(MobBase m) {
      if (m.getAssignment() != null && m.getAssignment().getAssignmentType() != null) {
         this.mobsByAssignmentAll.get(m.getAssignment().getAssignmentType()).add(m);
         if (!m.isDead()) {
            this.mobsByAssignmentAlive.get(m.getAssignment().getAssignmentType()).add(m);
         } else {
            this.mobsByAssignmentDead.get(m.getAssignment().getAssignmentType()).add(m);
         }

         if (m.getAssignment().getDeliveryObject() != null) {
            this.mobsWithDeliveryAssignmentAll.add(m);
            if (!m.isDead()) {
               this.mobsWithDeliveryAssignmentAlive.add(m);
            } else {
               this.mobsWithDeliveryAssignmentDead.add(m);
            }
         }
      }
   }

   private void loadNames() {
      MobModule.MobNameType[] var4;
      for (MobModule.MobNameType t : MobModule.MobNameType.values()) {
         ArrayList<String> maleNames = new ArrayList<>();
         ArrayList<String> femaleNames = new ArrayList<>();

         try {
            FileInputStream file = new FileInputStream("res/mobNames/" + t.toString().toLowerCase() + "NamesList.txt");
            this.names.put(t, new EnumMap<>(MobBase.MobGender.class));
            BufferedReader reader = new BufferedReader(new InputStreamReader(file));

            String name;
            while ((name = reader.readLine()) != null) {
               if (SettingsParser.getDebug()) {
                  if (name.startsWith("M ") && maleNames.contains(name.substring(2))) {
                     System.out.println("WARNING: " + name.substring(2) + " already exists in the MALE database.");
                  }

                  if (name.startsWith("F ") && femaleNames.contains(name.substring(2))) {
                     System.out.println("WARNING: " + name.substring(2) + " already exists in the FEMALE database.");
                  }
               }

               String fixedName = name.replaceAll("[^A-Za-z0-9 ']", "");
               if (!name.equals(fixedName) && !name.contains("--")) {
                  System.out.println("Invalid name " + name + " changed to " + fixedName);
               }

               if (fixedName.startsWith("M ")) {
                  maleNames.add(fixedName.substring(2));
               } else if (fixedName.startsWith("F ")) {
                  femaleNames.add(fixedName.substring(2));
               } else if (fixedName.startsWith("A ")) {
                  maleNames.add(fixedName.substring(2));
                  femaleNames.add(fixedName.substring(2));
               }
            }

            this.names.get(t).put(MobBase.MobGender.MALE, maleNames);
            this.names.get(t).put(MobBase.MobGender.FEMALE, femaleNames);
            file.close();
         } catch (IOException var11) {
            var11.printStackTrace();
         }
      }
   }

   public String getRandomName(MobModule.MobNameType t, MobBase.MobGender g) {
      return this.names.get(t).get(g).get(Utilities.randomInt(this.names.get(t).get(g).size()));
   }

   private void loadDeathMessages() {
      MobBase.MobGender[] var4;
      for (MobBase.MobGender g : MobBase.MobGender.values()) {
         this.deathMessages.put(g, new ArrayList<>());
         int position = 0;

         while (!Text.getText("console.banner.mobAI.death.randomSubtitles." + position).contains("console.banner")) {
            String message = Text.getText("console.banner.mobAI.death.randomSubtitles." + position);
            position++;
            if (message.contains("(")) {
               int start = -1;
               int end = -1;

               for (int i = 0; i < message.length(); i++) {
                  char c = message.charAt(i);
                  if (c == '(') {
                     start = i;
                  }

                  if (c == ')') {
                     end = i;
                  }

                  if (start >= 0 && end >= 0) {
                     String genderList = message.substring(start + 1, end);
                     String[] genderSplit = genderList.split(":");
                     message = message.replace("(" + genderList + ")", genderSplit[g.ordinal()]);
                     start = -1;
                     end = -1;
                     i = 0;
                  }
               }
            }

            this.deathMessages.get(g).add(message);
         }
      }
   }

   private void loadEerieVesselMessages() {
      MobBase.MobGender[] var4;
      for (MobBase.MobGender g : MobBase.MobGender.values()) {
         this.eerieVesselMessages.put(g, new ArrayList<>());
         int position = 0;

         while (!Text.getText("console.banner.mobAI.sellEerieVessel.randomSubtitles." + position).contains("console.banner")) {
            String message = Text.getText("console.banner.mobAI.sellEerieVessel.randomSubtitles." + position);
            position++;
            if (message.contains("(")) {
               int start = -1;
               int end = -1;

               for (int i = 0; i < message.length(); i++) {
                  char c = message.charAt(i);
                  if (c == '(') {
                     start = i;
                  }

                  if (c == ')') {
                     end = i;
                  }

                  if (start >= 0 && end >= 0) {
                     String genderList = message.substring(start + 1, end);
                     String[] genderSplit = genderList.split(":");
                     message = message.replace("(" + genderList + ")", genderSplit[g.ordinal()]);
                     start = -1;
                     end = -1;
                     i = 0;
                  }
               }
            }

            this.eerieVesselMessages.get(g).add(message);
         }
      }
   }

   public String getRandomDeathMessage(MobBase.MobGender g) {
      return this.deathMessages.get(g).get(Utilities.randomInt(this.deathMessages.get(g).size()));
   }

   public String getRandomEerieVesselMessage(MobBase.MobGender g) {
      return this.eerieVesselMessages.get(g).get(Utilities.randomInt(this.eerieVesselMessages.get(g).size()));
   }

   public MobBase newMob(float x, float y, MobBase.MobType type, boolean createReachMap) throws SlickException {
      int nextID = this.getNextMobID();
      MobBase newMob = MobFactory.getNewInstance(type);
      newMob.configNew(nextID, x * 16.0F, y * 16.0F, createReachMap);

      for (int i = 0; i < 3; i++) {
         this.projectile.newEnergyRing(newMob.getCenterPixelX(true), newMob.getCenterPixelY(true), 6, ParticleModule.ParticleSet.MAGIC_BLUE);
      }

      for (int i = 0; i < 10; i++) {
         this.particle
            .newParticleGroup(
               newMob.getCenterPixelX(true),
               newMob.getCenterPixelY(true),
               ParticleModule.ParticleGroup.FLOATING_MAGIC_SQUARES,
               ParticleModule.ParticleSet.MAGIC_BLUE
            );
         this.particle
            .newParticleGroup(
               newMob.getCenterPixelX(true), newMob.getCenterPixelY(true), ParticleModule.ParticleGroup.SMOKE_BELLOW, ParticleModule.ParticleSet.MAGIC_BLUE
            );
      }

      if (newMob.getMobGroup() == MobBase.MobGroup.MONSTER) {
         this.stats.increaseMonstersSpawnedThisPeriod(type);
      }

      this.mobAdd.add(newMob);
      return newMob;
   }

   public void loadMob(YMLDataMap dataMap, MobBase.MobType type) throws SlickException {
      MobBase newMob = MobFactory.getNewInstance(type);
      newMob.loadMob(dataMap);
      this.mobAdd.add(newMob);
   }

   public MobBase spawnMob(MobBase mob, MobBase.MobType type, boolean deleteOriginal) throws SlickException {
      return this.spawnMob(mob, type, null, deleteOriginal);
   }

   public MobBase spawnMob(MobBase originalMob, MobBase.MobType type, String message, boolean deleteOriginal) throws SlickException {
      int nextID = this.getNextMobID();
      MobBase newMob = MobFactory.getNewInstance(type);
      newMob.convertInto(nextID, originalMob, message);
      this.mobAdd.add(newMob);
      if (originalMob.getInventoryHeadSlot() != null) {
         newMob.equipResource(originalMob.getInventoryHeadSlot(), ResourceModule.ResourceSlot.HEAD);
      }

      if (originalMob.getInventoryChestSlot() != null) {
         newMob.equipResource(originalMob.getInventoryChestSlot(), ResourceModule.ResourceSlot.CHEST);
      }

      if (originalMob.getInventoryToolSlot() != null) {
         newMob.equipResource(originalMob.getInventoryToolSlot(), ResourceModule.ResourceSlot.TOOL);
      }

      if (originalMob.getInventoryMeleeWeaponSlot() != null) {
         newMob.equipResource(originalMob.getInventoryMeleeWeaponSlot(), ResourceModule.ResourceSlot.MELEE_WEAPON);
      }

      if (originalMob.getInventoryShieldSlot() != null) {
         newMob.equipResource(originalMob.getInventoryShieldSlot(), ResourceModule.ResourceSlot.SHIELD);
      }

      if (originalMob.getInventoryRecoverySlot() != null) {
         newMob.equipResource(originalMob.getInventoryRecoverySlot(), ResourceModule.ResourceSlot.RECOVERY);
      }

      if (originalMob.getInventoryWaterSlot() != null) {
         newMob.equipResource(originalMob.getInventoryWaterSlot(), ResourceModule.ResourceSlot.WATER);
      }

      if (originalMob.getInventoryFoodSlot() != null) {
         newMob.equipResource(originalMob.getInventoryFoodSlot(), ResourceModule.ResourceSlot.FOOD);
      }

      if (originalMob.getInventoryRangedWeaponSlot() != null) {
         newMob.equipResource(originalMob.getInventoryRangedWeaponSlot(), ResourceModule.ResourceSlot.RANGED_WEAPON);
      }

      if (originalMob.getInventoryRangedWeaponAmmoSlot() != null) {
         newMob.equipResource(originalMob.getInventoryRangedWeaponAmmoSlot(), ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO);
      }

      for (ResourceBase r : originalMob.getCarriedResourcesList()) {
         newMob.addCarriedResource(r);
      }

      originalMob.getCarriedResourcesList().clear();
      originalMob.unequipResource(ResourceModule.ResourceSlot.HEAD);
      originalMob.unequipResource(ResourceModule.ResourceSlot.CHEST);
      originalMob.unequipResource(ResourceModule.ResourceSlot.TOOL);
      originalMob.unequipResource(ResourceModule.ResourceSlot.MELEE_WEAPON);
      originalMob.unequipResource(ResourceModule.ResourceSlot.SHIELD);
      originalMob.unequipResource(ResourceModule.ResourceSlot.RECOVERY);
      originalMob.unequipResource(ResourceModule.ResourceSlot.WATER);
      originalMob.unequipResource(ResourceModule.ResourceSlot.FOOD);
      originalMob.unequipResource(ResourceModule.ResourceSlot.RANGED_WEAPON);
      originalMob.unequipResource(ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO);
      if (deleteOriginal) {
         originalMob.unassignFromAll(false);
         this.deleteMob(originalMob);
      }

      return newMob;
   }

   public void newVillagerBirth(float x, float y, MobBase m, MobBase f) throws SlickException {
      int nextID = this.getNextMobID();
      MobBase newMob = null;
      if (f == null || f.isDead() || !this.mobMaster.containsValue(f)) {
         f = m;
      }

      if (m.isSocialChildBlessed() && m.getMobFlags().getSocialMateChildBlessedType() != null) {
         newMob = MobFactory.getNewInstance(m.getMobFlags().getSocialMateChildBlessedType());
      } else {
         newMob = MobFactory.getNewInstance(m.getMobFlags().getSocialMateChildType());
      }

      newMob.configNewFromParents(nextID, m, f, true);
      this.stats.increaseVillagerBirthsThisPeriod(newMob.getMobType());
      this.mobAdd.add(newMob);
   }

   public void deleteMob(MobBase m) {
      this.mobArray.remove(m);
      this.mobMaster.remove(m.getID());
   }

   public int getAverageHunger(MobBase.MobGroup group) {
      return this.getAverageHunger(group, null);
   }

   public int getAverageHunger(MobBase.MobType type) {
      return this.getAverageHunger(null, type);
   }

   private int getAverageHunger(MobBase.MobGroup group, MobBase.MobType type) {
      int hunger = 0;
      int mobCount = 0;
      ArrayList<MobBase> list = null;
      if (group != null) {
         list = this.mobsByGroupAlive.get(group);
      } else if (type != null) {
         list = this.mobsByTypeAlive.get(type);
      }

      for (MobBase m : list) {
         if (type == null || m.getMobType() == type) {
            hunger += m.getHunger();
            mobCount++;
         }
      }

      return mobCount != 0 && hunger != 0 ? hunger / mobCount : 0;
   }

   public int getAverageThirst(MobBase.MobGroup group) {
      return this.getAverageThirst(group, null);
   }

   public int getAverageThirst(MobBase.MobType type) {
      return this.getAverageThirst(null, type);
   }

   private int getAverageThirst(MobBase.MobGroup group, MobBase.MobType type) {
      int thirst = 0;
      int mobCount = 0;
      ArrayList<MobBase> list = null;
      if (group != null) {
         list = this.mobsByGroupAlive.get(group);
      } else if (type != null) {
         list = this.mobsByTypeAlive.get(type);
      }

      for (MobBase m : list) {
         if (type == null || m.getMobType() == type) {
            thirst += m.getThirst();
            mobCount++;
         }
      }

      return mobCount != 0 && thirst != 0 ? thirst / mobCount : 0;
   }

   public int getAverageAge(MobBase.MobGroup group) {
      return this.getAverageAge(group, null);
   }

   public int getAverageAge(MobBase.MobType type) {
      return this.getAverageAge(null, type);
   }

   private int getAverageAge(MobBase.MobGroup group, MobBase.MobType type) {
      int age = 0;
      int mobCount = 0;
      ArrayList<MobBase> list = null;
      if (group != null) {
         list = this.mobsByGroupAlive.get(group);
      } else if (type != null) {
         list = this.mobsByTypeAlive.get(type);
      }

      for (MobBase m : list) {
         if (type == null || m.getMobType() == type) {
            age += m.getAge();
            mobCount++;
         }
      }

      return mobCount != 0 && age != 0 ? age / mobCount : 0;
   }

   public int getAverageLifeExpectancy(MobBase.MobGroup group) {
      return this.getAverageLifeExpectancy(group, null);
   }

   public int getAverageLifeExpectancy(MobBase.MobType type) {
      return this.getAverageLifeExpectancy(null, type);
   }

   private int getAverageLifeExpectancy(MobBase.MobGroup group, MobBase.MobType type) {
      int lifeExpectancy = 0;
      int mobCount = 0;
      ArrayList<MobBase> list = null;
      if (group != null) {
         list = this.mobsByGroupAlive.get(group);
      } else if (type != null) {
         list = this.mobsByTypeAlive.get(type);
      }

      for (MobBase m : list) {
         if (type == null || m.getMobType() == type) {
            lifeExpectancy += m.getLifeExpectancy();
            mobCount++;
         }
      }

      return mobCount != 0 && lifeExpectancy != 0 ? lifeExpectancy / mobCount : 0;
   }

   public int getAverageEnergy(MobBase.MobGroup group) {
      return this.getAverageEnergy(group, null);
   }

   public int getAverageEnergy(MobBase.MobType type) {
      return this.getAverageEnergy(null, type);
   }

   private int getAverageEnergy(MobBase.MobGroup group, MobBase.MobType type) {
      int energy = 0;
      int mobCount = 0;
      ArrayList<MobBase> list = null;
      if (group != null) {
         list = this.mobsByGroupAlive.get(group);
      } else if (type != null) {
         list = this.mobsByTypeAlive.get(type);
      }

      for (MobBase m : list) {
         if (type == null || m.getMobType() == type) {
            energy += m.getEnergy();
            mobCount++;
         }
      }

      return mobCount != 0 && energy != 0 ? energy / mobCount : 0;
   }

   public MobSprite createMobSpriteSheet(MobBase.MobType mobType, MobBase.MobGender mobGender) throws SlickException {
      return this.mobSpriteLoader.createNewMobSprite(mobType, mobGender);
   }

   public byte[][] getMobPositionData() {
      return this.mobPositionMap;
   }

   public int getNextMobID() {
      return this.mobLastID++;
   }

   public void buildNewReachMaps() {
      for (MobBase m : this.mobArray) {
         m.removeReachMap();
      }

      for (MobBase m : this.mobArray) {
         m.buildNewReachMap();
      }
   }

   public ArrayList<MobBase> getMobArray() {
      return this.mobArray;
   }

   public HashMap<Integer, MobBase> getMobHashMap() {
      return this.mobMaster;
   }

   public ArrayList<MobBase> getMobsByType(MobBase.MobType type, boolean includeDead) {
      ArrayList<MobBase> list = new ArrayList<>();
      if (includeDead) {
         list.addAll(this.mobsByTypeAll.get(type));
      } else {
         list.addAll(this.mobsByTypeAlive.get(type));
      }

      return list;
   }

   public ArrayList<MobBase> getMobsByGroup(MobBase.MobGroup group, boolean includeDead) {
      ArrayList<MobBase> list = new ArrayList<>();
      if (includeDead) {
         list.addAll(this.mobsByGroupAll.get(group));
      } else {
         list.addAll(this.mobsByGroupAlive.get(group));
      }

      return list;
   }

   public ArrayList<MobBase> getMobsByGroups(ArrayList<MobBase.MobGroup> groups, boolean includeDead) {
      ArrayList<MobBase> list = new ArrayList<>();

      for (MobBase.MobGroup g : groups) {
         if (this.mobsByGroupAll.get(g) != null) {
            if (includeDead) {
               list.addAll(this.mobsByGroupAll.get(g));
            } else {
               list.addAll(this.mobsByGroupAlive.get(g));
            }
         }
      }

      return list;
   }

   public ArrayList<MobBase> getDeadMobsByGroups(MobBase.MobGroup group) {
      ArrayList<MobBase> list = new ArrayList<>();
      list.addAll(this.mobsByGroupDead.get(group));
      return list;
   }

   public ArrayList<MobBase> getMobsByJob(MobJobBase.MobJobType type, boolean includeDead) {
      ArrayList<MobBase> list = new ArrayList<>();
      if (includeDead) {
         list.addAll(this.mobsByJobAll.get(type));
      } else {
         list.addAll(this.mobsByJobAlive.get(type));
      }

      return list;
   }

   public ArrayList<MobBase> getMobsByAssignment(Assignment.AssignmentType type, boolean includeDead) {
      ArrayList<MobBase> list = new ArrayList<>();
      if (includeDead) {
         list.addAll(this.mobsByAssignmentAll.get(type));
      } else {
         list.addAll(this.mobsByAssignmentAlive.get(type));
      }

      return list;
   }

   public ArrayList<MobBase> getMobsWithDeliveryAssignment(boolean includeDead) {
      ArrayList<MobBase> list = new ArrayList<>();
      if (includeDead) {
         list.addAll(this.mobsWithDeliveryAssignmentAll);
      } else {
         list.addAll(this.mobsWithDeliveryAssignmentAlive);
      }

      return list;
   }

   public int getTotalAvailableFaith() {
      return this.totalAvailableFaith;
   }

   public int getTotalMaximumFaith() {
      return this.maximumAvailableFaith;
   }

   public MobBase getMobAt(int x, int y, MobBase mob) {
      if (this.mobPositionMap[x][y] == 0) {
         return null;
      } else {
         int nodeX = x / 8;
         int nodeY = y / 8;

         for (MobBase m : this.mobNodes[nodeX][nodeY]) {
            if (m != mob && m.getTileX() == x && m.getTileY() == y) {
               return m;
            }
         }

         return null;
      }
   }

   public MobBase getMobAt(int x, int y) {
      if (this.mobPositionMap[x][y] == 0) {
         return null;
      } else {
         int nodeX = x / 8;
         int nodeY = y / 8;

         for (MobBase m : this.mobNodes[nodeX][nodeY]) {
            if (m.getTileX() == x && m.getTileY() == y) {
               return m;
            }
         }

         return null;
      }
   }

   public MobBase getMobAt(Shape mouse, boolean includeDead) {
      for (MobBase m : this.mobArray) {
         if (m.intersects(mouse) && (!m.isDead() || includeDead)) {
            return m;
         }
      }

      return null;
   }

   public ArrayList<MobBase> getMobsAt(int x, int y) {
      return this.getMobsAt(x, y, null);
   }

   public ArrayList<MobBase> getMobsAt(int x, int y, MobBase mob) {
      ArrayList<MobBase> mobsAt = new ArrayList<>();
      if (this.mobPositionMap[x][y] == 0) {
         return mobsAt;
      } else {
         int nodeX = x / 8;
         int nodeY = y / 8;
         ArrayList<MobBase> node = this.mobNodes[nodeX][nodeY];
         if (node.size() == 0) {
            return mobsAt;
         } else {
            for (MobBase m : node) {
               if (m != mob && m.getTileX() == x && m.getTileY() == y) {
                  mobsAt.add(m);
               }
            }

            return mobsAt;
         }
      }
   }

   public ArrayList<MobBase> getMobsNearby(int tileX, int tileY, int distance) {
      ArrayList<MobBase> list = new ArrayList<>();
      ArrayList<MobBase> nodeCollection = this.getNodeCollection(tileX, tileY, distance);
      if (nodeCollection.size() <= 0) {
         return list;
      } else {
         for (MobBase m : nodeCollection) {
            if (m.isAvailable()) {
               int dx = tileX - m.getTileX();
               int dy = tileY - m.getTileY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < distance) {
                  list.add(m);
               }
            }
         }

         return list;
      }
   }

   public ArrayList<MobBase> getMobsNearby(int tileX, int tileY, int distance, MobBase.MobGroup group) {
      ArrayList<MobBase> list = new ArrayList<>();
      ArrayList<MobBase> nodeCollection = this.getNodeCollectionByGroup(tileX, tileY, distance, group);
      if (nodeCollection.size() <= 0) {
         return list;
      } else {
         for (MobBase m : nodeCollection) {
            if (m.isAvailable() && m.getMobGroup() == group) {
               int dx = tileX - m.getTileX();
               int dy = tileY - m.getTileY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < distance) {
                  list.add(m);
               }
            }
         }

         return list;
      }
   }

   public ArrayList<MobBase> getMobsNearby(int tileX, int tileY, int distance, ArrayList<MobBase.MobGroup> groups) {
      ArrayList<MobBase> list = new ArrayList<>();

      for (MobBase.MobGroup group : groups) {
         ArrayList<MobBase> nodeCollection = this.getNodeCollectionByGroup(tileX, tileY, distance, group);
         if (nodeCollection.size() > 0) {
            for (MobBase m : nodeCollection) {
               if (m.isAvailable() && m.getMobGroup() == group) {
                  int dx = tileX - m.getTileX();
                  int dy = tileY - m.getTileY();
                  int hValue = (int)Math.sqrt(dx * dx + dy * dy);
                  if (hValue < distance) {
                     list.add(m);
                  }
               }
            }
         }
      }

      return list;
   }

   public ArrayList<MobBase> getMobsNearby(int tileX, int tileY, int distance, MobBase.MobType type) {
      ArrayList<MobBase> list = new ArrayList<>();
      ArrayList<MobBase> nodeCollection = this.getNodeCollectionByType(tileX, tileY, distance, type);
      if (nodeCollection.size() <= 0) {
         return list;
      } else {
         for (MobBase m : nodeCollection) {
            if (m.isAvailable() && m.getMobType() == type) {
               int dx = tileX - m.getTileX();
               int dy = tileY - m.getTileY();
               int hValue = (int)Math.sqrt(dx * dx + dy * dy);
               if (hValue < distance) {
                  list.add(m);
               }
            }
         }

         return list;
      }
   }

   public ArrayList<MobBase> getDeadMobsNearby(int tileX, int tileY, int distance) {
      ArrayList<MobBase> list = new ArrayList<>();
      ArrayList<MobBase> nodeCollection = this.getNodeCollection(tileX, tileY, distance);
      if (nodeCollection.size() <= 0) {
         return list;
      } else {
         for (MobBase m : nodeCollection) {
            int dx = tileX - m.getTileX();
            int dy = tileY - m.getTileY();
            int hValue = (int)Math.sqrt(dx * dx + dy * dy);
            if (hValue < distance && m.isDead()) {
               list.add(m);
            }
         }

         return list;
      }
   }

   private ArrayList<MobBase> getNodeCollection(int tileX, int tileY, int range) {
      int nodeX = tileX / 8;
      int nodeY = tileY / 8;
      int nodeRange = range / 8 + 2;
      int nodeRangeStart = nodeRange * -1;
      int nodeRangeStop = nodeRange;
      ArrayList<MobBase> nodeCollection = new ArrayList<>();

      for (int x = nodeRangeStart; x < nodeRangeStop; x++) {
         for (int y = nodeRangeStart; y < nodeRangeStop; y++) {
            if (!Utilities.isOutOfBounds(32, 32, nodeX + x, nodeY + y)) {
               nodeCollection.addAll(this.mobNodes[nodeX + x][nodeY + y]);
            }
         }
      }

      return nodeCollection;
   }

   private ArrayList<MobBase> getNodeCollectionByType(int tileX, int tileY, int range, MobBase.MobType t) {
      int nodeX = tileX / 8;
      int nodeY = tileY / 8;
      int nodeRange = range / 8 + 2;
      int nodeRangeStart = nodeRange * -1;
      int nodeRangeStop = nodeRange;
      ArrayList<MobBase> nodeCollection = new ArrayList<>();
      ArrayList[][] nodeByType = this.mobNodesByType.get(t);

      for (int x = nodeRangeStart; x < nodeRangeStop; x++) {
         for (int y = nodeRangeStart; y < nodeRangeStop; y++) {
            if (!Utilities.isOutOfBounds(32, 32, nodeX + x, nodeY + y)) {
               nodeCollection.addAll(nodeByType[nodeX + x][nodeY + y]);
            }
         }
      }

      return nodeCollection;
   }

   private ArrayList<MobBase> getNodeCollectionByGroup(int tileX, int tileY, int range, MobBase.MobGroup g) {
      int nodeX = tileX / 8;
      int nodeY = tileY / 8;
      int nodeRange = range / 8 + 2;
      int nodeRangeStart = nodeRange * -1;
      int nodeRangeStop = nodeRange;
      ArrayList<MobBase> nodeCollection = new ArrayList<>();
      ArrayList[][] nodeByGroup = this.mobNodesByGroup.get(g);

      for (int x = nodeRangeStart; x < nodeRangeStop; x++) {
         for (int y = nodeRangeStart; y < nodeRangeStop; y++) {
            if (!Utilities.isOutOfBounds(32, 32, nodeX + x, nodeY + y)) {
               nodeCollection.addAll(nodeByGroup[nodeX + x][nodeY + y]);
            }
         }
      }

      return nodeCollection;
   }

   public ArrayList<MobBase> sortMobsByDistance(final int tileX, final int tileY, ArrayList<MobBase> list) {
      Collections.sort(list, new Comparator<MobBase>() {
         public int compare(MobBase m1, MobBase m2) {
            int dx1 = m1.getTileX() - tileX;
            int dy1 = m1.getTileY() - tileY;
            int hValue1 = dx1 * dx1 + dy1 * dy1;
            int dx2 = m2.getTileX() - tileX;
            int dy2 = m2.getTileY() - tileY;
            int hValue2 = dx2 * dx2 + dy2 * dy2;
            return m1 == m2 ? 0 : hValue1 - hValue2;
         }
      });
      return list;
   }

   public ArrayList<MobBase> cullMobsByDistance(int tileX, int tileY, int distance, ArrayList<MobBase> list, MapData.BlockMapGroup g) throws SlickException {
      ArrayList<MobBase> listOut = new ArrayList<>();
      ArrayList<OrderedPair> coordinates = this.pathFinder.getCoordinatesInRange(tileX, tileY, distance, false, g);

      for (MobBase m : list) {
         if (coordinates.contains(OrderedPair.getOrderedPair(m.getTileX(), m.getTileY()))) {
            listOut.add(m);
         }
      }

      return listOut;
   }

   public PathFinder getPathfinder() {
      return this.pathFinder;
   }

   public PathMapper getPathMapper() {
      return this.pathMapper;
   }

   public boolean isWorkAssigned(int checkX, int checkY, boolean checkPrimary, boolean checkSecondary, Assignment.AssignmentType t) {
      return this.isWorkAssigned(OrderedPair.getOrderedPair(checkX, checkY), checkPrimary, checkSecondary, t);
   }

   public boolean isWorkAssigned(int checkX, int checkY, Assignment.AssignmentType t) {
      return this.isWorkAssigned(OrderedPair.getOrderedPair(checkX, checkY), t);
   }

   public boolean isWorkAssigned(OrderedPair o, Assignment.AssignmentType t) {
      return this.isWorkAssigned(o, true, true, t);
   }

   public boolean isWorkAssigned(OrderedPair o, boolean checkPrimary, boolean checkSecondary, Assignment.AssignmentType t) {
      ArrayList<MobBase> mobList = this.getMobsByAssignment(t, false);

      for (int e = 0; e < mobList.size(); e++) {
         Assignment a = mobList.get(e).getAssignment();
         if (checkPrimary && o == a.getPrimaryCoordinates() || checkSecondary && o == a.getSecondaryCoordinates()) {
            return true;
         }
      }

      return false;
   }

   public void increaseMainMenuKillCount() {
      this.mainMenuKillCount++;
   }

   public int getMainMenuKillCount() {
      return this.mainMenuKillCount;
   }

   public void resetMainMenuKillCount() {
      this.mainMenuKillCount = 0;
   }

   public void tryPanicInArea(int tileX, int tileY, int distance, boolean faithBased, int chance) throws SlickException {
      for (MobBase m : this.getMobsNearby(tileX, tileY, distance)) {
         if ((!m.getMobFlags().getAllowCombat() || m.getAssignment().getPrimaryMob() == null)
            && m.getMobFlags().getAllowPanic()
            && m.getActiveNode() != m.getAIPanic()
            && m.getActiveNode() != m.getAIGrabbed()) {
            if (faithBased) {
               double chanceD = Utilities.getPercentage(m.getFaithMax(), m.getFaith());
               chance += (int)(chanceD / 10.0);
            }

            if (chance < 1 || Utilities.randomInt(chance) == 0) {
               m.interruptNode(m.getAIPanic());
            }
         }
      }
   }

   public void tryConfuseInArea(int tileX, int tileY, int distance, boolean faithBased, int chance) throws SlickException {
      for (MobBase m : this.getMobsNearby(tileX, tileY, distance)) {
         if ((!m.getMobFlags().getAllowCombat() || m.getAssignment().getPrimaryMob() == null)
            && m.getMobFlags().getAllowConfused()
            && m.getActiveNode() != m.getAIConfused()
            && m.getActiveNode() != m.getAIPanic()
            && m.getActiveNode() != m.getAIGrabbed()) {
            if (faithBased) {
               double chanceD = Utilities.getPercentage(m.getFaithMax(), m.getFaith());
               chance += (int)(chanceD / 10.0);
            }

            if (chance < 1 || Utilities.randomInt(chance) == 0) {
               m.interruptNode(m.getAIConfused());
               m.setConfusedTarget(OrderedPair.getOrderedPair(tileX, tileY));
            }
         }
      }
   }

   public void tryConfuseInAreaIfIdle(int tileX, int tileY, int distance, boolean faithBased, int chance) throws SlickException {
      for (MobBase m : this.getMobsNearby(tileX, tileY, distance)) {
         if ((!m.getMobFlags().getAllowCombat() || m.getAssignment().getPrimaryMob() == null)
            && m.getMobFlags().getAllowConfused()
            && m.getActiveNode() == m.getAIStart()) {
            if (faithBased) {
               double chanceD = Utilities.getPercentage(m.getFaithMax(), m.getFaith());
               chance += (int)(chanceD / 10.0);
            }

            if (chance < 1 || Utilities.randomInt(chance) == 0) {
               m.interruptNode(m.getAIConfused());
               m.setConfusedTarget(OrderedPair.getOrderedPair(tileX, tileY));
            }
         }
      }
   }

   public void influenceFaithInArea(int tileX, int tileY, int distance, int amountLow, int amountHigh, boolean positiveInfluence) throws SlickException {
      this.influenceFaithInArea(tileX, tileY, distance, Utilities.randomInt(amountLow, amountHigh), positiveInfluence);
   }

   public void influenceFaithInArea(int tileX, int tileY, int distance, int amount, boolean positiveInfluence) throws SlickException {
      for (MobBase m : this.getMobsNearby(tileX, tileY, distance)) {
         if (m.getMobFlags().getAllowFaith()) {
            this.goal.incrementGoal(GoalModule.GoalType.GOD_OR_MAGICIAN);
            if (positiveInfluence) {
               m.increaseFaith(amount, true);
            } else {
               m.decreaseFaith(amount, true);
            }
         }
      }
   }

   public MobTwitchData getMobTwitchData() {
      return this.mobTwitchData;
   }

   public void toggleDebugResetAI() {
      this.debugResetAI = !this.debugResetAI;
   }

   public boolean isDebugResetAI() {
      return this.debugResetAI;
   }

   public void addMobAndEquipmentToMasterList(MobBase m) throws SlickException {
      m.setID(this.getNextMobID());
      if (m.getInventoryHeadSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryHeadSlot());
         m.equipResource(m.getInventoryHeadSlot(), ResourceModule.ResourceSlot.HEAD);
      }

      if (m.getInventoryChestSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryChestSlot());
         m.equipResource(m.getInventoryChestSlot(), ResourceModule.ResourceSlot.CHEST);
      }

      if (m.getInventoryToolSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryToolSlot());
         m.equipResource(m.getInventoryToolSlot(), ResourceModule.ResourceSlot.TOOL);
      }

      if (m.getInventoryMeleeWeaponSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryMeleeWeaponSlot());
         m.equipResource(m.getInventoryMeleeWeaponSlot(), ResourceModule.ResourceSlot.MELEE_WEAPON);
      }

      if (m.getInventoryShieldSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryShieldSlot());
         m.equipResource(m.getInventoryShieldSlot(), ResourceModule.ResourceSlot.SHIELD);
      }

      if (m.getInventoryRecoverySlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryRecoverySlot());
         m.equipResource(m.getInventoryRecoverySlot(), ResourceModule.ResourceSlot.RECOVERY);
      }

      if (m.getInventoryWaterSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryWaterSlot());
         m.equipResource(m.getInventoryWaterSlot(), ResourceModule.ResourceSlot.WATER);
      }

      if (m.getInventoryFoodSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryFoodSlot());
         m.equipResource(m.getInventoryFoodSlot(), ResourceModule.ResourceSlot.FOOD);
      }

      if (m.getInventoryRangedWeaponSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryRangedWeaponSlot());
         m.equipResource(m.getInventoryRangedWeaponSlot(), ResourceModule.ResourceSlot.RANGED_WEAPON);
      }

      if (m.getInventoryRangedWeaponAmmoSlot() != null) {
         this.resource.addResourceToMasterList(m.getInventoryRangedWeaponAmmoSlot());
         m.equipResource(m.getInventoryRangedWeaponAmmoSlot(), ResourceModule.ResourceSlot.RANGED_WEAPON_AMMO);
      }

      ArrayList<ResourceBase> refreshList = new ArrayList<>();
      refreshList.addAll(m.getCarriedResourcesList());
      m.getCarriedResourcesList().clear();

      for (ResourceBase r : refreshList) {
         this.resource.addResourceToMasterList(r);
         m.addCarriedResource(r);
      }

      this.mobAdd.add(m);
   }

   public void refreshMobJobColors(MobJobBase.MobJobType j) {
      for (MobBase m : this.mobsByJobAll.get(j)) {
         m.refreshMobJobColors();
      }
   }

   public static enum MobNameType {
      STANDARD,
      DOGGO;
   }
}
