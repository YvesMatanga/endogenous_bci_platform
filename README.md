# endogenous_bci_platform
The Following platform describes the Matlab/Simulink platform designed for real time implementation of 
Endogenous Brain Computer Interfaces.

It comprises:

1. The folder "simulink model" containing the simulink models responsible for the signal processing of the EEG signals (slx files)

2. The folder "bci_control_app" that contains an optional JAVA platform that interfaces with the Simulink models and provides a GUI interface to control a computer cursor 

3. The folder "data" containing datasets for six session experiments of computer cursor control using motor imagery (SMR-based BCI) in which five  subjects participated ( mat files and csv files) including additional toolboxes to run demo matlab scripts.

4. The folder "media" that contains a demo video file that shows a computer cursor run of one of the subjects in the motor imagery experiment performed using the platform

Citation Information:

-- Platform : "A Matlab/Simulink Framework for real time Implementation of Endogenous Brain Computer Interfaces", Yves Matanga, Djouani Karim and Anish Kurien, 13th IEEE Africon Conference, September 2017 

-- Datasets : "Analysis of User Control Attainment in SMR-based Brain Computer Interfaces", Yves Matanga, Djouani Karim and Anish Kurien,
Journal of Artificial Intelligence in Medicine, Elsevier November 2017 (submitted)

System Operation
----------------

Calibration Phase :

1. On Matlab, Open the Calibration simulink model "bci_calibration_phase_research_system.slx"
2. Ensure that the EEG kit is connected to the computer and that its Sensor Block is accessible to Simulink
(if you are using gtec gnautilus, you may use the system as it is, else replace the gnautilus block on simulink with the appropriate one from your device manufacturer simulink libraries)
3. Using Netbeans, run the virtual bci platform java application and register BCI users
4. In the simulink model "bci_calibration_phase_research_system.slx" ensure that the correct PC IP address is set (else use the local address IP : "127.0.0.1")
5. Run the Simulink Model before starting an experiment in the Java application side
6. Specify experiment configurations on the Java application side then press the start button to start the simulation
7. Experiment Data may be retrieved at the end of a session in the Java application (database button)

Online Phase :

1. On Matlab, Open the online simulink model "bci_1d_horizontal_control_current_system_adaptive.slx"
2. Ensure that the EEG kit is connected to the computer and that its Sensor Block is accessible to Simulink
(if you are using gtec gnautilus, you may use the system as it is, else replace the gnautilus block on simulink with the appropriate one from your device manufacturer simulink libraries)
3. Using Netbeans, run the virtual bci platform java application and register/configure BCI users (if not existing)
4. In the simulink model "bci_calibration_phase_research_system.slx" ensure that the correct PC IP address is set (else use the local address IP : "127.0.0.1")
5. Run the Simulink Model before starting an experiment in the Java application side
6. Specify experiment configurations on the Java application side then press the start button to start the experiment
7. The experiment data may be retrieved on the Matlab worskpace after simulation is ended.

Note: - You may built your own desktop application for the experiment scene ("virtual bci platform java application").
      - Additional configurations on the simulink models can be customised under the model properties callback function rubric           
      on Simulink
      





