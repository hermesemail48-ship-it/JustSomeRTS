package rtr.mobile.ui;

import com.badlogic.gdx.graphics.Color;
import java.util.ArrayList;
import rtr.font.Text;
import rtr.map.MapTilesLoader;
import rtr.states.PlayState;

/**
 * A bottom sheet replacing the desktop build panel: a scrolling row of category tabs and a scrolling row of building
 * cards. Tapping a card starts placing it (then the usual aim / Build flow takes over).
 */
public final class BuildDrawer {
   public boolean open;
   private BuildCatalog.Group group = BuildCatalog.Group.HOUSING;
   private final Ui.Scroll tabScroll = new Ui.Scroll(true);
   private final Ui.Scroll cardScroll = new Ui.Scroll(true);

   private static boolean hidden(PlayState p, BuildCatalog.Entry e) {
      // Skirmish has no courier or migration (see RightPanel.render).
      return p.isSkirmish()
         && (e.building == MapTilesLoader.TileSet.COURIER_STATION || e.building == MapTilesLoader.TileSet.MIGRATION_WAY_STATION);
   }

   public void render(final Ui ui, final PlayState p) {
      float w = ui.width();
      float h = ui.height();
      float u = ui.unit();
      float margin = u * 0.2F;
      float pad0 = u * 0.18F;
      float imageH0 = u * 0.95F;
      float cardH0 = pad0 * 3.0F + imageH0 + ui.lineHeight(ui.small) * 1.25F * 2.0F + ui.lineHeight(ui.small) * 1.1F;
      float sheetH = Math.min(h * 0.6F, pad0 * 4.0F + u * 0.62F * 2.0F + cardH0);
      float sx = margin;
      float sy = h - sheetH - margin;
      float sw = w - margin * 2.0F;
      ui.panel(sx, sy, sw, sheetH);
      float pad = u * 0.18F;
      // Header: title, slots, close.
      float headH = u * 0.62F;
      ui.text(ui.title, "Build", sx + pad * 2.0F, sy + (headH - ui.title.getCapHeight()) / 2.0F + pad * 0.5F, Ui.TEXT);
      int[] slots = p.getBuildingSlots();
      String slotText = "Building slots " + slots[0] + " / " + slots[1];
      ui.text(ui.small, slotText, sx + pad * 2.0F + ui.textWidth(ui.title, "Build") + u * 0.4F,
         sy + (headH - ui.small.getCapHeight()) / 2.0F + pad * 0.5F, slots[0] >= slots[1] ? Ui.WARN : Ui.TEXT_DIM);
      float closeW = u * 1.5F;
      ui.button(sx + sw - closeW - pad, sy + pad, closeW, headH - pad * 0.5F, "Close", true, null, new Ui.Action() {
         @Override
         public void run() throws Exception {
            BuildDrawer.this.open = false;
         }
      });
      // Category tabs.
      float tabY = sy + headH + pad;
      float tabH = u * 0.62F;
      float tabsX = sx + pad;
      float tabsW = sw - pad * 2.0F;
      BuildCatalog.Group[] groups = BuildCatalog.Group.values();
      float tabGap = u * 0.1F;
      float total = 0.0F;
      float[] tabW = new float[groups.length];

      for (int i = 0; i < groups.length; i++) {
         tabW[i] = ui.textWidth(ui.small, groups[i].label) + u * 0.5F;
         total += tabW[i] + tabGap;
      }

      this.tabScroll.max = Math.max(0.0F, total - tabGap - tabsW);
      this.tabScroll.offset = Math.max(0.0F, Math.min(this.tabScroll.max, this.tabScroll.offset));
      ui.clip(tabsX, tabY, tabsW, tabH);
      float tx = tabsX - this.tabScroll.offset;

      for (int i = 0; i < groups.length; i++) {
         final BuildCatalog.Group g = groups[i];
         boolean locked = g == BuildCatalog.Group.ROADS && !p.canBuildRoads();
         ui.button(tx, tabY, tabW[i], tabH, g.label, true, g == this.group ? Ui.BUTTON_ON : (locked ? Ui.DISABLED : Ui.BUTTON), new Ui.Action() {
            @Override
            public void run() throws Exception {
               if (BuildDrawer.this.group != g) {
                  BuildDrawer.this.group = g;
                  BuildDrawer.this.cardScroll.offset = 0.0F;
               }
            }
         }, this.tabScroll, ui.small);
         tx += tabW[i] + tabGap;
      }

      ui.unclip();
      // Cards.
      float cardsY = tabY + tabH + pad;
      float cardsH = sy + sheetH - pad - cardsY;
      float cardH = cardsH;
      float imageH = Math.min(imageH0, cardH * 0.5F);
      float cardW = Math.max(u * 2.4F, imageH * 3.4F + pad * 2.0F);
      float cardGap = u * 0.14F;
      ArrayList<BuildCatalog.Entry> entries = new ArrayList<BuildCatalog.Entry>();

      for (BuildCatalog.Entry e : BuildCatalog.inGroup(this.group)) {
         if (!hidden(p, e)) {
            entries.add(e);
         }
      }

      this.cardScroll.max = Math.max(0.0F, entries.size() * (cardW + cardGap) - cardGap - tabsW);
      this.cardScroll.offset = Math.max(0.0F, Math.min(this.cardScroll.max, this.cardScroll.offset));
      boolean roadsLocked = this.group == BuildCatalog.Group.ROADS && !p.canBuildRoads();
      boolean noSlots = slots[0] >= slots[1];
      ui.clip(tabsX, cardsY, tabsW, cardsH);
      float cx = tabsX - this.cardScroll.offset;

      for (final BuildCatalog.Entry e : entries) {
         if (cx + cardW >= tabsX && cx <= tabsX + tabsW) {
            this.card(ui, p, e, cx, cardsY, cardW, cardH, imageH, roadsLocked, noSlots);
         }

         cx += cardW + cardGap;
      }

      ui.unclip();
      if (roadsLocked) {
         ui.textCentered(ui.small, "Build a Way-Maker Shack first", sx + sw / 2.0F, tabY - pad * 0.2F, Ui.WARN);
      }
   }

