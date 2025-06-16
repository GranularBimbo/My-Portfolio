using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class Human : ScriptableObject
{
    public int hospitalDuration;
    public move Move;
    public GameObject person;
    public bool infected, inFamily, wentOutside, wentShopping, iWillBeInfected, wentToHospital, inHospital, patientChecked, isdead,willLive,canInfect,deathChecked;
    public int coop, age, boredom, houseSlot,hunger,Slot,gestationProgress,gestationPeriod,discipline; //period is for how long it takes to show symptems
    public string personalityType, healthCondition;
    public int mostHungry, shoppingThreshhold, personalityModifier,maxInfect,numInfected;
    private string[] personalities, conditions;
    private int decider;

    public Human()
    {
        Setup();
    }

    public void GoOutside(World world) //decides if they go outside
    {
        Move.slotNum = Slot;
        mostHungry = 0;
        int prob;
        int thingy;

        for(int i = 0; i < world.neighborhood[houseSlot].family.Length; i++)
        {
            if(world.neighborhood[houseSlot].family[i] != null)
            {
                if (world.neighborhood[houseSlot].family[i].hunger > mostHungry)
                {
                    mostHungry = world.neighborhood[houseSlot].family[i].hunger;    //sets mostHungry to the most hungry person in the house
                }
            }
        }

        if (gestationProgress < gestationPeriod)
        {
            if (mostHungry > shoppingThreshhold && world.neighborhood[houseSlot].someoneShopping == false)
            {
                wentShopping = true;
                world.neighborhood[houseSlot].someoneShopping = true;
                Move.GoToStore();
                Move.agent.radius = 0.15f;
            }
        }
        else
        {
            //only do this if theres room in the hospital
            wentToHospital = true;
            Move.GoToHospital();
        }

        if(gestationProgress < gestationPeriod)
        {
            if (wentShopping == false)
            {
                if (personalityType.Equals("Introverted"))
                {
                    prob = (boredom - personalityModifier) / coop;   //makes them more likely to stay inside if introverted, vice versa
                }
                else
                {
                    prob = (boredom + personalityModifier) / coop;
                }

                thingy = Random.Range(0, 101);

                if (thingy <= prob)
                {
                    wentOutside = true;
                    Move.GoToPark();
                }
            }
        }
    }
    
    void Setup()
    {
        deathChecked = false;
        canInfect = true;
        isdead = false;
        inHospital = false;
        patientChecked = false;
        personalityModifier = 25;
        wentShopping = false;
        willLive = false;
        gestationPeriod = Random.Range(3,6);
        discipline = Random.Range(0, 11);
        gestationProgress = 0;
        wentToHospital = false;
        maxInfect = 2;  //max people one person can infect per day
        numInfected = 0;
        wentOutside = false;
        shoppingThreshhold = 75;    //this is a percent value
        personalities = new string[] { "Introverted", "Extroverted" };   //helps decide if they stay in or go out and their walking paths
        conditions = new string[] { "None", "Diabetes", "None", "Cancer", "None", "Asthma", "None", "Bronchitis", "None", "None" };  //Makes an accurate 40% rate of people with diseases

        iWillBeInfected = false;
        infected = false;   //everyone starts fine, first people infected will be decided in the world script
        inFamily = false; //this is actually decided in the house script
        coop = Random.Range(5, 21);
        age = Random.Range(10, 81);
        hunger = Random.Range(1,101);
        boredom = Random.Range(25,76);    //Boredom increases with time, the rate depending on personality and other variables

        decider = Random.Range(0, 2);
        personalityType = personalities[decider];   //chooses a personality type from the personalities list

        decider = Random.Range(0, 9);
        healthCondition = conditions[decider];  //chooses a condition from the conditions list
    }

    // Start is called before the first frame update
    void Start()
    {
        Setup();
    }
}
