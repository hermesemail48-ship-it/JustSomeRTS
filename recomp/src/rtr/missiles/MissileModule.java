package rtr.missiles;

import java.util.ArrayList;
import java.util.EnumMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.console.Console;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.save.YMLDataMap;
import rtr.states.StateBase;
import rtr.system.Game;

public class MissileModule extends ModuleBase {
   private ArrayList<MissileBase> missileArray = new ArrayList<>();
   private static final int ANIMATION_SIZE = 32;
   private SpriteSheet missileBaseSpriteSheet;
   private EnumMap<MissileModule.MissileType, Image> missileImages = new EnumMap<>(MissileModule.MissileType.class);
   private MissileFactory missileFactory;

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.missileBaseSpriteSheet = new SpriteSheet(ImageLoader.getImage("res/towers/missiles.png"), 32, 32);
      this.missileImages.put(MissileModule.MissileType.BASIC_ARROW, this.missileBaseSpriteSheet.getSprite(1, 0));
      this.missileImages.put(MissileModule.MissileType.STURDY_ARROW, this.missileBaseSpriteSheet.getSprite(1, 0));
      this.missileImages.put(MissileModule.MissileType.REINFORCED_ARROW, this.missileBaseSpriteSheet.getSprite(1, 0));
      this.missileImages.put(MissileModule.MissileType.FIRE_ARROW, this.missileBaseSpriteSheet.getSprite(3, 0));
      this.missileImages.put(MissileModule.MissileType.POISON_ARROW, this.missileBaseSpriteSheet.getSprite(2, 0));
      this.missileImages.put(MissileModule.MissileType.ICE_ARROW, this.missileBaseSpriteSheet.getSprite(4, 0));
      this.missileImages.put(MissileModule.MissileType.LIGHTNING_ARROW, this.missileBaseSpriteSheet.getSprite(5, 0));
      this.missileImages.put(MissileModule.MissileType.BASIC_BALLISTA_ARROW, this.missileBaseSpriteSheet.getSprite(0, 1));
      this.missileImages.put(MissileModule.MissileType.STURDY_BALLISTA_ARROW, this.missileBaseSpriteSheet.getSprite(0, 1));
      this.missileImages.put(MissileModule.MissileType.REINFORCED_BALLISTA_ARROW, this.missileBaseSpriteSheet.getSprite(0, 1));
      this.missileImages.put(MissileModule.MissileType.FIRE_BALLISTA_ARROW, this.missileBaseSpriteSheet.getSprite(2, 1));
      this.missileImages.put(MissileModule.MissileType.POISON_BALLISTA_ARROW, this.missileBaseSpriteSheet.getSprite(1, 1));
      this.missileImages.put(MissileModule.MissileType.ICE_BALLISTA_ARROW, this.missileBaseSpriteSheet.getSprite(3, 1));
      this.missileImages.put(MissileModule.MissileType.BASIC_BULLET, this.missileBaseSpriteSheet.getSprite(0, 4));
      this.missileImages.put(MissileModule.MissileType.HEAVY_BULLET, this.missileBaseSpriteSheet.getSprite(0, 4));
      this.missileImages.put(MissileModule.MissileType.REINFORCED_BULLET, this.missileBaseSpriteSheet.getSprite(0, 4));
      this.missileImages.put(MissileModule.MissileType.FIRE_BULLET, this.missileBaseSpriteSheet.getSprite(1, 4));
      this.missileImages.put(MissileModule.MissileType.LIGHTNING_BULLET, this.missileBaseSpriteSheet.getSprite(2, 4));
      this.missileImages.put(MissileModule.MissileType.ICE_BULLET, this.missileBaseSpriteSheet.getSprite(3, 4));
      this.missileImages.put(MissileModule.MissileType.BASIC_SPRAY_BULLET, this.missileBaseSpriteSheet.getSprite(0, 5));
      this.missileImages.put(MissileModule.MissileType.HEAVY_SPRAY_BULLET, this.missileBaseSpriteSheet.getSprite(0, 5));
      this.missileImages.put(MissileModule.MissileType.REINFORCED_SPRAY_BULLET, this.missileBaseSpriteSheet.getSprite(0, 5));
      this.missileImages.put(MissileModule.MissileType.FIRE_SPRAY_BULLET, this.missileBaseSpriteSheet.getSprite(1, 5));
      this.missileImages.put(MissileModule.MissileType.LIGHTNING_SPRAY_BULLET, this.missileBaseSpriteSheet.getSprite(2, 5));
      this.missileImages.put(MissileModule.MissileType.ICE_SPRAY_BULLET, this.missileBaseSpriteSheet.getSprite(3, 5));
      this.missileImages.put(MissileModule.MissileType.SMALL_ELEMENTAL_BOLT, this.missileBaseSpriteSheet.getSprite(0, 2));
      this.missileImages.put(MissileModule.MissileType.ELEMENTAL_BOLT, this.missileBaseSpriteSheet.getSprite(0, 2));
      this.missileImages.put(MissileModule.MissileType.LARGE_ELEMENTAL_BOLT, this.missileBaseSpriteSheet.getSprite(0, 2));
      this.missileImages.put(MissileModule.MissileType.FIRE_BOLT, this.missileBaseSpriteSheet.getSprite(1, 2));
      this.missileImages.put(MissileModule.MissileType.LIGHTNING_BOLT, this.missileBaseSpriteSheet.getSprite(2, 2));
      this.missileImages.put(MissileModule.MissileType.ICE_BOLT, this.missileBaseSpriteSheet.getSprite(3, 2));
      this.missileImages.put(MissileModule.MissileType.SMALL_PHANTOM_DART, this.missileBaseSpriteSheet.getSprite(0, 2));
      this.missileImages.put(MissileModule.MissileType.PHANTOM_DART, this.missileBaseSpriteSheet.getSprite(0, 2));
      this.missileImages.put(MissileModule.MissileType.LARGE_PHANTOM_DART, this.missileBaseSpriteSheet.getSprite(0, 2));
      this.missileImages.put(MissileModule.MissileType.PHANTOM_FIRE_DART, this.missileBaseSpriteSheet.getSprite(1, 2));
      this.missileImages.put(MissileModule.MissileType.PHANTOM_LIGHTNING_DART, this.missileBaseSpriteSheet.getSprite(2, 2));
      this.missileImages.put(MissileModule.MissileType.PHANTOM_ICE_DART, this.missileBaseSpriteSheet.getSprite(3, 2));
      this.missileImages.put(MissileModule.MissileType.PHANTOM_POISON_DART, this.missileBaseSpriteSheet.getSprite(4, 2));
      this.missileImages.put(MissileModule.MissileType.BASIC_STONE_BALL, this.missileBaseSpriteSheet.getSprite(0, 3));
      this.missileImages.put(MissileModule.MissileType.HEAVY_STONE_BALL, this.missileBaseSpriteSheet.getSprite(0, 3));
      this.missileImages.put(MissileModule.MissileType.REINFORCED_STONE_BALL, this.missileBaseSpriteSheet.getSprite(0, 3));
      this.missileImages.put(MissileModule.MissileType.EXPLODING_STONE_BALL, this.missileBaseSpriteSheet.getSprite(1, 3));
      this.missileImages.put(MissileModule.MissileType.LIGHTNING_STONE_BALL, this.missileBaseSpriteSheet.getSprite(2, 3));
      this.missileImages.put(MissileModule.MissileType.ICE_STONE_BALL, this.missileBaseSpriteSheet.getSprite(3, 3));
      this.missileImages.put(MissileModule.MissileType.MOTIVATION_BOLT, this.missileBaseSpriteSheet.getSprite(3, 2));
      this.missileImages.put(MissileModule.MissileType.ATTRACT_BOLT, this.missileBaseSpriteSheet.getSprite(3, 2));
      this.missileImages.put(MissileModule.MissileType.BANISH_BOLT, this.missileBaseSpriteSheet.getSprite(3, 2));
      this.missileImages.put(MissileModule.MissileType.RECOMBOBULATOR_BOLT, this.missileBaseSpriteSheet.getSprite(3, 2));
      this.missileImages.put(MissileModule.MissileType.ITEM_BOW_ARROW, this.missileBaseSpriteSheet.getSprite(0, 0));
      this.missileImages.put(MissileModule.MissileType.FIRE_ELEMENTAL, this.missileBaseSpriteSheet.getSprite(0, 5));
      this.missileImages.put(MissileModule.MissileType.FIRE_GOLEM, this.missileBaseSpriteSheet.getSprite(0, 5));
      this.missileImages.put(MissileModule.MissileType.ICE_GOLEM, this.missileBaseSpriteSheet.getSprite(3, 5));
      this.missileImages.put(MissileModule.MissileType.LIGHTNING_GOLEM, this.missileBaseSpriteSheet.getSprite(2, 5));
      this.missileImages.put(MissileModule.MissileType.CRYSTAL_GOLEM, this.missileBaseSpriteSheet.getSprite(0, 5));
      this.missileImages.put(MissileModule.MissileType.FIRE_CRYSTAL_GOLEM, this.missileBaseSpriteSheet.getSprite(1, 5));
      this.missileImages.put(MissileModule.MissileType.ICE_CRYSTAL_GOLEM, this.missileBaseSpriteSheet.getSprite(3, 5));
      this.missileImages.put(MissileModule.MissileType.LIGHTNING_CRYSTAL_GOLEM, this.missileBaseSpriteSheet.getSprite(2, 5));
      this.missileImages.put(MissileModule.MissileType.SPECIAL_MAIN_MENU, this.missileBaseSpriteSheet.getSprite(1, 2));
      this.requiresSync = true;
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.missileArray.clear();
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

