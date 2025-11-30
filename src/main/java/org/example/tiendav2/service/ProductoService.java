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
        // ======= T-SHIRTS =======
        agregarProducto("ATTICUS | Black", 190000, "Camiseta ATTICUS en color negro");
        agregarProducto("Icon | Black", 220000, "Camiseta Icon en color negro");
        agregarProducto("Triple Icon | Green", 220000, "Camiseta Triple Icon en color verde");
        agregarProducto("LRN BLUE COIN", 220000, "Camiseta LRN BLUE COIN");
        agregarProducto("Icon | [RED]", 220000, "Camiseta Icon en color rojo");
        agregarProducto("Triple Icon | White", 160000, "Camiseta Triple Icon en color blanco");
        agregarProducto("LRN LUNA", 265000, "Camiseta LRN LUNA");
        agregarProducto("LRN RED WAX LABEL", 220000, "Camiseta LRN RED WAX LABEL");
        agregarProducto("Hygiea | Beige", 190000, "Camiseta Hygiea en color beige");
        agregarProducto("Cong | Green", 190000, "Camiseta Cong en color verde");
        agregarProducto("Garly | Black", 190000, "Camiseta Garly en color negro");
        agregarProducto("LRN MUNDO", 265000, "Camiseta LRN MUNDO");
        agregarProducto("Enzo | White / Blue Edition", 190000, "Camiseta Enzo en edición blanca/azul");

        // ======= CROP TOPS =======
        agregarProducto("LRN | VICTORIA | TOP | GREY", 99900, "Crop top LRN VICTORIA GRIS");
        agregarProducto("LRN | VICTORIA | TOP | BLACK", 99900, "Crop top LRN VICTORIA NEGRO");

        // ======= CAPS =======
        agregarProducto("LRR SHADOW EDITION", 195000, "Gorra LRR SHADOW EDITION");
        agregarProducto("LRN BLACK STAR", 195000, "Gorra LRN BLACK STAR");
        agregarProducto("LRN PINK BOLD", 195000, "Gorra LRN PINK BOLD");
        agregarProducto("LRN CLASIC", 195000, "Gorra LRN CLASIC");
        agregarProducto("LRN PURPLE NIGHT", 195000, "Gorra LRN PURPLE NIGHT");
        agregarProducto("LRN OLIVE ALPINE", 195000, "Gorra LRN OLIVE ALPINE");
        agregarProducto("LRN RED SUPREME", 195000, "Gorra LRN RED SUPREME");
        agregarProducto("LRN MONOGRAM CAP", 175000, "Gorra LRN MONOGRAM CAP");
        agregarProducto("LRN DANTE WHITE CAP", 175000, "Gorra LRN DANTE WHITE CAP");
        agregarProducto("LRN VANTINO", 180000, "Gorra LRN VANTINO");
        agregarProducto("LRN FILO BLACK", 195000, "Gorra LRN FILO BLACK");
        agregarProducto("CLASSIC CAP", 180000, "Gorra CLASSIC CAP");
        agregarProducto("Icon Red Cap", 180000, "Gorra Icon Red");
        agregarProducto("Gringnani Black Cap", 180000, "Gorra Gringnani Black");
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
