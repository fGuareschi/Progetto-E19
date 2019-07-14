package graphics.GUI;

import graphics.Screen;
import org.newdawn.slick.*;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.MouseOverArea;
import resources.PathHandler;
import resources.Resource;
import resources.ResourcePack;
import states.MultiplayerEndMenu;

public class MultiplayerEndMenuGUI extends AbstractMenuGUI {

    private MouseOverArea backButton;
    private MultiplayerEndMenu state;
    private String winString = "Sei grande, hai vinto!";
    private String loseString = "Peccato, hai perso!";
    private String pareggioString = "La partita è finita in pareggio";
    private UnicodeFont unicodeMessage;
    private String localPlayer;
    private String remotePlayer;
    private String localPlayerScore;
    private String remotePlayerScore;
    private Image goldcup;

    public MultiplayerEndMenuGUI(GameContainer container, Screen screen, MultiplayerEndMenu state) throws SlickException {
        super(container, screen);
        this.state = state;
        setBackground();

        int buttonHeight = container.getHeight()/10;
        int buttonWidth = container.getWidth()/3;

        java.awt.Font font = new java.awt.Font("Comic Sans MS", java.awt.Font.PLAIN, 10*getContainer().getWidth()/100);


        unicodeMessage= new UnicodeFont(font);
        unicodeMessage.getEffects().add(new ColorEffect(java.awt.Color.white));
        unicodeMessage.addAsciiGlyphs();
        unicodeMessage.loadGlyphs();

        goldcup = new Image(PathHandler.getInstance().getPath(ResourcePack.VARIOUS, Resource.GOLDCUP)).getScaledCopy(buttonWidth, buttonHeight);

        Image backImage = new Image(PathHandler.getInstance().getPath(ResourcePack.BUTTON, Resource.BACKTOMENUBUTTON)).getScaledCopy(buttonWidth, buttonHeight);
        backButton = new MouseOverArea(container, backImage, (container.getWidth() - buttonWidth) / 2, container.getHeight()-2*buttonHeight, buttonWidth, buttonHeight, this);

        addButton(backButton);
    }

    @Override
    public void reload() {
        super.reload();

        localPlayer = state.getLocalPlayerResult().getName();
        remotePlayer = state.getRemotePlayerResult().getName();
        localPlayerScore = String.valueOf(state.getLocalPlayerResult().getScore());
        remotePlayerScore = String.valueOf(state.getRemotePlayerResult().getScore());
    }

    @Override
    public void render() throws SlickException {
        renderBackground();
        renderButtons();
        if(state.getLocalPlayerResult().getScore() > state.getRemotePlayerResult().getScore()){
            unicodeMessage.drawString(40 * getContainer().getWidth() /100f, 20 * getContainer().getHeight() / 100f, winString, Color.white);
            goldcup.draw(48 * getContainer().getWidth() /100f, 40 * getContainer().getHeight() / 100f);
        }
        else if(state.getLocalPlayerResult().getScore() < state.getRemotePlayerResult().getScore()){
            unicodeMessage.drawString(40 * getContainer().getWidth() /100f, 20 * getContainer().getHeight() / 100f, loseString, Color.white);
            goldcup.draw(48 * getContainer().getWidth() /100f, 50 * getContainer().getHeight() / 100f);
        }
        else{
            unicodeMessage.drawString(40 * getContainer().getWidth() /100f, 20 * getContainer().getHeight() / 100f, pareggioString, Color.white);
        }
        unicodeMessage.drawString(40 * getContainer().getWidth() /100f, 40 * getContainer().getHeight() / 100f, localPlayer, Color.white);
        unicodeMessage.drawString(45 * getContainer().getWidth() /100f, 40 * getContainer().getHeight() / 100f, localPlayerScore, Color.white);
        unicodeMessage.drawString(40 * getContainer().getWidth() /100f, 50 * getContainer().getHeight() / 100f, remotePlayer, Color.white);
        unicodeMessage.drawString(45 * getContainer().getWidth() /100f, 50 * getContainer().getHeight() / 100f, remotePlayerScore, Color.white);
    }

    @Override
    public void componentActivated(AbstractComponent source) {
        if(source == backButton){
            state.backToMenu();
        }

    }
}
