package ru.miron.nonstop.logic;

import java.io.IOException;
import java.net.ConnectException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;

import ru.miron.nonstop.logic.commands.Command;
import ru.miron.nonstop.logic.commands.CommandAnswer;
import ru.miron.nonstop.logic.dao.Config;
import ru.miron.nonstop.logic.util.json.JSON;

public class UDPClient {
    public final InetSocketAddress serverSocketAddress;
    public final DatagramSocket serverSocket;
    public long connectionTriesCount;

    private final static int BYTES_BUFFER_SIZE = 64 * 1024;

    public UDPClient(Config config) throws SocketException {
        serverSocketAddress = new InetSocketAddress(config.hostname, config.port);
        if (serverSocketAddress.isUnresolved()) {
            throw new IllegalStateException();
        }
        serverSocket = new DatagramSocket();
        serverSocket.setSoTimeout(config.connectionTryWaiting);
        connectionTriesCount = config.connectionTriesCount;
    }
    
    public void send(Command command) throws IOException {
        String commandJSONString = command.toJSON().toString();
        System.out.println("command string to send: " + commandJSONString);
        serverSocket.send(genSendPacket(commandJSONString));
    }

    public String receiveString() throws IOException {
        byte[] receivingDataBuffer = new byte[BYTES_BUFFER_SIZE];
        DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length); 
        serverSocket.receive(inputPacket);
        return new String(inputPacket.getData(), StandardCharsets.UTF_8);
    }

    public DatagramPacket genSendPacket(String toSend) {
        byte[] toSendBytes = toSend.getBytes(StandardCharsets.UTF_8);
        DatagramPacket outputPacket = new DatagramPacket(
            toSendBytes, 
            toSendBytes.length,
            serverSocketAddress
        );
        return outputPacket;
    }

    /**
     * @return {@code null} if didn't get answer
     * @throws ConnectException if didn't get answer
     */
    public String tryToGetStringAnswer(Command commandToSend) throws ConnectException {
        String answer = null;
        for (int i = 0; i < connectionTriesCount; i++) {
            try {
                send(commandToSend);
                answer = receiveString();
                break;
            } catch (SocketTimeoutException e) {
                System.out.println("lol, didnt read");
            } catch (IOException e) {
                System.out.println("lol, ioex");
            }
        }
        if (answer == null) {
            throw new ConnectException();
        }
        return answer;
    }

    /**
     * @return {@code null} if didn't get answer
     * @throws ConnectException if didn't get string answer
     * @throws IllegalArgumentException if has any parse or wrong struct errors
     */
    public CommandAnswer tryToGetCommandAnswer(Command commandToSend) throws ConnectException, IllegalArgumentException {
        String answer = tryToGetStringAnswer(commandToSend);
        System.out.println(answer);
        try {
            var root = JSON.parse(answer).asMap().getValues();
            return CommandAnswer.initFromTheRoot(commandToSend.getCommandName(), root);
        } catch (IllegalStateException | ClassCastException e) {
            throw new IllegalArgumentException(); 
        }
    }

}
