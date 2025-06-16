using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TransferReference : MonoBehaviour
{
    public ReferenceManager Object;
    public ReferenceManager prefab;

    private void Start()
    {
        prefab.world = Object.world;
    }

    // Update is called once per frame
    void Update()
    {
        prefab.world = Object.world;
    }
}
