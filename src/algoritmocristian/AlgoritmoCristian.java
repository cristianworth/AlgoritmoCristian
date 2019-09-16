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
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Date InitialServerDate;
    Date FinalServerDate;
    Date InitialClientDate;
    Date FinalClientDate;
    Long TempoProcessamentoServidor;
    
    public static String formataDataString(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        String hour = Integer.toString(calendar.get(calendar.HOUR_OF_DAY));
        if (calendar.get(calendar.HOUR_OF_DAY)<10) {
            hour = "0"+hour;
        }
        
        String minutes = Integer.toString(calendar.get(calendar.MINUTE));
        if (calendar.get(calendar.MINUTE)<10) {
            minutes = "0"+minutes;
        }
        
        String seconds = Integer.toString(calendar.get(calendar.SECOND));
        if (calendar.get(calendar.SECOND)<10) {
            seconds = "0"+seconds;
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
    
    public void gravaLog(String texto){
        gravarArq.println(texto);
    }
    
    public long sincronizaData() throws InterruptedException{
        //Thread.currentThread().sleep(1000);
        long diffClient = Math.abs(getFinalClientDate().getTime() - getInitialClientDate().getTime());
        
        long i = TempoProcessamentoServidor;
        long diffInMillies = (diffClient-i)/2;
        long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }
     
    public AlgoritmoCristian(){
        try {
        arq = new FileWriter("D:\\CC\\6º Sem\\5 - Sistemas Distribuídos\\LogSyncTime.txt");
        gravarArq = new PrintWriter(arq, true);
        } catch (Exception e) {
             e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        //AlgoritmoCristian algoritmoCristian = new AlgoritmoCristian();
        //System.out.println("Date now +1: " + addHoursToDate(new Date(), 1));
        System.out.println("Date now +1: " + addMinutesToDate(new Date(), 30));
        long timezoneAlteredTime = TimeZone.getTimeZone("GMT+1:00").getRawOffset();
        System.out.println("timezoneAlteredTime: "+timezoneAlteredTime);
    }

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