      for (int count = 0; dataMap.containsKey("missile" + count); count++) {
         YMLDataMap thisMap = dataMap.getSubMap("missile" + count);
         this.loadMissile(count, thisMap);
      }
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   @Override
   public void initLoadSync() throws SlickException {
      this.synced = true;
      if (this.missileFactory == null) {
         this.missileFactory = new MissileFactory();
      }

      for (int e = 0; e < this.missileArray.size(); e++) {
         this.missileArray.get(e).loadSync();
      }
   }

   @Override
   public YMLDataMap getRegionalSaveData() {
      YMLDataMap dataMap = new YMLDataMap();
      dataMap.put("version", Game.getVersion());

      for (int x = 0; x < this.missileArray.size(); x++) {
         dataMap.put("missile" + x, this.missileArray.get(x).getRegionalSaveData());
      }

      return dataMap;
   }

   public void update() throws SlickException {
      for (int i = 0; i < this.missileArray.size(); i++) {
         this.missileArray.get(i).update();
      }
   }

   public void renderBottom() throws SlickException {
      this.missileBaseSpriteSheet.startUse();

      for (MissileBase m : this.missileArray) {
         if (!m.isTop()) {
            m.render();
         }
      }

      this.missileBaseSpriteSheet.endUse();
   }

   public void renderTop() throws SlickException {
      this.missileBaseSpriteSheet.startUse();

      for (MissileBase m : this.missileArray) {
         if (m.isTop()) {
            m.render();
         }
      }

      this.missileBaseSpriteSheet.endUse();
   }

