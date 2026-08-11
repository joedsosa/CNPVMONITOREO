package hn.gob.ine.listener.util;

import java.io.IOException;
import java.io.OutputStream;

public class TeeOutputStream extends OutputStream {

    private final OutputStream consola;
    private final OutputStream archivo;

    public TeeOutputStream(OutputStream consola, OutputStream archivo) {
        this.consola = consola;
        this.archivo = archivo;
    }

    @Override
    public void write(int b) throws IOException {
        consola.write(b);
        archivo.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        consola.write(b, off, len);
        archivo.write(b, off, len);
    }

    @Override
    public void flush() throws IOException {
        consola.flush();
        archivo.flush();
    }
}
