package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIIncapacitated extends AIBase {
   private static final int DROP_RESOURCES = 0;
   private static final int INCAPACITATED = 1;
   private static AIBase exitBranch;

   public AIIncapacitated(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_INCAPACITATED;
      this.interruptDeath = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowIncapacitated();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         exitBranch = this.thisMob.getAIConfused();
         switch (this.aiStep) {
            case 0:
               this.dropResources();
               break;
            case 1:
               this.incapacitated();
         }
      }
   }

   private void dropResources() throws SlickException {
      while (this.thisMob.hasCarriedResources()) {
         OrderedPair openSpace = this.resource.findOpenSpace(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getBlockMapGroup());
         this.resource.setResourcesOnGround(openSpace.getX(), openSpace.getY(), this.thisMob.removeFirstCarriedResource());
         this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 3.0F);
      }

      this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 3.0F);
      this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 3.0F);
      this.font.newFloater("iconExcite", this.thisMob.getMobX() + 4.0F, this.thisMob.getMobY() - 3.0F);
      this.thisMob.setPositionDeath();
      this.thisMob.getSprite().restart();
      this.aiStep = 1;
   }

   private void incapacitated() throws SlickException {
      this.thisMob.setCurrentTask(Text.getText("aiTask.incapacitated.incapacitated"));
      this.thisMob.setPositionDeath();
      if (this.thisMobFlags.getAllowSleep()) {
         this.thisMob.runSleepDelayTick(1);
         if (this.thisMob.getSleepDelayTick() > this.thisMobFlags.getSleepDelay() * 2) {
            this.thisMob.increaseEnergy(1);
            this.thisMob.resetSleepDelayTick();
         }
      }

      if (Utilities.randomInt(this.thisMobFlags.getIncapacitatedBreakawayChance()) == 0) {
         this.switchNode(exitBranch);
      }
   }
}
