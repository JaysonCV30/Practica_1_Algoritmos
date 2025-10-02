package solitaire;

import DeckOfCards.CartaInglesa;
import DeckOfCards.Palo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Juego de solitario.
 *
 * @author (Cecilia Curlango Rosas)
 * @version (2025-2)
 */
public class SolitaireGame {

    ArrayList<TableauDeck> tableau = new ArrayList<>();
    ArrayList<FoundationDeck> foundation = new ArrayList<>();
    FoundationDeck lastFoundationUpdated;
    DrawPile drawPile;
    WastePile wastePile;
    private Pila<Movimiento> historialMovimientos = new Pila<>(300);

    public SolitaireGame() {
        drawPile = new DrawPile();
        wastePile = new WastePile();
        createTableaux();
        createFoundations();
    }

    /**
     * Move cards from Waste pile to Draw Pile.
     */
    public void reloadDrawPile() {
        CartaInglesa[] cards = wastePile.emptyPile();
        drawPile.recargar(cards);

        ArrayList<CartaInglesa> cartasMovidas = new ArrayList<>();
        for (CartaInglesa carta : cards) {
            cartasMovidas.add(carta);
        }

        Movimiento m = new Movimiento(wastePile, drawPile, cartasMovidas, Movimiento.TipoMovimiento.RECARGAR_MAZO);
        registrarMovimiento(m);
    }

    /**
     * Move cards from Draw pile to Waste Pile.
     */
    public void drawCards() {
        CartaInglesa[] cards = drawPile.retirarCartas();
        ArrayList<CartaInglesa> cartasMovidas = new ArrayList<>();
        Movimiento m = new Movimiento(drawPile, wastePile, new ArrayList<>(), Movimiento.TipoMovimiento.SACAR_DEL_MAZO);

        for (int i = cards.length - 1; i >= 0; i--) {
            CartaInglesa carta = cards[i];
            m.getEstadoOriginal().put(carta, false); 
            cartasMovidas.add(carta);
        }

        wastePile.addCartas(cards); // se agregan en orden inverso en WastePile
        m.getCartasMovidas().addAll(cartasMovidas);
        registrarMovimiento(m);
    }

    /**
     * Tomar la carta del Waste pile y ponerla en el tableau
     *
     * @param tableauDestino donde se coloca la carta
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveWasteToTableau(int tableauDestino) {
        TableauDeck destino = tableau.get(tableauDestino - 1);
        CartaInglesa carta = wastePile.verCarta();

        if (moveCartaToTableau(carta, destino)) {
            carta = wastePile.getCarta(); // ya fue retirada
            ArrayList<CartaInglesa> cartasMovidas = new ArrayList<>();
            cartasMovidas.add(carta);
            Movimiento m = new Movimiento(wastePile, destino, cartasMovidas, Movimiento.TipoMovimiento.MOVER_CARTA);
            registrarMovimiento(m);
            return true;
        }
        return false;
    }

    /**
     * Tomar varias cartas del Tableau fuente y colocarlas en el Tableau
     * destino.
     *
     * @param tableauFuente de donde se toma la carta (1-7)
     * @param tableauDestino donde se coloca la carta (1-7)
     * @return true si se pudo hacer el movimiento, false si no
     */
    public boolean moveTableauToTableau(int tableauFuente, int tableauDestino) {
        boolean movimientoRealizado = false;
        TableauDeck fuente = tableau.get(tableauFuente - 1);
        TableauDeck destino = tableau.get(tableauDestino - 1);

        if (!fuente.isEmpty()) {
            int valorEsperado = destino.isEmpty() ? 13 : destino.verUltimaCarta().getValor() - 1;
            CartaInglesa cartaInicio = fuente.viewCardStartingAt(valorEsperado);

            if (cartaInicio != null && destino.sePuedeAgregarCarta(cartaInicio)) {
                ArrayList<CartaInglesa> cartas = fuente.removeStartingAt(valorEsperado, this);

                // Capturar carta que se va a voltear antes de hacerlo
                CartaInglesa cartaVolteada = fuente.verUltimaCarta();
                boolean seVolteara = cartaVolteada != null && !cartaVolteada.isFaceup();

                if (destino.agregarBloqueDeCartas(cartas)) {
                    if (!fuente.isEmpty() && seVolteara) {
                        cartaVolteada.makeFaceUp();
                    }

                    Movimiento m = new Movimiento(fuente, destino, cartas, Movimiento.TipoMovimiento.MOVER_CARTA);
                    if (seVolteara) {
                        m.getEstadoOriginal().put(cartaVolteada, false); // estaba boca abajo
                    }
                    registrarMovimiento(m);
                    movimientoRealizado = true;
                }
            }
        }
        return movimientoRealizado;
    }

