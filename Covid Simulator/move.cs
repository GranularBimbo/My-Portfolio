using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class move : MonoBehaviour
{
    public float distanceToDestination;

    public bool goingHome = false;
    public bool goingToHospital = false;

    public NavMeshAgent agent;
    public World world;
    public int slotNum,decider;
    public float distanceThreshold, hospitalDistanceThreshold;
    public GameObject destination;

    // Update is called once per frame
    public void Start()
    {
        distanceThreshold = 0.9f;

        hospitalDistanceThreshold = 4.0f;

        agent.radius = 0.15f;

        world = GameObject.Find("World").GetComponent<World>();
    }

    public void Avoidance()
    {
        agent.radius = ((world.population[slotNum].coop*3.5f) / 100f) + 0.15f;
    }

    public void GoToPark()
    {
        agent.enabled = true;

        decider = Random.Range(0, 2);

        if (decider == 0)
        {
            destination = GameObject.Find("Park Center");
        }
        else
        {
            if (decider == 1)
            {
                destination = GameObject.Find("Park Left Corner");
            }
        }

        agent.destination = destination.transform.position;

        StartCoroutine(waitThenGoHome());
    }

    public void GoToStore()
    {

        agent.enabled = true;

        destination = GameObject.Find("Grocery Store");

        agent.destination = destination.transform.position;

        StartCoroutine(waitThenGoHome());

    }

    public void GoToHospital()
    {
        world.population[slotNum].canInfect = false;

        agent.enabled = true;

        destination = GameObject.Find("Hospital");

        goingToHospital = true;

        agent.destination = destination.transform.position;
    }

    IEnumerator waitThenGoHome()
    {
        yield return new WaitForSeconds(12);

        goHome();

        if (world.population[slotNum].wentShopping)
        {
            for(int i = 0; i < world.neighborhood[world.population[slotNum].houseSlot].family.Length; i++)
            {
                world.neighborhood[world.population[slotNum].houseSlot].family[i].hunger = 0;   //makes people not hungry after shopping
            }
        }

        if (world.population[slotNum].wentOutside)
        {
            world.population[slotNum].boredom = 5;
        }
    }

    public void goHome()
    {
        
        world.population[slotNum].willLive = false;

        NavMeshAgent person = world.objectPopulation[slotNum].GetComponent<NavMeshAgent>();

        person.enabled = true;

        goingHome = true;

        destination = GameObject.Find("House" + (world.population[slotNum].houseSlot + 1));

        agent.destination = destination.transform.position;
    }

    void checkDistance()
    {
        distanceToDestination = Vector3.Distance(world.objectPopulation[slotNum].transform.position, destination.transform.position);
    }

    public void Update()
    {
        if (goingToHospital)
        {
            checkDistance();
        }

        if(distanceToDestination <= hospitalDistanceThreshold && goingToHospital)
        {

            world.population[slotNum].wentOutside = false;
            world.population[slotNum].wentShopping = false;
            world.population[slotNum].wentToHospital = false;

            world.objectPopulation[slotNum].transform.SetPositionAndRotation(destination.transform.position,
                destination.transform.rotation);

            
            
            if(world.careCapacity > 0)
            {
                world.population[slotNum].inHospital = true;
            }
            else
            {
                world.population[slotNum].patientChecked = true;
                world.population[slotNum].isdead = true;
                world.population[slotNum].inHospital = false;
                world.population[slotNum].infected = false;
                world.objectPopulation[slotNum].SetActive(false);

                NavMeshAgent agent = world.objectPopulation[slotNum].GetComponent<NavMeshAgent>();
                agent.enabled = false;

                BoxCollider collider = world.objectPopulation[slotNum].GetComponent<BoxCollider>();
                collider.enabled = false;

            }

            goingToHospital = false;
        }

        if (goingHome)
        {
            checkDistance();
        }

        if (distanceToDestination < distanceThreshold && goingHome)
        {
            goingHome = false;
            world.population[slotNum].wentOutside = false;
            world.population[slotNum].wentShopping = false;

            world.objectPopulation[slotNum].transform.SetPositionAndRotation(world.spawnLocations[world.population[slotNum].houseSlot].transform.position,
                world.spawnLocations[world.population[slotNum].houseSlot].transform.rotation);
        }

        Avoidance();

        if (world.population[slotNum].isdead && world.population[slotNum].infected == false)
        {
            destination = destination = GameObject.Find("Hospital");

            world.objectPopulation[slotNum].transform.SetPositionAndRotation(destination.transform.position,
                destination.transform.rotation);

            world.objectPopulation[slotNum].SetActive(false);

            agent.enabled = false;
        }

        if (world.population[slotNum].isdead)
        {
            agent.enabled = false;
        }
        else
        {
            if (world.population[slotNum].wentOutside || world.population[slotNum].wentShopping || world.population[slotNum].wentToHospital || goingHome || goingToHospital)
            {
                agent.enabled = true;
            }
            else
            {
                agent.enabled = false;
            }
        }
    }
}
