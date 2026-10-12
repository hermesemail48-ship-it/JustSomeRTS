# Rise to Ruins — iOS cloud build

Everything needed to compile the iPhone app on a GitHub-hosted Mac. Generated from the main workspace by
`scripts/make_ios_cloud_build.py`; don't edit here, re-generate instead.

**Keep this repository PRIVATE.** It contains the decompiled game code and game data from your own Steam copy,
which you may not redistribute. The resulting IPA is for your own devices only.

| Folder | What it is |
|---|---|
| `ports/mobile/` | The libGDX port: `slick-gles` (Slick2D on OpenGL ES), `game` (build glue), `ios` (MobiVM app) |
| `recomp/src/` | The recompilable game source the `game` module builds from |
| `mobile-data/` | Game data converted for iOS (`res/`, `localization/`, `maps/`; MP3/WAV audio) — bundled into the app |
| `.github/workflows/build-ipa.yml` | The cloud build |

## 1. Create the private repo and push
Create an **empty private** repository on GitHub (no README), then from this folder:
```
git init -b main
git add .
git commit -m "iOS cloud build"
git remote add origin https://github.com/<you>/<repo>.git
git push -u origin main
```
The first push uploads ~750 MB; that is fine for GitHub (no file is over 100 MB).

## 2. Run the build
Pushing to `main` starts it automatically, or go to **Actions → Build iOS IPA → Run workflow**.
The first run takes roughly 20–40 minutes (MobiVM compiles the whole game ahead of time); later runs reuse caches.

GitHub bills macOS minutes at 10× for private repos: the free plan's 2,000 minutes ≈ 200 macOS minutes a month,
so a handful of builds.

## 3. Install on your iPhone
1. Open the finished run, download the **RiseToRuins-ipa** artifact and unzip it to get `RiseToRuins.ipa`.
2. Install it with [Sideloadly](https://sideloadly.io) (Windows or Mac): plug in the iPhone, drop in the IPA,
   enter your Apple ID. Sideloadly signs it for your device.
3. On the iPhone: Settings → General → VPN & Device Management → trust your Apple ID; with iOS 16+ also enable
   Settings → Privacy & Security → Developer Mode.

With a free Apple ID the app expires after 7 days — re-install with Sideloadly (saves are kept). A paid Apple
Developer account ($99/yr) gives 1-year installs.

Saves live in **Files → On My iPhone → Rise to Ruins → rtr/profiles**.

## Controls
Tap = click · one-finger drag = drag · long press = right click (cancel) · two-finger drag = pan · pinch = zoom.

## If the build fails
Download the `build-logs` artifact from the failed run and the run's console output, and bring them back to the
workspace session.
