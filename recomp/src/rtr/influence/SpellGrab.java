package rtr.influence;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.GodModule;
import rtr.SoundModule;
import rtr.font.Text;
import rtr.goal.GoalModule;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.resources.ResourceBase;
import rtr.resources.ResourceModule;
import rtr.system.Game;
import rtr.utilities.Utilities;

public class SpellGrab extends SpellBase {
   @Override
   public void config(int x, int y, boolean freeCast) {
      this.type = SpellBase.SpellType.GRAB;
      this.name = Text.getText("spell.grab.name");
      this.description = Text.getText("spell.grab.description");
      this.castCost = freeCast ? 0 : 40;
      this.targetX = x;
      this.targetY = y;
      this.tileX = x / 16;
      this.tileY = y / 16;
   }

   @Override
   public void update() throws SlickException {
      switch (this.spellStep) {
         case 0:
            if (Utilities.isOutOfBoundsOfUsableMap(this.tileX, this.tileY)) {
               return;
            } else {
               MobBase mobSelection = this.mob.getMobAt(Game.getCS().getMouseRectangle(), false);
               if (mobSelection != null && mobSelection.isAvailable() && !mobSelection.getNoSelect()) {
                  mobSelection.getActiveNode().switchNode(mobSelection.getAIGrabbed());
                  if (mobSelection.getMobGroup() == MobBase.MobGroup.VILLAGER) {
                     this.goal.incrementGoal(GoalModule.GoalType.HANDS_OFF_ME);
                  }

                  this.influence.setMobGrabbed(mobSelection);
                  this.influence.setSelectedSpell(SpellBase.SpellType.DROP);
                  this.influence.removeSpell(this);
                  this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.GRABBING_CREATURES);
                  this.influence.decreaseInfluenceAmount(this.castCost);
                  if (mobSelection.getMobFlags().getAllowFaith()) {
                     mobSelection.decreaseFaith(Utilities.randomInt(5) + 1, true);
                  }

                  this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, 1, false);
                  this.mob.tryPanicInArea(this.tileX, this.tileY, 4, true, 1);
                  this.sound.playSound(SoundModule.SoundType.GRAB, this.targetX, this.targetY);
                  return;
               } else {
                  ResourceBase resourceSelection = this.resource.getResourceOnGround(this.tileX, this.tileY);
                  if (resourceSelection != null) {
                     this.resource.setResourceGrabbed(resourceSelection);
                     this.influence.setResourceGrabbed(resourceSelection);
                     this.influence.setSelectedSpell(SpellBase.SpellType.DROP);
                     this.influence.removeSpell(this);
                     this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.GRABBING_RESOURCES);
                     this.influence.decreaseInfluenceAmount(this.castCost);
                     this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, 1, true);
                     this.mob.tryConfuseInArea(this.tileX, this.tileY, 4, true, 1);
                     this.sound.playSound(SoundModule.SoundType.GRAB, this.targetX, this.targetY);
                     return;
                  } else {
                     ObjectBase objectSelection = this.object.getBuildingAtLocation(this.tileX, this.tileY);
                     if (objectSelection != null) {
                        ResourceModule.ResourceType t = this.influence.getGrabRotationResourceType();
                        if (objectSelection.isResourceAvailable(t)) {
                           resourceSelection = objectSelection.takeStores(t);
                           this.resource.setResourceGrabbed(resourceSelection);
                           this.influence.setResourceGrabbed(resourceSelection);
                           this.influence.setSelectedSpell(SpellBase.SpellType.DROP);
                           this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.GRABBING_RESOURCES);
                           this.influence.decreaseInfluenceAmount(this.castCost);
                           this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, 1, true);
                           this.mob.tryConfuseInArea(this.tileX, this.tileY, 4, true, 1);
                           this.sound.playSound(SoundModule.SoundType.GRAB, this.targetX, this.targetY);
                           this.influence.removeSpell(this);
                           this.influence.resetGrabRotationPeriodTick();
                           return;
                        }
                     }

                     if (this.resource.getResourceValues()[this.tileX][this.tileY] > 0 && !this.map.isTileFrozen(this.tileX, this.tileY)) {
                        resourceSelection = this.resource.grabResource(this.tileX, this.tileY);
                        this.resource.setResourceGrabbed(resourceSelection);
                        this.influence.setResourceGrabbed(resourceSelection);
                        this.influence.setSelectedSpell(SpellBase.SpellType.DROP);
                        this.influence.removeSpell(this);
                        this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.GRABBING_RESOURCES);
                        this.influence.decreaseInfluenceAmount(this.castCost);
                        this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, 1, true);
                        this.mob.tryConfuseInArea(this.tileX, this.tileY, 4, true, 1);
                        this.sound.playSound(SoundModule.SoundType.GRAB, this.targetX, this.targetY);
                        return;
                     } else {
                        ArrayList<ResourceBase> resourceSelectionArray = this.resource.getResourcesNearby(this.tileX, this.tileY, 2);
                        if (resourceSelectionArray.size() > 0) {
                           resourceSelection = resourceSelectionArray.get(Utilities.randomInt(resourceSelectionArray.size()));
                           this.resource.setResourceGrabbed(resourceSelection);
                           this.influence.setResourceGrabbed(resourceSelection);
                           this.influence.setSelectedSpell(SpellBase.SpellType.DROP);
                           this.influence.removeSpell(this);
                           this.god.increaseGodXP(this.castCost / 3000.0, GodModule.GodXPType.GRABBING_RESOURCES);
                           this.influence.decreaseInfluenceAmount(this.castCost);
                           this.mob.influenceFaithInArea(this.tileX, this.tileY, 4, 1, true);
                           this.mob.tryConfuseInArea(this.tileX, this.tileY, 4, true, 1);
                           this.sound.playSound(SoundModule.SoundType.GRAB, this.targetX, this.targetY);
                           return;
                        } else {
                           this.influence.removeSpell(this);
                        }
                     }
                  }
               }
            }
      }
   }
}
