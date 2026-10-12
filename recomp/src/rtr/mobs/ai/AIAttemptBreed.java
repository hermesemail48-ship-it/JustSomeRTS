package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.Assignment;
import rtr.utilities.Utilities;

public class AIAttemptBreed extends AIBase {
   private static final int FIND_MATE = 0;
   private static final int PATHFIND_TO_MATE = 1;
   private static final int VISIT_WITH_MATE = 2;
   private static AIBase resetBranch;
   private static AIBase exitBranch;

   public AIAttemptBreed(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_VISIT_MATE;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowSocial()
         && this.thisMobFlags.canSocialBreed()
         && this.thisMob.getSocialCoitusDelayTick() >= this.thisMobFlags.getSocialCoitusDelay();
   }

   @Override
   public boolean tryRun() {
      return this.allowRun() && Utilities.randomInt(this.thisMobFlags.getSocialBreedChance()) == 0;
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranch = this.thisMob.getAIChat();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.findMate();
                  break;
               case 1:
                  this.pathfindToMate();
                  break;
               case 2:
                  this.visitWithMate();
                  return;
            }
         }
      }
   }

   private void findMate() throws SlickException {
      ArrayList<MobBase> validMates = new ArrayList<>();
      if (this.thisMob.getAssignedHome() != -1) {
         for (MobBase m : this.object.getObjects().get(this.thisMob.getAssignedHome()).getOccupants()) {
            if (m.isAvailable()
               && this.thisMob.canReach(m)
               && this.thisMob.getMobType() == m.getMobType()
               && this.thisMob.getGender() != m.getGender()
               && m.getSocialCoitusDelayTick() >= m.getMobFlags().getSocialCoitusDelay()) {
               validMates.add(m);
            }
         }
      }

      if (validMates.size() == 0) {
         for (MobBase mx : this.mob.getMobsNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), 8)) {
            if (mx.isAvailable()
               && this.thisMob.canReach(mx)
               && this.thisMob.getMobType() == mx.getMobType()
               && this.thisMob.getGender() != mx.getGender()
               && mx.getSocialCoitusDelayTick() >= mx.getMobFlags().getSocialCoitusDelay()) {
               validMates.add(mx);
            }
         }
      }

      if (validMates.size() == 0) {
         this.switchNode(resetBranch);
      } else {
         MobBase selectedMate = validMates.get(Utilities.randomInt(validMates.size()));
         Assignment assignment = this.thisMob.getAssignment();
         assignment.setPrimaryMob(selectedMate);
         assignment.setAssignmentType(Assignment.AssignmentType.BREEDING);
         this.requestPath(selectedMate.getTileX(), selectedMate.getTileY());
         this.aiStep = 1;
      }
   }

   private void pathfindToMate() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMob().getFormattedName(false));
      this.thisMob.setCurrentTask(Text.getText("aiTask.attemptBreed.pathfindToMate"));
      this.followMob(this.thisMob.getAssignment().getPrimaryMob());
      if (this.pathFailed) {
         this.switchNode(resetBranch);
      } else {
         if (this.pathCompleted) {
            this.aiStep = 2;
         }
      }
   }

   private void visitWithMate() throws SlickException {
      float mobX = this.thisMob.getMobX();
      float mobY = this.thisMob.getMobY();
      MobBase mate = this.thisMob.getAssignment().getPrimaryMob();
      this.breed(this.thisMob, mate);
      this.breed(mate, this.thisMob);
      if (this.thisMobFlags.getAllowGodExperience()) {
         this.god.increaseGodXP(0.03, GodModule.GodXPType.BREEDING);
      }

      this.switchNode(exitBranch);
   }

   private void breed(MobBase m, MobBase mate) throws SlickException {
      for (int i = 0; i < 2; i++) {
         this.font.newFloater("iconHitPoints", m.getMobX() + 4.0F, m.getMobY());
      }

      if (m.getSocialPregnantDelayTick() >= m.getMobFlags().getSocialPregnantDelay()
         && Utilities.randomInt(m.getMobFlags().getSocialPregnantChance()) == 0
         && !m.isPregnant()
         && m.getSocialPregnantCount() < m.getMobFlags().getSocialPregnantMaximum()
         && m.getGender() == MobBase.MobGender.FEMALE
         && mate.getGender() == MobBase.MobGender.MALE) {
         m.increaseSocialPregnantCount(1);
         m.setPregnant(true);
         m.setSocialChildFather(mate);
         this.god.increaseGodXP(2.0, GodModule.GodXPType.ANIMAL_IMPREGNATED);
         this.font.newFloater("iconPregnant", m.getMobX() + 4.0F, m.getMobY());
      }

      if (m.getMobFlags().getAllowSleep()) {
         m.decreaseEnergy(m.getMobFlags().getSocialCoitusEnergyUse());
      }

      if (m.getMobFlags().getAllowHunger()) {
         m.decreaseHunger(m.getMobFlags().getSocialCoitusHungerUse());
      }

      m.resetSocialCoitusDelayTick();
      m.awardXP(Utilities.randomInt(20) + 20);
   }
}
