
String message;

void setup() {
  
  // Start USB communication
  Serial.begin(9600);
  
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
      Serial.println(message); //show the data
      message=""; //clear the data
    }
  }
  delay(1000); //delay
}

