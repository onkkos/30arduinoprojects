#include <AFMotor.h>
String message;

AF_DCMotor Motor1(1);
AF_DCMotor Motor2(3);

int velocity;
int direction;
int leftRightValue;

void setup() {
  
  // Start USB communication
  Serial.begin(9600);
  velocity = 0;
  direction = 1; //FORWARD
  Serial.println("Initialized.");
}

void loop(){
  while(Serial.available())
  {//while there is data available on the serial monitor
    message+=char(Serial.read());//store string from serial command
  }
  if(!Serial.available())
  {
    if(message!="")
    {//if data is available
      Serial.println(message);
      parsingMessage(message);
      message=""; //clear the data
    }
  }
  delay(1000); //delay
}

void parsingMessage(String message){

    String* messageSplitted = split(message,"=");
    String attribute = messageSplitted[0];
    int value = String(messageSplitted[1]).toInt();
    if( attribute == "fb"){
        direction = value;
        Serial.println(direction);
        Motor1.setSpeed(velocity);
        Motor2.setSpeed(velocity);
        Motor1.run(direction);
        Motor2.run(direction);
      }
    if(attribute == "lr"){
        if(value < 50){
            Serial.println("LEFT");
            Motor2.setSpeed(velocity);
            Motor2.run(direction);
        }
        if(value > 50){
            Serial.println("RIGHT");
            Motor1.setSpeed(velocity);
            Motor1.run(direction);
        }
        if(value == 50){
            Serial.println(direction);
            Motor1.setSpeed(velocity);
            Motor2.setSpeed(velocity);
            Motor1.run(direction);
            Motor2.run(direction);
        }
      }
    if(attribute == "ve"){
        velocity = value;
        Serial.println(direction);
        Motor1.setSpeed(velocity);
        Motor2.setSpeed(velocity);
        Motor1.run(direction);
        Motor2.run(direction);
      }
    
  }

String* split(String text,String pattern){
    
    int patternIndex = text.indexOf(pattern);
    if(patternIndex != -1){
      String attribute = text.substring(0,patternIndex);
      String value = text.substring(patternIndex+1); //porque o substring pega o começo aberto e o final fechado.
      String splitResult[2] = {attribute,value};
      return splitResult;
      }
      return 0;
  }
