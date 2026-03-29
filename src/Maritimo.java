public class Maritimo extends Envio {
    public Maritimo(String codigo, String cliente, double peso, double distancia) {
        super(codigo, cliente, peso, distancia);
    }

    @Override
    public double calcularTarifa() {
        return (distancia * 800) + (peso * 1000);
    }
}