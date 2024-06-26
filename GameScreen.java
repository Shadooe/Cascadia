import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;


public class GameScreen {

    private int currentBoardSize = 900;
    private int currentBoardXPos = 80;
    private int currentBoardYPos = 150;




    private int currentScoreXPos = 120;
    private int currentScoreYPos = 120;








    private int sideBoardSize = 400;
    private int sideBoardXPos = 630;
    private int sideScoreXPos = 820;
    private int sideLabelXPos = 925;




    private int sideBoard1YPos = 150;
    private int sideScore1YPos = 120;
    private int sideLabel1YPos = 225;




    private int sideBoard2YPos = 480;
    private int sideScore2YPos = 620;
    private int sideLabel2YPos = 825;




    private BufferedImage bg;
    private Button titleScreenButton;
    private Button manualButton;



    private boolean confirmQuit = false;
    private BufferedImage confirmQuitbg;
    private Button yesButton;
    private Button noButton;

    private Button shuffleOverpopulatedButton;
    private Button continueButton;

    private Button freeSelectButton;
    private Button shuffleTokensButton;

    private Button rotateButton;
    private Button discardButton;

    public Button[] pickTiles = new Button[4];
    public Button[] pickTokens = new Button[4];

    public BagOfAnimalTokens tokenBag = new BagOfAnimalTokens();
    public TilePile tilePile;
    public AnimalToken[] tokens = new AnimalToken[4];
    public HabitatTile[] tiles = new HabitatTile[4];


    private int selectedTile = -1;
    private int selectedToken = -1;
    private boolean freeSelect = false;

    private AnimalToken chosenToken = null;
    private HabitatTile chosenTile = null;

    private ScoreRules bearRules;
    private ScoreRules salmonRules;
    private ScoreRules hawkRules;
    private ScoreRules foxRules;
    private ScoreRules elkRules;

    private Player[] players;



    private int width = 0;
    private int height = 0;




    private int currentPlayerNum;
    private Player currentPlayer;


    private int round = 1;
    private int totalRounds = 20;

    private int gamestate = 0;
    /*-1 Testing
     * 0 Start of Round
     * 1 Start Player Turn/clear overpop
     * 2 Choose Tile
     * 3 Choose Token
     * 4 Place Tile
     * 5 Place Token
     * 6 End Player Turn
     */





    public GameScreen(int w, int h, ScoreRules bear, ScoreRules salmon, ScoreRules hawk, ScoreRules fox, ScoreRules elk,Player[] p, TilePile t) {

        width = w;
        height = h;

        bearRules = bear;
        salmonRules = salmon;
        hawkRules = hawk;
        foxRules = fox;
        elkRules = elk;

        players = p;
        tilePile = t;

        titleScreenButton = new ImageButton("IconTitleScreenButton",1068,12,52,52);
        manualButton = new ImageButton("IconManualButton",1129,13,48,48);




        yesButton = new ImageButton("YesButton",417,461,363,109);
        noButton = new ImageButton("NoButton",417,576,363,109);

        shuffleOverpopulatedButton = new ImageButton("ShuffleOverpopulatedButton",980,740,181,54);
        continueButton = new ImageButton("ContinueButton",980,800,181,54);

        freeSelectButton = new ImageButton("FreeSelectButton",980,740,181,54);
        shuffleTokensButton = new ImageButton("RemoveTokensButton",980,800,181,54);

        rotateButton = new ImageButton("RotateButton",980,740,181,54);
        discardButton = new ImageButton("DiscardButton",980,740,181,54);

        pickTiles[0] = new Button(1013,100,147);
        pickTiles[1] = new Button(1013,255,147);
        pickTiles[2] = new Button(1013,410,147);
        pickTiles[3] = new Button(1013,560,147);

        pickTokens[0] = new ImageButton("TokenButton",909,123,105);
        pickTokens[1] = new ImageButton("TokenButton",909,273,105);
        pickTokens[2] = new ImageButton("TokenButton",909,423,105);
        pickTokens[3] = new ImageButton("TokenButton",909,578,105);
        for(Button b: pickTokens) {
            //b.setHidden(true);
        }



        try {
            bg = ImageIO.read(StartScreen.class.getResource("/guiImages/GameScreenFinalBG.png"));
        }
        catch(Exception E) {
            bg = null;
            System.out.println("STARTSCREEN: BG IMAGE NOT FOUND");
        }

        try {
            confirmQuitbg = ImageIO.read(StartScreen.class.getResource("/guiImages/GameScreenFinalConfirmBG.png"));
        }
        catch(Exception E) {
            bg = null;
            System.out.println("STARTSCREEN: BG IMAGE NOT FOUND");
        }

        round = 1;



        for(int i = 0;i<tokens.length;i++) {
            tokens[i] = tokenBag.draw();
        }

        for(int i = 0;i<tiles.length;i++) {
            tiles[i] = tilePile.draw();
        }

        setCurrentPlayer(1);
        selectedTile = -1;
        updateSelected();



    }

