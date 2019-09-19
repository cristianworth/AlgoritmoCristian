package algoritmocristian;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AlgoritmoCristian {

    private FileWriter arq;
    private PrintWriter gravarArq;
    Server server = new Server();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Date InitialServerDate;
    static Date FinalServerDate;
    Date InitialClientDate;
    static Date FinalClientDate;
    Long TempoProcessamentoServidor;

    public static String formataDataString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        String hour = Integer.toString(calendar.get(calendar.HOUR_OF_DAY));
        if (calendar.get(calendar.HOUR_OF_DAY) < 10) {
            hour = "0" + hour;
        }

        String minutes = Integer.toString(calendar.get(calendar.MINUTE));
        if (calendar.get(calendar.MINUTE) < 10) {
            minutes = "0" + minutes;
        }

        String seconds = Integer.toString(calendar.get(calendar.SECOND));
        if (calendar.get(calendar.SECOND) < 10) {
            seconds = "0" + seconds;
        }

        return "[" + hour + ":" + minutes + ":" + seconds + "] ";
    }

    public static Date addHoursToDate(Date date, int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, hours);
        return calendar.getTime();
    }

    public static Date addMinutesToDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    public static Date addSecondsToDate(Date date, int seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);

        return calendar.getTime();
    }

    public void geraLog() {
        gravarArq.println("###########################################");
        gravarArq.println("t0 = Tempo Cliente Envio: " + formataDataString(getInitialClientDate()));
        gravarArq.println("t1 = Tempo do Servidor Recebimento: " + formataDataString(server.dataInicial));
        gravarArq.println("t2 = Tempo do Servidor Envio: " + formataDataString(server.dataFinal));
        gravarArq.println("t3 = Tempo Cliente Recebimento: " + formataDataString(getFinalClientDate()));

        gravarArq.println("*** RTT ***");
        long a = getFinalClientDate().getTime() - getInitialClientDate().getTime();
        gravarArq.println("a -> (t3 - t0) = " + a);
        long b = server.dataFinal.getTime() - server.dataInicial.getTime();
        gravarArq.println("b -> (t2 - t1) = " + b);

        long rtt = (a-b) / 2;
        gravarArq.println("*** RTT divido por 2 ***");
        gravarArq.println("(a-b)/2 =  " + rtt);

        int rttEmSegundos = (int) TimeUnit.SECONDS.convert(rtt, TimeUnit.MILLISECONDS);
        gravarArq.println("*** Horario de Cristian ***");
        gravarArq.println("Horario de Cristian -> t2 + (rtt/2): " + formataDataString(addSecondsToDate(server.dataFinal, rttEmSegundos)));
    }

    public long sincronizaData() throws InterruptedException {
        Server server = new Server();
        //Thread.currentThread().sleep(1000);
        long diffClient = Math.abs(getFinalClientDate().getTime() - getInitialClientDate().getTime());

        //long i = TempoProcessamentoServidor;
        long diffServer = Math.abs(server.dataInicial.getTime() - server.dataFinal.getTime());
        long diffInMillies = (diffClient - diffServer) / 2;
        long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }

    public AlgoritmoCristian() {
        try {
            arq = new FileWriter("D:\\CC\\6º Sem\\5 - Sistemas Distribuídos\\LogSyncTime.txt");
            gravarArq = new PrintWriter(arq, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //public static void main(String[] args) {}

    public Date getInitialServerDate() {
        return InitialServerDate;
    }

    public void setInitialServerDate(Date InitialServerDate) {
        this.InitialServerDate = InitialServerDate;
    }

    public Date getFinalServerDate() {
        return FinalServerDate;
    }

    public void setFinalServerDate(Date FinalServerDate) {
        this.FinalServerDate = FinalServerDate;
    }

    public Date getInitialClientDate() {
        return InitialClientDate;
    }

    public void setInitialClientDate(Date InitialClientDate) {
        this.InitialClientDate = InitialClientDate;
    }

    public Date getFinalClientDate() {
        return FinalClientDate;
    }

    public void setFinalClientDate(Date FinalClientDate) {
        this.FinalClientDate = FinalClientDate;
    }

    public Long getTempoProcessamentoServidor() {
        return TempoProcessamentoServidor;
    }

    public void setTempoProcessamentoServidor(Long TempoProcessamentoServidor) {
        this.TempoProcessamentoServidor = TempoProcessamentoServidor;
    }

}
