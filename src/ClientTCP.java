package chatclient;

import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

class ClientTCP {

    public static void main(String args[]) {

        Socket sock;
        InputStream is;
        OutputStream os;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            sock = new Socket(InetAddress.getByName("localhost"), 1024);//создаем сокет клиента локально по порту 1024, как и сервер
            is = sock.getInputStream();//получили входной поток для чтения данных
            os = sock.getOutputStream();//получили выходной поток для записи данных
            System.out.println("Enter your nickname: ");
            String nickname = br.readLine();
            System.out.println("Now you can write your messages. For exit write disconnect");
            while (true) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                String message = sdf.format(cal.getTime()) + " " + nickname + ": " + br.readLine();
                if (message.contains("disconnect")) {
                    byte bytemessage1[] = message.getBytes();//преобразование сообщения из типа String в тип byte[] 
                    os.write(bytemessage1);//запись в выходной поток преобразованного сообщения
                    break;	//выход из цила while
                } else {
                    byte bytemessage2[] = message.getBytes();//преобразование сообщения из типа String в тип byte[] 
                    os.write(bytemessage2);//запись в выходной поток преобразованного сообщения   
                              byte readmessage[]=new byte[30];//создаем массив байт для чтения информации от сервера
				is.read(readmessage);//считываем сообщение посланное от сервера
				System.out.println( new String(readmessage));//выводим полученное сообщение  

                   System.out.println("Your next message: ");
                }
            }
                    is.close();//закрываем входной поток
                    os.close();//закрываем выходной поток
                    sock.close();//закрываем сокет клиента
        } catch (Exception e) {
            System.out.println("Error " + e.toString());



        }

    }
}
