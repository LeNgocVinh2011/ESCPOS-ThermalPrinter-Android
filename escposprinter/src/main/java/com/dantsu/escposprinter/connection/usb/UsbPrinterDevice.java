package com.dantsu.escposprinter.connection.usb;

public class UsbPrinterDevice {
    private final int device_id;
    private final String device_name;
    private final int product_id;
    private final int vendor_id;

    public UsbPrinterDevice(int device_id, String device_name, int product_id, int vendor_id) {
        this.device_id = device_id;
        this.device_name = device_name;
        this.product_id = product_id;
        this.vendor_id = vendor_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public String getDevice_name() {
        return device_name;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsbPrinterDevice)) return false;
        UsbPrinterDevice that = (UsbPrinterDevice) o;
        return device_id == that.device_id &&
                product_id == that.product_id &&
                vendor_id == that.vendor_id &&
                device_name.equals(that.device_name);
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(device_id);
        result = 31 * result + device_name.hashCode();
        result = 31 * result + Integer.hashCode(product_id);
        result = 31 * result + Integer.hashCode(vendor_id);
        return result;
    }

    @Override
    public String toString() {
        return "PrinterUSB{" +
                "device_id=" + device_id +
                ", device_name='" + device_name + '\'' +
                ", product_id=" + product_id +
                ", vendor_id=" + vendor_id +
                '}';
    }
}