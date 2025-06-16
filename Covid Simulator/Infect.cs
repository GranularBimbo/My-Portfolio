using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;

public class Infect : MonoBehaviour
{
    public World world;
    public move me;
    public move guy;
    public NavMeshAgent agentGuy;

    public void OnTriggerEnter(Collider other)
    {
        if (world.population[me.slotNum].canInfect)
        {
            if (other.tag.Equals("Person") && world.population[me.slotNum].numInfected < world.population[me.slotNum].maxInfect)
            {
                guy = other.GetComponent<move>();
                agentGuy = other.GetComponent<NavMeshAgent>();
                if (world.population[guy.slotNum].infected == false)
                {
                    if (agentGuy.enabled)
                    {
                        world.numInfected++;


                        world.population[guy.slotNum].infected = true;
                        world.population[guy.slotNum].numInfected++;
                    }
                }
            }
        }
    }

    // Start is called before the first frame update
    void Start()
    {
        world = GameObject.Find("World").GetComponent<World>();
    }

    // Update is called once per frame
    void Update()
    {

    }
}
