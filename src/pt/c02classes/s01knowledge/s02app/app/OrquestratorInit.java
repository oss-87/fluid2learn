package pt.c02classes.s01knowledge.s02app.app;

import java.util.Scanner;

import pt.c02classes.s01knowledge.s01base.impl.BaseConhecimento;
import pt.c02classes.s01knowledge.s01base.impl.Statistics;
import pt.c02classes.s01knowledge.s01base.inter.IBaseConhecimento;
import pt.c02classes.s01knowledge.s01base.inter.IEnquirer;
import pt.c02classes.s01knowledge.s01base.inter.IResponder;
import pt.c02classes.s01knowledge.s01base.inter.IStatistics;
import pt.c02classes.s01knowledge.s02app.actors.MyEnquirerAnimals;
import pt.c02classes.s01knowledge.s02app.actors.ResponderAnimals;
import pt.c02classes.s01knowledge.s02app.actors.ResponderMaze;
import pt.c02classes.s01knowledge.s02app.actors.MyEnquirerMaze;

public class OrquestratorInit 
{

	static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args)
	{	
		System.out.println("Oi, bom dia, por favor escolha o que voce quer fazer");
		System.out.println("----> A: Jogar adivinhar o animal");
		System.out.println("----> L: Jogar solucionar o labirinto");
		System.out.println("----> F: Sair");
		System.out.println(" ");
		System.out.println("Por favor escolha 'A', 'L' ou 'F'");

		String tipo = scanner.nextLine();

		do{
			switch (tipo.toUpperCase()) 
			{
			case "A": PlayAnimals();
			break;
			case "L": PlayMaze();
			break;
			default:
				break;
			}

			System.out.println(" ");
			System.out.println("NOVO JOGO: por favor escolha um");
			System.out.println(" ");
			System.out.println("----> A: Jogar adivinhar o animal");
			System.out.println("----> L: Jogar solucionar o labirinto");
			System.out.println("----> F: Sair");
			System.out.println(" ");
			System.out.println("Por favor escolha 'A', 'L' ou 'F'");
			tipo = scanner.nextLine();

		} while(!tipo.equalsIgnoreCase("F"));

		scanner.close();

		System.out.println("Obrigado por jugar, ate a proxima !!");
	}

	public static void PlayAnimals()
	{
		IEnquirer enq;
		IResponder resp;
		IStatistics stat;

		IBaseConhecimento base = new BaseConhecimento();

		base.setScenario("animals");
		String listaAnimais[] = base.listaNomes();

		// Pick only one animal
		System.out.println("Por favor escolha um dos seguintes animais para ser adivinhado");
		for(int i = 0; i < listaAnimais.length; i++)
		{
			System.out.println("-----> " + (i+1) + "  " + listaAnimais[i]);
		}

		Integer animal_selected = Integer.parseInt(scanner.nextLine());

		if(animal_selected > listaAnimais.length)
		{
			System.out.println("Seleciono errado o animal, tente de novo");
			return;
		}

		int animal = (animal_selected-1);

		System.out.println("Pensando em " + listaAnimais[animal] + "...");
		stat = new Statistics();
		enq = new MyEnquirerAnimals();
		resp = new ResponderAnimals(stat, listaAnimais[animal]);
		enq.connect(resp);
		enq.discover();
		System.out.println("----------------------------------------------------------------------------------------\n");
	}

	public static void PlayMaze()
	{
		IEnquirer enq;
		IResponder resp;
		IStatistics stat;

		IBaseConhecimento base = new BaseConhecimento();

		base.setScenario("maze");
		String listaMaze[] = base.listaNomes();

		// Pick only one animal
		System.out.println("Por favor escolha um dos seguintes labirintos para ser resolvido");
		for(int i = 0; i < listaMaze.length; i++)
		{
			System.out.println("-----> " + (i+1) + "  " + listaMaze[i]);
		}

		Integer maze_selected = Integer.parseInt(scanner.nextLine());

		if(maze_selected > listaMaze.length)
		{
			System.out.println("Seleciono errado o labirinto, tente de novo");
			return;
		}

		String maze = listaMaze[(maze_selected-1)];

		System.out.println("Enquirer com " + maze);
		stat = new Statistics();
		resp = new ResponderMaze(stat, maze);
		enq = new MyEnquirerMaze();
		enq.connect(resp);
		enq.discover();
		System.out.println("----------------------------------------------------------------------------------------\n");
	}
}
