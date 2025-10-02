package GUI;

import solitaire.WastePile;
import DeckOfCards.CartaInglesa;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class WastePileGUI extends StackPane implements PileGUI {

    private WastePile waste;
    private SolitaireGUI gui;
    private CartaGUI cartaVisible;

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
            cartaVisible = new CartaGUI(carta);
            // al hacer click en la carta del waste seleccionarla como origen
            cartaVisible.setOnMouseClicked(e -> {
                if (gui.hayCartaSeleccionada()) {
                    if (gui.getCartaSeleccionada() == cartaVisible) {
                        gui.deseleccionarCarta(); // ← deselecciona si ya estaba seleccionada
                    } else {
                        gui.pileClicked(this); // ← intenta mover la carta seleccionada al Waste
                    }
                } else {
                    gui.seleccionarCarta(cartaVisible, this); // ← selecciona la carta del Waste
                }
                e.consume();
            });
            getChildren().add(cartaVisible);
        } else {
            Rectangle placeholder = new Rectangle(110, 150);
            placeholder.setFill(Color.TRANSPARENT);
            placeholder.setStroke(Color.GRAY);
            placeholder.setArcWidth(10);
            placeholder.setArcHeight(10);
            getChildren().add(placeholder);
        }
    }

    public void deseleccionarCartaVisible() {
        if (cartaVisible != null && cartaVisible.estaSeleccionada()) {
            cartaVisible.seleccionar(false);
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
