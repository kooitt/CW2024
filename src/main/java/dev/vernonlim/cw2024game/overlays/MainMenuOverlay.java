package dev.vernonlim.cw2024game.overlays;

import dev.vernonlim.cw2024game.Controller;
import dev.vernonlim.cw2024game.ScreenCode;
import dev.vernonlim.cw2024game.elements.ClickListener;
import dev.vernonlim.cw2024game.elements.TextBox;
import dev.vernonlim.cw2024game.factories.interfaces.OverlayFactory;
import dev.vernonlim.cw2024game.managers.InputManager;
import dev.vernonlim.cw2024game.screens.ScreenChangeHandler;
import javafx.scene.layout.Pane;

public class MainMenuOverlay extends MenuOverlay {
    public MainMenuOverlay(Controller controller, OverlayFactory overlayFactory, Pane root, InputManager input, ScreenChangeHandler screenChangeHandler) {
        super(controller, overlayFactory, root, input, screenChangeHandler);

        TextBox startGame = gridElementFactory.createTextBox("Start Game", new ClickListener() {
            @Override
            public void onClick() {
                screenChangeHandler.changeScreen(ScreenCode.LEVEL_ONE);
            }
        });

        TextBox levelOne = gridElementFactory.createTextBox("Level One", new ClickListener() {
            @Override
            public void onClick() {
                screenChangeHandler.changeScreen(ScreenCode.LEVEL_ONE);
            }
        });

        TextBox levelTwo = gridElementFactory.createTextBox("Level Two", new ClickListener() {
            @Override
            public void onClick() {
                screenChangeHandler.changeScreen(ScreenCode.LEVEL_TWO);
            }
        });

        addButton(startGame);
        addButton(levelOne);
        addButton(levelTwo);

        show();
    }
}
