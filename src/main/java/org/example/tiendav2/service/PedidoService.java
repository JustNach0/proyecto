package org.example.tiendav2.service;

import org.example.tiendav2.model.Carrito;
import org.example.tiendav2.model.ColaPedidos;
import org.example.tiendav2.model.HistorialCompra;
import org.example.tiendav2.model.Pedido;
import org.example.tiendav2.model.Producto;

public class PedidoService {
    private static PedidoService instancia;
    private final ColaPedidos colaPedidos;

    private PedidoService() {
        this.colaPedidos = new ColaPedidos();
    }

    public static synchronized PedidoService getInstancia() {
        if (instancia == null) {
            instancia = new PedidoService();
        }
        return instancia;
    }

    public Pedido crearPedido(Carrito carrito, String direccionEnvio, String metodoPago) {
        try {
            System.out.println("=== INICIO crearPedido ===");
            System.out.println("Carrito: " + (carrito != null ? "válido" : "nulo"));
            System.out.println("Usuario ID: " + (carrito != null ? carrito.getUsuarioId() : "N/A"));
            System.out.println("Total del carrito: " + (carrito != null ? carrito.getTotal() : "N/A"));
            System.out.println("Dirección: " + direccionEnvio);
            System.out.println("Método de pago: " + metodoPago);
            if (carrito == null || carrito.estaVacio()) {
                throw new IllegalArgumentException("El carrito no puede estar vacío");
            }
            
            if (direccionEnvio == null || direccionEnvio.trim().isEmpty()) {
                throw new IllegalArgumentException("La dirección de envío es requerida");
            }
            
            // Asegurarse de que el método de pago sea un String válido
            String metodoPagoStr = (metodoPago != null) ? metodoPago.toString().trim() : "";
            if (metodoPagoStr.isEmpty()) {
                throw new IllegalArgumentException("El método de pago es requerido");
            }
            
            // Crear el pedido con los parámetros validados
            Pedido pedido = new Pedido(carrito, direccionEnvio, metodoPagoStr);
            colaPedidos.encolar(pedido);
            
            // Registrar en el historial de compras
            HistorialCompra compra = null;
            try {
                System.out.println("Creando HistorialCompra...");
                int idCompra = (int) (System.currentTimeMillis() % 1000000);
                int usuarioId = carrito.getUsuarioId();
                double total = carrito.getTotal();
                
                System.out.println("ID Compra: " + idCompra);
                System.out.println("Usuario ID: " + usuarioId);
                System.out.println("Total: " + total);
                System.out.println("Dirección: " + direccionEnvio);
                
                // Crear la compra
                compra = new HistorialCompra(
                    idCompra,
                    usuarioId,
                    total,
                    direccionEnvio
                );
                
                // Agregar productos al historial
                if (compra != null) {
                    try {
                        int cantidadProductos = carrito.getCantidadProductos();
                        System.out.println("Intentando agregar " + cantidadProductos + " productos al historial...");
                        
                        for (int i = 0; i < cantidadProductos; i++) {
                            try {
                                Producto producto = carrito.obtenerProducto(i);
                                if (producto != null && producto.getNombre() != null) {
                                    System.out.println("Agregando producto al historial: " + producto.getNombre());
                                    compra.agregarProducto(producto);
                                } else {
                                    System.out.println("Producto nulo o sin nombre en la posición " + i);
                                }
                            } catch (Exception e) {
                                System.err.println("Error al obtener el producto " + i + ": " + e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        System.err.println("Error al obtener la cantidad de productos: " + e.getMessage());
                    }
                }
                
                // Registrar la compra en el historial
                HistorialService.getInstancia().agregarCompra(usuarioId, compra);
                System.out.println("Historial de compra registrado exitosamente");
                
            } catch (Exception e) {
                System.err.println("Error al crear el historial de compra: " + e.getMessage());
                e.printStackTrace();
                // No relanzamos la excepción para no interrumpir el flujo de compra
                // ya que el pedido ya fue creado exitosamente
            }
            
            return pedido;
            
        } catch (Exception e) {
            System.err.println("Error al crear el pedido: " + e.getMessage());
            e.printStackTrace();
            throw e; // Relanzar la excepción para manejarla en el controlador
        }
    }

    public Pedido procesarSiguientePedido() {
        Pedido pedido = colaPedidos.procesarSiguientePedido();
        if (pedido != null) {
            System.out.println("Pedido en proceso: " + pedido);
        }
        return pedido;
    }

    public ColaPedidos getColaPedidos() {
        return colaPedidos;
    }

    public int obtenerNumeroPedidosPendientes() {
        return colaPedidos.tamaño();
    }

    public void mostrarEstadoCola() {
        colaPedidos.mostrarEstadoCola();
    }
}
