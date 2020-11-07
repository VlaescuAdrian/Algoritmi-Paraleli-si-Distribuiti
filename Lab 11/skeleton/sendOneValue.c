#include<mpi.h>
#include<stdio.h>

/**
 * @author cristian.chilipirea
 * Run: mpirun -np 2 ./a.out
 */

int main(int argc, char * argv[]) {
	int rank;
	int nProcesses;
	int v, flag = 0;
	MPI_Init(&argc, &argv);
	MPI_Status stat;
    MPI_Request request1, request2;
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &nProcesses);

	if(rank==0) {
        v = 5;
		MPI_Isend(&v, 1, MPI_INT, 1, 0, MPI_COMM_WORLD, &request1);
        v = 10;
        while (!flag) {
            MPI_Test(&request1, &flag, &stat);
        }

//        MPI_Wait(&request1, &stat);
//		printf("Got here! MPI_Send is non-blocking because there is no one to do a receive.\n");
	} else if (rank == 1){
        MPI_Irecv(&v, 1, MPI_INT, 0, 0, MPI_COMM_WORLD, &request2);
        while (!flag) {
            MPI_Test(&request2, &flag, &stat);
        }
        printf("the number is %d\n", v);	
    }

	printf("Bye from %i/%i\n", rank, nProcesses);
	MPI_Finalize();
	return 0;
}