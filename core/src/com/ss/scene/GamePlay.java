package com.ss.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.ss.GMain;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.object.BoardGame;
import com.ss.object.CheckCard;
import com.ss.object.boardConfig;

import java.text.DecimalFormat;

public class GamePlay extends GScreen {
    public TextureAtlas uiAtlas, CardAtlas;
    public Array<Vector2> positionCards;
    Group bgGroup = new Group();
    public static Group uiGroup = new Group();
    public static long myMonney =GameStart.MyMonney;
    public static Label labelMonney;
    BitmapFont fontMyMonney;

    @Override
    public void dispose() {
        bgGroup.clear();
        uiGroup.clear();
        labelMonney.clear();

    }

    @Override
    public void init() {
        GStage.addToLayer(GLayer.ui,bgGroup);
        GStage.addToLayer(GLayer.ui,uiGroup);
        initAtlas();
        initFont();
        initPositionCards();
        showBg();
        loadMonney();

    }

    @Override
    public void run() {

    }

    void showBg(){
        Image bg = GUI.createImage(uiAtlas,"table1");
        bg.setPosition(0,0, Align.bottomLeft);
        bgGroup.addActor(bg);
//        Image table = GUI.createImage(uiAtlas,"table");
//        table.setPosition(GMain.screenWidth/2,GMain.screenHeight/2,Align.center);
//        uiGroup.addActor(table);
        new CheckCard();
        CheckCard.makeCards();

//        Integer.toBinaryString()
        new BoardGame(CardAtlas,uiAtlas, this);

    }
    private void initPositionCards(){
        positionCards = new Array<>();
        int numberPlayer = boardConfig.modePlay;
        float delta2 = (GMain.screenHeight*16/9)/5;
        float delta3 = (GMain.screenHeight*16/9)/15;
        float deltaY = GMain.screenHeight/20;
        float delta = (GMain.screenWidth - (float)GMain.screenHeight*16/9)/2;
        switch (numberPlayer){
            case 2: {
                Vector2 position1 = new Vector2(delta + delta2 + delta3, GMain.screenHeight*3/4+100);
                Vector2 position2 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight/3 - deltaY);
                positionCards.add(position1, position2);
                break;
            }
            case 3: {
                Vector2 position1 = new Vector2(GMain.screenWidth/2, GMain.screenHeight*3/4+100);
                Vector2 position2 = new Vector2(delta + delta2+200,GMain.screenHeight/3);
                Vector2 position3 = new Vector2((float)GMain.screenHeight*16/9 - delta - delta2-150, GMain.screenHeight/3);
                positionCards.add(position1, position2, position3);
                break;
            }
            case 4: {
                Vector2 position1 = new Vector2(delta + delta2 + delta3, GMain.screenHeight*3/4+100);
                Vector2 position2 = new Vector2(delta + delta2 + delta3,GMain.screenHeight/3 - deltaY);
                Vector2 position3 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight/3 - deltaY);
                Vector2 position4 = new Vector2((float)GMain.screenHeight*16/9 - (delta + delta2 + delta3 + boardConfig.widthCard*0.4f), GMain.screenHeight*3/4);
                positionCards.add(position1, position2, position3, position4);
                break;
            }
            case 5: {
                Vector2 position1 = new Vector2(GMain.screenWidth/2, GMain.screenHeight*3/4+100);
                Vector2 position2 = new Vector2(delta + delta2,GMain.screenHeight*2/3-40);
                Vector2 position3 = new Vector2(delta + delta2+200,GMain.screenHeight/3);
                Vector2 position4 = new Vector2((float)GMain.screenHeight*16/9 - delta - delta2-150, GMain.screenHeight/3);
                Vector2 position5 = new Vector2((float)GMain.screenHeight*16/9+50 - delta - delta2, GMain.screenHeight*2/3-40);
                positionCards.add(position1, position2, position3, position4);
                positionCards.add(position5);
                break;
            }

            default: break;
        }
    }
    void loadMonney(){
        labelMonney = new Label(""+FortmartPrice(myMonney),new Label.LabelStyle(fontMyMonney, null));
        labelMonney.setFontScale(0.9f);
        labelMonney.setOrigin(Align.center);
        labelMonney.setPosition(GMain.screenWidth/2+20, GMain.screenHeight-50,Align.center);
        labelMonney.setAlignment(Align.center);
        uiGroup.addActor(labelMonney);
    }
    void initFont(){
        fontMyMonney = GAssetsManager.getBitmapFont("gold.fnt");
    }

    void initAtlas(){
        uiAtlas = GAssetsManager.getTextureAtlas("ui/ui.atlas");
        CardAtlas = GAssetsManager.getTextureAtlas("Card/CardPoker.atlas");
    }
    private String FortmartPrice(Long Price) {

        DecimalFormat mDecimalFormat = new DecimalFormat("###,###,###,###");
        String mPrice = mDecimalFormat.format(Price);

        return mPrice;
    }
}
