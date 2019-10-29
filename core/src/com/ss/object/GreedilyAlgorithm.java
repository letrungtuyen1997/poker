package com.ss.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GArcMoveToAction;
import com.ss.core.util.GUI;

public class GreedilyAlgorithm {
  TextureAtlas textureAtlas;
  Group group;
  public Array<long[]> listMoney;
  long[] vt1, vt2, vt5, vt10, vt100, vt1k, vt10k, vt100k, vt1m;
  long[] vt10m, vt100m, vt1b, vt10b, vt100b;
  Array<Image> chips;
  int type;
  float moveX=0, moveY=0;

  public GreedilyAlgorithm(TextureAtlas textureAtlas, Group group){
    this.textureAtlas = textureAtlas;
    this.group = group;
    initListMoney();
  }

  private void initListMoney(){
    listMoney = new Array<>();
    vt1 = new long[]{1l, 0l};
    vt2 = new long[]{2l, 0l};
    vt5 = new long[]{5l, 0l};
    vt10 = new long[]{10l, 0l};
    vt100 = new long[]{100l, 0l};
    vt1k = new long[]{1000l, 0l};
    vt10k = new long[]{10000l, 0l};
    vt100k = new long[]{100000l, 0l};
    vt1m = new long[]{1000000l, 0l};
    vt10m = new long[]{10000000l, 0l};
    vt100m = new long[]{100000000l, 0l};
    vt1b = new long[]{1000000000l, 0l};
    vt10b = new long[]{10000000000l, 0l};
    vt100b = new long[]{100000000000l, 0l};
    listMoney.add(vt1, vt2, vt5, vt10);
    listMoney.add(vt100, vt1k);
    listMoney.add(vt10k, vt100k, vt1m, vt10m);
    listMoney.add(vt100m, vt1b, vt10b, vt100b);
  }

  private void greedily(long money){
    reset();
    long moneyTemp = money;
    while(moneyTemp != 0){
      if(moneyTemp >= 100000000000l){
        vt100b[1] = moneyTemp/100000000000l;
        moneyTemp = moneyTemp%100000000000l;
      }
      else if(moneyTemp >= 10000000000l){
        vt10b[1] = moneyTemp/10000000000l;
        moneyTemp = moneyTemp%10000000000l;
      }
      else if(moneyTemp >= 1000000000l){
        vt1b[1] = moneyTemp/1000000000l;
        moneyTemp = moneyTemp%1000000000l;
      }
      else if(moneyTemp >= 100000000l){
        vt100m[1] = moneyTemp/100000000l;
        moneyTemp = moneyTemp%100000000l;
      }
      else if(moneyTemp >= 10000000l){
        vt10m[1] = moneyTemp/10000000l;
        moneyTemp = moneyTemp%10000000l;
      }
      else if(moneyTemp >= 1000000l){
        vt1m[1] = moneyTemp/1000000l;
        moneyTemp = moneyTemp%1000000l;
      }
      else if(moneyTemp >= 100000l){
        vt100k[1] = moneyTemp/100000l;
        moneyTemp = moneyTemp%100000l;
      }
      else if(moneyTemp >= 10000l){
        vt10k[1] = moneyTemp/10000l;
        moneyTemp = moneyTemp%10000l;
      }
      else if(moneyTemp >= 1000l){
        vt1k[1] = moneyTemp/1000l;
        moneyTemp = moneyTemp%1000l;
      }
      else if(moneyTemp >= 100l){
        vt100[1] = moneyTemp/100l;
        moneyTemp = moneyTemp%100l;
      }
      else if(moneyTemp >= 10l){
        vt10[1] = moneyTemp/10l;
        moneyTemp = moneyTemp%10l;
      }
      else if(moneyTemp >= 5l){
        vt5[1] = moneyTemp/5l;
        moneyTemp = moneyTemp%5l;
      }
      else if(moneyTemp >= 2l){
        vt2[1] = moneyTemp/2l;
        moneyTemp = moneyTemp%2l;
      }
      else if(moneyTemp == 1){
        vt1[1] = 1;
        moneyTemp = 0;
      }
    }
  }

