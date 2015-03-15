package pt.c02classes.s01knowledge.s02app.actors;

import java.util.ArrayList;
import java.util.List;

import pt.c02classes.s01knowledge.s01base.impl.BaseConhecimento;
import pt.c02classes.s01knowledge.s01base.inter.IBaseConhecimento;
import pt.c02classes.s01knowledge.s01base.inter.IDeclaracao;
import pt.c02classes.s01knowledge.s01base.inter.IEnquirer;
import pt.c02classes.s01knowledge.s01base.inter.IObjetoConhecimento;
import pt.c02classes.s01knowledge.s01base.inter.IResponder;

public class MyEnquirerAnimals implements IEnquirer
{	
	IResponder responder;
	List<List<String>> declaracao_list;
	
	public MyEnquirerAnimals()
	{
		declaracao_list = new ArrayList<List<String>>();
	}
	
	public void connect(IResponder responder) {
		this.responder = responder;
	}
	
	public boolean discover() {
        IBaseConhecimento bc = new BaseConhecimento();
        bc.setScenario("animals");
        IObjetoConhecimento obj;
        String listaAnimais[] = bc.listaNomes();
        
        
     // Perguntando um por um todos os animais na base de dados
        for (int animal = 0; animal < listaAnimais.length; animal++) 
        {
        	obj = bc.recuperaObjeto(listaAnimais[animal]);
			IDeclaracao decl = obj.primeira();
			
			boolean animalEsperado = true;

			while (decl != null && animalEsperado) 
			{
				// Verificando pergunta repetida
				if(checkQuestion(decl, responder))
					decl = obj.proxima();
				else
				{
					animalEsperado = false;
					System.out.println("Nao e " + listaAnimais[animal]);
				}
				
				// Fin pergunta repetida


				/*
				// Sem verificar pergunta repetida
				String pergunta = decl.getPropriedade();
				String respostaEsperada = decl.getValor();

				String resposta = responder.ask(pergunta);

				if (resposta.equalsIgnoreCase(respostaEsperada)) {
					decl = obj.proxima();
				}
				else {
					animalEsperado = false;
					System.out.println("Nao e " + listaAnimais[animal]);
				}

				// Fin sem verificar pergunta repetida
				 */
			}
			
			if(animalEsperado)
			{
				boolean acertei = responder.finalAnswer(listaAnimais[animal]);

				if (acertei)
					System.out.println("Oba! Acertei!");
				else
					System.out.println("fuem! fuem! fuem!");

				break;
			}
        }
        
		return true;
	}
	
	private boolean checkQuestion(IDeclaracao declaracao, IResponder responder)
	{
		String pergunta = declaracao.getPropriedade();
		String respostaEsperada = declaracao.getValor();

		for(List<String> ls : declaracao_list) 
		{
			if(ls.get(0).equalsIgnoreCase(pergunta))
			{
				//Ja foi realizada est‡ pergunta
				return ls.get(1).equalsIgnoreCase(respostaEsperada);
			}
		}

		// n‹o est‡ na lista
		String resposta = responder.ask(pergunta);

		List<String> declaration = new ArrayList<String>();
		declaration.add(pergunta);
		declaration.add(resposta);
		declaracao_list.add(declaration);

		return resposta.equalsIgnoreCase(respostaEsperada);
	}

}
