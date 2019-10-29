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
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.scene.GamePlay;

public class moveCoins {
    Group group = new Group();
    Group groupChip = new Group();
    TextureAtlas uiAtlas;
    Image coins;
    int type;
    float moveX=0, moveY=0;

    Array<Image> arrCoins = new Array<>();
    Array<Image> arrCoins2 = new Array<>();
    moveCoins(int type,TextureAtlas uiAtlas,float x, float y,long value){
        this.type = type;
        GStage.addToLayer(GLayer.top,group);
        GStage.addToLayer(GLayer.top,groupChip);
        this.uiAtlas = uiAtlas;
        int quantityChip = (int)value/500;
        for (int i=0;i<quantityChip;i++){
            coins = GUI.createImage(uiAtlas,"c500");
            coins.setOrigin(Align.center);

            coins.setPosition(x,y-100, Align.center);
            group.addActor(coins);
            arrCoins.add(coins);

        }
        if(type==0){
            moveX = arrCoins.get(0).getX()-20;
            moveY = (arrCoins.get(0).getY()+200)/2;
        }else if(type==1){
            moveX = (arrCoins.get(0).getX()+800)/2;
            moveY = arrCoins.get(0).getY()-100;
        }else if(type==2){
            moveX = (arrCoins.get(0).getX()+700)/2;
            moveY = arrCoins.get(0).getY()+20;
        }else if(type==3){
            moveX = arrCoins.get(0).getX()-20;
            moveY = arrCoins.get(0).getY()+10;
        }else if(type==4){
            moveX = arrCoins.get(0).getX()-200;
            moveY = arrCoins.get(0).getY()-20;
        }
        move();
    }
    void move(){
            for (int i=0;i<arrCoins.size;i++){
            arrCoins.get(i).addAction(Actions.parallel(
                    GArcMoveToAction.arcMoveTo(GMain.screenWidth/2,200-(1*i),moveX,moveY,0.12f*i, Interpolation.fastSlow),
                    Actions.scaleTo(0.3f,0.3f,0.12f*i)
            ));
            }
        Tweens.setTimeout(group,0.12f*arrCoins.size,()->{
            group.clear();
           initChip(arrCoins.size);
           arrCoins.clear();
        });
    }
    void initChip(int quantity){
        for(int i =0; i<quantity;i++){
            Image chip = GUI.createImage(uiAtlas,"c500");
            chip.setPosition(GMain.screenWidth/2+100,250-(i*2),Align.center);
            chip.setWidth(chip.getWidth()*0.3f);
            chip.setHeight(chip.getHeight()*0.3f);
            chip.setOrigin(Align.center);
            groupChip.addActor(chip);
            arrCoins2.add(chip);
        }

    }


}