   private final java.util.HashMap<MapTilesLoader.TileSet, String> costs = new java.util.HashMap<MapTilesLoader.TileSet, String>();

   /** "24 Wood  12 Rock": the materials villagers haul to build it (cached; flags are built per call). */
   private String cost(PlayState p, BuildCatalog.Entry e) {
      if (e.building == null) {
         return "";
      }

      String c = this.costs.get(e.building);
      if (c == null) {
         StringBuilder sb = new StringBuilder();

         try {
            rtr.objects.objectflags.ObjectFlags f = p.getBuiltFlags(e.building);
            if (f != null && f.getResourceBaseValues() != null) {
               for (java.util.Map.Entry<rtr.resources.ResourceModule.ResourceType, Integer> r : f.getResourceBaseValues().entrySet()) {
                  if (r.getValue() != null && r.getValue() > 0) {
                     if (sb.length() > 0) {
                        sb.append("  ");
                     }

                     sb.append(r.getValue()).append(' ').append(Ui.plain(r.getKey().getTemplate().getNamePlural()));
                  }
               }
            }
         } catch (RuntimeException ex) {
            System.err.println("Phone UI: no cost for " + e.building + ": " + ex);
         }

         c = sb.toString();
         this.costs.put(e.building, c);
      }

      return c;
   }

   private void card(Ui ui, final PlayState p, final BuildCatalog.Entry e, float x, float y, float w, float h, float imageH, boolean locked, boolean noSlots) {
      float u = ui.unit();
      float pad = u * 0.12F;
      // Out-of-slot buildings stay tappable: the Build button then reports why the spot is invalid.
      boolean enabled = !locked;
      boolean down = ui.isPressed(x, y, w, h);
      ui.frame(x, y, w, h, down ? Ui.BUTTON_PRESSED : (enabled ? Ui.PANEL_DARK : Ui.DISABLED));
      ui.image(e.imagePath(), x + pad, y + pad, w - pad * 2.0F, imageH, enabled ? Color.WHITE : new Color(0.5F, 0.5F, 0.5F, 1.0F));
      String name = Ui.plain(Text.getText(e.labelKey));
      float ty = y + pad * 2.0F + imageH;
      ui.textWrapped(ui.small, name, x + pad * 1.5F, ty, w - pad * 3.0F, 2, enabled ? Ui.TEXT : Ui.TEXT_DIM);
      String cost = this.cost(p, e);
      if (cost.length() > 0) {
         ui.textFit(ui.small, cost, x + pad * 1.5F, y + h - pad - ui.lineHeight(ui.small), w - pad * 3.0F - ui.textWidth(ui.small, "×99 "), Ui.TEXT_DIM);
      }
      if (e.building != null) {
         int count = p.countBuildings(e.building);
         if (count > 0) {
            String c = "×" + count;
            ui.text(ui.small, c, x + w - pad * 2.0F - ui.textWidth(ui.small, c), y + h - pad - ui.lineHeight(ui.small), Ui.TEXT_DIM);
         }
      }

      ui.area(x, y, w, h, enabled ? new Ui.Action() {
         @Override
         public void run() throws Exception {
            if (e.building != null) {
               p.startPlacing(e.building);
            } else if (e.road != null) {
               p.startRoadTool(e.road);
            } else {
               p.startTool(e.tool);
            }

            BuildDrawer.this.open = false;
         }
      } : null, this.cardScroll);
   }
}
