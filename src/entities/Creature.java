package entities;

import tiles.Tile;
import world.World;

public abstract class Creature extends Entity{
    public int health;
    public double xSpeed, ySpeed;
    public double speed;
    public Creature(double x, double y) {
        super(x, y);
    }
    public void move(){
        
        //Can you move?
        //are you frozen?
        //Collision?
        
        // -- Set correct animation
        //east
        if(xSpeed >0){
            if(ySpeed ==0){
                setAnimation(info.animations.get("walk_east"));
            }else{
                if(ySpeed>0){
                    setAnimation(info.animations.get("walk_south_east"));
                    
                }else{
                    setAnimation(info.animations.get("walk_north_east"));
                }
            }
        }
        //west
        if(xSpeed <0){
            if(ySpeed ==0){
                setAnimation(info.animations.get("walk_west"));
            }else{
                if(ySpeed>0){
                    setAnimation(info.animations.get("walk_south_west"));
                    
                }else{
                    setAnimation(info.animations.get("walk_north_west"));
                }
            }
        }
        //south
        if(ySpeed >0){
            if(xSpeed ==0){
                setAnimation(info.animations.get("walk_south"));
            }
        }
        
        //north
        if(ySpeed <0){
            if(xSpeed ==0){
                setAnimation(info.animations.get("walk_north"));
            }
        }
        

        moveX();
        moveY();
        if(xSpeed == 0 && ySpeed == 0){
            setAnimation(info.animations.get("idle"));
        }
        
    }
    private void moveX(){
        boolean canMove = true;
        
        if(xSpeed <0){
            //System.out.println("Moving left");
            canMove = checkTilesX();
            
        }else if(xSpeed >0){
            //System.out.println("Moving right");
            canMove = checkTilesX();
        }
        
        
        if(canMove){
            x += xSpeed;
        }
        
    }
    private void moveY(){
        boolean canMove = true;
        if(ySpeed <0){
            //System.out.println("Moving up");
            canMove = checkTilesY();
            
        }else if(ySpeed >0){
            //System.out.println("Moving down");
            canMove = checkTilesY();
        }


        if(canMove){
            y+= ySpeed;
        }

    }
    private boolean checkTilesX(){
        int currentTileX = (int)(bounds.x/Tile.tileSize);
        int currentTileY = (int)(bounds.y/Tile.tileSize);

        if(xSpeed > 0){
            currentTileX = (int)((bounds.x + bounds.width)/Tile.tileSize);
        }

        int currentTileY2 = (int)((bounds.y+bounds.height)/Tile.tileSize);
        int nextTileX = (int)((bounds.x+xSpeed)/Tile.tileSize);
        if(xSpeed > 0){
            nextTileX = (int)((bounds.x+bounds.width+xSpeed)/Tile.tileSize);
        }
        //Out of bounds check
        if(nextTileX < 0 || nextTileX > World.map.binaryMap.length||currentTileY < 0||currentTileY > World.map.binaryMap[0].length||currentTileY2 < 0||currentTileY2 > World.map.binaryMap[0].length){
            
            return false;
            
        }
        boolean currentSolid = World.map.binaryMap[currentTileX][currentTileY];
        boolean currentSolid2 = World.map.binaryMap[currentTileX][currentTileY2];
        if(!currentSolid&&!currentSolid2){
            return false;
        }
        
        boolean nextSolid = World.map.binaryMap[nextTileX][currentTileY];
        boolean nextSolid2 = World.map.binaryMap[nextTileX][currentTileY2];
        if(!nextSolid||!nextSolid2){
            if(xSpeed < 0){
                int pos = nextTileX*Tile.tileSize + Tile.tileSize+1;
                x = pos;
                
            }else{
                int pos = nextTileX*Tile.tileSize -bounds.width-1;
                x = pos;
            }
            return false;
        }else{
            return true;
        }
    }
    
    private boolean checkTilesY(){
        int currentTileX = (int)(bounds.x/Tile.tileSize);
        int currentTileY = (int)(bounds.y/Tile.tileSize);

        if(ySpeed > 0){
            currentTileY = (int)((bounds.y + bounds.height)/Tile.tileSize);
        }

        int currentTileX2 = (int)((bounds.x+bounds.width)/Tile.tileSize);
        int nextTileY = (int)((bounds.y+ySpeed)/Tile.tileSize);
        if(ySpeed > 0){
            nextTileY = (int)((bounds.y+bounds.height+ySpeed)/Tile.tileSize);
        }
        //Out of bounds check
        if(nextTileY < 0 || nextTileY > World.map.binaryMap[0].length||currentTileY < 0||currentTileY > World.map.binaryMap[0].length||currentTileX2 < 0||currentTileX2 > World.map.binaryMap.length){
            
            return false;
            
        }
        boolean currentSolid = World.map.binaryMap[currentTileX][currentTileY];
        boolean currentSolid2 = World.map.binaryMap[currentTileX2][currentTileY];
        if(!currentSolid&&!currentSolid2){
            return false;
        }
        
        boolean nextSolid = World.map.binaryMap[currentTileX][nextTileY];
        boolean nextSolid2 = World.map.binaryMap[currentTileX2][nextTileY];
        if(!nextSolid||!nextSolid2){
            if(ySpeed < 0){
                int pos = nextTileY*Tile.tileSize + Tile.tileSize+1;
                y = pos;
                
            }else{
                int pos = nextTileY*Tile.tileSize -bounds.height-1;
                y = pos;
            }
            return false;
        }else{
            return true;
        }
    }
}
