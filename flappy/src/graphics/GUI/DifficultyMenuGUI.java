package graphics.GUI;

import graphics.Screen;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.MouseOverArea;
import states.DifficultyMenu;

public class DifficultyMenuGUI extends AbstractMenuGUI {
    private DifficultyMenu state;
    private MouseOverArea easyButton;
    private MouseOverArea mediumButton;
    private MouseOverArea hardButton;

    public DifficultyMenuGUI(GameContainer container, Screen screen, DifficultyMenu state) throws SlickException {
        super(container, screen);
        this.state = state;
        Image easy = new Image("res/easybutton.png").getScaledCopy(250, 70);
        easyButton = new Button(container, screen, easy,  0.3, this);
        Image medium = new Image("res/mediumbutton.png").getScaledCopy(250, 70);
        mediumButton = new Button(container,screen, medium,  0.5, this);
        Image hard = new Image("res/hardbutton.png").getScaledCopy(250, 70);
        hardButton = new Button(container,screen, hard,  0.7, this);
    }

    @Override
    public void render() {
        easyButton.render(getContainer(),getContainer().getGraphics());
        mediumButton.render(getContainer(),getContainer().getGraphics());
        hardButton.render(getContainer(),getContainer().getGraphics());
    }

    @Override
    public void componentActivated(AbstractComponent source) {
        if(source == easyButton){
            state.easy();
        }
        if(source == mediumButton){
            state.medium();
        }
        if(source == hardButton){
            state.hard();
        }
    }
}
