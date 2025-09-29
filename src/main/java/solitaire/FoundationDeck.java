package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

/**
 * Modela un monículo donde se ponen las cartas de un solo palo.
 *
 * @author Cecilia M. Curlango
 * @version 2025
 */
public class FoundationDeck {

    Palo palo;
    Pila<CartaInglesa> cartas;

    public FoundationDeck(Palo palo) {
        this.palo = palo;
        this.cartas = new Pila<>();
    }

    public FoundationDeck(CartaInglesa carta) {
        palo = carta.getPalo();
        cartas = new Pila<>();
        // solo agrega la carta si es un A
        if (carta.getValorBajo() == 1) {
            cartas.push(carta);
        }
    }

    /**
     * Agrega una carta al montículo. Sólo la agrega si la carta es del palo del
     * montículo y el la siguiente carta en la secuencia.
     *
     * @param carta que se intenta almancenar
     * @return true si se pudo guardar la carta, false si no
     */
    public boolean agregarCarta(CartaInglesa carta) {
        if (carta.tieneElMismoPalo(palo)) {
            if (cartas.pila_vacia()) {
                if (carta.getValorBajo() == 1) {
                    // si no hay cartas entonces la carta debe ser un A
                    cartas.push(carta);
                    return true;
                }
            } else {
                // si hay cartas entonces debe haber secuencia
                CartaInglesa ultimaCarta = cartas.peek();
                if (ultimaCarta.getValorBajo() + 1 == carta.getValorBajo()) {
                    // agregar la carta si el la siguiente a la última
                    cartas.push(carta);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Remover la última carta del montículo.
     *
     * @return la carta que removió, null si estaba vacio
     */
    CartaInglesa removerUltimaCarta() {
        return cartas.pop();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (cartas.pila_vacia()) {
            builder.append("---");
        } else {
            for (int i = 0; i <= cartas.getTope(); i++) {
                builder.append(cartas.getElemento(i).toString());
            }
        }
        return builder.toString();
    }

    /**
     * Determina si hay cartas en el Foundation.
     *
     * @return true hay al menos una carta, false no hay cartas
     */
    public boolean estaVacio() {
        return cartas.pila_vacia();
    }

    /**
     * Obtiene la última carta del Foundation sin removerla.
     *
     * @return última carta, null si no hay cartas
     */
    public CartaInglesa getUltimaCarta() {
        CartaInglesa ultimaCarta = null;
        if (!cartas.pila_vacia()) {
            ultimaCarta = cartas.peek();
        }
        return ultimaCarta;
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

    public void agregarCartaSinValidacion(CartaInglesa carta) {
        cartas.push(carta);
    }
}