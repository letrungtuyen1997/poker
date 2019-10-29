package com.ss.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.effect.SoundEffect;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.object.frameSelectPeo;

import java.text.DecimalFormat;

public class GameStart extends GScreen {
    public TextureAtlas ui;
    BitmapFont font,fontUi;
    public  Group group = new Group();
    public  Group groupavt = new Group();
    public  Group groupOut = new Group();
    public  Group groupIn = new Group();
    public static long MyMonney = GMain.prefs.getLong("mymonney");;
    Label Labelmonney;
    Image btnBD,btnTG,btnDG,btnTP;
    Image btngetChips, btnStart,Text ;

    @Override
    public void dispose() {
        groupIn.clear();
        group.clear();
        groupavt.clear();
        groupOut.clear();
    }

    @Override
    public void init() {
        SoundEffect.Playmusic();
        GStage.addToLayer(GLayer.ui,group);
        GStage.addToLayer(GLayer.ui,groupavt);
        GStage.addToLayer(GLayer.ui,groupOut);
        GStage.addToLayer(GLayer.ui,groupIn);
        initAtlas();
        showbg();
        Gdx.app.log("okok","checkMonney: "+MyMonney);
    }

    @Override
    public void run() {

    }
    void showbg(){
        if(GMain.prefs.getInteger("checkFirst")==0){
            GMain.prefs.putLong("mymonney",1500000);
            GMain.prefs.flush();
            GMain.prefs.putInteger("checkFirst",1);
            GMain.prefs.flush();
            MyMonney = GMain.prefs.getLong("mymonney");
        }
        Image bg = GUI.createImage(ui,"bgStart");
        group.addActor(bg);
        ////////// information /////////
        Image frmAvt = GUI.createImage(ui,"frmAvt");
        frmAvt.setPosition(frmAvt.getWidth()/2,frmAvt.getHeight()/2,Align.center);
        groupavt.addActor(frmAvt);
        Labelmonney = new Label(""+FortmartPrice(MyMonney),new Label.LabelStyle(font,null));
        Labelmonney.setFontScale(0.6f);
        Labelmonney.setOrigin(Align.center);
        Labelmonney.setPosition(270,75,Align.center);
        Labelmonney.setAlignment(Align.center);
        groupavt.addActor(Labelmonney);
        /////////// btn start //////
        btnStart = GUI.createImage(ui,"btnStart");
        btnStart.setOrigin(Align.center);
        btnStart.setPosition(GMain.screenWidth/2,GMain.screenHeight/2, Align.center);
        groupOut.addActor(btnStart);
        Text = GUI.createImage(ui,"text");
        Text.setOrigin(Align.center);
        Text.setPosition(GMain.screenWidth/2,GMain.screenHeight/2+80, Align.center);
        groupOut.addActor(Text);
        //////// btn  get chips/////
        btngetChips = GUI.createImage(ui,"btnGetChip");
        btngetChips.setPosition(GMain.screenWidth/2,GMain.screenHeight/2+250,Align.center);
        groupOut.addActor(btngetChips);
        eventbtnGetChips(btngetChips);
        ///////// event ///////
        aniBtnStart(btnStart);
        aniBtnStart(Text);
        aniText(Text);
        eventbtnStart(btnStart);
        eventbtnStart(Text);
        //////////// ui in groupIn/////
        loadUiGroupIn();





    }
    void aniText(Image btn){
        btn.addAction(Actions.sequence(
                Actions.scaleBy(0.2f,-0.1f,1f),
                Actions.scaleBy(-0.2f,0.1f,1f),
                GSimpleAction.simpleAction((d,a)->{
                    aniText(btn);
                    return true;
                })
        ));
    }
    void aniBtnStart(Image btn){
        btn.addAction(Actions.sequence(
                Actions.moveBy(0,30,1f),
                Actions.moveBy(0,-30,1f),
                GSimpleAction.simpleAction((d,a)->{
                    aniBtnStart(btn);
                    return true;
                })
        ));
    }
    void eventbtnStart(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                Text.setTouchable(Touchable.disabled);
                btnStart.setTouchable(Touchable.disabled);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(group,0.2f,()->{
                    Text.setTouchable(Touchable.enabled);
                    btnStart.setTouchable(Touchable.enabled);
                    groupOut.addAction(Actions.moveBy(-GMain.screenWidth,0,0.2f,Interpolation.swingOut));
                    groupIn.addAction(Actions.moveBy(-GMain.screenWidth,0,0.4f,Interpolation.swingIn));
                    groupavt.addAction(Actions.scaleTo(0.5f,0.5f,0.2f));

                });

                return super.touchDown(event, x, y, pointer, button);

            }
        });
    }
    void loadUiGroupIn(){
        groupIn.setPosition(GMain.screenWidth,0);
        Image TextChonBan = GUI.createImage(ui,"textChonBan");
        TextChonBan.setPosition(GMain.screenWidth/2,GMain.screenHeight/2-200,Align.center);
        groupIn.addActor(TextChonBan);
        ///////// btn nong dan ///
        btnBD = GUI.createImage(ui,"btnBinhdan");
        btnBD.setWidth(btnBD.getWidth()*0.8f);
        btnBD.setHeight(btnBD.getHeight()*0.8f);
        btnBD.setOrigin(Align.center);
        btnBD.setPosition(GMain.screenWidth/2-btnBD.getWidth()/2,GMain.screenHeight/2,Align.center);
        groupIn.addActor(btnBD);
        ///////// btn thuong gia ///
        btnTG = GUI.createImage(ui,"btnThuongGia");
        btnTG.setWidth(btnTG.getWidth()*0.8f);
        btnTG.setHeight(btnTG.getHeight()*0.8f);
        btnTG.setOrigin(Align.center);
        btnTG.setPosition(GMain.screenWidth/2+btnTG.getWidth()/2,GMain.screenHeight/2,Align.center);
        groupIn.addActor(btnTG);
        ///////// btn dai gia ///
        btnDG = GUI.createImage(ui,"btnDaiGia");
        btnDG.setWidth(btnDG.getWidth()*0.8f);
        btnDG.setHeight(btnDG.getHeight()*0.8f);
        btnDG.setOrigin(Align.center);
        btnDG.setPosition(GMain.screenWidth/2-btnDG.getWidth()/2,GMain.screenHeight/2+btnDG.getHeight(),Align.center);
        groupIn.addActor(btnDG);
        ///////// btn ti phu ///
        btnTP = GUI.createImage(ui,"btnTiPhu");
        btnTP.setWidth(btnTP.getWidth()*0.8f);
        btnTP.setHeight(btnTP.getHeight()*0.8f);
        btnTP.setOrigin(Align.center);
        btnTP.setPosition(GMain.screenWidth/2+btnTP.getWidth()/2,GMain.screenHeight/2+btnTP.getHeight(),Align.center);
        groupIn.addActor(btnTP);
        //////// btn quay ve ////
        Image btnReturn = GUI.createImage(ui,"btnReturn");
        btnReturn.setPosition(GMain.screenWidth-btnReturn.getWidth()/2,btnReturn.getHeight()/2,Align.center);
        groupIn.addActor(btnReturn);
        eventbtnReturn(btnReturn);
        ///////// event btn/////
        eventBtn(btnBD,1);
        eventBtn(btnTG,2);
        eventBtn(btnDG,3);
        eventBtn(btnTP,4);
        //////// animation/////
        aniBtn(btnBD,3);
        aniBtn(btnTG,3.6f);
        aniBtn(btnDG,2.4f);
        aniBtn(btnTP,5f);

    }
    void eventbtnReturn(Image btn){
        btn.setOrigin(Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SoundEffect.Play(SoundEffect.click);
                btn.setTouchable(Touchable.disabled);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(group,0.2f,()->{
                    btn.setTouchable(Touchable.enabled);
                    groupIn.addAction(Actions.moveBy(GMain.screenWidth,0,0.2f,Interpolation.swingOut));
                    groupOut.addAction(Actions.moveBy(GMain.screenWidth,0,0.4f,Interpolation.swingIn));
                    groupavt.addAction(Actions.scaleTo(1f,1f,0.2f));

                });

            }
        });

    }
    void eventBtn(Image btn,int type){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btnBD.setTouchable(Touchable.disabled);
                btnTG.setTouchable(Touchable.disabled);
                btnDG.setTouchable(Touchable.disabled);
                btnTP.setTouchable(Touchable.disabled);
                SoundEffect.Play(SoundEffect.click);
                btn.setTouchable(Touchable.disabled);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(group,0.2f,()->{
                    new frameSelectPeo(ui,btnBD,btnTG,btnDG,btnTP,fontUi,type,GameStart.this);
                });

                return super.touchDown(event, x, y, pointer, button);
            }


        });
    }
    void  eventbtnGetChips(Image btn){
        btn.setOrigin(Align.center);
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                SoundEffect.Play(SoundEffect.click);
                btn.setTouchable(Touchable.disabled);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(group,0.2f,()->{
                    showAds();
                });
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    void showAds(){
        if(GMain.platform.isVideoRewardReady()) {
            GMain.platform.ShowVideoReward((boolean success) -> {
                if (success) {
                    MyMonney+=500000;
                    GMain.prefs.putLong("mymonney",MyMonney);
                    GMain.prefs.flush();
                    Labelmonney.setText(FortmartPrice(MyMonney));
                    btngetChips.setTouchable(Touchable.enabled);

                }else {
                    btngetChips.setTouchable(Touchable.enabled);

                }
            });
        }else {
            Label notice = new Label("Kiểm tra kết nối",new Label.LabelStyle(fontUi,null));
            notice.setPosition(GMain.screenWidth/2,GMain.screenHeight/2,Align.center);
            groupOut.addActor(notice);
            notice.addAction(Actions.sequence(
                    Actions.moveBy(0,-50,0.5f),
                    GSimpleAction.simpleAction((d, a)->{
                        notice.clear();
                        notice.remove();
                        btngetChips.setTouchable(Touchable.enabled);
                        return true;
                    })
            ));

        }
    }
    void aniBtn(Image btn,float duration){
        btn.addAction(Actions.sequence(
                Actions.moveBy(0,30,duration),
                Actions.moveBy(0,-30,duration),
                GSimpleAction.simpleAction((d,a)->{
                    aniBtn(btn,duration);
                    return true;
                })
        ));
    }


    private String FortmartPrice(Long Price) {

        DecimalFormat mDecimalFormat = new DecimalFormat("###,###,###,###");
        String mPrice = mDecimalFormat.format(Price);

        return mPrice;
    }
    public void initAtlas(){
        ui = GAssetsManager.getTextureAtlas("ui/uiStart.atlas");
        font = GAssetsManager.getBitmapFont("gold.fnt");
        fontUi = GAssetsManager.getBitmapFont("font_white.fnt");
    }

}
