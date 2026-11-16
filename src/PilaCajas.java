package bodega;

/**
 * Pila de cajas implementada con nodos enlazados (LIFO).
 * El atributo tope apunta al nodo de la caja que está arriba.
 */
public class PilaCajas {

    public int numCajas;
    public Nodo tope;

    public PilaCajas() {
        //Competar todo
    }

    /** Indica si la pila no tiene cajas. */
    public boolean esVacia() {
         //Competar todo
    }

    /** Coloca una caja en el tope de la pila. */
    public void apilarCaja(Caja unaCaja) {
        Nodo nuevo = new Nodo(unaCaja);
        // el nuevo nodo queda encima del tope actual
        
        // el nuevo nodo pasa a ser el tope
        
        
        numCajas++;
    }

    /**
     * Retira la caja del tope.
     * @return true si se retiró una caja, false si la pila estaba vacía.
     */
    public boolean desapilarCaja() {
        if (esVacia()) {
            return false;
        }
        
        //completar todo
    }

    /**
     * Devuelve la caja del tope sin retirarla.
     * @return la caja del tope, o null si la pila está vacía.
     */
    public Caja darTope() {
        if (esVacia()) {
            return null;
        }
        
        //completar todo
    }

    /**
     * Busca una caja por su referencia recorriendo los nodos desde el tope,
     * sin modificar la pila.
     * @return la caja encontrada, o null si no está en la pila.
     */
    public Caja consultarCaja(String referencia) {
        Nodo actual = tope;
        //Completar todo
    }

    /** Suma el peso de todas las cajas de la pila. */
    public double darPesoTotal() {
        double total = 0;
        Nodo actual = tope;
        
        //Completar todo
    }
}
