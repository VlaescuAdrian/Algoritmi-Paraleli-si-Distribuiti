#include<mpi.h>
#include<stdio.h>

/**
 * @author cristian.chilipirea
 * Run: mpirun -np 10 ./a.out
 */

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
	double recv, sent;
	MPI_Init(&argc, &argv);
	MPI_Status status;
	MPI_Request request;


	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	MPI_Comm_size(MPI_COMM_WORLD, &nProcesses);

	sent = rank;
	for (int i = 0; i < nProcesses; i++) {
		for (int j = 0; j < 30; j++) {
			if (graph[j][0] == rank) {
				MPI_Send(&sent, 1, MPI_DOUBLE, graph[j][1], 0, MPI_COMM_WORLD);
			}
		}
		for (int j = 0; j < 30; j++) {
			if (graph[j][1] == rank) {
				MPI_Recv(&recv, 1, MPI_DOUBLE, graph[j][0], 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
				if (recv > sent) {
					sent = recv;
				}
			}
		}
	}
	double leader = sent;
	// printf("The parent is %.0f\n", sent);
	
    MPI_Barrier(MPI_COMM_WORLD);
	
	if (rank == leader) {
		sent = 1;
	} else {
		sent = 0;
	}
    MPI_Barrier(MPI_COMM_WORLD);

	for (int i = 0; i < 1000; i++) {
		for (int j = 0; j < 30; j++) {
            if (graph[j][0] == rank) {
                MPI_Sendrecv(&sent, 1, MPI_DOUBLE, graph[j][1], 0, &recv, 1, MPI_DOUBLE, graph[j][1], 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
                sent = (recv + sent) / 2;
               // printf("trimis si receptionat %f %f\n", recv, sent);
            }
            // if (graph[j][1] == rank) {
            //     MPI_Sendrecv(&sent, 1, MPI_DOUBLE, graph[j][0], 0, &recv, 1, MPI_DOUBLE, graph[j][1], 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
            //     sent = (recv + sent) / 2;
            // }
		}
	}

	printf("Bye from %i/%i and node numbers is %f\n", rank, nProcesses, 1/sent);
	MPI_Finalize();
	return 0;
}