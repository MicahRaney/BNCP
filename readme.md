# Basic NXT Communication Protocol
Simplifying and abstracting the communication and control of the [Lego Mindstorms NXT](https://shop.lego.com/en-US/LEGO-MINDSTORMS-NXT-2-0-8547), allowing for networked control in Java. I built this project in 2015 while a sophomore in high school making projects with my Lego Mindstorms and it has not been updated since.

---

## Usage

My main project expanding off of this protocol was a network remote of my Lego Mindstorms through a java client on my PC. It enabled me to drive around wirelessly or through a series of networked relays instead of over the Bluetooth protocol shipped with the NXT.

## Communication

The protocol is based off of various packet types, as defined in the io package. Different packet types either initialize different Mindstorms NXT functions (e.g. a sensor), or may also reconfigure/control an active component (e.g. change a motor speed).

## leJOS Attribution

The BNCP protocol was built off of [leJOS](http://www.lejos.org/), an alternative firmware for the NXT that allowed programming the device in Java. I developed the protocol to allow control of the NXT remotely, but the credit goes to the leJOS team for building their own APIs for controlling the device, upon which BNCP was built.

## Background

I built this project back in 2015, during my freshman year in High School. Consequently, though it works beautifully, the code may not be written according to convention or formatted consistently. I decided to upload this project to my GitHub in August 2017 so it would not get lost in time and as a reminder of my first real coding projects.
