using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CameraRotation : MonoBehaviour
{
    public GameObject camera;
    public float ry,x;
    public Vector3 vector;
    
    // Update is called once per frame
    void Update()
    {
        ry = camera.transform.rotation.y;
        x = camera.transform.position.x;

        if(Input.GetKey("a") || Input.GetKey(KeyCode.LeftArrow)) // if a is pressed...
        {
            if(ry >= 0.67f && x < 26.3)
            {
                camera.transform.RotateAround(vector, Vector3.down, -50 * Time.deltaTime); // rotate 50 degrees
            }
        }

        if (Input.GetKey("d") || Input.GetKey(KeyCode.RightArrow)) // if d is pressed...
        {
            if(ry >= 0.65f && x > -26.6)
            {
                camera.transform.RotateAround(vector, Vector3.down, 50 * Time.deltaTime); // rotate 50 degrees
            }
        }
    }
}
