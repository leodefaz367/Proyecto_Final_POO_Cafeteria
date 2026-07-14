package model;

public class DetalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idProducto;
    private String nombreProducto;
    private int cantidad;
    private double precio;
    private double subtotal;

    public DetalleVenta(int idProducto, String nombreProducto, int cantidad, double precio, double subtotal) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.subtotal = subtotal;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public double getPrecio() { return precio; }
    public double getSubtotal() { return subtotal; }

    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
}