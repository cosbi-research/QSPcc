//copy to resources/Memrolib.c and also memrolib.h
// also code will be rewwritten by code in rousources
// tomasoni@cosbi.eu
//copy to resources/Memrolib.c and also memrolib.h
// also code will be rewwritten by code in rousources
// tomasoni@cosbi.eu
#ifndef __USER_IMPLEMENTED_MEM_FUNCTIONS__
#define __USER_IMPLEMENTED_MEM_FUNCTIONS__
/*user impelmented struct functions*/
#ifndef __HEADERS__
#define __HEADERS__
#define ADD_BYTES(base_addr, num_bytes) (((char *)(base_addr)) + (num_bytes))
#define SUBTRACT_BYTES(base_addr, num_bytes) (((char *)(base_addr)) - (num_bytes))
typedef int bool;
#define true 1
#define false 0
#define SIZE 45000000
#include <stdio.h>
#include <time.h>
#include <gperftools/tcmalloc.h>
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <nvector/nvector_serial.h>
#include <sundials/sundials_types.h>
#include "mkl.h"
#include "loadLib.h"
#include "structures.h"
#include "user-structures.h"
#include "sundialsLib.h"
#include "mklLib.h"
#include "memoryLib.h"
#include "matrixLib.h"
#include "saveLib.h"
#include "functions.h"
#include "unittest.h"
#endif
clock_t __tic;
double __toc;


int tests_run = 0;



