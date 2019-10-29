package com.ss.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.Array;
import com.effect.SoundEffect;
import com.effect.effectWin;
import com.ss.GMain;
import com.ss.commons.Tweens;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.action.exAction.GTemporalAction;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.scene.GamePlay;

import java.text.DecimalFormat;
import java.util.HashMap;

public class BoardGame {
    TextureAtlas cardAtlas ,uiAtlas;
    GamePlay gamePlay;
    BitmapFont font, fontmonney, fontName,font_white;
    Group uiGroup = new Group();
    Group fontGroup = new Group();
    Group groupBtn = new Group();
    Array<Card> allCard = new Array<>();
    Array<Integer> cards;
    Image btnSkip,btnAddMore,btnEspouse;
    Group group = new Group();
    Group groupChips = new Group();
    Group groupAvt = new Group();
    Group groupBot1 = new Group();
    Group groupBot2 = new Group();
    Group groupBot3 = new Group();
    Group groupBot4 = new Group();
    Group groupResult = new Group();
    Group groupparticle = new Group();

    Array<Array<Card>> CardPlay = new Array<>();
    Array<Long> monneyBotArr = new Array<>();
    Array<Long> monneyArrBegin = new Array<>();
    Array<Long> monneyArrCuoc = new Array<>();
    Array<Card> cardInTableArr = new Array<>();
    Array<Label> monneyBotLabelArr = new Array<>();
    Array<Image> SkipImgBotArr = new Array<>();
    Array<Image> AvtBotArr = new Array<>();
    Array<Image> frameNameBotArr = new Array<>();
    Array<Label> LabelNameBotArr = new Array<>();
    Array<Array<Integer>> allArrCheck = new Array<>();
    Array<Integer> arrayWinner = new Array<>();
    boolean arraySkip = false;
    int indexCard =3, Winner;
    Boolean checkSkip = false;
    Boolean setcheckSkip = false;
    public long monneyCuoc =boardConfig.monneyStart;
    long monneyCuocBegin= boardConfig.monneyStart;
    GreedilyAlgorithm SumChips;
    long monneyIngame =0;
    Label LableSumMonney;
    Boolean CheckTo = false;
    Boolean Checktheo = false;
    Boolean checkAllSkip = false;
    int[] percent = new int[4];
    Label LableMonneyCuoc ,labelMucCuoc,labelSonguoi;
    Image lightTurn;
    float duratitionKickBot=0;
    int countAds=0;


    public BoardGame(TextureAtlas cardAtlas, TextureAtlas uiAtlas, GamePlay gamePlay){
        GStage.addToLayer(GLayer.top,group);
        GStage.addToLayer(GLayer.top,groupChips);
        GStage.addToLayer(GLayer.top,groupAvt);
        GStage.addToLayer(GLayer.top,groupResult);
        GStage.addToLayer(GLayer.top,groupparticle);
        GStage.addToLayer(GLayer.top,uiGroup);
        GStage.addToLayer(GLayer.top,fontGroup);
        GStage.addToLayer(GLayer.top,groupBtn);
        GStage.addToLayer(GLayer.top,groupBot1);
        GStage.addToLayer(GLayer.top,groupBot2);
        GStage.addToLayer(GLayer.top,groupBot3);
        GStage.addToLayer(GLayer.top,groupBot4);
        this.uiAtlas = uiAtlas;
        this.cardAtlas = cardAtlas;
        this.gamePlay =gamePlay;
        SumChips = new GreedilyAlgorithm(uiAtlas,group);
        initFont();
        innitPlayer();
        renderCard();
        distributeCardsOutSide();
        showAllbutton();
        loadSkipImg();
        loadframeMonney();
        setPercentCuoc();
        loadFrameNotice();
        //////// load avt/////
        loadAvtPlayer();
        loadAvtBot();
        //////// load frame setting/////
        loadframeSetting();

        if(boardConfig.modePlay==3){
            LoadMonneyBotMode3();
        }else {
            LoadMonneyBotMode5();
        }
        loadmonneyBegin();
        /////////// setArrAllCard /////////
        Tweens.setTimeout(group,boardConfig.modePlay*1.5f,()->{
            for(int i=0;i<boardConfig.modePlay;i++){
                setArrCheck(CardPlay.get(i));
            }
            checkWinGame();
        });
        //////// load lightTurn//////



    }
    void innitPlayer(){
        for(int i=0;i<boardConfig.modePlay;i++){
            Array<Card> card = new Array<>();
            CardPlay.add(card);
            arrayWinner.add(i);
            monneyArrCuoc.add((long)0);

        }
    }
    void renderCard(){
        cards = CheckCard.makeCards();
        for(int i=20;i<53;i++){
                Card card = new Card(cardAtlas,group,i);
                card.setVisibleAll();
                allCard.add(card);
        }
        for (int i=0 ; i < allCard.size; i++){
            allCard.get(i).setKey(cards.get(i+19));
        }
    }
    void distributeCardsOutSide(){
        allCard.shuffle();
        distributeCardInside(0);

    }

