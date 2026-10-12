package rtr.particles.projectiles;

import java.util.ArrayList;
import org.newdawn.slick.SlickException;
import rtr.ModuleBase;
import rtr.particles.ParticleModule;
import rtr.states.StateBase;

public class ProjectileModule extends ModuleBase {
   private ArrayList<ProjectileBase> projectilesArray = new ArrayList<>();

   @Override
   public void initModule(ModuleBase.ModuleType mT, StateBase cS) throws SlickException {
      super.initModule(mT, cS);
      this.initLoad();
   }

   @Override
   public void resetModule() throws SlickException {
      super.resetModule();
      this.projectilesArray.clear();
   }

   @Override
   protected void newMainMenu() throws SlickException {
   }

   @Override
   protected void newPlay() throws SlickException {
   }

   @Override
   protected void loadPlay() throws SlickException {
   }

   @Override
   protected void newMapEditor() throws SlickException {
   }

   @Override
   protected void loadMapEditor() throws SlickException {
   }

   @Override
   protected void newWorldMap() throws SlickException {
   }

   @Override
   protected void loadWorldMap() throws SlickException {
   }

   @Override
   protected void loadRegionalSaveData() throws SlickException {
   }

   @Override
   protected void loadWorldSaveData() throws SlickException {
   }

   public void update() throws SlickException {
      for (int i = 0; i < this.projectilesArray.size(); i++) {
         this.projectilesArray.get(i).update();
      }
   }

   public void newEnergyNova(int x, int y, ParticleModule.ParticleSet s) {
      ProjectileBase projectile = new ProjectileEnergyNova(x, y, s);
      this.projectilesArray.add(projectile);
   }

   public void newEnergyRing(int x, int y, int r, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2) {
      ProjectileBase projectile = new ProjectileEnergyRing(x, y, r, c1, c2);
      this.projectilesArray.add(projectile);
   }

   public void newEnergyRing(int x, int y, int r, ParticleModule.ParticleSet c1) {
      ProjectileBase projectile = new ProjectileEnergyRing(x, y, r, c1);
      this.projectilesArray.add(projectile);
   }

   public void newExpandingEnergyTrail(int x, int y, ParticleModule.ParticleSet s) {
      this.newExpandingEnergyTrail(x, y, s, false);
   }

   public void newExpandingEnergyTrail(int x, int y, ParticleModule.ParticleSet s, boolean guiProjectile) {
      ProjectileBase projectile = new ProjectileExpandingEnergyTrail(x, y, s, guiProjectile);
      this.projectilesArray.add(projectile);
   }

   public void newExpandingFireRing(int x, int y, int r, float speed, ParticleModule.ParticleSet s) {
      this.newExpandingFireRing(x, y, r, speed, s, false);
   }

   public void newExpandingFireRing(int x, int y, int r, float speed, ParticleModule.ParticleSet s, boolean guiProjectile) {
      ProjectileBase projectile = new ProjectileExpandingFireRing(x, y, r, speed, s, guiProjectile);
      this.projectilesArray.add(projectile);
   }

   public void newConstrictingFireRing(int x, int y, int r, float speed, ParticleModule.ParticleSet s) {
      this.newConstrictingFireRing(x, y, r, speed, s, false);
   }

   public void newConstrictingFireRing(int x, int y, int r, float speed, ParticleModule.ParticleSet s, boolean guiProjectile) {
      ProjectileBase projectile = new ProjectileConstrictingFireRing(x, y, r, speed, s, guiProjectile);
      this.projectilesArray.add(projectile);
   }

   public void newExpandingEnergyRing(int x, int y, int r, float speed, ParticleModule.ParticleSet c1) {
      this.newExpandingEnergyRing(x, y, r, speed, c1, c1, false);
   }

   public void newExpandingEnergyRing(int x, int y, int r, float speed, ParticleModule.ParticleSet c1, boolean guiProjectile) {
      this.newExpandingEnergyRing(x, y, r, speed, c1, c1, guiProjectile);
   }

   public void newExpandingEnergyRing(int x, int y, int r, float speed, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2) {
      this.newExpandingEnergyRing(x, y, r, speed, c1, c2, false);
   }

   public void newExpandingEnergyRing(int x, int y, int r, float speed, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2, boolean guiProjectile) {
      ProjectileBase projectile = new ProjectileExpandingEnergyRing(x, y, r, speed, c1, c2, guiProjectile);
      this.projectilesArray.add(projectile);
   }

   public void newConstrictingEnergyRing(int x, int y, int r, float speed, ParticleModule.ParticleSet c1) {
      this.newConstrictingEnergyRing(x, y, r, speed, c1, c1, false);
   }

   public void newConstrictingEnergyRing(int x, int y, int r, float speed, ParticleModule.ParticleSet c1, boolean guiProjectile) {
      this.newConstrictingEnergyRing(x, y, r, speed, c1, c1, guiProjectile);
   }

   public void newConstrictingEnergyRing(int x, int y, int r, float speed, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2) {
      this.newConstrictingEnergyRing(x, y, r, speed, c1, c2, false);
   }

   public void newConstrictingEnergyRing(int x, int y, int r, float speed, ParticleModule.ParticleSet c1, ParticleModule.ParticleSet c2, boolean guiProjectile) {
      ProjectileBase projectile = new ProjectileConstrictingEnergyRing(x, y, r, speed, c1, c2, guiProjectile);
      this.projectilesArray.add(projectile);
   }

   public void newGroundStatic(int x, int y, ParticleModule.ParticleSet s) {
      ProjectileBase projectile = new ProjectileGroundStatic(x, y, s);
      this.projectilesArray.add(projectile);
   }

   public void newEnergyBeam(int x, int y, int x2, int y2) {
      ProjectileBase projectile = new ProjectileEnergyBeam(x, y, x2, y2);
      this.projectilesArray.add(projectile);
   }

   public void newSmallBlast(int x, int y, ParticleModule.ParticleSet s) {
      ProjectileBase projectile = new ProjectileSmallBlast(x, y, s);
      this.projectilesArray.add(projectile);
   }

   public void newMagicTrail(int x, int y, ParticleModule.ParticleSet s) {
      ProjectileBase projectile = new ProjectileMagicTrail(x, y, s);
      this.projectilesArray.add(projectile);
   }

   public void newDestruction(int x, int y) {
      ProjectileBase projectile = new ProjectileDestruction(x, y);
      this.projectilesArray.add(projectile);
   }

   public void remove(ProjectileBase p) {
      this.projectilesArray.remove(p);
   }
}
