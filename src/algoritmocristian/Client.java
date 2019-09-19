package algoritmocristian;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class Client extends JFrame implements ActionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private boolean usarDataServidor = false;
    private Date ServerDate;
    private Date ClientDate;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private AlgoritmoCristian algoritmoCristian = new AlgoritmoCristian();
    private Server server = new Server();
    private JTextArea areaTexto;
    private JTextField jtfMensagem;
    private JButton btnAtualizar;
    private JButton btnEnviar;
    private JLabel lblNome;
    private JLabel jblMensagem;
    private JPanel jpPainelPrincipal;
    private Socket socket;
    private OutputStream saidaStream;
    private Writer writer;
    private BufferedWriter buffWriter;
    private JTextField jtfIpServidor;
    private JTextField jtfPortaServidor;
    private JTextField jtfNomeUsuario;

    public Client() throws IOException, ParseException, InterruptedException {
        ClientDate = new Date();
        System.out.println("ClientDate: " + ClientDate);
        ServerDate=geraDataCristian();

        JLabel lblMessage = new JLabel("Digite seu nome abaixo: ");
        jtfIpServidor = new JTextField("127.0.0.1");
        jtfPortaServidor = new JTextField("12345");
        jtfNomeUsuario = new JTextField("Cliente");
        Object[] texts = {lblMessage, jtfNomeUsuario};
        JOptionPane.showMessageDialog(null, texts);
        jpPainelPrincipal = new JPanel();
        areaTexto = new JTextArea(15, 30);
        areaTexto.setEditable(false);
        areaTexto.setBackground(new Color(240, 240, 240));
        jtfMensagem = new JTextField(24);
        lblNome = new JLabel(jtfNomeUsuario.getText());
        btnAtualizar = new JButton("Sinc. Hora");
        btnAtualizar.setToolTipText("Atualizar Hora");
        btnAtualizar.addActionListener(this);
        jblMensagem = new JLabel("Digite sua Mensagem:");
        btnEnviar = new JButton("Enviar");
        btnEnviar.setToolTipText("Enviar Mensagem");
        btnEnviar.addActionListener(this);
        btnEnviar.addKeyListener(this);
        jtfMensagem.addKeyListener(this);
        JScrollPane scroll = new JScrollPane(areaTexto);
        areaTexto.setLineWrap(true);

        jpPainelPrincipal.add(lblNome);
        jpPainelPrincipal.add(scroll);
        //jpPainelPrincipal.add(jblMensagem);
        jpPainelPrincipal.add(jtfMensagem);
        jpPainelPrincipal.add(btnEnviar);
        //jpPainelPrincipal.add(btnAtualizar);
        jpPainelPrincipal.setBackground(Color.LIGHT_GRAY);
        setTitle("Chat");
        setContentPane(jpPainelPrincipal);
        setLocationRelativeTo(null);
        setResizable(false);
        setSize(375, 350);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public Date geraDataCristian() throws InterruptedException, ParseException {
        Date dataInicial = formatter.parse("19-09-2019 09:00:00");
        Date dataFinal = formatter.parse("19-09-2019 09:01:06");
        //Date dataInicial = new Date();
        //Date dataFinal;

        algoritmoCristian.setInitialClientDate(dataInicial);
        dataFinal = algoritmoCristian.addSecondsToDate(dataInicial, 66);
        algoritmoCristian.setFinalClientDate(dataFinal);

        Date dataServidor = server.dataFinal;

        algoritmoCristian.geraLog();
        int retorno = (int) algoritmoCristian.sincronizaData();
        //ServerDate = algoritmoCristian.addSecondsToDate(dataServidor, retorno);
        return algoritmoCristian.addSecondsToDate(dataServidor, retorno);
    }

    public void conectar() throws IOException {
        socket = new Socket(jtfIpServidor.getText(), Integer.parseInt(jtfPortaServidor.getText()));
        saidaStream = socket.getOutputStream();
        writer = new OutputStreamWriter(saidaStream);
        buffWriter = new BufferedWriter(writer);
        buffWriter.write(jtfNomeUsuario.getText() + "\r\n");
        buffWriter.flush();
    }

    public void enviarMensagem(String msg) throws IOException, Exception {
        String dataFormatadaString = "";
        //geraDataCristian();
        dataFormatadaString = algoritmoCristian.formataDataString(ServerDate);
        //dataFormatadaString = algoritmoCristian.formataDataString(ClientDate);
        
        buffWriter.write(jtfMensagem.getText() + "\r\n");
        areaTexto.append(dataFormatadaString + jtfNomeUsuario.getText() + ": " + jtfMensagem.getText() + "\r\n");

        buffWriter.flush();
        jtfMensagem.setText("");
    }

    public void escutar() throws IOException, InterruptedException, ParseException {
        InputStream in = socket.getInputStream();
        InputStreamReader inr = new InputStreamReader(in);
        BufferedReader bfr = new BufferedReader(inr);
        String msg = "";
        String dataFormatadaString = "";
        
        //geraDataCristian();
        dataFormatadaString = algoritmoCristian.formataDataString(ServerDate);
        //dataFormatadaString = algoritmoCristian.formataDataString(ClientDate);
        
        while (true) {
            if (bfr.ready()) {
                msg = bfr.readLine();
                areaTexto.append(dataFormatadaString + msg + "\r\n");
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equals(btnEnviar.getActionCommand())) {
                enviarMensagem(jtfMensagem.getText());
            }
            if (e.getActionCommand().equals(btnAtualizar.getActionCommand())) {
                usarDataServidor = !usarDataServidor;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                enviarMensagem(jtfMensagem.getText());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        Client cliente = new Client();
        cliente.conectar();
        cliente.escutar();
    }
}
