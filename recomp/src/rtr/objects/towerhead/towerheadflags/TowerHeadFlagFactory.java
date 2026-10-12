package rtr.objects.towerhead.towerheadflags;

import java.util.HashMap;
import org.newdawn.slick.SlickException;
import rtr.objects.towerhead.TowerBase;

public class TowerHeadFlagFactory {
   private HashMap<TowerBase.TowerType, HashMap<TowerBase.TowerUpgradeStage, TowerHeadFlags>> towerHeadFlags = new HashMap<>();

   public TowerHeadFlagFactory() throws SlickException {
      TowerBase.TowerType[] var4;
      for (TowerBase.TowerType type : TowerBase.TowerType.values()) {
         this.towerHeadFlags.put(type, new HashMap<>());
      }

      for (TowerBase.TowerUpgradeStage subType : TowerBase.TowerUpgradeStage.values()) {
         this.towerHeadFlags.get(TowerBase.TowerType.ATTRACT).put(subType, new AttractTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.BALLISTA).put(subType, new BallistaTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.BANISH).put(subType, new BanishTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.BOW).put(subType, new BowTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.BULLET).put(subType, new BulletTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.GOD_BOW).put(subType, new GodBow(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.CORRUPTED_FIRE_BOW).put(subType, new CorruptedFireBowTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.CORRUPTED_FIRE_PHANTOM_DART).put(subType, new CorruptedFirePhantomDartTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.CORRUPTED_ICE_BOW).put(subType, new CorruptedIceBowTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.CORRUPTED_ICE_PHANTOM_DART).put(subType, new CorruptedIcePhantomDartTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.CORRUPTED_LIGHTNING_BOW).put(subType, new CorruptedLightningBowTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.CORRUPTED_LIGHTNING_PHANTOM_DART).put(subType, new CorruptedLightningPhantomDartTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.CORRUPTED_POISON_BOW).put(subType, new CorruptedPoisonBowTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.CORRUPTED_POISON_PHANTOM_DART).put(subType, new CorruptedPoisonPhantomDartTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.ELEMENTAL_BOLT).put(subType, new ElementalBoltTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.MOTIVATOR).put(subType, new MotivatorTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.PHANTOM_DART).put(subType, new PhantomDartTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.RECOMBOBULATOR).put(subType, new RecombobulatorTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.SLING).put(subType, new SlingTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.SPRAY).put(subType, new SprayTowerHeadFlags(subType));
         this.towerHeadFlags.get(TowerBase.TowerType.STATIC).put(subType, new StaticTowerHeadFlags(subType));
      }
   }

   public TowerHeadFlags getTowerFlags(TowerBase.TowerType baseType, TowerBase.TowerUpgradeStage subType) {
      TowerHeadFlags thisTowerFlags = this.towerHeadFlags.get(baseType).get(subType);
      if (!thisTowerFlags.isInitialized()) {
         thisTowerFlags.initialize();
      }

      return thisTowerFlags;
   }

   public HashMap<TowerBase.TowerType, HashMap<TowerBase.TowerUpgradeStage, TowerHeadFlags>> getObjectFlagsList() {
      return this.towerHeadFlags;
   }
}
