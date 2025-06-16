using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class nextScene : MonoBehaviour
{
    public DataStorage data;
    public Scene currentScene;
    public TransferData dataTransfer;
    
    public void changeScene()
    {
        data.TransferData();
        dataTransfer.ToPrefab();
        SceneManager.LoadScene("Simulator");
    }

}
