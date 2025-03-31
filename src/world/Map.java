package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Stack;

import javax.imageio.ImageIO;

import entities.Entity;
import entities.ai.PlayerAI;
import entities.collision.CollisionBox;
import main.Game;
import tiles.Tile;

public class Map {
    public int[][] map;
    //Binary map is used to find places where entities can be generated. AND to check entity movement
    public boolean[][] binaryMap;
    public int width, height;
    public String biomeName;
    public int startX, startY, endX, endY;
    public static HashMap<String,Structure> structures = new HashMap<String,Structure>();
    private ArrayList<Rectangle> structureBounds;
    private boolean updating = false;
    private Room cave;
    public ArrayList<Structure> withEastConnection;
    public ArrayList<Structure> withWestConnection;
    public ArrayList<Structure> withNorthConnection;
    public ArrayList<Structure> withSouthConnection;

    public ArrayList<Room> rooms;
    public Map(String wn, int width, int height){
        this.width = width;
        this.height = height;
        generate(wn);
        populateWithEnemies(Biome.biomes.get(wn),cave);     
    }
    public Map(int length){
        this.width = 1000;
        this.height = 1000;
        map = new int[width][height];
        structureBounds = new ArrayList<Rectangle>();
        initMapTo(41);
        generateDungeon(length);
        generateWalls(5);
        generateBinaryMap();
        saveMapAsImage();
    }
    public Map(){}
    public static void loadStructures() throws Exception{
        File[] folderList = new File("res/maps/structures").listFiles();

        
        ArrayList<File> files = new ArrayList<File>();
        for(File f : folderList){
            if(!f.isDirectory()){
                continue;
            }
            for(File ff : f.listFiles()){
                if(ff.getName().endsWith("entities.csv")){
                    continue;
                }else{
                    files.add(ff);
                }
            }
        }


        for(File f : files){
            String fileName = f.getName();
            String name = fileName.substring(0,fileName.length()-4);
            // System.out.println(name);
            Structure s = new Structure(name);
            structures.put(name, s);
        }
    }
    public Room loadMap(String name){
        rooms = new ArrayList<Room>();
        
        Structure loadedStructure = new Structure(name);
        map = loadedStructure.tiles;
        width = map.length;
        height = map[0].length;
        Room r = new Room(0,0,loadedStructure);
        ArrayList<Entity> ent = generateStructureEntities(r.structure, r.x,r.y);
        
        for(Entity e : ent){
            r.addEntity(e);
            e.setHomeRoom(r);
        }


        generateBinaryMap();
        return r;
        
    }
    public void generate(String wn){
        structureBounds = new ArrayList<Rectangle>();
        rooms = new ArrayList<Room>();
        rooms.clear();
        cave = new Room(0,0,null);
        cave.bounds = new Rectangle(0,0,width*Tile.tileSize,height*Tile.tileSize);
        rooms.add(cave);

        System.out.println("Amount of rooms: "+rooms.size());
        
        switch (wn) {
            case "cave_spider":
                generateSpiderCave();        
                break;
            case "cave_goblin":
                generateGoblinCave();
                break;
            case "cave_skeleton":
                generateSkeletonCave();
                break;
            default:
                generateSpiderCave();
                break;
        }
        System.out.println("Amount of rooms after:"+ rooms.size());
        generateWalls(5);
        generateBinaryMap();
        saveMapAsImage();
    }
    public void saveMapAsImage(){
        System.out.println("Trying to save map");
        BufferedImage i = new BufferedImage(width,height,BufferedImage.TYPE_INT_ARGB);
        for(int y = 0; y < binaryMap[0].length;y++){
            for(int x = 0; x < binaryMap.length;x++){
                
                if(binaryMap[x][y]){
                    i.setRGB(x, y, new Color(0,0,0,0).getRGB());
                }else{
                    i.setRGB(x, y, new Color(0,0,0,255).getRGB());
                }
                
            }
        }
        try {
            File f =new File("res/screenshots/map.png");
            f.getParentFile().mkdirs();
            System.out.println("saving " + f.getAbsolutePath());
            ImageIO.write(i, "png",f);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void generateSpiderCave(){
        Biome b = Biome.biomes.get("cave_spider");
        basicGeneration(b);
        //BIOME SPECIFIC STRUCTURES THAT ONLY NEED TO SPAWN ONE TIME
        // generateStructureAtRandomSpot("lvl2_tunnel");
        // generateStructureAtRandomSpot("lvl2_tunnel");
        // generateStructureAtRandomSpot("trader_room_v1");
        
        
    }
    private void generateGoblinCave(){
        Biome b = Biome.biomes.get("cave_goblin");
        basicGeneration(b);

        //BIOME SPECIFIC STRUCTURES THAT ONLY NEED TO SPAWN ONE TIME
        generateStructureAtRandomSpot("trader_room_v1");
        generateStructureAtRandomSpot("boss_door");
    }
    private void generateSkeletonCave(){
        Biome b = Biome.biomes.get("cave_skeleton");
        basicGeneration(b);

        //BIOME SPECIFIC STRUCTURES THAT ONLY NEED TO SPAWN ONE TIME
        generateStructureAtRandomSpot("trader_room_v1");
        generateStructureAtRandomSpot("boss_door");
    }

    private void basicGeneration(Biome b){
        
        biomeName = b.name;
        Tile[] nonSolidTiles = b.nonSolidTiles;
        Tile[] solidTiles = b.solidTiles;
        Tile[] borderTiles = b.borderTiles;
        
        map = new int[width][height];
        
        //Neccesary
        initMapToZero();
        generateWalls(5);

        //Custom
        cellularAutomata(48,6);
        setNonSolidTiles(nonSolidTiles);
        setSolidTiles(solidTiles,borderTiles);
        // generateDungeon(5);
        
        //SPAWN
        Structure s1 = new Structure("spawn_area");
        int centerX = width/2-s1.width;
        int centerY = height/2-s1.height;
        generateStructure(s1, centerX, centerY);
        Room spawn = new Room(centerX,centerY, s1);
        ArrayList<Entity> ents = generateStructureEntities(spawn.structure,spawn.x,spawn.y);
        spawn.entities.addAll(ents);
        rooms.add(spawn);
        
        //Styling
        
        //Last steps.
        String[] structureList =  b.structures;
        int size = structureList.length;
        System.out.println("structure list size " + size);
        for(int i = 0; i < size; i++){
            for(int j = 0; j < 10; j++){
                generateStructureAtRandomSpot(structureList[i]);
            }
        }
    }

    private void generateDungeon(int length){
        rooms = new ArrayList<Room>();
        int centerX = map.length/2;
        int centerY = map[0].length/2;
        Structure s1 = structures.get("spawn_area");
        Stack<Room> roomStack = new Stack<Room>();
        //Spawn
        Room spawn = new Room(centerX, centerY,s1);
        generateStructure(spawn.structure, spawn.x,spawn.y);
        ArrayList<Entity> ents = generateStructureEntities(spawn.structure,spawn.x,spawn.y);
        for(Entity e : ents){
            if(e!=null){
                e.setHomeRoom(spawn);
            }
        }
        spawn.entities.addAll(ents);
        rooms.add(spawn);
        // World.entityManager.roomsToUpdate.add(spawn);
        roomStack.add(spawn);

        //Load rooms that have connections.
        String[] banned = {"spawn_area","exit","t_r1",
                            "d_room_15x15_up","d_room_15x15_down","d_room_15x15_left",
                            "d_room_15x15_right"};
        loadConnections("",banned);
        
        int min = 2;
        Random rand = new Random();
        int amount =min + rand.nextInt(length);
        PlayerAI.hud.roomCount = amount;
        Room lastRoom = spawn;
        for(int i = 0; i < amount; i++){
            if(i>0 && i%8==0){
                Room junction;
                while(true){
                    junction = generateRoom(roomStack,lastRoom, structures.get("d_room_15x15_all"),-1);
                    if(junction!=null){
                        break;
                    }
                    lastRoom = getRoomWithConnectionsFromStack(roomStack);
                }
                lastRoom = junction;
                int len = 1+rand.nextInt(10);
                for(int j = 0; j < len; j++){
                    lastRoom = generateRoom(roomStack, lastRoom, null,-1);
                }
                //Endpoint with key
                boolean success = generateEndPoint(roomStack,lastRoom);
                if(!success){
                    System.out.println("Spawning key");
                    //Spawn key in lastRoom.
                    Point p = getRandomSpotInRoom(lastRoom);
                    lastRoom.entities.add(World.entityManager.generateEntityWithID(58, -1, p.getX()*Tile.tileSize,p.getY()*Tile.tileSize));
                }


                lastRoom = junction;
                System.out.println("treasure");
                lastRoom = generateRoom(roomStack,lastRoom,structures.get("t_r1"),-1);
            }else{
                if(lastRoom==null){
                    lastRoom = getRoomWithConnectionsFromStack(roomStack);
                }
                lastRoom = generateRoom(roomStack,lastRoom,null,-1);
            }
        }
        generateExit(roomStack, lastRoom);
        
        // Room pipe1 = connectRoomEast(spawn, pipe_h);
        // generateStructure(pipe1.structure,pipe1.x,pipe1.y);

        
    }
    private boolean generateExit(Stack<Room> roomStack, Room lastRoom){
        while(true){
            boolean success = false;
            lastRoom = getRoomWithConnectionsFromStack(roomStack);
            ArrayList<Integer> connections = lastRoom.getConnections();
            for(int i : connections){
                Room f = connectRoom(lastRoom,structures.get("exit"), i);
                if(f!=null){
                    success = generateStructure(f.structure, f.x,f.y);
                    
                    if(success){
                        ArrayList<Entity> ents2 = generateStructureEntities(f.structure,f.x,f.y);
                        for(Entity e : ents2){
                            if(e!=null){
                                e.setHomeRoom(f);
                            }
                        }
                        f.entities.addAll(ents2);
                        rooms.add(f);
                        break;
                    }
                }
            }
            if(success){
                return success;
            }
        }
    }
    private boolean generateEndPoint(Stack<Room> roomStack, Room lastRoom){
        System.out.println("generating endpoint");
        ArrayList<Integer> sides = lastRoom.getConnections();
        System.out.println("available sides: " + sides);
        String structureName = "";
        boolean success = false;
        int side = -1;
        for(int i : sides){
            switch (i) {
                case 0:
                    //East
                    structureName = "d_room_15x15_left";
                    side = 0;
                    break;
                case 1:
                    //West
                    structureName = "d_room_15x15_right";
                    side = 1;
                    break;
                case 2:
                    //North
                    structureName = "d_room_15x15_down";
                    side = 2;
                    break;
                case 3:
                    //South
                    structureName = "d_room_15x15_up";
                    side = 3;
                    break;
                default:
                    break;
            }
            System.out.println(structureName);
            Room r = generateRoom(roomStack, lastRoom, structures.get(structureName),side);
            if(r!=null){
                success = true;
                break;
            }
        }
        return success;
    }
    private Room generateRoom(Stack<Room> roomStack, Room lastRoom, Structure force, int side){
        int tries = 0;
        boolean bool = false;
        Room b = null;
        int a = 0;
        ArrayList<Integer> choises = lastRoom.getConnections();
        while(!bool && tries<5){
            if(choises.size()==0){
                break;
            }
            Random r = new Random();
            int dir = r.nextInt(choises.size());
            int ans = choises.get(dir);
            if(side!=-1){
                ans = side;
            }
            if(force!=null){
                System.out.println("generating forced room");
                b = connectRoom(lastRoom, force, ans);
            }else{
                b = connectRandomRoom(lastRoom, ans);    
            }
            
            if(b!=null){
                bool = generateStructure(b.structure,b.x,b.y);
                if(bool){
                    a = 0;
                    ArrayList<Entity> ent = generateStructureEntities(b.structure, b.x,b.y);
                    for(Entity e : ent){
                        b.addEntity(e);
                        e.setHomeRoom(b);
                    }
                }
            }
            if(!bool){
                a++;
                if(a>=5){
                    a = 0;
                    choises.remove(dir);
                }
            }
            tries++;
        }
        //Room generated properly
        if(b!=null && bool){
            roomStack.add(b);
            lastRoom = b;
            rooms.add(lastRoom);
            if(lastRoom.structure.name.startsWith("d_")){
                spawnEnemies(lastRoom, World.playerLevel);
            }
        }else{
            //Room did not generate properly.
            //Trace back to previous rooms.
            //If available connections, try to continue there.
            //If failure, trace even further back.
            //If at spawn and fails, stop.
            if(force!=null){
                return null;
            }
            lastRoom = getRoomWithConnectionsFromStack(roomStack);
        }
        return lastRoom;
    
    }
    private void spawnEnemies(Room r, int lvl){
        Random random = new Random();
        int amount = random.nextInt(10);
        ArrayList<Point> points = new ArrayList<Point>();
        r.entities = new ArrayList<Entity>();
        int[] lvl1 = {15};
        int[] lvl5 = {15,49};
        int[] lvl10 = {50};


        Point randomPoint;
        for(int i = 0; i < amount; i++){
            while(true){
                randomPoint = getRandomSpotInRoom(r);
                boolean notFound = true;
                for(Point p : points){
                    if(p.x == randomPoint.x && p.y == randomPoint.y){
                        notFound = false;
                        break;
                    }
                }
                if(notFound){
                    points.add(randomPoint);
                    break;
                }
            }
            if(randomPoint!=null){
                int enemy = 2;
                if(lvl >=0 && lvl <5){
                    enemy = lvl1[random.nextInt(lvl1.length)];
                }else if(lvl >=5 && lvl <10){
                    enemy = lvl5[random.nextInt(lvl5.length)];
                }else if(lvl >=10){
                    enemy = lvl10[random.nextInt(lvl10.length)];
                }
                r.entities.add(World.entityManager.generateEntityWithID(enemy, -1, randomPoint.getX()*Tile.tileSize,randomPoint.getY()*Tile.tileSize));
            }
        }

        


    }
    private Point getRandomSpotInRoom(Room r){
        int width = r.structure.width;
        int height = r.structure.height;
        Random random = new Random();
        
        while(true){
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int tileID = r.structure.tiles[x][y];
            boolean solid = Tile.getTileByID(tileID).solid;

            if(!solid){
                return new Point(r.x+x,r.y+y);
            }
        }
    }
    private Room getRoomWithConnectionsFromStack(Stack<Room> stack){
        Room rr = null;
        while(stack.size()!=0){
            Room r = stack.pop();
            if(r.getConnections().size()>0){
                rr = r;
                break;
            }
        }
        return rr;

    }
    //Loads rooms that you want to be in dungeon. If prefix is not empty, player can only get for example lvl1_rooms or lvl2_rooms
    private void loadConnections(String prefix,String[] banned){
        withEastConnection = new ArrayList<Structure>();
        withWestConnection = new ArrayList<Structure>();
        withNorthConnection = new ArrayList<Structure>();
        withSouthConnection = new ArrayList<Structure>();
        
        for(Structure s : structures.values()){
            if(prefix!=""){
                if(!s.name.startsWith(prefix)){
                    continue;
                }
            }
            boolean foundBanned = false;
            for(String ban : banned){
                if(ban.equals(s.name)){
                    System.out.println("Skipping " + s.name);
                    foundBanned = true;
                    break;
                }
            }
            if(foundBanned){
                continue;
            }
            if(s.getEastConnectionPoint()!=null){
                withEastConnection.add(s);
            }
            if(s.getWestConnectionPoint()!=null){
                withWestConnection.add(s);
            }
            if(s.getSouthConnectionPoint()!=null){
                withSouthConnection.add(s);
            }
            if(s.getNorthConnectionPoint()!=null){
                withNorthConnection.add(s);
            }
        }
    }
    private Room connectRandomRoom(Room lastRoom, int side){
        switch (side) {
            case 0:
                return connectRoomEast(lastRoom, getRandomStructureFromList(withWestConnection));
            case 1:
                return connectRoomWest(lastRoom, getRandomStructureFromList(withEastConnection));
            case 2:
                return connectRoomNorth(lastRoom, getRandomStructureFromList(withSouthConnection));
            case 3:
                return connectRoomSouth(lastRoom, getRandomStructureFromList(withNorthConnection));
            default:
                return null;
        }
    }
    private Room connectRoom(Room lastRoom, Structure newRoom, int side){
        switch (side) {
            case 0:
                return connectRoomEast(lastRoom, newRoom);
            case 1:
                return connectRoomWest(lastRoom, newRoom);
            case 2:
                return connectRoomNorth(lastRoom, newRoom);
            case 3:
                return connectRoomSouth(lastRoom, newRoom);
            default:
                return null;
        }
    }
    private Structure getRandomStructureFromList(ArrayList<Structure> list){
        int i = list.size();
        Random r = new Random();
        int random = r.nextInt(i);
        Structure s = list.get(random);
        return s;
    }
    private Room connectRoomEast(Room a, Structure b){
        Point connectionA = a.structure.getEastConnectionPoint();

        Point connectionB = b.getWestConnectionPoint();
        if(connectionA!=null && connectionB !=null){

            int startX = (int)(a.x + connectionA.getX());
            int startY = (int)(a.y + connectionA.getY());

            int offsetX = (int)(startX+connectionB.getX()+1);
            int offsetY = (int)(startY-connectionB.getY());

            Room roomb = new Room(offsetX,offsetY,b);
            roomb.west = true;
            a.east = true;
            return roomb;
        }

        return null;
    }
    private Room connectRoomWest(Room a, Structure b){
        Point connectionA = a.structure.getWestConnectionPoint();

        Point connectionB = b.getEastConnectionPoint();
        if(connectionA!=null && connectionB !=null){

            int startX = (int)(a.x + connectionA.getX());
            int startY = (int)(a.y + connectionA.getY());

            int offsetX = (int)(startX-connectionB.getX()-1);
            int offsetY = (int)(startY-connectionB.getY());

            Room roomb = new Room(offsetX,offsetY,b);
            roomb.east = true;
            a.west = true;
            return roomb;
        }

        return null;
    }
    private Room connectRoomSouth(Room a, Structure b){
        Point connectionA = a.structure.getSouthConnectionPoint();

        Point connectionB = b.getNorthConnectionPoint();
        if(connectionA!=null && connectionB !=null){

            int startX = (int)(a.x + connectionA.getX());
            int startY = (int)(a.y + connectionA.getY());

            int offsetX = (int)(startX-connectionB.getX());
            int offsetY = (int)(startY+connectionB.getY()+1);

            Room roomb = new Room(offsetX,offsetY,b);
            roomb.north = true;
            a.south = true;
            return roomb;
        }

        return null;
    }
    private Room connectRoomNorth(Room a, Structure b){
        Point connectionA = a.structure.getNorthConnectionPoint();

        Point connectionB = b.getSouthConnectionPoint();
        if(connectionA!=null && connectionB !=null){

            int startX = (int)(a.x + connectionA.getX());
            int startY = (int)(a.y + connectionA.getY());

            int offsetX = (int)(startX-connectionB.getX());
            int offsetY = (int)(startY-connectionB.getY()-1);

            Room roomb = new Room(offsetX,offsetY,b);
            roomb.south = true;
            a.north = true;
            return roomb;
        }

        return null;
    }
    private void generateBinaryMap(){
        binaryMap = new boolean[width][height];
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                boolean solid = Tile.getTileByID(map[x][y]).solid;
                if(solid){
                    binaryMap[x][y] = false;
                } else{
                    binaryMap[x][y] = true;
                    
                }
            }
        }
    }