    public void setCurrentPlayer(int num) {
        currentPlayerNum = num;
        currentPlayer = players[num-1];
        System.out.println(currentPlayer);
    }

    //returns true if its the end of the round
    public boolean nextPlayer() {
        if(currentPlayerNum == players.length) {
            setCurrentPlayer(1);
            return true;
        }else {
            setCurrentPlayer(currentPlayerNum+1);
            return false;
        }

    }

    public int getNextPlayerNum(int playerNum) {

        if(playerNum >= players.length) {
            return 1;
        }else {
            return playerNum + 1;
        }
    }

    public Player getNextPlayer(int playerNum) {

        if(playerNum >= players.length) {
            return players[0];
        }else {
            return players[playerNum];
        }
    }

    public void updateSelected() {
        if(selectedTile < 0||selectedTile > pickTiles.length) {
            for(Button b:pickTiles) {
                b.setDisabled(false);
                b.setSelected(false);
            }
            for(Button b:pickTokens) {
                b.setDisabled(false);
                b.setSelected(false);
            }
        }
        else {
            for(int i = 0; i<pickTiles.length;i++) {
                if(i==selectedTile) {
                    pickTiles[i].setDisabled(false);


                }else {
                    pickTiles[i].setDisabled(true);


                }

            }
            for(int i = 0; i<pickTokens.length;i++) {
                if(i==selectedTile) {
                    pickTokens[i].setDisabled(false);


                }else {
                    pickTokens[i].setDisabled(true);


                }

            }

        }
    }



    /*
     * 1: bear
     * 2: elk
     * 3: salmon
     * 4: hawks
     * 5: fox
     */
    public int getPopulation(int animal){

        int pop = 0;

        for(AnimalToken a:tokens) {
            if(a.getAnimalType()==animal) {
                pop++;
            }
        }

        return pop;
    }

    public int getOverpopulated(){
        for(int i = 1; i<6;i++) {
            if(getPopulation(i)>=3) {
                return i;
            }
        }
        return -1;
    }

    public void shuffleAllTokens(boolean putBack){
        if(putBack) {
            for(AnimalToken a:tokens) {
                tokenBag.addTokens(a);
            }
        }
        for(int i = 0; i<tokens.length;i++) {
            tokens[i] = tokenBag.draw();
        }
    }

    public void shuffleSelectedTokens(boolean putBack) {
        for(int i = 0; i < pickTokens.length; i++) {
            if(pickTokens[i].getSelected() == true) {
                if(putBack) {
                    tokenBag.addTokens(tokens[i]);
                }
                tokens[i] = tokenBag.draw();
                pickTokens[i].setSelected(false);
            }
        }
    }

    public void rotateChosenTile(){
        chosenTile.rotateCCW();
    }
























