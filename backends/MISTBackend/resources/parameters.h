#ifndef __MODEL_PARAMETERS_H__
#define __MODEL_PARAMETERS_H__

/*==========================================================================*/
/*---------------------------- DATA STRUCTURES -----------------------------*/
/*==========================================================================*/

/*
* Important. Maybe this could be factored out and instead we'd do arithmetic
* like [(k * N_COLS) + N_ROWS] ... but this is the number of 'ADD_COLUMN()'
* statements in the data structure below, plus the sum of all columns for all
* treatments (the sum of all 'TR_<treatment>' values that occur). This would
* not be a problem if we use dynamic memory allocation. Maybe in next version.
*/

#define NUM_PARAMS      3               /* CORE(1) + INIT(1) + PARAM(1) = 3 */

/*-------------------------------------------------------------- SIMULATION */

/*
* Universal parameters. Populated using CSV file via 'read_csv()' or by one of
* the supported Interfaces ('core/interfaces/<interface>'). The order in which
* these are specified is the order in which columns are expected. These should
* only be changed if the MIST format evolves. This section is reserved for
* UNIVERSAL single-column-multiple-row structures.
*/

/* SIMULATION */        ADD_COLUMN(simulation_setting, N_SIMULATION);
#define T0              GET_COLUMN(simulation_setting, t_start)
#define NOUT            GET_COLUMN(simulation_setting, t_final)
#define TINC            GET_COLUMN(simulation_setting, t_interval)
#define MAX_STEP        GET_COLUMN(simulation_setting, t_maxstep)
#define TIGHTEN         GET_COLUMN(simulation_setting, t_tighten)
#define RTOL            GET_COLUMN(simulation_setting, tol_relative)
#define ATOL            GET_COLUMN(simulation_setting, tol_absolute)

/*-------------------------------------------------------------- INIT_CONDS */

/*
* Initial conditions. The values supplied in this column of input will be used
* as the initial state of the system.
*/

/* INIT_CONDS */        ADD_COLUMN(/*--<INIT_COND_COLUMN_NAME>--*/, N_SPECIES);
#define IM(n)           GET_COLUMN(/*--<INIT_COND_COLUMN_NAME>--*/, n)

/*------------------------------------------------------------ USER_DEFINED */

/* U01 */               ADD_COLUMN(/*--<PARAMETERS_COLUMN_NAME>--*/, U01);
#define PM(n)           GET_COLUMN(/*--<PARAMETERS_COLUMN_NAME>--*/, n)

/*--------------------------------------------------------------------------*/

#endif /* __MODEL_PARAMETERS_H__ */