static char * test_mem_case1() {
    
    init_metadata();
    void* starting_addr = get_address();
    double* big = (double *)starting_addr;
    
    
    
    
    void* firsty = mem_alloc(big, 20);
    
    void* secondy = mem_alloc(big, 20);
    
    void* thirdy = mem_alloc(big, 20);
    
    
    
    
    mu_assert("count free blocks1", num_free_blocks() == 1);
    
    mem_free(big, firsty);
    
    mu_assert("count free blocks2", num_free_blocks() == 2);
    
    mem_free(big, secondy);
    
    mu_assert("count free blocks3", num_free_blocks() == 2);
    
    mem_free(big, thirdy);
    
    mu_assert("count free blocks4", num_free_blocks() == 1);
    
    
    
    
    //Create 5 blocks of memory and free one in the center
    void* first = mem_alloc(big, 1);
    
    mu_assert("count free blocks5", num_free_blocks() == 1);
    
    mu_assert("first one placed should be at the beginning", (long)first == (long)starting_addr);
    
    void* second = mem_alloc(big, 20);
    
    mu_assert("count free blocks100", num_free_blocks() == 1);
    
    void* third = mem_alloc(big, 9);
    
    mu_assert("count free blocks6", num_free_blocks() == 1);
    
    mem_free(big, second);
    
    mu_assert("count free block8787", num_free_blocks() == 2);
    
    void* fifth = mem_alloc(big, 30);
    
    mu_assert("count free blocks7", num_free_blocks() == 2);
    
    void* fourth = mem_alloc(big, 20);
    
    mu_assert("Case 1: Error freeing and placing block of size 20", (long)fourth == (long)second);
    
    mu_assert("count free blocks8", num_free_blocks() == 1);
    
    //free memory from left to right
    mem_free(big, first);
    
    mu_assert("count free blocks9", num_free_blocks() == 2);
    
    mem_free(big, fourth);
    
    
    
    mu_assert("count free blocks10", num_free_blocks() == 2);
    
    
    
    mem_free(big, third);
    
    mem_free(big, fifth);
    
    
    
    void* sixth = mem_alloc(big, 10);
    
    mu_assert("Placing block in what should be one free block", (long)starting_addr == (long)sixth);
    
    mem_free(big, sixth);
    
    
    
    
    //go through a bunch of blocks and allocated/deallocate them
    for(int i = 0; i < 100; i++){
        int r = rand() % 3;
        
        if(r == 0){
            
            void* one = mem_alloc(big, 1000);
            
            mu_assert("Failed to allocate a block", one != NULL);
            
            void* two = mem_alloc(big, 4000);
            
            mu_assert("Failed to allocate a block", two != NULL);
            
            void* three = mem_alloc(big, 200);
            
            mu_assert("Failed to allocate a block", three != NULL);
            
            void* four = mem_alloc(big, 1);
            
            mu_assert("Failed to allocate a block", four != NULL);
            
            
            mem_free(big, one);
            
            mem_free(big, two);
            
            mem_free(big, four);
            
            mem_free(big, three);
            
            
            
        }
        
        else if(r == 1){
            //mixed allocating blocks and releasin g
            void* one = mem_alloc(big, 1000);
            
            mu_assert("Failed to allocate a block", one != NULL);
            
            void* two = mem_alloc(big, 4000);
            
            mu_assert("Failed to allocate a block", two != NULL);
            
            mem_free(big, one);
            
            
            
            void* three = mem_alloc(big, 60000);
            
            mu_assert("Failed to allocate a block", three != NULL);
            
            void* four = mem_alloc(big, 300000);
            
            mu_assert("Failed to allocate a block", four != NULL);
            
            
            void* five = mem_alloc(big, 500000);
            
            
            mu_assert("Failed to allocate a block", five != NULL);
            
            mem_free(big, five);
            
            mem_free(big, four);
            
            mem_free(big, three);
            
            mem_free(big, two);
            
            
            
        }
        
        
        
        if(r == 2){
            
            
            void* one = mem_alloc(big, 1000);
            
            mu_assert("Failed to allocate a block", one != NULL);
            
            void* two = mem_alloc(big, 4000);
            
            mu_assert("Failed to allocate a block", two != NULL);
            
            void* three = mem_alloc(big, 60000);
            
            mu_assert("Failed to allocate a block", three != NULL);
            
            void* four = mem_alloc(big, 300000);
            
            mu_assert("Failed to allocate a block", four != NULL);
            
            mem_free(big, three);
            mem_free(big, four);
            
            void* five = mem_alloc(big, 60000);
            
            mu_assert("Failed to allocate a block", three != NULL);
            
            void* six = mem_alloc(big, 300000);
            
            mu_assert("Failed to allocate a block", four != NULL);
            
            mem_free(big, five);
            
            mem_free(big, six);
            
            mem_free(big, one);
            
            mem_free(big, two);
            
        }
        
        
    }
    
    
    first = mem_alloc(big, 1);
    
    mu_assert("placing just a single block after many", (long)first == (long)starting_addr);
    
    mem_free(big, first);
    
    
    
    return 0;
}

