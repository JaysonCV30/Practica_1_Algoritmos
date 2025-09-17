package GUI;

import DeckOfCards.CartaInglesa;
import javafx.animation.ScaleTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class CartaGUI extends StackPane {

    private CartaInglesa carta;
    private ImageView imageView;
    private boolean seleccionada = false;

    public CartaGUI(CartaInglesa carta) {
        this.carta = carta;
        this.imageView = new ImageView();
        //this.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-radius: 5px;");
        imageView.setFitWidth(110);
        imageView.setFitHeight(150);
        actualizarImagen();
        this.getChildren().add(imageView);
    }

    public void actualizarImagen() {
        try {
            String nombre;
            int valor = carta.getValor();

            // Traducir valor numérico a letra si es figura
            nombre = switch (valor) {
                case 11 ->
                    "J";
                case 12 ->
                    "Q";
                case 13 ->
                    "K";
                case 14 ->
                    "As";
                default ->
                    String.valueOf(valor);
            };

            String figura = carta.getPalo().getFigura();
            String color = carta.getColor();

            String ruta = carta.isFaceup()
                    ? "/cartas/" + nombre + "_" + figura + "_" + color + ".png"
                    : "/cartas/carta_Volteada.png";

            System.out.println("Buscando imagen: " + ruta);

            Image imagen = new Image(getClass().getResourceAsStream(ruta));
            imageView.setImage(imagen);
        } catch (Exception e) {
            System.err.println("No se pudo cargar la imagen de la carta: " + e.getMessage());
        }
    }

    public CartaInglesa getCarta() {
        return carta;
    }

    public void seleccionar(boolean sel) {
        seleccionada = sel;
        if (seleccionada) {
            setEffect(new DropShadow(20, Color.BLUE));
        } else {
            setEffect(null);
        }
    }

    public boolean estaSeleccionada() {
        return seleccionada;
    }

    public void animarSeleccion() {
        ScaleTransition resaltar = new ScaleTransition(Duration.millis(150), this);
        resaltar.setFromX(1.0);
        resaltar.setToX(1.1);
        resaltar.setFromY(1.0);
        resaltar.setToY(1.1);
        resaltar.setAutoReverse(true);
        resaltar.setCycleCount(2);
        resaltar.play();
    }

    // Método para voltear la carta y actualizar la imagen
    public void flip() {
        if (carta.isFaceup()) {
            carta.makeFaceDown();
        } else {
            carta.makeFaceUp();
        }
        actualizarImagen();
    }
}
