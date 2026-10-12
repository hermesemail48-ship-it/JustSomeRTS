package rtr.particles;

import java.util.ArrayList;
import java.util.HashMap;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import rtr.ImageLoader;
import rtr.utilities.OrderedPair;
import rtr.utilities.Utilities;

public class ParticleAnimationLoader {
   private HashMap<ParticleAnimationLoader.ParticleAnimation, ArrayList<Image[]>> particleAnimationImages;

   public ParticleAnimationLoader() throws SlickException {
      if (this.particleAnimationImages == null) {
         this.particleAnimationImages = new HashMap<>();
         Image particleAnimationSheet = ImageLoader.getImage("res/particleSheet.png");

         ParticleAnimationLoader.ParticleAnimation[] var5;
         for (ParticleAnimationLoader.ParticleAnimation a : ParticleAnimationLoader.ParticleAnimation.values()) {
            this.particleAnimationImages.put(a, new ArrayList<>());

            for (int r = 0; r < 4; r++) {
               OrderedPair[] coordinates = a.getTextureCoordinateData();
               Image[] imageOut = new Image[coordinates.length];

               for (int x = 0; x < coordinates.length; x++) {
                  Image spriteOut = particleAnimationSheet.getSubImage(coordinates[x].getX(), coordinates[x].getY(), a.getWidth(), a.getHeight());
                  spriteOut = spriteOut.getFlippedCopy(r == 0 || r == 2, r == 1 || r == 2);
                  imageOut[x] = spriteOut;
               }

               this.particleAnimationImages.get(a).add(imageOut);
            }
         }
      }
   }

   public void startUse() {
      this.particleAnimationImages.get(ParticleAnimationLoader.ParticleAnimation.SPECK).get(0)[0].startUse();
   }

   public void endUse() {
      this.particleAnimationImages.get(ParticleAnimationLoader.ParticleAnimation.SPECK).get(0)[0].endUse();
   }

   public Image[] getParticleAnimationImages(ParticleAnimationLoader.ParticleAnimation a) {
      return this.particleAnimationImages.get(a).get(Utilities.randomInt(4));
   }

