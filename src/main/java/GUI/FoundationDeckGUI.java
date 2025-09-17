package GUI;

import DeckOfCards.CartaInglesa;
import solitaire.FoundationDeck;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class FoundationDeckGUI extends StackPane implements PileGUI {

    private FoundationDeck foundation;
    private SolitaireGUI gui;

    public FoundationDeckGUI(FoundationDeck foundation, SolitaireGUI gui) {
        this.foundation = foundation;
        this.gui = gui;
        actualizar();
        // clic en la foundation (intentar mover carta seleccionada aquí)
        setOnMouseClicked(e -> {
            gui.pileClicked(this);
            e.consume();
        });
    }

    public void actualizar() {
        getChildren().clear();
        CartaInglesa ultima = foundation.getUltimaCarta();
        if (ultima != null) {
            CartaGUI cartaGUI = new CartaGUI(ultima);
            cartaGUI.setOnMouseClicked(e -> {
                if (gui.hayCartaSeleccionada()) {
                    gui.pileClicked(this); // intentar colocar la carta seleccionada aquí
                } else {
                    gui.seleccionarCarta(cartaGUI, this); // seleccionar esta carta
                }
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

    public FoundationDeck getFoundation() {
        return foundation;
    }

    @Override
    public Object getPile() {
        return foundation;
    }
}
