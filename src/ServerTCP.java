package chatserver;

import java.net.*;
import java.io.*;

class ServerTCP {

    static int countclients = 0;//счетчик подключившихся клиентов	

    public static void main(String args[]) {
        ServerSocket sock;
        try {
            sock = new ServerSocket(1024);//создаем серверный сокет работающий локально по порту 1024
            while (true)//бесконечный цикл для возможности подключения параллельно нескольких клиентов
            {
                Socket client = sock.accept();//сработает, когда клиент подключится, для него выделится отдельный сокет client
                new NewThread(client);//вызываем конструктор класса NewThread

            }
        } catch (Exception e) {
            System.out.println("Error " + e.toString());

        }

    }
}

class NewThread implements Runnable {

    Socket client = null;
    InputStream is;
    OutputStream os;

    NewThread(Socket clientsock)//конструктор класса NewThread
    {
        client = clientsock;
        Thread t = new Thread(this);//создаем дочерний поток, который ссылается на объект текущего класса
        t.start();//запуск метода run()
    }

    public void run() {
        try {
            ServerTCP.countclients++;//количество подключившихся клиентов увеличивается на 1
            System.out.println("Client  " + ServerTCP.countclients + "  connected");//вывод сообщения

            is = client.getInputStream();//получили входной поток для чтения данных
            os = client.getOutputStream();//получили выходной поток для записи данных
            boolean flag = true;

            while (flag == true) {//цикл для работы с клиентом

                byte clientMessage[] = new byte[30];//байтовый массив, который будет использоваться при чтении из входного потока

                is.read(clientMessage);//чтение иформации, посланной клиентом, из вхоного потока в массив clientMessage[]
                String clientmessage = new String(clientMessage, 0, clientMessage.length);

                if (clientmessage.contains("disconnect")) {
                    System.out.println("receaved from client 'disconnect' ");
                    flag = false;
                } else {

                    os.write(clientMessage);//запись в выходной поток полученной от клиента информации 
                    System.out.println(new String(clientMessage));//вывод сообщения
                }


            }
            is.close();//закрытие входного потока
            os.close();//закрытие выходного потока
            client.close();//закрытие сокета, выделенного для работы с подключившимся клиентом
            System.out.println("Client disconnected");

        } catch (Exception e) {
            System.out.println("Error is " + e.toString());
        }
    }
}
