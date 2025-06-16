using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class World : MonoBehaviour
{
    public int daycount;

    public WindowGraph windowgraph;

    public int currentPopulationLoaded;

    public Infect infect;
    public int nameNumber;

    public careDays care;
    public deathDays deaths;
    public WindowGraph windowGraph;


    public int dayCountLimit;
    public int dayCountScale;
    int numOfChildren;
    public Material[] materials;
    public Renderer rend;
    public Human human;
    public GameObject person;
    public Human[] population;
    public int healthCareCapacity;
    public House housePrefab;
    public move[] moves;
    public World prefab;
    public int total,slotNum;
    public int starter;
    public int housePosition;
    public SpawnHumans spawner;
    public GameObject[] objectPopulation;
    public House[] neighborhood;
    public House[] houseScripts;
    public int houseCounter;    //for counting how many human scripts have been placed in houses
    public GameObject[] spawnLocations;
    public DataStorage data;
    public int decider, p, numInfected, crazy, numDead, careCapacity, deathDecider;

    public void Day()
    {

        numDead = 0;
        numInfected = 0;
        careCapacity = data.healthCareCapacity;

        for(int i = 0; i < population.Length; i++)
        {
            if(population[i].inHospital)
            {
                population[i].hospitalDuration++;
            }
        }

        for(int i = 0; i < population.Length; i++)
        {
            if(population[i].inHospital)
            {
                careCapacity = careCapacity - 1;
            }

            if(population[i].infected)
            {
                numInfected = numInfected + 1;
            }

            if(population[i].isdead)
            {
                numDead = numDead + 1;
            }
        }

        Vector2 newInfectedGraphPoint = new Vector2(daycount * dayCountScale, numInfected);
        windowgraph.createCircle(newInfectedGraphPoint);

        Vector2 newCareGraphPoint = new Vector2(daycount * dayCountScale, careCapacity);
        care.createCircle(newCareGraphPoint);

        Vector2 newDeathGraphPoint = new Vector2(daycount * dayCountScale, numDead);
        deaths.createCircle(newDeathGraphPoint);

        

        daycount++;

        if (daycount >= dayCountLimit)
        {
            windowgraph.fontSize = windowgraph.fontSize / 2;
            dayCountScale = dayCountScale / 2;

            windowgraph.changePosition();
            deaths.changeDeathPosition();
            care.changeDeathPosition();

            dayCountLimit = dayCountLimit * 2;
        }

        for(int i = 0; i < population.Length; i++)
        {
            if(population[i].iWillBeInfected)
            {
                population[i].infected = true;
            }
        }

        changeMaterial();

        for (int i = 0; i < population.Length; i++)
        {
            population[i].mostHungry = 0;
            population[i].GoOutside(this);

            if(!population[i].wentOutside && !population[i].wentShopping)
            {
                population[i].boredom += 10;
            }

            if (neighborhood[population[i].houseSlot].someoneShopping)
            {
                for(int c = 0; c < neighborhood[population[i].houseSlot].family.Length; c++)
                {
                    neighborhood[population[i].houseSlot].family[c].hunger = 0;
                    neighborhood[population[i].houseSlot].someoneShopping = false;
                }
            }

            if(population[i].boredom >= 50 && population[i].coop > 4)
            {
                population[i].coop -= 4;
            }
            else if(population[i].boredom <= 20)
            {
                population[i].coop += 9;
            }

            if (population[i].personalityType.Equals("Extroverted"))
            {
                population[i].boredom += 5;
            }

            if(population[i].boredom >= 10)
            {
                population[i].boredom -= population[i].discipline;
            }

            population[i].hunger += 30;

            if (population[i].infected)
            {
                population[i].gestationProgress++;
            }
        }
    }
    
    private void Start()
    {
        numDead = data.deaths;
        careCapacity = data.healthCareCapacity;
        dayCountLimit = 7;
        dayCountScale = 50;
        daycount = 0;
        housePosition = 0;
        prefab.slotNum = 0;
        starter = 0;
        slotNum = 0;
        population = new Human[data.population];    //for storing scripts
        moves = new move[data.population];
        houseScripts = new House[neighborhood.Length];
        numInfected = data.infected;
        objectPopulation = new GameObject[data.population]; //for storing gameobjects
        p = 0;
        crazy = 0;
        houseCounter = 0;

        for(int i = 0; i < neighborhood.Length; i++)
        {
            neighborhood[i].slotNum = i;
            neighborhood[i].family = new Human[5];
            neighborhood[i].someoneShopping = false;

            for (int c = 0; c < neighborhood[i].family.Length; c++)
            {
                if (p >= population.Length)
                {
                    break;
                }

                if (houseCounter >= population.Length)
                {
                    break;
                }

                objectPopulation[p] = Instantiate(person);  //adds a gameobject clone to the array
                moves[p] = objectPopulation[p].GetComponent<move>();
                moves[p].slotNum = 1;
                objectPopulation[p].name = "Human" + ' ' + nameNumber;
                nameNumber++;
                population[p] = new Human();    //adds a script to the array
                population[p].houseSlot = i;
                population[p].Slot = p;
                population[p].Move = moves[p];
                neighborhood[i].family[housePosition] = population[p];
                p++;
                slotNum++;
                prefab.slotNum++;

                if (housePosition < 4)
                {
                    housePosition++;
                    houseCounter++;
                }
                else
                {
                    housePosition = 0;
                    houseCounter++;
                }
            }
        }

        spawner.Spawn();

        for (int f = 0; f < numInfected; f++)
        {
            crazy = Random.Range(0, 5); //picks a random person in the house to infect
            neighborhood[f].family[crazy].infected = true;
        }

        Day();
    }

    void changeMaterial()
    {
        

        for (int i = 0; i < objectPopulation.Length; i++)
        {
            numOfChildren = objectPopulation[i].transform.childCount;
            if (population[i].gestationProgress >= population[i].gestationPeriod)
            {
                for (int c = 0; c < (numOfChildren - 1); c++)
                {
                    GameObject child = objectPopulation[i].transform.GetChild(c).gameObject;
                    rend = child.GetComponent<Renderer>();
                    rend.sharedMaterial = materials[2];
                }
            }
            
            else if (population[i].gestationProgress < population[i].gestationPeriod && population[i].infected)
            {
                for (int c = 0; c < (numOfChildren - 1); c++)
                {
                    GameObject child = objectPopulation[i].transform.GetChild(c).gameObject;
                    rend = child.GetComponent<Renderer>();
                    rend.sharedMaterial = materials[1];
                }
            }

            else if (population[i].infected == false)
            {
                for (int c = 0; c < (numOfChildren - 1); c++)
                {
                    GameObject child = objectPopulation[i].transform.GetChild(c).gameObject;
                    rend = child.GetComponent<Renderer>();
                    rend.sharedMaterial = materials[0];
                }
            }
        }
     }

    public void checkPatients()
    {
        for(int i = 0; i < population.Length; i++)
        {
            if(population[i].inHospital)
            {
                if (population[i].infected)
                {
                    if (population[i].isdead == false || population[i].willLive == false)
                    {
                        if (population[i].hospitalDuration >= 3)
                        {
                            if (population[i].healthCondition.Equals("None"))
                            {
                                decider = Random.Range(0, 100 - population[i].age);
                                if (decider <= 10)
                                {

                                    objectPopulation[i].SetActive(false);
                                    

                                    population[i].inHospital = false;
                                    population[i].isdead = true;
                                    
                                    break;
                                }
                                else
                                {
                                    for (int c = 0; c < (numOfChildren - 1); c++)
                                    {
                                        GameObject child = objectPopulation[i].transform.GetChild(c).gameObject;
                                        rend = child.GetComponent<Renderer>();
                                        rend.sharedMaterial = materials[0];
                                    }

                                    NavMeshAgent agent = objectPopulation[i].GetComponent<NavMeshAgent>();
                                    agent.enabled = true;

                                    population[i].infected = false;
                                    population[i].canInfect = true;
                                    population[i].Move.goHome();

                                    population[i].gestationProgress = 0;

                                    population[i].inHospital = false;
                                    population[i].willLive = true;

                                    break;
                                }
                            }
                            else
                            {
                                decider = Random.Range(0, 125 - population[i].age);
                                if (decider <= 35)
                                {

                                    objectPopulation[i].SetActive(false);
                                    

                                    population[i].inHospital = false;
                                    population[i].isdead = true;

                                    break;
                                }
                                else
                                {
                                    for (int c = 0; c < (numOfChildren - 1); c++)
                                    {
                                        GameObject child = objectPopulation[i].transform.GetChild(c).gameObject;
                                        rend = child.GetComponent<Renderer>();
                                        rend.sharedMaterial = materials[0];
                                    }

                                    NavMeshAgent agent = objectPopulation[i].GetComponent<NavMeshAgent>();
                                    agent.enabled = true;

                                    population[i].infected = false;
                                    population[i].canInfect = true;
                                    population[i].Move.goHome();

                                    population[i].gestationProgress = 0;

                                    population[i].inHospital = false;
                                    population[i].willLive = true;

                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Start is called before the first frame update
    void Update()
    {
        checkPatients();
    }
}
