package org.example.tiendav2.service;

import org.example.tiendav2.model.HistorialCompra;
import org.example.tiendav2.model.Producto;

import java.util.*;
import java.util.stream.Collectors;

public class HistorialService {
    private static HistorialService instancia;
    private final Map<Integer, List<HistorialCompra>> historialPorUsuario;
    private int ultimoId;

    private HistorialService() {
        this.historialPorUsuario = new HashMap<>();
        this.ultimoId = 0;
    }

    public static synchronized HistorialService getInstancia() {
        if (instancia == null) {
            instancia = new HistorialService();
        }
        return instancia;
    }

    public void agregarCompra(int usuarioId, HistorialCompra compra) {
        historialPorUsuario.computeIfAbsent(usuarioId, k -> new ArrayList<>()).add(compra);
    }

    public List<HistorialCompra> obtenerHistorialUsuario(int usuarioId) {
        return historialPorUsuario.getOrDefault(usuarioId, new ArrayList<>())
                .stream()
                .sorted(Comparator.comparing(HistorialCompra::getFechaCompra).reversed())
                .collect(Collectors.toList());
    }

    public HistorialCompra obtenerCompra(int usuarioId, int compraId) {
        return historialPorUsuario.getOrDefault(usuarioId, new ArrayList<>())
                .stream()
                .filter(c -> c.getId() == compraId)
                .findFirst()
                .orElse(null);
    }

    public int generarNuevoId() {
        return ++ultimoId;
    }
}
