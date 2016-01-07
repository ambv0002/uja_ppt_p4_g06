package ujaen.git.ppt;

// Añadimos la mayoría del código, implementando el método run().
// Inicialización de los stream de entrada y salida, envío del mensaje de bienvenida y máquina de estados.




import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

import ujaen.git.ppt.smtp.RFC5321;
import ujaen.git.ppt.smtp.RFC5322;
import ujaen.git.ppt.smtp.SMTPMessage;

public class Connection implements Runnable, RFC5322 {

	public static final int S_HELO = 0;
	public static final int S_MAIL = 1;
	public static final int S_RCPT = 2;
	public static final int S_DATA = 3;
	public static final int S_RSET = 4;
	public static final int S_QUIT = 5;
	
	//creacion de un nuevo objeto de la clase Socket
	protected Socket mSocket;
	protected int mEstado = S_HELO;;
	private boolean mFin = false;

	public Connection(Socket s) {
		mSocket = s;
		mEstado = 0;
		mFin = false;
	}

	@Override
	public void run() {
		//variables de entrada y salida de datos
		String inputData = null;
		String outputData = "";
		

		if (mSocket != null) {
			try {
				// Inicialización de los streams de entrada y salida
				DataOutputStream output = new DataOutputStream(
						mSocket.getOutputStream());
				BufferedReader input = new BufferedReader(
						new InputStreamReader(mSocket.getInputStream()));

				// Envío del mensaje de bienvenida
				String response = RFC5321.getReply(RFC5321.R_220) + SP + RFC5321.MSG_WELCOME
						+ RFC5322.CRLF;
				output.write(response.getBytes());
				output.flush();

				while (!mFin && ((inputData = input.readLine()) != null)) {
					
					System.out.println("Servidor [Recibido]> " + inputData);
				
					
					// Todo análisis del comando recibido
					SMTPMessage m = new SMTPMessage(inputData);
					if(m.getCommand()==null)
					{
						outputData= RFC5321.getError(RFC5321.E_500_SINTAXERROR) + SP + "ERROR EN COMANDO" + CRLF; 
			
					}
					else{
						mEstado=m.getCommandId();
						
					}

					// TODO: Máquina de estados del protocolo
					//comprobación de error
					if (m.getCommand()!=null){
					
						switch (mEstado) 
						{
								case S_HELO:
									//Comprobacion de mayusculas y minusculas
									if (m.getCommand().equalsIgnoreCase("HELO"))
									{
									outputData = RFC5321.getReply(RFC5321.R_250) + SP +
									 "Nice to meet you!" + CRLF;
									}
								break;
								case S_MAIL:
									
									if (m.getCommand().equalsIgnoreCase("MAIL FROM")){
										
										outputData=RFC5321.getReply(RFC5321.R_250)+ SP + "MAIL FROM" + CRLF;
									}
								case S_RCPT:
									
										if (m.getCommand().equalsIgnoreCase("RCPT TO")){
										
										outputData=RFC5321.getReply(RFC5321.R_250)+ SP + "RCPT TO" + CRLF;
										}
								case S_DATA:
									
									if (m.getCommand().equalsIgnoreCase("DATA")){
									
									outputData=RFC5321.getReply(RFC5321.R_354)+ SP + "DATA" + CRLF;
									}
								case S_RSET:
									
									if (m.getCommand().equalsIgnoreCase("RSET")){
										
										outputData=RFC5321.getReply(RFC5321.R_250)+ SP + "RESET" + CRLF;
										}
									
								
								default:
									outputData= RFC5321.getError(RFC5321.E_500_SINTAXERROR) + SP + "ERROR EN COMANDO" + CRLF; 
								break;
						}
					}
					// TODO montar la respuesta
					// El servidor responde con lo recibido
					outputData = RFC5321.getReply(RFC5321.R_220) + SP + inputData + CRLF;
					output.write(outputData.getBytes());
					output.flush();

				}
				System.out.println("Servidor [Conexión finalizada]> "
						+ mSocket.getInetAddress().toString() + ":"
						+ mSocket.getPort());

				input.close();
				output.close();
				mSocket.close();
			} catch (SocketException se) {
				se.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}
