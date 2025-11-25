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
        agregarProducto("Chaqueta Nova LayerWind", 299900, "Chaqueta resistente al viento con capucha desmontable");
        agregarProducto("Pantalón Cargo StreetFlex 2.0", 189900, "Pantalones cargo con múltiples bolsillos y ajuste moderno");
        agregarProducto("Jogger SoftFlex Premium", 159900, "Pantalones jogger en algodón elástico para máxima comodidad");
        agregarProducto("Camiseta Essential Cotton", 49900, "Camiseta básica 100% algodón, disponible en varios colores");
        agregarProducto("Sudadera Oversize Urban", 179900, "Sudadera con capucha y corte oversize");
        agregarProducto("Zapatillas UrbanWalk Pro", 349900, "Zapatillas deportivas con soporte para caminata");
        agregarProducto("Gorra Snapback Classic", 45900, "Gorra ajustable con diseño urbano");
        agregarProducto("Chaleco Térmico HeatTech", 219900, "Chaleco fino con tecnología de aislamiento térmico");
        agregarProducto("Polo SportFit", 89900, "Polo de manga corta con tecnología de secado rápido");
        agregarProducto("Shorts Running Flex", 129900, "Shorts deportivos con bolsillo para celular");
        agregarProducto("Camisa Lino Essential", 149900, "Camisa de lino para looks casuales de verano");
        agregarProducto("Cinturón Cuero Premium", 99900, "Cinturón de cuero genuino con hebilla metálica");
    }

    public Producto agregarProducto(String nombre, double precio, String descripcion) {
        Producto nuevoProducto = new Producto(++ultimoId, nombre, precio, descripcion);
        productos.add(nuevoProducto);
        return nuevoProducto;
    }

    public Optional<Producto> obtenerProductoPorId(int id) {
        return productos.stream()
                      .filter(p -> p.getId() == id)
                      .findFirst();
    }

    public List<Producto> obtenerTodosLosProductos() {
        return new ArrayList<>(productos);
    }

    public List<Producto> buscarProductos(String terminoBusqueda) {
        String termino = terminoBusqueda.toLowerCase();
        return productos.stream()
                      .filter(p -> p.getNombre().toLowerCase().contains(termino) || 
                                 p.getDescripcion().toLowerCase().contains(termino))
                      .toList();
    }

    public boolean actualizarProducto(Producto productoActualizado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == productoActualizado.getId()) {
                productos.set(i, productoActualizado);
                return true;
            }
        }
        return false;
    }

    public boolean eliminarProducto(int id) {
        return productos.removeIf(p -> p.getId() == id);
    }
}
