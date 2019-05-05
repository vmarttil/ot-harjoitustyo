# Requirements specification

## Purpose of the application
- a system to model the power relay system of a spaceship
- to be used in conjunction with the Empty Epsilon Space Ship Simulator (http://daid.github.io/EmptyEpsilon/)
  - separate system, eventually communicating with EE through http requests (through EE's inbuilt http server)
- works by simulating the varying stability of power conditioning from reactors to ship's power distribution system (whish is simulated inside Empty Epsilon) and a cybernetic system with various feedback loops

## UI draft
The initial draft of the application UI looked as follows:
<img src="https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/pictures/kayttoliittymaluonnos.jpg" width="750">
The UI design was refined for the second draft:
<img src="https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/pictures/kayttoliittymaluonnos_2.jpg" width="750">
The finished UI design, which is still a prototype, is quite close to the second draft:
<img src="https://github.com/vmarttil/ot-harjoitustyo/blob/master/documentation/pictures/Finished_UI.png" width="750">
The UI was designed to be as immersive as possible, which is why it contains no nondiegetic elements. The next step is to redesign the colour scheme and graphical elements of th UI to match those of the Empty Epsilon UI.
## Functionality
- the frequency, amplitude and phase of power input, represented by a signal generator object, fluctuates randomly over time
  - depends both on the power draw and the reactor output (which will be controlled by a later reactor expansion)
  - exacerbated by power draw of over 100%, mitigated by draw of under 100%, same with reactor output
  - imbalance in the power channel stage will also cause instability and extra fluctuation  
- the stability of power conditioning, i.e. how well the power lines are calibrated to the power input from the reactors, determines the amount of power available to the ship's systems
  - the user's task is to calibrate the relays by adjusting counterbalancing signal generators using a set of controls and oscilloscope displays, trying to maintain optimal performance
  -the system also contains buttons that allow individual power lines to be switched offline or entirely shut down and restarted (and counterbalanced at the power channel stage) if they become too unstable
  -the final output stage allows the output of either channel to be boosted beyond 100% in order to balance them or to give a total power output of over 100%
- overdriving the system, excessive distortion of the waveform at the power line stage and imbalance at the power channel stage all cause heat in the system
  -the junction between the power lines and channels has a set of heat-sensitive power breakers which soak off the heat produced by the system, and if they overheat, they cut off and need to be reset, which takes a significant amount of time, during which the connection from the line to the channel is broken
  - the accumulation of heat can be monitored with a temperature gauge associated with each breaker
  - lowering power draw and keeping the waveform pure and balanced help dissipate heat
- the application keeps a log of the system state and regularly saves it to a file in JSON format

## Structure
- 4 reference oscillators, representing 4 power inputs from reactors
- 4 counterbalancing oscillators, adjustable by 4 sets of frequency, amplitude and phase controls
- 2 balancers, used to balance the outputs of the relays
- 2 main output regulator, used to adjust the total power output to the demands of the ship
- output gauges at line, channel and main output levels, with balance indicators at channel and main output stages and temperature gauge at each channel input
- cutoff switches on each line and breaker reset buttons at channel stage

## Classes/Objects
### Application logic
- Fluctuator
- Oscillator
- Power Line
- Power Channel
- Power Manager
### UI
- Oscilloscope
- Slider
- Gauge
- Button
- Light
- Label
### Logging
- JSON logger

## Additional functionality (future development)
- more sophisticated UI
- additional delays and timers to make offline and shutdown switches have a slightly delayed and gradual effect on power output
- link to Emppty Epsilon  
  - the system regularly (every 1000 ms) updates the power level to EE using http POST requests
  - testing and development requires setting up an environment with multiple EE stations on separate PCs
- damage in EE to any system can cause blowback to power output stage
- excessive fluctuation starts to cause damage to systems in EE
- MIDI control of all adjustments using custom controller based on physical controls and an Arduino
- integration with lights, sound effects and external devices like relays that need to be physically reset
- a separate system for simulating the alignment and calibration of reactor cores, which affects the power input

