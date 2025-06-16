using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using System;

public class SpawnHumans : MonoBehaviour
{
    public World world;

    public void Spawn()
    {
        for(int i = 0; i < world.population.Length; i++)
        {
            try
            {
                world.objectPopulation[i].transform.SetPositionAndRotation(world.spawnLocations[world.population[i].houseSlot].transform.position,
               world.spawnLocations[world.population[i].houseSlot].transform.rotation);
            }
            catch(Exception e)
            {
                Debug.Log(e.Message);
            }
        }
    }
}
