package org.example.tiendav2.service;

import org.example.tiendav2.model.ListaDeseos;
import org.example.tiendav2.model.Producto;

import java.util.HashMap;
import java.util.Map;

public class ListaDeseosService {
    private static ListaDeseosService instancia;
    private final Map<Integer, ListaDeseos> listasPorUsuario;
    private final ProductoService productoService;

    private ListaDeseosService() {
        this.listasPorUsuario = new HashMap<>();
        this.productoService = new ProductoService();
    }

    public static synchronized ListaDeseosService getInstancia() {
        if (instancia == null) {
            instancia = new ListaDeseosService();
        }
        return instancia;
    }

    public ListaDeseos obtenerListaDeseos(int usuarioId) {
        return listasPorUsuario.computeIfAbsent(usuarioId, ListaDeseos::new);
    }

    public boolean agregarAListaDeseos(int usuarioId, int productoId) {
        return productoService.obtenerProductoPorId(productoId)
                .map(producto -> {
                    ListaDeseos lista = obtenerListaDeseos(usuarioId);
                    lista.agregarProducto(producto);
                    return true;
                })
                .orElse(false);
    }

    public boolean eliminarDeListaDeseos(int usuarioId, int productoId) {
        ListaDeseos lista = listasPorUsuario.get(usuarioId);
        return lista != null && lista.eliminarProducto(productoId);
    }

    public boolean moverACarrito(int usuarioId, int productoId, org.example.tiendav2.model.Carrito carrito) {
        ListaDeseos lista = listasPorUsuario.get(usuarioId);
        return lista != null && lista.moverACarrito(carrito, productoId);
    }

    public void vaciarListaDeseos(int usuarioId) {
        ListaDeseos lista = listasPorUsuario.get(usuarioId);
        if (lista != null) {
            lista.vaciar();
        }
    }
}
