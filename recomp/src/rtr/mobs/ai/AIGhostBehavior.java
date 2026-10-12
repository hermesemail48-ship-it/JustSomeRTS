package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.TimeModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.map.MapData;
import rtr.mobs.MobBase;
import rtr.particles.ParticleModule;
import rtr.resources.ResourceModule;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIGhostBehavior extends AIBase {
   private static AIBase resetBranch;

   public AIGhostBehavior(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_GHOST_BEHAVIOR;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowGhostBehavior();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getGhostBehaviorChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         if (this.time.getTimeOfDay() != TimeModule.TimeOfDay.DUSK && this.time.getTimeOfDay() != TimeModule.TimeOfDay.NIGHT && !this.time.isEclipse()) {
            if (Utilities.randomInt(this.thisMobFlags.getGhostFadeOutChance()) == 0) {
               this.thisMob.setNoSelect(true);
               this.thisMob.fadeOut();
            }
         } else if (Utilities.randomInt(this.thisMob.isBoundGhost() ? this.thisMobFlags.getGhostFadeInChance() / 10 : this.thisMobFlags.getGhostFadeInChance())
               == 0
            || this.time.isFullMoon() && Utilities.randomInt(this.thisMobFlags.getGhostFullMoonFadeChance()) == 0
            || this.time.isEclipse() && Utilities.randomInt(this.thisMobFlags.getGhostEclipseFadeChance()) == 0
            || this.time.isBloodMoon() && Utilities.randomInt(this.thisMobFlags.getGhostBloodMoonFadeChance()) == 0) {
            this.thisMob.setNoSelect(false);
            this.thisMob.fadeIn();
         }

         if (!this.thisMob.isBoundGhost() && this.thisMob.getAlpha() <= 0.0F && Utilities.randomInt(this.thisMobFlags.getGhostLeaveChance() / 100) == 0) {
            Text.setVariableText("mobName", this.thisMob.getFormattedName(false));
            Console.out(Text.getText("console.panel.ghostLeave"), false);
            int centerPixelX = this.thisMob.getCenterPixelX(true);
            int centerPixelY = this.thisMob.getCenterPixelY(true);
            int tileX = this.thisMob.getTileX();
            int tileY = this.thisMob.getTileY();
            this.projectile
               .newExpandingEnergyRing(centerPixelX, centerPixelY, 128, 0.1F, ParticleModule.ParticleSet.MAGIC_BLUE, ParticleModule.ParticleSet.BLACK);

            for (int i = 0; i < 500; i++) {
               this.particle
                  .newParticleGroup(centerPixelX, centerPixelY, ParticleModule.ParticleGroup.FLOATING_MAGIC_RINGS, ParticleModule.ParticleSet.MAGIC_PURPLE);
            }

            for (int i = 0; i < 25; i++) {
               this.essence.newEssence(centerPixelX, centerPixelY, Utilities.randomInt(5) + 1, Utilities.randomInt(5) + 1);
            }

            int amount = Utilities.randomInt(5) + 1;

            for (int i = 0; i < amount; i++) {
               if (!this.resource.isResourcesOnGround(tileX, tileY) && this.map.getMapData().getBlockMap(MapData.BlockMapGroup.STANDARD)[tileX][tileY] == 0) {
                  this.resource.createResourceOnGround(ResourceModule.ResourceType.GHOST_DUST, ResourceModule.ResourceColorSet.DEFAULT, tileX, tileY);
               } else {
                  OrderedPair o = this.resource.findOpenSpace(tileX, tileY, MapData.BlockMapGroup.STANDARD);
                  this.resource.createResourceOnGround(ResourceModule.ResourceType.GHOST_DUST, ResourceModule.ResourceColorSet.DEFAULT, o.getX(), o.getY());
               }
            }

            this.thisMob.unassignFromAll(false);
            this.mob.deleteMob(this.thisMob);
         }

         this.switchNode(resetBranch);
      }
   }
}
