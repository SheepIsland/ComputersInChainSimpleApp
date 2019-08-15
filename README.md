# ComputersInChainSimpleApp
Simple Application coining computers in chain 

Algorithm :

    1. There are local vars for counting left, right hosts.
    
    2. Initially left_count = 0, right_count = 0
    
    3. Client sends 1 to left and right hosts.
       This way hosts learn about each other.
       
    4. Each server is waiting for a message from left,right. 
    
    5. When a server gets a message from left:
    
        5.1 Server compares left_count from getting_count(count from message).
        
        5.2 If getting_count > left_count than left_count = getting_count.
            This way server understands that there are more left hosts.
            
        5.3 Server sends getting_count right down the chain.
        
    6. When a server gets a message from right it compares etc.
    
    7. Hosts exchange messages such way until their left_count, right_count becomes equal to received counts.
    
    8. Total number computers in chain will be (left_count + right_count + 1).
   
    
