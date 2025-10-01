package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;
import java.util.HashMap;

public class Movimiento {

    // enum es una "clase" especial que representa un grupo de constantes
    public enum TipoMovimiento {
        MOVER_CARTA,
        SACAR_DEL_MAZO,
        RECARGAR_MAZO,
        VOLTEAR_CARTA
    }

    private Object origen; // Puede ser TableauDeck, DrawPile, WastePile, etc.
    private Object destino;
    private ArrayList<CartaInglesa> cartasMovidas;
    private boolean[] estadosPrevios; // true si estaba faceup, false si estaba facedown
    private TipoMovimiento tipo;
    private HashMap<CartaInglesa, Boolean> estadoOriginal = new HashMap<>();

    public Movimiento(Object origen, Object destino, ArrayList<CartaInglesa> cartasMovidas, TipoMovimiento tipo) {
        this.origen = origen;
        this.destino = destino;
        this.cartasMovidas = cartasMovidas;
        this.tipo = tipo;

        for (CartaInglesa carta : cartasMovidas) {
            estadoOriginal.put(carta, carta.isFaceup());
        }
    }

    public Object getOrigen() {
        return origen;
    }

    public Object getDestino() {
        return destino;
    }

    public HashMap<CartaInglesa, Boolean> getEstadoOriginal() {
        return estadoOriginal;
    }

    public ArrayList<CartaInglesa> getCartasMovidas() {
        return cartasMovidas;
    }

    public boolean[] getEstadosPrevios() {
        return estadosPrevios;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void restaurarEstadoCartas() {
        for (HashMap.Entry<CartaInglesa, Boolean> entry : estadoOriginal.entrySet()) {
            CartaInglesa carta = entry.getKey();
            boolean estabaVisible = entry.getValue();
            if (estabaVisible) {
                carta.makeFaceUp();
            } else {
                carta.makeFaceDown();
            }
        }
    }

    @Override
    public String toString() {
        return "Movimiento{"
                + "tipo=" + tipo
                + ", cartas=" + cartasMovidas
                + ", origen=" + origen
                + ", destino=" + destino
                + '}';
    }
}
