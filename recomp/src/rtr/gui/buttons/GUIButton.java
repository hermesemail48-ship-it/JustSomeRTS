package rtr.gui.buttons;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import rtr.ModuleBase;
import rtr.PerkModule;
import rtr.SoundModule;
import rtr.font.FontModule;
import rtr.mobs.MigrationModule;
import rtr.mobs.MobModule;
import rtr.mobs.jobs.MobJobModule;
import rtr.particles.ParticleModule;
import rtr.particles.projectiles.ProjectileModule;
import rtr.resources.ResourceModule;
import rtr.states.StateBase;

public class GUIButton {
   protected FontModule font = (FontModule)StateBase.getModule(ModuleBase.ModuleType.FONT);
   protected ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   protected ProjectileModule projectile = (ProjectileModule)StateBase.getModule(ModuleBase.ModuleType.PROJECTILE);
   protected ResourceModule resource = (ResourceModule)StateBase.getModule(ModuleBase.ModuleType.RESOURCE);
   protected MobModule mob = (MobModule)StateBase.getModule(ModuleBase.ModuleType.MOB);
   protected MigrationModule migration = (MigrationModule)StateBase.getModule(ModuleBase.ModuleType.MIGRATION);
   protected PerkModule perk = (PerkModule)StateBase.getModule(ModuleBase.ModuleType.PERK);
   protected SoundModule sound = (SoundModule)StateBase.getModule(ModuleBase.ModuleType.SOUND);
   protected MobJobModule mobJob = (MobJobModule)StateBase.getModule(ModuleBase.ModuleType.MOB_JOB);
   protected boolean disabled;
   protected boolean isSelected;
   protected Rectangle intersectBox;
   protected String toolTipTitle;
   protected String toolTipDescription;
   protected GUIToolTipOverlay.ToolTipPosition toolTipPosition = GUIToolTipOverlay.ToolTipPosition.RIGHT;
   protected String buttonName = "none";
   protected boolean flashing = false;
   protected int flashAlpha;
   protected boolean flashUp;
   protected GUIToolTipOverlay toolTip;
   private float baseBoxW = -1.0F;
   private float baseBoxH;

   public GUIButton() throws SlickException {
   }

   public void render(Graphics g, int x, int y, boolean d) {
      this.disabled = d;
      this.intersectBox.setX(x + 3);
      this.intersectBox.setY(y + 3);
      if (this.toolTip != null) {
         this.toolTip.intersectBox.setX(x + 3);
         this.toolTip.intersectBox.setY(x + 3);
      }

      if (this.flashing) {
         if (this.flashUp) {
            if (this.flashAlpha < 200) {
               this.flashAlpha += 10;
            } else {
               this.flashUp = false;
            }
         } else if (this.flashAlpha > 0) {
            this.flashAlpha -= 10;
         } else {
            this.flashUp = true;
         }
      }
   }

   public void hideButton() {
      this.intersectBox.setX(-10000.0F);
      this.intersectBox.setY(-10000.0F);
   }

   /**
    * Phone port: after the button was drawn under a scale of {@code s} about (ox, oy), moves its hit box to where it
    * appeared. The unscaled size is kept so this can run every frame.
    */
   public void scaleIntersectBox(float ox, float oy, float s) {
      if (this.intersectBox.getX() <= -9000.0F) {
         return;
      }

      if (this.baseBoxW < 0.0F) {
         this.baseBoxW = this.intersectBox.getWidth();
         this.baseBoxH = this.intersectBox.getHeight();
      }

      this.intersectBox.setBounds(
         ox + (this.intersectBox.getX() - ox) * s, oy + (this.intersectBox.getY() - oy) * s, this.baseBoxW * s, this.baseBoxH * s
      );
   }

   public boolean intersects(Rectangle mouse) {
      return this.intersects(mouse, false);
   }

   public boolean intersects(Rectangle mouse, boolean ignoreDisabled) {
      return !ignoreDisabled && this.disabled ? false : this.intersectBox.intersects(mouse);
   }

   public void setToolTip(String title) throws SlickException {
      this.toolTip = new GUIToolTipOverlay();
      this.toolTipTitle = title;
   }

   public void setToolTip(String title, GUIToolTipOverlay.ToolTipPosition toolTipPosition) throws SlickException {
      this.toolTip = new GUIToolTipOverlay();
      this.toolTipTitle = title;
      this.toolTipPosition = toolTipPosition;
   }

   public void setToolTip(String title, String description) throws SlickException {
      this.toolTip = new GUIToolTipOverlay();
      this.toolTipTitle = title;
      this.toolTipDescription = description;
   }

   public void setToolTip(String title, String description, GUIToolTipOverlay.ToolTipPosition toolTipPosition) throws SlickException {
      this.toolTip = new GUIToolTipOverlay();
      this.toolTipTitle = title;
      this.toolTipDescription = description;
      this.toolTipPosition = toolTipPosition;
   }

   public void renderDescriptionToolTip(Rectangle mouse, Graphics g) throws SlickException {
      if (mouse.intersects(this.intersectBox)) {
         if (this.toolTip != null) {
            this.toolTip.renderToolTip(g, mouse, this.toolTipPosition, this.toolTipTitle, this.toolTipDescription, false);
         }
      }
   }

   public String getButtonName() {
      return this.buttonName;
   }

   public void select() {
      this.isSelected = true;
   }

   public void deselect() {
      this.isSelected = false;
   }

   public boolean isSelected() {
      return this.isSelected;
   }

   public void setFlashing(boolean f) {
      this.flashing = f;
   }

   public void drawToDisk() throws SlickException {
   }

   public void setIntersectBoxSize(int x, int y) {
      this.intersectBox.setWidth(x);
      this.intersectBox.setHeight(y);
   }

   public Rectangle getIntersectBox() {
      return this.intersectBox;
   }
}
