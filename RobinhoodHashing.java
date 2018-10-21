import java.util.HashSet;
import java.util.Random;

public class RobinhoodHashing<T>
{

	 //Entry Class
	 private class Entry<E>{
		 E key;
		 boolean isDeleted;
		 
		 public Entry(E key)
		 {
			 this.key = key;
			 this.isDeleted = false;
		 }
	 }
	 
	 private int MaxDisplacement; //Max Displacement to search with in the range of the displacement
   private int size;
   private int capacity;
   private double loadFactor;
   private Entry<T>[] hSet;
   
	 //Constructor for Initializing size, Capacity, Load Factor
	public RobinhoodHashing() {
		capacity = 16;
		size = 0;
		loadFactor = 0.75;
		MaxDisplacement = -1;
		hSet = new Entry[capacity];
	}

	public RobinhoodHashing(int capacity) {
		this();
		this.capacity = capacity;
	}

	private int indexFor(T x) {
		return indexFor(hash(x.hashCode()), capacity);
	}
	
	private static int hash(int h) {
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
	}

	private static int indexFor(int h, int length) {
	    return h & (length-1);
	}
	
	
	
	
	public int displacement(T x, int loc)
	{
		int index = indexFor(x);
		return loc >= index ? loc - index : capacity + loc - index;
	}


	public boolean add(T x) {
    	 if(contains(x)) return false;
    	 if(size >= loadFactor * capacity) {
    		 rehash();
    	 }
    	 int loc = indexFor(x);
    	 Entry<T> temp = new Entry<>(x);
    	 int d = 0;
    	 while(true)
    	 {
    		 Entry<T> elmAtIndex = hSet[loc];
    		 if(elmAtIndex == null || elmAtIndex.isDeleted == true)
    		 {
    			 if(d > MaxDisplacement)
    			 {
    				 MaxDisplacement = d;
    			 }
    			 hSet[loc] = temp;
    			 if(hSet[loc].isDeleted == true)
    				 hSet[loc].isDeleted = false;
    			 size++;
    			 return true;
    		 }
    		 else if(displacement(elmAtIndex.key, loc) >= d)
    		 {
    			 d = d+1;
    			 loc = (loc + 1)%capacity;
    		 }
    		 else
    		 {
    			 hSet[loc] = temp;
    			 temp = elmAtIndex;
    			 loc = (loc + 1)%capacity;
    			 d = displacement(x, loc);
    		 }
    	 }
	}
	
	
	//Custom Method for print hash Table elements
   public void printHashTable()
   {
  	 for(int i = 0;i < capacity; i++)
  	 {
  		 if(hSet[i] != null && hSet[i].isDeleted == false)
  		 	System.out.println("Element at index "+i+" is "+hSet[i].key);
  	 }
   }
	
	//Rehashing as the loadfactor increased
	//Rehashing rehashes all the elements into a new entry array with twice the capacity of the current capacity
	private void rehash() {
		int oldCapacity = capacity;
		capacity = oldCapacity * 2;
		Entry<T>[] set = hSet;
		hSet = new Entry[capacity];
		size = 0; //setting size to zero
		MaxDisplacement = -1;
		for(int i = 0; i < oldCapacity; i++)
		{
			Entry<T> ent = set[i];
			if(ent != null  && ent.isDeleted == false)
			{
				this.add(ent.key);
			}
		}
	}

	
	public boolean contains(T x) {
	  return find(x) == -1 ?  false : true;
	}
	
	//Returns the index if the element is present otherwise returns -1
	public int find(T x) {
		int index = indexFor(x);
		int it = 0;
		Entry<T> elmAtLoc = hSet[index];
		while (it <= MaxDisplacement && elmAtLoc != null) {
			T key = elmAtLoc.key;
			if (key.equals(x) && elmAtLoc.isDeleted == false) {
				return index;
			}
			index = (index + 1) % capacity;
			elmAtLoc = hSet[index];
			it++;
		}
		return -1;
	}
	
	 /**
   * To count the number of distinct elements in an array
   * Static method
   * Adds each element of the array to a DoubleHashing Hash Set
   * Returns the size of the Hash Set
   * @param arr Array of generic type
   * @return int size of the created Hash Set
   * @exception IllegalStateException If empty Hash Set
   * @see IllegalStateException
   */
  static<T> int distinctElements(T[ ] arr) {
      if(arr.length == 0)
          throw new IllegalStateException("Empty Array Provided");
      RobinhoodHashing<T> dHashSet = new RobinhoodHashing<>();
      for(T elm: arr) {
          dHashSet.add(elm);
      }
      return dHashSet.size;
  }


