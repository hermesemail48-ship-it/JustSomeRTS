package rtr.objects.gates;

import org.newdawn.slick.SlickException;
import rtr.mobs.MobBase;
import rtr.objects.ObjectBase;
import rtr.save.YMLDataMap;

public class StoneGateNS extends GateBase {
   public StoneGateNS(int x, int y, ObjectBase g) throws SlickException {
      super(x, y, g);
      this.gateType = GateBase.GateType.STONE_GATE_NS;
      this.sprite = this.object.createGateSpriteSheet(this.gateType);
      this.mobGroup = MobBase.MobGroup.VILLAGER;
   }

   public StoneGateNS(YMLDataMap dataMap, ObjectBase g) throws SlickException {
      super(dataMap, g);
   }
}
