package rtr.mobs.ai;

import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.PerkModule;
import rtr.console.Console;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.utilities.Utilities;

public class AIMateCoitus extends AIBase {
   private static final int AWAIT_COITUS = 0;
   private static final int FINISH_COITUS = 1;
   private static AIBase exitBranchWander;
   private static AIBase exitBranchInHome;

   public AIMateCoitus(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_MATE_COITUS;
      this.interruptDeath = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowSocial() && this.thisMob.getMate() != null;
   }

   @Override
   public boolean tryRun() {
      return this.allowRun();
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         exitBranchInHome = this.thisMob.getAIInHome();
         exitBranchWander = this.thisMob.getAIWander();
         if (this.runDropResource()) {
            switch (this.aiStep) {
               case 0:
                  this.awaitCoitus();
                  break;
               case 1:
                  this.finishCoitus();
            }
         }
      }
   }

   private void awaitCoitus() throws SlickException {
      MobBase mate = this.thisMob.getMate();
      if (mate != null
         && (
            AIMateCoitus.class.isAssignableFrom(mate.getActiveNode().getClass())
               || AIMateGoCoitus.class.isAssignableFrom(mate.getActiveNode().getClass())
               || AIMateGoCoitusOther.class.isAssignableFrom(mate.getActiveNode().getClass())
         )) {
         Text.setVariableText("mobName", mate.getFormattedName(true));
         this.thisMob.setCurrentTask(Text.getText("aiTask.mateCoitus.awaitCoitusWaiting"));
         float mobX = this.thisMob.getMobX();
         float mobY = this.thisMob.getMobY();
         int tileX = this.thisMob.getTileX();
         int tileY = this.thisMob.getTileY();
         int mateX = mate.getTileX();
         int mateY = mate.getTileY();
         ObjectBase thisBuilding = this.object.getBuildingAtLocation(tileX, tileY);
         ObjectBase mateBuilding = this.object.getBuildingAtLocation(mateX, mateY);
         if (thisBuilding != null && mateBuilding != null && thisBuilding == mateBuilding && this.thisMob.isInsideBuilding() && mate.isInsideBuilding()) {
            Text.setVariableText("mobName", this.thisMob.getMate().getFormattedName(true));
            this.thisMob.setCurrentTask(Text.getText("aiTask.mateCoitus.awaitCoitus"));
            if (this.thisMob.getID() < mate.getID()) {
               Text.setVariableText("mobName", this.thisMob.getFormattedName(true));
               Text.setVariableText("mateMobName", mate.getFormattedName(true));
               Console.out(Text.getText("console.panel.coitus"), false);
            }

            for (int i = 0; i < 2; i++) {
               this.font.newFloater("iconHitPoints", mobX + 4.0F, mobY);
            }

            int modifiedSocialPregnantChance = this.thisMobFlags.getSocialPregnantChance();
            if (this.thisMob.getMobGroup() == MobBase.MobGroup.VILLAGER && this.perk.getPerkTypeTotal(PerkModule.PerkType.FERTILITY) > 0.0) {
               modifiedSocialPregnantChance = (int)(
                  modifiedSocialPregnantChance - modifiedSocialPregnantChance * this.perk.getPerkTypeTotal(PerkModule.PerkType.FERTILITY)
               );
            }

            if (this.thisMob.getGender() == MobBase.MobGender.FEMALE && mate.getGender() == MobBase.MobGender.MALE) {
               if (!this.thisMob.isSocialBlessed() && !mate.isSocialBlessed()) {
                  if (this.thisMob.getSocialPregnantDelayTick() >= this.thisMobFlags.getSocialPregnantDelay()
                     && Utilities.randomInt(modifiedSocialPregnantChance) == 0
                     && !this.thisMob.isPregnant()
                     && this.thisMob.getSocialPregnantCount() < this.thisMobFlags.getSocialPregnantMaximum()) {
                     this.impregnante(false);
                     if (this.thisMobFlags.getAllowFaith()) {
                        this.thisMob.increaseFaith(Utilities.randomInt(3, 5) + 5, true);
                        if (Utilities.randomInt(4) == 0) {
                           this.mob.influenceFaithInArea(tileX, tileY, 6, 1, true);
                        }
                     }

                     this.god.increaseGodXP(2.0, GodModule.GodXPType.VILLAGERS_IMPREGNATED);
                  }
               } else {
                  this.impregnante(true);
                  if (this.thisMobFlags.getAllowFaith()) {
                     this.thisMob.increaseFaith(Utilities.randomInt(8, 10) + 5, true);
                     this.mob.influenceFaithInArea(tileX, tileY, 6, 1, true);
                  }

                  this.god.increaseGodXP(4.0, GodModule.GodXPType.BLESSED_VILLAGERS_IMPREGNATED);
               }
            }

            this.stats.increaseVillagerCoitusThisPeriod(this.thisMob.getMobType());
            if (this.thisMobFlags.getAllowSleep()) {
               this.thisMob.decreaseEnergy(this.thisMobFlags.getSocialCoitusEnergyUse());
            }

            if (this.thisMobFlags.getAllowHunger()) {
               this.thisMob.decreaseHunger(this.thisMobFlags.getSocialCoitusHungerUse());
            }

            this.thisMob.resetSocialCoitusDelayTick();
            this.thisMob.awardXP(Utilities.randomInt(20) + 20);
            if (this.thisMobFlags.getAllowGodExperience()) {
               this.god.increaseGodXP(0.03, GodModule.GodXPType.COITUS);
            }

            this.aiStep = 1;
         }
      } else {
         this.thisMob.leaveBuilding();
         this.switchNode(this.thisMob.getAIWander());
      }
   }

   private void impregnante(boolean wasBlessed) throws SlickException {
      MobBase mate = this.thisMob.getMate();
      float mobX = this.thisMob.getMobX();
      float mobY = this.thisMob.getMobY();
      this.thisMob.setSocialBlessed(false);
      mate.setSocialBlessed(false);
      this.thisMob.increaseSocialPregnantCount(1);
      this.thisMob.setPregnant(true);
      double totalMaxFaith = this.thisMob.getFaithMax() + mate.getFaithMax();
      double totalFaith = this.thisMob.getFaith() + mate.getFaith();
      double percentFaith = Utilities.getPercentage(totalFaith, totalMaxFaith);
      if (percentFaith > 95.0) {
         this.thisMob.setSocialChildBlessed(true);
      }

      this.thisMob.setSocialChildFather(mate);
      this.stats.increaseVillagerNewPregnanciesThisPeriod(this.thisMob.getMobType());
      this.font.newFloater("iconPregnant", mobX + 4.0F, mobY);
      Text.setVariableText("mobName", this.thisMob.getFormattedName(true));
      if (wasBlessed) {
         Console.out(Text.getText("console.panel.isPregnantBlessed"), false);
      } else {
         Console.out(Text.getText("console.panel.isPregnant"), false);
      }
   }

   private void finishCoitus() throws SlickException {
      if (this.thisMob.getAssignedHome() == this.object.getBuildingAtLocation(this.thisMob.getTileX(), this.thisMob.getTileY()).getID()) {
         this.switchNode(exitBranchInHome);
      } else {
         this.thisMob.leaveBuilding();
         this.switchNode(exitBranchWander);
      }
   }
}
