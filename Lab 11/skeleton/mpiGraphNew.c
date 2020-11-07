#include<mpi.h>
#include<stdio.h>
#include<stdlib.h>

/**
 * @author cristian.chilipirea
 * Run: mpirun -np 2 ./a.out
 */

int size = 10;
int graph[][2] = { { 0, 1 }, { 0, 4 }, { 0, 5 }, 
                  { 1, 0 }, { 1, 2 }, { 1, 6 }, 
                  { 2, 1 }, { 2, 3 }, { 2, 7 },
                  { 3, 2 }, { 3, 4 }, { 3, 8 },
                  { 4, 0 }, { 4, 3 }, { 4, 9 },
                  { 5, 0 }, { 5, 7 }, { 5, 8 },
                  { 6, 1 }, { 6, 8 }, { 6, 9 },
                  { 7, 2 }, { 7, 5 }, { 7, 9 },
                  { 8, 3 }, { 8, 5 }, { 8, 6 },
                  { 9, 4 }, { 9, 6 }, { 9, 7 } };

int main(int argc, char * argv[]) {
	int rank;
	int nProcesses;
	int *v;
	MPI_Init(&argc, &argv);
	MPI_Status status;

	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &nProcesses);
	printf("Hello from %i/%i\n", rank, nProcesses);

	v = (int*) malloc(sizeof(int) * size);
	int* vRecv = malloc(sizeof(int) * 10);

	if(rank==0) {
		for (int i = 0; i < size; i++) {
			v[i] = -1;
			vRecv[i] = -1;
		}

		for (int i = 0; i < 30; i++) {
			if (graph[i][0] == rank) {
				MPI_Send(v, size, MPI_INT, graph[i][1] , 0, MPI_COMM_WORLD);
			}
		}

		for (int i = 0; i < 30; i++) {
			if(graph[i][0] == rank && graph[i][1] != v[rank]) {
				MPI_Recv(vRecv, size, MPI_INT, MPI_ANY_SOURCE , 0, MPI_COMM_WORLD, &status);
				
				for (int j = 0; j < 10; j++) {
					if (vRecv[j] != -1) {
						v[j] = vRecv[j];
					}
				}
				
			}
		}

		for (int i = 0; i < 10; i++) {
			if (v[i] == rank) {
				MPI_Send(v, 10, MPI_INT, i, 0, MPI_COMM_WORLD);
			}
		}
	} else {
		MPI_Recv(v, size, MPI_INT, MPI_ANY_SOURCE , 0, MPI_COMM_WORLD, &status);
		v[rank] = status.MPI_SOURCE;

		for (int i = 0; i < 30; i++) {
			if(graph[i][0] == rank && graph[i][1] != v[rank]) {
				MPI_Send(v, size, MPI_INT, graph[i][1] , 0, MPI_COMM_WORLD);
			}
		}

		for (int i = 0; i < 30; i++) {
			if(graph[i][0] == rank && graph[i][1] != v[rank]) {
				MPI_Recv(vRecv, size, MPI_INT, MPI_ANY_SOURCE , 0, MPI_COMM_WORLD, &status);
				
				for (int j = 0; j < 10; j++) {
					if (vRecv[j] != -1) {
						v[j] = vRecv[j];
					}
				}
				
			}
		}

		MPI_Send(v, 10, MPI_INT, v[rank], 0, MPI_COMM_WORLD);

		MPI_Recv(v, 10, MPI_INT, v[rank], 0, MPI_COMM_WORLD, &status);

		for (int i = 0; i < 10; i++) {
			if (v[i] == rank) {
				MPI_Send(v, 10, MPI_INT, i, 0, MPI_COMM_WORLD);
			}
		}

	}

	for(int i = 0; i < 10; i++) {
		printf("%d ", v[i]);
	}
	printf("\n");

	// printf("Bye from %i/%i\n", rank, nProcesses);
	MPI_Finalize();
	return 0;
}