   public Image getMissileImage(MissileModule.MissileType type) {
      return this.missileImages.get(type);
   }

   private void loadMissile(int id, YMLDataMap dataMap) throws SlickException {
      MissileModule.MissileType type = null;
      String missileTypeString = (String)dataMap.get("missileType");

      MissileModule.MissileType[] var8;
      for (MissileModule.MissileType mT : MissileModule.MissileType.values()) {
         if (mT.toString().equals(missileTypeString)) {
            type = mT;
         }
      }

      if (type == null) {
         Console.out("Missile " + id + " failed to load.", true);
      } else {
         MissileBase newMissile = this.missileFactory.getNewInstance(type);
         newMissile.loadMissile(id, dataMap);
         this.missileArray.add(newMissile);
      }
   }

   public MissileBase newMissile(int x, int y, ObjectBase f, ObjectBase t, int objectTargetX, int objectTargetY, MissileModule.MissileType type) {
      MissileBase newMissile = this.missileFactory.getNewInstance(type);
      newMissile.configNew(x, y, t, objectTargetX, objectTargetY);
      newMissile.setFiringObject(f);
      this.missileArray.add(newMissile);
      return newMissile;
   }

   public MissileBase newMissile(int x, int y, ObjectBase f, MobBase t, MissileModule.MissileType type) {
      MissileBase newMissile = this.missileFactory.getNewInstance(type);
      newMissile.configNew(x, y, t);
      newMissile.setFiringObject(f);
      this.missileArray.add(newMissile);
      return newMissile;
   }

