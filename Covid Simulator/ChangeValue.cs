using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class ChangeValue : MonoBehaviour
{
    public World world;
    public Text Max, Mid;
    public float maxValue, halfMaxValue;

    // Update is called once per frame
    void Update()
    {
        maxValue = world.dayCountLimit;
        halfMaxValue = maxValue / 2;

        Max.text = maxValue.ToString();
        Mid.text = halfMaxValue.ToString();
    }
}
