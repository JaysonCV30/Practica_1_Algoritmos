package solitaire;

import DeckOfCards.CartaInglesa;
import java.util.ArrayList;

public class Movimiento {
    
    // enum es una "clase" especial que representa un grupo de constantes
    public enum TipoMovimiento {
        MOVER_CARTA,
        SACAR_DEL_MAZO,
        RECARGAR_MAZO
    }

    private Object origen; // Puede ser TableauDeck, DrawPile, WastePile, etc.
    private Object destino;
    private ArrayList<CartaInglesa> cartasMovidas;
    private boolean[] estadosPrevios; // true si estaba faceup, false si estaba facedown
    private TipoMovimiento tipo;

    public Movimiento(Object origen, Object destino, ArrayList<CartaInglesa> cartasMovidas, TipoMovimiento tipo) {
        this.origen = origen;
        this.destino = destino;
        this.cartasMovidas = cartasMovidas;
        this.tipo = tipo;
        this.estadosPrevios = new boolean[cartasMovidas.size()];

        // Guardamos el estado previo de cada carta
        for (int i = 0; i < cartasMovidas.size(); i++) {
            estadosPrevios[i] = cartasMovidas.get(i).isFaceup();
        }
    }

    public Object getOrigen() {
        return origen;
    }

    public Object getDestino() {
        return destino;
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
        for (int i = 0; i < cartasMovidas.size(); i++) {
            if (estadosPrevios[i]) {
                cartasMovidas.get(i).makeFaceUp();
            } else {
                cartasMovidas.get(i).makeFaceDown();
            }
        }
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "tipo=" + tipo +
                ", cartas=" + cartasMovidas +
                ", origen=" + origen +
                ", destino=" + destino +
                '}';
    }
}
