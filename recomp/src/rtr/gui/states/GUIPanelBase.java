package rtr.gui.states;

import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.DataModule;
import rtr.GodModule;
import rtr.ModuleBase;
import rtr.ProfileModule;
import rtr.SoundModule;
import rtr.StatsModule;
import rtr.TimeModule;
import rtr.font.FontModule;
import rtr.goal.GoalModule;
import rtr.gui.buttons.GUIButton;
import rtr.help.HelpModule;
import rtr.influence.InfluenceModule;
import rtr.map.MapModule;
import rtr.map.MiniMapModule;
import rtr.missiles.MissileModule;
import rtr.mobs.MigrationModule;
import rtr.mobs.MobModule;
import rtr.mobs.jobs.MobJobModule;
import rtr.objects.ObjectModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.resources.TradeModule;
import rtr.save.SaveModule;
import rtr.spawning.CorruptionModule;
import rtr.spawning.SpawnModule;
import rtr.states.StateBase;
import rtr.weather.WeatherModule;

public class GUIPanelBase {
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected SpawnModule spawn = (SpawnModule)StateBase.getModule(ModuleBase.ModuleType.SPAWN);
   protected ObjectModule object = (ObjectModule)StateBase.getModule(ModuleBase.ModuleType.OBJECT);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   protected MissileModule missile = (MissileModule)StateBase.getModule(ModuleBase.ModuleType.MISSILE);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected TradeModule trade = (TradeModule)StateBase.getModule(ModuleBase.ModuleType.TRADE);
   protected MapModule map = (MapModule)StateBase.getModule(ModuleBase.ModuleType.MAP);
   protected MiniMapModule miniMap = (MiniMapModule)StateBase.getModule(ModuleBase.ModuleType.MINI_MAP);
   protected TimeModule time = (TimeModule)StateBase.getModule(ModuleBase.ModuleType.TIME);
   protected WeatherModule weather = (WeatherModule)StateBase.getModule(ModuleBase.ModuleType.WEATHER);
   protected InfluenceModule influence = (InfluenceModule)StateBase.getModule(ModuleBase.ModuleType.INFLUENCE);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected HelpModule help = (HelpModule)StateBase.getModule(ModuleBase.ModuleType.HELP);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected SaveModule save = (SaveModule)StateBase.getModule(ModuleBase.ModuleType.SAVE);
   protected DataModule data = (DataModule)StateBase.getModule(ModuleBase.ModuleType.DATA);
   protected ProfileModule profile = (ProfileModule)StateBase.getModule(ModuleBase.ModuleType.PROFILE);
   protected StatsModule stats = (StatsModule)StateBase.getModule(ModuleBase.ModuleType.STATS);
   protected MigrationModule migration = (MigrationModule)StateBase.getModule(ModuleBase.ModuleType.MIGRATION);
   protected GodModule god = (GodModule)StateBase.getModule(ModuleBase.ModuleType.GOD);
   protected CorruptionModule corruption = (CorruptionModule)StateBase.getModule(ModuleBase.ModuleType.CORRUPTION);
   protected GoalModule goal = (GoalModule)StateBase.getModule(ModuleBase.ModuleType.GOAL);
   protected Graphics g;
   protected GameContainer gc;
   protected Rectangle mouse;
   protected int x;
   protected int y;
   protected ArrayList<GUIButton> guiButtons = new ArrayList<>();
   protected ArrayList<Rectangle> masks = new ArrayList<>();

   public GUIPanelBase(Graphics g, GameContainer gc, Rectangle mouse) {
      this.g = g;
      this.gc = gc;
      this.mouse = mouse;
   }

   public void update() throws SlickException {
   }

   public void hideButtonsAndMasks() throws SlickException {
      this.hideButtons();
      this.hideMasks();
   }

   private void hideButtons() {
      for (int i = 0; i < this.guiButtons.size(); i++) {
         this.guiButtons.get(i).hideButton();
      }
   }

   private void hideMasks() {
      for (Rectangle r : this.masks) {
         r.setX(-10000.0F);
         r.setY(-10000.0F);
      }
   }

   public void renderDescriptionToolTip() throws SlickException {
      for (int i = 0; i < this.guiButtons.size(); i++) {
         this.guiButtons.get(i).renderDescriptionToolTip(this.mouse, this.g);
      }
   }

   public void renderMasks() {
      this.g.setColor(Color.yellow);

      for (Rectangle r : this.masks) {
         this.g.draw(r);
      }
   }

   public void select(String buttonName) {
      for (int i = 0; i < this.guiButtons.size(); i++) {
         if (this.guiButtons.get(i).getButtonName().equals(buttonName)) {
            this.guiButtons.get(i).select();
            break;
         }
      }
   }

   public void deselectAll() {
      for (int i = 0; i < this.guiButtons.size(); i++) {
         this.guiButtons.get(i).deselect();
      }
   }

   public boolean intersects(String buttonName) {
      for (int i = 0; i < this.guiButtons.size(); i++) {
         if (this.guiButtons.get(i).intersects(this.mouse) && this.guiButtons.get(i).getButtonName().equals(buttonName)) {
            return true;
         }
      }

      return false;
   }

   public String getIntersects() {
      for (int i = 0; i < this.guiButtons.size(); i++) {
         if (this.guiButtons.get(i).intersects(this.mouse)) {
            return this.guiButtons.get(i).getButtonName();
         }
      }

      return null;
   }

   public boolean intersectsGUIMask() {
      for (Rectangle r : this.masks) {
         if (r.intersects(this.mouse)) {
            return true;
         }
      }

      for (GUIButton b : this.guiButtons) {
         if (b.intersects(this.mouse)) {
            return true;
         }
      }

      return false;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getCenterX() {
      return 0;
   }

   public int getCenterY() {
      return 0;
   }

   public int getLeftX() {
      return this.x;
   }

   public int getTopY() {
      return this.y;
   }

   public int getRightX() {
      return 0;
   }

   public int getBottomY() {
      return 0;
   }
}
