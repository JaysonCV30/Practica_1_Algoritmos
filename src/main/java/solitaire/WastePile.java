package solitaire;

import DeckOfCards.CartaInglesa;

/**
 * Modela el montículo donde se colocan las cartas que se extraen de Draw pile.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class WastePile {

    private Pila<CartaInglesa> cartas;

    public WastePile() {
        cartas = new Pila<>();
    }

    public void addCartas(CartaInglesa[] nuevas) {
        for (int i = 0; i < nuevas.length; i++) {
            cartas.push(nuevas[i]);
        }
    }

    public CartaInglesa[] emptyPile() {
        int cantidad = cartas.size();
        CartaInglesa[] pile = new CartaInglesa[cantidad];
        for (int i = cantidad - 1; i >= 0; i--) {
            pile[i] = cartas.pop();
        }
        return pile;
    }

    /**
     * Obtener la última carta sin removerla.
     *
     * @return Carta que está encima. Si está vacía, es null.
     */
    public CartaInglesa verCarta() {
        CartaInglesa regresar = null;
        if (!cartas.pila_vacia()) {
            regresar = cartas.peek();
        }
        return regresar;
    }

    public CartaInglesa getCarta() {
        CartaInglesa regresar = null;
        if (!cartas.pila_vacia()) {
            regresar = cartas.pop();
        }
        return regresar;
    }

    public void removerCarta(CartaInglesa carta) {
        Pila<CartaInglesa> temporal = new Pila<>(cartas.size());
        boolean encontrada = false;

        while (!cartas.pila_vacia()) {
            CartaInglesa actual = cartas.pop();
            if (!encontrada && actual.equals(carta)) {
                encontrada = true; // no la agregamos
            } else {
                temporal.push(actual);
            }
        }

        while (!temporal.pila_vacia()) {
            cartas.push(temporal.pop());
        }
    }

    public void agregarCarta(CartaInglesa carta) {
        cartas.push(carta);
    }

    @Override
    public String toString() {
        StringBuilder stb = new StringBuilder();
        if (cartas.pila_vacia()) {
            stb.append("---");
        } else {
            CartaInglesa regresar = cartas.peek();
            regresar.makeFaceUp();
            stb.append(regresar.toString());
        }
        return stb.toString();
    }

    public boolean hayCartas() {
        return !cartas.pila_vacia();
    }
}
