package graphics;

import logic.SinglePlayer.Player;
import logic.gameElements.Bird;
import logic.gameElements.Coin;
import logic.gameElements.Heart;
import logic.gameElements.Pipe;
import org.newdawn.slick.*;

import static logic.gameConstants.GameConstants.*;


public class SpriteDrawer{
    private Image birdImage;
    private Image coinImage;
    private Image heartImage;
    private Image lowerPipeImage;
    private Image upperPipeImage;
    private Image backgroundSingle;
    private SpriteSheet coinSheet;
    private Animation coinAnimation;
    private SpriteSheet birdSheet;
    private Animation birdAnimation;
    private SpriteSheet heartSheet;
    private Animation heartAnimation;
    private int screenWidth;
    private int screenHeight;
    private int offsetX;
    private int offsetY;
    private int lives;
    private int lifeBias;

    public SpriteDrawer(Screen screen){
        this.screenWidth = screen.getWidth();
        this.screenHeight = screen.getHeight();
        this.offsetX = screen.getOffsetX();
        this.offsetY = screen.getOffsetY();
        try {
            backgroundSingle = new Image("res/cimitero.png").getScaledCopy(screenWidth,screenHeight);
            birdImage = new Image("res/bird.png").getScaledCopy((int)(BIRD_WIDTH*screenWidth), (int)(BIRD_HEIGHT*screenHeight));
            heartImage = new Image("res/heart_full.png").getScaledCopy((int) (HEART_SIZE*screenWidth), (int)(HEART_SIZE*screenHeight));
            coinImage = new Image("res/onecoin.png").getScaledCopy((int)(COIN_SIZE*screenWidth), (int)(COIN_SIZE*screenHeight));
            lowerPipeImage= new Image("res/pipe.png").getScaledCopy((int)(PIPE_WIDTH*screenWidth), (int)(PIPE_HEIGHT*screenHeight));
            upperPipeImage= lowerPipeImage.getFlippedCopy(false, true);

            coinSheet = new SpriteSheet("res/Coin.png",32,32);
            coinAnimation = new Animation(coinSheet,100);

            birdSheet = new SpriteSheet("res/piccioneSheet.png",20,17);
            birdAnimation = new Animation(birdSheet,200);

            heartSheet = new SpriteSheet("res/Items_Heart.png", 16, 16);
            heartAnimation = new Animation(heartSheet, 200);

        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
    public void drawBackgroundSingle(Graphics graphics){
        graphics.drawImage(backgroundSingle,offsetX,offsetY);
    }

    public void drawBird(float x, float y, Graphics graphics, double speedY){
        birdAnimation.getCurrentFrame().setRotation( (float) 180*( (float) Math.atan2(speedY, JUMP_SPEED))/((float)Math.PI)) ;
        birdAnimation.getCurrentFrame().setCenterOfRotation( (float)(BIRD_WIDTH*screenWidth)/2f,  (float)(BIRD_HEIGHT*screenHeight)/2f);
        birdAnimation.draw(offsetX + x*screenWidth, y*screenHeight+ offsetY, (float) BIRD_WIDTH*screenWidth, (float) BIRD_HEIGHT*screenHeight+ offsetY);

    }

    public void drawLives(Player player, Graphics graphics){
        lives = player.getHearts();
        lifeBias = screenWidth/15;
        for(int i = 0; i<lives;i++){
            graphics.drawImage(heartImage,offsetX + lifeBias,0.065f*screenHeight+offsetY);
            lifeBias += screenWidth/15;
        }
    }

    public void drawCoin(float x, float y, Graphics graphics){
        coinAnimation.draw(offsetX + x*screenWidth, y*screenHeight + offsetY, (float) COIN_SIZE*screenWidth, (float) COIN_SIZE*screenHeight);
    }

    public void drawHeart(float x, float y, Graphics graphics){
        heartAnimation.draw(offsetX + x*screenWidth, y*screenHeight + offsetY, (float) HEART_SIZE*screenWidth, (float) HEART_SIZE*screenHeight);
    }

    public void drawPipe(float x, float y, Graphics graphics){
        graphics.drawImage(upperPipeImage,offsetX + x*screenWidth,  offsetY + y*screenHeight - (float)PIPE_HEIGHT*screenHeight - 0.5f*((float)PIPE_FREE_SPACE)*screenHeight);
        graphics.drawImage(lowerPipeImage, offsetX + x*screenWidth, offsetY + y*screenHeight + 0.5f*((float)PIPE_FREE_SPACE)*screenHeight);

    }

    public void setBirdAlpha(float alpha){
        for (int i = 0; i<birdAnimation.getFrameCount();i++){
            birdAnimation.getImage(i).setAlpha(alpha);
        }
    }
}