   public static enum ParticleAnimation {
      LINE_2PX(new OrderedPair[]{new OrderedPair(0, 16), new OrderedPair(2, 16), new OrderedPair(4, 16), new OrderedPair(6, 16)}, 2, 2),
      LINE_3PX(new OrderedPair[]{new OrderedPair(0, 18), new OrderedPair(3, 18), new OrderedPair(6, 18), new OrderedPair(9, 18)}, 3, 3),
      LINE_4PX(new OrderedPair[]{new OrderedPair(0, 21), new OrderedPair(4, 21), new OrderedPair(8, 21), new OrderedPair(12, 21)}, 4, 4),
      LINE_5PX(new OrderedPair[]{new OrderedPair(0, 25), new OrderedPair(5, 25), new OrderedPair(10, 25), new OrderedPair(15, 25)}, 5, 5),
      LINE_6PX(new OrderedPair[]{new OrderedPair(0, 30), new OrderedPair(6, 30), new OrderedPair(12, 30), new OrderedPair(18, 30)}, 6, 6),
      PUFF_4PX(
         new OrderedPair[]{
            new OrderedPair(0, 148),
            new OrderedPair(4, 148),
            new OrderedPair(8, 148),
            new OrderedPair(12, 148),
            new OrderedPair(16, 148),
            new OrderedPair(20, 148),
            new OrderedPair(24, 148)
         },
         4,
         4
      ),
      PUFF_6PX(
         new OrderedPair[]{
            new OrderedPair(0, 152),
            new OrderedPair(6, 152),
            new OrderedPair(12, 152),
            new OrderedPair(18, 152),
            new OrderedPair(24, 152),
            new OrderedPair(30, 152),
            new OrderedPair(36, 152),
            new OrderedPair(42, 152),
            new OrderedPair(48, 152)
         },
         6,
         6
      ),
      PUFF_8PX(
         new OrderedPair[]{
            new OrderedPair(0, 158),
            new OrderedPair(8, 158),
            new OrderedPair(16, 158),
            new OrderedPair(24, 158),
            new OrderedPair(32, 158),
            new OrderedPair(40, 158),
            new OrderedPair(48, 158),
            new OrderedPair(56, 158),
            new OrderedPair(64, 158),
            new OrderedPair(72, 158),
            new OrderedPair(80, 158),
            new OrderedPair(88, 158),
            new OrderedPair(96, 158),
            new OrderedPair(104, 158),
            new OrderedPair(112, 158),
            new OrderedPair(120, 158)
         },
         8,
         8
      ),
      EXPANDING_CIRCLE_16PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(32, 0),
            new OrderedPair(48, 0),
            new OrderedPair(64, 0),
            new OrderedPair(80, 0),
            new OrderedPair(96, 0),
            new OrderedPair(112, 0),
            new OrderedPair(128, 0)
         },
         16,
         16
      ),
      EXPANDING_CIRCLE_12PX(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(34, 2),
            new OrderedPair(50, 2),
            new OrderedPair(66, 2),
            new OrderedPair(82, 2),
            new OrderedPair(98, 2)
         },
         12,
         12
      ),
      EXPANDING_CIRCLE_8PX(
         new OrderedPair[]{new OrderedPair(4, 4), new OrderedPair(20, 4), new OrderedPair(36, 4), new OrderedPair(52, 4), new OrderedPair(68, 4)}, 8, 8
      ),
      EXPANDING_CIRCLE_4PX(new OrderedPair[]{new OrderedPair(6, 6), new OrderedPair(22, 6), new OrderedPair(38, 6)}, 4, 4),
      EXPANDING_CIRCLE_2PX(new OrderedPair[]{new OrderedPair(7, 7), new OrderedPair(23, 7)}, 2, 2),
      EXPANDING_CIRCLE_16PX_HOLE_2PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(32, 16),
            new OrderedPair(48, 16),
            new OrderedPair(64, 16),
            new OrderedPair(80, 16),
            new OrderedPair(96, 16),
            new OrderedPair(112, 16),
            new OrderedPair(128, 16)
         },
         16,
         16
      ),
      EXPANDING_CIRCLE_12PX_HOLE_2PX(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(34, 18),
            new OrderedPair(50, 18),
            new OrderedPair(66, 18),
            new OrderedPair(82, 18),
            new OrderedPair(98, 18)
         },
         12,
         12
      ),
      EXPANDING_CIRCLE_8PX_HOLE_2PX(
         new OrderedPair[]{new OrderedPair(4, 4), new OrderedPair(20, 4), new OrderedPair(36, 20), new OrderedPair(52, 20), new OrderedPair(68, 20)}, 8, 8
      ),
      EXPANDING_CIRCLE_16PX_HOLE_6PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(32, 16),
            new OrderedPair(48, 32),
            new OrderedPair(64, 48),
            new OrderedPair(80, 48),
            new OrderedPair(96, 48),
            new OrderedPair(112, 48),
            new OrderedPair(128, 48)
         },
         16,
         16
      ),
      EXPANDING_CIRCLE_12PX_HOLE_6PX(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(34, 18),
            new OrderedPair(50, 34),
            new OrderedPair(66, 50),
            new OrderedPair(82, 50),
            new OrderedPair(98, 50)
         },
         12,
         12
      ),
      EXPANDING_CIRCLE_16PX_HOLE_10PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(32, 16),
            new OrderedPair(48, 32),
            new OrderedPair(64, 48),
            new OrderedPair(80, 64),
            new OrderedPair(96, 80),
            new OrderedPair(112, 80),
            new OrderedPair(128, 80)
         },
         16,
         16
      ),
      EXPANDING_RING_4PX(new OrderedPair[]{new OrderedPair(6, 6), new OrderedPair(22, 6), new OrderedPair(38, 22)}, 4, 4),
      EXPANDING_RING_4PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(6, 6), new OrderedPair(22, 6), new OrderedPair(38, 22), new OrderedPair(38, 38), new OrderedPair(38, 54), new OrderedPair(38, 70)
         },
         4,
         4
      ),
      EXPANDING_RING_8PX(
         new OrderedPair[]{new OrderedPair(4, 4), new OrderedPair(20, 4), new OrderedPair(36, 20), new OrderedPair(52, 36), new OrderedPair(68, 52)}, 8, 8
      ),
      EXPANDING_RING_8PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(4, 4),
            new OrderedPair(20, 4),
            new OrderedPair(36, 20),
            new OrderedPair(52, 36),
            new OrderedPair(68, 52),
            new OrderedPair(68, 68),
            new OrderedPair(68, 84),
            new OrderedPair(68, 100)
         },
         8,
         8
      ),
      EXPANDING_RING_12PX(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(34, 18),
            new OrderedPair(50, 34),
            new OrderedPair(66, 50),
            new OrderedPair(82, 66),
            new OrderedPair(98, 82)
         },
         12,
         12
      ),
      EXPANDING_RING_12PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(34, 18),
            new OrderedPair(50, 34),
            new OrderedPair(66, 50),
            new OrderedPair(82, 66),
            new OrderedPair(98, 82),
            new OrderedPair(98, 98),
            new OrderedPair(98, 114),
            new OrderedPair(98, 130),
            new OrderedPair(98, 146)
         },
         12,
         12
      ),
      EXPANDING_RING_16PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(32, 16),
            new OrderedPair(48, 32),
            new OrderedPair(64, 48),
            new OrderedPair(80, 64),
            new OrderedPair(96, 80),
            new OrderedPair(112, 96),
            new OrderedPair(128, 112)
         },
         16,
         16
      ),
      EXPANDING_RING_16PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(32, 16),
            new OrderedPair(48, 32),
            new OrderedPair(64, 48),
            new OrderedPair(80, 64),
            new OrderedPair(96, 80),
            new OrderedPair(112, 96),
            new OrderedPair(128, 112),
            new OrderedPair(128, 128),
            new OrderedPair(128, 144),
            new OrderedPair(128, 160),
            new OrderedPair(128, 176)
         },
         16,
         16
      ),
      SOLID_CIRCLE_EXPANDING_HOLE_16PX(
         new OrderedPair[]{
            new OrderedPair(128, 0),
            new OrderedPair(128, 16),
            new OrderedPair(128, 32),
            new OrderedPair(128, 48),
            new OrderedPair(128, 64),
            new OrderedPair(128, 80),
            new OrderedPair(128, 96),
            new OrderedPair(128, 112)
         },
         16,
         16
      ),
      SOLID_CIRCLE_EXPANDING_HOLE_12PX(
         new OrderedPair[]{
            new OrderedPair(98, 2), new OrderedPair(98, 18), new OrderedPair(98, 34), new OrderedPair(98, 50), new OrderedPair(98, 66), new OrderedPair(98, 82)
         },
         12,
         12
      ),
      SOLID_CIRCLE_EXPANDING_HOLE_8PX(
         new OrderedPair[]{new OrderedPair(68, 4), new OrderedPair(68, 20), new OrderedPair(68, 36), new OrderedPair(68, 52)}, 8, 8
      ),
      SOLID_CIRCLE_EXPANDING_HOLE_4PX(new OrderedPair[]{new OrderedPair(38, 6), new OrderedPair(38, 22)}, 4, 4),
      SPECK(new OrderedPair[]{new OrderedPair(8, 8)}, 1, 1),
      SPECKS_2PX(
         new OrderedPair[]{
            new OrderedPair(0, 39),
            new OrderedPair(2, 39),
            new OrderedPair(4, 39),
            new OrderedPair(6, 39),
            new OrderedPair(8, 39),
            new OrderedPair(10, 39),
            new OrderedPair(12, 39),
            new OrderedPair(14, 39),
            new OrderedPair(16, 39),
            new OrderedPair(18, 39),
            new OrderedPair(20, 39),
            new OrderedPair(22, 39)
         },
         2,
         2
      ),
      SPECKS_3PX(
         new OrderedPair[]{
            new OrderedPair(0, 41),
            new OrderedPair(3, 41),
            new OrderedPair(6, 41),
            new OrderedPair(9, 41),
            new OrderedPair(12, 41),
            new OrderedPair(15, 41),
            new OrderedPair(18, 41),
            new OrderedPair(21, 41),
            new OrderedPair(24, 41),
            new OrderedPair(27, 41),
            new OrderedPair(30, 41),
            new OrderedPair(33, 41)
         },
         3,
         3
      ),
      SPECKS_4PX(
         new OrderedPair[]{
            new OrderedPair(0, 44),
            new OrderedPair(4, 44),
            new OrderedPair(8, 44),
            new OrderedPair(12, 44),
            new OrderedPair(16, 44),
            new OrderedPair(20, 44),
            new OrderedPair(24, 44),
            new OrderedPair(28, 44),
            new OrderedPair(32, 44),
            new OrderedPair(36, 44),
            new OrderedPair(40, 44),
            new OrderedPair(44, 44)
         },
         4,
         4
      ),
      SPLATTER_3PX(
         new OrderedPair[]{
            new OrderedPair(0, 55),
            new OrderedPair(3, 55),
            new OrderedPair(6, 55),
            new OrderedPair(9, 55),
            new OrderedPair(12, 55),
            new OrderedPair(15, 55),
            new OrderedPair(18, 55),
            new OrderedPair(21, 55),
            new OrderedPair(24, 55),
            new OrderedPair(27, 55),
            new OrderedPair(30, 55),
            new OrderedPair(33, 55)
         },
         3,
         3
      ),
      SPLATTER_5PX(
         new OrderedPair[]{
            new OrderedPair(0, 58),
            new OrderedPair(5, 58),
            new OrderedPair(10, 58),
            new OrderedPair(15, 58),
            new OrderedPair(20, 58),
            new OrderedPair(25, 58),
            new OrderedPair(30, 58),
            new OrderedPair(35, 58),
            new OrderedPair(40, 58),
            new OrderedPair(45, 58),
            new OrderedPair(50, 58),
            new OrderedPair(55, 58)
         },
         5,
         5
      ),
      EXPANDING_SQUARE_16PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(144, 0),
            new OrderedPair(160, 0),
            new OrderedPair(176, 0),
            new OrderedPair(192, 0),
            new OrderedPair(208, 0),
            new OrderedPair(224, 0),
            new OrderedPair(240, 0)
         },
         16,
         16
      ),
      EXPANDING_SQUARE_12PX(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(146, 2),
            new OrderedPair(162, 2),
            new OrderedPair(178, 2),
            new OrderedPair(194, 2),
            new OrderedPair(210, 2)
         },
         12,
         12
      ),
      EXPANDING_SQUARE_8PX(
         new OrderedPair[]{new OrderedPair(4, 4), new OrderedPair(20, 4), new OrderedPair(148, 4), new OrderedPair(164, 4), new OrderedPair(180, 4)}, 8, 8
      ),
      EXPANDING_SQUARE_4PX(new OrderedPair[]{new OrderedPair(6, 6), new OrderedPair(22, 6), new OrderedPair(150, 6)}, 4, 4),
      EXPANDING_SQUARE_2PX(new OrderedPair[]{new OrderedPair(7, 7), new OrderedPair(23, 7)}, 2, 2),
      EXPANDING_SQUARE_16PX_HOLE_2PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(144, 16),
            new OrderedPair(160, 16),
            new OrderedPair(176, 16),
            new OrderedPair(192, 16),
            new OrderedPair(208, 16),
            new OrderedPair(224, 16),
            new OrderedPair(240, 16)
         },
         16,
         16
      ),
      EXPANDING_SQUARE_12PX_HOLE_2PX(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(146, 18),
            new OrderedPair(162, 18),
            new OrderedPair(178, 18),
            new OrderedPair(194, 18),
            new OrderedPair(210, 18)
         },
         12,
         12
      ),
      EXPANDING_SQUARE_8PX_HOLE_2PX(
         new OrderedPair[]{new OrderedPair(4, 4), new OrderedPair(20, 4), new OrderedPair(148, 20), new OrderedPair(164, 20), new OrderedPair(180, 20)}, 8, 8
      ),
      EXPANDING_SQUARE_16PX_HOLE_6PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(144, 16),
            new OrderedPair(160, 32),
            new OrderedPair(176, 48),
            new OrderedPair(192, 48),
            new OrderedPair(208, 48),
            new OrderedPair(224, 48),
            new OrderedPair(240, 48)
         },
         16,
         16
      ),
      EXPANDING_SQUARE_12PX_HOLE_6PX(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(146, 18),
            new OrderedPair(162, 34),
            new OrderedPair(178, 50),
            new OrderedPair(194, 50),
            new OrderedPair(210, 50)
         },
         12,
         12
      ),
      EXPANDING_SQUARE_16PX_HOLE_10PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(144, 16),
            new OrderedPair(160, 32),
            new OrderedPair(176, 48),
            new OrderedPair(192, 64),
            new OrderedPair(208, 80),
            new OrderedPair(224, 80),
            new OrderedPair(240, 80)
         },
         16,
         16
      ),
      EXPANDING_SQUARE_RING_4PX(new OrderedPair[]{new OrderedPair(6, 6), new OrderedPair(22, 6), new OrderedPair(150, 22)}, 4, 4),
      EXPANDING_SQUARE_RING_4PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(6, 6),
            new OrderedPair(22, 6),
            new OrderedPair(150, 22),
            new OrderedPair(150, 38),
            new OrderedPair(150, 54),
            new OrderedPair(150, 70)
         },
         4,
         4
      ),
      EXPANDING_SQUARE_RING_8PX(
         new OrderedPair[]{new OrderedPair(4, 4), new OrderedPair(20, 4), new OrderedPair(148, 20), new OrderedPair(164, 36), new OrderedPair(180, 52)}, 8, 8
      ),
      EXPANDING_SQUARE_RING_8PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(4, 4),
            new OrderedPair(20, 4),
            new OrderedPair(148, 20),
            new OrderedPair(164, 36),
            new OrderedPair(180, 52),
            new OrderedPair(180, 68),
            new OrderedPair(180, 84),
            new OrderedPair(180, 100)
         },
         8,
         8
      ),
      EXPANDING_SQUARE_RING_12PX(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(146, 18),
            new OrderedPair(162, 34),
            new OrderedPair(178, 50),
            new OrderedPair(194, 66),
            new OrderedPair(210, 82)
         },
         12,
         12
      ),
      EXPANDING_SQUARE_RING_12PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(2, 2),
            new OrderedPair(18, 2),
            new OrderedPair(146, 18),
            new OrderedPair(162, 34),
            new OrderedPair(178, 50),
            new OrderedPair(194, 66),
            new OrderedPair(210, 82),
            new OrderedPair(210, 98),
            new OrderedPair(210, 114),
            new OrderedPair(210, 130),
            new OrderedPair(210, 146)
         },
         12,
         12
      ),
      EXPANDING_SQUARE_RING_16PX(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(144, 16),
            new OrderedPair(160, 32),
            new OrderedPair(176, 48),
            new OrderedPair(192, 64),
            new OrderedPair(208, 80),
            new OrderedPair(224, 96),
            new OrderedPair(240, 112)
         },
         16,
         16
      ),
      EXPANDING_SQUARE_RING_16PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(0, 0),
            new OrderedPair(16, 0),
            new OrderedPair(144, 16),
            new OrderedPair(160, 32),
            new OrderedPair(176, 48),
            new OrderedPair(192, 64),
            new OrderedPair(208, 80),
            new OrderedPair(224, 96),
            new OrderedPair(240, 112),
            new OrderedPair(240, 128),
            new OrderedPair(240, 144),
            new OrderedPair(240, 160),
            new OrderedPair(240, 176)
         },
         16,
         16
      ),
      SOLID_SQUARE_EXPANDING_HOLE_16PX(
         new OrderedPair[]{
            new OrderedPair(240, 0),
            new OrderedPair(240, 16),
            new OrderedPair(240, 32),
            new OrderedPair(240, 48),
            new OrderedPair(240, 64),
            new OrderedPair(240, 80),
            new OrderedPair(240, 96),
            new OrderedPair(240, 112)
         },
         16,
         16
      ),
      SOLID_SQUARE_EXPANDING_HOLE_16PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(240, 0),
            new OrderedPair(240, 16),
            new OrderedPair(240, 32),
            new OrderedPair(240, 48),
            new OrderedPair(240, 64),
            new OrderedPair(240, 80),
            new OrderedPair(240, 96),
            new OrderedPair(240, 112),
            new OrderedPair(240, 128),
            new OrderedPair(240, 144),
            new OrderedPair(240, 160),
            new OrderedPair(240, 176)
         },
         16,
         16
      ),
      SOLID_SQUARE_EXPANDING_HOLE_12PX(
         new OrderedPair[]{
            new OrderedPair(210, 2),
            new OrderedPair(210, 18),
            new OrderedPair(210, 34),
            new OrderedPair(210, 50),
            new OrderedPair(210, 66),
            new OrderedPair(210, 82)
         },
         12,
         12
      ),
      SOLID_SQUARE_EXPANDING_HOLE_12PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(210, 2),
            new OrderedPair(210, 18),
            new OrderedPair(210, 34),
            new OrderedPair(210, 50),
            new OrderedPair(210, 66),
            new OrderedPair(210, 82),
            new OrderedPair(210, 98),
            new OrderedPair(210, 114),
            new OrderedPair(210, 130),
            new OrderedPair(210, 146)
         },
         12,
         12
      ),
      SOLID_SQUARE_EXPANDING_HOLE_8PX(
         new OrderedPair[]{new OrderedPair(180, 4), new OrderedPair(180, 20), new OrderedPair(180, 36), new OrderedPair(180, 52)}, 8, 8
      ),
      SOLID_SQUARE_EXPANDING_HOLE_8PX_DISSIPATING(
         new OrderedPair[]{
            new OrderedPair(180, 4),
            new OrderedPair(180, 20),
            new OrderedPair(180, 36),
            new OrderedPair(180, 52),
            new OrderedPair(180, 68),
            new OrderedPair(180, 84),
            new OrderedPair(180, 100)
         },
         8,
         8
      ),
      SOLID_SQUARE_EXPANDING_HOLE_4PX(new OrderedPair[]{new OrderedPair(150, 6), new OrderedPair(150, 22)}, 4, 4),
      SOLID_SQUARE_EXPANDING_HOLE_4PX_DISSIPATING(
         new OrderedPair[]{new OrderedPair(150, 6), new OrderedPair(150, 22), new OrderedPair(150, 38), new OrderedPair(150, 54), new OrderedPair(150, 70)},
         4,
         4
      );

      private int width;
      private int height;
      private OrderedPair[] textureCoordinateData;

      private ParticleAnimation(OrderedPair[] textureCoordinateData, int width, int height) {
         this.textureCoordinateData = textureCoordinateData;
         this.width = width;
         this.height = height;
      }

      public OrderedPair[] getTextureCoordinateData() {
         return this.textureCoordinateData;
      }

      public int getWidth() {
         return this.width;
      }

      public int getHeight() {
         return this.height;
      }
   }
}
