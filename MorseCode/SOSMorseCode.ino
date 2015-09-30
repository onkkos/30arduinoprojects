/*
  MorseCode
  Turns on an LED on for one second, then off for one second, repeatedly.
  This example code is in the public domain.

  modified 30 Set 2015
  by Douglas Silva
 */

int ledPin = 12;
// the setup function runs once when you press reset or power the board
void setup() {
  
  // initialize digital pin 13 as an output.
  pinMode(ledPin, OUTPUT);
}

// the loop function runs over and over again forever
void loop() {
flash(200); flash(200);  flash(200); // S (...)
delay(300); // time between letters 
flash(500); flash(500); flash(500); // O (---)
flash(200); flash(200);  flash(200); // S (...)
delay(1000);
}

void flash(int duration){
  digitalWrite(ledPin, HIGH); 
  delay(duration);              
  digitalWrite(ledPin, LOW);
  delay(duration); 
}