   public MissileBase newMissile(int x, int y, MobBase f, ObjectBase t, int objectTargetX, int objectTargetY, MissileModule.MissileType type) {
      MissileBase newMissile = this.missileFactory.getNewInstance(type);
      newMissile.configNew(x, y, t, objectTargetX, objectTargetY);
      newMissile.setFiringMob(f);
      this.missileArray.add(newMissile);
      return newMissile;
   }

   public MissileBase newMissile(int x, int y, MobBase f, MobBase t, MissileModule.MissileType type) {
      MissileBase newMissile = this.missileFactory.getNewInstance(type);
      newMissile.configNew(x, y, t);
      newMissile.setFiringMob(f);
      this.missileArray.add(newMissile);
      return newMissile;
   }

   public MissileBase newMissile(int x, int y, MobBase t, MissileModule.MissileType type) {
      MissileBase newMissile = this.missileFactory.getNewInstance(type);
      newMissile.configNew(x, y, t);
      this.missileArray.add(newMissile);
      return newMissile;
   }

   public MissileBase newMissile(int x, int y, int tX, int tY, MissileModule.MissileType type) {
      MissileBase newMissile = this.missileFactory.getNewInstance(type);
      newMissile.configNew(x, y, tX, tY);
      this.missileArray.add(newMissile);
      return newMissile;
   }

   public ArrayList<MissileBase> getMissileArray() {
      return this.missileArray;
   }

   public void remove(MissileBase p) {
      this.missileArray.remove(p);
   }

   public MissileFactory getMissileFactory() {
      return this.missileFactory;
   }