static char * test_seg_fault() {
    
    
    void* one;
    void* two;
    void* three;
    void* four;
    void* five;
    void* six;
    void* seven;
    void* eight;
    void* nine;
    void* ten;
    void* eleven;
    void* twelve;
    void* thirteen;
    void* fourteen;
    void* fifteen;
    void* sixteen;
    void* seventeen;
    void* eightteen;
    void* nineteen;
    void* twenty;
    
    __tic = clock();
    
    int switchy = 1;
    
    
    for(int i = 0; i < 10000000; i++){
        
        init_metadata();
        void* starting_addr = get_address();
        double* big = (double *)starting_addr;
        
        if(switchy){
            
            one = mem_alloc(big, SIZE / 16);
            two = mem_alloc(big, SIZE / 8);
            three = mem_alloc(big, SIZE / 8);
            four = mem_alloc(big, SIZE / 32);
            five = mem_alloc(big, SIZE / 8);
            six = mem_alloc(big, SIZE / 64);
            seven = mem_alloc(big, SIZE / 32);
            eight = mem_alloc(big, SIZE / 32);
            nine = mem_alloc(big, SIZE / 32);
            ten = mem_alloc(big, SIZE / 128);
            eleven = mem_alloc(big, SIZE / 16);
            twelve = mem_alloc(big, SIZE / 256);
            thirteen = mem_alloc(big, SIZE / 128);
            fourteen = mem_alloc(big, SIZE / 128);
            fifteen = mem_alloc(big, SIZE / 64);
            sixteen = mem_alloc(big, SIZE / 256);
            seventeen = mem_alloc(big, SIZE / 32);
            eightteen = mem_alloc(big, SIZE / 64);
            nineteen = mem_alloc(big, SIZE / 128);
            twenty = mem_alloc(big, SIZE / 128);
            
            switchy = (switchy + 1) % 2;
        }
        
        
        else{
            
            int arr[20] = {0};
            int counter = 0;
            
            while(counter < 20){
                int ran = rand() % 20;
                if(arr[ran] == 0){
                    counter++;
                    arr[ran] = 1;
                    
                    if(ran == 0){
                        mem_free(big, one);
                    }
                    
                    if(ran == 1){
                        mem_free(big, two);
                        
                    }
                    
                    if(ran == 2){
                        mem_free(big, three);
                        
                    }
                    
                    if(ran == 3){
                        mem_free(big, four);
                        
                    }
                    
                    if(ran == 4){
                        mem_free(big, five);
                        
                    }
                    
                    if(ran == 5){
                        mem_free(big, six);
                        
                    }
                    
                    if(ran == 6){
                        mem_free(big, seven);
                        
                    }
                    
                    if(ran == 7){
                        
                        mem_free(big, eight);
                    }
                    
                    if(ran == 8){
                        mem_free(big, nine);
                        
                    }
                    
                    if(ran == 9){
                        mem_free(big, ten);
                        
                    }
                    
                    if(ran == 10){
                        mem_free(big, eleven);
                        
                    }
                    
                    if(ran == 11){
                        mem_free(big, twelve);
                        
                    }
                    
                    if(ran == 12){
                        mem_free(big, thirteen);
                        
                    }
                    
                    if(ran == 13){
                        mem_free(big, fourteen);
                        
                    }
                    
                    if(ran == 14){
                        mem_free(big, fifteen);
                        
                    }
                    
                    if(ran == 15){
                        mem_free(big, sixteen);
                        
                    }
                    
                    if(ran == 16){
                        mem_free(big, seventeen);
                        
                    }
                    
                    if(ran == 17){
                        mem_free(big, eightteen);
                        
                    }
                    
                    if(ran == 18){
                        mem_free(big, nineteen);
                        
                    }
                    
                    if(ran == 19){
                        mem_free(big, twenty);
                        
                    }
                    
                    
                }
                
            }
            
            switchy = (switchy + 1) % 2;
        }
        
    }
    
    __toc = ((double)clock() - __tic)/CLOCKS_PER_SEC;
    printf("%.5e\n", __toc);
    
    
    return 0;
}


