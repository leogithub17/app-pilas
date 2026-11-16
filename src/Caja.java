package bodega;

/**
 * Representa una caja con su número de referencia, la descripción
 * de su contenido y su peso.
 */
public class Caja {

    public String referencia;
    public String descripcion;
    public double peso;

    public Caja(String referencia, String descripcion, double peso) {
        //Completar todo
    }

    // Como el atributo peso es privado (-), se necesita un método para leerlo.
    public double getPeso() {
        //completar todo
    }

    @Override
    public String toString() {
        return "Ref: " + referencia + " | Contenido: " + descripcion + " | Peso: " + peso + " kg";
    }
}
