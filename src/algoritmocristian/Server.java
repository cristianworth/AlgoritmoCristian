package algoritmocristian;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class Server extends Thread {
    private static ArrayList<BufferedWriter> listaDeUsuarios;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    //Date dataInicial = new Date(2019, 9, 19, 20, 30, 11);
    Date dataInicial = new Date(1571447410000l);//2019-09-18 22:10:10
    Date dataFinal = algoritmoCristian.addSecondsToDate(dataInicial, 60);
    private static AlgoritmoCristian algoritmoCristian = new AlgoritmoCristian();
    private static ServerSocket server;
    private String nome;
    private Socket socketServior;
    private InputStream input;
    private InputStreamReader inputReader;
    private BufferedReader buffReader;
    
    public Server(){}
    
    public void setDataServidor() throws InterruptedException{
        Date dataInicial = new Date();
        Date dataFinal;
        algoritmoCristian.setInitialServerDate(dataInicial);
        
        dataFinal=algoritmoCristian.addSecondsToDate(dataInicial, 60); //delay tempo de processamento;
        algoritmoCristian.setFinalServerDate(dataFinal);
        
        long tempoProcessamento = Math.abs(dataFinal.getTime() - dataInicial.getTime());
        algoritmoCristian.setTempoProcessamentoServidor(tempoProcessamento);;
    }
    
    public Server(Socket con) throws ParseException {
        dataInicial = formatter.parse("19-09-2019 12:00:00");
        dataFinal = formatter.parse("19-09-2019 12:01:00");

        this.socketServior = con;
        try {
            input = con.getInputStream();
            inputReader = new InputStreamReader(input);
            buffReader = new BufferedReader(inputReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String msg;
            OutputStream ou = this.socketServior.getOutputStream();
            Writer ouw = new OutputStreamWriter(ou);
            BufferedWriter bfw = new BufferedWriter(ouw);
            listaDeUsuarios.add(bfw);
            nome = msg = buffReader.readLine();

            while (msg != null) {
                msg = buffReader.readLine();
                sendToAll(bfw, msg);
                System.out.println(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    
    public void sendToAll(BufferedWriter bwSaida, String msg) throws IOException, ParseException {
        System.out.println("datafinal no server: " + dataFinal);
        BufferedWriter bwS;
        for (BufferedWriter bw : listaDeUsuarios) {
            bwS = (BufferedWriter) bw;
            if (!(bwSaida == bwS)) {
                bw.write(nome + ": " + msg + "\r\n");
                bw.flush();
            }
        }
    }

    public static void main(String[] args) {
        try {
            server = new ServerSocket(12345);
            listaDeUsuarios = new ArrayList<BufferedWriter>();
            JOptionPane.showMessageDialog(null, "Servidor Iniciado com Sucesso");
            //setDataServidor();
            //dataFinal = new Date();
            
            while (true) {
                System.out.println("Aguardando conexão...");
                Socket con = server.accept();
                System.out.println("Cliente conectado...");
                Thread t = new Server(con);
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
