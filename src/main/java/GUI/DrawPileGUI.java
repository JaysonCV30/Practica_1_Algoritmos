package GUI;

import solitaire.DrawPile;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import DeckOfCards.CartaInglesa;

public class DrawPileGUI extends StackPane implements PileGUI {

    private DrawPile drawPile;
    private SolitaireGUI gui;

    public DrawPileGUI(DrawPile drawPile, SolitaireGUI gui) {
        this.drawPile = drawPile;
        this.gui = gui;
        actualizar();

        // clic en el mazo: sacar cartas o recargar si está vacío
        setOnMouseClicked(e -> {
            if (drawPile.hayCartas()) {
                gui.comerDesdeDrawPile();
            } else {
                gui.recargarDrawPile();
            }
            gui.actualizarTodo();
            gui.actualizarEstadoBotonDeshacer();
            e.consume();
        });
    }

    public void actualizar() {
        getChildren().clear();
        CartaInglesa carta = drawPile.verCarta();

        if (carta != null) {
            // Mostrar la carta volteada (el reverso del mazo)
            CartaGUI cartaGUI = new CartaGUI(carta);
            cartaGUI.getCarta().makeFaceDown(); // asegurar que se muestre volteada
            cartaGUI.actualizarImagen();
            getChildren().add(cartaGUI);
        } else {
            Rectangle placeholder = new Rectangle(110, 150);
            placeholder.setFill(Color.TRANSPARENT);
            placeholder.setStroke(Color.DARKGRAY);
            placeholder.setArcWidth(10);
            placeholder.setArcHeight(10);
            getChildren().add(placeholder);
        }
    }

    @Override
    public Object getPile() {
        return drawPile;
    }
}
