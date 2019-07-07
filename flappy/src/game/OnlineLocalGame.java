package game;

import entityComponent.Entity;
import entityComponent.EntityFactory;
import entityComponent.components.LogicComponent;
import entityComponent.implementations.bird.BirdLogicComponent;
import entityComponent.implementations.items.coin.CoinLogicComponent;
import entityComponent.implementations.obstacles.ObstacleLogicComponent;
import game.gameEvents.GameEventDispatcher;
import game.gameEvents.GameEventType;
import game.itemGeneration.coin.CoinListener;
import game.itemGeneration.coin.CoinGenerator;
import game.itemGeneration.coin.CoinListener;
import game.itemGeneration.obstacle.ObstacleGenerator;
import game.itemGeneration.obstacle.ObstacleGeneratorFactory;
import game.itemGeneration.obstacle.ObstacleListener;
import graphics.Canvas;
import logic.SinglePlayer.Player;
import logic.gameElements.Coin;
import network.test.CommandHandler;
import network.test.commands.*;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Shape;
import resources.FileKeys;
import resources.PathHandler;
import resources.PathKeys;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public class OnlineLocalGame extends GameEventDispatcher implements CoinListener, ObstacleListener {
    private CopyOnWriteArrayList<Entity> entities;
    private CopyOnWriteArrayList<ObstacleLogicComponent> obstacles;
    private CopyOnWriteArrayList<CoinLogicComponent> coins;
    private BirdLogicComponent bird;
    private Player player;
    private Canvas canvas;
    private double gameSpeed;
    private ObstacleGenerator obstacleGenerator;
    private CoinGenerator coinGenerator;
    private Image background;
    private CommandHandler commandHandler;
    private int IDcount;

    public OnlineLocalGame(Canvas canvas, DifficultySettings settings, CommandHandler commandHandler) {
        this.canvas = canvas;
        this.gameSpeed = settings.getSpeed();
        this.obstacleGenerator = ObstacleGeneratorFactory.makeObstacleGenerator(settings.getObstacleGenerator(), canvas);
        this.coinGenerator = new CoinGenerator(canvas);
        this.commandHandler= commandHandler;
        entities = new CopyOnWriteArrayList<>();
        coins = new CopyOnWriteArrayList<>();
        obstacles = new CopyOnWriteArrayList<>();
        Entity birdEntity = EntityFactory.makeBird(0.2, 0.5,canvas);
        addEntity(birdEntity);
        player = new Player();
        bird = (BirdLogicComponent) birdEntity.getLogicComponent();
        obstacleGenerator.addListener(this);
        obstacleGenerator.addListener(coinGenerator);
        coinGenerator.addListener(this);
        try {
            background = new Image(PathHandler.getInstance().getPath(FileKeys.SPRITES, PathKeys.BACKGROUND));
        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    private void addEntity(Entity entity){
        entity.setID(IDcount++);
        entities.add(entity);
    }
    public void update(int delta){
 //       delta*=gameSpeed;
        updateEntities(delta);
        obstacleGenerator.update(delta);
        if (!bird.isImmune()){
            checkCollisions();
            checkScore();
        }
        checkOutOfBounds();

    }
    public void render(){
        canvas.drawImage(background, 0, 0, 1, 1);
        renderEntities();
    }
    public void playerJump(){
        bird.jump();
        notifyEvent(GameEventType.JUMP);
        commandHandler.sendCommand(new JumpCommand(bird.getX(), bird.getY()));
    }
    private void checkOutOfBounds(){
        for( ObstacleLogicComponent obstacle : obstacles){
            if (obstacle.outOfBounds()){
                removeObstacle(obstacle);
            }
        }
        for( CoinLogicComponent coin : coins){
            if (coin.outOfBounds()){
                removeCoin(coin);
            }
        }
    }
    private void checkCollisions(){
        checkCoinCollisions();
        checkObstacleCollisions();
    }

    public BirdLogicComponent getBird() {
        return bird;
    }

    private void checkScore(){
        for(ObstacleLogicComponent obstacle: obstacles){
            if( ( !obstacle.isPassed() ) && (bird.getX() > obstacle.getX()) ){
                obstacle.setPassed(true);
                player.addScore();
                gameSpeed += 0.05;
            }
        }
    }
    private void checkObstacleCollisions(){

        for(ObstacleLogicComponent obstacle : obstacles){
            if (obstacle.collide(bird)){
                obstacleCollision(obstacle);
            }
        }
    }
    private void obstacleCollision(ObstacleLogicComponent obstacle){
        bird.acquireImmunity();
        if (gameSpeed>0.45)
            gameSpeed-=0.20;
        notifyEvent(GameEventType.COLLISION);
        commandHandler.sendCommand(new ObstacleCollisionCommand(Objects.requireNonNull(getEntity(obstacle))));
        if(obstacle.destroyOnHit())
            removeObstacle(obstacle);

    }
    private void checkCoinCollisions(){
        for(CoinLogicComponent coin: coins){
            if(coin.collide(bird)){
                removeCoin(coin);
            }
        }
    }

    private void updateEntities(int delta){
        for(Entity entity: entities){
            entity.update(delta);
        }
    }
    public void removeCoin(LogicComponent logic){
        coins.removeIf(obstacleLogicComponent -> obstacleLogicComponent == logic);
        commandHandler.sendCommand(new CoinCollisionCommand(Objects.requireNonNull(getEntity(logic))));
        removeEntity(logic);

    }
    private void removeObstacle(LogicComponent logic){
        obstacles.removeIf(obstacleLogicComponent -> obstacleLogicComponent == logic);
        removeEntity(logic);
    }
    private Entity getEntity(LogicComponent logic){
        for (Entity entity: entities){
            if(entity.getLogicComponent()== logic)
                return entity;
        }
        return null;
    }
    private void removeEntity(LogicComponent logic){
        entities.remove(getEntity(logic));
    }
    private void renderEntities(){
        for(Entity entity: entities){
            entity.render();
        }
    }
    private void gameover(){
        notifyEvent(GameEventType.GAMEOVER);
    }
    @Override
    public void onCoinGenerated(Entity coin) {
        addEntity(coin);
        coins.add((CoinLogicComponent) coin.getLogicComponent());
        commandHandler.sendCommand(new CoinGeneratedCommand(coin));

    }

    @Override
    public void onObstacleGenerated(Entity obstacle) {
        addEntity(obstacle);
        obstacles.add((ObstacleLogicComponent) obstacle.getLogicComponent());
        commandHandler.sendCommand(new ObstacleGeneratedCommand(obstacle));
    }
}
