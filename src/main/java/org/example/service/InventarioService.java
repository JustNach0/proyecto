package org.example.service;

import org.example.model.Producto;
import org.example.model.Inventario;
import java.util.ArrayList;
import java.util.List;

public class InventarioService {
    private List<Producto> productos;
    private List<Inventario> inventarios;

    public InventarioService() {
        productos = new ArrayList<>();
        inventarios = new ArrayList<>();
    }

    public void agregarProducto(Producto producto, int cantidad) {
        productos.add(producto);
        inventarios.add(new Inventario(producto.getId(), cantidad));
    }

    public boolean eliminarProducto(int productoId) {
        productos.removeIf(p -> p.getId() == productoId);
        return inventarios.removeIf(i -> i.getProductoId() == productoId);
    }

    public boolean editarProducto(Producto productoEditado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == productoEditado.getId()) {
                productos.set(i, productoEditado);
                return true;
            }
        }
        return false;
    }

    public boolean actualizarCantidad(int productoId, int nuevaCantidad) {
        for (Inventario inv : inventarios) {
            if (inv.getProductoId() == productoId) {
                inv.setCantidad(nuevaCantidad);
                return true;
            }
        }
        return false;
    }

    public Producto buscarProducto(int productoId) {
        for (Producto p : productos) {
            if (p.getId() == productoId) return p;
        }
        return null;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public int getCantidad(int productoId) {
        for (Inventario inv : inventarios) {
            if (inv.getProductoId() == productoId) return inv.getCantidad();
        }
        return 0;
    }
}
