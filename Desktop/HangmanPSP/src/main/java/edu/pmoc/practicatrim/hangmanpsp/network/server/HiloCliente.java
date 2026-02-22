package edu.pmoc.practicatrim.hangmanpsp.network.server;

import edu.pmoc.practicatrim.hangmanpsp.dao.PalabraDao;
import edu.pmoc.practicatrim.hangmanpsp.dao.PartidaDAO;
import edu.pmoc.practicatrim.hangmanpsp.model.EstadoPartida;
import edu.pmoc.practicatrim.hangmanpsp.model.Jugador;
import edu.pmoc.practicatrim.hangmanpsp.model.Partida;
import java.io.*;
import java.net.SocketException;
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

            while (!socket.isClosed()) {
                enviarEstado();

                if (!partida.isActiva()) break;

                synchronized (partida) {
                    while (partida.getTurnoActual() != idPropio && partida.isActiva()) {
                        partida.wait();
                        if (!socket.isClosed()) enviarEstado();
                    }
                }

                if (!partida.isActiva()) break;

                try {
                    Object msg = in.readObject();
                    if (msg instanceof Character) {
                        char letra = (Character) msg;
                        synchronized (partida) {
                            partida.procesarJugada(idPropio, letra);
                            if (!partida.getProgreso().contains("_")) {
                                int puntos = (partida.getPalabraSecreta().length() < 10) ? 1 : 2;
                                registrarResultadoEnBD(true, puntos);
                                String nueva = PalabraDao.getPalabraSecreta();
                                partida.iniciarNuevaRonda(nueva);
                            } else if (!partida.isActiva()) {
                                registrarResultadoEnBD(false, 0);
                            }
                            partida.notifyAll();
                        }
                    } else if (msg instanceof String) {
                        String texto = (String) msg;
                        if ("PUNTUACION".equals(texto)) {
                            long puntos = new PartidaDAO().obtenerPuntuacionTotal(jugador.getId());
                            out.writeObject("PUNTUACION:" + puntos);
                            out.flush();
                        } else if ("CANCELAR".equals(texto)) {
                            partida.cancelarPartida();
                            synchronized (partida) {
                                partida.notifyAll();
                            }
                            // No hacemos break aquí para que el ciclo termine
                            // y se limpie en el siguiente flujo
                        }
                    }
                } catch (EOFException | SocketException e) {
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Sesion finalizada.");
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
            new PartidaDAO().guardarPartida(record);
        } catch (Exception e) {}
    }

    private void enviarEstado() throws IOException {
        if (socket.isClosed()) return;
        String mensaje = "";

        if (!partida.isActiva()) {
            mensaje = "PARTIDA FINALIZADA/CANCELADA";
        } else {
            if (partida.getVidas(idPropio) <= 0) {
                mensaje = "Sin vidas. Esperando rival...";
            } else {
                mensaje = (partida.getTurnoActual() == idPropio) ? "Es tu turno" : "Turno del rival";
                if (partida.huboFalloRival(idPropio)) {
                    mensaje = "¡Tu rival ha fallado! " + mensaje;
                }
            }
        }

        EstadoPartida ep = new EstadoPartida(
                partida.getProgreso(),
                partida.getVidas(idPropio),
                partida.getTurnoActual() == idPropio && partida.isActiva(),
                !partida.isActiva(),
                mensaje,
                partida.getLetrasAcertadas()
        );
        out.writeObject(ep);
        out.flush();
        out.reset();
    }

    private void desconectar() {
        try { if (socket != null && !socket.isClosed()) socket.close(); } catch (IOException e) {}
    }
}