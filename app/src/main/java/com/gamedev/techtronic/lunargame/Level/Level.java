package com.gamedev.techtronic.lunargame.Level;

import android.content.Context;
import android.graphics.Bitmap;

import com.gamedev.techtronic.lunargame.AI.Graph;
import com.gamedev.techtronic.lunargame.Level.Entities.MovingPlatform;
import com.gamedev.techtronic.lunargame.Level.Entities.ScorePickUp;
import com.gamedev.techtronic.lunargame.Level.Triggers.Trigger;
import com.gamedev.techtronic.lunargame.gage.engine.ElapsedTime;
import com.gamedev.techtronic.lunargame.gage.engine.graphics.IGraphics2D;
import com.gamedev.techtronic.lunargame.gage.engine.io.FileIO;
import com.gamedev.techtronic.lunargame.gage.util.BoundingBox;
import com.gamedev.techtronic.lunargame.gage.util.Vector2;
import com.gamedev.techtronic.lunargame.gage.world.GameObject;
import com.gamedev.techtronic.lunargame.gage.world.GameScreen;
import com.gamedev.techtronic.lunargame.gage.world.LayerViewport;
import com.gamedev.techtronic.lunargame.gage.world.ScreenViewport;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Level extends GameObject{

    private GameObject background;

    private int widthInTiles;
    private int heightInTiles;

    private ArrayList<String> tempLevelTileStringList;
    private DocumentBuilderFactory factory;
    private DocumentBuilder builder;
    private Document doc;
    private XPath xPath;
    private InputStreamReader inputStreamReader;
    private BufferedReader bufferedReader;
    private String levelFilePath;
    private FileIO f;


    // Both levelTileArray and levelTileList contain the same tiles
    // but in different structures for convenience
    private Tile[][] levelTileArray;
    private ArrayList<Tile> levelTileList = new ArrayList<>();

    private Graph graph;

    private ArrayList<Tile> collidableTileList = new ArrayList<>();
    private ArrayList<MovingPlatform> movingPlatformList = new ArrayList<>();
    private ArrayList<ScorePickUp> pickUpList = new ArrayList<>();
    private ArrayList<Trigger> triggerList = new ArrayList<>();

    private Vector2 playerSpawnPosition;


    // TODO: CE: Improve code reusability, lots of redundancy in xml parsing and reading various data into game

    public Level(float x, float y, Bitmap bitmap, GameScreen gameScreen) {
        super(x, y, bitmap, gameScreen);
    }


    public void loadLevel(String levelID, Context context) {
        levelFilePath = "";

        f = new FileIO(context);

        String s= "";

        tempLevelTileStringList = new ArrayList<>();

        try {

            loadLevelFilePath(levelID);

            String expression;
            NodeList nodeList;


            inputStreamReader = new InputStreamReader(f.readAsset(levelFilePath));
            bufferedReader = new BufferedReader(inputStreamReader);

//            readTilesIntoArray();


            loadPlayerSpawn();

            loadMovingPlatforms();

            loadPickUps();

//            readTriggers();
            loadLevelTiles();


            bufferedReader.close();
            inputStreamReader.close();

        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        background = new GameObject(0,0,mGameScreen.getGame().getAssetManager().getBitmap("gameBackground"),mGameScreen);

        int x=0;
        int y=0;

        levelTileArray = new Tile[widthInTiles][heightInTiles];

        for (int i = 0; i < levelTileList.size(); i++) {
            levelTileArray[x][y] = levelTileList.get(i);
            x++;
            if (x>=widthInTiles){
                y++;
                x=0;
            }
        }

        // Generate graph (for use in pathfinding)
        graph = new Graph(levelTileArray);

    }

    private void loadLevelTiles() throws SAXException, IOException, XPathExpressionException {
        String expression;
        NodeList nodeList;
        String s;
        doc = builder.parse(f.readAsset("xml/tileData"));
        expression = "/Tiles/Tile";
        nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

        // x is offset a little to fix alignment
        // Hard coded value alert! AWOOGA!
        // TODO: CE: Allow for tiles of various sizes automatically
        float x=15.5f;
        float y=0;

        AnimatedTile animTile;
        Tile tile;

        readTilesIntoArray();

        // First read the level from the bottom to top, line by line, left to right
        // Read each character, and add the corresponding tile (data gotten from xml) to levelTileList
        for (int i = tempLevelTileStringList.size()-1; i >= 0; i--) {

            s = tempLevelTileStringList.get(i);

            for (int j = 0; j < s.length(); j++) {

                for (int k = 0; k < nodeList.getLength(); k++) {

                    Node nNode = nodeList.item(k);

                    if (((Element) nNode).getAttribute("char").equalsIgnoreCase(String.valueOf(s.charAt(j)))) {
                        if (Boolean.parseBoolean(((Element) nNode).getAttribute("isAnimated"))){

                            animTile = new AnimatedTile(x, y, mGameScreen.getGame().getAssetManager().getBitmap("tileSheet"), mGameScreen,
                                    ((Element) nNode).getAttribute("name"),
                                    ((Element) nNode).getAttribute("char").charAt(0),
                                    Integer.parseInt(((Element) nNode).getAttribute("startX")),
                                    Integer.parseInt(((Element) nNode).getAttribute("startY")),
                                    Integer.parseInt(((Element) nNode).getAttribute("tileWidth")),
                                    Integer.parseInt(((Element) nNode).getAttribute("tileHeight")),
                                    Boolean.parseBoolean(((Element) nNode).getAttribute("hasCollision")),
                                    Boolean.parseBoolean(((Element) nNode).getAttribute("isAnimated")),
                                    Integer.parseInt(((Element) nNode).getAttribute("frameCount")),
                                    Integer.parseInt(((Element) nNode).getAttribute("frameRate")),
                                    Boolean.parseBoolean(((Element) nNode).getAttribute("isDamaging")));

                            levelTileList.add(animTile);

                            if (animTile.isCollidable()){
                                collidableTileList.add(animTile);
                            }
                        }else {
                            tile = new Tile(x, y, mGameScreen.getGame().getAssetManager().getBitmap("tileSheet"), mGameScreen,
                                    ((Element) nNode).getAttribute("name"),
                                    ((Element) nNode).getAttribute("char").charAt(0),
                                    Integer.parseInt(((Element) nNode).getAttribute("startX")),
                                    Integer.parseInt(((Element) nNode).getAttribute("startY")),
                                    Integer.parseInt(((Element) nNode).getAttribute("tileWidth")),
                                    Integer.parseInt(((Element) nNode).getAttribute("tileHeight")),
                                    Boolean.parseBoolean(((Element) nNode).getAttribute("hasCollision")),
                                    Boolean.parseBoolean(((Element) nNode).getAttribute("isAnimated")),
                                    Boolean.parseBoolean(((Element) nNode).getAttribute("isDamaging")));
                            levelTileList.add(tile);

                            if (tile.isCollidable()){
                                collidableTileList.add(tile);
                            }
                        }
                        // positional stuff
                        // Hard coded value! Fix this
                        // Some values are decremented by 0.5, reason: small pixel gaps line between tiles
                        // not sure why it happens but this fixes it by moving them together a little bit (They don't seem to overlap).
                        // If someone knows why this happens I'd like to know, these hardcoded values are gonna be dodgy later down the road. (Future CE: This is causing issues with AI pathfinding, I knew it..)
                        // Also i+1 because: (0 % Integer) = 0
                        if ((j+1) % widthInTiles ==0){
                            y+=15.5f;
                            x=15.5f;
                        }else {
                            x+=15.5f;
                        }
                    }

                }

            }
        }
    }

    private void loadLevelFilePath(String levelID) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        // Start of level xml stuff
        factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();

        doc = builder.parse(f.readAsset("xml/levels"));

        xPath = XPathFactory.newInstance().newXPath();

        String expression = "/Levels/Level";
        NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

        // Get location of level data from xml file
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node nNode = nodeList.item(i);
            if (((Element) nNode).getAttribute("id").equalsIgnoreCase(String.valueOf(levelID))) {
                levelFilePath = ((Element) nNode).getAttribute("tileData");
                break;
            }
        }
    }

    private void loadPickUps() throws IOException {
        String[] stringSplitArray;
        for (String pickUpsString: readEntity("Pickups")){
            stringSplitArray = pickUpsString.split(",");
            pickUpList.add(new ScorePickUp(Float.valueOf(stringSplitArray[0]), Float.valueOf(stringSplitArray[1]), this.mGameScreen.getGame().getAssetManager().getBitmap("miscSheet"),mGameScreen, stringSplitArray[2], true));
        }
    }

    private void loadMovingPlatforms() throws IOException, SAXException, XPathExpressionException {
        String[] stringSplitArray;
        String expression;
        NodeList nodeList;//            read MovingPlatforms
        // a couple of local variables for use in constructing the moving platform
        ArrayList<Vector2> positionList = new ArrayList<>();
        int startX=0;
        int startY=0;
        int spriteWidth=1;
        int spriteHeight=1;

        for (String movingPlatString: readEntity("MovingPlatforms")){
            positionList.clear();
            stringSplitArray = movingPlatString.split(",");
            doc = builder.parse(f.readAsset("xml/platformSpriteData"));

            xPath = XPathFactory.newInstance().newXPath();

            expression = "/Platforms/Platform";
            nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);

            // Get location of platform data from xml file
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);

                if (((Element) nNode).getAttribute("name").equalsIgnoreCase(String.valueOf(stringSplitArray[0]))) {

                    startX = Integer.valueOf(((Element) nNode).getAttribute("startX"));
                    startY = Integer.valueOf(((Element) nNode).getAttribute("startY"));
                    spriteWidth = Integer.valueOf(((Element) nNode).getAttribute("spriteWidth"));
                    spriteHeight = Integer.valueOf(((Element) nNode).getAttribute("spriteHeight"));

                    break;
                }
            }

            // Move through the string array in 2's, important because it is reading values for a
            // Vector2 object which takes 2 values.
            // Each line represents a new MovingPlatform object
            // e.g. level file follows this format for movingPlatform:
            // @MovingPlatform
            // Name,XPosition1,YPosition1,XPosition2,YPosition2, etc...
            // Name,XPosition1,YPosition1,XPosition2,YPosition2, etc...
            // Etc...
            for (int i = 1; i < stringSplitArray.length; i+=2) {
                positionList.add(new Vector2(Float.valueOf(stringSplitArray[i]), Float.valueOf(stringSplitArray[i+1])));
            }

            movingPlatformList.add(new MovingPlatform(positionList, this.mGameScreen.getGame().getAssetManager().getBitmap("miscSheet"), this.mGameScreen, startX, startY, spriteWidth, spriteHeight));
        }
    }

    private void loadPlayerSpawn() throws IOException {
        String[] stringSplitArray;
        for (String spawnPosString: readEntity("PlayerSpawn")){
            stringSplitArray = spawnPosString.split(",");
            playerSpawnPosition = new Vector2(Integer.valueOf(stringSplitArray[0]), Integer.valueOf(stringSplitArray[1]));
        }
    }

    private void readTilesIntoArray()throws IOException{

        String s;
        // Close all readers
        bufferedReader.close();
        inputStreamReader.close();

        // Reinstantiate readers
        inputStreamReader = new InputStreamReader(f.readAsset(levelFilePath));
        bufferedReader = new BufferedReader(inputStreamReader);

        // Load level data from file into tempLevelTileStringList

        while (bufferedReader.ready()) {

            // read 1 line at a time, add to tempLevelTileStringList for additional processing

            s= bufferedReader.readLine();

            if (s.contains("@")){
                // Once the reader reaches this character you're done reading tiles
                break;
            }
            tempLevelTileStringList.add(s);
        }

        // Get the width/height tile number implicitly from the number of characters per line
        widthInTiles = tempLevelTileStringList.get(0).length();
        heightInTiles = tempLevelTileStringList.size();
    }

    /*
     * General reusable code fragment for reading entities from the level file.
     *
     * @param entityName e.g. playerSpawn, pickups, enemies. See level files.
     */
    private ArrayList<String> readEntity(String entityName) throws IOException {
        String s;
        Boolean isTriggerLinesReached=false;
        ArrayList<String> stringSplitArray = new ArrayList<>();

        // Close all readers
        bufferedReader.close();
        inputStreamReader.close();

        // Reinstantiate readers
        inputStreamReader = new InputStreamReader(f.readAsset(levelFilePath));
        bufferedReader = new BufferedReader(inputStreamReader);

        while (bufferedReader.ready()){

            s = bufferedReader.readLine();

            if (isTriggerLinesReached){
                if (s.contains("@")){
                    // if you reach another @ line, you're done
                    break;
                }

                // Add the lines we're looking for, related to entityName
                stringSplitArray.add(s);
            }

            // Once the line containing String entityName is found, the boolean isLinesReached becomes true,
            // and the following lines are parsed until a line contains an @ symbol.
            if (s.contains(entityName)){
                isTriggerLinesReached = true;
            }
        }
        return stringSplitArray;
    }
    
    /**
     * Returns the list of all tiles that make up the level, including empty tiles.
     * @return
     */
    public ArrayList<Tile> getLevelTileList(){
        return levelTileList;
    }

    public Tile[][] getLevelTileArray() {
        return levelTileArray;
    }

    /**
     * Returns the list of only the tiles that have collision.
     * @return
     */
    public ArrayList<Tile> getCollidableTileList(){return collidableTileList;}


    /**
     * Return the list of all moving platforms.
     * @return
     */
    public ArrayList<MovingPlatform> getMovingPlatformList() {
        return movingPlatformList;
    }

    /**
     * Return the list of all pickUps
     * @return
     */
    public ArrayList<ScorePickUp> getPickUpList() {
        return pickUpList;
    }

    /**
     * Tiles the specified background image over the area of the level.
     * This is a placeholder until a good parallax/animated background class is made.
     * @param elapsedTime
     * @param graphics2D
     * @param layerViewport
     * @param screenViewport
     */
    private void drawBackground(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport){

        // Fill the level background with the specified background tile
        // PlaceHolder until a good class with parallax and animation and all that good stuff is made.
        for (int x = 0; x < widthInTiles *16; x+=background.getBitmap().getWidth()) {
            for (int y= 0; y < 1000; y+= background.getBitmap().getHeight()) {
                background.setPosition((float) x, (float) y);
                background.draw(elapsedTime,graphics2D, layerViewport, screenViewport);
            }
        }
    }

    @Override
    public void setPosition(float x, float y) {/* Do nothing */}

    @Override
    public Bitmap getBitmap() {/* Do nothing */ return null;}

    @Override
    public BoundingBox getBound() {/* Do nothing */ return null;}

    public Vector2 getPlayerSpawnPosition() {return playerSpawnPosition;}

    public Graph getGraph() {return graph;}

    @Override
    public void update(ElapsedTime elapsedTime) {
        // TODO: CE: Make animated tile updates more efficient! (maybe make another arraylist for animatedTiles)
        for (Tile tile: collidableTileList) {
            tile.update(elapsedTime);
        }
        for (MovingPlatform movingPlatform: movingPlatformList){
            movingPlatform.update(elapsedTime);
        }
        for (ScorePickUp pickUp: pickUpList){
            pickUp.update(elapsedTime);
        }
    }

    @Override
    public void draw(ElapsedTime elapsedTime, IGraphics2D graphics2D, LayerViewport layerViewport, ScreenViewport screenViewport) {

        // Draw the background
        drawBackground(elapsedTime,graphics2D, layerViewport, screenViewport);

        // Draw all visible tiles to screen
        for (Tile tile: levelTileList) {
            tile.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
        }

        for (MovingPlatform movingPlatform: movingPlatformList){
            movingPlatform.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
        }

        for (ScorePickUp pickUp: pickUpList){
            pickUp.draw(elapsedTime, graphics2D, layerViewport, screenViewport);
        }

    }
}

