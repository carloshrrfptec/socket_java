/**
 * Aula realizada em 16/02/2021
 * 
 * PERGUNTAS
 * **** ATENÃ‡ÃƒO! **** ESTA CLASSE CLIENTE Nï¿½O DEVE SER EXECUTADA PRIMEIRO, PORQUE DEVE-SE ESTABELECER UMA CONEXÃƒO COM A PORTA 
 * FORNCECIDA PARA O SERVIDOR 4.4 PG3
 * 
 * Por que a classe apresenta erros? Como corrigir?
 * RESPOSTA: 
 * 
 * Qual classe deve ser executada primeiro?
 * RESPOSTA: A classe a ser executada primeiro deverÃ¡ ser a classe Servidor. 
 * Se for executado primeiro a classe Cliente.java a conexÃ£o serÃ¡ recusada porque nï¿½o terï¿½ porta aberta para poder conversar.
 * 
 * O que acontece quando um novo cliente envia mensagem para o servidor (alternando de um cliente para o outro)?
 * RESPOSTA: A classe Servidor.java conseguira conversar apenas com um cliente por vez. Isso ocorre PORQUE o cliente estÃ¡ ocupando a mesma
 * porta. Seguindo o protocolo TCP, ele estabelece a conexï¿½o com aquela porta e fica comunicando com aquela porta. O segundo cliente sabe
 * que aquela porta existe, porï¿½m o servidor faz uma fila.
 * 
 * Como posso fazer para vÃ¡rios clientes se conectar na MESMA PORTA e conversar com o servidor ao mesmo tempo sem ter o enfileiramento de mensagem?
 * RESPOSTA: Multitrheads/Multithreading
 * Para que o servidor seja capaz de trabalhar com dois clientes ao mesmo tempo Ã© necessÃ¡rio criar uma thread logo apÃ³s executar o mÃ©todo accept.
 * A thread criada serÃ¡ responsÃ¡vel pelo tratamento dessa conexÃ£o, enquanto o laï¿½o do servidor disponibilizarÃ¡ a porta para uma nova conexÃ£o.
 *  
 * Por que no SERVIDOR eu crio um ServerSocket e também preciso criar um Socket?
 * RESPOSTA: Quando eu crio o ServerSocket eu abro a porta. Para obter Resposta fazer um handshake
 * 
 */

package br.com.promove.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

@SuppressWarnings({ "unused", "resource" })
public class Servidor extends Thread {

	// VARIÁVEIS PARA CONEXÃO
	private static Vector<DataOutputStream> salaEconomia = new Vector<DataOutputStream>();
	private static Vector<DataOutputStream> salaEntretenimento = new Vector<DataOutputStream>();
	private static Vector<DataOutputStream> salaTecnologia = new Vector<DataOutputStream>();

	public static void main(String[] args) throws IOException {
		// CRIA SOCKET DE COMUNICAÇÃO COM OS CLIENTES NA PORTA 9000 (MESMA PORTA
		// NO CLIENTE)
		int porta = 9000;
		ServerSocket servidor = new ServerSocket(porta);
		// FICA AGUARDANDO A MENSAGEM DE ALGUM CLIENTE E TRATA
		System.out.println("Porta " + servidor.getLocalPort() + " aberta. Aguardando cliente se conectar...\n");

		while (true) {

			// AGUARDA ALGUM CLIENTE CONECTAR.
			// A EXECUÇÃO DO SERVIDOR FICA BLOQUEADA NA CHAMADA DO MÉTODO accept CLASSE
			// ServerSocket ATÉ QUE ALGUM
			// CLIENTE SE CONECTE AO SERVIDOR.
			// O PRÓPRIO MÉDO DESBLOQUEIA E RETORNA COM UM OBJETO DA CLASSE Socket
			Socket cnnx = servidor.accept(); // QUANDO RECEBE CONEXÃO DO CLIENTE EU TENHO UM IP E
												// PORTA. E VAO PARA VARIÁVEL CONEXÃO

			// O IP DO CLIENTE CRIA UMA NOVA THREAD PARA TRATAR ESTA CONEXÃƒO
			Thread thread = new Servidor(cnnx);
			thread.start();// INICIA A THREAD
			System.out.println("Cliente conectado!");
			// VOLTA AO LOOP E ESPERA MAIS ALGUÉM SE CONECTAR.
//			System.out.println("Conectado ao endereço " + conexao.getInetAddress().getHostAddress() + ":" + porta + "\n");
		}
	}

