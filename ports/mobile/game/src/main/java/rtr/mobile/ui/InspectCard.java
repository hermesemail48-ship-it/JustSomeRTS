package rtr.mobile.ui;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import rtr.map.MapTilesLoader;
import rtr.mobs.MobBase;
import rtr.mobs.MobFlagsBase;
import rtr.mobs.jobs.MobJobBase;
import rtr.objects.ObjectBase;
import rtr.objects.objectflags.ObjectFlags;
import rtr.states.PlayState;

/** A large card for the selected villager or building, replacing the desktop's bottom info panels. */
public final class InspectCard {
   private static final Color HEALTH = new Color(0.80F, 0.22F, 0.20F, 1.0F);
   private static final Color HUNGER = new Color(0.85F, 0.55F, 0.20F, 1.0F);
   private static final Color THIRST = new Color(0.25F, 0.55F, 0.90F, 1.0F);
   private static final Color ENERGY = new Color(0.90F, 0.80F, 0.25F, 1.0F);
   private static final Color FAITH = new Color(0.65F, 0.45F, 0.90F, 1.0F);
   private static final Color SOCIAL = new Color(0.35F, 0.80F, 0.55F, 1.0F);
   private static final Color PROGRESS = new Color(0.35F, 0.70F, 0.30F, 1.0F);
   private ObjectBase confirmFor;
   private ObjectBase upgradeFor;

   /** Draws the card if something is selected; returns false when nothing is. */
   public boolean render(Ui ui, PlayState p) {
      MobBase m = p.getTouchSelectedMob();
      ObjectBase o = m == null ? p.getTouchSelectedObject() : null;
      if (m == null && o == null) {
         this.confirmFor = null;
         this.upgradeFor = null;
         return false;
      }

      float w = ui.width();
      float h = ui.height();
      float u = ui.unit();
      float cw = Math.min(w * 0.52F, u * 9.5F);
      float ch = Math.min(h * 0.62F, m != null ? this.mobHeight(ui, m) : this.objectHeight(ui, p, o));
      float cx = (w - cw) / 2.0F;
      float cy = h - ch - u * 0.2F;
      ui.panel(cx, cy, cw, ch);
      float pad = u * 0.2F;
      final PlayState play = p;
      float closeW = u * 0.9F;
      float headH = u * 0.62F;
      ui.button(cx + cw - closeW - pad, cy + pad, closeW, headH, "×", true, null, new Ui.Action() {
         @Override
         public void run() throws Exception {
            play.deselectAll();
         }
      }, null, ui.title);
      float textW = cw - closeW - pad * 3.0F;
      if (m != null) {
         this.renderMob(ui, m, cx + pad, cy + pad, cw - pad * 2.0F, ch - pad * 2.0F, textW);
      } else {
         this.renderObject(ui, p, o, cx + pad, cy + pad, cw - pad * 2.0F, ch - pad * 2.0F, textW);
      }

      return true;
   }

   private static final float BAR_ROW = 0.54F;

   private static int mobBarCount(MobBase m) {
      MobFlagsBase f = m.getMobFlags();
      int n = 1;
      n += f.getAllowHunger() ? 1 : 0;
      n += f.getAllowThirst() ? 1 : 0;
      n += f.getAllowSleep() ? 1 : 0;
      n += f.getAllowFaith() ? 1 : 0;
      n += f.getAllowSocial() ? 1 : 0;
      return n;
   }

   private float mobHeight(Ui ui, MobBase m) {
      float u = ui.unit();
      return u * 0.4F + u * 0.7F + ui.lineHeight(ui.small) * 1.5F + ui.lineHeight(ui.body) * 1.6F
         + (mobBarCount(m) + 1) / 2 * u * BAR_ROW + ui.lineHeight(ui.small) * 1.6F;
   }