    /**
     * Tomar la carta de Tableau y colocarla en el Foundation.
     *
     * @param numero de tableau donde se moverá la carta (1-7)
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveTableauToFoundation(int numero) {
        TableauDeck fuente = tableau.get(numero - 1);
        CartaInglesa carta = fuente.removerUltimaCarta(this);

        if (moveCartaToFoundation(carta)) {
            ArrayList<CartaInglesa> cartasMovidas = new ArrayList<>();
            cartasMovidas.add(carta);
            Movimiento m = new Movimiento(fuente, lastFoundationUpdated, cartasMovidas, Movimiento.TipoMovimiento.MOVER_CARTA);
            registrarMovimiento(m);
            return true;
        } else {
            fuente.agregarCarta(carta); // revertir si no se pudo
            return false;
        }
    }

    /**
     * Tomar la carta de Waste y colocarla en el Tableau.
     *
     * @param tableau donde se moverá la carta
     * @return true si se pudo move la carta, false si no
     */
    public boolean moveWasteToTableau(TableauDeck destino) {
        CartaInglesa carta = wastePile.verCarta();

        if (moveCartaToTableau(carta, destino)) {
            carta = wastePile.getCarta();
            ArrayList<CartaInglesa> cartasMovidas = new ArrayList<>();
            cartasMovidas.add(carta);
            Movimiento m = new Movimiento(wastePile, destino, cartasMovidas, Movimiento.TipoMovimiento.MOVER_CARTA);
            registrarMovimiento(m);
            return true;
        }
        return false;
    }

    /**
     * Tomar una carta de Waste y ponerla en una de las Foundations.
     *
     * @return true si se pudo hacer el movimiento.
     */
    public boolean moveWasteToFoundation() {
        CartaInglesa carta = wastePile.verCarta();

        if (moveCartaToFoundation(carta)) {
            carta = wastePile.getCarta();
            ArrayList<CartaInglesa> cartasMovidas = new ArrayList<>();
            cartasMovidas.add(carta);
            Movimiento m = new Movimiento(wastePile, lastFoundationUpdated, cartasMovidas, Movimiento.TipoMovimiento.MOVER_CARTA);
            registrarMovimiento(m);
            return true;
        }
        return false;
    }

    /**
     * Coloca la carta recibida en el Tableau recibido.
     *
     * @param carta a colocar
     * @param destino Tableau que recibe la carta.
     * @return true si se pudo hacer el movimiento, false si no
     */
    private boolean moveCartaToTableau(CartaInglesa carta, TableauDeck destino) {
        return destino.agregarCarta(carta);
    }

    /**
     * Coloca la carta recibida en el Foundation correspondiente.
     *
     * @param carta a colocar
     * @return true si se pudo hacer el movimiento, false si no.
     */
    private boolean moveCartaToFoundation(CartaInglesa carta) {
        int cualFoundation = carta.getPalo().ordinal();
        FoundationDeck destino = foundation.get(cualFoundation);
        lastFoundationUpdated = destino;
        return destino.agregarCarta(carta);
    }

    /**
     * Determina si se terminó el juego. El juego se termina cuando todas las
     * cartas están en Foundation
     *
     * @return true si se terminó el juego
     */
    public boolean isGameOver() {
        boolean gameOver = true;
        for (FoundationDeck foundation : foundation) {
            if (foundation.estaVacio()) {
                gameOver = false;
            } else {
                CartaInglesa ultimaCarta = foundation.getUltimaCarta();
                // si la última carta no es rey, no se ha terminado
                if (ultimaCarta.getValor() != 13) {
                    gameOver = false;
                }
            }
        }
        return gameOver;
    }

    private void createFoundations() {
        for (Palo palo : Palo.values()) {
            foundation.add(new FoundationDeck(palo));
        }
    }

