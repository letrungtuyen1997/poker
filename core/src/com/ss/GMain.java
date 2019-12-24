package com.ss;

import com.badlogic.gdx.Preferences;
import com.effect.SoundEffect;
import com.platform.IPlatform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.ss.assetManager.XAssetsManager;
import com.ss.core.exSprite.particle.GParticleSystem;
import com.ss.core.util.GDirectedGame;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GStage.StageBorder;
import com.ss.gameLogic.scene.GMenu;
import com.ss.scene.GamePlay;
import com.ss.scene.GameStart;

public class GMain
        extends GDirectedGame
{

  public static boolean isDebug = false;
  public static final boolean isTest = false;
  public static GScreen mapEditorScreen;
  public static GMain me;
  public static float screenHeight = 0;
  public static float screenWidth = 0;
  public static int screenId = -1;
  public static GScreen shooterTestScreen;
  public static final int testType = 2;
  public static Preferences prefs;
  public static long mymonney = 0;
  public static IPlatform platform;
  public static int checkFrist = 0;
  public GMain(IPlatform plat){
    platform = plat;
  }

  private void init()
  {
    float n = 480.0f;
    final boolean b = false;// 0.0f > 1.0f;
    final float n2 = Gdx.graphics.getWidth();
    final float n3 = Gdx.graphics.getHeight();
    final float n4 = n2 / n3;
    float n5;
    float n6;
    if (n4 == 0.0f) {
      n5 = 0.0f;
      n6 = 848.0f;
    }
    else if ((b && n4 > 0.0f) || (!b && n4 < 0.0f)) {
      final float n7 = 848.0f * n4;
      n5 = (n - n7) / 2.0f;
      n = n7;
      n6 = 848.0f;
    }
    else if ((b && n4 < 0.0f) || (!b && n4 > 0.0f)) {
      final float max = Math.max(800.0f, n / n4);
      GMain.screenHeight = (int)(0.5f + max);
      n6 = max;
      n5 = 0.0f;
    }
    else {
      n = n2;
      n6 = n3;
      n5 = 0.0f;
    }

    screenWidth = 1280;
    screenHeight = 720;
    n = 1280;
    n6 = 720;

    GStage.init(n, n6, n5, 0, new StageBorder() {
      @Override
      public void drawHorizontalBorder(Batch spriteBatch, float paramFloat1, float paramFloat2) {

      }

      @Override
      public void drawVerticalBorder(Batch spriteBatch, float paramFloat1, float paramFloat2) {

      }
    });
  }

  public static GScreen menuScreen()
  {
    return new GMenu();
  }

  public void create()
  {
    prefs = Gdx.app.getPreferences("MyData");

    XAssetsManager.init();
    SoundEffect.initSound();
    this.init();
    checkFrist = prefs.getInteger("checkFirst");
    this.setScreen(new GameStart());
  }

  public void dispose()
  {
    GMain.platform.log("############## gmain dispose");
    GParticleSystem.saveAllFreeMin();
    super.dispose();
  }
}