   private float objectHeight(Ui ui, PlayState p, ObjectBase o) {
      float u = ui.unit();
      float top = u * 0.4F + u * 0.7F + ui.lineHeight(ui.small) * 1.8F;
      if (this.confirmFor == o || this.upgradeFor == o) {
         return top + u * 1.4F + u * 0.75F;
      }

      ObjectFlags f = o.getObjectFlags();
      int bars = objectBars(o).size();
      boolean workers = f.getSubType() == ObjectBase.ObjectSubType.BUILT && p.getWorkers(o) != null;
      ObjectBase.ObjectSubType st = f.getSubType();
      boolean buttons = f.allowPause()
         || st == ObjectBase.ObjectSubType.BUILT && f.canUpgrade()
         || o.isInRange() && f.allowDismantle() && st != ObjectBase.ObjectSubType.DISMANTLE;
      return top + (bars + 1) / 2 * u * BAR_ROW + (workers ? u * 0.8F : 0.0F) + (buttons ? u * 0.75F : 0.0F) + u * 0.15F;
   }

   private static ArrayList<Object[]> objectBars(ObjectBase o) {
      ObjectFlags f = o.getObjectFlags();
      ArrayList<Object[]> bars = new ArrayList<Object[]>();
      if (o.getHitPointsMax() > 0) {
         bars.add(new Object[]{"Health", o.getHitPoints(), o.getHitPointsMax(), HEALTH});
      }

      if (f.getSubType() != ObjectBase.ObjectSubType.BUILT) {
         int need = f.getTotalValueBase() - o.getTotalValue();
         bars.add(new Object[]{"Materials", o.getTotalResourcesCount(), Math.max(need, 0), PROGRESS});
      }

      if (f.getOccupantsMax() > 0) {
         bars.add(new Object[]{"Occupants", o.getOccupants().size(), f.getOccupantsMax(), SOCIAL});
      }

      return bars;
   }

   private static String job(MobBase m) {
      MobJobBase.MobJobType jt = m.getMobJobType();
      if (jt != null && jt != MobJobBase.MobJobType.NONE) {
         return Ui.plain(jt.getJob().getMobJobName());
      }

      return Ui.plain(m.getMobType().getName());
   }

   private void renderMob(Ui ui, MobBase m, float x, float y, float w, float h, float textW) {
      float u = ui.unit();
      ui.textFit(ui.title, Ui.plain(m.getFullName()), x + u * 0.1F, y + u * 0.1F, textW, Ui.TEXT);
      float ly = y + u * 0.7F;
      String sub = job(m) + "  •  Level " + m.getLevel() + "  •  Age " + m.getAge();
      ui.textFit(ui.small, sub, x + u * 0.1F, ly, textW, Ui.TEXT_DIM);
      ly += ui.lineHeight(ui.small) * 1.5F;
      String task = Ui.plain(m.getCurrentTask());
      if (task.length() > 0) {
         ui.textFit(ui.body, task, x + u * 0.1F, ly, w - u * 0.2F, Ui.TEXT_LIGHT);
      }

      ly += ui.lineHeight(ui.body) * 1.6F;
      MobFlagsBase f = m.getMobFlags();
      ArrayList<Object[]> bars = new ArrayList<Object[]>();
      bars.add(new Object[]{"Health", m.getHitPoints(), m.getHitPointsMax(), HEALTH});
      if (f.getAllowHunger()) {
         bars.add(new Object[]{"Food", m.getHunger(), m.getHungerMax(), HUNGER});
      }

      if (f.getAllowThirst()) {
         bars.add(new Object[]{"Water", m.getThirst(), m.getThirstMax(), THIRST});
      }

      if (f.getAllowSleep()) {
         bars.add(new Object[]{"Energy", m.getEnergy(), m.getEnergyMax(), ENERGY});
      }

      if (f.getAllowFaith()) {
         bars.add(new Object[]{"Faith", m.getFaith(), m.getFaithMax(), FAITH});
      }

      if (f.getAllowSocial()) {
         bars.add(new Object[]{"Social", m.getSocial(), m.getSocialMax(), SOCIAL});
      }

      float statusH = ui.lineHeight(ui.small) * 1.6F;
      this.bars(ui, bars, x, ly, w, y + h - statusH - ly);
      StringBuilder status = new StringBuilder();
      this.flag(status, m.isPoisoned(), "Poisoned");
      this.flag(status, m.isOnFire(), "On fire");
      this.flag(status, m.isFrozen(), "Frozen");
      this.flag(status, m.isDysentery(), "Dysentery");
      this.flag(status, m.isBlighted(), "Blighted");
      this.flag(status, m.isPregnant(), "Pregnant");
      if (status.length() > 0) {
         ui.textFit(ui.small, status.toString(), x + u * 0.1F, y + h - statusH * 0.8F, w, Ui.WARN);
      }
   }

