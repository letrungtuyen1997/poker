package com.ss.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.exSprite.GShapeSprite;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.scene.GamePlay;

import java.text.DecimalFormat;

public class MoreBets {
    TextureAtlas uiAtlas;
    BitmapFont font;
    Group group = new Group();
    float xmax,xmin;
    Label monney;
    static long monneyCuocMore=0;
    GamePlay gamePlay;
    BoardGame boardGame;
    Runnable runnable;
    MoreBets(TextureAtlas uiAtlas,Runnable runnable){
        GStage.addToLayer(GLayer.top,group);
        this.uiAtlas = uiAtlas;
        this.runnable = runnable;
        initfont();
        showbgMoreBets();

    }
    void showbgMoreBets(){
        group.setPosition(GMain.screenWidth/2,GMain.screenHeight/2,Align.center);
        group.setOrigin(Align.center);
        final GShapeSprite blackOverlay = new GShapeSprite();
        blackOverlay.createRectangle(true, -GMain.screenWidth/2,-GMain.screenHeight/2, GMain.screenWidth*2, GMain.screenHeight*2);
        blackOverlay.setColor(0,0,0,0.8f);
        group.addActor(blackOverlay);
        loadSlider();


    }
    void loadSlider(){
        ////////// bar////////
        Image sliderBar = GUI.createImage(uiAtlas,"sliderBar");
        sliderBar.setPosition(0,0, Align.center);
        group.addActor(sliderBar);
        /////// coins ///////
        Image sliderCoins = GUI.createImage(uiAtlas,"sliderCoins");
        sliderCoins.setPosition(-sliderBar.getWidth()/2+sliderCoins.getWidth()/2,0, Align.center);
        group.addActor(sliderCoins);
        //////////////
        xmin = -sliderBar.getWidth()/2+sliderCoins.getWidth()/2;
        xmax = 220;
        /////// btn Done //////
        Image btnDone = GUI.createImage(uiAtlas,"btnDone");
        btnDone.setOrigin(Align.center);
        btnDone.setPosition(0,200,Align.center);
        group.addActor(btnDone);
        //////// event btn ////////
        eventBtnDone(btnDone);
        eventBtnCoins(sliderCoins);
        //////// label //////////
        monney = new Label("",new Label.LabelStyle(font,null));
        monney.setPosition(0,100,Align.center);
        monney.setAlignment(Align.center);
        group.addActor(monney);
//        monneyCuocMore= (long)(gamePlay.myMonney*(1/4));
//        monney.setText(""+FortmartPrice(monneyCuocMore));
    }

    void eventBtnDone(Image btnDone){
        btnDone.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                btnDone.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.2f),
                        Actions.scaleTo(1f,1f,0.2f)
                ));
                Tweens.setTimeout(group,0.4f,()->{
                    GamePlay.uiGroup.addAction(Actions.run(runnable));
                    group.clear();
                });
            }
        });
    }
    void eventBtnCoins(Image btn){
        float divindend = 4;
        btn.addListener(new DragListener(){
            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
                float detal = x - btn.getWidth()/2;
                if(btn.getX() + detal < xmin - btn.getWidth()/2){
                    btn.setX(xmin-btn.getWidth()/2);
                }
                else if(btn.getX() + detal > xmax){
                    btn.setX(xmax);
                }
                else btn.moveBy(x-btn.getWidth()/2, 0);
                Gdx.app.log("debug", "x: " + btn.getX());
                //(btn.getX()+detal)/xmax
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                super.dragStop(event, x, y, pointer);
                float part = 490/divindend;
                int div = (int) ((btn.getX() + 270)/part);
                Gdx.app.log("debug","div: " + div + " part: " + part);
                if(btn.getX() == -270){
                    btn.setX(xmin-btn.getWidth()/2);
                }
                else if(btn.getX() == 220|| div*490/divindend - 270 > 220)
                    btn.setX(220);
                else btn.setX(div*490/divindend - 270);
                monneyCuocMore= (long)(gamePlay.myMonney*(div/divindend));
                monney.setText("" + FortmartPrice(monneyCuocMore));
            }
        });
    }

    private String FortmartPrice(Long Price) {

        DecimalFormat mDecimalFormat = new DecimalFormat("###,###,###,###");
        String mPrice = mDecimalFormat.format(Price);

        return mPrice;
    }
    void initfont(){
        font = GAssetsManager.getBitmapFont("gold.fnt");

    }
}
