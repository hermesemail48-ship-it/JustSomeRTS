package rtr.mobs.ai;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.font.Text;
import rtr.mobs.MobBase;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class AIChat extends AIBase {
   private static final int FIND_CHAT_PARTNER = 0;
   private static final int PATHFIND_TO_CHAT_PARTNER = 1;
   private static final int SET_CHAT_POSITION = 2;
   private static final int CHAT = 3;
   private static AIBase resetBranch;
   private static AIBase exitBranchVisitMate;
   private static AIBase exitBranchDomesticate;
   private static AIBase exitBranchCoitus;
   private static AIBase exitBranchWithResource;
   private static AIBase exitBranchTrade;
   private static AIBase exitBranch;

   public AIChat(MobBase m) {
      super(m);
      this.aiName = AIBase.AIName.AI_CHAT;
      this.interruptAll = true;
   }

   @Override
   public boolean allowRun() {
      return this.thisMobFlags.getAllowSocial();
   }

   @Override
   public boolean tryRun() {
      if (this.allowRun() && this.thisMob.getSocial() <= this.thisMobFlags.getSocialBoredLevel()) {
         int socialChatChanceModified = this.thisMobFlags.getSocialChatChance();
         if (this.thisMobFlags.getSocialChatPrefersVillage() && !this.object.isInRange(this.thisMob.getTileX(), this.thisMob.getTileY())) {
            socialChatChanceModified *= 10;
         }

         if (this.thisMob.getSocial() <= this.thisMobFlags.getSocialSadnessLevel()) {
            socialChatChanceModified = (int)(socialChatChanceModified * 0.1);
         } else if (this.thisMob.getSocial() <= this.thisMobFlags.getSocialLonelyLevel()) {
            socialChatChanceModified = (int)(socialChatChanceModified * 0.5);
         }

         return Utilities.randomInt(socialChatChanceModified) == 0;
      } else {
         return false;
      }
   }

   @Override
   public void execute() throws SlickException {
      super.execute();
      if (!this.interruptCheck()) {
         resetBranch = this.thisMob.getAIStart();
         exitBranchVisitMate = this.thisMob.getAIVisitMate();
         exitBranchDomesticate = this.thisMob.getAIWildDoggoDomesticate();
         exitBranchCoitus = this.thisMob.getAIMateGoCoitus();
         exitBranchWithResource = this.thisMob.getAICaptureAnimalsInHand();
         exitBranchTrade = this.thisMob.getAITrade();
         exitBranch = this.thisMob.getAIWander();
         switch (this.aiStep) {
            case 0:
               this.findChatPartner();
               break;
            case 1:
               this.pathfindToChatPartner();
               break;
            case 2:
               this.setChatPosition();
               break;
            case 3:
               this.chat();
         }
      }
   }

   private void findChatPartner() throws SlickException {
      if (this.thisMob.getAssignment().getPrimaryMob() != null) {
         this.requestPath(this.thisMob.getAssignment().getPrimaryMob().getTileX(), this.thisMob.getAssignment().getPrimaryMob().getTileY());
         this.aiStep = 1;
      } else {
         ArrayList<MobBase> mobList = this.mob.getMobsNearby(this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getSocialChatSearchRadius());
         mobList = this.mob
            .cullMobsByDistance(
               this.thisMob.getTileX(), this.thisMob.getTileY(), this.thisMobFlags.getSocialChatSearchRadius(), mobList, this.thisMobFlags.getBlockMapGroup()
            );
         mobList.remove(this.thisMob);

         for (int i = mobList.size() - 1; i >= 0; i--) {
            MobBase mobCheck = mobList.get(i);
            if (!mobCheck.getMobFlags().getAllowSocial()
               || !this.thisMob.canReach(mobCheck.getTileX(), mobCheck.getTileY())
               || !mobCheck.isAvailable()
               || mobCheck.getAssignment().getPrimaryMob() != null) {
               mobList.remove(mobCheck);
            }

            if (mobCheck.getActiveNode() != mobCheck.getAIStart() && Utilities.randomInt(this.thisMobFlags.getSocialChatInterruptChance()) != 0) {
               mobList.remove(mobCheck);
            }
         }

         MobBase chatTarget = this.thisMob.getAssignment().getPrimaryMob();
         if (mobList.size() > 0) {
            OrderedPair checkForChatTarget = this.pathFinder
               .searchForMob(this.thisMob.getTileX(), this.thisMob.getTileY(), mobList, false, true, this.thisMobFlags.getBlockMapGroup());
            if (checkForChatTarget != null) {
               chatTarget = this.pathFinder.mobFetch(mobList, checkForChatTarget.getX(), checkForChatTarget.getY());
               this.thisMob.getAssignment().setPrimaryMob(chatTarget);
               chatTarget.interruptNode(chatTarget.getAIChat());
               chatTarget.getAssignment().setPrimaryMob(this.thisMob);
               this.requestPath(chatTarget.getTileX(), chatTarget.getTileY());
               this.aiStep = 1;
            } else {
               this.switchNode(resetBranch);
            }
         } else {
            this.switchNode(resetBranch);
         }
      }
   }

   private void pathfindToChatPartner() throws SlickException {
      Text.setVariableText("mobName", this.thisMob.getAssignment().getPrimaryMobName());
      this.thisMob.setCurrentTask(Text.getText("aiTask.chat.pathfindToChatPartner"));
      if (this.thisMob.getAssignment().getPrimaryMob() == null) {
         this.thisMob.getAssignment().reset();
         this.aiStep = 0;
      } else {
         this.followMob(this.thisMob.getAssignment().getPrimaryMob());
         if (this.pathFailed) {
            this.thisMob.getAssignment().reset();
            this.aiStep = 0;
         } else {
            if (this.pathCompleted) {
               this.aiStep = 2;
            }
         }
      }
   }

   private void setChatPosition() {
      MobBase chatTarget = this.thisMob.getAssignment().getPrimaryMob();
      float mobX = this.thisMob.getMobX();
      float mobY = this.thisMob.getMobY();
      float directionX = Math.abs(chatTarget.getMobX() - mobX);
      float directionY = Math.abs(chatTarget.getMobY() - mobY);
      if (directionX > directionY) {
         if (mobX > chatTarget.getMobX()) {
            this.thisMob.setPosition(MobBase.MobPosition.WEST);
         } else {
            this.thisMob.setPosition(MobBase.MobPosition.EAST);
         }
      } else if (mobY > chatTarget.getMobY()) {
         this.thisMob.setPosition(MobBase.MobPosition.NORTH);
      } else {
         this.thisMob.setPosition(MobBase.MobPosition.SOUTH);
      }

      this.aiStep = 3;
   }

   private void chat() throws SlickException {
      MobBase chatTarget = this.thisMob.getAssignment().getPrimaryMob();
      float mobX = this.thisMob.getMobX();
      float mobY = this.thisMob.getMobY();
      this.thisMob.setPositionFace();
      Text.setVariableText("mobName", chatTarget.getFormattedName(true));
      this.thisMob.setCurrentTask(Text.getText("aiTask.chat.chat"));
      this.thisMob.runSocialRateTick(20);
      if (Utilities.randomInt(200) == 0) {
         int bubbleType = Utilities.randomInt(69) + 1;
         if (Utilities.randomInt(5) == 0) {
            this.font.newFloater("iconChatBubbleRed" + bubbleType, mobX, mobY - 16.0F, true);
         } else if (Utilities.randomInt(5) == 0 && chatTarget.getGender() != this.thisMob.getGender()) {
            this.font.newFloater("iconChatFlirting", mobX + 5.0F, mobY - 16.0F, true);
         } else if (Utilities.randomInt(5) == 0) {
            this.font.newFloater("iconChatBubbleGreen" + bubbleType, mobX, mobY - 16.0F, true);
         } else {
            this.font.newFloater("iconChatBubble" + bubbleType, mobX, mobY - 16.0F, true);
         }
      }

      if (this.thisMobFlags.getSocialChatShareWisdom()
         && this.thisMob.getLevel() > chatTarget.getLevel()
         && Utilities.randomInt(this.thisMobFlags.getSocialChatShareWisdomChance()) == 0) {
         chatTarget.awardXP(10);
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.02, GodModule.GodXPType.ELDERS_SHARING_WISDOM);
         }
      }

      if (this.thisMobFlags.getSocialChatShareFaith()
         && chatTarget.getMobFlags().getAllowFaith()
         && this.thisMob.getFaith() / 2 > chatTarget.getFaith()
         && Utilities.randomInt(this.thisMobFlags.getSocialChatShareFaithChance()) == 0) {
         chatTarget.increaseFaith(1, true);
         if (this.thisMobFlags.getAllowGodExperience()) {
            this.god.increaseGodXP(0.02, GodModule.GodXPType.ELDERS_SHARING_WISDOM);
         }
      }

      if (this.thisMobFlags.canSocialMate()
         && chatTarget.getMobFlags().canSocialMate()
         && Utilities.randomInt(this.thisMobFlags.getSocialMateChance()) == 0
         && this.thisMob.canReach(chatTarget.getTileX(), chatTarget.getTileY())
         && chatTarget.getMate() == null
         && this.thisMob.getMate() == null
         && chatTarget.getGender() != this.thisMob.getGender()) {
         this.thisMob.setMate(chatTarget);
         chatTarget.setMate(this.thisMob);
         if (this.thisMobFlags.getAllowFaith()) {
            this.thisMob.increaseFaith(10, true);
         }

         this.switchNode(exitBranchVisitMate);
         this.thisMob.getMate().interruptNode(this.thisMob.getMate().getAIStart());
      }

      if (this.thisMobFlags.canSocialHaveOwner()
         && chatTarget.getMobFlags().canSocialBeOwner()
         && Utilities.randomInt(this.thisMobFlags.getSocialOwnerChance()) == 0
         && this.thisMob.canReach(chatTarget.getTileX(), chatTarget.getTileY())
         && chatTarget.getOwner() == null
         && this.thisMob.getOwner() == null) {
         this.thisMob.setOwner(chatTarget);
         chatTarget.setOwner(this.thisMob);
         if (this.thisMobFlags.getAllowFaith()) {
            this.thisMob.increaseFaith(10, true);
         }

         if (this.thisMob.getAIWildDoggoDomesticate().tryRun()) {
            this.switchNode(exitBranchDomesticate);
            this.thisMob.getOwner().interruptNode(this.thisMob.getOwner().getAIStart());
            return;
         }
      }

      if (this.thisMob.getMate() != null
         && chatTarget == this.thisMob.getMate()
         && chatTarget.getMate() == this.thisMob
         && this.thisMob.getAIMateGoCoitus().tryRun()
         && this.thisMob.getMate().getAIMateGoCoitusOther().tryRun()) {
         this.switchNode(exitBranchCoitus);
         this.thisMob.getMate().interruptNode(this.thisMob.getMate().getAIMateGoCoitusOther());
      } else {
         int socialChatBreakawayChanceModified = this.thisMobFlags.getSocialChatBreakawayChance();
         if (this.thisMobFlags.getSocialChatPrefersVillage() && !this.object.isInRange(this.thisMob.getTileX(), this.thisMob.getTileY())) {
            socialChatBreakawayChanceModified = (int)(socialChatBreakawayChanceModified * 0.3);
         }

         if (this.thisMob.getSocial() == this.thisMob.getSocialMax() && chatTarget.getSocial() == chatTarget.getSocialMax()) {
            socialChatBreakawayChanceModified = (int)(socialChatBreakawayChanceModified * 0.8);
         } else if (this.thisMob.getSocial() > this.thisMobFlags.getSocialBoredLevel()
            && chatTarget.getSocial() > chatTarget.getMobFlags().getSocialBoredLevel()) {
            socialChatBreakawayChanceModified = (int)(socialChatBreakawayChanceModified * 0.5);
         } else if (this.thisMob.getSocial() < this.thisMobFlags.getSocialLonelyLevel()
            || chatTarget.getSocial() < chatTarget.getMobFlags().getSocialLonelyLevel()) {
            socialChatBreakawayChanceModified = (int)(socialChatBreakawayChanceModified * 2.5);
         } else if (this.thisMob.getSocial() < this.thisMobFlags.getSocialSadnessLevel()
            || chatTarget.getSocial() < chatTarget.getMobFlags().getSocialSadnessLevel()) {
            socialChatBreakawayChanceModified = (int)(socialChatBreakawayChanceModified * 3.5);
         }

         if (Utilities.randomInt(socialChatBreakawayChanceModified) == 0 || chatTarget.getAssignment().getPrimaryMob() == null) {
            if (!this.thisMob.hasCarriedResources()) {
               if (this.thisMobFlags.canSocialTrade()
                  && chatTarget.getMobFlags().canSocialTrade()
                  && Utilities.randomInt(this.thisMobFlags.getSocialTradeChance()) == 0) {
                  this.switchNode(exitBranchTrade);
                  return;
               }

               this.switchNode(exitBranch);
               return;
            }

            this.switchNode(exitBranchWithResource);
         }
      }
   }
}
