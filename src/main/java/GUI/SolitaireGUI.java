package GUI;

import DeckOfCards.CartaInglesa;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import solitaire.*;

public class SolitaireGUI extends Application {

    private SolitaireGame juego;

    private CartaGUI cartaSeleccionada;
    private PileGUI origenSeleccionado;

    private DrawPileGUI drawPileGUI;
    private WastePileGUI wastePileGUI;
    private FoundationDeckGUI[] foundationsGUI;
    private TableauDeckGUI[] tableauxGUI;
    private Button botonDeshacer;

    @Override
    public void start(Stage stage) {
        juego = new SolitaireGame();

        drawPileGUI = new DrawPileGUI(juego.getDrawPile(), this);
        wastePileGUI = new WastePileGUI(juego.getWastePile(), this);

        foundationsGUI = new FoundationDeckGUI[4];
        for (int i = 0; i < 4; i++) {
            foundationsGUI[i] = new FoundationDeckGUI(juego.getFoundations().get(i), this);
        }

        tableauxGUI = new TableauDeckGUI[7];
        for (int i = 0; i < 7; i++) {
            tableauxGUI[i] = new TableauDeckGUI(juego.getTableau().get(i), this);
        }

        HBox zonaSuperior = new HBox(20, drawPileGUI, wastePileGUI, foundationsGUI[0], foundationsGUI[1], foundationsGUI[2], foundationsGUI[3]);

        HBox zonaInferior = new HBox(20, tableauxGUI[0], tableauxGUI[1], tableauxGUI[2], tableauxGUI[3], tableauxGUI[4], tableauxGUI[5], tableauxGUI[6]);

        botonDeshacer = new Button("Deshacer");
        botonDeshacer.setOnAction(e -> {
            juego.deshacerUltimoMovimiento(); // revierte el movimiento
            actualizarTodo(); // refresca la GUI
            actualizarEstadoBotonDeshacer();
        });
        actualizarEstadoBotonDeshacer();
        
        BorderPane root = new BorderPane();
        root.setTop(zonaSuperior);
        root.setCenter(zonaInferior);
        VBox layoutPrincipal = new VBox(40); // 40 px de separación vertical
        layoutPrincipal.getChildren().addAll(zonaSuperior, zonaInferior);
        HBox controlesInferiores = new HBox(20, botonDeshacer);
        layoutPrincipal.getChildren().add(controlesInferiores);
        root.setCenter(layoutPrincipal);

        Scene scene = new Scene(root, 950, 900);
        scene.setFill(Color.DARKGREEN);
        stage.setScene(scene);
        stage.setTitle("Solitario");
        stage.show();
    }

    public void actualizarEstadoBotonDeshacer(){
        botonDeshacer.setDisable(juego.historialVacio());
    }
    
    public void sacudirCarta(CartaGUI cartaGUI) {
        TranslateTransition shake = new TranslateTransition(Duration.millis(100), cartaGUI);
        shake.setFromX(-10);
        shake.setToX(10);
        shake.setCycleCount(4);
        shake.setAutoReverse(true);
        shake.setOnFinished(e -> cartaGUI.setTranslateX(0));
        shake.play();
    }

    public void seleccionarCarta(CartaGUI carta, PileGUI origen) {
        if (cartaSeleccionada != null) {
            cartaSeleccionada.seleccionar(false);
        }
        cartaSeleccionada = carta;
        origenSeleccionado = origen;
        cartaSeleccionada.seleccionar(true);
        cartaSeleccionada.animarSeleccion();

        System.out.println("Carta seleccionada: " + carta.getCarta().toString());
    }

    public boolean hayCartaSeleccionada() {
        return cartaSeleccionada != null;
    }

    public void pileClicked(PileGUI destino) {
        if (cartaSeleccionada == null || origenSeleccionado == null || destino == origenSeleccionado) {
            return;
        }

        boolean movimientoExitoso = false;

        Object origen = origenSeleccionado.getPile();
        Object destinoPile = destino.getPile();

        CartaInglesa carta = cartaSeleccionada.getCarta();

        if (origen instanceof WastePile && destino instanceof TableauDeckGUI) {
            TableauDeckGUI destinoGUI = (TableauDeckGUI) destino;
            CartaInglesa cartaDestino = destinoGUI.getUltimaCartaVisible();
            CartaInglesa cartaOrigen = cartaSeleccionada.getCarta();

            if (cartaDestino == null) {
                if (cartaOrigen.getValor() == 13) {
                    movimientoExitoso = juego.moveWasteToTableau((TableauDeck) destinoPile);
                }
            } else {
                boolean coloresAlternados = !cartaOrigen.getColor().equals(cartaDestino.getColor());
                boolean valorCorrecto = cartaOrigen.getValor() + 1 == cartaDestino.getValor();

                if (coloresAlternados && valorCorrecto) {
                    movimientoExitoso = juego.moveWasteToTableau((TableauDeck) destinoPile);
                } else {
                    System.out.println("Movimiento inválido desde WastePile: " + cartaOrigen + " no puede colocarse sobre " + cartaDestino);
                }
            }
        }

        if (origen instanceof WastePile && destinoPile instanceof FoundationDeck) {
            movimientoExitoso = juego.moveWasteToFoundation();
        }

        if (origen instanceof TableauDeck && destinoPile instanceof FoundationDeck) {
            int index = juego.getTableau().indexOf((TableauDeck) origen);
            movimientoExitoso = juego.moveTableauToFoundation(index + 1);
        }

        if (origen instanceof TableauDeck && destinoPile instanceof TableauDeck) {
            int fuente = juego.getTableau().indexOf((TableauDeck) origen);
            int destinoIndex = juego.getTableau().indexOf((TableauDeck) destinoPile);
            movimientoExitoso = juego.moveTableauToTableau(fuente + 1, destinoIndex + 1);
        }

        if (movimientoExitoso) {
            cartaSeleccionada.seleccionar(false);
            cartaSeleccionada = null;
            origenSeleccionado = null;
            actualizarTodo();
            actualizarEstadoBotonDeshacer();
        } else {
            sacudirCarta(cartaSeleccionada);
        }
        System.out.println(juego.toString());
    }

    public CartaGUI getCartaSeleccionada() {
        return cartaSeleccionada;
    }

    public void deseleccionarCarta() {
        if (cartaSeleccionada != null) {
            cartaSeleccionada.seleccionar(false);
            cartaSeleccionada = null;
            origenSeleccionado = null;
        }
    }

    public void recargarDrawPile() {
        juego.reloadDrawPile();
    }

    public WastePile getWastePile() {
        return juego.getWastePile();
    }

    public void actualizarTodo() {
        drawPileGUI.actualizar();
        wastePileGUI.actualizar();
        for (FoundationDeckGUI f : foundationsGUI) {
            f.actualizar();
        }
        for (TableauDeckGUI t : tableauxGUI) {
            t.actualizar();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}