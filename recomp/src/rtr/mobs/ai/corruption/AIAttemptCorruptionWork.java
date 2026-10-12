package rtr.mobs.ai.corruption;

import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.mobs.ai.AIBase;
import rtr.utilities.Utilities;

public class AIAttemptCorruptionWork extends AIBase {
   public AIAttemptCorruptionWork(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_ATTEMPT_CORRUPTION_WORK;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowCorruptionWork();
   }

   @Override
   public boolean tryRun() {
      return !this.allowRun() ? false : Utilities.randomInt(this.thisMobFlags.getCorruptionWorkChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         this.switchNode(this.thisMob.getAICorruptionClearTopographyToBuildWork());
      }
   }
}
