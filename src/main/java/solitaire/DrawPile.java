package solitaire;

import DeckOfCards.CartaInglesa;

/**
 * Modela un mazo de cartas de solitario.
 *
 * @author Cecilia Curlango
 * @version 2025
 */
public class DrawPile {

    private Pila<CartaInglesa> cartas;
    private int cuantasCartasSeEntregan = 3;

    public DrawPile() {
        DeckOfCards.Mazo mazo = new DeckOfCards.Mazo();
        cartas = new Pila<>();
        for (CartaInglesa carta : mazo.getCartas()) {
            cartas.push(carta);
        }
        setCuantasCartasSeEntregan(3);
    }

    /**
     * Establece cuantas cartas se sacan cada vez. Puede ser 1 o 3 normalmente.
     *
     * @param cuantasCartasSeEntregan
     */
    public void setCuantasCartasSeEntregan(int cuantasCartasSeEntregan) {
        this.cuantasCartasSeEntregan = cuantasCartasSeEntregan;
    }

    /**
     * Regresa la cantidad de cartas que se sacan cada vez.
     *
     * @return cantidad de cartas que se entregan
     */
    public int getCuantasCartasSeEntregan() {
        return cuantasCartasSeEntregan;
    }

    /**
     * Retirar una cantidad de cartas. Este método se utiliza al inicio de una
     * partida para cargar las cartas de los tableaus. Si se tratan de remover
     * más cartas de las que hay, se provocará un error.
     *
     * @param cantidad de cartas que se quieren a retirar
     * @return cartas retiradas
     */
    public CartaInglesa[] getCartas(int cantidad) {
        int disponibles = cartas.size();
        int cantidadReal = Math.min(cantidad, disponibles);

        CartaInglesa[] retiradas = new CartaInglesa[cantidadReal];
        for (int i = 0; i < cantidadReal; i++) {
            retiradas[i] = cartas.pop();
        }
        return retiradas;
    }

    /**
     * Retira y entrega las cartas del monton. La cantidad que retira depende de
     * cuántas cartas quedan en el montón y serán hasta el máximo que se
     * configuró inicialmente.
     *
     * @return Cartas retiradas.
     */
    public CartaInglesa[] retirarCartas() {
        int maximoARetirar = cartas.size() < cuantasCartasSeEntregan ? cartas.size() : cuantasCartasSeEntregan;
        CartaInglesa[] retiradas = new CartaInglesa[maximoARetirar];

        for (int i = 0; i < maximoARetirar; i++) {
            CartaInglesa retirada = cartas.pop();
            retirada.makeFaceUp();
            retiradas[i] = retirada;
        }
        return retiradas;
    }

    /**
     * Indica si aún quedan cartas para entregar.
     *
     * @return true si hay cartas, false si no.
     */
    public boolean hayCartas() {
        return cartas.size() > 0;
    }

    public CartaInglesa verCarta() {
        CartaInglesa regresar = null;
        if (!cartas.pila_vacia()) {
            regresar = cartas.getElemento(cartas.getTope());
        }
        return regresar;
    }

    /**
     * Agrega las cartas recibidas al monton y las voltea para que no se vean
     * las caras.
     *
     * @param cartasAgregar cartas que se agregan
     */
    public void recargar(CartaInglesa[] cartasAgregar) {
        for (CartaInglesa carta : cartasAgregar) {
            carta.makeFaceDown();
            cartas.push(carta);
        }
    }

    public void agregarCartaAlTope(CartaInglesa carta) {
        cartas.push(carta);
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

    @Override
    public String toString() {
        if (cartas.pila_vacia()) {
            return "-E-";
        }
        return "@";
    }
}
