using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class DataStorage : MonoBehaviour
{
    public int healthCareCapacity, infected, deaths;
    public int population;
    public StartingOptions options;
    public GameObject storage;
    public GameObject collector;
    public GameObject EscToGoToMenu;
    public Slider i;    //infected slider
    public Slider p;    //population slider

    public void TransferData()
    {
        population = options.population;    //transfers slider data to this script
        healthCareCapacity = options.healthCareCapacity;
        infected = options.infected;
        deaths = 0;
    }

    // Update is called once per frame
    void Update()
    {
        DontDestroyOnLoad(EscToGoToMenu);
        DontDestroyOnLoad(collector);
        i.maxValue = 31;
        DontDestroyOnLoad(storage);  //carries the data over to the simulation scene
    }
}
