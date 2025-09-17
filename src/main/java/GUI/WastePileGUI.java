package GUI;

import solitaire.WastePile;
import DeckOfCards.CartaInglesa;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class WastePileGUI extends StackPane implements PileGUI {
    private WastePile waste;
    private SolitaireGUI gui;

    public WastePileGUI(WastePile waste, SolitaireGUI gui) {
        this.waste = waste;
        this.gui = gui;
        actualizar();

        // clic sobre la pila (placeholder vacío)
        setOnMouseClicked(e -> {
            gui.pileClicked(this);
            e.consume();
        });
    }

    public void actualizar() {
        getChildren().clear();
        CartaInglesa carta = waste.verCarta();
        if (carta != null) {
            CartaGUI cartaGUI = new CartaGUI(carta);
            // al hacer click en la carta del waste seleccionarla como origen
            cartaGUI.setOnMouseClicked(e -> {
                gui.seleccionarCarta(cartaGUI, this);
                e.consume();
            });
            getChildren().add(cartaGUI);
        } else {
            Rectangle placeholder = new Rectangle(110, 150);
            placeholder.setFill(Color.TRANSPARENT);
            placeholder.setStroke(Color.GRAY);
            placeholder.setArcWidth(10);
            placeholder.setArcHeight(10);
            getChildren().add(placeholder);
        }
    }

    public WastePile getWaste() {
        return waste;
    }

    @Override
    public Object getPile() {
        return waste;
    }
}