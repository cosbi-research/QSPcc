import roadrunner; 
import time;
from roadrunner import Config
Config.setValue(Config.MAX_OUTPUT_ROWS, 100000);

start=time.clock(); 

rr = roadrunner.RoadRunner("Giordano2020.xml"); 
rr.getIntegrator().setValue('relative_tolerance', 1e-3); 
rr.getIntegrator().setValue('absolute_tolerance', 1e-6); 

rr.simulate(0,1000,100000);

print(time.clock()-start);
