import java.util.ArrayList;
import static java.lang.System.*;


import java.awt.*;
public class GameBoard {
    private Hex[][] board;
    private Button[][] buttonGrid = new Button[42][42];
    private int xPos;
    private int yPos;
    private int size;
    private int natureTokens;


    public GameBoard(int x,int y,int s) {
        board = new Hex[42][42];
        for (int r=0; r<42; r++) {
            for (int c=0; c<42; c++) {
                board[r][c] = new Hex(r, c);
            }
        }
        natureTokens = 0;
        xPos = x;
        yPos = y;
        size = s;
    }


    public Hex[][] getBoard() {
        return board;
    }


    public int getHeight() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int r=0; r<42; r++) {
            for (int c=0; c<42; c++) {
                if (!board[r][c].getEmpty()) {
                    list.add(r);
                }
            }
        }
        int diff = 0;
        if (!list.isEmpty()) {
            diff = list.get(list.size()-1) - list.get(0);
        }
        return diff+1;
    }
    public int getLength() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int c=0; c<42; c++) {
            for (int r=0; r<42; r++) {
                if (!board[r][c].getEmpty()) {
                    list.add(c);
                }
            }
        }
        int diff = 0;
        if (!list.isEmpty()) {
            diff = list.get(list.size()-1) - list.get(0);
        }
        return diff+1;
    }


    // returns -1 if board is empty
    public int getStartingX() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int c=0; c<42; c++) {
            for (int r=0; r<42; r++) {
                if (!board[r][c].getEmpty()) {
                    list.add(c);
                }
            }
        }
        if (!list.isEmpty())
            return list.get(0);
        return -1;
    }


    // returns -1 if board is empty
    public int getStartingY() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int r=0; r<42; r++) {
            for (int c=0; c<42; c++) {
                if (!board[r][c].getEmpty()) {
                    list.add(r);
                }
            }
        }
        if (!list.isEmpty())
            return list.get(0);
        return -1;
    }


    public void addStarterTile(StarterTile st) {
        placeTile(20, 21, st.getTop());
        placeTile(21, 20, st.getLeft());
        placeTile(21, 21, st.getRight());
    }




    public boolean placeTile(int r, int c, HabitatTile tile) {
        if (board[r][c].getEmpty()) {
            board[r][c].setTile(tile);
            updateButtonGrid();
            //board[r][c].setEmpty(false);
            return true;
        }
        return false;
    }


    public boolean placeAnimal(int r, int c, AnimalToken t) {
        int type = t.getAnimalType();
        HabitatTile tile = board[r][c].getTile();


        if (type == 1 && tile!=null) {
            if (!tile.getContainsAnimal() && tile.getCanHoldBear()) {
                tile.setToken(t);
                tile.setContainsAnimal(true);
                if (tile.getIsKeystone())
                    natureTokens++;
                return true;
            }
        }


        if (type == 2 && tile!=null) {


            if (!tile.getContainsAnimal() && tile.getCanHoldElk()) {
                tile.setToken(t);
                tile.setContainsAnimal(true);
                if (tile.getIsKeystone())
                    natureTokens++;
                return true;
            }
        }


        if (type == 3 && tile!=null) {
            if (!tile.getContainsAnimal() && tile.getCanHoldSalmon()) {
                tile.setToken(t);
                tile.setContainsAnimal(true);
                if (tile.getIsKeystone())
                    natureTokens++;
                return true;
            }
        }


        if (type == 4 && tile!=null) {
            if (!tile.getContainsAnimal() && tile.getCanHoldHawk()) {
                tile.setToken(t);
                tile.setContainsAnimal(true);
                if (tile.getIsKeystone())
                    natureTokens++;
                return true;
            }
        }


        if (type == 5 && tile!=null) {
            if (!tile.getContainsAnimal() && tile.getCanHoldFox()) {
                tile.setToken(t);
                tile.setContainsAnimal(true);
                if (tile.getIsKeystone())
                    natureTokens++;
                return true;
            }
        }
        return false;
    }


    public Hex getAdjHex(int r, int c, int s) {
        // r==2, c==1, s==1
        int[] evenCol = {-1, 0, 1, 0, -1, -1};
        int[] evenRow = {-1, -1, 0, 1, 1, 0};
        int[] oddCol = {0, 1, 1, 1, 0, -1};
        int[] oddRow = {-1, -1, 0, 1, 1, 0};
        Hex h = board[r][c];
        boolean even = (r%2 == 0);


        if (r==0) {
            if (s==0 || s==1) {
                return null;
            }
        }
        if (c==0) {
            if (!even) {
                if (s==5)
                    return null;
            }
            if (even) {
                if (s==0 || s==4 || s==5) {
                    return null;
                }
            }
        }
        if (c == 41) {
            if(even) {
                if (s==2)
                    return null;
            }
            if (!even) {
                if (s==1 || s==2 || s==3)
                    return null;
            }
        }
        if (r == 41) {
            if (s==3 || s==4)
                return null;
        }


        if (!even) {
            return board[r+oddRow[s]][c+oddCol[s]];
        }
        else {
            return board[r+evenRow[s]][c+evenCol[s]];
            // return board[r-1][c-1]
        }


    }

    public ArrayList<Hex> getAdjacents(int r, int c) {
        ArrayList<Hex> list = new ArrayList<Hex>();
        for (int i=0; i<6; i++) {
            list.add(getAdjHex(r, c, i));
        }
        return list;
    }


    public boolean getOpen(int r, int c) {
        boolean tileAdjacent = false;
        ArrayList<Hex> adjacents = getAdjacents(r, c);
        for (int i=0; i<6; i++) {
            if (adjacents.get(i) != null){
                if (!adjacents.get(i).getEmpty()) {
                    tileAdjacent = true;
                }
            }
        }
        return (board[r][c].getEmpty() && tileAdjacent);
    }


    // determines if the hawk at the given coordinate does not have any adjacent hawks
    public int soloHawk(int row, int col) {
        ArrayList<Hex> list = getAdjacents(row, col);


        for (Hex h: list) {
            if (h!=null && h.getTile() != null) {
                if (h.getTile().containsHawk())
                    return 0;
            }
        }
        return 1;
    }

    public int calculateHawk() {
        int numHawk = 0;
        for (int r=0; r<42; r++) {
            for (int c=0; c<42; c++) {
                if (!board[r][c].getEmpty()) {
                    if (board[r][c].getTile().containsHawk()==true)
                        numHawk += soloHawk(r, c);
                }
            }
        }
        if (numHawk < 1) {
            return 0;
        } else if (numHawk < 2) {
            return 2;
        } else if (numHawk < 3) {
            return 5;
        } else if (numHawk < 4) {
            return 8;
        } else if (numHawk < 5) {
            return 11;
        } else if (numHawk < 6) {
            return 14;
        } else if (numHawk < 7) {
            return 18;
        } else if (numHawk < 8) {
            return 22;
        } else {
            return 28;
        }
    }


    public int calculateFox() {
        int scoreFox =0;
        for (int r=0; r<42; r++) {
            for (int c=0; c<42; c++) {
                if (!board[r][c].getEmpty() && board[r][c].getTile().containsFox()) {
                    scoreFox += uniqueFox(r, c);
                }
            }
        }
        return scoreFox;
    }


    public int uniqueFox(int row, int col) {
        ArrayList<Hex> list = getAdjacents(row, col);
        boolean adjacentBear = false;
        boolean adjacentElk = false;
        boolean adjacentSalmon = false;
        boolean adjacentHawk = false;
        boolean adjacentFox = false;
        int total = 0;
        for (Hex h: list) {
            if (h!=null && h.getTile()!=null) {
                if (h.getTile().containsBear()) {
                    adjacentBear = true;
                }
                if (h.getTile().containsElk()) {
                    adjacentElk = true;
                }
                if (h.getTile().containsSalmon()) {
                    adjacentSalmon = true;
                }
                if (h.getTile().containsHawk()) {
                    adjacentHawk = true;
                }
                if (h.getTile().containsFox()) {
                    adjacentFox = true;
                }
            }
        }
        if (adjacentBear)
            total++;
        if (adjacentElk)
            total++;
        if (adjacentSalmon)
            total++;
        if (adjacentHawk)
            total++;
        if (adjacentFox)
            total++;
        return total;
    }


    public int calculateElk() {
        int score = 0;
        for (int r = 0; r<42; r++) {
            for (int c = 0; c<42; c++) {
                Hex h = board[r][c];
                if (!h.getEmpty() && h.getTile().containsElk() && !h.getTile().getToken().getScored()) {
                    int runSize = elkRun(r, c)+1;
                    if (runSize < 1) {
                        score += 0;
                    } else if (runSize < 2) {
                        score += 2;
                    } else if (runSize < 3) {
                        score += 4;
                    } else if (runSize < 4) {
                        score += 7;
                    } else if (runSize < 5) {
                        score += 10;
                    } else if (runSize < 6) {
                        score += 14;
                    } else if (runSize < 7) {
                        score += 18;
                    } else if (runSize < 8) {
                        score += 23;
                    } else {
                        score += 28;
                    }
                }
            }
        }
        return score;
    }


    public int calculateSalmon() {
        int score = 0;
        for (int r=0; r<42; r++) {
            for (int c=0; c<42; c++) {
                Hex h = board[r][c];
                if (!h.getEmpty() && h.getTile().containsSalmon() && !h.getTile().getToken().getScored()) {
                    int numAdjSalmon = 0;
                    int side = 0;
                    ArrayList<Hex> adjacentSalmons = new ArrayList<Hex>();
                    for (int i=0; i<6; i++) {
                        Hex spot = getAdjHex(r, c, i);
                        if (spot!=null && !spot.getEmpty()) {
                            if (spot.getTile().containsSalmon() && !spot.getTile().getToken().getScored()) {
                                numAdjSalmon++;
                                side = i;
                                adjacentSalmons.add(spot);
                            }
                        }
                    }
                    if (adjacentSalmons.isEmpty()) {
                        score += 2;
                        board[r][c].getTile().getToken().setScored(true);
                    }
                    if (numAdjSalmon==1) {
                        boolean b = adjacentRun(r, c, side);
                        if (b) {
                            int size = runSize(r, c) + 1;
                            out.println("size " + size);
                            if (size < 3) {
                                score += 5;
                            } else if (size < 4) {
                                score += 8;
                            } else if (size < 5) {
                                score += 12;
                            } else if (size < 6) {
                                score += 16;
                            } else if (size < 7) {
                                score += 20;
                            } else {
                                score += 25;
                            }
                        }
                    }
                    if (numAdjSalmon == 2) {
                        Hex firstSalmon = adjacentSalmons.get(0);
                        Hex secondSalmon = adjacentSalmons.get(1);
                        if (findTypeAdjacent(firstSalmon)<=2 && findTypeAdjacent(secondSalmon)<=2) {
                            score += 8;
                            board[r][c].getTile().getToken().setScored(true);
                            firstSalmon.getTile().getToken().setScored(true);
                            secondSalmon.getTile().getToken().setScored(true);
                        }
                    }
                }
            }
        }
        return score;


    }


    public int findTypeAdjacent(Hex h) {
        int cnt = 0;
        if (!h.getEmpty() && h.getTile().getContainsAnimal()) {
            ArrayList<Hex> adjacents = getAdjacents(h.getRow(), h.getColumn());
            for (Hex spot: adjacents) {
                if (spot != null && !spot.getEmpty() && spot.getTile().getContainsAnimal() && spot.getTile().getToken().getAnimalType() == h.getTile().getToken().getAnimalType()) {
                    cnt++;
                }

            }
        }
        return cnt;
    }


    public int elkRun(int r, int c) {
        int cnt = 0;
        board[r][c].getTile().getToken().setScored(true);
        for (int i=0; i<6; i++) {
            Hex spot = getAdjHex(r, c, i);
            if (spot!=null && !spot.getEmpty()) {
                if (spot.getTile().containsElk() && !spot.getTile().getToken().getScored()) {
                    cnt += 1 + elkRun(spot.getRow(), spot.getColumn());
                }
            }
        }
        return cnt;
    }


    public int runSize(int r, int c) {
        ArrayList<Hex> adjacentSalmons = new ArrayList<Hex>();
        board[r][c].getTile().getToken().setScored(true);
        for (int i=0; i<6; i++) {
            Hex spot = getAdjHex(r, c, i);
            if (spot!=null && !spot.getEmpty()) {
                if (spot.getTile().containsSalmon() && !spot.getTile().getToken().getScored()) {
                    adjacentSalmons.add(spot);
                }
            }


        }
        if (adjacentSalmons.isEmpty()) {
            return 0;
        } else {
            Hex hexagon = adjacentSalmons.get(0);
            return 1 + runSize(hexagon.getRow(), hexagon.getColumn());
        }
    }


    public boolean adjacentRun(int r, int c, int s) {
        Hex hexagon = board[r][c];
        int side = 0;
        int blockedSide = s + 3;
        if (blockedSide == 6) {
            blockedSide = 0;
        }
        if (s==0) {
            blockedSide = 3;
        }
        if (s==1) {
            blockedSide = 4;
        }
        if (s==2) {
            blockedSide = 5;
        }
        if (s==3) {
            blockedSide = 0;
        }
        if (s==4) {
            blockedSide = 1;
        }
        if (s==5) {
            blockedSide = 2;
        }
        ArrayList<Hex> adjacentSalmons = new ArrayList<Hex>();
        for (int i=0; i<6; i++) {
            if (i!=blockedSide) {
                Hex spot = getAdjHex(r, c, i);
                if (spot!=null && !spot.getEmpty()) {
                    if (spot.getTile().containsSalmon() && !spot.getTile().getToken().getScored()) {
                        side = i;
                        adjacentSalmons.add(spot);
                    }
                }
            }
        }
        int cnt = adjacentSalmons.size();
        if (cnt==1 || cnt==0) {
            if (cnt == 1) {
                Hex spot1 = adjacentSalmons.get(0);
                boolean one = adjacentRun(spot1.getRow(), spot1.getColumn(), side);
                return one;
            }
            if (cnt == 0) {
                return true;
            }
        }
        return false;
    }


    public int calculateBear() {
        int numPairs = 0;
        for (int r=0; r<42; r++) {
            for (int c=0; c<42; c++) {
                Hex h = board[r][c];
                if (!h.getEmpty() && h.getTile().containsBear() && !h.getTile().getToken().getScored()) {
                    boolean pair = pairBear(r, c);
                    if (pair) {
                        board[r][c].getTile().getToken().setScored(true);
                        numPairs++;
                    }
                }
            }
        }
        if (numPairs < 1) {
            return 0;
        } else if (numPairs < 2) {
            return 4;
        } else if (numPairs < 3) {
            return 11;
        } else if (numPairs < 4) {
            return 19;
        } else {
            return 27;
        }
    }


    public boolean pairBear(int row, int col) {
        ArrayList<Hex> adjacents = getAdjacents(row, col);
        int numAdjacentBears = 0;
        ArrayList<Hex> matches = new ArrayList<Hex>();
        for (Hex h: adjacents) {
            if (h!=null && !h.getEmpty()) {
                if (h.getTile().containsBear()) {
                    if (!h.getTile().getToken().getScored()) {
                        numAdjacentBears++;
                        matches.add(h);
                    }
                }
            }
        }
        if (numAdjacentBears==1) {
            Hex match = matches.get(0);
            ArrayList<Hex> matchAdjacents = getAdjacents(match.getRow(), match.getColumn());
            int numAdjacentToMatch = 0;
            for (Hex h: matchAdjacents) {
                if (h!=null && !h.getEmpty()) {
                    if (h.getTile().containsBear()) {
                        numAdjacentToMatch = numAdjacentToMatch + 1;
                    }
                }
            }
            if (numAdjacentToMatch==1) {


                return true;
            }


        }
        return false;
    }


    public int calculateMountain() {
        int max = 0;
        for (int r = 0; r < 42; r++) {
            for (int c = 0; c < 42; c++) {
                Hex h = board[r][c];
                if (!h.getEmpty() && !h.getTile().getMountainChecked()) {
                    if (h.getTile().getTotalTerrain1()==1 || h.getTile().getTotalTerrain2()==1) {
                        int size = 1 + mountainCorridor(r, c);
                        if (size > max) {
                            max = size;
                        }
                    }
                }
            }
        }
        return max;
    }


    public int calculateForest() {
        int max = 0;
        for (int r = 0; r < 42; r++) {
            for (int c = 0; c < 42; c++) {
                Hex h = board[r][c];
                if (!h.getEmpty() && !h.getTile().getForestChecked()) {
                    if (h.getTile().getTotalTerrain1()==2 || h.getTile().getTotalTerrain2()==2) {
                        int size = 1 + forestCorridor(r, c);
                        if (size > max) {
                            max = size;
                        }
                    }
                }
            }
        }
        return max;
    }


    public int calculatePrairie() {
        int max = 0;
        for (int r = 0; r < 42; r++) {
            for (int c = 0; c < 42; c++) {
                Hex h = board[r][c];
                if (!h.getEmpty() && !h.getTile().getPrairieChecked()) {
                    if (h.getTile().getTotalTerrain1()==3 || h.getTile().getTotalTerrain2()==3) {
                        int size = 1 + prairieCorridor(r, c);
                        if (size > max) {
                            max = size;
                        }
                    }
                }
            }
        }
        return max;
    }


    public int calculateWetland() {
        int max = 0;
        for (int r = 0; r < 42; r++) {
            for (int c = 0; c < 42; c++) {
                Hex h = board[r][c];
                if (!h.getEmpty() && !h.getTile().getWetlandChecked()) {
                    // checks if tile at [r][c] contains wetland terrain
                    if (h.getTile().getTotalTerrain1()==4 || h.getTile().getTotalTerrain2()==4) {
                        // calculates size of corridor
                        int size = 1 + wetlandCorridor(r, c);
                        if (size > max) {
                            max = size;
                        }
                    }
                }
            }
        }
        return max;
    }


    public int calculateRiver() {
        int max = 0;
        for (int r = 0; r < 42; r++) {
            for (int c = 0; c < 42; c++) {
                Hex h = board[r][c];
                if (!h.getEmpty() && !h.getTile().getRiverChecked()) {
                    if (h.getTile().getTotalTerrain1()==5 || h.getTile().getTotalTerrain2()==5) {
                        int size = 1 + riverCorridor(r, c);
                        if (size > max) {
                            max = size;
                        }
                    }
                }
            }
        }
        return max;
    }


    public int mountainCorridor(int r, int c) {
        board[r][c].getTile().setMountainChecked(true);
        int cnt = 0;
        for (int i=0; i<6; i++) {
            int x = i+3;
            if (i==0) {
                x = 3;
            }
            if (i==1) {
                x = 4;
            }
            if (i==2) {
                x = 5;
            }
            if (i==3) {
                x = 0;
            }
            if (i==4) {
                x = 1;
            }
            if (i==5) {
                x = 2;
            }
            Hex spot = getAdjHex(r, c, i);
            if (spot != null && !spot.getEmpty()) {
                if (spot.getTile().getTerrain(x)==1 && !spot.getTile().getMountainChecked()) {
                    cnt += 1 + mountainCorridor(spot.getRow(), spot.getColumn());
                }
            }
        }
        return cnt;
    }


    public int forestCorridor(int r, int c) {
        board[r][c].getTile().setForestChecked(true);
        int cnt = 0;
        for (int i=0; i<6; i++) {
            int x = i+3;
            if (i==0) {
                x = 3;
            }
            if (i==1) {
                x = 4;
            }
            if (i==2) {
                x = 5;
            }
            if (i==3) {
                x = 0;
            }
            if (i==4) {
                x = 1;
            }
            if (i==5) {
                x = 2;
            }
            Hex spot = getAdjHex(r, c, i);
            if (spot != null && !spot.getEmpty()) {
                if (spot.getTile().getTerrain(x)==2 && !spot.getTile().getForestChecked()) {
                    cnt += 1 + forestCorridor(spot.getRow(), spot.getColumn());
                }
            }
        }
        return cnt;
    }


    public int prairieCorridor(int r, int c) {
        board[r][c].getTile().setPrairieChecked(true);
        int cnt = 0;
        for (int i=0; i<6; i++) {
            int x = i+3;
            if (i==0) {
                x = 3;
            }
            if (i==1) {
                x = 4;
            }
            if (i==2) {
                x = 5;
            }
            if (i==3) {
                x = 0;
            }
            if (i==4) {
                x = 1;
            }
            if (i==5) {
                x = 2;
            }
            Hex spot = getAdjHex(r, c, i);
            if (spot != null && !spot.getEmpty()) {
                if (spot.getTile().getTerrain(x)==3 && !spot.getTile().getPrairieChecked()) {
                    cnt += 1 + prairieCorridor(spot.getRow(), spot.getColumn());
                }
            }
        }
        return cnt;
    }


    public int wetlandCorridor(int r, int c) {
        board[r][c].getTile().setWetlandChecked(true);
        int cnt = 0;
        for (int i=0; i<6; i++) {
            int x = i+3;
            if (i==0) {
                x = 3;
            }
            if (i==1) {
                x = 4;
            }
            if (i==2) {
                x = 5;
            }
            if (i==3) {
                x = 0;
            }
            if (i==4) {
                x = 1;
            }
            if (i==5) {
                x = 2;
            }
            Hex spot = getAdjHex(r, c, i);
            if (spot != null && !spot.getEmpty()) {
                if (spot.getTile().getTerrain(x)==4 && !spot.getTile().getWetlandChecked()) {
                    cnt += 1 + wetlandCorridor(spot.getRow(), spot.getColumn());
                }
            }
        }
        return cnt;
    }


    public int riverCorridor(int r, int c) {
        board[r][c].getTile().setRiverChecked(true);
        int cnt = 0;
        for (int i=0; i<6; i++) {
            int x = i+3;
            if (i==0) {
                x = 3;
            }
            if (i==1) {
                x = 4;
            }
            if (i==2) {
                x = 5;
            }
            if (i==3) {
                x = 0;
            }
            if (i==4) {
                x = 1;
            }
            if (i==5) {
                x = 2;
            }
            Hex spot = getAdjHex(r, c, i);
            if (spot != null && !spot.getEmpty()) {
                if (spot.getTile().getTerrain(x)==5 && !spot.getTile().getRiverChecked()) {
                    cnt += 1 + riverCorridor(spot.getRow(), spot.getColumn());
                }
            }
        }
        return cnt;
    }






    public int getNatureTokens() {
        return natureTokens;
    }

    public void updateButtonGrid() {

        buttonGrid = new Button[getHeight()+2][getLength()+2];
        System.out.println(buttonGrid.length);


        //in units of radiuses


        double heightSize = (getHeight()+2)*1.5+0.5;
        double widthSize = (getLength()+2)*(Math.sqrt(3)*2)+Math.sqrt(3);


        int radius = 1;


        if(heightSize>= widthSize){
            radius = (int) Math.round(Double.valueOf(size)/heightSize);
        }else{
            radius = (int) Math.round(Double.valueOf(size)/widthSize);
        }


        Button tempButton = new Button(0,0,radius*2);


        int x = xPos;
        int y = yPos;


        for(int r = 0; r < buttonGrid.length; r++){
            x = xPos;
            if((r+1)%2!=0){x+=tempButton.getWidth()/2;}


            for(int c = 0; c < buttonGrid[r].length; c++){

                System.out.println(c+","+r);


                buttonGrid[r][c] = new Button(x,y,radius*2);



                x+=tempButton.getWidth();
            }


            y+=radius*3/2;


        }

    }

    public Button[][] getButtonGrid(){return buttonGrid;}
    public void setDisplay(int x, int y, int s) {
        xPos = x;
        yPos = y;
        size = s;
    }


    public void disableAllButtons() {
        updateButtonGrid();
        for(Button[] row:buttonGrid) {
            for(Button b:row) {
                b.setDisabled(true);
            }
        }
    }

    public void enableOpenTileButtons() {

        disableAllButtons();

        for(int r = 0, y = getStartingY()-1; r < buttonGrid.length; r++, y++){
            for(int c = 0, x = getStartingX()-1; c < buttonGrid[r].length; c++, x++){


                if(x > -1 && x < board[0].length && y > -1 && y < board.length){



                    if(getOpen(y,x)) {
                        buttonGrid[r][c].setDisabled(false);
                    }


                }


            }
        }

    }

    public void enableOpenTokenButtons(AnimalToken token) {

        /*
         * 1: bear
         * 2: elk
         * 3: salmon
         * 4: hawks
         * 5: fox

         */
        disableAllButtons();
        for(int r = 0, y = getStartingY()-1; r < buttonGrid.length; r++, y++){
            for(int c = 0, x = getStartingX()-1; c < buttonGrid[r].length; c++, x++){


                if(x > -1 && x < board[0].length && y > -1 && y < board.length){

                    HabitatTile tile = board[y][x].getTile();

                    if(tile!=null&&tile.getToken()==null) {
                        switch(token.getAnimalType()) {
                            case 1:
                                if(tile.getCanHoldBear()) {buttonGrid[r][c].setDisabled(false);}
                            case 2:
                                if(tile.getCanHoldElk()) {buttonGrid[r][c].setDisabled(false);}
                            case 3:
                                if(tile.getCanHoldSalmon()) {buttonGrid[r][c].setDisabled(false);}
                            case 4:
                                if(tile.getCanHoldHawk()) {buttonGrid[r][c].setDisabled(false);}
                            case 5:
                                if(tile.getCanHoldFox()) {buttonGrid[r][c].setDisabled(false);}

                        }
                    }


                }


            }
        }

    }

    public void paint(Graphics g) {
        for(int r = 0, y = getStartingY()-1; r < buttonGrid.length; r++, y++){
            for(int c = 0, x = getStartingX()-1; c < buttonGrid[r].length; c++, x++){


                if(x > -1 && x < board[0].length && y > -1 && y < board.length){



                    buttonGrid[r][c].paint(g);
                    if(board[y][x].getTile() != null){
                        board[y][x].getTile().paint(g,buttonGrid[r][c]);
                    }


                }


            }
        }
    }


    public boolean addTileToClicked(HabitatTile tile, int clickX, int clickY){
        for(int r = 0; r < buttonGrid.length; r++){
            for(int c = 0; c < buttonGrid[r].length; c++){
                if(buttonGrid[r][c].mouseClicked(clickX,clickY) > 0){
                    return placeTile(r+getStartingY()-1,c+getStartingX()-1, tile);
                }
            }
        }




        return false;


    }




    public boolean addTokenToClicked(AnimalToken token, int clickX, int clickY){
        for(int r = 0; r < buttonGrid.length; r++){
            for(int c = 0; c < buttonGrid[r].length; c++){
                if(buttonGrid[r][c].mouseClicked(clickX,clickY) > 0){
                    boolean placed = placeAnimal(r+getStartingY()-1,c+getStartingX()-1, token);
                    System.out.println(placed);
                    return placed;




                }
            }
        }




        return false;




    }

    public void setNatureTokens(int t) {
        natureTokens = t;
    }

    public void mouseMoved(int clickX, int clickY){




        for(Button[] r:buttonGrid){


            for(Button b:r) {




                b.mouseMoved(clickX,clickY);
            }




        }
    }


}