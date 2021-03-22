package br.com.promove.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente extends Thread {
	// PARTE QUE CONTROLA A RECEPÇÃO DE MENSAGENS DO CLIENTE
	// ESTAS VARIÁVEIS SÃO ESTÁTICAS PORQUE O CONTEÚDO É COMPARTILHADO ENTRE VÁRIOS
	// OBJETOS DA CLASSE
	private static String msgDigitada;

	public static void main(String args[]) throws UnknownHostException, IOException {
		String msgRecebida;
		String nomeDoCliente;
		String salaEscolhida;

		BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("********** SEJA BEM VINDO AO CHAT DE NOTÍCIAS PROMOVE **********");
		System.out.println("Digite seu nome:");
		nomeDoCliente = teclado.readLine();

		// CRIA SOCKET DE COMUNICAÇÃO COM SERVIDOR NO LOCALHOST NA PORTA 9000
		Socket cnnx = new Socket("localhost", 9000);

		// CONTROLA O FLUXO DE COMUNICAÇÃO
		DataOutputStream saidaServidor = new DataOutputStream(cnnx.getOutputStream());
		BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(cnnx.getInputStream()));

		saidaServidor.writeBytes(nomeDoCliente + '\n');
		msgRecebida = entradaServidor.readLine();
		System.out.println(msgRecebida);

		System.out.print("\n##-------------------------------- SALAS DISPONÍVEIS --------------------------------##\n");
		System.out.print("|-------------------------------------------------------------------------------------|\n");
		System.out.print("|    SALA 1 - ECONOMIA    |    SALA 2 - ENTRETENIMENTO    |    SALA 3 - TECNOLOGIA    |\n");
		System.out.print("|-------------------------------------------------------------------------------------|\n");

		System.out.print("Digite a sala: ");
		salaEscolhida = teclado.readLine();
		saidaServidor.writeBytes(salaEscolhida + '\n');

		msgRecebida = entradaServidor.readLine();
		System.out.println(msgRecebida);

		// INSTANCIA A THREAD PARA IP E PORTA CONECTADOS E DEPOIS INICIA
		Thread thread = new Cliente(entradaServidor);
		thread.start();

		// TRATA A ESCOLHA FEITA NO MENU
		while (true) {
			// CRIA A LINHA PARA DIGITAÇÃO DA MENSAGEM E ARMAZENA NA VARIÁVEL msgEnviada
			msgDigitada = teclado.readLine();
			// ENVIA A MENSAGEM PARA O SERVIDOR
			saidaServidor.writeBytes(msgDigitada + '\n');

			if (msgDigitada.startsWith("fim") == true) {
				break;
			}

			msgRecebida = entradaServidor.readLine();
			// SE A MENSAGEM RECEBIDA CONTIVER DADOS PASSA PELO IF,
			// CASO CONTRÁRIO CAI NO BREAK E ENCERRA A CONEXÃO
			if (msgRecebida != null) {
				System.out.println(msgRecebida);
//				System.out.println("Conexão encerrada!");
			}
			cnnx.close();
		}

	}

	private BufferedReader entrada;

	// COSNTRUTOR QUE RECEBE O BUFFERED DO CLIENTE
	public Cliente(BufferedReader Buff) {
		entrada = Buff;
		msgDigitada = "Not Null";
	}

	public void run() {
		try {
			while (msgDigitada != null && !(msgDigitada.trim().equals("")) && !(msgDigitada.startsWith("fim"))) {
				System.out.println(entrada.readLine());
			}
			// SAI DO CLIENTE
			System.exit(0);
		} catch (IOException e) {
			// EM CASO DE ERRO DESLIGA O OBJETO DA CLASSE CLIENTE
			System.exit(0);
		}
	}
}
