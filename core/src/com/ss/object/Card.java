package com.ss.object;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GArcMoveToAction;
import com.ss.core.util.GUI;

public class Card {
    TextureAtlas cardAtlas;
    Image card, tileDown, overlay;
    Group group;
    public int[] value;
    int Key ;

    Card( TextureAtlas cardAtlas,Group group, int value){
        this.cardAtlas = cardAtlas;
        this.group = group;
        this.value =new int[]{value};
        card = GUI.createImage(cardAtlas,""+this.value[0]);
        card.setWidth(card.getWidth()*0.7f);
        card.setHeight(card.getHeight()*0.7f);
        card.setOrigin(Align.center);
        card.setPosition(GMain.screenWidth/2-100,card.getHeight()/2,Align.center);
        this.group.addActor(card);
        //////// tileDown////////
        tileDown = GUI.createImage(cardAtlas,"tileDown1");
        tileDown.setWidth(tileDown.getWidth()*0.7f);
        tileDown.setHeight(tileDown.getHeight()*0.7f);
        tileDown.setOrigin(Align.center);
        tileDown.setAlign(Align.center);
        tileDown.setPosition(GMain.screenWidth/2-100,tileDown.getHeight()/2,Align.center);
        this.group.addActor(tileDown);
        //////// over lay ////////
        overlay = GUI.createImage(cardAtlas,"overLay");
        overlay.setWidth(overlay.getWidth()*0.7f);
        overlay.setHeight(overlay.getHeight()*0.7f);
        overlay.setOrigin(Align.center);
        overlay.setAlign(Align.center);
        overlay.setPosition(GMain.screenWidth/2-100,overlay.getHeight()/2-2,Align.center);
        this.group.addActor(overlay);
        overlay.setVisible(false);
    }
    public void setVisibleTiledown(){
        tileDown.setVisible(false);
    }
    public void moveCard(String type,float x,float y){
        card.setVisible(true);
        tileDown.setVisible(true);
        card.addAction(Actions.moveTo(x-card.getWidth()/2, y-card.getHeight()/2, boardConfig.durationDistrbute, Interpolation.slowFast));
        card.addAction(Actions.rotateTo(-360, boardConfig.durationDistrbute, Interpolation.circleIn));
        tileDown.addAction(Actions.rotateTo(-360, boardConfig.durationDistrbute, Interpolation.circleIn));
        tileDown.addAction(Actions.moveTo(x- tileDown.getWidth()/2, y-tileDown.getHeight()/2, boardConfig.durationDistrbute, Interpolation.slowFast));
        overlay.addAction(Actions.moveTo(x- overlay.getWidth()/2, y-overlay.getHeight()/2, boardConfig.durationDistrbute, Interpolation.slowFast));
        if(type=="intable"){
            Tweens.setTimeout(group,0.27f,()->{
                aniCard(100,80,0.6f); 
            });
        }
    }
    public void aniCard(float x, float y,float dura){
        card.addAction(Actions.sequence(
                Actions.moveBy(x,y,dura,Interpolation.sineIn),
                Actions.moveBy(-x,-y,dura,Interpolation.swingOut)
        ));
        tileDown.addAction(Actions.sequence(
                Actions.moveBy(x,y,dura,Interpolation.sineIn),
                Actions.moveBy(-x,-y,dura,Interpolation.swingOut)
        ));
        overlay.addAction(Actions.sequence(
                Actions.moveBy(x,y,dura,Interpolation.sineIn),
                Actions.moveBy(-x,-y,dura,Interpolation.swingOut)
        ));
    }
    public void scaleCard(){
        card.addAction(Actions.scaleTo(boardConfig.scaleBot,boardConfig.scaleBot, boardConfig.durationDistrbute));
        tileDown.addAction(Actions.scaleTo(boardConfig.scaleBot,boardConfig.scaleBot, boardConfig.durationDistrbute));
        overlay.addAction(Actions.scaleTo(boardConfig.scaleBot,boardConfig.scaleBot, boardConfig.durationDistrbute));


    }
    public void rotateCard(int angle){
        card.addAction(Actions.rotateTo(angle, boardConfig.durationDistrbute));
        tileDown.addAction(Actions.rotateTo(angle, boardConfig.durationDistrbute));
        overlay.addAction(Actions.rotateTo(angle, boardConfig.durationDistrbute));
    }
    public void flipCard(int x){
        card.addAction(Actions.scaleTo(x,1,boardConfig.durationDistrbute));
    }
    public void flipAllCard(float x){
        card.addAction(Actions.scaleTo(x,boardConfig.scaleBot,boardConfig.durationDistrbute));
    }
    public void setPosition(float x,float y){
        card.setPosition(x,y, Align.center);
        tileDown.setPosition(x,y, Align.center);
        overlay.setPosition(x,y, Align.center);
    }
    public void setColorCard(){
        card.setColor(Color.DARK_GRAY);
    }
    public void setKey(int id){
        Key=id;
    }
    public void setVisibleAll(){
        card.setVisible(false);
        tileDown.setVisible(false);
        overlay.setVisible(false);
    }

}
