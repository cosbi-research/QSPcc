import roadrunner; 
import time;

start=time.clock(); 

rr = roadrunner.RoadRunner("capuani2015.xml"); 
rr.getIntegrator().setValue('relative_tolerance', 1e-3); 
rr.getIntegrator().setValue('absolute_tolerance', 1e-6); 

results = rr.simulate(0,120,12001);

print(time.clock()-start);

with open('results_roadrunner.csv', 'w') as f:
    for line in results:
        f.write(','.join([str(v) for v in line])+'\n')

