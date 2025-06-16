using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TextData : MonoBehaviour
{
    public StartingOptions data;
    public Text popText;
    public Text healthText;
    public Text infectedText;

    // Update is called once per frame
    void Update()
    {
        popText.text = data.population.ToString();
        healthText.text = data.healthCareCapacity.ToString();
        infectedText.text = data.infected.ToString();
    }
}
