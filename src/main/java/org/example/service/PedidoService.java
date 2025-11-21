package org.example.service;

import org.example.model.Compra;
import java.util.List;

public class PedidoService {
    public enum EstadoPedido { PENDIENTE, PAGADO, ENTREGADO }

    public static class Pedido {
        private Compra compra;
        private EstadoPedido estado;
        public Pedido(Compra compra) {
            this.compra = compra;
            this.estado = EstadoPedido.PENDIENTE;
        }
        public Compra getCompra() { return compra; }
        public EstadoPedido getEstado() { return estado; }
        public void setEstado(EstadoPedido estado) { this.estado = estado; }
    }

    private List<Pedido> pedidos;

    public PedidoService(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    public void agregarPedido(Compra compra) {
        pedidos.add(new Pedido(compra));
    }

    public boolean cambiarEstado(int idCompra, EstadoPedido nuevoEstado) {
        for (Pedido p : pedidos) {
            if (p.getCompra().getId() == idCompra) {
                p.setEstado(nuevoEstado);
                return true;
            }
        }
        return false;
    }

    public Pedido buscarPedido(int idCompra) {
        for (Pedido p : pedidos) {
            if (p.getCompra().getId() == idCompra) return p;
        }
        return null;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }
}