	// CRIA UM MÉTODO CONSTRUTOR PARA O SERVIDOR
	private Socket conexao;

	public Servidor(Socket s) {
		conexao = s;
	}// fim método construtor Servidor

	// MÉTODO PRIVADO PARA CAPTURA E FORMATAÇÃO DA DATA E HORA
	private String dataAtual() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

	public void run() {
		String nomeDoCliente;
		String msgRecebida;
		String msgEnviada;
		String escolhaSala;

		BufferedReader entradaCliente;

		try {
			entradaCliente = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			DataOutputStream saidaCliente = new DataOutputStream(conexao.getOutputStream());

			nomeDoCliente = entradaCliente.readLine();
			saidaCliente.writeBytes("[" + dataAtual() + "] SERVIDOR DIZ: SEJA BEM VINDO " + nomeDoCliente + "!\n");
			escolhaSala = entradaCliente.readLine();

			Integer i;
			Vector<DataOutputStream> v;

			switch (escolhaSala) {
			case "1":
				salaEconomia.add(saidaCliente);
				v = salaEconomia;
				escolhaSala = "ECONOMIA";
				break;
			case "2":
				salaEntretenimento.add(saidaCliente);
				v = salaEntretenimento;
				escolhaSala = "ENTRETENIMENTO";
				break;
			case "3":
				salaTecnologia.add(saidaCliente);
				v = salaTecnologia;
				escolhaSala = "TECNOLOGIA";
				break;
			default:
				salaTecnologia.add(saidaCliente);
				v = salaTecnologia;
				escolhaSala = "TECNOLOGIA";
				break;
			}

			i = 0;
			while (i < v.size()) {// DIZ EM QUAL SALA O CLIENTE ENTROU
				v.get(i).writeBytes("[" + dataAtual() + "] SERVIDOR DIZ: " + nomeDoCliente + " entrou na sala "
						+ escolhaSala + "\n");
				i++;
			}

			msgRecebida = entradaCliente.readLine();

			while (msgRecebida != null && !(msgRecebida.trim().equals("")) && !(msgRecebida.startsWith("fim"))) {

				// MOSTRA MENSAGEM RECEBIDA NO CONSOLE
				System.out.println(nomeDoCliente + ": " + msgRecebida);

				// MONTA RETORNO PARA O CLIENTE
				msgEnviada = "[" + dataAtual() + " " + escolhaSala + "] " + nomeDoCliente + " diz>>> " + msgRecebida
						+ "\n";

				// ENVIA MENSAGEM PARA TODOS, COM EXCEÇÃO DELE MESMO
				i = 0;
				while (i < v.size()) {
					if (v.get(i) != saidaCliente) {
						v.get(i).writeBytes(msgEnviada);
					} // fim if
					i++;
				} // fim while

				// ESPERA UMA NOPVA MENSAGEM PARA O CLIENTE
				msgRecebida = entradaCliente.readLine();

			} // fim while

			i = 0;
			while (i < v.size()) {
				v.get(i).writeBytes("[" + dataAtual() + "] SERVIDOR diz: " + nomeDoCliente + " saiu da sala\n");
				i++;
			}

			// REMOVE DO VETOR
			i = 0;
			while (i < v.size()) {
				if (v.get(i) == saidaCliente) {
					v.remove(v.get(i));
					System.out.println("Cliente desconectado!");
				} // fim if
				i++;
			} // fim while

			conexao.close();

		} catch (IOException e) {
			// Caso ocorra alguma excessÃ£o de E/S, mostre qual foi.
			System.out.println("Falha na Conexao..." + " IOException: ");
			e.printStackTrace();
		} // fim catch
	}// fim run
}// fim classe Servidor