   private void flag(StringBuilder sb, boolean on, String label) {
      if (on) {
         if (sb.length() > 0) {
            sb.append("  •  ");
         }

         sb.append(label);
      }
   }

   /** Two columns of labelled bars. */
   private void bars(Ui ui, ArrayList<Object[]> bars, float x, float y, float w, float avail) {
      float u = ui.unit();
      int rows = (bars.size() + 1) / 2;
      if (rows == 0) {
         return;
      }

      float gap = u * 0.12F;
      float bh = Math.min(u * 0.42F, Math.max(u * 0.25F, (avail - gap * (rows - 1)) / rows));
      float colW = (w - gap) / 2.0F;

      for (int i = 0; i < bars.size(); i++) {
         Object[] b = bars.get(i);
         int cur = (Integer)b[1];
         int max = (Integer)b[2];
         float bx = x + (i % 2) * (colW + gap);
         float by = y + (i / 2) * (bh + gap);
         ui.bar(bx, by, colW, bh, max <= 0 ? 0.0F : (float)cur / max, (Color)b[3], b[0] + "  " + cur + " / " + max);
      }
   }

   private void renderObject(Ui ui, final PlayState p, final ObjectBase o, float x, float y, float w, float h, float textW) {
      float u = ui.unit();
      ObjectFlags f = o.getObjectFlags();
      ui.textFit(ui.title, Ui.plain(f.getName()), x + u * 0.1F, y + u * 0.1F, textW, Ui.TEXT);
      float ly = y + u * 0.7F;
      ObjectBase.ObjectSubType st = f.getSubType();
      String state;
      Color stateColor = Ui.TEXT_DIM;
      if (st == ObjectBase.ObjectSubType.CONSTRUCTION) {
         state = "Under construction";
      } else if (st == ObjectBase.ObjectSubType.ABANDONED) {
         state = "Abandoned";
         stateColor = Ui.WARN;
      } else if (st == ObjectBase.ObjectSubType.DISMANTLE) {
         state = "Being dismantled";
         stateColor = Ui.WARN;
      } else {
         state = "Built";
      }

      if (o.isPaused()) {
         state = state + "  •  Paused";
         stateColor = Ui.WARN;
      }

      if (!o.isInRange()) {
         state = state + "  •  Outside your territory";
         stateColor = Ui.WARN;
      }

      ui.textFit(ui.small, state, x + u * 0.1F, ly, textW, stateColor);
      ly += ui.lineHeight(ui.small) * 1.8F;
      float buttonsH = u * 0.75F;
      float buttonsY = y + h - buttonsH;
      if (this.confirmFor == o) {
         ui.textWrapped(ui.body, "Dismantle this building? Villagers will carry its materials back.", x + u * 0.1F, ly, w - u * 0.2F, 3, Ui.TEXT_LIGHT);
         this.buttonRow(ui, x, buttonsY, w, buttonsH, new String[]{"Dismantle", "Keep it"}, new Ui.Action[]{new Ui.Action() {
            @Override
            public void run() throws Exception {
               p.dismantleTouch(o);
               InspectCard.this.confirmFor = null;
            }
         }, new Ui.Action() {
            @Override
            public void run() throws Exception {
               InspectCard.this.confirmFor = null;
            }
         }});
         return;
      }

      if (this.upgradeFor == o) {
         ArrayList<String> labels = new ArrayList<String>();
         ArrayList<Ui.Action> actions = new ArrayList<Ui.Action>();

         for (final MapTilesLoader.TileSet next : new MapTilesLoader.TileSet[]{f.getNextUpgradeType(), f.getNextUpgradeType2(), f.getNextUpgradeType3()}) {
            if (next != null) {
               ObjectFlags nf = p.getBuiltFlags(next);
               labels.add(nf == null ? next.name() : Ui.plain(nf.getName()));
               actions.add(new Ui.Action() {
                  @Override
                  public void run() throws Exception {
                     p.upgradeTouch(o, next);
                     InspectCard.this.upgradeFor = null;
                  }
               });
            }
         }

         labels.add("Back");
         actions.add(new Ui.Action() {
            @Override
            public void run() throws Exception {
               InspectCard.this.upgradeFor = null;
            }
         });
         ui.text(ui.body, "Upgrade to:", x + u * 0.1F, ly, Ui.TEXT_LIGHT);
         this.buttonRow(ui, x, buttonsY, w, buttonsH, labels.toArray(new String[0]), actions.toArray(new Ui.Action[0]));
         return;
      }

      ArrayList<Object[]> bars = objectBars(o);

      int[] workers = st == ObjectBase.ObjectSubType.BUILT ? p.getWorkers(o) : null;
      float workersH = workers == null ? 0.0F : u * 0.7F;
      this.bars(ui, bars, x, ly, w, buttonsY - workersH - u * 0.12F - ly);
      if (workers != null) {
         float wy = buttonsY - workersH - u * 0.08F;
         float sb = workersH * 0.92F;
         ui.button(x, wy, sb * 1.2F, sb, "−", true, null, new Ui.Action() {
            @Override
            public void run() throws Exception {
               p.changeWorkers(o, -1);
            }
         }, null, ui.title);
         ui.button(x + sb * 1.2F + u * 0.1F, wy, sb * 1.2F, sb, "+", true, null, new Ui.Action() {
            @Override
            public void run() throws Exception {
               p.changeWorkers(o, 1);
            }
         }, null, ui.title);
         String wt = "Workers " + workers[0] + "   wanted " + workers[1] + " / " + workers[2];
         ui.textFit(ui.body, wt, x + sb * 2.4F + u * 0.35F, wy + (sb - ui.body.getCapHeight()) / 2.0F, w - sb * 2.4F - u * 0.4F, Ui.TEXT_LIGHT);
      }

      ArrayList<String> labels = new ArrayList<String>();
      ArrayList<Ui.Action> actions = new ArrayList<Ui.Action>();
      if (f.allowPause()) {
         labels.add(o.isPaused() ? "Resume" : "Pause");
         actions.add(new Ui.Action() {
            @Override
            public void run() throws Exception {
               p.togglePausedTouch(o);
            }
         });
      }

      if (st == ObjectBase.ObjectSubType.BUILT && f.canUpgrade()
         && (f.getNextUpgradeType() != null || f.getNextUpgradeType2() != null || f.getNextUpgradeType3() != null)) {
         labels.add("Upgrade");
         actions.add(new Ui.Action() {
            @Override
            public void run() throws Exception {
               InspectCard.this.upgradeFor = o;
            }
         });
      }

      if (o.isInRange() && f.allowDismantle() && st != ObjectBase.ObjectSubType.DISMANTLE) {
         labels.add("Dismantle");
         actions.add(new Ui.Action() {
            @Override
            public void run() throws Exception {
               InspectCard.this.confirmFor = o;
            }
         });
      }

      if (!labels.isEmpty()) {
         this.buttonRow(ui, x, buttonsY, w, buttonsH, labels.toArray(new String[0]), actions.toArray(new Ui.Action[0]));
      }
   }

   private void buttonRow(Ui ui, float x, float y, float w, float h, String[] labels, Ui.Action[] actions) {
      float gap = ui.unit() * 0.15F;
      float bw = Math.min(ui.unit() * 2.6F, (w - gap * (labels.length - 1)) / labels.length);

      for (int i = 0; i < labels.length; i++) {
         Color base = "Dismantle".equals(labels[i]) ? new Color(0.45F, 0.14F, 0.10F, 1.0F) : null;
         ui.button(x + i * (bw + gap), y, bw, h, labels[i], true, base, actions[i], null, ui.body);
      }
   }
}