   public static enum MissileType {
      BASIC_ARROW(Text.getText("missileName.basicArrow")),
      STURDY_ARROW(Text.getText("missileName.sturdyArrow")),
      REINFORCED_ARROW(Text.getText("missileName.reinforcedArrow")),
      FIRE_ARROW(Text.getText("missileName.fireArrow")),
      LIGHTNING_ARROW(Text.getText("missileName.lightningArrow")),
      POISON_ARROW(Text.getText("missileName.poisonArrow")),
      ICE_ARROW(Text.getText("missileName.iceArrow")),
      BASIC_BALLISTA_ARROW(Text.getText("missileName.basicBallistaArrow")),
      STURDY_BALLISTA_ARROW(Text.getText("missileName.sturdyBallistaArrow")),
      REINFORCED_BALLISTA_ARROW(Text.getText("missileName.reinforcedBallistaArrow")),
      FIRE_BALLISTA_ARROW(Text.getText("missileName.fireBallistaArrow")),
      POISON_BALLISTA_ARROW(Text.getText("missileName.poisonBallistaArrow")),
      ICE_BALLISTA_ARROW(Text.getText("missileName.iceBallistaArrow")),
      BASIC_BULLET(Text.getText("missileName.basicBullet")),
      HEAVY_BULLET(Text.getText("missileName.heavyBullet")),
      REINFORCED_BULLET(Text.getText("missileName.reinforcedBullet")),
      FIRE_BULLET(Text.getText("missileName.fireBullet")),
      LIGHTNING_BULLET(Text.getText("missileName.lightningBullet")),
      ICE_BULLET(Text.getText("missileName.iceBullet")),
      BASIC_SPRAY_BULLET(Text.getText("missileName.basicSprayBullet")),
      HEAVY_SPRAY_BULLET(Text.getText("missileName.heavySprayBullet")),
      REINFORCED_SPRAY_BULLET(Text.getText("missileName.reinforcedSprayBullet")),
      FIRE_SPRAY_BULLET(Text.getText("missileName.fireSprayBullet")),
      LIGHTNING_SPRAY_BULLET(Text.getText("missileName.lightningSprayBullet")),
      ICE_SPRAY_BULLET(Text.getText("missileName.iceSprayBullet")),
      SMALL_ELEMENTAL_BOLT(Text.getText("missileName.smallElementalBolt")),
      ELEMENTAL_BOLT(Text.getText("missileName.elementalBolt")),
      LARGE_ELEMENTAL_BOLT(Text.getText("missileName.largeElementalBolt")),
      FIRE_BOLT(Text.getText("missileName.fireBolt")),
      LIGHTNING_BOLT(Text.getText("missileName.lightningBolt")),
      ICE_BOLT(Text.getText("missileName.iceBolt")),
      SMALL_PHANTOM_DART(Text.getText("missileName.smallPhantomDart")),
      PHANTOM_DART(Text.getText("missileName.phantomDart")),
      LARGE_PHANTOM_DART(Text.getText("missileName.largePhantomDart")),
      PHANTOM_FIRE_DART(Text.getText("missileName.phantomFireDart")),
      PHANTOM_LIGHTNING_DART(Text.getText("missileName.phantomLightningDart")),
      PHANTOM_ICE_DART(Text.getText("missileName.phantomIceDart")),
      PHANTOM_POISON_DART(Text.getText("missileName.phantomPoisonDart")),
      BASIC_STONE_BALL(Text.getText("missileName.basicStoneBall")),
      HEAVY_STONE_BALL(Text.getText("missileName.heavyStoneBall")),
      REINFORCED_STONE_BALL(Text.getText("missileName.reinforcedStoneBall")),
      EXPLODING_STONE_BALL(Text.getText("missileName.explodingStoneBall")),
      LIGHTNING_STONE_BALL(Text.getText("missileName.lightningStoneBall")),
      ICE_STONE_BALL(Text.getText("missileName.iceStoneBall")),
      MOTIVATION_BOLT(Text.getText("missileName.motivationBolt")),
      ATTRACT_BOLT(Text.getText("missileName.attractBolt")),
      BANISH_BOLT(Text.getText("missileName.banishBolt")),
      RECOMBOBULATOR_BOLT(Text.getText("missileName.recombobulatorBolt")),
      ITEM_BOW_ARROW(Text.getText("missileName.bowArrow")),
      FIRE_ELEMENTAL(Text.getText("missileName.fireElemental")),
      FIRE_GOLEM(Text.getText("missileName.fireGolem")),
      ICE_GOLEM(Text.getText("missileName.iceGolem")),
      LIGHTNING_GOLEM(Text.getText("missileName.lightningGolem")),
      CRYSTAL_GOLEM(Text.getText("missileName.crystalGolem")),
      FIRE_CRYSTAL_GOLEM(Text.getText("missileName.fireCrystalGolem")),
      ICE_CRYSTAL_GOLEM(Text.getText("missileName.iceCrystalGolem")),
      LIGHTNING_CRYSTAL_GOLEM(Text.getText("missileName.lightningCrystalGolem")),
      SPECIAL_MAIN_MENU(Text.getText("missileName.specialMainMenu"));

      private final String missileTypeText;

      private MissileType(String missileTypeText) {
         this.missileTypeText = missileTypeText;
      }

      public String getText() {
         return this.missileTypeText;
      }
   }
}