    void distributeCardInside(int index){
        if(index >= gamePlay.positionCards.size*2){

            Tweens.setTimeout(group,0.3f,()->{
                cardInTable();
            });
            Tweens.setTimeout(group,1f,()->{

                flipCardIntableBegin();
                onTurn(0,gamePlay.positionCards.get(0).x,gamePlay.positionCards.get(0).y,40f);
                groupBtn.addAction(Actions.moveTo(0,0,0.3f, Interpolation.swingOut));
                setMonneyCuocBegin();
                setTouch();
                if(gamePlay.myMonney<=monneyCuoc){
                    btnAddMore.setColor(Color.DARK_GRAY);
                    btnAddMore.setTouchable(Touchable.disabled);
                }else {
                    btnAddMore.setColor(Color.WHITE);
                    btnAddMore.setTouchable(Touchable.enabled);
                }
            });
            return;
        }
        float detalX;
        if(index!=0&&index!=(gamePlay.positionCards.size)){
             detalX = index >= gamePlay.positionCards.size ? allCard.get(0).card.getWidth()*boardConfig.scaleBot: 0;

        }else {
             detalX = index >= gamePlay.positionCards.size ? allCard.get(0).card.getWidth(): 0;
        }
        final int indexTemp = index+1;
        Tweens.setTimeout(group,0.3f,()->{
            SoundEffect.Play(SoundEffect.chiabai);
            allCard.get(index).moveCard("null",gamePlay.positionCards.get(index%boardConfig.modePlay).x + detalX - allCard.get(index).card.getWidth()/2,gamePlay.positionCards.get(index%boardConfig.modePlay).y - allCard.get(index).card.getHeight()/2);
            CardPlay.get(index%boardConfig.modePlay).add(allCard.get(index));
            if(index!=0&&index!=(boardConfig.modePlay)){
                allCard.get(index).scaleCard();
            }else {
                 allCard.get(index).setVisibleTiledown();
            }
        });


        Tweens.setTimeout(group, 0.2f, ()->{
            distributeCardInside(indexTemp);
        });
    }
    void cardInTable(){
        for (int i=15; i<20;i++){
            int finalI = i;
            Tweens.setTimeout(group,0.2f*(i-15),()->{
                SoundEffect.Play(SoundEffect.latbai);
                allCard.get(finalI).moveCard("intable",285+ allCard.get(finalI).card.getWidth()*(finalI -14), GMain.screenHeight/2+10);
                allCard.get(finalI).flipCard(-1);
                cardInTableArr.add(allCard.get(finalI));
            });
        }
    }
    void moreBets(){
        Image btnAddcoins = GUI.createImage(uiAtlas,"btnNewGame");
        btnAddcoins.setOrigin(Align.center);
        btnAddcoins.setPosition(GMain.screenWidth/2, GMain.screenHeight/2,Align.center);
        uiGroup.addActor(btnAddcoins);
        btnAddcoins.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                btnAddcoins.setTouchable(Touchable.disabled);
                btnAddcoins.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1,1,0.1f)
                        ));
                Tweens.setTimeout(group,0.2f,()->{
                    btnAddcoins.setTouchable(Touchable.enabled);
                    if(gamePlay.myMonney==0||gamePlay.myMonney<boardConfig.monneyStart){
                        new WatchVideo(uiAtlas,()->{
                        });
                    }else {
                        btnAddcoins.remove();
                        reset();
                    }
                });
            }
        });
    }
    void CheckPokerBig(int Player, Array<Card> object, float x, float y){
        Array<Integer> hand = new Array<>();
        hand.add(object.get(0).Key);
        hand.add(object.get(1).Key);
        Array<Integer> deck = new Array<>();
        for (int i=0;i<5;i++){
            deck.add(cardInTableArr.get(i).Key);
        }
        Array<Integer> check = CheckCard.move(deck,hand);
        int type= CheckCard.check5(CheckCard.move(deck,hand))>>13;
        //////// particle///////////
        if(type>=5&& SkipImgBotArr.get(Player).isVisible()==false){
            effectWin win2 = new effectWin(10,x,y);
            groupparticle.addActor(win2);
            win2.start();
        }
        if(Player!=0){x=x-30;}
        else {y=y-30;}
        effectWin win = new effectWin(type,x,y);
        groupparticle.addActor(win);
        if(Player==0){
            setVisibleOverLay(check);
        }
        allArrCheck.add(check);
    }
    void flipCardIntableBegin(){

        for(int i =0; i<3;i++){
            int finalI = i;
            Tweens.setTimeout(group,0.2f*i,()->{
//                SoundEffect.Play(SoundEffect.latbai);
                cardInTableArr.get(finalI).flipCard(1);
                cardInTableArr.get(finalI).setVisibleTiledown();
            });
        }
    }
    void flipCardIntable(int index){
        SoundEffect.Play(SoundEffect.latbai);
        cardInTableArr.get(index).flipCard(1);
        cardInTableArr.get(index).setVisibleTiledown();
    }

    void setVisibleOverLay(Array<Integer> object){
        for(int i=0;i<object.size;i++){
            for (int j=0;j<allCard.size;j++){
                if(object.get(i)==allCard.get(j).Key){
                    allCard.get(j).overlay.setVisible(true);
                }
            }
        }
    }
    void showAllbutton(){
        groupBtn.setPosition(0,GMain.screenHeight/2);
        ////////// btn skip///////
        btnSkip = GUI.createImage(uiAtlas,"btnSkip");
        btnSkip.setOrigin(Align.center);
        btnSkip.setPosition(btnSkip.getWidth()+10,GMain.screenHeight-btnSkip.getHeight()/2-20,Align.center);
        groupBtn.addActor(btnSkip);
        //////// btn  Espouse /////////
        btnEspouse = GUI.createImage(uiAtlas,"btnEspouse");
        btnEspouse.setOrigin(Align.center);
        btnEspouse.setPosition(GMain.screenWidth-btnEspouse.getWidth()*2+50,GMain.screenHeight-btnEspouse.getHeight()/2-20,Align.center);
        groupBtn.addActor(btnEspouse);
        /////////// btn add more //////////
        btnAddMore = GUI.createImage(uiAtlas,"btnAddMore");
        btnAddMore.setOrigin(Align.center);
        btnAddMore.setPosition(GMain.screenWidth-btnAddMore.getWidth()/2-30,GMain.screenHeight-btnAddMore.getHeight()/2-20,Align.center);
        groupBtn.addActor(btnAddMore);
        eventBtnAddMore(btnAddMore);
        eventBtnEspouse(btnEspouse);
        eventBtnSkip(btnSkip);
    }
    ////////////////////////// to them//////////////////////
    void eventBtnAddMore(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btn.setTouchable(Touchable.disabled);
                btnEspouse.setTouchable(Touchable.disabled);
                btnSkip.setTouchable(Touchable.disabled);
                SoundEffect.Play(SoundEffect.click);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(groupBtn,0.2f,()->{
                    if(gamePlay.myMonney>monneyCuoc){
                        new MoreBets(uiAtlas,()->{
                            groupBtn.addAction(Actions.moveTo(0,GMain.screenHeight/2,0.3f));
                            if(MoreBets.monneyCuocMore==gamePlay.myMonney){
                                SoundEffect.Play(SoundEffect.chipFly5);
                                aniXalang(gamePlay.positionCards.get(0).x,gamePlay.positionCards.get(0).y,0);
                            }else {
                                if(monneyCuoc/boardConfig.monneyStart >=1 && monneyCuoc/boardConfig.monneyStart<3){
                                    SoundEffect.Play(SoundEffect.chipFly2);
                                }else if(monneyCuoc/boardConfig.monneyStart >=3 && monneyCuoc/boardConfig.monneyStart<5){
                                    SoundEffect.Play(SoundEffect.chipFly3);
                                }else if(monneyCuoc/boardConfig.monneyStart >=5){
                                    SoundEffect.Play(SoundEffect.chipFly4);
                                }
                                aniToThem(gamePlay.positionCards.get(0).x,gamePlay.positionCards.get(0).y,0);

                            }
                            monneyCuoc+=MoreBets.monneyCuocMore;
                            LableMonneyCuoc.setText("$ "+FortmartPrice(monneyCuoc));
                            counterMonney(gamePlay.labelMonney,gamePlay.myMonney,MoreBets.monneyCuocMore);
                            gamePlay.myMonney -=MoreBets.monneyCuocMore;
                            counterMonneyUp(LableSumMonney,monneyIngame,monneyCuoc);
                            monneyIngame+=MoreBets.monneyCuocMore;
                            monneyArrCuoc.set(0,monneyArrCuoc.get(0)+MoreBets.monneyCuocMore);
                            GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
                            greedilyAlgorithm.renderChip(MoreBets.monneyCuocMore,gamePlay.positionCards.get(0).x-130,gamePlay.positionCards.get(0).y-100,false);
                            greedilyAlgorithm.moveChips(0,GMain.screenWidth/2-70,180,0.3f,Interpolation.fastSlow,()->{
                                Tweens.setTimeout(group,0.3f,()->{
                                    greedilyAlgorithm.disposeChip();
                                    SumChips.renderChip(monneyIngame,GMain.screenWidth/2-70,180,true);
                                });
                            });
                            lightTurn.remove();
                            lifeInGame(0);

                        });
                    }
                });
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    /////////////////////// theo /////////////////////////
    void eventBtnEspouse(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btn.setTouchable(Touchable.disabled);
                btnAddMore.setTouchable(Touchable.disabled);
                btnSkip.setTouchable(Touchable.disabled);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(groupBtn,0.2f,()->{
                    groupBtn.addAction(Actions.moveTo(0,GMain.screenHeight/2,0.3f));
                    if(monneyCuocBegin<monneyCuoc&& gamePlay.myMonney>monneyCuoc){
                        if(monneyCuoc/boardConfig.monneyStart >=1 && monneyCuoc/boardConfig.monneyStart<3){
                            SoundEffect.Play(SoundEffect.chipFly2);
                        }else if(monneyCuoc/boardConfig.monneyStart >=3 && monneyCuoc/boardConfig.monneyStart<5){
                            SoundEffect.Play(SoundEffect.chipFly3);
                        }else if(monneyCuoc/boardConfig.monneyStart >=5){
                            SoundEffect.Play(SoundEffect.chipFly4);
                        }
                        aniTheoTo(gamePlay.positionCards.get(0).x+20,gamePlay.positionCards.get(0).y,0);
                        counterMonney(gamePlay.labelMonney,gamePlay.myMonney,(monneyCuoc-monneyCuocBegin+boardConfig.monneyStart));
                        gamePlay.myMonney-=(monneyCuoc-monneyCuocBegin+boardConfig.monneyStart);
                        counterMonneyUp(LableSumMonney,monneyIngame,monneyCuoc);
                        monneyIngame+=(monneyCuoc-monneyCuocBegin+boardConfig.monneyStart);
                        monneyArrCuoc.set(0,monneyArrCuoc.get(0)+(monneyCuoc-monneyCuocBegin+boardConfig.monneyStart));////// loi o day
                        monneyCuocBegin=monneyCuoc;
                        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
                        greedilyAlgorithm.renderChip((monneyCuoc-monneyCuocBegin+boardConfig.monneyStart),gamePlay.positionCards.get(0).x-130,gamePlay.positionCards.get(0).y-100,false);
                        greedilyAlgorithm.moveChips(0,GMain.screenWidth/2-70,180,0.3f,Interpolation.fastSlow,()->{
                            Tweens.setTimeout(group,0.3f,()->{
                                greedilyAlgorithm.disposeChip();
                                SumChips.renderChip(monneyIngame,GMain.screenWidth/2-70,180,true);
                            });
                        });
                    }else if(monneyCuocBegin<monneyCuoc&&gamePlay.myMonney<monneyCuoc){
                        SoundEffect.Play(SoundEffect.chipFly5);
                        aniXalang(gamePlay.positionCards.get(0).x+20,gamePlay.positionCards.get(0).y,1);
                        counterMonney(gamePlay.labelMonney,gamePlay.myMonney,gamePlay.myMonney);
                        counterMonneyUp(LableSumMonney,monneyIngame,gamePlay.myMonney);
                        monneyIngame+=gamePlay.myMonney;
                        monneyArrCuoc.set(0,monneyArrCuoc.get(0)+gamePlay.myMonney);
                        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
                        greedilyAlgorithm.renderChip(gamePlay.myMonney,gamePlay.positionCards.get(0).x-130,gamePlay.positionCards.get(0).y-100,false);
                        greedilyAlgorithm.moveChips(0,GMain.screenWidth/2-70,180,0.3f,Interpolation.fastSlow,()->{
                            Tweens.setTimeout(group,0.3f,()->{
                                greedilyAlgorithm.disposeChip();
                                SumChips.renderChip(monneyIngame,GMain.screenWidth/2-70,180,true);
                            });
                        });
                        gamePlay.myMonney=0;
                    }else {
                        SoundEffect.Play(SoundEffect.theo);
                        aniTheoTo(gamePlay.positionCards.get(0).x+20,gamePlay.positionCards.get(0).y,0);
                    }
                    lightTurn.remove();
                    lifeInGame(0);

                });
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }
    //////////////////////// skip ////////////////
    void eventBtnSkip(Image btn){
        btn.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                btn.setTouchable(Touchable.disabled);
                btnAddMore.setTouchable(Touchable.disabled);
                btnEspouse.setTouchable(Touchable.disabled);
                checkSkip = true;
                arraySkip= true;
                aniSkip(SkipImgBotArr.get(0),gamePlay.positionCards.get(0).x+30,gamePlay.positionCards.get(0).y);
                btn.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(group,0.2f,()->{
                    groupBtn.addAction(Actions.moveTo(0,GMain.screenHeight/2));
                    for(int i =0 ; i<CardPlay.get(0).size;i++){
                        CardPlay.get(0).get(i).setColorCard();
                    }
                    lightTurn.remove();
                    lifeInGame(0);
                });
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    void lifeInGame(int index){
        if(index>=monneyBotLabelArr.size){
            Tweens.setTimeout(group,1f,()->{
                if(indexCard<=4){
                    flipCardIntable(indexCard);
                    Tweens.setTimeout(group,0.3f,()->{
                        int countSkip=0;
                        for(int i =0; i<SkipImgBotArr.size;i++){
                            if(SkipImgBotArr.get(i).isVisible()==true){
                                countSkip++;
                            }
                        }
                        if(countSkip==boardConfig.modePlay-1){
                            checkAllSkip=true;

                            if(indexCard==3){
                                flipCardIntable(indexCard);
                                indexCard++;
                                flipCardIntable(indexCard);
                                for(int i=0;i<boardConfig.modePlay;i++){
                                    CheckPokerBig(i,CardPlay.get(i),gamePlay.positionCards.get(i).x,gamePlay.positionCards.get(i).y);
                                }
                                Tweens.setTimeout(group,1f,()->{
                                    setWinner();
                                });

                            }else if(indexCard==4){
                                flipCardIntable(indexCard);
                                for(int i=0;i<boardConfig.modePlay;i++){
                                    CheckPokerBig(i,CardPlay.get(i),gamePlay.positionCards.get(i).x,gamePlay.positionCards.get(i).y);
                                }
                                Tweens.setTimeout(group,1f,()->{
                                    setWinner();
                                });
                            }
                        }
                        if(arraySkip==false&& gamePlay.myMonney>0){
                            setTouch();
                            if(gamePlay.myMonney<monneyCuoc){
                                btnAddMore.setColor(Color.DARK_GRAY);
                                btnAddMore.setTouchable(Touchable.disabled);
                            }else {
                                btnAddMore.setColor(Color.WHITE);
                                btnAddMore.setTouchable(Touchable.enabled);
                            }
                            onTurn(0,gamePlay.positionCards.get(0).x,gamePlay.positionCards.get(0).y,40f);
                            groupBtn.addAction(Actions.moveTo(0,0,0.5f));
                        }else  {
                            monneyCuocBegin=monneyCuoc;
                            lightTurn.remove();
                            lifeInGame(0);
                        }
                        indexCard++;
                    });
                }else {
                    for(int i=0;i<boardConfig.modePlay;i++){
                        CheckPokerBig(i,CardPlay.get(i),gamePlay.positionCards.get(i).x,gamePlay.positionCards.get(i).y);
                    }
                    Tweens.setTimeout(group,1f,()->{
                        if(checkAllSkip==false) {
                            setWinner();
                        }
                    });
                }
            });
            return;
        }
        int finalIndex = index;
        int indexTemp = index + 1;
        Gdx.app.log("okok","checkMonney bot: "+monneyBotArr.get(finalIndex));
        if(monneyBotArr.get(finalIndex)>0&&SkipImgBotArr.get(finalIndex+1).isVisible()==false){
            setCheckSkip(CardPlay.get(indexTemp));
        }
        if(setcheckSkip==true&& SkipImgBotArr.get(finalIndex).isVisible()==false){
            Tweens.setTimeout(group,1f,()->{
                aniSkip(SkipImgBotArr.get(finalIndex+1),gamePlay.positionCards.get(finalIndex+1).x,gamePlay.positionCards.get(finalIndex+1).y);
                for(int i =0 ; i<CardPlay.get(finalIndex+1).size;i++){
                    CardPlay.get(finalIndex+1).get(i).setColorCard();
                }
            });

        }else {
            if(SkipImgBotArr.get(finalIndex+1).isVisible()==true||monneyBotArr.get(finalIndex)==0){
                int count=0;
                for(int i=0;i<monneyBotArr.size;i++){
                    if(monneyBotArr.get(i)==0){
                        count++;
                    }
                }
                if(count==3){
                    monneyCuocBegin=monneyCuoc;
                }
                setcheckSkip=false;
                lightTurn.remove();
                lifeInGame(indexTemp);
                return;
            }
            if(checkAllSkip==false){
                onTurn(indexTemp,gamePlay.positionCards.get(indexTemp).x,gamePlay.positionCards.get(indexTemp).y,40f);
                Tweens.setTimeout(group,1f,()->{
                    int choice =(int)(Math.random()*2+1);
                    if(monneyBotArr.get(finalIndex)>monneyCuoc&& monneyCuoc>monneyCuocBegin){
                        if(monneyCuoc/boardConfig.monneyStart >=1 && monneyCuoc/boardConfig.monneyStart<3){
                            SoundEffect.Play(SoundEffect.chipFly2);
                        }else if(monneyCuoc/boardConfig.monneyStart >=3 && monneyCuoc/boardConfig.monneyStart<5){
                            SoundEffect.Play(SoundEffect.chipFly3);
                        }else if(monneyCuoc/boardConfig.monneyStart >=5){
                            SoundEffect.Play(SoundEffect.chipFly4);
                        }
                        aniTheoTo(gamePlay.positionCards.get(finalIndex+1).x,gamePlay.positionCards.get(finalIndex+1).y,1);
                        counterMonney(monneyBotLabelArr.get(finalIndex),monneyBotArr.get(finalIndex),monneyCuoc);
                        monneyBotArr.set(finalIndex,monneyBotArr.get(finalIndex)-monneyCuoc);
                        counterMonneyUp(LableSumMonney,monneyIngame,monneyCuoc);
                        monneyIngame+=monneyCuoc;
                        monneyArrCuoc.set(finalIndex+1,monneyArrCuoc.get(finalIndex+1)+monneyCuoc);
                        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
                        greedilyAlgorithm.renderChip(monneyCuoc,gamePlay.positionCards.get(finalIndex+1).x-130,gamePlay.positionCards.get(finalIndex+1).y-100,false);
                        greedilyAlgorithm.moveChips(finalIndex+1,GMain.screenWidth/2-70,180,0.3f,Interpolation.fastSlow,()->{
                            Tweens.setTimeout(group,0.3f,()->{
                                greedilyAlgorithm.disposeChip();
                                SumChips.renderChip(monneyIngame,GMain.screenWidth/2-70,180,true);
                            });
                        });
                        if(finalIndex==monneyBotArr.size){monneyCuocBegin=monneyCuoc;}
                    } else if(monneyBotArr.get(finalIndex)>monneyCuoc&& monneyCuoc==monneyCuocBegin && choice==2){
                        int a= (int)(Math.random()*3+1);
                        long monneyTothem = (((monneyBotArr.get(finalIndex)-monneyCuoc)*percent[a])/100);
                        if(a==3){
                            SoundEffect.Play(SoundEffect.chipFly5);
                            aniXalang(gamePlay.positionCards.get(finalIndex+1).x,gamePlay.positionCards.get(finalIndex+1).y,1);
                            monneyTothem+=boardConfig.monneyStart;
                        }else {
                            if(monneyCuoc/boardConfig.monneyStart >=1 && monneyCuoc/boardConfig.monneyStart<3){
                                SoundEffect.Play(SoundEffect.chipFly2);
                            }else if(monneyCuoc/boardConfig.monneyStart >=3 && monneyCuoc/boardConfig.monneyStart<5){
                                SoundEffect.Play(SoundEffect.chipFly3);
                            }else if(monneyCuoc/boardConfig.monneyStart >=5){
                                SoundEffect.Play(SoundEffect.chipFly4);
                            }
                            aniToThem(gamePlay.positionCards.get(finalIndex+1).x,gamePlay.positionCards.get(finalIndex+1).y,1);
                        }
                        monneyCuoc+=monneyTothem;
                        LableMonneyCuoc.setText("$ "+FortmartPrice(monneyCuoc));
                        counterMonney(monneyBotLabelArr.get(finalIndex),monneyBotArr.get(finalIndex),monneyTothem);
                        monneyBotArr.set(finalIndex,monneyBotArr.get(finalIndex)-monneyTothem);
                        counterMonneyUp(LableSumMonney,monneyIngame,monneyTothem);
                        monneyIngame+=monneyTothem;
                        monneyArrCuoc.set(finalIndex+1,monneyArrCuoc.get(finalIndex+1)+monneyTothem);
                        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
                        greedilyAlgorithm.renderChip(monneyTothem,gamePlay.positionCards.get(finalIndex+1).x-130,gamePlay.positionCards.get(finalIndex+1).y-100,false);
                        greedilyAlgorithm.moveChips(finalIndex+1,GMain.screenWidth/2-70,180,0.3f,Interpolation.fastSlow,()->{
                            Tweens.setTimeout(group,0.3f,()->{
                                greedilyAlgorithm.disposeChip();
                                SumChips.renderChip(monneyIngame,GMain.screenWidth/2-70,180,true);
                            });
                        });
                    } else if(monneyBotArr.get(finalIndex)<=monneyCuoc&& monneyCuoc>monneyCuocBegin && monneyBotArr.get(finalIndex)>0) {
                        SoundEffect.Play(SoundEffect.chipFly5);
                        monneyArrCuoc.set(finalIndex+1,monneyArrCuoc.get(finalIndex+1)+monneyBotArr.get(finalIndex));
                        aniXalang(gamePlay.positionCards.get(finalIndex+1).x,gamePlay.positionCards.get(finalIndex+1).y,1);
                        counterMonney(monneyBotLabelArr.get(finalIndex),monneyBotArr.get(finalIndex),monneyBotArr.get(finalIndex));
                        counterMonneyUp(LableSumMonney,monneyIngame,monneyBotArr.get(finalIndex));
                        monneyIngame+=monneyBotArr.get(finalIndex);
                        monneyBotArr.set(finalIndex,monneyBotArr.get(finalIndex)-monneyBotArr.get(finalIndex));
                        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
                        greedilyAlgorithm.renderChip(monneyCuoc,gamePlay.positionCards.get(finalIndex+1).x-130,gamePlay.positionCards.get(finalIndex+1).y-100,false);
                        greedilyAlgorithm.moveChips(finalIndex+1,GMain.screenWidth/2-70,180,0.3f,Interpolation.fastSlow,()->{
                            Tweens.setTimeout(group,0.3f,()->{
                                greedilyAlgorithm.disposeChip();
                                SumChips.renderChip(monneyIngame,GMain.screenWidth/2-70,180,true);
                            });
                        });
                    } else {
                        SoundEffect.Play(SoundEffect.theo);
                        aniTheoTo(gamePlay.positionCards.get(finalIndex+1).x,gamePlay.positionCards.get(finalIndex+1).y,1);
                    }


                });
            }

        }
        int finalIndexTemp = indexTemp;
        Tweens.setTimeout(group,2f,()->{
        setcheckSkip=false;
        if(lightTurn!=null)
            lightTurn.remove();
        lifeInGame(finalIndexTemp);
    });
    }
    void setCheckSkip(Array<Card> object){
        Array<Integer> hand = new Array<>();
        hand.add(object.get(0).Key);
        hand.add(object.get(1).Key);
        Array<Integer> deck = new Array<>();
        for (int i=0;i<5;i++){
            deck.add(cardInTableArr.get(i).Key);
        }
        Array<Integer> check = CheckCard.move(deck,hand);
//        for (int i=0;i<check.size;i++){
//            System.out.print(CheckCard.nameMap.get(check.get(i))+ " ");
//        }
        int type = CheckCard.check5(check)>>13;

        int percent = 100*(type/10);

        if(percent<60){
            int options = (int)(Math.random()*5+1);
            if(options==1){
                setcheckSkip = true;
            }
        }
    }
    void LoadMonneyBotMode3(){
        for(int i=0;i<2;i++){
            long monney = boardConfig.monneyStart*(long)(Math.random()*10+5);
            monneyBotArr.add(monney);
        }
        /////// bot1///
        Label monneyBot1 = new Label(""+FortmartPrice(monneyBotArr.get(0)),new Label.LabelStyle(fontmonney,null));
        monneyBot1.setFontScale(0.5f);
        monneyBot1.setOrigin(Align.center);
        monneyBot1.setAlignment(Align.center);
        monneyBot1.setPosition(gamePlay.positionCards.get(1).x-30,gamePlay.positionCards.get(1).y-150,Align.center);
        groupBot1.addActor(monneyBot1);
        monneyBotLabelArr.add(monneyBot1);
        /////// bot2 ///////
        Label monneyBot2 = new Label(""+FortmartPrice(monneyBotArr.get(1)),new Label.LabelStyle(fontmonney,null));
        monneyBot2.setFontScale(0.5f);
        monneyBot2.setOrigin(Align.center);
        monneyBot2.setAlignment(Align.center);
        monneyBot2.setPosition(gamePlay.positionCards.get(2).x-30,gamePlay.positionCards.get(2).y-150,Align.center);
        groupBot2.addActor(monneyBot2);
        monneyBotLabelArr.add(monneyBot2);
    }
    void LoadMonneyBotMode5(){
        for(int i=0;i<4;i++){
            long monney = boardConfig.monneyStart*(long)(Math.random()*10+5);
            monneyBotArr.add(monney);
        }

        /////// bot1///
        Label monneyBot1 = new Label(""+FortmartPrice(monneyBotArr.get(0)),new Label.LabelStyle(fontmonney,null));
        monneyBot1.setFontScale(0.5f);
        monneyBot1.setOrigin(Align.center);
        monneyBot1.setAlignment(Align.center);
        monneyBot1.setPosition(gamePlay.positionCards.get(1).x-30,gamePlay.positionCards.get(1).y-150,Align.center);
        groupBot1.addActor(monneyBot1);
        monneyBotLabelArr.add(monneyBot1);
        /////// bot2 ///////
        Label monneyBot2 = new Label(""+FortmartPrice(monneyBotArr.get(1)),new Label.LabelStyle(fontmonney,null));
        monneyBot2.setFontScale(0.5f);
        monneyBot2.setOrigin(Align.center);
        monneyBot2.setAlignment(Align.center);
        monneyBot2.setPosition(gamePlay.positionCards.get(2).x-30,gamePlay.positionCards.get(2).y-150,Align.center);
        groupBot2.addActor(monneyBot2);
        monneyBotLabelArr.add(monneyBot2);

        ///////// bot3 ///////
        Label monneyBot3 = new Label(""+FortmartPrice(monneyBotArr.get(2)),new Label.LabelStyle(fontmonney,null));
        monneyBot3.setFontScale(0.5f);
        monneyBot3.setOrigin(Align.center);
        monneyBot3.setAlignment(Align.center);
        monneyBot3.setPosition(gamePlay.positionCards.get(3).x-30,gamePlay.positionCards.get(3).y-150,Align.center);
        groupBot3.addActor(monneyBot3);
        monneyBotLabelArr.add(monneyBot3);

        ////////// bot4//////
        Label monneyBot4 = new Label(""+FortmartPrice(monneyBotArr.get(3)),new Label.LabelStyle(fontmonney,null));
        monneyBot4.setFontScale(0.5f);
        monneyBot4.setOrigin(Align.center);
        monneyBot4.setAlignment(Align.center);
        monneyBot4.setPosition(gamePlay.positionCards.get(4).x-30,gamePlay.positionCards.get(4).y-150,Align.center);
        groupBot4.addActor(monneyBot4);
        monneyBotLabelArr.add(monneyBot4);
    }
    void loadSkipImg(){
        for(int i=0;i<boardConfig.modePlay;i++){
            Image skipImg = GUI.createImage(uiAtlas,"Skip");
            group.addActor(skipImg);
            skipImg.setVisible(false);
            SkipImgBotArr.add(skipImg);
        }
    }
    void aniSkip(Image object,float x, float y){
        SoundEffect.Play(SoundEffect.skip);
        object.setVisible(true);
        object.setScale(0);
        object.setOrigin(Align.center);
        object.setPosition(x-30,y-80,Align.center);
        object.addAction(Actions.scaleTo(1,1,0.3f,Interpolation.swingOut));
    }

    void counterMonney(Label object, long monney,long targetMonney){

        group.addAction(
                GTemporalAction.add(1, (percent, actor) -> {
                  object.setText(""+FortmartPrice((long)(monney - targetMonney*percent)));
                }));

    }
    void counterMonneyUp(Label object, long monney,long targetMonney){

        group.addAction(
                GTemporalAction.add(1, (percent, actor) -> {
                    object.setText(""+FortmartPrice((long)(monney + targetMonney*percent)));
                }));

    }
    void reset(){
        indexCard=3;
        cardInTableArr.clear();
        CardPlay.clear();
        allCard.clear();
        SkipImgBotArr.clear();
        arrayWinner.clear();
        allArrCheck.clear();
        fontGroup.clear();
        group.clear();
        groupResult.clear();
        groupChips.clear();
        monneyArrBegin.clear();
        monneyArrCuoc.clear();
        groupparticle.clear();
        arraySkip = false;
        checkAllSkip=false;
        monneyIngame=0;
        monneyCuoc = boardConfig.monneyStart;
        monneyCuocBegin=boardConfig.monneyStart;
        LableMonneyCuoc.setText("$ "+FortmartPrice(monneyCuoc));
        setmonneyBot();
        Tweens.setTimeout(group,duratitionKickBot,()->{
            duratitionKickBot=0;
            innitPlayer();
            renderCard();
            distributeCardsOutSide();
            loadSkipImg();
            loadframeMonney();
            loadmonneyBegin();
            loadFrameNotice();
        });


        /////////// setArrAllCard /////////
        Tweens.setTimeout(group,boardConfig.modePlay *2f,()->{
            for(int i=0;i<boardConfig.modePlay;i++){
                setArrCheck(CardPlay.get(i));

            }
            checkWinGame();
            for (int i=0;i<arrayWinner.size;i++){
                Gdx.app.log("okok","okok: "+arrayWinner.get(i));
            }
        });
    }
    void checkWinGame(){
        Array<Integer> tg;
        int tg2;
        for(int i=0;i<allArrCheck.size-1;i++){
            for (int j=i+1;j<allArrCheck.size;j++){
                if(CheckCard.compare5(allArrCheck.get(i),allArrCheck.get(j))==-1){
                    tg2=arrayWinner.get(i);
                    arrayWinner.set(i,arrayWinner.get(j));
                    arrayWinner.set(j,tg2);
                    tg = allArrCheck.get(i);
                    allArrCheck.set(i,allArrCheck.get(j));
                    allArrCheck.set(j,tg);
                }
            }
        }
    }
    void setArrCheck(Array<Card> object){
        Array<Integer> hand = new Array<>();
        Array<Integer> deck = new Array<>();
        hand.add(object.get(0).Key);
        hand.add(object.get(1).Key);
        for (int i=0;i<5;i++){
            deck.add(cardInTableArr.get(i).Key);
        }
        Array<Integer> check = CheckCard.move(deck,hand);
        allArrCheck.add(check);

    }

    void setWinner(){
        if(SkipImgBotArr.get(arrayWinner.get(0)).isVisible()==false){
            Winner=0;
        }else if(SkipImgBotArr.get(arrayWinner.get(1)).isVisible()==false){
            Winner=1;
        }else if(SkipImgBotArr.get(arrayWinner.get(2)).isVisible()==false){
            Winner=2;
        }else if(SkipImgBotArr.get(arrayWinner.get(3)).isVisible()==false){
            Winner=3;
        }else if(SkipImgBotArr.get(arrayWinner.get(4)).isVisible()==false){
            Winner=4;
        }
        Gdx.app.log("okok","winner: "+arrayWinner.get(Winner));
        Gdx.app.log("okok","winner: "+Winner);
        int index = arrayWinner.get(Winner);
        for(int i=0;i<monneyArrBegin.size;i++){
            Gdx.app.log("okok","monney begin: "+monneyArrBegin.get(i));
        }
        for(int i=0;i<monneyArrCuoc.size;i++){
            Gdx.app.log("okok","monney Cuoc: "+monneyArrCuoc.get(i));
        }
        setmonneyEndGame(index);
    }
    void setmonneyEndGame(int winner){
        flipAllCard();
        SoundEffect.Play(SoundEffect.chipFly5);
        SumChips.moveChipsWin(winner,gamePlay.positionCards.get(winner).x-60,gamePlay.positionCards.get(winner).y-50,0.5f,Interpolation.fastSlow,()->{
            long summonney;
            if(monneyArrCuoc.get(winner)==0){
                summonney=monneyArrCuoc.get(winner)+(boardConfig.modePlay*boardConfig.monneyStart);
            }else {
                summonney=monneyArrCuoc.get(winner)+(boardConfig.modePlay*boardConfig.monneyStart);

            }
            for(int i=0;i<boardConfig.modePlay;i++){
                if(monneyArrCuoc.get(winner)<monneyArrCuoc.get(i)&& i!=winner){
                    summonney+=monneyArrCuoc.get(winner);
                    if(i==0){
                        SoundEffect.Play(SoundEffect.chipFly3);
                        gamePlay.myMonney+=(monneyArrCuoc.get(i)-monneyArrCuoc.get(winner));
                        gamePlay.labelMonney.setText(FortmartPrice(gamePlay.myMonney));
                        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
                        greedilyAlgorithm.renderChip(7000000,GMain.screenWidth/2-70,180,false);
                        greedilyAlgorithm.moveChipsWin(0,gamePlay.positionCards.get(0).x-130,gamePlay.positionCards.get(0).y-100,0.3f,Interpolation.fastSlow,()->{
                            Tweens.setTimeout(group,0.3f,()->{
                                greedilyAlgorithm.disposeChip();

                            });
                        });

                    }else {
                        SoundEffect.Play(SoundEffect.chipFly3);
                        monneyBotArr.set(i-1,monneyBotArr.get(i-1)+(monneyArrCuoc.get(i)-monneyArrCuoc.get(winner)));
                        monneyBotLabelArr.get(i-1).setText(FortmartPrice(monneyBotArr.get(i-1)));
                        GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
                        greedilyAlgorithm.renderChip(7000000,GMain.screenWidth/2-70,180,false);
                        greedilyAlgorithm.moveChipsWin(i,gamePlay.positionCards.get(i).x-130,gamePlay.positionCards.get(i).y-100,0.3f,Interpolation.fastSlow,()->{
                          Tweens.setTimeout(group,0.3f,()->{
                                greedilyAlgorithm.disposeChip();

                            });
                        });
                    }
                }
                if(monneyArrCuoc.get(winner)>=monneyArrCuoc.get(i)&& i!=winner){
                    summonney+=monneyArrCuoc.get(i);
                }
            }
            Gdx.app.log("okok","summonney: "+summonney);

            if(winner>0){
                SoundEffect.Play(SoundEffect.gameOver);
                SoundEffect.Play(SoundEffect.youLose);
                monneyBotArr.set(winner-1,monneyBotArr.get(winner-1)+summonney);
                monneyBotLabelArr.get(winner-1).setText(FortmartPrice(monneyBotArr.get(winner-1)));
            }else {
                SoundEffect.Play(SoundEffect.win);
                SoundEffect.Play(SoundEffect.youWin);
                gamePlay.myMonney+=summonney;
                gamePlay.labelMonney.setText(FortmartPrice(gamePlay.myMonney));
            }
            monneyIngame=0;
            Tweens.setTimeout(group,2f,()->{
                SumChips.disposeChip();
                groupChips.clear();
                for (int i=0; i<SkipImgBotArr.size;i++){
                    SkipImgBotArr.get(i).setVisible(false);
                }
                showResult();
                moreBets();
                showAds();

            });

            Gdx.app.log("okok","monney player: "+gamePlay.myMonney);
            for (int i=0;i<monneyBotArr.size;i++){
                Gdx.app.log("okok","monney Bot: "+monneyBotArr.get(i));
            }
        });

    }

    void showResult(){
        ///// player////
        for (int i=0;i<boardConfig.modePlay;i++){
            long finalmonney=0;
            Label result;
            Image frameResult = GUI.createImage(uiAtlas,"frameResult");

            if(i!=0){
                finalmonney = (monneyArrBegin.get(i)-monneyBotArr.get(i-1))*(-1);
            }else {
                finalmonney = (monneyArrBegin.get(i)-gamePlay.myMonney)*(-1);
            }
            if(finalmonney>0){
                result = new Label("+"+FortmartPrice(finalmonney),new Label.LabelStyle(font,Color.GREEN));
            }else {
                result = new Label(""+FortmartPrice(finalmonney),new Label.LabelStyle(font,null));
            }

            if(i!=0){
                frameResult.setPosition(gamePlay.positionCards.get(i).x-30,gamePlay.positionCards.get(i).y-70,Align.center);
                result.setFontScale(0.7f);
                result.setOrigin(Align.center);
                result.setPosition(gamePlay.positionCards.get(i).x-30,gamePlay.positionCards.get(i).y-70,Align.center);
            }else {
                frameResult.setScale(1.8f);
                frameResult.setOrigin(Align.center);
                frameResult.setPosition(gamePlay.positionCards.get(i).x-30,gamePlay.positionCards.get(i).y-70,Align.center);
                result.setFontScale(1.5f);
                result.setOrigin(Align.center);
                result.setPosition(gamePlay.positionCards.get(i).x,gamePlay.positionCards.get(i).y-70,Align.center);
            }
            groupResult.addActor(frameResult);
            result.setAlignment(Align.center);
            groupResult.addActor(result);
        }
    }
    void flipAllCard(){
            for (int i=0;i<CardPlay.size;i++){
                if(i!=0 && i != boardConfig.modePlay){
                    CardPlay.get(i).get(0).flipAllCard(-boardConfig.scaleBot);
                    CardPlay.get(i).get(1).flipAllCard(-boardConfig.scaleBot);
                    int finalI = i;
                    Tweens.setTimeout(group,0.2f,()->{
                        CardPlay.get(finalI).get(0).setVisibleTiledown();
                        CardPlay.get(finalI).get(0).flipAllCard(boardConfig.scaleBot);
                        CardPlay.get(finalI).get(1).setVisibleTiledown();
                        CardPlay.get(finalI).get(1).flipAllCard(boardConfig.scaleBot);
                    });


                }
            }
    }
    void loadmonneyBegin(){
        monneyArrBegin.add(gamePlay.myMonney);
        for(int i=0;i<monneyBotArr.size;i++){
            monneyArrBegin.add(monneyBotArr.get(i));
        }
    }
    void loadframeMonney(){
        Image frameMonney = GUI.createImage(uiAtlas,"frameMonney");
        frameMonney.setOrigin(Align.center);
        frameMonney.setPosition(GMain.screenWidth/2,250,Align.center);
        group.addActor(frameMonney);
        LableSumMonney = new Label(""+monneyIngame,new Label.LabelStyle(fontmonney,null));
        LableSumMonney.setFontScale(0.5f);
        LableSumMonney.setOrigin(Align.center);
        LableSumMonney.setPosition(GMain.screenWidth/2+10,250,Align.center);
        LableSumMonney.setAlignment(Align.center);
        group.addActor(LableSumMonney);
    }

    void aniTheoTo(float x, float y, int type){
        Image object = GUI.createImage(uiAtlas,"noticeTheo");
        group.addActor(object);
        float moveY=0;
        if(type==0){
            moveY=-130;
        }else {
            moveY=-100;
        }
        object.setOrigin(Align.center);
        object.setPosition(x-30,y-80,Align.center);
        object.addAction(Actions.moveBy(0,moveY,0.5f,Interpolation.fastSlow));
        Tweens.setTimeout(group,0.7f,()->{
            object.remove();
            object.clear();
        });
    }
    public void aniToThem(float x, float y,int type){
        SoundEffect.Play(SoundEffect.tothem);
        Image Img = GUI.createImage(uiAtlas,"noticeTo");
        group.addActor(Img);
        Img.setVisible(true);
        Img.setScale(8);
        Img.setOrigin(Align.center);
        if(type==1){x=x-20;}
        Img.setPosition(x,y-200,Align.center);
        Img.addAction(Actions.scaleTo(1,1,0.3f,Interpolation.bounceOut));
        Tweens.setTimeout(GamePlay.uiGroup,0.6f,()->{
            Img.clear();
            Img.remove();
        });
    }
    public void aniXalang(float x, float y,int type){
        SoundEffect.Play(SoundEffect.xalang);
        Image Img = GUI.createImage(uiAtlas,"noticeXalang");
        group.addActor(Img);
        Img.setVisible(true);
        Img.setScale(8);
        Img.setOrigin(Align.center);
        if(type==1){x=x-20;}
        Img.setPosition(x,y-200,Align.center);
        Img.addAction(Actions.scaleTo(1,1,0.3f,Interpolation.bounceOut));
        Tweens.setTimeout(GamePlay.uiGroup,0.6f,()->{
            Img.clear();
            Img.remove();
        });
    }
    void setPercentCuoc(){
        percent[0]=100/4;
        percent[1]=100/2;
        percent[2]=(100*3)/4;
        percent[3]=100;
    }
    void loadFrameNotice(){
        Image frame = GUI.createImage(uiAtlas,"frameNotice");
        frame.setPosition(0,0,Align.bottomLeft);
        group.addActor(frame);
        LableMonneyCuoc = new Label("$ "+FortmartPrice(monneyCuoc),new Label.LabelStyle(font_white,Color.GREEN));
        LableMonneyCuoc.setFontScale(0.4f);
        LableMonneyCuoc.setOrigin(Align.center);
        LableMonneyCuoc.setPosition(170,115,Align.center);
        LableMonneyCuoc.setAlignment(Align.center);
        group.addActor(LableMonneyCuoc);
        //////// muc cuoc ////////
        labelMucCuoc = new Label("$"+FortmartPrice(boardConfig.monneyStart),new Label.LabelStyle(font_white,Color.SKY));
        labelMucCuoc.setFontScale(0.4f);
        labelMucCuoc.setOrigin(Align.center);
        labelMucCuoc.setPosition(170,30,Align.center);
        labelMucCuoc.setAlignment(Align.center);
        group.addActor(labelMucCuoc);
        //////// so nguoi ////////
        labelSonguoi = new Label(""+boardConfig.modePlay,new Label.LabelStyle(font_white,Color.GOLD));
        labelSonguoi.setFontScale(0.4f);
        labelSonguoi.setOrigin(Align.center);
        labelSonguoi.setPosition(170,70,Align.center);
        labelSonguoi.setAlignment(Align.center);
        group.addActor(labelSonguoi);
        effectWin lightFrame =new effectWin(11,125,80);
        groupparticle.addActor(lightFrame);
    }
    void setmonneyBot(){
        for(int i=0;i<monneyBotArr.size;i++){
            if(monneyBotArr.get(i)==0||monneyBotArr.get(i)<boardConfig.monneyStart){
                duratitionKickBot=1;
               aniKickBot(i);
            }

        }
    }
    void aniKickBot(int index){
        SoundEffect.Play(SoundEffect.dingdong);
        float x=0;
        if(boardConfig.modePlay==5){
            if(index==0||index==1){x=-1000;}
            else if(index==2||index==3){x=1000;}
        }else {
            if(index==0){x=-1000;}
            if(index==1){x=1000;}

        }

        AvtBotArr.get(index).addAction(Actions.sequence(
                Actions.moveBy(x,0,0.5f),
                Actions.moveBy(-x,0,0.5f,Interpolation.swingOut)
                ));
        frameNameBotArr.get(index).addAction(Actions.sequence(
                Actions.moveBy(x,0,0.5f),
                Actions.moveBy(-x,0,0.5f,Interpolation.swingOut)
        ));
        LabelNameBotArr.get(index).addAction(Actions.sequence(
                Actions.moveBy(x,0,0.5f),
                Actions.moveBy(-x,0,0.5f,Interpolation.swingOut)
        ));
        Tweens.setTimeout(group,1f,()->{
            long monney = boardConfig.monneyStart*(long)(Math.random()*10+5);
            counterMonneyUp(monneyBotLabelArr.get(index),monneyBotArr.get(index),monney);
            monneyBotArr.set(index,monney);
        });
    }
    void setMonneyCuocBegin(){
        SoundEffect.Play(SoundEffect.chipFlyFirst);
        Gdx.app.log("okok","check: " +monneyBotArr.size);
        for(int i=0;i<boardConfig.modePlay;i++){
            GreedilyAlgorithm greedilyAlgorithm = new GreedilyAlgorithm(uiAtlas,groupChips);
            greedilyAlgorithm.renderChip(monneyCuoc,gamePlay.positionCards.get(i).x-100,gamePlay.positionCards.get(i).y-100,false);
            greedilyAlgorithm.moveChips(i,GMain.screenWidth/2,180,0.5f,Interpolation.fastSlow,()->{
                Tweens.setTimeout(group,0.2f,()->{
                    greedilyAlgorithm.disposeChip();
                    SumChips.renderChip(monneyIngame,GMain.screenWidth/2-70,180,true);
                });
            });
        }
        counterMonneyUp(LableSumMonney,monneyIngame,monneyCuoc*boardConfig.modePlay);
        monneyIngame =(monneyCuoc*boardConfig.modePlay);
        counterMonney(gamePlay.labelMonney,gamePlay.myMonney,monneyCuoc);
        gamePlay.myMonney -=monneyCuoc;
        for (int i=0;i<monneyBotArr.size;i++){
            counterMonney(monneyBotLabelArr.get(i),monneyBotArr.get(i),boardConfig.monneyStart);
            monneyBotArr.set(i,monneyBotArr.get(i)-boardConfig.monneyStart);
        }
    }
    void loadAvtPlayer(){
        Image MyAvt = GUI.createImage(uiAtlas,"avtMe");
        MyAvt.setPosition(gamePlay.positionCards.get(0).x-MyAvt.getWidth()-50,gamePlay.positionCards.get(0).y,Align.center);
        groupAvt.addActor(MyAvt);
        Image frameName = GUI.createImage(uiAtlas,"namePlayer");
        frameName.setPosition(MyAvt.getX()+MyAvt.getWidth()/2,MyAvt.getY()+MyAvt.getWidth()-18,Align.center);
        groupAvt.addActor(frameName);
        Label name = new Label("Thánh Bài",new Label.LabelStyle(fontName,null));
        name.setFontScale(0.7f);
        name.setOrigin(Align.center);
        name.setPosition(MyAvt.getX()+MyAvt.getWidth()/2+20,MyAvt.getY()+MyAvt.getWidth()-28,Align.center);
        groupAvt.addActor(name);

    }
    void loadAvtBot(){
        for(int i=1;i<boardConfig.modePlay;i++){
            Image avtBot = GUI.createImage(uiAtlas,"avtBot"+i);
            Image frameName = GUI.createImage(uiAtlas,"nameBot");
            Label name = new Label(""+boardConfig.arrName[i],new Label.LabelStyle(fontName,null));

            if(i==3||i==4){
                avtBot.setPosition(gamePlay.positionCards.get(i).x+100,gamePlay.positionCards.get(i).y-90,Align.center);
                frameName.setPosition(gamePlay.positionCards.get(i).x+100,gamePlay.positionCards.get(i).y-55,Align.center);
                name.setFontScale(0.4f);
                name.setOrigin(Align.center);
                float x = gamePlay.positionCards.get(i).x+155;
                if(i==4)
                    x= gamePlay.positionCards.get(i).x+170;
                name.setPosition(x,gamePlay.positionCards.get(i).y-60,Align.center);
            }else {
                if(boardConfig.modePlay==3&&i==2){
                    avtBot.setPosition(gamePlay.positionCards.get(i).x+100,gamePlay.positionCards.get(i).y-90,Align.center);
                    frameName.setPosition(gamePlay.positionCards.get(i).x+100,gamePlay.positionCards.get(i).y-55,Align.center);
                    name.setFontScale(0.4f);
                    name.setOrigin(Align.center);
                    name.setPosition(gamePlay.positionCards.get(i).x+170,gamePlay.positionCards.get(i).y-60,Align.center);
                }else{
                    avtBot.setPosition(gamePlay.positionCards.get(i).x-150,gamePlay.positionCards.get(i).y-90,Align.center);
                    frameName.setPosition(gamePlay.positionCards.get(i).x-150,gamePlay.positionCards.get(i).y-55,Align.center);
                    name.setFontScale(0.4f);
                    name.setOrigin(Align.center);
                    name.setPosition(gamePlay.positionCards.get(i).x-80,gamePlay.positionCards.get(i).y-60,Align.center);
                }
            }
            groupAvt.addActor(avtBot);
            groupAvt.addActor(frameName);
            groupAvt.addActor(name);
            AvtBotArr.add(avtBot);
            frameNameBotArr.add(frameName);
            LabelNameBotArr.add(name);

        }
    }

    void loadframeSetting(){
        Image Setting = GUI.createImage(uiAtlas,"btnSetting");
        Setting.setOrigin(Align.center);
        Setting.setPosition(GMain.screenWidth-Setting.getWidth()/2,Setting.getHeight()/2,Align.center);
        groupAvt.addActor(Setting);
        Setting.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                SoundEffect.Play(SoundEffect.click);
                Setting.setTouchable(Touchable.disabled);
                Setting.addAction(Actions.sequence(
                        Actions.scaleTo(0.8f,0.8f,0.1f),
                        Actions.scaleTo(1f,1f,0.1f)
                ));
                Tweens.setTimeout(group,0.2f,()->{
                    new Setting(uiAtlas, Setting,gamePlay);
                });
            }
        });
    }
    void onTurn(int type, float x, float y,float duration){
        lightTurn = GUI.createImage(uiAtlas,"lightTurn");
        if(boardConfig.modePlay==5) {
            if (type == 1 || type == 2) {
                lightTurn.setWidth(lightTurn.getWidth() * 0.7f);
                lightTurn.setHeight(lightTurn.getHeight() * 0.7f);
                x = x - 115;
                y = y - 60;
            } else if (type == 3 || type == 4) {
                lightTurn.setWidth(lightTurn.getWidth() * 0.7f);
                lightTurn.setHeight(lightTurn.getHeight() * 0.7f);
                x = x + 135;
                y = y - 60;
            } else {

                x = x - 150;
                y = y + 50;
            }
        }else {

            if(type==1){
                lightTurn.setWidth(lightTurn.getWidth() * 0.7f);
                lightTurn.setHeight(lightTurn.getHeight() * 0.7f);
                x = x - 115;
                y = y - 60;
            }else if(type==2){
                lightTurn.setWidth(lightTurn.getWidth() * 0.7f);
                lightTurn.setHeight(lightTurn.getHeight() * 0.7f);
                x= x + 135;
                y= y -60;
            }else {
                x = x - 150;
                y = y + 50;
            }
        }

        lightTurn.setPosition(x,y,Align.center);
        groupparticle.addActor(lightTurn);
        lightTurn.addAction(Actions.rotateTo(10000,duration));

    }

    void initFont(){
        font = GAssetsManager.getBitmapFont("silver.fnt");
        fontName = GAssetsManager.getBitmapFont("fontVn.fnt");
        fontmonney = GAssetsManager.getBitmapFont("gold.fnt");
        font_white = GAssetsManager.getBitmapFont("font_white.fnt");
    }
    void setTouch(){
        btnAddMore.setTouchable(Touchable.enabled);
        btnEspouse.setTouchable(Touchable.enabled);
        btnSkip.setTouchable(Touchable.enabled);
    }
    private String FortmartPrice(Long Price) {

        DecimalFormat mDecimalFormat = new DecimalFormat("###,###,###,###");
        String mPrice = mDecimalFormat.format(Price);

        return mPrice;
    }
    void showAds(){
        countAds++;
        if(countAds==4){
            GMain.platform.ShowFullscreen();
            countAds=0;
        }

    }


}