  public void output(){
    for(long[] moneys : listMoney){
      Gdx.app.log("" + moneys[0], " " + moneys[1] + " to");
    }
  }

  public void renderChip(long money, float x, float y, boolean mode){
    greedily(money);
    if(chips != null){
      for(Image chip : chips){
        chip.remove();
      }
      chips.clear();
      chips = null;
    }
    chips = new Array<>();
    int dem = 0;
    int dem1 = 0;
    for(long[] vt : listMoney){
      if(vt[1] != 0){
        dem++;
        if(dem%5 == 0){
          dem1++;
          dem = 1;
        }
      }
      for(int i = 0; i < vt[1]; i++){
        Image chip = GUI.createImage(textureAtlas, "c" + vt[0]);
        chip.setVisible(mode);
        chip.setSize(chip.getWidth()*0.3f, chip.getHeight()*0.3f);
        chip.setPosition(dem*chip.getWidth() + x, dem1*chip.getHeight() + y - i*2);
        group.addActor(chip);
        chips.add(chip);
      }
    }
  }

  private void reset(){
    for(long[] vt : listMoney){
      vt[1] = 0;
    }
  }

  public void moveChips(int type,float x, float y, float duaration, Interpolation interpolation, Runnable runnable){
    if(boardConfig.modePlay==3){
      if(type==0){
        moveX = x-70;
        moveY = y+200;
      }else if(type==1){
        moveX = x-20;
        moveY = y-20;
      }else if(type==2){
        moveX = x+30;
        moveY = y-20;
      }
    }else {
      if(type==0){
        moveX = x-70;
        moveY = y+200;
      }else if(type==1){
        moveX = x-130;
        moveY = y+20;
      }else if(type==2){
        moveX = x-20;
        moveY = y-20;
      }else if(type==3){
        moveX = x+30;
        moveY = y-20;
      }else if(type==4){
        moveX = x+100;
        moveY = y+70;
      }
    }
    for(Image chip : chips){
      chip.setScale(2f);
      Tweens.setTimeout(group, 0.05f*chips.indexOf(chip, true), ()->{
        chip.setVisible(true);
        chip.addAction(Actions.parallel(
                GArcMoveToAction.arcMoveTo(x,y-(2*chips.indexOf(chip,true)),moveX,moveY,duaration, Interpolation.fastSlow),
                Actions.scaleTo(1f,1f,duaration)
        ));
      });
    }
    group.addAction(Actions.sequence(Actions.delay(0.05f*chips.size + duaration), Actions.run(runnable)));
  }
  public void moveChipsWin(int type,float x, float y, float duaration, Interpolation interpolation, Runnable runnable){
    if(boardConfig.modePlay==3){
      if(type==0){
        moveX = x-20;
        moveY = y-150;
      }else if(type==1){
        moveX = x+30;
        moveY = y+20;
      }else if(type==2){
        moveX = x-30;
        moveY = y+20;
      }
    }else {
      if(type==0){
        moveX = x-20;
        moveY = y-150;
      }else if(type==1){
        moveX = x+100;
        moveY = y;
      }else if(type==2){
        moveX = x+10;
        moveY = y+20;
      }else if(type==3){
        moveX = x-30;
        moveY = y+20;
      }else if(type==4){
        moveX = x-70;
        moveY = y-20;
      }
    }

    for(Image chip : chips){
      chip.setScale(2f);
      Tweens.setTimeout(group, 0.05f*chips.indexOf(chip, true), ()->{
        chip.setVisible(true);
        chip.addAction(Actions.parallel(
                GArcMoveToAction.arcMoveTo(x,y-(2*chips.indexOf(chip,true)),moveX,moveY,duaration, Interpolation.fastSlow),
                Actions.scaleTo(1f,1f,duaration)
        ));
      });

    }
    group.addAction(Actions.sequence(Actions.delay(0.05f*chips.size + duaration), Actions.run(runnable)));
  }
  public void disposeChip(){
    for (Image chip :chips){
      chip.clear();
      chip.remove();
    }
  }
}
