package pt.c02classes.s01knowledge.s02app.actors;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.lang.Character;

import pt.c02classes.s01knowledge.s01base.inter.IEnquirer;
import pt.c02classes.s01knowledge.s01base.inter.IResponder;

public class MyEnquirerMaze implements IEnquirer 
{
	boolean m_isFinish;
	boolean m_hadToReturn; 
	IResponder m_responder;
	List<List<String>> m_movedHistory;
	HashMap<Integer, String> m_directionMap;

	public MyEnquirerMaze()
	{
		this.m_isFinish = false;
		this.m_hadToReturn = false;
		this.m_movedHistory = new ArrayList<List<String>>();
		this.m_directionMap = new HashMap<Integer, String>();

		this.m_directionMap.put(1, "norte");
		this.m_directionMap.put(2, "leste");
		this.m_directionMap.put(3, "sul");
		this.m_directionMap.put(4, "oeste");
	}

	public void connect(IResponder responder) 
	{
		this.m_responder = responder;
	}

	private void showMsg(String msg)
	{
		System.out.println(msg);
	}

	public boolean discover()
	{
		while(!this.m_isFinish)
		{
			// Perguntar o que tem na posicao atual
			String current_question = getQuestionList();

			String answer_list = "";
			for(int i = 0; i < 4; i++) 
			{ // perguntar pelas 4 possiveis direcoes
				char current_char = current_question.charAt(i);
				Integer value = Character.getNumericValue(current_char);

				String direction = m_directionMap.get(value);
				String answer = m_responder.ask(direction);

				if(answer.equalsIgnoreCase("passagem") || answer.equalsIgnoreCase("entrada"))
					answer_list = answer_list + value;
				
				if(answer.equalsIgnoreCase("saida")) 
				{
					// Achei a saida
					if(m_responder.move(direction))
						showMsg("Movimento para o " + direction + " executado!");
					
					if (m_responder.finalAnswer("cheguei"))
						System.out.println("Voce encontrou a saida!");
					else
						System.out.println("Fuem fuem fuem!");
					
					showFullPath(direction);
					
					return true;
				}
			}

			String moveTo = processAnswer(answer_list);

			if(moveTo.isEmpty()){
				showMsg("----------ERRO--------");
				showMsg("Acabaram todas as possibilidades, o labirinto nao tem saida!!");
				this.m_isFinish = true;
				continue;
			}

			if(m_responder.move(moveTo))
				showMsg("Movimento para o " + moveTo + " executado!");
			else{
				showMsg("Nao foi possivel mover");
				this.m_isFinish = true;
			}
		}
		return true;
	}

	private String getQuestionList()
	{
		String questions = "";

		if(m_movedHistory.size() == 0) {
			questions = "1234";
			return questions;
		}

		List<String> last_entery = m_movedHistory.get(m_movedHistory.size()-1);
		String last_mov = last_entery.get(1);

		switch(Character.getNumericValue(last_mov.charAt(last_mov.length()-1)))
		{
			case 1: questions = "1243";
				break;
			case 2: questions = "2314";
				break;
			case 3: questions = "3421";
				break;
			case 4: questions = "4312";
				break;
		}

		return questions;
	}

	private String processAnswer(String possible_directions)
	{
		String moveTo = "";

		if(m_movedHistory.size() == 0)
		{
			if(possible_directions.length() == 0)
				return moveTo; // Erro, nao tem para onde se movimentar

			List<String> move_item = new ArrayList<String>();
			move_item.add(possible_directions);
			move_item.add(Character.toString(possible_directions.charAt(0)));
			m_movedHistory.add(move_item);

			moveTo = m_directionMap.get(Character.getNumericValue(possible_directions.charAt(0)));
		}
		else
		{
			if(possible_directions.length() == 0)
				return moveTo; // Erro, nao tem para onde se movimentar
			
			if(possible_directions.length() == 1) 
			{	// Tem que retornar
				m_hadToReturn = true;
				moveTo = m_directionMap.get(Character.getNumericValue(possible_directions.charAt(0)));
				showMsg("Opa !!  vou ter que voltar !!");
			}
			else
			{
				if(m_hadToReturn)
				{
					List<String> last_entery = m_movedHistory.get(m_movedHistory.size()-1);
					String moviments = last_entery.get(1);
					
					if(moviments.length() == possible_directions.length())
						return moveTo;	// Foram esgotadas todas as possibilidades
					
					char next_move = possible_directions.charAt(moviments.length());

					m_hadToReturn = hasToReturn(Character.getNumericValue(next_move));
					
					if(m_hadToReturn)
					{
						moveTo = m_directionMap.get(Character.getNumericValue(next_move));
						m_movedHistory.remove(m_movedHistory.size()-1);
					}
					else
					{
						moviments = moviments + next_move;
						m_movedHistory.remove(m_movedHistory.size()-1);
						
						List<String> move_item = new ArrayList<String>();
						move_item.add(possible_directions);
						move_item.add(moviments);
						m_movedHistory.add(move_item);
						
						moveTo = m_directionMap.get(Character.getNumericValue(next_move));
					}
				}
				else
				{
					List<String> move_item = new ArrayList<String>();
					move_item.add(possible_directions);
					move_item.add(Character.toString(possible_directions.charAt(0)));
					m_movedHistory.add(move_item);

					moveTo = m_directionMap.get(Character.getNumericValue(possible_directions.charAt(0)));
				}
			}
		}
		return moveTo;
	}
	
	private Boolean hasToReturn(Integer value)
	{
		List<String> last_entery;
		
		if(m_movedHistory.size() > 1)
			last_entery = m_movedHistory.get(m_movedHistory.size()-2);
		else
			last_entery = m_movedHistory.get(m_movedHistory.size()-1);
		
		Integer last_mov = Character.getNumericValue(last_entery.get(1).charAt(last_entery.get(1).length()-1)); // get the last character of the movement list and convert to Integer
		
		switch(last_mov) {
			case 1: if(value == 3) return true; else return false;
			case 2: if(value == 4) return true; else return false;
			case 3: if(value == 1) return true; else return false;
			case 4: if(value == 2) return true; else return false;
			default: return false;
		}
		
	}
	
	private void showFullPath(String last_move_to_win)
	{
		showMsg("-------------------------------");
		showMsg("------ RESUMO DO PERCURSO -----");
		showMsg("-------------------------------");
		
		for(List<String> item : m_movedHistory)
		{
			String moviments = item.get(1);
			Integer last_move = Character.getNumericValue(moviments.charAt(moviments.length()-1));
			
			showMsg( m_directionMap.get(last_move));
		}
		
		showMsg(last_move_to_win);
	}
}
