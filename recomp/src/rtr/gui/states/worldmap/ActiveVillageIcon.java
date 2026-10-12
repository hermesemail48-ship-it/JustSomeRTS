package rtr.gui.states.worldmap;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ImageLoader;
import rtr.ModuleBase;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;
import rtr.utilities.Utilities;

public class ActiveVillageIcon {
   private ParticleModule particle = (ParticleModule)StateBase.getModule(ModuleBase.ModuleType.PARTICLE);
   private Image arrow;
   private boolean canBounce;
   private float x;
   private float y;
   private float bounceSpeed = 0.0F;
   private float bounce = 0.0F;

   public ActiveVillageIcon(float x, float y, int type) throws SlickException {
      switch (type) {
         case 0:
            this.arrow = ImageLoader.getImage("res/GUI/worldMap/activeVillageArrow.png");
            this.bounce = (50 + Utilities.randomInt(150)) * -1;
            this.x = x - this.arrow.getWidth() / 2;
            this.y = y - this.arrow.getHeight() / 2 - 10.0F;
            this.canBounce = true;
            break;
         case 1:
            this.arrow = ImageLoader.getImage("res/GUI/worldMap/newVillageArrow.png");
            this.bounce = (50 + Utilities.randomInt(150)) * -1;
            this.x = x - this.arrow.getWidth() / 2;
            this.y = y - this.arrow.getHeight() / 2 - 10.0F;
            this.canBounce = true;
            break;
         case 2:
            this.arrow = ImageLoader.getImage("res/GUI/worldMap/lostVillage.png");
            this.x = x - this.arrow.getWidth() / 2;
            this.y = y - this.arrow.getHeight() / 2;
      }
   }

   public void render(float mapX, float mapY) {
      if (this.canBounce) {
         this.bounceSpeed += 0.025F;
         this.bounce = this.bounce + this.bounceSpeed;
         if (this.bounce >= 0.0F) {
            this.bounceSpeed = -0.5F;
         }
      } else {
         this.particle
            .newParticleGroup(
               this.x + Utilities.randomInt(128) - 54.0F,
               this.y + Utilities.randomInt(128) - 48.0F,
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.FIRE_RED,
               ParticleModule.ParticleSet.FIRE_RED
            );
         this.particle
            .newParticleGroup(
               this.x + Utilities.randomInt(128) - 54.0F,
               this.y + Utilities.randomInt(128) - 48.0F,
               ParticleModule.ParticleGroup.SMOKE_BELLOW,
               ParticleModule.ParticleSet.BLACK,
               ParticleModule.ParticleSet.BLACK
            );
         this.particle
            .newParticleGroup(
               this.x + Utilities.randomInt(128) - 54.0F,
               this.y + Utilities.randomInt(128) - 48.0F,
               ParticleModule.ParticleGroup.FIRE,
               ParticleModule.ParticleSet.FIRE_RED
            );
         this.particle
            .newParticleGroup(
               this.x + Utilities.randomInt(128) - 54.0F,
               this.y + Utilities.randomInt(128) - 48.0F,
               ParticleModule.ParticleGroup.FIRE,
               ParticleModule.ParticleSet.BLACK
            );

         for (int i = 0; i < 12; i++) {
            if (Utilities.randomInt(5) == 0) {
               this.particle
                  .newParticleGroup(
                     this.x + 11.0F + Utilities.randomInt(3), this.y + 14.0F, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED
                  );
            }

            if (Utilities.randomInt(5) == 0) {
               this.particle
                  .newParticleGroup(
                     this.x + 20.0F + Utilities.randomInt(3), this.y + 14.0F, ParticleModule.ParticleGroup.FIRE, ParticleModule.ParticleSet.FIRE_RED
                  );
            }
         }
      }

      this.arrow.draw(this.x + mapX + 6.0F, this.y + mapY + 6.0F + this.bounce);
   }
}
