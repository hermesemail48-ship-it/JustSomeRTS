package rtr.influence;

import java.util.EnumMap;

public class SpellFactory {
   private EnumMap<SpellBase.SpellType, SpellBase> spellInstances = new EnumMap<>(SpellBase.SpellType.class);

   public SpellFactory() {
      SpellBase s = null;
      SpellBase var2 = new SpellGrab();
      var2.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.GRAB, var2);
      SpellBase var3 = new SpellBanish();
      var3.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.BANISH, var3);
      SpellBase var4 = new SpellDissolve();
      var4.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.DISSOLVE, var4);
      SpellBase var5 = new SpellSummonHolyGolem();
      var5.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.SUMMON_HOLY_GOLEM, var5);
      SpellBase var6 = new SpellSummonLaborGolem();
      var6.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.SUMMON_LABOR_GOLEM, var6);
      SpellBase var7 = new SpellDrop();
      var7.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.DROP, var7);
      SpellBase var8 = new SpellMeteor();
      var8.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.METEOR, var8);
      SpellBase var9 = new SpellComet();
      var9.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.COMET, var9);
      SpellBase var10 = new SpellFlame();
      var10.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.FLAME, var10);
      SpellBase var11 = new SpellHealingAura();
      var11.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.HEALING_AURA, var11);
      SpellBase var12 = new SpellMotivateLand();
      var12.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.MOTIVATE_LAND, var12);
      SpellBase var13 = new SpellEarthquake();
      var13.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.EARTHQUAKE, var13);
      SpellBase var14 = new SpellRecall();
      var14.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.RECALL, var14);
      SpellBase var15 = new SpellResurrect();
      var15.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.RESURRECT, var15);
      SpellBase var16 = new SpellSpecialInitialWorldMapSpawnIn();
      var16.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.SPECIAL_INITIAL_WORLD_MAP_SPAWN_IN, var16);
      SpellBase var17 = new SpellStorm();
      var17.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.STORM, var17);
      SpellBase var18 = new SpellConjureEssence();
      var18.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.CONJURE_ESSENCE, var18);
      SpellBase var19 = new SpellLightningBolt();
      var19.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.LIGHTNING_BOLT, var19);
      SpellBase var20 = new SpellConjureMaterial();
      var20.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.CONJURE_MATERIAL, var20);
      SpellBase var21 = new SpellMend();
      var21.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.MEND, var21);
      SpellBase var22 = new SpellCharm();
      var22.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.CHARM, var22);
      SpellBase var23 = new SpellColdAura();
      var23.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.COLD_AURA, var23);
      SpellBase var24 = new SpellIlluminate();
      var24.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.ILLUMINATE, var24);
      SpellBase var25 = new SpellMagicBolts();
      var25.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.MAGIC_BOLTS, var25);
      SpellBase var26 = new SpellRegenerate();
      var26.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.REGENERATE, var26);
      SpellBase var27 = new SpellGodWall();
      var27.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.GOD_WALL, var27);
      SpellBase var28 = new SpellGodTower();
      var28.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.GOD_TOWER, var28);
      SpellBase var29 = new SpellDispelGodStructure();
      var29.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.DISPEL_GOD_STRUCTURE, var29);
      SpellBase var30 = new SpellHarvest();
      var30.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.HARVEST, var30);
      SpellBase var31 = new SpellDispelGolem();
      var31.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.DISPEL_GOLEM, var31);
      SpellBase var32 = new SpellConstruct();
      var32.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.CONSTRUCT, var32);
      SpellBase var33 = new SpellHolyWood();
      var33.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.HOLY_WOOD, var33);
      SpellBase var34 = new SpellHolyPotatoes();
      var34.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.HOLY_POTATOES, var34);
      SpellBase var35 = new SpellDivineBlessing();
      var35.config(0, 0, false);
      this.spellInstances.put(SpellBase.SpellType.DIVINE_BLESSING, var35);
   }

   public SpellBase getSpellInstance(SpellBase.SpellType baseType) {
      return this.spellInstances.get(baseType);
   }

   public EnumMap<SpellBase.SpellType, SpellBase> getSpellInstanceList() {
      return this.spellInstances;
   }

   public SpellBase getNewInstance(SpellBase.SpellType t) {
      try {
         return (SpellBase)Class.forName(this.spellInstances.get(t).getClass().getName()).newInstance();
      } catch (IllegalAccessException | ClassNotFoundException | InstantiationException var3) {
         var3.printStackTrace();
         return null;
      }
   }
}
