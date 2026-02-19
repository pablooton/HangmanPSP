package edu.pmoc.practicatrim.hangmanpsp.network.server;

import edu.pmoc.practicatrim.hangmanpsp.dao.PalabraDao;
import edu.pmoc.practicatrim.hangmanpsp.dao.PartidaDAO;
import edu.pmoc.practicatrim.hangmanpsp.model.EstadoPartida;
import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.model.Partida;

import java.io.*;
import javax.net.ssl.SSLSocket;

public class HiloCliente implements Runnable {
    private SSLSocket socket;
    private int idPropio;
    private LogicaPartida partida;
    private ObjectOutputStream out;
    private Jugador jugador;

    public HiloCliente(SSLSocket s, int id, LogicaPartida p) {
        this.socket = s; this.idPropio = id; this.partida = p;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            this.jugador = (Jugador) in.readObject();
            System.out.println("Jugador conectado: " + jugador.getNombre());

            while (partida.isActiva()) {
                enviarEstado();

                synchronized (partida) {
                    while (partida.getTurnoActual() != idPropio && partida.isActiva()) {
                        partida.wait();
                        enviarEstado();
                    }
                }

                if (!partida.isActiva()) break;

                Object msg = in.readObject();

                if (msg instanceof Character) {
                    char letra = (Character) msg;
                    partida.procesarJugada(idPropio, letra);
                    if (!partida.getProgreso().contains("_")) {
                        int puntos = (partida.getPalabraSecreta().length() < 10) ? 1 : 2;
                        registrarResultadoEnBD(true, puntos);

                        String nueva = PalabraDao.getPalabraSecreta();
                        synchronized (partida) {
                            partida.iniciarNuevaRonda(nueva);
                            partida.notifyAll();
                        }
                    }
                    else if (!partida.isActiva()) {
                        registrarResultadoEnBD(false, 0);
                        synchronized (partida) {
                            partida.notifyAll();
                        }
                    }
                    else {
                        synchronized (partida) {
                            partida.notifyAll();
                        }
                    }
                }
                else if (msg instanceof String) {
                    String texto = (String) msg;
                    if ("PUNTUACION".equals(texto)) {
                        long puntosTotales = new PartidaDAO().obtenerPuntuacionTotal(jugador.getId());
                        out.writeObject("PUNTUACION:" + puntosTotales);
                        out.flush();
                    } else if ("CANCELAR".equals(texto)) {
                        partida.cancelarPartida();
                        synchronized (partida) { partida.notifyAll(); }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error en HiloCliente " + idPropio + ": " + e.getMessage());
        } finally {
            desconectar();
        }
    }

    private void registrarResultadoEnBD(boolean victoria, int puntos) {
        try {
            Partida record = new Partida();
            record.setJugador(this.jugador);
            record.setAcertado(victoria);
            record.setPuntuacionObtenida(puntos);

            PartidaDAO dao = new PartidaDAO();
            dao.guardarPartida(record);

            System.out.println("[Hibernate] Partida registrada para " + jugador.getNombre() + ": " + puntos + " puntos.");
        } catch (Exception e) {
            System.err.println("Error al persistir datos: " + e.getMessage());
        }
    }
    private void enviarEstado() throws IOException {
        if (socket.isClosed()) return;
        EstadoPartida ep = new EstadoPartida(
                partida.getProgreso(),
                partida.getVidas(idPropio),
                partida.getTurnoActual() == idPropio,
                !partida.isActiva(),
                !partida.isActiva() ? "PARTIDA FINALIZADA" : ""
        );
        out.writeObject(ep);
        out.flush();
        out.reset();
    }

    private void desconectar() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {}
    }
}