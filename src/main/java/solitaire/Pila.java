package solitaire;

public class Pila<T> {

    private int tope = -1;
    private T[] pila;

    public Pila() {
        this.tope = -1;
        pila = (T[]) new Object[52];
    }

    // Devuelve el índice actual del tope
    public int getTope() {
        return tope;
    }

    // Devuelve el elemento en un índice dado (para mostrar la pila)
    public T getElemento(int index) {
        return pila[index];
    }

    public void push(T dato) {
        if (pila_llena()) {
            System.out.println("Desbordamiento");
        } else {
            tope++;
            pila[tope] = dato;
        }
    }

    public T pop() {
        if (pila_vacia()) {
            System.out.println("Subdesbordamiento");
            return null;
        } else {
            T dato = pila[tope];
            tope--;
            return dato;
        }
    }
    
    public T peek() {
        if (pila_vacia()) return null;
        return pila[tope];
    }

    public boolean pila_llena() {
        return (tope == pila.length - 1);
    }

    public boolean pila_vacia() {
        return (tope == -1);
    }
    
    public int size(){
        return tope + 1;
    }

    public void mostrarPila() {
        System.out.print("Pila actual: [");
        for (int i = 0; i <= tope; i++) {
            System.out.print(pila[i] + " ");
        }
        System.out.println("]");
    }
}