    public boolean checkIfSolid(int x, int y){
        if(x < 0 || y < 0 || x > binaryMap.length-1 || y > binaryMap[0].length-1){
            return false;
        }
        return !binaryMap[x][y];
    }
    //init everything to 0
    private void initMapToZero(){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                map[x][y] = 0;
            }
        }
    }
    private void initMapTo(int i){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                map[x][y] = i;
            }
        }
    }
    //Cellular Automata algorithm
    private void cellularAutomata(int percentage, int iterations){
        Random random = new Random();
        int WIDTH = map.length;
        int HEIGHT = map[0].length;
        // Randomly set initial walkable tiles
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if(random.nextInt(100)>percentage){
                    map[i][j] = 1; // 0 or 1 (non-walkable or walkable)
                }else{
                    map[i][j] = 0;
                }
                
            }
        }

        // Apply cellular automata rules for each iteration
        for (int iteration = 0; iteration < iterations; iteration++) {
            int[][] newMap = new int[WIDTH][HEIGHT];

            // Apply rules to each tile
            for (int i = 1; i < WIDTH - 1; i++) {
                for (int j = 1; j < HEIGHT - 1; j++) {
                    int neighbors = countWalkableNeighbors(map, i, j);

                    // Apply rules
                    if (neighbors > 4) {
                        newMap[i][j] = 1; // Set as walkable
                    } else {
                        newMap[i][j] = 0; // Set as non-walkable
                    }
                }
            }

            // Update the map with the new values
            map = newMap;
        }
    }

    private static int countWalkableNeighbors(int[][] map, int x, int y) {
        int count = 0;

        // Check the eight neighbors
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                count += map[x + i][y + j];
            }
        }

        return count;
    }

    //change every 1 on map to -> something in nonSolidTiles
    private void setNonSolidTiles(Tile[] nonSolidTiles){
        Random r = new Random();
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(map[x][y]!=0){
                    int rr = r.nextInt(nonSolidTiles.length);
                    int id = nonSolidTiles[rr].id;
                    map[x][y] = id;
                }
            }
        }
    }
    //change every 0 on map to -> something solid.
    private void setSolidTiles(Tile[] solidTiles, Tile[] borderTiles){
        int w = map.length;
        int h = map[0].length;
        int[][] newMap = new int[w][h];
        
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                newMap[x][y] = map[x][y];
            }
        }
        Random r = new Random();
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(map[x][y]==0){
                    if(checkIfSurrounded(x, y)){
                        newMap[x][y] = solidTiles[0].id;
                    }else{
                        int rr = r.nextInt(borderTiles.length);
                        int id = borderTiles[rr].id;
                        newMap[x][y] = id;
                    }
                }
            }
        }
        map = newMap;
    }
    private boolean checkIfSurrounded(int x, int y){
        if(x < 2 || x >= map.length-1 || y < 2 ||y >= map[0].length-1){
            
            return true;
        }else{
            int count = 0;
            int west = map[x-1][y];
            int north = map[x][y-1];
            int east = map[x+1][y];
            int south = map[x][y+1];
            if(west == 0){
                count++;
            }
            if(north == 0){
                count++;
            }
            if(east == 0){
                count++;
            }
            if(south == 0){
                count++;
            }
            if(count==4){
                return true;
            }
        }
        return false;
    }
    
    private ArrayList<Entity> generateStructureEntities(Structure s, int x, int y){
        ArrayList<Entity> entities = new ArrayList<Entity>();
        for(StructureEntity e : s.entities){
            Entity ent = World.entityManager.generateEntityWithID(e.id,-1, (e.x + x)*Tile.tileSize, (e.y+y)*Tile.tileSize);
            entities.add(ent);
        }
        return entities;
    }
    
    //Generate border
    private void generateWalls(int id){
        for(int y = 0; y < map[0].length; y++){
            for(int x = 0; x < map.length; x++){
                if(x <=2||x >= map.length-3 || y <= 2|| y >= map[0].length-3){
                    map[x][y] = id;
                }

            }
        }
    }
    public void populateWithEnemies(Biome biome, Room r){
        if(biome==null){
            return;
        }
        int[] entities = biome.entities;
        int[] collectables = biome.collectables;
        for(int y = 0; y < binaryMap[0].length; y++){
            for(int x = 0; x < binaryMap.length; x++){
                if(binaryMap[x][y]){
                    //IF cannot spawn entity, try to spawn collectable.
                    if(!generateRandomEntity(entities, x*Tile.tileSize, y*Tile.tileSize, r)){
                        generateRandomEntity(collectables, x*Tile.tileSize, y*Tile.tileSize,r);
                    }
                }
            }
        }
    }
    
    public boolean generateRandomEntity(int[] entities, int x, int y, Room room){
        Random r = new Random();
        int d = r.nextInt(100);
        //change to spawn enemy.
        if(d <=3){
            int d2 = r.nextInt(entities.length);
            Entity e = World.entityManager.spawnEntity(entities[d2],-1, x, y);
            if(e.homeRoom==null){
                room.addEntity(e);
            }
            return true;
        }
        return false;
    }

    private void generateStructureAtRandomSpot(String name){
        boolean b = false;
        int rx = 0;
        int ry = 0;
        Random r = new Random();
        int retrys = 4;
        int counter = 0;
        Structure s = structures.get(name);
        while(!b){
            rx = 25 + r.nextInt(map.length-s.width);
            ry = 25 + r.nextInt(map[0].length-s.height);
            // System.out.println("Random Spot: " + rx + "," + ry);
            b = generateStructure(s,rx, ry);
            if(b){
                Room room = new Room(rx,ry,s);
                ArrayList<Entity> ents = generateStructureEntities(s, rx, ry);
                if(s.name.startsWith("d_")){
                    spawnEnemies(room, 1);
                }
                room.entities.addAll(ents);
                rooms.add(room);
            }
            counter++;
            if(counter>=retrys){
                // System.out.println("Failed to generate Strucure: " + name);
                break;
            }
        }
    }
    
    public boolean generateStructure(Structure s, int x, int y){
        if(s==null){
            return false;
        }
        int w = s.width;
        int h = s.height;
        int tiles[][] = s.tiles;
        if(x+w > map.length-2 || y+h >map[0].length-2 || x < 2 || y < 2){
            return false;
        }
        
        Rectangle r = new Rectangle(x,y,w,h);
        for(Rectangle r2 : structureBounds){
            if(r2.intersects(r)){
                System.out.println(r2.x + " " + r2.y + " " + r2.width + " " + r2.height);
                // System.out.println("structurebounds intersection");
                return false;
            }
        }
        for(int j = 0; j < h; j++){
            for(int i = 0; i < w; i++){
                map[x+i][y+j] = tiles[i][j];
            }
        }
        structureBounds.add(r);
        return true;
    }
    //Can there be added some kind of rayCasting to set viewable area?
    public void updateVisible(Entity e){
        updating = true;
        int centerX = (int)(e.x/Tile.tileSize);
        int centerY = (int)(e.y/Tile.tileSize);
        // System.out.println("Updatevisible: " + centerX + " " + centerY);
        int screenWidth = Game.w.getWidth();
        int screenHeight = Game.w.getHeight();
        int wTiles = screenWidth/Tile.tileSize;
        int hTiles = screenHeight/Tile.tileSize;
        int buffer = 6;
        // System.out.println("UpdateVisible");
        // System.out.println("startX " +startX);
        // System.out.println("startY " +startY);
        startX = (centerX - wTiles/2)-buffer;
        startY = (centerY - hTiles/2)-buffer;

        if(startX <= 0){
            startX = 0;
        }
        if(startY <= 0){
            startY = 0;
        }
        endX = startX + wTiles + buffer*2;
        endY = startY + hTiles+ buffer*2;
        if(endX > map.length){
            endX = map.length;
        }
        if(endY > map[0].length){
            endY = map[0].length;
        }
        ArrayList<CollisionBox> boxes = new ArrayList<CollisionBox>();
        //Generate Collision Rectangles
        for(int y = startY; y < endY; y++){
            for(int x = startX; x < endX; x++){
                Rectangle r = new Rectangle(x*Tile.tileSize, y*Tile.tileSize,Tile.tileSize,Tile.tileSize);
                boolean solid = !binaryMap[x][y];
                CollisionBox b = new CollisionBox(null,r,solid);
                if(solid)
                    boxes.add(b);
            }
        }
        World.tileCollisionBoxes = boxes;

        // System.out.println("endX " +endX);
        // System.out.println("endY " +endY);
        updating = false;
    }
    public void render(Graphics g){
        
        if(startX>map.length-1){
            startX = 0;
        }
        if(startY>map[0].length-1){
            startY = 0;
        }
        //avoid outOfBounds exception
        if(startX < 0){
            startX = 0;
        }
        if(endX >= map.length){
            endX = map.length;
        }
        if(startY < 0){
            startY = 0;
        }
        if(endY >= map[0].length){
            endY = map[0].length;
        }
        double xOffset = World.camera.getXOffset();
        double yOffset = World.camera.getYOffset();
        for(int y = startY; y < endY; y++){
            for(int x = startX; x < endX; x++){
                if(updating){
                    break;
                }
                if(!World.ready){
                    break;
                }
                if(x > map.length){
                    break;
                }
                if(y > map[0].length){
                    break;
                }
                // System.out.println("In Forloop: " + x + " " + y + "  StartX and Y " + startX + " " + startY);
                
                g.drawImage(Tile.getTileByID(map[x][y]).texture, (int)((x*Tile.tileSize) - xOffset), (int)((y*Tile.tileSize) - yOffset),Tile.tileSize,Tile.tileSize, null);
            }
        }
    }
}
