# Requirements specification

## Purpose of the application
- a system to model the power relay system of a spaceship
- to be used in conjunction with the Empty Epsilon Space Ship Simulator (http://daid.github.io/EmptyEpsilon/)
  - separate system, communicating with EE through http requests
- works by simulating the varying stability of power conditioning from reactors to ship's power distribution system (whish is simulated inside Empty Epsilon)

## UI draft
The main view of the application with its controls looks as follows:
<img src="https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/kayttoliittymaluonnos.jpg" width="750">

## Basic functionality
- the frequency, amplitude and phase of power input, represented by a signal generator object, fluctuates randomly over time
  - depends on the power draw: exacerbated by power draw of over 100%, mitigated by draw of under 100%
  - can change suddenly due to damage to ship's reactor (relayed from EE through http requests)
- the stability of power conditioning, i.e. how well the power relays are calibrated to the power input from the reactors, determines the amount of power available to the ship's systems
	-the user's task is to calibrate the relays by adjusting counterbalancing signal generators using a set of controls and oscilloscope displays, trying to maintain optimal performance
- the system regularly (every 1000 ms) updates the power level to EE using http POST requests
- the application keeps a log of changes in power status, which is saved to a database from which log reports can be generated
- the frequency and magnitude of fluctuation can be adjusted through a separate administration view, determining the difficulty of maintaining the balance

## Structure
- 4 reference oscillators, representing 4 power inputs from reactors
- 4 counterbalancing oscillators, adjustable by 4 sets of frequency, amplitude and phase controls
- 2 balancers, used to balance the outputs of the relays
- 1 main regulator, used to adjust the total power output to the ship's systems

## Classes/Objects
### Controls
- Fluctuator
- Oscillator
- Controller
- Balancer
- Regulator
- Switch
### UI
- Oscilloscope
- Slider
- Meter
- Knob
- Field
- Button
- Light
- Label
### Communication
- HTTPSender
- HTTPReceiver

## Further functionality
- fluctuation in power input stage increases reactor heat output in EE
- fluctuation in power output stage increases heat output of all systems in EE
- damage in EE to any system can cause blowback to power output stage
- excessive fluctuation starts to cause damage to systems in EE
- fuses between power stages can be blown by excessive peak power; linked to external devices through MIDI
- MIDI control of all adjustments through CCs
