package org.example.model;

public class Favorito {
    private int usuarioId;
    private int productoId;

    public Favorito(int usuarioId, int productoId) {
        this.usuarioId = usuarioId;
        this.productoId = productoId;
    }

    public int getUsuarioId() { return usuarioId; }
    public int getProductoId() { return productoId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setProductoId(int productoId) { this.productoId = productoId; }
}
