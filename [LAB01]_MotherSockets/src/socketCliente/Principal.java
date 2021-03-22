/**
 * L�GICA:
 * ABRE UM SOCKET DE COMUNICA��O
 * PEDE PARA CONECTAR NO LOCAL HOST (PODENDO SER UM ENDERE�O IP FORA DA REDE DESDE QUE TENHA ACESSO A ELE VIA PORTA FIREWALL, OUTRA M�QUINA NA REDE)
 * CONVERSAR COM AQUELE HOST NA PORTA 80
 * CRIAR UM BUFFER () PARA RECEBER INFORMA��ES
 * FICO AGUARDANDO CHEGAR ALGUMA INFORMA��O FICA PARADO ESPERANDO
 * SE A INFORMA��O CHEGA JOGA-SE ELA NA CONSOLE
 * 
 * QUAL O PROBLEMA DE SE USAR A PORTA 80?
 * RESPOSTA: Ela � um a porta reservada para o protocolo TCP HTTP WEB trafegando dados TCP
 * 8080 trafegando dados UDP 
 * N�o se pode utilizar portas baixas que esteja reservadas, a n�o ser que minha comunica��o seja feita em cima desse protocolo
 * 
 * PERGUNTAS
 * O QUE ACONTECE AP�S A EXECU��O?
 * 
 * 
 * ALTERANDO O N�MERO DA PORTA DE 80 PARA 7979, O QUE ACONTECE?
 * RESPOSTA: Quando a porta n�o est� liberada retorna 'Connection refused: connect (Conex�o recusada: conecte)'
 * N�o existe ningu�m do outro lado tentando estabelecer esta conex�o com voc�
 * Foi criado um cliente, por�m n�o temos um servidor
 * � preciso ter alguem pronto para conversar com o cliente nessa porta por este motivo conex�o recusada.
 * O OBJETO SOCKET PRECISA TER ALGU�M DO OUTRO LADO PARA CONVERSAR COM ELE
 * � PRESISO TER UM SERVERSOCKET QUE � O OUTRO LADO PARA COMUNICAR COM O SOCKET CLIENTE.
 * A PORTA NESTE MOMENTO EST� FECHADA. TEMOS UM CLIENTE BATENDO NA PORTA, N�O TEM NINGU�M ESPERANDO, ET�O ELE VOLTOU
 * 
 * OUTRO MOTIVO DA CONEX�O RECUSADA � QUE O PRTOTOCOLO UTILIZADO � O PROTOCOLO TCP QUE CONCEITUALMENTE � ORIENTADO A CONEX�O (significa que,
 * para estabelecer uma comunica��o de enviar e receber mensagens � preciso fazer um procedimento de 'aperto de m�o' (handshake) que � estabelecer
 * a conex�o. Estabeleceu conex�o entre CLIENTE e SERVIDOR ent�o podera come�ar a se comunicar.
 * 
 * Estabelecimento da conex�o
 * 
 * Para estabelecer uma conex�o, o TCP usa um three-way handshake (acordo de tr�s vias). Antes que o cliente tente se conectar com o servidor, 
 * o servidor deve primeiro ligar e escutar a sua pr�pria porta, para s� depois abri-la para conex�es: isto � chamado de abertura passiva.
 * Uma vez que a abertura passiva esteja estabelecida, um cliente pode iniciar uma abertura ativa. Para estabelecer uma conex�o, 
 * o acordo de tr�s vias (ou 3 etapas) � realizado:
 * 
 * SYN: A abertura ativa � realizada por meio do envio de um segmento (unidade de dados da camada de transporte) com uma flag SYN 
 * pelo cliente ao servidor. O cliente define o n�mero de sequ�ncia de segmento como um valor aleat�rio A.
 * 
 * SYN+ACK: Em resposta, o servidor responde com um segmento SYN-ACK. O n�mero de reconhecimento (ACKnowledgment) � definido como 
 * sendo um a mais que o n�mero de sequ�ncia recebido, i.e. A+1, e o n�mero de sequ�ncia que o servidor escolhe para o pacote � outro
 * n�mero aleat�rio B.
 *  
 *  ACK: Finalmente, o cliente envia um ACK de volta ao servidor. O n�mero de sequ�ncia � definido ao valor de reconhecimento recebido,
 *  i.e. A+1, e o n�mero de reconhecimento � definido como um a mais que o n�mero de sequ�ncia recebido, i.e B+1.
 * 
 * SE FOSSE UM PROTOCOLO UDP?
 * RESPOSTA: ELe n�o d� erro porque n�o o cliente n�o necessita do servidor
 * Simplismente envia alguma coisa, se tiver alguem para receber bem se n�o tiver am�m.
 * Ent�o nem ter� mensagem de retorno dizendo que n�o recebeu
 * 
 * PORTAS QUE FUNCIONAM PARA O EXERC�CIO:
 * 80	TCP	HTTP
 * 445  SMB PORT Blocos de mensagens do servidor
 * 3389	TCP	TERMINAL SERVER 
 * 
 */
package socketCliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author CARLOS RODRIGUES
 * @since 20210209
 */
public class Principal {
	/**
	 * @param args
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	@SuppressWarnings({"resource","unused"})
	public static void main(String[] args) throws UnknownHostException, IOException {
		System.out.println("Cliente Ativo!");

		String msg_recebida; // MENSAGEM RECEBIDA

		// CRIA O SOCKET DE ACESSO AO SERVER HOSTNAME NA PORTA 80
		// O 'localhost' PODE SER SUBSTITU�DO PELO ENDERE�O IP, BASTA FAZER A ABERTURA DA PORTA NO FIREWALL
		Socket cliente = new Socket("localhost", 9000);
		System.out.println("Socket ativo!");
		
		// ONDE SE RECEBE O INPUT DO SOCKET CLIENTE
		BufferedReader entrada_servidor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

		while (true) {
			// L� UMA LINHA DO SERVIDOR. PARA EM READLINE E FICA ESCUTANDO AT� CHEGAR ALGUMA COISA NA PORTA. 
			//  SE N�O MANDA NADA FICA AQUI AGUARDANDO PRA SEMPRE.
			msg_recebida = entrada_servidor.readLine();
			
			// APRESENTA A LINHA DO SERVIDOR NO CONSOLE
			if (msg_recebida != null)
				System.out.println("Servidor: " + msg_recebida);
		}

	}

}
