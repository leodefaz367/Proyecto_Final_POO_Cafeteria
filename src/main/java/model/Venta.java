package model;

public class Venta {
    private int idVenta;
    private String fecha;
    private double total;
    private int idCliente;
    private int idCajero;

    public Venta(int idVenta, String fecha, double total, int idCliente, int idCajero) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
        this.idCliente = idCliente;
        this.idCajero = idCajero;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdCajero() {
        return idCajero;
    }

    public void setIdCajero(int idCajero) {
        this.idCajero = idCajero;
    }
}