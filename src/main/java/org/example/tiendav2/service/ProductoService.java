package org.example.tiendav2.service;

import org.example.tiendav2.model.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoService {
    private final List<Producto> productos;
    private int ultimoId;

    public ProductoService() {
        this.productos = new ArrayList<>();
        this.ultimoId = 0;
        inicializarDatosEjemplo();
    }

    private void inicializarDatosEjemplo() {
        // Agregar algunos productos de ejemplo
        agregarProducto("Laptop HP", 25000.00, "Laptop HP con 8GB RAM, 512GB SSD");
        agregarProducto("Mouse inalámbrico", 350.00, "Mouse inalámbrico óptico");
        agregarProducto("Teclado mecánico", 1200.00, "Teclado mecánico retroiluminado");
        agregarProducto("Monitor 24\"", 4500.00, "Monitor Full HD 24 pulgadas");
    }

    /**
     * Agrega un nuevo producto al inventario
     */
    public Producto agregarProducto(String nombre, double precio, String descripcion) {
        Producto nuevoProducto = new Producto(++ultimoId, nombre, precio, descripcion);
        productos.add(nuevoProducto);
        return nuevoProducto;
    }

    /**
     * Obtiene un producto por su ID
     */
    public Optional<Producto> obtenerProductoPorId(int id) {
        return productos.stream()
                      .filter(p -> p.getId() == id)
                      .findFirst();
    }

    /**
     * Obtiene todos los productos disponibles
     */
    public List<Producto> obtenerTodosLosProductos() {
        return new ArrayList<>(productos); // Devolver una copia para evitar modificaciones externas
    }

    /**
     * Busca productos que coincidan con el término de búsqueda
     */
    public List<Producto> buscarProductos(String terminoBusqueda) {
        String termino = terminoBusqueda.toLowerCase();
        return productos.stream()
                      .filter(p -> p.getNombre().toLowerCase().contains(termino) || 
                                 p.getDescripcion().toLowerCase().contains(termino))
                      .toList();
    }

    /**
     * Actualiza un producto existente
     */
    public boolean actualizarProducto(Producto productoActualizado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == productoActualizado.getId()) {
                productos.set(i, productoActualizado);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina un producto por su ID
     */
    public boolean eliminarProducto(int id) {
        return productos.removeIf(p -> p.getId() == id);
    }
}