    public void paint(Graphics g, Graphics2D g2d) {

        if(confirmQuit) {
            g.drawImage(confirmQuitbg, 0, 0, width, height, null);
            yesButton.paint(g);
            noButton.paint(g);
        }else {
            g.drawImage(bg, 0, 0, width, height, null);

            titleScreenButton.paint(g);
            manualButton.paint(g);









            Player next = getNextPlayer(currentPlayerNum);
            Player last = getNextPlayer(getNextPlayerNum(currentPlayerNum));




            switch(gamestate) {
                case -1:
                    break;
                case 0:
                    g.drawString("Round "+round+"/"+totalRounds +" click anywhere to continue", 10, 20);
                    break;
                case 1:
                    int over = getOverpopulated();
                    if(over > -1) {
                        switch(getPopulation(over)) {
                            case 3:
                                shuffleOverpopulatedButton.setDisabled(false);
                                break;
                            case 2:
                                shuffleAllTokens(true);
                                shuffleOverpopulatedButton.setDisabled(true);
                        }
                    }

                    g.drawString(currentPlayer.getName()+"'S TURN", 10, 20);
                    g.drawString(currentPlayer.getNatureTokens()+"", 55, 135);
                    currentPlayer.getBoard().setDisplay(currentBoardXPos, currentBoardYPos, currentBoardSize);
                    currentPlayer.getBoard().paint(g);
                    g.drawString(next.getNatureTokens()+"", 620, 130);
                    next.getBoard().setDisplay(sideBoardXPos, sideBoard1YPos, sideBoardSize);
                    next.getBoard().paint(g);
                    g.drawString(last.getNatureTokens()+"", 620, 445);
                    last.getBoard().setDisplay(sideBoardXPos, sideBoard2YPos, sideBoardSize);
                    last.getBoard().paint(g);
                    for(int i = 0; i < tiles.length;i++) {
                        pickTiles[i].paint(g);
                        tiles[i].paint(g2d, pickTiles[i]);
                    }

                    for(int i = 0; i < tokens.length;i++) {
                        Button b = pickTokens[i];
                        b.paint(g);
                        tokens[i].paint(g, b.getXPos(), b.getYPos(), b.getHeight(), b.getHeight());

                    }


                    continueButton.paint(g);

                    break;
                case 2:
                    g.drawString(currentPlayer.getName()+"'S TURN", 10, 20);
                    g.drawString(currentPlayer.getNatureTokens()+"", 55, 135);
                    currentPlayer.getBoard().setDisplay(currentBoardXPos, currentBoardYPos, currentBoardSize);
                    currentPlayer.getBoard().paint(g);
                    g.drawString(next.getNatureTokens()+"", 620, 130);
                    next.getBoard().setDisplay(sideBoardXPos, sideBoard1YPos, sideBoardSize);
                    next.getBoard().paint(g);
                    g.drawString(last.getNatureTokens()+"", 620, 445);
                    last.getBoard().setDisplay(sideBoardXPos, sideBoard2YPos, sideBoardSize);
                    last.getBoard().paint(g);

                    for(int i = 0; i < tiles.length;i++) {
                        pickTiles[i].paint(g);
                        tiles[i].paint(g2d, pickTiles[i]);
                    }

                    for(int i = 0; i < tokens.length;i++) {
                        Button b = pickTokens[i];
                        b.paint(g);
                        tokens[i].paint(g, b.getXPos(), b.getYPos(), b.getHeight(), b.getHeight());

                    }

                    g.drawString("Choose A Tile", 10, 40);
                    break;
                case 3:
                    g.drawString(currentPlayer.getName()+"'S TURN", 10, 20);
                    g.drawString(currentPlayer.getNatureTokens()+"", 55, 135);
                    currentPlayer.getBoard().setDisplay(currentBoardXPos, currentBoardYPos, currentBoardSize);
                    currentPlayer.getBoard().paint(g);
                    g.drawString(next.getNatureTokens()+"", 620, 130);
                    next.getBoard().setDisplay(sideBoardXPos, sideBoard1YPos, sideBoardSize);
                    next.getBoard().paint(g);
                    g.drawString(last.getNatureTokens()+"", 620, 445);
                    last.getBoard().setDisplay(sideBoardXPos, sideBoard2YPos, sideBoardSize);
                    last.getBoard().paint(g);

                    for(int i = 0; i < tiles.length;i++) {
                        pickTiles[i].paint(g);
                        tiles[i].paint(g2d, pickTiles[i]);
                    }

                    for(int i = 0; i < tokens.length;i++) {
                        Button b = pickTokens[i];
                        b.paint(g);
                        tokens[i].paint(g, b.getXPos(), b.getYPos(), b.getHeight(), b.getHeight());

                    }

                    g.drawString("Choose A Token", 10, 40);
                    freeSelectButton.paint(g);
                    shuffleTokensButton.paint(g);
                    break;
                case 4:
                    g.drawString(currentPlayer.getName()+"'S TURN", 10, 20);
                    g.drawString(currentPlayer.getNatureTokens()+"", 55, 135);
                    currentPlayer.getBoard().setDisplay(currentBoardXPos, currentBoardYPos, currentBoardSize);
                    currentPlayer.getBoard().paint(g);
                    g.drawString(next.getNatureTokens()+"", 620, 130);
                    next.getBoard().setDisplay(sideBoardXPos, sideBoard1YPos, sideBoardSize);
                    next.getBoard().paint(g);
                    g.drawString(last.getNatureTokens()+"", 620, 445);
                    last.getBoard().setDisplay(sideBoardXPos, sideBoard2YPos, sideBoardSize);
                    last.getBoard().paint(g);

                    chosenTile.paint(g,new Button(950,300,147));
                    rotateButton.paint(g);

                    g.drawString("Place Your Tile", 10, 40);
                    break;
                case 5:
                    g.drawString(currentPlayer.getName()+"'S TURN", 10, 20);
                    g.drawString(currentPlayer.getNatureTokens()+"", 55, 135);
                    currentPlayer.getBoard().setDisplay(currentBoardXPos, currentBoardYPos, currentBoardSize);
                    currentPlayer.getBoard().paint(g);
                    g.drawString(next.getNatureTokens()+"", 620, 130);
                    next.getBoard().setDisplay(sideBoardXPos, sideBoard1YPos, sideBoardSize);
                    next.getBoard().paint(g);
                    g.drawString(last.getNatureTokens()+"", 620, 445);
                    last.getBoard().setDisplay(sideBoardXPos, sideBoard2YPos, sideBoardSize);
                    last.getBoard().paint(g);

                    chosenToken.paint(g, 950, 300, 105, 105);
                    discardButton.paint(g);

                    g.drawString("Place Your Token", 10, 40);
                    break;
                case 6:
                    g.drawString(currentPlayer.getName()+"'S TURN", 10, 20);
                    g.drawString(currentPlayer.getNatureTokens()+"", 55, 135);
                    currentPlayer.getBoard().setDisplay(currentBoardXPos, currentBoardYPos, currentBoardSize);
                    currentPlayer.getBoard().paint(g);
                    g.drawString(next.getNatureTokens()+"", 620, 130);
                    next.getBoard().setDisplay(sideBoardXPos, sideBoard1YPos, sideBoardSize);
                    next.getBoard().paint(g);
                    g.drawString(last.getNatureTokens()+"", 620, 445);
                    last.getBoard().setDisplay(sideBoardXPos, sideBoard2YPos, sideBoardSize);
                    last.getBoard().paint(g);

                    g.drawString("Click To Continue", 10, 40);
                    continueButton.paint(g);
                    break;

                case 7:
                    g.drawString(currentPlayer.getName()+"'S TURN", 10, 20);
                    g.drawString(currentPlayer.getNatureTokens()+"", 55, 135);
                    currentPlayer.getBoard().setDisplay(currentBoardXPos, currentBoardYPos, currentBoardSize);
                    currentPlayer.getBoard().paint(g);
                    g.drawString(next.getNatureTokens()+"", 620, 130);
                    next.getBoard().setDisplay(sideBoardXPos, sideBoard1YPos, sideBoardSize);
                    next.getBoard().paint(g);
                    g.drawString(last.getNatureTokens()+"", 620, 445);
                    last.getBoard().setDisplay(sideBoardXPos, sideBoard2YPos, sideBoardSize);
                    last.getBoard().paint(g);

                    for(int i = 0; i < tiles.length;i++) {
                        pickTiles[i].paint(g);
                        tiles[i].paint(g2d, pickTiles[i]);
                    }

                    for(int i = 0; i < tokens.length;i++) {
                        Button b = pickTokens[i];
                        b.paint(g);
                        tokens[i].paint(g, b.getXPos(), b.getYPos(), b.getHeight(), b.getHeight());

                    }
                    discardButton.paint(g);




            }
        }




    }

