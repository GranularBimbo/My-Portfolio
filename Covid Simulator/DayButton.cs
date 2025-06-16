using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class DayButton : MonoBehaviour
{
    public World world;
    public bool canCall = false;
    public Button button;
    public Image buttonImage;
    public Text buttonText;

    public void CallDay()
    {
        if (canCall)
        {
            world.Day();

            startWait();

            canCall = false;
        }
    }

    public void Update()
    {
        if(canCall == false)
        {
            button.enabled = false;
            buttonImage.enabled = false;
            buttonText.enabled = false;
        }

        else
        {
            button.enabled = true;
            buttonImage.enabled = true;
            buttonText.enabled = true;
        }
    }

    public void Start()
    {
        startWait();
    }

    public void startWait()
    {
        StartCoroutine(wait19Seconds());
    }

    IEnumerator wait19Seconds()
    {
        yield return new WaitForSeconds(19);

        canCall = true;
    }
}
