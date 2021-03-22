/**
 * LÓGICA:
 * ABRE UM SOCKET DE COMUNICAÇÃO
 * PEDE PARA CONECTAR NO LOCAL HOST (PODENDO SER UM ENDEREÇO IP FORA DA REDE DESDE QUE TENHA ACESSO A ELE VIA PORTA FIREWALL, OUTRA MÁQUINA NA REDE)
 * CONVERSAR COM AQUELE HOST NA PORTA 80
 * CRIAR UM BUFFER () PARA RECEBER INFORMAÇÕES
 * FICO AGUARDANDO CHEGAR ALGUMA INFORMAÇÃO FICA PARADO ESPERANDO
 * SE A INFORMAÇÃO CHEGA JOGA-SE ELA NA CONSOLE
 * 
 * QUAL O PROBLEMA DE SE USAR A PORTA 80?
 * RESPOSTA: Ela é um a porta reservada para o protocolo TCP HTTP WEB trafegando dados TCP
 * 8080 trafegando dados UDP 
 * Não se pode utilizar portas baixas que esteja reservadas, a não ser que minha comunicação seja feita em cima desse protocolo
 * 
 * PERGUNTAS
 * O QUE ACONTECE APÓS A EXECUÇÃO?
 * 
 * 
 * ALTERANDO O NÚMERO DA PORTA DE 80 PARA 7979, O QUE ACONTECE?
 * RESPOSTA: Quando a porta não está liberada retorna 'Connection refused: connect (Conexão recusada: conecte)'
 * Não existe ninguém do outro lado tentando estabelecer esta conexão com você
 * Foi criado um cliente, porém não temos um servidor
 * É preciso ter alguem pronto para conversar com o cliente nessa porta por este motivo conexão recusada.
 * O OBJETO SOCKET PRECISA TER ALGUÉM DO OUTRO LADO PARA CONVERSAR COM ELE
 * É PRESISO TER UM SERVERSOCKET QUE É O OUTRO LADO PARA COMUNICAR COM O SOCKET CLIENTE.
 * A PORTA NESTE MOMENTO ESTÁ FECHADA. TEMOS UM CLIENTE BATENDO NA PORTA, NÃO TEM NINGUÉM ESPERANDO, ETÃO ELE VOLTOU
 * 
 * OUTRO MOTIVO DA CONEXÃO RECUSADA É QUE O PRTOTOCOLO UTILIZADO É O PROTOCOLO TCP QUE CONCEITUALMENTE É ORIENTADO A CONEXÃO (significa que,
 * para estabelecer uma comunicação de enviar e receber mensagens é preciso fazer um procedimento de 'aperto de mão' (handshake) que é estabelecer
 * a conexão. Estabeleceu conexão entre CLIENTE e SERVIDOR então podera começar a se comunicar.
 * 
 * Estabelecimento da conexão
 * 
 * Para estabelecer uma conexão, o TCP usa um three-way handshake (acordo de três vias). Antes que o cliente tente se conectar com o servidor, 
 * o servidor deve primeiro ligar e escutar a sua própria porta, para só depois abri-la para conexões: isto é chamado de abertura passiva.
 * Uma vez que a abertura passiva esteja estabelecida, um cliente pode iniciar uma abertura ativa. Para estabelecer uma conexão, 
 * o acordo de três vias (ou 3 etapas) é realizado:
 * 
 * SYN: A abertura ativa é realizada por meio do envio de um segmento (unidade de dados da camada de transporte) com uma flag SYN 
 * pelo cliente ao servidor. O cliente define o número de sequência de segmento como um valor aleatório A.
 * 
 * SYN+ACK: Em resposta, o servidor responde com um segmento SYN-ACK. O número de reconhecimento (ACKnowledgment) é definido como 
 * sendo um a mais que o número de sequência recebido, i.e. A+1, e o número de sequência que o servidor escolhe para o pacote é outro
 * número aleatório B.
 *  
 *  ACK: Finalmente, o cliente envia um ACK de volta ao servidor. O número de sequência é definido ao valor de reconhecimento recebido,
 *  i.e. A+1, e o número de reconhecimento é definido como um a mais que o número de sequência recebido, i.e B+1.
 * 
 * SE FOSSE UM PROTOCOLO UDP?
 * RESPOSTA: ELe não dá erro porque não o cliente não necessita do servidor
 * Simplismente envia alguma coisa, se tiver alguem para receber bem se não tiver amém.
 * Então nem terá mensagem de retorno dizendo que não recebeu
 * 
 * PORTAS QUE FUNCIONAM PARA O EXERCÍCIO:
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
		// O 'localhost' PODE SER SUBSTITUÍDO PELO ENDEREÇO IP, BASTA FAZER A ABERTURA DA PORTA NO FIREWALL
		Socket cliente = new Socket("localhost", 9000);
		System.out.println("Socket ativo!");
		
		// ONDE SE RECEBE O INPUT DO SOCKET CLIENTE
		BufferedReader entrada_servidor = new BufferedReader(new InputStreamReader(cliente.getInputStream()));

		while (true) {
			// LÊ UMA LINHA DO SERVIDOR. PARA EM READLINE E FICA ESCUTANDO ATÉ CHEGAR ALGUMA COISA NA PORTA. 
			//  SE NÃO MANDA NADA FICA AQUI AGUARDANDO PRA SEMPRE.
			msg_recebida = entrada_servidor.readLine();
			
			// APRESENTA A LINHA DO SERVIDOR NO CONSOLE
			if (msg_recebida != null)
				System.out.println("Servidor: " + msg_recebida);
		}

	}

}
