package rtr.objects.towerhead.spritesheet;

import java.util.EnumMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import r2d.image.SpriteSheet;
import rtr.ImageLoader;
import rtr.objects.towerhead.TowerBase;
import rtr.objects.towerhead.spritesheet.templates.TowerSpriteTemplateBallista;
import rtr.objects.towerhead.spritesheet.templates.TowerSpriteTemplateBase;
import rtr.objects.towerhead.spritesheet.templates.TowerSpriteTemplateBow;
import rtr.objects.towerhead.spritesheet.templates.TowerSpriteTemplateBullet;
import rtr.objects.towerhead.spritesheet.templates.TowerSpriteTemplateSlingTower;
import rtr.objects.towerhead.spritesheet.templates.TowerSpriteTemplateSpray;

public class TowerSpriteLoader {
   private static final int ANIMATION_COUNT = 4;
   private static final int ANIMATION_FRAMES = 8;
   private static final int SMALL_TOWER_SIZE = 32;
   private static final int LARGE_TOWER_SIZE = 48;
   private Image towerBaseSpriteSheet;
   private EnumMap<TowerBase.TowerType, SpriteSheet> spriteSheets = new EnumMap<>(TowerBase.TowerType.class);
   private EnumMap<TowerBase.TowerType, TowerSpriteTemplateBase> spriteTemplates = new EnumMap<>(TowerBase.TowerType.class);

   public TowerSpriteLoader() throws SlickException {
      this.towerBaseSpriteSheet = ImageLoader.getImage("res/towers/towers.png");
      this.spriteTemplates.put(TowerBase.TowerType.BOW, new TowerSpriteTemplateBow());
      this.spriteTemplates.put(TowerBase.TowerType.SLING, new TowerSpriteTemplateSlingTower());
      this.spriteTemplates.put(TowerBase.TowerType.BULLET, new TowerSpriteTemplateBullet());
      this.spriteTemplates.put(TowerBase.TowerType.SPRAY, new TowerSpriteTemplateSpray());
      this.spriteTemplates.put(TowerBase.TowerType.GOD_BOW, new TowerSpriteTemplateBow());
      this.spriteTemplates.put(TowerBase.TowerType.CORRUPTED_FIRE_BOW, new TowerSpriteTemplateBow());
      this.spriteTemplates.put(TowerBase.TowerType.CORRUPTED_ICE_BOW, new TowerSpriteTemplateBow());
      this.spriteTemplates.put(TowerBase.TowerType.CORRUPTED_POISON_BOW, new TowerSpriteTemplateBow());
      this.spriteTemplates.put(TowerBase.TowerType.CORRUPTED_LIGHTNING_BOW, new TowerSpriteTemplateBow());
      this.spriteTemplates.put(TowerBase.TowerType.BALLISTA, new TowerSpriteTemplateBallista());
      this.loadTower(TowerBase.TowerType.BOW, 0, false);
      this.loadTower(TowerBase.TowerType.SLING, 1, false);
      this.loadTower(TowerBase.TowerType.BULLET, 2, false);
      this.loadTower(TowerBase.TowerType.SPRAY, 4, false);
      this.loadTower(TowerBase.TowerType.GOD_BOW, 0, false);
      this.loadTower(TowerBase.TowerType.CORRUPTED_FIRE_BOW, 0, false);
      this.loadTower(TowerBase.TowerType.CORRUPTED_ICE_BOW, 0, false);
      this.loadTower(TowerBase.TowerType.CORRUPTED_POISON_BOW, 0, false);
      this.loadTower(TowerBase.TowerType.CORRUPTED_LIGHTNING_BOW, 0, false);
      this.loadTower(TowerBase.TowerType.BALLISTA, 0, true);
   }

   private void loadTower(TowerBase.TowerType type, int row, boolean largeTower) {
      int size = 32;
      int posX = 0;
      if (largeTower) {
         size = 48;
         posX = 256;
      }

      int posY = size * 4 * row;
      this.spriteSheets.put(type, new SpriteSheet(this.towerBaseSpriteSheet.getSubImage(posX, posY, 8 * size, 4 * size), size, size));
   }

   public Image getBaseSpriteSheet() throws SlickException {
      return this.towerBaseSpriteSheet;
   }

   public TowerSprite createNewTowerSprite(TowerBase.TowerType type) throws SlickException {
      return this.spriteTemplates.containsKey(type) ? new TowerSprite(this.spriteSheets.get(type), this.spriteTemplates.get(type)) : null;
   }
}
