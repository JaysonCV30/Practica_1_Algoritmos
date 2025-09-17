package GUI;

import DeckOfCards.CartaInglesa;
import javafx.animation.TranslateTransition;
import solitaire.TableauDeck;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class TableauDeckGUI extends Pane implements PileGUI {

    private TableauDeck deck;
    private SolitaireGUI gui;

    public TableauDeckGUI(TableauDeck deck, SolitaireGUI gui) {
        this.deck = deck;
        this.gui = gui;
        actualizar();
        // clic en el área vacía del tableau (mover bloque o intentar destino)
        setOnMouseClicked(e -> {
            gui.pileClicked(this);
            e.consume();
        });
    }

    public void actualizar() {
        getChildren().clear();
        double offsetY = 0;
        if (deck.getCards().isEmpty()) {
            Rectangle placeholder = new Rectangle(110, 150);
            placeholder.setArcWidth(10);
            placeholder.setArcHeight(10);
            placeholder.setStroke(Color.DARKGRAY);
            placeholder.setFill(Color.TRANSPARENT);
            placeholder.setOnMouseClicked(e -> {
                gui.pileClicked(this);
                e.consume();
            });
            getChildren().add(placeholder);
        } else {
            for (CartaInglesa carta : deck.getCards()) {
                CartaGUI cartaGUI = new CartaGUI(carta);
                cartaGUI.setLayoutY(offsetY);
                offsetY += 40;

                cartaGUI.setOnMouseClicked(e -> {
                    if (gui.hayCartaSeleccionada()) {
                        if (gui.getCartaSeleccionada() == cartaGUI) {
                            // Deseleccionar si se hace clic sobre la misma carta
                            gui.deseleccionarCarta();
                        } else {
                            // Intentar mover la carta seleccionada a esta pila
                            gui.pileClicked(this);
                        }
                    } else {
                        gui.seleccionarCarta(cartaGUI, this);
                    }
                    e.consume();
                });
                getChildren().add(cartaGUI);
            }
        }
    }

    public CartaInglesa getUltimaCartaVisible() {
        for (int i = getChildren().size() - 1; i >= 0; i--) {
            if (getChildren().get(i) instanceof CartaGUI cartaGUI) {
                if (cartaGUI.getCarta().isFaceup()) {
                    return cartaGUI.getCarta();
                }
            }
        }
        return null;
    }

    public TableauDeck getDeck() {
        return deck;
    }

    @Override
    public Object getPile() {
        return deck;
    }
}