    public T remove(T x) {
        int loc = find(x);
        if(loc != -1)
        {
          hSet[loc].isDeleted = true;
          size--;
          return hSet[loc].key;
        }
        return null;
    }

	public static void main(String[] args) {
/*        RobinhoodHashing<Integer> hash = new RobinhoodHashing<>();
        hash.add(12497);
        hash.add(28754);
        hash.add(34678);
        hash.add(45500);
        hash.add(56699);
        hash.add(67891);
        hash.add(70011);
        hash.add(81209);
        hash.printHashTable();
        hash.add(99194);
        hash.add(18608);
        hash.printHashTable(); */
		RobinhoodHashing<Integer> hSet = new RobinhoodHashing<>();
    HashSet<Integer> haSet = new HashSet<>();

    Timer timer = new Timer();
    for(int i=0; i<1000000; i++) {
        hSet.add(i);
    }
    System.out.println("RobinhoodHashing hash set contains 18: " + hSet.contains(18));
    timer.end();
    System.out.println("RobinhoodHashing Total time for adding million elements: " + timer);
    timer.start();
    int k = 0;
    for(int i=0; i<1000000; i++) {
        if(hSet.contains(i))
        	k++;
    }
    
    System.out.println("Contains Check "+k);
    timer.end();
    System.out.println("RobinhoodHashing hash set Contains Check for 1 million elements: " + timer);
    timer.start();
    for(int i=0; i<500000; i++) {
    	hSet.remove(i);
    }
    timer.end();
    System.out.println("RobinhoodHashing hash set Removal Check for 500000 elements Total: " + timer);
    timer.start();
    System.out.println("RobinhoodHashing hash set contains 18: " + hSet.contains(18));
    k = 0;
    for(int i=0; i<1000000; i++) {
    	if(hSet.add(i))
    		k++;
    }
    timer.end();
    System.out.println("RobinhoodHashing Total time for adding million elements: " + timer+" Add Check "+k);
    timer.start();
    k = 0;
    for(int i=20; i<1000000; i++) {
        if(hSet.contains(i))
        	k++;
    }
    System.out.println("Contains Check "+k);
    timer.end();  
    System.out.println("RobinhoodHashing hash set Total for Contains Check: " + timer);
    
    
    
    timer.start();
    for(int i=0; i<1000000; i++) {
    	haSet.add(i);
  }
  System.out.println("Java Hashing hash set contains 18: " + hSet.contains(18));
  timer.end();
  System.out.println("Java Hashing Total time for adding million elements: " + timer);
  timer.start();
  k = 0;
  for(int i=0; i<1000000; i++) {
      if(haSet.contains(i))
      	k++;
  }
  
  System.out.println("Contains Check "+k);
  timer.end();
  System.out.println("Java Hashing hash set Contains Check for 1 million elements: " + timer);
  timer.start();
  for(int i=0; i<500000; i++) {
  	haSet.remove(i);
  }
  timer.end();
  System.out.println("Java Hashing hash set Removal Check for 500000 elements Total: " + timer);
  timer.start();
  System.out.println("Java Hashing hash set contains 18: " + hSet.contains(18));
  k = 0;
  for(int i=0; i<1000000; i++) {
  	if(haSet.add(i))
  		k++;
  }
  timer.end();
  System.out.println("Java Hashing Total time for adding million elements: " + timer+" Add Check "+k);
  timer.start();
  k = 0;
  for(int i=20; i<1000000; i++) {
      if(haSet.contains(i))
      	k++;
  }
  System.out.println("Contains Check "+k);
  timer.end();  
  System.out.println("Java Hashing hash set Total for Contains Check: " + timer);    
  
  Integer[] arr = new Integer[1000000];
  for(int i=0;i <1000000; i++) {
      arr[i] = new Random().nextInt();
  }
  timer.start();
  System.out.println(RobinhoodHashing.distinctElements(arr));
  timer.end();
  System.out.println("Time to calculate number of distinct elements using double Hashing" + timer);
  haSet.clear();
  timer.start();
  for(Integer i: arr) {
      haSet.add(i);
  }
  System.out.println(haSet.size());
  timer.end();
  System.out.println("ime to calculate number of distinct elements using Java Hash set: " + timer);
  
  
  
  
  }

}
