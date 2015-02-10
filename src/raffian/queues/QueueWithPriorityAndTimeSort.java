package raffian.queues;

import java.util.Comparator;
import java.util.PriorityQueue;

public class QueueWithPriorityAndTimeSort {
   enum TaskPriority {
      High, Medium, Low
   }

   class Task implements Comparable<Task> {   
      long time;
      TaskPriority priority;

      Task(TaskPriority p, long t) {      
         time = t;
         priority = p;
      }

      public int compareTo(Task task2) {
         return priority.compareTo(task2.priority);
      }
   }

   class HighPriorityWithTimeComparator implements Comparator<Task> {
      public int compare(Task task1, Task task2) {
         int compareResult = task1.compareTo(task2);
         if( compareResult == 0){           
            //same priority, now compare on time
            if( task2.time < task1.time)
               compareResult = 1;
            else 
               compareResult = -1;
         }                          
         return compareResult;
      }
   }
   
   public void buildAndTestQueue(){
      PriorityQueue<Task> queue = 
            new PriorityQueue<Task>(3, new HighPriorityWithTimeComparator());
      queue.add(new Task(TaskPriority.High, 9));
      queue.add(new Task(TaskPriority.Low, 7));
      queue.add(new Task(TaskPriority.Low, 3));
      queue.add(new Task(TaskPriority.High, 2));
      queue.add(new Task(TaskPriority.Medium, 5));
      queue.add(new Task(TaskPriority.Medium, 4));
      queue.add(new Task(TaskPriority.High, 6));
      queue.add(new Task(TaskPriority.High, 8));
      queue.add(new Task(TaskPriority.Low, 15));
      queue.add(new Task(TaskPriority.Low, 10));

      Task m = null;
      while ((m = queue.poll()) != null)
         System.out.println(String.format("Priority: %s, %d", m.priority, m.time));     
   }
   
   public void buildAndTestQueueWithoutTimeSort(){
      PriorityQueue<Task> queue = 
            new PriorityQueue<Task>(3);
      queue.add(new Task(TaskPriority.High, 9));
      queue.add(new Task(TaskPriority.Low, 7));
      queue.add(new Task(TaskPriority.Low, 3));
      queue.add(new Task(TaskPriority.High, 2));
      queue.add(new Task(TaskPriority.Medium, 5));
      queue.add(new Task(TaskPriority.Medium, 4));
      queue.add(new Task(TaskPriority.High, 6));
      queue.add(new Task(TaskPriority.High, 8));
      queue.add(new Task(TaskPriority.Low, 15));
      queue.add(new Task(TaskPriority.Low, 10));

      System.out.println("");
      Task m = null;
      while ((m = queue.poll()) != null)
         System.out.println(String.format("Priority: %s, %d", m.priority, m.time));     
   }

   public static void main(String args[]) {
      QueueWithPriorityAndTimeSort queueTest = new QueueWithPriorityAndTimeSort();
      queueTest.buildAndTestQueue(); 
      queueTest.buildAndTestQueueWithoutTimeSort();
   }
}




