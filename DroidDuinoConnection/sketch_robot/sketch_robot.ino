#include <AFMotor.h>

#include <NewPing.h>

#define MAX_DISTANCE 200
#define TRIGGER_PIN  14
#define ECHO_PIN     15


NewPing DistanceSensor(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);
AF_DCMotor Motor1(1);
AF_DCMotor Motor2(3);

long randInt;

void setup()
{
  Serial.begin(9600);  
  randomSeed(analogRead(0));
}

void loop()
{
    
    unsigned int cm = DistanceSensor.ping_cm();
    Serial.print("Distance: ");
    Serial.print(cm);
    Serial.println("cm");
    delay(1000);
    if(cm > 30){
    Serial.println("FRENTE");
    Motor1.setSpeed(255);
    Motor2.setSpeed(255);
    Motor1.run(FORWARD);
    Motor2.run(FORWARD);
    delay(1000);
    }else{
    Serial.println("RE");  
    Motor1.setSpeed(255);
    Motor2.setSpeed(255);
    Motor1.run(BACKWARD);
    Motor2.run(BACKWARD);
    delay(1000);
    randInt = random(10);
    Serial.print("Random: ");
    Serial.println(randInt);  
    if(randInt < 5){
    Serial.println("DIREITA");
    Motor1.setSpeed(255);
    Motor1.run(FORWARD);
    delay(1000);
    }else{
    Serial.println("ESQUERDA");
    Motor2.setSpeed(255);
    Motor2.run(FORWARD);
    }
  }
  

}

