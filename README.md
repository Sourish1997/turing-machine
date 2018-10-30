## Single Tape Turing Machine Simulator

### Software Requirements

This is a Java Swing project and requires the java development kit to be installed for compilation. If you are only interested in running the application, you can simply launch the TuringMachine.jar file provided Java is installed on your system.

### Usage Instructions

- To create a turing machine, first fill in the set of states, input state, set of final states and the transition function. A set of 
values must always be comma separated. The productions of the transition function should be in the following form: (qcur, varcur, qres, varres, move). 

- Click on the 'Compile' button. On doing so, the Turing Machine will be initialized and the Control Panel will be activated.

- Type an input string into the text field to the left of the 'Load' button and then click on the 'Load' button to mount a string onto the tape.

- The 'Play' and 'Pause' buttons can then be used to start and stop the simulation respectively. The simulation will execute as per the productions in the loaded Turing Machine. Use the speed slider to adjust the speed of the animation to your liking.

- You can also save and load Turing Machines using the 'Save' and 'Load' options under the File menu. Turing Machines are saved as .tm files.

- The 'Refresh' button removes the currently loaded Turing Machine so that a new one can be created. This step is aso necessary before loading a saved Turing Machine or creating a Turing Machine from a Regex as demonstrated in the following section.

### Creating a Turing Machine from a Regular Expression

- As an experimental feature, I have also included a fetaure that allows you to automatically generate Turing Machines for regular expressions. This feature works only with 2 variable regular expressions at the moment.

- The only allowed variable names are 'a' and 'b' and only the following operators are recognized:
  - '|' for union
  - '*' for kleene star
  - '.' for concatenation
  - '(' and ')' for priority determination

- To build a Turing Machine from a regular expression, selelct the 'Create from Regex' option under the File Menu.

- Enter a regular expression in the prompt that appears and click the 'OK' button to generate and load the Turing Machine corresponding to the regex input.

- Compile and run the loaded Turing Machine as you would do normally.
