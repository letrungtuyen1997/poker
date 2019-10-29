package com.ss.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ss.assetManager.XAssetsManager;

public class animation extends Actor {
  Batch batch;
  Animation<TextureRegion> tg;
  float stateTime = 0;
  float x, y;
  public animation(float x, float y, String key, int col , int row, float duration, boolean isFlipX){

    tg = XAssetsManager.getAnimation(key,col,row,duration);
    batch = new SpriteBatch();
    for (int i=0;i<tg.getKeyFrames().length;i++){
      tg.getKeyFrames()[i].flip(isFlipX,true);
    }
    this.x = x-tg.getKeyFrames()[0].getRegionWidth()/2;
    this.y = y-tg.getKeyFrames()[0].getRegionHeight()/2;


  }
  @Override
  public void draw(Batch batch, float parentAlpha) {
    TextureRegion t = tg.getKeyFrame(stateTime);
    stateTime += Gdx.graphics.getDeltaTime();
    if((Math.floor(stateTime)<= tg.getFrameDuration()*tg.getKeyFrames().length)){
      batch.draw(t,x,y);
      return;

    }
  }
  public void start(){
    stateTime=0;
  }
  public void stop(){
    stateTime =tg.getKeyFrames().length+1;
  }


}