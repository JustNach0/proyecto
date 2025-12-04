package org.example.tiendav2.model;

public enum TipoMetodoPago {
    TARJETA_CREDITO("Tarjeta de Crédito"),
    TARJETA_DEBITO("Tarjeta de Débito"),
    PAYPAL("PayPal"),
    EFECTIVO("Efectivo");

    private final String descripcion;

    TipoMetodoPago(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
