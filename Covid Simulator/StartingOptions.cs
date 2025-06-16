using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class StartingOptions : MonoBehaviour
{
    public int population;
    public int healthCareCapacity;
    public int infected;
    public Slider pop, hc, nI;
    public Toggle coop;

    public void Update()
    {
        /*
        population = PlayerPrefs.GetInt("Population");
        healthCareCapacity = PlayerPrefs.GetInt("Health Care Capacity");
        infected = PlayerPrefs.GetInt("Number of Infected");
        */

        population = (int)pop.value;    //casts slider values to int and sets the variable to it's value
        healthCareCapacity = (int)hc.value;
        infected = (int)nI.value;

    }
}