    public void mouseMoved(int clickX, int clickY) {

        if(confirmQuit) {
            yesButton.mouseMoved(clickX, clickY);
            noButton.mouseMoved(clickX, clickY);
        }else {

            titleScreenButton.mouseMoved(clickX, clickY);
            manualButton.mouseMoved(clickX, clickY);

            shuffleOverpopulatedButton.mouseMoved(clickX, clickY);
            continueButton.mouseMoved(clickX, clickY);

            for(Button b:pickTiles) {
                b.mouseMoved(clickX, clickY);
            }

            for(Button b:pickTokens) {
                b.mouseMoved(clickX, clickY);
            }
            currentPlayer.getBoard().mouseMoved(clickX, clickY);





        }
    }

    public int mouseClicked(int clickX, int clickY) {

        if(!confirmQuit) {

            switch(titleScreenButton.mouseClicked(clickX,clickY)) {

                case 0:
                    return 0;

                case 1:
                    confirmQuit = true;
                    return 1;
            }
            switch(manualButton.mouseClicked(clickX,clickY)) {

                case 0:
                    return 0;

                case 1:
                    return 3;
            }

            int clicked;
            switch(gamestate) {
                case -1:
                    break;
                case 0:
                    gamestate = 1;
                    break;
                case 1:
                    gamestate = 2;
                    break;
                case 2:
                    for(int i = 0; i < pickTiles.length; i++){
                        if(pickTiles[i].mouseClicked(clickX,clickY)>0){
                            selectedTile = i;
                            pickTiles[i].setSelected(true);
                            chosenTile = tiles[i];


                            for(int j = 0; j < tokens.length; j++){


                                if(j==i){pickTokens[j].setDisabled(false);}
                                else{pickTokens[j].setDisabled(true);}


                            }


                            gamestate = 3;


                        }


                    }




                    break;
                case 3:
                    if(shuffleOverpopulatedButton.mouseClicked(clickX,clickY)>0){


                        //shuffleOverpopulated();


                    }
                    if(shuffleTokensButton.mouseClicked(clickX,clickY) > 0 && currentPlayer.getNatureTokens() > 0){


                        currentPlayer.removeNatureToken();
                        gamestate = 7;
                        //currentPlayer.setNatureTokens(currentPlayer.getNatureTokens()-1);


                    }
                    if(freeSelectButton.mouseClicked(clickX,clickY)> 0 && currentPlayer.getNatureTokens() > 0){


                        freeSelect = true;
                        for(Button b:pickTokens){b.setDisabled(false);}
                        currentPlayer.removeNatureToken();



                    }
                    for(int i = 0; i < pickTokens.length; i++){


                        if(pickTokens[i].mouseClicked(clickX,clickY) > 0){


                            selectedToken = i;
                            chosenToken = tokens[i];
                            pickTiles[selectedTile].setSelected(false);
                            freeSelect = false;


                            tiles[selectedTile] = tilePile.draw();
                            tokens[selectedToken] = tokenBag.draw();
                            if(getOverpopulated() > -1){


                                if(getPopulation(getOverpopulated())>=4){


                                    shuffleAllTokens(true);


                                }


                            }
                            currentPlayer.getBoard().enableOpenTileButtons();
                            gamestate = 4;


                        }


                    }
                    break;
                case 4:
                    if(rotateButton.mouseClicked(clickX,clickY) > 0){


                        rotateChosenTile();


                    }
                    if(currentPlayer.getBoard().addTileToClicked(chosenTile,clickX,clickY)){


                        chosenTile = null;
                        currentPlayer.getBoard().enableOpenTokenButtons(chosenToken);
                        gamestate = 5;


                    }
                    break;
                case 5:
                    //tokenBag.addTokens(tokens[]);


                    if(discardButton.mouseClicked(clickX,clickY) > 0){


                        tokenBag.addTokens(chosenToken);
                        chosenToken = null;
                        gamestate = 6;


                    }
                    if(currentPlayer.getBoard().addTokenToClicked(chosenToken,clickX,clickY)){




                        chosenToken = null;
                        gamestate = 6;
                    }
                    break;
                case 6:
                    selectedTile = -1;
                    selectedToken = -1;
                    updateSelected();
                    boolean newRound = nextPlayer();
                    if(newRound) {
                        round++;
                        gamestate = 0;
                        if(round>totalRounds) {
                            return 4;
                        }
                    }else {
                        gamestate = 1;
                    }
                    break;

                case 7:
                    if(discardButton.mouseClicked(clickX,clickY)>0){




                        shuffleSelectedTokens(true);
                        gamestate = 2;
                        for(Button b:pickTokens){
                            b.setSelected(false);
                        }




                    }
                    for(Button b:pickTokens){
                        b.setSelected(!b.getSelected());
                    }




                    break;

            }
            System.out.println(gamestate);
            return 1;
        }else {
            switch(yesButton.mouseClicked(clickX,clickY)) {
                case 0:
                    return 0;
                case 1:
                    confirmQuit = false;
                    return 2;

            }

            switch(noButton.mouseClicked(clickX,clickY)) {
                case 0:
                    return 0;
                case 1:
                    confirmQuit = false;
                    return 1;

            }
        }

        return -1;


    }


    public EndScreen getEndScreen() {
        return new EndScreen(players,width,height);
    }
}