    private void createTableaux() {
        for (int i = 0; i < 7; i++) {
            TableauDeck tableauDeck = new TableauDeck();
            CartaInglesa[] cartasIniciales = drawPile.getCartas(i + 1);

            ArrayList<CartaInglesa> listaDeCartas = new ArrayList<>();
            for (CartaInglesa carta : cartasIniciales) {
                listaDeCartas.add(carta);
            }
            System.out.println("Tableau " + (i + 1) + " recibió " + listaDeCartas.size() + " cartas.");
            tableauDeck.inicializar(listaDeCartas);
            tableau.add(tableauDeck);
        }
    }

    public DrawPile getDrawPile() {
        return drawPile;
    }

    public ArrayList<TableauDeck> getTableau() {
        return tableau;
    }

    public ArrayList<FoundationDeck> getFoundations() {
        return foundation;
    }

    public WastePile getWastePile() {
        return wastePile;
    }

    public FoundationDeck getLastFoundationUpdated() {
        return lastFoundationUpdated;
    }

    public void registrarMovimiento(Movimiento m) {
        historialMovimientos.push(m);
    }

    public void deshacerUltimoMovimiento() {
        if (!historialMovimientos.pila_vacia()) {
            Movimiento m = historialMovimientos.pop();
            revertirMovimiento(m);
        }
    }

    public void revertirMovimiento(Movimiento m) {
        switch (m.getTipo()) {
            case MOVER_CARTA -> {
                Object origen = m.getOrigen();
                Object destino = m.getDestino();

                if (origen instanceof TableauDeck tOrigen && destino instanceof FoundationDeck fDestino) {
                    for (CartaInglesa carta : m.getCartasMovidas()) {
                        fDestino.removerCarta(carta);
                        tOrigen.agregarCartaSinValidacion(carta);
                    }
                } else if (origen instanceof WastePile wOrigen && destino instanceof FoundationDeck fDestino) {
                    for (CartaInglesa carta : m.getCartasMovidas()) {
                        fDestino.removerCarta(carta);
                        wOrigen.agregarCarta(carta);
                    }
                } else if (origen instanceof TableauDeck tOrigen && destino instanceof TableauDeck tDestino) {
                    for (CartaInglesa carta : m.getCartasMovidas()) {
                        tDestino.removerCarta(carta);
                    }
                    tOrigen.agregarBloqueSinValidacion(m.getCartasMovidas());
                } else if (origen instanceof WastePile wOrigen && destino instanceof TableauDeck tDestino) {
                    for (CartaInglesa carta : m.getCartasMovidas()) {
                        tDestino.removerCarta(carta);
                        wOrigen.agregarCarta(carta);
                    }
                }
                m.restaurarEstadoCartas();
            }
            case SACAR_DEL_MAZO -> {
                if (m.getOrigen() instanceof DrawPile draw
                        && m.getDestino() instanceof WastePile waste) {

                    for (CartaInglesa carta : m.getCartasMovidas()) {
                        waste.removerCarta(carta);
                        draw.agregarCartaAlTope(carta);
                    }
                    m.restaurarEstadoCartas();
                }
            }
            case RECARGAR_MAZO -> {
                if (m.getOrigen() instanceof WastePile waste
                        && m.getDestino() instanceof DrawPile draw) {

                    for (CartaInglesa carta : m.getCartasMovidas()) {
                        draw.removerCarta(carta);
                        waste.agregarCarta(carta);
                    }
                    m.restaurarEstadoCartas();
                }
            }
            case VOLTEAR_CARTA -> {
                m.restaurarEstadoCartas();
            }
        }
    }

    public boolean historialVacio() {
        return historialMovimientos.pila_vacia();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        // add foundations
        str.append("Foundation\n");
        for (FoundationDeck foundationDeck : foundation) {
            str.append(foundationDeck);
            str.append("\n");
        }

        // add tableaux
        str.append("\nTableaux\n");
        int tableauNumber = 1;
        for (TableauDeck tableauDeck : tableau) {
            str.append(tableauNumber + " ");
            str.append(tableauDeck);
            str.append("\n");
            tableauNumber++;
        }
        str.append("Waste\n");
        str.append(wastePile);
        str.append("\nDraw\n");
        str.append(drawPile);
        return str.toString();
    }
}
