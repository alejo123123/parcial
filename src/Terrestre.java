public class Terrestre extends Envio {
    public Terrestre(String codigo, String cliente, double peso, double distancia) {
        super(codigo, cliente, peso, distancia);
    }

    @Override
    public double calcularTarifa() {
        return (distancia * 1500) + (peso * 2000);
    }
}