static char * test_time() {
    
    char* one;
    char* two;
    char* three;
    char* four;
    char* five;
    char* six;
    char* seven;
    char* eight;
    char* nine;
    char* ten;
    char* eleven;
    char* twelve;
    
    
    int switchy = 1;
    
    __tic = clock();
    
    
    for(int i = 0; i < 1; i++){
        if(switchy == 1){
            
            one = malloc(300000);
            for(int i = 0; i < 300000; i++){
                one[i] = 'a';
            }
            two = malloc( 40000);
            for(int i = 0; i < 40000; i++){
                two[i] = 'b';
            }
            three = malloc( 10000);
            for(int i = 0; i < 10000; i++){
                three[i] = 'c';
            }
            four = malloc( 23000);
            for(int i = 0; i < 23000; i++){
                four[i] = 'd';
            }
            five = malloc(40000);
            for(int i = 0; i < 40000; i++){
                five[i] = 'e';
            }
            six = malloc(10000);
            for(int i = 0; i < 10000; i++){
                six[i] = 'f';
            }
            seven = malloc(5000);
            for(int i = 0; i < 5000; i++){
                seven[i] = 'g';
            }
            eight = malloc(2000);
            for(int i = 0; i < 2000; i++){
                eight[i] = 'h';
            }
            nine = malloc(5000);
            for(int i = 0; i < 5000; i++){
                nine[i] = 'i';
            }
            ten = malloc(10000);
            for(int i = 0; i < 10000; i++){
                ten[i] = 'j';
            }
            eleven = malloc(10000);
            for(int i = 0; i < 10000; i++){
                eleven[i] = 'r';
            }
            twelve = malloc(40000);
            for(int i = 0; i < 40000; i++){
                twelve[i] = 'z';
            }
            
            switchy = (switchy + 1) % 2;
            
        }
        
        
        else{
            
            free(one);
            free(two);
            free(three);
            free(four);
            free(five);
            free(six);
            free(seven);
            free(eight);
            free(nine);
            free(ten);
            free(eleven);
            free(twelve);
            
            switchy = (switchy + 1) % 2;
            
            
        }
        
        
    }
    
    __toc = ((double)clock() - __tic)/CLOCKS_PER_SEC;
    
    printf("%.5e\n", __toc);
    
    
    return 0;
}

static char * test_correctness(){
    
    init_metadata();
    void* starting_addr = get_address();
    double* big = (double *)starting_addr;
    
    char* one;
    char* two;
    char* three;
    char* four;
    char* five;
    
    one = (char *)mem_alloc(big, sizeof(char) * 26);
    two = (char *)mem_alloc(big, sizeof(char) * 26);
    three = (char *)mem_alloc(big, sizeof(char) * 26);
    four = (char *)mem_alloc(big, sizeof(char) * 26);
    five = (char *)mem_alloc(big, sizeof(char) * 26);
    
    char alphabet[26] = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    
    for(int i = 0; i < 4; i++){
        for(int j = 0; j < 26; j++){
            if(i == 0){
                one[j] = alphabet[(j + i) % 26];
            }
            
            if(i == 1){
                two[j] = alphabet[(j + i) % 26];
            }
            
            if(i == 2){
                three[j] = alphabet[(j + i) % 26];
                
            }
            if(i == 3){
                four[j] = alphabet[(j + i) % 26];
                
            }
            if(i == 4){
                five[j] = alphabet[(j + i) % 26];
                
                
            }
        }
    }
    
    for(int i = 0; i < 4; i++){
        for(int j = 0; j < 26; j++){
            if(i == 0){
                mu_assert("first correctness", one[(j)] == alphabet[(j + i) % 26]);
            }
            
            if(i == 1){
                mu_assert("second correctness", two[j] == alphabet[(j + i) % 26]);
            }
            
            if(i == 2){
                mu_assert("third correctness", three[j] == alphabet[(j + i) % 26]);
                
            }
            if(i == 3){
                mu_assert("fourth correctness", four[j] == alphabet[(j + i) % 26]);
                
            }
            if(i == 4){
                mu_assert("five correctness", five[j] == alphabet[(j + i) % 26]);
                
                
            }
        }
    }
    
    return 0;
    
    
}

static char * addon(){
    
    init_metadata();
    void* starting_addr = get_address();
    double* big = (double *)starting_addr;
    
    int counter = 1;
    
    while(mem_alloc(big, 1000) != NULL){
        counter++;
    }
    
    printf( "%d",counter);
    
    
    
    
    
    return 0;
}



static char * all_tests() {
    printf("here");
    //mu_run_test(test_mem_case1);
    //mu_run_test(test_seg_fault);
    mu_run_test(test_time);
    return 0;
}

int run() {
    char *result = all_tests();
    if (result != 0) {
        printf("%s\n", result);
    }
    else {
        printf("ALL TESTS PASSED\n");
    }
    printf("Tests run: %d\n", tests_run);
    
    return result != 0;
}


#endif

