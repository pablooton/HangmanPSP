package edu.pmoc.practicatrim.hangmanpsp.network.server;

import edu.pmoc.practicatrim.hangmanpsp.dao.PartidaDAO;
import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.model.Partida;
import java.io.*;
import javax.net.ssl.SSLSocket;

public class HiloCliente implements Runnable {
    private SSLSocket socket;
    private int idPropio;
    private LogicaPartida partida;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public HiloCliente(SSLSocket socket, int id, LogicaPartida partida) {
        this.socket = socket;
        this.idPropio = id;
        this.partida = partida;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());


            Jugador jugador = (Jugador) in.readObject();
            System.out.println("[CONSOLA] Jugador " + idPropio + " es " + jugador.getNombre());


            while (partida.isActiva()) {
                enviarEstadoACliente();

                if (partida.getTurnoActual() == idPropio) {
                    Object msg = in.readObject();
                    if (msg instanceof Character) {
                        partida.procesarJugada(idPropio, (Character) msg);
                    } else if ("CANCELAR".equals(msg)) {
                        partida.cancelarPartida();
                    }
                } else {
                    synchronized (partida) {
                        while (partida.getTurnoActual() != idPropio && partida.isActiva()) {
                            partida.wait();
                        }
                    }
                }
            }

            finalizarJuego(jugador);

        } catch (Exception e) {
            System.err.println("[CONSOLA] Error en hilo " + idPropio + ": " + e.getMessage());
            partida.cancelarPartida();
        } finally {
            desconectar();
        }
    }

    private void enviarEstadoACliente() throws IOException {
        out.writeObject(partida.getProgreso());
        out.writeInt(partida.getVidas(idPropio));
        out.writeBoolean(partida.getTurnoActual() == idPropio);
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
            System.out.println("[CONSOLA] Puntos guardados para: " + j.getNombre());
        }

        out.writeObject("FIN");
        out.flush();
    }

    private void desconectar() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) { e.printStackTrace(); }
    }
}