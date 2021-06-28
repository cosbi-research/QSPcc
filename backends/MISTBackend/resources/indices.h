#ifndef __MODEL_INDICES_H__
#define __MODEL_INDICES_H__

/*==========================================================================*/
/*------------------------------ CORE COLUMNS ------------------------------*/
/*==========================================================================*/

/* COL 1
* Ordered list of simulation settings. This is the order in which simulation
* setting values will be supplied in the SIMULATION input column of the CSV or
* other supported formats.
*/

#define N_SIMULATION    7
enum ORDER_SIMULATION
{
    t_start,                            /* simulation start time            */
    t_final,                            /* simulation end   time            */
    t_interval,                         /* observation interval             */
    t_maxstep,                          /* maximum step size                */
    t_tighten,                          /* enforce maximum step size after  */
    tol_relative,                       /* relative tolerance               */
    tol_absolute                        /* absolute tolerance               */
};

/*--------------------------------------------------------------------------*/

/* state variable indices */

#define N_SPECIES       /*--<NUMBER_OF_SPECIES>--*/
enum ORDER_SPECIES
{
    /*--<SPECIES_NAMES>--*/
};

/*==========================================================================*/
/*------------------------------ USER COLUMNS ------------------------------*/
/*==========================================================================*/

#define U01             /*--<NUMBER_OF_OTHER_PARAMETERS>--*/
enum /*--<PARAMETERS_COLUMN_NAME>--*/
{
    /*--<ALL_OTHER_PARAMETERS>--*/
};

/*--------------------------------------------------------------------------*/

#endif /* __MODEL_INDICES_H__ */