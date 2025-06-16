using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class valueChange : MonoBehaviour
{
    public Slider pop;
    public StartingOptions options;

    // Update is called once per frame
    void Update()
    {
        pop.minValue = options.nI.value + 1;
    }
}
