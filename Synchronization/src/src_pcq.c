#include <pthread.h>
#include <stdlib.h>

#include "csesem.h"
#include "pcq.h"

/* This structure must contain everything you need for an instance of a
 * PCQueue.  The given definition is ABSOLUTELY NOT COMPLETE.  You will
 * have to add several items to this structure. */
struct PCQueue {
  int slots;
    //int lastUsedIndex;
  int front;
  int rear;
  int busy;
  CSE_Semaphore available, items;
  pthread_mutex_t mutex;
  void **queue;
};

/* The given implementation performs no error checking and simply
 * allocates the queue itself.  You will have to create and initialize
 * (appropriately) semaphores, mutexes, condition variables, flags,
 * etc. in this function. */
PCQueue pcq_create(int slots) {
    PCQueue pcq;
    pcq = calloc(1, sizeof(*pcq));
    pcq->queue = calloc(slots, sizeof(void *));
    pcq->slots = slots;
    //pcq->lastUsedIndex = -1;
    pcq->busy = 0;
    pcq->front = pcq->rear = 0; //CSAPP
    pthread_mutex_init(&pcq->mutex, NULL);
    pcq->available = csesem_create(slots);
    pcq->items = csesem_create(0);

    return pcq;
}

/* This implementation does nothing, as there is not enough information
 * in the given struct PCQueue to even usefully insert a pointer into
 * the data structure. */
void pcq_insert(PCQueue pcq, void *data) {
  csesem_wait(pcq->available);
  pthread_mutex_lock(&pcq->mutex);
  
  pcq->busy = 1;
  pcq->queue[(++pcq->rear)%(pcq->slots)] = data; //CSAPP

  /*
  if (pcq->lastUsedIndex == -1) {
    pcq->lastUsedIndex = 0;
    pcq->queue[0] = data;
  } else {
    if (pcq->lastUsedIndex < pcq->slots - 1) {
      pcq->lastUsedIndex++;
      pcq->queue[pcq->lastUsedIndex] = data;
    }
  }
  */

  pcq->busy = 0;

  pthread_mutex_unlock(&pcq->mutex);
  csesem_post(pcq->items);
}

/* This implementation does nothing, for the same reason as
 * pcq_insert(). */
void *pcq_retrieve(PCQueue pcq) {
  void *ret_val = NULL;
  
  csesem_wait(pcq->items);
  pthread_mutex_lock(&pcq->mutex);

  pcq->busy = 1;
  ret_val = pcq->queue[(++pcq->front) % (pcq->slots)]; // CSAPP

  /*
  ret_val = pcq->queue[0];
  for (int i = 0; i < pcq->slots - 1; i++) {
    pcq->queue[i] = pcq->queue[i+1];
  }
  */

  pcq->busy = 0;

  pthread_mutex_unlock(&pcq->mutex);
  csesem_post(pcq->available);

  return ret_val;
}

/* The given implementation blindly frees the queue.  A minimal
 * implementation of this will need to work harder, and ensure that any
 * synchronization primitives allocated here are destroyed; a complete
 * and correct implementation will have to synchronize with any threads
 * blocked in pcq_insert() or pcq_retrieve().
 *
 * You should implement the complete and correct clean teardown LAST.
 * Make sure your other operations work, first, as they will be tightly
 * intertwined with teardown and you don't want to be debugging it all
 * at once!
 */
void pcq_destroy(PCQueue pcq) {
  if (!pcq->busy) {
    free(pcq->queue);
    csesem_destroy(pcq->available);
    csesem_destroy(pcq->items);
    pthread_mutex_destroy(&pcq->mutex);
    free(pcq);
  }
}

