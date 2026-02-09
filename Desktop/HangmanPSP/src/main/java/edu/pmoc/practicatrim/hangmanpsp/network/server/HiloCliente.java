package edu.pmoc.practicatrim.hangmanpsp.network.server;

import edu.pmoc.practicatrim.hangmanpsp.dao.PartidaDAO;
import edu.pmoc.practicatrim.hangmanpsp.model.EstadoPartida;
import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.model.Partida;
import java.io.*;
import javax.net.ssl.SSLSocket;
import java.net.SocketException;

public class HiloCliente implements Runnable {
    private SSLSocket socket;
    private int idPropio;
    private LogicaPartida partida;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Jugador jugador;

    public HiloCliente(SSLSocket socket, int id, LogicaPartida partida) {
        this.socket = socket;
        this.idPropio = id;
        this.partida = partida;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());

            this.jugador = (Jugador) in.readObject();
            System.out.println("[SERVIDOR] Jugador conectado: " + jugador.getNombre());

            while (partida.isActiva()) {
                enviarEstado(false, null);

                try {
                    Object msg = in.readObject();
                    procesarMensaje(msg);
                } catch (SocketException se) {
                    break;
                }
            }
            finalizarJuego(jugador);

        } catch (Exception e) {
            System.err.println("[SERVIDOR] Error en hilo " + idPropio + ": " + e.getMessage());
            partida.cancelarPartida();
        } finally {
            desconectar();
        }
    }

    private void procesarMensaje(Object msg) throws IOException {
        if ("PUNTUACION".equals(msg)) {
            Long puntos = new PartidaDAO().obtenerPuntuacionTotal(jugador.getId());
            out.writeObject("PUNTUACION:" + puntos);
            out.flush();
            out.reset();
        }
        else if ("CANCELAR".equals(msg)) {
            partida.cancelarPartida();
        }
        else if (msg instanceof Character) {
            if (partida.getTurnoActual() == idPropio) {
                partida.procesarJugada(idPropio, (Character) msg);
            }
        }
    }

    private void enviarEstado(boolean terminado, String msgFinal) throws IOException {
        if (socket.isClosed()) return;
        EstadoPartida estado = new EstadoPartida(
                partida.getProgreso(),
                partida.getVidas(idPropio),
                partida.getTurnoActual() == idPropio,
                terminado,
                msgFinal
        );
        out.writeObject(estado);
        out.flush();
        out.reset();
    }

    private void finalizarJuego(Jugador j) throws IOException {
        boolean ganado = !partida.getProgreso().contains("_");
        if (ganado && !partida.isCancelada() && partida.getTurnoActual() == idPropio) {
            int puntos = partida.calcularPuntos();
            Partida p = new Partida();
            p.setJugador(j);
            p.setAcertado(true);
            p.setPuntuacionObtenida(puntos);
            p.setFechaHora(java.time.LocalDateTime.now());
            new PartidaDAO().guardarPartida(p);
        }
        enviarEstado(true, "FIN");
    }

    private void desconectar() {
        try {
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) { }
    }
}